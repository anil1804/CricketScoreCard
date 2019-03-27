package com.theNewCone.cricketScoreCard;

import android.app.Activity;
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

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TeamManageTest {

	private final String nameFirst = "India Blue";
	private final String shortNameFirst = "IND-B";
	private final String nameSecond = "India Red";
	private final String shortNameSecond = "IND-R";
	@Rule
	public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

	@BeforeClass
	public static void beforeClass() {
		IdlingPolicies.setMasterPolicyTimeout(5, TimeUnit.SECONDS);
		IdlingPolicies.setIdlingResourceTimeout(5, TimeUnit.SECONDS);
	}

	@Test
	public void validateInputData() {
		CommonTestUtils.loadDBData();
		ViewInteraction btnManageTeam = CommonTestUtils.getDisplayedView(R.id.btnManageTeam);
		btnManageTeam.perform(click());

		//Team Name Empty
		CommonTestUtils.getDisplayedView(R.id.btnSaveTeam).perform(click());
		CommonTestUtils.checkIfToastShown(R.string.Team_enterValidTeamName);

		//Team Name Less than 5 characters
		CommonTestUtils.getDisplayedView(R.id.etTeamName).perform(replaceText("IND"), closeSoftKeyboard());
		CommonTestUtils.getDisplayedView(R.id.btnSaveTeam).perform(click());
		CommonTestUtils.checkIfToastShown(R.string.Team_enterValidTeamName);

		//Team Short Name Empty
		CommonTestUtils.getDisplayedView(R.id.etTeamName).perform(replaceText("India"), closeSoftKeyboard());
		CommonTestUtils.getDisplayedView(R.id.btnSaveTeam).perform(click());
		CommonTestUtils.checkIfToastShown(R.string.Team_enterValidTeamShortName);

		//Team Name Less than 2 characters
		CommonTestUtils.getDisplayedView(R.id.etTeamName).perform(replaceText("India"), closeSoftKeyboard());
		CommonTestUtils.getDisplayedView(R.id.etTeamShortName).perform(replaceText("I"), closeSoftKeyboard());
		CommonTestUtils.getDisplayedView(R.id.btnSaveTeam).perform(click());
		CommonTestUtils.checkIfToastShown(R.string.Team_enterValidTeamShortName);
	}

	@Test
	public void manageTeams() {
		CommonTestUtils.loadDBData();
		CommonTestUtils.getDisplayedView(R.id.btnManageTeam).perform(click());
		teamDelete(nameFirst);
		teamDelete(nameSecond);

		/*Create the Team*/
		teamCreate();
		CommonTestUtils.checkIfToastShown(R.string.Team_saveSuccess);
		/*Check the team is visible in the list of teams and click the same*/
		CommonTestUtils.getDisplayedView(R.id.menu_getTeamList).perform(click());
		CommonTestUtils.goToView(nameFirst).perform(click());
		/*Verify that the team name and team short name are same as that which are created*/
		ViewInteraction etTeamName = CommonTestUtils.getDisplayedView(R.id.etTeamName);
		ViewInteraction etTeamShortName = CommonTestUtils.getDisplayedView(R.id.etTeamShortName);
		etTeamName.check(matches(withText(nameFirst)));
		etTeamShortName.check(matches(withText(shortNameFirst)));

		/*Test for duplicate creation*/
		CommonTestUtils.getDisplayedView(R.id.btnTeamClear).perform(click());
		teamCreate();
		CommonTestUtils.checkIfToastShown(R.string.Team_selectDifferentTeamName);


		/*Update the team details with the new ones*/
		teamUpdate();
		CommonTestUtils.checkIfToastShown(R.string.Team_saveSuccess);
		/*Update the team details with the new ones*/
		CommonTestUtils.getDisplayedView(R.id.menu_getTeamList).perform(click());
		CommonTestUtils.goToView(nameSecond).perform(click());
		/*Verify that the team name and team short name are same as that which are updated*/
		CommonTestUtils.getDisplayedView(R.id.etTeamName).check(matches(withText(nameSecond)));
		CommonTestUtils.getDisplayedView(R.id.etTeamShortName).check(matches(withText(shortNameSecond)));


		/*Delete the team*/
		teamDelete(nameSecond);
		CommonTestUtils.checkIfToastShown(R.string.Team_deleteSuccess);
		/*Verify that the team deleted is not on the list of teams*/
		try {
			ViewInteraction menu_getTeamList = CommonTestUtils.getDisplayedView(R.id.menu_getTeamList);
			menu_getTeamList.perform(click());
			CommonTestUtils.getDisplayedView(nameSecond);
			assert false;
		} catch (IdlingResourceTimeoutException | NoMatchingViewException ex) {
			assert true;
		}
	}

	@Test
	public void teamPlayerAssign() {
		CommonTestUtils.loadDBData();
		Activity activity = mActivityTestRule.getActivity();

		CommonTestUtils.getDisplayedView(R.id.btnManageTeam).perform(click());
		teamDelete(nameFirst);
		teamDelete(nameSecond);

		/*Top Menu - Team not selected*/
		CommonTestUtils.getDisplayedView(R.id.menu_updatePlayers).perform(click());
		CommonTestUtils.checkIfToastShown(R.string.Team_selectCreateTeam);

		/*Edit Button - Team not selected*/
		CommonTestUtils.getDisplayedView(R.id.btnTeamManagePlayers).perform(click());
		CommonTestUtils.checkIfToastShown(R.string.Team_selectCreateTeam);

		/*Player Assignment*/
		String[] players = {
				"Aaron Finch",
				"AB de Villiers"
		};
		//Get Current number of teams assigned to
		CommonTestUtils.clickNavigationMenuItem(2);
		CommonTestUtils.getDisplayedView(R.id.menu_getPlayerList).perform(click());
		CommonTestUtils.goToViewStarting(players[0]).perform(click());
		int teamCount = Integer.parseInt(((TextView) activity.findViewById(R.id.tvTeams)).getText().toString());

		CommonTestUtils.clickNavigationMenuItem(3);
		teamCreate();
		CommonTestUtils.getDisplayedView(R.id.menu_getTeamList).perform(click());
		CommonTestUtils.goToView(nameFirst).perform(click());
		CommonTestUtils.getDisplayedView(R.id.btnTeamManagePlayers).perform(click());

		/*Click on each player in the above list to select them*/
		for (String playerName : players) {
			CommonTestUtils.goToViewStarting(playerName)
					.perform(click());
		}
		CommonTestUtils.getDisplayedView(R.id.btnSelPlayerOK).perform(click());
		CommonTestUtils.checkIfToastShown(R.string.Team_saveSuccess);

		/*Verify that the count shown is same as the number of players selected*/
		CommonTestUtils.getDisplayedView(R.id.tvPlayers).check(matches(withText(String.valueOf(players.length))));
		CommonTestUtils.getDisplayedView(R.id.btnTeamClear).perform(click());
		CommonTestUtils.getDisplayedView(R.id.menu_getTeamList).perform(click());
		CommonTestUtils.goToView(nameFirst).perform(click());
		CommonTestUtils.getDisplayedView(R.id.tvPlayers).check(matches(withText(String.valueOf(players.length))));

		//Check Team Count increased by 1
		CommonTestUtils.clickNavigationMenuItem(2);
		CommonTestUtils.getDisplayedView(R.id.menu_getPlayerList).perform(click());
		CommonTestUtils.goToViewStarting(players[0]).perform(click());
		CommonTestUtils.getDisplayedView(R.id.tvTeams).check(matches(withText(String.valueOf(teamCount + 1))));

		CommonTestUtils.clickNavigationMenuItem(3);
		teamDelete(nameFirst);
	}

	private void teamCreate() {
		ViewInteraction etTeamName = CommonTestUtils.getDisplayedView(R.id.etTeamName);
		etTeamName.perform(replaceText(nameFirst), closeSoftKeyboard());

		ViewInteraction etTeamShortName = CommonTestUtils.getDisplayedView(R.id.etTeamShortName);
		etTeamShortName.perform(replaceText(shortNameFirst), closeSoftKeyboard());

		ViewInteraction btnSaveTeam = CommonTestUtils.getDisplayedView(R.id.btnSaveTeam);
		btnSaveTeam.perform(click());
	}

	private void teamUpdate() {
		ViewInteraction menu_getTeamList = CommonTestUtils.getDisplayedView(R.id.menu_getTeamList);
		menu_getTeamList.perform(click());

		ViewInteraction rcvItem = CommonTestUtils.getDisplayedView(nameFirst);
		rcvItem.perform(click());

		ViewInteraction etTeamName = CommonTestUtils.getDisplayedView(R.id.etTeamName);
		ViewInteraction etTeamShortName = CommonTestUtils.getDisplayedView(R.id.etTeamShortName);

		etTeamName.check(matches(withText(nameFirst)));
		etTeamShortName.check(matches(withText(shortNameFirst)));

		etTeamName.perform(replaceText(nameSecond));
		etTeamShortName.perform(replaceText(shortNameSecond));

		ViewInteraction btnSaveTeam = CommonTestUtils.getDisplayedView(R.id.btnSaveTeam);
		btnSaveTeam.perform(click());
	}

	private void teamDelete(String teamName) {
		ViewInteraction menu_getTeamList = CommonTestUtils.getDisplayedView(R.id.menu_getTeamList);
		menu_getTeamList.perform(click());

		try {
			CommonTestUtils.getDisplayedView(teamName).perform(click());
			CommonTestUtils.getDisplayedView(R.id.btnDeleteTeam).perform(click());
			CommonTestUtils.getDisplayedView("Yes").perform(click());
		} catch (IdlingResourceTimeoutException | NoMatchingViewException ex) {
			Espresso.pressBack();
			//DO NOTHING
		}
	}
}
