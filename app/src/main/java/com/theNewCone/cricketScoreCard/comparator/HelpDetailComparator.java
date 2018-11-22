package com.theNewCone.cricketScoreCard.comparator;

import com.theNewCone.cricketScoreCard.help.HelpDetail;

import java.util.Comparator;

public class HelpDetailComparator implements Comparator<HelpDetail> {

	@Override
	public int compare(HelpDetail helpDetail1, HelpDetail helpDetail2) {
		return (helpDetail1.getOrder() - (helpDetail2.getOrder()));
	}
}
