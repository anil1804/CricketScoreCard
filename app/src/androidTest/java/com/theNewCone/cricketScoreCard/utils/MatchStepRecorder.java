package com.theNewCone.cricketScoreCard.utils;

import android.content.Context;
import android.util.Log;

import com.theNewCone.cricketScoreCard.Constants;
import com.theNewCone.cricketScoreCard.enumeration.AutoScoreType;
import com.theNewCone.cricketScoreCard.enumeration.DismissalType;
import com.theNewCone.cricketScoreCard.enumeration.ExtraType;
import com.theNewCone.cricketScoreCard.match.Team;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class MatchStepRecorder {
	private final Context context;

	MatchStepRecorder(Context context) {
		this.context = context;
	}

	private MatchStep recordMatchStep(String line, int lineNum) {

		String[] matchStepDetails = line.split(",");
		Log.d(Constants.LOG_TAG,
				String.format("Number of records in line number %d are %d\n",
						lineNum,
						matchStepDetails.length));

		return createNewMatchStep(matchStepDetails);
	}

	private MatchStep createNewMatchStep(String[] stepData) {
		MatchStep matchStep = new MatchStep();

		AutoScoreType scoreType = AutoScoreType.valueOf(stepData[0]);
		switch (scoreType) {
			case BATSMAN:
				String batsman = stepData[11];
				matchStep.selectBatsman(batsman);
				break;

			case FACING:
				String facingBatsman = stepData[12];
				matchStep.selectFacing(facingBatsman);
				break;

			case BOWLER:
				String bowler = null;
				if (stepData.length > 10) {
					bowler = stepData[10];
				}
				matchStep.selectBowler(bowler);
				break;

			case BALL:
				String stringRuns = stepData.length > 1 ? stepData[1] : "0";
				int runs = (stringRuns == null || stringRuns.equals("")) ? 0 : Integer.parseInt(stringRuns);
				boolean isExtra = false, isWicket = false;
				ExtraType extraType = null, extraSubType = null;
				DismissalType wicketType = null;
				String outBatsman = null, fielder = null;
				int extraRuns = 0;

				if (stepData.length > 6 && stepData[6] != null && !stepData[6].equals("")) {
					isExtra = Boolean.parseBoolean(stepData[6].toLowerCase());

					if (isExtra) {
						extraType = ExtraType.valueOf(stepData[7]);
						extraSubType = (stepData.length > 8 && stepData[8] != null && !stepData[8].equals("")) ? ExtraType.valueOf(stepData[8]) : ExtraType.NONE;
						extraRuns = (stepData.length > 9 && stepData[9] != null && !stepData[9].equals("")) ? Integer.parseInt(stepData[9]) : 0;
					}
				}

				if (stepData.length > 2 && stepData[2] != null && !stepData[2].equals("")) {
					isWicket = Boolean.parseBoolean(stepData[2].toLowerCase());

					if (isWicket) {
						wicketType = DismissalType.valueOf(stepData[3]);
						fielder = (stepData.length > 4 && stepData[4] != null && !stepData[4].equals("")) ? stepData[4] : null;
						outBatsman = (stepData.length > 5 && stepData[5] != null && !stepData[5].equals("")) ? stepData[5] : null;
					}
				}

				matchStep.newBallBowled(runs, isExtra, extraType, extraSubType, extraRuns, isWicket, wicketType, outBatsman, fielder);
				break;

			case NEXT_INNINGS:
				matchStep.startNextInnings();
				break;

			case PLAYER_OF_MATCH:
				String pomTeam = (stepData.length > 13 && stepData[13] != null && !stepData[13].equals("")) ? stepData[13] : null;
				String pom = (stepData.length > 14 && stepData[14] != null && !stepData[14].equals("")) ? stepData[14] : null;
				matchStep.recordPOM(pomTeam, pom);
				break;

			case COMPLETE_MATCH:
				matchStep.completeMatch();
				break;

			case PENALTY:
				matchStep.recordPenalty(stepData[7]);
				break;

			case CANCEL:
				matchStep.recordCancellation(Integer.parseInt(stepData[1]));
				break;
		}

		return matchStep;
	}

	List<MatchStep> recordMatchSteps(String templateFile, Team team1, Team team2,
									 String[] team1Players, String[] team2Players) {

		List<MatchStep> matchStepList = new ArrayList<>();

		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(context.getAssets().open(templateFile)));
			List<Integer> breakSteps = new ArrayList<>();
			breakSteps.add(-1);

			String line;
			int lineNum = 1;

			while ((line = br.readLine()) != null) {
				if (breakSteps.contains(lineNum))
					Log.v(Constants.LOG_TAG, "Applying a break");

				if (lineNum == 1) {
					lineNum++;
					continue;
				}

				for (int i = team1Players.length; i > 0; i--) {
					String stringToReplace = "Team1Player" + i;
					line = line.replaceAll(stringToReplace, team1Players[i - 1]);
					stringToReplace = "Team2Player" + i;
					line = line.replaceAll(stringToReplace, team2Players[i - 1]);
				}
				String stringToReplace = "Team1SName";
				line = line.replaceAll(stringToReplace, team1.getShortName());
				stringToReplace = "Team2SName";
				line = line.replaceAll(stringToReplace, team2.getShortName());
				stringToReplace = "Team1FName";
				line = line.replaceAll(stringToReplace, team1.getName());
				stringToReplace = "Team2FName";
				line = line.replaceAll(stringToReplace, team2.getName());


				matchStepList.add(recordMatchStep(line, lineNum));
				lineNum++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return matchStepList;
	}
}
