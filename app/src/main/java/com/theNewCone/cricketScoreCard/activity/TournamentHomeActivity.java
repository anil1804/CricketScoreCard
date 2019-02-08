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
import com.theNewCone.cricketScoreCard.fragment.HomeFragment;
import com.theNewCone.cricketScoreCard.fragment.TournamentHomeFragment;
import com.theNewCone.cricketScoreCard.fragment.TournamentHomePointsTableFragment;
import com.theNewCone.cricketScoreCard.fragment.TournamentHomeScheduleFragment;
import com.theNewCone.cricketScoreCard.fragment.TournamentStatsFragment;
import com.theNewCone.cricketScoreCard.tournament.Tournament;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;
import com.theNewCone.cricketScoreCard.utils.TournamentUtils;
import com.theNewCone.cricketScoreCard.utils.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class TournamentHomeActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {

	public static final String ARG_TOURNAMENT_ID = "TournamentID";
	public static final String ARG_TOURNAMENT_FORMAT = "TournamentFormat";

	DrawerLayout drawer;
	ActionBarDrawerToggle toggle;
	NavigationView navigationView;

	Toolbar toolbar;

	Tournament tournament = null;
	int tournamentID = 0;
	List<TournamentTab> visibleTabList;
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tournament);

		if (savedInstanceState != null) {
			tournamentID = savedInstanceState.getInt(ARG_TOURNAMENT_ID);
		}

		TournamentFormat format = null;
		if (getIntent() != null && getIntent().getExtras() != null) {
			Bundle bundle = getIntent().getExtras();
			tournamentID = bundle.getInt(ARG_TOURNAMENT_ID);
			format = (TournamentFormat) bundle.getSerializable(ARG_TOURNAMENT_FORMAT);
		}

		if (format != null)
			deriveTournamentTabs(format);

		toolbar = findViewById(R.id.toolbar);
		SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		mViewPager = findViewById(R.id.container);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		TabLayout tabLayout = findViewById(R.id.tabs);

		/* Setting Tab Visibility based on the Tournament Format */
		TabLayout.Tab tournamentHomeTab = tabLayout.getTabAt(0);
		TabLayout.Tab tournamentScheduleTab = tabLayout.getTabAt(1);
		TabLayout.Tab tournamentPointsTab = tabLayout.getTabAt(2);
		TabLayout.Tab tournamentStatsTab = tabLayout.getTabAt(3);

		if (!visibleTabList.contains(TournamentTab.HOME) && tournamentHomeTab != null)
			tabLayout.removeTab(tournamentHomeTab);
		if (!visibleTabList.contains(TournamentTab.SCHEDULE) && tournamentScheduleTab != null)
			tabLayout.removeTab(tournamentScheduleTab);
		if (!visibleTabList.contains(TournamentTab.POINTS) && tournamentPointsTab != null)
			tabLayout.removeTab(tournamentPointsTab);
		if (!visibleTabList.contains(TournamentTab.STATS) && tournamentStatsTab != null)
			tabLayout.removeTab(tournamentStatsTab);

		mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
		tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

		setupDrawer();
	}

	@Override
	protected void onStart() {
		if (tournamentID > 0) {
			tournament = new DatabaseHandler(this).getTournamentContent(tournamentID);

			TournamentUtils utils = new TournamentUtils(this);
			utils.checkTournamentStageComplete(tournament);
		}

		super.onStart();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(ARG_TOURNAMENT_ID, tournamentID);
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
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
		Intent intent = new Intent(this, HomeActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	private void showHelp() {
		startActivity(new Intent(this, HelpListActivity.class));
	}

	private void deriveTournamentTabs(TournamentFormat format) {
		visibleTabList = new ArrayList<>();

		visibleTabList.add(TournamentTab.HOME);
		visibleTabList.add(TournamentTab.SCHEDULE);

		switch (format) {
			case ROUND_ROBIN:
			case GROUPS:
				visibleTabList.add(TournamentTab.POINTS);
				break;
		}

		visibleTabList.add(TournamentTab.STATS);
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

			TournamentTab tab = visibleTabList.get(position);
			switch (tab) {
				case HOME:
					frag = TournamentHomeFragment.newInstance(tournament);
					break;

				case SCHEDULE:
					frag = TournamentHomeScheduleFragment.newInstance(tournament);
					break;

				case POINTS:
					frag = TournamentHomePointsTableFragment.newInstance(tournament);
					break;

				case STATS:
					frag = TournamentStatsFragment.newInstance(tournament);
					break;
			}

			return frag;
		}

		@Override
		public int getCount() {
			return visibleTabList.size();
		}
	}
}
