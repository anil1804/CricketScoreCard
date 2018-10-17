package com.thenewcone.myscorecard.player;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Player implements Serializable {
	String name;
	int age;
	BattingType battingType;

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public BattingType getBattingType() {
		return battingType;
	}

	public BowlingType getBowlingType() {
		return bowlingType;
	}

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
