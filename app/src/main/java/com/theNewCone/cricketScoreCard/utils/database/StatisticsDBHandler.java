package com.theNewCone.cricketScoreCard.utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.theNewCone.cricketScoreCard.match.CricketCard;
import com.theNewCone.cricketScoreCard.match.CricketCardUtils;
import com.theNewCone.cricketScoreCard.player.BatsmanStats;
import com.theNewCone.cricketScoreCard.player.BowlerStats;
import com.theNewCone.cricketScoreCard.player.FielderStats;

import java.util.Collection;

public class StatisticsDBHandler extends DatabaseHandler {
	public StatisticsDBHandler(Context context) {
		super(context);
	}

	public void addPlayerStats(CricketCardUtils ccUtils, int tournamentID) {
		if (ccUtils != null) {
			addPlayerStats(ccUtils.getPrevInningsCard(), ccUtils.getMatchInfo().getMatchID(), tournamentID);
			addPlayerStats(ccUtils.getCard(), ccUtils.getMatchInfo().getMatchID(), tournamentID);
		}
	}

	private void addPlayerStats(CricketCard card, int matchID, int tournamentID) {
		if (card != null) {
			addPlayerStats(card.getFielderMap().values(), matchID, tournamentID);
			addBatsmanStats(card.getBatsmen().values(), matchID, tournamentID);
			addBowlerStats(card.getBowlerMap().values(), matchID, tournamentID);
		}
	}

	private void addPlayerStats(@NonNull Collection<FielderStats> fielderStatsCollection, int matchID, int tournamentID) {
		if (fielderStatsCollection.size() > 0) {
			SQLiteDatabase db = this.getWritableDatabase();

			for (FielderStats fielderStats : fielderStatsCollection) {
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
		if (batsmanStatsCollection.size() > 0) {
			SQLiteDatabase db = this.getWritableDatabase();

			for (BatsmanStats batsmanStats : batsmanStatsCollection) {
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
				if (batsmanStats.getDismissalType() != null)
					batsmanValues.put(TBL_BATSMAN_STATS_DISMISSAL_TYPE, batsmanStats.getDismissalType().toString());
				if (batsmanStats.getWicketTakenBy() != null)
					batsmanValues.put(TBL_BATSMAN_STATS_DISMISSED_BY, batsmanStats.getWicketTakenBy().getPlayer().getID());

				db.insert(TBL_BATSMAN_STATS, null, batsmanValues);
			}
		}
	}

	private void addBowlerStats(@NonNull Collection<BowlerStats> bowlerStatsCollection, int matchID, int tournamentID) {
		if (bowlerStatsCollection.size() > 0) {
			SQLiteDatabase db = this.getWritableDatabase();

			for (BowlerStats bowlerStats : bowlerStatsCollection) {
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
