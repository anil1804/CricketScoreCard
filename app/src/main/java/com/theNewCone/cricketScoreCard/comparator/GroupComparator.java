package com.theNewCone.cricketScoreCard.comparator;

import com.theNewCone.cricketScoreCard.tournament.Group;

import java.util.Comparator;

public class GroupComparator implements Comparator<Group> {

	@Override
	public int compare(Group group1, Group group2) {
		return (group1.getGroupNumber() - (group2.getGroupNumber()));
	}
}
