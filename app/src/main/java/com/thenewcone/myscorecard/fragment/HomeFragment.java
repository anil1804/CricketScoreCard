package com.thenewcone.myscorecard.fragment;

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

import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.activity.CompletedMatchSelectActivity;
import com.thenewcone.myscorecard.activity.MatchStateSelectActivity;
import com.thenewcone.myscorecard.intf.ConfirmationDialogClickListener;
import com.thenewcone.myscorecard.intf.DrawerLocker;
import com.thenewcone.myscorecard.match.CricketCardUtils;
import com.thenewcone.myscorecard.match.Match;
import com.thenewcone.myscorecard.match.MatchState;
import com.thenewcone.myscorecard.utils.CommonUtils;
import com.thenewcone.myscorecard.utils.database.AddDBData;
import com.thenewcone.myscorecard.utils.database.DatabaseHandler;

import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements View.OnClickListener, ConfirmationDialogClickListener {

	//private static final String STRING_DIALOG_LOAD_SAVED_MATCHES = "LoadSavedMatches";

	private static final int REQ_CODE_MATCH_LIST_LOAD = 1;
	private static final int REQ_CODE_MATCH_LIST_DELETE = 2;
	private static final int REQ_CODE_MATCH_LIST_FINISHED = 3;

	private static final int CONFIRMATION_CODE_DELETE_MATCHES = 1;
	private static final int CONFIRMATION_CODE_LOAD_LAST_MATCH = 2;

	DatabaseHandler dbHandler;

	MatchState[] matchesToDelete;
	int matchStateID = -1;

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
		theView = inflater.inflate(R.layout.home_fragment, container, false);

		theView.findViewById(R.id.btnNewMatch).setOnClickListener(this);
		theView.findViewById(R.id.btnManagePlayer).setOnClickListener(this);
        theView.findViewById(R.id.btnManageTeam).setOnClickListener(this);
        theView.findViewById(R.id.btnLoadMatch).setOnClickListener(this);
        theView.findViewById(R.id.btnDeleteMatches).setOnClickListener(this);
        theView.findViewById(R.id.btnFinishedMatches).setOnClickListener(this);

		dbHandler = new DatabaseHandler(getContext());

		if(getActivity() != null)
			((DrawerLocker) getActivity()).setDrawerEnabled(true);

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
			case R.id.menu_loadData:
				if(new AddDBData(getContext()).addAll())
					Toast.makeText(getContext(), "Data uploaded successfully", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(getContext(), "Data upload failed", Toast.LENGTH_SHORT).show();
				break;

			case R.id.menu_quit:
				if(getActivity() != null) {
					getActivity().finishAndRemoveTask();
				}
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
					displayFinishedMatches();
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
					matchesToDelete = CommonUtils.objectArrToMatchStateArr(
							(Object[]) data.getSerializableExtra(MatchStateSelectActivity.ARG_RESP_SEL_MATCHES));

					if(matchesToDelete.length > 0 && getFragmentManager() != null) {
						ConfirmationDialog confirmationDialog = ConfirmationDialog.newInstance(CONFIRMATION_CODE_DELETE_MATCHES,
								"Confirm Delete", String.format(Locale.getDefault(), "Do you want to delete these %d saved matches?", matchesToDelete.length));
						confirmationDialog.setConfirmationClickListener(this);
						confirmationDialog.show(getFragmentManager(), "DeleteSavedMatches");
					}
				}
				break;

			case REQ_CODE_MATCH_LIST_FINISHED:
				if(resultCode == CompletedMatchSelectActivity.RESP_CODE_OK) {
					Match selMatch = (Match) data.getSerializableExtra(CompletedMatchSelectActivity.ARG_RESP_SEL_MATCH);
					if(getActivity() != null) {
						CricketCardUtils ccUtils = CommonUtils.convertToCCUtils(dbHandler.getCompletedMatch(selMatch.getId()));
						FragmentManager fragMgr = getActivity().getSupportFragmentManager();
						String fragmentTag = MatchSummaryFragment.class.getSimpleName();
						fragMgr.beginTransaction()
								.replace(R.id.frame_container, MatchSummaryFragment.newInstance(ccUtils, selMatch), fragmentTag)
								.addToBackStack(fragmentTag)
								.commit();
					}
				}
				break;
		}
	}

	@Override
	public void onConfirmationClick(int confirmationCode, boolean accepted) {
		switch (confirmationCode) {
			case CONFIRMATION_CODE_DELETE_MATCHES:
				if(accepted) {
					if(dbHandler.deleteSavedMatchStates(matchesToDelete)) {
						Toast.makeText(getContext(), "Matches Deleted", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getContext(), "Unable to delete all matches. Please retry", Toast.LENGTH_LONG).show();
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

			getActivity().getSupportFragmentManager().beginTransaction()
					.replace(R.id.frame_container, LimitedOversFragment.loadInstance(matchStateID), fragmentTag)
					.addToBackStack(fragmentTag)
					.commit();
		}
	}

	private void showSavedMatchDialog(boolean isMulti, int requestCode) {
		List<MatchState> savedMatchDataList = dbHandler.getSavedMatches(DatabaseHandler.SAVE_MANUAL, 0, null);
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

	private void displayFinishedMatches() {
		List<Match> finishedMatches = dbHandler.getCompletedMatches();
		if(finishedMatches.size() > 0) {
			Intent finishedMatchesIntent = new Intent(getContext(), CompletedMatchSelectActivity.class);
			finishedMatchesIntent.putExtra(CompletedMatchSelectActivity.ARG_MATCH_LIST, finishedMatches.toArray());
			startActivityForResult(finishedMatchesIntent, REQ_CODE_MATCH_LIST_FINISHED);
		} else {
			Toast.makeText(getContext(), "No Finished Matches found.", Toast.LENGTH_SHORT).show();
		}
	}
}
