package com.thenewcone.myscorecard.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
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
    TextView tvBatStyle, tvBowlStyle, tvPlayerID;
    EditText etName, etAge;
    CheckBox cbIsWK;
    Player player;

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
                    if (selPlayer != null)
                        player = selPlayer;
                    populateData();
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
        tvPlayerID = theView.findViewById(R.id.tvPlayerId);
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

            EnumDialog dialog = EnumDialog.newInstance("Select Batting Style", battingStyles, EnumDialog.ARG_ENUM_TYPE_BAT_STYLE);
            dialog.setDialogItemClickListener(this);
            dialog.show(getFragmentManager(), EnumDialog.ARG_ENUM_TYPE_BAT_STYLE + "Dialog");
        }
    }

    private void showBowlingStyleDialog() {
        if(getFragmentManager() != null) {
            Player.BowlingType[] bowlingTypes = Player.BowlingType.values();
            String[] bowlingStyles = new String[bowlingTypes.length];

            int i=0;
            for(Player.BowlingType type : bowlingTypes)
                bowlingStyles[i++] = type.toString();

            EnumDialog dialog = EnumDialog.newInstance("Select Bowling Style", bowlingStyles, EnumDialog.ARG_ENUM_TYPE_BOWL_STYLE);
            dialog.setDialogItemClickListener(this);
            dialog.show(getFragmentManager(), EnumDialog.ARG_ENUM_TYPE_BOWL_STYLE + "Dialog");
        }
    }

    private void populateData() {
        etName.setText(player.getName());
        etAge.setText(String.valueOf(player.getAge()));
        tvBatStyle.setText(player.getBattingStyle().toString());
        tvBowlStyle.setText(player.getBowlingStyle().toString());
        cbIsWK.setChecked(player.isWicketKeeper());

        if(player.getID() >= 0) {
            tvPlayerID.setText(String.valueOf(player.getID()));
            btnDelete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemSelect(String enumType, String value) {
        switch (enumType) {
            case EnumDialog.ARG_ENUM_TYPE_BAT_STYLE:
                tvBatStyle.setText(value);
                break;

            case EnumDialog.ARG_ENUM_TYPE_BOWL_STYLE:
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

            if(player != null)
                currPlayer.setPlayerID(player.getID());

            int playerID = dbHandler.upsertPlayer(currPlayer);

            if (playerID == dbHandler.CODE_INS_PLAYER_DUP_RECORD) {
                Toast.makeText(getContext(), "Player with same name exists.\nChange name or update/delete existing player.", Toast.LENGTH_LONG).show();
            } else {
                tvPlayerID.setText(String.valueOf(playerID));
                currPlayer.setPlayerID(playerID);

                Toast.makeText(getContext(), "Player Saved Successfully", Toast.LENGTH_SHORT).show();
                player = currPlayer;
            }
        } else {
            Toast.makeText(getContext(), errorSB.toString().trim(), Toast.LENGTH_LONG).show();
        }
    }

    private void clearScreen() {
        tvPlayerID.setText("");
        etName.setText("");
        etAge.setText("");
        tvBatStyle.setText(R.string.selectBatStyle);
        tvBowlStyle.setText(R.string.selectBowlStyle);
        cbIsWK.setChecked(false);
        btnDelete.setVisibility(View.INVISIBLE);
        player = null;
    }

    private void deletePlayer() {
        DatabaseHandler dbHandler = new DatabaseHandler(getContext());
        boolean success = dbHandler.deletePlayer(Integer.parseInt(tvPlayerID.getText().toString()));

        if(success) {
            Toast.makeText(getContext(), "Record Deleted Successfully", Toast.LENGTH_SHORT).show();
            clearScreen();
        } else {
            Toast.makeText(getContext(), "Problem deleting Data", Toast.LENGTH_SHORT).show();
        }
    }
}
