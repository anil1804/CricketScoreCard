package com.theNewCone.cricketScoreCard.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.adapter.PointsTableViewAdapter;
import com.theNewCone.cricketScoreCard.comparator.GroupScheduleComparator;
import com.theNewCone.cricketScoreCard.tournament.Group;
import com.theNewCone.cricketScoreCard.tournament.Tournament;

import java.util.Collections;
import java.util.List;

public class TournamentHomePointsTableFragment extends Fragment {
	Tournament tournament;

	public TournamentHomePointsTableFragment() {
		// Required empty public constructor
	}

	public static TournamentHomePointsTableFragment newInstance(Tournament tournament) {
		TournamentHomePointsTableFragment fragment = new TournamentHomePointsTableFragment();
		fragment.tournament = tournament;

		return fragment;
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View theView = inflater.inflate(R.layout.fragment_tournament_points_table, container, false);

		if (tournament != null) {
			List<Group> groupList = tournament.getGroupList();

			if (groupList != null) {
				Collections.sort(groupList, new GroupScheduleComparator());

				RecyclerView rcvPointsList = theView.findViewById(R.id.rcvPointsTableList);
				rcvPointsList.setLayoutManager(new LinearLayoutManager(getContext()));
				rcvPointsList.setHasFixedSize(false);

				PointsTableViewAdapter adapter = new PointsTableViewAdapter(getContext(), groupList);
				rcvPointsList.setAdapter(adapter);

				LinearLayoutManager llm = new LinearLayoutManager(getContext());
				llm.setOrientation(LinearLayoutManager.VERTICAL);
				rcvPointsList.setLayoutManager(llm);

				rcvPointsList.setItemAnimator(new DefaultItemAnimator());
			}
		}

		return theView;
	}

}
