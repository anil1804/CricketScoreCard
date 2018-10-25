package com.thenewcone.myscorecard.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.thenewcone.myscorecard.match.Team;

public class TeamViewModel extends ViewModel {

	private final MutableLiveData<Team> selectedTeam = new MutableLiveData<>();

	public LiveData<Team> getSelectedTeam() {
		return selectedTeam;
	}

	public void selectTeam(Team team) {
		selectedTeam.setValue(team);
	}
}
