package com.theNewCone.cricketScoreCard.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.test.espresso.NoMatchingViewException;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.theNewCone.cricketScoreCard.Constants;
import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.enumeration.TeamEnum;
import com.theNewCone.cricketScoreCard.match.Team;
import com.theNewCone.cricketScoreCard.utils.database.ManageDBData;
import com.theNewCone.cricketScoreCard.utils.database.TournamentDBHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class TournamentTestUtils {

	private static final String[] IND_PLAYERS = {
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

	private static final String[] AUS_PLAYERS = {
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

	private static final String[] NZ_PLAYERS = {
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

	private static void triggerMatch(int matchNumber, MatchRunInfo info, String matchNumberText, String currentRoundText) {
		Activity currentActivity = CommonTestUtils.getCurrentActivity();
		Resources resources = currentActivity.getResources();

		currentRoundText = currentRoundText.toUpperCase();
		matchNumberText = matchNumberText == null
				? resources.getString(R.string.matchPrefix) + String.valueOf(matchNumber)
				: matchNumberText;

		if (CommonTestUtils.checkViewExists(allOf(
				withParent(
						allOf(
								hasDescendant(withText(matchNumberText)),
								isDescendantOfA(withChild(withText(currentRoundText)))
						)),
				withText("Start")
		))) {

			CommonTestUtils
					.getView(
							allOf(
									hasDescendant(withText(matchNumberText)),
									isDescendantOfA(withChild(withText(currentRoundText)))
							),
							withText("Start")
					)
					.perform(click());

			MatchSimulator simulator = new MatchSimulator(currentActivity);
			String csvFile = "csv/templates/5Overs/" + CommonUtils.generateRandomInt(1, 5) + ".csv";
			Log.i(Constants.LOG_TAG, "CSV File being used to simulate match is " + csvFile);

			simulator.simulateCSV(csvFile, info);
		} else {
			Log.i(Constants.LOG_TAG, String.format("Match-%d is already running/complete", matchNumber));
		}
	}

	public static void closeLoadMatchPopup() {
		Activity currentActivity = CommonTestUtils.getCurrentActivity();
		Resources resources = currentActivity.getResources();
		try {
			CommonTestUtils.getDisplayedView(resources.getString(R.string.no)).perform(click());
		} catch (NoMatchingViewException ex) {
			//Do Nothing
		}
	}

	private static String getFullTeamName(String teamName) {
		Activity currentActivity = CommonTestUtils.getCurrentActivity();

		HashMap<String, String> teamNames = new HashMap<>();
		List<Team> allTeams = new ManageDBData(currentActivity.getApplicationContext()).addTeams(TeamEnum.ALL);
		for (Team team : allTeams) {
			teamNames.put(team.getShortName().toUpperCase(), team.getName());
		}

		return teamNames.get(teamName.toUpperCase());
	}

	private static String[] getPlayingTeams(int groupIndex, int matchNumber) {
		Activity currentActivity = CommonTestUtils.getCurrentActivity();

		String[] teamsPlaying = null;

		RecyclerView rcvGroupList = currentActivity.findViewById(R.id.rcvScheduleList);
		if (rcvGroupList != null && rcvGroupList.getLayoutManager() != null) {
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

		for (int i = 0; teamsPlaying != null && i < teamsPlaying.length; i++)
			teamsPlaying[i] = teamsPlaying[i].trim();

		return teamsPlaying;
	}

	public static void triggerMatch(TeamEnum team1Enum, TeamEnum team2Enum, int matchNumber, String matchNumberText, String currentRoundText) {
		Activity currentActivity = CommonTestUtils.getCurrentActivity();
		Resources resources = currentActivity.getResources();

		if (team1Enum != null && team2Enum != null) {
			String team1 = team1Enum.toString();
			String team2 = team2Enum.toString();
			String team1Full = TournamentTestUtils.getFullTeamName(team1);
			String team2Full = TournamentTestUtils.getFullTeamName(team2);

			String tossWonBy = (new Random().nextInt(2)) == 0 ? team1.toUpperCase() : team2.toUpperCase();
			int choseTo = (new Random().nextInt(2)) == 0 ? R.string.batting : R.string.bowling;

			MatchRunInfo info = new MatchRunInfo(true);
			info.updateTossDetails(tossWonBy, choseTo);

			switch (TeamEnum.valueOf(team1.toUpperCase())) {
				case AUS:
					info.setTeam1(team1Full, team1, TournamentTestUtils.AUS_PLAYERS, TournamentTestUtils.AUS_PLAYERS[0], TournamentTestUtils.AUS_PLAYERS[5]);
					break;
				case NZ:
					info.setTeam1(team1Full, team1, TournamentTestUtils.NZ_PLAYERS, TournamentTestUtils.NZ_PLAYERS[3], TournamentTestUtils.NZ_PLAYERS[2]);
					break;
				case IND:
					info.setTeam1(team1Full, team1, TournamentTestUtils.IND_PLAYERS, TournamentTestUtils.IND_PLAYERS[3], TournamentTestUtils.IND_PLAYERS[4]);
					break;
			}

			switch (TeamEnum.valueOf(team2.toUpperCase())) {
				case AUS:
					info.setTeam2(team2Full, team2, TournamentTestUtils.AUS_PLAYERS, TournamentTestUtils.AUS_PLAYERS[0], TournamentTestUtils.AUS_PLAYERS[5]);
					break;
				case NZ:
					info.setTeam2(team2Full, team2, TournamentTestUtils.NZ_PLAYERS, TournamentTestUtils.NZ_PLAYERS[3], TournamentTestUtils.NZ_PLAYERS[2]);
					break;
				case IND:
					info.setTeam2(team2Full, team2, TournamentTestUtils.IND_PLAYERS, TournamentTestUtils.IND_PLAYERS[3], TournamentTestUtils.IND_PLAYERS[4]);
					break;
			}

			Log.i(Constants.LOG_TAG, String.format("Match-%d, %s: %s won the toss and chose to %s", matchNumber, (team1 + " vs " + team2), tossWonBy, resources.getString(choseTo)));

			TournamentTestUtils.triggerMatch(matchNumber, info, matchNumberText, currentRoundText);
		}
	}

	public static void triggerMatch(int groupIndex, int matchNumber, String matchNumberText, String currentRoundText) {
		String[] teamsPlaying = TournamentTestUtils.getPlayingTeams(groupIndex, matchNumber);
		triggerMatch(TeamEnum.valueOf(teamsPlaying[0]), TeamEnum.valueOf(teamsPlaying[1]), matchNumber, matchNumberText, currentRoundText);
	}

	public static void deleteTournament(String tournamentName, Context context) {
		new TournamentDBHandler(context).deleteTournament(tournamentName);
	}
}
