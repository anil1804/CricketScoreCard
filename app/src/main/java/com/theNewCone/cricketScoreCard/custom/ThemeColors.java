package com.theNewCone.cricketScoreCard.custom;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.theNewCone.cricketScoreCard.R;

public class ThemeColors {
	private static final String APPLIED_THEME = "Theme";

	public ThemeColors(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		int theme = prefs.getInt(APPLIED_THEME, R.style.AppTheme);
		context.setTheme(theme);
	}

	public static void applyTheme(Activity activity, int theme) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
		prefs.edit().putInt(APPLIED_THEME, theme).apply();
		activity.recreate();
	}
}
