package com.theNewCone.cricketScoreCard;

import android.content.res.Resources;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;

import com.theNewCone.cricketScoreCard.activity.HomeActivity;
import com.theNewCone.cricketScoreCard.utils.CommonTestUtils;
import com.theNewCone.cricketScoreCard.utils.MatchRunInfo;
import com.theNewCone.cricketScoreCard.utils.MatchSimulator;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.action.ViewActions.click;

public class LimitedOversTest {
	@Rule
	public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

	@BeforeClass
	public static void beforeClass() {
		IdlingPolicies.setMasterPolicyTimeout(5, TimeUnit.SECONDS);
		IdlingPolicies.setIdlingResourceTimeout(5, TimeUnit.SECONDS);
	}

	@Test
	public void simulateIndVsWI20Overs() {
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
				"AShai Hope",
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

		MatchRunInfo info = new MatchRunInfo("Ind v WI - 1st T20 - Nov042018", 20, 10, 11);
		info.setTeam1("West Indies", "WI", WI_PLAYERS, WI_PLAYERS[5], WI_PLAYERS[2]);
		info.setTeam2("India", "IND", IND_PLAYERS, IND_PLAYERS[0], IND_PLAYERS[3]);
		info.updateTossDetails("IND", R.string.bowling);

		simulateMatch("csv/templates/20Overs/1.csv", info);
	}

	@Test
	public void simulateIndWVsPakW20Overs() {
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

		MatchRunInfo info = new MatchRunInfo("IndW v PakW WT20 GrpB-5", 20, 10, 11);
		info.setTeam1("India Women", "INDW", INDW_PLAYERS, INDW_PLAYERS[3], INDW_PLAYERS[5]);
		info.setTeam2("Pakistan Women", "PAKW", PAKW_PLAYERS, PAKW_PLAYERS[1], PAKW_PLAYERS[8]);
		info.updateTossDetails("INDW", R.string.bowling);

		simulateMatch("csv/templates/20Overs/2.csv", info);
	}

	@Test
	public void simulateWIvsSA20Overs() {
		closeLoadMatchPopup();

		final String[] WI_PLAYERS = {
				"Chris Gayle",
				"Devon Smith",
				"AMarlon Samuels",
				"AShivnarine Chanderpaul",
				"Dwayne Smith",
				"ARamnaresh Sarwan",
				"Denesh Ramdin",
				"Dwayne Bravo",
				"Daren Powell",
				"ARavi Rampaul",
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

		MatchRunInfo info = new MatchRunInfo("WI v SA 2007 WT20 GrpA-1", 20, 10, 11);
		info.setTeam1("West Indies", "WI", WI_PLAYERS, WI_PLAYERS[5], WI_PLAYERS[6]);
		info.setTeam2("South Africa", "SA", SA_PLAYERS, SA_PLAYERS[0], SA_PLAYERS[4]);
		info.updateTossDetails("SA", R.string.bowling);

		simulateMatch("csv/templates/20Overs/3.csv", info);
	}

	@Test
	public void simulateIndVsPak20Overs() {
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
				"AUmar Gul",
				"Mohammad Asif"
		};

		MatchRunInfo info = new MatchRunInfo("Ind v Pak 2007 WT20 Final", 20, 10, 11);
		info.setTeam1("India", "IND", IND_PLAYERS, IND_PLAYERS[4], IND_PLAYERS[4]);
		info.setTeam2("Pakistan", "PAK", PAK_PLAYERS, PAK_PLAYERS[4], PAK_PLAYERS[2]);
		info.updateTossDetails("IND", R.string.batting);

		simulateMatch("csv/templates/20Overs/4.csv", info);
	}

	@Test
	public void simulateNZvsSL20Overs() {
		closeLoadMatchPopup();

		final String[] NZ_PLAYERS = {
				"Rob Nicol",
				"Martin Guptill",
				"Brendon McCullum",
				"Ross Taylor",
				"Jacob Oram",
				"Nathan McCullum",
				"James Franklin",
				"Kane Williamson",
				"Daniel Vettori",
				"Tim Southee",
				"Kyle Mills"
		};

		final String[] SL_PLAYERS = {
				"Mahela Jayawardene",
				"Tillakaratne Dilshan",
				"Kumar Sangakkara",
				"Jeevan Mendis",
				"Angelo Mathews",
				"Thisara Perera",
				"Lahiru Thirimanne",
				"Nuwan Kulasekara",
				"Lasith Malinga",
				"Ajantha Mendis",
				"Akila Dananjaya"
		};

		MatchRunInfo info = new MatchRunInfo("NZ v SL 2012 WT20 Group1-13", 20, 10, 11);
		info.setTeam1("New Zealand", "NZ", NZ_PLAYERS, NZ_PLAYERS[3], NZ_PLAYERS[2]);
		info.setTeam2("Sri Lanka", "SL", SL_PLAYERS, SL_PLAYERS[0], SL_PLAYERS[2]);
		info.updateTossDetails("NZ", R.string.batting);

		simulateMatch("csv/templates/20Overs/5.csv", info);
	}

	@Test
	public void simulateIndVsWI5Overs() {
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
				"AShai Hope",
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

		MatchRunInfo info = new MatchRunInfo("Ind v WI - 1st T5 - Nov042018", 5, 2, 2, 11);
		info.setTeam1("West Indies", "WI", WI_PLAYERS, WI_PLAYERS[5], WI_PLAYERS[2]);
		info.setTeam2("India", "IND", IND_PLAYERS, IND_PLAYERS[0], IND_PLAYERS[3]);
		info.updateTossDetails("IND", R.string.batting);

		simulateMatch("csv/templates/5Overs/1.csv", info);
	}

	@Test
	public void simulateIndWVsPakW5Overs() {
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

		MatchRunInfo info = new MatchRunInfo("IndW v PakW WT5 GrpB-5", 5, 2, 2, 11);
		info.setTeam1("India Women", "INDW", INDW_PLAYERS, INDW_PLAYERS[3], INDW_PLAYERS[5]);
		info.setTeam2("Pakistan Women", "PAKW", PAKW_PLAYERS, PAKW_PLAYERS[1], PAKW_PLAYERS[8]);
		info.updateTossDetails("INDW", R.string.bowling);

		simulateMatch("csv/templates/5Overs/2.csv", info);
	}

	@Test
	public void simulateWIvsSA5Overs() {
		closeLoadMatchPopup();

		final String[] WI_PLAYERS = {
				"Chris Gayle",
				"Devon Smith",
				"AMarlon Samuels",
				"AShivnarine Chanderpaul",
				"Dwayne Smith",
				"ARamnaresh Sarwan",
				"Denesh Ramdin",
				"Dwayne Bravo",
				"Daren Powell",
				"ARavi Rampaul",
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

		MatchRunInfo info = new MatchRunInfo("WI v SA 2007 WT5 GrpA-1", 5, 2, 2, 11);
		info.setTeam1("West Indies", "WI", WI_PLAYERS, WI_PLAYERS[5], WI_PLAYERS[6]);
		info.setTeam2("South Africa", "SA", SA_PLAYERS, SA_PLAYERS[0], SA_PLAYERS[4]);
		info.updateTossDetails("SA", R.string.bowling);

		simulateMatch("csv/templates/5Overs/3.csv", info);
	}

	@Test
	public void simulateIndVsPak5Overs() {
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
				"AUmar Gul",
				"Mohammad Asif"
		};

		MatchRunInfo info = new MatchRunInfo("Ind v Pak 2007 WT5 Final", 5, 2, 2, 11);
		info.setTeam1("India", "IND", IND_PLAYERS, IND_PLAYERS[4], IND_PLAYERS[4]);
		info.setTeam2("Pakistan", "PAK", PAK_PLAYERS, PAK_PLAYERS[4], PAK_PLAYERS[2]);
		info.updateTossDetails("IND", R.string.batting);

		simulateMatch("csv/templates/5Overs/4.csv", info);
	}

	@Test
	public void simulateNZvsSL5Overs() {
		closeLoadMatchPopup();

		final String[] NZ_PLAYERS = {
				"Rob Nicol",
				"Martin Guptill",
				"Brendon McCullum",
				"Ross Taylor",
				"Jacob Oram",
				"Nathan McCullum",
				"James Franklin",
				"Kane Williamson",
				"Daniel Vettori",
				"Tim Southee",
				"Kyle Mills"
		};

		final String[] SL_PLAYERS = {
				"Mahela Jayawardene",
				"Tillakaratne Dilshan",
				"Kumar Sangakkara",
				"Jeevan Mendis",
				"Angelo Mathews",
				"Thisara Perera",
				"Lahiru Thirimanne",
				"Nuwan Kulasekara",
				"Lasith Malinga",
				"Ajantha Mendis",
				"Akila Dananjaya"
		};

		MatchRunInfo info = new MatchRunInfo("NZ v SL 2012 WT5 Group1-13", 5, 2, 2, 11);
		info.setTeam1("New Zealand", "NZ", NZ_PLAYERS, NZ_PLAYERS[3], NZ_PLAYERS[2]);
		info.setTeam2("Sri Lanka", "SL", SL_PLAYERS, SL_PLAYERS[0], SL_PLAYERS[2]);
		info.updateTossDetails("NZ", R.string.batting);

		simulateMatch("csv/templates/5Overs/5.csv", info);
	}

	private void simulateMatch(String templateFile, MatchRunInfo info) {

		MatchSimulator matchSimulator = new MatchSimulator(mActivityTestRule.getActivity());
		matchSimulator.simulateCSV(templateFile, info);
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
