package com.theNewCone.cricketScoreCard.enumeration;

import java.io.Serializable;

public enum TournamentStageType implements Serializable {
	SUPER_FOUR, SUPER_SIX, KNOCK_OUT, QUALIFIER, NONE, ROUND_ROBIN;

	public String stringValue() {
		switch (this) {
			case SUPER_FOUR:
				return "Super-Four";

			case SUPER_SIX:
				return "Super Six";

			case KNOCK_OUT:
				return "Knock-Out";

			case QUALIFIER:
				return "Qualifier-Eliminator";

			case ROUND_ROBIN:
				return "Round-Robin";
		}

		return "None";
	}
}
