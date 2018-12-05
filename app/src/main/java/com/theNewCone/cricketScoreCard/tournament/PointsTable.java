package com.theNewCone.cricketScoreCard.tournament;

import com.theNewCone.cricketScoreCard.match.MatchResult;
import com.theNewCone.cricketScoreCard.match.Team;

class PointsTable {
	private Team team;
	private int maxOvers, maxWickets, tempMaxOversBowled = -1, tempMaxOversPlayed = -1;
	private int played, won, lost, tied, noResult, points;
	private int totalRunsScored, totalsRunsGiven;
	private double totalOversPlayed, totalOversBowled, netRunRate;

	public PointsTable(Team team, int maxOvers, int maxWickets) {
		this.team = team;
		this.maxOvers = maxOvers;
		this.maxWickets = maxWickets;
	}

	public int getPlayed() {
		return played;
	}

	public int getWon() {
		return won;
	}

	public int getLost() {
		return lost;
	}

	public int getTied() {
		return tied;
	}

	public int getNoResult() {
		return noResult;
	}

	public int getPoints() {
		return points;
	}

	public double getNetRunRate() {
		return netRunRate;
	}

	public void updateMaxOvers(int maxOversBowled, int maxOverPlayed) {
		this.tempMaxOversBowled = maxOversBowled;
		this.tempMaxOversPlayed = maxOverPlayed;
	}

	public void addPlayedMatchData(MatchResult result, int runsScored, String oversPlayed, int wicketsLost,
								   int runsGiven, String oversBowled, int wicketsTaken) {
		this.totalRunsScored += runsScored;
		this.totalsRunsGiven += runsGiven;

		oversPlayed = (wicketsLost == maxWickets) ?
				(tempMaxOversPlayed > 0 ? String.valueOf(tempMaxOversPlayed) : String.valueOf(maxOvers)) :
				oversPlayed;
		oversBowled = (wicketsTaken == maxWickets) ?
				(tempMaxOversBowled > 0 ? String.valueOf(tempMaxOversBowled) : String.valueOf(maxOvers)) :
				oversBowled;

		this.totalOversPlayed = addOvers(totalOversPlayed, Double.parseDouble(oversPlayed));
		this.totalOversBowled = addOvers(totalOversBowled, Double.parseDouble(oversBowled));

		this.played += 1;
		this.won += (result == MatchResult.WON) ? 1 : 0;
		this.lost += (result == MatchResult.LOST) ? 1 : 0;
		this.noResult += (result == MatchResult.NO_RESULT) ? 1 : 0;
		this.tied += (result == MatchResult.TIED) ? 1 : 0;
		this.netRunRate = ((double) totalRunsScored) / totalOversPlayed - ((double) totalsRunsGiven) / totalOversBowled;

		switch (result) {
			case WON:
				this.points += 3;
				break;

			case NO_RESULT:
			case TIED:
				this.points += 1;
				break;
		}
	}

	private double addOvers(double overs1, double overs2) {
		int ballsInOver1 = (int) (overs1 * 10) % 10;
		int oversInOver1 = (int) (overs1 % 1);
		int ballsInOver2 = (int) (overs2 * 10) % 10;
		int oversInOver2 = (int) (overs2 % 1);

		int totalOvers = oversInOver1 + oversInOver2;
		int totalBalls = ballsInOver1 + ballsInOver2;

		String overs = String.valueOf(totalOvers + (totalBalls / 6)) + "." + String.valueOf(totalBalls % 6);

		return Double.parseDouble(overs);
	}
}
