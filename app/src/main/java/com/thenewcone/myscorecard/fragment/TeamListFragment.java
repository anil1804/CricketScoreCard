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

    public TeamListFragment() {
    }

    public static TeamListFragment newInstance() {
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

        // Set the adapter
        Context context = view.getContext();
        List<Team> teamList = getTeamList();

        RecyclerView recyclerView = view.findViewById(R.id.rcvTeamList);
        if(teamList.size() > 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new TeamViewAdapter(teamList, this));
        } else {
            recyclerView.setVisibility(View.GONE);
            view.findViewById(R.id.tvNoTeams).setVisibility(View.VISIBLE);
        }

        view.findViewById(R.id.btnCancel).setOnClickListener(this);

        return view;
    }

    private List<Team> getTeamList() {
        DatabaseHandler dbHandler = new DatabaseHandler(getContext());
        return dbHandler.getTeams(null, -1);
    }

    @Override
    public void onListFragmentInteraction(Object selObject) {
        teamViewModel.selectTeam((Team) selObject);
        gotoTeamFragment();
    }

    private void gotoTeamFragment() {
        if(getActivity() != null) {
            String fragmentTag = TeamFragment.class.getSimpleName();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, TeamFragment.newInstance(), fragmentTag)
                    .commit();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                gotoTeamFragment();
                break;
        }
    }
}
