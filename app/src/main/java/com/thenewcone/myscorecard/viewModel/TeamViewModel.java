package com.thenewcone.myscorecard.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.thenewcone.myscorecard.match.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamViewModel extends ViewModel {

	private final MutableLiveData<Team> selectedTeam = new MutableLiveData<>();

	private final MutableLiveData<List<Team>> selectedTeamList = new MutableLiveData<>();

	public LiveData<Team> getSelectedTeam() {
		return selectedTeam;
	}

	public LiveData<List<Team>> getSelectedTeams() {
		return selectedTeamList;
	}

	public void selectTeam(Team team) {
		selectedTeam.setValue(team);
	}

	public void selectTeamList(List<Object> teamList) {
		List<Team> selTeamList = null;

		if(teamList != null) {
			selTeamList = new ArrayList<>();
			for(Object team : teamList)
				selTeamList.add((Team) team);
		}

		selectedTeamList.setValue(selTeamList);
	}
}
