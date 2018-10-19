package com.thenewcone.myscorecard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.adapter.BowlerListAdapter;
import com.thenewcone.myscorecard.intf.ItemClickListener;
import com.thenewcone.myscorecard.player.BowlerStats;
import com.thenewcone.myscorecard.player.Player;
import com.thenewcone.myscorecard.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BowlerSelectActivity extends Activity
        implements ItemClickListener, View.OnClickListener {

    public static final String ARG_PLAYER_LIST = "PlayerList";
    public static final String ARG_BOWLER_LIST = "BowlerList";
    public static final String ARG_PREV_BOWLER = "PreviousBowler";
    public static final String ARG_MAX_OVERS_PER_BOWLER = "MaxOversPerBowler";
    public static final String ARG_SEL_BOWLER = "SelectedBowler";

    public static final int RESP_CODE_OK = 1;
    public static final int RESP_CODE_CANCEL = -1;

    BowlerStats[] bowlers;
    Player[] players;
    BowlerStats selBowler, prevBowler;
    int maxOversPerBowler = -1;
    List<BowlerStats> dispBowlers;

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
            bowlers = CommonUtils.objectArrToBowlerArr((Object[]) intent.getSerializableExtra(ARG_BOWLER_LIST));
            prevBowler = (BowlerStats) intent.getSerializableExtra(ARG_PREV_BOWLER);
            maxOversPerBowler = intent.getIntExtra(ARG_MAX_OVERS_PER_BOWLER, 10);

            dispBowlers = getDispBowlers(players, bowlers);
        }

        if(dispBowlers != null && dispBowlers.size() > 0) {
            rcvPlayerList.setLayoutManager(new LinearLayoutManager(this));
            BowlerListAdapter adapter = new BowlerListAdapter(this, dispBowlers, maxOversPerBowler, prevBowler);
            adapter.setClickListener(this);
            rcvPlayerList.setAdapter(adapter);

            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            rcvPlayerList.setLayoutManager(llm);

            DividerItemDecoration itemDecor = new DividerItemDecoration(rcvPlayerList.getContext(), DividerItemDecoration.HORIZONTAL);
            rcvPlayerList.addItemDecoration(itemDecor);

            rcvPlayerList.setItemAnimator(new DefaultItemAnimator());
        }
    }

    private List<BowlerStats> getDispBowlers(Player[] players, BowlerStats[] bowlers) {
        List<BowlerStats> dispBowlers = Arrays.asList(bowlers);

        List<String> bowledBowlers;
        if(players != null && players.length > 0) {
            bowledBowlers = getPlayedBowlers(bowlers);

            for(Player player : players) {
                if(!bowledBowlers.contains(player.getName())) {
                    dispBowlers.add(new BowlerStats(player));
                }
            }
        }

        return dispBowlers;
    }

    private List<String> getPlayedBowlers(BowlerStats[] bowlers) {
        List<String> playedPlayers = new ArrayList<>();

        for(BowlerStats bowler : bowlers)
            playedPlayers.add(bowler.getBowlerName());

        return playedPlayers;
    }

    @Override
    public void onClick(View view) {
        Intent respIntent = getIntent();

        switch (view.getId()) {
            case R.id.btnSelPlayerOK:
                if(selBowler == null)
                    Toast.makeText(this, "Bowler not selected", Toast.LENGTH_SHORT).show();
                else {
                    respIntent.putExtra(ARG_SEL_BOWLER, selBowler);
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

    @Override
    public void onItemClick(View view, int position) {
        selBowler = dispBowlers.get(position);
    }
}
