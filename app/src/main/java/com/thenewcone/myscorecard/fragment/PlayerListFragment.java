package com.thenewcone.myscorecard.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.adapter.PlayerViewAdapter;
import com.thenewcone.myscorecard.intf.PlayerInteractionListener;
import com.thenewcone.myscorecard.player.Player;
import com.thenewcone.myscorecard.utils.database.DatabaseHandler;
import com.thenewcone.myscorecard.viewModel.PlayerViewModel;

import java.util.List;

public class PlayerListFragment extends Fragment
    implements PlayerInteractionListener, View.OnClickListener {

    public static final String ARG_SEL_PLAYER = "Player";
    public static final int ARG_RESP_CODE_OK = 1;
    private PlayerViewModel playerViewModel;

    public PlayerListFragment() {
    }

    public static PlayerListFragment newInstance() {
        return new PlayerListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playerViewModel = ViewModelProviders.of(getActivity()).get(PlayerViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_list, container, false);

        // Set the adapter
        Context context = view.getContext();
        List<Player> playerList = getPlayerList();

        RecyclerView recyclerView = view.findViewById(R.id.rcvPlayerList);
        if(playerList.size() > 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new PlayerViewAdapter(playerList, this));
        } else {
            recyclerView.setVisibility(View.GONE);
            view.findViewById(R.id.tvNoPlayers).setVisibility(View.VISIBLE);
        }

        view.findViewById(R.id.btnCancel).setOnClickListener(this);

        return view;
    }

    private List<Player> getPlayerList() {
        DatabaseHandler dbHandler = new DatabaseHandler(getContext());
        return dbHandler.getAllPlayers();
    }

    @Override
    public void onListFragmentInteraction(Player selPlayer) {
        playerViewModel.selectPlayer(selPlayer);
        gotoPlayerFragment();
    }

    private void gotoPlayerFragment() {
        if(getActivity() != null) {
            String fragmentTag = PlayerFragment.class.getSimpleName();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, PlayerFragment.newInstance(), fragmentTag)
                    .commit();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                gotoPlayerFragment();
                break;
        }
    }
}
