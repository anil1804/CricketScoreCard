package com.thenewcone.myscorecard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.adapter.TeamViewAdapter;
import com.thenewcone.myscorecard.comparator.TeamComparator;
import com.thenewcone.myscorecard.intf.ListInteractionListener;
import com.thenewcone.myscorecard.match.Team;
import com.thenewcone.myscorecard.utils.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeamSelectActivity extends Activity
	implements View.OnClickListener, ListInteractionListener {

	public static final String ARG_IS_MULTI = "isMultiSelect";
	public static final String ARG_EXISTING_TEAMS = "CurrentAssociatedTeams";
	public static final String ARG_RESP_TEAM = "SelectedTeam";
	public static final String ARG_RESP_TEAMS = "SelectedTeams";

	public static int RESP_CODE_OK = 1;
	public static int RESP_CODE_CANCEL = -1;

	private static boolean isMultiSelect = false;

	List<Team> selTeams;
	Team selTeam;
	ArrayList<Integer> currentlyAssociatedTeams;

	Button btnTeamSelectOk, btnTeamSelectCancel, btnCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		selTeams = new ArrayList<>();

		Intent intent = getIntent();
		if(intent !=  null && intent.getExtras() != null) {
			Bundle extras = intent.getExtras();
			isMultiSelect = extras.getBoolean(ARG_IS_MULTI, false);
			currentlyAssociatedTeams = extras.getIntegerArrayList(ARG_EXISTING_TEAMS);
			if(currentlyAssociatedTeams != null) {
				selTeams.addAll(new DatabaseHandler(this).getTeams(currentlyAssociatedTeams));
			}
		}

		if(isMultiSelect) {
			setContentView(R.layout.activity_team_select_multiple);
			btnTeamSelectOk = findViewById(R.id.btnTeamSelectOK);
			btnTeamSelectCancel = findViewById(R.id.btnTeamSelectCancel);
			btnTeamSelectOk.setOnClickListener(this);
			btnTeamSelectCancel.setOnClickListener(this);
		} else {
			setContentView(R.layout.activity_team_select);
		}

		btnCancel = findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);

		// Set the adapter
		List<Team> teamList = getTeamList();
		Collections.sort(teamList, new TeamComparator(currentlyAssociatedTeams));

		RecyclerView recyclerView = findViewById(R.id.rcvTeamList);
		if(teamList.size() > 0) {
			recyclerView.setLayoutManager(new LinearLayoutManager(this));
			recyclerView.setAdapter(new TeamViewAdapter(teamList, currentlyAssociatedTeams,this, isMultiSelect));
		} else {
			recyclerView.setVisibility(View.GONE);
			if(isMultiSelect)
				findViewById(R.id.llTeamSelectButtons).setVisibility(View.GONE);
			findViewById(R.id.llNoTeams).setVisibility(View.VISIBLE);
		}
	}

	private List<Team> getTeamList() {
		DatabaseHandler dbHandler = new DatabaseHandler(this);
		return dbHandler.getTeams(null, -1);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnCancel:
			case R.id.btnTeamSelectCancel:
				sendResponse(RESP_CODE_CANCEL);
				break;

			case R.id.btnTeamSelectOK:
				sendResponse(RESP_CODE_OK);
				break;

		}
	}

	private void sendResponse(int resultCode) {
		Intent respIntent = new Intent();

		if(resultCode == RESP_CODE_OK)
			if(isMultiSelect)
				respIntent.putExtra(ARG_RESP_TEAMS, selTeams.toArray());
			else
				respIntent.putExtra(ARG_RESP_TEAM, selTeam);

		setResult(resultCode, respIntent);
		finish();
	}

	@Override
	public void onListFragmentInteraction(Object selItem) {
		selTeam = (Team) selItem;
		sendResponse(RESP_CODE_OK);
	}

	@Override
	public void onListFragmentMultiSelect(Object selItem, boolean removeItem) {
		if(removeItem) {
			for (int i = 0; i < selTeams.size(); i++) {
				if (selTeams.get(i).getId() == ((Team) selItem).getId()) {
					selTeams.remove(i);
					break;
				}
			}
		} else {
			selTeams.add((Team) selItem);
		}
	}
}
