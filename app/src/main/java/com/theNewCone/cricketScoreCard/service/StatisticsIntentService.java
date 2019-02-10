package com.theNewCone.cricketScoreCard.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.theNewCone.cricketScoreCard.match.CricketCardUtils;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;
import com.theNewCone.cricketScoreCard.utils.database.StatisticsDatabaseHandler;


public class StatisticsIntentService extends IntentService {
	private static final String ACTION_STORE_MATCH_STATS = "StoreMatchStatistics";

/*
	private static final String ACTION_GET_PLAYER_STATS = "GetPlayerStatistics";

	private static final String ACTION_TOURNAMENT_GET_BATSMAN_STATS = "GetTournamentBatsmanStatistics";
	private static final String ACTION_TOURNAMENT_GET_BOWLER_STATS = "GetTournamentBowlerStatistics";
	private static final String ACTION_TOURNAMENT_GET_WK_STATS = "GetTournamentWKStatistics";
	private static final String ACTION_TOURNAMENT_GET_FIELDER_STATS = "GetTournamentFielderStatistics";
	private static final String ACTION_TOURNAMENT_GET_PLAYER_STATS = "GetTournamentPlayerStatistics";
	private static final String ACTION_TOURNAMENT_GET_SCORE_STATS = "GetTournamentPlayerStatistics";
*/

	private static final String EXTRA_CC_UTILS = "CricketCardUtils";
	private static final String EXTRA_TOURNAMENT_ID = "isTournament";

	private Context context;

	public StatisticsIntentService() {
		super("StatisticsIntentService");
	}

	public void startActionStoreMatchStatistics(Context context, CricketCardUtils ccUtils, boolean isTournament, int tournamentID) {
		this.context = context;

		if(!isTournament)
			tournamentID = 0;

		Intent intent = new Intent(context, StatisticsIntentService.class);
		intent.setAction(ACTION_STORE_MATCH_STATS);
		intent.putExtra(EXTRA_CC_UTILS, CommonUtils.convertToJSON(ccUtils));
		intent.putExtra(EXTRA_TOURNAMENT_ID, tournamentID);
		context.startService(intent);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent != null) {
			final String action = intent.getAction();
			if(action != null) {
				switch (action) {
					case ACTION_STORE_MATCH_STATS:
						final CricketCardUtils ccUtils = CommonUtils.convertToCCUtils(intent.getStringExtra(EXTRA_CC_UTILS));
						final int tournamentID = intent.getIntExtra(EXTRA_TOURNAMENT_ID, 0);
						storeMatchStatistics(ccUtils, tournamentID);
						break;
				}
			}
		}
	}

	private void storeMatchStatistics(CricketCardUtils ccUtils, int tournamentID) {
		StatisticsDatabaseHandler sdbHandler = new StatisticsDatabaseHandler(context);
		sdbHandler.addPlayerStats(ccUtils, tournamentID);
	}
}
