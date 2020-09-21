package com.demo.ingredisearch.features.favorites;

import androidx.fragment.app.testing.FragmentScenario;

import com.demo.ingredisearch.R;

import org.junit.Test;

public class FavoritesFragmentTest {

    @Test
    public void favoritesFragmentInView_withFavorites() throws Exception {
        // Arrange (Given)

        // Act (When)
        FragmentScenario.launchInContainer(FavoritesFragment.class, null, R.style.AppTheme, null);

        // Assert (Then)

    }

    @Test
    public void favoritesFragmentInView_withNoFavorites() {
        // Given

        // When


        // Then - No favorites yet

    }

    @Test
    public void selectRecipeAsNonFavorite_removesTheRecipeFromView() throws Exception {
        // Arrange (Given)

        // Act (When)

        // Assert (Then)

    }

    @Test
    public void selectLastRecipeAsNonFavorite_displaysNoFavorites() throws Exception {
        // Arrange (Given)

        // Act (When)

        // Assert (Then)

    }

    @Test
    public void navigateToRecipeDetailsView() {
        // Arrange (Given)

        // Act (When)

        // Assert (Then)

    }

}