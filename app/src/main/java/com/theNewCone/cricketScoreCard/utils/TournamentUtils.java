package com.theNewCone.cricketScoreCard.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.comparator.GroupComparator;
import com.theNewCone.cricketScoreCard.comparator.PointsDataComparator;
import com.theNewCone.cricketScoreCard.enumeration.MatchResult;
import com.theNewCone.cricketScoreCard.enumeration.Stage;
import com.theNewCone.cricketScoreCard.enumeration.TournamentFormat;
import com.theNewCone.cricketScoreCard.enumeration.TournamentStageType;
import com.theNewCone.cricketScoreCard.match.CricketCard;
import com.theNewCone.cricketScoreCard.match.CricketCardUtils;
import com.theNewCone.cricketScoreCard.match.Team;
import com.theNewCone.cricketScoreCard.tournament.Group;
import com.theNewCone.cricketScoreCard.tournament.MatchInfo;
import com.theNewCone.cricketScoreCard.tournament.PointsData;
import com.theNewCone.cricketScoreCard.tournament.Tournament;
import com.theNewCone.cricketScoreCard.utils.database.GroupsDBHandler;
import com.theNewCone.cricketScoreCard.utils.database.MatchDBHandler;
import com.theNewCone.cricketScoreCard.utils.database.MatchInfoDBHandler;
import com.theNewCone.cricketScoreCard.utils.database.PointsDataDBHandler;
import com.theNewCone.cricketScoreCard.utils.database.TournamentDBHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TournamentUtils {
	private List<Team> unselectedTeams = new ArrayList<>();
	private final Context context;

	public TournamentUtils(Context context) {
		this.context = context;
	}

	private Group group;
	private Tournament tournament;

	@NonNull
	public String getScheduleMatchTag(TournamentFormat format, Stage stage, int groupNumber, int matchNumber) {
		String matchTag = "";

		if (format == TournamentFormat.GROUPS) {
			matchTag = stage.getTag() + groupNumber + "-";
		} else if (stage == Stage.QUALIFIER
				|| stage == Stage.ELIMINATOR_1
				|| stage == Stage.ELIMINATOR_2
				|| stage == Stage.SUPER_FOUR
				|| stage == Stage.SUPER_SIX) {
			matchTag = stage.getTag() + "-";
		}

		matchTag = matchTag + context.getResources().getString(R.string.matchPrefix) + matchNumber;
		return matchTag;
	}

	public Group createSchedule(@NonNull Group theGroup, TournamentFormat format, Stage stage) {

		group = theGroup;
		group.clearSchedule();

		switch (format) {
			case ROUND_ROBIN:
				createRoundRobinSchedule(stage);
				break;

			case BILATERAL:
				createBilateralSchedule();
				break;

			case GROUPS:
				createGroupSchedule(stage);
				break;

			case KNOCK_OUT:
				createKnockOutSchedule(stage, false);
				break;
		}

		return group;
	}

	private void createSchedule(Stage stage) {
		group.clearSchedule();

		switch (stage) {
			case SUPER_SIX:
			case SUPER_FOUR:
				group = createRoundRobinSchedule(stage);
				break;

			case ROUND_1:
			case ELIMINATOR_1:
			case ELIMINATOR_2:
			case QUALIFIER:
			case FINAL:
				group = createKnockOutSchedule(stage, false);
				break;

			case ROUND_2:
			case ROUND_3:
			case QUARTER_FINAL:
			case SEMI_FINAL:
				group = createKnockOutSchedule(stage, true);
				break;
		}
	}

	private Group createRoundRobinSchedule(Stage stage) {
		int matchesPerTeamPerRound = group.getTeams().length - 1;
		int matchNumber = 1;

		for (int a = 0; a < group.getNumRounds(); a++) {
			createRoundRobinSchedule(stage, matchesPerTeamPerRound, matchNumber);
			matchNumber += group.getMatchInfoList().size();
		}

		return group;
	}

	private void createBilateralSchedule() {
		for (int a = 0; a < group.getNumRounds(); a++) {
			group.updateMatchInfo(new MatchInfo(a + 1, group.getGroupNumber(), group.getName(), Stage.NONE,
					group.getTeams()[0], group.getTeams()[1]));
		}
	}

	private void createGroupSchedule(Stage stage) {
		int matchesPerTeamPerRound = group.getTeams().length - 1;
		int matchNumber = 1;

		for (int a = 0; a < group.getNumRounds(); a++) {
			createRoundRobinSchedule(stage, matchesPerTeamPerRound, matchNumber);
			matchNumber += group.getMatchInfoList().size();
		}
	}

	private Group createKnockOutSchedule(Stage stage, boolean isRankBased) {
		unselectedTeams.clear();
		unselectedTeams.addAll(Arrays.asList(group.getTeams()));

		int totalMatches = group.getMatchesPerRound();
		for (int i = 0; i < totalMatches; i++) {
			Team team1, team2;
			if (isRankBased) {
				team1 = unselectedTeams.get(i);
				team2 = unselectedTeams.get((2 * totalMatches) - 1 - i);
			} else {
				team1 = getNextTeam(null);
				team2 = getNextTeam(team1);
			}

			MatchInfo matchInfo = new MatchInfo((i + 1), group.getGroupNumber(), group.getName(),
					stage, team1, team2);
			group.updateMatchInfo(matchInfo);
		}

		return group;
	}

	private void createRoundRobinSchedule(Stage stage, int matchesPerTeamPerRound, int matchNumber) {
		SparseArray<List<Integer>> groupMatches = new SparseArray<>();

		for (int i = 0; i < group.getMatchesPerRound(); i++) {
			unselectedTeams.clear();
			for (Team team : group.getTeams()) {
				List<Integer> teamsMatches = groupMatches.get(team.getId());
				if (teamsMatches == null || teamsMatches.size() < matchesPerTeamPerRound) {
					unselectedTeams.add(team);
				}
			}
			Team team1 = getNextTeam(null);

			/* Get the teams are paired with Team-1 and exclude them from being paired again */
			List<Integer> scheduledWithTeam1 = groupMatches.get(team1.getId());
			if (scheduledWithTeam1 == null)
				scheduledWithTeam1 = new ArrayList<>();

			List<Integer> excludeTeamIDs = new ArrayList<>(scheduledWithTeam1);
			excludeTeamIDs.add(team1.getId());
			Team team2 = selectNextTeam(excludeTeamIDs);

			MatchInfo matchInfo = new MatchInfo(matchNumber++, group.getGroupNumber(), group.getName(),
					stage, team1, team2);
			group.updateMatchInfo(matchInfo);

			/*Get List of teams paired with Team-2*/
			List<Integer> scheduledWithTeam2 = groupMatches.get(team2.getId());
			if (scheduledWithTeam2 == null)
				scheduledWithTeam2 = new ArrayList<>();

			/* Added Team-1 and Team-2 as already paired teams */
			scheduledWithTeam1.add(team2.getId());
			scheduledWithTeam2.add(team1.getId());
			groupMatches.put(team1.getId(), scheduledWithTeam1);
			groupMatches.put(team2.getId(), scheduledWithTeam2);
		}
	}

	private Team getNextTeam(Team team) {
		List<Integer> excludeTeamIDs = null;
		if (team != null) {
			excludeTeamIDs = new ArrayList<>();
			excludeTeamIDs.add(team.getId());
		}

		return selectNextTeam(excludeTeamIDs);
	}

	private Team selectNextTeam(List<Integer> excludeTeamIDs) {
		int nextTeamIndex = unselectedTeams.size() > 1 ? new Random().nextInt(unselectedTeams.size()) : 0;

		Team team = unselectedTeams.get(nextTeamIndex);

		if (excludeTeamIDs != null && team != null) {
			while (excludeTeamIDs.contains(team.getId())) {
				nextTeamIndex = new Random().nextInt(unselectedTeams.size());
				team = unselectedTeams.get(nextTeamIndex);
			}
		}

		unselectedTeams.remove(nextTeamIndex);

		return team;
	}

	public Tournament createInitialGroups(Tournament theTournament) {
		tournament = theTournament;
		unselectedTeams.clear();
		unselectedTeams.addAll(Arrays.asList(tournament.getTeams()));

		clearCurrentGroups(tournament);

		Group group;
		Stage groupStage;
		switch (tournament.getFormat()) {
			case ROUND_ROBIN:
				groupStage = Stage.ROUND_ROBIN;
				group = new Group(1, tournament.getTeamSize(), groupStage.enumString(), tournament.getTeams(),
						tournament.getNumRounds(), tournament.getStageType(), groupStage,
						tournament.getMaxOvers(), tournament.getMaxWickets());
				tournament.updateGroup(group);

				break;

			case BILATERAL:
				groupStage = Stage.NONE;
				group = new Group(1, 2, groupStage.enumString(), tournament.getTeams(),
						tournament.getNumRounds(), tournament.getStageType(), groupStage,
						tournament.getMaxOvers(), tournament.getMaxWickets());
				tournament.updateGroup(group);
				break;

			case KNOCK_OUT:
				groupStage = Stage.ROUND_1;
				group = new Group(1, tournament.getTeamSize(), groupStage.enumString(),
						tournament.getTeams(), 1, tournament.getStageType(), groupStage,
						tournament.getMaxOvers(), tournament.getMaxWickets());
				tournament.updateGroup(group);
				break;

			case GROUPS:
				for (int i = 0; i < tournament.getNumGroups(); i++) {
					Team[] groupTeams = new Team[tournament.getTeamSize() / tournament.getNumGroups()];
					for (int a = 0; a < groupTeams.length; a++) {
						groupTeams[a] = selectNextTeam(null);
					}

					groupStage = Stage.GROUP;
					int groupNumber = i + 1;
					group = new Group(groupNumber, groupTeams.length, "Group-" + groupNumber,
							groupTeams, tournament.getNumRounds(), tournament.getStageType(), groupStage,
							tournament.getMaxOvers(), tournament.getMaxWickets());

					tournament.updateGroup(group);
				}
				break;
		}

		return tournament;
	}

	private void clearCurrentGroups(Tournament tournament) {
		tournament.clearGroups();
	}

	private void progressToNextStage(Tournament theTournament) {
		tournament = theTournament;
		List<Group> tournamentGroupList = tournament.getGroupList();
		if (tournamentGroupList != null && tournamentGroupList.size() > 0) {
			Group lastGroup = tournamentGroupList.get(tournamentGroupList.size() - 1);
			Stage lastGroupsStage = lastGroup.getStage();

			if (!lastGroup.isComplete())
				completeGroup(lastGroup);

			int groupNumber = tournamentGroupList.size() + 1;
			switch (tournament.getStageType()) {
				case KNOCK_OUT:
					progressToNextStage_KnockOut(lastGroup, lastGroupsStage, groupNumber);
					break;

				case QUALIFIER:
					progressToNextStage_Qualifiers(lastGroup, groupNumber);
					break;

				case ROUND_ROBIN:
					progressToNextStage_RoundRobin(lastGroup, groupNumber);
					break;

				case SUPER_FOUR:
				case SUPER_SIX:
					progressToNextStage_Supers(lastGroup, groupNumber);
					break;

				case NONE:
					switch (tournament.getFormat()) {
						case BILATERAL:
							int team1Count = 0, team2Count = 0;
							Team team1 = tournament.getTeams()[0], team2 = tournament.getTeams()[1];
							for (MatchInfo matchInfo : lastGroup.getMatchInfoList()) {
								team1Count += team1.equals(matchInfo.getWinningTeam()) ? 1 : 0;
								team2Count += team2.equals(matchInfo.getWinningTeam()) ? 1 : 0;
							}

							Team tournamentWinner = team1Count == team2Count
									? null
									: (team1Count > team2Count) ? team1 : team2;
							completeTournament(tournamentWinner);
							break;

						case ROUND_ROBIN:
							List<PointsData> pointsTable = lastGroup.getPointsData();
							Collections.sort(pointsTable, new PointsDataComparator());
							completeTournament(pointsTable.get(0).getTeam());
							break;
					}
					break;
			}

			for (int i = 0; i < tournamentGroupList.size(); i++) {
				Group currGroup = tournamentGroupList.get(i);
				if (currGroup.getId() == 0) {
					Group updatedGroup = saveNewGroup(currGroup);
					tournament.updateGroup(updatedGroup);
					i = 0;
				}
			}
		}
	}

	private void completeGroup(Group group) {
		group.setComplete(true);
		tournament.updateGroup(group);

		GroupsDBHandler groupsDBHandler = new GroupsDBHandler(context);
		groupsDBHandler.completeGroup(group.getId());
	}

	private Group saveNewGroup(Group group) {
		GroupsDBHandler groupsDBHandler = new GroupsDBHandler(context);

		int groupID = groupsDBHandler.updateGroup(tournament.getId(), group);
		group.setId(groupID);

		List<MatchInfo> matchInfoList = group.getMatchInfoList();
		if (matchInfoList != null) {
			MatchInfoDBHandler matchInfoDBHandler = new MatchInfoDBHandler(context);
			for (int i = 0; i < matchInfoList.size(); i++) {
				MatchInfo matchInfo = matchInfoList.get(i);
				matchInfoDBHandler.addNewMatchInfo(groupID, matchInfo);
			}

			group.setMatchInfoList(matchInfoList);
		}

		List<PointsData> pointsDataList = group.getPointsData();
		if (pointsDataList != null) {
			PointsDataDBHandler pointsDataDBHandler = new PointsDataDBHandler(context);
			for (int i = 0; i < pointsDataList.size(); i++) {
				PointsData pointsData = pointsDataList.get(i);
				pointsDataDBHandler.addNewPointsDataRecord(pointsData, groupID, tournament.getMaxOvers(), tournament.getMaxWickets());
			}

			group.updatePointsTable(pointsDataList);
		}

		return group;
	}

	private void progressToNextStage_KnockOut(Group lastGroup, Stage lastGroupsStage, int groupNumber) {
		List<Team> winningsTeams = new ArrayList<>();

		if (tournament.getFormat() == TournamentFormat.KNOCK_OUT
				|| (tournament.getFormat() == TournamentFormat.ROUND_ROBIN && lastGroupsStage != Stage.ROUND_ROBIN)) {
			if (lastGroup.getMatchInfoList() != null) {
				for (MatchInfo matchInfo : lastGroup.getMatchInfoList()) {
					winningsTeams.add(matchInfo.getWinningTeam());
				}
			}
		} else if (tournament.getFormat() == TournamentFormat.GROUPS) {
			int numberOfTeamsPerGroup = 2;
			if (tournament.getNumGroups() == 2) {
				numberOfTeamsPerGroup = 4;
			}

			for (Group tournamentGroup : tournament.getGroupList()) {
				List<PointsData> pointsDataList = tournamentGroup.getPointsData();
				for (int i = 0; i < numberOfTeamsPerGroup; i++) {
					winningsTeams.add(pointsDataList.get(i).getTeam());
				}
			}
		} else if (tournament.getFormat() == TournamentFormat.ROUND_ROBIN) {
			int numberOfTeams = 2;
			int lastGroupTeamCount = lastGroup.getTeams().length;
			if (lastGroupTeamCount >= 12) {
				numberOfTeams = 8;
			} else if (lastGroupTeamCount >= 6) {
				numberOfTeams = 4;
			}

			List<PointsData> pointsDataList = lastGroup.getPointsData();
			for (int i = 0; i < numberOfTeams; i++) {
				winningsTeams.add(pointsDataList.get(i).getTeam());
			}
		}

		Stage groupStage = Stage.NONE;
		if (winningsTeams.size() > 8) {
			if (lastGroupsStage == Stage.ROUND_1) {
				groupStage = Stage.ROUND_2;
			} else if (lastGroupsStage == Stage.ROUND_2) {
				groupStage = Stage.ROUND_3;
			}
		} else {
			switch (winningsTeams.size()) {
				case 8:
					groupStage = Stage.QUARTER_FINAL;
					break;

				case 4:
					groupStage = Stage.SEMI_FINAL;
					break;

				case 2:
					groupStage = Stage.FINAL;
					break;

				case 1:
					groupStage = Stage.NONE;
					break;
			}
		}

		if (groupStage != Stage.NONE) {
			group = new Group(groupNumber, winningsTeams.size(),
					groupStage.enumString(), CommonUtils.objectArrToTeamArr(winningsTeams.toArray()),
					1, tournament.getStageType(), groupStage,
					tournament.getMaxOvers(), tournament.getMaxWickets());
			createSchedule(groupStage);
			tournament.updateGroup(group);
		} else {
			if (lastGroup.getMatchInfoList() != null) {
				MatchInfo matchInfo = lastGroup.getMatchInfoList().get(0);
				completeTournament(matchInfo.getWinningTeam());
			}
		}
	}

	private void progressToNextStage_Qualifiers(Group lastGroup, int groupNumber) {
		List<Group> tournamentGroupList = tournament.getGroupList();
		MatchInfo qualifierOne;
		Stage groupStage;
		switch (lastGroup.getStage()) {
			case ELIMINATOR_1:
				qualifierOne = tournamentGroupList.get(tournamentGroupList.size() - 2).getMatchInfoList().get(0);
				MatchInfo eliminatorOne = lastGroup.getMatchInfoList().get(0);

				Team qualifierOneWinTeam = qualifierOne.getWinningTeam();
				Team eliminatorTwoTeam1 = qualifierOne.getTeam1().equals(qualifierOneWinTeam) ? qualifierOne.getTeam2() : qualifierOne.getTeam1();

				groupStage = Stage.ELIMINATOR_2;
				group = new Group(groupNumber, 2, groupStage.enumString(),
						new Team[]{eliminatorTwoTeam1, eliminatorOne.getWinningTeam()}, 1,
						tournament.getStageType(), groupStage,
						tournament.getMaxOvers(), tournament.getMaxWickets());
				createSchedule(groupStage);
				tournament.updateGroup(group);

				break;

			case ELIMINATOR_2:
				qualifierOne = tournamentGroupList.get(tournamentGroupList.size() - 3).getMatchInfoList().get(0);
				MatchInfo qualifierTwo = lastGroup.getMatchInfoList().get(0);

				groupStage = Stage.FINAL;
				group = new Group(groupNumber, 2, groupStage.enumString(),
						new Team[]{qualifierOne.getWinningTeam(), qualifierTwo.getWinningTeam()},
						1, tournament.getStageType(), groupStage,
						tournament.getMaxOvers(), tournament.getMaxWickets());
				createSchedule(groupStage);
				tournament.updateGroup(group);

				break;

			case FINAL:
				MatchInfo matchInfo = lastGroup.getMatchInfoList().get(0);
				completeTournament(matchInfo.getWinningTeam());
				break;

			default:
				List<PointsData> pointsData = lastGroup.getPointsData();
				Team topTeam = pointsData.get(0).getTeam();
				Team secondTeam = pointsData.get(1).getTeam();
				Team thirdTeam = pointsData.get(2).getTeam();
				Team fourthTeam = pointsData.get(3).getTeam();

				groupStage = Stage.QUALIFIER;
				group = new Group(groupNumber++, 2, groupStage.enumString(),
						new Team[]{topTeam, secondTeam}, 1, tournament.getStageType(), groupStage,
						tournament.getMaxOvers(), tournament.getMaxWickets());
				createSchedule(groupStage);
				tournament.updateGroup(group);

				groupStage = Stage.ELIMINATOR_1;
				group = new Group(groupNumber, 2, groupStage.enumString(),
						new Team[]{thirdTeam, fourthTeam}, 1, tournament.getStageType(), groupStage,
						tournament.getMaxOvers(), tournament.getMaxWickets());
				createSchedule(groupStage);
				tournament.updateGroup(group);

		}
	}

	private void progressToNextStage_RoundRobin(Group lastGroup, int groupNumber) {
		List<Group> tournamentGroupList = tournament.getGroupList();

		switch (lastGroup.getStage()) {
			case GROUP:
				int numTeamsFromGroup = 2;
				if (tournament.getNumGroups() > 3) {
					numTeamsFromGroup = 1;
				}

				List<Team> nextStageGroupList = new ArrayList<>();
				for (Group tournamentGroup : tournamentGroupList) {
					List<PointsData> pointsDataList = tournamentGroup.getPointsData();
					nextStageGroupList.add(pointsDataList.get(0).getTeam());
					if (numTeamsFromGroup == 2)
						nextStageGroupList.add(pointsDataList.get(1).getTeam());
				}

				Stage groupStage = Stage.ROUND_ROBIN;
				group = new Group(groupNumber, nextStageGroupList.size(), groupStage.enumString(),
						CommonUtils.objectArrToTeamArr(nextStageGroupList.toArray()), 1,
						tournament.getStageType(), groupStage,
						tournament.getMaxOvers(), tournament.getMaxWickets());
				createGroupSchedule(groupStage);
				tournament.updateGroup(group);
				break;

			case ROUND_ROBIN:
				List<PointsData> pointsDataList = lastGroup.getPointsData();
				Team team1 = pointsDataList.get(0).getTeam();
				Team team2 = pointsDataList.get(2).getTeam();

				groupStage = Stage.FINAL;
				group = new Group(groupNumber, 2, groupStage.enumString(), new Team[]{team1, team2},
						1, tournament.getStageType(), groupStage,
						tournament.getMaxOvers(), tournament.getMaxWickets());
				createGroupSchedule(groupStage);
				tournament.updateGroup(group);
				break;

			case FINAL:
				completeTournament(lastGroup.getMatchInfoList().get(0).getWinningTeam());
				break;

		}
	}

	private void progressToNextStage_Supers(Group lastGroup, int groupNumber) {
		List<Team> nextStageTeamList = new ArrayList<>();
		List<Group> tournamentGroupList = tournament.getGroupList();
		if (lastGroup.getStage().compareTo(Stage.SUPER_FOUR) < 0) {
			switch (tournament.getFormat()) {
				case GROUPS:
					int numberTeamsFromGroup = 2;
					if (tournament.getStageType() == TournamentStageType.SUPER_FOUR
							&& tournament.getNumGroups() == 4)
						numberTeamsFromGroup = 1;
					if (tournament.getStageType() == TournamentStageType.SUPER_SIX) {
						if (tournament.getNumGroups() == 2)
							numberTeamsFromGroup = 3;
						else if (tournament.getNumGroups() == 6)
							numberTeamsFromGroup = 1;
					}

					for (Group tournamentGroup : tournamentGroupList) {
						List<PointsData> pointsDataList = tournamentGroup.getPointsData();
						for (int i = 0; i < numberTeamsFromGroup; i++) {
							nextStageTeamList.add(pointsDataList.get(i).getTeam());
						}
					}
					break;

				case ROUND_ROBIN:
					List<PointsData> pointsDataList = lastGroup.getPointsData();
					int teamLimit = tournament.getStageType() == TournamentStageType.SUPER_FOUR ? 4 : 6;
					for (int i = 0; i < teamLimit; i++) {
						nextStageTeamList.add(pointsDataList.get(i).getTeam());
					}
					break;

			}
		}

		Stage groupStage;
		switch (lastGroup.getStage()) {
			case GROUP:
			case ROUND_ROBIN:
				groupStage = tournament.getStageType() == TournamentStageType.SUPER_FOUR
						? Stage.SUPER_FOUR : Stage.SUPER_SIX;
				group = new Group(groupNumber, nextStageTeamList.size(), groupStage.enumString(),
						CommonUtils.objectArrToTeamArr(nextStageTeamList.toArray()), 1,
						tournament.getStageType(), groupStage,
						tournament.getMaxOvers(), tournament.getMaxWickets());

				createGroupSchedule(groupStage);
				tournament.updateGroup(group);
				break;

			case SUPER_FOUR:
			case SUPER_SIX:
				List<PointsData> pointsDataList = lastGroup.getPointsData();
				Team team1 = pointsDataList.get(0).getTeam();
				Team team2 = pointsDataList.get(2).getTeam();

				groupStage = Stage.FINAL;
				group = new Group(groupNumber, 2, groupStage.enumString(), new Team[]{team1, team2},
						1, tournament.getStageType(), groupStage,
						tournament.getMaxOvers(), tournament.getMaxWickets());

				createGroupSchedule(groupStage);
				tournament.updateGroup(group);
				break;

			case FINAL:
				completeTournament(lastGroup.getMatchInfoList().get(0).getWinningTeam());
				break;
		}
	}

	public void checkTournamentStagesComplete(Tournament theTournament) {
		this.tournament = theTournament;
		boolean allStagesComplete = true;

		if (tournament != null) {
			List<Group> groupList = tournament.getGroupList();
			Collections.sort(groupList, new GroupComparator());
			for (int i = 0; i < groupList.size(); i++) {
				Group currGroup = groupList.get(i);
				if (!currGroup.isComplete() && currGroup.getMatchInfoList() != null) {
					boolean isStageComplete = true;
					for (MatchInfo matchInfo : currGroup.getMatchInfoList()) {
						if (!matchInfo.isComplete()) {
							MatchDBHandler matchDBHandler = new MatchDBHandler(context);
							int matchID = matchInfo.getMatchID();
							boolean isMatchComplete = matchID > 0 && matchDBHandler.checkIfMatchComplete(matchID);
							if (isMatchComplete) {
								closeTournamentMatch(matchID);
							} else {
								allStagesComplete = false;
								isStageComplete = false;
								break;
							}
						}
					}

					if (isStageComplete)
						completeGroup(currGroup);
				}
			}

			if (allStagesComplete && !tournament.isComplete()) {
				progressToNextStage(tournament);
			}
		}
	}

	private void checkTournamentStagesComplete(int matchInfoID) {
		MatchInfoDBHandler matchInfoDBHandler = new MatchInfoDBHandler(context);
		Tournament tournament = matchInfoDBHandler.getTournamentByMatchInfoID(matchInfoID);
		checkTournamentStagesComplete(tournament);
	}

	private void completeTournament(Team winner) {
		TournamentDBHandler tournamentDBHandler = new TournamentDBHandler(context);
		tournament.setTournamentWinner(winner);
		tournament.setComplete(true);
		tournamentDBHandler.updateTournament(tournament);
		tournamentDBHandler.completeTournament(tournament.getId());
	}

	public Tournament storePointsData(Tournament tournament) {
		PointsDataDBHandler pointsDataDBHandler = new PointsDataDBHandler(context);
		List<Group> groupList = tournament.getGroupList();
		if (groupList != null) {
			for (Group group : groupList) {
				List<PointsData> pointsTable = group.getPointsData();
				if (pointsTable != null) {
					for (PointsData pointsData : pointsTable) {
						int id = pointsDataDBHandler.addNewPointsDataRecord(pointsData, group.getId(), tournament.getMaxOvers(), tournament.getMaxWickets());
						pointsData.setId(id);
					}
				}
			}
		}
		return tournament;
	}

	private void closeTournamentMatch(int matchID) {
		MatchDBHandler matchDBHandler = new MatchDBHandler(context);
		CricketCardUtils ccUtils = matchDBHandler.getCompletedMatchData(matchID);
		closeTournamentMatch(ccUtils);
	}

	public void closeTournamentMatch(CricketCardUtils ccUtils) {
		MatchInfoDBHandler matchInfoDBHandler = new MatchInfoDBHandler(context);

		if (ccUtils != null) {
			int winningTeamID = ccUtils.getWinningTeam() != null ? ccUtils.getWinningTeam().getId() : 0;
			matchInfoDBHandler.completeTournamentMatch(ccUtils.getMatchInfo().getId(), ccUtils.getMatchInfo().getMatchID(), winningTeamID);
			checkTournamentStagesComplete(ccUtils.getMatchInfo().getId());
			updatePointsData(ccUtils);
		}
	}

	private void updatePointsData(CricketCardUtils ccUtils) {
		MatchInfoDBHandler matchInfoDBHandler = new MatchInfoDBHandler(context);
		PointsDataDBHandler pointsDataDBHandler = new PointsDataDBHandler(context);

		CricketCard firstInningsCard = ccUtils.getPrevInningsCard(), secondInningsCard = ccUtils.getCard();
		MatchInfo matchInfo = ccUtils.getMatchInfo();
		if (matchInfo.getMatchID() == 0 || matchInfo.getGroupID() == 0) {
			matchInfo = matchInfoDBHandler.getMatchInfo(matchInfo.getId());
		}
		if (ccUtils.isAbandoned()) {
			if (ccUtils.getCard().getInnings() == 1) {
				firstInningsCard = ccUtils.getCard();
				secondInningsCard = null;
			} else {
				firstInningsCard = ccUtils.getPrevInningsCard();
				secondInningsCard = ccUtils.getCard();
			}
		}

		int groupID = matchInfo.getGroupID();
		PointsData pointsDataTeam1 = pointsDataDBHandler.getTeamPointsData(groupID, ccUtils.getTeam1().getId());
		PointsData pointsDataTeam2 = pointsDataDBHandler.getTeamPointsData(groupID, ccUtils.getTeam2().getId());

		MatchResult team1Result = MatchResult.NO_RESULT, team2Result = MatchResult.NO_RESULT;
		if (ccUtils.isMatchTied()) {
			team1Result = MatchResult.TIED;
			team2Result = MatchResult.TIED;
		} else if (!ccUtils.isAbandoned()) {
			int winningTeamId = ccUtils.getWinningTeam().getId();
			team1Result = (ccUtils.getTeam1().getId() == winningTeamId) ? MatchResult.WON : MatchResult.LOST;
			team2Result = (ccUtils.getTeam2().getId() == winningTeamId) ? MatchResult.WON : MatchResult.LOST;
		}

		int team1Score, team1Wickets, team2Score, team2Wickets;
		String team1Overs, team2Overs;

		team1Score = firstInningsCard != null ? firstInningsCard.getScore() : 0;
		team1Wickets = firstInningsCard != null ? firstInningsCard.getWicketsFallen() : 0;
		team1Overs = firstInningsCard != null ? firstInningsCard.getTotalOversBowled() : "0";

		team2Score = secondInningsCard != null ? secondInningsCard.getScore() : 0;
		team2Wickets = secondInningsCard != null ? secondInningsCard.getWicketsFallen() : 0;
		team2Overs = secondInningsCard != null ? secondInningsCard.getTotalOversBowled() : "0";

		pointsDataTeam1.addPlayedMatchData(team1Result, team1Score, team1Overs, team1Wickets,
				team2Score, team2Overs, team2Wickets);
		pointsDataTeam2.addPlayedMatchData(team2Result, team2Score, team2Overs, team2Wickets,
				team1Score, team1Overs, team1Wickets);

		pointsDataDBHandler.updatePointsDataRecord(pointsDataTeam1);
		pointsDataDBHandler.updatePointsDataRecord(pointsDataTeam2);
	}
}
