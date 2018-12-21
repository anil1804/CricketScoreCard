package com.theNewCone.cricketScoreCard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.enumeration.TournamentFormat;
import com.theNewCone.cricketScoreCard.fragment.TournamentHomePointsTableFragment;
import com.theNewCone.cricketScoreCard.fragment.TournamentHomeScheduleFragment;
import com.theNewCone.cricketScoreCard.tournament.Tournament;
import com.theNewCone.cricketScoreCard.utils.TournamentUtils;

import java.util.HashMap;

public class TournamentHomeActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {

	public static final String ARG_TOURNAMENT = "Tournament";

	DrawerLayout drawer;
	ActionBarDrawerToggle toggle;
	NavigationView navigationView;

	Toolbar toolbar;

	Tournament tournament = null;
	HashMap<TournamentTab, Boolean> tabMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tournament);

		if (savedInstanceState != null) {
			tournament = (Tournament) savedInstanceState.getSerializable(ARG_TOURNAMENT);
		}

		toolbar = findViewById(R.id.toolbar);

		SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		ViewPager mViewPager = findViewById(R.id.container);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		TabLayout tabLayout = findViewById(R.id.tabs);

		mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
		tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

		setupDrawer();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (getIntent() != null && getIntent().getExtras() != null) {
			Bundle bundle = getIntent().getExtras();
			tournament = (Tournament) bundle.getSerializable(ARG_TOURNAMENT);

			if (tournament != null && tournament.getTournamentWinner() != null) {
				Intent intent = new Intent(this, TournamentCompleteActivity.class);
				intent.putExtra(TournamentCompleteActivity.ARG_WINNER, tournament.getTournamentWinner().getName());
				startActivity(intent);

				deriveTournamentTabs(tournament.getFormat());

				TournamentUtils utils = new TournamentUtils(this);
				utils.checkTournamentStageComplete(tournament);
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(ARG_TOURNAMENT, tournament);
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
			while (backStackCount > 0) {
				getSupportFragmentManager().popBackStack();
				backStackCount = getSupportFragmentManager().getBackStackEntryCount();
			}
			goHome();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_tournament, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_help:
				showHelp();
				break;

			case R.id.menu_home:
				goHome();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
		switch (menuItem.getItemId()) {
			case R.id.nav_home:
				goHome();
				return true;

			case R.id.nav_help:
				showHelp();
				return true;
		}

		return false;
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

		for (int i = 0; i < navigationView.getMenu().size(); i++) {
			MenuItem item = navigationView.getMenu().getItem(i);
			if (item.isVisible())
				item.setVisible(false);
		}

		navigationView.getMenu().findItem(R.id.nav_home).setVisible(true);
		navigationView.getMenu().findItem(R.id.nav_help).setVisible(true);
	}

	public void updateDrawerEnabled(boolean enabled) {
		int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
		drawer.setDrawerLockMode(lockMode);
		toggle.setDrawerIndicatorEnabled(enabled);
	}

	private void goHome() {
		startActivity(new Intent(this, HomeActivity.class));
	}

	private void showHelp() {
		startActivity(new Intent(this, HelpListActivity.class));
	}

	private void deriveTournamentTabs(TournamentFormat format) {
		tabMap = new HashMap<>();

		tabMap.put(TournamentTab.HOME, true);
		tabMap.put(TournamentTab.SCHEDULE, true);
		tabMap.put(TournamentTab.STATS, true);

		switch (format) {
			case BILATERAL:
				tabMap.put(TournamentTab.POINTS, false);
				break;

			case KNOCK_OUT:
				tabMap.put(TournamentTab.POINTS, false);
				break;

			case ROUND_ROBIN:
				tabMap.put(TournamentTab.POINTS, true);
				break;

			case GROUPS:
				tabMap.put(TournamentTab.POINTS, true);
				break;
		}
	}

	private enum TournamentTab {
		HOME, SCHEDULE, POINTS, STATS
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment frag = null;
			switch (position) {
				case 0:
					frag = TournamentHomeScheduleFragment.newInstance(tournament);
					break;

				case 1:
					frag = TournamentHomePointsTableFragment.newInstance(tournament);
					break;
			}
			return frag;
		}

		@Override
		public int getCount() {
			int count = 0;
			for (Boolean isPresent : tabMap.values())
				if (isPresent)
					count++;

			return count;

		}
	}
}
