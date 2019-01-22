package com.theNewCone.cricketScoreCard;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingResourceTimeoutException;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import com.theNewCone.cricketScoreCard.activity.HomeActivity;
import com.theNewCone.cricketScoreCard.utils.CommonTestUtils;
import com.theNewCone.cricketScoreCard.utils.database.DatabaseHandler;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PlayerManageTest {

	private final String nameFirst = "Anil";
	private final String batStyleFirst = "LHB";
	private final String bowlStyleFirst = "SLA";
	private final boolean isWKFirst = false;
	private final String nameSecond = "Ankit";
	private final String batStyleSecond = "RHB";
	private final String bowlStyleSecond = "NONE";
	private final boolean isWKSecond = true;
	@Rule
	public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

	@BeforeClass
	public static void beforeClass() {
		IdlingPolicies.setMasterPolicyTimeout(5, TimeUnit.SECONDS);
		IdlingPolicies.setIdlingResourceTimeout(5, TimeUnit.SECONDS);
	}

	@Test
	public void validateInput() {
		CommonTestUtils.getDisplayedView(R.id.btnManagePlayer).perform(click());
		Resources resources = mActivityTestRule.getActivity().getResources();

		//Player Name Empty
		CommonTestUtils.getDisplayedView(R.id.btnPlayerSave).perform(click());
		String errorMessage = resources.getString(R.string.Player_enterValidPlayerName)
				+ resources.getString(R.string.Player_selectValidBatStyle)
				+ resources.getString(R.string.Player_selectValidBowlStyle);
		CommonTestUtils.checkIfToastShown(errorMessage);

		//Player Name less than 3 characters
		CommonTestUtils.getDisplayedView(R.id.etPlayerName).perform(replaceText("AK"), closeSoftKeyboard());
		CommonTestUtils.getDisplayedView(R.id.btnPlayerSave).perform(click());
		errorMessage = resources.getString(R.string.Player_enterValidPlayerName)
				+ resources.getString(R.string.Player_selectValidBatStyle)
				+ resources.getString(R.string.Player_selectValidBowlStyle);
		CommonTestUtils.checkIfToastShown(errorMessage);

		//Batting Style Not selected
		CommonTestUtils.getDisplayedView(R.id.etPlayerName).perform(replaceText(nameFirst), closeSoftKeyboard());
		CommonTestUtils.getDisplayedView(R.id.btnPlayerSave).perform(click());
		errorMessage = resources.getString(R.string.Player_selectValidBatStyle)
				+ resources.getString(R.string.Player_selectValidBowlStyle);
		CommonTestUtils.checkIfToastShown(errorMessage);

		//Bowling Style Not selected
		CommonTestUtils.getDisplayedView(R.id.btnSelectPlayerBatStyle).perform(click());
		CommonTestUtils.goToView(batStyleFirst).perform(click());
		CommonTestUtils.getDisplayedView(R.id.btnPlayerSave).perform(click());
		errorMessage = resources.getString(R.string.Player_selectValidBowlStyle);
		CommonTestUtils.checkIfToastShown(errorMessage);
	}

	//@Test
	public void testNoPlayersFoundIsShown() {
		deleteAllPlayers();

		CommonTestUtils.getDisplayedView(R.id.btnManagePlayer).perform(click());
		CommonTestUtils.getDisplayedView(R.id.menu_getPlayerList).perform(click());
		ViewInteraction tvNoPlayers = CommonTestUtils.getDisplayedView(R.id.tvNoPlayers);
		tvNoPlayers.check(matches(isDisplayed()));
		tvNoPlayers.check(matches(withText(R.string.noPlayersFound)));

		CommonTestUtils.getDisplayedView(R.id.btnCancel).check(matches(isDisplayed()));
	}

	@Test
	public void testPlayerManage() {
		CommonTestUtils.getDisplayedView(R.id.btnManagePlayer).perform(click());
		playerDelete(nameFirst);
		playerDelete(nameSecond);

		/*Create the Player*/
		playerCreate();
		CommonTestUtils.checkIfToastShown(R.string.Player_saveSuccess);

		/*Select the created Player from the list*/
		/*Verify that the team name and team short name are same as that which are created*/
		CommonTestUtils.getDisplayedView(R.id.menu_getPlayerList).perform(click());
		CommonTestUtils.goToView(nameFirst).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etPlayerName).check(matches(withText(nameFirst)));
		CommonTestUtils.getDisplayedView(R.id.tvPlayerBatStyle).check(matches(withText(batStyleFirst)));
		CommonTestUtils.getDisplayedView(R.id.tvPlayerBowlStyle).check(matches(withText(bowlStyleFirst)));
		ViewInteraction cbIsWK = CommonTestUtils.getDisplayedView(R.id.cbIsPlayerWK);
		if (isWKFirst)
			cbIsWK.check(matches(isChecked()));
		else
			cbIsWK.check(matches(isNotChecked()));

		/*Test Duplicate Player Creation*/
		CommonTestUtils.getDisplayedView(R.id.btnPlayerClear).perform(click());
		playerCreate();
		CommonTestUtils.checkIfToastShown(R.string.Player_selectDifferentPlayerName);

		/*Update the team details with the new ones*/
		playerUpdate();
		CommonTestUtils.checkIfToastShown(R.string.Player_saveSuccess);
		CommonTestUtils.getDisplayedView(R.id.btnPlayerClear).perform(click());

		/*Select the updated Player from the list*/
		/*Verify that the team name and team short name are same as that which are updated*/
		CommonTestUtils.getDisplayedView(R.id.menu_getPlayerList).perform(click());
		CommonTestUtils.goToViewStarting(nameSecond).perform(click());
		CommonTestUtils.getDisplayedView(R.id.etPlayerName).check(matches(withText(nameSecond)));
		CommonTestUtils.getDisplayedView(R.id.tvPlayerBatStyle).check(matches(withText(batStyleSecond)));
		CommonTestUtils.getDisplayedView(R.id.tvPlayerBowlStyle).check(matches(withText(bowlStyleSecond)));
		if (isWKSecond)
			cbIsWK.check(matches(isChecked()));
		else
			cbIsWK.check(matches(isNotChecked()));

		/*Delete the Player*/
		playerDelete(nameSecond);
		CommonTestUtils.checkIfToastShown(R.string.Player_deleteSuccess);

		/*Verify that the player deleted is not on the list of players*/
		try {
			CommonTestUtils.getDisplayedView(R.id.menu_getPlayerList).perform(click());
			CommonTestUtils.goToViewStarting(nameSecond);
			assert false;
		} catch (IdlingResourceTimeoutException | NoMatchingViewException ex) {
			assert true;
		}
	}

	@Test
	public void testTeamAssign() {
		Activity activity = mActivityTestRule.getActivity();

		String[] teams = {
				"India",
				"Australia",
		};

		CommonTestUtils.clickNavigationMenuItem(2);
		playerDelete(nameSecond);
		playerDelete(nameFirst);

		/*Capture initial count of players in the team*/
		CommonTestUtils.clickNavigationMenuItem(3);
		CommonTestUtils.getDisplayedView(R.id.menu_getTeamList).perform(click());
		CommonTestUtils.goToView(teams[0]).perform(click());
		TextView tvPlayerCount = activity.findViewById(R.id.tvPlayers);
		int playerCount = Integer.parseInt(tvPlayerCount.getText().toString());

		CommonTestUtils.clickNavigationMenuItem(2);
		playerCreate();
		CommonTestUtils.getDisplayedView(R.id.menu_getPlayerList).perform(click());
		CommonTestUtils.goToViewStarting(nameFirst).perform(click());

		CommonTestUtils.getDisplayedView(R.id.btnPlayerManageTeams).perform(click());

		/*Click on each team in the above list to select them*/
		for (String teamName : teams) {
			CommonTestUtils.goToView(teamName).perform(click());
		}

		CommonTestUtils.getDisplayedView(R.id.btnTeamSelectOK).perform(click());
		CommonTestUtils.checkIfToastShown(R.string.Player_saveSuccess);

		/*Verify that the count shown is same as the number of teams selected*/
		CommonTestUtils.getDisplayedView(R.id.tvTeams).check(matches(withText(String.valueOf(teams.length))));
		CommonTestUtils.getDisplayedView(R.id.btnPlayerClear).perform(click());
		CommonTestUtils.getDisplayedView(R.id.menu_getPlayerList).perform(click());
		CommonTestUtils.goToViewStarting(nameFirst).perform(click());
		CommonTestUtils.getDisplayedView(R.id.tvTeams).check(matches(withText(String.valueOf(teams.length))));

		/*Verify that the player count has increased in the Team management*/
		CommonTestUtils.clickNavigationMenuItem(3);
		CommonTestUtils.getDisplayedView(R.id.menu_getTeamList).perform(click());
		CommonTestUtils.goToView(teams[0]).perform(click());
		CommonTestUtils.getDisplayedView(R.id.tvPlayers).check(matches(withText(String.valueOf(playerCount + 1))));

		/*Delete the Player created*/
		CommonTestUtils.clickNavigationMenuItem(2);
		playerDelete(nameFirst);
	}

	private void deleteAllPlayers() {
		Context context = mActivityTestRule.getActivity().getApplicationContext();
		DatabaseHandler dbh = new DatabaseHandler(context);
		dbh.deleteAllRecords(dbh.TBL_PLAYER);
	}

	private void playerCreate() {
		CommonTestUtils.getDisplayedView(R.id.etPlayerName).perform(replaceText(nameFirst), closeSoftKeyboard());

		CommonTestUtils.getDisplayedView(R.id.btnSelectPlayerBatStyle).perform(click());
		CommonTestUtils.goToView(batStyleFirst).perform(click());

		CommonTestUtils.getDisplayedView(R.id.btnSelectPlayerBowlStyle).perform(click());
		CommonTestUtils.goToView(bowlStyleFirst).perform(click());

		if (isWKFirst)
			CommonTestUtils.getDisplayedView(R.id.cbIsPlayerWK).perform(click());

		CommonTestUtils.getDisplayedView(R.id.btnPlayerSave).perform(click());
	}

	private void playerUpdate() {
		CommonTestUtils.getDisplayedView(R.id.menu_getPlayerList).perform(click());
		CommonTestUtils.goToViewStarting(nameFirst).perform(click());

		ViewInteraction etPlayerName = CommonTestUtils.getDisplayedView(R.id.etPlayerName);
		ViewInteraction tvBatStyle = CommonTestUtils.getDisplayedView(R.id.tvPlayerBatStyle);
		ViewInteraction tvBowlStyle = CommonTestUtils.getDisplayedView(R.id.tvPlayerBowlStyle);
		ViewInteraction cbIsWK = CommonTestUtils.getDisplayedView(R.id.cbIsPlayerWK);

		etPlayerName.check(matches(withText(nameFirst)));
		tvBatStyle.check(matches(withText(batStyleFirst)));
		tvBowlStyle.check(matches(withText(bowlStyleFirst)));
		if (isWKFirst)
			cbIsWK.check(matches(isChecked()));
		else
			cbIsWK.check(matches(isNotChecked()));

		etPlayerName.perform(replaceText(nameSecond), closeSoftKeyboard());
		tvBatStyle.perform(click());
		CommonTestUtils.goToView(batStyleSecond).perform(click());
		tvBowlStyle.perform(click());
		CommonTestUtils.goToView(bowlStyleSecond).perform(click());
		if (isWKSecond)
			cbIsWK.perform(click());

		CommonTestUtils.getDisplayedView(R.id.btnPlayerSave).perform(click());
	}

	private void playerDelete(String playerName) {
		CommonTestUtils.getDisplayedView(R.id.menu_getPlayerList).perform(click());

		try {
			CommonTestUtils.goToViewStarting(playerName).perform(click());
			CommonTestUtils.getDisplayedView(R.id.btnPlayerDelete).perform(click());
			CommonTestUtils.getDisplayedView("Yes").perform(click());
		} catch (IdlingResourceTimeoutException | NoMatchingViewException ex) {
			Espresso.pressBack();
			//DO NOTHING
		}
	}
}
