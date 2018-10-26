package com.thenewcone.myscorecard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.adapter.PlayerViewAdapter;
import com.thenewcone.myscorecard.comparator.PlayerComparator;
import com.thenewcone.myscorecard.intf.ListInteractionListener;
import com.thenewcone.myscorecard.player.Player;
import com.thenewcone.myscorecard.utils.CommonUtils;
import com.thenewcone.myscorecard.utils.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlayerSelectActivity extends Activity
	implements ListInteractionListener, View.OnClickListener {

	public static final String ARG_PLAYER_LIST = "PlayerList";
	public static final String ARG_IS_MULTI_SELECT = "isMultiSelect";
	public static final String ARG_ASSOCIATED_PLAYERS = "AssociatedPlayers";
	public static final String ARG_RESP_SEL_PLAYER = "SelectedPlayer";
	public static final String ARG_RESP_SEL_PLAYERS = "SelectedPlayers";

	public static final int RESP_CODE_OK = 1;
	public static final int RESP_CODE_CANCEL = -1;

    @Override
    public void onBackPressed() {
        //
    }

    List<Player> playerList = new ArrayList<>();
	ArrayList<Integer> associatedPlayers;
	boolean isMultiSelect = false;

	List<Player> selPlayers = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_select);

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
		}

		RecyclerView rcvPlayerList = findViewById(R.id.rcvPlayerList);
		rcvPlayerList.setHasFixedSize(false);

        findViewById(R.id.btnSelPlayerOK).setOnClickListener(this);
        findViewById(R.id.btnSelPlayerCancel).setOnClickListener(this);
        findViewById(R.id.btnCancel).setOnClickListener(this);

		if(playerList.size() > 0) {
			Collections.sort(playerList, new PlayerComparator(associatedPlayers));

			rcvPlayerList.setLayoutManager(new LinearLayoutManager(this));
			PlayerViewAdapter adapter = new PlayerViewAdapter(playerList, this, isMultiSelect, associatedPlayers);
			rcvPlayerList.setAdapter(adapter);

			LinearLayoutManager llm = new LinearLayoutManager(this);
			llm.setOrientation(LinearLayoutManager.VERTICAL);
			rcvPlayerList.setLayoutManager(llm);

			rcvPlayerList.setItemAnimator(new DefaultItemAnimator());
		} else {
			rcvPlayerList.setVisibility(View.GONE);
			findViewById(R.id.llPlayerSelectButtons).setVisibility(View.GONE);
			findViewById(R.id.llNoPlayers).setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btnSelPlayerOK:
				sendResponse(RESP_CODE_OK);
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
				respIntent.putExtra(ARG_RESP_SEL_PLAYER, selPlayers.get(0));

		setResult(resultCode, respIntent);
		finish();
	}

	@Override
	public void onListFragmentInteraction(Object selItem) {
		selPlayers.add((Player) selItem);

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
