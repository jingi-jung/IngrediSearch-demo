package com.demo.ingredisearch.features.details;

import android.support.v4.app.INotificationSideChannel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.demo.ingredisearch.models.Recipe;
import com.demo.ingredisearch.repository.RecipeRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class RecipeDetailsViewModelTest {

    // SUT
    RecipeDetailsViewModel mViewModel;

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    RecipeRepository mRecipeRepository;

    @Before
    public void init() {
        mRecipeRepository = RecipeRepository.getInstance(/* TODO */);
        mViewModel = new RecipeDetailsViewModel(mRecipeRepository);
    }

    @Test
    public void searchRecipe_returnsThatRecipe() throws InterruptedException {
        // Given

        // When

        // Then
    }

    @Test
    public void searchRecipe_noMatch_returnsNull() throws InterruptedException {
        // Given

        // When

        // Then
    }

    @Test
    public void searchRecipe_error_returnsError() throws InterruptedException {
        // Given

        // When

        // Then
    }
}