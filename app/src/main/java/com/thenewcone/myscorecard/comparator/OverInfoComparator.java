package com.thenewcone.myscorecard.comparator;

import com.thenewcone.myscorecard.match.MatchState;
import com.thenewcone.myscorecard.match.OverInfo;

import java.util.Comparator;

public class OverInfoComparator implements Comparator<OverInfo> {

	@Override
	public int compare(OverInfo overInfo1, OverInfo overInfo2) {
		return (overInfo1.getOverNumber() - (overInfo2.getOverNumber()));
	}
}
