package com.demo.ingredisearch.repository.sources.favorites;

import com.demo.ingredisearch.models.Recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FakeFavoritesSource {

    private List<Recipe> mFavorites = new ArrayList<>();

    public void addFavorite(Recipe recipe) {
        List<Recipe> favorites = getFavorites();
        if (contains(recipe)) return;

        Recipe newFavorite = new Recipe(recipe);
        newFavorite.setFavorite(true);
        favorites.add(newFavorite);
    }

    private boolean contains(Recipe recipe) {
        return mFavorites.stream().anyMatch(r -> r.isSameAs(recipe));
    }

    public void removeFavorite(Recipe recipe) {
        List<Recipe> favorites = getFavorites();
        favorites.removeIf(r -> r.isSameAs(recipe));
    }

    public void clearFavorites() {
        mFavorites.clear();
    }

    public List<Recipe> getFavorites() {
        return mFavorites;
    }

    public void addFavorites(List<Recipe> recipes) {
        recipes.forEach(this::addFavorite);
    }

    public void addFavorites(Recipe... recipes) {
        Arrays.stream(recipes).forEach(this::addFavorite);
    }
}
