package com.thenewcone.myscorecard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.adapter.SavedMatchViewAdapter;
import com.thenewcone.myscorecard.comparator.MatchStateComparator;
import com.thenewcone.myscorecard.intf.ListInteractionListener;
import com.thenewcone.myscorecard.match.MatchState;
import com.thenewcone.myscorecard.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SavedMatchSelectActivity extends Activity
		implements ListInteractionListener, View.OnClickListener {

	public static final String ARG_MATCH_LIST = "MatchList";
	public static final String ARG_IS_MULTI_SELECT = "isMultiSelect";
	public static final String ARG_RESP_SEL_MATCH = "SelectedMatch";
	public static final String ARG_RESP_SEL_MATCHES = "SelectedMatch";

	public static final int RESP_CODE_OK = 1;
	public static final int RESP_CODE_CANCEL = -1;

	List<MatchState> savedMatchList = new ArrayList<>();
	boolean isMultiSelect = false;

	List<MatchState> selMatches = new ArrayList<>();
	MatchState selMatch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		if(intent != null && intent.getExtras() != null) {
			Bundle extras = intent.getExtras();
			MatchState[] savedMatches = CommonUtils.objectArrToMatchStateArr((Object[]) extras.getSerializable(ARG_MATCH_LIST));
			if(savedMatches != null && savedMatches.length > 0) {
				savedMatchList = Arrays.asList(savedMatches);
			}
			isMultiSelect = extras.getBoolean(ARG_IS_MULTI_SELECT, false);
		}

		if(isMultiSelect) {
			setContentView(R.layout.activity_saved_match_select_multiple);

			findViewById(R.id.btnSelMatchOK).setOnClickListener(this);
			findViewById(R.id.btnSelMatchCancel).setOnClickListener(this);
		} else {
			setContentView(R.layout.activity_saved_match_select);
		}

		RecyclerView rcvMatchList = findViewById(R.id.rcvMatchList);
		rcvMatchList.setHasFixedSize(false);

		if(savedMatchList != null) {
			Collections.sort(savedMatchList, new MatchStateComparator());

			rcvMatchList.setLayoutManager(new LinearLayoutManager(this));
			SavedMatchViewAdapter adapter = new SavedMatchViewAdapter(savedMatchList, this, isMultiSelect);
			rcvMatchList.setAdapter(adapter);

			LinearLayoutManager llm = new LinearLayoutManager(this);
			llm.setOrientation(LinearLayoutManager.VERTICAL);
			rcvMatchList.setLayoutManager(llm);

			rcvMatchList.setItemAnimator(new DefaultItemAnimator());
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
		selMatch = (MatchState) selItem;
		sendResponse(RESP_CODE_OK);
	}

	@Override
	public void onListFragmentMultiSelect(Object selItem, boolean removeItem) {
		if(removeItem) {
			for (int i = 0; i < selMatches.size(); i++) {
				if (selMatches.get(i).getId() == ((MatchState) selItem).getId()) {
					selMatches.remove(i);
					break;
				}
			}
		} else {
			selMatches.add((MatchState) selItem);
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
