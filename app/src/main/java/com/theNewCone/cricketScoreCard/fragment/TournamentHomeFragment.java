package com.theNewCone.cricketScoreCard.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.enumeration.TournamentFormat;
import com.theNewCone.cricketScoreCard.enumeration.TournamentStageType;
import com.theNewCone.cricketScoreCard.match.Team;
import com.theNewCone.cricketScoreCard.tournament.Tournament;


public class TournamentHomeFragment extends Fragment {

	Tournament tournament;
	View theView;

	public TournamentHomeFragment() {
		// Required empty public constructor
	}

	public static TournamentHomeFragment newInstance(Tournament tournament) {
		TournamentHomeFragment fragment = new TournamentHomeFragment();
		fragment.tournament = tournament;

		return fragment;
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		theView = inflater.inflate(R.layout.fragment_tournament_home, container, false);

		initializeView();

		return theView;
	}

	private void initializeView() {
		TextView tvTHName = theView.findViewById(R.id.tvTHName);
		TextView tvTHFormat = theView.findViewById(R.id.tvTHFormat);
		TextView tvTHStageType = theView.findViewById(R.id.tvTHStageType);
		TextView tvTHTeams = theView.findViewById(R.id.tvTHTeams);
		TextView tvTHWinner = theView.findViewById(R.id.tvTHWinner);

		LinearLayout llTHWinner = theView.findViewById(R.id.llTHWinner);

		tvTHName.setText(tournament.getName());
		tvTHFormat.setText(tournament.getFormat().stringValue());

		String tournamentStage =
				(tournament.getFormat() == TournamentFormat.KNOCK_OUT
						&& tournament.getStageType() == TournamentStageType.KNOCK_OUT)
						? TournamentStageType.NONE.stringValue()
						: tournament.getStageType().stringValue();
		tvTHStageType.setText(tournamentStage);

		StringBuilder teamSB = new StringBuilder();

		Team[] teams = tournament.getTeams();
		for (int i = 0; i < teams.length; i++) {
			teamSB.append(teams[i].getName());
			if (i < teams.length - 1) {
				teamSB.append(",");
				teamSB.append("\n");
			}
		}
		tvTHTeams.setText(teamSB.toString());

		if (tournament.isComplete()) {
			llTHWinner.setVisibility(View.VISIBLE);
			String winnerText = tournament.getTournamentWinner().getName();
			tvTHWinner.setText(winnerText);
		} else {
			llTHWinner.setVisibility(View.GONE);
		}
	}
}
