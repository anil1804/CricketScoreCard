package com.theNewCone.cricketScoreCard.utils.database;

import android.content.Context;

import com.theNewCone.cricketScoreCard.enumeration.BattingType;
import com.theNewCone.cricketScoreCard.enumeration.BowlingType;
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
		dbh.upsertPlayer(new Player("Fakhar Zaman", 28, BattingType.LHB, BowlingType.SLA, false));
		dbh.upsertPlayer(new Player("Babar Azam", 24, BattingType.RHB, BowlingType.OB, false));
		dbh.upsertPlayer(new Player("Mohammad Hafeez", 38, BattingType.RHB, BowlingType.OB, false));
		dbh.upsertPlayer(new Player("Asif Ali", 27, BattingType.RHB, BowlingType.RM, false));
		dbh.upsertPlayer(new Player("Hussain Talat", 21, BattingType.LHB, BowlingType.RM, false));
		dbh.upsertPlayer(new Player("Faheem Ashraf", 24, BattingType.LHB, BowlingType.RM, false));
		dbh.upsertPlayer(new Player("Sarfraz Ahmed", 31, BattingType.RHB, BowlingType.NONE, true));
		dbh.upsertPlayer(new Player("Shadab Khan", 24, BattingType.RHB, BowlingType.LB, false));
		dbh.upsertPlayer(new Player("Imad Wasim", 29, BattingType.LHB, BowlingType.SLA, false));
		dbh.upsertPlayer(new Player("Hasan Ali", 24, BattingType.RHB, BowlingType.RFM, false));
		dbh.upsertPlayer(new Player("Shaheen Afridi", 18, BattingType.LHB, BowlingType.LFM, false));

		dbh.upsertPlayer(new Player("Aaron Finch", 31, BattingType.RHB, BowlingType.SLA, false));
		dbh.upsertPlayer(new Player("D Arcy Short", 28, BattingType.LHB, BowlingType.SLC, false));
		dbh.upsertPlayer(new Player("Chris Lynn", 28, BattingType.RHB, BowlingType.SLC, false));
		dbh.upsertPlayer(new Player("Glenn Maxwell", 30, BattingType.RHB, BowlingType.OB, false));
		dbh.upsertPlayer(new Player("Ben McDermott", 23, BattingType.RHB, BowlingType.RM, false));
		dbh.upsertPlayer(new Player("Alex Carey", 27, BattingType.LHB, BowlingType.NONE, true));
		dbh.upsertPlayer(new Player("Ashton Agar", 25, BattingType.LHB, BowlingType.SLA, false));
		dbh.upsertPlayer(new Player("Nathan Coulter-Nile", 31, BattingType.RHB, BowlingType.RF, false));
		dbh.upsertPlayer(new Player("Adam Zampa", 26, BattingType.RHB, BowlingType.LB, false));
		dbh.upsertPlayer(new Player("Andrew Tye", 31, BattingType.RHB, BowlingType.RFM, false));
		dbh.upsertPlayer(new Player("Billy Stanlake", 23, BattingType.LHB, BowlingType.RF, false));

		dbh.upsertPlayer(new Player("Rohit Sharma", 31, BattingType.RHB, BowlingType.OB, false));
		dbh.upsertPlayer(new Player("Skhikhar Dhawan", 32, BattingType.LHB, BowlingType.OB, false));
		dbh.upsertPlayer(new Player("Lokesh Rahul", 26, BattingType.RHB, BowlingType.NONE, true));
		dbh.upsertPlayer(new Player("Manish Pandey", 29, BattingType.RHB, BowlingType.RM, false));
		dbh.upsertPlayer(new Player("Dinesh Karthik", 33, BattingType.RHB, BowlingType.NONE, true));
		dbh.upsertPlayer(new Player("Rishabh Pant", 21, BattingType.LHB, BowlingType.NONE, true));
		dbh.upsertPlayer(new Player("Krunal Pandya", 27, BattingType.LHB, BowlingType.SLA, false));
		dbh.upsertPlayer(new Player("Bhuvneshwar Kumar", 28, BattingType.RHB, BowlingType.RFM, false));
		dbh.upsertPlayer(new Player("Kuldeep Yadav", 23, BattingType.LHB, BowlingType.SLC, false));
		dbh.upsertPlayer(new Player("Jasprit Bumrah", 24, BattingType.RHB, BowlingType.RFM, false));
		dbh.upsertPlayer(new Player("K Khaleel Ahmed", 20, BattingType.RHB, BowlingType.LFM, false));
		dbh.upsertPlayer(new Player("Yuzvendra Chahal", 28, BattingType.RHB, BowlingType.LB, false));
		dbh.upsertPlayer(new Player("Washington Sundar", 20, BattingType.LHB, BowlingType.OB, false));
		dbh.upsertPlayer(new Player("Shreyas Iyer", 23, BattingType.RHB, BowlingType.LB, false));
		dbh.upsertPlayer(new Player("Umesh Yadav", 31, BattingType.RHB, BowlingType.RF, false));
		dbh.upsertPlayer(new Player("Shahbaz Nadeem", 29, BattingType.RHB, BowlingType.SLA, false));

		dbh.upsertPlayer(new Player("Rovman Powell", 25, BattingType.RHB, BowlingType.RFM, false));
		dbh.upsertPlayer(new Player("Nicholas Pooran", 23, BattingType.LHB, BowlingType.NONE, true));
		dbh.upsertPlayer(new Player("Darren Bravo", 29, BattingType.LHB, BowlingType.RFM, false));
		dbh.upsertPlayer(new Player("Shimron Hetmyer", 21, BattingType.LHB, BowlingType.NONE, false));
		dbh.upsertPlayer(new Player("Denesh Ramdin", 33, BattingType.RHB, BowlingType.NONE, true));
		dbh.upsertPlayer(new Player("Sherfane Rutherford", 20, BattingType.LHB, BowlingType.RFM, false));
		dbh.upsertPlayer(new Player("Kieron Pollard", 31, BattingType.RHB, BowlingType.RM, false));
		dbh.upsertPlayer(new Player("Carlos Brathwaite", 30, BattingType.RHB, BowlingType.RFM, false));
		dbh.upsertPlayer(new Player("Fabian Allen", 23, BattingType.RHB, BowlingType.SLA, false));
		dbh.upsertPlayer(new Player("Keemo Paul", 20, BattingType.RHB, BowlingType.RFM, false));
		dbh.upsertPlayer(new Player("Oshane Thomas", 20, BattingType.LHB, BowlingType.RFM, false));
		dbh.upsertPlayer(new Player("Khary Pierre", 27, BattingType.LHB, BowlingType.SLA, false));
		dbh.upsertPlayer(new Player("Obed McCoy", 21, BattingType.LHB, BowlingType.LFM, false));

		return true;
	}
}
