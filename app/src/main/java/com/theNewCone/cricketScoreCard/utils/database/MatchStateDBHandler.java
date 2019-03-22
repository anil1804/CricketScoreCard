package com.theNewCone.cricketScoreCard.utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.theNewCone.cricketScoreCard.Constants;
import com.theNewCone.cricketScoreCard.match.Match;
import com.theNewCone.cricketScoreCard.match.MatchState;
import com.theNewCone.cricketScoreCard.match.Team;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MatchStateDBHandler extends DatabaseHandler {

	public MatchStateDBHandler(Context context) {
		super(context);
	}

	private int saveMatchState(int matchID, String matchJson, int isAuto, String saveName, String matchName) {
		ContentValues values = new ContentValues();
		String timestamp = CommonUtils.currTimestamp();

		int saveOrder = 0;
		switch (isAuto) {
			case 0:
				int count = getSavedMatchesWithSameName(matchName);
				saveName = (count > 0) ? saveName + "(" + count + ")" : saveName;
				break;

			case 1:
				saveName = matchName + "@auto";
				saveOrder = getSaveMatchStateSaveOrder(matchID);
				break;
		}

		values.put(TBL_STATE_MATCH_JSON, matchJson);
		values.put(TBL_STATE_IS_AUTO, isAuto);
		values.put(TBL_STATE_NAME, saveName);
		values.put(TBL_STATE_TIMESTAMP, timestamp);
		values.put(TBL_STATE_ORDER, saveOrder);
		values.put(TBL_STATE_MATCH_ID, matchID);

		SQLiteDatabase db = this.getWritableDatabase();
		long rowIID = db.insert(TBL_STATE, null, values);
		db.close();

		if (isAuto == 1)
			clearMatchStateHistory(maxUndoAllowed, matchID, -1);

		return (int) rowIID;
	}

	public int saveMatchState(int matchID, String matchJson, String saveName) {
		return saveMatchState(matchID, matchJson, 0, saveName, null);
	}

	public void autoSaveMatch(int matchID, String matchJson, String matchName) {
		saveMatchState(matchID, matchJson, 1, null, matchName);
	}

	private int getSaveMatchStateSaveOrder(int matchID) {
		int saveOrder = 1;
		final String MAX_SAVE_ORDER = "MAX_SAVE_ORDER";

		String selectQuery = String.format(Locale.getDefault(),
				"SELECT MAX(%s) AS %s FROM %s WHERE %s = %d AND %s = 1",
				TBL_STATE_ORDER, MAX_SAVE_ORDER, TBL_STATE, TBL_STATE_MATCH_ID, matchID, TBL_STATE_IS_AUTO);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			saveOrder = cursor.getInt(cursor.getColumnIndex(MAX_SAVE_ORDER)) + 1;
		}

		cursor.close();
		db.close();

		return saveOrder;
	}

	int getSavedMatchesWithSameName(String matchName) {
		int count = 0;
		final String countString = "ROW_COUNT";

		String selectQuery = String.format(Locale.getDefault(), "SELECT COUNT(%s) AS %s FROM %s WHERE %s = '%s' AND %s = %d"
				, TBL_STATE_ID, countString, TBL_STATE, TBL_STATE_NAME, matchName, TBL_STATE_IS_AUTO, 1);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor != null && cursor.moveToFirst()) {
			count = cursor.getInt(cursor.getColumnIndex(countString));
			cursor.close();
		}
		db.close();

		return count;
	}

	public List<MatchState> getSavedMatches(String autoOrManual, int matchID, String partialName, boolean includeTournaments) {
		List<MatchState> savedMatches = new ArrayList<>();
		final String MATCH_STATE_ID = "MatchStateID";
		final String MATCH_SAVE_NAME = "MatchSaveName";
		final String MATCH_NAME = "MatchName";
		final String TEAM1_SHORT_NAME = "Team1ShortName";
		final String TEAM2_SHORT_NAME = "Team2ShortName";

		String selectQuery = String.format(Locale.getDefault(),
				"SELECT %s AS %s, %s, %s AS %s, " +
						"%s, %s, %s, " +
						"%s AS %s, %s, %s, " +
						"(SELECT %s FROM %s WHERE %s = %s) AS %s," +
						"(SELECT %s FROM %s WHERE %s = %s) AS %s " +
						"FROM %s, %s " +
						"WHERE %s = %s",
				TBL_STATE + "." + TBL_STATE_ID, MATCH_STATE_ID, TBL_STATE_IS_AUTO, TBL_STATE + "." + TBL_STATE_NAME, MATCH_SAVE_NAME,
				TBL_STATE_ORDER, TBL_STATE_MATCH_ID, TBL_STATE_TIMESTAMP,
				TBL_MATCH + "." + TBL_MATCH_NAME, MATCH_NAME, TBL_MATCH_TEAM1, TBL_MATCH_TEAM2,
				TBL_TEAM_SHORT_NAME, TBL_TEAM, TBL_TEAM + "." + TBL_TEAM_ID, TBL_MATCH + "." + TBL_MATCH_TEAM1, TEAM1_SHORT_NAME,
				TBL_TEAM_SHORT_NAME, TBL_TEAM, TBL_TEAM + "." + TBL_TEAM_ID, TBL_MATCH + "." + TBL_MATCH_TEAM2, TEAM2_SHORT_NAME,
				TBL_STATE, TBL_MATCH,
				TBL_MATCH + "." + TBL_MATCH_ID, TBL_STATE + "." + TBL_STATE_MATCH_ID);

		switch (autoOrManual) {
			case SAVE_AUTO:
				selectQuery += " AND " + TBL_STATE_IS_AUTO + "=1";
				break;

			case SAVE_MANUAL:
				selectQuery += " AND " + TBL_STATE_IS_AUTO + "=0";
				break;
		}

		if (matchID > 0) {
			selectQuery += " AND " + TBL_STATE_MATCH_ID + " = " + matchID;
		} else if (partialName != null) {
			partialName = partialName.replaceAll("\\*", "%");
			selectQuery += " AND " + TBL_STATE_NAME + " LIKE '%" + partialName + "%'";
		}

		if (!includeTournaments) {
			selectQuery += " AND " + TBL_STATE_MATCH_ID + " NOT IN " +
					"(SELECT DISTINCT " + TBL_MATCH_INFO_MATCH_ID + " FROM " + TBL_MATCH_INFO + " WHERE " + TBL_STATE_MATCH_ID + " IS NOT NULL)";
		}

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor.getColumnIndex(MATCH_STATE_ID));
				boolean isAuto = cursor.getInt(cursor.getColumnIndex(TBL_STATE_IS_AUTO)) == 1;
				String name = cursor.getString(cursor.getColumnIndex(MATCH_SAVE_NAME));
				int saveOrder = cursor.getInt(cursor.getColumnIndex(TBL_STATE_ORDER));
				int dbMatchID = cursor.getInt(cursor.getColumnIndex(TBL_STATE_MATCH_ID));
				String timestamp = cursor.getString(cursor.getColumnIndex(TBL_STATE_TIMESTAMP));
				String matchName = cursor.getString(cursor.getColumnIndex(MATCH_NAME));
				int team1ID = cursor.getInt(cursor.getColumnIndex(TBL_MATCH_TEAM1));
				int team2ID = cursor.getInt(cursor.getColumnIndex(TBL_MATCH_TEAM2));
				String team1ShortName = cursor.getString(cursor.getColumnIndex(TEAM1_SHORT_NAME));
				String team2ShortName = cursor.getString(cursor.getColumnIndex(TEAM2_SHORT_NAME));

				Team team1 = new Team(team1ID, team1ShortName);
				Team team2 = new Team(team2ID, team2ShortName);

				Date saveDate = null;
				try {
					saveDate = new SimpleDateFormat(Constants.DEF_DATE_FORMAT, Locale.getDefault()).parse(timestamp);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				Match match = new Match(dbMatchID, matchName, team1, team2);
				MatchState matchState = new MatchState(id, name, isAuto, saveOrder, saveDate, match);

				savedMatches.add(matchState);

			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();

		return savedMatches;
	}

	public List<Integer> getSavedMatchStateIDs(String autoOrManual, int matchID, String partialName, boolean includeTournaments) {
		List<Integer> savedMatchStateIDList = new ArrayList<>();

		String selectQuery = String.format(Locale.getDefault(),
				"SELECT %s FROM %s WHERE %s > 0",
				TBL_STATE_ID, TBL_STATE, TBL_STATE_ID);

		switch (autoOrManual) {
			case SAVE_AUTO:
				selectQuery += " AND " + TBL_STATE_IS_AUTO + "=1";
				break;

			case SAVE_MANUAL:
				selectQuery += " AND " + TBL_STATE_IS_AUTO + "=0";
				break;
		}

		if (matchID > 0) {
			selectQuery += " AND " + TBL_STATE_MATCH_ID + " = " + matchID;
		} else if (partialName != null) {
			partialName = partialName.replaceAll("\\*", "%");
			selectQuery += " AND " + TBL_STATE_NAME + " LIKE '%" + partialName + "%'";
		}

		if (!includeTournaments) {
			selectQuery += " AND " + TBL_STATE_MATCH_ID + " NOT IN " +
					"(SELECT DISTINCT" + TBL_MATCH_INFO_MATCH_ID + " FROM " + TBL_MATCH_INFO + ")";
		}

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor != null && cursor.moveToFirst()) {
			do {
				savedMatchStateIDList.add(cursor.getInt(0));
			} while (cursor.moveToNext());

			cursor.close();
		}
		db.close();

		return savedMatchStateIDList;
	}

	public boolean deleteSavedMatchStates(MatchState[] matchesToDelete) {
		StringBuilder whereClauseSB = null;
		int recordsToDelete = 0;
		if (matchesToDelete != null && matchesToDelete.length > 0) {
			whereClauseSB = new StringBuilder(TBL_STATE_ID + " IN (");
			for (MatchState match : matchesToDelete) {
				whereClauseSB.append(match.getId());
				whereClauseSB.append(", ");
			}

			whereClauseSB.delete(whereClauseSB.length() - 2, whereClauseSB.length());
			whereClauseSB.append(")");
			recordsToDelete = matchesToDelete.length;
		}

		int recordsDeleted = 0;
		if (whereClauseSB != null) {
			SQLiteDatabase db = this.getWritableDatabase();
			recordsDeleted = db.delete(TBL_STATE, whereClauseSB.toString(), null);
			db.close();
		}

		return recordsDeleted == recordsToDelete;
	}

	public String retrieveMatchData(int matchStateID) {
		String matchData = null;

		String selectQuery = "SELECT " + TBL_STATE_MATCH_JSON + " FROM " + TBL_STATE + " WHERE " + TBL_STATE_ID + " = " + matchStateID;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			matchData = cursor.getString(0);
		}
		cursor.close();
		db.close();

		return matchData;
	}

	public int getLastAutoSave(int matchID) {
		int matchStateID = -1;

		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = String.format(Locale.getDefault(),
				"SELECT %s FROM %s WHERE %s = %d AND %s = 1 ORDER BY %s DESC LIMIT 1"
				, TBL_STATE_ID, TBL_STATE, TBL_STATE_MATCH_ID, matchID, TBL_STATE_IS_AUTO, TBL_STATE_ORDER);
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor != null && cursor.moveToFirst()) {
			matchStateID = cursor.getInt(cursor.getColumnIndex(TBL_STATE_ID));
			cursor.close();
		}
		db.close();

		return matchStateID;
	}

	public int getLastAutoSave() {
		int matchStateID = -1;

		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = String.format(Locale.getDefault(),
				"SELECT %s FROM %s WHERE %s = 1 ORDER BY %s DESC LIMIT 1"
				, TBL_STATE_ID, TBL_STATE, TBL_STATE_IS_AUTO, TBL_STATE_ORDER);
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor != null && cursor.moveToFirst()) {
			matchStateID = cursor.getInt(cursor.getColumnIndex(TBL_STATE_ID));
			cursor.close();
		}
		db.close();

		return matchStateID;
	}

	public void deleteMatchState(int matchStateID) {
		String query = String.format(Locale.getDefault(),
				"DELETE FROM %s WHERE %s = %d", TBL_STATE, TBL_STATE_ID, matchStateID);

		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(query);
		db.close();
	}

	public void clearMatchStateHistory(int ignoreCount, int matchID, int matchStateID) {
		if (matchID < 0) {
			getMatchID(matchStateID);
		}

		SQLiteDatabase db = this.getWritableDatabase();
		String query = String.format(Locale.getDefault(),
				"DELETE FROM %s WHERE %s NOT IN " +
						"(SELECT %s FROM %s WHERE %s = %d ORDER BY %s DESC LIMIT %d) " +
						"AND %s = 1 AND %s = %d"
				, TBL_STATE, TBL_STATE_ORDER,
				TBL_STATE_ORDER, TBL_STATE, TBL_STATE_MATCH_ID, matchID, TBL_STATE_ORDER, ignoreCount,
				TBL_STATE_IS_AUTO, TBL_STATE_MATCH_ID, matchID);

		db.execSQL(query);
		db.close();
	}

	public void clearAllAutoSaveHistory() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TBL_STATE, TBL_STATE_IS_AUTO + " = ?", new String[]{String.valueOf(1)});
		db.close();
	}

	public int getMatchID(int matchStateID) {
		int matchID = -1;

		SQLiteDatabase db = this.getReadableDatabase();
		String matchIDQuery = String.format(Locale.getDefault(),
				"SELECT %s FROM %s WHERE %s = %d", TBL_STATE_MATCH_ID, TBL_STATE, TBL_STATE_ID, matchStateID);
		Cursor cursor = db.rawQuery(matchIDQuery, null);

		if (cursor != null && cursor.moveToFirst()) {
			matchID = cursor.getInt(cursor.getColumnIndex(TBL_STATE_MATCH_ID));
			cursor.close();
		}
		db.close();

		return matchID;
	}

	public void clearAllMatchHistory(int matchID) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TBL_STATE, TBL_STATE_MATCH_ID + " = ?", new String[]{String.valueOf(matchID)});
		db.close();
	}
}
