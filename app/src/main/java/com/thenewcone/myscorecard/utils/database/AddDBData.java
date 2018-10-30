package com.thenewcone.myscorecard.utils.database;

import android.content.Context;

import com.thenewcone.myscorecard.match.Team;
import com.thenewcone.myscorecard.player.Player;

public class AddDBData {

	Context context;
	DatabaseHandler dbh;

	public AddDBData(Context context) {
		this.context = context;
		dbh = new DatabaseHandler(context);
	}

	public boolean addTeams() {
		dbh.upsertTeam(new Team("Australia", "AUS"));
		dbh.upsertTeam(new Team("Pakistan", "PAK"));

		return true;
	}

	public boolean addPlayers() {
		dbh.upsertPlayer(new Player("Fakhar Zaman", 28, Player.BattingType.LHB, Player.BowlingType.SLA, false));
		dbh.upsertPlayer(new Player("Babar Azam", 24, Player.BattingType.RHB, Player.BowlingType.OB, false));
		dbh.upsertPlayer(new Player("Mohammad Hafeez", 38, Player.BattingType.RHB, Player.BowlingType.OB, false));
		dbh.upsertPlayer(new Player("Asif Ali", 27, Player.BattingType.RHB, Player.BowlingType.RM, false));
		dbh.upsertPlayer(new Player("Hussain Talat", 21, Player.BattingType.LHB, Player.BowlingType.RM, false));
		dbh.upsertPlayer(new Player("Faheem Ashraf", 24, Player.BattingType.LHB, Player.BowlingType.RM, false));
		dbh.upsertPlayer(new Player("Sarfraz Ahmed", 31, Player.BattingType.RHB, Player.BowlingType.NONE, true));
		dbh.upsertPlayer(new Player("Shadab Khan", 24, Player.BattingType.RHB, Player.BowlingType.LB, false));
		dbh.upsertPlayer(new Player("Imad Wasim", 29, Player.BattingType.LHB, Player.BowlingType.SLA, false));
		dbh.upsertPlayer(new Player("Hasan Ali", 24, Player.BattingType.RHB, Player.BowlingType.RFM, false));
		dbh.upsertPlayer(new Player("Shaheen Afridi", 18, Player.BattingType.LHB, Player.BowlingType.LFM, false));

		dbh.upsertPlayer(new Player("Aaron Finch", 31, Player.BattingType.RHB, Player.BowlingType.SLA, false));
		dbh.upsertPlayer(new Player("D Arcy Short", 28, Player.BattingType.LHB, Player.BowlingType.SLC, false));
		dbh.upsertPlayer(new Player("Chris Lynn", 28, Player.BattingType.RHB, Player.BowlingType.SLC, false));
		dbh.upsertPlayer(new Player("Glenn Maxwell", 30, Player.BattingType.RHB, Player.BowlingType.OB, false));
		dbh.upsertPlayer(new Player("Ben McDermott", 23, Player.BattingType.RHB, Player.BowlingType.RM, false));
		dbh.upsertPlayer(new Player("Alex Carey", 27, Player.BattingType.LHB, Player.BowlingType.NONE, true));
		dbh.upsertPlayer(new Player("Ashton Agar", 25, Player.BattingType.LHB, Player.BowlingType.SLA, false));
		dbh.upsertPlayer(new Player("Nathan Coulter-Nile", 31, Player.BattingType.RHB, Player.BowlingType.RF, false));
		dbh.upsertPlayer(new Player("Adam Zampa", 26, Player.BattingType.RHB, Player.BowlingType.LB, false));
		dbh.upsertPlayer(new Player("Andrew Tye", 31, Player.BattingType.RHB, Player.BowlingType.RFM, false));
		dbh.upsertPlayer(new Player("Billy Stanlake", 23, Player.BattingType.LHB, Player.BowlingType.RF, false));
		return true;
	}
}
