package com.demo.ingredisearch.features.searchresults;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.demo.ingredisearch.TestData;
import com.demo.ingredisearch.models.Recipe;
import com.demo.ingredisearch.repository.RecipeRepository;
import com.demo.ingredisearch.repository.sources.favorites.FakeFavoritesSource;
import com.demo.ingredisearch.repository.sources.remote.FakeRemoteDataSource;
import com.demo.ingredisearch.repository.sources.remote.FakeRemoteDataSource.DataStatus;
import com.demo.ingredisearch.util.LiveDataTestUtil;
import com.demo.ingredisearch.util.SingleExecutors;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class SearchResultsViewModelTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    // SUT
    SearchResultsViewModel mViewModel;

    RecipeRepository mRecipeRepository;

    FakeFavoritesSource mFavoritesSource;

    FakeRemoteDataSource mRemoteDataSource;

    @Before
    public void init() {
        mRemoteDataSource = new FakeRemoteDataSource(new SingleExecutors());
        mFavoritesSource = new FakeFavoritesSource();
        mRecipeRepository = RecipeRepository.getInstance(mRemoteDataSource, mFavoritesSource);

        mViewModel = new SearchResultsViewModel(mRecipeRepository);
    }

    @After
    public void wrapUp() {
        mRecipeRepository.destroy();
    }

    @Test
    public void searchRecipes_allNonFavorites_displayRecipesAsTheyAre() throws Exception {
        // Arrange (Given)
        mRemoteDataSource.addRecipes(TestData.mRecipes);

        // Act (When)
        mViewModel.searchRecipes("eggs");

        // Assert (Then)
        List<Recipe> recipes = LiveDataTestUtil.getOrAwaitValue(mViewModel.getRecipes());
        assertThat(recipes, is(TestData.mRecipes));
    }

    @Test
    public void searchRecipes_emptyRecipes_displayNoRecipes() throws Exception {
        // Arrange (Given)

        // Act (When)
        mViewModel.searchRecipes("query with empty results");

        // Assert (Then)
        List<Recipe> recipes = LiveDataTestUtil.getOrAwaitValue(mViewModel.getRecipes());
        assertThat(recipes.size(), is(0));
    }

    @Test
    public void searchRecipes_networkError_displayRetry() throws Exception {
        // Arrange (Given)
        mRemoteDataSource.setDataStatus(DataStatus.NetworkError);

        // Act (When)
        mViewModel.searchRecipes("unused query");

        // Assert (Then)
        List<Recipe> recipes = LiveDataTestUtil.getOrAwaitValue(mViewModel.getRecipes());
        assertThat(recipes, is(nullValue()));

        Boolean doRetry = LiveDataTestUtil.getOrAwaitValue(mViewModel.doRetry());
        assertThat(doRetry, is(true));
    }

    @Test
    public void searchRecipes_withSomeFavorites_displayRecipesAsDecorated() throws Exception {
        // Arrange (Given)
        mRemoteDataSource.addRecipes(TestData.mRecipesWithFavorites);
        mFavoritesSource.addFavorites(TestData.recipe1);

        // Act (When)
        mViewModel.searchRecipes("eggs");

        // Assert (Then)
        List<Recipe> recipes = LiveDataTestUtil.getOrAwaitValue(mViewModel.getRecipes());
//        assertThat(recipes, is(Arrays.asList(TestData.recipe1_favored, TestData.recipe3, TestData.recipe4)));
        assertThat(getFavoritesStatus(recipes), is(Arrays.asList(true, false, false)));
    }

    private List<Boolean> getFavoritesStatus(List<Recipe> recipes) {
        return recipes.stream().map(Recipe::isFavorite).collect(Collectors.toList());
    }

    @Test
    public void markFavorite_reloadUpdatedRecipes() throws Exception {
        // Arrange (Given)
        mRemoteDataSource.addRecipes(TestData.mRecipes);
        mViewModel.searchRecipes("eggs");
        List<Recipe> recipes = LiveDataTestUtil.getOrAwaitValue(mViewModel.getRecipes());
        assertThat(getFavoritesStatus(recipes), is(Arrays.asList(false, false, false, false)));

        // Act (When)
        mViewModel.markFavorite(TestData.recipe1);

        // Assert (Then)
        recipes = LiveDataTestUtil.getOrAwaitValue(mViewModel.getRecipes());
        assertThat(getFavoritesStatus(recipes), is(Arrays.asList(true, false, false, false)));
    }

    @Test
    public void unMarkFavorite_reloadUpdatedRecipes() throws Exception {
        // Arrange (Given)
        mRemoteDataSource.addRecipes(TestData.mRecipes);
        mFavoritesSource.addFavorites(TestData.recipe1);
        mViewModel.searchRecipes("eggs");
        List<Recipe> recipes = LiveDataTestUtil.getOrAwaitValue(mViewModel.getRecipes());
        assertThat(getFavoritesStatus(recipes), is(Arrays.asList(true, false, false, false)));

        // Act (When)
        mViewModel.unmarkFavorite(TestData.recipe1_favored);

        // Assert (Then)
        recipes = LiveDataTestUtil.getOrAwaitValue(mViewModel.getRecipes());
        assertThat(getFavoritesStatus(recipes), is(Arrays.asList(false, false, false, false)));
    }

    @Test
    public void requestRecipeDetails_shouldTriggerNavToRecipeDetails() throws Exception {
        // Arrange (Given)
        mRemoteDataSource.addRecipes(TestData.mRecipes);

        // Act (When)
        mViewModel.requestRecipeDetails(TestData.recipe1);

        // Assert (Then)
        String recipeId = LiveDataTestUtil.getOrAwaitValue(mViewModel.navToRecipeDetails());
        assertThat(recipeId, is(TestData.recipe1.getRecipeId()));
    }

    @Test
    public void requestFavorites_shouldTriggerNavToFavorites() throws Exception {
        // Arrange (Given)

        // Act (When)
        mViewModel.requestFavorites();

        // Assert (Then)
        Boolean navToFavorites = LiveDataTestUtil.getOrAwaitValue(mViewModel.navToFavorites());
        assertThat(navToFavorites, is(true));
    }

}
