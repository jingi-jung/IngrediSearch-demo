package com.demo.ingredisearch.features.search;

import android.os.RemoteException;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import com.demo.ingredisearch.R;

import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SearchFragmentTest {

    @Test
    public void searchFragmentInView() throws Exception {
        // Arrange (Given)

        // Act (When)
        FragmentScenario.launchInContainer(SearchFragment.class, null, R.style.AppTheme, null);

        // Assert (Then)
        // check whether search_header text is displayed
        onView(withText(R.string.search_header)).check(matches(isDisplayed()));

        // check whether ingredients is displayed
        onView(withId(R.id.ingredients)).check(matches(isDisplayed()));

        // check whether searchActionButton is displayed
        onView(withId(R.id.searchActionButton)).check(matches(isDisplayed()));
    }

    @Test
    public void search_validQuery_navigateToSearchResultsView() {
        // Arrange (Given)
        NavController navController = mock(NavController.class);

        FragmentScenario<SearchFragment> scenario = FragmentScenario.launchInContainer(SearchFragment.class, null, R.style.AppTheme, null);
        scenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        // Act (When)
        onView(withId(R.id.ingredients)).perform(typeText("eggs"), closeSoftKeyboard());
        onView(withId(R.id.searchActionButton)).perform(click());

        // Assert (Then)
        verify(navController).navigate(
                SearchFragmentDirections.actionSearchFragmentToSearchResultsFragment("eggs")
        );
    }

    @Test
    public void search_emptyQuery_displayWarningSnackBar() {
        // Arrange (Given)
        FragmentScenario.launchInContainer(SearchFragment.class, null, R.style.AppTheme, null);

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Act (When)
        onView(withId(R.id.ingredients)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.searchActionButton)).perform(click());

        // Assert (Then)
        // check snackbar is displayed: search_query_required
        onView(withText(R.string.search_query_required)).check(matches(isDisplayed()));
    }

    @Test
    public void search_emptyQuery_displayWarningSnackBar_shouldNotDisplayAgain_whenRotated() throws RemoteException {
        // Arrange (Given)
        FragmentScenario.launchInContainer(SearchFragment.class, null, R.style.AppTheme, null);

        // Act (When)
        onView(withId(R.id.ingredients)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.searchActionButton)).perform(click());

        // Assert (Then)
        // check snackbar is displayed
        onView(withText(R.string.search_query_required)).check(matches(isDisplayed()));

        // Act (When)
        // Rotate screen
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        uiDevice.setOrientationLeft();

        // check snackbar does not exist
        onView(withText(R.string.search_query_required)).check(doesNotExist());
    }
}