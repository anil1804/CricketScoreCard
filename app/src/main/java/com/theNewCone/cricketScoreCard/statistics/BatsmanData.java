package com.theNewCone.cricketScoreCard.statistics;

import com.theNewCone.cricketScoreCard.player.Player;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BatsmanData {
	private Player player;
	private int highestScore, lowestScore, fifties, hundreds;
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

	public int getFifties() {
		return fifties;
	}

	public int getHundreds() {
		return hundreds;
	}

	public int getRunsScored() {
		return runsScored;
	}

	public int getBallsPlayed() {
		return ballsPlayed;
	}

	public int getTotalInnings() {
		return totalInnings;
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

			totalInnings++;
			this.notOuts += matchData.isOut() ? 0 : 1;
		}
	}

	public double getStrikeRate() {
		return (runsScored > 0) ? (double) (runsScored * 100) / ballsPlayed : 0;
	}

	public double getAverage() {
		return (totalInnings - notOuts) > 0 ? (double) runsScored / (totalInnings - notOuts) : -1;
	}

	public enum Sort implements Comparator<BatsmanData> {
		ByHighestScore() {
			@Override
			public int compare(BatsmanData lhs, BatsmanData rhs) {
				return lhs.getHighestScore() - rhs.getHighestScore();
			}
		},

		ByTotalRuns() {
			@Override
			public int compare(BatsmanData lhs, BatsmanData rhs) {
				return lhs.getRunsScored() - rhs.getRunsScored();
			}
		},

		ByHundreds() {
			@Override
			public int compare(BatsmanData lhs, BatsmanData rhs) {
				int diff = lhs.getHundreds() - rhs.getHundreds();
				if (diff == 0) {
					diff = lhs.getFifties() - rhs.getFifties();
				}
				return diff;
			}
		},

		ByFifties() {
			@Override
			public int compare(BatsmanData lhs, BatsmanData rhs) {
				int diff = lhs.getFifties() - rhs.getFifties();
				if (diff == 0) {
					diff = lhs.getHundreds() - rhs.getFifties();
				}

				return diff;
			}
		};

		public Comparator<BatsmanData> descending() {
			return Collections.reverseOrder(this);
		}
	}
}
