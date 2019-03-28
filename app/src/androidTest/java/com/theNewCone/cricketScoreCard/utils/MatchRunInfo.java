package com.theNewCone.cricketScoreCard.utils;

import com.theNewCone.cricketScoreCard.match.Team;

public class MatchRunInfo {
	private final boolean isTournament;
	private String matchName;
	private TeamInfo team1Info, team2Info;
	private String tossWonBy;
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
		team1Info = new TeamInfo(new Team(name, shortName));
		team1Info.setPlayerInfo(players, captain, wicketKeeper);
	}

	void setTeam1(TeamInfo teamInfo) {
		this.team1Info = teamInfo;
	}

	public void setTeam2(String name, String shortName, String[] players, String captain, String wicketKeeper) {
		team2Info = new TeamInfo(new Team(name, shortName));
		team2Info.setPlayerInfo(players, captain, wicketKeeper);
	}

	void setTeam2(TeamInfo teamInfo) {
		this.team2Info = teamInfo;
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

	TeamInfo getTeam1Info() {
		return team1Info;
	}

	TeamInfo getTeam2Info() {
		return team2Info;
	}

	String getTossWonBy() {
		return tossWonBy;
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
