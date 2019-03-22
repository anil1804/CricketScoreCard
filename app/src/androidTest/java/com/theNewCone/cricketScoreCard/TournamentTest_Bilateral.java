package com.theNewCone.cricketScoreCard;

import android.content.res.Resources;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.theNewCone.cricketScoreCard.activity.HomeActivity;
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

public class TournamentTest_Bilateral {
	private final String tournamentName = "Aus V Ind - India 2019";

	@Rule
	public ActivityTestRule<HomeActivity> homeActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

	@BeforeClass

	public static void beforeClass() {
		IdlingPolicies.setMasterPolicyTimeout(5, TimeUnit.SECONDS);
		IdlingPolicies.setIdlingResourceTimeout(5, TimeUnit.SECONDS);
	}

	@Test
	public void testBilateralSeries() {
		createAusVsIndSeries();
		openTournamentScheduleScreen();

		triggerMatch(1);
		triggerMatch(2);
		triggerMatch(3);
	}

	private void createAusVsIndSeries() {
		HomeActivity activity = homeActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		TournamentTestUtils.closeLoadMatchPopup(homeActivityTestRule);
		CommonTestUtils.getDisplayedView(R.id.btnLoadTournament).perform(click());

		if(CommonTestUtils.checkViewExists(withText(tournamentName))) {
			Espresso.pressBack();
		} else {
			if(CommonTestUtils.checkViewExists(withId(R.id.rcvTournamentList))) {
				Espresso.pressBack();
			}
			CommonTestUtils.getDisplayedView(R.id.btnNewTournament).perform(click());
			CommonTestUtils.getDisplayedView(R.id.etTournamentName).perform(replaceText(tournamentName));
			CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("2"));
			CommonTestUtils.getDisplayedView(R.id.btnSelectTournamentTeams).perform(click());

			CommonTestUtils.getDisplayedView("Australia").perform(click());
			CommonTestUtils.getDisplayedView("India").perform(click());
			CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());

			CommonTestUtils.getDisplayedView(resources.getString(R.string.bilateral)).perform(click());
			CommonTestUtils.getDisplayedView(R.id.etNumRounds).perform(replaceText(String.valueOf(3)));

			CommonTestUtils.getDisplayedView(resources.getString(R.string.setMatchData)).perform(click());

			/* 20 Overs*/
			/*CommonTestUtils.goToView(R.id.etMaxOvers).perform(replaceText("20"));
			CommonTestUtils.goToView(R.id.etMaxWickets).perform(replaceText("10"));
			CommonTestUtils.goToView(R.id.etNumPlayers).perform(replaceText("11"));*/

			/* 5 Overs*/
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

	private void triggerMatch(int matchNumber) {
		HomeActivity activity = homeActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		final String[] IND_PLAYERS = {
				"Rohit Sharma",
				"Shikhar Dhawan",
				"Lokesh Rahul",
				"AVirat Kohli",
				"MS Dhoni",
				"Dinesh Karthik",
				"Krunal Pandya",
				"Ravindra Jadeja",
				"Kuldeep Yadav",
				"Jasprit Bumrah",
				"Bhuvneshwar Kumar"
		};

		final String[] AUS_PLAYERS = {
				"Aaron Finch",
				"D Arcy Short",
				"Chris Lynn",
				"Glenn Maxwell",
				"Ben McDermott",
				"Alex Carey",
				"Ashton Agar",
				"Nathan Coulter-Nile",
				"Adam Zampa",
				"Andrew Tye",
				"Billy Stanlake"
		};


		String tossWonBy = (new Random().nextInt(2)) == 0 ? "AUS" : "IND";
		int choseTo = (new Random().nextInt(2)) == 0 ? R.string.batting : R.string.bowling;

		MatchRunInfo info = new MatchRunInfo(true);
		info.setTeam1("Australia", "AUS", AUS_PLAYERS, AUS_PLAYERS[0], AUS_PLAYERS[5]);
		info.setTeam2("India", "IND", IND_PLAYERS, IND_PLAYERS[3], IND_PLAYERS[4]);
		info.updateTossDetails(tossWonBy, choseTo);

		Log.i(Constants.LOG_TAG, String.format("Match-%d, %s won the toss and chose to %s", matchNumber, tossWonBy, resources.getString(choseTo)));

		TournamentTestUtils.triggerMatch(matchNumber, info, homeActivityTestRule);
	}
}
