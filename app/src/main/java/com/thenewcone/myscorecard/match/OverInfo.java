package com.thenewcone.myscorecard.match;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OverInfo implements Serializable {
	private int overNumber;
	private List<BallInfo> ballInfoList;
	private boolean isMaiden;

	public int getOverNumber() {
		return overNumber;
	}

	public boolean isMaiden() {
		return isMaiden;
	}

	void setMaiden(boolean maiden) {
		isMaiden = maiden;
	}

	void newBallBowled(BallInfo ballInfo) {
		if(ballInfoList == null)
			ballInfoList = new ArrayList<>();

		ballInfoList.add(ballInfo);
	}

	public int getRuns() {
		int runsScored = 0;
		if(ballInfoList != null) {
			for(BallInfo ballInfo : ballInfoList) {
				runsScored += ballInfo.getRunsScored();
			}
		}

		return runsScored;
	}

	public int getWickets() {
		int wicketsCount = 0;
		if(ballInfoList != null) {
			for(BallInfo ballInfo : ballInfoList) {
				wicketsCount += (ballInfo.getWicketData() != null) ? 1 : 0;
			}
		}

		return wicketsCount;
	}
}
