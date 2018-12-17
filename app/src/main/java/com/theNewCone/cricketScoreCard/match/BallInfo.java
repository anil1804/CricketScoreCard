package com.theNewCone.cricketScoreCard.match;

import com.theNewCone.cricketScoreCard.enumeration.ExtraType;
import com.theNewCone.cricketScoreCard.player.Player;
import com.theNewCone.cricketScoreCard.scorecard.Extra;
import com.theNewCone.cricketScoreCard.scorecard.WicketData;

import java.io.Serializable;

public class BallInfo implements Serializable {
	private int ballNumber, runsScored;
	private Extra extra;
	private WicketData wicketData;
	private Player bowler;

	BallInfo(int ballNumber, int runsScored, Extra extra, WicketData wicketData, Player bowler) {
		this.ballNumber = ballNumber;
		this.runsScored = runsScored;
		this.extra = extra;
		this.wicketData = wicketData;
		this.bowler = bowler;
	}

	public int getBallNumber() {
		return ballNumber;
	}

	public int getRunsScored() {
		return runsScored;
	}

	int getAllRunsScored() {
		int allRuns = runsScored;
		if (extra != null && (extra.getType() == ExtraType.BYE || extra.getType() == ExtraType.LEG_BYE))
			allRuns += extra.getRuns();

		return allRuns;
	}

	public Extra getExtra() {
		return extra;
	}

	public WicketData getWicketData() {
		return wicketData;
	}

	public Player getBowler() {
		return bowler;
	}
}
