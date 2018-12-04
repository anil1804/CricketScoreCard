package com.theNewCone.cricketScoreCard.tournament;

import com.theNewCone.cricketScoreCard.match.Team;

import java.io.Serializable;

public class Tournament implements Serializable {
	private int id, numGroups, numRounds;
	private String name;
	private Team[] teams;
	private int maxOvers, maxWickets, players, maxPerBowler;
	private TournamentType type;
	private TournamentStageType stageType;
	private Schedule schedule;

	public Tournament(String name, Team[] teams, int maxOvers, int maxWickets, int players, int maxPerBowler,
					  int numGroups, int numRounds, TournamentType type, TournamentStageType stageType) {
		this.numGroups = numGroups;
		this.numRounds = numRounds;
		this.name = name;
		this.teams = teams;
		this.maxOvers = maxOvers;
		this.maxWickets = maxWickets;
		this.players = players;
		this.maxPerBowler = maxPerBowler;
		this.type = type;
		this.stageType = stageType;
	}

	public Tournament(String name, Team[] teams, int maxOvers, int maxWickets, int players, int maxPerBowler,
					  int numRounds, TournamentType type, TournamentStageType stageType) {
		this.numGroups = -1;
		this.numRounds = numRounds;
		this.name = name;
		this.teams = teams;
		this.maxOvers = maxOvers;
		this.maxWickets = maxWickets;
		this.players = players;
		this.maxPerBowler = maxPerBowler;
		this.type = type;
		this.stageType = stageType;
	}

	public Tournament(int id, String name, Team[] teams, int maxOvers, int maxWickets, int players, int maxPerBowler,
					  int numGroups, int numRounds, TournamentType type, TournamentStageType stageType) {
		this.id = id;
		this.numGroups = numGroups;
		this.numRounds = numRounds;
		this.name = name;
		this.teams = teams;
		this.maxOvers = maxOvers;
		this.maxWickets = maxWickets;
		this.players = players;
		this.maxPerBowler = maxPerBowler;
		this.type = type;
		this.stageType = stageType;
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

	public TournamentType getType() {
		return type;
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

	public enum TournamentType {
		ROUND_ROBIN, GROUPS, KNOCK_OUT, BILATERAL
	}

	public enum TournamentStageType {
		SUPER_FOUR, SUPER_SIX, KNOCK_OUT, QUALIFIER, NONE
	}
}
