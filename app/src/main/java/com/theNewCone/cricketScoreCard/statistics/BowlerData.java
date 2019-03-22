package com.theNewCone.cricketScoreCard.statistics;

import com.theNewCone.cricketScoreCard.player.Player;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BowlerData {
	private Player player;
	private double oversBowled;
	private int runsGiven, wicketsTaken, maidens, totalInnings;
	private String bestFigures;
	private List<PlayerMatchData> playerMatchDataList;

	public BowlerData(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public int getWicketsTaken() {
		return wicketsTaken;
	}

	public String getBestFigures() {
		return bestFigures;
	}

	public int getRunsGiven() {
		return runsGiven;
	}

	public void setPlayerMatchDataList(List<PlayerMatchData> playerMatchDataList) {
		this.playerMatchDataList = playerMatchDataList;
		updatePlayerStats();
	}

	private void updatePlayerStats() {
		int bfRunsGiven = Integer.MAX_VALUE, bfWicketsTaken = 0;

		for(PlayerMatchData matchData : playerMatchDataList) {
			int runsGiven = matchData.getRunsGiven();
			int wicketsTaken = matchData.getWicketsTaken();

			if (wicketsTaken > bfWicketsTaken)
			{
				bfWicketsTaken = wicketsTaken;
				bfRunsGiven = runsGiven;
			} else if(wicketsTaken == bfWicketsTaken && runsGiven < bfRunsGiven) {
				bfRunsGiven = runsGiven;
			}

			this.oversBowled = CommonUtils.ballsToOvers(
					CommonUtils.oversToBalls(this.oversBowled) +
							CommonUtils.oversToBalls(matchData.getOversBowled()));
			this.runsGiven += matchData.getRunsGiven();
			this.maidens += matchData.getMaidens();
			this.wicketsTaken += matchData.getWicketsTaken();

			this.totalInnings++;
		}

		this.bestFigures = bfWicketsTaken + "/" + bfRunsGiven;
	}

	public double getStrikeRate() {
		double strikeRate = -1;
		if (wicketsTaken > 0)
			strikeRate = (double) CommonUtils.oversToBalls(oversBowled) / wicketsTaken;

		return strikeRate;
	}

	public double getAverage() {
		double average = -1;
		if (runsGiven == 0)
			average = 0;
		else if (wicketsTaken > 0)
			average = (double) runsGiven / wicketsTaken;

		return average;
	}

	public double getEconomy() {
		double economy = -1;
		if (runsGiven == 0)
			economy = 0;
		else if (oversBowled > 0)
			economy = (double) runsGiven / oversBowled;

		return economy;
	}

	public int getTotalInnings() {
		return totalInnings;
	}

	public double getOversBowled() {
		return oversBowled;
	}

	public enum Sort implements Comparator<BowlerData> {
		ByBestFigures() {
			@Override
			public int compare(BowlerData lhs, BowlerData rhs) {
				return lhs.getBestFigures().compareTo(rhs.getBestFigures());
			}
		},

		ByTotalWickets() {
			@Override
			public int compare(BowlerData lhs, BowlerData rhs) {
				return lhs.getWicketsTaken() - rhs.getWicketsTaken();
			}
		},

		ByEconomy() {
			@Override
			public int compare(BowlerData lhs, BowlerData rhs) {
				double diff = lhs.getEconomy() - rhs.getEconomy();
				if (diff == 0) {
					diff = (lhs.getOversBowled() - rhs.getOversBowled()) * -1;
				}
				return (diff > 0) ? 1 : (diff < 0) ? -1 : 0;
			}
		};

		public Comparator<BowlerData> descending() {
			return Collections.reverseOrder(this);
		}
	}
}
