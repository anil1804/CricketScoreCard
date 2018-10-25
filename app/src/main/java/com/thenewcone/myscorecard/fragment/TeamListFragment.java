package com.thenewcone.myscorecard.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.adapter.TeamViewAdapter;
import com.thenewcone.myscorecard.intf.ListInteractionListener;
import com.thenewcone.myscorecard.match.Team;
import com.thenewcone.myscorecard.utils.database.DatabaseHandler;
import com.thenewcone.myscorecard.viewModel.TeamViewModel;

import java.util.List;

public class TeamListFragment extends Fragment
    implements ListInteractionListener, View.OnClickListener {

    private TeamViewModel teamViewModel;
    private static boolean isMultiSelect = false;

    Button btnTeamSelectOk, btnTeamSelectCancel, btnCancel;

    public TeamListFragment() {
    }

    public static TeamListFragment newInstance(boolean getMultipleTeams) {
    	isMultiSelect = getMultipleTeams;
        return new TeamListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getActivity() !=  null)
            teamViewModel = ViewModelProviders.of(getActivity()).get(TeamViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_list, container, false);

		btnTeamSelectOk = view.findViewById(R.id.btnTeamSelectOK);
		btnTeamSelectCancel = view.findViewById(R.id.btnTeamSelectCancel);
		btnCancel = view.findViewById(R.id.btnCancel);
		btnTeamSelectOk.setOnClickListener(this);
		btnTeamSelectCancel.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

        // Set the adapter
        Context context = view.getContext();
        List<Team> teamList = getTeamList();

        RecyclerView recyclerView = view.findViewById(R.id.rcvTeamList);
        if(teamList.size() > 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new TeamViewAdapter(teamList, this, isMultiSelect));
        } else {
            recyclerView.setVisibility(View.GONE);
			view.findViewById(R.id.llTeamSelectButtons).setVisibility(View.GONE);
            view.findViewById(R.id.llNoTeams).setVisibility(View.VISIBLE);
        }

        return view;
    }

    private List<Team> getTeamList() {
        DatabaseHandler dbHandler = new DatabaseHandler(getContext());
        return dbHandler.getTeams(null, -1);
    }

    @Override
    public void onListFragmentInteraction(Object selObject) {
        teamViewModel.selectTeam((Team) selObject);
        gotoCallingFragment();
    }

	@Override
	public void onListFragmentMultiSelect(List<Object> selItemList) {
		teamViewModel.selectTeamList(selItemList);
	}

	private void gotoCallingFragment() {
		if(getActivity() != null)
			getActivity().onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
			case R.id.btnTeamSelectCancel:
			case R.id.btnTeamSelectOK:
            	gotoCallingFragment();
                break;

        }
    }
}
