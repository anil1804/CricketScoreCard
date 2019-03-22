package com.theNewCone.cricketScoreCard.utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.theNewCone.cricketScoreCard.enumeration.Stage;
import com.theNewCone.cricketScoreCard.match.Team;
import com.theNewCone.cricketScoreCard.tournament.MatchInfo;
import com.theNewCone.cricketScoreCard.tournament.Tournament;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MatchInfoDBHandler extends DatabaseHandler {
	public MatchInfoDBHandler(Context context) {
		super(context);
	}

	public int addNewMatchInfo(int groupID, MatchInfo matchInfo) {
		ContentValues values = new ContentValues();
		int rowID = 0;
		if (matchInfo != null) {
			values.put(TBL_MATCH_INFO_NUMBER, matchInfo.getMatchNumber());
			values.put(TBL_MATCH_INFO_GROUP_ID, groupID);
			values.put(TBL_MATCH_INFO_GROUP_NUMBER, matchInfo.getGroupNumber());
			values.put(TBL_MATCH_INFO_GROUP_NAME, matchInfo.getGroupName());
			values.put(TBL_MATCH_INFO_STAGE, matchInfo.getTournamentStage().toString());
			values.put(TBL_MATCH_INFO_TEAM1_ID, matchInfo.getTeam1().getId());
			values.put(TBL_MATCH_INFO_TEAM2_ID, matchInfo.getTeam2().getId());

			SQLiteDatabase db = this.getWritableDatabase();
			if (matchInfo.getId() > 0) {
				rowID = matchInfo.getId();
				db.update(TBL_MATCH_INFO, values, TBL_MATCH_INFO_ID + " = ?", new String[]{String.valueOf(rowID)});
			} else {
				rowID = (int) db.insert(TBL_MATCH_INFO, null, values);
			}
			db.close();
		}

		return rowID;
	}

	public void startTournamentMatch(MatchInfo matchInfo) {
		ContentValues values = new ContentValues();

		if (matchInfo != null && matchInfo.getId() > 0) {
			values.put(TBL_MATCH_INFO_HAS_STARTED, 1);
			values.put(TBL_MATCH_INFO_MATCH_ID, matchInfo.getMatchID());
			values.put(TBL_MATCH_INFO_DATE, CommonUtils.currTimestamp("yyyy.MMMdd"));

			SQLiteDatabase db = this.getWritableDatabase();
			db.update(TBL_MATCH_INFO, values, TBL_MATCH_INFO_ID + " = ?", new String[]{String.valueOf(matchInfo.getId())});
			db.close();
		}
	}

	public void completeTournamentMatch(int matchInfoID, int matchID, int winnerTeamID) {
		ContentValues values = new ContentValues();

		if (matchInfoID > 0) {
			values.put(TBL_MATCH_INFO_IS_COMPLETE, 1);
			values.put(TBL_MATCH_INFO_HAS_STARTED, 1);
			values.put(TBL_MATCH_INFO_WINNER_ID, winnerTeamID);

			if (matchID > 0)
				values.put(TBL_MATCH_INFO_MATCH_ID, matchID);

			SQLiteDatabase db = this.getWritableDatabase();
			db.update(TBL_MATCH_INFO, values, TBL_MATCH_INFO_ID + " = ?", new String[]{String.valueOf(matchInfoID)});
			db.close();
		}
	}

	List<MatchInfo> getGroupMatchInfo(int groupID, SQLiteDatabase db) {
		List<MatchInfo> matchInfoList = new ArrayList<>();

		if (groupID > 0) {
			boolean hasDBConn = true;

			String sqlQuery = String.format(Locale.getDefault(),
					"SELECT * FROM %s WHERE %s = %d", TBL_MATCH_INFO, TBL_MATCH_INFO_GROUP_ID, groupID);

			if (db == null) {
				db = this.getReadableDatabase();
				hasDBConn = false;
			} else if (!db.isOpen()) {
				db = this.getReadableDatabase();
			}

			Cursor cursor = db.rawQuery(sqlQuery, null);

			if (cursor != null && cursor.moveToFirst()) {
				do {
					MatchInfo matchInfo = getMatchInfoFromCursor(db, cursor);

					matchInfoList.add(matchInfo);
				} while (cursor.moveToNext());

				cursor.close();
			}

			if (!hasDBConn)
				db.close();
		}

		return matchInfoList;
	}

	private MatchInfo getMatchInfoFromCursor(SQLiteDatabase db, Cursor cursor) {
		int id = cursor.getInt(cursor.getColumnIndex(TBL_MATCH_INFO_ID));
		int groupID = cursor.getInt(cursor.getColumnIndex(TBL_MATCH_INFO_GROUP_ID));
		int number = cursor.getInt(cursor.getColumnIndex(TBL_MATCH_INFO_NUMBER));
		int matchID = cursor.getInt(cursor.getColumnIndex(TBL_MATCH_INFO_MATCH_ID));
		int groupNumber = cursor.getInt(cursor.getColumnIndex(TBL_MATCH_INFO_GROUP_NUMBER));
		String groupName = cursor.getString(cursor.getColumnIndex(TBL_MATCH_INFO_GROUP_NAME));
		Stage stage = Stage.valueOf(cursor.getString(cursor.getColumnIndex(TBL_MATCH_INFO_STAGE)));
		int team1ID = cursor.getInt(cursor.getColumnIndex(TBL_MATCH_INFO_TEAM1_ID));
		int team2ID = cursor.getInt(cursor.getColumnIndex(TBL_MATCH_INFO_TEAM2_ID));
		int winningTeamID = cursor.getInt(cursor.getColumnIndex(TBL_MATCH_INFO_WINNER_ID));
		String matchDate = cursor.getString(cursor.getColumnIndex(TBL_MATCH_INFO_DATE));
		boolean hasStarted = cursor.getInt(cursor.getColumnIndex(TBL_MATCH_INFO_HAS_STARTED)) == 1;
		boolean isComplete = cursor.getInt(cursor.getColumnIndex(TBL_MATCH_INFO_IS_COMPLETE)) == 1;

		List<Integer> teamIDs = new ArrayList<>();
		teamIDs.add(team1ID);
		teamIDs.add(team2ID);

		List<Team> teamList = new TeamDBHandler(super.context).getTeams(teamIDs, db, true);

		Team team1 = null, team2 = null, winningTeam = null;
		for (Team team : teamList) {
			if (team.getId() == team1ID) {
				team1 = team;
			} else if (team.getId() == team2ID) {
				team2 = team;
			}

			if (winningTeamID > 0 && winningTeamID == team.getId())
				winningTeam = team;
		}

		MatchInfo matchInfo = new MatchInfo(number, groupNumber, groupName, stage, team1, team2);
		matchInfo.setId(id);
		matchInfo.setMatchID(matchID);
		matchInfo.setGroupID(groupID);
		matchInfo.setHasStarted(hasStarted);
		matchInfo.setMatchDate(matchDate);
		matchInfo.setComplete(isComplete);

		if (winningTeam != null)
			matchInfo.setWinningTeam(winningTeam);
		return matchInfo;
	}

	public Tournament getTournamentByMatchInfoID(int matchInfoID) {
		Tournament tournament = null;

		if (matchInfoID > 0) {
			int tournamentID = 0;
			SQLiteDatabase db = this.getReadableDatabase();

			String sqlQuery = String.format(Locale.getDefault(),
					"SELECT %s FROM %s WHERE %s = " +
							"(SELECT %s FROM %s WHERE %s = %d)",
					TBL_GROUP_TOURNAMENT_ID, TBL_GROUP, TBL_GROUP_ID,
					TBL_MATCH_INFO_GROUP_ID, TBL_MATCH_INFO, TBL_MATCH_INFO_ID, matchInfoID);
			Cursor cursor = db.rawQuery(sqlQuery, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				tournamentID = cursor.getInt(0);
				cursor.close();
			}

			db.close();

			if (tournamentID > 0)
				tournament = new TournamentDBHandler(super.context).getTournamentContent(tournamentID);
		}

		return tournament;
	}

	public MatchInfo getMatchInfoBasedOnMatchStateID(int matchStateID) {
		int matchInfoID = 0;
		if (matchStateID > 0) {
			String sqlQuery = String.format(Locale.getDefault(),
					"SELECT %s FROM %s WHERE %s = " +
							"(SELECT %s FROM %s WHERE %s = %d)",
					TBL_MATCH_INFO_ID, TBL_MATCH_INFO, TBL_MATCH_INFO_MATCH_ID,
					TBL_STATE_MATCH_ID, TBL_STATE, TBL_STATE_ID, matchStateID);

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(sqlQuery, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				matchInfoID = cursor.getInt(cursor.getColumnIndex(TBL_MATCH_INFO_ID));
				cursor.close();
			}
			db.close();

		}

		return getMatchInfo(matchInfoID);
	}

	public MatchInfo getMatchInfo(int matchInfoID) {
		MatchInfo matchInfo = null;
		if (matchInfoID > 0) {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.query(TBL_MATCH_INFO, null, TBL_MATCH_INFO_ID + " = ?",
					new String[]{String.valueOf(matchInfoID)}, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				matchInfo = getMatchInfoFromCursor(db, cursor);
				cursor.close();
			}
			db.close();
		}

		return matchInfo;
	}
}
