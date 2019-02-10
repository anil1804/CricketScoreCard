package com.theNewCone.cricketScoreCard.match;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.theNewCone.cricketScoreCard.enumeration.ExtraType;
import com.theNewCone.cricketScoreCard.player.BatsmanStats;
import com.theNewCone.cricketScoreCard.player.BowlerStats;
import com.theNewCone.cricketScoreCard.player.FielderStats;
import com.theNewCone.cricketScoreCard.player.Player;
import com.theNewCone.cricketScoreCard.scorecard.Extra;
import com.theNewCone.cricketScoreCard.scorecard.WicketData;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CricketCard implements Serializable {
	private int maxWickets;

	private int maxPerBowler;
	private boolean inningsComplete;

    private int futurePenalty;

	private int score;
	private int target = -1;
	private int wicketsFallen;

	private int innings, currBallNum;
	private int byes, legByes, wides, noBalls, penalty;
	private double runRate, reqRate = -1.00;
	private String maxOvers, totalOversBowled;
	private boolean incrementNextBallNumber = true;

	private Team battingTeam, bowlingTeam;

	private HashMap<String, BowlerStats> bowlerMap = new HashMap<>();
	private HashMap<Integer, BatsmanStats> batsmen = new HashMap<>();
	private HashMap<Integer, FielderStats> fielderMap = new HashMap<>();

	private List<Partnership> partnershipData = new ArrayList<>();
	private List<OverInfo> overInfoData = new ArrayList<>();
	private OverInfo currOver = null;
	private Partnership currPartnership;

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

	public Partnership getCurrPartnership() {
		return currPartnership;
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

	public HashMap<Integer, FielderStats> getFielderMap() {
		return fielderMap;
	}

	void updateFielderInMap(@NonNull FielderStats fielderStats) {
		this.fielderMap.remove(fielderStats.getPlayer().getID());
		this.fielderMap.put(fielderStats.getPlayer().getID(), fielderStats);
	}

    int getFuturePenalty() {
		return futurePenalty;
	}

	void addFuturePenalty(int futurePenalty) {
		this.futurePenalty += futurePenalty;
	}

	public List<Partnership> getPartnershipData() {
		return partnershipData;
	}

	public List<OverInfo> getOverInfoData() {
		return overInfoData;
	}

	public OverInfo getCurrOver() {
		return currOver;
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

	void addWides(int wides, boolean isCancel) {
		this.wides += isCancel ? (wides * -1) : wides;
	}

	void incNoBalls() {
		this.noBalls++;
	}

	void addByes(int byes, boolean isCancel) {
		this.byes += isCancel ? (byes * -1) : byes;
	}

	void addLegByes(int legByes, boolean isCancel) {
		this.legByes += isCancel ? (legByes * -1) : legByes;
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
		switch (innings) {
			case 1:
				if (totalOversBowled.equals(maxOvers) || wicketsFallen == maxWickets) {
					inningsComplete = true;
				}
				break;

			case 2:
				if (totalOversBowled.equals(maxOvers) || wicketsFallen == maxWickets || score >= target) {
					inningsComplete = true;
				}
				break;
		}
	}

	void updateScore(int runsScored, @Nullable Extra extra) {
		int runs = runsScored;
		if(extra != null) {
			boolean isCancel = extra.getType() == ExtraType.CANCEL;

			if (isCancel)
				runs = runs * -1;

			runs += isCancel ? (extra.getRuns() * -1) : extra.getRuns();

			if (extra.getType() == ExtraType.WIDE || extra.getType() == ExtraType.NO_BALL)
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
		Player player1 = (batsman2.getRunsScored() == 0) ? batsman1.getPlayer() : batsman2.getPlayer();
		Player player2 = (batsman1.getPlayer().getID() != player1.getID()) ? batsman1.getPlayer() : batsman2.getPlayer();

		currPartnership = new Partnership(player1, player2, score);
	}

	void updatePartnership(BatsmanStats batsman, int ballsPlayed, int runsScored, boolean isOut, Extra extra) {
		currPartnership.updatePlayerStats(batsman.getPlayer(), ballsPlayed, runsScored, isOut, score, totalOversBowled, extra);
		if(isOut) {
			partnershipData.add(currPartnership);
			currPartnership = null;
		} else if(inningsComplete) {
			currPartnership.setUnBeaten();
			partnershipData.add(currPartnership);
			currPartnership = null;
		}
	}

	void addNewOver() {
		completeOver();
		int overNumber = (overInfoData != null && overInfoData.size() > 0) ? overInfoData.size() + 1 : 1;
		currOver = new OverInfo(overNumber);

		currBallNum = 0;
	}

	public void completeOver() {
		if(currOver != null) {
			currOver.setMaiden();
			if(currOver.getOverNumber() > overInfoData.size()) {
				overInfoData.add(currOver);
			}
		}
	}

	void addNewBall(int runsScored, BowlerStats bowler, Extra extra, WicketData wicketData, BatsmanStats batsman) {
		currBallNum = incrementNextBallNumber ? currBallNum + 1 : currBallNum;
		incrementNextBallNumber = (extra == null || (extra.getType() != ExtraType.NO_BALL && extra.getType() != ExtraType.WIDE));

		BallInfo ballInfo = new BallInfo(currBallNum, runsScored, extra, wicketData, bowler.getPlayer(), batsman.getPlayer());
		currOver.newBallBowled(ballInfo);
	}
}