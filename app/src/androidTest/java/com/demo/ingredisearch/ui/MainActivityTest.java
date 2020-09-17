package com.demo.ingredisearch.ui;

import android.view.Gravity;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.DrawerMatchers;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.demo.ingredisearch.R;
import com.demo.ingredisearch.RecipeApplication;
import com.demo.ingredisearch.TestData;
import com.demo.ingredisearch.UITests;
import com.demo.ingredisearch.repository.RecipeRepository;
import com.demo.ingredisearch.repository.sources.favorites.FakeFavoritesSource;
import com.demo.ingredisearch.repository.sources.remote.FakeRemoteDataSource;
import com.demo.ingredisearch.util.SingleExecutors;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

    RecipeRepository mRecipeRepository;
    FakeRemoteDataSource mRemoteDataSource;

    @Before
    public void init() {
        RecipeApplication app = ApplicationProvider.getApplicationContext();
        mRemoteDataSource = new FakeRemoteDataSource(new SingleExecutors());
        FakeFavoritesSource mFavoritesSource = new FakeFavoritesSource();
        mRecipeRepository = RecipeRepository.getInstance(mRemoteDataSource, mFavoritesSource);
        app.getInjection().setRecipeRepository(mRecipeRepository);
    }

    @After
    public void wrapUp() {
        mRecipeRepository.destroy();
    }

    @Test
    public void activityInView() {
        // check toolbar main title displayed
        onView(withText(R.string.main_title)).check(matches(isDisplayed()));

        checkMainScreenBodyDisplayed();
    }

    private void checkMainScreenBodyDisplayed() {
        // check whether 'searchButton' view displayed
        onView(withId(R.id.searchButton)).check(matches(isDisplayed()));
        onView(allOf(
                withText(R.string.search),
                hasSibling(withContentDescription(R.string.search))
        )).check(matches(isDisplayed()));

        // check whether 'favButton' view displayed
        onView(withId(R.id.favButton)).check(matches(isDisplayed()));
        onView(allOf(
                withText(R.string.favorites),
                withParent(withId(R.id.favButton))
        )).check(matches(isDisplayed()));
    }

    @Test
    public void testOpenDrawer() {
        // Arrange (Given)
//        ActivityScenario.launch(MainActivity.class);

        // Act (When)
        // open drawer (drawer_layout)
        openDrawer();

        // Assert (Then)
        // check drawer Header displayed
        onView(withContentDescription(R.string.search_header_image_content_description)).check(matches(isDisplayed()));
        onView(withText(R.string.drawer_search_title)).check(matches(isDisplayed()));

        // check drawer Contents displayed
        onView(withText(R.string.home)).check(matches(isDisplayed()));
        onView(allOf(
                withText(R.string.search),
                withParent(withId(R.id.searchFragment))
        )).check(matches(isDisplayed()));
        onView(allOf(
                withText(R.string.favorites),
                withParent(withId(R.id.favoritesFragment))
        )).check(matches(isDisplayed()));
    }

    @Test
    public void testCloseDrawer() {
        // Arrange (Given)

        // Act (When)
        // open drawer and close drawer
        openDrawer();
        closeDrawer();

        // Assert (Then)
        // check drawer Header not displayed
        onView(withContentDescription(R.string.search_header_image_content_description)).check(matches(not(isDisplayed())));
        onView(withText(R.string.drawer_search_title)).check(matches(not(isDisplayed())));

        // check drawer Contents not displayed
        onView(withText(R.string.home)).check(matches(not(isDisplayed())));
        onView(allOf(
                withText(R.string.search),
                withParent(withId(R.id.searchFragment))
        )).check(matches(not(isDisplayed())));
        onView(allOf(
                withText(R.string.favorites),
                withParent(withId(R.id.favoritesFragment))
        )).check(matches(not(isDisplayed())));
    }

    @Test
    public void navigateToHomeScreen() {
        // Arrange (Given)
        // open drawer
        openDrawer();

        // Act (When)
        // click on Home menu item
        onView(withId(R.id.mainFragment)).perform(click());

        // Assert (Then)
        // check whether searchButton and favButton is displayed
        checkMainScreenBodyDisplayed();
    }

    @Test
    public void navigateToSearchScreen() {
        // Arrange (Given)
        // open drawer
        openDrawer();

        // Act (When)
        // click on Search menu item
        onView(withId(R.id.searchFragment)).perform(click());

        // Assert (Then)
        // check whether search_title text is displayed
        onView(withText(R.string.search_title)).check(matches(isDisplayed()));

        // check whether search_header text is displayed
        onView(withText(R.string.search_header)).check(matches(isDisplayed()));

        // check whether ingredients is displayed
        onView(withId(R.id.ingredients)).check(matches(isDisplayed()));

        // check whether searchActionButton is displayed
        onView(withId(R.id.searchActionButton)).check(matches(isDisplayed()));
    }

    // TODO
    @Test
    public void navigateToFavoriteScreen() {
        // Arrange (Given)
        // open drawer
        openDrawer();

        // Act (When)
        // click on Favorites menu item
        onView(withId(R.id.favoritesFragment)).perform(click());

        // Assert (Then)
        // check whether "Favorites" toolbar title is displayed
        onView(allOf(
                withText(R.string.favorites_title),
                withParent(withId(R.id.toolbar))
        )).check(matches(isDisplayed()));
    }

    @Test
    public void navigateToSearchScreen_and_backToHomeScreen() {
        // Arrange (Given)
        // click on Search button
        onView(withId(R.id.searchButton)).perform(click());

        // Act (When)
        // press on the back button or navigateBack()
//        Espresso.pressBack();
        navigateBack();

        // Assert (Then)
        // check whether searchButton and favButton is displayed
        checkMainScreenBodyDisplayed();
    }

    @Test
    public void navigateToSearchScreenToSearchResults_and_backToMainScreen() {
        // Arrange (Given)
        // click on Search button
        onView(withId(R.id.searchButton)).perform(click());

        // enter query and press searchActionButton
        onView(withId(R.id.ingredients)).perform(typeText("eggs"), closeSoftKeyboard());
        onView(withId(R.id.searchActionButton)).perform(click());

        // Act (When)
        // press back button twice
        Espresso.pressBack();
        Espresso.pressBack();

        // Assert (Then)
        // check whether main screen in view
        checkMainScreenBodyDisplayed();
    }

    @Test
    public void navigateToSearchScreenToSearchResultsToRecipeDetails_and_backToMainScreen() {
        // Arrange (Given)
        // add test data to fake remote data source
        mRemoteDataSource.addRecipes(TestData.mRecipes);

        // click on Search button
        onView(withId(R.id.searchButton)).perform(click());

        // enter query and press searchActionButton
        onView(withId(R.id.ingredients)).perform(typeText("eggs"), closeSoftKeyboard());
        onView(withId(R.id.searchActionButton)).perform(click());

        // select a recipe
        onView(withId(R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Act (When)
        // press back button twice
        Espresso.pressBack();
        Espresso.pressBack();
        Espresso.pressBack();

        // Assert (Then)
        // check whether main screen in view
        checkMainScreenBodyDisplayed();
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
//        onView(withContentDescription("Navigate up")).perform(click());
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
    }

}
