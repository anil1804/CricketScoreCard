package com.theNewCone.cricketScoreCard;

import android.content.res.Resources;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.rule.ActivityTestRule;

import com.theNewCone.cricketScoreCard.activity.HomeActivity;
import com.theNewCone.cricketScoreCard.enumeration.Stage;
import com.theNewCone.cricketScoreCard.enumeration.TournamentFormat;
import com.theNewCone.cricketScoreCard.utils.CommonTestUtils;
import com.theNewCone.cricketScoreCard.utils.TournamentTestUtils;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class TournamentTest_KnockOut {

	@Rule
	public ActivityTestRule testRule = new ActivityTestRule<>(HomeActivity.class);

	@BeforeClass

	public static void beforeClass() {
		IdlingPolicies.setMasterPolicyTimeout(5, TimeUnit.SECONDS);
		IdlingPolicies.setIdlingResourceTimeout(5, TimeUnit.SECONDS);
	}

	@Test
	public void test4NationSeries() {
		String[] teams = {
				"Australia",
				"India",
				"New Zealand",
				"South Africa"
		};
		testKnockOutSeries(teams, "4 Nation Cup KO - Test");
	}

	@Test
	public void test8NationSeries() {
		String[] teams = {
				"Australia",
				"India",
				"New Zealand",
				"Pakistan",
				"West Indies",
				"South Africa",
				"Sri Lanka",
				"England",
		};
		testKnockOutSeries(teams, "8 Nation Cup KO - Test");
	}

	private void testKnockOutSeries(String[] teams, String tournamentName) {
		TournamentTestUtils.closeLoadMatchPopup();
		CommonTestUtils.loadDBData();

		createKnockOutSeries(teams, tournamentName);

		int numMatches = teams.length;
		Stage stage = Stage.NONE;
		int groupNumber = 0;
		do {
			openTournamentScheduleScreen(tournamentName);

			numMatches = numMatches / 2;
			groupNumber++;
			stage = getStage(numMatches, stage);

			for (int matchNum = 1; matchNum <= numMatches; matchNum++)
				triggerMatch(stage, groupNumber, matchNum);


			TournamentTestUtils.goHome();
		} while (stage != Stage.FINAL);
	}

	private Stage getStage(int numMatches, Stage prevStage) {
		Stage stage = Stage.NONE;
		if (prevStage == Stage.NONE)
			stage = Stage.ROUND_1;
		else if (numMatches == 1)
			stage = Stage.FINAL;
		else if (numMatches == 2)
			stage = Stage.SEMI_FINAL;
		else if (numMatches == 4)
			stage = Stage.QUARTER_FINAL;
		else if (numMatches > 4) {
			switch (prevStage) {
				case ROUND_1:
					stage = Stage.ROUND_2;
					break;

				case ROUND_2:
					stage = Stage.ROUND_3;
					break;
			}
		}

		return stage;
	}

	private void createKnockOutSeries(String[] teams, String tournamentName) {
		Resources resources = testRule.getActivity().getResources();

		TournamentTestUtils.deleteTournament(tournamentName, testRule.getActivity());

		CommonTestUtils.getDisplayedView(R.id.btnLoadTournament).perform(click());

		if (CommonTestUtils.checkViewExists(withText(tournamentName))) {
			Espresso.pressBack();
		} else {
			if (CommonTestUtils.checkViewExists(withId(R.id.rcvTournamentList))) {
				Espresso.pressBack();
			}
			CommonTestUtils.getDisplayedView(R.id.btnNewTournament).perform(click());
			CommonTestUtils.getDisplayedView(R.id.etTournamentName).perform(replaceText(tournamentName));
			CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText(String.valueOf(teams.length)));
			CommonTestUtils.getDisplayedView(R.id.btnSelectTournamentTeams).perform(click());

			for (String team : teams)
				CommonTestUtils.goToView(team).perform(click());
			CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());

			CommonTestUtils.getDisplayedView(resources.getString(R.string.knockOut)).perform(click());

			CommonTestUtils.getDisplayedView(resources.getString(R.string.setMatchData)).perform(click());
			CommonTestUtils.goToView(R.id.etMaxOvers).perform(replaceText("5"));
			CommonTestUtils.goToView(R.id.etMaxWickets).perform(replaceText("2"));
			CommonTestUtils.goToView(R.id.etMaxPerBowler).perform(replaceText("2"));
			CommonTestUtils.goToView(R.id.etNumPlayers).perform(replaceText("11"));

			CommonTestUtils.goToView(resources.getString(R.string.confirm)).perform(click());

			CommonTestUtils.checkViewExists(withText(resources.getString(R.string.confirm)));
			CommonTestUtils.goToView(R.id.btnTournamentScheduleConfirm).perform(click());
		}

		CommonTestUtils.clickNavigationMenuItem(1);
	}

	private void openTournamentScheduleScreen(String tournamentName) {
		Resources resources = testRule.getActivity().getResources();

		CommonTestUtils.getDisplayedView(R.id.btnLoadTournament).perform(click());
		CommonTestUtils.getDisplayedView(tournamentName).perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.tournamentSchedule)).perform(click());
	}

	private void triggerMatch(Stage stage, int groupIndex, int matchNumber) {
		Resources resources = testRule.getActivity().getResources();
		CommonTestUtils.getDisplayedView(resources.getString(R.string.tournamentSchedule)).perform(click());

		groupIndex--;
		TournamentTestUtils.triggerMatch(groupIndex, matchNumber, TournamentFormat.KNOCK_OUT, stage);
	}
}