package com.theNewCone.cricketScoreCard.tournament;

import android.support.annotation.NonNull;

import com.theNewCone.cricketScoreCard.comparator.MatchInfoComparator;
import com.theNewCone.cricketScoreCard.comparator.PointsDataComparator;
import com.theNewCone.cricketScoreCard.enumeration.Stage;
import com.theNewCone.cricketScoreCard.enumeration.TournamentStageType;
import com.theNewCone.cricketScoreCard.match.Team;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Group implements Serializable {
	private int id, groupNumber, numberOfTeams, numRounds, matchesPerRound;
	private TournamentStageType stageType;
	private String name;
	private Team[] teams;
	private List<PointsData> pointsTable;
	private List<MatchInfo> matchInfoList;
	private Stage stage;
	private boolean isScheduled = false;

	public Group(String name, @NonNull Stage stage) {
		this.name = name;
		this.stage = stage;
	}

	public Group(String name, Team[] teams, @NonNull Stage stage) {
		this.name = name;
		this.teams = teams;
		this.stage = stage;
	}

	public Group(int groupNumber, int numberOfTeams, String name, Team[] teams, int numRounds
			, @NonNull TournamentStageType stageType, @NonNull Stage stage) {
		this.groupNumber = groupNumber;
		this.numberOfTeams = numberOfTeams;
		this.name = name;
		this.teams = teams;
		this.stageType = stageType;
		this.numRounds = numRounds;
		this.stage = stage;
		this.matchesPerRound = calculateMatchesPerRound();
	}

	public Group(int groupNumber, int numberOfTeams, String name, Team[] teams, int numRounds
			, @NonNull TournamentStageType stageType, @NonNull Stage stage, int maxOvers, int maxWickets) {
		this.groupNumber = groupNumber;
		this.numberOfTeams = numberOfTeams;
		this.name = name;
		this.teams = teams;
		this.stageType = stageType;
		this.numRounds = numRounds;
		this.stage = stage;
		this.matchesPerRound = calculateMatchesPerRound();

		setupPointsData(maxOvers, maxWickets);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGroupNumber() {
		return groupNumber;
	}

	public String getName() {
		return name;
	}

	public Team[] getTeams() {
		return teams;
	}

	public TournamentStageType getStageType() {
		return stageType;
	}

	public List<PointsData> getPointsData() {
		return pointsTable;
	}

	public List<MatchInfo> getMatchInfoList() {
		return matchInfoList;
	}

	public void setMatchInfoList(@NonNull List<MatchInfo> matchInfoList) {
		if (matchInfoList.size() > 0)
			this.matchInfoList = matchInfoList;
	}

	public int getNumRounds() {
		return numRounds;
	}

	public int getMatchesPerRound() {
		return matchesPerRound;
	}

	public Stage getStage() {
		return stage;
	}

	public boolean isScheduled() {
		return isScheduled;
	}

	public void setScheduled(boolean scheduled) {
		isScheduled = scheduled;
	}

	private void addToSchedule(@NonNull MatchInfo matchInfo) {
		if (matchInfoList == null)
			matchInfoList = new ArrayList<>();

		matchInfoList.add(matchInfo);
		Collections.sort(matchInfoList, new MatchInfoComparator());
	}

	public void updateMatchInfo(MatchInfo matchInfo) {
		try {
			matchInfoList.remove(matchInfo);
		} catch (Exception ex) {
			//DO NOTHING
		}
		addToSchedule(matchInfo);
	}

	public void clearSchedule() {
		matchInfoList = new ArrayList<>();
	}

	private int calculateMatchesPerRound() {
		int totalCount = 0;

		switch (stage) {
			case GROUP:
				totalCount = getMatchCountByGroupStageType(TournamentStageType.ROUND_ROBIN);
				break;

			case ROUND_1:
			case ROUND_2:
			case ROUND_3:
				totalCount = getMatchCountByGroupStageType(TournamentStageType.KNOCK_OUT);
				break;

			case ROUND_ROBIN:
				totalCount = getMatchCountByGroupStageType(TournamentStageType.ROUND_ROBIN);
				break;

			case QUARTER_FINAL:
				totalCount = 4;
				break;

			case SEMI_FINAL:
				totalCount = 2;
				break;

			case ELIMINATOR_1:
			case ELIMINATOR_2:
			case QUALIFIER:
			case FINAL:
				totalCount = 1;
				break;

			case SUPER_SIX:
				totalCount = getMatchCountByGroupStageType(TournamentStageType.SUPER_SIX);
				break;

			case SUPER_FOUR:
				totalCount = getMatchCountByGroupStageType(TournamentStageType.SUPER_FOUR);
				break;
		}

		return totalCount;
	}

	private int getMatchCountByGroupStageType(TournamentStageType stageType) {
		int totalCount = 0;

		switch (stageType) {
			case KNOCK_OUT:
				totalCount = numberOfTeams / 2;
				break;

			case SUPER_SIX:
			case SUPER_FOUR:
			case ROUND_ROBIN:
				totalCount = (numberOfTeams * (numberOfTeams - 1)) / 2;
				break;

			case QUALIFIER:
				totalCount = 3;
		}

		return totalCount;
	}

	private void addToPointsTable(PointsData pointsData) {
		if (pointsTable == null) {
			pointsTable = new ArrayList<>();
		}

		pointsTable.add(pointsData);
		Collections.sort(pointsTable, new PointsDataComparator());
	}

	public void updatePointsTable(@NonNull List<PointsData> pointsTable) {
		if (pointsTable.size() > 0) {
			this.pointsTable = pointsTable;
			Collections.sort(pointsTable, new PointsDataComparator());
		}
	}

	private void setupPointsData(int maxOvers, int maxWickets) {
		if (teams != null) {
			for (Team team : teams) {
				addToPointsTable(new PointsData(team, maxOvers, maxWickets));
			}
		}
	}
}
