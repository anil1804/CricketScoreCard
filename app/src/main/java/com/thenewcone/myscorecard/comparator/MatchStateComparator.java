package com.thenewcone.myscorecard.comparator;

import com.thenewcone.myscorecard.match.MatchState;
import com.thenewcone.myscorecard.player.Player;

import java.util.Comparator;
import java.util.List;

public class MatchStateComparator implements Comparator<MatchState> {

	@Override
	public int compare(MatchState savedMatch1, MatchState savedMatch2) {
		return (savedMatch1.getSavedDate().compareTo(savedMatch2.getSavedDate())) * -1;
	}
}
