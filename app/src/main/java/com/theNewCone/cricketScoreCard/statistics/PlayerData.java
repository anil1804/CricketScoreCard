package com.theNewCone.cricketScoreCard.statistics;

import com.theNewCone.cricketScoreCard.player.Player;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PlayerData {
	private int catches = 0, stumps = 0, runOuts = 0, totalInnings;

	private Player player;
	private List<PlayerMatchData> playerMatchDataList;

	public PlayerData(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public int getCatches() {
		return catches;
	}

	public int getStumps() {
		return stumps;
	}

	public int getRunOuts() {
		return runOuts;
	}

	public int getTotalInnings() {
		return totalInnings;
	}

	public void setPlayerMatchDataList(List<PlayerMatchData> playerData) {
		this.playerMatchDataList = playerData;
		updatePlayerFieldingData();
	}

	private void updatePlayerFieldingData() {
		for (PlayerMatchData playerMatchData : playerMatchDataList) {
			catches += playerMatchData.getCatches();
			stumps += playerMatchData.getStumps();
			runOuts += playerMatchData.getRunOuts();
			totalInnings++;
		}
	}

	public enum Sort implements Comparator<PlayerData> {
		ByCatches() {
			@Override
			public int compare(PlayerData lhs, PlayerData rhs) {
				int diff = lhs.getCatches() - rhs.getCatches();
				if (diff == 0) {
					diff = lhs.getRunOuts() - rhs.getRunOuts();
				}

				return diff;
			}
		},

		ByStumping() {
			@Override
			public int compare(PlayerData lhs, PlayerData rhs) {
				int diff = lhs.getStumps() - rhs.getStumps();
				if (diff == 0) {
					diff = lhs.getRunOuts() - rhs.getRunOuts();
				}
				return diff;
			}
		},

		ByRunOuts() {
			@Override
			public int compare(PlayerData lhs, PlayerData rhs) {
				return lhs.getRunOuts() - rhs.getRunOuts();
			}
		};

		public Comparator<PlayerData> descending() {
			return Collections.reverseOrder(this);
		}
	}
}
