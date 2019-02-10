package com.theNewCone.cricketScoreCard.player;

import com.theNewCone.cricketScoreCard.enumeration.DismissalType;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;

import java.io.Serializable;

public class BatsmanStats implements Serializable {
	private int ballsPlayed;

	private int runsScored;
	private int position;
	private int num4s;
	private int num6s;

	private int dots, singles, twos, threes, fives, sevens;
	private double strikeRate;
	private boolean notOut = true;
	private Player wicketEffectedBy, player;
	private DismissalType dismissalType;
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

	public int getDots() {
		return dots;
	}

	public int getSingles() {
		return singles;
	}

	public int getTwos() {
		return twos;
	}

	public int getThrees() {
		return threes;
	}

	public int getFives() {
		return fives;
	}

	public int getSevens() {
		return sevens;
	}

	public double getStrikeRate() {
		return strikeRate;
	}

	public Player getPlayer() {
	    return player;
    }

	public boolean isNotOut() {
		return notOut;
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

	public DismissalType getDismissalType() {
		return dismissalType;
	}

	public void setDismissalType(DismissalType dismissalType) {
		this.dismissalType = dismissalType;
	}

	public BatsmanStats(Player player, int position) {
		this.player = player;
		this.position = position;
		this.strikeRate = CommonUtils.getStrikeRate(runsScored, ballsPlayed);
	}

	public void addDot() {
		dots++;
	}

	public void addSingle(boolean isCancel) {
		singles += isCancel ? -1 : 1;
		runsScored += isCancel ? -1 : 1;
	}

	public void addTwos(boolean isCancel) {
		twos += isCancel ? -1 : 1;
		runsScored += isCancel ? -2 : 2;
	}

	public void addThrees(boolean isCancel) {
		threes += isCancel ? -1 : 1;
		runsScored += isCancel ? -3 : 3;
	}

	public void addFours(boolean isCancel) {
		num4s += isCancel ? -1 : 1;
		runsScored += isCancel ? -4 : 4;
	}

	public void addSixes(boolean isCancel) {
		num6s += isCancel ? -1 : 1;
		runsScored += isCancel ? -6 : 6;
	}

	public void addFives(boolean isCancel) {
		fives += isCancel ? -1 : 1;
		runsScored += isCancel ? -5 : 5;
	}

	public void addSevens(boolean isCancel) {
		sevens += isCancel ? -1 : 1;
		runsScored += isCancel ? -7 : 7;
	}

	public void addOthers(int runs, boolean isCancel) {
		runsScored += isCancel ? (runs * -1) : runs;
	}

	public void evaluateStrikeRate() {
		this.strikeRate = CommonUtils.getStrikeRate(runsScored, ballsPlayed);
	}
}
