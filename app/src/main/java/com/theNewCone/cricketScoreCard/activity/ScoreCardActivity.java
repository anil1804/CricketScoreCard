package com.theNewCone.cricketScoreCard.activity;

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

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.adapter.SCBatsmanAdapter;
import com.theNewCone.cricketScoreCard.adapter.SCBowlerAdapter;
import com.theNewCone.cricketScoreCard.match.CricketCard;
import com.theNewCone.cricketScoreCard.match.CricketCardUtils;
import com.theNewCone.cricketScoreCard.match.Partnership;
import com.theNewCone.cricketScoreCard.match.Team;
import com.theNewCone.cricketScoreCard.player.BatsmanStats;
import com.theNewCone.cricketScoreCard.player.BowlerStats;
import com.theNewCone.cricketScoreCard.utils.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ScoreCardActivity extends AppCompatActivity {

	public static final String ARG_CRICKET_CARD_UTILS = "CricketCardUtils";

	private static CricketCard innings1Card, innings2Card;
	private static BatsmanStats currentFacing, otherBatsman;
	private static BowlerStats bowler;
	private static Team team1, team2;

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

		CricketCardUtils ccUtils = null;
		if(getIntent() != null && getIntent().getExtras() != null) {
			Bundle extras = getIntent().getExtras();

			ccUtils = CommonUtils.convertToCCUtils(extras.getString(ARG_CRICKET_CARD_UTILS));

			if(ccUtils != null) {
				innings1Card = ccUtils.getCard().getInnings() == 2 ? ccUtils.getPrevInningsCard() : ccUtils.getCard();
				innings2Card = ccUtils.getCard().getInnings() == 2 ? ccUtils.getCard() : null;
				currentFacing = ccUtils.getCurrentFacing();
				otherBatsman = ccUtils.getOtherBatsman();
				bowler = ccUtils.getBowler();
				team1 = ccUtils.getTeam1();
				team2 = ccUtils.getTeam2();
			}
		}

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = findViewById(R.id.viewPagerContainer);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		TabLayout tabLayout = findViewById(R.id.tabs);
		tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.lime_50));
		tabLayout.setTabTextColors(getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.lime_A100));

		TabLayout.Tab team1Tab = tabLayout.getTabAt(0);
		TabLayout.Tab team2Tab = tabLayout.getTabAt(1);

		if(team1Tab != null) {
			team1Tab.setText(String.format(getString(R.string.tabInningsText), team1.getShortName()));
		}
		if(team2Tab != null) {
			team2Tab.setText(String.format(getString(R.string.tabInningsText), team2.getShortName()));
			if(innings2Card == null) {
				tabLayout.removeTab(team2Tab);

			} else if(ccUtils != null && ccUtils.getResult() == null) {
				mViewPager.setCurrentItem(1);
			}
		}

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
			rcvSCBatsmanData.setHasFixedSize(true);
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
				int wides = inningsCard.getWides(), noBalls = inningsCard.getNoBalls();
				int penalty = inningsCard.getPenalty(), totalExtras = legByes + byes + wides + noBalls + penalty;

				String extras = String.format(Locale.getDefault()
						, "Extras - %d (Lb-%d, B-%d, Wd-%d, N-%d, P-%d)"
						, totalExtras, legByes, byes, wides, noBalls, penalty);
				tvSCExtras.setText(extras);
				tvSCTotal.setText(String.format(Locale.getDefault(), "TOTAL : %d", inningsCard.getScore()));

				List<Partnership> partnershipData = inningsCard.getPartnershipData();
				StringBuilder fowSB = new StringBuilder("FOW : ");
				int i=1;
				if (partnershipData != null) {
					for (Partnership partnership : partnershipData) {
						if (!partnership.isUnBeaten()) {
							fowSB.append(i++);
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
				rcvSCBowlerData.setMinimumHeight(500);

				LinearLayoutManager llm = new LinearLayoutManager(getContext());
				llm.setOrientation(LinearLayoutManager.VERTICAL);
				rcvSCBowlerData.setLayoutManager(llm);

				rcvSCBowlerData.setItemAnimator(new DefaultItemAnimator());
			}
		}
	}

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
