package com.theNewCone.cricketScoreCard.match;

import android.util.SparseIntArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OverInfo implements Serializable {
	private int overNumber;
	private List<BallInfo> ballInfoList;
	private boolean isMaiden;

	OverInfo(int overNumber) {
		this.overNumber = overNumber;
	}

	public int getOverNumber() {
		return overNumber;
	}

	public boolean isMaiden() {
		return isMaiden;
	}

	void setMaiden() {
		isMaiden = (ballInfoList != null && ballInfoList.size() == 6 && getRunsGivenByBowler() == 0);
	}

	void newBallBowled(BallInfo ballInfo) {
		if(ballInfoList == null)
			ballInfoList = new ArrayList<>();

		ballInfoList.add(ballInfo);
	}

	private int getRunsGivenByBowler() {
		int runsScored = 0;
		if(ballInfoList != null) {
			for(BallInfo ballInfo : ballInfoList) {
				runsScored += ballInfo.getRunsScored();
			}
		}

		return runsScored;
	}

	public int getRunsScored() {
		int runsScored = 0;
		if(ballInfoList != null) {
			for(BallInfo ballInfo : ballInfoList) {
				runsScored += ballInfo.getAllRunsScored();
			}
		}

		return runsScored;
	}

	public SparseIntArray getRunsPerBall() {
		SparseIntArray runsPerBallArray = new SparseIntArray();
		if(ballInfoList != null) {
			int runsInPrevBall = 0, prevBallNumber = 0;
			for(BallInfo ballInfo : ballInfoList) {
				if(ballInfo.getBallNumber() == prevBallNumber) {
					runsInPrevBall += ballInfo.getAllRunsScored();
				} else {
					if(prevBallNumber > 0) {
						runsPerBallArray.put(prevBallNumber, runsInPrevBall);
					}
					prevBallNumber = ballInfo.getBallNumber();
					runsInPrevBall = ballInfo.getAllRunsScored();
				}
			}
			runsPerBallArray.put(prevBallNumber, runsInPrevBall);
		}

		return runsPerBallArray;
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

	public List<BallInfo> getBallInfo() {
		return ballInfoList;
	}
}
