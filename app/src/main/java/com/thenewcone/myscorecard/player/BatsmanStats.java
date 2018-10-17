package com.thenewcone.myscorecard.player;

import com.thenewcone.myscorecard.utils.CommonUtils;

public class BatsmanStats {
	private String batsmanName;
	private int ballsPlayed;

	private int runsScored;
	private int position;
	private int num4s;
	private int num6s;

	private int dots, singles, twos, threes;
	private double strikeRate;
	private boolean notOut = true;
	private Player wicketEffectedBy;
	private BowlerStats wicketTakenBy;

	public String getBatsmanName() {
		return batsmanName;
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

	public void setNotOut(boolean notOut) {
		this.notOut = notOut;
	}

	public void setWicketEffectedBy(Player wicketEffectedBy) {
		this.wicketEffectedBy = wicketEffectedBy;
	}

	public void setWicketTakenBy(BowlerStats wicketTakenBy) {
		this.wicketTakenBy = wicketTakenBy;
	}


	public BatsmanStats(String batsmanName, int position) {
		this.batsmanName = batsmanName;
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
