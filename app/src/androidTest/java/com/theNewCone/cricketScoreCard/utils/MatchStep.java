package com.theNewCone.cricketScoreCard.utils;

import com.theNewCone.cricketScoreCard.enumeration.AutoScoreType;
import com.theNewCone.cricketScoreCard.enumeration.DismissalType;
import com.theNewCone.cricketScoreCard.enumeration.ExtraType;

class MatchStep {
	private int runs, extraRuns;
	private boolean isWicket, isExtra;
	private DismissalType wicketType;
	private ExtraType extraType, extraSubType;
	private String batsman, bowler, player;
	private AutoScoreType scoreType;
	private String pomTeam, pom, penaltyAwardedTo;

	int getRuns() {
		return runs;
	}

	int getExtraRuns() {
		return extraRuns;
	}

	boolean isWicket() {
		return isWicket;
	}

	boolean isExtra() {
		return isExtra;
	}

	DismissalType getWicketType() {
		return wicketType;
	}

	ExtraType getExtraType() {
		return extraType;
	}

	ExtraType getExtraSubType() {
		return extraSubType;
	}

	String getBatsman() {
		return batsman;
	}

	String getBowler() {
		return bowler;
	}

	String getPlayer() {
		return player;
	}

	AutoScoreType getScoreType() {
		return scoreType;
	}

	String getPomTeam() {
		return pomTeam;
	}

	String getPom() {
		return pom;
	}

	String getPenaltyAwardedTo() {
		return penaltyAwardedTo;
	}

	void newBallBowled(int runs, boolean isExtra, ExtraType extraType, ExtraType extraSubType, int extraRuns, boolean isWicket, DismissalType wicketType, String outBatsman, String fielder) {
		this.scoreType = AutoScoreType.BALL;
		this.runs = runs;
		this.isExtra = isExtra;
		this.extraType = extraType;
		this.extraSubType = extraSubType;
		this.extraRuns = extraRuns;

		this.isWicket = isWicket;
		this.wicketType = wicketType;
		this.batsman = outBatsman;
		this.player = fielder;
	}

	void selectBatsman(String batsmanName) {
		this.scoreType = AutoScoreType.BATSMAN;
		this.batsman = batsmanName;
	}

	void selectFacing(String batsmanName) {
		this.scoreType = AutoScoreType.FACING;
		this.batsman = batsmanName;
	}

	void selectBowler(String bowlerName) {
		this.scoreType = AutoScoreType.BOWLER;
		this.bowler = bowlerName;
	}

	void startNextInnings() {
		this.scoreType = AutoScoreType.NEXT_INNINGS;
	}

	void recordPOM(String pomTeam, String pom) {
		this.scoreType = AutoScoreType.PLAYER_OF_MATCH;
		this.pomTeam = pomTeam;
		this.pom = pom;
	}

	void completeMatch() {
		this.scoreType = AutoScoreType.COMPLETE_MATCH;
	}

	void recordPenalty(String teamAwaredeTo) {
		this.scoreType = AutoScoreType.PENALTY;
		this.penaltyAwardedTo = teamAwaredeTo;
	}

	void recordCancellation(int numRuns) {
		this.scoreType = AutoScoreType.CANCEL;
		this.runs = numRuns;
	}
}
