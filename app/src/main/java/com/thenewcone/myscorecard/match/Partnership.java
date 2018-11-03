package com.thenewcone.myscorecard.match;

import com.thenewcone.myscorecard.player.Player;

import java.io.Serializable;

public class Partnership implements Serializable {
	private Player player1, player2;
	private int p1BallsPlayed, p2BallsPlayed, p1RunsScored, p2RunsScored, totalBallsPlayed, totalRunsScored, startScore, endScore;
	private double overForWicket = -1;
	private boolean unBeaten = false;

	Partnership(Player player1, Player player2, int startScore) {
		this.player1 = player1;
		this.player2 = player2;
		this.startScore = startScore;
	}

	public Player getPlayer1() {
		return player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public int getP1BallsPlayed() {
		return p1BallsPlayed;
	}

	public int getP2BallsPlayed() {
		return p2BallsPlayed;
	}

	public int getP1RunsScored() {
		return p1RunsScored;
	}

	public int getP2RunsScored() {
		return p2RunsScored;
	}

	public int getTotalBallsPlayed() {
		return totalBallsPlayed;
	}

	public int getTotalRunsScored() {
		return totalRunsScored;
	}

	public int getStartScore() {
		return startScore;
	}

	public int getEndScore() {
		return endScore;
	}

	public boolean isUnBeaten() {
		return unBeaten;
	}

	void setUnBeaten(boolean unBeaten) {
		this.unBeaten = unBeaten;
	}

	void updatePlayerStats(Player player, int ballsPlayed, int runsScored, boolean isOut, int endScore, String overForWicket) {
		if(player.getID() == player1.getID()) {
			p1RunsScored += runsScored;
			p1BallsPlayed += ballsPlayed;
		} else if(player.getID() == player2.getID()) {
			p2RunsScored += runsScored;
			p2BallsPlayed += ballsPlayed;
		}

		totalBallsPlayed += ballsPlayed;
		totalRunsScored += runsScored;

		if(isOut) {
			this.endScore = endScore;
			this.overForWicket = Double.parseDouble(overForWicket);
		}
	}
}
