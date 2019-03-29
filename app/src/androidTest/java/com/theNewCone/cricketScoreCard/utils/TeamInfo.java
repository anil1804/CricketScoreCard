package com.theNewCone.cricketScoreCard.utils;

import com.theNewCone.cricketScoreCard.match.Team;

class TeamInfo {
	private final Team team;
	private String captain, wicketKeeper;
	private String[] players;

	TeamInfo(Team team) {
		this.team = team;
	}

	TeamInfo(Team team, String[] players, String captain, String wicketKeeper) {
		this.team = team;
		this.players = players;
		this.captain = captain;
		this.wicketKeeper = wicketKeeper;
	}

	String getCaptain() {
		return captain;
	}

	String getWicketKeeper() {
		return wicketKeeper;
	}

	String[] getPlayers() {
		return players;
	}

	void setPlayerInfo(String[] players, String captain, String wicketKeeper) {
		this.players = players;
		this.captain = captain;
		this.wicketKeeper = wicketKeeper;
	}

	Team getTeam() {
		return team;
	}
}
