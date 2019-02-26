package com.theNewCone.cricketScoreCard.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.adapter.TournamentPlayerStatsAdapter;
import com.theNewCone.cricketScoreCard.enumeration.StatisticsType;
import com.theNewCone.cricketScoreCard.statistics.BatsmanData;
import com.theNewCone.cricketScoreCard.statistics.BowlerData;
import com.theNewCone.cricketScoreCard.statistics.PlayerData;
import com.theNewCone.cricketScoreCard.utils.database.StatisticsDBHandler;

import java.util.Collections;
import java.util.List;


public class TournamentPlayerStats extends Fragment {

	private static final String PARAM_TOURNAMENT_ID = "TournamentID";
	private static final String PARAM_STATS_TYPE = "StatsType";
	List<BatsmanData> batsmanDataList = null;
	List<BowlerData> bowlerDataList = null;
	List<PlayerData> playerDataList = null;
	private int tournamentID;
	private StatisticsType statsType;

	public TournamentPlayerStats() {
		// Required empty public constructor
	}

	public static TournamentPlayerStats newInstance(int tournamentID, StatisticsType statsType) {
		TournamentPlayerStats fragment = new TournamentPlayerStats();
		Bundle args = new Bundle();
		args.putInt(PARAM_TOURNAMENT_ID, tournamentID);
		args.putSerializable(PARAM_STATS_TYPE, statsType);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			Bundle args = getArguments();
			tournamentID = args.getInt(PARAM_TOURNAMENT_ID, 0);
			statsType = (StatisticsType) args.get(PARAM_STATS_TYPE);
		}
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View theView = inflater.inflate(R.layout.fragment_tournament_player_stats, container, false);
		RecyclerView rcvStatsPlayersList = theView.findViewById(R.id.rcvStatsPlayersList);

		TournamentPlayerStatsAdapter tpsAdapter = null;
		switch (statsType) {
			case HIGHEST_SCORE:
			case TOTAL_RUNS:
			case HUNDREDS_FIFTIES:
				if (batsmanDataList == null)
					batsmanDataList = getBatsmanStats();

				tpsAdapter = new TournamentPlayerStatsAdapter(getContext(), batsmanDataList, statsType);
				break;

			case BOWLING_BEST_FIGURES:
			case TOTAL_WICKETS:
			case ECONOMY:
				if (bowlerDataList == null)
					bowlerDataList = getBowlerStats();

				tpsAdapter = new TournamentPlayerStatsAdapter(getContext(), bowlerDataList, statsType);
				break;

			case CATCHES:
			case STUMPING:
				if (playerDataList == null)
					playerDataList = getFielderStats();

				tpsAdapter = new TournamentPlayerStatsAdapter(getContext(), playerDataList, statsType);
				break;
		}

		if (rcvStatsPlayersList != null)
			rcvStatsPlayersList.setAdapter(tpsAdapter);

		return theView;
	}

	private List<BatsmanData> getBatsmanStats() {
		StatisticsDBHandler statisticsDBHandler = new StatisticsDBHandler(getContext());

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
				break;
		}

		return batsmanDataList;
	}

	private List<BowlerData> getBowlerStats() {
		StatisticsDBHandler statisticsDBHandler = new StatisticsDBHandler(getContext());

		switch (statsType) {
			case BOWLING_BEST_FIGURES:
				bowlerDataList = statisticsDBHandler.getBowlerStatistics(tournamentID);
				Collections.sort(bowlerDataList, BowlerData.Sort.ByBestFigures.descending());
				break;

			case ECONOMY:
				bowlerDataList = statisticsDBHandler.getBowlerStatistics(tournamentID);
				Collections.sort(bowlerDataList, BowlerData.Sort.ByEconomy.descending());
				break;

			case TOTAL_WICKETS:
				bowlerDataList = statisticsDBHandler.getBowlerStatistics(tournamentID);
				Collections.sort(bowlerDataList, BowlerData.Sort.ByTotalWickets.descending());
				break;
		}

		return bowlerDataList;
	}

	private List<PlayerData> getFielderStats() {
		StatisticsDBHandler statisticsDBHandler = new StatisticsDBHandler(getContext());

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
