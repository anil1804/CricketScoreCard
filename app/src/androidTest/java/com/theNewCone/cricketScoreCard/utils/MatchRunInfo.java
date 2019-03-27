package com.theNewCone.cricketScoreCard.utils;

import com.theNewCone.cricketScoreCard.match.Team;

public class MatchRunInfo {
	private final boolean isTournament;
	private String matchName;
	private String team1Capt, team1WK, team2Capt, team2WK, tossWonBy;
	private String[] team1Players, team2Players;
	private Team team1, team2;
	private int choseTo;
	private int maxOvers, maxWickets, numPlayers, oversPerBowler;

	MatchRunInfo(boolean isTournament) {
		this.isTournament = isTournament;
		this.matchName = null;
		this.maxOvers = 0;
		this.maxWickets = 0;
		this.numPlayers = 0;
		this.oversPerBowler = 0;
	}

	public MatchRunInfo(String matchName, int maxOvers, int maxWickets, int numPlayers) {
		this.isTournament = false;
		this.matchName = matchName;
		this.maxOvers = maxOvers;
		this.maxWickets = maxWickets;
		this.numPlayers = numPlayers;
		this.oversPerBowler = 0;
	}

	public MatchRunInfo(String matchName, int maxOvers, int maxWickets, int oversPerBowler, int numPlayers) {
		this.isTournament = false;
		this.matchName = matchName;
		this.maxOvers = maxOvers;
		this.maxWickets = maxWickets;
		this.numPlayers = numPlayers;
		this.oversPerBowler = oversPerBowler;
	}

	public void setTeam1(String name, String shortName, String[] players, String captain, String wicketKeeper) {
		this.team1 = new Team(name, shortName);
		this.team1Players = players;
		this.team1Capt = captain;
		this.team1WK = wicketKeeper;
	}

	public void setTeam2(String name, String shortName, String[] players, String captain, String wicketKeeper) {
		this.team2 = new Team(name, shortName);
		this.team2Players = players;
		this.team2Capt = captain;
		this.team2WK = wicketKeeper;
	}

	void updateMatchRunInfo(String matchName, int maxOvers, int maxWickets, int oversPerBowler, int numPlayers) {
		this.matchName = matchName;
		this.maxOvers = maxOvers;
		this.maxWickets = maxWickets;
		this.numPlayers = numPlayers;
		this.oversPerBowler = oversPerBowler;
	}

	public void updateTossDetails(String tossWonBy, int choseTo) {
		this.tossWonBy = tossWonBy;
		this.choseTo = choseTo;
	}

	boolean isTournament() {
		return isTournament;
	}

	String getMatchName() {
		return matchName;
	}

	String getTeam1Capt() {
		return team1Capt;
	}

	String getTeam1WK() {
		return team1WK;
	}

	String getTeam2Capt() {
		return team2Capt;
	}

	String getTeam2WK() {
		return team2WK;
	}

	Team getTeam1() {
		return team1;
	}

	Team getTeam2() {
		return team2;
	}

	String getTossWonBy() {
		return tossWonBy;
	}

	String[] getTeam1Players() {
		return team1Players;
	}

	String[] getTeam2Players() {
		return team2Players;
	}

	int getChoseTo() {
		return choseTo;
	}

	int getMaxOvers() {
		return maxOvers;
	}

	int getMaxWickets() {
		return maxWickets;
	}

	int getNumPlayers() {
		return numPlayers;
	}

	int getOversPerBowler() {
		return oversPerBowler;
	}
}
