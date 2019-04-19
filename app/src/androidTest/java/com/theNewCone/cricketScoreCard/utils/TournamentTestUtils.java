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
import com.theNewCone.cricketScoreCard.enumeration.Stage;
import com.theNewCone.cricketScoreCard.enumeration.TeamEnum;
import com.theNewCone.cricketScoreCard.enumeration.TournamentFormat;
import com.theNewCone.cricketScoreCard.enumeration.TournamentStageType;
import com.theNewCone.cricketScoreCard.match.Team;
import com.theNewCone.cricketScoreCard.utils.database.ManageDBData;
import com.theNewCone.cricketScoreCard.utils.database.TournamentDBHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class TournamentTestUtils {

	private static void triggerMatch(MatchRunInfo info, String matchTag, String currentRoundText, boolean isEliminator) {
		Activity currentActivity = CommonTestUtils.getCurrentActivity();

		currentRoundText = currentRoundText.toUpperCase();

		if (CommonTestUtils.checkViewExists(allOf(
				withParent(
						allOf(
								hasDescendant(withText(matchTag)),
								isDescendantOfA(hasDescendant(allOf(withId(R.id.tvGroupName), withText(currentRoundText))))
						)),
				withText("Start")
		))) {

			CommonTestUtils
					.getView(
							allOf(
									hasDescendant(withText(matchTag)),
									isDescendantOfA(hasDescendant(allOf(withId(R.id.tvGroupName), withText(currentRoundText))))
							),
							withText("Start")
					)
					.perform(scrollTo()).perform(click());

			MatchSimulator simulator = new MatchSimulator(currentActivity);
			int matchNumber = isEliminator ? CommonUtils.generateRandomInt(1, 5) : CommonUtils.generateRandomInt(1, 6);
			String csvFile = "csv/templates/5Overs/" + matchNumber + ".csv";
			Log.i(Constants.LOG_TAG, "CSV File being used to simulate match is " + csvFile);

			simulator.simulateCSV(csvFile, info);
		} else {
			Log.i(Constants.LOG_TAG, String.format("%s is already running/complete", matchTag));
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

	public static void goHome() {
		CommonTestUtils.getDisplayedView(R.id.menu_home).perform(click());
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

	public static void triggerMatch(TeamEnum team1Enum, TeamEnum team2Enum, String matchTag, Stage stage) {
		Activity currentActivity = CommonTestUtils.getCurrentActivity();
		Resources resources = currentActivity.getResources();

		if (team1Enum != null && team2Enum != null) {
			String team1 = team1Enum.toString();
			String team2 = team2Enum.toString();

			String tossWonBy = (new Random().nextInt(2)) == 0 ? team1.toUpperCase() : team2.toUpperCase();
			int choseTo = (new Random().nextInt(2)) == 0 ? R.string.batting : R.string.bowling;

			MatchRunInfo info = new MatchRunInfo(true);
			info.updateTossDetails(tossWonBy, choseTo);

			info.setTeam1(getTeamInfo(team1));
			info.setTeam2(getTeamInfo(team2));

			Log.i(Constants.LOG_TAG,
					String.format("Stage: %s, %s, %s: %s won the toss and chose to %s",
							stage.enumString(), matchTag, (team1 + " vs " + team2),
							tossWonBy, resources.getString(choseTo)));

			String roundTag = stage == Stage.NONE ? resources.getString(R.string.matches) : stage.enumString();
			TournamentTestUtils.triggerMatch(info, matchTag, roundTag, isAnEliminator(stage));
		}
	}

	private static TeamInfo getTeamInfo(String team) {
		String teamFull = TournamentTestUtils.getFullTeamName(team);
		TeamInfo teamInfo = new TeamInfo(new Team(teamFull, team));
		switch (TeamEnum.valueOf(team.toUpperCase())) {
			case AUS:
				teamInfo.setPlayerInfo(TeamPlayers.AUS_PLAYERS, TeamPlayers.AUS_PLAYERS[0], TeamPlayers.AUS_PLAYERS[5]);
				break;
			case NZ:
				teamInfo.setPlayerInfo(TeamPlayers.NZ_PLAYERS, TeamPlayers.NZ_PLAYERS[3], TeamPlayers.NZ_PLAYERS[2]);
				break;
			case IND:
				teamInfo.setPlayerInfo(TeamPlayers.IND_PLAYERS, TeamPlayers.IND_PLAYERS[3], TeamPlayers.IND_PLAYERS[4]);
				break;
			case PAK:
				teamInfo.setPlayerInfo(TeamPlayers.PAK_PLAYERS, TeamPlayers.PAK_PLAYERS[4], TeamPlayers.PAK_PLAYERS[2]);
				break;
			case WI:
				teamInfo.setPlayerInfo(TeamPlayers.WI_PLAYERS, TeamPlayers.WI_PLAYERS[6], TeamPlayers.WI_PLAYERS[2]);
				break;
			case SA:
				teamInfo.setPlayerInfo(TeamPlayers.SA_PLAYERS, TeamPlayers.SA_PLAYERS[0], TeamPlayers.SA_PLAYERS[4]);
				break;
			case SL:
				teamInfo.setPlayerInfo(TeamPlayers.SL_PLAYERS, TeamPlayers.SL_PLAYERS[0], TeamPlayers.SL_PLAYERS[2]);
				break;
			case INDW:
				teamInfo.setPlayerInfo(TeamPlayers.INDW_PLAYERS, TeamPlayers.INDW_PLAYERS[3], TeamPlayers.INDW_PLAYERS[5]);
				break;
			case PAKW:
				teamInfo.setPlayerInfo(TeamPlayers.PAKW_PLAYERS, TeamPlayers.PAKW_PLAYERS[1], TeamPlayers.PAKW_PLAYERS[8]);
				break;
			case ENG:
				teamInfo.setPlayerInfo(TeamPlayers.ENG_PLAYERS, TeamPlayers.ENG_PLAYERS[3], TeamPlayers.ENG_PLAYERS[5]);
				break;
			case BAN:
				teamInfo.setPlayerInfo(TeamPlayers.BAN_PLAYERS, TeamPlayers.BAN_PLAYERS[3], TeamPlayers.BAN_PLAYERS[9]);
				break;
			case AFG:
				teamInfo.setPlayerInfo(TeamPlayers.AFG_PLAYERS, TeamPlayers.AFG_PLAYERS[1], TeamPlayers.AFG_PLAYERS[7]);
				break;
		}

		return teamInfo;
	}

	public static void triggerMatch(int groupIndex, int matchNumber, TournamentFormat format, Stage stage) {
		String[] teamsPlaying = TournamentTestUtils.getPlayingTeams(groupIndex, matchNumber);
		String matchTag = new TournamentUtils(CommonTestUtils.getCurrentActivity())
				.getScheduleMatchTag(format, stage, groupIndex, matchNumber);
		triggerMatch(TeamEnum.valueOf(teamsPlaying[0]), TeamEnum.valueOf(teamsPlaying[1]), matchTag, stage);
	}

	public static void deleteTournament(String tournamentName, Context context) {
		new TournamentDBHandler(context).deleteTournament(tournamentName);
	}

	public static int getTSButtonID(TournamentStageType stageType) {
		int buttonID = 0;
		switch (stageType) {
			case KNOCK_OUT:
				buttonID = R.id.rbTSKnockOut;
				break;

			case NONE:
				buttonID = R.id.rbTSNone;
				break;

			case QUALIFIER:
				buttonID = R.id.rbTSQualifiers;
				break;

			case SUPER_FOUR:
				buttonID = R.id.rbTSSuperFourStage;
				break;

			case SUPER_SIX:
				buttonID = R.id.rbTSSuperSixStage;
				break;
		}
		return buttonID;
	}

	private static boolean isAnEliminator(Stage stage) {
		boolean isEliminator = false;

		switch (stage) {
			case ROUND_1:
			case ROUND_2:
			case ROUND_3:
			case QUARTER_FINAL:
			case SEMI_FINAL:
			case QUALIFIER:
			case ELIMINATOR_1:
			case ELIMINATOR_2:
			case FINAL:
				isEliminator = true;
				break;
		}

		return isEliminator;
	}
}
