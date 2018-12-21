package com.theNewCone.cricketScoreCard.tournament;

import com.theNewCone.cricketScoreCard.enumeration.MatchResult;
import com.theNewCone.cricketScoreCard.match.Team;

import java.io.Serializable;

public class PointsData implements Serializable {
	private Team team;
	private int id, maxOvers, maxWickets, tempMaxOversBowled = -1, tempMaxOversPlayed = -1;
	private int played, won, lost, tied, noResult, points = 0;
	private int totalRunsScored, totalsRunsGiven, totalWicketsLost, totalWicketsTaken;
	private double totalOversPlayed, totalOversBowled, netRunRate = 0.0;

	PointsData(Team team, int maxOvers, int maxWickets) {
		this.team = team;
		this.maxOvers = maxOvers;
		this.maxWickets = maxWickets;
	}

	public PointsData(int id, Team team, int maxOvers, int maxWickets,
					  int played, int won, int lost, int tied, int noResult, double netRunRate,
					  int totalRunsScored, int totalsRunsGiven, int totalWicketsLost,
					  int totalWicketsTaken, double totalOversPlayed, double totalOversBowled) {
		this.team = team;
		this.id = id;
		this.maxOvers = maxOvers;
		this.maxWickets = maxWickets;
		this.played = played;
		this.won = won;
		this.lost = lost;
		this.tied = tied;
		this.noResult = noResult;
		this.totalRunsScored = totalRunsScored;
		this.totalsRunsGiven = totalsRunsGiven;
		this.totalWicketsLost = totalWicketsLost;
		this.totalWicketsTaken = totalWicketsTaken;
		this.totalOversPlayed = totalOversPlayed;
		this.totalOversBowled = totalOversBowled;
		this.netRunRate = netRunRate;
		this.points = calculatePoints();
	}

	public Team getTeam() {
		return team;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMaxOvers() {
		return maxOvers;
	}

	public int getMaxWickets() {
		return maxWickets;
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

	public int getTotalRunsScored() {
		return totalRunsScored;
	}

	public int getTotalsRunsGiven() {
		return totalsRunsGiven;
	}

	public double getTotalOversPlayed() {
		return totalOversPlayed;
	}

	public double getTotalOversBowled() {
		return totalOversBowled;
	}

	public int getTotalWicketsLost() {
		return totalWicketsLost;
	}

	public int getTotalWicketsTaken() {
		return totalWicketsTaken;
	}

	public void updateMaxOvers(int maxOversBowled, int maxOverPlayed) {
		this.tempMaxOversBowled = maxOversBowled;
		this.tempMaxOversPlayed = maxOverPlayed;
	}

	public void addPlayedMatchData(MatchResult result, int runsScored, String oversPlayed, int wicketsLost,
								   int runsGiven, String oversBowled, int wicketsTaken) {
		this.totalRunsScored += runsScored;
		this.totalsRunsGiven += runsGiven;
		this.totalWicketsLost += wicketsLost;
		this.totalWicketsTaken += wicketsTaken;

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

		this.points = calculatePoints();
	}

	private double addOvers(double overs1, double overs2) {
		int ballsInOver1 = (int) (overs1 * 10) % 10;
		int oversInOver1 = (int) (overs1 / 1);
		int ballsInOver2 = (int) (overs2 * 10) % 10;
		int oversInOver2 = (int) (overs2 / 1);

		int totalOvers = oversInOver1 + oversInOver2;
		int totalBalls = ballsInOver1 + ballsInOver2;

		String overs = String.valueOf(totalOvers + (totalBalls / 6)) + "." + String.valueOf(totalBalls % 6);

		return Double.parseDouble(overs);
	}

	private int calculatePoints() {
		return (3 * won) + noResult + tied;
	}
}
