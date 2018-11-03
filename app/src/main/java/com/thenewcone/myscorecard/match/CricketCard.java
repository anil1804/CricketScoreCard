package com.thenewcone.myscorecard.match;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.thenewcone.myscorecard.player.BatsmanStats;
import com.thenewcone.myscorecard.player.BowlerStats;
import com.thenewcone.myscorecard.player.Player;
import com.thenewcone.myscorecard.scorecard.Extra;
import com.thenewcone.myscorecard.scorecard.WicketData;
import com.thenewcone.myscorecard.utils.CommonUtils;

import java.io.Serializable;
import java.util.HashMap;

public class CricketCard implements Serializable {
	private int maxWickets;

	private int maxPerBowler;
	private boolean inningsComplete;

    private int futurePenalty;

	private int score;
	private int target = -1;
	private int wicketsFallen;

	private int innings;
	private int byes, legByes, wides, noBalls, penalty;
	private double runRate, reqRate = -1.00;
	private String maxOvers, totalOversBowled;

	private Team battingTeam, bowlingTeam;

	private HashMap<String, BowlerStats> bowlerMap = new HashMap<>();
	private HashMap<Integer, BatsmanStats> batsmen = new HashMap<>();

	private HashMap<Integer, Partnership> partnershipData = new HashMap<>();
	private HashMap<Integer, OverInfo> overInfoData = new HashMap<>();
	private OverInfo currOver;
	private Partnership currPartnership;
	private int currBallNum;

	public int getScore() {
		return score;
	}

	public int getTarget() {
		return target;
	}

	void setTarget(int target) {
		this.target = target;
	}

	public int getWicketsFallen() {
		return wicketsFallen;
	}

	void incWicketsFallen() {
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

	public String getBattingTeamName() {
		return battingTeam.getShortName();
	}

	int getMaxWickets() {
	    return maxWickets;
    }

	public String getMaxOvers() {
		return maxOvers;
	}

	public String getTotalOversBowled() {
		return totalOversBowled;
	}

    public int getMaxPerBowler() {
        return maxPerBowler;
    }

    public boolean isInningsComplete() {
        return inningsComplete;
    }

    void updateTotalOversBowled() {
		this.totalOversBowled = incrementOvers(totalOversBowled);
	}

	public Team getBattingTeam() {
		return battingTeam;
	}

	public Team getBowlingTeam() {
		return bowlingTeam;
	}

	public HashMap<String, BowlerStats> getBowlerMap() {
		return bowlerMap;
	}

	void updateBowlerInBowlerMap(BowlerStats bowler) {
		this.bowlerMap.remove(bowler.getBowlerName());
		this.bowlerMap.put(bowler.getBowlerName(), bowler);
	}

	public HashMap<Integer, BatsmanStats> getBatsmen() {
		return batsmen;
	}

	void appendToBatsmen(BatsmanStats batsman) {
		this.batsmen.put(batsman.getPosition(), batsman);
	}

	void updateBatsmenData (BatsmanStats batsmanStats) {
	    this.batsmen.remove(batsmanStats.getPosition());
	    appendToBatsmen(batsmanStats);
    }

    int getFuturePenalty() {
		return futurePenalty;
	}

	void addFuturePenalty(int futurePenalty) {
		this.futurePenalty += futurePenalty;
	}

	public HashMap<Integer, Partnership> getPartnershipData() {
		return partnershipData;
	}

	public HashMap<Integer, OverInfo> getOverInfoData() {
		return overInfoData;
	}

	public CricketCard(Team battingTeam, Team bowlingTeam, String maxOvers, int maxPerBowler, int maxWickets, int innings) {
		this.battingTeam = battingTeam;
		this.bowlingTeam = bowlingTeam;
		this.maxOvers = maxOvers.indexOf(".") >  0 ? maxOvers : maxOvers + ".0";
		this.maxPerBowler = maxPerBowler;
		this.maxWickets = maxWickets;
		this.innings = innings;
		this.totalOversBowled = "0.0";
		inningsComplete = false;
	}

	void addWides(int wides) {
		this.wides += wides;
	}

	void incNoBalls() {
		this.noBalls++;
	}

	void addByes(int byes) {
		this.byes += byes;
	}

	void addLegByes(int legByes) {
		this.legByes += legByes;
	}

	void addPenalty(int runs) {
		this.penalty += runs;
	}

	String incrementOvers(String oversBowled) {
		String[] overDetails = oversBowled.split("\\.");
		int overs = Integer.parseInt(overDetails[0]), balls = Integer.parseInt(overDetails[1]);

		balls += 1;
		if(balls == 6) {
			overs++;
			balls = 0;
		}

		return overs + "." + balls;
	}

	void inningsCheck() {
		if(innings == 1) {
			if (totalOversBowled.equals(maxOvers) || wicketsFallen == maxWickets) {
				inningsComplete = true;
			}
		} else if(innings == 2) {
			if (totalOversBowled.equals(maxOvers) || wicketsFallen == maxWickets || score >= target) {
				inningsComplete = true;
			}
		}
	}

	void updateScore(int runsScored, @Nullable Extra extra) {
		int runs = runsScored;
		if(extra != null) {
			runs += extra.getRuns();
			if(extra.getType() == Extra.ExtraType.WIDE || extra.getType() == Extra.ExtraType.NO_BALL)
				runs++;
		}

		score += runs;
	}

	void updateRunRate(){
		runRate = CommonUtils.calcRunRate(score, Double.parseDouble(totalOversBowled));
		if(innings == 2) {
			reqRate = CommonUtils.calReqRate(score, Double.parseDouble(totalOversBowled), target, Double.parseDouble(maxOvers));
		}
	}

	void addNewPartnershipRecord(@NonNull BatsmanStats batsman1, @NonNull BatsmanStats batsman2) {
		int key = partnershipData.size() + 1;
		Player player1 = (batsman2.getRunsScored() == 0) ? batsman1.getPlayer() : batsman2.getPlayer();
		Player player2 = (batsman1.getPlayer().getID() != player1.getID()) ? batsman1.getPlayer() : batsman2.getPlayer();

		currPartnership = new Partnership(player1, player2, score);
	}

	void updatePartnership(BatsmanStats batsman, int ballsPlayed, int runsScored, boolean isOut) {
		currPartnership.updatePlayerStats(batsman.getPlayer(), ballsPlayed, runsScored, isOut, score, totalOversBowled);
		if(isOut) {
			partnershipData.put(wicketsFallen, currPartnership);
		} else if(inningsComplete) {
			currPartnership.setUnBeaten(true);
			partnershipData.put(wicketsFallen + 1, currPartnership);
		}
	}

	void addNewOver(boolean isPrevOverMaiden) {
		OverInfo overInfo = new OverInfo();
		if(currOver != null) {
			currOver.setMaiden(isPrevOverMaiden);
			overInfoData.put((int) Double.parseDouble(totalOversBowled), currOver);
		}
		currOver = overInfo;

		currBallNum = 0;
	}

	void addNewBall(int runsScored, BowlerStats bowler, Extra extra, WicketData wicketData) {
		if(extra != null
				&& extra.getType() != Extra.ExtraType.NO_BALL
				&& extra.getType() != Extra.ExtraType.WIDE) {
			currBallNum++;
		}

		BallInfo ballInfo = new BallInfo(currBallNum, runsScored, extra, wicketData, bowler.getPlayer());
		currOver.newBallBowled(ballInfo);
	}
}