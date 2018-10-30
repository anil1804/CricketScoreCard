package com.thenewcone.myscorecard.fragment;

import android.content.Intent;
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
import android.widget.Toast;

import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.activity.PlayerSelectActivity;
import com.thenewcone.myscorecard.activity.TeamSelectActivity;
import com.thenewcone.myscorecard.intf.DialogItemClickListener;
import com.thenewcone.myscorecard.match.Team;
import com.thenewcone.myscorecard.player.Player;
import com.thenewcone.myscorecard.utils.CommonUtils;
import com.thenewcone.myscorecard.utils.database.AddDBData;
import com.thenewcone.myscorecard.utils.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class TeamFragment extends Fragment
    implements View.OnClickListener, DialogItemClickListener {

	private final int REQ_CODE_TEAM_SELECT = 1;
	private final int REQ_CODE_UPDATE_PLAYERS = 2;

    Button btnSaveTeam, btnDeleteTeam, btnReset;
    EditText etTeamName, etShortName;

    List<Team> teamList;
    Team selTeam;
    List<Integer> associatedPlayers = new ArrayList<>();

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
				showTeamsSelectDialog();
				break;

			case R.id.menu_updatePlayers:
				if(selTeam != null && selTeam.getId() > 0)
					showPlayerListDialog();
				else
					Toast.makeText(getContext(), "Select/Create a team to update player list", Toast.LENGTH_SHORT).show();
				break;

			case R.id.menu_loadData:
				AddDBData addData = new AddDBData(getContext());
				if(addData.addTeams())
					Toast.makeText(getContext(), "Data uploaded successfully", Toast.LENGTH_SHORT).show();
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
			etTeamName.requestFocus();

			btnDeleteTeam.setVisibility(View.GONE);
		}
	}

    @Override
    public void onItemSelect(String type, String value, int position) {
        selTeam = teamList.get(position);
    }

    private void saveTeam() {
		String teamName = etTeamName.getText().toString();
		String shortName = etShortName.getText().toString();
		DatabaseHandler dbh = new DatabaseHandler(getContext());

		int teamID = selTeam != null ? selTeam.getId() : -1;
		selTeam = new Team(teamName, shortName);
		if(teamID > -1)
			selTeam.setId(teamID);

		boolean isNew = teamID < 0;
		int rowID = dbh.upsertTeam(selTeam);

		if(rowID == dbh.CODE_NEW_TEAM_DUP_RECORD) {
			Toast.makeText(getContext(), "Team with same name already exists. Choose a different name.", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getContext(), "Team created successfully.", Toast.LENGTH_SHORT).show();
			selTeam = isNew ? null : selTeam;
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

	private void showTeamsSelectDialog() {
		Intent batsmanIntent = new Intent(getContext(), TeamSelectActivity.class);

		batsmanIntent.putExtra(TeamSelectActivity.ARG_IS_MULTI, false);

		startActivityForResult(batsmanIntent, REQ_CODE_TEAM_SELECT);
	}

	private void showPlayerListDialog() {
    	Intent updPlayersIntent = new Intent(getContext(), PlayerSelectActivity.class);
    	DatabaseHandler dbh = new DatabaseHandler(getContext());
		associatedPlayers = dbh.getAssociatedPlayers(selTeam.getId());
    	updPlayersIntent.putExtra(PlayerSelectActivity.ARG_PLAYER_LIST, dbh.getAllPlayers().toArray());
    	updPlayersIntent.putExtra(PlayerSelectActivity.ARG_IS_MULTI_SELECT, true);
    	updPlayersIntent.putIntegerArrayListExtra(PlayerSelectActivity.ARG_ASSOCIATED_PLAYERS,
				(ArrayList<Integer>) associatedPlayers);

    	startActivityForResult(updPlayersIntent, REQ_CODE_UPDATE_PLAYERS);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case REQ_CODE_TEAM_SELECT:
				if(resultCode == TeamSelectActivity.RESP_CODE_OK) {
					selTeam = (Team) data.getSerializableExtra(TeamSelectActivity.ARG_RESP_TEAM);
					populateData();
				}
				break;

			case REQ_CODE_UPDATE_PLAYERS:
				if(resultCode == PlayerSelectActivity.RESP_CODE_OK) {
					Player[] selPlayers =
							CommonUtils.objectArrToPlayerArr((Object[]) data.getSerializableExtra(PlayerSelectActivity.ARG_RESP_SEL_PLAYERS));

					List<Integer> addedPlayers = getAddedPlayers(selPlayers, associatedPlayers);
					List<Integer> removedPlayers = getRemovedPlayers(selPlayers, associatedPlayers);

					new DatabaseHandler(getContext()).updateTeamList(selTeam, addedPlayers, removedPlayers);
				}
		}
	}

	private List<Integer> getAddedPlayers(Player[] selPlayers, List<Integer> pastPlayers) {
    	List<Integer> newPlayers = new ArrayList<>();
		if(pastPlayers == null)
			pastPlayers = new ArrayList<>();

    	if(selPlayers != null) {
			for (Player player : selPlayers) {
				if (!pastPlayers.contains(player.getID())) {
					newPlayers.add(player.getID());
				}
			}
		}

		return newPlayers;
	}

	private List<Integer> getRemovedPlayers(Player[] selPlayers, List<Integer> pastPlayers) {
    	List<Integer> removedPlayers = new ArrayList<>();
		if(pastPlayers == null)
			pastPlayers = new ArrayList<>();

    	List<Integer> selPlayerIDs = new ArrayList<>();
    	if(selPlayers != null) {
			for (Player player : selPlayers)
				selPlayerIDs.add(player.getID());
		}

		for(int pastPlayer : pastPlayers) {
    		if(!selPlayerIDs.contains(pastPlayer))
    			removedPlayers.add(pastPlayer);
		}

		return removedPlayers;
	}
}
