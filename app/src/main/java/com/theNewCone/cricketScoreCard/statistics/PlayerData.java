package com.theNewCone.cricketScoreCard.statistics;

import com.theNewCone.cricketScoreCard.player.Player;

public class PlayerData {
	private Player player;
	private BatsmanData batsmanData;
	private BowlerData bowlerData;
	private PlayerMatchData playerData;

	public PlayerData(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public BatsmanData getBatsmanData() {
		return batsmanData;
	}

	public void setBatsmanData(BatsmanData batsmanData) {
		this.batsmanData = batsmanData;
	}

	public BowlerData getBowlerData() {
		return bowlerData;
	}

	public void setBowlerData(BowlerData bowlerData) {
		this.bowlerData = bowlerData;
	}

	public PlayerMatchData getPlayerData() {
		return playerData;
	}

	public void setPlayerData(PlayerMatchData playerData) {
		this.playerData = playerData;
	}
}
