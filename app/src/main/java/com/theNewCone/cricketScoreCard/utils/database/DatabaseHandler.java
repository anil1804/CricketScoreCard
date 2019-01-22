package com.theNewCone.cricketScoreCard.utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.theNewCone.cricketScoreCard.Constants;
import com.theNewCone.cricketScoreCard.enumeration.BattingType;
import com.theNewCone.cricketScoreCard.enumeration.BowlingType;
import com.theNewCone.cricketScoreCard.enumeration.Stage;
import com.theNewCone.cricketScoreCard.enumeration.TournamentFormat;
import com.theNewCone.cricketScoreCard.enumeration.TournamentStageType;
import com.theNewCone.cricketScoreCard.help.HelpContent;
import com.theNewCone.cricketScoreCard.help.HelpDetail;
import com.theNewCone.cricketScoreCard.match.CricketCardUtils;
import com.theNewCone.cricketScoreCard.match.Match;
import com.theNewCone.cricketScoreCard.match.MatchState;
import com.theNewCone.cricketScoreCard.match.Team;
import com.theNewCone.cricketScoreCard.player.Player;
import com.theNewCone.cricketScoreCard.tournament.Group;
import com.theNewCone.cricketScoreCard.tournament.MatchInfo;
import com.theNewCone.cricketScoreCard.tournament.PointsData;
import com.theNewCone.cricketScoreCard.tournament.Tournament;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {

    public final int CODE_INS_PLAYER_DUP_RECORD = -10;
    public final int CODE_NEW_TEAM_DUP_RECORD = -10;
    public final int CODE_NEW_MATCH_DUP_RECORD = -10;
    public final int CODE_NEW_HELP_CONTENT_DUP_RECORD = -10;
	public final int CODE_NEW_TOURNAMENT_DUP_RECORD = -10;

	public static final int maxUndoAllowed = Integer.MAX_VALUE;
	private static final int DB_VERSION = 21;
    private static final String DB_NAME = "CricketScoreCard";

	private static final String SAVE_AUTO = "Auto";
	public static final String SAVE_MANUAL = "Manual";

	public final String TBL_STATE = "CricketMatch_State";
    private final String TBL_STATE_ID = "ID";
    private final String TBL_STATE_MATCH_JSON = "MatchData";
    private final String TBL_STATE_IS_AUTO = "AutoSave";
    private final String TBL_STATE_TIMESTAMP = "Timestamp";
    private final String TBL_STATE_NAME = "Name";
    private final String TBL_STATE_ORDER = "SaveOrder";
    private final String TBL_STATE_MATCH_ID = "MatchID";

	public final String TBL_PLAYER = "Player";
    private final String TBL_PLAYER_ID = "ID";
    private final String TBL_PLAYER_NAME = "Name";
    private final String TBL_PLAYER_AGE = "Age";
    private final String TBL_PLAYER_BAT_STYLE = "BattingStyle";
    private final String TBL_PLAYER_BOWL_STYLE = "BowlingStyle";
    private final String TBL_PLAYER_IS_WK = "IsWK";
	private final String TBL_PLAYER_ARCHIVED = "isArchived";

	public final String TBL_TEAM = "Team";
    private final String TBL_TEAM_ID = "ID";
    private final String TBL_TEAM_NAME = "Name";
    private final String TBL_TEAM_SHORT_NAME = "ShortName";
    private final String TBL_TEAM_ARCHIVED = "isArchived";

	public final String TBL_TEAM_PLAYERS = "TeamPlayers";
    private final String TBL_TEAM_PLAYERS_TEAM_ID = "TeamID";
    private final String TBL_TEAM_PLAYERS_PLAYER_ID = "PlayerID";

	public final String TBL_MATCH = "Matches";
    private final String TBL_MATCH_ID = "ID";
    private final String TBL_MATCH_NAME = "Name";
    private final String TBL_MATCH_TEAM1 = "Team1";
    private final String TBL_MATCH_TEAM2 = "Team2";
    private final String TBL_MATCH_DATE = "DatePlayed";
    private final String TBL_MATCH_IS_COMPLETE = "isComplete";
    private final String TBL_MATCH_IS_ARCHIVED = "isArchived";
    private final String TBL_MATCH_JSON = "MatchData";

    private final String TBL_HELP_CONTENT = "HelpContent";
    private final String TBL_HELP_CONTENT_ID = "ID";
    private final String TBL_HELP_CONTENT_CONTENT = "Content";

	private final String TBL_HELP_DETAILS = "HelpDetails";
	private final String TBL_HELP_DETAILS_CONTENT_ID = "ContentID";
	private final String TBL_HELP_DETAILS_VIEW_TYPE = "ViewType";
	private final String TBL_HELP_DETAILS_TEXT = "Text";
	private final String TBL_HELP_DETAILS_SRC_ID_JSON = "SourceIDJson";
	private final String TBL_HELP_DETAILS_ORDER = "ContentOrder";

	private final String TBL_TOURNAMENT = "Tournament";
	private final String TBL_TOURNAMENT_ID = "ID";
	private final String TBL_TOURNAMENT_NAME = "Name";
	private final String TBL_TOURNAMENT_TEAM_SIZE = "TeamSize";
	private final String TBL_TOURNAMENT_FORMAT = "Format";
	private final String TBL_TOURNAMENT_JSON = "Content";
	private final String TBL_TOURNAMENT_IS_SCHEDULED = "isScheduled";
	private final String TBL_TOURNAMENT_IS_COMPLETE = "isComplete";
	private final String TBL_TOURNAMENT_CREATED_DATE = "CreatedDate";

	private final String TBL_GROUP = "TournamentGroup";
	private final String TBL_GROUP_ID = "ID";
	private final String TBL_GROUP_NUMBER = "TournamentGroupNumber";
	private final String TBL_GROUP_NAME = "Name";
	private final String TBL_GROUP_TOURNAMENT_ID = "TournamentID";
	private final String TBL_GROUP_NUM_ROUNDS = "NumberOfRounds";
	private final String TBL_GROUP_STAGE_TYPE = "StageType";
	private final String TBL_GROUP_STAGE = "Stage";
	private final String TBL_GROUP_TEAMS = "TeamIDs";
	private final String TBL_GROUP_IS_SCHEDULED = "Scheduled";

	private final String TBL_MATCH_INFO = "GroupMatchInfo";
	private final String TBL_MATCH_INFO_ID = "ID";
	private final String TBL_MATCH_INFO_NUMBER = "MatchNumber";
	private final String TBL_MATCH_INFO_MATCH_ID = "MatchID";
	private final String TBL_MATCH_INFO_GROUP_ID = "GroupID";
	private final String TBL_MATCH_INFO_GROUP_NUMBER = "GroupNumber";
	private final String TBL_MATCH_INFO_GROUP_NAME = "GroupName";
	private final String TBL_MATCH_INFO_STAGE = "Stage";
	private final String TBL_MATCH_INFO_TEAM1_ID = "Team1ID";
	private final String TBL_MATCH_INFO_TEAM2_ID = "Team2ID";
	private final String TBL_MATCH_INFO_WINNER_ID = "WinningTeamID";
	private final String TBL_MATCH_INFO_DATE = "MatchDate";
	private final String TBL_MATCH_INFO_HAS_STARTED = "hasStarted";
	private final String TBL_MATCH_INFO_IS_COMPLETE = "isComplete";

	private final String TBL_POINTS_DATA = "PointsData";
	private final String TBL_POINTS_DATA_ID = "ID";
	private final String TBL_POINTS_DATA_GROUP_ID = "GroupID";
	private final String TBL_POINTS_DATA_TEAM_ID = "TeamID";
	private final String TBL_POINTS_DATA_MAX_OVERS = "MaxOvers";
	private final String TBL_POINTS_DATA_MAX_WICKETS = "MaxWickets";
	private final String TBL_POINTS_DATA_PLAYED = "Played";
	private final String TBL_POINTS_DATA_WON = "Won";
	private final String TBL_POINTS_DATA_LOST = "Lost";
	private final String TBL_POINTS_DATA_TIED = "Tied";
	private final String TBL_POINTS_DATA_NO_RESULT = "NoResult";
	private final String TBL_POINTS_DATA_NRR = "NetRR";
	private final String TBL_POINTS_DATA_RUNS_SCORED = "TotalRunsScored";
	private final String TBL_POINTS_DATA_RUNS_GIVEN = "TotalRunsGiven";
	private final String TBL_POINTS_DATA_WICKETS_LOST = "TotalWicketsLost";
	private final String TBL_POINTS_DATA_WICKETS_TAKEN = "TotalWicketsTaken";
	private final String TBL_POINTS_DATA_OVERS_PLAYED = "TotalOversPlayed";
	private final String TBL_POINTS_DATA_OVERS_BOWLED = "TotalOversBowled";

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createStateTable(db);
        createPlayerTable(db);
        createTeamTable(db);
        createTeamPlayersTable(db);
        createMatchTable(db);
        createHelpContentTable(db);
        createHelpDetailsTable(db);
		createTournamentTable(db);
		createGroupTable(db);
		createMatchInfoTable(db);
		createPointsDataTable(db);
    }

	@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	if(oldVersion < 2) {
			String alterTeamTableSQL = String.format(Locale.getDefault(),
					"ALTER TABLE %s ADD COLUMN %s INTEGER DEFAULT 0", TBL_TEAM, TBL_TEAM_ARCHIVED);

			db.execSQL(alterTeamTableSQL);

			String alterPlayerTableSQL = String.format(Locale.getDefault(),
					"ALTER TABLE %s ADD COLUMN %s INTEGER DEFAULT 0", TBL_PLAYER, TBL_PLAYER_ARCHIVED);
			db.execSQL(alterPlayerTableSQL);
		}
		if(oldVersion < 3) {
			db.delete(TBL_STATE, null, null);
		}
		if(oldVersion < 4) {
			String alterMatchTableSQL = String.format(Locale.getDefault(),
					"ALTER TABLE %s ADD COLUMN %s TEXT", TBL_MATCH, TBL_MATCH_DATE);
			db.execSQL(alterMatchTableSQL);

			alterMatchTableSQL = String.format(Locale.getDefault(),
					"ALTER TABLE %s ADD COLUMN %s INTEGER DEFAULT 0", TBL_MATCH, TBL_MATCH_IS_COMPLETE);
			db.execSQL(alterMatchTableSQL);

			alterMatchTableSQL = String.format(Locale.getDefault(),
					"ALTER TABLE %s ADD COLUMN %s TEXT", TBL_MATCH, TBL_MATCH_JSON);
			db.execSQL(alterMatchTableSQL);
		}
		if(oldVersion < 5) {
			createHelpContentTable(db);
			createHelpDetailsTable(db);
		}
		if(oldVersion < 14) {
			String dropTableSQL = "DROP TABLE IF EXISTS " + TBL_HELP_DETAILS;
			db.execSQL(dropTableSQL);
			createHelpDetailsTable(db);
		}
		if(oldVersion < 15) {
			String alterMatchTableSQL = String.format(Locale.getDefault(),
					"ALTER TABLE %s ADD COLUMN %s INTEGER", TBL_MATCH, TBL_MATCH_IS_ARCHIVED);
			db.execSQL(alterMatchTableSQL);
		}
		if (oldVersion < 16) {
			createTournamentTable(db);
		}
		if (oldVersion < 17) {
			String dropTableSQL = "DROP TABLE IF EXISTS " + TBL_TOURNAMENT;
			db.execSQL(dropTableSQL);

			createTournamentTable(db);
		}
		if (oldVersion < 18) {
			createGroupTable(db);
			createMatchInfoTable(db);
		}
		if (oldVersion < 19) {
			String alterMatchInfoTableSQL = String.format(Locale.getDefault(),
					"ALTER TABLE %s ADD COLUMN %s INTEGER", TBL_MATCH_INFO, TBL_MATCH_INFO_GROUP_NUMBER);
			db.execSQL(alterMatchInfoTableSQL);
		}
		if (oldVersion < 20) {
			String alterMatchInfoTableSQL = String.format(Locale.getDefault(),
					"ALTER TABLE %s ADD COLUMN %s INTEGER", TBL_MATCH_INFO, TBL_MATCH_INFO_WINNER_ID);
			db.execSQL(alterMatchInfoTableSQL);
		}
		if (oldVersion < 21) {
			createPointsDataTable(db);
		}
    }

    private void createStateTable(SQLiteDatabase db) {
        String createTableSQL =
                "CREATE TABLE " + TBL_STATE + "("
                        + TBL_STATE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + TBL_STATE_MATCH_JSON + " TEXT, "
                        + TBL_STATE_IS_AUTO + " INTEGER, "
                        + TBL_STATE_TIMESTAMP + " TEXT, "
                        + TBL_STATE_NAME + " TEXT, "
						+ TBL_STATE_ORDER + " INTEGER, "
						+ TBL_STATE_MATCH_ID + " INTEGER, "
						+ "FOREIGN KEY (" + TBL_STATE_MATCH_ID + ") REFERENCES " + TBL_MATCH + "(" + TBL_MATCH_ID + ")"
                        + ")";

        db.execSQL(createTableSQL);
    }

    private void createPlayerTable(SQLiteDatabase db) {
		String TBL_PLAYER_TEAM_ASSOCIATION = "TeamAssociation";
		String createTableSQL =
                "CREATE TABLE " + TBL_PLAYER + "("
                        + TBL_PLAYER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + TBL_PLAYER_NAME + " TEXT, "
                        + TBL_PLAYER_AGE + " TEXT, "
                        + TBL_PLAYER_BAT_STYLE + " TEXT, "
                        + TBL_PLAYER_BOWL_STYLE + " TEXT, "
                        + TBL_PLAYER_IS_WK + " INTEGER, "
						+ TBL_PLAYER_TEAM_ASSOCIATION + " TEXT, "
						+ TBL_PLAYER_ARCHIVED + " INTEGER DEFAULT 0"
                        + ")";

        db.execSQL(createTableSQL);
    }

    private void createTeamTable(SQLiteDatabase db) {
        String createTableSQL =
                "CREATE TABLE " + TBL_TEAM + "("
                        + TBL_TEAM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + TBL_TEAM_NAME + " TEXT, "
                        + TBL_TEAM_SHORT_NAME + " TEXT, "
						+ TBL_TEAM_ARCHIVED + " INTEGER DEFAULT 0"
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

    private void createMatchTable(SQLiteDatabase db) {
        String createTableSQL =
                "CREATE TABLE " + TBL_MATCH + "("
                        + TBL_MATCH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + TBL_MATCH_NAME + " TEXT, "
                        + TBL_MATCH_TEAM1 + " INTEGER, "
                        + TBL_MATCH_TEAM2 + " INTEGER, "
						+ TBL_MATCH_DATE + " TEXT, "
						+ TBL_MATCH_IS_COMPLETE + " INTEGER DEFAULT 0, "
						+ TBL_MATCH_IS_ARCHIVED + " INTEGER DEFAULT 0, "
						+ TBL_MATCH_JSON + " TEXT, "
                        + "FOREIGN KEY (" + TBL_MATCH_TEAM1 + ") REFERENCES " + TBL_TEAM + "(" + TBL_TEAM_ID + "), "
                        + "FOREIGN KEY (" + TBL_MATCH_TEAM2 + ") REFERENCES " + TBL_TEAM + "(" + TBL_TEAM_ID + ")"
                        + ")";

        db.execSQL(createTableSQL);
    }

    private void createHelpContentTable(SQLiteDatabase db) {
    	String createTableSQL =
				"CREATE TABLE " + TBL_HELP_CONTENT + "("
						+ TBL_HELP_CONTENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ TBL_HELP_CONTENT_CONTENT + " TEXT "
						+ ")";

    	db.execSQL(createTableSQL);
	}

	private void createHelpDetailsTable(SQLiteDatabase db) {
    	String createTableSQL =
				"CREATE TABLE " + TBL_HELP_DETAILS + "("
						+ TBL_HELP_DETAILS_CONTENT_ID + " INTEGER , "
						+ TBL_HELP_DETAILS_VIEW_TYPE + " TEXT, "
						+ TBL_HELP_DETAILS_TEXT + " TEXT, "
						+ TBL_HELP_DETAILS_SRC_ID_JSON + " TEXT, "
						+ TBL_HELP_DETAILS_ORDER + " INTEGER"
						+ ")";

    	db.execSQL(createTableSQL);
	}

	private void createTournamentTable(SQLiteDatabase db) {
		String createTableSQL =
				"CREATE TABLE " + TBL_TOURNAMENT + "("
						+ TBL_TOURNAMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ TBL_TOURNAMENT_NAME + " TEXT, "
						+ TBL_TOURNAMENT_TEAM_SIZE + " INTEGER, "
						+ TBL_TOURNAMENT_FORMAT + " TEXT, "
						+ TBL_TOURNAMENT_JSON + " TEXT, "
						+ TBL_TOURNAMENT_CREATED_DATE + " TEXT, "
						+ TBL_TOURNAMENT_IS_SCHEDULED + " INTEGER DEFAULT 0, "
						+ TBL_TOURNAMENT_IS_COMPLETE + " INTEGER DEFAULT 0"
						+ ")";

		db.execSQL(createTableSQL);
	}

	private void createGroupTable(SQLiteDatabase db) {
		String createTableSQL =
				"CREATE TABLE " + TBL_GROUP + "("
						+ TBL_GROUP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ TBL_GROUP_NUMBER + " INTEGER, "
						+ TBL_GROUP_NAME + " TEXT, "
						+ TBL_GROUP_TOURNAMENT_ID + " INTEGER, "
						+ TBL_GROUP_NUM_ROUNDS + " INTEGER, "
						+ TBL_GROUP_STAGE_TYPE + " TEXT, "
						+ TBL_GROUP_STAGE + " TEXT, "
						+ TBL_GROUP_TEAMS + " TEXT, "
						+ TBL_GROUP_IS_SCHEDULED + " INTEGER DEFAULT 0"
						+ ")";

		db.execSQL(createTableSQL);
	}

	private void createMatchInfoTable(SQLiteDatabase db) {
		String createTableSQL =
				"CREATE TABLE " + TBL_MATCH_INFO + "("
						+ TBL_MATCH_INFO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ TBL_MATCH_INFO_NUMBER + " INTEGER, "
						+ TBL_MATCH_INFO_MATCH_ID + " INTEGER, "
						+ TBL_MATCH_INFO_GROUP_ID + " INTEGER, "
						+ TBL_MATCH_INFO_GROUP_NUMBER + " INTEGER, "
						+ TBL_MATCH_INFO_GROUP_NAME + " TEXT, "
						+ TBL_MATCH_INFO_STAGE + " TEXT, "
						+ TBL_MATCH_INFO_TEAM1_ID + " INTEGER, "
						+ TBL_MATCH_INFO_TEAM2_ID + " INTEGER, "
						+ TBL_MATCH_INFO_DATE + " TEXT, "
						+ TBL_MATCH_INFO_HAS_STARTED + " INTEGER DEFAULT 0, "
						+ TBL_MATCH_INFO_IS_COMPLETE + " INTEGER DEFAULT 0, "
						+ TBL_MATCH_INFO_WINNER_ID + " INTEGER"
						+ ")";

		db.execSQL(createTableSQL);
	}

	private void createPointsDataTable(SQLiteDatabase db) {
		String createTableSQL =
				"CREATE TABLE " + TBL_POINTS_DATA + "("
						+ TBL_POINTS_DATA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ TBL_POINTS_DATA_GROUP_ID + " INTEGER, "
						+ TBL_POINTS_DATA_TEAM_ID + " INTEGER, "
						+ TBL_POINTS_DATA_MAX_OVERS + " INTEGER, "
						+ TBL_POINTS_DATA_MAX_WICKETS + " INTEGER, "
						+ TBL_POINTS_DATA_PLAYED + " INTEGER DEFAULT 0, "
						+ TBL_POINTS_DATA_WON + " INTEGER DEFAULT 0, "
						+ TBL_POINTS_DATA_LOST + " INTEGER DEFAULT 0, "
						+ TBL_POINTS_DATA_TIED + " INTEGER DEFAULT 0, "
						+ TBL_POINTS_DATA_NO_RESULT + " INTEGER DEFAULT 0, "
						+ TBL_POINTS_DATA_NRR + " TEXT DEFAULT 0, "
						+ TBL_POINTS_DATA_RUNS_SCORED + " INTEGER DEFAULT 0, "
						+ TBL_POINTS_DATA_RUNS_GIVEN + " INTEGER DEFAULT 0, "
						+ TBL_POINTS_DATA_WICKETS_LOST + " INTEGER DEFAULT 0, "
						+ TBL_POINTS_DATA_WICKETS_TAKEN + " INTEGER DEFAULT 0, "
						+ TBL_POINTS_DATA_OVERS_PLAYED + " TEXT DEFAULT 0, "
						+ TBL_POINTS_DATA_OVERS_BOWLED + " TEXT DEFAULT 0"
						+ ")";

		db.execSQL(createTableSQL);
	}

    private int saveMatchState(int matchID, String matchJson, int isAuto, String saveName, String matchName) {
        ContentValues values = new ContentValues();
        String timestamp = CommonUtils.currTimestamp();

        int saveOrder = 0;
		switch (isAuto){
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

		if(isAuto == 1)
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
    	if(cursor.moveToFirst()) {
    		saveOrder = cursor.getInt(cursor.getColumnIndex(MAX_SAVE_ORDER)) + 1;
		}

		cursor.close();
    	db.close();

    	return saveOrder;
	}

	private int getSavedMatchesWithSameName(String matchName) {
    	int count = 0;
    	final String countString = "ROW_COUNT";

    	String selectQuery = String.format(Locale.getDefault(), "SELECT COUNT(%s) AS %s FROM %s WHERE %s = '%s' AND %s = %d"
				, TBL_STATE_ID, countString, TBL_STATE, TBL_STATE_NAME, matchName, TBL_STATE_IS_AUTO, 1);

    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(selectQuery, null);

    	if(cursor != null && cursor.moveToFirst()) {
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
				TBL_STATE+"."+TBL_STATE_ID, MATCH_STATE_ID, TBL_STATE_IS_AUTO, TBL_STATE+"."+TBL_STATE_NAME, MATCH_SAVE_NAME,
				TBL_STATE_ORDER, TBL_STATE_MATCH_ID, TBL_STATE_TIMESTAMP,
				TBL_MATCH+"."+TBL_MATCH_NAME, MATCH_NAME, TBL_MATCH_TEAM1, TBL_MATCH_TEAM2,
				TBL_TEAM_SHORT_NAME, TBL_TEAM, TBL_TEAM+"."+TBL_TEAM_ID, TBL_MATCH+"."+TBL_MATCH_TEAM1, TEAM1_SHORT_NAME,
				TBL_TEAM_SHORT_NAME, TBL_TEAM, TBL_TEAM+"."+TBL_TEAM_ID, TBL_MATCH+"."+TBL_MATCH_TEAM2, TEAM2_SHORT_NAME,
				TBL_STATE, TBL_MATCH,
				TBL_MATCH+"."+TBL_MATCH_ID, TBL_STATE+"."+TBL_STATE_MATCH_ID);

        switch (autoOrManual) {
            case SAVE_AUTO:
                selectQuery += " AND " + TBL_STATE_IS_AUTO + "=1";
                break;

            case SAVE_MANUAL:
                selectQuery += " AND " + TBL_STATE_IS_AUTO + "=0";
                break;
        }

        if(matchID > 0) {
			selectQuery += " AND " + TBL_STATE_MATCH_ID + " = " + matchID;
		} else  if(partialName != null) {
            partialName = partialName.replaceAll("\\*", "%");
            selectQuery += " AND " + TBL_STATE_NAME + " LIKE '%" + partialName + "%'";
        }

		if (!includeTournaments) {
			selectQuery += " AND " + TBL_STATE_MATCH_ID + " NOT IN " +
					"(SELECT DISTINCT " + TBL_MATCH_INFO_MATCH_ID + " FROM " + TBL_MATCH_INFO + " WHERE " + TBL_STATE_MATCH_ID + " IS NOT NULL)";
		}

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
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
    	if(matchesToDelete != null && matchesToDelete.length > 0) {
    		whereClauseSB = new StringBuilder(TBL_STATE_ID + " IN (");
			for(MatchState match : matchesToDelete) {
				whereClauseSB.append(match.getId());
				whereClauseSB.append(", ");
			}

			whereClauseSB.delete(whereClauseSB.length() - 2, whereClauseSB.length());
			whereClauseSB.append(")");
			recordsToDelete = matchesToDelete.length;
		}

		int recordsDeleted = 0;
		if(whereClauseSB != null) {
    		SQLiteDatabase db = this.getWritableDatabase();
    		recordsDeleted = db.delete(TBL_STATE, whereClauseSB.toString(), null);
		}

		return recordsDeleted == recordsToDelete;
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

    public int getLastAutoSave(int matchID) {
    	int matchStateID = -1;

    	SQLiteDatabase db = this.getReadableDatabase();
    	String selectQuery = String.format(Locale.getDefault(),
				"SELECT %s FROM %s WHERE %s = %d AND %s = 1 ORDER BY %s DESC LIMIT 1"
				, TBL_STATE_ID, TBL_STATE, TBL_STATE_MATCH_ID, matchID, TBL_STATE_IS_AUTO, TBL_STATE_ORDER);
    	Cursor cursor = db.rawQuery(selectQuery, null);

    	if(cursor != null && cursor.moveToFirst()) {
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

    	if(cursor != null && cursor.moveToFirst()) {
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

    public boolean deleteMatches(Match[] matches) {
    	int rowsUpdated = 0;
    	if(matches != null && matches.length > 0) {
    		StringBuilder whereClauseSB = new StringBuilder();
    		whereClauseSB.append(TBL_MATCH_ID);
    		whereClauseSB.append(" IN (");

			for(Match match : matches) {
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

	public void clearMatchStateHistory(int ignoreCount, int matchID, int matchStateID) {
    	if(matchID < 0) {
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

		if(cursor != null && cursor.moveToFirst()) {
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

	public Player getPlayer(int playerID) {
		return getPlayer(playerID, false);
	}

	public Player getPlayer(int playerID, boolean includeArchived) {
		int archivedValue = includeArchived ? -1 : 1;
		String selectQuery = String.format(Locale.getDefault(),
				"SELECT * FROM %s WHERE %s = %d AND %s <> %d"
				, TBL_PLAYER, TBL_PLAYER_ID, playerID, TBL_PLAYER_ARCHIVED, archivedValue);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		Player player = null;
		List<Player> playerList = getPlayersFromCursor(cursor);
		if(playerList.size() > 0)
			player = playerList.get(0);

		cursor.close();
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

		if(playerIDList != null && playerIDList.size() > 0) {
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

		if(cursor != null && cursor.moveToFirst()) {
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
			} while(cursor.moveToNext());
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
        int rowsUpdated = db.update(TBL_TEAM, values, TBL_TEAM_ID + " = ?", new String[] {String.valueOf(teamID)});
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

        if(teamID > 0) {
            whereClauseSB.append(String.format(" AND %s = %s ", TBL_TEAM_ID, teamID));
        } else if(teamNamePattern != null) {
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
		return getTeams(teamIDList, db, includeArchived);
	}

	public List<Team> getTeams(List<Integer> teamIDList, SQLiteDatabase db) {
		return getTeams(teamIDList, db, false);
	}

	public List<Team> getTeams(List<Integer> teamIDList, SQLiteDatabase db, boolean includeArchived) {
		List<Team> teamList = new ArrayList<>();

		if(teamIDList != null && teamIDList.size() > 0) {
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
    	if(cursor != null && cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor.getColumnIndex(TBL_TEAM_ID));
				String name = cursor.getString(cursor.getColumnIndex(TBL_TEAM_NAME));
				String shortName = cursor.getString(cursor.getColumnIndex(TBL_TEAM_SHORT_NAME));

				teamList.add(new Team(id, name, shortName));
			} while(cursor.moveToNext());
		}

		return teamList;
	}

	public List<Integer> getAssociatedPlayers(int teamID) {
    	List<Integer> playerIDList = new ArrayList<>();

    	String selectQuery = String.format(Locale.getDefault(),
				"SELECT DISTINCT %s FROM %s WHERE %s = %d AND %s NOT IN " +
						"(SELECT %s FROM %s WHERE %s = 1)"
				,TBL_TEAM_PLAYERS_PLAYER_ID, TBL_TEAM_PLAYERS, TBL_TEAM_PLAYERS_TEAM_ID, teamID, TBL_TEAM_PLAYERS_PLAYER_ID
				, TBL_PLAYER_ID, TBL_PLAYER, TBL_PLAYER_ARCHIVED);

    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(selectQuery, null);

    	if(cursor.moveToFirst()) {
    		do {
    			playerIDList.add(cursor.getInt(cursor.getColumnIndex(TBL_TEAM_PLAYERS_PLAYER_ID)));
			} while (cursor.moveToNext());
		}

		cursor.close();
    	db.close();

    	return playerIDList;
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

    	if(cursor.moveToFirst()) {
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

    public void updateTeamList(@NonNull Team team, List<Integer> newPlayers, List<Integer> deletedPlayers) {

		if(newPlayers != null && newPlayers.size() > 0) {

			for (int playerID : newPlayers) {
				SQLiteDatabase db = this.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put(TBL_TEAM_PLAYERS_TEAM_ID, team.getId());
				values.put(TBL_TEAM_PLAYERS_PLAYER_ID, playerID);

				db.insert(TBL_TEAM_PLAYERS, null, values);

				Player player = getPlayer(playerID);
				if (player != null) {
					List<Integer> teamsAssociatedTo = player.getTeamsAssociatedTo();
					if (teamsAssociatedTo == null)
						teamsAssociatedTo = new ArrayList<>();
					if (!teamsAssociatedTo.contains(team.getId())) {
						teamsAssociatedTo.add(team.getId());
						player.setTeamsAssociatedTo(teamsAssociatedTo);
						updatePlayer(player);
					}
				}
				db.close();
			}
		}

		if(deletedPlayers != null && deletedPlayers.size() > 0) {
			for (int playerID : deletedPlayers) {
				SQLiteDatabase db = this.getWritableDatabase();
				db.delete(TBL_TEAM_PLAYERS, TBL_TEAM_PLAYERS_PLAYER_ID + " = ? AND " + TBL_TEAM_PLAYERS_TEAM_ID + " = ?",
						new String[]{String.valueOf(playerID), String.valueOf(team.getId())});

				Player player = getPlayer(playerID);
				if (player != null) {
					List<Integer> teamsAssociatedTo = player.getTeamsAssociatedTo();
					if (teamsAssociatedTo != null && teamsAssociatedTo.contains(team.getId())) {
						for (int i = 0; i < teamsAssociatedTo.size(); i++) {
							if (teamsAssociatedTo.get(i) == team.getId()) {
								teamsAssociatedTo.remove(i);
								player.setTeamsAssociatedTo(teamsAssociatedTo);
								updatePlayer(player);
								break;
							}
						}
					}
				}
				db.close();
			}
		}
    }

    public void updateTeamList(@NonNull Player player, List<Integer> newTeams, List<Integer> deletedTeams) {
		List<Integer> teamsAssociatedTo = getAssociatedTeams(player.getID());

		if(newTeams != null && newTeams.size() > 0) {
			SQLiteDatabase db = this.getWritableDatabase();
			for (int teamID : newTeams) {
				if(teamsAssociatedTo == null)
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

		if(deletedTeams != null && deletedTeams.size() > 0) {
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

    public int addNewMatch(Match match) {
        ContentValues values = new ContentValues();

        values.put(TBL_MATCH_NAME, match.getName());
        values.put(TBL_MATCH_TEAM1, match.getTeam1ID());
        values.put(TBL_MATCH_TEAM2, match.getTeam2ID());
        values.put(TBL_MATCH_DATE, CommonUtils.currTimestamp());

        long rowID = CODE_NEW_MATCH_DUP_RECORD;

		boolean createRecord = Constants.ALLOW_DUPLICATE_MATCH_NAME;
		if (!createRecord)
			createRecord = getSavedMatchesWithSameName(match.getName()) == 0;

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

    	if(cursor != null && cursor.moveToFirst()) {
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
		List<Team> teams = getTeams(teamIDList, db, true);

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

		if(cursor.moveToFirst()) {
			String matchData = cursor.getString(0);
			ccUtils = CommonUtils.convertToCCUtils(matchData);
		}
		cursor.close();
		db.close();

		return ccUtils;
	}

	public int addHelpContent(String content) {
    	ContentValues values = new ContentValues();
    	values.put(TBL_HELP_CONTENT_CONTENT, content);

    	SQLiteDatabase db = this.getWritableDatabase();

    	boolean alreadyExists = false;
    	String selectQuery = String.format(Locale.getDefault(),
				"SELECT %s FROM %s WHERE %s = '%s'",
				TBL_HELP_CONTENT_ID, TBL_HELP_CONTENT, TBL_HELP_CONTENT_CONTENT, content);
    	Cursor cursor = db.rawQuery(selectQuery, null);
    	if(cursor != null && cursor.moveToFirst()) {
			alreadyExists = true;
			cursor.close();
		}

    	long contentID = CODE_NEW_HELP_CONTENT_DUP_RECORD;

    	if(!alreadyExists)
    		contentID = db.insert(TBL_HELP_CONTENT, null, values);

		db.close();

    	return (int) contentID;
	}

	public boolean hasHelpContent() {
    	boolean hasContent = false;

		String selectQuery = String.format(Locale.getDefault(), "SELECT %s FROM %s", TBL_HELP_CONTENT_ID, TBL_HELP_CONTENT);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if(cursor != null && cursor.moveToFirst()) {
			hasContent = true;
			cursor.close();
		}
		db.close();

		return hasContent;
	}

	public List<HelpContent> getAllHelpContent() {
    	List<HelpContent> helpContentList = new ArrayList<>();
    	String selectQuery = "SELECT * FROM " + TBL_HELP_CONTENT;
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor cursor = db.rawQuery(selectQuery, null);

    	if(cursor != null && cursor.moveToFirst()) {
    		do {
    			int contentID = cursor.getInt(cursor.getColumnIndex(TBL_HELP_CONTENT_ID));
    			String content = cursor.getString(cursor.getColumnIndex(TBL_HELP_CONTENT_CONTENT));

    			List<HelpDetail> helpDetailList = getHelpDetails(contentID, content);
    			helpContentList.add(new HelpContent(contentID, content, helpDetailList));
			} while (cursor.moveToNext());

    		cursor.close();
		}
		db.close();

		return helpContentList;
	}

	public void addHelpDetails(HelpDetail helpDetail) {
		int maxContentOrder = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		String getMaxContentOrderIDSQL =
				String.format(Locale.getDefault(), "SELECT MAX(%s) FROM %s WHERE %s = %d",
						TBL_HELP_DETAILS_ORDER, TBL_HELP_DETAILS, TBL_HELP_DETAILS_CONTENT_ID, helpDetail.getContentID());
		Cursor cursor = db.rawQuery(getMaxContentOrderIDSQL, null);
		if(cursor != null && cursor.moveToFirst()) {
			maxContentOrder = cursor.getInt(0);
			cursor.close();
		}

		ContentValues values = new ContentValues();
		values.put(TBL_HELP_DETAILS_CONTENT_ID, helpDetail.getContentID());
		if(helpDetail.getText() != null) {
			values.put(TBL_HELP_DETAILS_TEXT, helpDetail.getText());
		}
		if(helpDetail.getSourceIDList() != null) {
			values.put(TBL_HELP_DETAILS_SRC_ID_JSON, CommonUtils.intListToJSON(helpDetail.getSourceIDList()));
		}
		if(helpDetail.getViewType() != null) {
			values.put(TBL_HELP_DETAILS_VIEW_TYPE, helpDetail.getViewType().toString());
		}
		values.put(TBL_HELP_DETAILS_ORDER, maxContentOrder + 1);

		db.insert(TBL_HELP_DETAILS, null, values);
		db.close();
	}

	private List<HelpDetail> getHelpDetails(int contentID, String content) {
    	List<HelpDetail> helpDetailList = new ArrayList<>();
    	if(contentID > 0) {
			SQLiteDatabase db = this.getReadableDatabase();

			if(content != null) {
				/*Getting Data from HelpDetail table*/
				String selectQuery = String.format(Locale.getDefault(),
						"SELECT * FROM %s WHERE %s = %d ORDER BY %s",
						TBL_HELP_DETAILS, TBL_HELP_DETAILS_CONTENT_ID, contentID, TBL_HELP_DETAILS_ORDER);

				Cursor cursor = db.rawQuery(selectQuery, null);
				if (cursor != null && cursor.moveToFirst()) {
					do {
						HelpDetail.ViewType viewType = HelpDetail.ViewType.valueOf(cursor.getString(cursor.getColumnIndex(TBL_HELP_DETAILS_VIEW_TYPE)));
						String text = cursor.getString(cursor.getColumnIndex(TBL_HELP_DETAILS_TEXT));
						String srcIDJson = cursor.getString(cursor.getColumnIndex(TBL_HELP_DETAILS_SRC_ID_JSON));
						int order = cursor.getInt(cursor.getColumnIndex(TBL_HELP_DETAILS_ORDER));

						helpDetailList.add(new HelpDetail(contentID, content, viewType, text, CommonUtils.jsonToIntList(srcIDJson), order));
					} while (cursor.moveToNext());

					cursor.close();
				}
			}

			db.close();
		}

		return helpDetailList;
	}

	public void clearHelpContent() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TBL_HELP_CONTENT, null, null);
		db.delete(TBL_HELP_DETAILS, null, null);

		db.close();
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

			List<Group> groupList = getTournamentGroups(tournamentID);
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

	private List<Group> getTournamentGroups(int tournamentID) {
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

					List<Integer> teamIDs = CommonUtils.jsonToIntList(cursor.getString(cursor.getColumnIndex(TBL_GROUP_TEAMS)));
					List<Team> teamList = getTeams(teamIDs, db, true);

					Group group = new Group(number, teamList.size(), name,
							CommonUtils.objectArrToTeamArr(teamList.toArray()),
							numRounds, type, stage);

					group.setId(id);
					group.setScheduled(isScheduled);

					group.setMatchInfoList(getGroupMatchInfo(id, db));

					group.updatePointsTable(getGroupPointsTable(group.getId(), db));

					groupList.add(group);
				} while (cursor.moveToNext());

				cursor.close();
			}
		}

		return groupList;
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

	private List<MatchInfo> getGroupMatchInfo(int groupID, SQLiteDatabase db) {
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

		List<Team> teamList = getTeams(teamIDs, db, true);

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
				tournament = getTournamentContent(tournamentID);
		}

		return tournament;
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
		}
	}

	private List<PointsData> getGroupPointsTable(int groupID, SQLiteDatabase db) {
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

				Team team = getTeams(null, teamID, true).get(0);

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
			}
		}

		return pointsData;
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
			}
		}

		return matchInfo;
	}

	public boolean checkIfMatchComplete(int matchID) {
		boolean isComplete = false;

		if (matchID > 0) {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.query(TBL_MATCH, new String[]{TBL_MATCH_IS_COMPLETE},
					TBL_MATCH_ID, new String[]{String.valueOf(matchID)},
					null, null, null);

			if (cursor != null && cursor.moveToFirst()) {
				isComplete = cursor.getInt(cursor.getColumnIndex(TBL_MATCH_IS_COMPLETE)) == 1;
				cursor.close();
			}

			db.close();
		}

		return isComplete;
	}

	public void deleteAllRecords(String tableName) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		switch (tableName) {
			case TBL_TEAM:
				values.put(TBL_TEAM_ARCHIVED, 1);
				db.update(TBL_TEAM, values, null, null);
				break;

			case TBL_PLAYER:
				values.put(TBL_PLAYER_ARCHIVED, 1);
				db.update(TBL_PLAYER, values, null, null);
				break;

			case TBL_MATCH:
				/*values.put(TBL_MATCH_IS_ARCHIVED, 1);
				db.update(TBL_MATCH, values, null, null);*/
				break;

			case TBL_STATE:
				db.delete(TBL_STATE, null, null);
				break;
		}
	}
}
