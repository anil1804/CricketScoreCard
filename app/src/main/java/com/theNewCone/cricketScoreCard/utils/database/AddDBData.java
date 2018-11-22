package com.theNewCone.cricketScoreCard.utils.database;

import android.content.Context;

import com.theNewCone.cricketScoreCard.match.Team;
import com.theNewCone.cricketScoreCard.player.Player;

public class AddDBData {

	private Context context;
	private DatabaseHandler dbh;

	public AddDBData(Context context) {
		this.context = context;
		dbh = new DatabaseHandler(context);
	}

	public boolean addAll() {
		if(addTeams())
		{
			return addPlayers();
		}

		return false;
	}

	private boolean addTeams() {
		dbh.upsertTeam(new Team("Australia", "AUS"));
		dbh.upsertTeam(new Team("Pakistan", "PAK"));
		dbh.upsertTeam(new Team("India", "IND"));
		dbh.upsertTeam(new Team("West Indies", "WI"));

		return true;
	}

	private boolean addPlayers() {
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

		dbh.upsertPlayer(new Player("Rohit Sharma", 31, Player.BattingType.RHB, Player.BowlingType.OB, false));
		dbh.upsertPlayer(new Player("Skhikhar Dhawan", 32, Player.BattingType.LHB, Player.BowlingType.OB, false));
		dbh.upsertPlayer(new Player("Lokesh Rahul", 26, Player.BattingType.RHB, Player.BowlingType.NONE, true));
		dbh.upsertPlayer(new Player("Manish Pandey", 29, Player.BattingType.RHB, Player.BowlingType.RM, false));
		dbh.upsertPlayer(new Player("Dinesh Karthik", 33, Player.BattingType.RHB, Player.BowlingType.NONE, true));
		dbh.upsertPlayer(new Player("Rishabh Pant", 21, Player.BattingType.LHB, Player.BowlingType.NONE, true));
		dbh.upsertPlayer(new Player("Krunal Pandya", 27, Player.BattingType.LHB, Player.BowlingType.SLA, false));
		dbh.upsertPlayer(new Player("Bhuvneshwar Kumar", 28, Player.BattingType.RHB, Player.BowlingType.RFM, false));
		dbh.upsertPlayer(new Player("Kuldeep Yadav", 23, Player.BattingType.LHB, Player.BowlingType.SLC, false));
		dbh.upsertPlayer(new Player("Jasprit Bumrah", 24, Player.BattingType.RHB, Player.BowlingType.RFM, false));
		dbh.upsertPlayer(new Player("K Khaleel Ahmed", 20, Player.BattingType.RHB, Player.BowlingType.LFM, false));
		dbh.upsertPlayer(new Player("Yuzvendra Chahal", 28, Player.BattingType.RHB, Player.BowlingType.LB, false));
		dbh.upsertPlayer(new Player("Washington Sundar", 20, Player.BattingType.LHB, Player.BowlingType.OB, false));
		dbh.upsertPlayer(new Player("Shreyas Iyer", 23, Player.BattingType.RHB, Player.BowlingType.LB, false));
		dbh.upsertPlayer(new Player("Umesh Yadav", 31, Player.BattingType.RHB, Player.BowlingType.RF, false));
		dbh.upsertPlayer(new Player("Shahbaz Nadeem", 29, Player.BattingType.RHB, Player.BowlingType.SLA, false));

		dbh.upsertPlayer(new Player("Rovman Powell", 25, Player.BattingType.RHB, Player.BowlingType.RFM, false));
		dbh.upsertPlayer(new Player("Nicholas Pooran", 23, Player.BattingType.LHB, Player.BowlingType.NONE, true));
		dbh.upsertPlayer(new Player("Darren Bravo", 29, Player.BattingType.LHB, Player.BowlingType.RFM, false));
		dbh.upsertPlayer(new Player("Shimron Hetmyer", 21, Player.BattingType.LHB, Player.BowlingType.NONE, false));
		dbh.upsertPlayer(new Player("Denesh Ramdin", 33, Player.BattingType.RHB, Player.BowlingType.NONE, true));
		dbh.upsertPlayer(new Player("Sherfane Rutherford", 20, Player.BattingType.LHB, Player.BowlingType.RFM, false));
		dbh.upsertPlayer(new Player("Kieron Pollard", 31, Player.BattingType.RHB, Player.BowlingType.RM, false));
		dbh.upsertPlayer(new Player("Carlos Brathwaite", 30, Player.BattingType.RHB, Player.BowlingType.RFM, false));
		dbh.upsertPlayer(new Player("Fabian Allen", 23, Player.BattingType.RHB, Player.BowlingType.SLA, false));
		dbh.upsertPlayer(new Player("Keemo Paul", 20, Player.BattingType.RHB, Player.BowlingType.RFM, false));
		dbh.upsertPlayer(new Player("Oshane Thomas", 20, Player.BattingType.LHB, Player.BowlingType.RFM, false));
		dbh.upsertPlayer(new Player("Khary Pierre", 27, Player.BattingType.LHB, Player.BowlingType.SLA, false));
		dbh.upsertPlayer(new Player("Obed McCoy", 21, Player.BattingType.LHB, Player.BowlingType.LFM, false));

		return true;
	}
}
