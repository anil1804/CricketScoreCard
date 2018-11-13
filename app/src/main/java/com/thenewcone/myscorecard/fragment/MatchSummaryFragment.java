package com.thenewcone.myscorecard.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.activity.ScoreCardActivity;
import com.thenewcone.myscorecard.match.CricketCardUtils;
import com.thenewcone.myscorecard.match.Match;
import com.thenewcone.myscorecard.match.Team;
import com.thenewcone.myscorecard.player.Player;
import com.thenewcone.myscorecard.utils.CommonUtils;

import java.util.Locale;

public class MatchSummaryFragment extends Fragment {

	CricketCardUtils ccUtils;
	Match matchInfo;

	public static MatchSummaryFragment newInstance(CricketCardUtils ccUtils, Match matchInfo) {
		MatchSummaryFragment fragment = new MatchSummaryFragment();
		fragment.initialize(ccUtils, matchInfo);

		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View theView = inflater.inflate(R.layout.fragment_match_summary, container, false);

		setupView(theView);

		return theView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_fragments_match_summary, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_scoreCard:
				if(getActivity() != null) {
					Intent scoreCardIntent = new Intent(getContext(), ScoreCardActivity.class);
					scoreCardIntent.putExtra(ScoreCardActivity.ARG_CRICKET_CARD_UTILS, CommonUtils.convertToJSON(ccUtils));
					startActivity(scoreCardIntent);
				}
				break;
		}
		return true;
	}

	private void initialize(CricketCardUtils ccUtils, Match matchInfo) {
		this.ccUtils = ccUtils;
		this.matchInfo = matchInfo;
	}

	private void setupView(View rootView) {
		TextView tvSCVersus = rootView.findViewById(R.id.tvSCVersus);
		TextView tvSCMatchDate = rootView.findViewById(R.id.tvSCMatchDate);
		TextView tvSCTossInfo = rootView.findViewById(R.id.tvSCTossInfo);
		TextView tvSCTeam1Name = rootView.findViewById(R.id.tvSCTeam1Name);
		TextView tvSCTeam2Name = rootView.findViewById(R.id.tvSCTeam2Name);
		TextView tvSCTeam1 = rootView.findViewById(R.id.tvSCTeam1);
		TextView tvSCTeam2 = rootView.findViewById(R.id.tvSCTeam2);
		TextView tvSCMatchResult = rootView.findViewById(R.id.tvSCMatchResult);
		TextView tvSCPoM = rootView.findViewById(R.id.tvSCPoM);

		if(ccUtils != null) {

			Team team1 = ccUtils.getTeam1(), team2 = ccUtils.getTeam2();

			String tossWonBy = (team1.getId() == ccUtils.getTossWonBy())  ? team1.getName() : team2.getName();
			String electedTo = (team1.getId() == ccUtils.getTossWonBy()) ? "Bat" : "Field";

			tvSCVersus.setText(String.format(Locale.getDefault(), "%s vs %s", team1.getName(), team2.getName()));
			tvSCTossInfo.setText(String.format(Locale.getDefault(),
					"%s won the toss and elected to %s", tossWonBy.toUpperCase(), electedTo.toUpperCase()));

			if(matchInfo != null && matchInfo.getDate() != null) {
				tvSCMatchDate.setText(String.format(Locale.getDefault(), "Played on %s",
						CommonUtils.dateToString(matchInfo.getDate(), "EEEE, MMMM d, yyyy")));
			}

			String teamName = team1.getShortName() + " Team";
			tvSCTeam1Name.setText(teamName);
			teamName = team2.getShortName() + " Team";
			tvSCTeam2Name.setText(teamName);

			StringBuilder teamInfoSB = new StringBuilder();
			for (Player player : team1.getMatchPlayers()) {
				String playerName = player.getName();
				playerName = (player.getID() == team1.getCaptain().getID()) ? playerName + " (c)" : playerName;
				playerName = (player.getID() == team1.getWicketKeeper().getID()) ? playerName + " (wk)" : playerName;
				teamInfoSB.append(playerName);
				teamInfoSB.append(", ");
			}
			teamInfoSB.delete(teamInfoSB.length() - 2, teamInfoSB.length());
			tvSCTeam1.setText(teamInfoSB.toString());

			teamInfoSB = new StringBuilder();
			for (Player player : team2.getMatchPlayers()) {
				String playerName = player.getName();
				playerName = (player.getID() == team1.getCaptain().getID()) ? playerName + " (c)" : playerName;
				playerName = (player.getID() == team1.getWicketKeeper().getID()) ? playerName + " (wk)" : playerName;
				teamInfoSB.append(playerName);
				teamInfoSB.append(", ");
			}
			teamInfoSB.delete(teamInfoSB.length() - 2, teamInfoSB.length());
			tvSCTeam2.setText(teamInfoSB.toString());

			if(ccUtils.getResult() != null)
				tvSCMatchResult.setText(ccUtils.getResult());

			if(ccUtils.getPlayerOfMatch() != null)
				tvSCPoM.setText(String.format(Locale.getDefault(), getString(R.string.tvPoMData), ccUtils.getPlayerOfMatch().getName()));
		}
	}
}
