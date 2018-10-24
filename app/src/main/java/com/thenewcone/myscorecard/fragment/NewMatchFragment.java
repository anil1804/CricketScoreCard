package com.thenewcone.myscorecard.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.intf.DialogItemClickListener;
import com.thenewcone.myscorecard.intf.ItemClickListener;
import com.thenewcone.myscorecard.match.Team;
import com.thenewcone.myscorecard.utils.database.DatabaseHandler;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewMatchFragment extends Fragment
    implements View.OnClickListener, DialogItemClickListener {
    EditText etMatchName;
    TextView tvTeam1, tvTeam2;
    Button btnCancel, btnSave, btnStartMatch;
    List<Team> teams;
    Team team1, team2;

    private final int REQ_CODE_TEAM1_SELECT = 1;
    private final int REQ_CODE_TEAM2_SELECT = 2;
    private final String ENUM_TYPE_TEAM1 = "Team1Select";
    private final String ENUM_TYPE_TEAM2 = "Team2Select";

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
        tvTeam1 = theView.findViewById(R.id.tvTeam1);
        tvTeam2 = theView.findViewById(R.id.tvTeam2);
        btnCancel = theView.findViewById(R.id.btnMatchCancel);
        btnSave = theView.findViewById(R.id.btnMatchSave);
        btnStartMatch = theView.findViewById(R.id.btnStartMatch);

        tvTeam1.setOnClickListener(this);
        tvTeam2.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnStartMatch.setOnClickListener(this);

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
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvTeam1:
                displayBatsmanSelect(REQ_CODE_TEAM1_SELECT, ENUM_TYPE_TEAM1);
                break;

            case R.id.tvTeam2:
                displayBatsmanSelect(REQ_CODE_TEAM2_SELECT, ENUM_TYPE_TEAM2);
                break;

            case R.id.btnNewMatch:
                if(team1.getId() == team2.getId()) {
                    Toast.makeText(getContext(), "Both teams are the. Select different teams", Toast.LENGTH_LONG).show();
                } else {
                    setLayoutForMatchStart();
                }

            case R.id.btnStartMatch:
                Toast.makeText(getContext(), "Show Toss and Player Select Screen", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnCancel:
                if(getActivity() != null)
                    getActivity().onBackPressed();

                break;
        }
    }

    private void setLayoutForMatchStart() {
        etMatchName.setEnabled(false);
        tvTeam1.setEnabled(false);
        tvTeam2.setEnabled(false);

        btnSave.setVisibility(View.GONE);
        btnStartMatch.setVisibility(View.VISIBLE);
    }

    public void getTeams() {
        DatabaseHandler dbHandler = new DatabaseHandler(getContext());
        teams = dbHandler.getTeams(null, -1);
    }

    private void displayBatsmanSelect(int reqCode, String teamSelect) {
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
                break;

            case ENUM_TYPE_TEAM2:
                team2 = teams.get(position);
                tvTeam2.setText(value);
                break;
        }
    }
}
