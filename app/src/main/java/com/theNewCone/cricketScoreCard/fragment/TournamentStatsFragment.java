package com.theNewCone.cricketScoreCard.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.activity.TournamentPlayerStats;
import com.theNewCone.cricketScoreCard.adapter.SimpleListAdapter;
import com.theNewCone.cricketScoreCard.enumeration.StatisticsType;
import com.theNewCone.cricketScoreCard.intf.ListInteractionListener;
import com.theNewCone.cricketScoreCard.tournament.Tournament;

import java.util.ArrayList;
import java.util.List;

public class TournamentStatsFragment extends Fragment implements ListInteractionListener {

	private final String ARG_TOURNAMENT_ID = "TournamentID";
	private int tournamentID = 0;

	public TournamentStatsFragment() {
	}

	public static TournamentStatsFragment newInstance(Tournament tournament) {
		TournamentStatsFragment fragment = new TournamentStatsFragment();
		fragment.tournamentID = tournament.getId();

		return fragment;
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		saveBundle(outState);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View theView = inflater.inflate(R.layout.fragment_tournament_stats, container, false);
		RecyclerView rcvStatsList = theView.findViewById(R.id.rcvStatsList);

		List<Object> typesOfStatsList = new ArrayList<>();
		typesOfStatsList.add(StatisticsType.HIGHEST_SCORE);
		typesOfStatsList.add(StatisticsType.TOTAL_RUNS);
		typesOfStatsList.add(StatisticsType.HUNDREDS_FIFTIES);
		typesOfStatsList.add(StatisticsType.BOWLING_BEST_FIGURES);
		typesOfStatsList.add(StatisticsType.ECONOMY);
		typesOfStatsList.add(StatisticsType.TOTAL_WICKETS);
		typesOfStatsList.add(StatisticsType.CATCHES);
		typesOfStatsList.add(StatisticsType.STUMPING);

		SimpleListAdapter slAdapter = new SimpleListAdapter(typesOfStatsList, this);
		rcvStatsList.setLayoutManager(new LinearLayoutManager(getContext()));
		rcvStatsList.setAdapter(slAdapter);

		if (savedInstanceState != null && tournamentID == 0)
			loadFromBundle(savedInstanceState);

		return theView;
	}

	@Override
	public void onListFragmentInteraction(Object selItem) {
		StatisticsType statsType = (StatisticsType) selItem;
		showStats(statsType);
	}

	@Override
	public void onListFragmentMultiSelect(Object selItem, boolean removeItem) {

	}

	private void saveBundle(Bundle outState) {
		outState.putSerializable(ARG_TOURNAMENT_ID, tournamentID);
	}

	private void loadFromBundle(Bundle savedInstanceState) {
		tournamentID = savedInstanceState.getInt(ARG_TOURNAMENT_ID);
	}

	private void showStats(StatisticsType statsType) {
		Intent showStatsIntent = new Intent(getContext(), TournamentPlayerStats.class);
		showStatsIntent.putExtra(TournamentPlayerStats.PARAM_TOURNAMENT_ID, tournamentID);
		showStatsIntent.putExtra(TournamentPlayerStats.PARAM_STATS_TYPE, statsType);
		startActivity(showStatsIntent);
	}
}
