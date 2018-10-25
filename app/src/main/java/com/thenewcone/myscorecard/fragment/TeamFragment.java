package com.thenewcone.myscorecard.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.intf.DialogItemClickListener;
import com.thenewcone.myscorecard.match.Team;
import com.thenewcone.myscorecard.utils.database.DatabaseHandler;
import com.thenewcone.myscorecard.viewModel.TeamViewModel;

import java.util.List;

public class TeamFragment extends Fragment
    implements View.OnClickListener, DialogItemClickListener {

    Button btnSaveTeam, btnDeleteTeam, btnReset;
    EditText etTeamName, etShortName;

    String[] teams;
    List<Team> teamList;
    Team selTeam;

    public TeamFragment() {
		setHasOptionsMenu(true);
    }

    public static TeamFragment newInstance() {
        return new TeamFragment();
    }

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if(getActivity() != null) {
			TeamViewModel model = ViewModelProviders.of(getActivity()).get(TeamViewModel.class);
			model.getSelectedTeam().observe(this, new Observer<Team>() {
				@Override
				public void onChanged(@Nullable Team selTeam) {
					if (selTeam != null) {
						TeamFragment.this.selTeam = selTeam;
						populateData();
					}
				}
			});
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_fragment_team, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_getTeamList:
				if(getActivity() != null) {
					getTeams();

					String fragmentTag = TeamListFragment.class.getSimpleName();
					TeamListFragment fragment = TeamListFragment.newInstance();

					getActivity().getSupportFragmentManager().beginTransaction()
							.replace(R.id.frame_container, fragment, fragmentTag)
							.addToBackStack(fragmentTag)
							.commit();
				}
				break;

			case R.id.menu_updatePlayers:
				Toast.makeText(getContext(), "Show Player List", Toast.LENGTH_SHORT).show();
				break;
		}

		return true;
	}


	@Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
        View theView = inflater.inflate(R.layout.content_team, container, false);

        etTeamName = theView.findViewById(R.id.etTeamName);
        etShortName = theView.findViewById(R.id.etTeamShortName);

        btnSaveTeam = theView.findViewById(R.id.btnSaveTeam);
        btnDeleteTeam = theView.findViewById(R.id.btnDeleteTeam);
        btnReset = theView.findViewById(R.id.btnResetData);

        btnSaveTeam.setOnClickListener(this);
        btnDeleteTeam.setOnClickListener(this);
        btnReset.setOnClickListener(this);

        return theView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSaveTeam:
            	saveTeam();
                break;

            case R.id.btnDeleteTeam:
            	deleteTeam();
                break;

			case R.id.btnResetData:
				selTeam = null;
				populateData();
				break;
        }
    }

	private void populateData() {
    	if(selTeam != null) {
			etTeamName.setText(selTeam.getName());
			etShortName.setText(selTeam.getShortName());

			btnDeleteTeam.setVisibility(View.VISIBLE);
		} else {
			etTeamName.setText("");
			etShortName.setText("");

			btnDeleteTeam.setVisibility(View.GONE);
		}
	}

    public void getTeams() {
        DatabaseHandler dbHandler = new DatabaseHandler(getContext());
        teamList = dbHandler.getTeams(null, -1);

        teams = new String[teamList.size()];
        int i=0;
        for(Team team : teamList)
            teams[i] = team.getName();
    }

    @Override
    public void onItemSelect(String enumType, String value, int position) {
        selTeam = teamList.get(position);
    }

    private void saveTeam() {
		String teamName = etTeamName.getText().toString();
		String shortName = etShortName.getText().toString();
		DatabaseHandler dbh = new DatabaseHandler(getContext());

		Team tempTeam = new Team(teamName, shortName);
		if(selTeam != null)
			tempTeam.setId(selTeam.getId());

		int rowID = dbh.upsertTeam(tempTeam);

		if(rowID == dbh.CODE_NEW_TEAM_DUP_RECORD) {
			Toast.makeText(getContext(), "Team with same name already exists. Choose a different name.", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getContext(), "Team created successfully.", Toast.LENGTH_SHORT).show();
			tempTeam.setId(rowID);
			selTeam = tempTeam;
			populateData();
		}
	}

	private void deleteTeam() {
    	DatabaseHandler dbh = new DatabaseHandler(getContext());
    	boolean success = dbh.deleteTeam(selTeam.getId());

    	if(success) {
			Toast.makeText(getContext(), "Team deleted successfully", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getContext(), "Team deletion failed", Toast.LENGTH_SHORT).show();
		}
	}
}
