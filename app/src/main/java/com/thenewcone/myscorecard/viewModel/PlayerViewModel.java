package com.thenewcone.myscorecard.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.thenewcone.myscorecard.player.Player;

public class PlayerViewModel extends ViewModel {
    private final MutableLiveData<Player> selectedPlayer = new MutableLiveData<>();

    public LiveData<Player> getSelectedPlayer() {
        return selectedPlayer;
    }

    public void selectPlayer(Player player) {
        selectedPlayer.setValue(player);
    }
}
