package com.theNewCone.cricketScoreCard.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.PerformException;
import android.util.Log;
import android.widget.TextView;

import com.theNewCone.cricketScoreCard.Constants;
import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.match.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class MatchSimulator {


	private final Resources resources;
	private final Activity activity;

	public MatchSimulator(Activity activity) {
		this.resources = activity.getResources();
		this.activity = activity;
	}


	/*
	 * T20 Matches
	 * -----------
	 * CSV - 1 : India vs West Indies Nov-04, 2018 T20
	 * CSV - 2 : India Women vs Pakistan Women, World Cup T20, 2018, Group-B, Match-5
	 * CSV - 3 : South Africa vs West Indies, World Cup T20, 2007, Group-A, Match-1
	 * CSV - 4 : India vs Pakistan, World Cup T20, 2007, Final
	 * CSV - 5 : New Zealand vs Sri Lanka, World Cup T20, 2012, Group-l, Match-13
	 */

	public void simulateCSV(String templateFile, MatchRunInfo info) {
		createNewMatch(info);

		Team team1 = info.getTeam1(), team2 = info.getTeam2();
		String[] team1PlayerList = info.getTeam1Players(), team2PlayerList = info.getTeam2Players();
		if ((team1.getShortName().equals(info.getTossWonBy()) && info.getChoseTo() == R.string.bowling)
				|| (team2.getShortName().equals(info.getTossWonBy()) && info.getChoseTo()== R.string.batting)) {
			team1 = info.getTeam2();
			team2 = info.getTeam1();
			team1PlayerList = info.getTeam2Players();
			team2PlayerList = info.getTeam1Players();
		}

		startSimulation(templateFile, team1, team2, team1PlayerList, team2PlayerList);
	}

	private void createNewMatch(MatchRunInfo info) {
		Resources resources = activity.getResources();

		int maxOvers = info.getMaxOvers();
		int maxWickets = info.getMaxWickets();
		int numPlayers = info.getNumPlayers();
		int oversPerBowler = info.getOversPerBowler();
		String matchName = info.getMatchName();

		if (info.isTournament()) {
			Activity currActivity = CommonTestUtils.getCurrentActivity();
			maxOvers = Integer.parseInt(((TextView) currActivity.findViewById(R.id.tvMaxOvers)).getText().toString());
			maxWickets = Integer.parseInt(((TextView) currActivity.findViewById(R.id.tvMaxWickets)).getText().toString());
			numPlayers = Integer.parseInt(((TextView) currActivity.findViewById(R.id.tvNumPlayers)).getText().toString());
			oversPerBowler = Integer.parseInt(((TextView) currActivity.findViewById(R.id.tvMaxPerBowler)).getText().toString());
			matchName = ((TextView) currActivity.findViewById(R.id.etMatchName)).getText().toString();
		} else {
			CommonTestUtils.getDisplayedView(R.id.btnNewMatch).perform(click());
			if (maxOvers > 0) {
				CommonTestUtils.getDisplayedView(R.id.etMaxOvers).perform(replaceText(String.valueOf(maxOvers)));
			}
			if (maxWickets > 0) {
				CommonTestUtils.getDisplayedView(R.id.etMaxWickets).perform(replaceText(String.valueOf(maxWickets)));
			}
			if (numPlayers > 0) {
				CommonTestUtils.getDisplayedView(R.id.etNumPlayers).perform(replaceText(String.valueOf(numPlayers)));
			}
			if (oversPerBowler > 0) {
				CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).perform(replaceText(String.valueOf(oversPerBowler)));
			}
			if (matchName != null && !"".equals(matchName.trim())) {
				CommonTestUtils.getDisplayedView(R.id.etMatchName).perform(replaceText(matchName));
			}
		}

		info.updateMatchRunInfo(matchName, maxOvers, maxWickets, oversPerBowler, numPlayers);

		CommonTestUtils.selectTeam(info, true);
		CommonTestUtils.selectTeam(info, false);

		CommonTestUtils.goToView(resources.getString(R.string.toss)).perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.tossWonBy)).check(matches(isDisplayed()));
		CommonTestUtils.goToView(info.getTossWonBy()).perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.choose)).check(matches(isDisplayed()));
		CommonTestUtils.getDisplayedView(resources.getString(info.getChoseTo())).perform(click());
		CommonTestUtils.goToView(resources.getString(R.string.startMatch)).perform(click());
	}


	private void startSimulation(String templateFile, Team team1, Team team2,
								 String[] team1Players, String[] team2Players) {
		MatchStepRecorder stepRecorder = new MatchStepRecorder(activity.getApplicationContext());

		List<MatchStep> matchStepList = stepRecorder.recordMatchSteps(templateFile, team1, team2, team1Players, team2Players);

		simulateSteps(matchStepList);
	}

	private void simulateSteps(List<MatchStep> matchStepList) {
		int stepNum = 1;
		List<Integer> breakSteps = new ArrayList<>();
		breakSteps.add(-1);

		Collections.sort(breakSteps);

		for (MatchStep step : matchStepList) {
			Log.v(Constants.LOG_TAG, String.format("Step Number %d started", stepNum));

			if (breakSteps.contains(stepNum))
				Log.v(Constants.LOG_TAG, "Applying a break");

			switch (step.getScoreType()) {
				case BATSMAN:
					selectBatsman(step);
					break;

				case FACING:
					selectFacingBatsman(step);
					break;

				case BOWLER:
					selectBowler(step);
					break;

				case BALL:
					processBall(step);
					break;

				case NEXT_INNINGS:
					startNextInnings();
					break;

				case PLAYER_OF_MATCH:
					selectPlayerOfMatch(step);
					break;

				case COMPLETE_MATCH:
					completeMatch();
					break;

				case PENALTY:
					addPenalty(step);
					break;

				case CANCEL:
					cancelRuns(step);
					break;
			}

			Log.v(Constants.LOG_TAG, String.format("Step Number %d completed", stepNum));
//				CommonTestUtils.sleepABit(200);
			stepNum++;
		}
	}

	private void selectBatsman(MatchStep matchStep) {
		CommonTestUtils.getDisplayedView(resources.getString(R.string.selBatsman)).perform(click());
		CommonTestUtils.goToViewStarting(matchStep.getBatsman()).perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());
	}

	private void selectFacingBatsman(MatchStep matchStep) {
		CommonTestUtils.getDisplayedView(resources.getString(R.string.selFacingBatsman)).perform(click());
		CommonTestUtils.goToViewStarting(matchStep.getBatsman()).perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());
	}

	private void selectBowler(MatchStep matchStep) {
		CommonTestUtils.getDisplayedView(resources.getString(R.string.selBowler)).perform(click());
		if (matchStep.getBowler() != null)
			CommonTestUtils.goToViewStarting(matchStep.getBowler()).perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());
	}

	private void processBall(MatchStep matchStep) {
		if (matchStep.isWicket()) {
			recordWicket(matchStep);
		} else if (matchStep.isExtra()) {
			recordExtra(matchStep);
		} else {
			processRuns(matchStep);
		}
	}

	private void recordWicket(MatchStep matchStep) {
		String wicketType = null;
		switch (matchStep.getWicketType()) {
			case BOWLED:
				wicketType = resources.getString(R.string.wicket_bowled);
				break;

			case CAUGHT:
				wicketType = resources.getString(R.string.wicket_caught);
				break;

			case HIT_BALL_TWICE:
				wicketType = resources.getString(R.string.wicket_hitTwice);
				break;

			case HIT_WICKET:
				wicketType = resources.getString(R.string.wicket_hitWicket);
				break;

			case LBW:
				wicketType = resources.getString(R.string.wicket_lbw);
				break;

			case OBSTRUCTING_FIELD:
				wicketType = resources.getString(R.string.wicket_obstruct);
				break;

			case RETIRED:
				wicketType = resources.getString(R.string.wicket_retired);
				break;

			case RUN_OUT:
				wicketType = resources.getString(R.string.wicket_runOut);
				break;

			case STUMPED:
				wicketType = resources.getString(R.string.wicket_stump);
				break;

			case TIMED_OUT:
				break;
		}

		CommonTestUtils.getView(withContentDescription(R.string.scoringButtons), withText(R.string.wicket)).perform(click());
		CommonTestUtils.getDisplayedView(wicketType).perform(click());

		String extraType = null;
		String extraSubType = null;
		if (matchStep.isExtra()) {
			switch (matchStep.getExtraType()) {
				case BYE:
					extraType = resources.getString(R.string.bye);
					break;

				case LEG_BYE:
					extraType = resources.getString(R.string.legBye);
					break;

				case WIDE:
					extraType = resources.getString(R.string.wide);
					break;

				case NO_BALL:
					extraType = resources.getString(R.string.noBall);
					switch (matchStep.getExtraSubType()) {
						case NONE:
							extraSubType = resources.getString(R.string.extraNBNone);
							break;

						case BYE:
							extraSubType = resources.getString(R.string.extraNBBye);
							break;

						case LEG_BYE:
							extraSubType = resources.getString(R.string.extraNBLegBye);
							break;
					}
			}

			CommonTestUtils.getDisplayedView(resources.getString(R.string.isExtra)).perform(click());
			CommonTestUtils.getDisplayedView(extraType).perform(click());
			if (extraSubType != null)
				CommonTestUtils.getDisplayedView(extraSubType).perform(click());

			if(matchStep.getExtraRuns() > 0) {
				CommonTestUtils.getDisplayedView(R.id.sbRORuns).perform(CommonTestUtils.setProgress(matchStep.getExtraRuns()));
			}
		}
		else if(matchStep.getRuns() > 0) {
			CommonTestUtils.getDisplayedView(R.id.sbRORuns).perform(CommonTestUtils.setProgress(matchStep.getRuns()));
		}

		if (matchStep.getBatsman() != null) {
			CommonTestUtils.getDisplayedView(R.id.btnBatsmanOut).perform(click());
			CommonTestUtils.goToViewStarting(matchStep.getBatsman()).perform(click());
			CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());
		}
		if (matchStep.getPlayer() != null) {
			CommonTestUtils.getDisplayedView(R.id.btnEffectedBy).perform(click());
			CommonTestUtils.goToViewStarting(matchStep.getPlayer()).perform(click());
		}

		CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());
	}

	private void recordExtra(MatchStep matchStep) {
		String extraType = null;
		String extraSubType = null;
		if (matchStep.isExtra()) {
			switch (matchStep.getExtraType()) {
				case BYE:
					extraType = resources.getString(R.string.btnByeText);
					break;

				case LEG_BYE:
					extraType = resources.getString(R.string.btnLegByeText);
					break;

				case WIDE:
					extraType = resources.getString(R.string.btnWideText);
					break;

				case PENALTY:
					extraType = resources.getString(R.string.btnPenaltyText);
					break;

				case NO_BALL:
					extraType = resources.getString(R.string.btnNoBallText);
					switch (matchStep.getExtraSubType()) {
						case NONE:
							extraSubType = resources.getString(R.string.extraNBNone);
							break;

						case BYE:
							extraSubType = resources.getString(R.string.extraNBBye);
							break;

						case LEG_BYE:
							extraSubType = resources.getString(R.string.extraNBLegBye);
							break;
					}
			}
		}

		CommonTestUtils.getView(withContentDescription(R.string.scoringButtons), withText(extraType)).perform(click());

		if (extraSubType != null) {
			CommonTestUtils.getDisplayedView(extraSubType).perform(click());
		}
		String runs = matchStep.getExtraRuns() > 0
				? String.valueOf(matchStep.getExtraRuns())
				: String.valueOf(matchStep.getRuns());
		CommonTestUtils.getDisplayedView(runs).perform(click());

		CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());
	}

	private void processRuns(MatchStep matchStep) {
		int runsButtonId;
		String runsButtonText;
		boolean otherRuns = false;

		switch (matchStep.getRuns()) {
			case 0:
				runsButtonId = R.id.btnRuns0;
				runsButtonText = resources.getString(R.string.zero);
				break;

			case 1:
				runsButtonId = R.id.btnRuns1;
				runsButtonText = resources.getString(R.string.one);
				break;

			case 2:
				runsButtonId = R.id.btnRuns2;
				runsButtonText = resources.getString(R.string.two);
				break;

			case 3:
				runsButtonId = R.id.btnRuns3;
				runsButtonText = resources.getString(R.string.three);
				break;

			case 4:
				runsButtonId = R.id.btnRuns4;
				runsButtonText = resources.getString(R.string.four);
				break;

			case 6:
				runsButtonId = R.id.btnRuns6;
				runsButtonText = resources.getString(R.string.six);
				break;

			default:
				runsButtonId = R.id.btnMoreRuns;
				runsButtonText = resources.getString(R.string.moreRuns);
				otherRuns = true;
				break;
		}

		CommonTestUtils.getDisplayedView(runsButtonId);
		CommonTestUtils.getView(withContentDescription(R.string.scoringButtons), withText(runsButtonText)).perform(click());
		if (otherRuns) {
			CommonTestUtils.getDisplayedView(R.id.etStringInput).perform(replaceText(String.valueOf(matchStep.getRuns())));
			CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());
		}
	}

	private void startNextInnings() {
		CommonTestUtils.getDisplayedView(resources.getString(R.string.startNextInnings)).perform(click());
	}

	private void selectPlayerOfMatch(MatchStep matchStep) {
		CommonTestUtils.getView(withContentDescription(R.string.playerOfMatch), withText(matchStep.getPomTeam())).perform(click());
		CommonTestUtils.goToViewStarting(matchStep.getPom()).perform(click());
	}

	private void completeMatch() {
		CommonTestUtils.getDisplayedView(resources.getString(R.string.closeMatch)).perform(click());
		try {
			CommonTestUtils.getDisplayedView(resources.getString(R.string.yes)).perform(click());
		} catch (PerformException ex) {
			//DO NOTHING
		}
		Log.i(Constants.LOG_TAG, "Finished Match Simulation");
	}

	private void addPenalty(MatchStep matchStep) {
		Espresso.openActionBarOverflowOrOptionsMenu(activity.getApplicationContext());
		CommonTestUtils.goToView(resources.getString(R.string.runs)).perform(click());
		CommonTestUtils.goToView(resources.getString(R.string.addPenalty)).perform(click());
		CommonTestUtils.getDisplayedView(matchStep.getPenaltyAwardedTo()).perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());
	}

	private void cancelRuns(MatchStep matchStep) {
		Espresso.openActionBarOverflowOrOptionsMenu(activity.getApplicationContext());
		CommonTestUtils.goToView(resources.getString(R.string.runs)).perform(click());
		CommonTestUtils.goToView(resources.getString(R.string.cancelRuns)).perform(click());
		CommonTestUtils.getDisplayedView(R.id.sbInput).perform(CommonTestUtils.setProgress(matchStep.getRuns()));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());
	}
}
