package com.theNewCone.cricketScoreCard.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.intf.ConfirmationDialogClickListener;

public class ConfirmationDialog extends DialogFragment{

    private String title, message;
    private int confirmationCode;
    ConfirmationDialogClickListener confirmationClickListener;

    public static final String ARG_TITLE = "Title";
    public static final String ARG_MESSAGE = "Message";
    public static final String ARG_CONFIRMATION_CODE = "ConfirmationCode";

    public ConfirmationDialog() {
    }

    public static ConfirmationDialog newInstance(int confirmationCode, String title, String message) {
        ConfirmationDialog dialog = new ConfirmationDialog();
        Bundle args = new Bundle();

        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        args.putInt(ARG_CONFIRMATION_CODE, confirmationCode);

        dialog.setArguments(args);

        return dialog;
    }

    public void setConfirmationClickListener(ConfirmationDialogClickListener listener) {
        confirmationClickListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        if(args != null) {
            this.title = args.getString(ARG_TITLE);
            this.message = args.getString(ARG_MESSAGE);
            this.confirmationCode = args.getInt(ARG_CONFIRMATION_CODE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmationClickListener.onConfirmationClick(confirmationCode, true);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmationClickListener.onConfirmationClick(confirmationCode, false);
                    }
                });

        return builder.create();
    }
}
