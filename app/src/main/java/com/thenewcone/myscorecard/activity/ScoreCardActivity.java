package com.thenewcone.myscorecard.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thenewcone.myscorecard.R;
import com.thenewcone.myscorecard.adapter.SCBatsmanAdapter;
import com.thenewcone.myscorecard.adapter.SCBowlerAdapter;
import com.thenewcone.myscorecard.match.CricketCard;
import com.thenewcone.myscorecard.match.Partnership;
import com.thenewcone.myscorecard.match.Team;
import com.thenewcone.myscorecard.player.BatsmanStats;
import com.thenewcone.myscorecard.player.BowlerStats;
import com.thenewcone.myscorecard.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ScoreCardActivity extends AppCompatActivity {

	public static final String ARG_INNINGS_1_CARD = "Innings1_Card";
	public static final String ARG_INNINGS_2_CARD = "Innings2_Card";
	public static final String ARG_FACING_BATSMAN = "Facing_Batsman";
	public static final String ARG_OTHER_BATSMAN = "Other_Batsman";
	public static final String ARG_BOWLER = "Bowler";
	public static final String ARG_TEAM_1 = "Team1";
	public static final String ARG_TEAM_2 = "Team2";
	public static final String ARG_TOSS_WON_BY = "TossWonBy";

	private static CricketCard innings1Card, innings2Card;
	private static BatsmanStats currentFacing, otherBatsman;
	private static BowlerStats bowler;
	private static Team team1, team2;
	private static int tossWonById;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link FragmentPagerAdapter} derivative, which will keep every
	 * loaded fragment in memory. If this becomes too memory intensive, it
	 * may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score_card);

		SectionsPagerAdapter mSectionsPagerAdapter;
		ViewPager mViewPager;

		if(getIntent() != null && getIntent().getExtras() != null) {
			Bundle extras = getIntent().getExtras();

			innings1Card = (CricketCard) extras.getSerializable(ARG_INNINGS_1_CARD);
			innings2Card = (CricketCard) extras.getSerializable(ARG_INNINGS_2_CARD);
			currentFacing = (BatsmanStats) extras.getSerializable(ARG_FACING_BATSMAN);
			otherBatsman = (BatsmanStats) extras.getSerializable(ARG_OTHER_BATSMAN);
			bowler = (BowlerStats) extras.getSerializable(ARG_BOWLER);
			team1 = (Team) extras.getSerializable(ARG_TEAM_1);
			team2 = (Team) extras.getSerializable(ARG_TEAM_2);
			tossWonById = extras.getInt(ARG_TOSS_WON_BY);
		}

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = findViewById(R.id.viewPagerContainer);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		TabLayout tabLayout = findViewById(R.id.tabs);
		tabLayout.getTabAt(0).setText(String.format(getString(R.string.tabInningsText), team1.getShortName()));
		tabLayout.getTabAt(1).setText(String.format(getString(R.string.tabInningsText), team2.getShortName()));

		mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
		tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";
		private int sectionNumber;

		public PlaceholderFragment() {
		}

		/**
		 * Returns a new instance of this fragment for the given section
		 * number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_score_card, container, false);

			updateCardView(rootView);

			return rootView;
		}

		private void updateCardView(View rootView) {
			TextView tvSCExtras = rootView.findViewById(R.id.tvSCExtras);
			TextView tvSCTotal = rootView.findViewById(R.id.tvSCTotal);
			TextView tvSCFallOfWickets = rootView.findViewById(R.id.tvSCFallOfWickets);

			if (getArguments() != null)
				sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER, 0);

			RecyclerView rcvSCBatsmanData = rootView.findViewById(R.id.rcvSCBatsmanData);
			rcvSCBatsmanData.setHasFixedSize(false);
			RecyclerView rcvSCBowlerData = rootView.findViewById(R.id.rcvSCBowlerData);
			rcvSCBowlerData.setHasFixedSize(false);

			List<BatsmanStats> batsmanStatsList = null;
			List<BowlerStats> bowlerStatsList = null;

			CricketCard inningsCard = (sectionNumber == 1)
					? innings1Card
					: (sectionNumber == 2 ? innings2Card : null);

			if (inningsCard != null) {
				HashMap<Integer, BatsmanStats> batsmanMap = inningsCard.getBatsmen();
				if (batsmanMap != null && batsmanMap.size() > 0) {
					batsmanStatsList = new ArrayList<>(batsmanMap.values());
				}

				HashMap<String, BowlerStats> bowlerMap = inningsCard.getBowlerMap();
				if (bowlerMap != null && bowlerMap.size() > 0) {
					bowlerStatsList = new ArrayList<>(bowlerMap.values());
				}

				int legByes = inningsCard.getLegByes(), byes = inningsCard.getByes();
				int wides = inningsCard.getNoBalls(), noBalls = inningsCard.getNoBalls();
				int penalty = inningsCard.getPenalty(), totalExtras = legByes + byes + wides + noBalls + penalty;

				String extras = String.format(Locale.getDefault()
						, "Extras-%d : Lb-%d, B-%d, Wd-%d, N-%d, P-%d"
						, totalExtras, legByes, byes, wides, noBalls, penalty);
				tvSCExtras.setText(extras);
				tvSCTotal.setText(String.format(Locale.getDefault(), "TOTAL : %d", inningsCard.getScore()));

				HashMap<Integer, Partnership> partnershipData = inningsCard.getPartnershipData();
				StringBuilder fowSB = new StringBuilder("FOW : ");
				if (partnershipData != null) {
					for (int key : partnershipData.keySet()) {
						Partnership partnership = partnershipData.get(key);
						if (partnership != null && !partnership.isUnBeaten()) {
							fowSB.append(key);
							fowSB.append("-");
							fowSB.append(partnership.getEndScore());
							fowSB.append(", ");
						}
					}
				}
				fowSB.trimToSize();
				if (fowSB.length() > 6)
					fowSB.delete(fowSB.length() - 2, fowSB.length());

				tvSCFallOfWickets.setText(fowSB.toString().trim());
			}

			if (batsmanStatsList != null && batsmanStatsList.size() > 0 && getContext() != null) {
				rcvSCBatsmanData.setLayoutManager(new LinearLayoutManager(getContext()));
				SCBatsmanAdapter adapter = new SCBatsmanAdapter(getContext(), batsmanStatsList, currentFacing, otherBatsman);
				rcvSCBatsmanData.setAdapter(adapter);

				LinearLayoutManager llm = new LinearLayoutManager(getContext());
				llm.setOrientation(LinearLayoutManager.VERTICAL);
				rcvSCBatsmanData.setLayoutManager(llm);

				rcvSCBatsmanData.setItemAnimator(new DefaultItemAnimator());
			}

			if (bowlerStatsList != null && bowlerStatsList.size() > 0 && getContext() != null) {
				rcvSCBowlerData.setLayoutManager(new LinearLayoutManager(getContext()));
				SCBowlerAdapter adapter = new SCBowlerAdapter(getContext(), bowlerStatsList, bowler);
				rcvSCBowlerData.setAdapter(adapter);

				LinearLayoutManager llm = new LinearLayoutManager(getContext());
				llm.setOrientation(LinearLayoutManager.VERTICAL);
				rcvSCBowlerData.setLayoutManager(llm);

				rcvSCBowlerData.setItemAnimator(new DefaultItemAnimator());
			}
		}
	}

/*
	private static void updateSummaryView(View rootView) {
		TextView tvSCVersus = rootView.findViewById(R.id.tvSCVersus);
		TextView tvSCTossInfo = rootView.findViewById(R.id.tvSCTossInfo);
		TextView tvSCTeam1 = rootView.findViewById(R.id.tvSCTeam1);
		TextView tvSCTeam2 = rootView.findViewById(R.id.tvSCTeam2);

		String tossWonBy = (team1.getId() == tossWonById) ? team1.getName() : team2.getName();
		String electedTo = (team1.getName().equals(tossWonBy)) ? "Bat" : "Field";

		tvSCVersus.setText(String.format(Locale.getDefault(), "%s vs %s", team1.getName(), team2.getName()));
		tvSCTossInfo.setText(String.format(Locale.getDefault(),
				"%s won the toss and elected to %s", tossWonBy, electedTo));

		StringBuilder teamInfoSB = new StringBuilder(team1.getName());
		teamInfoSB.append("Playing team:\n");
		for(Player player : team1.getMatchPlayers()) {
			String playerName = player.getName();
			playerName = (player.getID() == team1.getCaptain().getID()) ? playerName + " (c)" : playerName;
			playerName = (player.getID() == team1.getWicketKeeper().getID()) ? playerName + " (wk)" : playerName;
			teamInfoSB.append(playerName);
			teamInfoSB.append(", ");
		}
		teamInfoSB.delete(teamInfoSB.length()-2, teamInfoSB.length());
		tvSCTeam1.setText(teamInfoSB.toString());

		teamInfoSB = new StringBuilder(team2.getName());
		teamInfoSB.append("Playing team:\n");
		for(Player player : team2.getMatchPlayers()) {
			String playerName = player.getName();
			playerName = (player.getID() == team1.getCaptain().getID()) ? playerName + " (c)" : playerName;
			playerName = (player.getID() == team1.getWicketKeeper().getID()) ? playerName + " (wk)" : playerName;
			teamInfoSB.append(playerName);
			teamInfoSB.append(", ");
		}
		teamInfoSB.delete(teamInfoSB.length()-2, teamInfoSB.length());
		tvSCTeam2.setText(teamInfoSB.toString());
	}
*/

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class below).
			return PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}
	}
}
