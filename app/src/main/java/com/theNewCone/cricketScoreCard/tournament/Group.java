package com.theNewCone.cricketScoreCard.tournament;

import com.theNewCone.cricketScoreCard.match.Team;

public class Group {
	private int id, numberOfTeams;
	private String name;
	private Team[] teams;
	private PointsTable[] pointsTable;

	public Group(int id, int numberOfTeams, String name, Team[] teams) {
		this.id = id;
		this.numberOfTeams = numberOfTeams;
		this.name = name;
		this.teams = teams;
	}

	public int getId() {
		return id;
	}

	public int getNumberOfTeams() {
		return numberOfTeams;
	}

	public String getName() {
		return name;
	}

	public Team[] getTeams() {
		return teams;
	}

	public PointsTable[] getPointsTable() {
		return pointsTable;
	}
}
