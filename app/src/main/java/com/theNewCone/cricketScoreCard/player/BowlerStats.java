package com.theNewCone.cricketScoreCard.player;

import com.theNewCone.cricketScoreCard.utils.CommonUtils;

import java.io.Serializable;

public class BowlerStats implements Serializable {
	private String oversBowled;
	private double economy;
	private int runsGiven, maidens, wickets;

    private Player player;

	public String getBowlerName() {
		return player.getName();
	}

	public String getOversBowled() {
		return oversBowled;
	}

	public void setOversBowled(String oversBowled) {
		this.oversBowled = oversBowled;
	}

	public double getEconomy() {
		return economy;
	}

	public int getRunsGiven() {
		return runsGiven;
	}

	public void incRunsGiven(int runsGiven) {
		this.runsGiven += runsGiven;
	}

	public int getMaidens() {
		return maidens;
	}

	public void incMaidens() {
		this.maidens++;
	}

	public int getWickets() {
		return wickets;
	}

	public void incWickets() {
		this.wickets++;
	}

    public Player getPlayer() {
        return player;
    }

	public BowlerStats(Player player) {
		this.player = player;
		this.oversBowled = "0.0";
		this.economy = CommonUtils.calcRunRate(runsGiven, Double.parseDouble(oversBowled));
	}

	public void evaluateEconomy() {
		this.economy = CommonUtils.calcRunRate(runsGiven, Double.parseDouble(oversBowled));
	}
}
