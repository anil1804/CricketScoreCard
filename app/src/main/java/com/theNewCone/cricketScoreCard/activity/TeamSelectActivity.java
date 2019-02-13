package com.theNewCone.cricketScoreCard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.adapter.TeamViewAdapter;
import com.theNewCone.cricketScoreCard.comparator.TeamComparator;
import com.theNewCone.cricketScoreCard.intf.ListInteractionListener;
import com.theNewCone.cricketScoreCard.match.Team;
import com.theNewCone.cricketScoreCard.utils.database.TeamDBHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class TeamSelectActivity extends Activity
	implements View.OnClickListener, ListInteractionListener {

	public static final String ARG_IS_MULTI = "isMultiSelect";
	public static final String ARG_EXISTING_TEAMS = "CurrentAssociatedTeams";
	public static final String ARG_RESP_TEAM = "SelectedTeam";
	public static final String ARG_RESP_TEAMS = "SelectedTeams";
	public static final String ARG_SELECT_COUNT = "SelectionCount";

	public static int RESP_CODE_OK = 1;
	public static int RESP_CODE_CANCEL = -1;


	private static boolean isMultiSelect = false;
	private static int selectionCount = -1;

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
				selTeams.addAll(new TeamDBHandler(this).getTeams(currentlyAssociatedTeams));
			}
			selectionCount = extras.getInt(ARG_SELECT_COUNT, -1);
		}

		setContentView(R.layout.activity_team_select);
		btnTeamSelectOk = findViewById(R.id.btnTeamSelectOK);
		btnTeamSelectCancel = findViewById(R.id.btnTeamSelectCancel);
		btnTeamSelectOk.setOnClickListener(this);
		btnTeamSelectCancel.setOnClickListener(this);

		btnCancel = findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);

		// Set the adapter
		List<Team> teamList = getTeamList();
		Collections.sort(teamList, new TeamComparator(currentlyAssociatedTeams));

		RecyclerView recyclerView = findViewById(R.id.rcvTeamList);
		if(teamList.size() > 0) {
			recyclerView.setLayoutManager(new LinearLayoutManager(this));
			recyclerView.setAdapter(new TeamViewAdapter(teamList, currentlyAssociatedTeams,this, isMultiSelect));
			if(!isMultiSelect)
				findViewById(R.id.llTeamSelectButtons).setVisibility(View.GONE);
		} else {
			recyclerView.setVisibility(View.GONE);
			findViewById(R.id.llTeamSelectButtons).setVisibility(View.GONE);
			findViewById(R.id.llNoTeams).setVisibility(View.VISIBLE);
		}
	}

	private List<Team> getTeamList() {
		return new TeamDBHandler(this).getTeams(null, -1);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnCancel:
			case R.id.btnTeamSelectCancel:
				sendResponse(RESP_CODE_CANCEL);
				break;

			case R.id.btnTeamSelectOK:
				if (selectionCount > -1 && selTeams != null && selTeams.size() != selectionCount) {
					Toast.makeText(this, String.format(Locale.getDefault()
							, "Expected %d teams. Selected %d teams. Please selected %d teams."
							, selectionCount, selTeams.size(), selectionCount),
							Toast.LENGTH_LONG).show();
				} else {
					sendResponse(RESP_CODE_OK);
				}
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
