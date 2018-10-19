package com.thenewcone.myscorecard.match;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.thenewcone.myscorecard.player.BatsmanStats;
import com.thenewcone.myscorecard.player.BowlerStats;
import com.thenewcone.myscorecard.player.Player;
import com.thenewcone.myscorecard.scorecard.Extra;
import com.thenewcone.myscorecard.scorecard.WicketData;
import com.thenewcone.myscorecard.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class CricketCardUtils {

	private int numConsecutiveDots = 0;
	private boolean newOver = false;

	private CricketCard card;
	private BowlerStats bowler, nextBowler, prevBowler;
	private BatsmanStats currentFacing, otherBatsman;

	private List<Player> battingTeam, bowlingTeam;

	public BowlerStats getBowler() {
		return bowler;
	}

	public BowlerStats getPrevBowler() {
	    return prevBowler;
    }

	public BatsmanStats getCurrentFacing() {
		return currentFacing;
	}

    public BatsmanStats getOtherBatsman() {
        return otherBatsman;
    }

	public CricketCard getCard() {
		return card;
	}

	public List<Player> getBattingTeam() {
		return battingTeam;
	}

	public void addToBattingTeam(Player player) {
	    if(battingTeam == null) {
            battingTeam = new ArrayList<>();
        }

		battingTeam.add(player);
	}

	public List<Player> getBowlingTeam() {
		return bowlingTeam;
	}

	public void addToBowlingTeam(Player player) {
        if(bowlingTeam == null)
            bowlingTeam = new ArrayList<>();

		bowlingTeam.add(player);
	}

	public CricketCardUtils(CricketCard card) {
		this.card = card;
	}

	public void updateFacingBatsman(BatsmanStats batsman) {
	    if(currentFacing != null)
	        otherBatsman = currentFacing;

	    currentFacing = batsman;
    }

	public void newBatsman(@NonNull BatsmanStats batsman) {
	    if(currentFacing == null)
	        currentFacing = batsman;
	    else
	        otherBatsman = batsman;

		card.appendToBatsmen(batsman);
	}

	private void setNextBowler() {
        prevBowler = nextBowler;

        nextBowler = (Double.parseDouble(bowler.getOversBowled()) == (double) card.getMaxPerBowler())
                ? null
                : bowler;

        if(prevBowler != null) {
            setBowler(prevBowler);
        }
    }


	public void setBowler(BowlerStats bowler) {
		this.bowler = bowler;
		card.updateBowlerInBowlerMap(bowler);
	}

	private void updateBowlerFigures(double ballsBowled, int runsGiven, WicketData wicketData) {
		BowlerStats bowlerDetails = card.getBowlerMap().get(bowler.getBowlerName());
		if(bowlerDetails != null) {
			if (runsGiven > 0) {
				bowlerDetails.incRunsGiven(runsGiven);
			} else {
				numConsecutiveDots++;
			}
			if (ballsBowled > 0) {
				String oversBowled =  card.incrementOvers(bowlerDetails.getOversBowled());
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

			card.updateBowlerInBowlerMap(bowler);
		}
	}

	private void updateBatsmanScore(int runs, int balls, @Nullable WicketData wicketData) {
		BatsmanStats batsman = currentFacing;
		if(balls > 0) {
			batsman.incBallsPlayed(balls);
		}

		if(balls > 0) {
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
		}

		if(wicketData != null) {
            if(currentFacing.getPosition() == wicketData.getBatsman().getPosition()) {
                currentFacing.setNotOut(false);
                currentFacing.setWicketEffectedBy(wicketData.getEffectedBy());
                currentFacing.setWicketTakenBy(wicketData.getBowler());
                currentFacing.evaluateStrikeRate();
                card.updateBatsmenData(currentFacing);
                currentFacing = null;
            } else {
                otherBatsman.setNotOut(false);
                otherBatsman.setWicketEffectedBy(wicketData.getEffectedBy());
                otherBatsman.setWicketTakenBy(wicketData.getBowler());
                otherBatsman.evaluateStrikeRate();
                card.updateBatsmenData(otherBatsman);
                otherBatsman = null;
            }
		} else {
            batsman.evaluateStrikeRate();
        }

		checkNextBatsmanFacingBall(runs);
	}

	private void checkNextBatsmanFacingBall(int runs) {
        if((runs % 2 == 1 && !newOver)  || (runs % 2 == 0 && newOver)) {
            BatsmanStats tempBatsman = otherBatsman;
            otherBatsman = currentFacing;
            currentFacing = tempBatsman;
        }
	}

	public void processBallActivity(@Nullable Extra extra, int runs, @Nullable WicketData wicketData, boolean bowlerChanged) {
		int batsmanRuns = runs, batsmanBalls = 1;
		int bowlerRuns = runs, bowlerBalls = 1;
		if(extra != null) {
			switch (extra.getType()) {
				case BYE:
					batsmanRuns = 0;
					bowlerRuns = 0;
					card.addByes(extra.getRuns());
					break;

				case LEG_BYE:
					batsmanRuns = 0;
					bowlerRuns = 0;
					card.addLegByes(extra.getRuns());
					break;

				case WIDE:
					batsmanRuns = 0;
					batsmanBalls = 0;
					bowlerRuns = 1 + extra.getRuns();
					bowlerBalls = 0;
					card.addWides(extra.getRuns() + 1);
					break;

				case NO_BALL:
					bowlerBalls = 0;
					switch (extra.getSubType()) {
						case NONE:
							bowlerRuns = runs + 1;
							break;
						case BYE:
							bowlerRuns = 1;
							batsmanRuns = 0;
							card.addByes(runs);
							break;
						case LEG_BYE:
							bowlerRuns = 1;
							batsmanRuns = 0;
							card.addLegByes(runs);
							break;
					}
					card.incNoBalls();
					break;

				case PENALTY:
					batsmanBalls = 0;
					batsmanRuns = 0;
					bowlerBalls = 0;
					bowlerRuns = 0;
					break;
			}
		}

		if(bowlerBalls > 0) {
			card.updateTotalOversBowled();
			newOver = card.getTotalOversBowled().split("\\.")[1].equals("0");
		} else {
			newOver = false;
		}

		if(newOver)
		    setNextBowler();

		if(bowlerChanged)
			numConsecutiveDots = 0;

		if(wicketData != null) {
			card.incWicketsFallen();
			card.inningsCheck();
		}

		updateBatsmanScore(batsmanRuns, batsmanBalls, wicketData);
		updateBowlerFigures((double) bowlerBalls, bowlerRuns, wicketData);
		card.updateScore(runs, extra);
		card.updateRunRate();
	}


	public void processExtra(Extra.ExtraType extraType, int numExtraRuns, String penaltyFavouringTeam, Extra.ExtraType extraSubType) {
		Extra extra;
		switch (extraType) {
			case PENALTY:
				if(numExtraRuns > 0) {
					extra = new Extra(Extra.ExtraType.PENALTY, numExtraRuns);
					addPenalty(extra, penaltyFavouringTeam);
				}
				break;

			case LEG_BYE:
				if(numExtraRuns > 0) {
					extra = new Extra(Extra.ExtraType.LEG_BYE, numExtraRuns);
					processBallActivity(extra, 0, null, false);
					checkNextBatsmanFacingBall(extra.getRuns());
				}
				break;

			case BYE:
				if(numExtraRuns > 0) {
					extra = new Extra(Extra.ExtraType.BYE, numExtraRuns);
					processBallActivity(extra, 0, null, false);
					checkNextBatsmanFacingBall(extra.getRuns());
				}
				break;

			case WIDE:
				if(numExtraRuns >= 0) {
					extra = new Extra(Extra.ExtraType.WIDE, numExtraRuns);
					processBallActivity(extra, 0, null, false);
				}
				break;

			case NO_BALL:
				if(numExtraRuns >= 0) {
					extra = new Extra(Extra.ExtraType.NO_BALL, 0, extraSubType);
					processBallActivity(extra, numExtraRuns, null, false);
				}
				break;
		}
	}

	private void addPenalty(Extra extra, @NonNull String favouringTeam) {
		if(extra.getRuns() > 0) {
			int penaltyRuns = extra.getRuns();
			if(favouringTeam.equals(CommonUtils.BATTING_TEAM)) {
				card.addPenalty(penaltyRuns);
				card.updateScore(0, extra);
			} else if(favouringTeam.equals(CommonUtils.BOWLING_TEAM)) {
				if(card.getInnings() == 1) {
					card.addFuturePenalty(penaltyRuns);
				} else if(card.getInnings() == 2) {
					card.setTarget(card.getTarget() + penaltyRuns);
					if(card.getPrevInningsCard() != null) {
						CricketCardUtils ccUtils = new CricketCardUtils(card.getPrevInningsCard());
						ccUtils.addPenalty(extra, CommonUtils.BATTING_TEAM);
					}
				}
			}
		}
	}
}
