package com.theNewCone.cricketScoreCard;

import android.content.Context;
import android.content.res.Resources;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.rule.ActivityTestRule;

import com.theNewCone.cricketScoreCard.activity.HomeActivity;
import com.theNewCone.cricketScoreCard.enumeration.TournamentFormat;
import com.theNewCone.cricketScoreCard.enumeration.TournamentStageType;
import com.theNewCone.cricketScoreCard.utils.CommonTestUtils;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.GONE;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

public class NewTournamentTest {

	@Rule
	public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

	@BeforeClass
	public static void beforeClass() {
		IdlingPolicies.setMasterPolicyTimeout(5, TimeUnit.SECONDS);
		IdlingPolicies.setIdlingResourceTimeout(5, TimeUnit.SECONDS);
	}

/*
	@Test
	public void notEnoughTeams() {
		HomeActivity activity = mActivityTestRule.getActivity();
		Context context = activity.getApplicationContext();
		Resources resources = activity.getResources();

		CommonTestUtils.clearTeams(context);

		CommonTestUtils.getDisplayedView(R.id.btnNewTournament).perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.notEnoughTeams))
				.check(matches(isDisplayed()));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.tournamentNotEnoughTeamsMessage))
				.check(matches(isDisplayed()));
	}
*/

	@Test
	public void showTeamCountChangedMessage() {
		HomeActivity activity = mActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		String tournamentName = "Aus V Ind - India Visit 2019";
		CommonTestUtils.getDisplayedView(R.id.btnNewTournament).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentName).perform(replaceText(tournamentName));
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("2"));
		CommonTestUtils.getDisplayedView(R.id.btnSelectTournamentTeams).perform(click());

		CommonTestUtils.getDisplayedView("Australia").perform(click());
		CommonTestUtils.getDisplayedView("India").perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("3"));

		CommonTestUtils.checkIfToastShown(R.string.teamCountChangedReselect);
	}

	@Test
	public void showValidTournamentFormats() {
		HomeActivity activity = mActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		CommonTestUtils.getDisplayedView(R.id.btnNewTournament).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("2"));
		CommonTestUtils.getDisplayedView(R.id.btnSelectTournamentTeams).perform(click());

		CommonTestUtils.getDisplayedView("Australia").perform(click());
		CommonTestUtils.getDisplayedView("India").perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());

		checkPossibleTournamentFormat(false, false, false, true);

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("3"));
		checkPossibleTournamentFormat(true, false, false, false);

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("4"));
		checkPossibleTournamentFormat(true, false, true, false);

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("5"));
		checkPossibleTournamentFormat(true, false, false, false);

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("6"));
		checkPossibleTournamentFormat(true, true, false, false);

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("7"));
		checkPossibleTournamentFormat(true, false, false, false);

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("8"));
		checkPossibleTournamentFormat(true, true, true, false);

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("9"));
		checkPossibleTournamentFormat(true, true, false, false);

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("10"));
		checkPossibleTournamentFormat(true, true, false, false);

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("11"));
		checkPossibleTournamentFormat(true, false, false, false);

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("12"));
		checkPossibleTournamentFormat(true, true, false, false);

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("13"));
		checkPossibleTournamentFormat(true, false, false, false);

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("14"));
		checkPossibleTournamentFormat(true, true, false, false);

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("15"));
		checkPossibleTournamentFormat(true, true, false, false);

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("16"));
		checkPossibleTournamentFormat(true, true, true, false);

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("17"));
		checkPossibleTournamentFormat(true, false, false, false);

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("18"));
		checkPossibleTournamentFormat(true, true, false, false);

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("19"));
		checkPossibleTournamentFormat(true, false, false, false);

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("20"));
		checkPossibleTournamentFormat(true, true, false, false);

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("21"));
		checkPossibleTournamentFormat(true, true, false, false);
	}

	@Test
	public void createBilateralSeries() {
		HomeActivity activity = mActivityTestRule.getActivity();
		Context context = activity.getApplicationContext();
		Resources resources = activity.getResources();

		String tournamentName = "Aus V Ind - India 2019";
		CommonTestUtils.getDisplayedView(R.id.btnNewTournament).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentName).perform(replaceText(tournamentName));
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("2"));
		CommonTestUtils.getDisplayedView(R.id.btnSelectTournamentTeams).perform(click());

		CommonTestUtils.getDisplayedView("Australia").perform(click());
		CommonTestUtils.getDisplayedView("India").perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());

		String teams = "Australia,\tIndia";
		CommonTestUtils.getDisplayedView(R.id.tvSelectedTeams).check(matches(withText(teams)));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.tournamentFormat)).check(matches(isDisplayed()));

		CommonTestUtils.getDisplayedView(resources.getString(R.string.bilateral)).perform(click());
		CommonTestUtils.getView(resources.getString(R.string.tournamentStages)).check(matches(withEffectiveVisibility(GONE)));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.numMatches)).check(matches(isDisplayed()));

		checkPossibleTournamentFormat(false, false, false, true);

		CommonTestUtils.getDisplayedView(resources.getString(R.string.setMatchData)).perform(click());
		CommonTestUtils.goToView(R.id.etMaxOvers).perform(replaceText("5"));
		CommonTestUtils.goToView(R.id.etMaxWickets).perform(replaceText("2"));
		CommonTestUtils.goToView(R.id.etNumPlayers).perform(replaceText("11"));

		CommonTestUtils.goToView(resources.getString(R.string.confirm)).perform(click());

		assert (CommonTestUtils.checkViewExists(withText(resources.getString(R.string.tournamentHome))));
		assert (CommonTestUtils.checkViewExists(withText(resources.getString(R.string.tournamentSchedule))));
		assert (CommonTestUtils.checkViewExists(withText(resources.getString(R.string.tournamentStats))));
		assert !(CommonTestUtils.checkViewExists(withText(resources.getString(R.string.tournamentPoints))));

		teams = teams.replaceAll("\t", "\n");
		CommonTestUtils.getDisplayedView(R.id.tvTHName).check(matches(withText(tournamentName)));
		CommonTestUtils.getDisplayedView(R.id.tvTHFormat).check(matches(withText(TournamentFormat.BILATERAL.stringValue())));
		CommonTestUtils.getDisplayedView(R.id.tvTHStageType).check(matches(withText(TournamentStageType.NONE.stringValue())));
		CommonTestUtils.getDisplayedView(R.id.tvTHTeams).check(matches(withText(teams)));

		CommonTestUtils.deleteTournament(context, tournamentName);
	}

	@Test
	public void showValidRoundRobinStages() {
		HomeActivity activity = mActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		CommonTestUtils.getDisplayedView(R.id.btnNewTournament).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("2"));
		CommonTestUtils.getDisplayedView(R.id.btnSelectTournamentTeams).perform(click());

		CommonTestUtils.getDisplayedView("Australia").perform(click());
		CommonTestUtils.getDisplayedView("India").perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("3"));
		checkPossibleTournamentFormat(true, false, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.roundRobin, mActivityTestRule.getActivity()).perform(click());
		checkPossibleTournamentStage(false, false, true, false, true);

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("4"));
		checkPossibleTournamentFormat(true, false, true, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.roundRobin, mActivityTestRule.getActivity()).perform(click());
		checkPossibleTournamentStage(false, false, true, false, true);

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("5"));
		checkPossibleTournamentFormat(true, false, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.roundRobin, mActivityTestRule.getActivity()).perform(click());
		checkPossibleTournamentStage(true, false, true, true, true);

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("6"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.roundRobin, mActivityTestRule.getActivity()).perform(click());
		checkPossibleTournamentStage(true, false, true, true, true);

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("7"));
		checkPossibleTournamentFormat(true, false, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.roundRobin, mActivityTestRule.getActivity()).perform(click());
		checkPossibleTournamentStage(true, true, true, true, true);

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("8"));
		checkPossibleTournamentFormat(true, true, true, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.roundRobin, mActivityTestRule.getActivity()).perform(click());
		checkPossibleTournamentStage(true, true, true, true, true);

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("9"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.roundRobin, mActivityTestRule.getActivity()).perform(click());
		checkPossibleTournamentStage(true, true, true, true, true);

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("10"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.roundRobin, mActivityTestRule.getActivity()).perform(click());
		checkPossibleTournamentStage(true, true, true, true, true);

		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("11"));
		checkPossibleTournamentFormat(true, false, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.roundRobin, mActivityTestRule.getActivity()).perform(click());
		checkPossibleTournamentStage(true, true, true, true, true);
	}

	@Test
	public void validateRoundRobinInfo() {
		HomeActivity activity = mActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		CommonTestUtils.getDisplayedView(R.id.btnNewTournament).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("3"));
		CommonTestUtils.getDisplayedView(R.id.btnSelectTournamentTeams).perform(click());

		CommonTestUtils.getDisplayedView("Australia").perform(click());
		CommonTestUtils.getDisplayedView("India").perform(click());
		CommonTestUtils.getDisplayedView("Pakistan").perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());

		checkPossibleTournamentFormat(true, false, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.roundRobin, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etNumRounds).perform(replaceText("2"));

		CommonTestUtils.getDisplayedView(R.id.etTournamentName).perform(replaceText("Round Robin Testing"));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.setMatchData)).perform(click());

		assert CommonTestUtils.checkViewExists(withText(containsString(resources.getString(R.string.NT_selectType))));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());

		CommonTestUtils.getDisplayedView(R.id.etNumRounds).perform(replaceText("4"));
		assert CommonTestUtils.checkViewExists(withText(containsString(resources.getString(R.string.NT_limitRoundsForRR))));
	}

	@Test
	public void createRoundRobinTournament() {
		HomeActivity activity = mActivityTestRule.getActivity();
		Context context = activity.getApplicationContext();
		Resources resources = activity.getResources();

		String tournamentName = "Round Robin Testing";
		CommonTestUtils.getDisplayedView(R.id.btnNewTournament).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentName).perform(replaceText(tournamentName));
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("3"));
		CommonTestUtils.getDisplayedView(R.id.btnSelectTournamentTeams).perform(click());

		CommonTestUtils.getDisplayedView("Australia").perform(click());
		CommonTestUtils.getDisplayedView("India").perform(click());
		CommonTestUtils.getDisplayedView("Pakistan").perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());

		String teams = "Australia,\tIndia,\tPakistan";
		CommonTestUtils.getDisplayedView(R.id.tvSelectedTeams).check(matches(withText(teams)));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.tournamentFormat)).check(matches(isDisplayed()));

		CommonTestUtils.getView(R.string.tournamentFormat, R.string.roundRobin, activity).perform(click());
		CommonTestUtils.getView(resources.getString(R.string.tournamentStages)).check(matches(withEffectiveVisibility(VISIBLE)));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.numRounds)).check(matches(isDisplayed()));

		checkPossibleTournamentFormat(true, false, false, false);

		CommonTestUtils.getView(R.string.tournamentStages, R.string.none, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etNumRounds).perform(replaceText("2"));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.setMatchData)).perform(click());

		CommonTestUtils.goToView(R.id.etMaxOvers).perform(replaceText("5"));
		CommonTestUtils.goToView(R.id.etMaxWickets).perform(replaceText("2"));
		CommonTestUtils.goToView(R.id.etNumPlayers).perform(replaceText("11"));
		CommonTestUtils.goToView(resources.getString(R.string.confirm)).perform(click());

		CommonTestUtils.getDisplayedView(resources.getString(R.string.reOrderSchedule)).check(matches(isDisplayed()));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.confirm)).check(matches(isDisplayed()));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.confirm)).perform(click());

		assert (CommonTestUtils.checkViewExists(withText(resources.getString(R.string.tournamentHome))));
		assert (CommonTestUtils.checkViewExists(withText(resources.getString(R.string.tournamentSchedule))));
		assert (CommonTestUtils.checkViewExists(withText(resources.getString(R.string.tournamentStats))));
		assert (CommonTestUtils.checkViewExists(withText(resources.getString(R.string.tournamentPoints))));

		teams = teams.replaceAll("\t", "\n");
		CommonTestUtils.getDisplayedView(R.id.tvTHName).check(matches(withText(tournamentName)));
		CommonTestUtils.getDisplayedView(R.id.tvTHFormat).check(matches(withText(TournamentFormat.ROUND_ROBIN.stringValue())));
		CommonTestUtils.getDisplayedView(R.id.tvTHStageType).check(matches(withText(TournamentStageType.NONE.stringValue())));
		CommonTestUtils.getDisplayedView(R.id.tvTHTeams).check(matches(withText(teams)));

		CommonTestUtils.deleteTournament(context, tournamentName);
	}

	@Test
	public void showValidGroupStages() {
		HomeActivity activity = mActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		CommonTestUtils.getDisplayedView(R.id.btnNewTournament).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("2"));
		CommonTestUtils.getDisplayedView(R.id.btnSelectTournamentTeams).perform(click());

		CommonTestUtils.getDisplayedView("Australia").perform(click());
		CommonTestUtils.getDisplayedView("India").perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());

		//Teams - 6, Groups - 2
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("6"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("2"));
		checkPossibleTournamentStage(true, false, true, true, false);

		//Teams - 6, Groups - 3
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("6"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("3"));
		checkPossibleTournamentStage(false, false, false, false, false);

		//Teams - 6, Groups - 4
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("6"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("4"));
		checkPossibleTournamentStage(false, false, false, false, false);

		//Teams - 8, Groups - 2
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("8"));
		checkPossibleTournamentFormat(true, true, true, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("2"));
		checkPossibleTournamentStage(true, true, true, true, false);

		//Teams - 8, Groups - 3
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("8"));
		checkPossibleTournamentFormat(true, true, true, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("3"));
		checkPossibleTournamentStage(false, false, false, false, false);

		//Teams - 8, Groups - 4
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("8"));
		checkPossibleTournamentFormat(true, true, true, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("4"));
		checkPossibleTournamentStage(false, false, false, false, false);

		//Teams - 8, Groups - 8
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("8"));
		checkPossibleTournamentFormat(true, true, true, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("8"));
		checkPossibleTournamentStage(false, false, false, false, false);

		//Teams - 9, Groups - 2
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("9"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("2"));
		checkPossibleTournamentStage(false, false, false, false, false);

		//Teams - 9, Groups - 3
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("9"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("3"));
		checkPossibleTournamentStage(false, true, false, false, false);

		//Teams - 9, Groups - 5
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("9"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("5"));
		checkPossibleTournamentStage(false, false, false, false, false);

		//Teams - 10, Groups - 2
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("10"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("2"));
		checkPossibleTournamentStage(true, true, true, true, false);

		//Teams - 10, Groups - 3
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("10"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("3"));
		checkPossibleTournamentStage(false, false, false, false, false);

		//Teams - 10, Groups - 5
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("10"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("5"));
		checkPossibleTournamentStage(false, false, false, false, false);

		//Teams - 12, Groups - 2
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("12"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("2"));
		checkPossibleTournamentStage(true, true, true, true, false);

		//Teams - 12, Groups - 3
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("12"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("3"));
		checkPossibleTournamentStage(false, true, false, false, false);

		//Teams - 12, Groups - 4
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("12"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("4"));
		checkPossibleTournamentStage(true, false, true, true, false);

		//Teams - 12, Groups - 6
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("12"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("6"));
		checkPossibleTournamentStage(false, false, false, false, false);

		//Teams - 14, Groups - 2
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("14"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("2"));
		checkPossibleTournamentStage(true, true, true, true, false);

		//Teams - 14, Groups - 3
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("14"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("3"));
		checkPossibleTournamentStage(false, false, false, false, false);

		//Teams - 14, Groups - 7
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("14"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("7"));
		checkPossibleTournamentStage(false, false, false, false, false);

		//Teams - 15, Groups - 3
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("15"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("3"));
		checkPossibleTournamentStage(false, true, false, false, false);

		//Teams - 15, Groups - 5
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("15"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("5"));
		checkPossibleTournamentStage(false, false, false, false, false);

		//Teams - 15, Groups - 15
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("15"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("15"));
		checkPossibleTournamentStage(false, false, false, false, false);

		//Teams - 16, Groups - 2
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("16"));
		checkPossibleTournamentFormat(true, true, true, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("2"));
		checkPossibleTournamentStage(true, true, true, true, false);

		//Teams - 16, Groups - 3
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("16"));
		checkPossibleTournamentFormat(true, true, true, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("3"));
		checkPossibleTournamentStage(false, false, false, false, false);

		//Teams - 16, Groups - 4
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("16"));
		checkPossibleTournamentFormat(true, true, true, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("4"));
		checkPossibleTournamentStage(true, false, true, true, false);

		//Teams - 16, Groups - 8
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("16"));
		checkPossibleTournamentFormat(true, true, true, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("8"));
		checkPossibleTournamentStage(false, false, false, false, false);

		//Teams - 18, Groups - 2
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("18"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("2"));
		checkPossibleTournamentStage(true, true, true, true, false);

		//Teams - 18, Groups - 3
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("18"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("3"));
		checkPossibleTournamentStage(false, true, false, false, false);

		//Teams - 18, Groups - 6
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("18"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("6"));
		checkPossibleTournamentStage(false, true, false, false, false);

		//Teams - 20 Groups - 2
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("20"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("2"));
		checkPossibleTournamentStage(true, true, true, true, false);

		//Teams - 20 Groups - 4
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("20"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("4"));
		checkPossibleTournamentStage(true, false, true, true, false);

		//Teams - 20 Groups - 5
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("20"));
		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("5"));
		checkPossibleTournamentStage(false, false, false, false, false);
	}

	@Test
	public void validateGroupsInfo() {
		HomeActivity activity = mActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		CommonTestUtils.getDisplayedView(R.id.btnNewTournament).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("6"));
		CommonTestUtils.getDisplayedView(R.id.btnSelectTournamentTeams).perform(click());

		CommonTestUtils.getDisplayedView("Australia").perform(click());
		CommonTestUtils.getDisplayedView("India").perform(click());
		CommonTestUtils.getDisplayedView("Pakistan").perform(click());
		CommonTestUtils.getDisplayedView("West Indies").perform(click());
		CommonTestUtils.getDisplayedView("Team-X").perform(click());
		CommonTestUtils.getDisplayedView("Team-Y").perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());

		checkPossibleTournamentFormat(true, true, false, false);
		CommonTestUtils.getDisplayedView(R.id.etTournamentName).perform(replaceText("Groups Testing"));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.setMatchData)).perform(click());

		assert CommonTestUtils.checkViewExists(withText(containsString(resources.getString(R.string.NT_selectType))));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());

		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentGroups).perform(replaceText("2"));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.setMatchData)).perform(click());

		assert CommonTestUtils.checkViewExists(withText(containsString(resources.getString(R.string.NT_selectStage))));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());

		CommonTestUtils.getView(R.string.tournamentStages, R.string.superFourStage, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etNumRounds).perform(replaceText("3"));
		assert CommonTestUtils.checkViewExists(withText(containsString(resources.getString(R.string.NT_limitRoundsForGroup))));
	}

	@Test
	public void createGroupFormatTournament() {
		HomeActivity activity = mActivityTestRule.getActivity();
		Context context = activity.getApplicationContext();
		Resources resources = activity.getResources();

		String tournamentName = "Group Format Testing";
		CommonTestUtils.getDisplayedView(R.id.btnNewTournament).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentName).perform(replaceText(tournamentName));
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("6"));
		CommonTestUtils.getDisplayedView(R.id.btnSelectTournamentTeams).perform(click());

		for (int i = 0; i < 6; i++)
			CommonTestUtils.goToChildAtPosition(withId(R.id.rcvTeamList), i).perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());

		String teams = "Australia,\tIndia,\tPakistan,\tTeam-X,\tTeam-Y,\tWest Indies";
		CommonTestUtils.getDisplayedView(R.id.tvSelectedTeams).check(matches(withText(teams)));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.tournamentFormat)).check(matches(isDisplayed()));

		CommonTestUtils.getView(R.string.tournamentFormat, R.string.groups, activity).perform(click());
		CommonTestUtils.getView(resources.getString(R.string.tournamentStages)).check(matches(withEffectiveVisibility(VISIBLE)));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.numRounds)).check(matches(isDisplayed()));

		checkPossibleTournamentFormat(true, true, false, false);

		CommonTestUtils.getView(R.string.tournamentStages, R.string.superFourStage, activity).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etNumRounds).perform(replaceText("2"));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.setMatchData)).perform(click());

		CommonTestUtils.goToView(R.id.etMaxOvers).perform(replaceText("5"));
		CommonTestUtils.goToView(R.id.etMaxWickets).perform(replaceText("2"));
		CommonTestUtils.goToView(R.id.etNumPlayers).perform(replaceText("11"));
		CommonTestUtils.goToView(resources.getString(R.string.confirm)).perform(click());

		CommonTestUtils.getDisplayedView(resources.getString(R.string.reOrderGroups)).check(matches(isDisplayed()));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).check(matches(isDisplayed()));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());

		CommonTestUtils.getDisplayedView(resources.getString(R.string.reOrderSchedule)).check(matches(isDisplayed()));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.next)).check(matches(isDisplayed()));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.next)).perform(click());

		CommonTestUtils.getDisplayedView(resources.getString(R.string.previous)).check(matches(isDisplayed()));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.reOrderSchedule)).check(matches(isDisplayed()));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.confirm)).check(matches(isDisplayed()));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.confirm)).perform(click());

		assert (CommonTestUtils.checkViewExists(withText(resources.getString(R.string.tournamentHome))));
		assert (CommonTestUtils.checkViewExists(withText(resources.getString(R.string.tournamentSchedule))));
		assert (CommonTestUtils.checkViewExists(withText(resources.getString(R.string.tournamentStats))));
		assert (CommonTestUtils.checkViewExists(withText(resources.getString(R.string.tournamentPoints))));

		teams = teams.replaceAll("\t", "\n");
		CommonTestUtils.getDisplayedView(R.id.tvTHName).check(matches(withText(tournamentName)));
		CommonTestUtils.getDisplayedView(R.id.tvTHFormat).check(matches(withText(TournamentFormat.GROUPS.stringValue())));
		CommonTestUtils.getDisplayedView(R.id.tvTHStageType).check(matches(withText(TournamentStageType.SUPER_FOUR.stringValue())));
		CommonTestUtils.getDisplayedView(R.id.tvTHTeams).check(matches(withText(teams)));

		CommonTestUtils.deleteTournament(context, tournamentName);
	}

	@Test
	public void createKnockOutSeries() {
		HomeActivity activity = mActivityTestRule.getActivity();
		Context context = activity.getApplicationContext();
		Resources resources = activity.getResources();

		String tournamentName = "Knock-Out Testing";
		CommonTestUtils.getDisplayedView(R.id.btnNewTournament).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTournamentName).perform(replaceText(tournamentName));
		CommonTestUtils.getDisplayedView(R.id.etTournamentTeamCount).perform(replaceText("4"));
		CommonTestUtils.getDisplayedView(R.id.btnSelectTournamentTeams).perform(click());

		CommonTestUtils.getDisplayedView("Australia").perform(click());
		CommonTestUtils.getDisplayedView("India").perform(click());
		CommonTestUtils.getDisplayedView("Pakistan").perform(click());
		CommonTestUtils.getDisplayedView("West Indies").perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());

		String teams = "Australia,\tIndia,\tPakistan,\tWest Indies";
		CommonTestUtils.getDisplayedView(R.id.tvSelectedTeams).check(matches(withText(teams)));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.tournamentFormat)).check(matches(isDisplayed()));

		CommonTestUtils.getView(R.string.tournamentFormat, R.string.knockOut, activity).perform(click());
		CommonTestUtils.getView(resources.getString(R.string.tournamentStages)).check(matches(withEffectiveVisibility(GONE)));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.setMatchData)).perform(click());

		CommonTestUtils.goToView(R.id.etMaxOvers).perform(replaceText("5"));
		CommonTestUtils.goToView(R.id.etMaxWickets).perform(replaceText("2"));
		CommonTestUtils.goToView(R.id.etNumPlayers).perform(replaceText("11"));
		CommonTestUtils.goToView(resources.getString(R.string.confirm)).perform(click());

		CommonTestUtils.getDisplayedView(resources.getString(R.string.reOrderSchedule)).check(matches(isDisplayed()));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.confirm)).check(matches(isDisplayed()));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.confirm)).perform(click());

		assert (CommonTestUtils.checkViewExists(withText(resources.getString(R.string.tournamentHome))));
		assert (CommonTestUtils.checkViewExists(withText(resources.getString(R.string.tournamentSchedule))));
		assert (CommonTestUtils.checkViewExists(withText(resources.getString(R.string.tournamentStats))));
		assert (CommonTestUtils.checkViewExists(withText(resources.getString(R.string.tournamentPoints))));

		teams = teams.replaceAll("\t", "\n");
		CommonTestUtils.getDisplayedView(R.id.tvTHName).check(matches(withText(tournamentName)));
		CommonTestUtils.getDisplayedView(R.id.tvTHFormat).check(matches(withText(TournamentFormat.KNOCK_OUT.stringValue())));
		CommonTestUtils.getDisplayedView(R.id.tvTHStageType).check(matches(withText(TournamentStageType.NONE.stringValue())));
		CommonTestUtils.getDisplayedView(R.id.tvTHTeams).check(matches(withText(teams)));

		CommonTestUtils.deleteTournament(context, tournamentName);
	}

	private void checkPossibleTournamentFormat(boolean isRoundRobinEnabled, boolean isGroupsEnabled,
											   boolean isKnockOutEnabled, boolean isBilateralEnabled) {
		checkEnabled(R.string.tournamentFormat, R.string.roundRobin, isRoundRobinEnabled);
		checkEnabled(R.string.tournamentFormat, R.string.groups, isGroupsEnabled);
		checkEnabled(R.string.tournamentFormat, R.string.knockOut, isKnockOutEnabled);
		checkEnabled(R.string.tournamentFormat, R.string.bilateral, isBilateralEnabled);
	}

	private void checkPossibleTournamentStage(boolean isSuper4Enabled, boolean isSuper6Enabled,
											  boolean isKnockOutEnabled, boolean isQualifiersEnabled,
											  boolean isNoneEnabled) {
		checkEnabled(R.string.tournamentStages, R.string.superFourStage, isSuper4Enabled);
		checkEnabled(R.string.tournamentStages, R.string.superSixStage, isSuper6Enabled);
		checkEnabled(R.string.tournamentStages, R.string.knockOut, isKnockOutEnabled);
		checkEnabled(R.string.tournamentStages, R.string.qualifiers, isQualifiersEnabled);
		checkEnabled(R.string.tournamentStages, R.string.none, isNoneEnabled);
	}

	private void checkEnabled(int parentContentDescriptionStringId, int childTextStringId, boolean isOptionEnabled) {
		CommonTestUtils.getView(parentContentDescriptionStringId, childTextStringId, mActivityTestRule.getActivity())
				.check(matches(isOptionEnabled ? isEnabled() : not(isEnabled())));
	}

}
