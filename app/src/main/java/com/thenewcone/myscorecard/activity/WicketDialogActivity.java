package com.thenewcone.myscorecard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.player.BatsmanStats;
import com.thenewcone.myscorecard.player.BowlerStats;
import com.thenewcone.myscorecard.player.Player;
import com.thenewcone.myscorecard.scorecard.Extra;
import com.thenewcone.myscorecard.scorecard.WicketData;
import com.thenewcone.myscorecard.utils.CommonUtils;

import java.util.Arrays;
import java.util.List;

public class WicketDialogActivity extends Activity
	implements View.OnClickListener{

	private static final int ACTIVITY_REQ_CODE_FIELDER_SELECT = 1;
    private static final int ACTIVITY_REQ_CODE_OUT_BATSMAN_SELECT = 2;

	public static final String ARG_FACING_BATSMAN = "FacingBatsman";
	public static final String ARG_OTHER_BATSMAN = "OtherBatsman";
	public static final String ARG_BOWLER = "Bowler";
	public static final String ARG_FIELDING_TEAM = "FieldingTeam";
	public static final String ARG_WICKET_DATA = "WicketData";

	BatsmanStats facingBatsman, otherBatsman, outBatsman;
	BowlerStats bowler;
	Player effectedBy;
	WicketData.DismissalType dismissalType;
	Extra extraData;
	Player[] fieldingTeam;

	GridLayout glWicket, glRORunsExtra;
	CheckBox cbIsExtra;
	int minExtraRuns = 0;
	SeekBar sbRORuns;
    TextView tvEffectedBy;

	public static final int RESP_CODE_OK = 1;
	public static final int RESP_CODE_CANCEL = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wicket_dialog);

		setView();

		Intent incomingIntent = getIntent();
		if(incomingIntent != null) {
			facingBatsman = (BatsmanStats) incomingIntent.getSerializableExtra(ARG_FACING_BATSMAN);
			otherBatsman = (BatsmanStats) incomingIntent.getSerializableExtra(ARG_OTHER_BATSMAN);
			bowler = (BowlerStats) incomingIntent.getSerializableExtra(ARG_BOWLER);
			fieldingTeam = CommonUtils.objectArrToPlayerArr((Object[]) incomingIntent.getSerializableExtra(ARG_FIELDING_TEAM));
		}
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

	private void setView() {
		glWicket = findViewById(R.id.glWicket);
		sbRORuns = findViewById(R.id.sbRORuns);
		sbRORuns.setProgress(0);
		final TextView tvRORunsScoredText = findViewById(R.id.tvRORunsScoredText);
		tvRORunsScoredText.setText(String.format(getString(R.string.runsScored), sbRORuns.getProgress()));

		sbRORuns.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
				if(sbRORuns.getProgress() < minExtraRuns) {
					Toast.makeText(getApplicationContext(), String.format("Minimum runs '%d' for the Extra Type Selected", minExtraRuns), Toast.LENGTH_SHORT).show();
					sbRORuns.setProgress(minExtraRuns);
				}

				tvRORunsScoredText.setText(String.format(getString(R.string.runsScored), sbRORuns.getProgress()));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});


		Button btnEffectedBy = findViewById(R.id.btnEffectedByText);
		Button btnBatsmanOut = findViewById(R.id.btnBatsmanOutText);
		Button btnOK = findViewById(R.id.btnWktOK);
		Button btnCancel = findViewById(R.id.btnWktCancel);

		btnEffectedBy.setOnClickListener(this);
		btnBatsmanOut.setOnClickListener(this);
		btnOK.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		RadioButton rbWktCaught = findViewById(R.id.rbWktCaught);
		RadioButton rbWktBowled = findViewById(R.id.rbWktBowled);
		RadioButton rbWktLBW = findViewById(R.id.rbWktLBW);
		RadioButton rbWktStump = findViewById(R.id.rbWktStump);
		RadioButton rbWktRunOut = findViewById(R.id.rbWktRunOut);
		RadioButton rbWktHitwicket = findViewById(R.id.rbWktHitwicket);
		RadioButton rbWktRetiredHurt = findViewById(R.id.rbWktRetiredHurt);
		RadioButton rbWktObstruct = findViewById(R.id.rbWktObstruct);
		RadioButton rbWktHitTwice = findViewById(R.id.rbWktHitTwice);
		RadioButton rbWktTimedOut = findViewById(R.id.rbWktTimedOut);

		rbWktCaught.setOnClickListener(this);
		rbWktBowled.setOnClickListener(this);
		rbWktLBW.setOnClickListener(this);
		rbWktStump.setOnClickListener(this);
		rbWktRunOut.setOnClickListener(this);
		rbWktHitwicket.setOnClickListener(this);
		rbWktRetiredHurt.setOnClickListener(this);
		rbWktObstruct.setOnClickListener(this);
		rbWktHitTwice.setOnClickListener(this);
		rbWktTimedOut.setOnClickListener(this);

		cbIsExtra = findViewById(R.id.cbIsExtra);
		RadioButton rbROWide = findViewById(R.id.rbROWide);
		RadioButton rbRONB = findViewById(R.id.rbRONB);
		RadioButton rbROBye = findViewById(R.id.rbROBye);
		RadioButton rbROLegBye = findViewById(R.id.rbROLegBye);

		cbIsExtra.setOnClickListener(this);
		rbROWide.setOnClickListener(this);
		rbRONB.setOnClickListener(this);
		rbROBye.setOnClickListener(this);
		rbROLegBye.setOnClickListener(this);

		RadioButton rbRONBNone = findViewById(R.id.rbRONBNone);
		RadioButton rbRONBBye = findViewById(R.id.rbRONBBye);
		RadioButton rbRONBLB = findViewById(R.id.rbRONBLB);

		rbRONBNone.setOnClickListener(this);
		rbRONBBye.setOnClickListener(this);
		rbRONBLB.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		glRORunsExtra = findViewById(R.id.glRORunsExtra);

		tvEffectedBy = findViewById(R.id.tvEffectedBy);

		RadioGroup rgRONB = findViewById(R.id.rgRONB);

		switch (view.getId()) {
			/*Capturing the details of the Batsman who it out*/
			case R.id.btnBatsmanOutText:
                displayBatsmen();
				break;

			/*Capturing the details of the Fielder who effected the dismissal*/
			case R.id.btnEffectedByText:
                displayFieldingTeam();
				break;

			/*Capturing all details of the Wicket*/
			case R.id.btnWktOK:
				sendResponse(RESP_CODE_OK);
				break;

			/*Cancel the Capture*/
			case R.id.btnWktCancel:
				sendResponse(RESP_CODE_CANCEL);
				break;

			/*Capturing the individual Dismissals from here*/
			case R.id.rbWktCaught:
				clearOtherCheckedRadioButtons(glWicket, view.getId());
				setViewVisibility(View.VISIBLE, View.GONE, View.GONE);
				tvEffectedBy.setText(R.string.caughtBy);

				dismissalType = WicketData.DismissalType.CAUGHT;
				outBatsman = facingBatsman;
				break;

			case R.id.rbWktRunOut:
				clearOtherCheckedRadioButtons(glWicket, view.getId());
				setViewVisibility(View.VISIBLE, View.VISIBLE, View.VISIBLE);
				tvEffectedBy.setText(R.string.runoutBy);

				dismissalType = WicketData.DismissalType.RUN_OUT;
				break;

			case R.id.rbWktObstruct:
				clearOtherCheckedRadioButtons(glWicket, view.getId());
				setViewVisibility(View.VISIBLE, View.VISIBLE, View.VISIBLE);

				dismissalType = WicketData.DismissalType.OBSTRUCTING_FIELD;
				break;

			case R.id.rbWktBowled:
				clearOtherCheckedRadioButtons(glWicket, view.getId());
				setViewVisibility(View.GONE, View.GONE, View.GONE);

				dismissalType = WicketData.DismissalType.BOWLED;
				outBatsman = facingBatsman;
				break;

			case R.id.rbWktHitTwice:
				clearOtherCheckedRadioButtons(glWicket, view.getId());
				setViewVisibility(View.GONE, View.GONE, View.GONE);

				dismissalType = WicketData.DismissalType.HIT_BALL_TWICE;
				outBatsman = facingBatsman;
				break;

			case R.id.rbWktHitwicket:
				clearOtherCheckedRadioButtons(glWicket, view.getId());
				setViewVisibility(View.GONE, View.GONE, View.GONE);

				dismissalType = WicketData.DismissalType.HIT_WICKET;
				outBatsman = facingBatsman;
				break;

			case R.id.rbWktLBW:
				clearOtherCheckedRadioButtons(glWicket, view.getId());
				setViewVisibility(View.GONE, View.GONE, View.GONE);

				dismissalType = WicketData.DismissalType.LBW;
				outBatsman = facingBatsman;
				break;

			case R.id.rbWktRetiredHurt:
				clearOtherCheckedRadioButtons(glWicket, view.getId());
				setViewVisibility(View.GONE, View.VISIBLE, View.GONE);

				dismissalType = WicketData.DismissalType.RETIRED;
				break;

			case R.id.rbWktStump:
				clearOtherCheckedRadioButtons(glWicket, view.getId());
				setViewVisibility(View.GONE, View.GONE, View.GONE);

				dismissalType = WicketData.DismissalType.STUMPED;
				outBatsman = facingBatsman;
				break;

			case R.id.rbWktTimedOut:
				clearOtherCheckedRadioButtons(glWicket, view.getId());
				setViewVisibility(View.GONE, View.VISIBLE, View.GONE);

				dismissalType = WicketData.DismissalType.TIMED_OUT;
				break;

			/*Capturing details if the runs scored as extras, during a Run-out/Field Obstruction*/
			case R.id.cbIsExtra:
				if(cbIsExtra.isChecked())
					glRORunsExtra.setVisibility(View.VISIBLE);
				else
					glRORunsExtra.setVisibility(View.GONE);
				break;

			case R.id.rbROWide:
			case R.id.rbROBye:
			case R.id.rbROLegBye:
				adjustROExtraRuns(view);
				rgRONB.setVisibility(View.GONE);
				break;

			case R.id.rbRONB:
				adjustROExtraRuns(view);
				rgRONB.setVisibility(View.VISIBLE);
				break;

			case R.id.rbRONBNone:
			case R.id.rbRONBBye:
			case R.id.rbRONBLB:
				adjustROExtraRuns(view);
				break;
		}
	}

	private void adjustROExtraRuns(View view) {
		switch (view.getId()) {
			case R.id.rbROBye:
			case R.id.rbROLegBye:
				clearOtherCheckedRadioButtons(glRORunsExtra, view.getId());
				minExtraRuns = 1;
				break;

			case R.id.rbROWide:
			case R.id.rbRONB:
				clearOtherCheckedRadioButtons(glRORunsExtra, view.getId());
				minExtraRuns = 0;
				break;

			case R.id.rbRONBNone:
				minExtraRuns = 1;
				break;

			case R.id.rbRONBBye:
			case R.id.rbRONBLB:
				minExtraRuns = 1;
				break;
		}

		if(sbRORuns.getProgress() < minExtraRuns)
			sbRORuns.setProgress(minExtraRuns);
	}

	private void clearOtherCheckedRadioButtons(ViewGroup viewGroup, int selectedViewID) {
		for(int i=0; i<viewGroup.getChildCount(); i++) {
			RadioButton radioButton = findViewById(viewGroup.getChildAt(i).getId());
			if(radioButton.getId() != selectedViewID) {
				radioButton.setChecked(false);
			}
		}
	}

	private void setViewVisibility(int effectedByVisibility, int outBatsmanVisibility, int runoutRunsVisibility) {
		LinearLayout llWicketDetails = findViewById(R.id.llWicketDetails);
		LinearLayout llEffectedBy = findViewById(R.id.llEffectedBy);
		LinearLayout llOutBatsman = findViewById(R.id.llOutBatsman);
		LinearLayout llRORuns = findViewById(R.id.llRORuns);

		if(effectedByVisibility == View.VISIBLE || outBatsmanVisibility == View.VISIBLE)
			llWicketDetails.setVisibility(View.VISIBLE);
		else if (effectedByVisibility == View.INVISIBLE || outBatsmanVisibility == View.INVISIBLE)
			llWicketDetails.setVisibility(View.INVISIBLE);

		llEffectedBy.setVisibility(effectedByVisibility);
		llOutBatsman.setVisibility(outBatsmanVisibility);
		llRORuns.setVisibility(runoutRunsVisibility);
	}

	private void displayFieldingTeam() {
		Intent playerIntent = new Intent(this, PlayerSelectActivity.class);
		playerIntent.putExtra(PlayerSelectActivity.ARG_PLAYER_LIST, fieldingTeam);
		startActivityForResult(playerIntent, ACTIVITY_REQ_CODE_FIELDER_SELECT);
	}

	private void displayBatsmen() {
	    Intent batsmanIntent = new Intent(this, BatsmanSelectActivity.class);
	    BatsmanStats[] batsmen = {facingBatsman, otherBatsman};
	    batsmanIntent.putExtra(BatsmanSelectActivity.ARG_BATSMAN_LIST, batsmen);
	    startActivityForResult(batsmanIntent, ACTIVITY_REQ_CODE_OUT_BATSMAN_SELECT);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case ACTIVITY_REQ_CODE_FIELDER_SELECT:
				if(resultCode == RESP_CODE_OK) {
					effectedBy = (Player) data.getSerializableExtra(PlayerSelectActivity.ARG_EFFECTED_BY);
					tvEffectedBy.setText(effectedBy.getName());
				}
				break;

            case ACTIVITY_REQ_CODE_OUT_BATSMAN_SELECT:
                if(resultCode == RESP_CODE_OK) {
                    outBatsman = (BatsmanStats) data.getSerializableExtra(BatsmanSelectActivity.ARG_SEL_BATSMAN);
                    TextView tvOutBatsman = findViewById(R.id.tvBatsmanOut);
                    tvOutBatsman.setText(outBatsman.getBatsmanName());
                }
                break;
		}
	}

    private void sendResponse(int responseCode) {
        setResult(responseCode);

        WicketData wicketData;
        wicketData = new WicketData(outBatsman, dismissalType, effectedBy, bowler);

        Intent respIntent = new Intent();
        respIntent.putExtra(ARG_WICKET_DATA, wicketData);
        setResult(responseCode, respIntent);

        finish();
    }
}
