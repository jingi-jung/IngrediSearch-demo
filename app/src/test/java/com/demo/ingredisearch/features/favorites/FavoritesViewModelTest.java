package com.demo.ingredisearch.features.favorites;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.demo.ingredisearch.repository.RecipeRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class FavoritesViewModelTest {

    // SUT
    FavoritesViewModel mViewModel;

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    RecipeRepository mRecipeRepository;

    @Before
    public void init() {
        mRecipeRepository = RecipeRepository.getInstance(/* TODO */);
        mViewModel = new FavoritesViewModel(mRecipeRepository);
    }

    @Test
    public void getFavorites_returnFavoriteRecipes() throws Exception {
        // Arrange (Given)

        // Act (When)

        // Assert (Then)
    }

    @Test
    public void removeFavorite_shouldRemoveFavorite() {
        // Arrange (Given)

        // Act (When)

        // Assert (Then)
    }

    @Test
    public void clearFavorites_shouldResetFavoritesToEmpty() throws Exception {
        // Arrange (Given)

        // Act (When)

        // Assert (Then)
    }

    @Test
    public void requestToRecipeDetails_shouldTriggerNavToRecipeDetails() throws Exception {
        // Arrange (Given)

        // Act (When)

        // Assert (Then)
    }

}
