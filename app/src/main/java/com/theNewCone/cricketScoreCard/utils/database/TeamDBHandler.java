package com.theNewCone.cricketScoreCard.utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.theNewCone.cricketScoreCard.match.Team;
import com.theNewCone.cricketScoreCard.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TeamDBHandler extends DatabaseHandler {
	public TeamDBHandler(Context context) {
		super(context);
	}

	public int updateTeam(Team team) {
		return updateTeam(team, false);
	}

	int updateTeam(Team team, boolean getIdIfExisting) {
		ContentValues values = new ContentValues();

		if (team.getId() <= 0) {
			List<Team> teamList = getTeams(team.getName(), -1);
			if (teamList.size() > 0) {
				if (getIdIfExisting)
					return teamList.get(0).getId();
				else
					return CODE_NEW_TEAM_DUP_RECORD;
			}
		}

		values.put(TBL_TEAM_NAME, team.getName());
		values.put(TBL_TEAM_SHORT_NAME, team.getShortName());

		SQLiteDatabase db = this.getWritableDatabase();

		long rowID;
		if (team.getId() < 1)
			rowID = db.insert(TBL_TEAM, null, values);
		else
			rowID = team.getId();
		db.update(TBL_TEAM, values, TBL_TEAM_ID + " = ?", new String[]{String.valueOf(team.getId())});

		db.close();

		return (int) rowID;
	}

	public boolean deleteTeam(int teamID) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(TBL_TEAM_ARCHIVED, 1);
		int rowsUpdated = db.update(TBL_TEAM, values, TBL_TEAM_ID + " = ?", new String[]{String.valueOf(teamID)});
		boolean success = rowsUpdated > 0;

		db.close();

		return success;
	}

	public List<Team> getTeams(String teamNamePattern, int teamID) {
		return getTeams(teamNamePattern, teamID, false);
	}

	public List<Team> getTeams(String teamNamePattern, int teamID, boolean includeArchived) {
		int archivedValue = includeArchived ? -1 : 1;

		StringBuilder whereClauseSB = new StringBuilder(" WHERE ");
		whereClauseSB.append(TBL_TEAM_ARCHIVED);
		whereClauseSB.append(" <> ");
		whereClauseSB.append(archivedValue);

		if (teamID > 0) {
			whereClauseSB.append(String.format(" AND %s = %s ", TBL_TEAM_ID, teamID));
		} else if (teamNamePattern != null) {
			whereClauseSB.append(" AND ");
			whereClauseSB.append(TBL_TEAM_NAME);
			whereClauseSB.append(" LIKE '%");
			whereClauseSB.append(teamNamePattern);
			whereClauseSB.append("%'");
		}

		String selectQuery = "SELECT * FROM " + TBL_TEAM + whereClauseSB.toString();

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		List<Team> teamList = getTeamsFromCursor(cursor);

		cursor.close();
		db.close();

		return teamList;
	}

	public List<Team> getTeams(List<Integer> teamIDList) {
		return getTeams(teamIDList, false);
	}

	public List<Team> getTeams(List<Integer> teamIDList, boolean includeArchived) {
		int archivedValue = includeArchived ? -1 : 1;

		StringBuilder whereClauseSB = new StringBuilder(" WHERE ");
		whereClauseSB.append(TBL_TEAM_ARCHIVED);
		whereClauseSB.append(" <> ");
		whereClauseSB.append(archivedValue);

		SQLiteDatabase db = this.getReadableDatabase();
		List<Team> teamList = getTeams(teamIDList, db, includeArchived);

		db.close();
		return teamList;
	}

	public List<Team> getTeams(List<Integer> teamIDList, SQLiteDatabase db) {
		return getTeams(teamIDList, db, false);
	}

	public List<Team> getTeams(List<Integer> teamIDList, SQLiteDatabase db, boolean includeArchived) {
		List<Team> teamList = new ArrayList<>();

		if (teamIDList != null && teamIDList.size() > 0) {
			boolean hasDBConn = true;
			StringBuilder whereClauseSB = new StringBuilder(" WHERE " + TBL_TEAM_ID + " IN (");

			for (int teamID : teamIDList) {
				whereClauseSB.append(String.format("%s, ", teamID));
			}

			whereClauseSB.delete(whereClauseSB.length() - 2, whereClauseSB.length());
			whereClauseSB.append(")");

			int archivedValue = includeArchived ? -1 : 1;
			whereClauseSB.append(" AND ");
			whereClauseSB.append(TBL_TEAM_ARCHIVED);
			whereClauseSB.append(" <> ");
			whereClauseSB.append(archivedValue);

			String selectQuery = "SELECT * FROM " + TBL_TEAM + whereClauseSB.toString();

			if (db == null) {
				db = this.getReadableDatabase();
				hasDBConn = false;
			} else if (!db.isOpen()) {
				db = this.getReadableDatabase();
			}

			Cursor cursor = db.rawQuery(selectQuery, null);

			teamList = getTeamsFromCursor(cursor);

			cursor.close();

			if (!hasDBConn)
				db.close();
		}

		return teamList;
	}

	private List<Team> getTeamsFromCursor(Cursor cursor) {
		List<Team> teamList = new ArrayList<>();
		if (cursor != null && cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor.getColumnIndex(TBL_TEAM_ID));
				String name = cursor.getString(cursor.getColumnIndex(TBL_TEAM_NAME));
				String shortName = cursor.getString(cursor.getColumnIndex(TBL_TEAM_SHORT_NAME));

				teamList.add(new Team(id, name, shortName));
			} while (cursor.moveToNext());
		}

		return teamList;
	}

	public void updateTeamList(@NonNull Team team, List<Integer> newPlayers, List<Integer> deletedPlayers) {

		PlayerDBHandler playerDBHandler = new PlayerDBHandler(super.context);

		if (newPlayers != null && newPlayers.size() > 0) {

			for (int playerID : newPlayers) {
				SQLiteDatabase db = this.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put(TBL_TEAM_PLAYERS_TEAM_ID, team.getId());
				values.put(TBL_TEAM_PLAYERS_PLAYER_ID, playerID);

				db.insert(TBL_TEAM_PLAYERS, null, values);

				Player player = playerDBHandler.getPlayer(playerID);
				if (player != null) {
					List<Integer> teamsAssociatedTo = player.getTeamsAssociatedTo();
					if (teamsAssociatedTo == null)
						teamsAssociatedTo = new ArrayList<>();
					if (!teamsAssociatedTo.contains(team.getId())) {
						teamsAssociatedTo.add(team.getId());
						player.setTeamsAssociatedTo(teamsAssociatedTo);
						playerDBHandler.updatePlayer(player);
					}
				}
				db.close();
			}
		}

		if (deletedPlayers != null && deletedPlayers.size() > 0) {
			for (int playerID : deletedPlayers) {
				SQLiteDatabase db = this.getWritableDatabase();
				db.delete(TBL_TEAM_PLAYERS, TBL_TEAM_PLAYERS_PLAYER_ID + " = ? AND " + TBL_TEAM_PLAYERS_TEAM_ID + " = ?",
						new String[]{String.valueOf(playerID), String.valueOf(team.getId())});

				Player player = playerDBHandler.getPlayer(playerID);
				if (player != null) {
					List<Integer> teamsAssociatedTo = player.getTeamsAssociatedTo();
					if (teamsAssociatedTo != null && teamsAssociatedTo.contains(team.getId())) {
						for (int i = 0; i < teamsAssociatedTo.size(); i++) {
							if (teamsAssociatedTo.get(i) == team.getId()) {
								teamsAssociatedTo.remove(i);
								player.setTeamsAssociatedTo(teamsAssociatedTo);
								playerDBHandler.updatePlayer(player);
								break;
							}
						}
					}
				}
				db.close();
			}
		}
	}

	public List<Integer> getAssociatedPlayers(int teamID) {
		List<Integer> playerIDList = new ArrayList<>();

		String selectQuery = String.format(Locale.getDefault(),
				"SELECT DISTINCT %s FROM %s WHERE %s = %d AND %s NOT IN " +
						"(SELECT %s FROM %s WHERE %s = 1)"
				, TBL_TEAM_PLAYERS_PLAYER_ID, TBL_TEAM_PLAYERS, TBL_TEAM_PLAYERS_TEAM_ID, teamID, TBL_TEAM_PLAYERS_PLAYER_ID
				, TBL_PLAYER_ID, TBL_PLAYER, TBL_PLAYER_ARCHIVED);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				playerIDList.add(cursor.getInt(cursor.getColumnIndex(TBL_TEAM_PLAYERS_PLAYER_ID)));
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return playerIDList;
	}
}
