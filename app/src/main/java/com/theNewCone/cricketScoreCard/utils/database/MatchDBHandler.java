package com.theNewCone.cricketScoreCard.utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.theNewCone.cricketScoreCard.Constants;
import com.theNewCone.cricketScoreCard.match.CricketCardUtils;
import com.theNewCone.cricketScoreCard.match.Match;
import com.theNewCone.cricketScoreCard.match.Team;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MatchDBHandler extends DatabaseHandler {

	public MatchDBHandler(Context context) {
		super(context);
	}

	public boolean deleteMatches(Match[] matches) {
		int rowsUpdated = 0;
		if (matches != null && matches.length > 0) {
			StringBuilder whereClauseSB = new StringBuilder();
			whereClauseSB.append(TBL_MATCH_ID);
			whereClauseSB.append(" IN (");

			for (Match match : matches) {
				whereClauseSB.append(match.getId());
				whereClauseSB.append(", ");
			}

			whereClauseSB.delete(whereClauseSB.length() - 2, whereClauseSB.length());
			whereClauseSB.append(")");

			ContentValues values = new ContentValues();
			values.put(TBL_MATCH_IS_ARCHIVED, 1);

			SQLiteDatabase db = this.getWritableDatabase();
			rowsUpdated = db.update(TBL_MATCH, values, whereClauseSB.toString(), null);
			db.close();
		}

		return (matches != null && rowsUpdated == matches.length);
	}

	public int addNewMatch(Match match) {
		ContentValues values = new ContentValues();

		values.put(TBL_MATCH_NAME, match.getName());
		values.put(TBL_MATCH_TEAM1, match.getTeam1ID());
		values.put(TBL_MATCH_TEAM2, match.getTeam2ID());
		values.put(TBL_MATCH_DATE, CommonUtils.currTimestamp());

		long rowID = CODE_NEW_MATCH_DUP_RECORD;

		boolean createRecord = Constants.ALLOW_DUPLICATE_MATCH_NAME;
		if (!createRecord)
			createRecord = new MatchStateDBHandler(super.context).getSavedMatchesWithSameName(match.getName()) == 0;

		if (createRecord) {
			SQLiteDatabase db = this.getWritableDatabase();
			rowID = db.insert(TBL_MATCH, null, values);
			db.close();
		}

		return (int) rowID;
	}

	public boolean completeMatch(int matchID, String matchJSON) {
		ContentValues values = new ContentValues();

		values.put(TBL_MATCH_IS_COMPLETE, 1);
		values.put(TBL_MATCH_JSON, matchJSON);

		SQLiteDatabase db = this.getWritableDatabase();
		int rowsUpdated = db.update(TBL_MATCH, values, TBL_MATCH_ID + " = ?", new String[]{String.valueOf(matchID)});
		db.close();

		return rowsUpdated == 1;
	}

	public List<Match> getCompletedMatches() {
		List<Match> matchList = new ArrayList<>();

		String sqlQuery = String.format(Locale.getDefault(),
				"SELECT * FROM %s WHERE %s = 1 AND (%s = 0 OR %s IS NULL)",
				TBL_MATCH, TBL_MATCH_IS_COMPLETE, TBL_MATCH_IS_ARCHIVED, TBL_MATCH_IS_ARCHIVED);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sqlQuery, null);

		if (cursor != null && cursor.moveToFirst()) {
			do {
				Match match = getMatchFromCursor(cursor, db);
				matchList.add(match);
			} while (cursor.moveToNext());

			cursor.close();
		}
		db.close();

		return matchList;
	}

	public Match getMatch(int matchID) {
		Match match = null;

		if (matchID > 0) {
			String sqlQuery = String.format(Locale.getDefault(),
					"SELECT * FROM %s WHERE (%s = 0 OR %s IS NULL) AND %s = %d",
					TBL_MATCH, TBL_MATCH_IS_ARCHIVED, TBL_MATCH_IS_ARCHIVED, TBL_MATCH_INFO_ID, matchID);

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(sqlQuery, null);

			if (cursor != null && cursor.moveToFirst()) {
				match = getMatchFromCursor(cursor, db);
				cursor.close();
			}
			db.close();
		}

		return match;
	}

	@NonNull
	private Match getMatchFromCursor(Cursor cursor, SQLiteDatabase db) {
		int id = cursor.getInt(cursor.getColumnIndex(TBL_MATCH_ID));
		String matchName = cursor.getString(cursor.getColumnIndex(TBL_MATCH_NAME));
		Date date = CommonUtils.stringToDate(cursor.getString(cursor.getColumnIndex(TBL_MATCH_DATE)));
		int team1ID = cursor.getInt(cursor.getColumnIndex(TBL_MATCH_TEAM1));
		int team2ID = cursor.getInt(cursor.getColumnIndex(TBL_MATCH_TEAM2));

		List<Integer> teamIDList = new ArrayList<>();
		teamIDList.add(team1ID);
		teamIDList.add(team2ID);
		List<Team> teams = new TeamDBHandler(super.context).getTeams(teamIDList, db, true);

		Team team1 = null, team2 = null;
		for (Team team : teams) {
			if (team.getId() == team1ID)
				team1 = team;
			else if (team.getId() == team2ID)
				team2 = team;
		}

		return new Match(id, matchName, date, team1, team2);
	}

	public CricketCardUtils getCompletedMatchData(int matchID) {
		CricketCardUtils ccUtils = null;

		String selectQuery = String.format(Locale.getDefault(),
				"SELECT %s FROM %s WHERE %s = %d", TBL_MATCH_JSON, TBL_MATCH, TBL_MATCH_ID, matchID);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			String matchData = cursor.getString(0);
			ccUtils = CommonUtils.convertToCCUtils(matchData);
		}
		cursor.close();
		db.close();

		return ccUtils;
	}

	public boolean checkIfMatchComplete(int matchID) {
		boolean isComplete = false;

		if (matchID > 0) {
			SQLiteDatabase db = this.getReadableDatabase();
			String sqlQuery = String.format(Locale.getDefault(),
					"SELECT %s FROM %s WHERE %s = %d",
					TBL_MATCH_IS_COMPLETE, TBL_MATCH, TBL_MATCH_ID, matchID);
			Cursor cursor = db.rawQuery(sqlQuery, null, null);

			if (cursor != null && cursor.moveToFirst()) {
				isComplete = cursor.getInt(cursor.getColumnIndex(TBL_MATCH_IS_COMPLETE)) == 1;
				cursor.close();
			}

			db.close();
		}

		return isComplete;
	}
}
