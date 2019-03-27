package com.theNewCone.cricketScoreCard;

import android.content.res.Resources;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.theNewCone.cricketScoreCard.activity.HomeActivity;
import com.theNewCone.cricketScoreCard.utils.CommonTestUtils;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;
import com.theNewCone.cricketScoreCard.utils.MatchRunInfo;

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

	private MatchRunInfo info = new MatchRunInfo("Random Match", 5, 2, 11);

	@Rule
	public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

	@BeforeClass
	public static void beforeClass() {
		IdlingPolicies.setMasterPolicyTimeout(5, TimeUnit.SECONDS);
		IdlingPolicies.setIdlingResourceTimeout(5, TimeUnit.SECONDS);
	}

	@Test
	public void matchNameValidation() {
		CommonTestUtils.loadDBData();
		HomeActivity activity = mActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		info.setTeam1("Australia", "AUS", CommonTestUtils.AUS_PLAYERS,
				CommonTestUtils.AUS_PLAYERS[0], CommonTestUtils.AUS_PLAYERS[5]);
		info.setTeam2("India", "IND", CommonTestUtils.IND_PLAYERS,
				CommonTestUtils.IND_PLAYERS[0], CommonTestUtils.IND_PLAYERS[3]);

		CommonTestUtils.getDisplayedView(R.id.btnNewMatch).perform(click());

		//Team Selection
		CommonTestUtils.selectTeam(info, true);
		CommonTestUtils.selectTeam(info, false);

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
		CommonTestUtils.loadDBData();
		HomeActivity activity = mActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		info.setTeam1("Team-X", "TX", new String[0], null, null);

		CommonTestUtils.getDisplayedView(R.id.btnNewMatch).perform(click());

		//Team1 - No Players
		CommonTestUtils.getDisplayedView(R.id.btnNMSelectTeam1).perform(click());
		CommonTestUtils.getDisplayedView("Team-X").perform(click());
		CommonTestUtils.getDisplayedView(R.id.btnPTSSelectPlayers).perform(click());
		CommonTestUtils.checkIfToastShown(resources.getString(R.string.PTS_notEnoughPlayers));
		CommonTestUtils.getDisplayedView(R.id.btnPTSCancel).perform(click());

		//Team1 - Not Enough players
		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).perform(replaceText("25"));
		info.setTeam1("Australia", "AUS", CommonTestUtils.AUS_PLAYERS,
				CommonTestUtils.AUS_PLAYERS[0], CommonTestUtils.AUS_PLAYERS[5]);
		CommonTestUtils.selectTeam(info, true, true, false, false);
		CommonTestUtils.checkIfToastShown(resources.getString(R.string.PTS_notEnoughPlayers));
		CommonTestUtils.getDisplayedView(R.id.btnPTSCancel).perform(click());

		//Team-1 Players not selected
		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).perform(replaceText("11"));
		CommonTestUtils.selectTeam(info, true, false, false, false);
		CommonTestUtils.getDisplayedView(R.id.btnPTSOk).perform(click());
		CommonTestUtils.checkIfToastShown(resources.getString(R.string.PTS_selectPlayersForTeam));
		CommonTestUtils.getDisplayedView(R.id.btnPTSCancel).perform(click());

		//Duplicate Player Selection
		String[] AUS_PLAYERS = new String[11];
		System.arraycopy(CommonTestUtils.AUS_PLAYERS, 0, AUS_PLAYERS, 0, 11);
		AUS_PLAYERS[10] = "K Khaleel Ahmed";

		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).perform(replaceText("11"));
		info.setTeam1("Australia", "AUS", AUS_PLAYERS,
				CommonTestUtils.AUS_PLAYERS[0], CommonTestUtils.AUS_PLAYERS[5]);
		info.setTeam2("India", "IND", CommonTestUtils.IND_PLAYERS,
				CommonTestUtils.IND_PLAYERS[0], CommonTestUtils.IND_PLAYERS[3]);

		CommonTestUtils.selectTeam(info, true);
		CommonTestUtils.selectTeam(info, false);
		CommonTestUtils.getDisplayedView(R.id.btnValidate).perform(click());
		CommonTestUtils.checkIfToastShown(
				String.format(resources.getString(R.string.NM_duplicatePlayer), AUS_PLAYERS[10]));
	}

	@Test
	public void validateOversPlayersWickets() {
		CommonTestUtils.loadDBData();
		HomeActivity activity = mActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		info.setTeam1("Australia", "AUS", CommonTestUtils.AUS_PLAYERS,
				CommonTestUtils.AUS_PLAYERS[0], CommonTestUtils.AUS_PLAYERS[5]);
		info.setTeam2("India", "IND", CommonTestUtils.IND_PLAYERS,
				CommonTestUtils.IND_PLAYERS[0], CommonTestUtils.IND_PLAYERS[3]);

		CommonTestUtils.getDisplayedView(R.id.btnNewMatch).perform(click());
		CommonTestUtils.selectTeam(info, true);
		CommonTestUtils.selectTeam(info, false);

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
		CommonTestUtils.loadDBData();
		HomeActivity activity = mActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		String team1Captain = CommonTestUtils.WI_PLAYERS[5], team1WK = CommonTestUtils.WI_PLAYERS[2];
		String team2Captain = CommonTestUtils.IND_PLAYERS[0], team2WK = CommonTestUtils.IND_PLAYERS[3];

		info.setTeam1("West Indies", "WI", CommonTestUtils.WI_PLAYERS, team1Captain, team1WK);
		info.setTeam2("India", "IND", CommonTestUtils.IND_PLAYERS, team2Captain, team2WK);

		CommonTestUtils.getDisplayedView(R.id.btnNewMatch).perform(click());

		//Team1 - None Selected
		CommonTestUtils.selectTeam(info, true, true, false, false);
		CommonTestUtils.checkIfToastShown(resources.getString(R.string.PTS_selectCapAndWK));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.cancel)).perform(click());

		//Team1 - Only Captain Selected
		CommonTestUtils.selectTeam(info, true, true, true, false);
		CommonTestUtils.checkIfToastShown(resources.getString(R.string.PTS_selectCapAndWK));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.cancel)).perform(click());

		//Team2 - None Selected
		CommonTestUtils.selectTeam(info, true);
		CommonTestUtils.selectTeam(info, false, true, false, false);
		CommonTestUtils.checkIfToastShown(resources.getString(R.string.PTS_selectCapAndWK));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.cancel)).perform(click());

		//Team2 - Only Wicket-Keeper Selected
		CommonTestUtils.selectTeam(info, false, true, false, true);
		CommonTestUtils.checkIfToastShown(resources.getString(R.string.PTS_selectCapAndWK));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.cancel)).perform(click());

		//Team1 - Wicket-Keeper Not in Team
		String[] WI_PLAYERS = new String[11];
		System.arraycopy(CommonTestUtils.WI_PLAYERS, 0, WI_PLAYERS, 0, 11);
//		WI_PLAYERS[2] = "AMarlon Samuels";
		WI_PLAYERS[2] = "Marlon Samuels";

		info.setTeam1("West Indies", "WI", WI_PLAYERS, team1Captain, team1WK);
		CommonTestUtils.selectTeam(info, true, true, false, false);
		CommonTestUtils.checkIfToastShown(
				String.format(resources.getString(R.string.PTS_wkNotInTeam), team1WK));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.cancel)).perform(click());

		//Team1 - Captain Not in Team
		System.arraycopy(CommonTestUtils.WI_PLAYERS, 0, WI_PLAYERS, 0, 11);
//		WI_PLAYERS[5] = "AMarlon Samuels";
		WI_PLAYERS[5] = "Marlon Samuels";
		info.setTeam1("West Indies", "WI", WI_PLAYERS, team1Captain, team1WK);
		CommonTestUtils.selectTeam(info, true, true, false, false);
		CommonTestUtils.checkIfToastShown(
				String.format(resources.getString(R.string.PTS_captNotInTeam), team1Captain));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.cancel)).perform(click());
	}

	@Test
	public void playerCountMismatch() {
		CommonTestUtils.loadDBData();
		HomeActivity activity = mActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		info.setTeam1("Australia", "AUS", CommonTestUtils.AUS_PLAYERS,
				CommonTestUtils.AUS_PLAYERS[0], CommonTestUtils.AUS_PLAYERS[5]);
		info.setTeam2("India", "IND", CommonTestUtils.IND_PLAYERS,
				CommonTestUtils.IND_PLAYERS[0], CommonTestUtils.IND_PLAYERS[3]);

		CommonTestUtils.getDisplayedView(R.id.btnNewMatch).perform(click());

		//Less Number of Players
		String[] IND_PLAYERS = new String[9];
		System.arraycopy(CommonTestUtils.IND_PLAYERS, 0, IND_PLAYERS, 0, 9);
		info.setTeam2("India", "IND", IND_PLAYERS,
				CommonTestUtils.IND_PLAYERS[0], CommonTestUtils.IND_PLAYERS[3]);

		CommonTestUtils.selectTeam(info, false, true, false, false);
		CommonTestUtils.checkIfToastShown(
				String.format(resources.getString(R.string.Player_selectExactPlayers), 9, 11));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.cancel)).perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.cancel)).perform(click());

		//More Number of Players
		IND_PLAYERS = new String[12];
		System.arraycopy(CommonTestUtils.IND_PLAYERS, 0, IND_PLAYERS, 0, 11);
		IND_PLAYERS[11] = "Ambati Rayudu";
		info.setTeam2("India", "IND", IND_PLAYERS,
				CommonTestUtils.IND_PLAYERS[0], CommonTestUtils.IND_PLAYERS[3]);

		CommonTestUtils.selectTeam(info, false, true, false, false);
		CommonTestUtils.checkIfToastShown(
				String.format(resources.getString(R.string.Player_selectExactPlayers), 12, 11));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.cancel)).perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.cancel)).perform(click());

		//Player Count Changed
		info.setTeam2("India", "IND", CommonTestUtils.IND_PLAYERS,
				CommonTestUtils.IND_PLAYERS[0], CommonTestUtils.IND_PLAYERS[3]);
		CommonTestUtils.selectTeam(info, true);
		CommonTestUtils.selectTeam(info, false);
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
		CommonTestUtils.loadDBData();
		HomeActivity activity = mActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		String matchName = "NLOMTest - Match1";
		int maxOvers = 5, maxWickets = 2, numPlayers = 11;
		MatchRunInfo info = new MatchRunInfo(matchName, maxOvers, maxWickets, numPlayers);
		info.setTeam1("Australia", "AUS", CommonTestUtils.AUS_PLAYERS,
				CommonTestUtils.AUS_PLAYERS[0], CommonTestUtils.AUS_PLAYERS[5]);
		info.setTeam2("India", "IND", CommonTestUtils.IND_PLAYERS,
				CommonTestUtils.IND_PLAYERS[0], CommonTestUtils.IND_PLAYERS[3]);

		CommonTestUtils.getDisplayedView(R.id.btnNewMatch).perform(click());

		CommonTestUtils.getDisplayedView(R.id.etMatchName).perform(replaceText(matchName));
		CommonTestUtils.getDisplayedView(R.id.etMaxOvers).perform(replaceText(String.valueOf(maxOvers)));
		CommonTestUtils.getDisplayedView(R.id.etMaxWickets).perform(replaceText(String.valueOf(maxWickets)));
		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).perform(replaceText(String.valueOf(numPlayers)));

		CommonTestUtils.selectTeam(info, true);
		CommonTestUtils.selectTeam(info, false);

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
