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
import java.util.Random;

public class CricketCardUtils {
    private int numConsecutiveDots = 0, tossWonByTeamID, maxWickets;
	private boolean newOver = false;

	private String matchName;

	private CricketCard card, prevInningsCard;

    private BowlerStats bowler, nextBowler, prevBowler;
	private BatsmanStats currentFacing, otherBatsman;

	private Team team1, team2;

	private List<String> last12Balls;

    public boolean isNewOver() {
        return newOver;
    }

    public BowlerStats getBowler() {
		return bowler;
	}

	public BowlerStats getPrevBowler() {
	    return prevBowler;
    }

	public BowlerStats getNextBowler() {
	    return nextBowler;
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

	public CricketCard getPrevInningsCard() {
    	return prevInningsCard;
	}

	public String getMatchName() {
        return matchName;
    }

	public int getTossWonBy() {
		return tossWonByTeamID;
	}

	public Team getTeam1() {
		return team1;
	}

	public Team getTeam2() {
		return team2;
	}

	public void setTossWonBy(int tossWonBy) {
		this.tossWonByTeamID = tossWonBy;
	}

	public int getMaxWickets() {
		return maxWickets;
	}

	public List<String> getLast12Balls() {
		return last12Balls;
	}

	public CricketCardUtils(CricketCard card, String matchName, Team team1, Team team2, int maxWickets) {
		this.card = card;
		this.matchName = matchName;
		this.team1 = team1;
		this.team2 = team2;
		this.maxWickets = maxWickets;
	}

	public void updateFacingBatsman(BatsmanStats batsman) {
	    if(currentFacing != null && currentFacing.getPlayer().getID() != batsman.getPlayer().getID())
	        otherBatsman = currentFacing;

	    currentFacing = batsman;
    }

	public void newBatsman(@NonNull BatsmanStats batsman) {
	    if(currentFacing == null)
	        currentFacing = batsman;
	    else
	        otherBatsman = batsman;

		card.appendToBatsmen(batsman);

		if(currentFacing != null && otherBatsman != null) {
			card.addNewPartnershipRecord(currentFacing, otherBatsman);
		}
	}

	private void setNextBowler() {
        nextBowler = (prevBowler != null && Double.parseDouble(prevBowler.getOversBowled()) == (double) card.getMaxPerBowler())
                ? null
                : prevBowler;

        prevBowler = bowler;

        if(prevBowler != null) {
            setBowler(prevBowler, true);
        }
    }

	public void setBowler(BowlerStats bowler, boolean isAutomatic) {
		this.bowler = bowler;
		if(bowler != null) {
			card.updateBowlerInBowlerMap(bowler);
		}

		newOver = isAutomatic;
	}

	private void updateBowlerFigures(double ballsBowled, int runsGiven, WicketData wicketData, Extra extra) {
		if(bowler != null) {
			if (runsGiven > 0) {
				bowler.incRunsGiven(runsGiven);
				numConsecutiveDots = 0;
			} else {
				numConsecutiveDots++;
			}
			if (ballsBowled > 0) {
				String oversBowled =  card.incrementOvers(bowler.getOversBowled());
				boolean isMaiden = false;
				if (oversBowled.split("\\.")[1].equals("0")) {
					if(numConsecutiveDots == 6){
						bowler.incMaidens();
						isMaiden = true;
					}

					numConsecutiveDots = 0;
				}
				card.addNewOver(isMaiden);
				bowler.setOversBowled(oversBowled);
			}

			if (wicketData != null) {
				if (WicketData.isBowlersWicket(wicketData.getDismissalType()))
					bowler.incWickets();
			}

			bowler.evaluateEconomy();

			card.addNewBall(runsGiven, bowler, extra, wicketData);

			card.updateBowlerInBowlerMap(bowler);
		}
	}

	private void updateBatsmanScore(int runs, int balls, @Nullable WicketData wicketData, Extra extra) {
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

		boolean isOut = false;
		if(wicketData != null) {
			isOut = true;
			Player effectedBy = (wicketData.getDismissalType() == WicketData.DismissalType.STUMPED)
					? card.getBowlingTeam().getWicketKeeper() : wicketData.getEffectedBy();

            if(currentFacing.getPosition() == wicketData.getBatsman().getPosition()) {
                currentFacing.setNotOut(false);
                currentFacing.setWicketEffectedBy(effectedBy);
                currentFacing.setWicketTakenBy(wicketData.getBowler());
                currentFacing.setDismissalType(wicketData.getDismissalType());
                currentFacing.evaluateStrikeRate();
                card.updateBatsmenData(currentFacing);
                currentFacing = null;
            } else {
                otherBatsman.setNotOut(false);
                otherBatsman.setWicketEffectedBy(effectedBy);
                otherBatsman.setWicketTakenBy(wicketData.getBowler());
				otherBatsman.setDismissalType(wicketData.getDismissalType());
                otherBatsman.evaluateStrikeRate();
                card.updateBatsmenData(otherBatsman);
                otherBatsman = null;
            }
		} else {
            batsman.evaluateStrikeRate();
        }

		card.updatePartnership(batsman, balls, runs + (extra != null ? extra.getRuns() : 0), isOut);
		checkNextBatsmanFacingBall(runs, extra);
	}

	private void checkNextBatsmanFacingBall(int runs, Extra extra) {
    	if(extra != null) {
    		if(extra.getType() == Extra.ExtraType.BYE
					|| extra.getType() == Extra.ExtraType.LEG_BYE
					|| extra.getType() == Extra.ExtraType.WIDE
					|| (extra.getType() == Extra.ExtraType.NO_BALL && (
							extra.getSubType() == Extra.ExtraType.BYE
									|| extra.getSubType() == Extra.ExtraType.LEG_BYE
					))) {
    			runs = extra.getRuns();
			}
		}

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
					if(extra.getSubType() != null) {
						switch (extra.getSubType()) {
							case NONE:
								bowlerRuns = runs + 1;
								break;
							case BYE:
								bowlerRuns = 1;
								batsmanRuns = 0;
								card.addByes(extra.getRuns());
								break;
							case LEG_BYE:
								bowlerRuns = 1;
								batsmanRuns = 0;
								card.addLegByes(extra.getRuns());
								break;
						}
					} else {
						bowlerRuns = runs + 1;
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

		if(newOver) {
		    setNextBowler();
		}

		if(bowlerChanged) {
			numConsecutiveDots = 0;
		}

		if(wicketData != null) {
			card.incWicketsFallen();
		}

		updateBatsmanScore(batsmanRuns, batsmanBalls, wicketData, extra);
		updateBowlerFigures((double) bowlerBalls, bowlerRuns, wicketData, extra);
		card.updateScore(runs, extra);
		card.updateRunRate();
		updateLast12Balls(extra, runs, wicketData);

		card.inningsCheck();
	}

	public void addPenalty(@NonNull Extra extra, @NonNull String favouringTeam) {
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
					if(prevInningsCard != null) {
						prevInningsCard.addPenalty(penaltyRuns);
						prevInningsCard.updateScore(0, extra);
					}
				}
			}
		}

		card.inningsCheck();
	}

	public void setFirstInnings(List<Player> batTeam, List<Player> bowlTeam) {

    	if(batTeam == null) {
			batTeam = new ArrayList<>();
			batTeam.add(new Player("Player-11", (new Random().nextInt(20)) + 18, Player.BattingType.RHB, Player.BowlingType.NONE, false));
			batTeam.add(new Player("Player-12", (new Random().nextInt(20)) + 18, Player.BattingType.LHB, Player.BowlingType.NONE, false));
			batTeam.add(new Player("Player-13", (new Random().nextInt(20)) + 18, Player.BattingType.RHB, Player.BowlingType.OB, false));
			batTeam.add(new Player("Player-14", (new Random().nextInt(20)) + 18, Player.BattingType.RHB, Player.BowlingType.NONE, true));
			batTeam.add(new Player("Player-15", (new Random().nextInt(20)) + 18, Player.BattingType.LHB, Player.BowlingType.SLA, false));
			batTeam.add(new Player("Player-16", (new Random().nextInt(20)) + 18, Player.BattingType.RHB, Player.BowlingType.RM, false));
			batTeam.add(new Player("Player-17", (new Random().nextInt(20)) + 18, Player.BattingType.LHB, Player.BowlingType.LF, false));
			batTeam.add(new Player("Player-18", (new Random().nextInt(20)) + 18, Player.BattingType.RHB, Player.BowlingType.RFM, false));
		}

		if(bowlTeam == null) {
			bowlTeam = new ArrayList<>();
			bowlTeam.add(new Player("Player-21", (new Random().nextInt(20)) + 18, Player.BattingType.LHB, Player.BowlingType.NONE, false));
			bowlTeam.add(new Player("Player-22", (new Random().nextInt(20)) + 18, Player.BattingType.RHB, Player.BowlingType.NONE, false));
			bowlTeam.add(new Player("Player-23", (new Random().nextInt(20)) + 18, Player.BattingType.LHB, Player.BowlingType.SLC, false));
			bowlTeam.add(new Player("Player-24", (new Random().nextInt(20)) + 18, Player.BattingType.RHB, Player.BowlingType.LB, false));
			bowlTeam.add(new Player("Player-25", (new Random().nextInt(20)) + 18, Player.BattingType.LHB, Player.BowlingType.NONE, true));
			bowlTeam.add(new Player("Player-26", (new Random().nextInt(20)) + 18, Player.BattingType.RHB, Player.BowlingType.RM, false));
			bowlTeam.add(new Player("Player-27", (new Random().nextInt(20)) + 18, Player.BattingType.RHB, Player.BowlingType.RM, false));
			bowlTeam.add(new Player("Player-28", (new Random().nextInt(20)) + 18, Player.BattingType.LHB, Player.BowlingType.LFM, false));
		}

    }

    public void setNewInnings() {
    	card.updateBatsmenData(currentFacing);
    	card.updateBatsmenData(otherBatsman);
    	card.updateBowlerInBowlerMap(bowler);

        prevInningsCard = card;
        card = new CricketCard(team2, team1, prevInningsCard.getMaxOvers(),
                prevInningsCard.getMaxPerBowler(), prevInningsCard.getMaxWickets(), 2);

        card.setTarget(prevInningsCard.getScore() + 1);
       	card.addPenalty(prevInningsCard.getFuturePenalty());
       	card.updateScore(prevInningsCard.getFuturePenalty(), null);

       	card.updateRunRate();
		last12Balls.clear();

       	currentFacing = null;
       	otherBatsman = null;
       	bowler = null;
       	newOver = true;
    }

	private void updateLast12Balls(Extra extra, int runs, @Nullable WicketData wicketData) {
		if(last12Balls == null) {
			last12Balls = new ArrayList<>();
		}

		StringBuilder currentBallSB = new StringBuilder();
		if(wicketData != null) {
			currentBallSB.append("W");
		}
		if(extra != null) {
			if(currentBallSB.length() > 0)
				currentBallSB.append("+");
			if(extra.getRuns() > 0)
				currentBallSB.append(extra.getRuns());
			switch (extra.getType()) {
				case BYE:
					currentBallSB.append("B");
					break;

				case LEG_BYE:
					currentBallSB.append("L");
					break;

				case PENALTY:
					currentBallSB.append("P");
					break;

				case WIDE:
					if(extra.getRuns() > 0) {
						currentBallSB.append("+");
					}
					currentBallSB.append("Wd");
					break;

				case NO_BALL:
					if(extra.getSubType() != null)
					{
						switch (extra.getSubType()) {
							case NONE:
								currentBallSB.append("N");
								break;
							case BYE:
								currentBallSB.append("B+N");
								break;
							case LEG_BYE:
								currentBallSB.append("L+N");
								break;
						}
					}
					else
						currentBallSB.append("N");
					break;

			}
		}
		if(currentBallSB.length() > 0) {
			if (runs > 0) {
				currentBallSB.append("+");
				currentBallSB.append(runs);
			}
		}
		else {
			currentBallSB.append(runs);
		}
		if(newOver)
			currentBallSB.append(" | ");

		last12Balls.add(currentBallSB.toString());
		if(last12Balls.size() > 12)
			last12Balls.remove(0);
	}
}
