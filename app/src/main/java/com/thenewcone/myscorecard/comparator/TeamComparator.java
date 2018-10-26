package com.thenewcone.myscorecard.comparator;

import com.thenewcone.myscorecard.match.Team;

import java.util.Comparator;
import java.util.List;

public class TeamComparator implements Comparator<Team> {

	private final List<Integer> priorityTeams;

	public TeamComparator(List<Integer> priorityTeams) {
		this.priorityTeams = priorityTeams;
	}

	@Override
	public int compare(Team team1, Team team2) {
		if(priorityTeams == null
				|| (priorityTeams.contains(team1.getId()) && priorityTeams.contains(team2.getId()))
				|| (!priorityTeams.contains(team1.getId()) && !priorityTeams.contains(team2.getId()))) {
			return team1.getName().compareToIgnoreCase(team2.getName());
		} else if(priorityTeams.contains(team1.getId())) {
			return -1;
		} else if(priorityTeams.contains(team2.getId())) {
			return 1;
		}

		return 0;
	}
}
