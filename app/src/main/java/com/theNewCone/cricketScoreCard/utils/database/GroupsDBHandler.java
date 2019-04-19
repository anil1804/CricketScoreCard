package com.theNewCone.cricketScoreCard.utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.theNewCone.cricketScoreCard.enumeration.Stage;
import com.theNewCone.cricketScoreCard.enumeration.TournamentStageType;
import com.theNewCone.cricketScoreCard.match.Team;
import com.theNewCone.cricketScoreCard.tournament.Group;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GroupsDBHandler extends DatabaseHandler {
	public GroupsDBHandler(Context context) {
		super(context);
	}

	public int updateGroup(int tournamentID, Group group) {
		int rowID = 0;
		if (group != null) {
			Team[] teams = group.getTeams();
			List<Integer> teamIDs = new ArrayList<>();
			for (Team team : teams) {
				teamIDs.add(team.getId());
			}

			ContentValues values = new ContentValues();
			values.put(TBL_GROUP_NUMBER, group.getGroupNumber());
			values.put(TBL_GROUP_NAME, group.getName());
			values.put(TBL_GROUP_NUM_ROUNDS, group.getNumRounds());
			values.put(TBL_GROUP_STAGE_TYPE, group.getStageType().toString());
			values.put(TBL_GROUP_STAGE, group.getStage().toString());
			values.put(TBL_GROUP_IS_SCHEDULED, group.isScheduled());
			values.put(TBL_GROUP_IS_COMPLETED, group.isComplete());
			values.put(TBL_GROUP_TOURNAMENT_ID, tournamentID);
			values.put(TBL_GROUP_TEAMS, CommonUtils.intListToJSON(teamIDs));

			SQLiteDatabase db = this.getWritableDatabase();
			if (group.getId() > 0) {
				rowID = group.getId();
				db.update(TBL_GROUP, values, TBL_GROUP_ID + " = ?", new String[]{String.valueOf(rowID)});
			} else {
				rowID = (int) db.insert(TBL_GROUP, null, values);
			}

			db.close();
		}

		return rowID;
	}

	List<Group> getTournamentGroups(int tournamentID) {
		List<Group> groupList = new ArrayList<>();

		if (tournamentID > 0) {
			String sqlQuery = String.format(Locale.getDefault(),
					"SELECT * FROM %s WHERE %s = %d", TBL_GROUP, TBL_GROUP_TOURNAMENT_ID, tournamentID);

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(sqlQuery, null);

			if (cursor != null && cursor.moveToFirst()) {
				do {
					int id = cursor.getInt(cursor.getColumnIndex(TBL_GROUP_ID));
					int number = cursor.getInt(cursor.getColumnIndex(TBL_GROUP_NUMBER));
					String name = cursor.getString(cursor.getColumnIndex(TBL_GROUP_NAME));
					TournamentStageType type = TournamentStageType.valueOf(cursor.getString(cursor.getColumnIndex(TBL_GROUP_STAGE_TYPE)));
					Stage stage = Stage.valueOf(cursor.getString(cursor.getColumnIndex(TBL_GROUP_STAGE)));
					int numRounds = cursor.getInt(cursor.getColumnIndex(TBL_GROUP_NUM_ROUNDS));
					boolean isScheduled = cursor.getInt(cursor.getColumnIndex(TBL_GROUP_IS_SCHEDULED)) == 1;
					boolean isCompleted = cursor.getInt(cursor.getColumnIndex(TBL_GROUP_IS_COMPLETED)) == 1;

					List<Integer> teamIDs = CommonUtils.jsonToIntList(cursor.getString(cursor.getColumnIndex(TBL_GROUP_TEAMS)));
					List<Team> teamList = new TeamDBHandler(super.context).getTeams(teamIDs, db, true);

					Group group = new Group(number, teamList.size(), name,
							CommonUtils.objectArrToTeamArr(teamList.toArray()),
							numRounds, type, stage);

					group.setId(id);
					group.setScheduled(isScheduled);
					group.setComplete(isCompleted);

					group.setMatchInfoList(new MatchInfoDBHandler(super.context).getGroupMatchInfo(id, db));

					group.updatePointsTable(new PointsDataDBHandler(super.context).getGroupPointsTable(group.getId(), db));

					groupList.add(group);
				} while (cursor.moveToNext());

				cursor.close();
			}

			db.close();
		}

		return groupList;
	}

	public void completeGroup(int groupID) {
		if (groupID > 0) {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(TBL_GROUP_IS_COMPLETED, 1);

			db.update(TBL_GROUP, values, TBL_GROUP_ID + " = ?", new String[]{String.valueOf(groupID)});
		}
	}
}
