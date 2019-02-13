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
import android.widget.TextView;
import android.widget.Toast;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.activity.PlayerSelectActivity;
import com.theNewCone.cricketScoreCard.comparator.TeamComparator;
import com.theNewCone.cricketScoreCard.intf.DialogItemClickListener;
import com.theNewCone.cricketScoreCard.intf.DrawerController;
import com.theNewCone.cricketScoreCard.match.Match;
import com.theNewCone.cricketScoreCard.match.Team;
import com.theNewCone.cricketScoreCard.player.Player;
import com.theNewCone.cricketScoreCard.tournament.Group;
import com.theNewCone.cricketScoreCard.tournament.MatchInfo;
import com.theNewCone.cricketScoreCard.tournament.Tournament;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;
import com.theNewCone.cricketScoreCard.utils.database.MatchDBHandler;
import com.theNewCone.cricketScoreCard.utils.database.PlayerDBHandler;
import com.theNewCone.cricketScoreCard.utils.database.TeamDBHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class NewMatchFragment extends Fragment
    implements View.OnClickListener, DialogItemClickListener {

	private final int REQ_CODE_PLAYER_SELECT_TEAM1 = 1;
	private final int REQ_CODE_PLAYER_SELECT_TEAM2 = 2;
	private final int REQ_CODE_SELECT_CAPTAIN_TEAM1 = 3;
	private final int REQ_CODE_SELECT_CAPTAIN_TEAM2 = 4;
	private final int REQ_CODE_SELECT_WK_TEAM1 = 5;
	private final int REQ_CODE_SELECT_WK_TEAM2 = 6;

	private final String ENUM_TYPE_TEAM1 = "Team1Select";
	private final String ENUM_TYPE_TEAM2 = "Team2Select";

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

	List<Player> team1Players, team2Players;

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
			case R.id.tvTeam1:
				displayTeamSelect(ENUM_TYPE_TEAM1);
				break;

			case R.id.tvTeam2:
				displayTeamSelect(ENUM_TYPE_TEAM2);
				break;

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
				displayPlayerSelect(team1, REQ_CODE_PLAYER_SELECT_TEAM1);
				break;

			case R.id.btnNMSelectTeam2:
				displayPlayerSelect(team2, REQ_CODE_PLAYER_SELECT_TEAM2);
				break;

			case R.id.tvTeam1Captain:
				if(team1 != null) {
					displayCaptainWKSelect(team1, REQ_CODE_SELECT_CAPTAIN_TEAM1, team1.getCaptain());
				} else {
					Toast.makeText(getContext(),
							getResources().getString(R.string.NM_selectTeamForCapAndWK).trim(),
							Toast.LENGTH_SHORT).show();
				}
				break;

			case R.id.tvTeam1WK:
				if(team1 != null) {
					displayCaptainWKSelect(team1, REQ_CODE_SELECT_WK_TEAM1, team1.getWicketKeeper());
				} else {
					Toast.makeText(getContext(),
							getResources().getString(R.string.NM_selectTeamForCapAndWK).trim(),
							Toast.LENGTH_SHORT).show();
				}
				break;

			case R.id.tvTeam2Captain:
				if(team2 != null) {
					displayCaptainWKSelect(team2, REQ_CODE_SELECT_CAPTAIN_TEAM2, team2.getCaptain());
				} else {
					Toast.makeText(getContext(),
							getResources().getString(R.string.NM_selectTeamForCapAndWK).trim(),
							Toast.LENGTH_SHORT).show();
				}
				break;

			case R.id.tvTeam2WK:
				if(team2 != null) {
					displayCaptainWKSelect(team2, REQ_CODE_SELECT_WK_TEAM2, team2.getWicketKeeper());
				} else {
					Toast.makeText(getContext(),
							getResources().getString(R.string.NM_selectTeamForCapAndWK).trim(),
							Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	@Override
	public void onItemSelect(String type, String value, int position) {
		switch (type) {
			case ENUM_TYPE_TEAM1:
				team1 = teams.get(position);
				tvTeam1.setText(value);
				rbTossTeam1.setText(team1.getShortName());
				setMatchName();
				btnNMSelectTeam1.setVisibility(View.VISIBLE);
				break;

			case ENUM_TYPE_TEAM2:
				team2 = teams.get(position);
				tvTeam2.setText(value);
				rbTossTeam2.setText(team2.getShortName());
				setMatchName();
				btnNMSelectTeam2.setVisibility(View.VISIBLE);
				break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case REQ_CODE_PLAYER_SELECT_TEAM1:
				if(resultCode == PlayerSelectActivity.RESP_CODE_OK) {
					team1Players = Arrays.asList
							(CommonUtils.objectArrToPlayerArr(
									(Object []) data.getSerializableExtra(PlayerSelectActivity.ARG_RESP_SEL_PLAYERS)));
					team1.setMatchPlayers(team1Players);
					tvTeam1Capt.setVisibility(View.VISIBLE);

					String captain = team1.getCaptain() != null ? team1.getCaptain().getName() : getResources().getString(R.string.none);
					String wk = team1.getWicketKeeper() != null ? team1.getWicketKeeper().getName() : getResources().getString(R.string.none);

					tvTeam1Capt.setText(String.format(getString(R.string.captainName), captain));
					tvTeam1WK.setVisibility(View.VISIBLE);
					tvTeam1WK.setText(String.format(getString(R.string.wkName), wk));
				}
				break;

			case REQ_CODE_PLAYER_SELECT_TEAM2:
				if(resultCode == PlayerSelectActivity.RESP_CODE_OK) {
					team2Players = Arrays.asList(
							CommonUtils.objectArrToPlayerArr(
									(Object []) data.getSerializableExtra(PlayerSelectActivity.ARG_RESP_SEL_PLAYERS)));
					team2.setMatchPlayers(team2Players);

					String captain = team2.getCaptain() != null ? team2.getCaptain().getName() : getResources().getString(R.string.none);
					String wk = team2.getWicketKeeper() != null ? team2.getWicketKeeper().getName() : getResources().getString(R.string.none);

					tvTeam2Capt.setVisibility(View.VISIBLE);
					tvTeam2Capt.setText(String.format(getString(R.string.captainName), captain));
					tvTeam2WK.setVisibility(View.VISIBLE);
					tvTeam2WK.setText(String.format(getString(R.string.wkName), wk));
				}
				break;

			case REQ_CODE_SELECT_CAPTAIN_TEAM1:
				if(resultCode == PlayerSelectActivity.RESP_CODE_OK) {
					Player captain = (Player) data.getSerializableExtra(PlayerSelectActivity.ARG_RESP_SEL_PLAYER);
					team1.setCaptain(captain);
					tvTeam1Capt.setText(String.format(getString(R.string.captainName), captain.getName()));
				}
				break;

			case REQ_CODE_SELECT_WK_TEAM1:
				if(resultCode == PlayerSelectActivity.RESP_CODE_OK) {
					Player wk = (Player) data.getSerializableExtra(PlayerSelectActivity.ARG_RESP_SEL_PLAYER);
					team1.setWicketKeeper(wk);
					tvTeam1WK.setText(String.format(getString(R.string.wkName), wk.getName()));
				}
				break;

			case REQ_CODE_SELECT_CAPTAIN_TEAM2:
				if(resultCode == PlayerSelectActivity.RESP_CODE_OK) {
					Player captain = (Player) data.getSerializableExtra(PlayerSelectActivity.ARG_RESP_SEL_PLAYER);
					team2.setCaptain(captain);
					tvTeam2Capt.setText(String.format(getString(R.string.captainName), captain.getName()));
				}
				break;

			case REQ_CODE_SELECT_WK_TEAM2:
				if(resultCode == PlayerSelectActivity.RESP_CODE_OK) {
					Player wk = (Player) data.getSerializableExtra(PlayerSelectActivity.ARG_RESP_SEL_PLAYER);
					team2.setWicketKeeper(wk);
					tvTeam2WK.setText(String.format(getString(R.string.wkName), wk.getName()));
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

		tvTeam1.setOnClickListener(this);
		tvTeam2.setOnClickListener(this);
		tvTeam1Capt.setOnClickListener(this);
		tvTeam1WK.setOnClickListener(this);
		tvTeam2Capt.setOnClickListener(this);
		tvTeam2WK.setOnClickListener(this);

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
		tvTeam1.setEnabled(false);
		tvTeam2.setEnabled(false);

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

		if(team1 != null) {
			tvTeam1.setText(team1.getName());
			btnNMSelectTeam1.setVisibility(View.VISIBLE);
			if(team1Players != null) {
				tvTeam1Capt.setVisibility(View.VISIBLE);
				if(team1.getCaptain() != null) {
					tvTeam1Capt.setText(team1.getCaptain().getName());
				}
				tvTeam1WK.setVisibility(View.VISIBLE);
				if(team1.getWicketKeeper() != null) {
					tvTeam1WK.setText(team1.getWicketKeeper().getName());
				}
			}
		}

		if(team2 != null) {
			tvTeam2.setText(team2.getName());
			btnNMSelectTeam2.setVisibility(View.VISIBLE);
			if(team2Players != null) {
				tvTeam2Capt.setVisibility(View.VISIBLE);
				if(team2.getCaptain() != null) {
					tvTeam2Capt.setText(team2.getCaptain().getName());
				}
				tvTeam2WK.setVisibility(View.VISIBLE);
				if(team2.getWicketKeeper() != null) {
					tvTeam2WK.setText(team2.getWicketKeeper().getName());
				}
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

	private void setMatchName() {
		if(team1 != null && team2 != null && team1.getId() != team2.getId()) {
			String teamName = etMatchName.getText().toString();
			if(teamName.length() == 0) {
				teamName = team1.getShortName() + " v " + team2.getShortName() + " " + CommonUtils.currTimestamp("yyyyMMMdd");
				etMatchName.setText(teamName);
				etMatchName.requestFocus();
			}
		}
	}

	private void setTournamentMatchName() {
		String versus = team1.getShortName() + "v" + team2.getShortName();
		StringBuilder teamNameSB = new StringBuilder(tournament.getName());
		teamNameSB.append(":");
		switch (tournament.getFormat()) {
			case KNOCK_OUT:
			case GROUPS:
			case ROUND_ROBIN:
				teamNameSB.append(group.getStage().getTag());
				teamNameSB.append(":");
				teamNameSB.append(versus);
				break;

			case BILATERAL:
				int matchNum = 0;
				for (MatchInfo groupMatchInfo : group.getMatchInfoList()) {
					matchNum++;
					if (matchInfo.getId() == groupMatchInfo.getId()) {
						break;
					}
				}
				teamNameSB.append("Match-");
				teamNameSB.append(matchNum);
				break;
		}

		etMatchName.setText(teamNameSB);
		etMatchName.setEnabled(false);
	}

	private void displayTeamSelect(String teamSelect) {
		if(getFragmentManager() != null) {
			String[] teamDetails = new String[teams.size()];

			int i=0;
			int ignoreTeam = -1;

			for(Team team : teams) {
				if(team.getId() != ignoreTeam) {
					teamDetails[i++] = team.getName();
				}
			}

			StringDialog dialog = StringDialog.newInstance("Select Team", teamDetails, teamSelect);
			dialog.setDialogItemClickListener(this);
			dialog.show(getFragmentManager(), teamSelect + "Dialog");
		}
	}

	private void displayPlayerSelect(Team team, int reqCode) {
		List<Player> displayPlayerList = new PlayerDBHandler(getContext()).getTeamPlayers(team.getId());
		if(displayPlayerList != null && displayPlayerList.size() > 0) {
			if(displayPlayerList.size() >= numPlayers) {
				ArrayList<Integer> associatedPlayers = new ArrayList<>();

				List<Player> teamPlayers = team.getMatchPlayers();

				for (int i = 0; i < numPlayers; i++) {
					associatedPlayers.add(
							(teamPlayers == null || teamPlayers.size() == 0)
									? displayPlayerList.get(i).getID()
									: teamPlayers.get(i).getID()
					);
				}

				Intent playerDisplayIntent = new Intent(getContext(), PlayerSelectActivity.class);
				playerDisplayIntent.putExtra(PlayerSelectActivity.ARG_PLAYER_LIST, displayPlayerList.toArray());
				playerDisplayIntent.putExtra(PlayerSelectActivity.ARG_IS_MULTI_SELECT, true);
				playerDisplayIntent.putIntegerArrayListExtra(
						PlayerSelectActivity.ARG_ASSOCIATED_PLAYERS, associatedPlayers);
				playerDisplayIntent.putExtra(PlayerSelectActivity.ARG_NUM_PLAYERS, numPlayers);

				startActivityForResult(playerDisplayIntent, reqCode);
			} else {
				Toast.makeText(getContext(),
						String.format(Locale.getDefault(), getResources().getString(R.string.NM_notEnoughPlayers), team.getShortName()).trim(),
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(getContext(),
					String.format(Locale.getDefault(),
							getResources().getString(R.string.NM_noPlayersInTeam),
							team.getShortName()).trim(),
					Toast.LENGTH_LONG).show();
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

		String deficientTeam = getTeamHavingInsufficientPlayers(numPlayers);

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
		} else if(deficientTeam != null) {
			errorMessage = getResources().getString(R.string.NM_notEnoughPlayersIn) + deficientTeam;
		} else if (team1.getMatchPlayers() == null) {
			errorMessage = String.format(Locale.getDefault(),
					getResources().getString(R.string.NM_selectPlayersForTeam),
					team1.getShortName());
		} else if(team2.getMatchPlayers() == null) {
			errorMessage = String.format(Locale.getDefault(),
					getResources().getString(R.string.NM_selectPlayersForTeam),
					team2.getShortName());
		} else if(dupPlayer != null) {
			errorMessage = String.format(Locale.getDefault(),
					getResources().getString(R.string.NM_duplicatePlayer),
					dupPlayer.getName());
		} else if(team1.getMatchPlayers().size() != numPlayers || team2.getMatchPlayers().size() != numPlayers) {
			errorMessage = getResources().getString(R.string.NM_playerCountChanged);
		}else if (team1.getCaptain() == null || team1.getWicketKeeper() == null) {
			errorMessage = String.format(Locale.getDefault(),
					getResources().getString(R.string.NM_selectCapAndWK),
					team1.getShortName());
		} else if (team2.getCaptain() == null || team2.getWicketKeeper() == null) {
			errorMessage = String.format(Locale.getDefault(),
					getResources().getString(R.string.NM_selectCapAndWK),
					team2.getShortName());
		} else if (!team1.contains(team1.getCaptain())) {
			errorMessage = String.format(Locale.getDefault(),
					getResources().getString(R.string.NM_captNotInTeam),
					team1.getCaptain().getName(), team1.getShortName());
		} else if (!team1.contains(team1.getWicketKeeper())) {
			errorMessage = String.format(Locale.getDefault(),
					getResources().getString(R.string.NM_wkNotInTeam),
					team1.getWicketKeeper().getName(), team1.getShortName());
		} else if (!team2.contains(team2.getCaptain())) {
			errorMessage = String.format(Locale.getDefault(),
					getResources().getString(R.string.NM_captNotInTeam),
					team2.getCaptain().getName(), team2.getShortName());
		} else if (!team2.contains(team2.getWicketKeeper())) {
			errorMessage = String.format(Locale.getDefault(),
					getResources().getString(R.string.NM_wkNotInTeam),
					team2.getWicketKeeper().getName(), team2.getShortName());
		}

		if(errorMessage != null) {
			Toast.makeText(getContext(), errorMessage.trim(), Toast.LENGTH_LONG).show();
		} else {
			setLayoutForMatchStart();
			Toast.makeText(getContext(), "Validation successful" , Toast.LENGTH_SHORT).show();
		}
	}

	private String getTeamHavingInsufficientPlayers(int numPlayers) {
		TeamDBHandler dbh = new TeamDBHandler(getContext());

		if(team1 != null && team2 != null) {
			if (dbh.getAssociatedPlayers(team1.getId()).size() < numPlayers)
				return team1.getShortName();
			else if (dbh.getAssociatedPlayers(team2.getId()).size() < numPlayers)
				return team2.getShortName();
		}

		return null;
	}

	private void displayCaptainWKSelect(Team team, int reqCode, Player selected) {
		if(team != null && team.getMatchPlayers() != null) {
			List<Player> displayPlayerList = team.getMatchPlayers();
			ArrayList<Integer> associatedPlayers = new ArrayList<>();

			if (selected != null)
				associatedPlayers.add(selected.getID());

			Intent playerDisplayIntent = new Intent(getContext(), PlayerSelectActivity.class);
			playerDisplayIntent.putExtra(PlayerSelectActivity.ARG_PLAYER_LIST, displayPlayerList.toArray());
			playerDisplayIntent.putExtra(PlayerSelectActivity.ARG_IS_MULTI_SELECT, false);
			playerDisplayIntent.putIntegerArrayListExtra(
					PlayerSelectActivity.ARG_ASSOCIATED_PLAYERS, associatedPlayers);

			startActivityForResult(playerDisplayIntent, reqCode);
		} else {
			Toast.makeText(getContext(),
					"Select the team and players prior to selecting captain/wiki", Toast.LENGTH_SHORT).show();
		}
	}

	private Player checkForCommonPlayers(){
    	Player dupPlayer = null;

    	if(team1 != null && team2 != null && team1Players != null && team2Players != null) {
			for (Player player : team1Players) {
				if (team2.contains(player)) {
					dupPlayer = player;
					break;
				}
			}
			for (Player player : team2Players) {
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
        tvTeam1.setEnabled(false);
        tvTeam2.setEnabled(false);
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
