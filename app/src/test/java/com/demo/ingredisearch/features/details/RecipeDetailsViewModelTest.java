package com.demo.ingredisearch.features.details;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.demo.ingredisearch.TestData;
import com.demo.ingredisearch.models.Recipe;
import com.demo.ingredisearch.repository.RecipeRepository;
import com.demo.ingredisearch.repository.sources.remote.FakeRemoteDataSource;
import com.demo.ingredisearch.repository.sources.remote.FakeRemoteDataSource.DataStatus;
import com.demo.ingredisearch.util.LiveDataTestUtil;
import com.demo.ingredisearch.util.Resource;
import com.demo.ingredisearch.util.SingleExecutors;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RecipeDetailsViewModelTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    // SUT
    RecipeDetailsViewModel mViewModel;

    RecipeRepository mRecipeRepository;
    FakeRemoteDataSource mRemoteDataSource;

    @Before
    public void init() {
        mRemoteDataSource = new FakeRemoteDataSource(new SingleExecutors());
        mRecipeRepository = RecipeRepository.getInstance(mRemoteDataSource, null);
        mViewModel = new RecipeDetailsViewModel(mRecipeRepository);
    }

    @After
    public void wrapUp() {
        mRecipeRepository.destroy();
    }

    @Test
    public void searchRecipe_returnsThatRecipe() throws InterruptedException {
        // Given
        mRemoteDataSource.addRecipes(TestData.mRecipes);

        // When
        mViewModel.searchRecipe(TestData.recipe1.getRecipeId());

        // Then
        Resource<Recipe> resource = LiveDataTestUtil.getOrAwaitValue(mViewModel.getRecipe());
        assertThat(resource, is(Resource.success(TestData.recipe1)));
    }

    @Test
    public void searchRecipe_noMatch_returnsNull() throws InterruptedException {
        // Given

        // When
        mViewModel.searchRecipe(TestData.recipe1.getRecipeId());

        // Then
        Resource<Recipe> resource = LiveDataTestUtil.getOrAwaitValue(mViewModel.getRecipe());
        assertThat(resource, is(Resource.success(null)));
    }

    @Test
    public void searchRecipe_error_returnsError() throws InterruptedException {
        // Given
        mRemoteDataSource.setDataStatus(DataStatus.NetworkError);

        // When
        mViewModel.searchRecipe(TestData.recipe1.getRecipeId());

        // Then
        Resource<Recipe> resource = LiveDataTestUtil.getOrAwaitValue(mViewModel.getRecipe());
        assertThat(resource, is(Resource.error("Network Error", null)));
    }
}