package com.thenewcone.myscorecard.comparator;

import com.thenewcone.myscorecard.match.BallInfo;
import com.thenewcone.myscorecard.match.OverInfo;

import java.util.Comparator;

public class BallInfoComparator implements Comparator<BallInfo> {

	@Override
	public int compare(BallInfo ballInfo1, BallInfo ballInfo2) {
		return (ballInfo1.getBallNumber() - (ballInfo2.getBallNumber()));
	}
}
