package com.thenewcone.myscorecard.player;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Player implements Serializable {
	private String name;
    private int age;
    private BattingType battingStyle;
    private BowlingType bowlingStyle;

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public BattingType getBattingStyle() {
		return battingStyle;
	}

	public BowlingType getBowlingStyle() {
		return bowlingStyle;
	}


	public Player (@NonNull String name, int age, @NonNull BattingType battingStyle, @NonNull BowlingType bowlingStyle) {
		this.name = name;
		this.age = age;
		this.battingStyle = battingStyle;
		this.bowlingStyle = bowlingStyle;
	}

	public enum BattingType {
		LHB, RHB
	}

	public enum BowlingType {
		RF, RFM, RMF, RM, LF, LFM, LMF, LM, OB, LB, SLA, SLC, NONE
	}
}
