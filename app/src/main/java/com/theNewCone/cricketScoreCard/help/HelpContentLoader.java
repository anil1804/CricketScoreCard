package com.theNewCone.cricketScoreCard.help;

import android.content.Context;
import android.util.SparseArray;

import com.theNewCone.cricketScoreCard.comparator.HelpContentComparator;
import com.theNewCone.cricketScoreCard.utils.database.HelpContentDBHandler;

import java.util.Collections;
import java.util.List;

public class HelpContentLoader {
	private SparseArray<HelpContent> helpContentItems = new SparseArray<>();

	public HelpContentLoader(Context context) {
		if(helpContentItems != null && helpContentItems.size() == 0) {
			List<HelpContent> helpContentList = new HelpContentDBHandler(context).getAllHelpContent();
			if (helpContentList != null) {
				Collections.sort(helpContentList, new HelpContentComparator());

				for (HelpContent helpContent : helpContentList)
					helpContentItems.put(helpContent.getContentID(), helpContent);

			}
		}
	}

	public SparseArray<HelpContent> getHelpContentItems() {
		return helpContentItems;
	}
}
