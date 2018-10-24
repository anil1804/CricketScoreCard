package com.thenewcone.myscorecard.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.activity.InputActivity;
import com.thenewcone.myscorecard.intf.DialogItemClickListener;
import com.thenewcone.myscorecard.match.Team;
import com.thenewcone.myscorecard.utils.database.DatabaseHandler;

import java.util.List;

public class ManageTeamFragment extends Fragment
    implements View.OnClickListener, DialogItemClickListener {

    public final int REQ_CODE_ADD_TEAM = 1;

    Button btnAddTeam, btnDeleteTeams, btnUpdateTeamPlayers;
    TextView tvTeamName;

    String[] teams;
    List<Team> teamList;
    Team team;

    public ManageTeamFragment() {
        // Required empty public constructor
    }

    public static ManageTeamFragment newInstance() {
        return new ManageTeamFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View theView = inflater.inflate(R.layout.fragment_manage_team, container, false);

        tvTeamName = theView.findViewById(R.id.tvTeamName);
        btnAddTeam = theView.findViewById(R.id.btnAddTeam);
        btnDeleteTeams = theView.findViewById(R.id.btnDeleteTeams);
        btnUpdateTeamPlayers = theView.findViewById(R.id.btnUpdateTeamPlayers);

        tvTeamName.setOnClickListener(this);
        btnAddTeam.setOnClickListener(this);
        btnUpdateTeamPlayers.setOnClickListener(this);
        btnUpdateTeamPlayers.setOnClickListener(this);

        getTeams();

        return theView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddTeam:
                Intent intent = new Intent(getContext(), InputActivity.class);
                startActivityForResult(intent, REQ_CODE_ADD_TEAM);
                break;

            case R.id.btnDeleteTeams:
                break;

            case R.id.btnUpdateTeamPlayers:
                break;

            case R.id.tvTeamName:
                StringDialog dialog = StringDialog.newInstance("Select Team", teams, "TeamSelect");
                dialog.setDialogItemClickListener(this);
                dialog.show(getFragmentManager(), "TeamSelectDialog");
                break;
        }
    }

    public void getTeams() {
        DatabaseHandler dbHandler = new DatabaseHandler(getContext());
        teamList = dbHandler.getTeams(null, -1);

        teams = new String[teamList.size()];
        int i=0;
        for(Team team : teamList)
            teams[i] = team.getName();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_ADD_TEAM:
                if(resultCode == InputActivity.RESP_CODE_OK) {
                    String teamName = data.getStringExtra(InputActivity.ARG_INPUT_TEXT);
                    DatabaseHandler dbh = new DatabaseHandler(getContext());
                    int rowID = dbh.addNewTeam(teamName);

                    if(rowID == dbh.CODE_NEW_TEAM_DUP_RECORD) {
                        Toast.makeText(getContext(), "Team with same name already exists. Choose a different name.", Toast.LENGTH_SHORT).show();
                        getTeams();
                    } else {
                        Toast.makeText(getContext(), "Team created successfully.", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    @Override
    public void onItemSelect(String enumType, String value, int position) {
        team = teamList.get(position);
    }
}
