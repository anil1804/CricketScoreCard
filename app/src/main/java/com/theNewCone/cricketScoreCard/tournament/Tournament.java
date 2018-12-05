package com.theNewCone.cricketScoreCard.tournament;

import com.theNewCone.cricketScoreCard.match.Team;

import java.io.Serializable;

public class Tournament implements Serializable {
	private int id, numGroups, numRounds, teamSize;
	private String name;
	private Team[] teams;
	private Group[] groups;
	private int maxOvers, maxWickets, players, maxPerBowler;
	private TournamentFormat format;
	private TournamentStageType stageType;
	private Schedule schedule;
	private String createdDate;

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

	public Group[] getGroups() {
		return groups;
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

	public void setGroups(Group[] groups) {
		this.groups = groups;
	}

	public TournamentStageType getStageType() {
		return stageType;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
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

	public enum TournamentFormat {
		ROUND_ROBIN, GROUPS, KNOCK_OUT, BILATERAL
	}

	public enum TournamentStageType {
		SUPER_FOUR, SUPER_SIX, KNOCK_OUT, QUALIFIER, NONE
	}
}
