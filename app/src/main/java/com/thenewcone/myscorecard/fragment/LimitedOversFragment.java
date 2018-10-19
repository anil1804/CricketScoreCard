package com.thenewcone.myscorecard.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.thenewcone.myscorecard.activity.BatsmanSelectActivity;
import com.thenewcone.myscorecard.activity.BowlerSelectActivity;
import com.thenewcone.myscorecard.activity.ExtraDialogActivity;
import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.activity.WicketDialogActivity;
import com.thenewcone.myscorecard.match.CricketCard;
import com.thenewcone.myscorecard.match.CricketCardUtils;
import com.thenewcone.myscorecard.player.BatsmanStats;
import com.thenewcone.myscorecard.player.BowlerStats;
import com.thenewcone.myscorecard.player.Player;
import com.thenewcone.myscorecard.scorecard.Extra;
import com.thenewcone.myscorecard.scorecard.WicketData;
import com.thenewcone.myscorecard.utils.CommonUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class LimitedOversFragment extends Fragment
	implements View.OnClickListener{
	View theView;
	WicketData.DismissalType dismissalType;

	private static final int ACTIVITY_REQ_CODE_EXTRA_DIALOG = 1;
	private static final int ACTIVITY_REQ_CODE_WICKET_DIALOG = 2;
    private static final int ACTIVITY_REQ_CODE_BATSMAN_DIALOG = 3;
    private static final int ACTIVITY_REQ_CODE_BOWLER_DIALOG = 4;
    private static final int ACTIVITY_REQ_CODE_CURRENT_FACING_DIALOG = 5;

	CricketCardUtils ccUtils;
    BatsmanStats newBatsman, outBatsman;

    TableRow trBatsman1, trBatsman2;
    TextView tvCurrScore, tvOvers, tvCRR, tvRRR;
    TextView tvBat1Name, tvBat1Runs, tvBat1Balls, tvBat14s, tvBat16s, tvBat1SR;
    TextView tvBat2Name, tvBat2Runs, tvBat2Balls, tvBat24s, tvBat26s, tvBat2SR;
    TextView tvLegByes, tvByes, tvWides, tvNoBalls, tvPenalty;
    TextView tvBowlName, tvBowlOvers, tvBowlMaidens, tvBowlRuns, tvBowlWickets, tvBowlEconomy;

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
		theView.findViewById(R.id.btnSelBatsman).setOnClickListener(this);
        theView.findViewById(R.id.btnSelBowler).setOnClickListener(this);
        theView.findViewById(R.id.btnSelFacingBatsman).setOnClickListener(this);
	}

	private void initCricketCard() {
		CricketCard card = new CricketCard("Team1", "50.0", 5, 10, 10, 1);

		ccUtils = new CricketCardUtils(card);

		ccUtils.addToBattingTeam(new Player("Player-11", (new Random().nextInt(20))+18, Player.BattingType.RHB, Player.BowlingType.NONE));
        ccUtils.addToBattingTeam(new Player("Player-12", (new Random().nextInt(20))+18, Player.BattingType.LHB, Player.BowlingType.NONE));
        ccUtils.addToBattingTeam(new Player("Player-13", (new Random().nextInt(20))+18, Player.BattingType.RHB, Player.BowlingType.OB));
        ccUtils.addToBattingTeam(new Player("Player-14", (new Random().nextInt(20))+18, Player.BattingType.RHB, Player.BowlingType.NONE));
        ccUtils.addToBattingTeam(new Player("Player-15", (new Random().nextInt(20))+18, Player.BattingType.LHB, Player.BowlingType.SLA));
        ccUtils.addToBattingTeam(new Player("Player-16", (new Random().nextInt(20))+18, Player.BattingType.RHB, Player.BowlingType.RM));
        ccUtils.addToBattingTeam(new Player("Player-17", (new Random().nextInt(20))+18, Player.BattingType.LHB, Player.BowlingType.LF));
        ccUtils.addToBattingTeam(new Player("Player-18", (new Random().nextInt(20))+18, Player.BattingType.RHB, Player.BowlingType.RFM));


        ccUtils.addToBowlingTeam(new Player("Player-21", (new Random().nextInt(20))+18, Player.BattingType.LHB, Player.BowlingType.NONE));
        ccUtils.addToBowlingTeam(new Player("Player-22", (new Random().nextInt(20))+18, Player.BattingType.RHB, Player.BowlingType.NONE));
        ccUtils.addToBowlingTeam(new Player("Player-23", (new Random().nextInt(20))+18, Player.BattingType.LHB, Player.BowlingType.SLC));
        ccUtils.addToBowlingTeam(new Player("Player-24", (new Random().nextInt(20))+18, Player.BattingType.RHB, Player.BowlingType.LB));
        ccUtils.addToBowlingTeam(new Player("Player-25", (new Random().nextInt(20))+18, Player.BattingType.LHB, Player.BowlingType.NONE));
        ccUtils.addToBowlingTeam(new Player("Player-26", (new Random().nextInt(20))+18, Player.BattingType.RHB, Player.BowlingType.RM));
        ccUtils.addToBowlingTeam(new Player("Player-27", (new Random().nextInt(20))+18, Player.BattingType.RHB, Player.BowlingType.RM));
        ccUtils.addToBowlingTeam(new Player("Player-28", (new Random().nextInt(20))+18, Player.BattingType.LHB, Player.BowlingType.LFM));

        ccUtils.newBatsman(new BatsmanStats(ccUtils.getBattingTeam().get(0), 1));
        ccUtils.newBatsman(new BatsmanStats(ccUtils.getBattingTeam().get(1), 2));
        ccUtils.setBowler(new BowlerStats(ccUtils.getBowlingTeam().get(7)));
	}

	private void updateCardDetails(boolean isInitial) {
		CricketCard currCard = ccUtils.getCard();

		/* Main Score Details*/
		if(isInitial) {
			TextView tvBattingTeam = theView.findViewById(R.id.tvBattingTeam);
			tvBattingTeam.setText(currCard.getBattingTeam());

		}

		if(isInitial) {
            tvCurrScore = theView.findViewById(R.id.tvScore);
            tvOvers = theView.findViewById(R.id.tvOvers);
            tvCRR = theView.findViewById(R.id.tvCRR);
        }
		tvCurrScore.setText(String.valueOf(currCard.getScore() + "/" + currCard.getWicketsFallen()));
		tvOvers.setText(String.format(getString(R.string.tvOversText), currCard.getTotalOversBowled()));
		tvCRR.setText(CommonUtils.doubleToString(currCard.getRunRate(), "#.##"));

		/* Chasing Score Details*/
		tvRRR = theView.findViewById(R.id.tvRRR);
		if (isInitial) {
            TextView tvTarget = theView.findViewById(R.id.tvTarget);
            TextView tvMaxOvers = theView.findViewById(R.id.tvMaxOvers);
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
        if(isInitial) {
            trBatsman1 = theView.findViewById(R.id.trBatsman1);
            tvBat1Name = theView.findViewById(R.id.tvBat1Name);
            tvBat1Runs = theView.findViewById(R.id.tvBat1RunsScored);
            tvBat1Balls = theView.findViewById(R.id.tvBat1BallsFaced);
            tvBat14s = theView.findViewById(R.id.tvBat14sHit);
            tvBat16s = theView.findViewById(R.id.tvBat16sHit);
            tvBat1SR = theView.findViewById(R.id.tvBat1SR);
        }


		if(ccUtils.getCurrentFacing() != null) {
            trBatsman1.setVisibility(View.VISIBLE);
            tvBat1Name.setText(String.valueOf(ccUtils.getCurrentFacing().getBatsmanName() + " *"));
            tvBat1Runs.setText(String.valueOf(ccUtils.getCurrentFacing().getRunsScored()));
            tvBat1Balls.setText(String.valueOf(ccUtils.getCurrentFacing().getBallsPlayed()));
            tvBat14s.setText(String.valueOf(ccUtils.getCurrentFacing().getNum4s()));
            tvBat16s.setText(String.valueOf(ccUtils.getCurrentFacing().getNum6s()));
            tvBat1SR.setText(CommonUtils.doubleToString(ccUtils.getCurrentFacing().getStrikeRate(), "#.##"));
        } else {
            trBatsman1.setVisibility(View.GONE);
        }

		/* Batsman-2 Details*/
        if(isInitial) {
            trBatsman2 = theView.findViewById(R.id.trBatsman2);
            tvBat2Name = theView.findViewById(R.id.tvBat2Name);
            tvBat2Runs = theView.findViewById(R.id.tvBat2RunsScored);
            tvBat2Balls = theView.findViewById(R.id.tvBat2BallsFaced);
            tvBat24s = theView.findViewById(R.id.tvBat24sHit);
            tvBat26s = theView.findViewById(R.id.tvBat26sHit);
            tvBat2SR = theView.findViewById(R.id.tvBat2SR);
        }

		if(ccUtils.getOtherBatsman() != null) {
            trBatsman2.setVisibility(View.VISIBLE);
            tvBat2Name.setText(ccUtils.getOtherBatsman().getBatsmanName());
            tvBat2Runs.setText(String.valueOf(ccUtils.getOtherBatsman().getRunsScored()));
            tvBat2Balls.setText(String.valueOf(ccUtils.getOtherBatsman().getBallsPlayed()));
            tvBat24s.setText(String.valueOf(ccUtils.getOtherBatsman().getNum4s()));
            tvBat26s.setText(String.valueOf(ccUtils.getOtherBatsman().getNum6s()));
            tvBat2SR.setText(CommonUtils.doubleToString(ccUtils.getOtherBatsman().getStrikeRate(), "#.##"));
        } else {
            trBatsman2.setVisibility(View.GONE);
        }

		/* Extras Details*/
        if(isInitial) {
            tvLegByes = theView.findViewById(R.id.tvLegByes);
            tvByes = theView.findViewById(R.id.tvByes);
            tvWides = theView.findViewById(R.id.tvWides);
            tvNoBalls = theView.findViewById(R.id.tvNoBalls);
            tvPenalty = theView.findViewById(R.id.tvPenalty);
        }

		tvLegByes.setText(String.format(getString(R.string.legByes), currCard.getLegByes()));
		tvByes.setText(String.format(getString(R.string.byes), currCard.getByes()));
		tvWides.setText(String.format(getString(R.string.wides), currCard.getWides()));
		tvNoBalls.setText(String.format(getString(R.string.noBalls), currCard.getNoBalls()));
		tvPenalty.setText(currCard.getPenalty() > 0 ? String.format(getString(R.string.penalty), currCard.getPenalty()) : "");

		/* Bowler Details */
        if(isInitial) {
            tvBowlName = theView.findViewById(R.id.tvBowlName);
            tvBowlOvers = theView.findViewById(R.id.tvBowlOvers);
            tvBowlMaidens = theView.findViewById(R.id.tvBowlMaidens);
            tvBowlRuns = theView.findViewById(R.id.tvBowlRuns);
            tvBowlWickets = theView.findViewById(R.id.tvBowlWickets);
            tvBowlEconomy = theView.findViewById(R.id.tvBowlEconomy);
        }

		tvBowlName.setText(ccUtils.getBowler().getBowlerName());
		tvBowlOvers.setText(ccUtils.getBowler().getOversBowled());
		tvBowlMaidens.setText(String.valueOf(ccUtils.getBowler().getMaidens()));
		tvBowlRuns.setText(String.valueOf(ccUtils.getBowler().getRunsGiven()));
		tvBowlWickets.setText(String.valueOf(ccUtils.getBowler().getWickets()));
		tvBowlEconomy.setText(CommonUtils.doubleToString(ccUtils.getBowler().getEconomy(), "#.##"));
	}

	private void newBallBowled(int runs, @Nullable WicketData wicketData) {
		ccUtils.processBallActivity(null, runs, wicketData, false);
		updateCardDetails(false);
	}

	@Override
	public void onClick(View view) {
		newBallBowled(view);
	}

	private void newBallBowled(View view) {
		switch (view.getId()) {
			case R.id.btnRuns0:
				newBallBowled(0, null);
				break;

			case R.id.btnRuns1:
				newBallBowled(1, null);
				break;

			case R.id.btnRuns2:
				newBallBowled(2, null);
				break;

			case R.id.btnRuns3:
				newBallBowled(3, null);
				break;

			case R.id.btnRuns4:
				newBallBowled(4, null);
				break;

			case R.id.btnRuns6:
				newBallBowled(6, null);
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

            case R.id.btnSelBatsman:
                SparseArray<BatsmanStats> batsmen = ccUtils.getCard().getBatsmen();
                BatsmanStats[] batsmenPlayed = new BatsmanStats[batsmen.size()];

                for(int i=0; i<batsmen.size(); i++) {
                    batsmenPlayed[i] = batsmen.get(i+1);
                }

                displayBatsmanSelect(ccUtils.getBattingTeam(), batsmenPlayed, ACTIVITY_REQ_CODE_BATSMAN_DIALOG);
                break;

            case R.id.btnSelBowler:
                displayBowlerSelect();

                break;

            case R.id.btnSelFacingBatsman:
                displayBatsmanSelect(null, new BatsmanStats[]{ccUtils.getCurrentFacing() , ccUtils.getOtherBatsman()},
                        ACTIVITY_REQ_CODE_CURRENT_FACING_DIALOG);
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

		dialogIntent.putExtra(WicketDialogActivity.ARG_FACING_BATSMAN, ccUtils.getCurrentFacing());
		dialogIntent.putExtra(WicketDialogActivity.ARG_OTHER_BATSMAN, ccUtils.getOtherBatsman());
		dialogIntent.putExtra(WicketDialogActivity.ARG_BOWLER, ccUtils.getBowler());
		dialogIntent.putExtra(WicketDialogActivity.ARG_FIELDING_TEAM, ccUtils.getBowlingTeam().toArray());

		startActivityForResult(dialogIntent, ACTIVITY_REQ_CODE_WICKET_DIALOG);
	}

    private void displayBatsmanSelect(@Nullable List<Player> battingTeam, BatsmanStats[] batsmen, int reqCode) {
        Intent batsmanIntent = new Intent(getContext(), BatsmanSelectActivity.class);

        if(battingTeam != null)
            batsmanIntent.putExtra(BatsmanSelectActivity.ARG_PLAYER_LIST, battingTeam.toArray());

        batsmanIntent.putExtra(BatsmanSelectActivity.ARG_BATSMAN_LIST, batsmen);

        startActivityForResult(batsmanIntent, reqCode);
    }

    private void displayBowlerSelect() {
        HashMap<String, BowlerStats> bowlerMap = ccUtils.getCard().getBowlerMap();
        BowlerStats[] currBowlers = new BowlerStats[bowlerMap.size()];
        Iterator<String> bowlerItr = bowlerMap.keySet().iterator();
        int i=0;
        while(bowlerItr.hasNext()) {
            currBowlers[i++] = bowlerMap.get(bowlerItr.next());
        }

        Intent bowlerIntent = new Intent(getContext(), BowlerSelectActivity.class);

        bowlerIntent.putExtra(BowlerSelectActivity.ARG_PLAYER_LIST, ccUtils.getBowlingTeam().toArray());
        bowlerIntent.putExtra(BowlerSelectActivity.ARG_BOWLER_LIST, currBowlers);
        bowlerIntent.putExtra(BowlerSelectActivity.ARG_MAX_OVERS_PER_BOWLER, ccUtils.getCard().getMaxPerBowler());
        bowlerIntent.putExtra(BowlerSelectActivity.ARG_PREV_BOWLER, ccUtils.getPrevBowler());

        startActivityForResult(bowlerIntent, ACTIVITY_REQ_CODE_BOWLER_DIALOG);
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
				    WicketData wktData = (WicketData) data.getSerializableExtra(WicketDialogActivity.ARG_WICKET_DATA);
                    outBatsman = wktData.getBatsman();

					newBallBowled(0, wktData);

                    updateScreenForBatsmanSelect(View.GONE, View.VISIBLE, View.GONE);
                    dismissalType = wktData.getDismissalType();
                    updateCardDetails(false);
				}
				break;

            case ACTIVITY_REQ_CODE_BATSMAN_DIALOG:
                if(resultCode == BatsmanSelectActivity.RESP_CODE_OK) {
                    newBatsman = (BatsmanStats) data.getSerializableExtra(BatsmanSelectActivity.ARG_SEL_BATSMAN);
                    if(newBatsman != null) {
                        switch (dismissalType) {
                            case RUN_OUT:
                            case RETIRED:
                            case OBSTRUCTING_FIELD:
                                updateScreenForBatsmanSelect(View.GONE, View.GONE, View.VISIBLE);
                                break;

                            default:
                                updateScreenForBatsmanSelect(View.VISIBLE, View.GONE, View.GONE);
                                break;
                        }
                        dismissalType = null;

                        ccUtils.newBatsman(newBatsman);
                        updateCardDetails(false);
                    }
                }
                break;

            case ACTIVITY_REQ_CODE_CURRENT_FACING_DIALOG:
                if(resultCode == BatsmanSelectActivity.RESP_CODE_OK) {
                    BatsmanStats facingBatsman = (BatsmanStats) data.getSerializableExtra(BatsmanSelectActivity.ARG_SEL_BATSMAN);
                    if(facingBatsman != null && ccUtils.getCurrentFacing().getPosition() != facingBatsman.getPosition()) {
                        ccUtils.updateFacingBatsman(facingBatsman);
                        updateCardDetails(false);
                    }

                    updateScreenForBatsmanSelect(View.VISIBLE, View.GONE, View.GONE);
                }
                break;

            case ACTIVITY_REQ_CODE_BOWLER_DIALOG:
                if(resultCode == BowlerSelectActivity.RESP_CODE_OK) {
                    BowlerStats nextBowler = (BowlerStats) data.getSerializableExtra(BowlerSelectActivity.ARG_SEL_BOWLER);
                    if(nextBowler != null) {
                        ccUtils.setBowler(nextBowler);
                        updateCardDetails(false);
                    }

                    updateScreenForBowlerSelect(View.VISIBLE, View.GONE);
                }
                break;
		}
	}

	private void updateScreenForBatsmanSelect(int scoringButtonsVisibility, int batsmanSelectionVisibility, int currentFacingSelectVisibility) {
        LinearLayout llScoring = theView.findViewById(R.id.llScoring);
        Button btnSelBatsman = theView.findViewById(R.id.btnSelBatsman);
        Button btnSelFacing = theView.findViewById(R.id.btnSelFacingBatsman);

        llScoring.setVisibility(scoringButtonsVisibility);
        btnSelBatsman.setVisibility(batsmanSelectionVisibility);
        btnSelFacing.setVisibility(currentFacingSelectVisibility);
    }

	private void updateScreenForBowlerSelect(int scoringButtonsVisibility, int bowlerSelectVisibility) {
        LinearLayout llScoring = theView.findViewById(R.id.llScoring);
        Button btnSelBowler = theView.findViewById(R.id.btnSelBowler);

        llScoring.setVisibility(scoringButtonsVisibility);
        btnSelBowler.setVisibility(bowlerSelectVisibility);
    }
}