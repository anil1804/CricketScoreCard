package com.thenewcone.myscorecard;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import com.thenewcone.myscorecard.activity.LimitedOversScorecardActivity;
import com.thenewcone.myscorecard.activity.MainActivity;
import com.thenewcone.myscorecard.activity.ManagePlayerActivity;
import com.thenewcone.myscorecard.activity.TestCricketScorecardActivity;

public class NavigationDrawerHandler {

	public static Intent onNavigationItemSelected(Context context, @NonNull MenuItem menuItem, Class sourceClass) {
		Intent intent = null;

		switch (menuItem.getItemId()) {
			case R.id.nav_home:
				if(sourceClass != MainActivity.class) {
					intent = new Intent(context, MainActivity.class);
				}
				break;

			case R.id.nav_cricket_test:
				if(sourceClass != TestCricketScorecardActivity.class) {
					intent = new Intent(context, TestCricketScorecardActivity.class);
				}
				break;

			case R.id.nav_cricket_limited:
				if(sourceClass != LimitedOversScorecardActivity.class) {
					intent = new Intent(context, LimitedOversScorecardActivity.class);
				}
				break;

			case R.id.nav_manage_player:
				if(sourceClass != ManagePlayerActivity.class) {
					intent = new Intent(context, ManagePlayerActivity.class);
				}
				break;
		}

		return intent;
	}
}
