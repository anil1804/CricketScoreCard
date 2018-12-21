package com.theNewCone.cricketScoreCard.comparator;

import com.theNewCone.cricketScoreCard.tournament.PointsData;

import java.util.Comparator;

public class PointsDataComparator implements Comparator<PointsData> {
	@Override
	public int compare(PointsData pointsData1, PointsData pointsData2) {
		int comparison = pointsData1.getPoints() - pointsData2.getPoints();
		if (comparison == 0) {
			double rrComparison = pointsData1.getNetRunRate() - pointsData2.getNetRunRate();
			if (rrComparison == 0) {
				comparison = pointsData1.getTotalWicketsLost() - pointsData2.getTotalWicketsLost();
				if (comparison == 0) {
					comparison = pointsData1.getTotalWicketsTaken() - pointsData2.getTotalWicketsTaken();
				}
			} else {
				comparison = rrComparison > 0 ? 1 : -1;
			}
		}

		return comparison * -1;
	}
}
