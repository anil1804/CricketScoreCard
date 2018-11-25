package com.theNewCone.cricketScoreCard.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.theNewCone.cricketScoreCard.Constants;
import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.fragment.HomeFragment;
import com.theNewCone.cricketScoreCard.fragment.NewMatchFragment;
import com.theNewCone.cricketScoreCard.fragment.PlayerFragment;
import com.theNewCone.cricketScoreCard.fragment.TeamFragment;
import com.theNewCone.cricketScoreCard.help.HelpContentData;
import com.theNewCone.cricketScoreCard.intf.DrawerController;
import com.theNewCone.cricketScoreCard.utils.database.DatabaseHandler;

import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener, DrawerController {

	DrawerLayout drawer;
	ActionBarDrawerToggle toggle;
	NavigationView navigationView;

	Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.frame_container, HomeFragment.newInstance())
					.commitNow();
		}

		loadHelpContent();
		setupDrawer();
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			if(getSupportFragmentManager().getBackStackEntryCount() > 0)
				getSupportFragmentManager().popBackStack();
			else {
				if (isFragmentVisible(HomeFragment.class.getSimpleName())) {
					finish();
				} else {
					super.onBackPressed();
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		HashMap<String, Object> fragDtlMap = getFragmentDetails(item);

		if(fragDtlMap.size()  == 2) {
			Fragment fragment = (Fragment) fragDtlMap.get(Constants.FRAGMENT);
			String fragmentTag = (String) fragDtlMap.get(Constants.FRAGMENT_TAG);

			if(fragment !=  null && fragmentTag != null) {
				getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, fragment, fragmentTag)
                        .addToBackStack(fragmentTag)
                        .commit();
			}
		}

		drawer.closeDrawer(GravityCompat.START);

		return true;
	}

	@Override
	public void setDrawerEnabled(boolean enabled) {
		updateDrawerEnabled(enabled);
	}

	@Override
	public void disableAllDrawerMenuItems() {
		int menuSize = navigationView.getMenu().size();
		for(int i=0; i<menuSize; i++) {
			MenuItem item = navigationView.getMenu().getItem(i);
			if(item.isVisible())
				item.setEnabled(false);
		}
	}

	@Override
	public void disableDrawerMenuItem(int id) {
		MenuItem item = navigationView.getMenu().findItem(id);
		if(item != null)
			item.setEnabled(false);
	}

	@Override
	public void enableAllDrawerMenuItems() {
		int menuSize = navigationView.getMenu().size();
		for(int i=0; i<menuSize; i++) {
			MenuItem item = navigationView.getMenu().getItem(i);
			if(item.isVisible())
				item.setEnabled(true);
		}
	}

	@Override
	public void enableDrawerMenuItem(int id) {
		MenuItem item = navigationView.getMenu().findItem(id);
		if(item != null)
			item.setEnabled(true);
	}

	void setupDrawer() {
		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		drawer = findViewById(R.id.drawer_layout);
		toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

		updateDrawerEnabled(true);

		drawer.addDrawerListener(toggle);
		toggle.syncState();

		navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
	}

	public void updateDrawerEnabled(boolean enabled) {
		int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
		drawer.setDrawerLockMode(lockMode);
		toggle.setDrawerIndicatorEnabled(enabled);
	}

	private HashMap<String, Object> getFragmentDetails(@NonNull MenuItem item) {
		HashMap<String, Object> respMap = new HashMap<>();

		switch (item.getItemId()) {
			case R.id.nav_home:
				if(!isFragmentVisible(HomeFragment.class.getSimpleName())) {
					respMap.put(Constants.FRAGMENT, HomeFragment.newInstance());
					respMap.put(Constants.FRAGMENT_TAG, HomeFragment.class.getSimpleName());
				}
				break;

			case R.id.nav_manage_player:
				if(!isFragmentVisible(PlayerFragment.class.getSimpleName())) {
					respMap.put(Constants.FRAGMENT, PlayerFragment.newInstance());
					respMap.put(Constants.FRAGMENT_TAG, PlayerFragment.class.getSimpleName());
				}
				break;

			case R.id.nav_manage_team:
				if(!isFragmentVisible(TeamFragment.class.getSimpleName())) {
					respMap.put(Constants.FRAGMENT, TeamFragment.newInstance());
					respMap.put(Constants.FRAGMENT_TAG, TeamFragment.class.getSimpleName());
				}
				break;

			case R.id.nav_new_match:
				if(!isFragmentVisible(NewMatchFragment.class.getSimpleName())) {
					respMap.put(Constants.FRAGMENT, NewMatchFragment.newInstance());
					respMap.put(Constants.FRAGMENT_TAG, NewMatchFragment.class.getSimpleName());
				}
				break;

			case R.id.nav_faq:
				startActivity(new Intent(this, HelpListActivity.class));
				break;
		}

		return respMap;
	}

	private boolean isFragmentVisible(@NonNull String fragmentTag) {
		List<Fragment> fragList = getSupportFragmentManager().getFragments();

		for(Fragment frag : fragList) {
			if(frag != null && frag.isVisible() && fragmentTag.equals(frag.getTag()))
				return true;
		}

		return false;
	}

	private void loadHelpContent() {
		if(isAppUpdated() || !(new DatabaseHandler(this).hasHelpContent()))
		{
			HelpContentData helpContentData = new HelpContentData(this);
			helpContentData.loadHelpContent();
		}
	}

	private boolean isAppUpdated() {
		boolean isAppUpdated = false;
		long lastModified = 0L;
		try {
			ComponentName componentName = new ComponentName(this, HomeActivity.class);
			PackageInfo pkgInfo = this.getPackageManager().getPackageInfo(componentName.getPackageName(), 0);
			lastModified = pkgInfo.lastUpdateTime;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		long storedLastModified = prefs.getLong(Constants.PREFS_APP_LAST_MODIFIED, 0L);

		Log.i(Constants.LOG_TAG, String.format("Stored Value - %d, New Value - %d", storedLastModified, lastModified));

		if(storedLastModified == 0L || storedLastModified < lastModified) {
			isAppUpdated = true;
			prefs.edit().putLong(Constants.PREFS_APP_LAST_MODIFIED, lastModified).apply();
		}

		return isAppUpdated;
	}
}
