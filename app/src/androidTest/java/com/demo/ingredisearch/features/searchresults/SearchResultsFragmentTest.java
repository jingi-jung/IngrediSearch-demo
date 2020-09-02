package com.demo.ingredisearch.features.searchresults;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;

import com.demo.ingredisearch.R;
import com.demo.ingredisearch.RecipeApplication;
import com.demo.ingredisearch.TestData;
import com.demo.ingredisearch.repository.RecipeRepository;
import com.demo.ingredisearch.repository.sources.favorites.FakeFavoritesSource;
import com.demo.ingredisearch.repository.sources.remote.FakeRemoteDataSource;
import com.demo.ingredisearch.repository.sources.remote.FakeRemoteDataSource.DataStatus;
import com.demo.ingredisearch.util.SingleExecutors;

import org.hamcrest.Matcher;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.demo.ingredisearch.util.CustomViewMatchers.withRecyclerView;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SearchResultsFragmentTest {

    RecipeRepository mRecipeRepository;
    FakeRemoteDataSource mRemoteDataSource;
    FakeFavoritesSource mFavoritesSource;
    Bundle args;

    @Before
    public void init() {
        mRemoteDataSource = new FakeRemoteDataSource(new SingleExecutors());
        mFavoritesSource = new FakeFavoritesSource();
        mRecipeRepository = RecipeRepository.getInstance(mRemoteDataSource, mFavoritesSource);
        RecipeApplication app = ApplicationProvider.getApplicationContext();
        app.getInjection().setRecipeRepository(mRecipeRepository);

        args = new SearchResultsFragmentArgs.Builder("eggs").build().toBundle();
    }

    @After
    public void wrapUp() {
        mRecipeRepository.destroy();
    }

    @Test
    public void SearchResultsFragmentInView() {
        // Arrange (Given)
        mRemoteDataSource.addRecipes(TestData.mRecipes);

        // Act (When)
        FragmentScenario.launchInContainer(SearchResultsFragment.class, args, R.style.AppTheme, null);

        // Assert (Then)
        onView(withId(R.id.list)).check(matches(isDisplayed()));
    }

    @Test
    public void searchRecipes_queryWithNoResults_displayNoRecipesView() {
        // Arrange (Given)

        // Act (When)
        Bundle args = new SearchResultsFragmentArgs.Builder("query with empty results").build().toBundle();
        FragmentScenario.launchInContainer(SearchResultsFragment.class, args, R.style.AppTheme, null);

        // Assert (Then)
        onView(withId(R.id.noresultsContainer)).check(matches(isDisplayed()));
        onView(withId(R.id.noresultsTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void searchRecipes_errorOnConnection_displayRetryView() {
        // Arrange (Given)
        mRemoteDataSource.setDataStatus(DataStatus.NetworkError);

        // Act (When)
        FragmentScenario.launchInContainer(SearchResultsFragment.class, args, R.style.AppTheme, null);

        // Assert (Then)
        onView(withId(R.id.errorContainer)).check(matches(isDisplayed()));
        onView(withId(R.id.retry)).check(matches(isDisplayed()));
    }

    @Test
    public void searchRecipes_errorOnConnection_displayRetryView_andThen_retry() {
        // Arrange (Given)
        mRemoteDataSource.setDataStatus(DataStatus.NetworkError);

        // Act (When)
        FragmentScenario.launchInContainer(SearchResultsFragment.class, args, R.style.AppTheme, null);

        // Assert (Then)
        onView(withId(R.id.errorContainer)).check(matches(isDisplayed()));
        onView(withId(R.id.retry)).check(matches(isDisplayed()));

        // Arrange (Given)
        mRemoteDataSource.setDataStatus(DataStatus.Success);
        mRemoteDataSource.addRecipes(TestData.mRecipes);

        // Act (When)
        onView(withId(R.id.retry)).perform(click());

        // Assert (Then)
        onView(withId(R.id.list)).check(matches(isDisplayed()));
    }

    @Test
    public void selectRecipeAsFavorite_markItsStatusAsFavorite() throws Exception {
        // Given
        mRemoteDataSource.addRecipes(TestData.mRecipes);

        FragmentScenario.launchInContainer(SearchResultsFragment.class, args, R.style.AppTheme, null);

        onView(getFavButton(R.drawable.ic_favorite_border_24dp, TestData.recipe1.getTitle())).check(matches(isDisplayed()));

        // Act (When)
        onView(getFavButton(R.drawable.ic_favorite_border_24dp, TestData.recipe1.getTitle())).perform(click());

        // Assert (Then)
        onView(getFavButton(R.drawable.ic_favorite_24dp, TestData.recipe1.getTitle())).check(matches(isDisplayed()));
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
    public void selectRecipeAsUnFavorite_markItsStatusAsUnFavorite() throws Exception {
        // Given
        mRemoteDataSource.addRecipes(TestData.mRecipes);
        mFavoritesSource.addFavorites(TestData.recipe1);

        FragmentScenario.launchInContainer(SearchResultsFragment.class, args, R.style.AppTheme, null);

        onView(withRecyclerView(R.id.list).atPositionOnView(0, R.id.favButton))
                .check(matches(withTagValue(is(R.drawable.ic_favorite_24dp))));

        // Act (When)
        onView(withRecyclerView(R.id.list).atPositionOnView(0, R.id.favButton))
                .perform(click());

        // Assert (Then)
        onView(withRecyclerView(R.id.list).atPositionOnView(0, R.id.favButton))
                .check(matches(withTagValue(is(R.drawable.ic_favorite_border_24dp))));
    }

    @Test
    public void navigateToRecipeDetailsView() {
        // Arrange (Given)
        mRemoteDataSource.addRecipes(TestData.mRecipes);

        NavController navController = mock(NavController.class);

        FragmentScenario<SearchResultsFragment> scenario = FragmentScenario.launchInContainer(SearchResultsFragment.class, args, R.style.AppTheme, null);
        scenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        // Act (When)
        onView(withId(R.id.list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click())
        );

        // Assert (Then)
        verify(navController).navigate(
                SearchResultsFragmentDirections.actionSearchResultsFragmentToRecipeDetailsFragment(TestData.recipe1.getRecipeId())
        );
    }

}