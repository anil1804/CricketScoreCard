package com.thenewcone.myscorecard.match;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.thenewcone.myscorecard.player.BatsmanStats;
import com.thenewcone.myscorecard.player.BowlerStats;
import com.thenewcone.myscorecard.scorecard.Extra;
import com.thenewcone.myscorecard.scorecard.WicketData;
import com.thenewcone.myscorecard.utils.CommonUtils;

import java.util.HashMap;

public class CricketCard {
	private int maxWickets, numConsecutiveDots;
	private boolean newOver = true;
	private HashMap<String, BowlerStats> bowlerMap = new HashMap<>();
	private SparseArray<BatsmanStats> batsmen = new SparseArray<>();
	private int maxBowlers, maxPerBowler;
	private boolean inningsComplete;

	public BatsmanStats batsman1, batsman2;
	public BatsmanStats currentFacing;
	public BowlerStats bowler;
	public int score, target = -1, wicketsFallen, innings;
	public int byes, legByes, wides, noBalls;
	public double runRate, reqRate = -1.00;
	public String battingTeam, maxOvers, totalOversBowled;

	public CricketCard(String battingTeam, String maxOvers, int maxBowlers, int maxPerBowler, int maxWickets) {
		this.battingTeam = battingTeam;
		this.maxOvers = maxOvers.indexOf(",") >  0 ? maxOvers : maxOvers + ".0";
		this.maxBowlers = maxBowlers;
		this.maxPerBowler = maxPerBowler;
		this.maxWickets = maxWickets;
		this.totalOversBowled = "0.0";
	}

	public void newBatsman(@NonNull BatsmanStats batsman, @Nullable WicketData wicketData) {
		if(batsman1 == null) {
			batsman1 = batsman;
		} else {
			batsman2 = batsman;
		}
		if(currentFacing == null) {
			currentFacing = batsman1;
		}

		batsmen.append(batsman.getPosition(), batsman);

		if(wicketData != null && currentFacing != null && currentFacing.equals(wicketData.getBatsman())) {
			currentFacing = batsman;
		}
	}

	public void setBowler(BowlerStats bowler) {
		bowlerMap.remove(bowler.getBowlerName());
		bowlerMap.put(bowler.getBowlerName(), bowler);
		this.bowler = bowler;
	}

	private void addWides(int wides) {
		this.wides += wides;
	}

	private void incNoBalls() {
		this.noBalls++;
	}

	private void addByes(int byes) {
		this.byes += byes;
	}

	private void addLegByes(int legByes) {
		this.legByes += legByes;
	}

	public boolean isNewOver() {
		return newOver;
	}

	private void updateBowlerDetails(double ballsBowled, int runsGiven, WicketData wicketData) {

		BowlerStats bowlerDetails = bowlerMap.get(bowler.getBowlerName());
		if(bowlerDetails != null) {
			if (runsGiven > 0) {
				bowlerDetails.incRunsGiven(runsGiven);
			} else {
				numConsecutiveDots++;
			}
			if (ballsBowled > 0) {
				String oversBowled =  incrementOvers(bowlerDetails.getOversBowled());
				if (oversBowled.split("\\.")[1].equals("0") && numConsecutiveDots == 6) {
						bowler.incMaidens();
						numConsecutiveDots = 0;
				}
				bowlerDetails.setOversBowled(oversBowled);
			}

			if (wicketData != null) {
				if (WicketData.isBowlersWicket(wicketData.getDismissalType()))
					bowlerDetails.incWickets();
			}

			bowler.evaluateEconomy();

			bowlerMap.remove(bowler.getBowlerName());
			bowlerMap.put(bowler.getBowlerName(), bowler);
		}
	}

	private void updateBatsmanDetails(int runs, int balls, @Nullable WicketData wicketData) {
		BatsmanStats batsman = currentFacing;
		if(balls > 0) {
			batsman.incBallsPlayed(balls);
		}

		switch (runs) {
			case 0:
				batsman.addDot();
				break;
			case 1:
				batsman.addSingle();
				break;
			case 2:
				batsman.addTwos();
				break;
			case 3:
				batsman.addThrees();
				break;
			case 4:
				batsman.addFours();
				break;
			case 6:
				batsman.addSixes();
				break;
		}

		if(wicketData != null && batsman.equals(wicketData.getBatsman())) {
			batsman.setNotOut(false);
			batsman.setWicketEffectedBy(wicketData.getEffectedBy());
			batsman.setWicketTakenBy(wicketData.getBowler());
		}

		batsman.evaluateStrikeRate();

		if(batsman1.getPosition() == batsman.getPosition()) {
			batsman1 = batsman;
			if((runs % 2 == 1) || (runs % 2 == 0 && newOver)) {
				currentFacing = batsman2;
			} else {
				currentFacing = batsman1;
			}
		}
		else if(batsman2.getPosition() == batsman.getPosition()) {
			batsman2 = batsman;
			if(runs % 2 == 1 || (runs % 2 == 0 && newOver)) {
				currentFacing = batsman1;
			} else {
				currentFacing = batsman2;
			}
		}
	}

	public void newBallBowled(@Nullable Extra extra, int runs, @Nullable WicketData wicketData, boolean bowlerChanged) {
		int batsmanRuns = runs, batsmanBalls = 1;
		int bowlerRuns = runs, bowlerBalls = 1;
		if(extra != null) {
			switch (extra.getType()) {
				case BYE:
					batsmanRuns = 0;
					addByes(extra.getRunsScored());
					break;
				case LEG_BYE:
					batsmanRuns = 0;
					addLegByes(extra.getRunsScored());
					break;
				case WIDE:
					bowlerRuns = 1 + extra.getRunsScored();
					bowlerBalls = 0;
					batsmanBalls = 0;
					batsmanRuns = 0;
					addWides(extra.getRunsScored() + 1);
					break;
				case NO_BALL:
					batsmanRuns = extra.getRunsScored();
					bowlerBalls = 0;
					bowlerRuns = 1 + extra.getRunsScored();
					incNoBalls();
					break;
			}
		}

		if(bowlerBalls > 0) {
			totalOversBowled = incrementOvers(totalOversBowled);
			if (totalOversBowled.split("\\.")[1].equals("0")) {
				newOver = true;
			} else {
				newOver = false;
			}
		}

		if(bowlerChanged)
			numConsecutiveDots = 0;

		if(wicketData != null) {
			wicketsFallen++;
			inningsCheck();
		}

		updateBatsmanDetails(batsmanRuns, batsmanBalls, wicketData);
		updateBowlerDetails((double) bowlerBalls, bowlerRuns, wicketData);
		updateScore(runs, extra);
		updateRunRate();
	}

	private String incrementOvers(String oversBowled) {
		String[] overDetails = oversBowled.split("\\.");
		int overs = Integer.parseInt(overDetails[0]), balls = Integer.parseInt(overDetails[1]);

		balls += 1;
		if(balls == 6) {
			overs++;
			balls = 0;
		}

		return overs + "." + balls;
	}

	private void inningsCheck() {
		if(totalOversBowled.equals(maxOvers) || wicketsFallen == maxWickets) {
			inningsComplete = true;
			if(innings == 1) {
				innings++;
				target = score;
			}
		}
	}

	private void updateScore(int runsScored, @Nullable Extra extra) {
		int runs = runsScored;
		if(extra != null) {
			runs = extra.getRunsScored();
			if(extra.getType() == Extra.ExtraType.WIDE || extra.getType() == Extra.ExtraType.NO_BALL)
				runs++;
		}

		score += runs;
	}

	private void updateRunRate(){
		runRate = CommonUtils.calcRunRate(score, Double.parseDouble(totalOversBowled));
		if(innings == 2) {
			reqRate = CommonUtils.calReqRate(score, Double.parseDouble(totalOversBowled), target, Double.parseDouble(maxOvers));
		}
	}
}