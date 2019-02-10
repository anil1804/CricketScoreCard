package com.theNewCone.cricketScoreCard;

import android.content.res.Resources;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.theNewCone.cricketScoreCard.activity.HomeActivity;
import com.theNewCone.cricketScoreCard.utils.CommonTestUtils;
import com.theNewCone.cricketScoreCard.utils.MatchRunInfo;
import com.theNewCone.cricketScoreCard.utils.MatchSimulator;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class TournamentTest {
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
		createAusVsIndBilateralSeries();
		testAusVsIndBilateralSeriesMatch1();
		testAusVsIndBilateralSeriesMatch2();
		testAusVsIndBilateralSeriesMatch3();
	}

	private void createAusVsIndBilateralSeries() {
		HomeActivity activity = homeActivityTestRule.getActivity();
		Resources resources = activity.getResources();

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
			CommonTestUtils.goToView(R.id.etMaxOvers).perform(replaceText("20"));
			CommonTestUtils.goToView(R.id.etMaxWickets).perform(replaceText("10"));
			CommonTestUtils.goToView(R.id.etNumPlayers).perform(replaceText("11"));

			CommonTestUtils.goToView(resources.getString(R.string.confirm)).perform(click());
		}

		CommonTestUtils.clickNavigationMenuItem(1);
	}

	private void testAusVsIndBilateralSeriesMatch1() {
		openTournamentScheduleScreen();
		triggerAusVsIndMatch(1);
		CommonTestUtils.clickNavigationMenuItem(1);
	}

	private void testAusVsIndBilateralSeriesMatch2() {
		openTournamentScheduleScreen();
		triggerAusVsIndMatch(2);
		CommonTestUtils.clickNavigationMenuItem(1);
	}

	private void testAusVsIndBilateralSeriesMatch3() {
		openTournamentScheduleScreen();
		triggerAusVsIndMatch(3);
		CommonTestUtils.clickNavigationMenuItem(1);
	}

	private void openTournamentScheduleScreen() {
		HomeActivity activity = homeActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		CommonTestUtils.getDisplayedView(R.id.btnLoadTournament).perform(click());
		CommonTestUtils.getDisplayedView(tournamentName).perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.tournamentSchedule)).perform(click());
	}

	private void triggerAusVsIndMatch(int matchNumber) {
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

		triggerMatch(matchNumber, info);
	}

	private void triggerMatch(int matchNumber, MatchRunInfo info) {
		HomeActivity activity = homeActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		String matchNumberText = resources.getString(R.string.matchPrefix) + String.valueOf(matchNumber);
		if(CommonTestUtils.checkViewExists(allOf(
				withText("Start"),
				withParent(allOf(
						withId(R.id.clTHScheduleItem),
						hasDescendant(withText(matchNumberText))))))) {
			CommonTestUtils
					.getChild(allOf(withId(R.id.clTHScheduleItem), hasDescendant(withText(matchNumberText))), withText("Start"))
					.perform(click());

			MatchSimulator simulator = new MatchSimulator(activity);
			String csvFile = "csv/templates/" +  matchNumber%5 + ".csv";
			Log.i(Constants.LOG_TAG, "CSV File being used to simulate match is " + csvFile);

			simulator.simulateCSV(csvFile, info);
		} else {
			Log.i(Constants.LOG_TAG, String.format("Match-%d is already running/complete", matchNumber));
		}
	}
}
