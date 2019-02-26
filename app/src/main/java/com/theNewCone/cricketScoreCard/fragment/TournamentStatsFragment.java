package com.theNewCone.cricketScoreCard.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.adapter.SimpleListAdapter;
import com.theNewCone.cricketScoreCard.enumeration.StatisticsType;
import com.theNewCone.cricketScoreCard.intf.ListInteractionListener;
import com.theNewCone.cricketScoreCard.tournament.Tournament;

import java.util.ArrayList;
import java.util.List;

public class TournamentStatsFragment extends Fragment implements ListInteractionListener {

	private Tournament tournament;

	public TournamentStatsFragment() {
	}

	public static TournamentStatsFragment newInstance(Tournament tournament) {
		TournamentStatsFragment fragment = new TournamentStatsFragment();
		fragment.tournament = tournament;

		return fragment;
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
		rcvStatsList.setAdapter(slAdapter);

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

	private void showStats(StatisticsType statsType) {
		String fragmentTag;
		fragmentTag = TournamentPlayerStats.class.getSimpleName();
		if (getFragmentManager() != null) {
			getFragmentManager().beginTransaction()
					.replace(R.id.frame_container, TournamentPlayerStats.newInstance(tournament.getId(), statsType), fragmentTag)
					.addToBackStack(fragmentTag)
					.commit();
		}
	}
}
