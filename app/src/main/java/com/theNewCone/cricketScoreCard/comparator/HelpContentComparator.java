package com.theNewCone.cricketScoreCard.comparator;

import com.theNewCone.cricketScoreCard.help.HelpContent;

import java.util.Comparator;

public class HelpContentComparator implements Comparator<HelpContent> {

	@Override
	public int compare(HelpContent helpDetail1, HelpContent helpDetail2) {
		return (helpDetail1.getContentID() - (helpDetail2.getContentID()));
	}
}
