package com.thenewcone.myscorecard.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thenewcone.myscorecard.match.CricketCardUtils;
import com.thenewcone.myscorecard.match.Match;
import com.thenewcone.myscorecard.match.MatchState;
import com.thenewcone.myscorecard.match.Team;
import com.thenewcone.myscorecard.player.BatsmanStats;
import com.thenewcone.myscorecard.player.BowlerStats;
import com.thenewcone.myscorecard.player.Player;
import com.thenewcone.myscorecard.scorecard.Extra;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommonUtils {
	public static final String LOG_TAG = "MyScoreCard";

	public static final String BATTING_TEAM = "Batting Team";
	public static final String BOWLING_TEAM = "Bowling Team";
	public static final String ARG_EXTRA_TYPE = "Extra Type";

	public static final String DEF_DATE_FORMAT = "yyyyMMdd_HHmmss";

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

		if(maxOvers > 0 && target > 0) {
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

    public static Team[] objectArrToTeamArr(Object[] objArr) {
        Team[] teams = null;

        if(objArr != null) {
            teams = new Team[objArr.length];

            int i=0;
            for(Object obj : objArr) {
                if(obj instanceof Team) {
                    teams[i] = (Team) obj;
                    i++;
                }
            }
        }

        return teams;
    }

    public static MatchState[] objectArrToMatchStateArr(Object[] objArr) {
		MatchState[] savedMatches = null;

        if(objArr != null) {
            savedMatches = new MatchState[objArr.length];

            int i=0;
            for(Object obj : objArr) {
                if(obj instanceof MatchState) {
                    savedMatches[i] = (MatchState) obj;
                    i++;
                }
            }
        }

        return savedMatches;
    }

    public static String convertToJSON(CricketCardUtils ccUtilsObj) {
		if(ccUtilsObj != null) {
			Gson gson = new Gson();
			Type type = new TypeToken<CricketCardUtils>() {
			}.getType();

			String jsonString = gson.toJson(ccUtilsObj, type);

			Log.i("JSON", jsonString);

			return jsonString;
		}

		return null;
    }

    public static CricketCardUtils convertToCCUtils(String jsonString) {
		CricketCardUtils ccUtils = null;
		if(jsonString != null) {
			Gson gson = new Gson();
			Type type = new TypeToken<CricketCardUtils>(){}.getType();

			ccUtils = gson.fromJson(jsonString, type);
		}

		return ccUtils;
    }

    public static String currTimestamp() {
        return currTimestamp(DEF_DATE_FORMAT);
    }

    public static String currTimestamp(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());

        return sdf.format(date);
    }

    public static List<Integer> jsonToIntList(String jsonString)  {
		if(jsonString != null) {
			Gson gson = new Gson();
			Type type = new TypeToken<List<Integer>>() {
			}.getType();

			return gson.fromJson(jsonString, type);
		}

		return null;
	}

	public static String intListToJSON(List<Integer> intList) {
		if(intList != null) {
			Gson gson = new Gson();
			Type type = new TypeToken<List<Integer>>() {
			}.getType();

			String jsonString = gson.toJson(intList, type);

			Log.i("JSON", jsonString);

			return jsonString;
		}

		return null;
	}

	public static String[] listToArray(List<String> theList) {
		String[] stringArr = null;

		if(theList != null && theList.size() > 0) {
			stringArr = new String[theList.size()];

			int i=0;
			for(String theStr : theList)
				stringArr[i++] = theStr;
		}

		return stringArr;
	}

	public static String getBatsmanOutData(BatsmanStats batsmanStats) {
		String outData = "Not Out";
		Player fielder = batsmanStats.getWicketEffectedBy();
		BowlerStats bowler = batsmanStats.getWicketTakenBy();

		if(batsmanStats.getDismissalType() != null) {
			switch (batsmanStats.getDismissalType()) {
				case BOWLED:
					outData = "b " + bowler.getBowlerName();
					break;

				case CAUGHT:
					outData = "c " + ((fielder.getID() == bowler.getPlayer().getID()) ? "&" : fielder.getName())
							+ " b " + bowler.getBowlerName();
					break;

				case HIT_BALL_TWICE:
					outData = "(Hit Ball Twice)";
					break;

				case HIT_WICKET:
					outData = "(Hit Wicket)";
					break;

				case LBW:
					outData = "lbw b " + bowler.getBowlerName();
					break;

				case OBSTRUCTING_FIELD:
					outData = "(Obstructing Field)";
					break;

				case RETIRED:
					outData = "(Retired)";
					break;

				case RUN_OUT:
					outData = "Runout (" + fielder.getName() + ")";
					break;

				case STUMPED:
					outData = "st " + fielder.getName() + " b " + bowler.getBowlerName();
					break;

				case TIMED_OUT:
					outData = "(Timed Out)";
					break;
			}
		}

		return outData;
	}
}
