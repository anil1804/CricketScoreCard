package com.theNewCone.cricketScoreCard.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.activity.PlayingTeamSelectActivity;
import com.theNewCone.cricketScoreCard.comparator.TeamComparator;
import com.theNewCone.cricketScoreCard.intf.DrawerController;
import com.theNewCone.cricketScoreCard.match.Match;
import com.theNewCone.cricketScoreCard.match.Team;
import com.theNewCone.cricketScoreCard.player.Player;
import com.theNewCone.cricketScoreCard.tournament.Group;
import com.theNewCone.cricketScoreCard.tournament.MatchInfo;
import com.theNewCone.cricketScoreCard.tournament.Tournament;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;
import com.theNewCone.cricketScoreCard.utils.database.MatchDBHandler;
import com.theNewCone.cricketScoreCard.utils.database.TeamDBHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class NewMatchFragment extends Fragment
		implements View.OnClickListener {

	private final int REQ_CODE_SELECT_TEAM1 = 1;
	private final int REQ_CODE_SELECT_TEAM2 = 2;

	private Tournament tournament = null;
	private MatchInfo matchInfo = null;
	private Group group = null;
	private boolean isTournament = false;

    EditText etMatchName, etMaxOvers, etMaxWickets, etMaxPerBowler, etNumPlayers;
    TextView tvTeam1, tvTeam2, tvTeam1Capt, tvTeam1WK, tvTeam2Capt, tvTeam2WK;
    Button btnCancel, btnValidate, btnStartMatch, btnManageTeam, btnNMSelectTeam1, btnNMSelectTeam2;
    List<Team> teams;
    Team team1, team2, tossWonBy, battingTeam, bowlingTeam;
    RadioGroup rgToss, rgTossChoose;
    RadioButton rbTossTeam1, rbTossTeam2, rbTossBat, rbTossBowl;
    ScrollView svNewMatch;
	TableLayout tlTeamData;

	int maxPerBowler = 0, maxOvers, maxWickets, numPlayers = 0;

    public NewMatchFragment() {
        // Required empty public constructor
    }

    public static NewMatchFragment newInstance() {
		return new NewMatchFragment();
    }

	public static NewMatchFragment newInstance(Tournament tournament, Group group, MatchInfo matchInfo) {
		NewMatchFragment fragment = new NewMatchFragment();
		fragment.tournament = tournament;
		fragment.group = group;
		fragment.matchInfo = matchInfo;
		fragment.isTournament = true;
		return fragment;
	}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View theView = inflater.inflate(R.layout.fragment_new_match, container, false);

		if(getActivity() != null) {
			DrawerController drawerController = (DrawerController) getActivity();
			drawerController.setDrawerEnabled(true);
			drawerController.enableAllDrawerMenuItems();
			getActivity().setTitle(getString(R.string.title_fragment_new_match));
		}

		loadViews(theView);

		if (isTournament)
			updateViewForTournament(theView);

        updateView(theView);

        return theView;
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnValidate:
				validateInput();
				break;

			case R.id.btnStartMatch:
				startNewMatch();
				break;

			case R.id.btnCancel:
				if(getActivity() != null)
					getActivity().onBackPressed();
				break;

			case R.id.btnManageTeam:
				if(getActivity() != null) {
					String fragmentTag = TeamFragment.class.getSimpleName();
					TeamFragment fragment = TeamFragment.newInstance();

					getActivity().getSupportFragmentManager().beginTransaction()
							.replace(R.id.frame_container, fragment, fragmentTag)
							.addToBackStack(fragmentTag)
							.commit();
				}
				break;

			case R.id.rbTossTeam1:
				tossWonBy = team1;
				rgTossChoose.setVisibility(View.VISIBLE);
				scrollToBottom();
				break;

			case R.id.rbTossTeam2:
				tossWonBy = team2;
				rgTossChoose.setVisibility(View.VISIBLE);
				scrollToBottom();
				break;

			case R.id.rbTossBat:
				if(tossWonBy.getId() == team1.getId()) {
					battingTeam = team1;
					bowlingTeam = team2;
				} else {
					battingTeam = team2;
					bowlingTeam = team1;
				}
				btnStartMatch.setVisibility(View.VISIBLE);
				scrollToBottom();
				break;

			case R.id.rbTossBowl:
				if(tossWonBy.getId() == team1.getId()) {
					battingTeam = team2;
					bowlingTeam = team1;
				} else {
					battingTeam = team1;
					bowlingTeam = team2;
				}
				btnStartMatch.setVisibility(View.VISIBLE);
				scrollToBottom();
				break;

			case R.id.btnNMSelectTeam1:
				displayTeamSelect(REQ_CODE_SELECT_TEAM1);
				break;

			case R.id.btnNMSelectTeam2:
				displayTeamSelect(REQ_CODE_SELECT_TEAM2);
				break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case REQ_CODE_SELECT_TEAM1:
				if (resultCode == PlayingTeamSelectActivity.RESP_CODE_OK) {
					team1 = (Team) data.getSerializableExtra(PlayingTeamSelectActivity.ARG_SEL_TEAM);
				}
				if (team1 != null) {
					tlTeamData.setVisibility(View.VISIBLE);
					tvTeam1.setText(team1.getName());
					if (team1.getCaptain() != null)
						tvTeam1Capt.setText(team1.getCaptain().getName());
					if (team1.getWicketKeeper() != null)
						tvTeam1WK.setText(team1.getWicketKeeper().getName());

					updateMatchInfo();
				}
				break;

			case REQ_CODE_SELECT_TEAM2:
				if (resultCode == PlayingTeamSelectActivity.RESP_CODE_OK) {
					team2 = (Team) data.getSerializableExtra(PlayingTeamSelectActivity.ARG_SEL_TEAM);
				}
				if (team2 != null) {
					tlTeamData.setVisibility(View.VISIBLE);
					tvTeam2.setText(team2.getName());
					if (team2.getCaptain() != null)
						tvTeam2Capt.setText(team2.getCaptain().getName());
					if (team2.getWicketKeeper() != null)
						tvTeam2WK.setText(team2.getWicketKeeper().getName());

					updateMatchInfo();
				}
				break;
		}
	}

	private void loadViews(View theView) {
		etMatchName = theView.findViewById(R.id.etMatchName);
		etMaxOvers = theView.findViewById(R.id.etMaxOvers);
		etMaxWickets = theView.findViewById(R.id.etMaxWickets);
		etMaxPerBowler = theView.findViewById(R.id.etMaxPerBowler);
		etNumPlayers = theView.findViewById(R.id.etNumPlayers);

		etMaxWickets.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void afterTextChanged(Editable editable) {
				if(editable != null && !editable.toString().equals("")) {
					if (maxWickets != Integer.parseInt(editable.toString())) {
						maxWickets = Integer.parseInt(editable.toString());
						updateNumPlayers();
						updateMaxPerBowler();
					}
				}
			}
		});

		etMaxOvers.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void afterTextChanged(Editable editable) {
				if(editable != null && !editable.toString().equals("")) {
					if (maxOvers != Integer.parseInt(editable.toString())) {
						maxOvers = Integer.parseInt(editable.toString());
						updateMaxPerBowler();
					}
				}
			}
		});

		etNumPlayers.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void afterTextChanged(Editable editable) {
				if(editable != null && !editable.toString().equals("")) {
					if (numPlayers != Integer.parseInt(editable.toString())) {
						numPlayers = Integer.parseInt(editable.toString());
						updateMaxPerBowler();
					}
				}
			}
		});

		tlTeamData = theView.findViewById(R.id.tlNMTeamData);

		tvTeam1 = theView.findViewById(R.id.tvTeam1);
		tvTeam2 = theView.findViewById(R.id.tvTeam2);
		tvTeam1Capt = theView.findViewById(R.id.tvTeam1Captain);
		tvTeam1WK = theView.findViewById(R.id.tvTeam1WK);
		tvTeam2Capt = theView.findViewById(R.id.tvTeam2Captain);
		tvTeam2WK = theView.findViewById(R.id.tvTeam2WK);

		btnCancel = theView.findViewById(R.id.btnMatchCancel);
		btnValidate = theView.findViewById(R.id.btnValidate);
		btnStartMatch = theView.findViewById(R.id.btnStartMatch);
		btnManageTeam = theView.findViewById(R.id.btnManageTeam);
		btnNMSelectTeam1 = theView.findViewById(R.id.btnNMSelectTeam1);
		btnNMSelectTeam2 = theView.findViewById(R.id.btnNMSelectTeam2);

		rgToss = theView.findViewById(R.id.rgToss);
		rgTossChoose = theView.findViewById(R.id.rgTossChoose);

		rbTossTeam1 = theView.findViewById(R.id.rbTossTeam1);
		rbTossTeam2 = theView.findViewById(R.id.rbTossTeam2);
		rbTossBat = theView.findViewById(R.id.rbTossBat);
		rbTossBowl = theView.findViewById(R.id.rbTossBowl);

		svNewMatch = theView.findViewById(R.id.svNewMatch);

		btnCancel.setOnClickListener(this);
		btnValidate.setOnClickListener(this);
		btnStartMatch.setOnClickListener(this);
		btnManageTeam.setOnClickListener(this);
		btnNMSelectTeam1.setOnClickListener(this);
		btnNMSelectTeam2.setOnClickListener(this);

		rbTossTeam1.setOnClickListener(this);
		rbTossTeam2.setOnClickListener(this);
		rbTossBat.setOnClickListener(this);
		rbTossBowl.setOnClickListener(this);
	}

	private void updateViewForTournament(View view) {
		team1 = matchInfo.getTeam1();
		team2 = matchInfo.getTeam2();

		setTournamentMatchName();

		tvTeam1.setText(matchInfo.getTeam1().getName());
		tvTeam2.setText(matchInfo.getTeam2().getName());

		maxOvers = tournament.getMaxOvers();
		maxPerBowler = tournament.getMaxPerBowler();
		maxWickets = tournament.getMaxWickets();
		numPlayers = tournament.getPlayers();

		view.findViewById(R.id.glSingleMatch).setVisibility(View.GONE);
		view.findViewById(R.id.glTournamentMatch).setVisibility(View.VISIBLE);

		TextView tvMaxOvers = view.findViewById(R.id.tvMaxOvers);
		TextView tvMaxPerBowler = view.findViewById(R.id.tvMaxPerBowler);
		TextView tvMaxWickets = view.findViewById(R.id.tvMaxWickets);
		TextView tvNumPlayers = view.findViewById(R.id.tvNumPlayers);

		tvMaxOvers.setText(String.valueOf(maxOvers));
		tvMaxPerBowler.setText(String.valueOf(maxPerBowler));
		tvMaxWickets.setText(String.valueOf(maxWickets));
		tvNumPlayers.setText(String.valueOf(numPlayers));

		rbTossTeam1.setText(team1.getShortName());
		rbTossTeam2.setText(team2.getShortName());
	}

	private void updateView(View theView) {
		if (!isTournament) {
			getTeams();

			if (teams != null && teams.size() < 2) {
				theView.findViewById(R.id.llNewMatch).setVisibility(View.GONE);
				theView.findViewById(R.id.llInsufficientTeams).setVisibility(View.VISIBLE);

				TextView tvInsufficientTeams = theView.findViewById(R.id.tvInsufficientTeams);
				String insufficientTeamsText =
						((teams.size() == 0)
								? getResources().getString(R.string.NM_noTeamsAvailable)
								: getResources().getString(R.string.NM_onlyOneTeamAvailable))
								+ getResources().getString(R.string.NM_needMinimumTwoTeams).trim();
				tvInsufficientTeams.setText(insufficientTeamsText);
			} else {
				maxOvers = etMaxOvers.getText().toString().equals("")
						? 0 : Integer.parseInt(etMaxOvers.getText().toString());
				maxWickets = etMaxWickets.getText().toString().equals("")
						? 0 : Integer.parseInt(etMaxWickets.getText().toString());
				updateNumPlayers();
				updateMaxPerBowler();
			}
		}
	}

    private void updateMaxPerBowler() {
		maxPerBowler = CommonUtils.updateMaxPerBowler(maxOvers, numPlayers);
		etMaxPerBowler.setText(String.valueOf(maxPerBowler));
	}

    private void updateNumPlayers() {
		numPlayers = CommonUtils.updateNumPlayers(maxWickets);
		etNumPlayers.setText(String.valueOf(numPlayers));
	}

	public void getTeams() {
		TeamDBHandler dbHandler = new TeamDBHandler(getContext());
		teams = dbHandler.getTeams(null, -1);
		Collections.sort(teams, new TeamComparator(null));
	}

	private void updateMatchInfo() {
		if (team1 != null && team2 != null && team1.getId() != team2.getId()
				&& team1.getMatchPlayers() != null && team2.getMatchPlayers() != null) {
			btnValidate.setVisibility(View.VISIBLE);
			rbTossTeam1.setText(team1.getShortName());
			rbTossTeam2.setText(team2.getShortName());

			String teamName = etMatchName.getText().toString();
			if(teamName.length() == 0) {
				teamName = team1.getShortName() + " v " + team2.getShortName() + " " + CommonUtils.currTimestamp("yyyyMMMdd");
				etMatchName.setText(teamName);
				etMatchName.requestFocus();
			}
		}
	}

	private void setTournamentMatchName() {
		StringBuilder teamNameSB = new StringBuilder();

		//Tournament Info
		teamNameSB.append("TMT-");
		teamNameSB.append(tournament.getId());

		//Group Info
		teamNameSB.append(":");
		teamNameSB.append("Grp-");
		teamNameSB.append(group.getGroupNumber());

		int matchNum = 0;
		for (MatchInfo groupMatchInfo : group.getMatchInfoList()) {
			if (matchInfo.getId() == groupMatchInfo.getId()) {
				matchNum = groupMatchInfo.getMatchNumber();
				break;
			}
		}

		//Match Info
		teamNameSB.append(":");
		teamNameSB.append("Match-");
		teamNameSB.append(matchNum);

		etMatchName.setText(teamNameSB);
		etMatchName.setEnabled(false);
	}

	private void displayTeamSelect(int reqCode) {
		if (numPlayers > 0) {
			Intent teamSelectIntent = new Intent(getContext(), PlayingTeamSelectActivity.class);
			ArrayList<Integer> ignoreTeamsList = new ArrayList<>();
			Team selectedTeam = null;
			if (reqCode == REQ_CODE_SELECT_TEAM1) {
				selectedTeam = team1;
				if (team2 != null)
					ignoreTeamsList.add(team2.getId());
			} else if (reqCode == REQ_CODE_SELECT_TEAM2) {
				selectedTeam = team2;
				if (team1 != null)
					ignoreTeamsList.add(team1.getId());
			}

			teamSelectIntent.putExtra(PlayingTeamSelectActivity.ARG_IS_TOURNAMENT, isTournament);
			teamSelectIntent.putExtra(PlayingTeamSelectActivity.ARG_NUM_PLAYERS, numPlayers);
			if (selectedTeam != null)
				teamSelectIntent.putExtra(PlayingTeamSelectActivity.ARG_SEL_TEAM, selectedTeam);
			if (ignoreTeamsList.size() > 0)
				teamSelectIntent.putExtra(PlayingTeamSelectActivity.ARG_IGNORE_TEAMS, ignoreTeamsList);

			startActivityForResult(teamSelectIntent, reqCode);
		} else {
			Toast.makeText(getContext(), "Provide Number Players before selecting team", Toast.LENGTH_SHORT).show();
		}
	}

	private void validateInput() {
    	if(!isTournament) {
			maxOvers = etMaxOvers.getText().toString().equals("")
					? 0 : Integer.parseInt(etMaxOvers.getText().toString());
			maxWickets = etMaxWickets.getText().toString().equals("")
					? 0 : Integer.parseInt(etMaxWickets.getText().toString());
			maxPerBowler = etMaxPerBowler.getText().toString().equals("")
					? 0 : Integer.parseInt(etMaxPerBowler.getText().toString());
			numPlayers = etNumPlayers.getText().toString().equals("")
					? 0 : Integer.parseInt(etNumPlayers.getText().toString());
		}

		String errorMessage = null;
		Player dupPlayer = checkForCommonPlayers();

		if(team1 == null || team2 == null) {
			errorMessage = getResources().getString(R.string.NM_selectBothTeams);
		} else if(team1.getId() == team2.getId()) {
			errorMessage = getResources().getString(R.string.NM_selectDifferentTeams);
		} else if (etMatchName.getText().toString().length() <= 5) {
			errorMessage = getResources().getString(R.string.NM_invalidMatchName);
		} else if(maxOvers <= 0 || maxWickets <= 0 || maxPerBowler <= 0 || numPlayers <= 0) {
			errorMessage = getResources().getString(R.string.NM_invalidPlayersOversWickets);
		} else if(maxWickets >= numPlayers) {
			errorMessage = getResources().getString(R.string.NM_wicketsMoreThanPlayers);
		} else if(maxPerBowler > maxOvers) {
			errorMessage = getResources().getString(R.string.NM_bowlerOversMoreThanMaxOvers);
		} else if(maxPerBowler < ((maxOvers % numPlayers == 0) ? maxOvers/numPlayers : (maxOvers/numPlayers + 1))) {
			errorMessage = getResources().getString(R.string.NM_notEnoughBowlers);
		} else if(dupPlayer != null) {
			errorMessage = String.format(Locale.getDefault(),
					getResources().getString(R.string.NM_duplicatePlayer),
					dupPlayer.getName());
		} else if(team1.getMatchPlayers().size() != numPlayers || team2.getMatchPlayers().size() != numPlayers) {
			errorMessage = getResources().getString(R.string.NM_playerCountChanged);
		}

		if(errorMessage != null) {
			Toast.makeText(getContext(), errorMessage.trim(), Toast.LENGTH_LONG).show();
		} else {
			setLayoutForMatchStart();
			Toast.makeText(getContext(), "Validation successful" , Toast.LENGTH_SHORT).show();
		}
	}

	private Player checkForCommonPlayers(){
    	Player dupPlayer = null;

		if (team1 != null && team2 != null && team1.getMatchPlayers() != null && team2.getMatchPlayers() != null) {
			for (Player player : team1.getMatchPlayers()) {
				if (team2.contains(player)) {
					dupPlayer = player;
					break;
				}
			}
			for (Player player : team2.getMatchPlayers()) {
				if (team1.contains(player)) {
					dupPlayer = player;
					break;
				}
			}
		}

    	return dupPlayer;
	}

	private void startNewMatch() {
		MatchDBHandler dbh = new MatchDBHandler(getContext());
		int matchID = dbh.addNewMatch(new Match(etMatchName.getText().toString(), battingTeam, bowlingTeam));

		if (matchID == dbh.CODE_NEW_MATCH_DUP_RECORD) {
			Toast.makeText(getContext(),
					"A match with the same name already exists\nUse different name or use Load match functionality",
					Toast.LENGTH_LONG).show();
		} else {
			if (getActivity() != null) {
				FragmentManager fragMgr = getActivity().getSupportFragmentManager();
				String fragmentTag = LimitedOversFragment.class.getSimpleName();
				LimitedOversFragment fragment =
						LimitedOversFragment.newInstance(matchID, etMatchName.getText().toString(),
								battingTeam, bowlingTeam, tossWonBy,
								maxOvers, maxWickets, maxPerBowler, matchInfo);

				fragMgr.beginTransaction()
						.replace(R.id.frame_container, fragment, fragmentTag)
						.commit();
			}
		}
	}

	private void setLayoutForMatchStart() {
        btnNMSelectTeam1.setEnabled(false);
        btnNMSelectTeam2.setEnabled(false);
        etMaxOvers.setEnabled(false);
        etMaxPerBowler.setEnabled(false);
        etMaxWickets.setEnabled(false);
        etNumPlayers.setEnabled(false);

		scrollToBottom();
        rgToss.setVisibility(View.VISIBLE);
    }

	private void scrollToBottom() {
		svNewMatch.postDelayed(new Runnable() {
			@Override
			public void run() {
				svNewMatch.fullScroll(ScrollView.FOCUS_DOWN);
			}
		}, 1);
	}
}
