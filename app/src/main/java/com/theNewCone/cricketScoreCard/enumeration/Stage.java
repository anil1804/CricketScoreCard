package com.theNewCone.cricketScoreCard.enumeration;

import java.io.Serializable;

public enum Stage implements Serializable {
	GROUP, ROUND_ROBIN,
	ROUND_1, ROUND_2, ROUND_3,
	SUPER_FOUR, SUPER_SIX,
	QUALIFIER, ELIMINATOR_1, ELIMINATOR_2,
	QUARTER_FINAL, SEMI_FINAL, FINAL,
	NONE;

	public String enumString() {
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

	public String getTag() {
		String tag = "None";
		switch (this) {
			case GROUP:
				tag = "GS";
				break;

			case ROUND_ROBIN:
				tag = "RR";
				break;

			case ROUND_1:
				tag = "R1";
				break;

			case ROUND_2:
				tag = "R2";
				break;

			case ROUND_3:
				tag = "R3";
				break;

			case SUPER_FOUR:
				tag = "S4";
				break;

			case SUPER_SIX:
				tag = "S6";
				break;

			case QUALIFIER:
				tag = "Q1";
				break;

			case ELIMINATOR_1:
				tag = "E1";
				break;

			case ELIMINATOR_2:
				tag = "E2";
				break;

			case QUARTER_FINAL:
				tag = "QF";
				break;

			case SEMI_FINAL:
				tag = "SF";
				break;

			case FINAL:
				tag = "Final";
				break;
		}

		return tag;
	}
}
