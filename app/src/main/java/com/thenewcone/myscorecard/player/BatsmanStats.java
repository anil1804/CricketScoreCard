package com.thenewcone.myscorecard.player;

import com.thenewcone.myscorecard.scorecard.WicketData;
import com.thenewcone.myscorecard.utils.CommonUtils;

import java.io.Serializable;

public class BatsmanStats implements Serializable {
	private int ballsPlayed;

	private int runsScored;
	private int position;
	private int num4s;
	private int num6s;

	private int dots, singles, twos, threes;
	private double strikeRate;
	private boolean notOut = true;
	private Player wicketEffectedBy, player;
	private WicketData.DismissalType dismissalType;
	private BowlerStats wicketTakenBy;

	public String getBatsmanName() {
		return player.getName();
	}

	public void incBallsPlayed(int ballsPlayed) {
		 this.ballsPlayed += ballsPlayed;
	}

	public int getBallsPlayed() {
		return ballsPlayed;
	}

	public int getRunsScored() {
		return runsScored;
	}

	public int getPosition() {
		return position;
	}

	public int getNum4s() {
		return num4s;
	}

	public int getNum6s() {
		return num6s;
	}

	public double getStrikeRate() {
		return strikeRate;
	}

	public Player getPlayer() {
	    return player;
    }

	public void setNotOut(boolean notOut) {
		this.notOut = notOut;
	}

	public Player getWicketEffectedBy() {
		return wicketEffectedBy;
	}

	public void setWicketEffectedBy(Player wicketEffectedBy) {
		this.wicketEffectedBy = wicketEffectedBy;
	}

	public BowlerStats getWicketTakenBy() {
		return wicketTakenBy;
	}

	public void setWicketTakenBy(BowlerStats wicketTakenBy) {
		this.wicketTakenBy = wicketTakenBy;
	}

	public WicketData.DismissalType getDismissalType() {
		return dismissalType;
	}

	public void setDismissalType(WicketData.DismissalType dismissalType) {
		this.dismissalType = dismissalType;
	}

	public BatsmanStats(Player player, int position) {
		this.player = player;
		this.position = position;
		this.strikeRate = CommonUtils.getStrikeRate(runsScored, ballsPlayed);
	}
	public void addDot() {
		++dots;
	}

	public void addSingle() {
		++singles;
		runsScored += 1;
	}

	public void addTwos() {
		++twos;
		runsScored += 2;
	}

	public void addThrees() {
		++threes;
		runsScored += 3;
	}

	public void addFours() {
		++num4s;
		runsScored += 4;
	}

	public void addSixes() {
		++num6s;
		runsScored += 6;
	}

	public void evaluateStrikeRate() {
		this.strikeRate = CommonUtils.getStrikeRate(runsScored, ballsPlayed);
	}
}
