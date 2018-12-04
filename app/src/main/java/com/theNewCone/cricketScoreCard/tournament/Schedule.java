package com.theNewCone.cricketScoreCard.tournament;

import android.content.Context;

import com.theNewCone.cricketScoreCard.match.CricketCardUtils;
import com.theNewCone.cricketScoreCard.match.Team;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;
import com.theNewCone.cricketScoreCard.utils.database.DatabaseHandler;

import java.io.Serializable;

public class Schedule implements Serializable {
	private int matchNumber, matchID;
	private Stage tournamentStage;
	private Team team1, team2;
	private boolean isComplete;

	private DatabaseHandler dbh;

	public Schedule(Context context, int matchNumber, Stage tournamentStage, Team team1, Team team2) {
		this.matchNumber = matchNumber;
		this.tournamentStage = tournamentStage;
		this.team1 = team1;
		this.team2 = team2;

		dbh = new DatabaseHandler(context);
	}

	public Schedule(Context context, int matchNumber, Stage tournamentStage) {
		this.matchNumber = matchNumber;
		this.tournamentStage = tournamentStage;

		dbh = new DatabaseHandler(context);
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

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean complete) {
		isComplete = complete;
	}

	public CricketCardUtils getMatchData() {
		String matchJson = dbh.getCompletedMatch(matchID);
		CricketCardUtils ccUtils = null;
		if (matchJson != null)
			ccUtils = CommonUtils.convertToCCUtils(matchJson);

		return ccUtils;
	}

	public enum Stage {
		GROUP, ROUND_1, ROUND_2, ROUND_3, SUPER_SIX, SUPER_FOUR, QUARTER_FINAL, SEMI_FINAL, FINAL,
		QUALIFIER_1, ELIMINATOR_1, ELIMINATOR_2
	}
}
