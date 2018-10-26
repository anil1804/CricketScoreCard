package com.thenewcone.myscorecard.comparator;

import com.thenewcone.myscorecard.match.Team;
import com.thenewcone.myscorecard.player.Player;

import java.util.Comparator;
import java.util.List;

public class PlayerComparator implements Comparator<Player> {

	private final List<Integer> priorityPlayers;

	public PlayerComparator(List<Integer> priorityPlayers) {
		this.priorityPlayers = priorityPlayers;
	}

	@Override
	public int compare(Player player1, Player player2) {
		if(priorityPlayers == null
				|| (priorityPlayers.contains(player1.getID()) && priorityPlayers.contains(player2.getID()))
				|| (!priorityPlayers.contains(player1.getID()) && !priorityPlayers.contains(player2.getID()))) {
			return player1.getName().compareToIgnoreCase(player2.getName());
		} else if(priorityPlayers.contains(player1.getID())) {
			return -1;
		} else if(priorityPlayers.contains(player2.getID())) {
			return 1;
		}

		return 0;
	}
}
