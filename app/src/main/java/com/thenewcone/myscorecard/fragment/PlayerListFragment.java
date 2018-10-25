package com.thenewcone.myscorecard.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.adapter.PlayerViewAdapter;
import com.thenewcone.myscorecard.intf.ListInteractionListener;
import com.thenewcone.myscorecard.player.Player;
import com.thenewcone.myscorecard.utils.database.DatabaseHandler;
import com.thenewcone.myscorecard.viewModel.PlayerViewModel;

import java.util.List;

public class PlayerListFragment extends Fragment
    implements ListInteractionListener, View.OnClickListener {

    private PlayerViewModel playerViewModel;

    public PlayerListFragment() {
    }

    public static PlayerListFragment newInstance() {
        return new PlayerListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getActivity() !=  null)
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
    public void onListFragmentInteraction(Object selObject) {
        playerViewModel.selectPlayer((Player) selObject);
        gotoPlayerFragment();
    }

	@Override
	public void onListFragmentMultiSelect(List<Object> selItemList) {
		playerViewModel.selectPlayerList(selItemList);
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
