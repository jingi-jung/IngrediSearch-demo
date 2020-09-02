package com.demo.ingredisearch.features.favorites;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;

import com.demo.ingredisearch.R;
import com.demo.ingredisearch.RecipeApplication;
import com.demo.ingredisearch.TestData;
import com.demo.ingredisearch.repository.RecipeRepository;
import com.demo.ingredisearch.repository.sources.favorites.FakeFavoritesSource;

import org.hamcrest.Matcher;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.demo.ingredisearch.util.CustomViewMatchers.withRecyclerView;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FavoritesFragmentTest {

    RecipeRepository mRecipeRepository;
    FakeFavoritesSource mFavoritesSource;

    @Before
    public void init() {
        mFavoritesSource = new FakeFavoritesSource();
        mRecipeRepository = RecipeRepository.getInstance(null, mFavoritesSource);
        RecipeApplication app = ApplicationProvider.getApplicationContext();
        app.getInjection().setRecipeRepository(mRecipeRepository);
    }

    @After
    public void wrapUp() {
        mRecipeRepository.destroy();
    }

    @Test
    public void favoritesFragmentInView_withFavorites() throws Exception {
        // Arrange (Given)
        mFavoritesSource.addFavorites(TestData.mFavorites);

        // Act (When)
        FragmentScenario.launchInContainer(FavoritesFragment.class, null, R.style.AppTheme,null);

        // Assert (Then)
        onView(withId(R.id.list)).check(matches(isDisplayed()));
    }

    @Test
    public void favoritesFragmentInView_withNoFavorites() {
        // Given

        // When
        FragmentScenario.launchInContainer(FavoritesFragment.class, null, R.style.AppTheme,null);

        // Then - No favorites yet
        onView(withId(R.id.noresultsContainer)).check(matches(isDisplayed()));
        onView(withId(R.id.noresultsTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void selectRecipeAsNonFavorite_removesTheRecipeFromView() throws Exception {
        // Arrange (Given)
        mFavoritesSource.addFavorites(TestData.mFavorites);
        FragmentScenario.launchInContainer(FavoritesFragment.class, null, R.style.AppTheme,null);

        // Act (When)
        onView(withRecyclerView(R.id.list).atPositionOnView(0, R.id.favButton))
                .perform(click());

        // Assert (Then)
        onView(getFavButton(R.drawable.ic_favorite_24dp, TestData.recipe1_favored.getTitle()))
                .check(doesNotExist());

        // or
        onView(withRecyclerView(R.id.list).atPositionOnView(0, R.id.title))
                .check(matches(withText(TestData.recipe2_favored.getTitle())));
    }

    @NotNull
    private Matcher<View> getFavButton(@IdRes int tag, String title) {
        return allOf(
                withId(R.id.favButton),
                withTagValue(is(tag)),
                hasSibling(withText(title))
        );
    }

    @Test
    public void selectLastRecipeAsNonFavorite_displaysNoFavorites() throws Exception {
        // Arrange (Given)
        mFavoritesSource.addFavorites(TestData.recipe1);
        FragmentScenario.launchInContainer(FavoritesFragment.class, null, R.style.AppTheme,null);

        // Act (When)
        onView(withRecyclerView(R.id.list).atPositionOnView(0, R.id.favButton))
                .perform(click());

        // Assert (Then)
        onView(withId(R.id.list)).check(matches(not(isDisplayed())));
        onView(withId(R.id.noresultsContainer)).check(matches(isDisplayed()));
        onView(withId(R.id.noresultsTitle)).check(matches(isDisplayed()));
    }

    /**
     * Alas... This works only until fragment_version = "1.3.0-alpha06".
     */
    @Ignore
    @Test
    public void navigateToRecipeDetailsView() {
        // Arrange (Given)
        mFavoritesSource.addFavorites(TestData.mFavorites);

        TestNavHostController navHostController = new TestNavHostController(
                ApplicationProvider.getApplicationContext()
        );
        navHostController.setGraph(R.navigation.navigation);
        navHostController.setCurrentDestination(R.id.favoritesFragment);

        FragmentScenario<FavoritesFragment> scenario = FragmentScenario.launchInContainer(FavoritesFragment.class, null, R.style.AppTheme, null);
        scenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navHostController));

        // Act (When)
        onView(withId(R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Assert (Then)
        assertThat(navHostController.getCurrentDestination().getId(), is(R.id.recipeDetailsFragment));
    }

    @Test
    public void navigateToRecipeDetailsView_with_Mock() {
        // Arrange (Given)
        mFavoritesSource.addFavorites(TestData.mFavorites);

        NavController navController = mock(NavController.class);

        FragmentScenario<FavoritesFragment> scenario = FragmentScenario.launchInContainer(FavoritesFragment.class, null, R.style.AppTheme, null);
        scenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        // Act (When)
        onView(withId(R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Assert (Then)
        verify(navController).navigate(
                FavoritesFragmentDirections.actionFavoritesFragmentToRecipeDetailsFragment(TestData.recipe1.getRecipeId())
        );
    }
}