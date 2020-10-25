package com.demo.ingredisearch.repository;

import com.demo.ingredisearch.TestData;
import com.demo.ingredisearch.models.Recipe;
import com.demo.ingredisearch.repository.sources.favorites.FakeFavoritesSource;
import com.demo.ingredisearch.repository.sources.remote.FakeRemoteDataSource;
import com.demo.ingredisearch.repository.sources.remote.FakeRemoteDataSource.DataStatus;
import com.demo.ingredisearch.util.LiveDataTestUtil;
import com.demo.ingredisearch.util.Resource;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class RecipeRepositoryTest {

    // SUT
    RecipeRepository recipeRepository;
    FakeRemoteDataSource mRemoteDataSource;
    FakeFavoritesSource mFavoritesSource;

    @Before
    public void init() {
        mRemoteDataSource = new FakeRemoteDataSource();
        mFavoritesSource = new FakeFavoritesSource();
        recipeRepository = RecipeRepository.getInstance();
    }

    @Test
    public void searchRecipes_whenFailedByNetworkError_returnsErrorResponse() throws InterruptedException {
        // Arrange (Given)

        // Act (When)

        // Assert (Then)

    }

    @Test
    public void searchRecipes_whenFailedWithHTTPError_returnsErrorResponse() throws InterruptedException {
        // Arrange (Given)

        // Act (When)

        // Assert (Then)

    }

    @Test
    public void searchRecipes_whenFailedWithAuthError_returnsErrorResponse() {
        // Arrange (Given)

        // Act (When)

        // Assert (Then)
    }

    @Test
    public void searchRecipes_whenSucceedWithNullResult_returnsEmptyList() {
        // Arrange (Given)

        // Act (When)

        // Assert (Then)

    }

    @Test
    public void searchRecipes_whenSucceed_returnsRecipesList() throws InterruptedException {
        // Arrange (Given)

        // Act (When)

        // Assert (Then)
    }

    @Test
    public void searchRecipe_whenFailedByNetworkError_returnsErrorResponse() throws InterruptedException {
        // Arrange (Given)
        mRemoteDataSource.setDataStatus(DataStatus.NetworkError);

        // Act (When)
        recipeRepository.searchRecipe(TestData.recipe1.getRecipeId());

        // Assert (Then)
        Resource<Recipe> response = LiveDataTestUtil.getOrAwaitValue(recipeRepository.getRecipe());
        assertThat(response, is(Resource.error("Network Error", null)));
    }

    @Test
    public void searchRecipe_whenFailedWithHTTPError_returnsErrorResponse() throws InterruptedException {
        // Arrange (Given)
        mRemoteDataSource.setDataStatus(DataStatus.HTTPError);

        // Act (When)
        recipeRepository.searchRecipe(TestData.recipe1.getRecipeId());

        // Assert (Then)
        Resource<Recipe> response = LiveDataTestUtil.getOrAwaitValue(recipeRepository.getRecipe());
        assertThat(response, is(Resource.error("HTTP Error", null)));
    }

    @Test
    public void searchRecipe_whenFailedWithAuthError_returnsErrorResponse() throws InterruptedException {
        // Arrange (Given)
        mRemoteDataSource.setDataStatus(DataStatus.AuthError);

        // Act (When)
        recipeRepository.searchRecipe(TestData.recipe1.getRecipeId());

        // Assert (Then)
        Resource<Recipe> response = LiveDataTestUtil.getOrAwaitValue(recipeRepository.getRecipe());
        assertThat(response, is(Resource.error("401 Unauthorized. Token may be invalid", null)));
    }

    @Test
    public void searchRecipe_whenSucceedWithNullResult_returnsNull() throws InterruptedException {
        // Arrange (Given)

        // Act (When)
        recipeRepository.searchRecipe(TestData.recipe1.getRecipeId());

        // Assert (Then)
        Resource<Recipe> response = LiveDataTestUtil.getOrAwaitValue(recipeRepository.getRecipe());
        assertThat(response, is(Resource.success(null)));
    }

    @Test
    public void searchRecipe_whenSucceed_returnsRecipe() throws InterruptedException {
        // Arrange (Given)
        mRemoteDataSource.addRecipes(TestData.mRecipes);

        // Act (When)
        recipeRepository.searchRecipe(TestData.recipe1.getRecipeId());

        // Assert (Then)
        Resource<Recipe> response = LiveDataTestUtil.getOrAwaitValue(recipeRepository.getRecipe());
        assertThat(response, is(Resource.success(TestData.recipe1)));
    }

    @Test
    public void getFavorites_noFavorites_returnEmptyList() {
        // Arrange (Given)

        // Act (When)
        List<Recipe> favorites = recipeRepository.getFavorites();

        // Assert (Then)
        assertThat(favorites, hasSize(0));
    }

    @Test
    public void getFavorites_someFavorites_returnAll() {
        // Arrange (Given)
        mFavoritesSource.addFavorites(TestData.mFavorites);

        // Act (When)
        List<Recipe> favorites = recipeRepository.getFavorites();

        // Assert (Then)
        assertThat(favorites, hasSize(2));
    }

    @Test
    public void addFavorites_noDuplicateId_addToFavoritesWithFavoriteStatusAsTrue() {
        // Arrange (Given)
        mFavoritesSource.addFavorite(TestData.recipe1);

        // Act (When)
        recipeRepository.addFavorite(TestData.recipe2);

        // Assert (Then)
        List<Recipe> favorites = recipeRepository.getFavorites();
        assertThat(favorites, hasSize(2));
        assertThat(favoritesStatus(favorites), is(Arrays.asList(true, true)));
    }

    private List<Boolean> favoritesStatus(List<Recipe> favorites) {
        return favorites.stream().map(Recipe::isFavorite).collect(Collectors.toList());
    }

    @Test
    public void addFavorites_recipeWithSameIdAlreadyExists_rejectAddition() {
        // Arrange (Given)
        mFavoritesSource.addFavorite(TestData.recipe1);

        // Act (When)
        recipeRepository.addFavorite(TestData.recipe1);

        // Assert (Then)
        List<Recipe> favorites = recipeRepository.getFavorites();
        assertThat(favorites, hasSize(1));
    }

    @Test
    public void removeFavorite_removesRecipeFromFavorites() {
        // Arrange (Given)
        mFavoritesSource.addFavorite(TestData.recipe1);
        mFavoritesSource.addFavorite(TestData.recipe2);

        // Act (When)
        recipeRepository.removeFavorite(TestData.recipe1_favored);

        // Assert (Then)
        List<Recipe> favorites = recipeRepository.getFavorites();
        assertThat(favorites, hasSize(1));
    }

    @Test
    public void clearFavorites_removeAllFavorites() {
        // Arrange (Given)
        mFavoritesSource.addFavorite(TestData.recipe1);
        mFavoritesSource.addFavorite(TestData.recipe2);

        // Act (When)
        recipeRepository.clearFavorites();

        // Assert (Then)
        List<Recipe> favorites = recipeRepository.getFavorites();
        assertThat(favorites, hasSize(0));
    }

}