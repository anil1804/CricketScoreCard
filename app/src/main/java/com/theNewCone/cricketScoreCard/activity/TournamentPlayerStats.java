package com.theNewCone.cricketScoreCard.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.adapter.TournamentPlayerStatsAdapter;
import com.theNewCone.cricketScoreCard.enumeration.StatisticsType;
import com.theNewCone.cricketScoreCard.statistics.BatsmanData;
import com.theNewCone.cricketScoreCard.statistics.BowlerData;
import com.theNewCone.cricketScoreCard.statistics.PlayerData;
import com.theNewCone.cricketScoreCard.utils.database.StatisticsDBHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TournamentPlayerStats extends Activity {

	public static final String PARAM_TOURNAMENT_ID = "TournamentID";
	public static final String PARAM_STATS_TYPE = "StatsType";

	List<BatsmanData> batsmanDataList = null;
	List<BowlerData> bowlerDataList = null;
	List<PlayerData> playerDataList = null;
	private int tournamentID;
	private StatisticsType statsType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tournament_player_stats);

		Bundle args = getIntent().getExtras();
		if (args != null) {
			tournamentID = args.getInt(PARAM_TOURNAMENT_ID, 0);
			statsType = (StatisticsType) args.get(PARAM_STATS_TYPE);
		}
		RecyclerView rcvStatsPlayersList = findViewById(R.id.rcvStatsPlayersList);

		TournamentPlayerStatsAdapter tpsAdapter = null;
		switch (statsType) {
			case HIGHEST_SCORE:
			case TOTAL_RUNS:
			case HUNDREDS_FIFTIES:
				if (batsmanDataList == null)
					batsmanDataList = getBatsmanStats();

				if (batsmanDataList != null && batsmanDataList.size() > 0)
					tpsAdapter = new TournamentPlayerStatsAdapter(this, batsmanDataList, statsType);
				break;

			case BOWLING_BEST_FIGURES:
			case TOTAL_WICKETS:
			case ECONOMY:
				if (bowlerDataList == null)
					bowlerDataList = getBowlerStats();

				if (bowlerDataList != null && bowlerDataList.size() > 0)
					tpsAdapter = new TournamentPlayerStatsAdapter(this, bowlerDataList, statsType);
				break;

			case CATCHES:
			case STUMPING:
				if (playerDataList == null)
					playerDataList = getFielderStats();

				if (playerDataList != null && playerDataList.size() > 0)
					tpsAdapter = new TournamentPlayerStatsAdapter(this, playerDataList, statsType);
				break;
		}

		if (tpsAdapter != null) {
			rcvStatsPlayersList.setLayoutManager(new LinearLayoutManager(this));
			rcvStatsPlayersList.setAdapter(tpsAdapter);
		} else {
			Toast.makeText(this, "No statistics to show", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	private List<BatsmanData> getBatsmanStats() {
		StatisticsDBHandler statisticsDBHandler = new StatisticsDBHandler(this);

		switch (statsType) {
			case HIGHEST_SCORE:
				batsmanDataList = statisticsDBHandler.getBatsmanStatistics(tournamentID);
				Collections.sort(batsmanDataList, BatsmanData.Sort.ByHighestScore.descending());
				break;

			case TOTAL_RUNS:
				batsmanDataList = statisticsDBHandler.getBatsmanStatistics(tournamentID);
				Collections.sort(batsmanDataList, BatsmanData.Sort.ByTotalRuns.descending());
				break;

			case HUNDREDS_FIFTIES:
				batsmanDataList = statisticsDBHandler.getBatsmanStatistics(tournamentID);
				Collections.sort(batsmanDataList, BatsmanData.Sort.ByHundreds.descending());
				List<BatsmanData> compressedList = new ArrayList<>();
				for (BatsmanData batsmanData : batsmanDataList) {
					if (batsmanData.getFifties() > 0 || batsmanData.getHundreds() > 0)
						compressedList.add(batsmanData);
					else
						break;
				}
				batsmanDataList.clear();
				batsmanDataList.addAll(compressedList);
				break;
		}

		return batsmanDataList;
	}

	private List<BowlerData> getBowlerStats() {
		StatisticsDBHandler statisticsDBHandler = new StatisticsDBHandler(this);

		switch (statsType) {
			case BOWLING_BEST_FIGURES:
				bowlerDataList = statisticsDBHandler.getBowlerStatistics(tournamentID);
				Collections.sort(bowlerDataList, BowlerData.Sort.ByBestFigures.descending());
				break;

			case ECONOMY:
				bowlerDataList = statisticsDBHandler.getBowlerStatistics(tournamentID);
				Collections.sort(bowlerDataList, BowlerData.Sort.ByEconomy);
				break;

			case TOTAL_WICKETS:
				bowlerDataList = statisticsDBHandler.getBowlerStatistics(tournamentID);
				Collections.sort(bowlerDataList, BowlerData.Sort.ByTotalWickets.descending());
				break;
		}

		return bowlerDataList;
	}

	private List<PlayerData> getFielderStats() {
		StatisticsDBHandler statisticsDBHandler = new StatisticsDBHandler(this);

		switch (statsType) {
			case CATCHES:
				playerDataList = statisticsDBHandler.getFielderStatistics(tournamentID);
				Collections.sort(playerDataList, PlayerData.Sort.ByCatches.descending());
				break;

			case STUMPING:
				playerDataList = statisticsDBHandler.getFielderStatistics(tournamentID);
				Collections.sort(playerDataList, PlayerData.Sort.ByStumping.descending());
				break;
		}

		return playerDataList;
	}
}
