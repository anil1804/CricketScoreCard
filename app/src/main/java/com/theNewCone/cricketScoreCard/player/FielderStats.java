package com.theNewCone.cricketScoreCard.player;

public class FielderStats {
	private Player player;
	private int catches, runOuts, stumpOuts;

	public FielderStats(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public int getCatches() {
		return catches;
	}

	public int getRunOuts() {
		return runOuts;
	}

	public int getStumpOuts() {
		return stumpOuts;
	}

	public void incrementCatches() {
		catches++;
	}

	public void incrementRunOuts() {
		runOuts++;
	}

	public void incrementStumpOuts() {
		stumpOuts++;
	}
}
