package com.theNewCone.cricketScoreCard.utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {

    public final int CODE_INS_PLAYER_DUP_RECORD = -10;
    public final int CODE_NEW_TEAM_DUP_RECORD = -10;
    public final int CODE_NEW_MATCH_DUP_RECORD = -10;
    public final int CODE_NEW_HELP_CONTENT_DUP_RECORD = -10;
	public final int CODE_NEW_TOURNAMENT_DUP_RECORD = -10;

	public static final int maxUndoAllowed = Integer.MAX_VALUE;
	static final String SAVE_AUTO = "Auto";
    private static final String DB_NAME = "CricketScoreCard";
	private static final int DB_VERSION = 22;
	public static final String SAVE_MANUAL = "Manual";
	protected final Context context;
	final String TBL_STATE = "CricketMatch_State";
	final String TBL_STATE_ID = "ID";
	final String TBL_STATE_MATCH_JSON = "MatchData";
	final String TBL_STATE_IS_AUTO = "AutoSave";
	final String TBL_STATE_TIMESTAMP = "Timestamp";
	final String TBL_STATE_NAME = "Name";
	final String TBL_STATE_ORDER = "SaveOrder";

	public final String TBL_PLAYER = "Player";
	final String TBL_STATE_MATCH_ID = "MatchID";
	final String TBL_PLAYER_ID = "ID";
	final String TBL_PLAYER_NAME = "Name";
	final String TBL_PLAYER_AGE = "Age";
	final String TBL_PLAYER_BAT_STYLE = "BattingStyle";
	final String TBL_PLAYER_BOWL_STYLE = "BowlingStyle";
	final String TBL_PLAYER_IS_WK = "IsWK";

	public final String TBL_TEAM = "Team";
	final String TBL_PLAYER_ARCHIVED = "isArchived";
	final String TBL_TEAM_ID = "ID";
	final String TBL_TEAM_NAME = "Name";
	final String TBL_TEAM_SHORT_NAME = "ShortName";
	final String TBL_TEAM_ARCHIVED = "isArchived";
	final String TBL_TEAM_PLAYERS = "TeamPlayers";
	final String TBL_TEAM_PLAYERS_TEAM_ID = "TeamID";
	final String TBL_TEAM_PLAYERS_PLAYER_ID = "PlayerID";

	final String TBL_MATCH = "Matches";
	final String TBL_MATCH_ID = "ID";
	final String TBL_MATCH_NAME = "Name";
	final String TBL_MATCH_TEAM1 = "Team1";
	final String TBL_MATCH_TEAM2 = "Team2";
	final String TBL_MATCH_DATE = "DatePlayed";
	final String TBL_MATCH_IS_COMPLETE = "isComplete";
	final String TBL_MATCH_IS_ARCHIVED = "isArchived";
	final String TBL_MATCH_JSON = "MatchData";

	final String TBL_HELP_CONTENT = "HelpContent";
	final String TBL_HELP_CONTENT_ID = "ID";
	final String TBL_HELP_CONTENT_CONTENT = "Content";
	final String TBL_HELP_DETAILS = "HelpDetails";
	final String TBL_HELP_DETAILS_CONTENT_ID = "ContentID";
	final String TBL_HELP_DETAILS_VIEW_TYPE = "ViewType";
	final String TBL_HELP_DETAILS_TEXT = "Text";
	final String TBL_HELP_DETAILS_SRC_ID_JSON = "SourceIDJson";
	final String TBL_HELP_DETAILS_ORDER = "ContentOrder";

	final String TBL_TOURNAMENT = "Tournament";
	final String TBL_TOURNAMENT_ID = "ID";
	final String TBL_TOURNAMENT_NAME = "Name";
	final String TBL_TOURNAMENT_TEAM_SIZE = "TeamSize";
	final String TBL_TOURNAMENT_FORMAT = "Format";
	final String TBL_TOURNAMENT_JSON = "Content";
	final String TBL_TOURNAMENT_IS_SCHEDULED = "isScheduled";
	final String TBL_TOURNAMENT_IS_COMPLETE = "isComplete";
	final String TBL_TOURNAMENT_CREATED_DATE = "CreatedDate";

	final String TBL_GROUP = "TournamentGroup";
	final String TBL_GROUP_ID = "ID";
	final String TBL_GROUP_NUMBER = "TournamentGroupNumber";
	final String TBL_GROUP_NAME = "Name";
	final String TBL_GROUP_TOURNAMENT_ID = "TournamentID";
	final String TBL_GROUP_NUM_ROUNDS = "NumberOfRounds";
	final String TBL_GROUP_STAGE_TYPE = "StageType";
	final String TBL_GROUP_STAGE = "Stage";
	final String TBL_GROUP_TEAMS = "TeamIDs";
	final String TBL_GROUP_IS_SCHEDULED = "Scheduled";

	final String TBL_MATCH_INFO = "GroupMatchInfo";
	final String TBL_MATCH_INFO_ID = "ID";
	final String TBL_MATCH_INFO_NUMBER = "MatchNumber";
	final String TBL_MATCH_INFO_MATCH_ID = "MatchID";
	final String TBL_MATCH_INFO_GROUP_ID = "GroupID";
	final String TBL_MATCH_INFO_GROUP_NUMBER = "GroupNumber";
	final String TBL_MATCH_INFO_GROUP_NAME = "GroupName";
	final String TBL_MATCH_INFO_STAGE = "Stage";
	final String TBL_MATCH_INFO_TEAM1_ID = "Team1ID";
	final String TBL_MATCH_INFO_TEAM2_ID = "Team2ID";
	final String TBL_MATCH_INFO_WINNER_ID = "WinningTeamID";
	final String TBL_MATCH_INFO_DATE = "MatchDate";
	final String TBL_MATCH_INFO_HAS_STARTED = "hasStarted";
	final String TBL_MATCH_INFO_IS_COMPLETE = "isComplete";

	final String TBL_POINTS_DATA = "PointsData";
	final String TBL_POINTS_DATA_ID = "ID";
	final String TBL_POINTS_DATA_GROUP_ID = "GroupID";
	final String TBL_POINTS_DATA_TEAM_ID = "TeamID";
	final String TBL_POINTS_DATA_MAX_OVERS = "MaxOvers";
	final String TBL_POINTS_DATA_MAX_WICKETS = "MaxWickets";
	final String TBL_POINTS_DATA_PLAYED = "Played";
	final String TBL_POINTS_DATA_WON = "Won";
	final String TBL_POINTS_DATA_LOST = "Lost";
	final String TBL_POINTS_DATA_TIED = "Tied";
	final String TBL_POINTS_DATA_NO_RESULT = "NoResult";
	final String TBL_POINTS_DATA_NRR = "NetRR";
	final String TBL_POINTS_DATA_RUNS_SCORED = "TotalRunsScored";
	final String TBL_POINTS_DATA_RUNS_GIVEN = "TotalRunsGiven";
	final String TBL_POINTS_DATA_WICKETS_LOST = "TotalWicketsLost";
	final String TBL_POINTS_DATA_WICKETS_TAKEN = "TotalWicketsTaken";
	final String TBL_POINTS_DATA_OVERS_PLAYED = "TotalOversPlayed";
	final String TBL_POINTS_DATA_OVERS_BOWLED = "TotalOversBowled";

	final String TBL_PLAYER_STATS = "PlayerStatistics";
	final String TBL_PLAYER_STATS_ID = "ID";
	final String TBL_PLAYER_STATS_MATCH_ID = "MatchID";
	final String TBL_PLAYER_STATS_TOURNAMENT_ID = "TournamentID";
	final String TBL_PLAYER_STATS_CATCHES = "Catches";
	final String TBL_PLAYER_STATS_RUN_OUTS = "RunOuts";
	final String TBL_PLAYER_STATS_STUMP_OUTS = "StumpOuts";

	final String TBL_BATSMAN_STATS = "BatsmanStatistics";
	final String TBL_BATSMAN_STATS_ID = "ID";
	final String TBL_BATSMAN_STATS_MATCH_ID = "MatchID";
	final String TBL_BATSMAN_STATS_TOURNAMENT_ID = "TournamentID";
	final String TBL_BATSMAN_STATS_RUNS = "Runs";
	final String TBL_BATSMAN_STATS_BALLS = "Balls";
	final String TBL_BATSMAN_STATS_DOTS = "Dots";
	final String TBL_BATSMAN_STATS_ONES = "Ones";
	final String TBL_BATSMAN_STATS_TWOS = "Twos";
	final String TBL_BATSMAN_STATS_THREES = "Threes";
	final String TBL_BATSMAN_STATS_FOURS = "Fours";
	final String TBL_BATSMAN_STATS_FIVES = "Fives";
	final String TBL_BATSMAN_STATS_SIXES = "Sixes";
	final String TBL_BATSMAN_STATS_SEVENS = "Sevens";
	final String TBL_BATSMAN_STATS_IS_OUT = "isOut";
	final String TBL_BATSMAN_STATS_DISMISSAL_TYPE = "DismissalType";
	final String TBL_BATSMAN_STATS_DISMISSED_BY = "DismissedBy";

	final String TBL_BOWLER_STATS = "BowlerStatistics";
	final String TBL_BOWLER_STATS_ID = "ID";
	final String TBL_BOWLER_STATS_MATCH_ID = "MatchID";
	final String TBL_BOWLER_STATS_TOURNAMENT_ID = "TournamentID";
	final String TBL_BOWLER_STATS_RUNS_GIVEN = "RunsGiven";
	final String TBL_BOWLER_STATS_OVERS_BOWLED = "OversBowled";
	final String TBL_BOWLER_STATS_WICKETS_TAKEN = "WicketsTaken";
	final String TBL_BOWLER_STATS_MAIDENS = "Maidens";
	final String TBL_BOWLER_STATS_BOWLED = "Bowled";
	final String TBL_BOWLER_STATS_CAUGHT = "Caught";
	final String TBL_BOWLER_STATS_HIT_WICKET = "HitWicket";
	final String TBL_BOWLER_STATS_LBW = "LBW";
	final String TBL_BOWLER_STATS_STUMPED = "Stumped";

    public DatabaseHandler(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
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
		createPlayerStatsTable(db);
		createBatsmanStatsTable(db);
		createBowlerStatsTable(db);
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

		if (oldVersion < 22) {
			createPlayerStatsTable(db);
			createBatsmanStatsTable(db);
			createBowlerStatsTable(db);
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

	private void createPlayerStatsTable(SQLiteDatabase db) {
		String createTableSQL =
				"CREATE TABLE " + TBL_PLAYER_STATS + "("
						+ TBL_PLAYER_STATS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ TBL_PLAYER_STATS_MATCH_ID + " INTEGER, "
						+ TBL_PLAYER_STATS_TOURNAMENT_ID + " INTEGER, "
						+ TBL_PLAYER_STATS_CATCHES + " INTEGER, "
						+ TBL_PLAYER_STATS_RUN_OUTS + " INTEGER, "
						+ TBL_PLAYER_STATS_STUMP_OUTS + " INTEGER"
						+ ")";

		db.execSQL(createTableSQL);
	}

	private void createBatsmanStatsTable(SQLiteDatabase db) {
		String createTableSQL =
				"CREATE TABLE " + TBL_BATSMAN_STATS + "("
						+ TBL_BATSMAN_STATS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ TBL_BATSMAN_STATS_MATCH_ID + " INTEGER, "
						+ TBL_BATSMAN_STATS_TOURNAMENT_ID + " INTEGER, "
						+ TBL_BATSMAN_STATS_RUNS + " INTEGER, "
						+ TBL_BATSMAN_STATS_BALLS + " INTEGER, "
						+ TBL_BATSMAN_STATS_DOTS + " INTEGER, "
						+ TBL_BATSMAN_STATS_ONES + " INTEGER, "
						+ TBL_BATSMAN_STATS_TWOS + " INTEGER, "
						+ TBL_BATSMAN_STATS_THREES + " INTEGER, "
						+ TBL_BATSMAN_STATS_FOURS + " INTEGER, "
						+ TBL_BATSMAN_STATS_FIVES + " INTEGER, "
						+ TBL_BATSMAN_STATS_SIXES + " INTEGER, "
						+ TBL_BATSMAN_STATS_SEVENS + " INTEGER, "
						+ TBL_BATSMAN_STATS_IS_OUT + " INTEGER, "
						+ TBL_BATSMAN_STATS_DISMISSAL_TYPE + " TEXT, "
						+ TBL_BATSMAN_STATS_DISMISSED_BY + " INTEGER"
						+ ")";

		db.execSQL(createTableSQL);
	}

	private void createBowlerStatsTable(SQLiteDatabase db) {
		String createTableSQL =
				"CREATE TABLE " + TBL_BOWLER_STATS + "("
						+ TBL_BOWLER_STATS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ TBL_BOWLER_STATS_MATCH_ID + " INTEGER, "
						+ TBL_BOWLER_STATS_TOURNAMENT_ID + " INTEGER, "
						+ TBL_BOWLER_STATS_OVERS_BOWLED + " TEXT, "
						+ TBL_BOWLER_STATS_RUNS_GIVEN + " INTEGER, "
						+ TBL_BOWLER_STATS_WICKETS_TAKEN + " INTEGER, "
						+ TBL_BOWLER_STATS_MAIDENS + " INTEGER, "
						+ TBL_BOWLER_STATS_BOWLED + " INTEGER, "
						+ TBL_BOWLER_STATS_CAUGHT + " INTEGER, "
						+ TBL_BOWLER_STATS_HIT_WICKET + " INTEGER, "
						+ TBL_BOWLER_STATS_LBW + " INTEGER, "
						+ TBL_BOWLER_STATS_STUMPED + " INTEGER"
						+ ")";

		db.execSQL(createTableSQL);
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

			case TBL_STATE:
				db.delete(TBL_STATE, null, null);
				break;
		}
	}
}
