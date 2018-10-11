package com.thenewcone.myscorecard.player;

import com.thenewcone.myscorecard.utils.CommonUtils;

public class BowlerStats {
	private String bowlerName, oversBowled;
	private double economy;
	private int runsGiven, maidens, wickets;

	public String getBowlerName() {
		return bowlerName;
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

	public void setEconomy(double economy) {
		this.economy = economy;
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

	public BowlerStats(String bowlerName) {
		this.bowlerName = bowlerName;
		this.oversBowled = "0.0";
		this.economy = CommonUtils.calcRunRate(runsGiven, Double.parseDouble(oversBowled));
	}

	public void evaluateEconomy() {
		this.economy = CommonUtils.calcRunRate(runsGiven, Double.parseDouble(oversBowled));
	}
}
