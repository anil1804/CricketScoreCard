package com.theNewCone.cricketScoreCard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.adapter.CompletedMatchViewAdapter;
import com.theNewCone.cricketScoreCard.comparator.MatchComparator;
import com.theNewCone.cricketScoreCard.intf.ListInteractionListener;
import com.theNewCone.cricketScoreCard.match.Match;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CompletedMatchSelectActivity extends Activity
		implements ListInteractionListener, View.OnClickListener {

	public static final String ARG_MATCH_LIST = "MatchList";
	public static final String ARG_IS_MULTI_SELECT = "isMultiSelect";
	public static final String ARG_RESP_SEL_MATCH = "SelectedMatch";
	public static final String ARG_RESP_SEL_MATCHES = "SelectedMatch";

	public static final int RESP_CODE_OK = 1;
	public static final int RESP_CODE_CANCEL = -1;

	List<Match> completedMatchList = new ArrayList<>();

	List<Match> selMatches = new ArrayList<>();
	Match selMatch;
	private boolean isMultiSelect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		if(intent != null && intent.getExtras() != null) {
			Bundle extras = intent.getExtras();
			Match[] completedMatches = CommonUtils.objectArrToMatchArr((Object[]) extras.getSerializable(ARG_MATCH_LIST));
			if(completedMatches != null && completedMatches.length > 0) {
				completedMatchList = Arrays.asList(completedMatches);
			}
			isMultiSelect = extras.getBoolean(ARG_IS_MULTI_SELECT, false);
		}

		setContentView(R.layout.activity_saved_match_select);

		findViewById(R.id.btnSelMatchCancel).setOnClickListener(this);
		findViewById(R.id.btnSelMatchOK).setOnClickListener(this);

		RecyclerView rcvMatchList = findViewById(R.id.rcvMatchList);
		rcvMatchList.setHasFixedSize(false);

		if(completedMatchList != null) {
			Collections.sort(completedMatchList, new MatchComparator());

			rcvMatchList.setLayoutManager(new LinearLayoutManager(this));
			CompletedMatchViewAdapter adapter = new CompletedMatchViewAdapter(completedMatchList, this, isMultiSelect);
			rcvMatchList.setAdapter(adapter);

			LinearLayoutManager llm = new LinearLayoutManager(this);
			llm.setOrientation(LinearLayoutManager.VERTICAL);
			rcvMatchList.setLayoutManager(llm);

			rcvMatchList.setItemAnimator(new DefaultItemAnimator());

			if(!isMultiSelect)
				findViewById(R.id.btnSelMatchOK).setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btnSelMatchOK:
				sendResponse(RESP_CODE_OK);
				break;

			case R.id.btnSelMatchCancel:
				sendResponse(RESP_CODE_CANCEL);
				break;
		}
	}

	@Override
	public void onListFragmentInteraction(Object selItem) {
		selMatch = (Match) selItem;
		sendResponse(RESP_CODE_OK);
	}

	@Override
	public void onListFragmentMultiSelect(Object selItem, boolean removeItem) {
		if(removeItem) {
			for (int i = 0; i < selMatches.size(); i++) {
				if (selMatches.get(i).getId() == ((Match) selItem).getId()) {
					selMatches.remove(i);
					break;
				}
			}
		} else {
			selMatches.add((Match) selItem);
		}
	}

	private void sendResponse(int resultCode) {
		Intent respIntent = new Intent();

		if(resultCode == RESP_CODE_OK)
			if(isMultiSelect)
				respIntent.putExtra(ARG_RESP_SEL_MATCHES, selMatches.toArray());
			else
				respIntent.putExtra(ARG_RESP_SEL_MATCH, selMatch);

		setResult(resultCode, respIntent);
		finish();
	}
}
