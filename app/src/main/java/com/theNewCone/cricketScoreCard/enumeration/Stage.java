package com.theNewCone.cricketScoreCard.enumeration;

import android.support.annotation.NonNull;

import java.io.Serializable;

public enum Stage implements Serializable {
	GROUP, ROUND_ROBIN,
	ROUND_1, ROUND_2, ROUND_3,
	SUPER_FOUR, SUPER_SIX,
	QUALIFIER, ELIMINATOR_1, ELIMINATOR_2,
	QUARTER_FINAL, SEMI_FINAL, FINAL,
	NONE;

	@NonNull
	@Override
	public String toString() {
		String value = "None";
		switch (this) {
			case GROUP:
				value = "Group Stage";
				break;

			case ROUND_ROBIN:
				value = "Round-Robin";
				break;

			case ROUND_1:
				value = "Round-1";
				break;

			case ROUND_2:
				value = "Round-2";
				break;

			case ROUND_3:
				value = "Round-3";
				break;

			case SUPER_FOUR:
			case SUPER_SIX:
				value = "Supers";
				break;

			case QUALIFIER:
				value = "Qualifier-1";
				break;

			case ELIMINATOR_1:
				value = "Eliminator-1";
				break;

			case ELIMINATOR_2:
				value = "Eliminator-2";
				break;

			case QUARTER_FINAL:
				value = "Quarter-Finals";
				break;

			case SEMI_FINAL:
				value = "Semi-Finals";
				break;

			case FINAL:
				value = "Final";
				break;

			case NONE:
				value = "Final";
				break;
		}

		return value;
	}
}
