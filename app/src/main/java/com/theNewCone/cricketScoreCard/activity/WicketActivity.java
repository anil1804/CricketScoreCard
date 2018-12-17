package com.theNewCone.cricketScoreCard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.enumeration.DismissalType;
import com.theNewCone.cricketScoreCard.enumeration.ExtraType;
import com.theNewCone.cricketScoreCard.fragment.StringDialog;
import com.theNewCone.cricketScoreCard.intf.DialogItemClickListener;
import com.theNewCone.cricketScoreCard.player.BatsmanStats;
import com.theNewCone.cricketScoreCard.player.BowlerStats;
import com.theNewCone.cricketScoreCard.player.Player;
import com.theNewCone.cricketScoreCard.scorecard.Extra;
import com.theNewCone.cricketScoreCard.scorecard.WicketData;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;


public class WicketActivity extends FragmentActivity
	implements View.OnClickListener, DialogItemClickListener {

	private static final int ACTIVITY_REQ_CODE_FIELDER_SELECT = 1;
    private static final int ACTIVITY_REQ_CODE_OUT_BATSMAN_SELECT = 2;

	public static final String ARG_FACING_BATSMAN = "FacingBatsman";
	public static final String ARG_OTHER_BATSMAN = "OtherBatsman";
	public static final String ARG_BOWLER = "Bowler";
	public static final String ARG_FIELDING_TEAM = "FieldingTeam";
	public static final String ARG_NEW_BATSMAN_ARRIVED = "NewBatsmanArrived";
	public static final String ARG_WICKET_DATA = "WicketData";
    public static final String ARG_EXTRA_DATA = "ExtraData";
    public static final String ARG_BATSMAN_RUNS = "BatsmanRuns";

	BatsmanStats facingBatsman, otherBatsman, outBatsman;
	BowlerStats bowler;
	Player effectedBy;
	DismissalType dismissalType;
	Extra extraData;
	Player[] fieldingTeam;
	boolean newBatsmanArrived;

	GridLayout glWicket, glRORunsExtra;
	CheckBox cbIsExtra;
	int minExtraRuns = 0;
	SeekBar sbRORuns;
    TextView tvEffectedBy;
    int batsmanRuns = 0;

	public static final int RESP_CODE_OK = 1;
	public static final int RESP_CODE_CANCEL = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wicket);

		Intent incomingIntent = getIntent();
		if(incomingIntent != null) {
			facingBatsman = (BatsmanStats) incomingIntent.getSerializableExtra(ARG_FACING_BATSMAN);
			otherBatsman = (BatsmanStats) incomingIntent.getSerializableExtra(ARG_OTHER_BATSMAN);
			bowler = (BowlerStats) incomingIntent.getSerializableExtra(ARG_BOWLER);
			fieldingTeam = CommonUtils.objectArrToPlayerArr((Object[]) incomingIntent.getSerializableExtra(ARG_FIELDING_TEAM));
			newBatsmanArrived = incomingIntent.getBooleanExtra(ARG_NEW_BATSMAN_ARRIVED, false);
		}

		setView();
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
					sbRORuns.setProgress(minExtraRuns);
				}

				tvRORunsScoredText.setText(String.format(getString(R.string.runsScored), sbRORuns.getProgress()));

				if(extraData != null) {
				    extraData.setRuns(sbRORuns.getProgress());
                }
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
		RadioButton rbWktHitwicket = findViewById(R.id.rbWktHitWicket);
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
		RadioButton rbRONB = findViewById(R.id.rbRONoBall);
		RadioButton rbROBye = findViewById(R.id.rbROBye);
		RadioButton rbROLegBye = findViewById(R.id.rbROLegBye);

		cbIsExtra.setOnClickListener(this);
		rbROWide.setOnClickListener(this);
		rbRONB.setOnClickListener(this);
		rbROBye.setOnClickListener(this);
		rbROLegBye.setOnClickListener(this);

		RadioButton rbRONBNone = findViewById(R.id.rbRONoBallNone);
		RadioButton rbRONBBye = findViewById(R.id.rbRONoBallBye);
		RadioButton rbRONBLB = findViewById(R.id.rbRONoBallLegBye);

		rbRONBNone.setOnClickListener(this);
		rbRONBBye.setOnClickListener(this);
		rbRONBLB.setOnClickListener(this);

		if(newBatsmanArrived) {
			rbWktTimedOut.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View view) {
		glRORunsExtra = findViewById(R.id.glRORunsExtra);

		tvEffectedBy = findViewById(R.id.tvEffectedBy);

		RadioGroup rgRONB = findViewById(R.id.rgRONoBall);

		switch (view.getId()) {
			/*Capturing the details of the Batsman who it out*/
			case R.id.btnBatsmanOutText:
                displayBatsmen();
				break;

			/*Capturing the details of the Fielder who effected the dismissal*/
			case R.id.btnEffectedByText:
                //displayFieldingTeam();
				showFielderDialog();
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
				setViewVisibility(View.VISIBLE, View.GONE, View.GONE, View.GONE);
				tvEffectedBy.setText(R.string.caughtBy);
				dismissalType = DismissalType.CAUGHT;
				effectedBy = null;
				outBatsman = facingBatsman;
				break;

			case R.id.rbWktRunOut:
				clearOtherCheckedRadioButtons(glWicket, view.getId());
				setViewVisibility(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE);
				outBatsman = null;
				effectedBy = null;
				tvEffectedBy.setText(R.string.runOutBy);
				dismissalType = DismissalType.RUN_OUT;
				break;

			case R.id.rbWktObstruct:
				clearOtherCheckedRadioButtons(glWicket, view.getId());
				setViewVisibility(View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE);
				outBatsman = null;
				effectedBy = null;
				dismissalType = DismissalType.OBSTRUCTING_FIELD;
				break;

			case R.id.rbWktBowled:
				clearOtherCheckedRadioButtons(glWicket, view.getId());
				setViewVisibility(View.GONE, View.GONE, View.GONE, View.GONE);
				effectedBy = null;
				outBatsman = facingBatsman;
				dismissalType = DismissalType.BOWLED;
				break;

			case R.id.rbWktHitTwice:
				clearOtherCheckedRadioButtons(glWicket, view.getId());
				setViewVisibility(View.GONE, View.GONE, View.GONE, View.GONE);
				effectedBy = null;
				outBatsman = facingBatsman;
				dismissalType = DismissalType.HIT_BALL_TWICE;
				break;

			case R.id.rbWktHitWicket:
				clearOtherCheckedRadioButtons(glWicket, view.getId());
				setViewVisibility(View.GONE, View.GONE, View.GONE, View.GONE);
				effectedBy = null;
				outBatsman = facingBatsman;
				dismissalType = DismissalType.HIT_WICKET;
				break;

			case R.id.rbWktLBW:
				clearOtherCheckedRadioButtons(glWicket, view.getId());
				setViewVisibility(View.GONE, View.GONE, View.GONE, View.GONE);
				effectedBy = null;
				outBatsman = facingBatsman;
				dismissalType = DismissalType.LBW;
				break;

			case R.id.rbWktRetiredHurt:
				clearOtherCheckedRadioButtons(glWicket, view.getId());
				setViewVisibility(View.GONE, View.VISIBLE, View.GONE, View.GONE);
				outBatsman = null;
				effectedBy = null;
				dismissalType = DismissalType.RETIRED;
				break;

			case R.id.rbWktStump:
				clearOtherCheckedRadioButtons(glWicket, view.getId());
				setViewVisibility(View.GONE, View.GONE, View.GONE, View.VISIBLE);
				effectedBy = null;
				outBatsman = facingBatsman;
				dismissalType = DismissalType.STUMPED;
				break;

			case R.id.rbWktTimedOut:
				clearOtherCheckedRadioButtons(glWicket, view.getId());
				setViewVisibility(View.GONE, View.VISIBLE, View.GONE, View.GONE);
				outBatsman = null;
				effectedBy = null;
				dismissalType = DismissalType.TIMED_OUT;
				break;

			/*Capturing details if the runs scored as extras, during a Run-out/Field Obstruction*/
			case R.id.cbIsExtra:
				if(cbIsExtra.isChecked()) {
                    glRORunsExtra.setVisibility(View.VISIBLE);
                    findViewById(R.id.rbROWide).setSelected(true);
					extraData = new Extra(ExtraType.WIDE, sbRORuns.getProgress());

					if (dismissalType == DismissalType.STUMPED) {
                    	findViewById(R.id.rbRONoBall).setEnabled(false);
						findViewById(R.id.rbROBye).setEnabled(false);
						findViewById(R.id.rbROLegBye).setEnabled(false);
					} else {
						findViewById(R.id.rbRONoBall).setEnabled(true);
						findViewById(R.id.rbROBye).setEnabled(true);
						findViewById(R.id.rbROLegBye).setEnabled(true);
					}
                }
				else {
                    glRORunsExtra.setVisibility(View.GONE);
                    extraData = null;
                }
				break;

			case R.id.rbROWide:
				extraData = new Extra(ExtraType.WIDE, sbRORuns.getProgress());
                adjustROExtraRuns(view);
                rgRONB.setVisibility(View.GONE);
			    break;

			case R.id.rbROBye:
				extraData = new Extra(ExtraType.BYE, sbRORuns.getProgress());
                adjustROExtraRuns(view);
                rgRONB.setVisibility(View.GONE);
                break;

            case R.id.rbROLegBye:
				extraData = new Extra(ExtraType.LEG_BYE, sbRORuns.getProgress());
				adjustROExtraRuns(view);
				rgRONB.setVisibility(View.GONE);
				break;

			case R.id.rbRONoBall:
				extraData = new Extra(ExtraType.NO_BALL, sbRORuns.getProgress(), ExtraType.NONE);
				adjustROExtraRuns(view);
				rgRONB.setVisibility(View.VISIBLE);
				break;

			case R.id.rbRONoBallNone:
				extraData = new Extra(ExtraType.NO_BALL, sbRORuns.getProgress(), ExtraType.NONE);
                adjustROExtraRuns(view);
                break;

			case R.id.rbRONoBallBye:
				extraData = new Extra(ExtraType.NO_BALL, sbRORuns.getProgress(), ExtraType.BYE);
                adjustROExtraRuns(view);
                break;

			case R.id.rbRONoBallLegBye:
				extraData = new Extra(ExtraType.NO_BALL, sbRORuns.getProgress(), ExtraType.LEG_BYE);
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
			case R.id.rbRONoBall:
				clearOtherCheckedRadioButtons(glRORunsExtra, view.getId());
				minExtraRuns = 0;
				break;

			case R.id.rbRONoBallNone:
				minExtraRuns = 1;
				break;

			case R.id.rbRONoBallBye:
			case R.id.rbRONoBallLegBye:
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

	private void setViewVisibility(int effectedByVisibility, int outBatsmanVisibility, int runsVisibility, int isExtraVisibility) {
		LinearLayout llWicketDetails = findViewById(R.id.llWicketDetails);
		LinearLayout llEffectedBy = findViewById(R.id.llEffectedBy);
		LinearLayout llOutBatsman = findViewById(R.id.llOutBatsman);
		LinearLayout llRORuns = findViewById(R.id.llRORuns);
		LinearLayout llExtra = findViewById(R.id.llExtra);

		if(effectedByVisibility == View.VISIBLE || outBatsmanVisibility == View.VISIBLE || runsVisibility == View.VISIBLE || isExtraVisibility == View.VISIBLE)
			llWicketDetails.setVisibility(View.VISIBLE);
		else
			llWicketDetails.setVisibility(View.INVISIBLE);

		llEffectedBy.setVisibility(effectedByVisibility);
		llOutBatsman.setVisibility(outBatsmanVisibility);
		llRORuns.setVisibility(runsVisibility);
		llExtra.setVisibility(isExtraVisibility);

		if(llExtra.getVisibility() == View.GONE) {
			extraData = null;
		}
	}

	private void showFielderDialog() {
		if(getSupportFragmentManager() != null) {
			String[] fielders = new String[fieldingTeam.length];

			int i=0;
			for(Player fielder : fieldingTeam)
				fielders[i++] = fielder.getName();

			StringDialog dialog = StringDialog.newInstance("Select Fielder", fielders, null);
			dialog.setDialogItemClickListener(this);
			dialog.show(getSupportFragmentManager(), "EffectedByDialog");
		}
	}

	private void displayBatsmen() {
	    Intent batsmanIntent = new Intent(this, BatsmanSelectActivity.class);

		BatsmanStats[] batsmen;
		if (dismissalType != null && dismissalType == DismissalType.TIMED_OUT) {
			if(facingBatsman.getBallsPlayed() == 0 && otherBatsman.getBallsPlayed() == 0) {
				if(facingBatsman.getPosition() <= 2 && otherBatsman.getPosition() <= 2) {
					batsmen = new BatsmanStats[]{facingBatsman, otherBatsman};
				} else {
					BatsmanStats batsman = (facingBatsman.getPosition() > otherBatsman.getPosition()) ? facingBatsman : otherBatsman;
					batsmen = new BatsmanStats[]{batsman};
				}
			} else {
				BatsmanStats batsman = (facingBatsman.getBallsPlayed() == 0) ? facingBatsman : otherBatsman;
				batsmen = new BatsmanStats[]{batsman};
			}
		} else {
	    	batsmen = new BatsmanStats[]{facingBatsman, otherBatsman};
		}

	    batsmanIntent.putExtra(BatsmanSelectActivity.ARG_BATSMAN_LIST, batsmen);
        batsmanIntent.putExtra(BatsmanSelectActivity.ARG_DEFAULT_SEL_INDEX, 0);
	    startActivityForResult(batsmanIntent, ACTIVITY_REQ_CODE_OUT_BATSMAN_SELECT);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case ACTIVITY_REQ_CODE_FIELDER_SELECT:
				if(resultCode == RESP_CODE_OK) {
					effectedBy = (Player) data.getSerializableExtra(PlayerSelectActivity.ARG_RESP_SEL_PLAYER);
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

	private boolean validateData() {
		boolean isValid = true;

		if(dismissalType != null) {
			switch (dismissalType) {
				case CAUGHT:
					if (effectedBy == null) {
						Toast.makeText(this, "Please choose Caught By Player", Toast.LENGTH_SHORT).show();
						isValid = false;
					}
					break;

				case RUN_OUT:
					if (effectedBy == null) {
						Toast.makeText(this, "Please choose Run out by", Toast.LENGTH_SHORT).show();
						isValid = false;
					} else if (outBatsman == null) {
						Toast.makeText(this, "Please choose Batsman who is out", Toast.LENGTH_SHORT).show();
						isValid = false;
					} else if (extraData == null) {
						batsmanRuns = sbRORuns.getProgress();
					}
					break;

				case TIMED_OUT:
				case OBSTRUCTING_FIELD:
				case RETIRED:
					if (outBatsman == null) {
						Toast.makeText(this, "Please choose Batsman who is out", Toast.LENGTH_SHORT).show();
						isValid = false;
					}
					break;
			}
		} else {
			Toast.makeText(this, "Select a dismissal type", Toast.LENGTH_SHORT).show();
			isValid = false;
		}

		return isValid;
	}

    private void sendResponse(int responseCode) {
        if(responseCode == RESP_CODE_OK) {
			Intent respIntent = new Intent();
			WicketData wicketData;

        	if(validateData()) {
				wicketData = new WicketData(outBatsman, dismissalType, effectedBy, bowler);
				respIntent.putExtra(ARG_WICKET_DATA, wicketData);
				if(extraData != null)
					respIntent.putExtra(ARG_EXTRA_DATA, extraData);
				if(batsmanRuns > 0)
					respIntent.putExtra(ARG_BATSMAN_RUNS, batsmanRuns);

				setResult(responseCode, respIntent);

				finish();
			}
		} else if(responseCode == RESP_CODE_CANCEL) {
			setResult(responseCode);
			finish();
		}
    }

	@Override
	public void onItemSelect(String type, String value, int position) {
		effectedBy = fieldingTeam[position];
		tvEffectedBy.setText(effectedBy.getName());
	}
}
