package com.theNewCone.cricketScoreCard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.adapter.PlayerViewAdapter;
import com.theNewCone.cricketScoreCard.comparator.PlayerComparator;
import com.theNewCone.cricketScoreCard.intf.ListInteractionListener;
import com.theNewCone.cricketScoreCard.player.Player;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;
import com.theNewCone.cricketScoreCard.utils.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class PlayerSelectActivity extends Activity
	implements ListInteractionListener, View.OnClickListener {

	public static final String ARG_PLAYER_LIST = "PlayerList";
	public static final String ARG_IS_MULTI_SELECT = "isMultiSelect";
	public static final String ARG_ASSOCIATED_PLAYERS = "AssociatedPlayers";
	public static final String ARG_RESP_SEL_PLAYER = "SelectedPlayer";
	public static final String ARG_RESP_SEL_PLAYERS = "SelectedPlayers";
	public static final String ARG_NUM_PLAYERS = "NumberOfPlayers";

	public static final int RESP_CODE_OK = 1;
	public static final int RESP_CODE_CANCEL = -1;

    List<Player> playerList = new ArrayList<>();
	ArrayList<Integer> associatedPlayers;
	boolean isMultiSelect = false;
	int numPlayers;

	List<Player> selPlayers = new ArrayList<>();
	Player selPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		if(intent != null && intent.getExtras() != null) {
			Bundle extras = intent.getExtras();
			Player[] players = CommonUtils.objectArrToPlayerArr((Object[]) extras.getSerializable(ARG_PLAYER_LIST));
			if(players != null && players.length > 0) {
				playerList = Arrays.asList(players);
			}
			isMultiSelect = extras.getBoolean(ARG_IS_MULTI_SELECT, false);
			associatedPlayers = extras.getIntegerArrayList(ARG_ASSOCIATED_PLAYERS);
			if(associatedPlayers != null) {
				selPlayers.addAll(new DatabaseHandler(this).getPlayers(associatedPlayers));
			}
			numPlayers = extras.getInt(ARG_NUM_PLAYERS, 0);
		}

		setContentView(R.layout.activity_player_select);
		findViewById(R.id.btnSelPlayerOK).setOnClickListener(this);
		findViewById(R.id.btnSelPlayerCancel).setOnClickListener(this);

		RecyclerView rcvPlayerList = findViewById(R.id.rcvPlayerList);
		rcvPlayerList.setHasFixedSize(false);

        findViewById(R.id.btnCancel).setOnClickListener(this);

		if(playerList != null && ((!isMultiSelect && playerList.size() > 0) || (isMultiSelect && playerList.size() >= numPlayers))) {
			Collections.sort(playerList, new PlayerComparator(associatedPlayers));

			rcvPlayerList.setLayoutManager(new LinearLayoutManager(this));
			PlayerViewAdapter adapter = new PlayerViewAdapter(playerList, this, isMultiSelect, associatedPlayers);
			rcvPlayerList.setAdapter(adapter);

			LinearLayoutManager llm = new LinearLayoutManager(this);
			llm.setOrientation(LinearLayoutManager.VERTICAL);
			rcvPlayerList.setLayoutManager(llm);

			rcvPlayerList.setItemAnimator(new DefaultItemAnimator());

			if(!isMultiSelect)
				findViewById(R.id.llPlayerSelectButtons).setVisibility(View.GONE);
		} else {
			rcvPlayerList.setVisibility(View.GONE);
			findViewById(R.id.llPlayerSelectButtons).setVisibility(View.GONE);
			findViewById(R.id.llNoPlayers).setVisibility(View.VISIBLE);

			if(isMultiSelect && playerList.size() < numPlayers)
				((TextView) findViewById(R.id.tvNoPlayers)).setText(getString(R.string.notEnoughPlayers));
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btnSelPlayerOK:
				if(isMultiSelect && numPlayers > 0 && selPlayers.size() != numPlayers) {
					Toast.makeText(this,
							String.format(Locale.getDefault(), getString(R.string.Player_selectExactPlayers), selPlayers.size(), numPlayers),
							Toast.LENGTH_LONG).show();
				} else {
					sendResponse(RESP_CODE_OK);
				}
				break;

			case R.id.btnCancel:
			case R.id.btnSelPlayerCancel:
				sendResponse(RESP_CODE_CANCEL);
				break;

		}
	}

	private void sendResponse(int resultCode) {
		Intent respIntent = new Intent();

		if(resultCode == RESP_CODE_OK)
			if(isMultiSelect)
				respIntent.putExtra(ARG_RESP_SEL_PLAYERS, selPlayers.toArray());
			else
				respIntent.putExtra(ARG_RESP_SEL_PLAYER, selPlayer);

		setResult(resultCode, respIntent);
		finish();
	}

	@Override
	public void onListFragmentInteraction(Object selItem) {
		selPlayer = (Player) selItem;
		sendResponse(RESP_CODE_OK);
	}

	@Override
	public void onListFragmentMultiSelect(Object selItem, boolean removeItem) {
		if(removeItem) {
			for (int i = 0; i < selPlayers.size(); i++) {
				if (selPlayers.get(i).getID() == ((Player) selItem).getID()) {
					selPlayers.remove(i);
					break;
				}
			}
		} else {
			selPlayers.add((Player) selItem);
		}
	}
}
