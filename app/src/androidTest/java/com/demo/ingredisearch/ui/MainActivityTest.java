package com.demo.ingredisearch.ui;

import android.view.Gravity;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.DrawerMatchers;

import com.demo.ingredisearch.R;

import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class MainActivityTest {

    @Test
    public void activityInView() {
        // check toolbar main title displayed

        // check whether 'searchButton' view displayed

        // check whether 'favButton' view displayed
    }

    @Test
    public void testOpenDrawer() {
        // Arrange (Given)

        // Act (When)
        // open drawer (drawer_layout)
        openDrawer();

        // Assert (Then)
        // check drawer Header displayed

        // check drawer Contents displayed
    }

    @Test
    public void testCloseDrawer() {
        // Arrange (Given)

        // Act (When)
        // open drawer and close drawer

        // Assert (Then)
        // check drawer Header not displayed

        // check drawer Contents not displayed
    }

    @Test
    public void navigateToHomeScreen() {
        // Arrange (Given)
        // open drawer

        // Act (When)
        // click on Home menu item

        // Assert (Then)
        // check whether searchButton and favButton is displayed
    }

    @Test
    public void navigateToSearchScreen() {
        // Arrange (Given)
        // open drawer

        // Act (When)
        // click on Search menu item

        // Assert (Then)
        // check whether search_title text is displayed

        // check whether search_header text is displayed

        // check whether ingredients is displayed

        // check whether searchActionButton is displayed
    }

    @Test
    public void navigateToFavoriteScreen() {
        // Arrange (Given)
        // open drawer

        // Act (When)
        // click on Favorites menu item

        // Assert (Then)
        // check whether "Favorites" toolbar title is displayed
    }

    @Test
    public void navigateToSearchScreen_and_backToHomeScreen() {
        // Arrange (Given)
        // click on Search button

        // Act (When)
        // press on the back button or navigateBack()

        // Assert (Then)
        // check whether searchButton and favButton is displayed
    }

    @Test
    public void navigateToSearchScreenToSearchResults_and_backToMainScreen() {
        // Arrange (Given)
        // click on Search button

        // enter query and press searchActionButton

        // Act (When)
        // press back button twice

        // Assert (Then)
        // check whether main screen in view
    }

    @Test
    public void navigateToSearchScreenToSearchResultsToRecipeDetails_and_backToMainScreen() {
        // Arrange (Given)
        // add test data to fake remote data source

        // click on Search button

        // enter query and press searchActionButton

        // select a recipe

        // Act (When)
        // press back button twice
        Espresso.pressBack();
        Espresso.pressBack();
        Espresso.pressBack();

        // Assert (Then)
        // check whether main screen in view
    }

    private void openDrawer() {
        onView(withId(R.id.drawer_layout))
                .check(matches(DrawerMatchers.isClosed(Gravity.START)))
                .perform(DrawerActions.open());
    }

    private void closeDrawer() {
        onView(withId(R.id.drawer_layout))
                .check(matches(DrawerMatchers.isOpen(Gravity.START)))
                .perform(DrawerActions.close());
    }

    private void navigateBack() {
        // contentDescription "Navigate up" or R.string.abc_action_bar_up_description
    }

}
