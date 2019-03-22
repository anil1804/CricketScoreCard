package com.theNewCone.cricketScoreCard.utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.theNewCone.cricketScoreCard.match.Team;
import com.theNewCone.cricketScoreCard.tournament.PointsData;

import java.util.ArrayList;
import java.util.List;

public class PointsDataDBHandler extends DatabaseHandler {
	public PointsDataDBHandler(Context context) {
		super(context);
	}

	public int addNewPointsDataRecord(PointsData pointsData, int groupID, int maxOvers, int maxWickets) {
		int rowID = 0;
		if (pointsData != null && groupID > 0) {
			ContentValues values = new ContentValues();

			values.put(TBL_POINTS_DATA_GROUP_ID, groupID);
			values.put(TBL_POINTS_DATA_TEAM_ID, pointsData.getTeam().getId());
			values.put(TBL_POINTS_DATA_MAX_OVERS, maxOvers);
			values.put(TBL_POINTS_DATA_MAX_WICKETS, maxWickets);
			values.put(TBL_POINTS_DATA_PLAYED, pointsData.getPlayed());
			values.put(TBL_POINTS_DATA_WON, pointsData.getWon());
			values.put(TBL_POINTS_DATA_LOST, pointsData.getLost());
			values.put(TBL_POINTS_DATA_TIED, pointsData.getTied());
			values.put(TBL_POINTS_DATA_NO_RESULT, pointsData.getNoResult());
			values.put(TBL_POINTS_DATA_NRR, pointsData.getNetRunRate());
			values.put(TBL_POINTS_DATA_RUNS_SCORED, pointsData.getTotalRunsScored());
			values.put(TBL_POINTS_DATA_RUNS_GIVEN, pointsData.getTotalsRunsGiven());
			values.put(TBL_POINTS_DATA_WICKETS_LOST, pointsData.getTotalWicketsLost());
			values.put(TBL_POINTS_DATA_WICKETS_TAKEN, pointsData.getTotalWicketsTaken());
			values.put(TBL_POINTS_DATA_OVERS_PLAYED, pointsData.getTotalOversPlayed());
			values.put(TBL_POINTS_DATA_OVERS_BOWLED, pointsData.getTotalOversBowled());

			SQLiteDatabase db = this.getWritableDatabase();

			if (pointsData.getId() > 0) {
				rowID = pointsData.getId();
				db.update(TBL_POINTS_DATA, values, TBL_POINTS_DATA_ID + "= ?", new String[]{String.valueOf(rowID)});
			} else {
				db.insert(TBL_POINTS_DATA, null, values);
			}

			db.close();
		}

		return rowID;
	}

	public void updatePointsDataRecord(PointsData pointsData) {
		if (pointsData != null) {
			ContentValues values = new ContentValues();

			values.put(TBL_POINTS_DATA_PLAYED, pointsData.getPlayed());
			values.put(TBL_POINTS_DATA_WON, pointsData.getWon());
			values.put(TBL_POINTS_DATA_LOST, pointsData.getLost());
			values.put(TBL_POINTS_DATA_TIED, pointsData.getTied());
			values.put(TBL_POINTS_DATA_NO_RESULT, pointsData.getNoResult());
			values.put(TBL_POINTS_DATA_NRR, pointsData.getNetRunRate());
			values.put(TBL_POINTS_DATA_RUNS_SCORED, pointsData.getTotalRunsScored());
			values.put(TBL_POINTS_DATA_RUNS_GIVEN, pointsData.getTotalsRunsGiven());
			values.put(TBL_POINTS_DATA_WICKETS_LOST, pointsData.getTotalWicketsLost());
			values.put(TBL_POINTS_DATA_WICKETS_TAKEN, pointsData.getTotalWicketsTaken());
			values.put(TBL_POINTS_DATA_OVERS_PLAYED, pointsData.getTotalOversPlayed());
			values.put(TBL_POINTS_DATA_OVERS_BOWLED, pointsData.getTotalOversBowled());

			SQLiteDatabase db = this.getWritableDatabase();

			db.update(TBL_POINTS_DATA, values,
					TBL_POINTS_DATA_ID + "= ?", new String[]{String.valueOf(pointsData.getId())});

			db.close();
		}
	}

	List<PointsData> getGroupPointsTable(int groupID, SQLiteDatabase db) {
		List<PointsData> pointsTable = new ArrayList<>();
		boolean isConnLive = true;

		if (groupID > 0) {
			if (db == null) {
				db = this.getReadableDatabase();
				isConnLive = false;
			} else if (!db.isOpen()) {
				db = this.getReadableDatabase();
			}

			Cursor cursor = db.query(TBL_POINTS_DATA, null,
					TBL_POINTS_DATA_GROUP_ID + " = ?", new String[]{String.valueOf(groupID)},
					null, null, null);

			pointsTable = getPointsTableFromCursor(cursor);
			cursor.close();

			if (!isConnLive)
				db.close();
		}

		return pointsTable;
	}

	private List<PointsData> getPointsTableFromCursor(Cursor cursor) {
		List<PointsData> pointsTable = new ArrayList<>();

		if (cursor != null && cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor.getColumnIndex(TBL_POINTS_DATA_ID));
				int teamID = cursor.getInt(cursor.getColumnIndex(TBL_POINTS_DATA_TEAM_ID));
				int maxOvers = cursor.getInt(cursor.getColumnIndex(TBL_POINTS_DATA_MAX_OVERS));
				int maxWickets = cursor.getInt(cursor.getColumnIndex(TBL_POINTS_DATA_MAX_WICKETS));
				int played = cursor.getInt(cursor.getColumnIndex(TBL_POINTS_DATA_PLAYED));
				int won = cursor.getInt(cursor.getColumnIndex(TBL_POINTS_DATA_WON));
				int lost = cursor.getInt(cursor.getColumnIndex(TBL_POINTS_DATA_LOST));
				int tied = cursor.getInt(cursor.getColumnIndex(TBL_POINTS_DATA_TIED));
				int noResult = cursor.getInt(cursor.getColumnIndex(TBL_POINTS_DATA_NO_RESULT));
				double runRate = Double.parseDouble(cursor.getString(cursor.getColumnIndex(TBL_POINTS_DATA_NRR)));
				int runsScored = cursor.getInt(cursor.getColumnIndex(TBL_POINTS_DATA_RUNS_SCORED));
				int runsGiven = cursor.getInt(cursor.getColumnIndex(TBL_POINTS_DATA_RUNS_GIVEN));
				int wicketsLost = cursor.getInt(cursor.getColumnIndex(TBL_POINTS_DATA_WICKETS_LOST));
				int wicketsTaken = cursor.getInt(cursor.getColumnIndex(TBL_POINTS_DATA_WICKETS_TAKEN));
				double oversPlayed = Double.parseDouble(cursor.getString(cursor.getColumnIndex(TBL_POINTS_DATA_OVERS_PLAYED)));
				double oversBowled = Double.parseDouble(cursor.getString(cursor.getColumnIndex(TBL_POINTS_DATA_OVERS_BOWLED)));

				Team team = new TeamDBHandler(super.context).getTeams(null, teamID, true).get(0);

				PointsData pointsData = new PointsData(id, team, maxOvers, maxWickets,
						played, won, lost, tied, noResult, runRate,
						runsScored, runsGiven, wicketsLost, wicketsTaken, oversPlayed, oversBowled);

				pointsTable.add(pointsData);

			} while (cursor.moveToNext());
		}

		return pointsTable;
	}

	public PointsData getTeamPointsData(int groupID, int teamID) {
		PointsData pointsData = null;
		if (groupID > 0 && teamID > 0) {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.query(TBL_POINTS_DATA, null,
					TBL_POINTS_DATA_GROUP_ID + " = ? AND " + TBL_POINTS_DATA_TEAM_ID + " = ?",
					new String[]{String.valueOf(groupID), String.valueOf(teamID)},
					null, null, null);

			if (cursor != null && cursor.moveToFirst()) {
				pointsData = getPointsTableFromCursor(cursor).get(0);
				cursor.close();
			}

			db.close();
		}

		return pointsData;
	}
}
