package com.thenewcone.myscorecard.utils;

import com.thenewcone.myscorecard.player.BatsmanStats;
import com.thenewcone.myscorecard.player.BowlerStats;
import com.thenewcone.myscorecard.player.Player;
import com.thenewcone.myscorecard.scorecard.Extra;

import java.text.DecimalFormat;

public class CommonUtils {
	public static final String LOG_TAG = "MyScoreCard";

	public static final String BATTING_TEAM = "Batting Team";
	public static final String BOWLING_TEAM = "Bowling Team";
	public static final String ARG_EXTRA_TYPE = "Extra Type";

	public static double calcRunRate(int score, double overs) {
		double runRate = 0.00;

		if(score > 0 && overs > 0) {

			int balls = oversToBalls(overs);

			double actualOvers = balls / 6 + ((double)(balls % 6))/6;

			runRate = score / actualOvers;
		}

		return runRate;
	}

	public static double calReqRate(int score, double overs, int target, double maxOvers) {

		int reqRuns = target - score;
		double oversRem = 0.00;

		if(maxOvers > 0 && score > 0) {
			int ballsBowled = oversToBalls(overs);

			int maxBalls = oversToBalls(maxOvers);

			oversRem = ballsToOvers(maxBalls - ballsBowled);
		}

		return (oversRem > 0) ? calcRunRate(reqRuns, oversRem) : oversRem;
	}

    public static int oversToBalls(double overs) {
		int ballsBowled;

		String[] oversArr = String.valueOf(overs).split("\\.");
		ballsBowled = Integer.parseInt(oversArr[0]) * 6;
		if(oversArr.length > 1)
			ballsBowled += Integer.parseInt(oversArr[1]);

		return ballsBowled;
	}

    public static double ballsToOvers(int balls) {
		return Double.parseDouble((balls / 6) + "." + balls % 6);
	}

	public static double getStrikeRate(int runs, int balls) {
		double strikeRate = 0.00;
		if(runs > 0 && balls > 0) {
			strikeRate = (double) runs * 100 / ((double) balls);
			DecimalFormat rrDF = new DecimalFormat("#.##");

			strikeRate = Double.parseDouble(rrDF.format(strikeRate));
		}

		return strikeRate;
	}

	public static String doubleToString(double number, String format) {
		format = (format == null) ? "#.##" : format;
		DecimalFormat df = new DecimalFormat(format);

		return df.format(number);
	}

	public static String[] getExtraDetailsArray(Extra.ExtraType extraType, Extra.ExtraType extraSubType) {
		String[] extraRunsArray = null;

		switch (extraType) {
			case WIDE:
				extraRunsArray = new String[]{"0", "1", "2", "3", "4"};
				break;
			case NO_BALL:
				switch (extraSubType) {
					case NONE:
						extraRunsArray = new String[]{"0", "1", "2", "3", "4", "6"};
						break;
					case BYE:
						extraRunsArray = new String[]{"1", "2", "3", "4"};
						break;
					case LEG_BYE:
						extraRunsArray = new String[]{"1", "2", "3", "4", "6"};
						break;
				}
				break;
			case PENALTY:
				extraRunsArray = new String[]{BATTING_TEAM, BOWLING_TEAM};
				break;
			case BYE:
				extraRunsArray = new String[]{"1", "2", "3", "4", "6"};
				break;
			case LEG_BYE:
				extraRunsArray = new String[]{"1", "2", "3", "4"};
				break;
		}

		return extraRunsArray;

	}

    public static Player[] objectArrToPlayerArr(Object[] objArr) {
        Player[] players = null;

        if(objArr != null) {
            players = new Player[objArr.length];

            int i=0;
            for(Object obj : objArr) {
                if(obj instanceof Player) {
                    players[i] = (Player) obj;
                    i++;
                }
            }
        }

        return players;
    }

    public static BatsmanStats[] objectArrToBatsmanArr(Object[] objArr) {
        BatsmanStats[] players = null;

        if(objArr != null) {
            players = new BatsmanStats[objArr.length];

            int i=0;
            for(Object obj : objArr) {
                if(obj instanceof BatsmanStats) {
                    players[i] = (BatsmanStats) obj;
                    i++;
                }
            }
        }

        return players;
    }

    public static BowlerStats[] objectArrToBowlerArr(Object[] objArr) {
        BowlerStats[] players = null;

        if(objArr != null) {
            players = new BowlerStats[objArr.length];

            int i=0;
            for(Object obj : objArr) {
                if(obj instanceof BowlerStats) {
                    players[i] = (BowlerStats) obj;
                    i++;
                }
            }
        }

        return players;
    }
}
