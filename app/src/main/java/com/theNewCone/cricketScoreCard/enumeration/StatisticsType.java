package com.theNewCone.cricketScoreCard.enumeration;

import java.io.Serializable;

public enum StatisticsType implements Serializable {
	HIGHEST_SCORE,
	TOTAL_RUNS,
	HUNDREDS_FIFTIES,
	BOWLING_BEST_FIGURES,
	ECONOMY,
	TOTAL_WICKETS,
	CATCHES,
	STUMPING;

	@Override
	public String toString() {
		switch (this) {
			case HIGHEST_SCORE:
				return "Highest Score";

			case TOTAL_RUNS:
				return "Total Runs";

			case HUNDREDS_FIFTIES:
				return "100s and 50s";

			case BOWLING_BEST_FIGURES:
				return "Best Bowling Figures";

			case TOTAL_WICKETS:
				return "Total Wickets";

			case ECONOMY:
				return "Economy";

			case CATCHES:
				return "Catches";

			case STUMPING:
				return "Stumping, RunOut";
		}

		return "";
	}
}
