package com.theNewCone.cricketScoreCard;

import android.content.res.Resources;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.theNewCone.cricketScoreCard.activity.HomeActivity;
import com.theNewCone.cricketScoreCard.enumeration.Stage;
import com.theNewCone.cricketScoreCard.utils.CommonTestUtils;
import com.theNewCone.cricketScoreCard.utils.MatchRunInfo;
import com.theNewCone.cricketScoreCard.utils.TournamentTestUtils;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class TournamentTest_TriNation {
	private final String tournamentName = "Tri Nation Cup 2019";

	@Rule
	public ActivityTestRule<HomeActivity> homeActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

	@BeforeClass

	public static void beforeClass() {
		IdlingPolicies.setMasterPolicyTimeout(5, TimeUnit.SECONDS);
		IdlingPolicies.setIdlingResourceTimeout(5, TimeUnit.SECONDS);
	}

	@Test
	public void testTriNationSeries() {
		createAusIndNZSeries();
		openTournamentScheduleScreen();


		for (int i = 0; i < 6; i++)
			triggerMatch(Stage.ROUND_ROBIN, i);

		triggerMatch(Stage.FINAL, 1);
	}

	private void createAusIndNZSeries() {
		HomeActivity activity = homeActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		TournamentTestUtils.closeLoadMatchPopup(homeActivityTestRule);
		CommonTestUtils.getDisplayedView(R.id.btnLoadTournament).perform(click());

		if (CommonTestUtils.checkViewExists(withText(tournamentName))) {
			Espresso.pressBack();
		} else {
			if (CommonTestUtils.checkViewExists(withId(R.id.rcvTournamentList))) {
				Espresso.pressBack();
			}
			CommonTestUtils.getDisplayedView(R.id.btnNewTournament).perform(click());
			CommonTestUtils.getDisplayedView(R.id.etTournamentName).perform(replaceText(tournamentName));
			CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("2"));
			CommonTestUtils.getDisplayedView(R.id.btnSelectTournamentTeams).perform(click());

			CommonTestUtils.getDisplayedView("Australia").perform(click());
			CommonTestUtils.getDisplayedView("India").perform(click());
			CommonTestUtils.getDisplayedView("New Zealand").perform(click());
			CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());

			CommonTestUtils.getDisplayedView(resources.getString(R.string.roundRobin)).perform(click());
			CommonTestUtils.getDisplayedView(R.id.etNumRounds).perform(replaceText(String.valueOf(2)));
			CommonTestUtils.getDisplayedView(resources.getString(R.string.knockOut)).perform(click());

			CommonTestUtils.getDisplayedView(resources.getString(R.string.setMatchData)).perform(click());
			CommonTestUtils.goToView(R.id.etMaxOvers).perform(replaceText("5"));
			CommonTestUtils.goToView(R.id.etMaxWickets).perform(replaceText("2"));
			CommonTestUtils.goToView(R.id.etMaxPerBowler).perform(replaceText("2"));
			CommonTestUtils.goToView(R.id.etNumPlayers).perform(replaceText("11"));

			CommonTestUtils.goToView(resources.getString(R.string.confirm)).perform(click());
		}

		CommonTestUtils.clickNavigationMenuItem(1);
	}

	private void openTournamentScheduleScreen() {
		HomeActivity activity = homeActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		CommonTestUtils.getDisplayedView(R.id.btnLoadTournament).perform(click());
		CommonTestUtils.getDisplayedView(tournamentName).perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.tournamentSchedule)).perform(click());
	}

	private void triggerMatch(Stage stage, int matchNumber) {
		HomeActivity activity = homeActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		String[] teamsPlaying = TournamentTestUtils.getPlayingTeams(homeActivityTestRule, stage, matchNumber);

		if (!teamsPlaying[0].equals("") && !teamsPlaying[1].equals("")) {
			String team1 = teamsPlaying[0].trim(), team2 = teamsPlaying[1].trim();
			String team1Full = TournamentTestUtils.getFullTeamName(homeActivityTestRule, team1);
			String team2Full = TournamentTestUtils.getFullTeamName(homeActivityTestRule, team2);

			String tossWonBy = (new Random().nextInt(2)) == 0 ? team1.toUpperCase() : team2.toUpperCase();
			int choseTo = (new Random().nextInt(2)) == 0 ? R.string.batting : R.string.bowling;

			MatchRunInfo info = new MatchRunInfo(true);
			info.updateTossDetails(tossWonBy, choseTo);

			switch (team1.toUpperCase()) {
				case "AUS":
					info.setTeam1(team1Full, team1, TournamentTestUtils.AUS_PLAYERS, TournamentTestUtils.AUS_PLAYERS[0], TournamentTestUtils.AUS_PLAYERS[5]);
					break;
				case "NZ":
					info.setTeam1(team1Full, team1, TournamentTestUtils.NZ_PLAYERS, TournamentTestUtils.NZ_PLAYERS[3], TournamentTestUtils.NZ_PLAYERS[2]);
					break;
				case "IND":
					info.setTeam1(team1Full, team1, TournamentTestUtils.IND_PLAYERS, TournamentTestUtils.IND_PLAYERS[3], TournamentTestUtils.IND_PLAYERS[4]);
					break;
			}

			switch (team2.toUpperCase()) {
				case "AUS":
					info.setTeam2(team2Full, team2, TournamentTestUtils.AUS_PLAYERS, TournamentTestUtils.AUS_PLAYERS[0], TournamentTestUtils.AUS_PLAYERS[5]);
					break;
				case "NZ":
					info.setTeam2(team2Full, team2, TournamentTestUtils.NZ_PLAYERS, TournamentTestUtils.NZ_PLAYERS[3], TournamentTestUtils.NZ_PLAYERS[2]);
					break;
				case "IND":
					info.setTeam2(team2Full, team2, TournamentTestUtils.IND_PLAYERS, TournamentTestUtils.IND_PLAYERS[3], TournamentTestUtils.IND_PLAYERS[4]);
					break;
			}

			Log.i(Constants.LOG_TAG, String.format("Match-%d, %s won the toss and chose to %s", matchNumber, tossWonBy, resources.getString(choseTo)));

			TournamentTestUtils.triggerMatch(matchNumber, info, homeActivityTestRule);
		}
	}
}
