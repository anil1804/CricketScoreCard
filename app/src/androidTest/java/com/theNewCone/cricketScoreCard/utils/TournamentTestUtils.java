package com.theNewCone.cricketScoreCard.utils;

import android.content.res.Resources;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.theNewCone.cricketScoreCard.Constants;
import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.activity.HomeActivity;
import com.theNewCone.cricketScoreCard.enumeration.Stage;
import com.theNewCone.cricketScoreCard.enumeration.TeamEnum;
import com.theNewCone.cricketScoreCard.match.Team;
import com.theNewCone.cricketScoreCard.utils.database.ManageDBData;

import java.util.HashMap;
import java.util.List;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class TournamentTestUtils {

	public static final String[] IND_PLAYERS = {
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

	public static final String[] AUS_PLAYERS = {
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

	public static final String[] NZ_PLAYERS = {
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

	public static void triggerMatch(int matchNumber, MatchRunInfo info, ActivityTestRule<HomeActivity> homeActivityTestRule) {
		HomeActivity activity = homeActivityTestRule.getActivity();
		Resources resources = activity.getResources();

		String matchNumberText = resources.getString(R.string.matchPrefix) + String.valueOf(matchNumber);
		if (CommonTestUtils.checkViewExists(allOf(
				withText("Start"),
				withParent(allOf(
						withId(R.id.clTHScheduleItem),
						hasDescendant(withText(matchNumberText))))))) {
			CommonTestUtils
					.getChild(allOf(withId(R.id.clTHScheduleItem), hasDescendant(withText(matchNumberText))), withText("Start"))
					.perform(click());

			MatchSimulator simulator = new MatchSimulator(activity);
			String csvFile = "csv/templates/5Overs/" + CommonUtils.generateRandomInt(1, 5) + ".csv";
			Log.i(Constants.LOG_TAG, "CSV File being used to simulate match is " + csvFile);

			simulator.simulateCSV(csvFile, info);
		} else {
			Log.i(Constants.LOG_TAG, String.format("Match-%d is already running/complete", matchNumber));
		}
	}

	public static void closeLoadMatchPopup(ActivityTestRule<HomeActivity> homeActivityTestRule) {
		Resources resources = homeActivityTestRule.getActivity().getResources();
		try {
			CommonTestUtils.getDisplayedView(resources.getString(R.string.no)).perform(click());
		} catch (NoMatchingViewException ex) {
			//Do Nothing
		}
	}

	public static String getFullTeamName(ActivityTestRule testRule, String teamName) {
		HashMap<String, String> teamNames = new HashMap<>();
		List<Team> allTeams = new ManageDBData(testRule.getActivity().getApplicationContext()).addTeams(TeamEnum.ALL);
		for (Team team : allTeams) {
			teamNames.put(team.getShortName().toUpperCase(), team.getName());
		}

		return teamNames.get(teamName.toUpperCase());
	}

	public static String[] getPlayingTeams(ActivityTestRule testRule, Stage stage, int matchNumber) {
		String[] teamsPlaying = new String[2];

		RecyclerView rcvGroupList = testRule.getActivity().findViewById(R.id.rcvScheduleList);
		if (rcvGroupList != null && rcvGroupList.getLayoutManager() != null) {

			int groupIndex = stage == Stage.FINAL ? 1 : 0;

			View groupView = rcvGroupList.getLayoutManager().findViewByPosition(groupIndex);
			if (groupView != null) {
				RecyclerView rcvMatchList = groupView.findViewById(R.id.rcvGroupTeamList);
				if (rcvMatchList != null && rcvMatchList.getLayoutManager() != null) {
					View matchView = rcvMatchList.getLayoutManager().findViewByPosition(matchNumber - 1);
					if (matchView != null) {
						String matchName = ((TextView) matchView.findViewById(R.id.tvScheduleVersus)).getText().toString();
						if (!matchName.trim().equals("")) {
							teamsPlaying = matchName.split("vs");
						}
					}
				}
			}
		}

		return teamsPlaying;
	}
}
