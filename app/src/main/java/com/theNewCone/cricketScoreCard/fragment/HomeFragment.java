package com.theNewCone.cricketScoreCard.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.activity.CompletedMatchSelectActivity;
import com.theNewCone.cricketScoreCard.activity.MatchStateSelectActivity;
import com.theNewCone.cricketScoreCard.activity.TournamentCompleteActivity;
import com.theNewCone.cricketScoreCard.activity.TournamentHomeActivity;
import com.theNewCone.cricketScoreCard.activity.TournamentSelectActivity;
import com.theNewCone.cricketScoreCard.enumeration.TournamentFormat;
import com.theNewCone.cricketScoreCard.intf.ConfirmationDialogClickListener;
import com.theNewCone.cricketScoreCard.intf.DrawerController;
import com.theNewCone.cricketScoreCard.match.CricketCardUtils;
import com.theNewCone.cricketScoreCard.match.Match;
import com.theNewCone.cricketScoreCard.match.MatchState;
import com.theNewCone.cricketScoreCard.tournament.MatchInfo;
import com.theNewCone.cricketScoreCard.tournament.Tournament;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;
import com.theNewCone.cricketScoreCard.utils.TournamentUtils;
import com.theNewCone.cricketScoreCard.utils.database.DatabaseHandler;

import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements View.OnClickListener, ConfirmationDialogClickListener {

	private static final int REQ_CODE_MATCH_LIST_LOAD = 1;
	private static final int REQ_CODE_MATCH_LIST_DELETE = 2;
	private static final int REQ_CODE_MATCH_LIST_FINISHED_LOAD = 3;
	private static final int REQ_CODE_MATCH_LIST_FINISHED_DELETE = 4;
	private static final int REQ_CODE_TOURNAMENT_LOAD_RUNNING = 5;
	private static final int REQ_CODE_TOURNAMENT_LOAD_FINISHED = 6;

	private static final int CONFIRMATION_CODE_DELETE_SAVED_MATCHES = 1;
	private static final int CONFIRMATION_CODE_LOAD_LAST_MATCH = 2;
	private static final int CONFIRMATION_CODE_DELETE_FINISHED_MATCHES = 3;

	DatabaseHandler dbHandler;

	MatchState[] matchStatesToDelete;
	int matchStateID = -1;

	Match[] matchesToDelete;

	public static HomeFragment newInstance() {
		return new HomeFragment();
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
        View theView;
		theView = inflater.inflate(R.layout.fragment_home, container, false);

		theView.findViewById(R.id.btnNewMatch).setOnClickListener(this);
		theView.findViewById(R.id.btnManagePlayer).setOnClickListener(this);
        theView.findViewById(R.id.btnManageTeam).setOnClickListener(this);
        theView.findViewById(R.id.btnLoadMatch).setOnClickListener(this);
        theView.findViewById(R.id.btnDeleteMatches).setOnClickListener(this);
        theView.findViewById(R.id.btnFinishedMatches).setOnClickListener(this);
		theView.findViewById(R.id.btnNewTournament).setOnClickListener(this);
		theView.findViewById(R.id.btnLoadTournament).setOnClickListener(this);
		theView.findViewById(R.id.btnFinishedTournaments).setOnClickListener(this);

		dbHandler = new DatabaseHandler(getContext());

		if(getActivity() != null) {
			DrawerController drawerController = (DrawerController) getActivity();
			drawerController.setDrawerEnabled(true);
			drawerController.enableAllDrawerMenuItems();
			drawerController.disableDrawerMenuItem(R.id.nav_home);
			getActivity().setTitle(getString(R.string.app_name));
		}

		checkForAutoSavedMatches();

		return theView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_fragments_home, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

			case R.id.menu_quit:
				if(getActivity() != null) {
					getActivity().finishAndRemoveTask();
				}
				break;

			case R.id.menu_clearFinishedMatches:
				displayFinishedMatches(true, REQ_CODE_MATCH_LIST_FINISHED_DELETE);
				break;

			case R.id.menu_trophy:
				startActivity(new Intent(getContext(), TournamentCompleteActivity.class));
				break;

		}
		return true;
	}

	@Override
	public void onClick(View view) {
		if(getActivity() != null) {
			FragmentManager fragMgr = getActivity().getSupportFragmentManager();
            String fragmentTag;

			switch(view.getId()) {

                case R.id.btnNewMatch:
                    fragmentTag = NewMatchFragment.class.getSimpleName();
                    fragMgr.beginTransaction()
                            .replace(R.id.frame_container, NewMatchFragment.newInstance(), fragmentTag)
                            .addToBackStack(fragmentTag)
                            .commit();
                    break;

                case R.id.btnManageTeam:
                    fragmentTag = TeamFragment.class.getSimpleName();
                    fragMgr.beginTransaction()
                            .replace(R.id.frame_container, TeamFragment.newInstance(), fragmentTag)
                            .addToBackStack(fragmentTag)
                            .commit();
                    break;

                case R.id.btnManagePlayer:
                    fragmentTag = PlayerFragment.class.getSimpleName();
                    fragMgr.beginTransaction()
                            .replace(R.id.frame_container, PlayerFragment.newInstance(), fragmentTag)
                            .addToBackStack(fragmentTag)
                            .commit();
                    break;

				case R.id.btnLoadMatch:
					showSavedMatchDialog(false, REQ_CODE_MATCH_LIST_LOAD);
					break;

				case R.id.btnDeleteMatches:
					showSavedMatchDialog(true, REQ_CODE_MATCH_LIST_DELETE);
					break;

				case R.id.btnFinishedMatches:
					displayFinishedMatches(false, REQ_CODE_MATCH_LIST_FINISHED_LOAD);
					break;

				case R.id.btnNewTournament:
					startNewTournament(fragMgr);
					break;

				case R.id.btnLoadTournament:
					showTournamentsDialog(false, REQ_CODE_TOURNAMENT_LOAD_RUNNING, false);
					break;

				case R.id.btnFinishedTournaments:
					showTournamentsDialog(false, REQ_CODE_TOURNAMENT_LOAD_FINISHED, true);
					break;
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case REQ_CODE_MATCH_LIST_LOAD:
				if(resultCode == MatchStateSelectActivity.RESP_CODE_OK) {
					MatchState selSavedMatch = (MatchState) data.getSerializableExtra(MatchStateSelectActivity.ARG_RESP_SEL_MATCH);
					loadSavedMatch(selSavedMatch.getId());
				}
				break;

			case REQ_CODE_MATCH_LIST_DELETE:
				if(resultCode == MatchStateSelectActivity.RESP_CODE_OK) {
					matchStatesToDelete = CommonUtils.objectArrToMatchStateArr(
							(Object[]) data.getSerializableExtra(MatchStateSelectActivity.ARG_RESP_SEL_MATCHES));

					if(matchStatesToDelete.length > 0 && getFragmentManager() != null) {
						ConfirmationDialog confirmationDialog = ConfirmationDialog.newInstance(CONFIRMATION_CODE_DELETE_SAVED_MATCHES,
								"Confirm Delete", String.format(Locale.getDefault(), "Do you want to delete these %d saved matches?", matchStatesToDelete.length));
						confirmationDialog.setConfirmationClickListener(this);
						confirmationDialog.show(getFragmentManager(), "DeleteSavedMatches");
					}
				}
				break;

			case REQ_CODE_MATCH_LIST_FINISHED_DELETE:
				if(resultCode == CompletedMatchSelectActivity.RESP_CODE_OK) {
					matchesToDelete = CommonUtils.objectArrToMatchArr(
							(Object[]) data.getSerializableExtra(CompletedMatchSelectActivity.ARG_RESP_SEL_MATCHES));

					if(matchesToDelete.length > 0 && getFragmentManager() != null) {
						ConfirmationDialog confirmationDialog = ConfirmationDialog.newInstance(CONFIRMATION_CODE_DELETE_FINISHED_MATCHES,
								"Confirm Delete", String.format(Locale.getDefault(), "Do you want to delete these %d finished matches?", matchesToDelete.length));
						confirmationDialog.setConfirmationClickListener(this);
						confirmationDialog.show(getFragmentManager(), "DeleteSavedMatches");
					}
				}
				break;

			case REQ_CODE_MATCH_LIST_FINISHED_LOAD:
				if(resultCode == CompletedMatchSelectActivity.RESP_CODE_OK) {
					Match selMatch = (Match) data.getSerializableExtra(CompletedMatchSelectActivity.ARG_RESP_SEL_MATCH);
					if(getActivity() != null) {
						CricketCardUtils ccUtils = dbHandler.getCompletedMatchData(selMatch.getId());
						FragmentManager fragMgr = getActivity().getSupportFragmentManager();
						String fragmentTag = MatchSummaryFragment.class.getSimpleName();
						fragMgr.beginTransaction()
								.replace(R.id.frame_container, MatchSummaryFragment.newInstance(ccUtils, selMatch), fragmentTag)
								.addToBackStack(fragmentTag)
								.commit();
					}
				}
				break;

			case REQ_CODE_TOURNAMENT_LOAD_RUNNING:
				if (resultCode == TournamentSelectActivity.RESP_CODE_OK) {
					Tournament tempTournament = (Tournament) data.getSerializableExtra(TournamentSelectActivity.ARG_RESP_SEL_TOURNAMENT);
					Tournament tournament = dbHandler.getTournamentContent(tempTournament.getId());
					if (tournament.getFormat() == TournamentFormat.GROUPS) {
						if (tournament.getGroupList() == null || tournament.getGroupList().size() == 0) {
							showGroupConfirmation(tournament);
						} else if (!tournament.isScheduled()) {
							showScheduleView(tournament);
						} else {
							showTournamentHome(tournament);
						}
					} else {
						if (!tournament.isScheduled()) {
							if (tournament.getGroupList() == null) {
								TournamentUtils tournamentUtils = new TournamentUtils(getContext());
								tournament = tournamentUtils.createInitialGroups(tournament);
								showScheduleView(tournament);
							}
						} else {
							showTournamentHome(tournament);
						}
					}
				}
		}
	}

	@Override
	public void onConfirmationClick(int confirmationCode, boolean accepted) {
		switch (confirmationCode) {
			case CONFIRMATION_CODE_DELETE_SAVED_MATCHES:
				if(accepted) {
					if(dbHandler.deleteSavedMatchStates(matchStatesToDelete)) {
						Toast.makeText(getContext(), "Saved Match Instances Deleted", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getContext(), "Unable to delete all saved matches. Please retry", Toast.LENGTH_LONG).show();
					}
				}
				break;
			case CONFIRMATION_CODE_DELETE_FINISHED_MATCHES:
				if(accepted) {
					if(dbHandler.deleteMatches(matchesToDelete)) {
						Toast.makeText(getContext(), "Finished Matches Deleted", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getContext(), "Unable to delete all finished matches. Please retry", Toast.LENGTH_LONG).show();
					}
				}
				break;

			case CONFIRMATION_CODE_LOAD_LAST_MATCH:
				if(accepted) {
					loadSavedMatch(matchStateID);
					matchStateID = -1;
					dbHandler.clearMatchStateHistory(1, -1, matchStateID);
				} else {
					dbHandler.clearAllAutoSaveHistory();
				}
				break;
		}
	}

	private void loadSavedMatch(int matchStateID) {
		if(getActivity() != null) {
			String fragmentTag = NewMatchFragment.class.getSimpleName();

			MatchInfo matchInfo = dbHandler.getMatchInfoBasedOnMatchStateID(matchStateID);

			getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.frame_container, LimitedOversFragment.loadInstance(matchStateID, matchInfo), fragmentTag)
					.addToBackStack(fragmentTag)
					.commit();
		}
	}

	private void showSavedMatchDialog(boolean isMulti, int requestCode) {
		List<MatchState> savedMatchDataList = dbHandler.getSavedMatches(DatabaseHandler.SAVE_MANUAL, 0, null, false);
		if(savedMatchDataList != null && savedMatchDataList.size() > 0) {
			Intent getMatchListIntent = new Intent(getContext(), MatchStateSelectActivity.class);
			getMatchListIntent.putExtra(MatchStateSelectActivity.ARG_MATCH_LIST, savedMatchDataList.toArray());
			getMatchListIntent.putExtra(MatchStateSelectActivity.ARG_IS_MULTI_SELECT, isMulti);
			startActivityForResult(getMatchListIntent, requestCode);
		} else {
			Toast.makeText(getContext(), "No Saved matches found.", Toast.LENGTH_SHORT).show();
		}
	}

	private void checkForAutoSavedMatches() {
		matchStateID = dbHandler.getLastAutoSave();
		if(matchStateID > 0 && getFragmentManager() != null) {
			ConfirmationDialog confirmationDialog = ConfirmationDialog.newInstance(CONFIRMATION_CODE_LOAD_LAST_MATCH,
					"Load Match", "Abruptly closed match found. Do you want to load it?" +
							"\nYou will not be able to load it later." +
							"\n\nLast ball information in the score-card is however lost.");
			confirmationDialog.setConfirmationClickListener(this);
			confirmationDialog.show(getFragmentManager(), "LoadClosedMatches");
		}
	}

	private void displayFinishedMatches(boolean isMulti, int requestCode) {
		List<Match> finishedMatches = dbHandler.getCompletedMatches();
		if(finishedMatches.size() > 0) {
			Intent finishedMatchesIntent = new Intent(getContext(), CompletedMatchSelectActivity.class);
			finishedMatchesIntent.putExtra(CompletedMatchSelectActivity.ARG_MATCH_LIST, finishedMatches.toArray());
			finishedMatchesIntent.putExtra(CompletedMatchSelectActivity.ARG_IS_MULTI_SELECT, isMulti);
			startActivityForResult(finishedMatchesIntent, requestCode);
		} else {
			Toast.makeText(getContext(), "No Finished Matches found.", Toast.LENGTH_SHORT).show();
		}
	}

	private void startNewTournament(FragmentManager fragMgr) {
		String fragmentTag;
		if (dbHandler.getTeams(null, 0).size() < 2) {
			String title = "Not enough Teams";
			String message = "Need at-least 2 teams to start a tournament.\n" +
					"Use 'Manage Teams' to create teams before starting a new tournament.";
			if (getFragmentManager() != null) {
				InformationDialog infoDialog = InformationDialog.newInstance(title, message);
				infoDialog.show(getFragmentManager(), "NotEnoughTeams");
			} else {
				Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
			}
		} else {

			fragmentTag = NewTournamentFragment.class.getSimpleName();
			fragMgr.beginTransaction()
					.replace(R.id.frame_container, NewTournamentFragment.newInstance(), fragmentTag)
					.addToBackStack(fragmentTag)
					.commit();
		}
	}

	private void showTournamentsDialog(boolean isMulti, int requestCode, boolean getCompleted) {
		List<Tournament> tournamentList = dbHandler.getTournaments(getCompleted);
		if (tournamentList.size() > 0) {
			Intent showTournamentsIntent = new Intent(getContext(), TournamentSelectActivity.class);
			showTournamentsIntent.putExtra(TournamentSelectActivity.ARG_TOURNAMENT_LIST, tournamentList.toArray());
			startActivityForResult(showTournamentsIntent, requestCode);
		} else {
			String message = "No " + (getCompleted ? "Finished" : "Running") + " Tournaments Found.";
			Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
		}
	}

	private void showGroupConfirmation(Tournament tournament) {
		if (getActivity() != null) {
			FragmentManager fragMgr = getActivity().getSupportFragmentManager();
			String fragmentTag = TournamentGroupsFragment.class.getSimpleName();

			fragMgr.beginTransaction()
					.replace(R.id.frame_container, TournamentGroupsFragment.newInstance(tournament), fragmentTag)
					.addToBackStack(fragmentTag)
					.commit();
		}
	}

	private void showScheduleView(Tournament tournament) {
		if (getActivity() != null) {
			FragmentManager fragMgr = getActivity().getSupportFragmentManager();
			String fragmentTag = TournamentScheduleFragment.class.getSimpleName();

			fragMgr.beginTransaction()
					.replace(R.id.frame_container, TournamentScheduleFragment.newInstance(tournament, 0), fragmentTag)
					.addToBackStack(fragmentTag)
					.commit();
		}
	}

	private void showTournamentHome(Tournament tournament) {
		Intent intent = new Intent(getContext(), TournamentHomeActivity.class);
		intent.putExtra(TournamentHomeActivity.ARG_TOURNAMENT, tournament);
		startActivity(intent);
	}
}
