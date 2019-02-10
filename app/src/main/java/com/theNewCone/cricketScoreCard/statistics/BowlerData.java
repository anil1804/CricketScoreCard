package com.theNewCone.cricketScoreCard.statistics;

import com.theNewCone.cricketScoreCard.player.Player;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;

import java.util.List;

public class BowlerData {
	private Player player;
	private double oversBowled;
	private int runsGiven, wicketsTaken, maidens;
	private String bestFigures;
	private List<PlayerMatchData> playerMatchDataList;

	public BowlerData(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public double getOversBowled() {
		return oversBowled;
	}

	public int getRunsGiven() {
		return runsGiven;
	}

	public int getWicketsTaken() {
		return wicketsTaken;
	}

	public int getMaidens() {
		return maidens;
	}

	public String getBestFigures() {
		return bestFigures;
	}

	public List<PlayerMatchData> getPlayerMatchDataList() {
		return playerMatchDataList;
	}

	public void setPlayerMatchDataList(List<PlayerMatchData> playerMatchDataList) {
		this.playerMatchDataList = playerMatchDataList;
		updatePlayerStats();
	}


	private void updatePlayerStats() {
		int bfRunsGiven = 0, bfWicketsTaken = 0;

		for(PlayerMatchData matchData : playerMatchDataList) {
			int runsGiven = matchData.getRunsGiven();
			int wicketsTaken = matchData.getWicketsTaken();

			if(wicketsTaken > bfRunsGiven)
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
		}

		this.bestFigures = bfWicketsTaken + "/" + bfRunsGiven;
	}

	public double getStrikeRate() {
		return (double) CommonUtils.oversToBalls(oversBowled)/wicketsTaken;
	}

	public double getAverage() {
		return (double) runsGiven/wicketsTaken;
	}

	public double getEconomy() {
		return (double) runsGiven/oversBowled;
	}
}
