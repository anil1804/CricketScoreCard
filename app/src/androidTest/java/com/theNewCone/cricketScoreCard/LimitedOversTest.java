package com.theNewCone.cricketScoreCard;

import android.content.res.Resources;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;

import com.theNewCone.cricketScoreCard.activity.HomeActivity;
import com.theNewCone.cricketScoreCard.utils.CommonTestUtils;
import com.theNewCone.cricketScoreCard.utils.MatchSimulator;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

public class LimitedOversTest {

	@Rule
	public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

	@BeforeClass
	public static void beforeClass() {
		IdlingPolicies.setMasterPolicyTimeout(5, TimeUnit.SECONDS);
		IdlingPolicies.setIdlingResourceTimeout(5, TimeUnit.SECONDS);
	}

	@Test
	public void simulateIndVsWINov0420181stT20() {
		closeLoadMatchPopup();

		final String[] IND_PLAYERS = {
				"Rohit Sharma",
				"Shikhar Dhawan",
				"Lokesh Rahul",
				"Rishabh Pant",
				"Manish Pandey",
				"Dinesh Karthik",
				"Krunal Pandya",
				"Kuldeep Yadav",
				"Jasprit Bumrah",
				"K Khaleel Ahmed",
				"Umesh Yadav"
		};

		final String[] WI_PLAYERS = {
				"Shai Hope",
				"Denesh Ramdin",
				"Shimron Hetmyer",
				"Kieron Pollard",
				"Darren Bravo",
				"Rovman Powell",
				"Carlos Brathwaite",
				"Fabian Allen",
				"Keemo Paul",
				"Khary Pierre",
				"Oshane Thomas"
		};

		HomeActivity activity = mActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		CommonTestUtils.getDisplayedView(R.id.btnNewMatch).perform(click());

		CommonTestUtils.getDisplayedView(R.id.etMaxOvers).perform(replaceText("20"));
		CommonTestUtils.getDisplayedView(R.id.etMatchName).perform(replaceText("Ind v WI - 1st T20 - Nov042018"));

		CommonTestUtils.getDisplayedView(R.id.tvTeam1).perform(click());
		CommonTestUtils.getDisplayedView("West Indies").perform(click());
		CommonTestUtils.selectTeamByPlayerCount(activity, R.id.btnNMSelectTeam1, WI_PLAYERS);
		CommonTestUtils.getDisplayedView(R.id.tvTeam1Captain).perform(click());
		CommonTestUtils.goToViewStarting(WI_PLAYERS[5]).perform(click());
		CommonTestUtils.getDisplayedView(R.id.tvTeam1WK).perform(click());
		CommonTestUtils.goToViewStarting(WI_PLAYERS[2]).perform(click());

		CommonTestUtils.getDisplayedView(R.id.tvTeam2).perform(click());
		CommonTestUtils.getDisplayedView("India").perform(click());
		CommonTestUtils.selectTeamByPlayerCount(activity, R.id.btnNMSelectTeam2, IND_PLAYERS);
		CommonTestUtils.getDisplayedView(R.id.tvTeam2Captain).perform(click());
		CommonTestUtils.goToViewStarting(IND_PLAYERS[0]).perform(click());
		CommonTestUtils.getDisplayedView(R.id.tvTeam2WK).perform(click());
		CommonTestUtils.goToViewStarting(IND_PLAYERS[3]).perform(click());

		CommonTestUtils.getDisplayedView(resources.getString(R.string.toss)).perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.tossWonBy)).check(matches(isDisplayed()));
		CommonTestUtils.getDisplayedView("IND").perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.choose)).check(matches(isDisplayed()));
		CommonTestUtils.getDisplayedView(resources.getString(R.string.bowling)).perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.startMatch)).perform(click());

		MatchSimulator matchSimulator = new MatchSimulator(activity);
		matchSimulator.startSimulation("csv/templates/1.csv", "WI", "IND",
				WI_PLAYERS, IND_PLAYERS);
	}

	@Test
	public void simulateIndWVsPakW_WCT20GB5() {
		closeLoadMatchPopup();

		final String[] PAKW_PLAYERS = {
				"Ayesha Zafar",
				"Javeria Khan",
				"Umaima Sohail",
				"Bismah Maroof",
				"Nida Dar",
				"Aliya Riaz",
				"Nahida Khan",
				"Sana Mir",
				"Sidra Nawaz",
				"Diana Baig",
				"Anam Amin"
		};

		final String[] INDW_PLAYERS = {
				"Mithali Raj",
				"Smriti Mandhana",
				"Jemimah Rodrigues",
				"Harmanpreet Kaur",
				"Veda Krishnamurthy",
				"Taniya Bhatia",
				"Dayalan Hemalatha",
				"Deepti Sharma",
				"Radha Yadav",
				"Arundhati Reddy",
				"Poonam Yadav"
		};

		HomeActivity activity = mActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		CommonTestUtils.getDisplayedView(R.id.btnNewMatch).perform(click());

		CommonTestUtils.getDisplayedView(R.id.etMaxOvers).perform(replaceText("20"));
		CommonTestUtils.getDisplayedView(R.id.etMatchName).perform(replaceText("IndW v PakW WT20 GrpB-5"));

		CommonTestUtils.getDisplayedView(R.id.tvTeam1).perform(click());
		CommonTestUtils.getDisplayedView("India Women").perform(click());
		CommonTestUtils.selectTeamByPlayerCount(activity, R.id.btnNMSelectTeam1, INDW_PLAYERS);
		CommonTestUtils.getDisplayedView(R.id.tvTeam1Captain).perform(click());
		CommonTestUtils.goToViewStarting(INDW_PLAYERS[3]).perform(click());
		CommonTestUtils.getDisplayedView(R.id.tvTeam1WK).perform(click());
		CommonTestUtils.goToViewStarting(INDW_PLAYERS[5]).perform(click());

		CommonTestUtils.getDisplayedView(R.id.tvTeam2).perform(click());
		CommonTestUtils.getDisplayedView("Pakistan Women").perform(click());
		CommonTestUtils.selectTeamByPlayerCount(activity, R.id.btnNMSelectTeam2, PAKW_PLAYERS);
		CommonTestUtils.getDisplayedView(R.id.tvTeam2Captain).perform(click());
		CommonTestUtils.goToViewStarting(PAKW_PLAYERS[1]).perform(click());
		CommonTestUtils.getDisplayedView(R.id.tvTeam2WK).perform(click());
		CommonTestUtils.goToViewStarting(PAKW_PLAYERS[8]).perform(click());

		CommonTestUtils.goToView(resources.getString(R.string.toss)).perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.tossWonBy)).check(matches(isDisplayed()));
		CommonTestUtils.goToView("INDW").perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.choose)).check(matches(isDisplayed()));
		CommonTestUtils.goToView(resources.getString(R.string.bowling)).perform(click());
		CommonTestUtils.goToView(resources.getString(R.string.startMatch)).perform(click());

		MatchSimulator matchSimulator = new MatchSimulator(activity);
		matchSimulator.startSimulation("csv/templates/2.csv", "PAKW", "INDW",
				PAKW_PLAYERS, INDW_PLAYERS);
	}

	private void closeLoadMatchPopup() {
		Resources resources = mActivityTestRule.getActivity().getResources();
		try {
			CommonTestUtils.getDisplayedView(resources.getString(R.string.no)).perform(click());
		} catch (NoMatchingViewException ex) {
			//Do Nothing
		}
	}
}
