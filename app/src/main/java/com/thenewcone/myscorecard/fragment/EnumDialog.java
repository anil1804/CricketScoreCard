package com.thenewcone.myscorecard.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.thenewcone.myscorecard.intf.DialogItemClickListener;

public class EnumDialog extends DialogFragment{

    private String title, message, enumType;
    String[] enumeration;
    DialogItemClickListener dialogItemClickListener;

    public static final String ARG_TITLE = "Title";
    public static final String ARG_ENUM_VALUES = "EnumValues";
    public static final String ARG_ENUM_TYPE = "EnumType";
    public static final String ARG_ENUM_TYPE_BAT_STYLE = "BattingStyle";
    public static final String ARG_ENUM_TYPE_BOWL_STYLE = "BowlingStyle";

    public EnumDialog() {
    }

    public static EnumDialog newInstance(String title, String[] enumeration, String enumType) {
        EnumDialog dialog = new EnumDialog();
        Bundle args = new Bundle();

        args.putString(ARG_TITLE, title);
        args.putStringArray(ARG_ENUM_VALUES, enumeration);
        args.putString(ARG_ENUM_TYPE, enumType);

        dialog.setArguments(args);

        return dialog;
    }

    public void setDialogItemClickListener(DialogItemClickListener listener) {
        dialogItemClickListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        if(args != null) {
            this.title = args.getString(ARG_TITLE);
            this.enumeration = args.getStringArray(ARG_ENUM_VALUES);
            this.enumType = args.getString(ARG_ENUM_TYPE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(message)
                .setItems(this.enumeration, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogItemClickListener.onItemSelect(enumType, enumeration[which]);
                    }
                });

        return builder.create();
    }
}
