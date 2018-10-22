package com.thenewcone.myscorecard.utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.SparseArray;

import com.thenewcone.myscorecard.player.Player;
import com.thenewcone.myscorecard.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    public final int CODE_INS_PLAYER_DUP_RECORD = -10;

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "CricketScoreCard";

    private static final String SAVE_AUTO = "Auto";
    private static final String SAVE_MANUAL = "Manual";

    private final String TBL_STATE = "CricketMatch_State";
    private final String TBL_STATE_ID = "ID";
    private final String TBL_STATE_MATCH_JSON = "MatchData";
    private final String TBL_STATE_IS_AUTO = "AutoSave";
    private final String TBL_STATE_TIMESTAMP = "Timestamp";
    private final String TBL_STATE_NAME = "Name";

    private final String TBL_PLAYER = "Player";
    private final String TBL_PLAYER_ID = "ID";
    private final String TBL_PLAYER_NAME = "Name";
    private final String TBL_PLAYER_AGE = "Age";
    private final String TBL_PLAYER_BAT_STYLE = "BattingStyle";
    private final String TBL_PLAYER_BOWL_STYLE = "BowlingStyle";
    private final String TBL_PLAYER_IS_WK = "IsWK";

    private final String TBL_TEAM = "Team";
    private final String TBL_TEAM_ID = "ID";
    private final String TBL_TEAM_NAME = "Name";

    private final String TBL_TEAM_PLAYERS = "TeamPlayers";
    private final String TBL_TEAM_PLAYERS_TEAM_ID = "TeamID";
    private final String TBL_TEAM_PLAYERS_PLAYER_ID = "PlayerID";

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createStateTable(db);
        createPlayerTable(db);
        createTeamTable(db);
        createTeamPlayersTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createStateTable(SQLiteDatabase db) {
        String createTableSQL =
                "CREATE TABLE " + TBL_STATE + "("
                + TBL_STATE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TBL_STATE_MATCH_JSON + " TEXT, "
                + TBL_STATE_IS_AUTO + " INTEGER, "
                + TBL_STATE_TIMESTAMP + " TEXT, "
                + TBL_STATE_NAME + " TEXT"
                + ")";

        db.execSQL(createTableSQL);
    }

    private void createPlayerTable(SQLiteDatabase db) {
        String createTableSQL =
                "CREATE TABLE " + TBL_PLAYER + "("
                + TBL_PLAYER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TBL_PLAYER_NAME + " TEXT, "
                + TBL_PLAYER_AGE + " TEXT, "
                + TBL_PLAYER_BAT_STYLE + " TEXT, "
                + TBL_PLAYER_BOWL_STYLE + " TEXT, "
                + TBL_PLAYER_IS_WK + " INTEGER "
                + ")";

        db.execSQL(createTableSQL);
    }

    private void createTeamTable(SQLiteDatabase db) {
        String createTableSQL =
                "CREATE TABLE " + TBL_TEAM + "("
                + TBL_TEAM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TBL_TEAM_NAME + " TEXT "
                + ")";

        db.execSQL(createTableSQL);
    }

    private void createTeamPlayersTable(SQLiteDatabase db) {
        String createTableSQL =
                "CREATE TABLE " + TBL_TEAM_PLAYERS + "("
                + TBL_TEAM_PLAYERS_TEAM_ID + " INTEGER, "
                + TBL_TEAM_PLAYERS_PLAYER_ID + " INTEGER, "
                + "FOREIGN KEY (" + TBL_TEAM_PLAYERS_TEAM_ID + ") REFERENCES " + TBL_TEAM + "(" + TBL_TEAM_ID + "), "
                + "FOREIGN KEY (" + TBL_TEAM_PLAYERS_PLAYER_ID + ") REFERENCES " + TBL_PLAYER + "(" + TBL_PLAYER_ID + ")"
                + ")";

        db.execSQL(createTableSQL);
    }

    private int saveMatchState(int matchStateID, String matchJson, int isAuto, String saveName, String matchName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        String timestamp = CommonUtils.currTimestamp();

        if(matchStateID < 0) {
            switch (isAuto){
                case 0:
                    int suffix = getSavedMatches(SAVE_MANUAL, saveName).size();
                    saveName = (suffix > 0) ? saveName + "-" + suffix : saveName;
                    break;
                case 1:
                    SparseArray<String> savedMatches = getSavedMatches(matchName);
                    if(savedMatches != null && savedMatches.size() > 0) {
                        matchStateID = savedMatches.keyAt(0);
                    } else {
                        saveName = matchName + "@auto";
                    }
                    break;
            }
        }

        if(matchStateID >= 0)
            values.put(TBL_STATE_ID, matchStateID);

        values.put(TBL_STATE_MATCH_JSON, matchJson);
        values.put(TBL_STATE_IS_AUTO, isAuto);
        values.put(TBL_STATE_NAME, saveName);
        values.put(TBL_STATE_TIMESTAMP, timestamp);

        long rowIID = db.replace(TBL_STATE, null, values);
        db.close();

        return (int) rowIID;
    }

    public int saveMatchState(int matchStateID, String matchJson, String saveName) {
        return saveMatchState(matchStateID, matchJson, 0, saveName, null);
    }

    public int autoSaveMatch(int matchStateID, String matchJson, String matchName) {
        return saveMatchState(matchStateID, matchJson, 1, null, matchName);
    }

    public SparseArray<String> getSavedMatches(String autoOrManual) {
        return getSavedMatches(autoOrManual, null);
    }

    public SparseArray<String> getSavedMatches(String autoOrManual, String partialName) {
        SparseArray<String> savedMatchNames = new SparseArray<>();

        String selectQuery = "SELECT * FROM " + TBL_STATE;
        switch (autoOrManual) {
            case SAVE_AUTO:
                selectQuery += " WHERE " + TBL_STATE_IS_AUTO + "=0";
                break;
            case SAVE_MANUAL:
                selectQuery += "WHERE " + TBL_STATE_IS_AUTO + "=1";
                break;
        }

        if(partialName != null) {
            partialName = partialName.replaceAll("\\*", "%");
            selectQuery += " AND " + TBL_STATE_NAME + " LIKE " + partialName;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(TBL_STATE_ID));
                String name = cursor.getString(cursor.getColumnIndex(TBL_STATE_NAME));
                savedMatchNames.append(id, name);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return savedMatchNames;
    }

    public String retrieveMatchData(int matchStateID) {
        String matchData = null;

        String selectQuery = "SELECT " + TBL_STATE_MATCH_JSON + " FROM " + TBL_STATE + " WHERE " + TBL_STATE_ID + " = " + matchStateID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            matchData = cursor.getString(0);
        }
        cursor.close();
        db.close();

        return matchData;
    }

    public Player getPlayer(int playerID) {
        Player player = null;

        String selectQuery = "SELECT * FROM " + TBL_PLAYER + " WHERE " + TBL_PLAYER_ID + " = " + playerID;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(TBL_PLAYER_ID)));
            String name = cursor.getString(cursor.getColumnIndex(TBL_PLAYER_NAME));
            int age = cursor.getInt(cursor.getColumnIndex(TBL_PLAYER_AGE));
            String batStyle = cursor.getString(cursor.getColumnIndex(TBL_PLAYER_BAT_STYLE));
            String bowlStyle = cursor.getString(cursor.getColumnIndex(TBL_PLAYER_BOWL_STYLE));
            int isWK = cursor.getInt(cursor.getColumnIndex(TBL_PLAYER_IS_WK));

            player = new Player(id, name, age, Player.BattingType.valueOf(batStyle), Player.BowlingType.valueOf(bowlStyle), isWK == 1);
        }
        cursor.close();
        db.close();

        return player;
    }

    public List<Player> getMatchingPlayers(String playerName, boolean exactMatch) {
        List<Player> playerList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TBL_PLAYER + " WHERE " + TBL_PLAYER_NAME;
        selectQuery += (exactMatch)
                        ? String.format(" = '%s'", playerName)
                        : String.format(" LIKE '%s%s%s'", "%", playerName, "%");

        Log.i("Query", "Select Query -> " + selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                Player player;

                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(TBL_PLAYER_ID)));
                String name = cursor.getString(cursor.getColumnIndex(TBL_PLAYER_NAME));
                int age = cursor.getInt(cursor.getColumnIndex(TBL_PLAYER_AGE));
                String batStyle = cursor.getString(cursor.getColumnIndex(TBL_PLAYER_BAT_STYLE));
                String bowlStyle = cursor.getString(cursor.getColumnIndex(TBL_PLAYER_BOWL_STYLE));
                int isWK = cursor.getInt(cursor.getColumnIndex(TBL_PLAYER_IS_WK));

                player = new Player(id, name, age, Player.BattingType.valueOf(batStyle), Player.BowlingType.valueOf(bowlStyle), isWK == 1);
                playerList.add(player);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return playerList;
    }

    public List<Player> getAllPlayers() {
        List<Player> playerList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TBL_PLAYER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {

            do {
                Player player = null;

                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(TBL_PLAYER_ID)));
                String name = cursor.getString(cursor.getColumnIndex(TBL_PLAYER_NAME));
                int age = cursor.getInt(cursor.getColumnIndex(TBL_PLAYER_AGE));
                String batStyle = cursor.getString(cursor.getColumnIndex(TBL_PLAYER_BAT_STYLE));
                String bowlStyle = cursor.getString(cursor.getColumnIndex(TBL_PLAYER_BOWL_STYLE));
                int isWK = cursor.getInt(cursor.getColumnIndex(TBL_PLAYER_IS_WK));

                player = new Player(id, name, age, Player.BattingType.valueOf(batStyle), Player.BowlingType.valueOf(bowlStyle), isWK == 1);

                playerList.add(player);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return playerList;
    }

    public int upsertPlayer(Player player) {

        ContentValues values = new ContentValues();

        if(player.getID() < 0) {
            if(getMatchingPlayers(player.getName(), true).size() > 0) {
                return CODE_INS_PLAYER_DUP_RECORD;
            }
        } else {
            values.put(TBL_PLAYER_ID, player.getID());
        }

        values.put(TBL_PLAYER_NAME, player.getName());
        values.put(TBL_PLAYER_AGE, player.getAge());
        values.put(TBL_PLAYER_BAT_STYLE, player.getBattingStyle().toString());
        values.put(TBL_PLAYER_BOWL_STYLE, player.getBowlingStyle().toString());
        values.put(TBL_PLAYER_IS_WK, player.isWicketKeeper() ? 1 : 0);

        SQLiteDatabase db = this.getWritableDatabase();
        long rowID = db.replace(TBL_PLAYER, null, values);
        db.close();

        return (int) rowID;
    }

    public boolean deletePlayer(int playerID) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TBL_PLAYER, TBL_PLAYER_ID + "=?", new String[]{String.valueOf(playerID)});
        db.close();

        return (rowsDeleted > 0);
    }

    public int addNewTeam(String teamName) {
        ContentValues values = new ContentValues();
        values.put(TBL_TEAM_NAME, teamName);

        SQLiteDatabase db = this.getWritableDatabase();
        long rowID = db.insert(TBL_TEAM, null, values);
        db.close();

        return (int) rowID;
    }

    public void deleteTeam(int teamID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_TEAM, TBL_TEAM_ID + "=?", new String[]{String.valueOf(teamID)});
        db.close();
    }

    public SparseArray<String> getTeams() {
        SparseArray<String> teamList = new SparseArray<>();

        String selectQuery = "SELECT * FROM " + TBL_TEAM;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            teamList.append(cursor.getInt(0), cursor.getString(1));
        }
        cursor.close();
        db.close();

        return teamList;
    }

    public void updateTeamList(int teamID, List<Integer> playerIDs) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TBL_TEAM_PLAYERS, TBL_TEAM_PLAYERS_TEAM_ID + "=?", new String[]{String.valueOf(teamID)});

        for(int playerID : playerIDs) {
            ContentValues values = new ContentValues();
            values.put(TBL_TEAM_PLAYERS_TEAM_ID, teamID);
            values.put(TBL_TEAM_PLAYERS_PLAYER_ID, playerID);

            db.insert(TBL_TEAM_PLAYERS, null, values);
        }

        db.close();
    }
}
