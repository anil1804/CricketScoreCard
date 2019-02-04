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
	private String matchName, team1Capt, team1WK, team2Capt, team2WK;
	private String team1Name, team2Name, team1ShortName, team2ShortName, tossWonBy;
	private String[] team1Players, team2Players;
	private int choseTo;

	@Rule
	public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

	@BeforeClass
	public static void beforeClass() {
		IdlingPolicies.setMasterPolicyTimeout(5, TimeUnit.SECONDS);
		IdlingPolicies.setIdlingResourceTimeout(5, TimeUnit.SECONDS);
	}

	@Test
	public void simulateIndVsWI() {
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

		matchName = "Ind v WI - 1st T20 - Nov042018";
		team1Players = WI_PLAYERS;
		team2Players = IND_PLAYERS;
		team1Capt = WI_PLAYERS[5];
		team1WK = WI_PLAYERS[2];
		team2Capt = IND_PLAYERS[0];
		team2WK = IND_PLAYERS[3];

		team1Name = "West Indies";
		team2Name = "India";
		team1ShortName = "WI";
		team2ShortName = "IND";
		tossWonBy = "IND";
		choseTo = R.string.bowling;

		simulateCSV("csv/templates/1.csv", 20, 10, 11);
	}

	@Test
	public void simulateIndWVsPakW() {
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

		matchName = "IndW v PakW WT20 GrpB-5";
		team1Players = INDW_PLAYERS;
		team2Players = PAKW_PLAYERS;
		team1Capt = INDW_PLAYERS[3];
		team1WK = INDW_PLAYERS[5];
		team2Capt = PAKW_PLAYERS[1];
		team2WK = PAKW_PLAYERS[8];

		team1Name = "India Women";
		team2Name = "Pakistan Women";
		team1ShortName = "INDW";
		team2ShortName = "PAKW";
		tossWonBy = "INDW";
		choseTo = R.string.bowling;

		simulateCSV("csv/templates/2.csv", 20, 10, 11);
	}

	@Test
	public void simulateWIvsSA() {
		closeLoadMatchPopup();

		final String[] WI_PLAYERS = {
				"Chris Gayle",
				"Devon Smith",
				"Marlon Samuels",
				"AShivnarine Chanderpaul",
				"Dwayne Smith",
				"Ramnaresh Sarwan",
				"Denesh Ramdin",
				"Dwayne Bravo",
				"Daren Powell",
				"Ravi Rampaul",
				"Fidel Edwards"
		};

		final String[] SA_PLAYERS = {
				"Graeme Smith",
				"Herschelle Gibbs",
				"AB de Villiers",
				"Justin Kemp",
				"Mark Boucher",
				"Shaun Pollock",
				"Albie Morkel",
				"Johan van der Wath",
				"Vernon Philander",
				"Makhaya Ntini",
				"Morne Morkel"
		};

		matchName = "WI v SA 2007 WT20 GrpA-1";
		team1Players = WI_PLAYERS;
		team2Players = SA_PLAYERS;
		team1Capt = WI_PLAYERS[5];
		team1WK = WI_PLAYERS[6];
		team2Capt = SA_PLAYERS[0];
		team2WK = SA_PLAYERS[4];

		team1Name = "West Indies";
		team2Name = "South Africa";
		team1ShortName = "WI";
		team2ShortName = "SA";
		tossWonBy = "SA";
		choseTo = R.string.bowling;

		simulateCSV("csv/templates/3.csv", 20, 10, 11);
	}

	@Test
	public void simulateIndVsPak() {
		closeLoadMatchPopup();

		final String[] IND_PLAYERS = {
				"Gautam Gambhir",
				"AYusuf Pathan",
				"Robin Uthappa",
				"AYuvraj Singh",
				"MS Dhoni",
				"Rohit Sharma",
				"Irfan Pathan",
				"Harbhajan Singh",
				"Joginder Sharma",
				"S Sreesanth",
				"RP Singh"
		};

		final String[] PAK_PLAYERS = {
				"Mohammad Hafeez",
				"Imran Nazir",
				"Kamran Akmal",
				"AYounis Khan",
				"Shoaib Malik",
				"Misbah-ul-Haq",
				"Shahid Afridi",
				"AYasir Arafat",
				"Sohail Tanvir",
				"Umar Gul",
				"Mohammad Asif"
		};

		matchName = "Ind v Pak 2007 WT20 Final";
		team1Players = IND_PLAYERS;
		team2Players = PAK_PLAYERS;
		team1Capt = IND_PLAYERS[4];
		team1WK = IND_PLAYERS[4];
		team2Capt = PAK_PLAYERS[2];
		team2WK = PAK_PLAYERS[4];

		team1Name = "India";
		team2Name = "Pakistan";
		team1ShortName = "IND";
		team2ShortName = "PAK";
		tossWonBy = "IND";
		choseTo = R.string.batting;

		simulateCSV("csv/templates/4.csv", 20, 10, 11);
	}

	private void closeLoadMatchPopup() {
		Resources resources = mActivityTestRule.getActivity().getResources();
		try {
			CommonTestUtils.getDisplayedView(resources.getString(R.string.no)).perform(click());
		} catch (NoMatchingViewException ex) {
			//Do Nothing
		}
	}

	/*
	 * T20 Matches
	 * -----------
	 * CSV - 1 : India vs West Indies Nov-04, 2018 T20
	 * CSV - 2 : India Women vs Pakistan Women, World Cup T20, 2018, Group-B, Match-5
	 * CSV - 3 : South Africa vs West Indies, World Cup T20, 2007, Group-A, Match-1
	 * CSV - 4 : India vs Pakistan, World Cup T20, 2007, Final
	 * CSV - 5 : Australia vs Pakistan, World Cup T20, 2010, Semi-Final
	 */

	private void simulateCSV(String templateFile, int maxOvers, int maxWickets, int numPlayers) {
		HomeActivity activity = mActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		maxOvers = maxOvers == 0 ? 20 : maxOvers;
		maxWickets = maxWickets == 0 ? 10 : maxWickets;
		numPlayers = numPlayers == 0 ? 11 : numPlayers;

		CommonTestUtils.getDisplayedView(R.id.btnNewMatch).perform(click());

		CommonTestUtils.getDisplayedView(R.id.etMaxOvers).perform(replaceText(String.valueOf(maxOvers)));
		CommonTestUtils.getDisplayedView(R.id.etMaxWickets).perform(replaceText(String.valueOf(maxWickets)));
		CommonTestUtils.getDisplayedView(R.id.etNumPlayers).perform(replaceText(String.valueOf(numPlayers)));
		CommonTestUtils.getDisplayedView(R.id.etMatchName).perform(replaceText(matchName));

		CommonTestUtils.getDisplayedView(R.id.tvTeam1).perform(click());
		CommonTestUtils.getDisplayedView(team1Name).perform(click());
		CommonTestUtils.selectTeamPlayers(activity, R.id.btnNMSelectTeam1, team1Players);
		CommonTestUtils.getDisplayedView(R.id.tvTeam1Captain).perform(click());
		CommonTestUtils.goToViewStarting(team1Capt).perform(click());
		CommonTestUtils.getDisplayedView(R.id.tvTeam1WK).perform(click());
		CommonTestUtils.goToViewStarting(team1WK).perform(click());

		CommonTestUtils.getDisplayedView(R.id.tvTeam2).perform(click());
		CommonTestUtils.getDisplayedView(team2Name).perform(click());
		CommonTestUtils.selectTeamPlayers(activity, R.id.btnNMSelectTeam2, team2Players);
		CommonTestUtils.getDisplayedView(R.id.tvTeam2Captain).perform(click());
		CommonTestUtils.goToViewStarting(team2Capt).perform(click());
		CommonTestUtils.getDisplayedView(R.id.tvTeam2WK).perform(click());
		CommonTestUtils.goToViewStarting(team2WK).perform(click());

		CommonTestUtils.goToView(resources.getString(R.string.toss)).perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.tossWonBy)).check(matches(isDisplayed()));
		CommonTestUtils.goToView(tossWonBy).perform(click());
		CommonTestUtils.getDisplayedView(resources.getString(R.string.choose)).check(matches(isDisplayed()));
		CommonTestUtils.getDisplayedView(resources.getString(choseTo)).perform(click());
		CommonTestUtils.goToView(resources.getString(R.string.startMatch)).perform(click());

		String team1Name = team1ShortName, team2Name = team2ShortName;
		String[] team1PlayerList = team1Players, team2PlayerList = team2Players;
		if ((team1ShortName.equals(tossWonBy) && choseTo == R.string.bowling)
				|| (team2ShortName.equals(tossWonBy) && choseTo == R.string.batting)) {
			if (choseTo == R.string.batting) {
				team1Name = team2ShortName;
				team2Name = team1ShortName;
				team1PlayerList = team2Players;
				team2PlayerList = team1Players;
			}
		}

		MatchSimulator matchSimulator = new MatchSimulator(activity);
		matchSimulator.startSimulation(templateFile, team1Name, team2Name,
				team1PlayerList, team2PlayerList);
	}
}
