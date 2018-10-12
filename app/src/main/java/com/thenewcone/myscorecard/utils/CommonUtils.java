package com.thenewcone.myscorecard.utils;

import java.text.DecimalFormat;

public class CommonUtils {
	public static final String LOG_TAG = "MyScoreCard";

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

	private static int oversToBalls(double overs) {
		int ballsBowled;

		String[] oversArr = String.valueOf(overs).split("\\.");
		ballsBowled = Integer.parseInt(oversArr[0]) * 6;
		if(oversArr.length > 1)
			ballsBowled += Integer.parseInt(oversArr[1]);

		return ballsBowled;
	}

	private static double ballsToOvers(int balls) {
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
}
