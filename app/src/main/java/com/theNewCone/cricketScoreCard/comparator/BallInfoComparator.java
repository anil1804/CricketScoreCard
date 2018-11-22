package com.theNewCone.cricketScoreCard.comparator;

import com.theNewCone.cricketScoreCard.match.BallInfo;

import java.util.Comparator;

public class BallInfoComparator implements Comparator<BallInfo> {

	@Override
	public int compare(BallInfo ballInfo1, BallInfo ballInfo2) {
		return (ballInfo1.getBallNumber() - (ballInfo2.getBallNumber()));
	}
}
