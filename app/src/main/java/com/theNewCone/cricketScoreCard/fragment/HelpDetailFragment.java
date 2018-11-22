package com.theNewCone.cricketScoreCard.fragment;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.activity.HelpDetailActivity;
import com.theNewCone.cricketScoreCard.activity.HelpListActivity;
import com.theNewCone.cricketScoreCard.comparator.HelpDetailComparator;
import com.theNewCone.cricketScoreCard.help.HelpContent;
import com.theNewCone.cricketScoreCard.help.HelpDetail;

import java.util.Collections;
import java.util.List;

/**
 * A fragment representing a single Question detail screen.
 * This fragment is either contained in a {@link HelpListActivity}
 * in two-pane mode (on tablets) or a {@link HelpDetailActivity}
 * on handsets.
 */
public class HelpDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM = "item";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private HelpContent mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public HelpDetailFragment() {
	}

	public static HelpDetailFragment newInstance(HelpContent helpContent) {
		HelpDetailFragment fragment = new HelpDetailFragment();
		fragment.mItem = helpContent;

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (mItem != null) {

			Activity activity = this.getActivity();
			if(activity != null) {
				CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
				if (appBarLayout != null) {
					appBarLayout.setTitle(mItem.getContent());
				}
			}
		}
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.help_detail, container, false);

		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			writeHelpDetails(rootView, mItem);
//			((TextView) rootView.findViewById(R.id.question_detail)).setText(mItem.details);
		}

		return rootView;
	}

	private void writeHelpDetails(View view, HelpContent helpContent) {
		LinearLayout llHelpDetail = view.findViewById(R.id.llHelpDetail);

		if(helpContent != null && helpContent.getHelpDetailList() != null) {
			List<HelpDetail> helpDetailList = helpContent.getHelpDetailList();
			Collections.sort(helpDetailList, new HelpDetailComparator());

			for(HelpDetail helpDetail : helpContent.getHelpDetailList()) {
				switch (helpDetail.getViewType()) {
					case TEXT:
						TextView newTextView = new TextView(new ContextThemeWrapper(getContext(), R.style.TextViewStyle_Medium_MorePadding));
						newTextView.setId(View.generateViewId());
						newTextView.setText(helpDetail.getText());
						llHelpDetail.addView(newTextView);
						break;

					case IMAGE:
						ImageView newImageView = new ImageView(new ContextThemeWrapper(getContext(), R.style.ImageButtonStyle));
						newImageView.setId(View.generateViewId());
						newImageView.setImageResource(helpDetail.getSourceID());
						llHelpDetail.addView(newImageView);
						break;
				}
			}
		}
	}
}
