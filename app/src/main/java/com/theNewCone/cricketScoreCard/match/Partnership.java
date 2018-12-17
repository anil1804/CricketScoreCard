package com.theNewCone.cricketScoreCard.match;

import com.theNewCone.cricketScoreCard.enumeration.ExtraType;
import com.theNewCone.cricketScoreCard.player.Player;
import com.theNewCone.cricketScoreCard.scorecard.Extra;

import java.io.Serializable;

public class Partnership implements Serializable {
	private Player player1, player2;
	private int p1BallsPlayed, p2BallsPlayed, p1RunsScored, p2RunsScored, totalBallsPlayed, startScore, currScore, extras;
	private double overForWicket = -1;
	private boolean unBeaten = false;

	Partnership(Player player1, Player player2, int startScore) {
		this.player1 = player1;
		this.player2 = player2;
		this.startScore = startScore;
		this.currScore = startScore;

		this.p1BallsPlayed = 0;
		this.p1RunsScored = 0;
		this.p2BallsPlayed = 0;
		this.p2RunsScored = 0;
		this.extras = 0;

		totalBallsPlayed = 0;
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

	public int getPartnership() {
		return currScore - startScore;
	}

	public int getEndScore() {
		return currScore;
	}

	public boolean isUnBeaten() {
		return unBeaten;
	}

	public int getExtras() {
		return extras;
	}

	void setUnBeaten() {
		this.unBeaten = true;
	}

	void updatePlayerStats(Player player, int ballsPlayed, int runsScored, boolean isOut, int currScore, String overForWicket, Extra extra) {
		if(player.getID() == player1.getID()) {
			p1RunsScored += runsScored;
			p1BallsPlayed += ballsPlayed;
		} else if(player.getID() == player2.getID()) {
			p2RunsScored += runsScored;
			p2BallsPlayed += ballsPlayed;
		}

		totalBallsPlayed += ballsPlayed;
		this.currScore = currScore;

		if(extra != null) {
			extras += extra.getRuns();
			if (extra.getType() == ExtraType.NO_BALL || extra.getType() == ExtraType.WIDE)
				extras++;
		}

		if(isOut) {
			updateCurrentPartnershipData(currScore, false);

			if(overForWicket != null)
				this.overForWicket = Double.parseDouble(overForWicket);
		}
	}

	private void updateCurrentPartnershipData(int endScore, boolean isUnbeaten) {
		this.currScore = endScore;
		this.unBeaten = isUnbeaten;
	}
}
