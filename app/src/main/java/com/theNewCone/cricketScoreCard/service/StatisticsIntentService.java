package com.theNewCone.cricketScoreCard.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.theNewCone.cricketScoreCard.match.CricketCardUtils;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;
import com.theNewCone.cricketScoreCard.utils.database.StatisticsDBHandler;
import com.theNewCone.cricketScoreCard.utils.database.TournamentDBHandler;


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
	private static final String ARG_TOURNAMENT_ID = "isTournament";

	public StatisticsIntentService() {
		super("StatisticsIntentService");
	}

	public void startActionStoreMatchStatistics(Context context, CricketCardUtils ccUtils) {

		int tournamentID = new TournamentDBHandler(this).getTournamentIDUsingMatchID(ccUtils.getMatchID());

		Intent intent = new Intent(context, StatisticsIntentService.class);
		intent.setAction(ACTION_STORE_MATCH_STATS);
		intent.putExtra(ARG_CC_UTILS, CommonUtils.convertToJSON(ccUtils));
		intent.putExtra(ARG_TOURNAMENT_ID, tournamentID);
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
						final int tournamentID = intent.getIntExtra(ARG_TOURNAMENT_ID, 0);
						storeMatchStatistics(ccUtils, tournamentID);
						break;
				}
			}
		}
	}

	private void storeMatchStatistics(CricketCardUtils ccUtils, int tournamentID) {
		StatisticsDBHandler sdbHandler = new StatisticsDBHandler(this);
		sdbHandler.addPlayerStats(ccUtils, tournamentID);
	}
}
