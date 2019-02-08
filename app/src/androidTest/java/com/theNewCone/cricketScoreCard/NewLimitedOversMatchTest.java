package com.theNewCone.cricketScoreCard;

import android.content.res.Resources;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.theNewCone.cricketScoreCard.activity.HomeActivity;
import com.theNewCone.cricketScoreCard.utils.CommonTestUtils;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class NewLimitedOversMatchTest {

	private final String ADDITIONAL_IND_PLAYER = "Ambati Rayudu";
	@Rule
	public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

	@BeforeClass
	public static void beforeClass() {
		IdlingPolicies.setMasterPolicyTimeout(5, TimeUnit.SECONDS);
		IdlingPolicies.setIdlingResourceTimeout(5, TimeUnit.SECONDS);
	}

/*
	@Test
	public void noTeamsAvailable() {
		HomeActivity activity = homeActivityTestRule.getActivity();
		Context context = activity.getApplicationContext();
		CommonTestUtils.clearTeams(context);

		Resources resources = activity.getResources();
		CommonTestUtils.getDisplayedView(R.id.btnNewMatch).perform(click());
		String message = resources.getString(R.string.NM_noTeamsAvailable)
				+ resources.getString(R.string.NM_needMinimumTwoTeams);
		CommonTestUtils.getDisplayedView(R.id.tvInsufficientTeams).check(matches(withText(message.trim())));
		CommonTestUtils.getDisplayedView(R.id.btnManageTeam).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTeamName).check(matches(isDisplayed()));
	}

	@Test
	public void onlyOneTeamAvailable() {
		HomeActivity activity = homeActivityTestRule.getActivity();
		Context context = activity.getApplicationContext();
		CommonTestUtils.clearTeams(context);
		CommonTestUtils.setupTeams(context, TeamEnum.IND);

		Resources resources = activity.getResources();
		CommonTestUtils.getDisplayedView(R.id.btnNewMatch).perform(click());
		String message = resources.getString(R.string.NM_onlyOneTeamAvailable)
				+ resources.getString(R.string.NM_needMinimumTwoTeams);
		CommonTestUtils.getDisplayedView(R.id.tvInsufficientTeams).check(matches(withText(message.trim())));
		CommonTestUtils.getDisplayedView(R.id.btnManageTeam).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etTeamName).check(matches(isDisplayed()));

		CommonTestUtils.clearTeams(context);
	}
*/

	@Test
	public void selectTeamsCorrectly() {
		HomeActivity activity = mActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		//No Teams Selected
		CommonTestUtils.getDisplayedView(R.id.btnNewMatch).perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.toss)).perform(click());
		CommonTestUtils.checkIfToastShown(resources.getString(R.string.NM_selectBothTeams));

		//Only Team-1 selected
		CommonTestUtils.getDisplayedView(R.id.tvTeam1).perform(click());
		CommonTestUtils.getDisplayedView("Australia").perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.toss)).perform(click());
		CommonTestUtils.checkIfToastShown(resources.getString(R.string.NM_selectBothTeams));

		//Only Team-2 selected
		CommonTestUtils.clickNavigationMenuItem(1);
		CommonTestUtils.getDisplayedView(R.id.btnNewMatch).perform(click());
		CommonTestUtils.getDisplayedView(R.id.tvTeam2).perform(click());
		CommonTestUtils.getDisplayedView("India").perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.toss)).perform(click());
		CommonTestUtils.checkIfToastShown(resources.getString(R.string.NM_selectBothTeams));

		//Same Teams selected are same
		CommonTestUtils.getDisplayedView(R.id.tvTeam1).perform(click());
		CommonTestUtils.getDisplayedView("India").perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.toss)).perform(click());
		CommonTestUtils.checkIfToastShown(resources.getString(R.string.NM_selectDifferentTeams));
	}

	@Test
	public void matchNameValidation() {
		HomeActivity activity = mActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		CommonTestUtils.getDisplayedView(R.id.btnNewMatch).perform(click());

		CommonTestUtils.getDisplayedView(R.id.tvTeam1).perform(click());
		CommonTestUtils.getDisplayedView("Australia").perform(click());
		CommonTestUtils.getDisplayedView(R.id.tvTeam2).perform(click());
		CommonTestUtils.getDisplayedView("India").perform(click());


		//Automatic Name generation
		String teamName = "AUS v IND " + CommonUtils.currTimestamp("yyyyMMMdd");
		CommonTestUtils.getDisplayedView(R.id.etMatchName).check(matches(withText(teamName)));

		//Match Name should not be empty
		CommonTestUtils.getDisplayedView(R.id.etMatchName).perform(replaceText(""));
		CommonTestUtils.getDisplayedView(R.id.btnValidate).perform(click());
		CommonTestUtils.checkIfToastShown(resources.getString(R.string.NM_invalidMatchName));

		//Match Name should not be more than 5 characters
		CommonTestUtils.getDisplayedView(R.id.etMatchName).perform(replaceText("ODI1"));
		CommonTestUtils.getDisplayedView(R.id.btnValidate).perform(click());
		CommonTestUtils.checkIfToastShown(resources.getString(R.string.NM_invalidMatchName));
	}

	@Test
	public void playerSelection() {
		HomeActivity activity = mActivityTestRule.getActivity();
		Resources resources = activity.getResources();
		String[] playersAus = CommonTestUtils.AUS_PLAYERS;
		playersAus[10] = "K Khaleel Ahmed";

		CommonTestUtils.getDisplayedView(R.id.btnNewMatch).perform(click());

		CommonTestUtils.getDisplayedView(R.id.tvTeam1).perform(click());
		CommonTestUtils.getDisplayedView("Team-X").perform(click());
		CommonTestUtils.getDisplayedView(R.id.tvTeam2).perform(click());
		CommonTestUtils.getDisplayedView("Team-Y").perform(click());

		//Team1 - No Players
		CommonTestUtils.getDisplayedView(R.id.btnNMSelectTeam1).perform(click());
		CommonTestUtils.checkIfToastShown(
				String.format(resources.getString(R.string.NM_noPlayersInTeam), "TX"));

		CommonTestUtils.getDisplayedView(R.id.tvTeam1).perform(click());
		CommonTestUtils.getDisplayedView("Australia").perform(click());
		CommonTestUtils.getDisplayedView(R.id.tvTeam2).perform(click());
		CommonTestUtils.getDisplayedView("India").perform(click());

		//Team1 - not enough players
		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).perform(replaceText("25"));
		CommonTestUtils.getDisplayedView(R.id.btnNMSelectTeam1).perform(click());
		CommonTestUtils.checkIfToastShown(
				String.format(resources.getString(R.string.NM_notEnoughPlayers), "AUS"));

		//Team-1 Players not selected
		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).perform(replaceText("11"));
		CommonTestUtils.selectTeamPlayers(R.id.btnNMSelectTeam1, CommonTestUtils.AUS_PLAYERS);
		CommonTestUtils.getDisplayedView(R.id.btnValidate).perform(click());
		CommonTestUtils.checkIfToastShown(
				String.format(resources.getString(R.string.NM_selectPlayersForTeam), "IND"));

		//Duplicate Player Selection
		CommonTestUtils.selectTeamPlayers(R.id.btnNMSelectTeam2, CommonTestUtils.IND_PLAYERS);
		CommonTestUtils.getDisplayedView(R.id.btnValidate).perform(click());
		CommonTestUtils.checkIfToastShown(
				String.format(resources.getString(R.string.NM_duplicatePlayer), playersAus[10]));
	}

	@Test
	public void validateOversPlayersWickets() {
		HomeActivity activity = mActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		CommonTestUtils.getDisplayedView(R.id.btnNewMatch).perform(click());

		CommonTestUtils.getDisplayedView(R.id.tvTeam1).perform(click());
		CommonTestUtils.getDisplayedView("India").perform(click());
		CommonTestUtils.getDisplayedView(R.id.tvTeam2).perform(click());
		CommonTestUtils.getDisplayedView("Team-Y").perform(click());

		//Max Overs to be non-zero & positive
		CommonTestUtils.getDisplayedView(R.id.etMaxOvers).perform(replaceText("0"));
		CommonTestUtils.getDisplayedView(R.id.btnValidate).perform(click());
		CommonTestUtils.checkIfToastShown(resources.getString(R.string.NM_invalidPlayersOversWickets));

		CommonTestUtils.getDisplayedView(R.id.etMaxOvers).perform(replaceText("-1"));
		CommonTestUtils.getDisplayedView(R.id.btnValidate).perform(click());
		CommonTestUtils.checkIfToastShown(resources.getString(R.string.NM_invalidPlayersOversWickets));

		//Max Per Bowlers have to be non-zero
		CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).perform(replaceText("0"));
		CommonTestUtils.getDisplayedView(R.id.btnValidate).perform(click());
		CommonTestUtils.checkIfToastShown(resources.getString(R.string.NM_invalidPlayersOversWickets));

		CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).perform(replaceText("-1"));
		CommonTestUtils.getDisplayedView(R.id.btnValidate).perform(click());
		CommonTestUtils.checkIfToastShown(resources.getString(R.string.NM_invalidPlayersOversWickets));

		//Players have to be non-zero & positive
		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).perform(replaceText("0"));
		CommonTestUtils.getDisplayedView(R.id.btnValidate).perform(click());
		CommonTestUtils.checkIfToastShown(resources.getString(R.string.NM_invalidPlayersOversWickets));

		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).perform(replaceText("-1"));
		CommonTestUtils.getDisplayedView(R.id.btnValidate).perform(click());
		CommonTestUtils.checkIfToastShown(resources.getString(R.string.NM_invalidPlayersOversWickets));

		//Maximum Wickets have to be non-zero & positive
		CommonTestUtils.getDisplayedView(R.id.etMaxWickets).perform(replaceText("0"));
		CommonTestUtils.getDisplayedView(R.id.btnValidate).perform(click());
		CommonTestUtils.checkIfToastShown(resources.getString(R.string.NM_invalidPlayersOversWickets));

		CommonTestUtils.getDisplayedView(R.id.etMaxWickets).perform(replaceText("-1"));
		CommonTestUtils.getDisplayedView(R.id.btnValidate).perform(click());
		CommonTestUtils.checkIfToastShown(resources.getString(R.string.NM_invalidPlayersOversWickets));

		//Wickets should be less than players
		CommonTestUtils.getDisplayedView(R.id.etMaxOvers).perform(replaceText("50"));
		CommonTestUtils.getDisplayedView(R.id.etMaxWickets).perform(replaceText("10"));
		CommonTestUtils.getDisplayedView(R.id.etMaxWickets).perform(replaceText("12"));
		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).perform(replaceText("11"));
		CommonTestUtils.getDisplayedView(R.id.btnValidate).perform(click());
		CommonTestUtils.checkIfToastShown(resources.getString(R.string.NM_wicketsMoreThanPlayers));

		CommonTestUtils.getDisplayedView(R.id.etMaxOvers).perform(replaceText("50"));
		CommonTestUtils.getDisplayedView(R.id.etMaxWickets).perform(replaceText("10"));
		CommonTestUtils.getDisplayedView(R.id.etMaxWickets).perform(replaceText("11"));
		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).perform(replaceText("11"));
		CommonTestUtils.getDisplayedView(R.id.btnValidate).perform(click());
		CommonTestUtils.checkIfToastShown(resources.getString(R.string.NM_wicketsMoreThanPlayers));

		//Bowler Overs more than Max Overs
		CommonTestUtils.getDisplayedView(R.id.etMaxOvers).perform(replaceText("50"));
		CommonTestUtils.getDisplayedView(R.id.etMaxWickets).perform(replaceText("10"));
		CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).perform(replaceText("51"));
		CommonTestUtils.getDisplayedView(R.id.btnValidate).perform(click());
		CommonTestUtils.checkIfToastShown(resources.getString(R.string.NM_bowlerOversMoreThanMaxOvers));

		//Not Enough Bowlers - Not enough overs per bowler
		CommonTestUtils.getDisplayedView(R.id.etMaxOvers).perform(replaceText("50"));
		CommonTestUtils.getDisplayedView(R.id.etMaxWickets).perform(replaceText("10"));
		CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).perform(replaceText("4"));
		CommonTestUtils.getDisplayedView(R.id.btnValidate).perform(click());
		CommonTestUtils.checkIfToastShown(resources.getString(R.string.NM_notEnoughBowlers));

		//Not Enough Bowlers - Less number of Players
		CommonTestUtils.getDisplayedView(R.id.etMaxOvers).perform(replaceText("50"));
		CommonTestUtils.getDisplayedView(R.id.etMaxWickets).perform(replaceText("10"));
		CommonTestUtils.getDisplayedView(R.id.etMaxWickets).perform(replaceText("3"));
		CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).perform(replaceText("10"));
		CommonTestUtils.getDisplayedView(R.id.btnValidate).perform(click());
		CommonTestUtils.checkIfToastShown(resources.getString(R.string.NM_notEnoughBowlers));
	}

	@Test
	public void captainOrWKSelection() {
		HomeActivity activity = mActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		String team1Captain = CommonTestUtils.WI_PLAYERS[5], team1WK = CommonTestUtils.WI_PLAYERS[2];
		String team2Captain = CommonTestUtils.IND_PLAYERS[0], team2WK = CommonTestUtils.IND_PLAYERS[3];

		CommonTestUtils.getDisplayedView(R.id.btnNewMatch).perform(click());

		CommonTestUtils.getDisplayedView(R.id.tvTeam1).perform(click());
		CommonTestUtils.getDisplayedView("West Indies").perform(click());
		CommonTestUtils.getDisplayedView(R.id.tvTeam2).perform(click());
		CommonTestUtils.getDisplayedView("India").perform(click());

		//Team1 - None Selected
		CommonTestUtils.selectTeamPlayers(R.id.btnNMSelectTeam1, CommonTestUtils.WI_PLAYERS);
		CommonTestUtils.selectTeamPlayers(R.id.btnNMSelectTeam2, CommonTestUtils.IND_PLAYERS);
		CommonTestUtils.getDisplayedView(R.id.btnValidate).perform(click());
		CommonTestUtils.checkIfToastShown(
				String.format(resources.getString(R.string.NM_selectCapAndWK), "WI"));

		//Team1 - Only Captain Selected
		CommonTestUtils.getDisplayedView(R.id.tvTeam1Captain).perform(click());
		CommonTestUtils.goToViewStarting(team1Captain).perform(click());
		CommonTestUtils.getDisplayedView(R.id.btnValidate).perform(click());
		CommonTestUtils.checkIfToastShown(
				String.format(resources.getString(R.string.NM_selectCapAndWK), "WI"));

		//Team2 - None Selected
		CommonTestUtils.getDisplayedView(R.id.tvTeam1WK).perform(click());
		CommonTestUtils.goToViewStarting(team1WK).perform(click());
		CommonTestUtils.getDisplayedView(R.id.btnValidate).perform(click());
		CommonTestUtils.checkIfToastShown(
				String.format(resources.getString(R.string.NM_selectCapAndWK), "IND"));

		//Team2 - Only Wicket-Keeper Selected
		CommonTestUtils.getDisplayedView(R.id.tvTeam2WK).perform(click());
		CommonTestUtils.goToViewStarting(team2WK).perform(click());
		CommonTestUtils.getDisplayedView(R.id.btnValidate).perform(click());
		CommonTestUtils.checkIfToastShown(
				String.format(resources.getString(R.string.NM_selectCapAndWK), "IND"));

		//Team1 - Wicket-Keeper Not in Team
		CommonTestUtils.getDisplayedView(R.id.tvTeam2Captain).perform(click());
		CommonTestUtils.goToViewStarting(team2Captain).perform(click());
		String ADDITIONAL_WI_PLAYER = "Ashley Nurse";
		CommonTestUtils.clickPlayers(R.id.btnNMSelectTeam1, new String[]{ADDITIONAL_WI_PLAYER, CommonTestUtils.WI_PLAYERS[2]});
		CommonTestUtils.getDisplayedView(R.id.tvTeam1Captain).check(matches(withText(
				String.format(resources.getString(R.string.captainName), team1Captain))));
		CommonTestUtils.getDisplayedView(R.id.tvTeam1WK).check(matches(withText(
				String.format(resources.getString(R.string.wkName), team1WK))));
		CommonTestUtils.getDisplayedView(R.id.btnValidate).perform(click());
		CommonTestUtils.checkIfToastShown(
				String.format(resources.getString(R.string.NM_wkNotInTeam), team1WK, "WI"));

		//Team2 - Captain Not in Team
		CommonTestUtils.clickPlayers(R.id.btnNMSelectTeam1, new String[]{ADDITIONAL_WI_PLAYER, CommonTestUtils.WI_PLAYERS[2]});
		CommonTestUtils.clickPlayers(R.id.btnNMSelectTeam2, new String[]{ADDITIONAL_IND_PLAYER, CommonTestUtils.IND_PLAYERS[0]});
		CommonTestUtils.getDisplayedView(R.id.tvTeam2Captain).check(matches(withText(
				String.format(resources.getString(R.string.captainName), team2Captain))));
		CommonTestUtils.getDisplayedView(R.id.tvTeam2WK).check(matches(withText(
				String.format(resources.getString(R.string.wkName), team2WK))));
		CommonTestUtils.getDisplayedView(R.id.btnValidate).perform(click());
		CommonTestUtils.checkIfToastShown(
				String.format(resources.getString(R.string.NM_captNotInTeam), team2Captain, "IND"));
	}

	@Test
	public void playerCountMismatch() {
		HomeActivity activity = mActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		CommonTestUtils.getDisplayedView(R.id.btnNewMatch).perform(click());

		CommonTestUtils.getDisplayedView(R.id.tvTeam1).perform(click());
		CommonTestUtils.getDisplayedView("Australia").perform(click());
		CommonTestUtils.getDisplayedView(R.id.tvTeam2).perform(click());
		CommonTestUtils.getDisplayedView("India").perform(click());
		CommonTestUtils.selectTeamPlayers(R.id.btnNMSelectTeam2, CommonTestUtils.IND_PLAYERS);

		//Less Number of Players
		CommonTestUtils.clickPlayers(R.id.btnNMSelectTeam2,
				new String[]{CommonTestUtils.IND_PLAYERS[4], CommonTestUtils.IND_PLAYERS[7]});
		CommonTestUtils.checkIfToastShown(
				String.format(resources.getString(R.string.Player_selectExactPlayers), 9, 11));

		//More Number of Players
		CommonTestUtils.clickPlayers(R.id.btnNMSelectTeam2,
				new String[]{CommonTestUtils.IND_PLAYERS[4], CommonTestUtils.IND_PLAYERS[7], ADDITIONAL_IND_PLAYER});
		CommonTestUtils.checkIfToastShown(
				String.format(resources.getString(R.string.Player_selectExactPlayers), 12, 11));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.cancel)).perform(click());

		//Player Count Changed
		CommonTestUtils.getDisplayedView(R.id.etMaxWickets).perform(replaceText("9"));
		CommonTestUtils.getDisplayedView(R.id.btnValidate).perform(click());
		CommonTestUtils.checkIfToastShown(resources.getString(R.string.NM_playerCountChanged));
	}

	@Test
	public void updateNumPlayersIfMaxWicketsUpdated() {
		CommonTestUtils.getDisplayedView(R.id.btnNewMatch).perform(click());

		CommonTestUtils.getDisplayedView(R.id.etMaxWickets).perform(replaceText("5"));
		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).check(matches(withText("6")));

		CommonTestUtils.getDisplayedView(R.id.etMaxWickets).perform(replaceText("10"));
		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).check(matches(withText("11")));

		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).perform(replaceText("90"));
		CommonTestUtils.getDisplayedView(R.id.etMaxWickets).perform(replaceText("3"));
		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).check(matches(withText("4")));
	}

	@Test
	public void updateMaxPerBowlerIfMaxWicketsUpdated() {
		CommonTestUtils.getDisplayedView(R.id.btnNewMatch).perform(click());

		CommonTestUtils.getDisplayedView(R.id.etMaxWickets).perform(replaceText("5"));
		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).check(matches(withText("6")));
		CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).check(matches(withText("10")));

		CommonTestUtils.getDisplayedView(R.id.etMaxWickets).perform(replaceText("10"));
		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).check(matches(withText("11")));
		CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).check(matches(withText("10")));

		CommonTestUtils.getDisplayedView(R.id.etMaxWickets).perform(replaceText("3"));
		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).check(matches(withText("4")));
		CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).check(matches(withText("17")));

		CommonTestUtils.getDisplayedView(R.id.etMaxWickets).perform(replaceText("4"));
		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).check(matches(withText("5")));
		CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).check(matches(withText("13")));
	}

	@Test
	public void updateMaxPerBowlerIfNumPlayersUpdated() {
		CommonTestUtils.getDisplayedView(R.id.btnNewMatch).perform(click());

		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).perform(replaceText("6"));
		CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).check(matches(withText("10")));

		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).perform(replaceText("4"));
		CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).check(matches(withText("17")));

		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).perform(replaceText("11"));
		CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).check(matches(withText("10")));

		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).perform(replaceText("5"));
		CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).check(matches(withText("13")));

		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).perform(replaceText("12"));
		CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).check(matches(withText("10")));

		CommonTestUtils.getDisplayedView(R.id.etMaxOvers).perform(replaceText("12"));
		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).perform(replaceText("6"));
		CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).check(matches(withText("3")));

		CommonTestUtils.getDisplayedView(R.id.etMaxOvers).perform(replaceText("12"));
		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).perform(replaceText("3"));
		CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).check(matches(withText("6")));

		CommonTestUtils.getDisplayedView(R.id.etMaxOvers).perform(replaceText("24"));
		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).perform(replaceText("8"));
		CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).check(matches(withText("5")));

		CommonTestUtils.getDisplayedView(R.id.etMaxOvers).perform(replaceText("24"));
		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).perform(replaceText("5"));
		CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).check(matches(withText("6")));
	}

	@Test
	public void updateMaxPerBowlerIfMaxOversUpdated() {
		CommonTestUtils.getDisplayedView(R.id.btnNewMatch).perform(click());

		CommonTestUtils.getDisplayedView(R.id.etMaxOvers).perform(replaceText("1"));
		CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).check(matches(withText("1")));

		CommonTestUtils.getDisplayedView(R.id.etMaxOvers).perform(replaceText("5"));
		CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).check(matches(withText("1")));

		CommonTestUtils.getDisplayedView(R.id.etMaxOvers).perform(replaceText("6"));
		CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).check(matches(withText("2")));

		CommonTestUtils.getDisplayedView(R.id.etMaxOvers).perform(replaceText("11"));
		CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).check(matches(withText("3")));

		CommonTestUtils.getDisplayedView(R.id.etMaxOvers).perform(replaceText("28"));
		CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).check(matches(withText("6")));

		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).perform(replaceText("6"));
		CommonTestUtils.getDisplayedView(R.id.etMaxOvers).perform(replaceText("12"));
		CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).check(matches(withText("3")));

		CommonTestUtils.getDisplayedView(R.id.etMaxOvers).perform(replaceText("24"));
		CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).check(matches(withText("5")));

		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).perform(replaceText("5"));
		CommonTestUtils.getDisplayedView(R.id.etMaxOvers).perform(replaceText("12"));
		CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).check(matches(withText("3")));

		CommonTestUtils.getDisplayedView(R.id.etMaxOvers).perform(replaceText("24"));
		CommonTestUtils.getDisplayedView(R.id.etMaxPerBowler).check(matches(withText("6")));
	}

	@Test
	public void progressToNextStage() {
		HomeActivity activity = mActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		CommonTestUtils.getDisplayedView(R.id.btnNewMatch).perform(click());

		CommonTestUtils.getDisplayedView(R.id.etMaxOvers).perform(replaceText("5"));
		CommonTestUtils.getDisplayedView(R.id.etMaxWickets).perform(replaceText("2"));
		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).perform(replaceText("11"));

		CommonTestUtils.getDisplayedView(R.id.tvTeam1).perform(click());
		CommonTestUtils.getDisplayedView("Australia").perform(click());
		CommonTestUtils.selectTeamPlayers(R.id.btnNMSelectTeam1, CommonTestUtils.AUS_PLAYERS);
		CommonTestUtils.getDisplayedView(R.id.tvTeam1Captain).perform(click());
		CommonTestUtils.goToViewStarting(CommonTestUtils.AUS_PLAYERS[0]).perform(click());
		CommonTestUtils.getDisplayedView(R.id.tvTeam1WK).perform(click());
		CommonTestUtils.goToViewStarting(CommonTestUtils.AUS_PLAYERS[5]).perform(click());

		CommonTestUtils.getDisplayedView(R.id.tvTeam2).perform(click());
		CommonTestUtils.getDisplayedView("India").perform(click());
		CommonTestUtils.selectTeamPlayers(R.id.btnNMSelectTeam2, CommonTestUtils.IND_PLAYERS);
		CommonTestUtils.getDisplayedView(R.id.tvTeam2Captain).perform(click());
		CommonTestUtils.goToViewStarting(CommonTestUtils.IND_PLAYERS[0]).perform(click());
		CommonTestUtils.getDisplayedView(R.id.tvTeam2WK).perform(click());
		CommonTestUtils.goToViewStarting(CommonTestUtils.IND_PLAYERS[3]).perform(click());

		CommonTestUtils.getDisplayedView(resources.getString(R.string.toss)).perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.tossWonBy)).check(matches(isDisplayed()));
		CommonTestUtils.getDisplayedView("AUS").perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.choose)).check(matches(isDisplayed()));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.batting)).perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.startMatch)).perform(click());

		CommonTestUtils.getDisplayedView(R.id.tvBattingTeam).check(matches(withText("AUS")));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.selBatsman));
	}
}
