package com.demo.ingredisearch.repository.sources.favorites;

import com.demo.ingredisearch.models.Recipe;
import com.demo.ingredisearch.repository.FavoritesSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FakeFavoritesSource implements FavoritesSource {

    private List<Recipe> mFavorites = new ArrayList<>();

    @Override
    public void addFavorite(Recipe recipe) {
        List<Recipe> favorites = getFavorites();
        if (isDuplicated(favorites, recipe)) return;

        Recipe newFavorite = new Recipe(recipe);
        newFavorite.setFavorite(true);
        favorites.add(newFavorite);
    }

    private boolean isDuplicated(List<Recipe> recipes, Recipe recipe) {
        return recipes.stream().anyMatch(r -> r.isSameAs(recipe));
    }

    @Override
    public void removeFavorite(Recipe recipe) {
        List<Recipe> favorites = getFavorites();
        favorites.removeIf(r -> r.isSameAs(recipe));
    }

    @Override
    public void clearFavorites() {
        mFavorites.clear();
    }

    @Override
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
