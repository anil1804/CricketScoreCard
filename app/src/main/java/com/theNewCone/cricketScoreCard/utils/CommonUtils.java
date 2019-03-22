package com.theNewCone.cricketScoreCard.utils;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jjoe64.graphview.series.DataPoint;
import com.theNewCone.cricketScoreCard.Constants;
import com.theNewCone.cricketScoreCard.enumeration.ExtraType;
import com.theNewCone.cricketScoreCard.help.HelpContent;
import com.theNewCone.cricketScoreCard.match.CricketCardUtils;
import com.theNewCone.cricketScoreCard.match.Match;
import com.theNewCone.cricketScoreCard.match.MatchState;
import com.theNewCone.cricketScoreCard.match.Team;
import com.theNewCone.cricketScoreCard.player.BatsmanStats;
import com.theNewCone.cricketScoreCard.player.BowlerStats;
import com.theNewCone.cricketScoreCard.player.Player;
import com.theNewCone.cricketScoreCard.tournament.Group;
import com.theNewCone.cricketScoreCard.tournament.Tournament;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class CommonUtils {

	public static double calcRunRate(int score, double overs) {
		double runRate = 0.00;

		if(score > 0 && overs > 0) {

			int balls = oversToBalls(overs);

			double actualOvers = ((double) balls / 6) + ((double) (balls % 6)) / 6;

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

	public static String[] getExtraDetailsArray(ExtraType extraType, ExtraType extraSubType) {
		String[] extraRunsArray = null;

		switch (extraType) {
			case WIDE:
				extraRunsArray = new String[]{"0", "1", "2", "3", "4", "Other"};
				break;
			case NO_BALL:
				switch (extraSubType) {
					case NONE:
						extraRunsArray = new String[]{"0", "1", "2", "3", "4", "6", "Other"};
						break;
					case BYE:
					case LEG_BYE:
						extraRunsArray = new String[]{"1", "2", "3", "4", "6", "Other"};
						break;
				}
				break;
			case PENALTY:
				extraRunsArray = new String[]{Constants.BATTING_TEAM, Constants.BOWLING_TEAM};
				break;
			case BYE:
			case LEG_BYE:
				extraRunsArray = new String[]{"1", "2", "3", "4", "6", "Other"};
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

    public static Match[] objectArrToMatchArr(Object[] objArr) {
		Match[] matches = null;

        if(objArr != null) {
            matches = new Match[objArr.length];

            int i=0;
            for(Object obj : objArr) {
                if(obj instanceof Match) {
                    matches[i] = (Match) obj;
                    i++;
                }
            }
        }

        return matches;
    }

    public static HelpContent[] objectArrToHelpContentArr(Object[] objArr) {
		HelpContent[] helpContentData = null;

        if(objArr != null) {
			helpContentData = new HelpContent[objArr.length];

            int i=0;
            for(Object obj : objArr) {
                if(obj instanceof HelpContent) {
					helpContentData[i] = (HelpContent) obj;
                    i++;
                }
            }
        }

        return helpContentData;
    }

    public static DataPoint[] objectArrToDataPointArr(Object[] objArr) {
		DataPoint[] dataPoints = null;

        if(objArr != null) {
			dataPoints = new DataPoint[objArr.length];

            int i=0;
            for(Object obj : objArr) {
                if(obj instanceof DataPoint) {
					dataPoints[i] = (DataPoint) obj;
                    i++;
                }
            }
        }

        return dataPoints;
    }

	public static Tournament[] objectArrToTournamentArr(Object[] objArr) {
		Tournament[] tournaments = null;

		if (objArr != null) {
			tournaments = new Tournament[objArr.length];

			int i = 0;
			for (Object obj : objArr) {
				if (obj instanceof Tournament) {
					tournaments[i] = (Tournament) obj;
					i++;
				}
			}
		}

		return tournaments;
	}

    public static String convertToJSON(CricketCardUtils ccUtilsObj) {
		if(ccUtilsObj != null) {
			Gson gson = new Gson();
			Type type = new TypeToken<CricketCardUtils>() {
			}.getType();

			return gson.toJson(ccUtilsObj, type);
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
        return currTimestamp(Constants.DEF_DATE_FORMAT);
    }

    public static String currTimestamp(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());

        return sdf.format(date);
    }

    public static String dateToString(Date theDate) {
		return dateToString(theDate, null);
	}

    public static String dateToString(Date theDate, String format) {
		String stringDate = null;

		if(theDate != null) {
			format = format != null ? format : Constants.DEF_DATE_FORMAT;
			SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
			stringDate = sdf.format(theDate);
		}

		return stringDate;
	}

	public static Date stringToDate(String stringDate) {
		return stringToDate(stringDate, null);
	}

    private static Date stringToDate(String stringDate, String format) {
		Date theDate = null;

		if(stringDate != null) {
			format = format != null ? format : Constants.DEF_DATE_FORMAT;
			SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
			try {
				theDate = sdf.parse(stringDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return theDate;
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

			return gson.toJson(intList, type);
		}

		return null;
	}

	public static String listToString(List<String> theList, String separator) {
		StringBuilder builder = new StringBuilder();

		if(theList != null) {

			for(String theString : theList) {
				builder.append(theString);
				builder.append(separator);
			}

			builder.trimToSize();
			if(builder.length() >= 1) {
				builder.delete(builder.length() - 1, builder.length());
			}
			builder.trimToSize();
		}

		return builder.toString();
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

				case RETIRED_HURT:
					outData = "(Retired Hurt)";
					break;
			}
		}

		return outData;
	}


	public static int getScreenWidth(@NonNull Activity activity) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		return displayMetrics.widthPixels;
	}

	public static int dpToPx(final Context context, final int dp) {
		return dp * Math.round(context.getResources().getDisplayMetrics().density);
	}

	public static int updateNumPlayers(int maxWickets) {
		return maxWickets + 1;
	}

	public static int updateMaxPerBowler(int maxOvers, int numPlayers) {
		int maxPerBowler = 0;
		if (maxOvers > 0 && numPlayers > 0) {
			/*
			- Divide among 5 if bowlers if players more than 5
			- If players less than or equal to 5, but more than 1, then bowlers = players-1
			- If only one player, bowler = 1
			*/
			int numBowlers = (numPlayers > 5) ? 5 : (numPlayers > 1) ? (numPlayers - 1) : 1;
			maxPerBowler = (maxOvers % numBowlers == 0) ? maxOvers / numBowlers : (maxOvers / numBowlers + 1);
		}

		return maxPerBowler;
	}

	public static boolean isDivisibleBy(int number, int divisor) {
		return (divisor > 1 && number >= divisor && number % divisor == 0);
	}

	public static boolean isPowerOf(int number, int divisor) {
		if (divisor > 1) {
			if (number > divisor) {
				if (number % divisor == 0) {
					return isPowerOf(number / divisor, divisor);
				} else {
					return false;
				}
			} else return number == divisor;
		}

		return false;
	}

	public static boolean isPrime(int number) {
		if (number > 0 && number < 4)
			return true;

		for (int i = 2; i <= number / 2; i++) {
			if (number % i == 0)
				return false;
		}

		return true;
	}

	public static String convertTournamentToJSON(Tournament tournamentObj) {
		if (tournamentObj != null) {
			List<Group> groupList = new ArrayList<>();
			if (tournamentObj.getGroupList() != null) {
				groupList.addAll(tournamentObj.getGroupList());
			}
			tournamentObj.setGroupList(null);

			Gson gson = new Gson();
			Type type = new TypeToken<Tournament>() {
			}.getType();

			if (groupList.size() > 0) {
				tournamentObj.setGroupList(groupList);
			}

			return gson.toJson(tournamentObj, type);
		}

		return null;
	}

	public static Tournament convertJSONToTournament(String jsonString) {
		Tournament tournament = null;
		if (jsonString != null) {
			Gson gson = new Gson();
			Type type = new TypeToken<Tournament>() {
			}.getType();

			tournament = gson.fromJson(jsonString, type);
		}

		return tournament;
	}

	public static void clearBackStackUntil(FragmentManager fragmentManager, String backStackEntry) {
		fragmentManager.popBackStack(backStackEntry, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	static int generateRandomInt(int min, int max) {
		return new Random().nextInt(max - min + 1) + min;
	}
}
