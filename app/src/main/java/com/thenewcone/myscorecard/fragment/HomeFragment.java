package com.thenewcone.myscorecard.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.intf.DialogItemClickListener;
import com.thenewcone.myscorecard.utils.CommonUtils;
import com.thenewcone.myscorecard.utils.database.AddDBData;
import com.thenewcone.myscorecard.utils.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener, DialogItemClickListener {

	private static final String STRING_DIALOG_LOAD_SAVED_MATCHES = "LoadSavedMatches";
	DatabaseHandler dbHandler;
	SparseArray<String> savedMatchDataList;

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

		dbHandler = new DatabaseHandler(getContext());

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
					showSavedMatchDialog();
					break;
			}
		}
	}

	private void showSavedMatchDialog() {
		if(getFragmentManager() != null) {
			savedMatchDataList = dbHandler.getSavedMatches(DatabaseHandler.SAVE_MANUAL, 0, null);
			if (savedMatchDataList != null && savedMatchDataList.size() > 0) {
				List<String> savedMatches = new ArrayList<>();
				for (int i = 0; i < savedMatchDataList.size(); i++)
					savedMatches.add(savedMatchDataList.valueAt(i));

				Collections.sort(savedMatches);


				StringDialog dialog = StringDialog.newInstance("Select Match to Load", CommonUtils.listToArray(savedMatches), STRING_DIALOG_LOAD_SAVED_MATCHES);
				dialog.setDialogItemClickListener(this);
				dialog.show(getFragmentManager(), "SavedMatchDialog");
			} else {
				Toast.makeText(getContext(), "No Saved Matches Found", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onItemSelect(String type, String value, int position) {
		switch (type) {
			case STRING_DIALOG_LOAD_SAVED_MATCHES:
				if(getActivity() != null) {
					int matchStateID = savedMatchDataList.keyAt(savedMatchDataList.indexOfValue(value));
					String fragmentTag = NewMatchFragment.class.getSimpleName();
					getActivity().getSupportFragmentManager().beginTransaction()
							.replace(R.id.frame_container, LimitedOversFragment.loadInstance(matchStateID), fragmentTag)
							.addToBackStack(fragmentTag)
							.commit();
				}
				break;
		}
	}
}
