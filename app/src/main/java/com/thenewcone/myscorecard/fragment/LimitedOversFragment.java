package com.thenewcone.myscorecard.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.thenewcone.myscorecard.match.Team;
import com.thenewcone.myscorecard.player.BatsmanStats;
import com.thenewcone.myscorecard.player.BowlerStats;
import com.thenewcone.myscorecard.player.Player;
import com.thenewcone.myscorecard.scorecard.Extra;
import com.thenewcone.myscorecard.scorecard.WicketData;
import com.thenewcone.myscorecard.utils.CommonUtils;
import com.thenewcone.myscorecard.utils.database.DatabaseHandler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class LimitedOversFragment extends Fragment
	implements View.OnClickListener{
	View theView;
	WicketData.DismissalType dismissalType;
    DatabaseHandler dbHandler;

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
	}

	public static LimitedOversFragment newInstance
			(Team battingTeam, Team bowlingTeam, Team tossWonBy,
			 int maxOvers, int maxWickets, int maxPerBowler, int maxPlayers) {
		LimitedOversFragment fragment = new LimitedOversFragment();

		fragment.initCricketCard(battingTeam, bowlingTeam, tossWonBy, maxOvers, maxWickets, maxPerBowler);

		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        dbHandler = new DatabaseHandler(getContext());
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		theView = inflater.inflate(R.layout.fragment_limited_overs, container, false);

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

	private void initCricketCard(Team battingTeam, Team bowlingTeam, Team tossWonBy, int maxOvers, int maxWickets, int maxPerBowler) {
		CricketCard card =
				new CricketCard(battingTeam.getShortName(),
						String.valueOf(maxOvers),
						maxPerBowler,
						maxWickets,
						1);

		ccUtils = new CricketCardUtils(card, battingTeam.getShortName(), bowlingTeam.getShortName());

		ccUtils.setFirstInnings();
	}

	private void updateCardDetails(boolean isInitial) {
		CricketCard currCard = ccUtils.getCard();

		/* Main Score Details*/
		if(isInitial) {
			TextView tvBattingTeam = theView.findViewById(R.id.tvBattingTeam);
			tvBattingTeam.setText(currCard.getBattingTeamName());

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

	private void newBallBowled(Extra extra, int runs, @Nullable WicketData wicketData) {
		ccUtils.processBallActivity(extra, runs, wicketData, false);
		updateCardDetails(false);
        checkChangeOfBowler();
        if(checkInningsComplete()) {
            closeInnings();
        }
	}

	@Override
	public void onClick(View view) {
		newBallBowled(view);
	}

    public void processExtra(Extra.ExtraType extraType, int numExtraRuns, String penaltyFavouringTeam, Extra.ExtraType extraSubType) {
        Extra extra;
        switch (extraType) {
            case PENALTY:
                if(numExtraRuns > 0) {
                    extra = new Extra(Extra.ExtraType.PENALTY, numExtraRuns);
                    ccUtils.addPenalty(extra, penaltyFavouringTeam);
                }
                break;

            case LEG_BYE:
                if(numExtraRuns > 0) {
                    extra = new Extra(Extra.ExtraType.LEG_BYE, numExtraRuns);
                    newBallBowled(extra, numExtraRuns, null);
                    ccUtils.checkNextBatsmanFacingBall(extra.getRuns());
                }
                break;

            case BYE:
                if(numExtraRuns > 0) {
                    extra = new Extra(Extra.ExtraType.BYE, numExtraRuns);
                    newBallBowled(extra, numExtraRuns, null);
                    ccUtils.checkNextBatsmanFacingBall(extra.getRuns());
                }
                break;

            case WIDE:
                if(numExtraRuns >= 0) {
                    extra = new Extra(Extra.ExtraType.WIDE, numExtraRuns);
                    newBallBowled(extra, numExtraRuns, null);
                }
                break;

            case NO_BALL:
                if(numExtraRuns >= 0) {
                    extra = new Extra(Extra.ExtraType.NO_BALL, 0, extraSubType);
                    newBallBowled(extra, numExtraRuns, null);
                }
                break;
        }
    }

	private void newBallBowled(View view) {
		switch (view.getId()) {
			case R.id.btnRuns0:
				newBallBowled(null, 0, null);
				break;

			case R.id.btnRuns1:
				newBallBowled(null, 1, null);
				break;

			case R.id.btnRuns2:
				newBallBowled(null, 2, null);
				break;

			case R.id.btnRuns3:
				newBallBowled(null, 3, null);
				break;

			case R.id.btnRuns4:
				newBallBowled(null, 4, null);
				break;

			case R.id.btnRuns6:
				newBallBowled(null, 6, null);
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

                displayBatsmanSelect(ccUtils.getCard().getBattingTeam(), batsmenPlayed, ACTIVITY_REQ_CODE_BATSMAN_DIALOG, batsmen.size());
                break;

            case R.id.btnSelBowler:
                displayBowlerSelect();

                break;

            case R.id.btnSelFacingBatsman:
                displayBatsmanSelect(null, new BatsmanStats[]{ccUtils.getCurrentFacing() , ccUtils.getOtherBatsman()},
                        ACTIVITY_REQ_CODE_CURRENT_FACING_DIALOG, 0);
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
		dialogIntent.putExtra(WicketDialogActivity.ARG_FIELDING_TEAM, ccUtils.getCard().getBowlingTeam().toArray());

		startActivityForResult(dialogIntent, ACTIVITY_REQ_CODE_WICKET_DIALOG);
	}

    private void displayBatsmanSelect(@Nullable List<Player> battingTeam, BatsmanStats[] batsmen, int reqCode, int defaultSelIndex) {
        Intent batsmanIntent = new Intent(getContext(), BatsmanSelectActivity.class);

        if(battingTeam != null) {
            batsmanIntent.putExtra(BatsmanSelectActivity.ARG_PLAYER_LIST, battingTeam.toArray());
            batsmanIntent.putExtra(BatsmanSelectActivity.ARG_DEFAULT_SEL_INDEX, defaultSelIndex);
        }
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

        bowlerIntent.putExtra(BowlerSelectActivity.ARG_PLAYER_LIST, ccUtils.getCard().getBowlingTeam().toArray());
        bowlerIntent.putExtra(BowlerSelectActivity.ARG_BOWLER_LIST, currBowlers);
        bowlerIntent.putExtra(BowlerSelectActivity.ARG_MAX_OVERS_PER_BOWLER, ccUtils.getCard().getMaxPerBowler());
        bowlerIntent.putExtra(BowlerSelectActivity.ARG_NEXT_BOWLER, ccUtils.getNextBowler());
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

					processExtra(extraType, extraRuns, team, extraSubType);
					updateCardDetails(false);
				}
				break;

			case ACTIVITY_REQ_CODE_WICKET_DIALOG:
				if(resultCode == WicketDialogActivity.RESP_CODE_OK) {
				    WicketData wktData = (WicketData) data.getSerializableExtra(WicketDialogActivity.ARG_WICKET_DATA);
				    Extra extraData = (Extra) data.getSerializableExtra(WicketDialogActivity.ARG_EXTRA_DATA);
                    outBatsman = wktData.getBatsman();

					newBallBowled(extraData, 0, wktData);

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

	private void checkChangeOfBowler() {
	    if(ccUtils.isNewOver()) {
            updateScreenForBowlerSelect(View.GONE, View.VISIBLE);

            String jsonStr = CommonUtils.convertToJSON(ccUtils);

            ccUtils = null;
            Log.i("JSON", String.format("Is CCUtils NULL ?  %b", (ccUtils == null)));
            ccUtils = CommonUtils.convertToCCUtils(jsonStr);
            Log.i("JSON", String.format("Is CCUtils NULL ?  %b", (ccUtils == null)));
        }
    }

    private boolean checkInningsComplete() {
	    return ccUtils.getCard().isInningsComplete();
    }

    @Override
    public void onDetach() {
        //TODO: Implement logic to ensure that the state is saved before destroying the view.
        super.onDetach();
    }

    @Override
        public void onDestroyView() {
	    //TODO: Implement logic to ensure that the state is saved before destroying the view.
        super.onDestroyView();
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

    private void closeInnings() {
        theView.findViewById(R.id.llScoring).setVisibility(View.GONE);
        theView.findViewById(R.id.btnStartNextInnings).setVisibility(View.VISIBLE);

        if(ccUtils.getCard().getInnings() == 1)
            ccUtils.setNewInnings();
        else
            showResult();
    }

    private void showResult() {

    }

    private void saveMatch(String saveName){
        dbHandler.saveMatchState(ccUtils.getMatchStateID(), CommonUtils.convertToJSON(ccUtils), saveName);
    }
}