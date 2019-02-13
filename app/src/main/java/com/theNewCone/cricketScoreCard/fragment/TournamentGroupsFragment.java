package com.theNewCone.cricketScoreCard.fragment;

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

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.adapter.GroupViewAdapter;
import com.theNewCone.cricketScoreCard.intf.ConfirmationDialogClickListener;
import com.theNewCone.cricketScoreCard.intf.DrawerController;
import com.theNewCone.cricketScoreCard.tournament.Group;
import com.theNewCone.cricketScoreCard.tournament.Tournament;
import com.theNewCone.cricketScoreCard.utils.TournamentUtils;
import com.theNewCone.cricketScoreCard.utils.database.GroupsDBHandler;
import com.theNewCone.cricketScoreCard.utils.database.TournamentDBHandler;

import java.util.List;

public class TournamentGroupsFragment extends Fragment
		implements View.OnClickListener, ConfirmationDialogClickListener {

	private static final int CONFIRMATION_CODE_EXIT = 1;
	View theView;
	RecyclerView rcvGroupsList;
	private Tournament tournament;

	public TournamentGroupsFragment() {
		// Required empty public constructor
	}

	public static TournamentGroupsFragment newInstance(Tournament tournament) {
		TournamentGroupsFragment fragment = new TournamentGroupsFragment();
		fragment.tournament = tournament;

		return fragment;
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		theView = inflater.inflate(R.layout.fragment_tournament_groups, container, false);

		//Back pressed Logic for fragment
		theView.setFocusableInTouchMode(true);
		theView.requestFocus();
		final ConfirmationDialogClickListener listener = this;
		theView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						if (getFragmentManager() != null) {
							ConfirmationDialog confirmationDialog = ConfirmationDialog.newInstance(CONFIRMATION_CODE_EXIT,
									"Exit", "Do you want to exit confirming the Groups?" +
											"\n\nYou can redo it later by opening an 'ONGOING' Tournaments from the home screen.");
							confirmationDialog.setConfirmationClickListener(listener);
							confirmationDialog.show(getFragmentManager(), "ExitGroupSetupDialog");
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
			getActivity().setTitle(getResources().getString(R.string.title_fragment_tournament_groups));
		}

		theView.findViewById(R.id.btnTournamentGroupsOK).setOnClickListener(this);
		theView.findViewById(R.id.btnTournamentGroupsRegenerate).setOnClickListener(this);

		rcvGroupsList = theView.findViewById(R.id.rcvGroupsList);
		rcvGroupsList.setHasFixedSize(false);

		layoutView();

		return theView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnTournamentGroupsRegenerate:
				layoutView();
				break;

			case R.id.btnTournamentGroupsOK:
				new TournamentDBHandler(getContext()).updateTournament(tournament);
				saveTournamentGroups();
				showScheduleView();
				break;
		}
	}

	@Override
	public void onConfirmationClick(int confirmationCode, boolean accepted) {
		switch (confirmationCode) {
			case CONFIRMATION_CODE_EXIT:
				if (accepted) {
					goHome();
				}
				break;
		}
	}

	private void layoutView() {
		TournamentUtils tournamentUtils = new TournamentUtils(getContext());
		tournament = tournamentUtils.createInitialGroups(tournament);

		if (tournament.getGroupList() != null && getContext() != null) {
			GroupViewAdapter adapter = (GroupViewAdapter) rcvGroupsList.getAdapter();

			if (adapter != null) {
				for (int i = 0; i < adapter.getItemCount(); i++) {
					adapter.notifyItemRemoved(i);
				}
			}

			rcvGroupsList.setLayoutManager(new LinearLayoutManager(getContext()));
			adapter = new GroupViewAdapter(tournament.getGroupList(), getContext());
			rcvGroupsList.setAdapter(adapter);

			LinearLayoutManager llm = new LinearLayoutManager(getContext());
			llm.setOrientation(LinearLayoutManager.VERTICAL);
			rcvGroupsList.setLayoutManager(llm);

			rcvGroupsList.setItemAnimator(new DefaultItemAnimator());
		}
	}

	private void showScheduleView() {
		if (getActivity() != null) {
			FragmentManager fragMgr = getActivity().getSupportFragmentManager();
			String fragmentTag = TournamentScheduleFragment.class.getSimpleName();

			fragMgr.beginTransaction()
					.replace(R.id.frame_container, TournamentScheduleFragment.newInstance(tournament, 0), fragmentTag)
					.commit();
		}
	}

	private void goHome() {
		if (getActivity() != null && getFragmentManager() != null && getFragmentManager().getBackStackEntryCount() > 0) {
			int backStackCount = getFragmentManager().getBackStackEntryCount();
			for (int i = backStackCount - 1; i >= 0; i--) {
				FragmentManager.BackStackEntry entry = getFragmentManager().getBackStackEntryAt(i);
				if (HomeFragment.class.getSimpleName().equals(entry.getName())) {
					getActivity().onBackPressed();
				} else {
					getFragmentManager().popBackStack();
				}
			}
		}
	}

	private void saveTournamentGroups() {
		List<Group> groupList = tournament.getGroupList();
		for (int i = 0; i < groupList.size(); i++) {
			Group group = groupList.get(i);
			int groupId = new GroupsDBHandler(getContext()).updateGroup(tournament.getId(), group);
			group.setId(groupId);
			tournament.updateGroup(group);
		}
	}
}
