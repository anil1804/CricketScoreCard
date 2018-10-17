package com.thenewcone.myscorecard.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thenewcone.myscorecard.activity.ExtraDialogActivity;
import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.activity.WicketDialogActivity;
import com.thenewcone.myscorecard.match.CricketCard;
import com.thenewcone.myscorecard.match.CricketCardUtils;
import com.thenewcone.myscorecard.player.BatsmanStats;
import com.thenewcone.myscorecard.player.BowlerStats;
import com.thenewcone.myscorecard.scorecard.Extra;
import com.thenewcone.myscorecard.scorecard.WicketData;
import com.thenewcone.myscorecard.utils.CommonUtils;

public class LimitedOversFragment extends Fragment
	implements View.OnClickListener{
	View theView;

	private static final int ACTIVITY_REQ_CODE_EXTRA_DIALOG = 1;
	private static final int ACTIVITY_REQ_CODE_WICKET_DIALOG = 2;

	CricketCardUtils ccUtils;

	public LimitedOversFragment() {
		// Required empty public constructor
	}

	public static LimitedOversFragment newInstance() {
		return new LimitedOversFragment();
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		theView = inflater.inflate(R.layout.fragment_limited_overs, container, false);

		initCricketCard();
		initialSetup();

		return theView;
	}

	private void initialSetup() {
		updateCardDetails(true);

		theView.findViewById(R.id.btnRuns0).setOnClickListener(this);
		theView.findViewById(R.id.btnRuns1).setOnClickListener(this);
		theView.findViewById(R.id.btnRuns2).setOnClickListener(this);
		theView.findViewById(R.id.btnRuns3).setOnClickListener(this);
		theView.findViewById(R.id.btnRuns4).setOnClickListener(this);
		theView.findViewById(R.id.btnRuns6).setOnClickListener(this);
		theView.findViewById(R.id.btnWicket).setOnClickListener(this);
		theView.findViewById(R.id.btnExtrasLegByes).setOnClickListener(this);
		theView.findViewById(R.id.btnExtrasByes).setOnClickListener(this);
		theView.findViewById(R.id.btnExtrasWides).setOnClickListener(this);
		theView.findViewById(R.id.btnExtrasNoBall).setOnClickListener(this);
		theView.findViewById(R.id.btnExtraPenalty).setOnClickListener(this);
	}


	private void initCricketCard() {
		CricketCard card = new CricketCard("Team1", "50.0", 5, 10, 10, 1);

		ccUtils = new CricketCardUtils(card);

		ccUtils.newBatsman(new BatsmanStats("Batsman 1", 1), null);
		ccUtils.newBatsman(new BatsmanStats("Batsman 2", 2), null);
		ccUtils.setBowler(new BowlerStats("Bowler"));

	}

	private void updateCardDetails(boolean isInitial) {
		CricketCard currCard = ccUtils.getCard();

		/* Main Score Details*/
		if(isInitial) {
			TextView tvBattingTeam = theView.findViewById(R.id.tvBattingTeam);
			tvBattingTeam.setText(currCard.getBattingTeam());

		}

		TextView tvCurrScore = theView.findViewById(R.id.tvScore);
		TextView tvOvers = theView.findViewById(R.id.tvOvers);
		TextView tvCRR = theView.findViewById(R.id.tvCRR);

		tvCurrScore.setText(String.valueOf(currCard.getScore() + "/" + currCard.getWicketsFallen()));
		tvOvers.setText(String.format(getString(R.string.tvOversText), currCard.getTotalOversBowled()));
		tvCRR.setText(CommonUtils.doubleToString(currCard.getRunRate(), "#.##"));

		/* Chasing Score Details*/
		TextView tvTarget = theView.findViewById(R.id.tvTarget);
		TextView tvRRR = theView.findViewById(R.id.tvRRR);
		TextView tvMaxOvers = theView.findViewById(R.id.tvMaxOvers);
		if (isInitial) {
			if (currCard.getInnings() == 2) {
				tvTarget.setText(String.valueOf(currCard.getTarget()));
				tvMaxOvers.setText(String.format(getString(R.string.tvOversText), currCard.getMaxOvers()));
			} else {
				tvTarget.setText("-");
				tvRRR.setText("-");
				tvMaxOvers.setText("");
			}
		} else {
			if(currCard.getInnings() == 2) {
				tvRRR.setText(CommonUtils.doubleToString(currCard.getReqRate(), "#.##"));
			}
		}

		/* Batsman-1 Details*/
		TextView tvBat1Name = theView.findViewById(R.id.tvBat1Name);
		TextView tvBat1Runs = theView.findViewById(R.id.tvBat1RunsScored);
		TextView tvBat1Balls = theView.findViewById(R.id.tvBat1BallsFaced);
		TextView tvBat14s = theView.findViewById(R.id.tvBat14sHit);
		TextView tvBat16s = theView.findViewById(R.id.tvBat16sHit);
		TextView tvBat1SR = theView.findViewById(R.id.tvBat1SR);

		tvBat1Name.setText(ccUtils.getCurrentFacing().equals(ccUtils.getBatsman1())
				? String.valueOf(ccUtils.getBatsman1().getBatsmanName() + " *")
				: ccUtils.getBatsman1().getBatsmanName());
		tvBat1Runs.setText(String.valueOf(ccUtils.getBatsman1().getRunsScored()));
		tvBat1Balls.setText(String.valueOf(ccUtils.getBatsman1().getBallsPlayed()));
		tvBat14s.setText(String.valueOf(ccUtils.getBatsman1().getNum4s()));
		tvBat16s.setText(String.valueOf(ccUtils.getBatsman1().getNum6s()));
		tvBat1SR.setText(CommonUtils.doubleToString(ccUtils.getBatsman1().getStrikeRate(), "#.##"));

		/* Batsman-2 Details*/
		TextView tvBat2Name = theView.findViewById(R.id.tvBat2Name);
		TextView tvBat2Runs = theView.findViewById(R.id.tvBat2RunsScored);
		TextView tvBat2Balls = theView.findViewById(R.id.tvBat2BallsFaced);
		TextView tvBat24s = theView.findViewById(R.id.tvBat24sHit);
		TextView tvBat26s = theView.findViewById(R.id.tvBat26sHit);
		TextView tvBat2SR = theView.findViewById(R.id.tvBat2SR);

		tvBat2Name.setText(ccUtils.getCurrentFacing().equals(ccUtils.getBatsman2())
				? String.valueOf(ccUtils.getBatsman2().getBatsmanName() + " *")
				: ccUtils.getBatsman2().getBatsmanName());
		tvBat2Runs.setText(String.valueOf(ccUtils.getBatsman2().getRunsScored()));
		tvBat2Balls.setText(String.valueOf(ccUtils.getBatsman2().getBallsPlayed()));
		tvBat24s.setText(String.valueOf(ccUtils.getBatsman2().getNum4s()));
		tvBat26s.setText(String.valueOf(ccUtils.getBatsman2().getNum6s()));
		tvBat2SR.setText(CommonUtils.doubleToString(ccUtils.getBatsman2().getStrikeRate(), "#.##"));

		/* Extras Details*/
		TextView tvLegByes = theView.findViewById(R.id.tvLegByes);
		TextView tvByes = theView.findViewById(R.id.tvByes);
		TextView tvWides = theView.findViewById(R.id.tvWides);
		TextView tvNoBalls = theView.findViewById(R.id.tvNoBalls);
		TextView tvPenalty = theView.findViewById(R.id.tvPenalty);

		tvLegByes.setText(String.format(getString(R.string.legByes), currCard.getLegByes()));
		tvByes.setText(String.format(getString(R.string.byes), currCard.getByes()));
		tvWides.setText(String.format(getString(R.string.wides), currCard.getWides()));
		tvNoBalls.setText(String.format(getString(R.string.noBalls), currCard.getNoBalls()));
		tvPenalty.setText(currCard.getPenalty() > 0 ? String.format(getString(R.string.penalty), currCard.getPenalty()) : "");

		/* Bowler Details */
		TextView tvBowlName = theView.findViewById(R.id.tvBowlName);
		TextView tvBowlOvers = theView.findViewById(R.id.tvBowlOvers);
		TextView tvBowlMaidens = theView.findViewById(R.id.tvBowlMaidens);
		TextView tvBowlRuns = theView.findViewById(R.id.tvBowlRuns);
		TextView tvBowlWickets = theView.findViewById(R.id.tvBowlWickets);
		TextView tvBowlEconomy = theView.findViewById(R.id.tvBowlEconomy);

		tvBowlName.setText(ccUtils.getBowler().getBowlerName());
		tvBowlOvers.setText(ccUtils.getBowler().getOversBowled());
		tvBowlMaidens.setText(String.valueOf(ccUtils.getBowler().getMaidens()));
		tvBowlRuns.setText(String.valueOf(ccUtils.getBowler().getRunsGiven()));
		tvBowlWickets.setText(String.valueOf(ccUtils.getBowler().getWickets()));
		tvBowlEconomy.setText(CommonUtils.doubleToString(ccUtils.getBowler().getEconomy(), "#.##"));
	}

	private void newBallBowled(int runs, @Nullable Extra extraData, @Nullable WicketData wicketData) {
		ccUtils.processBallActivity(extraData, runs, wicketData, false);
		updateCardDetails(false);
	}

	@Override
	public void onClick(View view) {
		newBallBowled(view);
	}

	private void newBallBowled(View view) {
		switch (view.getId()) {
			case R.id.btnRuns0:
				newBallBowled(0, null, null);
				break;

			case R.id.btnRuns1:
				newBallBowled(1, null, null);
				break;

			case R.id.btnRuns2:
				newBallBowled(2, null, null);
				break;

			case R.id.btnRuns3:
				newBallBowled(3, null, null);
				break;

			case R.id.btnRuns4:
				newBallBowled(4, null, null);
				break;

			case R.id.btnRuns6:
				newBallBowled(6, null, null);
				break;

			case R.id.btnWicket:
				displayWicketDialog();
				break;

			case R.id.btnExtraPenalty:
				displayExtrasDialog(Extra.ExtraType.PENALTY);
				break;

			case R.id.btnExtrasLegByes:
				displayExtrasDialog(Extra.ExtraType.LEG_BYE);
				break;

			case R.id.btnExtrasByes:
				displayExtrasDialog(Extra.ExtraType.BYE);
				break;

			case R.id.btnExtrasWides:
				displayExtrasDialog(Extra.ExtraType.WIDE);
				break;

			case R.id.btnExtrasNoBall:
				displayExtrasDialog(Extra.ExtraType.NO_BALL);
				break;
		}
	}

	private void displayExtrasDialog(Extra.ExtraType type) {
		Intent dialogIntent = new Intent(getContext(), ExtraDialogActivity.class);
		dialogIntent.putExtra(CommonUtils.ARG_EXTRA_TYPE, type);
		startActivityForResult(dialogIntent, ACTIVITY_REQ_CODE_EXTRA_DIALOG);
	}

	private void displayWicketDialog() {
		Intent dialogIntent = new Intent(getContext(), WicketDialogActivity.class);

		BatsmanStats otherBatmsan = (ccUtils.getCurrentFacing().getPosition() == ccUtils.getBatsman1().getPosition())
				? ccUtils.getBatsman2()
				: ccUtils.getBatsman1();

		dialogIntent.putExtra(WicketDialogActivity.ARG_FACING_BATSMAN, ccUtils.getCurrentFacing());
		dialogIntent.putExtra(WicketDialogActivity.ARG_OTHER_BATSMAN, otherBatmsan);
		dialogIntent.putExtra(WicketDialogActivity.ARG_BOWLER, ccUtils.getBowler());
		dialogIntent.putExtra(WicketDialogActivity.ARG_FIELDING_TEAM, ccUtils.getBowlingTeam().toArray());

		startActivityForResult(dialogIntent, ACTIVITY_REQ_CODE_WICKET_DIALOG);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case ACTIVITY_REQ_CODE_EXTRA_DIALOG:
				if (resultCode == ExtraDialogActivity.RESULT_CODE_OK) {
					Extra.ExtraType extraType = (Extra.ExtraType) data.getSerializableExtra(CommonUtils.ARG_EXTRA_TYPE);
					Extra.ExtraType extraSubType = (Extra.ExtraType) data.getSerializableExtra(ExtraDialogActivity.ARG_NB_EXTRA);
					int extraRuns = data.getIntExtra(ExtraDialogActivity.ARG_EXTRA_RUNS, -1);
					String team = data.getStringExtra(ExtraDialogActivity.ARG_TEAM);

					ccUtils.processExtra(extraType, extraRuns, team, extraSubType);
					updateCardDetails(false);
				}
				break;

			case ACTIVITY_REQ_CODE_WICKET_DIALOG:
				if(resultCode == WicketDialogActivity.RESP_CODE_OK) {
					WicketData wicketData = new WicketData(ccUtils.getCurrentFacing(),
							WicketData.DismissalType.BOWLED, null, ccUtils.getBowler());
					newBallBowled(0, null, wicketData);
				}

		}
	}
}