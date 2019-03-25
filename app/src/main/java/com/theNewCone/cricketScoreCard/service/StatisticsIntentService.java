package com.theNewCone.cricketScoreCard.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.theNewCone.cricketScoreCard.match.CricketCardUtils;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;
import com.theNewCone.cricketScoreCard.utils.database.StatisticsDBHandler;


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

	private static final String ARG_CC_UTILS = "CricketCardUtils";

	public StatisticsIntentService() {
		super("StatisticsIntentService");
	}

	public void startActionStoreMatchStatistics(Context context, CricketCardUtils ccUtils) {
		Intent intent = new Intent(context, StatisticsIntentService.class);
		intent.setAction(ACTION_STORE_MATCH_STATS);
		intent.putExtra(ARG_CC_UTILS, CommonUtils.convertToJSON(ccUtils));
		context.startService(intent);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent != null) {
			final String action = intent.getAction();
			if(action != null) {
				switch (action) {
					case ACTION_STORE_MATCH_STATS:
						final CricketCardUtils ccUtils = CommonUtils.convertToCCUtils(intent.getStringExtra(ARG_CC_UTILS));
						storeMatchStatistics(ccUtils);
						break;
				}
			}
		}
	}

	private void storeMatchStatistics(CricketCardUtils ccUtils) {
		StatisticsDBHandler sdbHandler = new StatisticsDBHandler(this);
		sdbHandler.addMatchStats(ccUtils);
	}
}
