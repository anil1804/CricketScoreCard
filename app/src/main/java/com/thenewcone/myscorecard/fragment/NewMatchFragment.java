package com.thenewcone.myscorecard.fragment;


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
import android.widget.TextView;
import android.widget.Toast;

import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.activity.PlayerSelectActivity;
import com.thenewcone.myscorecard.intf.DialogItemClickListener;
import com.thenewcone.myscorecard.match.Team;
import com.thenewcone.myscorecard.player.Player;
import com.thenewcone.myscorecard.utils.CommonUtils;
import com.thenewcone.myscorecard.utils.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewMatchFragment extends Fragment
    implements View.OnClickListener, DialogItemClickListener {

	private final int REQ_CODE_PLAYER_SELECT_TEAM1 = 1;
	private final int REQ_CODE_PLAYER_SELECT_TEAM2 = 2;

	private final String ENUM_TYPE_TEAM1 = "Team1Select";
	private final String ENUM_TYPE_TEAM2 = "Team2Select";

    EditText etMatchName, etMaxOvers, etMaxWickets, etMaxPerBowler, etNumPlayers;
    TextView tvTeam1, tvTeam2;
    Button btnCancel, btnValidate, btnStartMatch, btnManageTeam;
    List<Team> teams;
    Team team1, team2, tossWonBy, battingTeam, bowlingTeam;
    RadioGroup rgToss, rgTossChoose;
    RadioButton rbTossTeam1, rbTossTeam2, rbTossBat, rbTossBowl;

	List<Player> team1Players, team2Players;

	int maxPerBowler = 0, maxOvers, maxWickets, numPlayers = 0;

    public NewMatchFragment() {
        // Required empty public constructor
    }

    public static NewMatchFragment newInstance() {
        return new NewMatchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View theView = inflater.inflate(R.layout.fragment_new_match, container, false);

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

        tvTeam1 = theView.findViewById(R.id.tvTeam1);
        tvTeam2 = theView.findViewById(R.id.tvTeam2);

        btnCancel = theView.findViewById(R.id.btnMatchCancel);
        btnValidate = theView.findViewById(R.id.btnValidate);
        btnStartMatch = theView.findViewById(R.id.btnStartMatch);
        btnManageTeam = theView.findViewById(R.id.btnManageTeam);

        rgToss = theView.findViewById(R.id.rgToss);
		rgTossChoose = theView.findViewById(R.id.rgTossChoose);

		rbTossTeam1 = theView.findViewById(R.id.rbTossTeam1);
		rbTossTeam2 = theView.findViewById(R.id.rbTossTeam2);
		rbTossBat = theView.findViewById(R.id.rbTossBat);
		rbTossBowl = theView.findViewById(R.id.rbTossBowl);

        tvTeam1.setOnClickListener(this);
        tvTeam2.setOnClickListener(this);

        btnCancel.setOnClickListener(this);
        btnValidate.setOnClickListener(this);
        btnStartMatch.setOnClickListener(this);
        btnManageTeam.setOnClickListener(this);

        rbTossTeam1.setOnClickListener(this);
        rbTossTeam2.setOnClickListener(this);
        rbTossBat.setOnClickListener(this);
        rbTossBowl.setOnClickListener(this);

        updateView(theView);

        return theView;
    }

    private void updateView(View theView) {
        getTeams();

        if(teams.size() < 2) {
            theView.findViewById(R.id.llNewMatch).setVisibility(View.GONE);
            theView.findViewById(R.id.llInsufficientTeams).setVisibility(View.VISIBLE);

            TextView tvInsufficientTeams = theView.findViewById(R.id.tvInsufficientTeams);
            String insufficientTeamsText =
                    ((teams.size() == 0) ? "No Team Available." : "Only 1 Team available.")
                            + "\nNeed at-least 2 teams to play a match.";
            tvInsufficientTeams.setText(insufficientTeamsText);
        } else {
			maxOvers = Integer.parseInt(etMaxOvers.getText().toString());
			maxWickets = Integer.parseInt(etMaxWickets.getText().toString());
			updateNumPlayers();
        	updateMaxPerBowler();
		}
    }

    private void updateMaxPerBowler() {
		maxPerBowler = (maxOvers % 5 == 0) ? maxOvers / 5 : (maxOvers / 5 + 1);

		if(maxPerBowler > 0) {
			if (maxOvers / maxPerBowler > (numPlayers)) {
				int oversPerBowler = maxOvers / numPlayers;
				maxPerBowler = (maxOvers % numPlayers == 0) ? oversPerBowler : (oversPerBowler + 1);
			}
		}

		etMaxPerBowler.setText(String.valueOf(maxPerBowler));
	}

    private void updateNumPlayers() {
		numPlayers = maxWickets + 1;
		etNumPlayers.setText(String.valueOf(numPlayers));
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
				break;

			case R.id.rbTossTeam2:
				tossWonBy = team2;
				rgTossChoose.setVisibility(View.VISIBLE);
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
				break;

        }
    }

	private void startNewMatch() {
		if(getActivity() != null) {
			FragmentManager fragMgr = getActivity().getSupportFragmentManager();
			String fragmentTag = LimitedOversFragment.class.getSimpleName();
			LimitedOversFragment fragment =
					LimitedOversFragment.newInstance(etMatchName.getText().toString(), battingTeam, bowlingTeam, tossWonBy,
							maxOvers, maxWickets, maxPerBowler);

			fragMgr.beginTransaction()
					.replace(R.id.frame_container, fragment, fragmentTag)
					.addToBackStack(fragmentTag)
					.commit();
		}
	}

	private void validateInput() {
    	maxOvers = Integer.parseInt(etMaxOvers.getText().toString());
    	maxWickets = Integer.parseInt(etMaxWickets.getText().toString());
    	maxPerBowler = Integer.parseInt(etMaxPerBowler.getText().toString());
    	numPlayers = Integer.parseInt(etNumPlayers.getText().toString());
		String deficientTeam = getDeficientTeam(numPlayers);

		if(team1 == null || team2 == null) {
			Toast.makeText(getContext(), "Both teams need to be selected to continue", Toast.LENGTH_LONG).show();
		} else if(etMatchName.getText().toString().length() < 5) {
			Toast.makeText(getContext(), "Provide a match name more than 5 characters", Toast.LENGTH_SHORT).show();
		} else if(team1.getId() == team2.getId()) {
			Toast.makeText(getContext(), "Both teams are the same. Select different teams", Toast.LENGTH_LONG).show();
		} else if(maxOvers == 0 || maxWickets == 0 || maxPerBowler == 0 || numPlayers == 0) {
			Toast.makeText(getContext(), "Players, Overs, Wickets cannot be zero", Toast.LENGTH_LONG).show();
		} else if(maxWickets >= numPlayers) {
			Toast.makeText(getContext(), "Number of Players has to be greater than Maximum Wickets", Toast.LENGTH_LONG).show();
		} else if(maxPerBowler > maxOvers) {
			Toast.makeText(getContext(), "Max overs per bowler cannot be more than max overs", Toast.LENGTH_LONG).show();
		} else if(maxPerBowler < ((maxOvers % numPlayers == 0) ? maxOvers/numPlayers : (maxOvers/numPlayers + 1))) {
			Toast.makeText(getContext(), "Not enough Players/Max Overs per Bowler to complete full quota of Overs", Toast.LENGTH_LONG).show();
		} else if(deficientTeam != null) {
			Toast.makeText(getContext(), String.format("Not enough players in %s", (deficientTeam.equals("TEAM1") ? team1.getShortName() : team2.getShortName())), Toast.LENGTH_LONG).show();
		} else {
			/*Toast.makeText(getContext(), "Update the playing team by clicking the buttons next to team name.\n" +
					"The playing team cannot be modified later on", Toast.LENGTH_LONG).show();*/
			Toast.makeText(getContext(), String.format("Select Players from %s", team1.getName()) , Toast.LENGTH_SHORT).show();
			displayPlayerSelect(team1, REQ_CODE_PLAYER_SELECT_TEAM1);
		}
	}

	private void setLayoutForMatchStart() {
        etMatchName.setEnabled(false);
        tvTeam1.setEnabled(false);
        tvTeam2.setEnabled(false);

        rgToss.setVisibility(View.VISIBLE);
    }

    public void getTeams() {
        DatabaseHandler dbHandler = new DatabaseHandler(getContext());
        teams = dbHandler.getTeams(null, -1);
    }

    private void displayTeamSelect(String teamSelect) {
        if(getFragmentManager() != null) {
            String[] teamDetails = new String[teams.size()];

            int i=0;
            for(Team team : teams)
                teamDetails[i++] = team.getName();

            StringDialog dialog = StringDialog.newInstance("Select Team", teamDetails, teamSelect);
            dialog.setDialogItemClickListener(this);
            dialog.show(getFragmentManager(), teamSelect + "Dialog");
        }
    }

    @Override
    public void onItemSelect(String enumType, String value, int position) {
        switch (enumType) {
            case ENUM_TYPE_TEAM1:
                team1 = teams.get(position);
                tvTeam1.setText(value);
                setTeamName();
                break;

            case ENUM_TYPE_TEAM2:
                team2 = teams.get(position);
                tvTeam2.setText(value);
				setTeamName();
                break;
        }
    }

	private void setTeamName() {
    	if(team1 != null && team2 != null) {
			String teamName = etMatchName.getText().toString();
			if(teamName.length() == 0) {
				teamName = team1.getShortName() + " vs " + team2.getShortName() + " " + CommonUtils.currTimestamp("ddMMMyyyy");
				etMatchName.setText(teamName);
				etMatchName.requestFocus();
			}
		}
	}

	private String getDeficientTeam(int numPlayers) {
    	DatabaseHandler dbh = new DatabaseHandler(getContext());

    	if(dbh.getAssociatedPlayers(team1.getId()).size() < numPlayers)
    		return "TEAM1";
    	else if(dbh.getAssociatedPlayers(team2.getId()).size() < numPlayers)
    		return "TEAM2";
    	else
    		return null;
	}

	private void displayPlayerSelect(Team team, int reqCode) {
    	List<Player> dispPlayerList = new DatabaseHandler(getContext()).getTeamPlayers(team.getId());
    	List<Integer> associatedPlayers = new ArrayList<>();

    	for (int i=0; i<numPlayers; i++) {
			associatedPlayers.add(dispPlayerList.get(i).getID());

			if(reqCode == REQ_CODE_PLAYER_SELECT_TEAM1) {
				team1Players.add(dispPlayerList.get(i));
			}else if(reqCode == REQ_CODE_PLAYER_SELECT_TEAM2) {
				team2Players.add(dispPlayerList.get(i));
			}
		}

		Intent playerDisplayIntent = new Intent(getContext(), PlayerSelectActivity.class);
		playerDisplayIntent.putExtra(PlayerSelectActivity.ARG_PLAYER_LIST, dispPlayerList.toArray());
		playerDisplayIntent.putExtra(PlayerSelectActivity.ARG_IS_MULTI_SELECT, true);
		playerDisplayIntent.putIntegerArrayListExtra(PlayerSelectActivity.ARG_ASSOCIATED_PLAYERS, (ArrayList<Integer>) associatedPlayers);

		startActivityForResult(playerDisplayIntent, reqCode);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case REQ_CODE_PLAYER_SELECT_TEAM1:
				if(resultCode == PlayerSelectActivity.RESP_CODE_OK) {
					team1Players = Arrays.asList(CommonUtils.objectArrToPlayerArr((Object []) data.getSerializableExtra(PlayerSelectActivity.ARG_RESP_SEL_PLAYERS)));
					team1.setMatchPlayers(team1Players);
					Toast.makeText(getContext(), String.format("Select Players from %s", team2.getName()) , Toast.LENGTH_SHORT).show();
					displayPlayerSelect(team2, REQ_CODE_PLAYER_SELECT_TEAM2);
				}
				break;

			case REQ_CODE_PLAYER_SELECT_TEAM2:
				if(resultCode == PlayerSelectActivity.RESP_CODE_OK) {
					team2Players = Arrays.asList(CommonUtils.objectArrToPlayerArr((Object []) data.getSerializableExtra(PlayerSelectActivity.ARG_RESP_SEL_PLAYERS)));
					team2.setMatchPlayers(team2Players);
				}
				setLayoutForMatchStart();
				break;
		}
	}
}
