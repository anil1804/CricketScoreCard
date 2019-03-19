package com.theNewCone.cricketScoreCard.utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.theNewCone.cricketScoreCard.match.CricketCard;
import com.theNewCone.cricketScoreCard.match.CricketCardUtils;
import com.theNewCone.cricketScoreCard.player.BatsmanStats;
import com.theNewCone.cricketScoreCard.player.BowlerStats;
import com.theNewCone.cricketScoreCard.player.FielderStats;
import com.theNewCone.cricketScoreCard.player.Player;
import com.theNewCone.cricketScoreCard.statistics.BatsmanData;
import com.theNewCone.cricketScoreCard.statistics.BowlerData;
import com.theNewCone.cricketScoreCard.statistics.PlayerData;
import com.theNewCone.cricketScoreCard.statistics.PlayerMatchData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class StatisticsDBHandler extends DatabaseHandler {
	public StatisticsDBHandler(Context context) {
		super(context);
	}

	public void addPlayerStats(CricketCardUtils ccUtils, int tournamentID) {
		if (ccUtils != null) {
			CricketCard winningTeam, losingTeam;
			if (ccUtils.getWinningTeam().equals(ccUtils.getCard().getBattingTeam())) {
				winningTeam = ccUtils.getCard();
				losingTeam = ccUtils.getPrevInningsCard();
			} else {
				losingTeam = ccUtils.getCard();
				winningTeam = ccUtils.getPrevInningsCard();
			}
			addMatchStats(winningTeam, losingTeam, ccUtils.getMatchID(), tournamentID);
		}
	}

	private void addMatchStats(@NonNull CricketCard winningTeamCard, @NonNull CricketCard losingTeamCard, int matchID, int tournamentID) {
		addPlayerStats(winningTeamCard, matchID, tournamentID);
		addPlayerStats(losingTeamCard, matchID, tournamentID);
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
			SQLiteDatabase db = super.getWritableDatabase();
			for (FielderStats fielderStats : fielderStatsCollection) {
				ContentValues playerValues = new ContentValues();

				playerValues.put(TBL_PLAYER_STATS_PLAYER_ID, fielderStats.getPlayer().getID());
				playerValues.put(TBL_PLAYER_STATS_MATCH_ID, matchID);
				playerValues.put(TBL_PLAYER_STATS_TOURNAMENT_ID, tournamentID);
				playerValues.put(TBL_PLAYER_STATS_CATCHES, fielderStats.getCatches());
				playerValues.put(TBL_PLAYER_STATS_RUN_OUTS, fielderStats.getRunOuts());
				playerValues.put(TBL_PLAYER_STATS_STUMP_OUTS, fielderStats.getStumpOuts());

				db.insert(TBL_PLAYER_STATS, null, playerValues);
			}

			db.close();
		}
	}

	private void addBatsmanStats(@NonNull Collection<BatsmanStats> batsmanStatsCollection, int matchID, int tournamentID) {
		if (batsmanStatsCollection.size() > 0) {
			SQLiteDatabase db = super.getWritableDatabase();
			for (BatsmanStats batsmanStats : batsmanStatsCollection) {
				ContentValues batsmanValues = new ContentValues();

				batsmanValues.put(TBL_BATSMAN_STATS_PLAYER_ID, batsmanStats.getPlayer().getID());
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

			db.close();
		}
	}

	private void addBowlerStats(@NonNull Collection<BowlerStats> bowlerStatsCollection, int matchID, int tournamentID) {
		if (bowlerStatsCollection.size() > 0) {
			SQLiteDatabase db = super.getWritableDatabase();
			for (BowlerStats bowlerStats : bowlerStatsCollection) {
				ContentValues bowlerValues = new ContentValues();

				bowlerValues.put(TBL_BOWLER_STATS_PLAYER_ID, bowlerStats.getPlayer().getID());
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

			db.close();
		}
	}

	public List<BatsmanData> getBatsmanStatistics(int tournamentID) {
		List<BatsmanData> batsmanDataList = new ArrayList<>();

		String sqlQuery = String.format(Locale.getDefault(),
				"SELECT * FROM %s WHERE %s = %d " +
						"ORDER BY %s",
				TBL_BATSMAN_STATS, TBL_BATSMAN_STATS_TOURNAMENT_ID, tournamentID,
				TBL_BATSMAN_STATS_PLAYER_ID);

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(sqlQuery, null, null);

		if (cursor != null && cursor.moveToFirst()) {
			int prevPlayerID = 0;
			BatsmanData batsmanData = null;
			List<PlayerMatchData> playerMatchDataList = null;

			while (cursor.moveToNext()) {
				int playerID = cursor.getInt(cursor.getColumnIndex(TBL_BATSMAN_STATS_PLAYER_ID));
				if (prevPlayerID != playerID) {
					if (batsmanData != null) {
						batsmanData.setPlayerMatchDataList(playerMatchDataList);
						batsmanDataList.add(batsmanData);
					}
					prevPlayerID = playerID;
					Player player = new PlayerDBHandler(context).getPlayer(playerID);
					batsmanData = new BatsmanData(player);
					playerMatchDataList = new ArrayList<>();
				}

				if (playerMatchDataList != null)
					playerMatchDataList.add(getBatsmanDataFromCursor(cursor));
			}

			if (batsmanData != null) {
				batsmanData.setPlayerMatchDataList(playerMatchDataList);
				batsmanDataList.add(batsmanData);
			}

			cursor.close();
		}
		db.close();

		return batsmanDataList;
	}

	private PlayerMatchData getBatsmanDataFromCursor(Cursor cursor) {
		PlayerMatchData playerMatchData = null;
		if (cursor != null) {
			playerMatchData = new PlayerMatchData();

			playerMatchData.setMatchID(cursor.getInt(cursor.getColumnIndex(TBL_BATSMAN_STATS_MATCH_ID)));

			playerMatchData.setRunsScored(cursor.getInt(cursor.getColumnIndex(TBL_BATSMAN_STATS_RUNS)));
			playerMatchData.setBallsPlayed(cursor.getInt(cursor.getColumnIndex(TBL_BATSMAN_STATS_BALLS)));

			playerMatchData.setFoursHit(cursor.getInt(cursor.getColumnIndex(TBL_BATSMAN_STATS_FOURS)));
			playerMatchData.setSixesHit(cursor.getInt(cursor.getColumnIndex(TBL_BATSMAN_STATS_SIXES)));

			playerMatchData.setOut(cursor.getInt(cursor.getColumnIndex(TBL_BATSMAN_STATS_IS_OUT)) == 1);
		}

		return playerMatchData;
	}

	public List<BowlerData> getBowlerStatistics(int tournamentID) {
		List<BowlerData> bowlerDataList = new ArrayList<>();

		String sqlQuery = String.format(Locale.getDefault(),
				"SELECT * FROM %s WHERE %s = %d " +
						"ORDER BY %s",
				TBL_BOWLER_STATS, TBL_BOWLER_STATS_TOURNAMENT_ID, tournamentID,
				TBL_BOWLER_STATS_PLAYER_ID);

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(sqlQuery, null, null);

		if (cursor != null && cursor.moveToFirst()) {
			int prevPlayerID = 0;
			BowlerData bowlerData = null;
			List<PlayerMatchData> playerMatchDataList = null;

			while (cursor.moveToNext()) {
				int playerID = cursor.getInt(cursor.getColumnIndex(TBL_BOWLER_STATS_PLAYER_ID));
				if (prevPlayerID != playerID) {
					if (bowlerData != null) {
						bowlerData.setPlayerMatchDataList(playerMatchDataList);
						bowlerDataList.add(bowlerData);
					}
					prevPlayerID = playerID;
					Player player = new PlayerDBHandler(context).getPlayer(playerID);
					bowlerData = new BowlerData(player);
					playerMatchDataList = new ArrayList<>();
				}

				if (playerMatchDataList != null)
					playerMatchDataList.add(getBowlerDataFromCursor(cursor));
			}

			if (bowlerData != null) {
				bowlerData.setPlayerMatchDataList(playerMatchDataList);
				bowlerDataList.add(bowlerData);
			}

			cursor.close();
		}
		db.close();

		return bowlerDataList;
	}

	private PlayerMatchData getBowlerDataFromCursor(Cursor cursor) {
		PlayerMatchData playerMatchData = null;
		if (cursor != null) {
			playerMatchData = new PlayerMatchData();

			playerMatchData.setMatchID(cursor.getInt(cursor.getColumnIndex(TBL_BOWLER_STATS_MATCH_ID)));

			playerMatchData.setOversBowled(Double.parseDouble(cursor.getString(cursor.getColumnIndex(TBL_BOWLER_STATS_OVERS_BOWLED))));
			playerMatchData.setRunsGiven(cursor.getInt(cursor.getColumnIndex(TBL_BOWLER_STATS_RUNS_GIVEN)));
			playerMatchData.setMaidens(cursor.getInt(cursor.getColumnIndex(TBL_BOWLER_STATS_MAIDENS)));
			playerMatchData.setWicketsTaken(cursor.getInt(cursor.getColumnIndex(TBL_BOWLER_STATS_WICKETS_TAKEN)));
		}

		return playerMatchData;
	}

	public List<PlayerData> getFielderStatistics(int tournamentID) {
		List<PlayerData> playerDataList = new ArrayList<>();

		String sqlQuery = String.format(Locale.getDefault(),
				"SELECT * FROM %s WHERE %s = %d " +
						"ORDER BY %s",
				TBL_PLAYER_STATS, TBL_PLAYER_STATS_TOURNAMENT_ID, tournamentID,
				TBL_PLAYER_STATS_PLAYER_ID);

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(sqlQuery, null, null);

		if (cursor != null && cursor.moveToFirst()) {
			int prevPlayerID = 0;
			PlayerData playerData = null;
			List<PlayerMatchData> playerMatchDataList = null;

			while (cursor.moveToNext()) {
				int playerID = cursor.getInt(cursor.getColumnIndex(TBL_PLAYER_STATS_PLAYER_ID));
				if (prevPlayerID != playerID) {
					if (playerData != null) {
						playerData.setPlayerMatchDataList(playerMatchDataList);
						playerDataList.add(playerData);
					}
					prevPlayerID = playerID;
					Player player = new PlayerDBHandler(context).getPlayer(playerID);
					playerData = new PlayerData(player);
					playerMatchDataList = new ArrayList<>();
				}

				if (playerMatchDataList != null)
					playerMatchDataList.add(getFielderDataFromCursor(cursor));
			}

			if (playerData != null) {
				playerData.setPlayerMatchDataList(playerMatchDataList);
				playerDataList.add(playerData);
			}

			cursor.close();
		}
		db.close();

		return playerDataList;
	}

	private PlayerMatchData getFielderDataFromCursor(Cursor cursor) {
		PlayerMatchData playerMatchData = null;
		if (cursor != null) {
			playerMatchData = new PlayerMatchData();

			playerMatchData.setMatchID(cursor.getInt(cursor.getColumnIndex(TBL_BOWLER_STATS_MATCH_ID)));

			playerMatchData.setCatches(cursor.getInt(cursor.getColumnIndex(TBL_PLAYER_STATS_CATCHES)));
			playerMatchData.setStumps(cursor.getInt(cursor.getColumnIndex(TBL_PLAYER_STATS_STUMP_OUTS)));
			playerMatchData.setRunOuts(cursor.getInt(cursor.getColumnIndex(TBL_PLAYER_STATS_RUN_OUTS)));
		}

		return playerMatchData;
	}
}
