package com.aniketjain.weatherapp;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.Manifest;
import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.NoMatchingRootException;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.UiController;
import androidx.test.espresso.matcher.ViewMatchers;

import android.view.View;

/**
 * Instrumentation test that verifies clicking on the warm metric triggers user-visible feedback.
 * It asserts that either a dialog with the warm info title appears, or a fallback toast/snackbar appears.
 */
@RunWith(AndroidJUnit4.class)
public class HomeActivityWarmClickTest {

    // Grant location permissions to avoid runtime permission dialog interfering with the test.
    @Rule
    public GrantPermissionRule permissionRuleFine = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule permissionRuleCoarse = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION);

    // Launches HomeActivity for the test.
    @Rule
    public ActivityScenarioRule<HomeActivity> activityScenarioRule = new ActivityScenarioRule<>(HomeActivity.class);

    @Test
    public void warmClick_showsDialogOrFallbackToast() {
        // Wait briefly to let initial UI settle; network is not required for this test path.
        onView(isRoot()).perform(waitFor(1000));

        // Ensure the warm container is present and click it.
        onView(withId(R.id.warm_container)).check(matches(isDisplayed())).perform(click());

        // First try to assert that the dialog with the warm info title is displayed.
        boolean dialogShown = tryAssertDialogWithTitle(R.string.warm_info_title);

        if (!dialogShown) {
            // If the dialog isn't shown for any reason, assert toast fallback text appears.
            // Compute localized expected message using the app context and mock provider values.
            Context ctx = ApplicationProvider.getApplicationContext();
            String expectedMessage = ctx.getString(
                    R.string.warm_info_message_template,
                    3,                         // MockWeatherProvider.getWarm() returns 3
                    "Mild",                    // MockWeatherProvider.getWarmInfo().description
                    "Stay hydrated"            // MockWeatherProvider.getWarmInfo().tip
            );

            // First try to match the full message toast; if not found, match the 'unavailable' fallback.
            boolean toastShown = tryAssertToastWithText(expectedMessage);
            if (!toastShown) {
                // Final fallback: check the 'warm info unavailable' toast text (localized).
                String unavailable = ctx.getString(R.string.warm_info_unavailable);
                // If this also fails, the assertion inside will throw and fail the test.
                onView(withText(unavailable)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
            }
        }
    }

    /**
     * Attempt to assert a dialog with the provided string resource title is displayed.
     * Returns true if assertion passes, false if not found.
     */
    private boolean tryAssertDialogWithTitle(int titleResId) {
        try {
            onView(withText(titleResId)).inRoot(isDialog()).check(matches(isDisplayed()));
            return true;
        } catch (NoMatchingViewException | AssertionError | NoMatchingRootException e) {
            return false;
        }
    }

    /**
     * Attempt to assert a toast with the provided text is displayed.
     * Returns true if assertion passes, false otherwise.
     */
    private boolean tryAssertToastWithText(String text) {
        try {
            onView(withText(text)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
            return true;
        } catch (NoMatchingViewException | AssertionError | NoMatchingRootException e) {
            // As a relaxed fallback, try to match the text anywhere on screen (e.g., snackbar or custom toast)
            try {
                onView(withText(text)).check(matches(isDisplayed()));
                return true;
            } catch (NoMatchingViewException | AssertionError ignored) {
                return false;
            }
        }
    }

    /**
     * Simple wait action to let UI settle without relying on network.
     */
    private static ViewAction waitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }
}
