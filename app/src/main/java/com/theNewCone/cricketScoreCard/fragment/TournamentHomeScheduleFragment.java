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
import com.theNewCone.cricketScoreCard.adapter.TournamentGroupScheduleAdapter;
import com.theNewCone.cricketScoreCard.comparator.GroupComparator;
import com.theNewCone.cricketScoreCard.comparator.GroupScheduleComparator;
import com.theNewCone.cricketScoreCard.comparator.MatchInfoComparator;
import com.theNewCone.cricketScoreCard.intf.ListInteractionListener;
import com.theNewCone.cricketScoreCard.tournament.Group;
import com.theNewCone.cricketScoreCard.tournament.MatchInfo;
import com.theNewCone.cricketScoreCard.tournament.Tournament;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TournamentHomeScheduleFragment extends Fragment
		implements ListInteractionListener {
	private static final String ARG_TOURNAMENT = "Tournament";

	Tournament tournament = null;

	public TournamentHomeScheduleFragment() {
	}

	public static TournamentHomeScheduleFragment newInstance(Tournament tournament) {
		TournamentHomeScheduleFragment fragment = new TournamentHomeScheduleFragment();
		Bundle args = new Bundle();
		args.putSerializable(ARG_TOURNAMENT, tournament);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_tournament_schedule, container, false);

		if (getArguments() != null) {
			tournament = (Tournament) getArguments().getSerializable(ARG_TOURNAMENT);
		}

		if (tournament != null && getContext() != null) {
			List<Group> groupList = tournament.getGroupList();

			if (groupList != null) {
				Collections.sort(groupList, new GroupScheduleComparator());

				Group prevGroup = groupList.get(0);
				List<Group> groupListForSchedule = new ArrayList<>();
				Group newGroup = new Group(prevGroup.getStage().enumString(), prevGroup.getStage());

				for (int i = 0; i < groupList.size(); i++) {
					Group group = groupList.get(i);

					if (prevGroup.getStage() != group.getStage()) {
						groupListForSchedule.add(newGroup);
						newGroup = new Group(group.getStage().enumString(), group.getStage());
					}

					List<MatchInfo> matchInfoList = newGroup.getMatchInfoList();
					if (matchInfoList == null)
						matchInfoList = new ArrayList<>();

					Collections.sort(matchInfoList, new MatchInfoComparator());
					matchInfoList.addAll(group.getMatchInfoList());
					newGroup.setMatchInfoList(matchInfoList);

					if (i == (groupList.size() - 1))
						groupListForSchedule.add(newGroup);
				}

				Collections.sort(groupListForSchedule, new GroupComparator());

				RecyclerView rcvScheduleList = rootView.findViewById(R.id.rcvScheduleList);
				rcvScheduleList.setLayoutManager(new LinearLayoutManager(getContext()));
				rcvScheduleList.setHasFixedSize(false);

				TournamentGroupScheduleAdapter adapter =
						new TournamentGroupScheduleAdapter(tournament.getFormat(), groupListForSchedule, getContext(), this);
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
	public void onListFragmentInteraction(Object selItem) {
		if (selItem instanceof MatchInfo) {
			MatchInfo selMatch = (MatchInfo) selItem;

			Group group = null;
			for (Group tournamentGroup : tournament.getGroupList()) {
				if (tournamentGroup.getId() == selMatch.getGroupID()) {
					group = tournamentGroup;
					break;
				}
			}

			Intent intent = new Intent(getContext(), HomeActivity.class);
			intent.putExtra(HomeActivity.ARG_TOURNAMENT, tournament);
			intent.putExtra(HomeActivity.ARG_GROUP, group);
			intent.putExtra(HomeActivity.ARG_MATCH_INFO, selMatch);
			startActivity(intent);
		}
	}

	@Override
	public void onListFragmentMultiSelect(Object selItem, boolean removeItem) {

	}
}
