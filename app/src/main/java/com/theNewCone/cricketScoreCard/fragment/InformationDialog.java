package com.theNewCone.cricketScoreCard.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

public class InformationDialog extends DialogFragment {

	public static final String ARG_TITLE = "Title";
	public static final String ARG_MESSAGE = "Message";
	private String title, message;

	public InformationDialog() {
	}

	public static InformationDialog newInstance(String title, String message) {
		InformationDialog dialog = new InformationDialog();
		Bundle args = new Bundle();

		args.putString(ARG_TITLE, title);
		args.putString(ARG_MESSAGE, message);

		dialog.setArguments(args);

		return dialog;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle args = getArguments();

		if (args != null) {
			this.title = args.getString(ARG_TITLE);
			this.message = args.getString(ARG_MESSAGE);
		}
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title)
				.setMessage(message)
				.setPositiveButton("OK", null);
		return builder.create();
	}
}
