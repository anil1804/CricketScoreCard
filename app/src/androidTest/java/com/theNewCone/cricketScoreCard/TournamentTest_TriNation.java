package com.theNewCone.cricketScoreCard;

import android.content.res.Resources;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.rule.ActivityTestRule;

import com.theNewCone.cricketScoreCard.activity.HomeActivity;
import com.theNewCone.cricketScoreCard.enumeration.Stage;
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

public class TournamentTest_TriNation {
	private final String tournamentName = "Tri Nation Cup 2019";

	@Rule
	public ActivityTestRule testRule = new ActivityTestRule<>(HomeActivity.class);

	@BeforeClass

	public static void beforeClass() {
		IdlingPolicies.setMasterPolicyTimeout(5, TimeUnit.SECONDS);
		IdlingPolicies.setIdlingResourceTimeout(5, TimeUnit.SECONDS);
	}

	@Test
	public void testTriNationSeries() {
		CommonTestUtils.loadDBData();

		int numRounds = 1;
		createAusIndNZSeries(numRounds);
		openTournamentScheduleScreen();

		for (int i = 1; i <= (3 * numRounds); i++)
			triggerMatch(Stage.ROUND_ROBIN, i);

		triggerMatch(Stage.FINAL, 1);
	}

	private void createAusIndNZSeries(int numRounds) {
		Resources resources = testRule.getActivity().getResources();

		TournamentTestUtils.closeLoadMatchPopup();
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
			CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("3"));
			CommonTestUtils.getDisplayedView(R.id.btnSelectTournamentTeams).perform(click());

			CommonTestUtils.getDisplayedView("Australia").perform(click());
			CommonTestUtils.getDisplayedView("India").perform(click());
			CommonTestUtils.getDisplayedView("New Zealand").perform(click());
			CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());

			CommonTestUtils.getDisplayedView(resources.getString(R.string.roundRobin)).perform(click());
			CommonTestUtils.getDisplayedView(R.id.etNumRounds).perform(replaceText(String.valueOf(numRounds)));
			CommonTestUtils.getDisplayedView(R.id.rbTSKnockOut).perform(click());

			CommonTestUtils.getDisplayedView(resources.getString(R.string.setMatchData)).perform(click());
			CommonTestUtils.goToView(R.id.etMaxOvers).perform(replaceText("5"));
			CommonTestUtils.goToView(R.id.etMaxWickets).perform(replaceText("2"));
			CommonTestUtils.goToView(R.id.etMaxPerBowler).perform(replaceText("2"));
			CommonTestUtils.goToView(R.id.etNumPlayers).perform(replaceText("11"));

			CommonTestUtils.goToView(resources.getString(R.string.confirm)).perform(click());

			CommonTestUtils.checkViewExists(withText(resources.getString(R.string.confirm)));
			CommonTestUtils.getDisplayedView(R.id.btnTournamentScheduleConfirm).perform(click());
		}

		CommonTestUtils.clickNavigationMenuItem(1);
	}

	private void openTournamentScheduleScreen() {
		Resources resources = testRule.getActivity().getResources();

		CommonTestUtils.getDisplayedView(R.id.btnLoadTournament).perform(click());
		CommonTestUtils.getDisplayedView(tournamentName).perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.tournamentSchedule)).perform(click());
	}

	private void triggerMatch(Stage stage, int matchNumber) {
		Resources resources = testRule.getActivity().getResources();
		CommonTestUtils.getDisplayedView(resources.getString(R.string.tournamentSchedule)).perform(click());

		int groupIndex = stage == Stage.FINAL ? 1 : 0;
		String currentRoundText = stage == Stage.FINAL ? Stage.FINAL.enumString() : Stage.ROUND_ROBIN.enumString();
		TournamentTestUtils.triggerMatch(groupIndex, matchNumber, null, currentRoundText);
	}
}