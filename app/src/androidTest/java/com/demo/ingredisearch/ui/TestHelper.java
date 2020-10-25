package com.demo.ingredisearch.ui;

import android.view.Gravity;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.DrawerMatchers;

import com.demo.ingredisearch.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class TestHelper {

    public static void openDrawer() {
        onView(withId(R.id.drawer_layout))
                .check(matches(DrawerMatchers.isClosed(Gravity.START)))
                .perform(DrawerActions.open());
    }

    public static void closeDrawer() {
        onView(withId(R.id.drawer_layout))
                .check(matches(DrawerMatchers.isClosed(Gravity.START)))
                .perform(DrawerActions.close());
    }

    public static void navigateBack() {
        // contentDescription "Navigate up" or R.string.abc_action_bar_up_description
        onView(withContentDescription("Navigate up")).check(matches(isDisplayed()));
    }
}
