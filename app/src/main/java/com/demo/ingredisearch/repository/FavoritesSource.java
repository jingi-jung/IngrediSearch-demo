package com.demo.ingredisearch.repository;

import com.demo.ingredisearch.models.Recipe;

import java.util.List;

public interface FavoritesSource {
    void addFavorite(Recipe recipe);

    void removeFavorite(Recipe recipe);

    void clearFavorites();

    List<Recipe> getFavorites();
}
