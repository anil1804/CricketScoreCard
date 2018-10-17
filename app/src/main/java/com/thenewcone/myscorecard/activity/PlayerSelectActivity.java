package com.thenewcone.myscorecard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.thenewcone.myscorecard.adapter.PlayerListAdapter;
import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.intf.ItemClickListener;
import com.thenewcone.myscorecard.player.Player;
import com.thenewcone.myscorecard.utils.CommonUtils;

public class PlayerSelectActivity extends Activity
	implements ItemClickListener, View.OnClickListener {

	public static final String ARG_PLAYER_LIST = "PlayerList";
	public static final String ARG_EFFECTED_BY = "EffectedBy";

	public static final int RESP_CODE_OK = 1;
	public static final int RESP_CODE_CANCEL = -1;

    @Override
    public void onBackPressed() {
        //
    }

    Player[] players;
	Player effectedBy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_select);

		RecyclerView rcvPlayerList = findViewById(R.id.rcvPlayerList);
		rcvPlayerList.setHasFixedSize(false);

        findViewById(R.id.btnSelPlayerOK).setOnClickListener(this);
        findViewById(R.id.btnSelPlayerCancel).setOnClickListener(this);

		Intent intent = getIntent();
		if(intent != null) {
			players = CommonUtils.objectArrToPlayerArr((Object[]) intent.getSerializableExtra(ARG_PLAYER_LIST));
		}

		if(players != null && players.length > 0) {
			rcvPlayerList.setLayoutManager(new LinearLayoutManager(this));
			PlayerListAdapter adapter = new PlayerListAdapter(this, players);
			adapter.setClickListener(this);
			rcvPlayerList.setAdapter(adapter);

			LinearLayoutManager llm = new LinearLayoutManager(this);
			llm.setOrientation(LinearLayoutManager.VERTICAL);
			rcvPlayerList.setLayoutManager(llm);

			rcvPlayerList.setItemAnimator(new DefaultItemAnimator());
		}
	}

	@Override
	public void onClick(View view, int position) {
		effectedBy = players[position];
	}

	@Override
	public void onClick(View view) {
		Intent respIntent = getIntent();

		switch (view.getId()) {
			case R.id.btnSelPlayerOK:
				if(effectedBy == null)
					Toast.makeText(this, "Player not selected", Toast.LENGTH_SHORT).show();
				else {
					respIntent.putExtra(ARG_EFFECTED_BY, effectedBy);
					setResult(RESP_CODE_OK, respIntent);
					finish();
				}
				break;

			case R.id.btnSelPlayerCancel:
				setResult(RESP_CODE_CANCEL);
				finish();
				break;

		}
	}
}
