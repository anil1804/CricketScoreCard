package com.theNewCone.cricketScoreCard.tournament;

import android.support.annotation.NonNull;

import com.theNewCone.cricketScoreCard.comparator.GroupComparator;
import com.theNewCone.cricketScoreCard.enumeration.TournamentFormat;
import com.theNewCone.cricketScoreCard.enumeration.TournamentStageType;
import com.theNewCone.cricketScoreCard.match.Team;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tournament implements Serializable {
	private int id, numGroups, numRounds, teamSize;
	private String name;
	private Team[] teams;
	private List<Group> groupList;
	private int maxOvers, maxWickets, players, maxPerBowler;
	private TournamentFormat format;
	private TournamentStageType stageType;
	private String createdDate;
	private boolean isScheduled = false;
	private Team tournamentWinner;

	public Tournament(String name, Team[] teams, int maxOvers, int maxWickets, int players, int maxPerBowler,
					  int numGroups, int numRounds, TournamentFormat format, TournamentStageType stageType) {
		this.numGroups = numGroups;
		this.numRounds = numRounds;
		this.name = name;
		this.teams = teams;
		this.maxOvers = maxOvers;
		this.maxWickets = maxWickets;
		this.players = players;
		this.maxPerBowler = maxPerBowler;
		this.format = format;
		this.stageType = stageType;
		this.teamSize = (teams != null) ? teams.length : 0;
	}

	public Tournament(String name, Team[] teams, int maxOvers, int maxWickets, int players, int maxPerBowler,
					  int numRounds, TournamentFormat format, TournamentStageType stageType) {
		this.numGroups = -1;
		this.numRounds = numRounds;
		this.name = name;
		this.teams = teams;
		this.maxOvers = maxOvers;
		this.maxWickets = maxWickets;
		this.players = players;
		this.maxPerBowler = maxPerBowler;
		this.format = format;
		this.stageType = stageType;
		this.teamSize = (teams != null) ? teams.length : 0;
	}

	public Tournament(int id, String name, Team[] teams, int maxOvers, int maxWickets, int players, int maxPerBowler,
					  int numGroups, int numRounds, TournamentFormat format, TournamentStageType stageType) {
		this.id = id;
		this.numGroups = numGroups;
		this.numRounds = numRounds;
		this.name = name;
		this.teams = teams;
		this.maxOvers = maxOvers;
		this.maxWickets = maxWickets;
		this.players = players;
		this.maxPerBowler = maxPerBowler;
		this.format = format;
		this.stageType = stageType;
		this.teamSize = (teams != null) ? teams.length : 0;
	}

	public Tournament(int id, String name, int teamSize, TournamentFormat format, String createdDate) {
		this.id = id;
		this.teamSize = teamSize;
		this.name = name;
		this.format = format;
		this.createdDate = createdDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumGroups() {
		return numGroups;
	}

	public int getNumRounds() {
		return numRounds;
	}

	public String getName() {
		return name;
	}

	public Team[] getTeams() {
		return teams;
	}

	public List<Group> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<Group> groupList) {
		this.groupList = groupList;
	}

	public int getMaxOvers() {
		return maxOvers;
	}

	public int getMaxWickets() {
		return maxWickets;
	}

	public int getPlayers() {
		return players;
	}

	public int getMaxPerBowler() {
		return maxPerBowler;
	}

	public void addToGroups(@NonNull Group group) {
		if (groupList == null)
			groupList = new ArrayList<>();

		groupList.add(group);

		Collections.sort(groupList, new GroupComparator());
	}

	public void replaceGroup(Group group) {
		groupList.remove(group);
		addToGroups(group);
	}

	public void clearGroups() {
		if (groupList != null)
			groupList.clear();
	}

	public TournamentStageType getStageType() {
		return stageType;
	}

	public TournamentFormat getFormat() {
		return format;
	}

	public int getTeamSize() {
		return teamSize;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public boolean isScheduled() {
		return isScheduled;
	}

	public void setTournamentWinner(Team tournamentWinner) {
		this.tournamentWinner = tournamentWinner;
	}

	public void checkScheduled() {
		if (groupList != null) {
			for (Group group : groupList) {
				if (!group.isScheduled()) {
					isScheduled = false;
					return;
				}
			}
			isScheduled = true;
		} else {
			isScheduled = false;
		}
	}
}
