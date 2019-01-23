package com.theNewCone.cricketScoreCard.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.IdRes;
import android.support.test.espresso.IdlingResourceTimeoutException;
import android.support.test.espresso.NoMatchingRootException;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.SeekBar;

import com.theNewCone.cricketScoreCard.R;
import com.theNewCone.cricketScoreCard.utils.database.DatabaseHandler;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

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

	public static ViewInteraction goToChildAtPosition(Matcher<View> parentMatcher, int position) {
		return onView(childAtPosition(parentMatcher, position)).perform(scrollTo());
	}

	static ViewInteraction getChild(Matcher<View> parentMatcher, Matcher<View> childMatcher) {
		return onView(childWithParent(parentMatcher, childMatcher));
	}

	public static boolean checkViewExists(Matcher<View> matcher) {
		try {
			onView(matcher);
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

/*
	private static void sleepABit(int mills) {
		try {
			Thread.sleep(mills);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
*/

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

	public static void selectTeamPlayers(Activity activity, int buttonId, String[] players) {
		EditText etNumPlayers = activity.findViewById(R.id.etNumPlayers);
		int numPlayers = 11;
		if (!etNumPlayers.getText().toString().equals("")) {
			numPlayers = Integer.parseInt(etNumPlayers.getText().toString());
		}

		goToView(buttonId).perform(click());
		if (getChildCount(R.id.rcvPlayerList) > numPlayers) {
			for (int i = 0; i < numPlayers; i++) {
				goToChildAtPosition(withId(R.id.rcvPlayerList), i).perform(click());
			}

			int i = 0;
			for (String playerName : players) {
				if (i >= numPlayers)
					break;
				try {
					goToViewStarting(playerName).perform(click());
				} catch (NoMatchingViewException ex) {
					getChild(withId(R.id.rcvPlayerList), withText(startsWith(playerName))).perform(click());
				}
				i++;
			}
		}
		getDisplayedView("OK").perform(click());
	}

	public static void clickPlayers(int buttonId, String[] players) {
		goToView(buttonId).perform(click());
		for (String playerName : players) {
			goToViewStarting(playerName).perform(click());
		}
		getDisplayedView("OK").perform(click());
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
		new DatabaseHandler(context).deleteTournament(tournamentName);
	}

	public static ViewInteraction getView(int parentContentDescriptionStringId, int childTextStringId, Activity activity) {
		Resources resources = activity.getResources();
		return getChild(
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
}
