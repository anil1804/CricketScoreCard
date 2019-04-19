package com.theNewCone.cricketScoreCard.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.IdRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResourceTimeoutException;
import android.support.test.espresso.NoMatchingRootException;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.SeekBar;
import android.widget.TextView;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.utils.database.TournamentDBHandler;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.startsWith;


public class CommonTestUtils {

	public static final String[] AUS_PLAYERS = {
			"Aaron Finch",
			"D Arcy Short",
			"Chris Lynn",
			"Glenn Maxwell",
			"Ben McDermott",
			"Alex Carey",
			"Ashton Agar",
			"Nathan Coulter-Nile",
			"Adam Zampa",
			"Andrew Tye",
			"Billy Stanlake"
	};
	public static final String[] IND_PLAYERS = {
			"Rohit Sharma",
			"Shikhar Dhawan",
			"Lokesh Rahul",
			"Dinesh Karthik",
			"Kuldeep Yadav",
			"Jasprit Bumrah",
			"K Khaleel Ahmed",
			"Umesh Yadav",
			"Krunal Pandya",
			"Manish Pandey",
			"Rishabh Pant"
	};
	public static final String[] WI_PLAYERS = {
			"Rovman Powell",
			"Darren Bravo",
			"Denesh Ramdin",
			"Shimron Hetmyer",
			"Kieron Pollard",
			"Carlos Brathwaite",
			"Fabian Allen",
			"Oshane Thomas",
			"Shai Hope",
			"Keemo Paul",
			"Khary Pierre"
	};

	public static ViewInteraction getDisplayedView(int resourceID) {
		return onView(allOf(withId(resourceID), isDisplayed()));
	}

	public static ViewInteraction getDisplayedView(String text) {
		text = text.trim();
		return onView(allOf(withText(text), isDisplayed()));
	}

	public static ViewInteraction getView(String text) {
		text = text.trim();
		return onView(withText(text));
	}

	public static ViewInteraction goToView(String text) {
		text = text.trim();
		return onView(withText(text)).perform(scrollTo());
	}

	public static ViewInteraction goToView(int resourceID) {
		return onView(withId(resourceID)).perform(scrollTo());
	}

	public static ViewInteraction goToViewStarting(String text) {
		text = text.trim();
		return onView(withText(startsWith(text))).perform(scrollTo());
	}

	static ViewInteraction getView(Matcher<View> parentMatcher, Matcher<View> viewMatcher) {
		return onView(childWithParent(parentMatcher, viewMatcher));
	}

	public static boolean checkViewExists(Matcher<View> matcher) {
		try {
			onView(matcher).check(matches(matcher));
			return true;
		} catch (IdlingResourceTimeoutException | NoMatchingViewException | NoMatchingRootException ex) {
			return false;
		}
	}

	public static void checkIfToastShown(int stringResourceId) {
		onView(withText(stringResourceId))
				.inRoot(new ToastMatcher())
				.check(matches(isDisplayed()));
	}

	public static void checkIfToastShown(String toastMessage) {
		toastMessage = toastMessage.trim();
		onView(withText(toastMessage))
				.inRoot(new ToastMatcher())
				.check(matches(isDisplayed()));
	}

	private static void openNavigationDrawer() {
		ViewInteraction appCompatImageButton = onView(
				allOf(withContentDescription("Open navigation drawer"),
						childAtPosition(withId(R.id.toolbar), 1),
						isDisplayed()));
		appCompatImageButton.perform(click());
	}

	public static void clickNavigationMenuItem(int position) {
		openNavigationDrawer();
		onView(allOf(childAtPosition(allOf(withId(R.id.design_navigation_view),
				childAtPosition(withId(R.id.nav_view), 0)),
				position),
				isDisplayed())).perform(click());

	}

	private static Matcher<View> childAtPosition(final Matcher<View> parentMatcher, final int position) {

		return new TypeSafeMatcher<View>() {
			@Override
			public void describeTo(Description description) {
				description.appendText("Child at position " + position + " in parent ");
				parentMatcher.describeTo(description);
			}

			@Override
			public boolean matchesSafely(View view) {
				ViewParent parent = view.getParent();
				return parent instanceof ViewGroup && parentMatcher.matches(parent)
						&& view.equals(((ViewGroup) parent).getChildAt(position));
			}
		};
	}

	private static int getChildCount(@IdRes int viewId) {
		final int[] COUNT = {0};

		Matcher<View> matcher = new TypeSafeMatcher<View>() {
			@Override
			protected boolean matchesSafely(View item) {
				RecyclerView.Adapter adapter = ((RecyclerView) item).getAdapter();
				if (adapter != null) {
					COUNT[0] = adapter.getItemCount();
				}
				return true;
			}

			@Override
			public void describeTo(Description description) {
			}
		};

		onView(withId(viewId)).check(matches(matcher));
		return COUNT[0];
	}

	private static boolean selectTeamPlayers(String[] players, int numPlayers) {
		getDisplayedView(R.id.btnPTSSelectPlayers).perform(click());
		if (checkViewExists(withId(R.id.rcvPlayerList))) {

			List<String> playersToSelect =
					getPlayersToSelect((RecyclerView) getCurrentActivity().findViewById(R.id.rcvPlayerList),
							players, numPlayers);

			for (String playerName : playersToSelect) {
				int childCount = getChildCount(R.id.rcvPlayerList);
				int currChildPosition = 0, incBy = 2;
				boolean foundItem = false;
				while (!foundItem) {
					try {
						goToViewStarting(playerName).perform(click());
						foundItem = true;
					} catch (NoMatchingViewException | PerformException ex) {
						if (currChildPosition <= childCount) {
							getDisplayedView(R.id.rcvPlayerList).perform(RecyclerViewActions.scrollToPosition(currChildPosition));
							int currDiff = childCount - currChildPosition;
							currChildPosition += currDiff > incBy ? incBy : (currDiff == 0) ? 1 : currDiff;
						} else {
							throw ex;
						}
					}
				}
			}

			getDisplayedView("OK").perform(click());

			return true;
		} else {
			return false;
		}
	}

	private static Matcher<View> childWithParent(final Matcher<View> parentMatcher, final Matcher<View> childMatcher) {
		return new TypeSafeMatcher<View>() {
			@Override
			protected boolean matchesSafely(View view) {
				return childMatcher.matches(view) && parentMatcher.matches(view.getParent());
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("Child -> ");
				childMatcher.describeTo(description);
				description.appendText(" under parent -> ");
				parentMatcher.describeTo(description);
			}
		};
	}

	public static void deleteTournament(Context context, String tournamentName) {
		new TournamentDBHandler(context).deleteTournament(tournamentName);
	}

	public static ViewInteraction getView(int parentContentDescriptionStringId, int childTextStringId, Activity activity) {
		Resources resources = activity.getResources();
		return getView(
				withContentDescription(resources.getString(parentContentDescriptionStringId)),
				withText(resources.getString(childTextStringId)));
	}

	static ViewAction setProgress(final int progress) {
		return new ViewAction() {
			@Override
			public Matcher<View> getConstraints() {
				return ViewMatchers.isAssignableFrom(SeekBar.class);
			}

			@Override
			public String getDescription() {
				return "Set SeekBar progress to " + progress;
			}

			@Override
			public void perform(UiController uiController, View view) {
				SeekBar seekBar = (SeekBar) view;
				seekBar.setProgress(progress);
			}
		};
	}

	public static void selectTeam(MatchRunInfo info, boolean isTeam1) {
		selectTeam(info, isTeam1, true, true, true);
	}

	public static void selectTeam(MatchRunInfo info, boolean isTeam1, boolean selectPlayers, boolean selectCaptain, boolean selectWiki) {
		Resources resources = getCurrentActivity().getResources();

		TeamInfo teamInfo = isTeam1 ? info.getTeam1Info() : info.getTeam2Info();

		Activity activity = getCurrentActivity();
		TextView teamNameView;
		//Select Team
		if (isTeam1) {
			teamNameView = activity.findViewById(R.id.tvTeam1);
			CommonTestUtils.getDisplayedView(R.id.btnNMSelectTeam1).perform(click());
		} else {
			teamNameView = activity.findViewById(R.id.tvTeam2);
			CommonTestUtils.getDisplayedView(R.id.btnNMSelectTeam2).perform(click());
		}

		if (!info.isTournament()) {
			boolean teamAlreadySelected = !teamNameView.getText().toString().equals("");
			if (teamAlreadySelected)
				CommonTestUtils.getDisplayedView(R.id.btnPTSSelectTeam).perform(click());
			CommonTestUtils.getDisplayedView(teamInfo.getTeam().getName()).perform(click());
		}

		//Select Players
		if (selectPlayers) {
			boolean playersSelected = CommonTestUtils.selectTeamPlayers(teamInfo.getPlayers(), info.getNumPlayers());

			if (playersSelected) {
				//Select Captain
				if (selectCaptain) {
					CommonTestUtils.getDisplayedView(R.id.btnPTSSelectCaptain).perform(click());
					CommonTestUtils.goToViewStarting(teamInfo.getCaptain()).perform(click());
				}

				//Select Wicket-Keeper
				if (selectWiki) {
					CommonTestUtils.getDisplayedView(R.id.btnPTSSelectWK).perform(click());
					CommonTestUtils.goToViewStarting(teamInfo.getWicketKeeper()).perform(click());
				}

				CommonTestUtils.getDisplayedView(resources.getString(R.string.ok)).perform(click());
			}
		}
	}

	public static Activity getCurrentActivity() {
		final Activity[] currentActivity = new Activity[1];
		InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
			@Override
			public void run() {
				Collection<Activity> allActivities = ActivityLifecycleMonitorRegistry.getInstance()
						.getActivitiesInStage(Stage.RESUMED);
				if (!allActivities.isEmpty()) {
					currentActivity[0] = allActivities.iterator().next();
				}
			}
		});

		return currentActivity[0];
	}

	public static void loadDBData() {
		Activity activity = getCurrentActivity();
		Resources resources = activity.getResources();

		Espresso.openActionBarOverflowOrOptionsMenu(activity.getApplicationContext());
		CommonTestUtils.goToView(resources.getString(R.string.loadData)).perform(click());
	}

	private static List<String> getPlayersToSelect(RecyclerView playerListView, String[] playersToSelect, int selectedCount) {
		List<String> finalPlayerList = new ArrayList<>();
		List<String> playersToSelectList = Arrays.asList(playersToSelect);

		List<String> currentlySelectedPlayers = new ArrayList<>();
		if (playerListView != null && playerListView.getLayoutManager() != null) {
			for (int i = 0; i < selectedCount; i++) {
				View playerView = playerListView.getLayoutManager().findViewByPosition(i);
				if (playerView != null) {
					String playerName = ((TextView) playerView.findViewById(R.id.tvPlayerName)).getText().toString();

					if (playerName.contains("(w)")) {
						playerName = playerName.substring(0, playerName.length() - 3).trim();
					}

					if (!playersToSelectList.contains(playerName)) {
						finalPlayerList.add(playerName);
					}

					currentlySelectedPlayers.add(playerName);
				}
			}
		}

		for (String playerName : playersToSelectList) {
			if (!currentlySelectedPlayers.contains(playerName)) {
				finalPlayerList.add(playerName);
			}
		}

		return finalPlayerList;
	}
}
