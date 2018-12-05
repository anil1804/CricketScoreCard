package com.theNewCone.cricketScoreCard.comparator;

import com.theNewCone.cricketScoreCard.tournament.Tournament;

import java.util.Comparator;

public class TournamentComparator implements Comparator<Tournament> {

	@Override
	public int compare(Tournament tournament1, Tournament tournament2) {
		return (tournament1.getCreatedDate().compareTo(tournament2.getCreatedDate())) * -1;
	}
}
