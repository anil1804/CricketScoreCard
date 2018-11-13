package com.thenewcone.myscorecard.comparator;

import com.thenewcone.myscorecard.match.Match;

import java.util.Comparator;

public class MatchComparator implements Comparator<Match> {

	@Override
	public int compare(Match match1, Match match2) {
		return (match1.getId() - match2.getId());
	}
}
