package com.theNewCone.cricketScoreCard.utils.database;

import android.content.Context;

import com.theNewCone.cricketScoreCard.enumeration.BattingType;
import com.theNewCone.cricketScoreCard.enumeration.BowlingType;
import com.theNewCone.cricketScoreCard.enumeration.TeamEnum;
import com.theNewCone.cricketScoreCard.match.Team;
import com.theNewCone.cricketScoreCard.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManageDBData {

	private final Context context;
	public ManageDBData(Context context) {
		this.context = context;
	}

	public List<Team> addTeams(TeamEnum teamValue) {
		List<Team> teamList = new ArrayList<>();

		if (teamValue == TeamEnum.AUS || teamValue == TeamEnum.ALL) {
			teamList.add(addTeam(new Team("Australia", "AUS")));
		}

		if (teamValue == TeamEnum.PAK || teamValue == TeamEnum.ALL) {
			teamList.add(addTeam(new Team("Pakistan", "PAK")));
		}

		if (teamValue == TeamEnum.IND || teamValue == TeamEnum.ALL) {
			teamList.add(addTeam(new Team("India", "IND")));
		}

		if (teamValue == TeamEnum.WI || teamValue == TeamEnum.ALL) {
			teamList.add(addTeam(new Team("West Indies", "WI")));
		}

		if (teamValue == TeamEnum.TX || teamValue == TeamEnum.ALL) {
			teamList.add(addTeam(new Team("Team-X", "TX")));
		}

		if (teamValue == TeamEnum.TY || teamValue == TeamEnum.ALL) {
			teamList.add(addTeam(new Team("Team-Y", "TY")));
		}

		if (teamValue == TeamEnum.PAKW || teamValue == TeamEnum.ALL) {
			teamList.add(addTeam(new Team("Pakistan Women", "PAKW")));
		}

		if (teamValue == TeamEnum.INDW || teamValue == TeamEnum.ALL) {
			teamList.add(addTeam(new Team("India Women", "INDW")));
		}

		if (teamValue == TeamEnum.SA || teamValue == TeamEnum.ALL) {
			teamList.add(addTeam(new Team("South Africa", "SA")));
		}

		if (teamValue == TeamEnum.NZ || teamValue == TeamEnum.ALL) {
			teamList.add(addTeam(new Team("New Zealand", "NZ")));
		}

		if (teamValue == TeamEnum.SL || teamValue == TeamEnum.ALL) {
			teamList.add(addTeam(new Team("Sri Lanka", "SL")));
		}

		if (teamValue == TeamEnum.ENG || teamValue == TeamEnum.ALL) {
			teamList.add(addTeam(new Team("England", "ENG")));
		}

		if (teamValue == TeamEnum.BAN || teamValue == TeamEnum.ALL) {
			teamList.add(addTeam(new Team("Bangladesh", "BAN")));
		}

		if (teamValue == TeamEnum.AFG || teamValue == TeamEnum.ALL) {
			teamList.add(addTeam(new Team("Afghanistan", "AFG")));
		}

		return teamList;
	}

	private Team addTeam(Team team) {
		TeamDBHandler dbh = new TeamDBHandler(context);
		int teamID = dbh.updateTeam(team, true);
		team.setId(teamID);
		return team;
	}

	private HashMap<TeamEnum, List<Integer>> addPlayers(TeamEnum teamEnum) {
		PlayerDBHandler dbh = new PlayerDBHandler(context);

		HashMap<TeamEnum, List<Integer>> countryPlayerIDMap = new HashMap<>();
		if (teamEnum == TeamEnum.PAK || teamEnum == TeamEnum.ALL) {
			List<Integer> playerIDList = new ArrayList<>();

			int playerID = dbh.updatePlayer(new Player("Fakhar Zaman", BattingType.LHB, BowlingType.SLA, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Babar Azam", BattingType.RHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Mohammad Hafeez", BattingType.RHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Asif Ali", BattingType.RHB, BowlingType.RM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Hussain Talat", BattingType.LHB, BowlingType.RM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Faheem Ashraf", BattingType.LHB, BowlingType.RM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Sarfraz Ahmed", BattingType.RHB, BowlingType.NONE, true), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Shadab Khan", BattingType.RHB, BowlingType.LB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Imad Wasim", BattingType.LHB, BowlingType.SLA, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Hasan Ali", BattingType.RHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Shaheen Afridi", BattingType.LHB, BowlingType.LFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Imran Nazir", BattingType.RHB, BowlingType.LB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Kamran Akmal", BattingType.RHB, BowlingType.NONE, true), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Younis Khan", BattingType.RHB, BowlingType.RM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Shoaib Malik", BattingType.RHB, BowlingType.RM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Misbah-ul-Haq", BattingType.RHB, BowlingType.LB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Shahid Afridi", BattingType.RHB, BowlingType.LB, true), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Yasir Arafat", BattingType.RHB, BowlingType.RM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Sohail Tanvir", BattingType.LHB, BowlingType.LFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Umar Gul", BattingType.RHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Mohammad Asif", BattingType.LHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);

			countryPlayerIDMap.put(TeamEnum.PAK, playerIDList);
		}

		if (teamEnum == TeamEnum.AUS || teamEnum == TeamEnum.ALL) {
			List<Integer> playerIDList = new ArrayList<>();

			int playerID = dbh.updatePlayer(new Player("Aaron Finch", BattingType.RHB, BowlingType.SLA, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("D Arcy Short", BattingType.LHB, BowlingType.SLC, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Chris Lynn", BattingType.RHB, BowlingType.SLC, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Glenn Maxwell", BattingType.RHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Ben McDermott", BattingType.RHB, BowlingType.RM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Alex Carey", BattingType.LHB, BowlingType.NONE, true), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Ashton Agar", BattingType.LHB, BowlingType.SLA, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Nathan Coulter-Nile", BattingType.RHB, BowlingType.RF, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Adam Zampa", BattingType.RHB, BowlingType.LB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Andrew Tye", BattingType.RHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Billy Stanlake", BattingType.LHB, BowlingType.RF, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("K Khaleel Ahmed", BattingType.RHB, BowlingType.LFM, false), true);
			playerIDList.add(playerID);

			countryPlayerIDMap.put(TeamEnum.AUS, playerIDList);
		}

		if (teamEnum == TeamEnum.IND || teamEnum == TeamEnum.ALL || teamEnum == TeamEnum.TEST) {
			List<Integer> playerIDList = new ArrayList<>();

			int playerID = dbh.updatePlayer(new Player("Rohit Sharma", BattingType.RHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Shikhar Dhawan", BattingType.LHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Lokesh Rahul", BattingType.RHB, BowlingType.NONE, true), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Manish Pandey", BattingType.RHB, BowlingType.RM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Dinesh Karthik", BattingType.RHB, BowlingType.NONE, true), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Rishabh Pant", BattingType.LHB, BowlingType.NONE, true), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Krunal Pandya", BattingType.LHB, BowlingType.SLA, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Bhuvneshwar Kumar", BattingType.RHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Kuldeep Yadav", BattingType.LHB, BowlingType.SLC, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Jasprit Bumrah", BattingType.RHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("K Khaleel Ahmed", BattingType.RHB, BowlingType.LFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Yuzvendra Chahal", BattingType.RHB, BowlingType.LB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Washington Sundar", BattingType.LHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Shreyas Iyer", BattingType.RHB, BowlingType.LB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Umesh Yadav", BattingType.RHB, BowlingType.RF, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Shahbaz Nadeem", BattingType.RHB, BowlingType.SLA, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Virat Kohli", BattingType.RHB, BowlingType.RM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("MS Dhoni", BattingType.RHB, BowlingType.RM, true), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Ambati Rayudu", BattingType.RHB, BowlingType.OB, true), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Mohammed Shami", BattingType.RHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Ravindra Jadeja", BattingType.LHB, BowlingType.SLA, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Gautam Gambhir", BattingType.LHB, BowlingType.LB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Yusuf Pathan", BattingType.RHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Robin Uthappa", BattingType.RHB, BowlingType.RM, true), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Yuvraj Singh", BattingType.LHB, BowlingType.SLA, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Irfan Pathan", BattingType.LHB, BowlingType.LFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Harbhajan Singh", BattingType.RHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Joginder Sharma", BattingType.RHB, BowlingType.RFM, true), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("S Sreesanth", BattingType.RHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("RP Singh", BattingType.RHB, BowlingType.LFM, false), true);
			playerIDList.add(playerID);

			countryPlayerIDMap.put(TeamEnum.IND, playerIDList);
		}

		if (teamEnum == TeamEnum.WI || teamEnum == TeamEnum.ALL) {
			List<Integer> playerIDList = new ArrayList<>();

			int playerID = dbh.updatePlayer(new Player("Rovman Powell", BattingType.RHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Nicholas Pooran", BattingType.LHB, BowlingType.NONE, true), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Darren Bravo", BattingType.LHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Shimron Hetmyer", BattingType.LHB, BowlingType.NONE, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Denesh Ramdin", BattingType.RHB, BowlingType.NONE, true), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Sherfane Rutherford", BattingType.LHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Kieron Pollard", BattingType.RHB, BowlingType.RM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Carlos Brathwaite", BattingType.RHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Fabian Allen", BattingType.RHB, BowlingType.SLA, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Keemo Paul", BattingType.RHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Oshane Thomas", BattingType.LHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Khary Pierre", BattingType.LHB, BowlingType.SLA, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Obed McCoy", BattingType.LHB, BowlingType.LFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Kieron Powell", BattingType.LHB, BowlingType.RM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Chandrapaul Hemraj", BattingType.LHB, BowlingType.SLA, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Shai Hope", BattingType.RHB, BowlingType.NONE, true), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Marlon Samuels", BattingType.RHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Jason Holder", BattingType.RHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Ashley Nurse", BattingType.RHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Kemar Roach", BattingType.RHB, BowlingType.RF, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Devendra Bishoo", BattingType.LHB, BowlingType.LB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Chris Gayle", BattingType.LHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Devon Smith", BattingType.LHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Shivnarine Chanderpaul", BattingType.LHB, BowlingType.LB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Dwayne Smith", BattingType.RHB, BowlingType.RM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Dwayne Bravo", BattingType.RHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Ramnaresh Sarwan", BattingType.RHB, BowlingType.LB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Daren Powell", BattingType.RHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Ravi Rampaul", BattingType.LHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Fidel Edwards", BattingType.RHB, BowlingType.RF, false), true);
			playerIDList.add(playerID);

			countryPlayerIDMap.put(TeamEnum.WI, playerIDList);
		}

		if (teamEnum == TeamEnum.PAKW || teamEnum == TeamEnum.ALL) {
			List<Integer> playerIDList = new ArrayList<>();

			int playerID = dbh.updatePlayer(new Player("Ayesha Zafar", BattingType.RHB, BowlingType.LB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Javeria Khan", BattingType.RHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Umaima Sohail", BattingType.RHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Bismah Maroof", BattingType.LHB, BowlingType.LB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Nida Dar", BattingType.RHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Aliya Riaz", BattingType.RHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Nahida Khan", BattingType.RHB, BowlingType.RM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Sana Mir", BattingType.RHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Sidra Nawaz", BattingType.RHB, BowlingType.NONE, true), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Diana Baig", BattingType.RHB, BowlingType.RM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Anam Amin", BattingType.LHB, BowlingType.SLA, false), true);
			playerIDList.add(playerID);

			countryPlayerIDMap.put(TeamEnum.PAKW, playerIDList);
		}

		if (teamEnum == TeamEnum.INDW || teamEnum == TeamEnum.ALL) {
			List<Integer> playerIDList = new ArrayList<>();

			int playerID = dbh.updatePlayer(new Player("Mithali Raj", BattingType.RHB, BowlingType.LB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Smriti Mandhana", BattingType.LHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Jemimah Rodrigues", BattingType.RHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Harmanpreet Kaur", BattingType.RHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Veda Krishnamurthy", BattingType.RHB, BowlingType.LB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Taniya Bhatia", BattingType.RHB, BowlingType.NONE, true), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Dayalan Hemalatha", BattingType.RHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Deepti Sharma", BattingType.LHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Radha Yadav", BattingType.RHB, BowlingType.SLA, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Arundhati Reddy", BattingType.RHB, BowlingType.NONE, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Poonam Yadav", BattingType.RHB, BowlingType.LB, false), true);
			playerIDList.add(playerID);

			countryPlayerIDMap.put(TeamEnum.INDW, playerIDList);
		}

		if (teamEnum == TeamEnum.SA || teamEnum == TeamEnum.ALL) {
			List<Integer> playerIDList = new ArrayList<>();

			int playerID = dbh.updatePlayer(new Player("Graeme Smith", BattingType.LHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Herschelle Gibbs", BattingType.RHB, BowlingType.NONE, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("AB de Villiers", BattingType.RHB, BowlingType.RM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Justin Kemp", BattingType.RHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Mark Boucher", BattingType.RHB, BowlingType.RM, true), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Shaun Pollock", BattingType.RHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Albie Morkel", BattingType.LHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Johan van der Wath", BattingType.RHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Vernon Philander", BattingType.RHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Makhaya Ntini", BattingType.RHB, BowlingType.RF, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Morne Morkel", BattingType.LHB, BowlingType.RF, false), true);
			playerIDList.add(playerID);

			countryPlayerIDMap.put(TeamEnum.SA, playerIDList);
		}


		if (teamEnum == TeamEnum.NZ || teamEnum == TeamEnum.ALL) {
			List<Integer> playerIDList = new ArrayList<>();

			int playerID = dbh.updatePlayer(new Player("Rob Nicol", BattingType.RHB, BowlingType.RM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Martin Guptill", BattingType.RHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Brendon McCullum", BattingType.RHB, BowlingType.NONE, true), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Ross Taylor", BattingType.RHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Jacob Oram", BattingType.LHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Nathan McCullum", BattingType.RHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("James Franklin", BattingType.LHB, BowlingType.LM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Kane Williamson", BattingType.RHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Daniel Vettori", BattingType.LHB, BowlingType.SLA, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Tim Southee", BattingType.RHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Kyle Mills", BattingType.RHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);

			countryPlayerIDMap.put(TeamEnum.NZ, playerIDList);
		}

		if (teamEnum == TeamEnum.SL || teamEnum == TeamEnum.ALL) {
			List<Integer> playerIDList = new ArrayList<>();

			int playerID = dbh.updatePlayer(new Player("Mahela Jayawardene", BattingType.RHB, BowlingType.RM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Tillakaratne Dilshan", BattingType.RHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Kumar Sangakkara", BattingType.RHB, BowlingType.OB, true), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Jeevan Mendis", BattingType.LHB, BowlingType.LB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Angelo Mathews", BattingType.RHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Thisara Perera", BattingType.LHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Lahiru Thirimanne", BattingType.LHB, BowlingType.RM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Nuwan Kulasekara", BattingType.RHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Lasith Malinga", BattingType.RHB, BowlingType.RF, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Ajantha Mendis", BattingType.RHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Akila Dananjaya", BattingType.LHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);

			countryPlayerIDMap.put(TeamEnum.SL, playerIDList);
		}

		if (teamEnum == TeamEnum.ENG || teamEnum == TeamEnum.ALL) {
			List<Integer> playerIDList = new ArrayList<>();

			int playerID = dbh.updatePlayer(new Player("Jason Roy", BattingType.RHB, BowlingType.NONE, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Jonny Bairstow", BattingType.RHB, BowlingType.RM, true), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Joe Root", BattingType.RHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Eoin Morgan", BattingType.LHB, BowlingType.RM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Ben Stokes", BattingType.LHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Jos Buttler", BattingType.RHB, BowlingType.NONE, true), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Moeen Ali", BattingType.LHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Chris Woakes", BattingType.RHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Adil Rashid", BattingType.RHB, BowlingType.LB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Liam Plunkett", BattingType.RHB, BowlingType.RF, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Mark Wood", BattingType.RHB, BowlingType.RF, false), true);
			playerIDList.add(playerID);

			countryPlayerIDMap.put(TeamEnum.ENG, playerIDList);
		}

		if (teamEnum == TeamEnum.BAN || teamEnum == TeamEnum.ALL) {
			List<Integer> playerIDList = new ArrayList<>();

			int playerID = dbh.updatePlayer(new Player("Tamim Iqbal", BattingType.LHB, BowlingType.NONE, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Liton Das", BattingType.RHB, BowlingType.NONE, true), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Soumya Sarkar", BattingType.LHB, BowlingType.RM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Mushfiqur Rahim", BattingType.RHB, BowlingType.NONE, true), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Mohammad Mithun", BattingType.RHB, BowlingType.NONE, true), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Mahmudullah", BattingType.RHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Sabbir Rahman", BattingType.RHB, BowlingType.LB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Mehidy Hasan", BattingType.RHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Mohammad Saifuddin", BattingType.LHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Mashrafe Mortaza", BattingType.RHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Mustafizur Rahman", BattingType.LHB, BowlingType.LFM, false), true);
			playerIDList.add(playerID);

			countryPlayerIDMap.put(TeamEnum.BAN, playerIDList);
		}

		if (teamEnum == TeamEnum.AFG || teamEnum == TeamEnum.ALL) {
			List<Integer> playerIDList = new ArrayList<>();

			int playerID = dbh.updatePlayer(new Player("Hazratullah Zazai", BattingType.LHB, BowlingType.SLA, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Asghar Afghan", BattingType.RHB, BowlingType.RFM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Samiullah Shenwari", BattingType.RHB, BowlingType.LB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Karim Janat", BattingType.RHB, BowlingType.RM, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Mohammad Nabi", BattingType.RHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Sharafuddin Ashraf", BattingType.RHB, BowlingType.SLA, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Najibullah Zadran", BattingType.LHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Shafiqullah Shafiq", BattingType.RHB, BowlingType.NONE, true), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Rashid Khan", BattingType.RHB, BowlingType.LB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Mujeeb Ur Rahman", BattingType.RHB, BowlingType.OB, false), true);
			playerIDList.add(playerID);
			playerID = dbh.updatePlayer(new Player("Fareed Malik", BattingType.LHB, BowlingType.LFM, false), true);
			playerIDList.add(playerID);

			countryPlayerIDMap.put(TeamEnum.AFG, playerIDList);
		}

		return countryPlayerIDMap;
	}

	public void addTeamsAndPlayers(TeamEnum teamValue) {
		TeamDBHandler dbh = new TeamDBHandler(context);

		List<Team> teamList = addTeams(teamValue);
		for (Team team : teamList) {
			TeamEnum teamEnum = TeamEnum.valueOf(team.getShortName());
			List<Integer> playerIDList = addPlayers(teamEnum).get(teamEnum);
			dbh.updateTeamList(team, playerIDList, null);
		}
	}
}
