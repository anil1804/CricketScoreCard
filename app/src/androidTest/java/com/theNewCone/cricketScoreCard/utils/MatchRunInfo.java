package com.theNewCone.cricketScoreCard.utils;

public class MatchRunInfo {
	private String matchName, team1Capt, team1WK, team2Capt, team2WK;
	private String team1Name, team2Name, team1ShortName, team2ShortName, tossWonBy;
	private String[] team1Players, team2Players;
	private int choseTo, maxOvers, maxWickets, numPlayers;

	public MatchRunInfo(String matchName, int maxOvers, int maxWickets, int numPlayers) {
		this.matchName = matchName;
		this.maxOvers = maxOvers;
		this.maxWickets = maxWickets;
		this.numPlayers = numPlayers;
	}

	public void setTeam1(String name, String shortName, String[] players, String captain, String wicketKeeper) {
		this.team1Name = name;
		this.team1ShortName = shortName;
		this.team1Players = players;
		this.team1Capt = captain;
		this.team1WK = wicketKeeper;
	}

	public void setTeam2(String name, String shortName, String[] players, String captain, String wicketKeeper) {
		this.team2Name = name;
		this.team2ShortName = shortName;
		this.team2Players = players;
		this.team2Capt = captain;
		this.team2WK = wicketKeeper;
	}

	public void updateTossDetails(String tossWonBy, int choseTo) {
		this.tossWonBy = tossWonBy;
		this.choseTo = choseTo;
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

	String getTeam1Name() {
		return team1Name;
	}

	String getTeam2Name() {
		return team2Name;
	}

	String getTeam1ShortName() {
		return team1ShortName;
	}

	String getTeam2ShortName() {
		return team2ShortName;
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
}
