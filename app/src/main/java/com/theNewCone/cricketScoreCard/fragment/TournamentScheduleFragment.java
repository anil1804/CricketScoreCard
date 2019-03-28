package com.theNewCone.cricketScoreCard.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.activity.TournamentHomeActivity;
import com.theNewCone.cricketScoreCard.adapter.ScheduleSetupViewAdapter;
import com.theNewCone.cricketScoreCard.enumeration.TournamentFormat;
import com.theNewCone.cricketScoreCard.intf.DrawerController;
import com.theNewCone.cricketScoreCard.tournament.Group;
import com.theNewCone.cricketScoreCard.tournament.MatchInfo;
import com.theNewCone.cricketScoreCard.tournament.Tournament;
import com.theNewCone.cricketScoreCard.utils.TournamentUtils;
import com.theNewCone.cricketScoreCard.utils.database.GroupsDBHandler;
import com.theNewCone.cricketScoreCard.utils.database.MatchInfoDBHandler;
import com.theNewCone.cricketScoreCard.utils.database.TournamentDBHandler;

import java.util.List;

public class TournamentScheduleFragment extends Fragment
		implements View.OnClickListener {

	RecyclerView rcvScheduleList;
	private Tournament tournament;
	private Group currentGroup;
	private int currentGroupIndex;

	public TournamentScheduleFragment() {
	}

	public static TournamentScheduleFragment newInstance(Tournament tournament, int groupIndex) {
		TournamentScheduleFragment fragment = new TournamentScheduleFragment();
		fragment.tournament = tournament;
		fragment.currentGroupIndex = groupIndex;

		return fragment;
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View theView = inflater.inflate(R.layout.fragment_tournament_set_schedule, container, false);

		//Back pressed Logic for fragment
		theView.setFocusableInTouchMode(true);
		theView.requestFocus();
		theView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						if (getFragmentManager() != null) {
							InformationDialog infoDialog = InformationDialog.newInstance("Invalid Action",
									"Clicking on Back is not possible.\n\n Please confirm schedule to complete Tournament creation.");
							infoDialog.show(getFragmentManager(), "ExitGroupSetupDialog");
						}
						return true;
					}
				}
				return false;
			}
		});

		if (getActivity() != null) {
			DrawerController drawerController = (DrawerController) getActivity();
			drawerController.disableAllDrawerMenuItems();
			drawerController.enableDrawerMenuItem(R.id.menu_help);
			getActivity().setTitle(getResources().getString(R.string.title_fragment_tournament_schedule));
		}

		Button btnReschedule, btnNext, btnOK, btnPrevious;
		boolean isFirstGroup, isLastGroup;

		btnReschedule = theView.findViewById(R.id.btnTournamentScheduleRegenerate);
		btnNext = theView.findViewById(R.id.btnTournamentScheduleNext);
		btnPrevious = theView.findViewById(R.id.btnTournamentSchedulePrevious);
		btnOK = theView.findViewById(R.id.btnTournamentScheduleConfirm);

		btnReschedule.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		btnOK.setOnClickListener(this);
		btnPrevious.setOnClickListener(this);

		rcvScheduleList = theView.findViewById(R.id.rcvScheduleList);
		rcvScheduleList.setHasFixedSize(false);

		currentGroup = tournament.getGroupList().get(currentGroupIndex);
		isFirstGroup = currentGroupIndex == 0;
		isLastGroup = currentGroupIndex == (tournament.getGroupList().size() - 1);

		if (getActivity() != null) {
			DrawerController drawerController = (DrawerController) getActivity();
			drawerController.disableAllDrawerMenuItems();
			drawerController.enableDrawerMenuItem(R.id.menu_help);
			getActivity().setTitle(currentGroup.getName() + " " + getResources().getString(R.string.title_fragment_tournament_schedule));
		}

		if (isLastGroup) {
			btnOK.setVisibility(View.VISIBLE);
			btnNext.setVisibility(View.GONE);
		} else {
			btnOK.setVisibility(View.GONE);
			btnNext.setVisibility(View.VISIBLE);
		}

		if (isFirstGroup) {
			btnPrevious.setVisibility(View.GONE);
		} else {
			btnPrevious.setVisibility(View.VISIBLE);
		}

		layoutView(false);

		if (tournament.getFormat() == TournamentFormat.BILATERAL)
			theView.findViewById(R.id.btnTournamentScheduleConfirm).performClick();

		return theView;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btnTournamentScheduleRegenerate:
				layoutView(true);
				break;

			case R.id.btnTournamentSchedulePrevious:
				showScheduleView(currentGroupIndex - 1);
				break;

			case R.id.btnTournamentScheduleNext:
				saveMatchInfo();
				saveGroupInfo();
				showScheduleView(currentGroupIndex + 1);
				break;

			case R.id.btnTournamentScheduleConfirm:
				confirmTournament();
				break;
		}
	}

	private void confirmTournament() {
		saveMatchInfo();
		saveGroupInfo();
		savePointsDataInfo();
		showTournamentHome();
	}

	private void saveGroupInfo() {
		currentGroup.setScheduled(true);

		tournament.updateGroup(currentGroup);
		tournament.checkScheduled();
		new TournamentDBHandler(getContext()).updateTournament(tournament);
	}

	private void layoutView(boolean isRegenerate) {
		TournamentUtils tournamentUtils = new TournamentUtils(getContext());
		if (getContext() != null) {
			if (isRegenerate
					|| currentGroup.getMatchInfoList() == null
					|| currentGroup.getMatchInfoList().size() == 0) {
				currentGroup = tournamentUtils.createSchedule(currentGroup, tournament.getFormat(),
						currentGroup.getStage());
			}

			if (currentGroup.getMatchInfoList() != null) {
				ScheduleSetupViewAdapter adapter = (ScheduleSetupViewAdapter) rcvScheduleList.getAdapter();

				if (adapter != null) {
					for (int i = 0; i < adapter.getItemCount(); i++) {
						adapter.notifyItemRemoved(i);
					}
				}

				rcvScheduleList.setLayoutManager(new LinearLayoutManager(getContext()));
				adapter = new ScheduleSetupViewAdapter(currentGroup.getMatchInfoList());
				rcvScheduleList.setAdapter(adapter);

				LinearLayoutManager llm = new LinearLayoutManager(getContext());
				llm.setOrientation(LinearLayoutManager.VERTICAL);
				rcvScheduleList.setLayoutManager(llm);

				rcvScheduleList.setItemAnimator(new DefaultItemAnimator());
			}
		}
	}

	private void showScheduleView(int groupIndex) {
		if (getActivity() != null) {
			FragmentManager fragMgr = getActivity().getSupportFragmentManager();
			String fragmentTag = TournamentScheduleFragment.class.getSimpleName() + " " + currentGroup.getName();

			fragMgr.beginTransaction()
					.replace(R.id.frame_container, TournamentScheduleFragment.newInstance(tournament, groupIndex), fragmentTag)
					.commit();
		}
	}

	private void showTournamentHome() {
		Intent intent = new Intent(getContext(), TournamentHomeActivity.class);
		intent.putExtra(TournamentHomeActivity.ARG_TOURNAMENT_ID, tournament.getId());
		intent.putExtra(TournamentHomeActivity.ARG_TOURNAMENT_FORMAT, tournament.getFormat());
		startActivity(intent);
	}

	private void saveMatchInfo() {
		List<MatchInfo> matchInfoList = currentGroup.getMatchInfoList();
		if (currentGroup.getId() == 0) {
			int groupID = new GroupsDBHandler(getContext()).updateGroup(tournament.getId(), currentGroup);
			currentGroup.setId(groupID);
		}

		for (int i = 0; i < matchInfoList.size(); i++) {
			MatchInfo matchInfo = matchInfoList.get(i);
			int rowID = new MatchInfoDBHandler(getContext()).addNewMatchInfo(currentGroup.getId(), matchInfo);
			matchInfo.setId(rowID);
			currentGroup.updateMatchInfo(matchInfo);
		}
	}

	private void savePointsDataInfo() {
		tournament = new TournamentUtils(getContext()).storePointsData(tournament);
	}
}
