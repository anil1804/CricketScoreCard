package com.theNewCone.cricketScoreCard.player;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.theNewCone.cricketScoreCard.enumeration.BattingType;
import com.theNewCone.cricketScoreCard.enumeration.BowlingType;

import java.io.Serializable;
import java.util.List;

public class Player implements Serializable {
	private String name;
    private int age, id;
    private BattingType battingStyle;
    private BowlingType bowlingStyle;
    private boolean isWicketKeeper;
	private List<Integer> teamsAssociatedTo;

	public String getName() {
		return name;
	}

	public int getID() {
		return id;
	}

	public int getAge() {
		return age;
	}

	public BattingType getBattingStyle() {
		return battingStyle;
	}

    public boolean isWicketKeeper() {
        return isWicketKeeper;
    }

    public BowlingType getBowlingStyle() {
		return bowlingStyle;
	}

	public void setPlayerID(int id) {
	    this.id = id;
    }

	public List<Integer> getTeamsAssociatedTo() {
		return teamsAssociatedTo;
	}

	public void setTeamsAssociatedTo(List<Integer> teamsAssociatedTo) {
		this.teamsAssociatedTo = teamsAssociatedTo;
	}

	public Player (@NonNull String name, int age, @NonNull BattingType battingStyle, @NonNull BowlingType bowlingStyle, boolean isWicketKeeper) {
	    this.id = -1;
		this.name = name;
		this.age = age;
		this.battingStyle = battingStyle;
		this.bowlingStyle = bowlingStyle;
		this.isWicketKeeper = isWicketKeeper;
	}

	public Player (int id, @NonNull String name, int age, @NonNull BattingType battingStyle, @NonNull BowlingType bowlingStyle, boolean isWicketKeeper) {
	    this.id = id;
		this.name = name;
		this.age = age;
		this.battingStyle = battingStyle;
		this.bowlingStyle = bowlingStyle;
		this.isWicketKeeper = isWicketKeeper;
	}

	public Player(@NonNull String name, @NonNull BattingType battingStyle, @NonNull BowlingType bowlingStyle, boolean isWicketKeeper) {
		this.id = -1;
		this.name = name;
		this.age = 0;
		this.battingStyle = battingStyle;
		this.bowlingStyle = bowlingStyle;
		this.isWicketKeeper = isWicketKeeper;
	}

	public Player(int id, @NonNull String name, @NonNull BattingType battingStyle, @NonNull BowlingType bowlingStyle, boolean isWicketKeeper) {
		this.id = id;
		this.name = name;
		this.age = 0;
		this.battingStyle = battingStyle;
		this.bowlingStyle = bowlingStyle;
		this.isWicketKeeper = isWicketKeeper;
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		if (obj instanceof Player)
			return this.id == ((Player) obj).id;
		else
			return super.equals(obj);
	}
}
