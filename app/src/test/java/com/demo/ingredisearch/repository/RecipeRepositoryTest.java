package com.demo.ingredisearch.repository;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.demo.ingredisearch.TestData;
import com.demo.ingredisearch.models.Recipe;
import com.demo.ingredisearch.repository.sources.favorites.FakeFavoritesSource;
import com.demo.ingredisearch.repository.sources.remote.FakeRemoteDataSource;
import com.demo.ingredisearch.util.LiveDataTestUtil;
import com.demo.ingredisearch.util.Resource;
import com.demo.ingredisearch.util.SingleExecutors;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RecipeRepositoryTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    private static final String UNUSED_QUERY = null;
    private static final String VALID_QUERY = "eggs";

    // SUT
    RecipeRepository mRecipeRepository;

    FakeRemoteDataSource mRemoteDataSource;

    FakeFavoritesSource mFavoritesSource;

    @Before
    public void init() {
        mRemoteDataSource = new FakeRemoteDataSource(new SingleExecutors());
        mFavoritesSource = new FakeFavoritesSource();
        mRecipeRepository = RecipeRepository.getInstance(mRemoteDataSource, mFavoritesSource);
    }

    @After
    public void wrapUp() {
        mRecipeRepository.destroy();
    }

    @Test
    public void searchRecipes_whenFailedByNetworkError_returnsErrorResponse() throws InterruptedException {
        // Arrange (Given)
        mRemoteDataSource.setDataStatus(FakeRemoteDataSource.DataStatus.NetworkError);

        // Act (When)
        mRecipeRepository.searchRecipes(UNUSED_QUERY);

        // Assert (Then)
        Resource<List<Recipe>> response = LiveDataTestUtil.getOrAwaitValue(mRecipeRepository.getRecipes());
        assertThat(response, is(Resource.error("Network Error", null)));
    }

    @Test
    public void searchRecipes_whenFailedWithHTTPError_returnsErrorResponse() throws InterruptedException {
        // Arrange (Given)
        mRemoteDataSource.setDataStatus(FakeRemoteDataSource.DataStatus.HTTPError);

        // Act (When)
        mRecipeRepository.searchRecipes(UNUSED_QUERY);

        // Assert (Then)
        Resource<List<Recipe>> response = LiveDataTestUtil.getOrAwaitValue(mRecipeRepository.getRecipes());
        assertThat(response, is(Resource.error("HTTP Error", null)));

    }

    @Test
    public void searchRecipes_whenFailedWithAuthError_returnsErrorResponse() throws InterruptedException {
        // Arrange (Given)
        mRemoteDataSource.setDataStatus(FakeRemoteDataSource.DataStatus.AuthError);

        // Act (When)
        mRecipeRepository.searchRecipes(UNUSED_QUERY);

                // Assert (Then)
        Resource<List<Recipe>> response = LiveDataTestUtil.getOrAwaitValue(mRecipeRepository.getRecipes());
        assertThat(response, is(Resource.error("401 Unauthorized. Token may be invalid", null)));

    }

    @Test
    public void searchRecipes_whenSucceedWithNullResult_returnsEmptyList() throws InterruptedException {
        // Arrange (Given)

        // Act (When)
        mRecipeRepository.searchRecipes(VALID_QUERY);

        // Assert (Then)
        Resource<List<Recipe>> response = LiveDataTestUtil.getOrAwaitValue(mRecipeRepository.getRecipes());
        assertThat(response, is(Resource.success(emptyList())));
    }

    @Test
    public void searchRecipes_whenSucceed_returnsRecipesList() throws InterruptedException {
        // Arrange (Given)
        mRemoteDataSource.addRecipes(TestData.mRecipes);

        // Act (When)
        mRecipeRepository.searchRecipes(VALID_QUERY);

                // Assert (Then)
        Resource<List<Recipe>> response = LiveDataTestUtil.getOrAwaitValue(mRecipeRepository.getRecipes());
        assertThat(response, is(Resource.success(TestData.mRecipes)));
    }

    @Test
    public void searchRecipe_whenFailedByNetworkError_returnsErrorResponse() throws InterruptedException {
        // Arrange (Given)
        mRemoteDataSource.setDataStatus(FakeRemoteDataSource.DataStatus.NetworkError);

        // Act (When)
        mRecipeRepository.searchRecipe(TestData.recipe1.getRecipeId());

                // Assert (Then)
        Resource<Recipe> response = LiveDataTestUtil.getOrAwaitValue(mRecipeRepository.getRecipe());
        assertThat(response, is(Resource.error("Network Error", null)));
    }

    @Test
    public void searchRecipe_whenFailedWithHTTPError_returnsErrorResponse() throws InterruptedException {
        // Arrange (Given)
        mRemoteDataSource.setDataStatus(FakeRemoteDataSource.DataStatus.HTTPError);

        // Act (When)
        mRecipeRepository.searchRecipe(TestData.recipe1.getRecipeId());

                // Assert (Then)
        Resource<Recipe> response = LiveDataTestUtil.getOrAwaitValue(mRecipeRepository.getRecipe());
        assertThat(response, is(Resource.error("HTTP Error", null)));
    }

    @Test
    public void searchRecipe_whenFailedWithAuthError_returnsErrorResponse() throws InterruptedException {
        // Arrange (Given)
        mRemoteDataSource.setDataStatus(FakeRemoteDataSource.DataStatus.AuthError);

        // Act (When)
        mRecipeRepository.searchRecipe(TestData.recipe1.getRecipeId());

                // Assert (Then)
        Resource<Recipe> response = LiveDataTestUtil.getOrAwaitValue(mRecipeRepository.getRecipe());
        assertThat(response, is(Resource.error("401 Unauthorized. Token may be invalid", null)));
    }

    @Test
    public void searchRecipe_whenSucceedWithNullResult_returnsNull() throws InterruptedException {
        // Arrange (Given)

        // Act (When)
        mRecipeRepository.searchRecipe(TestData.recipe1.getRecipeId());

                // Assert (Then)
        Resource<Recipe> response = LiveDataTestUtil.getOrAwaitValue(mRecipeRepository.getRecipe());
        assertThat(response, is(Resource.success(null)));
    }

    @Test
    public void searchRecipe_whenSucceed_returnsRecipe() throws InterruptedException {
        // Arrange (Given)
        mRemoteDataSource.addRecipes(TestData.mRecipes);

        // Act (When)
        mRecipeRepository.searchRecipe(TestData.recipe1.getRecipeId());

                // Assert (Then)
        Resource<Recipe> response = LiveDataTestUtil.getOrAwaitValue(mRecipeRepository.getRecipe());
        assertThat(response, is(Resource.success(TestData.recipe1)));
    }

    /**/

    @Test
    public void getFavorites_noFavorites_returnEmptyList() {
        // Arrange (Given)

        // Act (When)
        List<Recipe> favorites = mRecipeRepository.getFavorites();

        // Assert (Then)
        assertThat(favorites.size(), is(0));
    }

    @Test
    public void getFavorites_someFavorites_returnAll() {
        // Arrange (Given)
        mFavoritesSource.addFavorites(TestData.recipe1, TestData.recipe2);

        // Act (When)
        List<Recipe> favorites = mRecipeRepository.getFavorites();

        // Assert (Then)
        assertThat(favorites.size(), is(2));
    }

    @Test
    public void addFavorites_noDuplicateId_addToFavoritesWithFavoriteStatusAsTrue() {
        // Arrange (Given)
        mFavoritesSource.addFavorites(TestData.recipe1);

        // Act (When)
        mRecipeRepository.addFavorite(TestData.recipe2);

        // Assert (Then)
        List<Recipe> favorites = mRecipeRepository.getFavorites();
        assertThat(favorites.size(), is(2));
        assertThat(favoritesStatus(favorites), is(Arrays.asList(true, true)));
    }

    private List<Boolean> favoritesStatus(List<Recipe> favorites) {
        return favorites.stream().map(Recipe::isFavorite).collect(Collectors.toList());
    }

    @Test
    public void addFavorites_recipeWithSameIdAlreadyExists_rejectAddition() {
        // Arrange (Given)
        mFavoritesSource.addFavorites(TestData.recipe1);

        // Act (When)
        mRecipeRepository.addFavorite(TestData.recipe1);

        // Assert (Then)
        List<Recipe> favorites = mRecipeRepository.getFavorites();
        assertThat(favorites.size(), is(1));
    }

    @Test
    public void removeFavorite_removesRecipeFromFavorites() {
        // Arrange (Given)
        mFavoritesSource.addFavorites(TestData.recipe1, TestData.recipe2);

        // Act (When)
        mRecipeRepository.removeFavorite(TestData.recipe1_favored);

        // Assert (Then)
        List<Recipe> favorites = mRecipeRepository.getFavorites();
        assertThat(favorites.size(), is(1));
    }

    @Test
    public void clearFavorites_removeAllFavorites() {
        // Arrange (Given)
        mFavoritesSource.addFavorites(TestData.recipe1, TestData.recipe2);

        // Act (When)
        mRecipeRepository.clearFavorites();

        // Assert (Then)
        List<Recipe> favorites = mRecipeRepository.getFavorites();
        assertThat(favorites.size(), is(0));
    }
}