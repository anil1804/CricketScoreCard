package com.theNewCone.cricketScoreCard.statistics;

import com.theNewCone.cricketScoreCard.player.Player;

import java.util.List;

public class BatsmanData {
	private Player player;
	private int highestScore, lowestScore, fours, sixers, fifties, hundreds;
	private int runsScored, ballsPlayed, totalInnings, notOuts;
	private List<PlayerMatchData> playerMatchDataList;

	public BatsmanData(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public int getHighestScore() {
		return highestScore;
	}

	public int getLowestScore() {
		return lowestScore;
	}

	public int getFours() {
		return fours;
	}

	public int getSixers() {
		return sixers;
	}

	public int getFifties() {
		return fifties;
	}

	public int getHundreds() {
		return hundreds;
	}

	public int getNotOuts() {
		return notOuts;
	}

	public int getRunsScored() {
		return runsScored;
	}

	public int getTotalInnings() {
		return totalInnings;
	}

	public List<PlayerMatchData> getPlayerMatchDataList() {
		return playerMatchDataList;
	}

	public void setPlayerMatchDataList(List<PlayerMatchData> playerMatchDataList) {
		this.playerMatchDataList = playerMatchDataList;
		updatePlayerStats();
	}

	private void updatePlayerStats() {
		for(PlayerMatchData matchData : playerMatchDataList) {
			int runsScored = matchData.getRunsScored();

			if(runsScored > highestScore) {
				highestScore = runsScored;
			} else if(runsScored < lowestScore) {
				lowestScore = runsScored;
			}

			if(runsScored > 100) {
				this.hundreds++;
			} else if(runsScored > 50) {
				this.fifties++;
			}

			this.runsScored += runsScored;
			this.ballsPlayed += matchData.getBallsPlayed();
			this.sixers += matchData.getSixesHit();
			this.fours += matchData.getFoursHit();

			totalInnings++;
			this.notOuts += matchData.isOut() ? 0 : 1;
		}
	}

	private double getStrikeRate() {
		return (double) runsScored/ballsPlayed;
	}

	private double getAverage() {
		return (double) runsScored/(totalInnings - notOuts);
	}
}
