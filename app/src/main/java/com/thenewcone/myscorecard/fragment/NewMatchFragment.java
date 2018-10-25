package com.thenewcone.myscorecard.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.intf.DialogItemClickListener;
import com.thenewcone.myscorecard.match.Team;
import com.thenewcone.myscorecard.utils.CommonUtils;
import com.thenewcone.myscorecard.utils.database.DatabaseHandler;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewMatchFragment extends Fragment
    implements View.OnClickListener, DialogItemClickListener {

    EditText etMatchName, etMaxOvers, etMaxWickes, etMaxPerBowler, etNumPlayers;
    TextView tvTeam1, tvTeam2;
    Button btnCancel, btnValidate, btnStartMatch, btnManageTeam;
    List<Team> teams;
    Team team1, team2;

    private final int REQ_CODE_TEAM1_SELECT = 1;
    private final int REQ_CODE_TEAM2_SELECT = 2;
    private final String ENUM_TYPE_TEAM1 = "Team1Select";
    private final String ENUM_TYPE_TEAM2 = "Team2Select";

	int maxPerBowler = 0, maxOvers, maxWickets, numPlayers = 0;

    public NewMatchFragment() {
        // Required empty public constructor
    }

    public static NewMatchFragment newInstance() {
        return new NewMatchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View theView = inflater.inflate(R.layout.fragment_new_match, container, false);

        etMatchName = theView.findViewById(R.id.etMatchName);
        etMaxOvers = theView.findViewById(R.id.etMaxOvers);
        etMaxWickes = theView.findViewById(R.id.etMaxWickets);
        etMaxPerBowler = theView.findViewById(R.id.etMaxPerBowler);
		etNumPlayers = theView.findViewById(R.id.etNumPlayers);

		etMaxWickes.addTextChangedListener(new TextWatcher() {
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

        tvTeam1.setOnClickListener(this);
        tvTeam2.setOnClickListener(this);

        btnCancel.setOnClickListener(this);
        btnValidate.setOnClickListener(this);
        btnStartMatch.setOnClickListener(this);
        btnManageTeam.setOnClickListener(this);

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
			maxWickets = Integer.parseInt(etMaxWickes.getText().toString());
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
                displayTeamSelect(REQ_CODE_TEAM1_SELECT, ENUM_TYPE_TEAM1);
                break;

            case R.id.tvTeam2:
                displayTeamSelect(REQ_CODE_TEAM2_SELECT, ENUM_TYPE_TEAM2);
                break;

            case R.id.btnValidate:
				validateInput();
				break;

			case R.id.btnStartMatch:
                Toast.makeText(getContext(), "Show Toss and Player Select Screen", Toast.LENGTH_SHORT).show();
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
        }
    }

	private void validateInput() {
    	maxOvers = Integer.parseInt(etMaxOvers.getText().toString());
    	maxWickets = Integer.parseInt(etMaxWickes.getText().toString());
    	maxPerBowler = Integer.parseInt(etMaxPerBowler.getText().toString());
    	numPlayers = Integer.parseInt(etNumPlayers.getText().toString());

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
		} else if(maxPerBowler < ((maxOvers % numPlayers == 0) ? maxOvers/numPlayers : (maxOvers/numPlayers + 1))) {
			Toast.makeText(getContext(), "Not enough Players/Max Overs per Bowler to complete full quota of Overs", Toast.LENGTH_LONG).show();
		} else {
			setLayoutForMatchStart();
		}
	}

	private void setLayoutForMatchStart() {
        etMatchName.setEnabled(false);
        tvTeam1.setEnabled(false);
        tvTeam2.setEnabled(false);

        btnStartMatch.setVisibility(View.VISIBLE);
    }

    public void getTeams() {
        DatabaseHandler dbHandler = new DatabaseHandler(getContext());
        teams = dbHandler.getTeams(null, -1);
    }

    private void displayTeamSelect(int reqCode, String teamSelect) {
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
}
