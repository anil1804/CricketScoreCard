package com.theNewCone.cricketScoreCard.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.theNewCone.cricketScoreCard.Constants;
import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.activity.BatsmanSelectActivity;
import com.theNewCone.cricketScoreCard.activity.BowlerSelectActivity;
import com.theNewCone.cricketScoreCard.activity.ExtrasActivity;
import com.theNewCone.cricketScoreCard.activity.GraphsActivity;
import com.theNewCone.cricketScoreCard.activity.InputActivity;
import com.theNewCone.cricketScoreCard.activity.MatchStateSelectActivity;
import com.theNewCone.cricketScoreCard.activity.ScoreCardActivity;
import com.theNewCone.cricketScoreCard.activity.WicketActivity;
import com.theNewCone.cricketScoreCard.async.LoadCCUtils;
import com.theNewCone.cricketScoreCard.async.StoreCCUtils;
import com.theNewCone.cricketScoreCard.enumeration.DismissalType;
import com.theNewCone.cricketScoreCard.enumeration.ExtraType;
import com.theNewCone.cricketScoreCard.enumeration.MatchResult;
import com.theNewCone.cricketScoreCard.intf.ConfirmationDialogClickListener;
import com.theNewCone.cricketScoreCard.intf.DialogItemClickListener;
import com.theNewCone.cricketScoreCard.intf.DrawerController;
import com.theNewCone.cricketScoreCard.match.BallInfo;
import com.theNewCone.cricketScoreCard.match.CricketCard;
import com.theNewCone.cricketScoreCard.match.CricketCardUtils;
import com.theNewCone.cricketScoreCard.match.MatchState;
import com.theNewCone.cricketScoreCard.match.Team;
import com.theNewCone.cricketScoreCard.player.BatsmanStats;
import com.theNewCone.cricketScoreCard.player.BowlerStats;
import com.theNewCone.cricketScoreCard.player.Player;
import com.theNewCone.cricketScoreCard.scorecard.Extra;
import com.theNewCone.cricketScoreCard.scorecard.WicketData;
import com.theNewCone.cricketScoreCard.tournament.MatchInfo;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;
import com.theNewCone.cricketScoreCard.utils.TournamentUtils;
import com.theNewCone.cricketScoreCard.utils.database.DatabaseHandler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class LimitedOversFragment extends Fragment
	implements View.OnClickListener, ConfirmationDialogClickListener, DialogItemClickListener {

	View theView;
	DismissalType dismissalType;
    DatabaseHandler dbHandler;

    private static final int REQ_CODE_EXTRA_DIALOG = 1;
	private static final int REQ_CODE_WICKET_DIALOG = 2;
    private static final int REQ_CODE_BATSMAN_DIALOG = 3;
    private static final int REQ_CODE_BOWLER_DIALOG = 4;
    private static final int REQ_CODE_CURRENT_FACING_DIALOG = 5;
    private static final int REQ_CODE_GET_SAVE_MATCH_NAME = 6;
    private static final int REQ_CODE_GET_MATCHES_TO_LOAD = 7;
    private static final int REQ_CODE_SEL_HURT_BATSMAN = 8;
    private static final int REQ_CODE_GET_OTHER_RUNS = 9;

    private static final int CONFIRMATION_CODE_SAVE_MATCH = 1;
	private static final int CONFIRMATION_CODE_EXIT_MATCH = 2;
	private static final int CONFIRMATION_CODE_CLOSE_MATCH = 3;

	private static final String DIALOG_CODE_GET_MOM_TEAM1 = "1";
	private static final String DIALOG_CODE_GET_MOM_TEAM2 = "2";

	private static final String BUNDLE_CC_UTILS = "Bundle_CricketCardUtils";
	private static final String BUNDLE_NEW_BATSMAN = "Bundle_NewBatsman";
	private static final String BUNDLE_OUT_BATSMAN = "Bundle_OutBatsman";
	private static final String BUNDLE_HURT_BOWLER = "Bundle_HurtBowler";
	private static final String BUNDLE_MATCH_STATE_ID = "Bundle_MatchStateID";
	private static final String BUNDLE_MATCH_ID = "Bundle_MatchID";
	private static final String BUNDLE_CURRENT_UNDO_COUNT = "Bundle_UndoCount";
	private static final String BUNDLE_IS_START_INNINGS = "Bundle_IsStartInnings";
	private static final String BUNDLE_IS_LOAD = "Bundle_IsLoad";
	private static final String BUNDLE_IS_UNDO = "Bundle_IsUndo";
	private static final String BUNDLE_SAVE_MATCH_NAME = "Bundle_SaveMatchName";
	private static final String BUNDLE_POM_PLAYERS = "Bundle_PoMPlayers";
	private static final String BUNDLE_BOWLER_CHANGED = "Bundle_BowlerChanged";
	private static final String BUNDLE_NEW_BATSMAN_ARRIVED = "Bundle_NewBatsmanArrived";
	private static final String BUNDLE_ENABLE_OPTION_BATSMAN_HURT = "Bundle_EnableBatsmanHurt";
	private static final String BUNDLE_ENABLE_OPTION_BATSMAN_FACING = "Bundle_EnableBatsmanFacing";
	private static final String BUNDLE_ENABLE_OPTION_BOWLER_HURT = "Bundle_EnableBolwerHurt";

    TableRow trBatsman1, trBatsman2, trBowler;
    TextView tvCurrScore, tvOvers, tvCRR, tvRRR, tvLast12Balls;
    TextView tvBat1Name, tvBat1Runs, tvBat1Balls, tvBat14s, tvBat16s, tvBat1SR;
    TextView tvBat2Name, tvBat2Runs, tvBat2Balls, tvBat24s, tvBat26s, tvBat2SR;
    TextView tvLegByes, tvByes, tvWides, tvNoBalls, tvPenalty;
    TextView tvBowlName, tvBowlOvers, tvBowlMaidens, tvBowlRuns, tvBowlWickets, tvBowlEconomy;
    TextView tvResult, tvRunsInBalls, tvInningsComplete, tvRunsInOver, tvPoM;

    RadioButton rbMoMTeam1, rbMoMTeam2;

    Button btnStartNextInnings;

	CricketCardUtils ccUtils;
	BatsmanStats newBatsman, outBatsman;
	BowlerStats hurtBowler;

	MatchInfo matchInfo;

	private int matchStateID = -1;
	private int matchID, currentUndoCount;
	private boolean startInnings = true, isLoad = false, isUndo = false;
	private String saveMatchName;
	String[] poMPlayers;

	boolean bowlerChanged = false, newBatsmanArrived = false;
	boolean enableBatsmanHurtOption = true, enableChangeFacingOption = true, enableBowlerHurtOption = true;
	boolean isTournament = false;

	StoreCCUtils storeCCUtils;
	LoadCCUtils loadCCUtils;

	public LimitedOversFragment() {
	}

	public static LimitedOversFragment loadInstance(int matchStateID, MatchInfo matchInfo) {
		LimitedOversFragment fragment = new LimitedOversFragment();
		fragment.matchStateID = matchStateID;
		fragment.isLoad = true;

		if (matchInfo != null) {
			fragment.matchInfo = matchInfo;
			fragment.isTournament = true;
		}

		return fragment;
	}

	public static LimitedOversFragment newInstance(int matchID, String matchName, Team battingTeam, Team bowlingTeam,
												   Team tossWonBy, int maxOvers, int maxWickets, int maxPerBowler,
												   MatchInfo matchInfo) {

		LimitedOversFragment fragment = new LimitedOversFragment();

		if (matchInfo != null) {
			fragment.matchInfo = matchInfo;
			fragment.isTournament = true;
		}
		fragment.initCricketCard(matchID, matchName, battingTeam, bowlingTeam, tossWonBy, maxOvers, maxWickets, maxPerBowler);

		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		dbHandler = new DatabaseHandler(getContext());

		if (isLoad) {
			if (matchStateID > 0) {
				loadMatch(matchStateID);
			} else {
				Toast.makeText(getContext(), "Unable to Load match.", Toast.LENGTH_LONG).show();
				if (getActivity() != null)
					getActivity().onBackPressed();
			}
		} else {
			if (isTournament) {
				startTournamentMatch();
				saveMatchName = ccUtils.getMatchName();
				saveMatch(saveMatchName);
			}
		}
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		theView = inflater.inflate(R.layout.fragment_limited_overs, container, false);

		//Back pressed Logic for fragment
		theView.setFocusableInTouchMode(true);
		theView.requestFocus();
		theView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						confirmExitMatch();
						return true;
					}
				}
				return false;
			}
		});

		if(savedInstanceState != null) {
			loadFromBundle(savedInstanceState);
		}

		isLoad = false;
		initialSetup();
		if (ccUtils != null) {
			dismissalType = determineDismissalType();
			updateLayout(getSelectFacingBatsman(), true);
		}

		if(getActivity() != null) {
			DrawerController drawerController = (DrawerController) getActivity();
			drawerController.setDrawerEnabled(true);
			drawerController.disableAllDrawerMenuItems();
			drawerController.enableDrawerMenuItem(R.id.nav_help);
			getActivity().setTitle(getString(R.string.title_fragment_limited_overs));
		}

		rbMoMTeam1.setText(ccUtils.getTeam1().getShortName());
		rbMoMTeam2.setText(ccUtils.getTeam2().getShortName());

		return theView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(storeCCUtils != null)
			storeCCUtils.cancel(true);

		if(loadCCUtils != null)
			loadCCUtils.cancel(true);
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		saveBundle(outState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_fragments_match_limited_overs, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);

		MenuItem batsmanHurtItem = menu.findItem(R.id.menu_batsman_hurt);
		MenuItem bowlerHurtItem = menu.findItem(R.id.menu_bowler_hurt);
		MenuItem changeFacingItem = menu.findItem(R.id.menu_change_facing);
		MenuItem abandonItem = menu.findItem(R.id.menu_abandon);

		batsmanHurtItem.setEnabled(enableBatsmanHurtOption);
		bowlerHurtItem.setEnabled(enableBowlerHurtOption);
		changeFacingItem.setEnabled(enableChangeFacingOption);

		abandonItem.setVisible(isTournament);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_save:
				if(!ccUtils.getCard().isInningsComplete()) {
					if(ccUtils.getCurrentFacing() == null || ccUtils.getOtherBatsman() == null) {
						Toast.makeText(getContext(), "Please Select Batsman before saving the match", Toast.LENGTH_SHORT).show();
						break;
					}
					if(ccUtils.getBowler() == null || ccUtils.isNewOver()) {
						Toast.makeText(getContext(), "Please Select Bowler before saving the match", Toast.LENGTH_SHORT).show();
						break;
					}
				}
				showInputActivity(saveMatchName, REQ_CODE_GET_SAVE_MATCH_NAME);
				break;

			case R.id.menu_undo:
				if(currentUndoCount >= DatabaseHandler.maxUndoAllowed) {
					Toast.makeText(getContext(), "Maximum UNDO limit reached.", Toast.LENGTH_SHORT).show();
				} else if (startInnings) {
					Toast.makeText(getContext(), "Innings just started. Nothing to UNDO", Toast.LENGTH_SHORT).show();
				} else {
					isUndo = true;
					currentUndoCount++;
					int matchStateID = dbHandler.getLastAutoSave(matchID);
					if(matchStateID > 0) {
						loadMatch(matchStateID);
						updateLayout(false, true);
						dbHandler.deleteMatchState(matchStateID);
					} else {
						Toast.makeText(getContext(), "Nothing to Undo.", Toast.LENGTH_SHORT).show();
					}
				}
				break;

			case R.id.menu_load:
				showSavedMatchDialog();
				break;

			case R.id.menu_scoreCard:
				showScoreCard();
				break;

			case R.id.menu_change_facing:
				displayBatsmanSelect(null, new BatsmanStats[]{ccUtils.getCurrentFacing() , ccUtils.getOtherBatsman()},
						REQ_CODE_CURRENT_FACING_DIALOG, 0);
				break;

			case R.id.menu_batsman_hurt:
				displayBatsmanSelect(null, new BatsmanStats[]{ccUtils.getCurrentFacing() , ccUtils.getOtherBatsman()},
						REQ_CODE_SEL_HURT_BATSMAN, 0);
				break;

			case R.id.menu_bowler_hurt:
				hurtBowler = ccUtils.getBowler();
				bowlerChanged = true;
				ccUtils.setBowler(null, false);
				updateLayout(false, false);
				break;

			case R.id.menu_show_graph:
				Intent graphIntent = new Intent(getContext(), GraphsActivity.class);
				graphIntent.putExtra(GraphsActivity.ARG_CRICKET_CARD_UTILS, CommonUtils.convertToJSON(ccUtils));
				startActivity(graphIntent);
				break;

			case R.id.menu_abandon:
				completeMatch(MatchResult.NO_RESULT.toString(), null, false, true);
				break;
		}

		return true;
	}

	@Override
	public void onClick(View view) {
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

			case R.id.btnMoreRuns:
				showInputActivity(String.valueOf(5), REQ_CODE_GET_OTHER_RUNS);
				break;

			case R.id.btnRuns6:
				newBallBowled(null, 6, null);
				break;

			case R.id.btnWicket:
				displayWicketDialog();
				break;

			case R.id.btnExtraPenalty:
				displayExtrasDialog(ExtraType.PENALTY);
				break;

			case R.id.btnExtrasLegByes:
				displayExtrasDialog(ExtraType.LEG_BYE);
				break;

			case R.id.btnExtrasByes:
				displayExtrasDialog(ExtraType.BYE);
				break;

			case R.id.btnExtrasWides:
				displayExtrasDialog(ExtraType.WIDE);
				break;

			case R.id.btnExtrasNoBall:
				displayExtrasDialog(ExtraType.NO_BALL);
				break;

			case R.id.btnSelBatsman:
				selectBatsman();
				break;

			case R.id.btnSelBowler:
				displayBowlerSelect();
				break;

			case R.id.btnSelFacingBatsman:
				displayBatsmanSelect(null, new BatsmanStats[]{ccUtils.getCurrentFacing() , ccUtils.getOtherBatsman()},
						REQ_CODE_CURRENT_FACING_DIALOG, 0);
				break;

			case R.id.btnStartNextInnings:
				startNewInnings();
				break;

			case R.id.rbPoMTeam1:
				displayPlayerDialog(ccUtils.getTeam1(), DIALOG_CODE_GET_MOM_TEAM1);
				break;

			case R.id.rbPoMTeam2:
				displayPlayerDialog(ccUtils.getTeam2(), DIALOG_CODE_GET_MOM_TEAM2);
				break;

			case R.id.btnClose:
				confirmCloseMatch();
				break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case REQ_CODE_EXTRA_DIALOG:
				if (resultCode == ExtrasActivity.RESULT_CODE_OK) {
					ExtraType extraType = (ExtraType) data.getSerializableExtra(Constants.ARG_EXTRA_TYPE);
					ExtraType extraSubType = (ExtraType) data.getSerializableExtra(ExtrasActivity.ARG_NB_EXTRA);
					int extraRuns = data.getIntExtra(ExtrasActivity.ARG_EXTRA_RUNS, -1);
					String team = data.getStringExtra(ExtrasActivity.ARG_TEAM);

					processExtra(extraType, extraRuns, team, extraSubType);
				}
				break;

			case REQ_CODE_WICKET_DIALOG:
				if(resultCode == WicketActivity.RESP_CODE_OK) {
					WicketData wktData = (WicketData) data.getSerializableExtra(WicketActivity.ARG_WICKET_DATA);
					Extra extraData = (Extra) data.getSerializableExtra(WicketActivity.ARG_EXTRA_DATA);
					outBatsman = (ccUtils.getCurrentFacing().getPlayer().getID() == wktData.getBatsman().getPlayer().getID())
							? ccUtils.getCurrentFacing() : ccUtils.getOtherBatsman();
					int batsmanRuns = data.getIntExtra(WicketActivity.ARG_BATSMAN_RUNS, 0);

					newBallBowled(extraData, batsmanRuns, wktData);

					dismissalType = wktData.getDismissalType();
					updateLayout(false, false);
					updateCardDetails();
				}
				break;

			case REQ_CODE_BATSMAN_DIALOG:
				if(resultCode == BatsmanSelectActivity.RESP_CODE_OK) {
					newBatsman = (BatsmanStats) data.getSerializableExtra(BatsmanSelectActivity.ARG_SEL_BATSMAN);
					newBatsmanArrived = true;

					if(newBatsman != null) {
						ccUtils.newBatsman(newBatsman);

						if(dismissalType != null) {
							updateLayout(getSelectFacingBatsman(), false);
							dismissalType = null;
						} else {
							if(startInnings)
								updateLayout(true, true);
							else
								updateLayout(false, false);
						}
					}
				}
				break;

			case REQ_CODE_CURRENT_FACING_DIALOG:
				if(resultCode == BatsmanSelectActivity.RESP_CODE_OK) {
					BatsmanStats selBatsman = (BatsmanStats) data.getSerializableExtra(BatsmanSelectActivity.ARG_SEL_BATSMAN);
					if(selBatsman != null && ccUtils.getCurrentFacing().getPosition() != selBatsman.getPosition()) {
						ccUtils.updateFacingBatsman(selBatsman);
						updateCardDetails();
					}

					updateLayout(false, false);
				}
				break;

			case REQ_CODE_BOWLER_DIALOG:
				if(resultCode == BowlerSelectActivity.RESP_CODE_OK) {
					BowlerStats nextBowler = (BowlerStats) data.getSerializableExtra(BowlerSelectActivity.ARG_SEL_BOWLER);
					if(nextBowler != null) {
						ccUtils.setBowler(nextBowler, false);
						updateCardDetails();
					}

					updateLayout(false, false);

					startInnings = false;
				}
				break;

			case REQ_CODE_GET_SAVE_MATCH_NAME:
				if(resultCode == InputActivity.RESP_CODE_OK) {
					saveMatchName = data.getStringExtra(InputActivity.ARG_INPUT_TEXT);
					int rowID = saveMatch(saveMatchName);
					if(rowID > 0) {
						Toast.makeText(getContext(), "Match saved successfully.", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getContext(), "Problem encountered saving the match", Toast.LENGTH_SHORT).show();
					}
				}
				break;

			case REQ_CODE_GET_MATCHES_TO_LOAD:
				if(resultCode == MatchStateSelectActivity.RESP_CODE_OK) {
					MatchState selSavedMatch = (MatchState) data.getSerializableExtra(MatchStateSelectActivity.ARG_RESP_SEL_MATCH);

					isLoad = true;
					loadMatch(selSavedMatch.getId());
					updateLayout(false, true);
					dbHandler.clearMatchStateHistory(0, -1, matchStateID);
					break;
				}
				break;

			case REQ_CODE_SEL_HURT_BATSMAN:
				if(resultCode == BatsmanSelectActivity.RESP_CODE_OK) {
					BatsmanStats selBatsman = (BatsmanStats) data.getSerializableExtra(BatsmanSelectActivity.ARG_SEL_BATSMAN);
					if(selBatsman != null) {
						dismissalType = DismissalType.RETIRED_HURT;
						ccUtils.updateBatsmanHurt(selBatsman);
						outBatsman = selBatsman;
						updateLayout(false, false);
					}
				}
				break;

			case REQ_CODE_GET_OTHER_RUNS:
				if(resultCode == InputActivity.RESP_CODE_OK) {
					String numRuns = data.getStringExtra(InputActivity.ARG_INPUT_TEXT);
					try {
						int runs = Integer.parseInt(numRuns);
						if(runs > Constants.MAX_RUNS_POSSIBLE) {
							Toast.makeText(getContext(),
									String.format(Locale.getDefault(), "Number of Runs limited to %d", Constants.MAX_RUNS_POSSIBLE),
									Toast.LENGTH_SHORT).show();
						} else if (runs < 0) {
							Toast.makeText(getContext(),"Number of runs cannot be negative", Toast.LENGTH_SHORT).show();
						} else {
							newBallBowled(null, runs, null);
						}
					} catch (NumberFormatException nfEx) {
						Toast.makeText(getContext(), "Invalid Number of runs", Toast.LENGTH_SHORT).show();
					}
				}
				break;
		}
	}

	@Override
	public void onConfirmationClick(int confirmationCode, boolean accepted) {
		switch (confirmationCode) {
			case CONFIRMATION_CODE_SAVE_MATCH:
				if(accepted) {
					String inputText = (ccUtils.getCard().getInnings() == 2 && ccUtils.getCard().isInningsComplete())
							? "Match End" : null;
					showInputActivity(inputText, REQ_CODE_GET_SAVE_MATCH_NAME);
				}				break;

			case CONFIRMATION_CODE_EXIT_MATCH:
				if(accepted && getActivity() != null) {
					dbHandler.clearMatchStateHistory(0, matchID, -1);
					getActivity().onBackPressed();
				}
				break;

			case CONFIRMATION_CODE_CLOSE_MATCH:
				if(accepted) {
					dbHandler.completeMatch(matchID, CommonUtils.convertToJSON(ccUtils));
					dbHandler.clearAllMatchHistory(matchID);

					if(getActivity() != null && getFragmentManager() != null && getFragmentManager().getBackStackEntryCount() > 0) {
						int backStackCount = getFragmentManager().getBackStackEntryCount();
						for(int i=backStackCount-1; i>=0; i--) {
							FragmentManager.BackStackEntry entry = getFragmentManager().getBackStackEntryAt(i);
							if(HomeFragment.class.getSimpleName().equals(entry.getName())) {
								getActivity().onBackPressed();
							} else {
								getFragmentManager().popBackStack();
							}
						}
					}
				}
				break;
		}
	}

	@Override
	public void onItemSelect(String type, String value, int position) {
		switch (type) {
			case DIALOG_CODE_GET_MOM_TEAM1:
				if(position >= 0) {
					showPlayerOfMatch(ccUtils.getTeam1().getMatchPlayers().get(position));
				}
				break;

			case DIALOG_CODE_GET_MOM_TEAM2:
				if(position >= 0) {
					showPlayerOfMatch(ccUtils.getTeam2().getMatchPlayers().get(position));
				}
				break;
		}
	}

	private void initialSetup() {
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
		theView.findViewById(R.id.btnStartNextInnings).setOnClickListener(this);
		theView.findViewById(R.id.btnMoreRuns).setOnClickListener(this);
		theView.findViewById(R.id.btnClose).setOnClickListener(this);

		tvPoM = theView.findViewById(R.id.tvPoM);

		rbMoMTeam1 = theView.findViewById(R.id.rbPoMTeam1);
		rbMoMTeam2 = theView.findViewById(R.id.rbPoMTeam2);

		rbMoMTeam1.setOnClickListener(this);
		rbMoMTeam2.setOnClickListener(this);
	}

	private void initCricketCard(int matchID, String matchName, Team battingTeam, Team bowlingTeam, Team tossWonBy,
								 int maxOvers, int maxWickets, int maxPerBowler) {
		this.matchID = matchID;

		CricketCard card =
				new CricketCard(battingTeam, bowlingTeam,
						String.valueOf(maxOvers), maxPerBowler, maxWickets, 1);

		ccUtils = new CricketCardUtils(card, matchName, battingTeam, bowlingTeam, maxWickets);
		ccUtils.setTossWonBy(tossWonBy.getId());

		if (matchInfo != null) {
			ccUtils.setMatchInfo(matchInfo);
		}
	}

	private void loadMatch(int matchStateID) {
		/*if(matchStateID > 0) {
			String matchData = dbHandler.retrieveMatchData(matchStateID);
			matchID = dbHandler.getMatchID(matchStateID);
			if (matchData != null) {
				ccUtils = CommonUtils.convertToCCUtils(matchData);
				if (isLoad) {
					Toast.makeText(getContext(), "Match Loaded", Toast.LENGTH_SHORT).show();
					if(ccUtils.getCard().getWicketsFallen() > 0 || ccUtils.getCard().getScore() > 0
							|| CommonUtils.oversToBalls(Double.parseDouble(ccUtils.getCard().getTotalOversBowled())) > 0) {
						startInnings = false;
					}
				} else if(isUndo){
					Toast.makeText(getContext(), "Undo Successful", Toast.LENGTH_SHORT).show();
					isUndo = false;
				}

				checkMenuOptions();
			}
		}*/

		if(matchStateID > 0) {
			matchID = dbHandler.getMatchID(matchStateID);
			loadCCUtils = new LoadCCUtils();
			try {
				ccUtils = loadCCUtils.execute(dbHandler, matchStateID).get(2, TimeUnit.SECONDS);
				loadCCUtils = null;
			} catch (ExecutionException|InterruptedException|TimeoutException e) {
				ccUtils = null;
			}

			if (ccUtils != null) {
				if (isLoad) {
					Toast.makeText(getContext(), "Match Loaded", Toast.LENGTH_SHORT).show();
					if(ccUtils.getCard().getWicketsFallen() > 0 || ccUtils.getCard().getScore() > 0
							|| CommonUtils.oversToBalls(Double.parseDouble(ccUtils.getCard().getTotalOversBowled())) > 0) {
						startInnings = false;
					}
				} else if(isUndo){
					Toast.makeText(getContext(), "Undo Successful", Toast.LENGTH_SHORT).show();
					isUndo = false;
				}

				checkMenuOptions();
			} else {
				Toast.makeText(getContext(), "Unable to load match.", Toast.LENGTH_LONG).show();
				if(getActivity() != null)
					getActivity().onBackPressed();
			}
		}
	}

	private void loadViews() {
		CricketCard currCard = ccUtils.getCard();

		/* Main Score Details*/
		TextView tvBattingTeam = theView.findViewById(R.id.tvBattingTeam);
		tvBattingTeam.setText(currCard.getBattingTeamName());

		TableRow trTarget = theView.findViewById(R.id.trTarget);
		tvRunsInBalls = theView.findViewById(R.id.tvRunsInBalls);
		if(currCard.getInnings() == 1) {
			trTarget.setVisibility(View.GONE);
			tvRunsInBalls.setVisibility(View.GONE);
		} else {
			trTarget.setVisibility(View.VISIBLE);
			tvRunsInBalls.setVisibility(View.VISIBLE);
		}
		tvRunsInOver = theView.findViewById(R.id.tvRunsInOver);

		tvCurrScore = theView.findViewById(R.id.tvScore);
		tvOvers = theView.findViewById(R.id.tvOvers);
		tvCRR = theView.findViewById(R.id.tvCRR);

		/* Chasing Score Details*/
		TextView tvTarget = theView.findViewById(R.id.tvTarget);
		TextView tvMaxOvers = theView.findViewById(R.id.tvMaxOvers);
		tvRRR = theView.findViewById(R.id.tvRRR);
		if (currCard.getInnings() == 2) {
			tvTarget.setText(String.valueOf(currCard.getTarget()));
			tvMaxOvers.setText(String.format(getString(R.string.tvOversText), currCard.getMaxOvers()));
		} else {
			tvTarget.setText("-");
			tvRRR.setText("-");
			tvMaxOvers.setText("");
		}
		tvLast12Balls = theView.findViewById(R.id.tvLast12Balls);

		/* Batsman-1 Details*/
		trBatsman1 = theView.findViewById(R.id.trBatsman1);
		tvBat1Name = theView.findViewById(R.id.tvBat1Name);
		tvBat1Runs = theView.findViewById(R.id.tvBat1RunsScored);
		tvBat1Balls = theView.findViewById(R.id.tvBat1BallsFaced);
		tvBat14s = theView.findViewById(R.id.tvBat14sHit);
		tvBat16s = theView.findViewById(R.id.tvBat16sHit);
		tvBat1SR = theView.findViewById(R.id.tvBat1SR);

		/* Batsman-2 Details*/
		trBatsman2 = theView.findViewById(R.id.trBatsman2);
		tvBat2Name = theView.findViewById(R.id.tvBat2Name);
		tvBat2Runs = theView.findViewById(R.id.tvBat2RunsScored);
		tvBat2Balls = theView.findViewById(R.id.tvBat2BallsFaced);
		tvBat24s = theView.findViewById(R.id.tvBat24sHit);
		tvBat26s = theView.findViewById(R.id.tvBat26sHit);
		tvBat2SR = theView.findViewById(R.id.tvBat2SR);

		/* Extras Details*/
		tvLegByes = theView.findViewById(R.id.tvLegByes);
		tvByes = theView.findViewById(R.id.tvByes);
		tvWides = theView.findViewById(R.id.tvWides);
		tvNoBalls = theView.findViewById(R.id.tvNoBalls);
		tvPenalty = theView.findViewById(R.id.tvPenalty);

		/* Bowler Details */
		trBowler = theView.findViewById(R.id.trBowler);
		tvBowlName = theView.findViewById(R.id.tvBowlName);
		tvBowlOvers = theView.findViewById(R.id.tvBowlOvers);
		tvBowlMaidens = theView.findViewById(R.id.tvBowlMaidens);
		tvBowlRuns = theView.findViewById(R.id.tvBowlRuns);
		tvBowlWickets = theView.findViewById(R.id.tvBowlWickets);
		tvBowlEconomy = theView.findViewById(R.id.tvBowlEconomy);

		/* Views related to Innings completion */
		tvInningsComplete = theView.findViewById(R.id.tvInningsComplete);
		tvInningsComplete.setVisibility(View.GONE);
		btnStartNextInnings = theView.findViewById(R.id.btnStartNextInnings);
		btnStartNextInnings.setVisibility(View.GONE);
		tvResult = theView.findViewById(R.id.tvResult);
		tvResult.setVisibility(View.GONE);

		theView.findViewById(R.id.llCompleteMatch).setVisibility(View.GONE);
		theView.findViewById(R.id.btnClose).setVisibility(View.GONE);
	}

	private void updateCardDetails() {
		CricketCard currCard = ccUtils.getCard();

		/* Main Score Details*/
		tvCurrScore.setText(String.valueOf(currCard.getScore() + "/" + currCard.getWicketsFallen()));
		tvOvers.setText(String.format(getString(R.string.tvOversText), currCard.getTotalOversBowled()));
		tvCRR.setText(CommonUtils.doubleToString(currCard.getRunRate(), "#.##"));
		tvLast12Balls.setText(CommonUtils.listToString(ccUtils.getLast12Balls(), " "));

		/* Chasing Score Details*/
		if(currCard.getInnings() == 2) {
			tvRRR.setText(CommonUtils.doubleToString(currCard.getReqRate(), "#.##"));
		}
		if(startInnings){
			tvRunsInOver.setVisibility(View.GONE);
		} else if(CommonUtils.oversToBalls(Double.parseDouble(ccUtils.getCard().getTotalOversBowled())) > 0 || ccUtils.getCard().getScore() > 0) {
			tvRunsInOver.setVisibility(View.VISIBLE);
		} else {
			tvRunsInOver.setVisibility(View.GONE);
		}

		if(ccUtils.getCard().getCurrOver() != null)
			tvRunsInOver.setText(String.format(getString(R.string.runsInOver), ccUtils.getCard().getCurrOver().getRunsScored()));

		/* Batsman-1 Details*/
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
		tvLegByes.setText(String.format(getString(R.string.legByes), currCard.getLegByes()));
		tvByes.setText(String.format(getString(R.string.byes), currCard.getByes()));
		tvWides.setText(String.format(getString(R.string.wides), currCard.getWides()));
		tvNoBalls.setText(String.format(getString(R.string.noBalls), currCard.getNoBalls()));
		tvPenalty.setText(currCard.getPenalty() > 0 ? String.format(getString(R.string.penalty), currCard.getPenalty()) : "");

		/* Bowler Details */
        if(ccUtils.getBowler() != null) {
			trBowler.setVisibility(View.VISIBLE);
			tvBowlName.setText(ccUtils.getBowler().getBowlerName());
			tvBowlOvers.setText(ccUtils.getBowler().getOversBowled());
			tvBowlMaidens.setText(String.valueOf(ccUtils.getBowler().getMaidens()));
			tvBowlRuns.setText(String.valueOf(ccUtils.getBowler().getRunsGiven()));
			tvBowlWickets.setText(String.valueOf(ccUtils.getBowler().getWickets()));
			tvBowlEconomy.setText(CommonUtils.doubleToString(ccUtils.getBowler().getEconomy(), "#.##"));
		} else {
        	trBowler.setVisibility(View.GONE);
		}

		if(currCard.getInnings() == 2) {
        	int runsReq = ccUtils.getCard().getTarget() - ccUtils.getCard().getScore();
        	int ballsRem = CommonUtils.oversToBalls(Double.parseDouble(ccUtils.getCard().getMaxOvers()))
							- CommonUtils.oversToBalls(Double.parseDouble(ccUtils.getCard().getTotalOversBowled()));

        	tvRunsInBalls.setText(String.format(getString(R.string.runsInBalls), ccUtils.getCard().getBattingTeam().getShortName(), runsReq, ballsRem));
		}
	}

	private void newBallBowled(Extra extra, int runs, @Nullable WicketData wicketData) {
		startInnings = false;
		if(currentUndoCount > 0)
			currentUndoCount--;

		autoSaveMatch();
		ccUtils.processBallActivity(extra, runs, wicketData, bowlerChanged);
		updateLayout(false, false);
		bowlerChanged = false;
		newBatsmanArrived = false;
		hurtBowler = null;

		checkMenuOptions();
	}

	private void checkMenuOptions() {
		if(ccUtils.getCurrentFacing() == null || ccUtils.getOtherBatsman() == null || ccUtils.getBowler() == null || ccUtils.isNewOver()) {
			enableBatsmanHurtOption = false;
			enableBowlerHurtOption = false;
			enableChangeFacingOption = false;
		} else {
			enableBatsmanHurtOption = true;
			enableBowlerHurtOption = true;
			enableChangeFacingOption = true;
		}
	}

	private void processExtra(ExtraType extraType, int numExtraRuns, String penaltyFavouringTeam, ExtraType extraSubType) {
        Extra extra = null;
        switch (extraType) {
            case PENALTY:
				autoSaveMatch();
                if(numExtraRuns > 0) {
					extra = new Extra(ExtraType.PENALTY, numExtraRuns);
                    ccUtils.addPenalty(extra, penaltyFavouringTeam);
                    updateLayout(false, true);
                    String toastText = String.format(Locale.getDefault(),
							"Additional runs awarded to %s. %s updated", penaltyFavouringTeam,
							(penaltyFavouringTeam.equals(Constants.BATTING_TEAM)) ? "Score" : "Target");
					Toast.makeText(getContext(), toastText, Toast.LENGTH_SHORT).show();
                }
                break;

            case LEG_BYE:
                if(numExtraRuns > 0) {
					extra = new Extra(ExtraType.LEG_BYE, numExtraRuns);
                    newBallBowled(extra, 0, null);
                }
                break;

            case BYE:
                if(numExtraRuns > 0) {
					extra = new Extra(ExtraType.BYE, numExtraRuns);
                    newBallBowled(extra, 0, null);
                }
                break;

            case WIDE:
                if(numExtraRuns >= 0) {
					extra = new Extra(ExtraType.WIDE, numExtraRuns);
                    newBallBowled(extra, 0, null);
                }
                break;

            case NO_BALL:
                if(numExtraRuns >= 0) {
                	int batsmanRuns = 0;
					if (extraSubType == null || extraSubType == ExtraType.NONE) {
						extra = new Extra(ExtraType.NO_BALL, 0, extraSubType);
						batsmanRuns = numExtraRuns;
					} else if (extraSubType == ExtraType.BYE || extraSubType == ExtraType.LEG_BYE) {
						extra = new Extra(ExtraType.NO_BALL, numExtraRuns, extraSubType);
					}
					newBallBowled(extra, batsmanRuns, null);
                }
                break;
        }
    }

	private void selectBatsman() {
		HashMap<Integer, BatsmanStats> batsmen = ccUtils.getCard().getBatsmen();
		BatsmanStats[] batsmenPlayed = new BatsmanStats[batsmen.size()];

		for(int i=0; i<batsmen.size(); i++) {
			batsmenPlayed[i] = batsmen.get(i+1);
		}

		displayBatsmanSelect(ccUtils.getCard().getBattingTeam().getMatchPlayers(), batsmenPlayed, REQ_CODE_BATSMAN_DIALOG, batsmen.size());
	}

	private void displayExtrasDialog(ExtraType type) {
		Intent dialogIntent = new Intent(getContext(), ExtrasActivity.class);
		dialogIntent.putExtra(Constants.ARG_EXTRA_TYPE, type);
		startActivityForResult(dialogIntent, REQ_CODE_EXTRA_DIALOG);
	}

	private void displayWicketDialog() {
		Intent dialogIntent = new Intent(getContext(), WicketActivity.class);

		dialogIntent.putExtra(WicketActivity.ARG_FACING_BATSMAN, ccUtils.getCurrentFacing());
		dialogIntent.putExtra(WicketActivity.ARG_OTHER_BATSMAN, ccUtils.getOtherBatsman());
		dialogIntent.putExtra(WicketActivity.ARG_BOWLER, ccUtils.getBowler());
		dialogIntent.putExtra(WicketActivity.ARG_FIELDING_TEAM, ccUtils.getCard().getBowlingTeam().getMatchPlayers().toArray());
		dialogIntent.putExtra(WicketActivity.ARG_NEW_BATSMAN_ARRIVED, newBatsmanArrived);

		startActivityForResult(dialogIntent, REQ_CODE_WICKET_DIALOG);
	}

    private void displayBatsmanSelect(@Nullable List<Player> battingTeam, BatsmanStats[] batsmen, int reqCode, int defaultSelIndex) {
        Intent batsmanIntent = new Intent(getContext(), BatsmanSelectActivity.class);

        Team currBattingTeam = ccUtils.getCard().getBattingTeam();

        if(battingTeam != null) {
            batsmanIntent.putExtra(BatsmanSelectActivity.ARG_PLAYER_LIST, battingTeam.toArray());
            batsmanIntent.putExtra(BatsmanSelectActivity.ARG_DEFAULT_SEL_INDEX, defaultSelIndex);
            batsmanIntent.putExtra(BatsmanSelectActivity.ARG_CAPT_PLAYER_ID, currBattingTeam.getCaptain().getID());
            batsmanIntent.putExtra(BatsmanSelectActivity.ARG_WK_PLAYER_ID, currBattingTeam.getWicketKeeper().getID());
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

		BowlerStats[] restrictedBowlers = null;
        BowlerStats prevBowler = ccUtils.getPrevBowler();
        if(hurtBowler != null && prevBowler != null) {
        	if(hurtBowler.getPlayer().getID() == prevBowler.getPlayer().getID())
				restrictedBowlers = new BowlerStats[]{prevBowler};
        	else
        		restrictedBowlers = new BowlerStats[]{prevBowler, hurtBowler};
		} else if(hurtBowler != null) {
			restrictedBowlers = new BowlerStats[]{hurtBowler};
		} else if(prevBowler != null) {
			restrictedBowlers = new BowlerStats[]{prevBowler};
		}

        Intent bowlerIntent = new Intent(getContext(), BowlerSelectActivity.class);

        bowlerIntent.putExtra(BowlerSelectActivity.ARG_PLAYER_LIST, ccUtils.getCard().getBowlingTeam().getMatchPlayers().toArray());
        bowlerIntent.putExtra(BowlerSelectActivity.ARG_BOWLER_LIST, currBowlers);
        bowlerIntent.putExtra(BowlerSelectActivity.ARG_MAX_OVERS_PER_BOWLER, ccUtils.getCard().getMaxPerBowler());
        bowlerIntent.putExtra(BowlerSelectActivity.ARG_NEXT_BOWLER, ccUtils.getNextBowler());
        bowlerIntent.putExtra(BowlerSelectActivity.ARG_RESTRICTED_BOWLERS, restrictedBowlers);

        startActivityForResult(bowlerIntent, REQ_CODE_BOWLER_DIALOG);
    }

	private void showInputActivity(String inputText, int reqCode) {
		Intent iaIntent = new Intent(getContext(), InputActivity.class);
		if(inputText != null)
			iaIntent.putExtra(InputActivity.ARG_INPUT_TEXT, inputText);
		startActivityForResult(iaIntent, reqCode);
	}

	private void updateLayout(boolean selectFacing, boolean isInitial) {
		if(isInitial)
			loadViews();

		if(ccUtils.getCard().isInningsComplete()) {
			ccUtils.getCard().completeOver();
			updateViewToCloseInnings();
		} else if(ccUtils.getCurrentFacing() == null || ccUtils.getOtherBatsman() == null) {
			updateScreenForBatsmanSelect(View.GONE, View.VISIBLE, View.GONE);
			updateScreenForBowlerSelect(View.GONE, View.GONE);
		} else if(selectFacing) {
			updateScreenForBatsmanSelect(View.GONE, View.GONE, View.VISIBLE);
			updateScreenForBowlerSelect(View.GONE, View.GONE);
		} else if(ccUtils.getBowler() == null || ccUtils.isNewOver()) {
			updateScreenForBatsmanSelect(View.GONE, View.GONE, View.GONE);
			updateScreenForBowlerSelect(View.GONE, View.VISIBLE);
		} else {
			updateScreenForBatsmanSelect(View.GONE, View.GONE, View.GONE);
			updateScreenForBowlerSelect(View.VISIBLE, View.GONE);
		}
		checkMenuOptions();
		updateCardDetails();
	}

    private void updateScreenForBatsmanSelect(int scoringButtonsVisibility, int batsmanSelectionVisibility, int currentFacingSelectVisibility) {
        theView.findViewById(R.id.llScoring).setVisibility(scoringButtonsVisibility);
        theView.findViewById(R.id.btnSelBatsman).setVisibility(batsmanSelectionVisibility);
        theView.findViewById(R.id.btnSelFacingBatsman).setVisibility(currentFacingSelectVisibility);
        TextView tvOutBatsmanDetails = theView.findViewById(R.id.tvOutBatsmanDetails);

        if(dismissalType != null && !startInnings)
		{
			int outBatsmanVisibility = (batsmanSelectionVisibility == View.VISIBLE || currentFacingSelectVisibility == View.VISIBLE)
										? View.VISIBLE : View.GONE;
			tvOutBatsmanDetails.setVisibility(outBatsmanVisibility);
			String outBy = "", bowledBy = "", score = outBatsman.getRunsScored() + "(" + outBatsman.getBallsPlayed() + ")";
			switch (dismissalType) {
				case CAUGHT:
					outBy = "c " + ((outBatsman.getWicketEffectedBy().getID() == ccUtils.getBowler().getPlayer().getID())
									? "&" : outBatsman.getWicketEffectedBy().getName());
					bowledBy = "b " + ccUtils.getBowler().getBowlerName();
					break;

				case STUMPED:
					outBy = "st " + ccUtils.getCard().getBowlingTeam().getWicketKeeper().getName();
					bowledBy = "b " + ccUtils.getBowler().getBowlerName();
					break;

				case RUN_OUT:
					outBy = "RunOut (" + outBatsman.getWicketEffectedBy().getName() + ")";
					break;

				case BOWLED:
					bowledBy = "b " + ccUtils.getBowler().getBowlerName();
					break;

				case LBW:
					bowledBy = "lbw b " + ccUtils.getBowler().getBowlerName();
					break;

				case HIT_BALL_TWICE:
					outBy = "(hit ball twice)";
					break;

				case HIT_WICKET:
					outBy = "(hit-wicket)";
					break;

				case OBSTRUCTING_FIELD:
					outBy = "(obstructing field)";
					break;

				case RETIRED:
					outBy = "(retired)";
					break;

				case TIMED_OUT:
					outBy = "(timed out)";
					break;
			}

			tvOutBatsmanDetails.setText(String.format(Locale.getDefault(), getString(R.string.outBatsmanData),
					outBatsman.getBatsmanName(), outBy, bowledBy, score));
		} else {
			tvOutBatsmanDetails.setVisibility(View.GONE);
		}
    }

	private void updateScreenForBowlerSelect(int scoringButtonsVisibility, int bowlerSelectVisibility) {
        theView.findViewById(R.id.llScoring).setVisibility(scoringButtonsVisibility);
        theView.findViewById(R.id.btnSelBowler).setVisibility(bowlerSelectVisibility);
    }

    private void updateViewToCloseInnings() {
        if(ccUtils.getCard().getInnings() == 1) {
			updateScreenForBatsmanSelect(View.GONE, View.GONE, View.GONE);
			updateScreenForBowlerSelect(View.GONE, View.GONE);
			btnStartNextInnings.setVisibility(View.VISIBLE);
			tvInningsComplete.setVisibility(View.VISIBLE);
		}
        else {
			showResult();
		}
    }

    private void startNewInnings() {
		dbHandler.clearMatchStateHistory(0, matchID, -1);
		ccUtils.setNewInnings();
		startInnings = true;
		tvInningsComplete.setVisibility(View.GONE);
		btnStartNextInnings.setVisibility(View.GONE);
		updateLayout(true, true);
	}

    private void showResult() {
		updateScreenForBatsmanSelect(View.GONE, View.GONE, View.GONE);
		updateScreenForBowlerSelect(View.GONE, View.GONE);
		theView.findViewById(R.id.tvInningsComplete).setVisibility(View.VISIBLE);
		int score = ccUtils.getCard().getScore();
		int target = ccUtils.getCard().getTarget();
		int wicketsFallen = ccUtils.getCard().getWicketsFallen();
		int maxWickets = ccUtils.getMaxWickets();

		String result;
		Team winningTeam = null;
		boolean matchTied = false;
		if(score >= target) {
			winningTeam = ccUtils.getTeam2();
			result = String.format(Locale.getDefault(), "%s WON by %d wickets", winningTeam.getName(), (maxWickets - wicketsFallen));
		} else if(score < (target - 1)) {
			winningTeam = ccUtils.getTeam1();
			result = String.format(Locale.getDefault(), "%s won by %d runs", winningTeam.getName(), (target - 1 - score));
		} else {
			result = "Match TIED";
			matchTied = true;
		}

		completeMatch(result, winningTeam, matchTied, false);

		theView.findViewById(R.id.llScoring).setVisibility(View.GONE);
		tvResult.setVisibility(View.VISIBLE);
		theView.findViewById(R.id.llCompleteMatch).setVisibility(View.VISIBLE);

		tvResult.setText(result);
    }

    private int saveMatch(String saveName){
        return dbHandler.saveMatchState(matchID, CommonUtils.convertToJSON(ccUtils), saveName);
    }

    private void autoSaveMatch(){
		storeCCUtils = new StoreCCUtils();
		/*try {
			CricketCardUtils saveCCUtils = ccUtils.clone();
			storeCCUtils.execute(saveCCUtils, dbHandler, matchID).get(0, TimeUnit.MICROSECONDS);
			storeCCUtils = null;
		} catch (ExecutionException | InterruptedException | TimeoutException e) {
			e.printStackTrace();
		}*/

        dbHandler.autoSaveMatch(matchID, CommonUtils.convertToJSON(ccUtils), ccUtils.getMatchName());
        dbHandler.clearMatchStateHistory(DatabaseHandler.maxUndoAllowed, matchID, -1);
    }

    private void confirmExitMatch() {
		if(getFragmentManager() != null) {
			ConfirmationDialog confirmationDialog = ConfirmationDialog.newInstance(CONFIRMATION_CODE_EXIT_MATCH,
					"Exit Match", "Do you want to exit the match? Consider saving the match, if not done, for loading it later.");
			confirmationDialog.setConfirmationClickListener(this);
			confirmationDialog.show(getFragmentManager(), "ExitMatchDialog");
		}
	}

    private void confirmCloseMatch() {
		if(getFragmentManager() != null) {
			ConfirmationDialog confirmationDialog = ConfirmationDialog.newInstance(CONFIRMATION_CODE_CLOSE_MATCH,
					"Close Match", "Do you want to complete the match? You will not be able to make any further changes to the match.");
			confirmationDialog.setConfirmationClickListener(this);
			confirmationDialog.show(getFragmentManager(), "CloseMatchDialog");
		}
	}

	private void showSavedMatchDialog() {
		List<MatchState> savedMatchDataList = dbHandler.getSavedMatches(DatabaseHandler.SAVE_MANUAL, matchID, null, false);
		if(savedMatchDataList != null && savedMatchDataList.size() > 0) {
			Intent getMatchListIntent = new Intent(getContext(), MatchStateSelectActivity.class);
			getMatchListIntent.putExtra(MatchStateSelectActivity.ARG_MATCH_LIST, savedMatchDataList.toArray());
			getMatchListIntent.putExtra(MatchStateSelectActivity.ARG_IS_MULTI_SELECT, false);
			startActivityForResult(getMatchListIntent, REQ_CODE_GET_MATCHES_TO_LOAD);
		} else {
			Toast.makeText(getContext(), "No Saved matches found.", Toast.LENGTH_SHORT).show();
		}
	}

	private void showScoreCard() {
		Intent scoreCardIntent = new Intent(getContext(), ScoreCardActivity.class);

		scoreCardIntent.putExtra(ScoreCardActivity.ARG_CRICKET_CARD_UTILS, CommonUtils.convertToJSON(ccUtils));

		startActivity(scoreCardIntent);
	}

	private void displayPlayerDialog(Team team, String type) {
		if(getFragmentManager() != null) {

			poMPlayers = new String[team.getMatchPlayers().size()];

			int i=0;
			for(Player fielder : team.getMatchPlayers()) {
				String fielderName = fielder.getName();
				if(fielder.getID() == team.getCaptain().getID()) {
					fielderName += " (c)";
				} else if(fielder.getID() == team.getWicketKeeper().getID()) {
					fielderName += " (w)";
				}
				poMPlayers[i++] = fielderName;
			}

			StringDialog dialog = StringDialog.newInstance("Select Fielder", poMPlayers, type);
			dialog.setDialogItemClickListener(this);
			dialog.show(getFragmentManager(), "EffectedByDialog");
		}
	}

	private void showPlayerOfMatch(Player player) {
		if (player != null) {
			ccUtils.setPlayerOfMatch(player);
			tvPoM.setText(player.getName());
			tvPoM.setVisibility(View.VISIBLE);
			theView.findViewById(R.id.btnClose).setVisibility(View.VISIBLE);
		}
	}

	private void saveBundle(Bundle outState) {
		outState.putString(BUNDLE_CC_UTILS, CommonUtils.convertToJSON(ccUtils));

		outState.putSerializable(BUNDLE_NEW_BATSMAN, newBatsman);
		outState.putSerializable(BUNDLE_OUT_BATSMAN, outBatsman);
		outState.putSerializable(BUNDLE_HURT_BOWLER, hurtBowler);

		outState.putInt(BUNDLE_MATCH_STATE_ID, matchStateID);
		outState.putInt(BUNDLE_MATCH_ID, matchID);
		outState.putInt(BUNDLE_CURRENT_UNDO_COUNT, currentUndoCount);

		outState.putBoolean(BUNDLE_IS_START_INNINGS, startInnings);
		outState.putBoolean(BUNDLE_IS_LOAD, isLoad);
		outState.putBoolean(BUNDLE_IS_UNDO, isUndo);

		outState.putString(BUNDLE_SAVE_MATCH_NAME, saveMatchName);
		outState.putStringArray(BUNDLE_POM_PLAYERS, poMPlayers);

		outState.putBoolean(BUNDLE_BOWLER_CHANGED, bowlerChanged);
		outState.putBoolean(BUNDLE_NEW_BATSMAN_ARRIVED, newBatsmanArrived);
		outState.putBoolean(BUNDLE_ENABLE_OPTION_BATSMAN_HURT, enableBatsmanHurtOption);
		outState.putBoolean(BUNDLE_ENABLE_OPTION_BATSMAN_FACING, enableChangeFacingOption);
		outState.putBoolean(BUNDLE_ENABLE_OPTION_BOWLER_HURT, enableBowlerHurtOption);
	}

	private void loadFromBundle(Bundle savedBundle) {
		ccUtils = CommonUtils.convertToCCUtils(savedBundle.getString(BUNDLE_CC_UTILS));

		newBatsman = (BatsmanStats) savedBundle.getSerializable(BUNDLE_NEW_BATSMAN);
		outBatsman = (BatsmanStats) savedBundle.getSerializable(BUNDLE_OUT_BATSMAN);
		hurtBowler = (BowlerStats) savedBundle.getSerializable(BUNDLE_HURT_BOWLER);

		matchStateID = savedBundle.getInt(BUNDLE_MATCH_STATE_ID);
		matchID = savedBundle.getInt(BUNDLE_MATCH_ID);
		currentUndoCount = savedBundle.getInt(BUNDLE_CURRENT_UNDO_COUNT);

		startInnings = savedBundle.getBoolean(BUNDLE_IS_START_INNINGS, false);
		isLoad = savedBundle.getBoolean(BUNDLE_IS_LOAD, false);
		isUndo = savedBundle.getBoolean(BUNDLE_IS_UNDO, false);

		saveMatchName = savedBundle.getString(BUNDLE_SAVE_MATCH_NAME, null);
		poMPlayers = savedBundle.getStringArray(BUNDLE_POM_PLAYERS);

		bowlerChanged = savedBundle.getBoolean(BUNDLE_BOWLER_CHANGED, false);
		newBatsmanArrived = savedBundle.getBoolean(BUNDLE_NEW_BATSMAN_ARRIVED, false);
		enableBatsmanHurtOption = savedBundle.getBoolean(BUNDLE_ENABLE_OPTION_BATSMAN_HURT, false);
		enableChangeFacingOption = savedBundle.getBoolean(BUNDLE_ENABLE_OPTION_BATSMAN_FACING, false);
		enableBowlerHurtOption = savedBundle.getBoolean(BUNDLE_ENABLE_OPTION_BOWLER_HURT, false);
	}

	private void startTournamentMatch() {
		if (matchInfo != null) {
			matchInfo.setMatchID(matchID);
			dbHandler.startTournamentMatch(matchInfo);
		}
	}

	private void completeMatch(String result, Team winningTeam, boolean matchTied, boolean isAbandoned) {
		ccUtils.setResult(result, winningTeam, matchTied, isAbandoned);

		if (isTournament) {
			int winningTeamID = winningTeam != null ? winningTeam.getId() : 0;
			dbHandler.completeTournamentMatch(ccUtils.getMatchInfo().getId(), matchID, winningTeamID);
			TournamentUtils utils = new TournamentUtils(getContext());
			utils.checkTournamentStageComplete(matchInfo.getId());
		}
	}

	private boolean getSelectFacingBatsman() {
		boolean selectFacing = false;
		if (dismissalType != null) {
			switch (dismissalType) {
				case RUN_OUT:
				case RETIRED:
				case RETIRED_HURT:
				case OBSTRUCTING_FIELD:
				case CAUGHT:
					selectFacing = true;
					break;
			}
		}

		return selectFacing;
	}

	private DismissalType determineDismissalType() {
		DismissalType dismissalType = null;
		if (ccUtils != null) {
			if (Double.parseDouble(ccUtils.getCard().getTotalOversBowled()) > 0
					|| !ccUtils.isNewOver()) {
				if (ccUtils.getCurrentFacing() == null || ccUtils.getOtherBatsman() == null) {
					List<BallInfo> ballInfoList = ccUtils.getCard().getCurrOver().getBallInfo();
					BallInfo lastBallInfo = ballInfoList.get(ballInfoList.size() - 1);
					if (lastBallInfo.getWicketData() != null) {
						dismissalType = lastBallInfo.getWicketData().getDismissalType();
					}
				}
			}
		}

		return dismissalType;
	}
}
