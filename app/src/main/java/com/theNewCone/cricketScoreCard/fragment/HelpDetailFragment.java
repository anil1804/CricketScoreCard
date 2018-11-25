package com.theNewCone.cricketScoreCard.fragment;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.resources.TextAppearance;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.comparator.HelpDetailComparator;
import com.theNewCone.cricketScoreCard.custom.VerticalImageSpan;
import com.theNewCone.cricketScoreCard.help.HelpContent;
import com.theNewCone.cricketScoreCard.help.HelpDetail;

import java.util.Collections;
import java.util.List;

public class HelpDetailFragment extends Fragment {

	public static final String ARG_ITEM = "item";
	private HelpContent mItem;

	private String helpText;

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
		if(savedInstanceState != null) {
			mItem = (HelpContent) savedInstanceState.getSerializable(ARG_ITEM);
		}

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

		if (mItem != null) {
			writeHelpDetails(rootView, mItem);
		}

		return rootView;
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		if(mItem != null) {
			outState.putSerializable(ARG_ITEM, mItem);
		}
	}

	private void writeHelpDetails(View view, HelpContent helpContent) {
		LinearLayout layoutHelpDetail = view.findViewById(R.id.layoutHelpDetail);

		if(helpContent != null && helpContent.getHelpDetailList() != null) {
			List<HelpDetail> helpDetailList = helpContent.getHelpDetailList();
			Collections.sort(helpDetailList, new HelpDetailComparator());

			for(HelpDetail helpDetail : helpContent.getHelpDetailList()) {
				switch (helpDetail.getViewType()) {
					case TEXT:
						TextView textView = new TextView(new ContextThemeWrapper(getContext(), R.style.TextViewStyle_Medium_MorePadding));
						textView.setId(View.generateViewId());
						helpText = helpDetail.getText();
						SpannableStringBuilder ssBuilder = new SpannableStringBuilder(helpText);

						List<Integer> imageSourceIDList = helpDetail.getSourceIDList();
						ssBuilder = insertImages(ssBuilder, imageSourceIDList);
						ssBuilder = formatText(ssBuilder);

						textView.setLineSpacing(20, 1);
						textView.setText(ssBuilder);

						layoutHelpDetail.addView(textView);
						break;

					case SEPARATOR:
						View separator = new View(getContext());
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3);
						params.setMargins(0, 20, 0, 30);
						separator.setLayoutParams(params);
						separator.setBackgroundColor(getResources().getColor(R.color.lineSeparator));
						layoutHelpDetail.addView(separator);
						break;
				}
			}
		}
	}

	private SpannableStringBuilder insertImages(SpannableStringBuilder ssBuilder, List<Integer> imageSourceIDList) {
		if(imageSourceIDList != null) {
			if(getContext() != null) {
				int i=0, searchFromIndex = 0;
				String searchString = "[I]";
				boolean continueScan = helpText.contains(searchString);
				while(continueScan) {
					if(imageSourceIDList.size() > i) {
						ImageSpan imageSpan = new ImageSpan(getContext(), imageSourceIDList.get(i++));
						VerticalImageSpan vImageSpan = new VerticalImageSpan(imageSpan.getDrawable());
						int start = helpText.indexOf(searchString, searchFromIndex);
						int end = start + searchString.length();
						ssBuilder.setSpan(vImageSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

						searchFromIndex = end;
						continueScan = helpText.indexOf(searchString, searchFromIndex) > 0;
					} else {
						continueScan = false;
					}
				}
			}
		}

		return ssBuilder;
	}

	private SpannableStringBuilder formatText(SpannableStringBuilder ssBuilder) {
		for(HelpDetail.FormattingType type : HelpDetail.FormattingType.values()) {
			ssBuilder = formatText(ssBuilder, type);
		}

		return ssBuilder;
	}

	private SpannableStringBuilder formatText(SpannableStringBuilder ssBuilder, HelpDetail.FormattingType type) {
		String searchStartString = "NONE";
		String searchEndString = "NONE";

		switch (type) {
			case BOLD:
				searchStartString = "<b>";
				searchEndString = "</b>";
				break;

			case ITALIC:
				searchStartString = "<i>";
				searchEndString = "</i>";
				break;

			case UNDERLINE:
				searchStartString = "<u>";
				searchEndString = "</u>";
				break;

			case SECTION_HEADER:
				searchStartString = "<sh>";
				searchEndString = "</sh>";
				break;

			case HIGHLIGHT:
				searchStartString = "<hi>";
				searchEndString = "</hi>";
				break;

			case HEADER:
				searchStartString = "<h>";
				searchEndString = "</h>";
				break;
		}

		boolean continueFormatting = helpText.contains(searchStartString);

		while(continueFormatting) {
			int start = helpText.indexOf(searchStartString);
			ssBuilder.replace(start, start + searchStartString.length(), "");
			helpText = helpText.replaceFirst(searchStartString, "");

			int end = helpText.indexOf(searchEndString);
			ssBuilder.replace(end, end + searchEndString.length(), "");
			helpText = helpText.replaceFirst(searchEndString, "");

			switch (type) {
				case BOLD:
					ssBuilder.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					break;

				case ITALIC:
					ssBuilder.setSpan(new StyleSpan(Typeface.ITALIC), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					break;

				case UNDERLINE:
					ssBuilder.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					break;

				case SECTION_HEADER:
					ssBuilder.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					ssBuilder.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					ssBuilder.setSpan(new RelativeSizeSpan(1.1f), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					break;

				case HIGHLIGHT:
					ssBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue_500)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					break;

				case HEADER:
					ssBuilder.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					ssBuilder.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					ssBuilder.setSpan(new RelativeSizeSpan(1.5f), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					ssBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue_900)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					ssBuilder.replace(start, end, ssBuilder.subSequence(start, end).toString().toUpperCase());
					break;
			}

			continueFormatting = helpText.contains(searchStartString);
		}

		return ssBuilder;
	}
}
