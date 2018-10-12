package com.thenewcone.myscorecard.scorecard;

public class Extra {
	private ExtraType type;
	private int runs;

	public ExtraType getType() {
		return type;
	}

	public int getRuns() {
		return runs;
	}

	public Extra(ExtraType type, int runs) {
		this.type = type;
		this.runs = runs;
	}

	public enum ExtraType {
		WIDE, NO_BALL, LEG_BYE, BYE, PENALTY
	}
}
