package com.theNewCone.cricketScoreCard.fragment;

import android.content.Intent;
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
import com.theNewCone.cricketScoreCard.activity.HomeActivity;
import com.theNewCone.cricketScoreCard.adapter.ScheduleViewAdapter;
import com.theNewCone.cricketScoreCard.comparator.GroupScheduleComparator;
import com.theNewCone.cricketScoreCard.comparator.MatchInfoComparator;
import com.theNewCone.cricketScoreCard.intf.ItemClickListener;
import com.theNewCone.cricketScoreCard.tournament.Group;
import com.theNewCone.cricketScoreCard.tournament.MatchInfo;
import com.theNewCone.cricketScoreCard.tournament.Tournament;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TournamentScheduleTabFragment extends Fragment
		implements ItemClickListener {
	private static final String ARG_TOURNAMENT = "Tournament";

	Tournament tournament = null;
	List<MatchInfo> matchInfoList = null;

	public TournamentScheduleTabFragment() {
	}

	public static TournamentScheduleTabFragment newInstance(Tournament tournament) {
		TournamentScheduleTabFragment fragment = new TournamentScheduleTabFragment();
		Bundle args = new Bundle();
		args.putSerializable(ARG_TOURNAMENT, tournament);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_tournament_tab_schedule, container, false);

		if (getArguments() != null) {
			tournament = (Tournament) getArguments().getSerializable(ARG_TOURNAMENT);
		}

		if (tournament != null) {
			List<Group> groupList = tournament.getGroupList();

			if (groupList != null) {
				Collections.sort(groupList, new GroupScheduleComparator());

				matchInfoList = new ArrayList<>();
				for (Group group : groupList) {
					matchInfoList.addAll(group.getMatchInfoList());
				}

				Collections.sort(matchInfoList, new MatchInfoComparator());

				RecyclerView rcvScheduleList = rootView.findViewById(R.id.rcvScheduleList);
				rcvScheduleList.setLayoutManager(new LinearLayoutManager(getContext()));
				rcvScheduleList.setHasFixedSize(false);

				ScheduleViewAdapter adapter = new ScheduleViewAdapter(getContext(), matchInfoList, false);
				adapter.setItemClickListener(this);
				rcvScheduleList.setAdapter(adapter);

				LinearLayoutManager llm = new LinearLayoutManager(getContext());
				llm.setOrientation(LinearLayoutManager.VERTICAL);
				rcvScheduleList.setLayoutManager(llm);

				rcvScheduleList.setItemAnimator(new DefaultItemAnimator());
			}
		}

		return rootView;
	}

	@Override
	public void onItemClick(View view, int position) {
		MatchInfo selMatch = matchInfoList.get(position);
		Intent intent = new Intent(getContext(), HomeActivity.class);
		intent.putExtra(HomeActivity.ARG_TOURNAMENT, tournament);
		intent.putExtra(HomeActivity.ARG_MATCH_INFO, selMatch);
		startActivity(intent);
	}
}
