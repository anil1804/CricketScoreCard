package com.theNewCone.cricketScoreCard.comparator;

import com.theNewCone.cricketScoreCard.match.MatchState;

import java.util.Comparator;

public class MatchStateComparator implements Comparator<MatchState> {

	@Override
	public int compare(MatchState savedMatch1, MatchState savedMatch2) {
		return (savedMatch1.getSavedDate().compareTo(savedMatch2.getSavedDate())) * -1;
	}
}
