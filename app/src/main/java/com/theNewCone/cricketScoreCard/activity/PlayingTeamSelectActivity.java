package com.theNewCone.cricketScoreCard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.match.Team;
import com.theNewCone.cricketScoreCard.player.Player;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;
import com.theNewCone.cricketScoreCard.utils.database.PlayerDBHandler;
import com.theNewCone.cricketScoreCard.utils.database.TeamDBHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class PlayingTeamSelectActivity extends Activity
		implements View.OnClickListener {

	public static final int RESP_CODE_OK = 1;
	public static final int RESP_CODE_CANCEL = -1;
	public static final String ARG_IGNORE_TEAMS = "IgnoreTeams";
	public static final String ARG_NUM_PLAYERS = "NumPlayers";
	public static final String ARG_IS_TOURNAMENT = "IsTournament";
	public static final String ARG_SEL_TEAM = "SelectedTeam";
	private static final int REQ_CODE_SEL_TEAM = 1;
	private static final int REQ_CODE_SEL_PLAYERS = 2;
	private static final int REQ_CODE_SEL_CAPTAIN = 3;
	private static final int REQ_CODE_SEL_WK = 4;
	Button btnSelectTeam, btnSelectPlayers, btnSelectCaptain, btnSelectWK, btnOK, btnCancel;
	TextView tvTeamName, tvPlayerList, tvCaptain, tvWK;
	private ArrayList<Integer> ignoreTeams = null;
	private Team selectedTeam = null;
	private int numPlayers;
	private boolean isTournament;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playing_team_select);

		btnSelectTeam = findViewById(R.id.btnPTSSelectTeam);
		btnSelectPlayers = findViewById(R.id.btnPTSSelectPlayers);
		btnSelectCaptain = findViewById(R.id.btnPTSSelectCaptain);
		btnSelectWK = findViewById(R.id.btnPTSSelectWK);
		btnOK = findViewById(R.id.btnPTSOk);
		btnCancel = findViewById(R.id.btnPTSCancel);

		btnSelectTeam.setOnClickListener(this);
		btnSelectPlayers.setOnClickListener(this);
		btnSelectCaptain.setOnClickListener(this);
		btnSelectWK.setOnClickListener(this);
		btnOK.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		tvTeamName = findViewById(R.id.tvPTSTeamName);
		tvPlayerList = findViewById(R.id.tvPTSTeamPlayers);
		tvCaptain = findViewById(R.id.tvPTSTeamCaptain);
		tvWK = findViewById(R.id.tvPTSTeamWK);

		if (getIntent() != null && getIntent().getExtras() != null) {
			Bundle extras = getIntent().getExtras();
			ignoreTeams = extras.getIntegerArrayList(ARG_IGNORE_TEAMS);
			numPlayers = extras.getInt(ARG_NUM_PLAYERS, 11);
			selectedTeam = (Team) extras.getSerializable(ARG_SEL_TEAM);
			isTournament = extras.getBoolean(ARG_IS_TOURNAMENT, false);
		}

		displayTeamData();

		if (selectedTeam == null)
			displayTeamSelect();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btnPTSSelectTeam:
				displayTeamSelect();
				break;

			case R.id.btnPTSSelectPlayers:
				displayPlayerSelect();
				break;

			case R.id.btnPTSSelectCaptain:
				displayCaptainWKSelect(REQ_CODE_SEL_CAPTAIN, selectedTeam.getCaptain());
				break;

			case R.id.btnPTSSelectWK:
				displayCaptainWKSelect(REQ_CODE_SEL_WK, selectedTeam.getWicketKeeper());
				break;

			case R.id.btnPTSOk:
				validateInput();
				break;

			case R.id.btnPTSCancel:
				sendResponse(RESP_CODE_CANCEL);
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQ_CODE_SEL_TEAM:
				if (resultCode == TeamSelectActivity.RESP_CODE_OK) {
					Team theTeam = (Team) data.getSerializableExtra(TeamSelectActivity.ARG_RESP_TEAM);
					if (!theTeam.equals(selectedTeam)) {
						selectedTeam = theTeam;
					}
				}
				break;

			case REQ_CODE_SEL_PLAYERS:
				if (resultCode == PlayerSelectActivity.RESP_CODE_OK) {
					List<Player> teamPlayers = Arrays.asList(
							CommonUtils.objectArrToPlayerArr(
									(Object[]) data.getSerializableExtra(PlayerSelectActivity.ARG_RESP_SEL_PLAYERS)));
					selectedTeam.setMatchPlayers(teamPlayers);
				}
				break;

			case REQ_CODE_SEL_CAPTAIN:
				if (resultCode == PlayerSelectActivity.RESP_CODE_OK) {
					Player captain = (Player) data.getSerializableExtra(PlayerSelectActivity.ARG_RESP_SEL_PLAYER);
					selectedTeam.setCaptain(captain);
				}
				break;

			case REQ_CODE_SEL_WK:
				if (resultCode == PlayerSelectActivity.RESP_CODE_OK) {
					Player wiki = (Player) data.getSerializableExtra(PlayerSelectActivity.ARG_RESP_SEL_PLAYER);
					selectedTeam.setWicketKeeper(wiki);
				}
				break;
		}

		displayTeamData();
	}

	private void displayTeamSelect() {
		if (isTournament) {
			Toast.makeText(this, "Cannot Change Team for a Tournament", Toast.LENGTH_SHORT).show();
		} else {
			Intent teamSelectIntent = new Intent(this, TeamSelectActivity.class);

			if (selectedTeam != null) {
				ArrayList<Integer> selTeams = new ArrayList<>();
				selTeams.add(selectedTeam.getId());
				teamSelectIntent.putExtra(TeamSelectActivity.ARG_EXISTING_TEAMS, selTeams);
			}
			if (ignoreTeams != null)
				teamSelectIntent.putExtra(TeamSelectActivity.ARG_IGNORE_TEAMS, ignoreTeams);

			startActivityForResult(teamSelectIntent, REQ_CODE_SEL_TEAM);
		}
	}

	private void displayPlayerSelect() {
		if (!hasEnoughPlayers()) {
			Toast.makeText(this, getResources().getString(R.string.PTS_notEnoughPlayers), Toast.LENGTH_SHORT).show();
			return;
		}

		List<Player> displayPlayerList = new PlayerDBHandler(this).getTeamPlayers(selectedTeam.getId());
		if (displayPlayerList != null && displayPlayerList.size() > 0) {
			if (displayPlayerList.size() >= numPlayers) {
				ArrayList<Integer> associatedPlayers = new ArrayList<>();

				List<Player> teamPlayers = selectedTeam.getMatchPlayers();

				for (int i = 0; i < numPlayers; i++) {
					associatedPlayers.add(
							(teamPlayers == null || teamPlayers.size() == 0)
									? displayPlayerList.get(i).getID()
									: teamPlayers.get(i).getID()
					);
				}

				Intent playerDisplayIntent = new Intent(this, PlayerSelectActivity.class);
				playerDisplayIntent.putExtra(PlayerSelectActivity.ARG_PLAYER_LIST, displayPlayerList.toArray());
				playerDisplayIntent.putExtra(PlayerSelectActivity.ARG_IS_MULTI_SELECT, true);
				playerDisplayIntent.putIntegerArrayListExtra(
						PlayerSelectActivity.ARG_ASSOCIATED_PLAYERS, associatedPlayers);
				playerDisplayIntent.putExtra(PlayerSelectActivity.ARG_NUM_PLAYERS, numPlayers);

				startActivityForResult(playerDisplayIntent, REQ_CODE_SEL_PLAYERS);
			} else {
				Toast.makeText(this,
						String.format(Locale.getDefault(), getResources().getString(R.string.NM_notEnoughPlayers), selectedTeam.getShortName()).trim(),
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(this,
					String.format(Locale.getDefault(),
							getResources().getString(R.string.NM_noPlayersInTeam),
							selectedTeam.getShortName()).trim(),
					Toast.LENGTH_LONG).show();
		}
	}

	private void displayCaptainWKSelect(int reqCode, Player selected) {
		if (selectedTeam != null && selectedTeam.getMatchPlayers() != null) {
			List<Player> displayPlayerList = selectedTeam.getMatchPlayers();
			ArrayList<Integer> associatedPlayers = new ArrayList<>();

			if (selected != null)
				associatedPlayers.add(selected.getID());

			Intent playerDisplayIntent = new Intent(this, PlayerSelectActivity.class);
			playerDisplayIntent.putExtra(PlayerSelectActivity.ARG_PLAYER_LIST, displayPlayerList.toArray());
			playerDisplayIntent.putExtra(PlayerSelectActivity.ARG_IS_MULTI_SELECT, false);
			playerDisplayIntent.putIntegerArrayListExtra(
					PlayerSelectActivity.ARG_ASSOCIATED_PLAYERS, associatedPlayers);

			startActivityForResult(playerDisplayIntent, reqCode);
		} else {
			Toast.makeText(this,
					"Select the team and players prior to selecting captain/wiki", Toast.LENGTH_SHORT).show();
		}
	}

	private void displayTeamData() {
		if (selectedTeam != null) {
			tvTeamName.setVisibility(View.VISIBLE);
			tvTeamName.setText(selectedTeam.getName().toUpperCase());

			btnSelectPlayers.setVisibility(View.VISIBLE);
			List<Player> matchPlayers = selectedTeam.getMatchPlayers();
			if (matchPlayers != null) {
				StringBuilder teamSB = new StringBuilder("Playing Team:\n");
				for (int i = 1; i <= matchPlayers.size(); i++) {
					teamSB.append(i);
					teamSB.append(". ");
					teamSB.append(matchPlayers.get(i - 1).getName());
					if (i < matchPlayers.size())
						teamSB.append("\n");
				}

				tvPlayerList.setVisibility(View.VISIBLE);
				tvPlayerList.setText(teamSB.toString());
				btnSelectCaptain.setVisibility(View.VISIBLE);
				btnSelectWK.setVisibility(View.VISIBLE);
			} else {
				tvPlayerList.setVisibility(View.GONE);
				btnSelectCaptain.setVisibility(View.GONE);
				btnSelectWK.setVisibility(View.GONE);
			}

			if (selectedTeam.getCaptain() != null) {
				tvCaptain.setVisibility(View.VISIBLE);
				tvCaptain.setText(String.format(getString(R.string.captainName), selectedTeam.getCaptain().getName()));
			} else {
				tvCaptain.setVisibility(View.GONE);
			}

			if (selectedTeam.getWicketKeeper() != null) {
				tvWK.setVisibility(View.VISIBLE);
				tvWK.setText(String.format(getString(R.string.wkName), selectedTeam.getWicketKeeper().getName()));
			} else {
				tvWK.setVisibility(View.GONE);
			}
		} else {
			btnSelectPlayers.setVisibility(View.GONE);
			btnSelectCaptain.setVisibility(View.GONE);
			btnSelectWK.setVisibility(View.GONE);

			tvTeamName.setVisibility(View.GONE);
			tvPlayerList.setVisibility(View.GONE);
			tvCaptain.setVisibility(View.GONE);
			tvWK.setVisibility(View.GONE);
		}
	}

	private void sendResponse(int resultCode) {
		Intent respIntent = new Intent();

		if (resultCode == RESP_CODE_OK)
			respIntent.putExtra(ARG_SEL_TEAM, selectedTeam);

		setResult(resultCode, respIntent);
		finish();
	}

	private void validateInput() {
		String errorMessage = null;

		if (selectedTeam == null) {
			errorMessage = getResources().getString(R.string.PTS_selectTeam);
		} else if (selectedTeam.getMatchPlayers() == null) {
			errorMessage = getResources().getString(R.string.PTS_selectPlayersForTeam);
		} else if (selectedTeam.getCaptain() == null || selectedTeam.getWicketKeeper() == null) {
			errorMessage = getResources().getString(R.string.PTS_selectCapAndWK);
		} else if (!selectedTeam.contains(selectedTeam.getCaptain())) {
			errorMessage = String.format(Locale.getDefault(),
					getResources().getString(R.string.PTS_captNotInTeam),
					selectedTeam.getCaptain().getName());
		} else if (!selectedTeam.contains(selectedTeam.getWicketKeeper())) {
			errorMessage = String.format(Locale.getDefault(),
					getResources().getString(R.string.PTS_wkNotInTeam),
					selectedTeam.getWicketKeeper().getName());
		}

		if (errorMessage != null) {
			Toast.makeText(this, errorMessage.trim(), Toast.LENGTH_LONG).show();
		} else {
			sendResponse(RESP_CODE_OK);
		}
	}

	private boolean hasEnoughPlayers() {
		TeamDBHandler dbh = new TeamDBHandler(this);

		if (selectedTeam != null) {
			return (dbh.getAssociatedPlayers(selectedTeam.getId()).size() >= numPlayers);
		}

		return false;
	}
}
