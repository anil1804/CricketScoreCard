package com.theNewCone.cricketScoreCard.scorecard;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.theNewCone.cricketScoreCard.enumeration.DismissalType;
import com.theNewCone.cricketScoreCard.player.BatsmanStats;
import com.theNewCone.cricketScoreCard.player.BowlerStats;
import com.theNewCone.cricketScoreCard.player.Player;

import java.io.Serializable;

public class WicketData implements Serializable {
	private BatsmanStats batsman;
	private DismissalType dismissalType;
	private Player effectedBy;
	private BowlerStats bowler;

	public BatsmanStats getBatsman() {
		return batsman;
	}

	public DismissalType getDismissalType() {
		return dismissalType;
	}

	public Player getEffectedBy() {
		return effectedBy;
	}

	public BowlerStats getBowler() {
		return bowler;
	}

	public WicketData(@NonNull BatsmanStats batsman, @NonNull DismissalType dismissalType, @Nullable Player effectedBy, @Nullable BowlerStats bowler) {
		this.batsman = batsman;
		this.dismissalType = dismissalType;
		this.effectedBy = effectedBy;
		this.bowler = bowler;
	}

	public static boolean isBowlersWicket(DismissalType type) {
		boolean isBowlersWicket = true;

		switch (type) {
			case RETIRED:
			case HIT_BALL_TWICE:
			case OBSTRUCTING_FIELD:
			case RUN_OUT:
			case TIMED_OUT:
				isBowlersWicket = false;
		}

		return isBowlersWicket;
	}

}
