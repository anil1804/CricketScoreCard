package com.thenewcone.myscorecard.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.match.CricketCard;
import com.thenewcone.myscorecard.player.BatsmanStats;
import com.thenewcone.myscorecard.player.BowlerStats;
import com.thenewcone.myscorecard.scorecard.Extra;
import com.thenewcone.myscorecard.scorecard.WicketData;
import com.thenewcone.myscorecard.utils.CommonUtils;

public class LimitedOversFragment extends Fragment
	implements View.OnClickListener{
	View theView;

	CricketCard card;

	/*public LimitedOversFragment() {
		// Required empty public constructor
	}*/

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
		theView.findViewById(R.id.btnRuns5).setOnClickListener(this);
		theView.findViewById(R.id.btnRuns6).setOnClickListener(this);
		theView.findViewById(R.id.btnWicket).setOnClickListener(this);
		theView.findViewById(R.id.btnExtrasLegByes).setOnClickListener(this);
		theView.findViewById(R.id.btnExtrasByes).setOnClickListener(this);
		theView.findViewById(R.id.btnExtrasWides).setOnClickListener(this);
		theView.findViewById(R.id.btnExtrasNoBall).setOnClickListener(this);
	}


	private void initCricketCard() {
		card = new CricketCard("Team1", "50.0", 5, 10, 10);

		card.newBatsman(new BatsmanStats("Batsman 1", 1), null);
		card.newBatsman(new BatsmanStats("Batsman 2", 2), null);
		card.setBowler(new BowlerStats("Bowler"));
	}

	private void updateCardDetails(boolean isInitial) {
		/* Main Score Details*/
		if(isInitial) {
			TextView tvBattingTeam = theView.findViewById(R.id.tvBattingTeam);
			tvBattingTeam.setText(card.battingTeam);

		}

		TextView tvCurrScore = theView.findViewById(R.id.tvScore);
		TextView tvOvers = theView.findViewById(R.id.tvOvers);
		TextView tvCRR = theView.findViewById(R.id.tvCRR);

		tvCurrScore.setText(String.valueOf(card.score + "/" + card.wicketsFallen));
		tvOvers.setText(String.format(getString(R.string.tvOversText), card.totalOversBowled));
		tvCRR.setText(CommonUtils.doubleToString(card.runRate, "#.##"));

		/* Chasing Score Details*/
		TextView tvTarget = theView.findViewById(R.id.tvTarget);
		TextView tvRRR = theView.findViewById(R.id.tvRRR);
		TextView tvMaxOvers = theView.findViewById(R.id.tvMaxOvers);
		if(card.innings == 2) {
			if (isInitial) {
				tvTarget.setText(String.valueOf(card.target));

				tvMaxOvers.setText(String.format(getString(R.string.tvOversText), card.maxOvers));
			}

			tvRRR.setText(CommonUtils.doubleToString(card.reqRate, "#.##"));
		} else {
			tvTarget.setText("-");
			tvRRR.setText("-");
			tvMaxOvers.setText("");
		}

		/* Batsman-1 Details*/
		TextView tvBat1Name = theView.findViewById(R.id.tvBat1Name);
		TextView tvBat1Runs = theView.findViewById(R.id.tvBat1RunsScored);
		TextView tvBat1Balls = theView.findViewById(R.id.tvBat1BallsFaced);
		TextView tvBat14s = theView.findViewById(R.id.tvBat14sHit);
		TextView tvBat16s = theView.findViewById(R.id.tvBat16sHit);
		TextView tvBat1SR = theView.findViewById(R.id.tvBat1SR);

		tvBat1Name.setText(card.currentFacing.equals(card.batsman1) ? String.valueOf(card.batsman1.getBatsmanName() + " *") : card.batsman1.getBatsmanName());
		tvBat1Runs.setText(String.valueOf(card.batsman1.getRunsScored()));
		tvBat1Balls.setText(String.valueOf(card.batsman1.getBallsPlayed()));
		tvBat14s.setText(String.valueOf(card.batsman1.getNum4s()));
		tvBat16s.setText(String.valueOf(card.batsman1.getNum6s()));
		tvBat1SR.setText(CommonUtils.doubleToString(card.batsman1.getStrikeRate(), "#.##"));

		/* Batsman-2 Details*/
		TextView tvBat2Name = theView.findViewById(R.id.tvBat2Name);
		TextView tvBat2Runs = theView.findViewById(R.id.tvBat2RunsScored);
		TextView tvBat2Balls = theView.findViewById(R.id.tvBat2BallsFaced);
		TextView tvBat24s = theView.findViewById(R.id.tvBat24sHit);
		TextView tvBat26s = theView.findViewById(R.id.tvBat26sHit);
		TextView tvBat2SR = theView.findViewById(R.id.tvBat2SR);

		tvBat2Name.setText(card.currentFacing.equals(card.batsman2) ? String.valueOf(card.batsman2.getBatsmanName() + " *") : card.batsman2.getBatsmanName());
		tvBat2Runs.setText(String.valueOf(card.batsman2.getRunsScored()));
		tvBat2Balls.setText(String.valueOf(card.batsman2.getBallsPlayed()));
		tvBat24s.setText(String.valueOf(card.batsman2.getNum4s()));
		tvBat26s.setText(String.valueOf(card.batsman2.getNum6s()));
		tvBat2SR.setText(CommonUtils.doubleToString(card.batsman2.getStrikeRate(), "#.##"));

		/* Extras Details*/
		TextView tvLegByes = theView.findViewById(R.id.tvLegByes);
		TextView tvByes = theView.findViewById(R.id.tvByes);
		TextView tvWides = theView.findViewById(R.id.tvWides);
		TextView tvNoBalls = theView.findViewById(R.id.tvNoBalls);

		tvLegByes.setText(String.format(getString(R.string.legByes), card.legByes));
		tvByes.setText(String.format(getString(R.string.byes), card.byes));
		tvWides.setText(String.format(getString(R.string.wides), card.wides));
		tvNoBalls.setText(String.format(getString(R.string.noBalls), card.noBalls));

		/* Bowler Details */
		TextView tvBowlName = theView.findViewById(R.id.tvBowlName);
		TextView tvBowlOvers = theView.findViewById(R.id.tvBowlOvers);
		TextView tvBowlMaidens = theView.findViewById(R.id.tvBowlMaidens);
		TextView tvBowlRuns = theView.findViewById(R.id.tvBowlRuns);
		TextView tvBowlWickets = theView.findViewById(R.id.tvBowlWickets);
		TextView tvBowlEconomy = theView.findViewById(R.id.tvBowlEconomy);

		tvBowlName.setText(card.bowler.getBowlerName());
		tvBowlOvers.setText(card.bowler.getOversBowled());
		tvBowlMaidens.setText(String.valueOf(card.bowler.getMaidens()));
		tvBowlRuns.setText(String.valueOf(card.bowler.getRunsGiven()));
		tvBowlWickets.setText(String.valueOf(card.bowler.getWickets()));
		tvBowlEconomy.setText(CommonUtils.doubleToString(card.bowler.getEconomy(), "#.##"));
	}

	private void newBallBowled(int runs, @Nullable Extra extraData, @Nullable WicketData wicketData) {
		card.newBallBowled(extraData, runs, wicketData, false);
		updateCardDetails(false);
	}

	@Override
	public void onClick(View view) {
		Extra extra;
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
				WicketData wicketData = new WicketData(card.currentFacing, WicketData.DismissalType.BOWLED, null, card.bowler);
				newBallBowled(0, null, wicketData);
				break;
			case R.id.btnExtrasLegByes:
				extra = new Extra(Extra.ExtraType.LEG_BYE, 1);
				newBallBowled(0, extra, null);
				break;
			case R.id.btnExtrasByes:
				extra = new Extra(Extra.ExtraType.BYE, 1);
				newBallBowled(0, extra, null);
				break;
			case R.id.btnExtrasWides:
				extra = new Extra(Extra.ExtraType.WIDE, 1);
				newBallBowled(0, extra, null);
				break;
			case R.id.btnExtrasNoBall:
				extra = new Extra(Extra.ExtraType.NO_BALL, 0);
				newBallBowled(0, extra, null);
				break;
		}
	}
}