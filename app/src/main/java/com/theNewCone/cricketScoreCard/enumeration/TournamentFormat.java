package com.theNewCone.cricketScoreCard.enumeration;

import java.io.Serializable;

public enum TournamentFormat implements Serializable {
	ROUND_ROBIN, GROUPS, KNOCK_OUT, BILATERAL;

	public String stringValue() {
		switch (this) {
			case ROUND_ROBIN:
				return "Round-Robin";

			case GROUPS:
				return "Groups";

			case KNOCK_OUT:
				return "Knock-Out";

			case BILATERAL:
				return "Bilateral";
		}

		return null;
	}
}
