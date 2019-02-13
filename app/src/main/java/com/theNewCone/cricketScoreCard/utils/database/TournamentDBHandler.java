package com.theNewCone.cricketScoreCard.utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.theNewCone.cricketScoreCard.Constants;
import com.theNewCone.cricketScoreCard.enumeration.TournamentFormat;
import com.theNewCone.cricketScoreCard.tournament.Group;
import com.theNewCone.cricketScoreCard.tournament.Tournament;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TournamentDBHandler extends DatabaseHandler {
	public TournamentDBHandler(Context context) {
		super(context);
	}

	public int createNewTournament(Tournament tournament) {
		int rowID = 0;
		if (tournament != null) {
			ContentValues values = new ContentValues();

			values.put(TBL_TOURNAMENT_NAME, tournament.getName());
			values.put(TBL_TOURNAMENT_TEAM_SIZE, tournament.getTeams().length);
			values.put(TBL_TOURNAMENT_FORMAT, tournament.getFormat().toString());
			values.put(TBL_TOURNAMENT_JSON, CommonUtils.convertTournamentToJSON(tournament));
			values.put(TBL_TOURNAMENT_CREATED_DATE, CommonUtils.currTimestamp());
			if (tournament.isScheduled())
				values.put(TBL_TOURNAMENT_IS_SCHEDULED, 1);

			SQLiteDatabase db = this.getWritableDatabase();

			boolean alreadyExists = false;
			if (!Constants.ALLOW_DUPLICATE_TOURNAMENT_NAME) {
				String selectQuery = String.format(Locale.getDefault(),
						"SELECT %s FROM %s WHERE %s = '%s'",
						TBL_TOURNAMENT_ID, TBL_TOURNAMENT, TBL_TOURNAMENT_NAME, tournament.getName());
				Cursor cursor = db.rawQuery(selectQuery, null);

				if (cursor != null && cursor.moveToFirst()) {
					alreadyExists = true;
					cursor.close();
				}
			}

			rowID = CODE_NEW_TOURNAMENT_DUP_RECORD;

			if (!alreadyExists) {
				rowID = (int) db.insert(TBL_TOURNAMENT, null, values);
			}

			db.close();
		}

		return rowID;
	}

	public void updateTournament(Tournament tournament) {
		ContentValues values = new ContentValues();

		values.put(TBL_TOURNAMENT_NAME, tournament.getName());
		values.put(TBL_TOURNAMENT_JSON, CommonUtils.convertTournamentToJSON(tournament));
		if (tournament.isScheduled())
			values.put(TBL_TOURNAMENT_IS_SCHEDULED, 1);

		SQLiteDatabase db = this.getWritableDatabase();
		db.update(TBL_TOURNAMENT, values, TBL_TOURNAMENT_ID + " = ?", new String[]{String.valueOf(tournament.getId())});

		db.close();
	}

	public void completeTournament(int tournamentID) {
		ContentValues values = new ContentValues();

		values.put(TBL_TOURNAMENT_IS_COMPLETE, 1);

		SQLiteDatabase db = this.getWritableDatabase();
		db.update(TBL_TOURNAMENT, values, TBL_TOURNAMENT_ID + " = ?", new String[]{String.valueOf(tournamentID)});

		db.close();
	}

	public List<Tournament> getTournaments(boolean getCompleted) {
		List<Tournament> tournamentList = new ArrayList<>();

		String sqlQuery = String.format(Locale.getDefault(),
				"SELECT %s, %s, %s, %s, %s " +
						"FROM %s WHERE %s = %d"
				, TBL_TOURNAMENT_ID, TBL_TOURNAMENT_NAME, TBL_TOURNAMENT_TEAM_SIZE, TBL_TOURNAMENT_FORMAT, TBL_TOURNAMENT_CREATED_DATE
				, TBL_TOURNAMENT, TBL_TOURNAMENT_IS_COMPLETE, (getCompleted ? 1 : 0));

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sqlQuery, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor.getColumnIndex(TBL_TOURNAMENT_ID));
				String name = cursor.getString(cursor.getColumnIndex(TBL_TOURNAMENT_NAME));
				String createdDate = cursor.getString(cursor.getColumnIndex(TBL_TOURNAMENT_CREATED_DATE));
				int teamSize = cursor.getInt(cursor.getColumnIndex(TBL_TOURNAMENT_TEAM_SIZE));
				String format = cursor.getString(cursor.getColumnIndex(TBL_TOURNAMENT_FORMAT));

				tournamentList.add(new Tournament(id, name, teamSize, TournamentFormat.valueOf(format), createdDate));
			} while (cursor.moveToNext());
			cursor.close();
		}
		db.close();

		return tournamentList;
	}

	public Tournament getTournamentContent(int tournamentID) {
		Tournament tournament = null;

		String selectQuery = String.format(Locale.getDefault(), "SELECT %s FROM %s WHERE %s = %d",
				TBL_TOURNAMENT_JSON, TBL_TOURNAMENT, TBL_TOURNAMENT_ID, tournamentID);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			String tournamentData = cursor.getString(cursor.getColumnIndex(TBL_TOURNAMENT_JSON));
			tournament = CommonUtils.convertJSONToTournament(tournamentData);
			tournament.setId(tournamentID);

			List<Group> groupList = new GroupsDBHandler(super.context).getTournamentGroups(tournamentID);
			if (groupList.size() > 0)
				tournament.setGroupList(groupList);

		}
		cursor.close();
		db.close();

		return tournament;
	}

	public void deleteTournament(String tournamentName) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TBL_TOURNAMENT, TBL_TOURNAMENT_NAME + " = ?", new String[]{tournamentName});
	}

	public int getTournamentIDUsingMatchID(int matchID) {
		SQLiteDatabase db = getWritableDatabase();

		String sqlQuery = String.format(Locale.getDefault(),
				"SELECT %s FROM %s WHERE %s = " +
						"(SELECT %s FROM %s WHERE %s = %d)",
				TBL_GROUP_TOURNAMENT_ID, TBL_GROUP, TBL_GROUP_ID,
				TBL_MATCH_INFO_GROUP_ID, TBL_MATCH_INFO, TBL_MATCH_INFO_MATCH_ID, matchID);
		Cursor cursor = db.rawQuery(sqlQuery, null);

		int tournamentID = 0;
		if (cursor != null && cursor.moveToFirst()) {
			tournamentID = cursor.getInt(0);
			cursor.close();
		}

		return tournamentID;
	}
}
