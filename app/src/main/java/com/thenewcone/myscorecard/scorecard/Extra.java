package com.thenewcone.myscorecard.scorecard;

public class Extra {
	private ExtraType type;
	private int runsScored;

	public ExtraType getType() {
		return type;
	}

	public int getRunsScored() {
		return runsScored;
	}

	public Extra(ExtraType type, int runs) {
		this.type = type;
		this.runsScored = runs;
	}

	public enum ExtraType {
		WIDE, NO_BALL, LEG_BYE, BYE;
	}
}
