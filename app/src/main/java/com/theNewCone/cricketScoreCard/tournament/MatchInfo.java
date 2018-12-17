package com.theNewCone.cricketScoreCard.tournament;

import android.support.annotation.NonNull;

import com.theNewCone.cricketScoreCard.enumeration.Stage;
import com.theNewCone.cricketScoreCard.match.Team;

import java.io.Serializable;

public class MatchInfo implements Serializable {
	private int id, matchNumber, matchID, groupNumber;
	private Stage tournamentStage;
	private Team team1, team2, winningTeam;
	private String groupName, matchDate;
	private boolean hasStarted, isComplete;

	public MatchInfo(int matchNumber, int groupNumber, String groupName, @NonNull Stage tournamentStage, @NonNull Team team1, @NonNull Team team2) {
		this.matchNumber = matchNumber;
		this.tournamentStage = tournamentStage;
		this.team1 = team1;
		this.team2 = team2;
		this.groupNumber = groupNumber;
		this.groupName = groupName;
	}

	public MatchInfo(int matchNumber, int groupNumber, String groupName, @NonNull Stage tournamentStage) {
		this.matchNumber = matchNumber;
		this.tournamentStage = tournamentStage;
		this.groupNumber = groupNumber;
		this.groupName = groupName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMatchNumber() {
		return matchNumber;
	}

	public int getMatchID() {
		return matchID;
	}

	public void setMatchID(int matchID) {
		this.matchID = matchID;
	}

	public Stage getTournamentStage() {
		return tournamentStage;
	}

	public int getGroupNumber() {
		return groupNumber;
	}

	public String getGroupName() {
		return groupName;
	}

	public Team getTeam1() {
		return team1;
	}

	public void setTeam1(Team team1) {
		this.team1 = team1;
	}

	public Team getTeam2() {
		return team2;
	}

	public void setTeam2(Team team2) {
		this.team2 = team2;
	}

	public boolean hasStarted() {
		return hasStarted;
	}

	public void setHasStarted(boolean hasStarted) {
		this.hasStarted = hasStarted;
	}

	public String getMatchDate() {
		return matchDate;
	}

	public void setMatchDate(String matchDate) {
		this.matchDate = matchDate;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean complete) {
		isComplete = complete;
	}

	public Team getWinningTeam() {
		return winningTeam;
	}

	public void setWinningTeam(Team winningTeam) {
		this.winningTeam = winningTeam;
	}
}
