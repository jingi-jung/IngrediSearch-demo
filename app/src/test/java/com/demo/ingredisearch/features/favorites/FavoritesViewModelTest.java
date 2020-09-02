package com.demo.ingredisearch.features.favorites;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.demo.ingredisearch.TestData;
import com.demo.ingredisearch.models.Recipe;
import com.demo.ingredisearch.repository.RecipeRepository;
import com.demo.ingredisearch.repository.sources.favorites.FakeFavoritesSource;
import com.demo.ingredisearch.util.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class FavoritesViewModelTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    // SUT
    FavoritesViewModel mViewModel;

    RecipeRepository mRecipeRepository;
    FakeFavoritesSource mFavoritesSource;

    @Before
    public void init() {
        mFavoritesSource = new FakeFavoritesSource();
        mRecipeRepository = RecipeRepository.getInstance(null, mFavoritesSource);
        mViewModel = new FavoritesViewModel(mRecipeRepository);
    }

    @After
    public void wrapUp() {
        mRecipeRepository.destroy();
    }

    @Test
    public void getFavorites_returnFavoriteRecipes() throws Exception {
        // Arrange (Given)
        mFavoritesSource.addFavorites(TestData.mFavorites);

        // Act (When)
        List<Recipe> favorites = LiveDataTestUtil.getOrAwaitValue(mViewModel.getFavorites());

        // Assert (Then)
        assertThat(favorites.size(), is(2));
    }

    @Test
    public void removeFavorite_shouldRemoveFavorite() throws InterruptedException {
        // Arrange (Given)
        mFavoritesSource.addFavorites(TestData.recipe1, TestData.recipe2);

        // Act (When)
        mViewModel.removeFavorite(TestData.recipe1_favored);

        // Assert (Then)
        List<Recipe> favorites = LiveDataTestUtil.getOrAwaitValue(mViewModel.getFavorites());
        assertThat(favorites, is(singletonList(TestData.recipe2_favored)));
    }

    @Test
    public void clearFavorites_shouldResetFavoritesToEmpty() throws Exception {
        // Arrange (Given)
        mFavoritesSource.addFavorites(TestData.mFavorites);

        // Act (When)
        mViewModel.clearFavorites();

        // Assert (Then)
        List<Recipe> favorites = LiveDataTestUtil.getOrAwaitValue(mViewModel.getFavorites());
        assertThat(favorites.size(), is(0));
    }

    @Test
    public void requestToRecipeDetails_shouldTriggerNavToRecipeDetails() throws Exception {
        // Arrange (Given)
        mFavoritesSource.addFavorites(TestData.mFavorites);

        // Act (When)
        mViewModel.requestRecipeDetails(TestData.recipe1_favored.getRecipeId());

        // Assert (Then)
        String recipeId = LiveDataTestUtil.getOrAwaitValue(mViewModel.navToRecipeDetails());
        assertThat(recipeId, is(TestData.recipe1_favored.getRecipeId()));
    }

}
