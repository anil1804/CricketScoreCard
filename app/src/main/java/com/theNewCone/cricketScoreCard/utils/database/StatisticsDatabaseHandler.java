package com.theNewCone.cricketScoreCard.utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.theNewCone.cricketScoreCard.match.CricketCard;
import com.theNewCone.cricketScoreCard.match.CricketCardUtils;
import com.theNewCone.cricketScoreCard.player.BatsmanStats;
import com.theNewCone.cricketScoreCard.player.BowlerStats;
import com.theNewCone.cricketScoreCard.player.FielderStats;

import java.util.Collection;

public class StatisticsDatabaseHandler extends SQLiteOpenHelper {

	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "Statistics";

	private static final String TBL_PLAYER_STATS = "PlayerStatistics";
	private static final String TBL_PLAYER_STATS_ID = "ID";
	private static final String TBL_PLAYER_STATS_MATCH_ID = "MatchID";
	private static final String TBL_PLAYER_STATS_TOURNAMENT_ID = "TournamentID";
	private static final String TBL_PLAYER_STATS_CATCHES = "Catches";
	private static final String TBL_PLAYER_STATS_RUN_OUTS = "RunOuts";
	private static final String TBL_PLAYER_STATS_STUMP_OUTS = "StumpOuts";

	private static final String TBL_BATSMAN_STATS = "BatsmanStatistics";
	private static final String TBL_BATSMAN_STATS_ID = "ID";
	private static final String TBL_BATSMAN_STATS_MATCH_ID = "MatchID";
	private static final String TBL_BATSMAN_STATS_TOURNAMENT_ID = "TournamentID";
	private static final String TBL_BATSMAN_STATS_RUNS = "Runs";
	private static final String TBL_BATSMAN_STATS_BALLS = "Balls";
	private static final String TBL_BATSMAN_STATS_DOTS = "Dots";
	private static final String TBL_BATSMAN_STATS_ONES = "Ones";
	private static final String TBL_BATSMAN_STATS_TWOS = "Twos";
	private static final String TBL_BATSMAN_STATS_THREES = "Threes";
	private static final String TBL_BATSMAN_STATS_FOURS = "Fours";
	private static final String TBL_BATSMAN_STATS_FIVES = "Fives";
	private static final String TBL_BATSMAN_STATS_SIXES = "Sixes";
	private static final String TBL_BATSMAN_STATS_SEVENS = "Sevens";
	private static final String TBL_BATSMAN_STATS_IS_OUT = "isOut";
	private static final String TBL_BATSMAN_STATS_DISMISSAL_TYPE = "DismissalType";
	private static final String TBL_BATSMAN_STATS_DISMISSED_BY = "DismissedBy";

	private static final String TBL_BOWLER_STATS = "BowlerStatistics";
	private static final String TBL_BOWLER_STATS_ID = "ID";
	private static final String TBL_BOWLER_STATS_MATCH_ID = "MatchID";
	private static final String TBL_BOWLER_STATS_TOURNAMENT_ID = "TournamentID";
	private static final String TBL_BOWLER_STATS_RUNS_GIVEN = "RunsGiven";
	private static final String TBL_BOWLER_STATS_OVERS_BOWLED = "OversBowled";
	private static final String TBL_BOWLER_STATS_WICKETS_TAKEN = "WicketsTaken";
	private static final String TBL_BOWLER_STATS_MAIDENS = "Maidens";
	private static final String TBL_BOWLER_STATS_BOWLED = "Bowled";
	private static final String TBL_BOWLER_STATS_CAUGHT = "Caught";
	private static final String TBL_BOWLER_STATS_HIT_WICKET = "HitWicket";
	private static final String TBL_BOWLER_STATS_LBW = "LBW";
	private static final String TBL_BOWLER_STATS_STUMPED = "Stumped";

	public StatisticsDatabaseHandler(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createPlayerStatsTable(db);
		createBatsmanStatsTable(db);
		createBowlerStatsTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

	private void createPlayerStatsTable(SQLiteDatabase db) {
		String createTableSQL =
				"CREATE TABLE " + TBL_PLAYER_STATS + "("
						+ TBL_PLAYER_STATS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ TBL_PLAYER_STATS_MATCH_ID + " INTEGER, "
						+ TBL_PLAYER_STATS_TOURNAMENT_ID + " INTEGER, "
						+ TBL_PLAYER_STATS_CATCHES + " INTEGER, "
						+ TBL_PLAYER_STATS_RUN_OUTS + " INTEGER, "
						+ TBL_PLAYER_STATS_STUMP_OUTS + " INTEGER"
						+ ")";

		db.execSQL(createTableSQL);
	}

	private void createBatsmanStatsTable(SQLiteDatabase db) {
		String createTableSQL =
				"CREATE TABLE " + TBL_BATSMAN_STATS + "("
						+ TBL_BATSMAN_STATS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ TBL_BATSMAN_STATS_MATCH_ID + " INTEGER, "
						+ TBL_BATSMAN_STATS_TOURNAMENT_ID + " INTEGER, "
						+ TBL_BATSMAN_STATS_RUNS + " INTEGER, "
						+ TBL_BATSMAN_STATS_BALLS + " INTEGER, "
						+ TBL_BATSMAN_STATS_DOTS + " INTEGER, "
						+ TBL_BATSMAN_STATS_ONES + " INTEGER, "
						+ TBL_BATSMAN_STATS_TWOS + " INTEGER, "
						+ TBL_BATSMAN_STATS_THREES + " INTEGER, "
						+ TBL_BATSMAN_STATS_FOURS + " INTEGER, "
						+ TBL_BATSMAN_STATS_FIVES + " INTEGER, "
						+ TBL_BATSMAN_STATS_SIXES + " INTEGER, "
						+ TBL_BATSMAN_STATS_SEVENS + " INTEGER, "
						+ TBL_BATSMAN_STATS_IS_OUT + " INTEGER, "
						+ TBL_BATSMAN_STATS_DISMISSAL_TYPE + " TEXT, "
						+ TBL_BATSMAN_STATS_DISMISSED_BY + " INTEGER"
						+ ")";

		db.execSQL(createTableSQL);
	}

	private void createBowlerStatsTable(SQLiteDatabase db) {
		String createTableSQL =
				"CREATE TABLE " + TBL_BOWLER_STATS + "("
						+ TBL_BOWLER_STATS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ TBL_BOWLER_STATS_MATCH_ID + " INTEGER, "
						+ TBL_BOWLER_STATS_TOURNAMENT_ID + " INTEGER, "
						+ TBL_BOWLER_STATS_OVERS_BOWLED + " TEXT, "
						+ TBL_BOWLER_STATS_RUNS_GIVEN + " INTEGER, "
						+ TBL_BOWLER_STATS_WICKETS_TAKEN + " INTEGER, "
						+ TBL_BOWLER_STATS_MAIDENS + " INTEGER, "
						+ TBL_BOWLER_STATS_BOWLED + " INTEGER, "
						+ TBL_BOWLER_STATS_CAUGHT + " INTEGER, "
						+ TBL_BOWLER_STATS_HIT_WICKET + " INTEGER, "
						+ TBL_BOWLER_STATS_LBW + " INTEGER, "
						+ TBL_BOWLER_STATS_STUMPED + " INTEGER"
						+ ")";

		db.execSQL(createTableSQL);
	}

	public void addPlayerStats(CricketCardUtils ccUtils, int tournamentID) {
		if(ccUtils != null) {
			addPlayerStats(ccUtils.getPrevInningsCard(), ccUtils.getMatchInfo().getMatchID(), tournamentID);
			addPlayerStats(ccUtils.getCard(), ccUtils.getMatchInfo().getMatchID(), tournamentID);
		}
	}

	private void addPlayerStats(CricketCard card, int matchID, int tournamentID) {
		if(card != null) {
			addPlayerStats(card.getFielderMap().values(), matchID, tournamentID);
			addBatsmanStats(card.getBatsmen().values(), matchID, tournamentID);
			addBowlerStats(card.getBowlerMap().values(), matchID, tournamentID);
		}
	}

	private void addPlayerStats(@NonNull Collection<FielderStats> fielderStatsCollection, int matchID, int tournamentID) {
		if(fielderStatsCollection.size() > 0) {
			SQLiteDatabase db = this.getWritableDatabase();

			for(FielderStats fielderStats : fielderStatsCollection) {
				ContentValues playerValues = new ContentValues();

				playerValues.put(TBL_PLAYER_STATS_MATCH_ID, matchID);
				playerValues.put(TBL_PLAYER_STATS_TOURNAMENT_ID, tournamentID);
				playerValues.put(TBL_PLAYER_STATS_CATCHES, fielderStats.getCatches());
				playerValues.put(TBL_PLAYER_STATS_RUN_OUTS, fielderStats.getRunOuts());
				playerValues.put(TBL_PLAYER_STATS_STUMP_OUTS, fielderStats.getStumpOuts());

				db.insert(TBL_PLAYER_STATS, null, playerValues);
			}
		}
	}

	private void addBatsmanStats(@NonNull Collection<BatsmanStats> batsmanStatsCollection, int matchID, int tournamentID) {
		if(batsmanStatsCollection.size() > 0) {
			SQLiteDatabase db = this.getWritableDatabase();

			for(BatsmanStats batsmanStats : batsmanStatsCollection) {
				ContentValues batsmanValues = new ContentValues();

				batsmanValues.put(TBL_BATSMAN_STATS_MATCH_ID, matchID);
				batsmanValues.put(TBL_BATSMAN_STATS_TOURNAMENT_ID, tournamentID);
				batsmanValues.put(TBL_BATSMAN_STATS_RUNS, batsmanStats.getRunsScored());
				batsmanValues.put(TBL_BATSMAN_STATS_BALLS, batsmanStats.getBallsPlayed());
				batsmanValues.put(TBL_BATSMAN_STATS_DOTS, batsmanStats.getDots());
				batsmanValues.put(TBL_BATSMAN_STATS_ONES, batsmanStats.getSingles());
				batsmanValues.put(TBL_BATSMAN_STATS_TWOS, batsmanStats.getTwos());
				batsmanValues.put(TBL_BATSMAN_STATS_THREES, batsmanStats.getThrees());
				batsmanValues.put(TBL_BATSMAN_STATS_FOURS, batsmanStats.getNum4s());
				batsmanValues.put(TBL_BATSMAN_STATS_FIVES, batsmanStats.getFives());
				batsmanValues.put(TBL_BATSMAN_STATS_SIXES, batsmanStats.getNum6s());
				batsmanValues.put(TBL_BATSMAN_STATS_SEVENS, batsmanStats.getSevens());
				batsmanValues.put(TBL_BATSMAN_STATS_IS_OUT, batsmanStats.isNotOut() ? 0 : 1);
				if(batsmanStats.getDismissalType() != null)
					batsmanValues.put(TBL_BATSMAN_STATS_DISMISSAL_TYPE, batsmanStats.getDismissalType().toString());
				if(batsmanStats.getWicketTakenBy() != null)
					batsmanValues.put(TBL_BATSMAN_STATS_DISMISSED_BY, batsmanStats.getWicketTakenBy().getPlayer().getID());

				db.insert(TBL_BATSMAN_STATS, null, batsmanValues);
			}
		}
	}

	private void addBowlerStats(@NonNull Collection<BowlerStats> bowlerStatsCollection, int matchID, int tournamentID) {
		if(bowlerStatsCollection.size() > 0) {
			SQLiteDatabase db = this.getWritableDatabase();

			for(BowlerStats bowlerStats : bowlerStatsCollection) {
				ContentValues bowlerValues = new ContentValues();

				bowlerValues.put(TBL_BOWLER_STATS_MATCH_ID, matchID);
				bowlerValues.put(TBL_BOWLER_STATS_TOURNAMENT_ID, tournamentID);
				bowlerValues.put(TBL_BOWLER_STATS_RUNS_GIVEN, bowlerStats.getRunsGiven());
				bowlerValues.put(TBL_BOWLER_STATS_OVERS_BOWLED, bowlerStats.getOversBowled());
				bowlerValues.put(TBL_BOWLER_STATS_WICKETS_TAKEN, bowlerStats.getWickets());
				bowlerValues.put(TBL_BOWLER_STATS_MAIDENS, bowlerStats.getMaidens());
				bowlerValues.put(TBL_BOWLER_STATS_BOWLED, bowlerStats.getBowled());
				bowlerValues.put(TBL_BOWLER_STATS_CAUGHT, bowlerStats.getCaught());
				bowlerValues.put(TBL_BOWLER_STATS_HIT_WICKET, bowlerStats.getHitWicket());
				bowlerValues.put(TBL_BOWLER_STATS_LBW, bowlerStats.getLbw());
				bowlerValues.put(TBL_BOWLER_STATS_STUMPED, bowlerStats.getStumped());

				db.insert(TBL_BOWLER_STATS, null, bowlerValues);
			}
		}

	}
}
