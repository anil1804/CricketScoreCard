package com.thenewcone.myscorecard;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.thenewcone.myscorecard.scorecard.Extra;
import com.thenewcone.myscorecard.utils.CommonUtils;

public class ExtrasDialogFragment extends DialogFragment {

	int selectedIndex;
	Extra.ExtraType extraType;
	public static final String BATTING_TEAM = "Batting Team";
	public static final String BOWLING_TEAM = "Bowling Team";
	public static final String ARG_EXTRA_TYPE = "Extra Type";

	public interface ExtrasDialogListener {
		void getExtraDetails(Extra.ExtraType extraType, int numRuns, String team);
	}

	ExtrasDialogListener mListener;

	public static DialogFragment newInstance(Extra.ExtraType type) {
		ExtrasDialogFragment dialog = new ExtrasDialogFragment();

		Bundle bundle = new Bundle();
		bundle.putSerializable(ARG_EXTRA_TYPE, type);
		dialog.setArguments(bundle);

		return dialog;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = getArguments();
		if(bundle != null) {
			extraType = (Extra.ExtraType) bundle.getSerializable(ARG_EXTRA_TYPE);
		}
		Log.i(CommonUtils.LOG_TAG, "Extra Type : " + extraType);
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);

		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the ExtrasDialogListener so we can send events to the host
			mListener = (ExtrasDialogListener) getTargetFragment();
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(getTargetFragment().toString() + " must implement NoticeDialogListener");
		}
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		int defaultIndex = 0;
		selectedIndex = defaultIndex;

		String title = getTitle();

		final String[] extraRunArray = getExtraRunArray();
		builder.setTitle(title)
				.setSingleChoiceItems(extraRunArray, defaultIndex, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						selectedIndex = i;
					}
				})
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						int numRuns = 5;
						String team = null;
						if(extraType != Extra.ExtraType.PENALTY)
							numRuns = Integer.parseInt(extraRunArray[selectedIndex]);
						else
							team = extraRunArray[selectedIndex];

						mListener.getExtraDetails(extraType, numRuns, team);
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						mListener.getExtraDetails(extraType,-1, null);
					}
				});

		return builder.create();
	}

	private String[] getExtraRunArray() {
		String[] extraRunsArray = null;

		switch (extraType) {
			case WIDE:
				extraRunsArray = new String[]{"0", "1", "2", "3", "4"};
				break;
			case NO_BALL:
				extraRunsArray = new String[]{"0", "1", "2", "3", "4", "6"};
				break;
			case PENALTY:
				extraRunsArray = new String[]{BATTING_TEAM, BOWLING_TEAM};
				break;
			case BYE:
				extraRunsArray = new String[]{"1", "2", "3", "4", "6"};
				break;
			case LEG_BYE:
				extraRunsArray = new String[]{"1", "2", "3", "4"};
				break;
		}

		return extraRunsArray;
	}

	private String getTitle() {
		switch (extraType) {
			case WIDE:
				return getString(R.string.extrasDialogTitleAdditional);
			case NO_BALL:
				return getString(R.string.extrasDialogTitleBatsman);
			case PENALTY:
				return getString(R.string.extraDialogTitlePenalty);
			default:
				return getString(R.string.extrasDialogTitle);
		}
	}
}
