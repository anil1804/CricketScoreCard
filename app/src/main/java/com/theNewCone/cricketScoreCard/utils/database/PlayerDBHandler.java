package com.theNewCone.cricketScoreCard.utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.theNewCone.cricketScoreCard.enumeration.BattingType;
import com.theNewCone.cricketScoreCard.enumeration.BowlingType;
import com.theNewCone.cricketScoreCard.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PlayerDBHandler extends DatabaseHandler {

	public PlayerDBHandler(Context context) {
		super(context);
	}

	public Player getPlayer(int playerID) {
		return getPlayer(playerID, false);
	}

	public Player getPlayer(int playerID, SQLiteDatabase db) {
		return getPlayer(playerID, false);
	}

	public Player getPlayer(int playerID, boolean includeArchived) {
		return getPlayer(playerID, includeArchived, null);
	}

	public Player getPlayer(int playerID, boolean includeArchived, SQLiteDatabase db) {
		int archivedValue = includeArchived ? -1 : 1;
		String selectQuery = String.format(Locale.getDefault(),
				"SELECT * FROM %s WHERE %s = %d AND %s <> %d"
				, TBL_PLAYER, TBL_PLAYER_ID, playerID, TBL_PLAYER_ARCHIVED, archivedValue);

		boolean isDBConnOpen = db != null;
		if (!isDBConnOpen)
			db = this.getReadableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, null);

		Player player = null;
		List<Player> playerList = getPlayersFromCursor(cursor);
		if (playerList.size() > 0)
			player = playerList.get(0);

		cursor.close();
		if (!isDBConnOpen)
			db.close();

		return player;
	}

	private List<Player> getMatchingPlayers(String playerName) {
		int archivedValue = 1;

		String selectQuery = String.format(Locale.getDefault(),
				"SELECT * FROM %s WHERE %s = '%s' AND %s <> %d"
				, TBL_PLAYER, TBL_PLAYER_NAME, playerName, TBL_PLAYER_ARCHIVED, archivedValue);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		List<Player> playerList = getPlayersFromCursor(cursor);

		cursor.close();
		db.close();

		return playerList;
	}

	public List<Player> getPlayers(List<Integer> playerIDList) {
		int archivedValue = 1;

		List<Player> playerList = new ArrayList<>();

		if (playerIDList != null && playerIDList.size() > 0) {
			StringBuilder whereClauseSB = new StringBuilder(" WHERE " + TBL_PLAYER_ID + " IN (");

			for (int playerID : playerIDList) {
				whereClauseSB.append(String.format("%s, ", playerID));
			}

			whereClauseSB.delete(whereClauseSB.length() - 2, whereClauseSB.length());
			whereClauseSB.append(")");

			String selectQuery = "SELECT * FROM " + TBL_PLAYER + whereClauseSB.toString() + " AND " + TBL_PLAYER_ARCHIVED + " <> " + archivedValue;

			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			playerList = getPlayersFromCursor(cursor);

			cursor.close();
			db.close();
		}

		return playerList;
	}

	public List<Player> getAllPlayers() {
		int archivedValue = 1;
		String selectQuery = "SELECT * FROM " + TBL_PLAYER + " WHERE " + TBL_PLAYER_ARCHIVED + " <> " + archivedValue;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		List<Player> playerList = getPlayersFromCursor(cursor);

		cursor.close();
		db.close();

		return playerList;
	}

	public int updatePlayer(Player player) {
		return updatePlayer(player, false);
	}

	int updatePlayer(Player player, boolean getIdIfExisting) {

		ContentValues values = new ContentValues();

		if (player.getID() <= 0) {
			List<Player> playerList = getMatchingPlayers(player.getName());
			if (playerList.size() > 0) {
				if (getIdIfExisting)
					return playerList.get(0).getID();
				else
					return CODE_INS_PLAYER_DUP_RECORD;
			}
		}

		values.put(TBL_PLAYER_NAME, player.getName());
		values.put(TBL_PLAYER_BAT_STYLE, player.getBattingStyle().toString());
		values.put(TBL_PLAYER_BOWL_STYLE, player.getBowlingStyle().toString());
		values.put(TBL_PLAYER_IS_WK, player.isWicketKeeper() ? 1 : 0);

		SQLiteDatabase db = this.getWritableDatabase();

		long rowID;
		if (player.getID() < 1)
			rowID = db.insert(TBL_PLAYER, null, values);
		else
			rowID = player.getID();
		db.update(TBL_PLAYER, values, TBL_PLAYER_ID + " = ?", new String[]{String.valueOf(player.getID())});
		db.close();

		return (int) rowID;
	}

	private List<Player> getPlayersFromCursor(Cursor cursor) {
		List<Player> playerList = new ArrayList<>();

		if (cursor != null && cursor.moveToFirst()) {
			do {
				Player player;

				int id = cursor.getInt(cursor.getColumnIndex(TBL_PLAYER_ID));
				String name = cursor.getString(cursor.getColumnIndex(TBL_PLAYER_NAME));
				int age = cursor.getInt(cursor.getColumnIndex(TBL_PLAYER_AGE));
				String batStyle = cursor.getString(cursor.getColumnIndex(TBL_PLAYER_BAT_STYLE));
				String bowlStyle = cursor.getString(cursor.getColumnIndex(TBL_PLAYER_BOWL_STYLE));
				int isWK = cursor.getInt(cursor.getColumnIndex(TBL_PLAYER_IS_WK));
				//String teamAssociation = cursor.getString(cursor.getColumnIndex(TBL_PLAYER_TEAM_ASSOCIATION));

				player = new Player(id, name, age, BattingType.valueOf(batStyle), BowlingType.valueOf(bowlStyle), isWK == 1);
				//player.setTeamsAssociatedToFromJSON(teamAssociation);
				player.setTeamsAssociatedTo(getAssociatedTeams(id));

				playerList.add(player);
			} while (cursor.moveToNext());
		}

		return playerList;
	}

	public boolean deletePlayer(int playerID) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TBL_PLAYER_ARCHIVED, 1);

		int rowsUpdated = db.update(TBL_PLAYER, values, TBL_PLAYER_ID + " = ?", new String[]{String.valueOf(playerID)});
		boolean success = rowsUpdated > 0;

		db.close();

		return success;
	}

	private List<Integer> getAssociatedTeams(int playerID) {
		List<Integer> teamIDList = new ArrayList<>();

		String selectQuery = String.format(Locale.getDefault(),
				"SELECT DISTINCT %s FROM %s WHERE %s = %d AND %s NOT IN " +
						"(SELECT %s FROM %s WHERE %s = 1)"
				, TBL_TEAM_PLAYERS_TEAM_ID, TBL_TEAM_PLAYERS, TBL_TEAM_PLAYERS_PLAYER_ID, playerID, TBL_TEAM_PLAYERS_TEAM_ID
				, TBL_TEAM_ID, TBL_TEAM, TBL_TEAM_ARCHIVED);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				teamIDList.add(cursor.getInt(cursor.getColumnIndex(TBL_TEAM_PLAYERS_TEAM_ID)));
			} while (cursor.moveToNext());
		}

		cursor.close();
		db.close();

		return teamIDList;
	}

	public List<Player> getTeamPlayers(int teamID) {
		int archivedValue = 1;
		String selectQuery = String.format(Locale.getDefault(),
				"SELECT * FROM %s WHERE %s IN (SELECT %s FROM %s WHERE %s = %s) AND %s <> %d",
				TBL_PLAYER, TBL_PLAYER_ID, TBL_TEAM_PLAYERS_PLAYER_ID,
				TBL_TEAM_PLAYERS, TBL_TEAM_PLAYERS_TEAM_ID, teamID, TBL_PLAYER_ARCHIVED, archivedValue);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		List<Player> playerList = getPlayersFromCursor(cursor);

		cursor.close();
		db.close();

		return playerList;
	}

	public void updateTeamList(@NonNull Player player, List<Integer> newTeams, List<Integer> deletedTeams) {
		List<Integer> teamsAssociatedTo = getAssociatedTeams(player.getID());

		if (newTeams != null && newTeams.size() > 0) {
			SQLiteDatabase db = this.getWritableDatabase();
			for (int teamID : newTeams) {
				if (teamsAssociatedTo == null)
					teamsAssociatedTo = new ArrayList<>();
				if (!teamsAssociatedTo.contains(teamID)) {
					ContentValues values = new ContentValues();
					values.put(TBL_TEAM_PLAYERS_TEAM_ID, teamID);
					values.put(TBL_TEAM_PLAYERS_PLAYER_ID, player.getID());

					db.insert(TBL_TEAM_PLAYERS, null, values);

					teamsAssociatedTo.add(teamID);
				}
			}
			db.close();
		}

		if (deletedTeams != null && deletedTeams.size() > 0) {
			SQLiteDatabase db = this.getWritableDatabase();
			for (int teamID : deletedTeams) {
				db.delete(TBL_TEAM_PLAYERS, TBL_TEAM_PLAYERS_PLAYER_ID + " = ? AND " + TBL_TEAM_PLAYERS_TEAM_ID + " = ?",
						new String[]{String.valueOf(player.getID()), String.valueOf(teamID)});

				if (teamsAssociatedTo != null && teamsAssociatedTo.contains(teamID)) {
					for (int i = 0; i < teamsAssociatedTo.size(); i++) {
						if (teamsAssociatedTo.get(i) == teamID) {
							teamsAssociatedTo.remove(i);
							break;
						}
					}
				}
			}
			db.close();
		}
	}
}
