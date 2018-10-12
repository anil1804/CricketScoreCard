package com.thenewcone.myscorecard.match;

import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.thenewcone.myscorecard.player.BatsmanStats;
import com.thenewcone.myscorecard.player.BowlerStats;
import com.thenewcone.myscorecard.scorecard.Extra;
import com.thenewcone.myscorecard.utils.CommonUtils;

import java.util.HashMap;

public class CricketCard {
	private int maxWickets;

	private HashMap<String, BowlerStats> bowlerMap = new HashMap<>();
	private SparseArray<BatsmanStats> batsmen = new SparseArray<>();

	private int maxBowlers, maxPerBowler;
	private boolean inningsComplete;

	private int futurePenalty;

	private CricketCard prevInningsCard;

	private int score;
	private int target = -1;
	private int wicketsFallen;

	private int innings;
	private int byes, legByes, wides, noBalls, penalty;
	private double runRate, reqRate = -1.00;
	private String battingTeam, maxOvers, totalOversBowled;

	public int getScore() {
		return score;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public int getWicketsFallen() {
		return wicketsFallen;
	}

	public void incWicketsFallen() {
		this.wicketsFallen++;
	}

	public int getInnings() {
		return innings;
	}

	public int getByes() {
		return byes;
	}

	public int getLegByes() {
		return legByes;
	}

	public int getWides() {
		return wides;
	}

	public int getNoBalls() {
		return noBalls;
	}

	public int getPenalty() {
		return penalty;
	}

	public double getRunRate() {
		return runRate;
	}

	public double getReqRate() {
		return reqRate;
	}

	public String getBattingTeam() {
		return battingTeam;
	}

	public String getMaxOvers() {
		return maxOvers;
	}

	public String getTotalOversBowled() {
		return totalOversBowled;
	}

	public void updateTotalOversBowled() {
		this.totalOversBowled = incrementOvers(totalOversBowled);
	}

	public HashMap<String, BowlerStats> getBowlerMap() {
		return bowlerMap;
	}

	public void updateBowlerInBowlerMap(BowlerStats bowler) {
		this.bowlerMap.remove(bowler.getBowlerName());
		this.bowlerMap.put(bowler.getBowlerName(), bowler);
	}

	public SparseArray<BatsmanStats> getBatsmen() {
		return batsmen;
	}

	public void appendToBatsmen(BatsmanStats batsman) {
		this.batsmen.append(batsman.getPosition(), batsman);
	}

	public int getFuturePenalty() {
		return futurePenalty;
	}

	public void addFuturePenalty(int futurePenalty) {
		this.futurePenalty += futurePenalty;
	}

	public CricketCard getPrevInningsCard() {
		return prevInningsCard;
	}

	public CricketCard(String battingTeam, String maxOvers, int maxBowlers, int maxPerBowler, int maxWickets, int innings) {
		this.battingTeam = battingTeam;
		this.maxOvers = maxOvers.indexOf(",") >  0 ? maxOvers : maxOvers + ".0";
		this.maxBowlers = maxBowlers;
		this.maxPerBowler = maxPerBowler;
		this.maxWickets = maxWickets;
		this.innings = innings;
		this.totalOversBowled = "0.0";
	}

	public void addWides(int wides) {
		this.wides += wides;
	}

	public void incNoBalls() {
		this.noBalls++;
	}

	public void addByes(int byes) {
		this.byes += byes;
	}

	public void addLegByes(int legByes) {
		this.legByes += legByes;
	}

	public void addPenalty(int runs) {
		this.penalty += runs;
	}

	public String incrementOvers(String oversBowled) {
		String[] overDetails = oversBowled.split("\\.");
		int overs = Integer.parseInt(overDetails[0]), balls = Integer.parseInt(overDetails[1]);

		balls += 1;
		if(balls == 6) {
			overs++;
			balls = 0;
		}

		return overs + "." + balls;
	}

	public void inningsCheck() {
		if(totalOversBowled.equals(maxOvers) || wicketsFallen == maxWickets) {
			inningsComplete = true;
			if(innings == 1) {
				innings++;
				target = score;
			}
		}
	}

	public void updateScore(int runsScored, @Nullable Extra extra) {
		int runs = runsScored;
		if(extra != null) {
			runs += extra.getRuns();
			if(extra.getType() == Extra.ExtraType.WIDE || extra.getType() == Extra.ExtraType.NO_BALL)
				runs++;
		}

		score += runs;
	}

	public void updateRunRate(){
		runRate = CommonUtils.calcRunRate(score, Double.parseDouble(totalOversBowled));
		if(innings == 2) {
			reqRate = CommonUtils.calReqRate(score, Double.parseDouble(totalOversBowled), target, Double.parseDouble(maxOvers));
		}
	}
}