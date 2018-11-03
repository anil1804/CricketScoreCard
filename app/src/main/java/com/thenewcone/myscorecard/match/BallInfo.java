package com.thenewcone.myscorecard.match;

import com.thenewcone.myscorecard.player.Player;
import com.thenewcone.myscorecard.scorecard.Extra;
import com.thenewcone.myscorecard.scorecard.WicketData;

import java.io.Serializable;

public class BallInfo implements Serializable {
	private int ballNumber, runsScored;
	private Extra extra;
	private WicketData wicketData;
	private Player bowler;

	BallInfo(int ballNumber, int runsScored, Extra extra, WicketData wicketData, Player bowler) {
		this.ballNumber = ballNumber;
		this.runsScored = runsScored;
		this.extra = extra;
		this.wicketData = wicketData;
		this.bowler = bowler;
	}

	public int getBallNumber() {
		return ballNumber;
	}

	public int getRunsScored() {
		return runsScored;
	}

	public Extra getExtra() {
		return extra;
	}

	WicketData getWicketData() {
		return wicketData;
	}

	public Player getBowler() {
		return bowler;
	}
}
