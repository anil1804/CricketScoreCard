package com.thenewcone.myscorecard.player;

import android.support.annotation.NonNull;

public class Player {
	String name;
	int age;
	BattingType battingType;
	BowlingType bowlingType;


	public Player (@NonNull String name, int age, @NonNull BattingType battingType, @NonNull BowlingType bowlingType) {
		this.name = name;
		this.age = age;
		this.battingType = battingType;
		this.bowlingType = bowlingType;
	}

	public enum BattingType {
		LHB, RHB
	}

	public enum BowlingType {
		RF, RFM, RMF, RM, LF, LFM, LMF, LM, OB, LB, SLA, SLC, NONE
	}
}
