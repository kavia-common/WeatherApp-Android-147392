package com.aniketjain.weatherapp;

import android.os.IBinder;
import android.view.WindowManager;

import androidx.test.espresso.Root;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Utility matcher for Toast messages in Espresso tests.
 * Matches windows of type TYPE_TOAST that are top-level (windowToken == appToken).
 */
class ToastMatcher extends TypeSafeMatcher<Root> {

    @Override
    public void describeTo(Description description) {
        description.appendText("is toast");
    }

    @Override
    public boolean matchesSafely(Root root) {
        int type = root.getWindowLayoutParams().get().type;
        if (type == WindowManager.LayoutParams.TYPE_TOAST) {
            IBinder windowToken = root.getDecorView().getWindowToken();
            IBinder appToken = root.getDecorView().getApplicationWindowToken();
            // windowToken == appToken means this window isn't contained by any other windows.
            return windowToken == appToken;
        }
        return false;
    }
}
