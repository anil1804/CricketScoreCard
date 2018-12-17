package com.theNewCone.cricketScoreCard.comparator;

import com.theNewCone.cricketScoreCard.tournament.MatchInfo;

import java.util.Comparator;

public class MatchInfoComparator implements Comparator<MatchInfo> {
	@Override
	public int compare(MatchInfo matchInfo1, MatchInfo matchInfo2) {
		int comparedValue = matchInfo1.getMatchNumber() - matchInfo2.getMatchNumber();
		if (comparedValue == 0) {
			comparedValue = matchInfo1.getGroupNumber() - matchInfo2.getGroupNumber();
		}

		return comparedValue;
	}
}
