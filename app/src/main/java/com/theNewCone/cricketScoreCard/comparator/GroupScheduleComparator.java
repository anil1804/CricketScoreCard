package com.theNewCone.cricketScoreCard.comparator;

import com.theNewCone.cricketScoreCard.tournament.Group;

import java.util.Comparator;

public class GroupScheduleComparator implements Comparator<Group> {
	@Override
	public int compare(Group group1, Group group2) {
		/*int compared = 0;

		if(group1.getStage() != group2.getStage()) {
			switch (group1.getStage()) {
				case ROUND_1:
					compared = -1;
					break;

				case ROUND_2:
					switch (group2.getStage()) {
						case ROUND_1:
							compared = 1;
							break;

						default:
							compared = -1;
					}
					break;

				case ROUND_3:
					switch (group2.getStage()) {
						case ROUND_1:
						case ROUND_2:
							compared = 1;
							break;

						default:
							compared = -1;
					}
					break;

				case GROUP:
					compared = -1;
					break;

				case ELIMINATOR_1:
					switch (group2.getStage()) {
						case ROUND_1:
						case ROUND_2:
						case ROUND_3:
						case GROUP:
							compared = 1;
							break;

						default:
							compared = -1;
					}
					break;

				case QUALIFIER:
					switch (group2.getStage()) {
						case ROUND_1:
						case ROUND_2:
						case ROUND_3:
						case GROUP:
						case ELIMINATOR_1:
							compared = 1;
							break;

						default:
							compared = -1;
					}
					break;

				case ELIMINATOR_2:
					switch (group2.getStage()) {
						case ROUND_1:
						case ROUND_2:
						case ROUND_3:
						case GROUP:
						case ELIMINATOR_1:
						case QUALIFIER:
							compared = 1;
							break;

						default:
							compared = -1;
					}
					break;

				case SUPER_FOUR:
				case SUPER_SIX:
					switch (group2.getStage()) {
						case FINAL:
						case SEMI_FINAL:
						case QUARTER_FINAL:
							compared = -1;
							break;

						default:
							compared = 1;
							break;
					}
					break;

				case QUARTER_FINAL:
					switch (group2.getStage()) {
						case FINAL:
						case SEMI_FINAL:
							compared = -1;
							break;

						default:
							compared = 1;
							break;
					}
					break;

				case SEMI_FINAL:
					switch (group2.getStage()) {
						case FINAL:
							compared = -1;
							break;

						default:
							compared = 1;
							break;
					}
					break;

				case FINAL:
					compared = 1;
					break;
			}
		}

		return compared;*/

		int comparisonValue = group1.getStage().compareTo(group2.getStage());

		if (comparisonValue == 0) {
			comparisonValue = group1.getGroupNumber() - group2.getGroupNumber();
		}

		return comparisonValue;
	}
}
