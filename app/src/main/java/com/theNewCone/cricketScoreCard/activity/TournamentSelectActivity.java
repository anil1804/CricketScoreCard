package com.theNewCone.cricketScoreCard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.adapter.TournamentViewAdapter;
import com.theNewCone.cricketScoreCard.comparator.TournamentComparator;
import com.theNewCone.cricketScoreCard.intf.ListInteractionListener;
import com.theNewCone.cricketScoreCard.tournament.Tournament;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TournamentSelectActivity extends Activity
		implements View.OnClickListener, ListInteractionListener {

	public static final String ARG_TOURNAMENT_LIST = "MatchList";
	public static final String ARG_IS_MULTI_SELECT = "isMultiSelect";
	public static final String ARG_RESP_SEL_TOURNAMENT = "SelectedTournament";
	public static final String ARG_RESP_SEL_TOURNAMENTS = "SelectedTournaments";

	public static final int RESP_CODE_OK = 1;
	public static final int RESP_CODE_CANCEL = -1;

	List<Tournament> tournamentList = new ArrayList<>();

	List<Tournament> selTournaments = new ArrayList<>();
	Tournament selTournament;
	private boolean isMultiSelect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		if (intent != null && intent.getExtras() != null) {
			Bundle extras = intent.getExtras();
			Tournament[] tournaments = CommonUtils.objectArrToTournamentArr((Object[]) extras.getSerializable(ARG_TOURNAMENT_LIST));
			if (tournaments != null && tournaments.length > 0) {
				tournamentList = Arrays.asList(tournaments);
			}
			isMultiSelect = extras.getBoolean(ARG_IS_MULTI_SELECT, false);
		}

		setContentView(R.layout.activity_tournament_select);

		findViewById(R.id.btnSelTournamentCancel).setOnClickListener(this);
		findViewById(R.id.btnSelTournamentOK).setOnClickListener(this);

		RecyclerView rcvTournamentList = findViewById(R.id.rcvTournamentList);
		rcvTournamentList.setHasFixedSize(false);

		if (tournamentList != null) {
			Collections.sort(tournamentList, new TournamentComparator());

			rcvTournamentList.setLayoutManager(new LinearLayoutManager(this));
			TournamentViewAdapter adapter = new TournamentViewAdapter(tournamentList, this, isMultiSelect);
			rcvTournamentList.setAdapter(adapter);

			LinearLayoutManager llm = new LinearLayoutManager(this);
			llm.setOrientation(LinearLayoutManager.VERTICAL);
			rcvTournamentList.setLayoutManager(llm);

			rcvTournamentList.setItemAnimator(new DefaultItemAnimator());

			if (!isMultiSelect)
				findViewById(R.id.btnSelTournamentOK).setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btnSelTournamentOK:
				sendResponse(RESP_CODE_OK);
				break;

			case R.id.btnSelTournamentCancel:
				sendResponse(RESP_CODE_CANCEL);
				break;
		}
	}

	@Override
	public void onListFragmentInteraction(Object selItem) {
		selTournament = (Tournament) selItem;
		sendResponse(RESP_CODE_OK);
	}

	@Override
	public void onListFragmentMultiSelect(Object selItem, boolean removeItem) {
		if (removeItem) {
			for (int i = 0; i < selTournaments.size(); i++) {
				if (selTournaments.get(i).getId() == ((Tournament) selItem).getId()) {
					selTournaments.remove(i);
					break;
				}
			}
		} else {
			selTournaments.add((Tournament) selItem);
		}
	}

	private void sendResponse(int resultCode) {
		Intent respIntent = new Intent();

		if (resultCode == RESP_CODE_OK)
			if (isMultiSelect)
				respIntent.putExtra(ARG_RESP_SEL_TOURNAMENTS, selTournaments.toArray());
			else
				respIntent.putExtra(ARG_RESP_SEL_TOURNAMENT, selTournament);

		setResult(resultCode, respIntent);
		finish();
	}
}
