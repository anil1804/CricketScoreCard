package com.theNewCone.cricketScoreCard.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.tournament.Tournament;

public class TournamentStatsFragment extends Fragment {

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
		return inflater.inflate(R.layout.fragment_tournament_stats, container, false);
	}

}
