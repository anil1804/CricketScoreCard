package com.thenewcone.myscorecard.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.intf.DialogItemClickListener;
import com.thenewcone.myscorecard.player.Player;
import com.thenewcone.myscorecard.utils.database.DatabaseHandler;
import com.thenewcone.myscorecard.viewModel.PlayerViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment implements DialogItemClickListener, View.OnClickListener {
    TextView tvBatStyle, tvBowlStyle;
    EditText etName, etAge;
    CheckBox cbIsWK;
    Player selPlayer;

    Button btnDelete;

    public PlayerFragment() {
        setHasOptionsMenu(true);
        // Required empty public constructor
    }

    public static PlayerFragment newInstance() {
        return new PlayerFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if(getActivity() != null) {
            PlayerViewModel model = ViewModelProviders.of(getActivity()).get(PlayerViewModel.class);
            model.getSelectedPlayer().observe(this, new Observer<Player>() {
                @Override
                public void onChanged(@Nullable Player selPlayer) {
                    if (selPlayer != null) {
                        PlayerFragment.this.selPlayer = selPlayer;
                        populateData();
                    }
                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_player, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_getPlayerList:
                if(getActivity() != null) {
                    String fragmentTag = PlayerListFragment.class.getSimpleName();
                    PlayerListFragment fragment = PlayerListFragment.newInstance();

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_container, fragment, fragmentTag)
							.addToBackStack(fragmentTag)
                            .commit();
                }
                break;
        }

        return true;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View theView = inflater.inflate(R.layout.fragment_player, container, false);

        tvBatStyle = theView.findViewById(R.id.tvPlayerBatStyle);
        tvBowlStyle = theView.findViewById(R.id.tvPlayerBowlStyle);
        etName = theView.findViewById(R.id.etPlayerName);
        etAge = theView.findViewById(R.id.etPlayerAge);
        cbIsWK = theView.findViewById(R.id.cbIsPlayerWK);
        btnDelete = theView.findViewById(R.id.btnPlayerDelete);

        tvBatStyle.setOnClickListener(this);
        tvBowlStyle.setOnClickListener(this);

        theView.findViewById(R.id.btnPlayerSave).setOnClickListener(this);
        theView.findViewById(R.id.btnPlayerClear).setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        return theView;
    }

    private void showBattingStyleDialog() {
        if(getFragmentManager() != null) {
            Player.BattingType[] battingTypes = Player.BattingType.values();
            String[] battingStyles = new String[battingTypes.length];

            int i=0;
            for(Player.BattingType type : battingTypes)
                battingStyles[i++] = type.toString();

            StringDialog dialog = StringDialog.newInstance("Select Batting Style", battingStyles, StringDialog.ARG_ENUM_TYPE_BAT_STYLE);
            dialog.setDialogItemClickListener(this);
            dialog.show(getFragmentManager(), StringDialog.ARG_ENUM_TYPE_BAT_STYLE + "Dialog");
        }
    }

    private void showBowlingStyleDialog() {
        if(getFragmentManager() != null) {
            Player.BowlingType[] bowlingTypes = Player.BowlingType.values();
            String[] bowlingStyles = new String[bowlingTypes.length];

            int i=0;
            for(Player.BowlingType type : bowlingTypes)
                bowlingStyles[i++] = type.toString();

            StringDialog dialog = StringDialog.newInstance("Select Bowling Style", bowlingStyles, StringDialog.ARG_ENUM_TYPE_BOWL_STYLE);
            dialog.setDialogItemClickListener(this);
            dialog.show(getFragmentManager(), StringDialog.ARG_ENUM_TYPE_BOWL_STYLE + "Dialog");
        }
    }

    private void populateData() {
        etName.setText(selPlayer.getName());
        etAge.setText(String.valueOf(selPlayer.getAge()));
        tvBatStyle.setText(selPlayer.getBattingStyle().toString());
        tvBowlStyle.setText(selPlayer.getBowlingStyle().toString());
        cbIsWK.setChecked(selPlayer.isWicketKeeper());

        if(selPlayer.getID() >= 0) {
            btnDelete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemSelect(String enumType, String value, int position) {
        switch (enumType) {
            case StringDialog.ARG_ENUM_TYPE_BAT_STYLE:
                tvBatStyle.setText(value);
                break;

            case StringDialog.ARG_ENUM_TYPE_BOWL_STYLE:
                tvBowlStyle.setText(value);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPlayerBatStyle:
                showBattingStyleDialog();
                break;

            case R.id.tvPlayerBowlStyle:
                showBowlingStyleDialog();
                break;

            case R.id.btnPlayerSave:
                savePlayer();
                break;

            case R.id.btnPlayerClear:
                clearScreen();
                break;

            case R.id.btnPlayerDelete:
                deletePlayer();
                break;
        }
    }

    private void savePlayer() {
        DatabaseHandler dbHandler = new DatabaseHandler(getContext());

        StringBuilder errorSB = new StringBuilder();

        if(etName.getText().toString().trim().length() < 3)
            errorSB.append("Enter valid name (at-least 3 characters)\n");
        if(etAge.getText().toString().trim().length() < 1)
            errorSB.append("Enter valid age\n");
        if(tvBatStyle.getText().toString().equals(getString(R.string.selectBatStyle)))
            errorSB.append("Select Batting Style\n");
        if(tvBowlStyle.getText().toString().equals(getString(R.string.selectBowlStyle)))
            errorSB.append("Select Bowling Style\n");

        if(errorSB.toString().trim().length() == 0) {
            Player currPlayer =
                    new Player(-1,
                            etName.getText().toString(),
                            Integer.parseInt(etAge.getText().toString()),
                            Player.BattingType.valueOf(tvBatStyle.getText().toString()),
                            Player.BowlingType.valueOf(tvBowlStyle.getText().toString()),
                            cbIsWK.isChecked());

            if(selPlayer != null)
                currPlayer.setPlayerID(selPlayer.getID());

            int playerID = dbHandler.upsertPlayer(currPlayer);

            if (playerID == dbHandler.CODE_INS_PLAYER_DUP_RECORD) {
                Toast.makeText(getContext(), "Player with same name exists.\nChange name or update/delete existing team.", Toast.LENGTH_LONG).show();
            } else {
                currPlayer.setPlayerID(playerID);

                Toast.makeText(getContext(), "Player Saved Successfully", Toast.LENGTH_SHORT).show();
                selPlayer = currPlayer;
            }
        } else {
            Toast.makeText(getContext(), errorSB.toString().trim(), Toast.LENGTH_LONG).show();
        }
    }

    private void clearScreen() {
        etName.setText("");
        etAge.setText("");
        tvBatStyle.setText(R.string.selectBatStyle);
        tvBowlStyle.setText(R.string.selectBowlStyle);
        cbIsWK.setChecked(false);
        btnDelete.setVisibility(View.INVISIBLE);
        selPlayer = null;
    }

    private void deletePlayer() {
        DatabaseHandler dbHandler = new DatabaseHandler(getContext());
        boolean success = dbHandler.deletePlayer(selPlayer.getID());

        if(success) {
            Toast.makeText(getContext(), "Record Deleted Successfully", Toast.LENGTH_SHORT).show();
            clearScreen();
        } else {
            Toast.makeText(getContext(), "Problem deleting Data", Toast.LENGTH_SHORT).show();
        }
    }
}
