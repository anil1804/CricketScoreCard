package com.thenewcone.myscorecard.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.thenewcone.myscorecard.player.Player;

import java.util.List;

public class PlayerViewModel extends ViewModel {
    private final MutableLiveData<Player> selectedPlayer = new MutableLiveData<>();
    private final MutableLiveData<List<Player>> selectedPlayerList = new MutableLiveData<>();

    public LiveData<Player> getSelectedPlayer() {
        return selectedPlayer;
    }

    public void selectPlayer(Player player) {
        selectedPlayer.setValue(player);
    }

    public LiveData<List<Player>> getSelectedPlayerList() {
        return selectedPlayerList;
    }

    public void selectPlayerList(List<Object> selPlayerList) {
    	List<Player> playerList = null;

    	if(selPlayerList != null) {
    		for(Object player : selPlayerList)
    			playerList.add((Player) player);
		}
		selectedPlayerList.setValue(playerList);
    }
}
