package com.demo.ingredisearch.repository.sources.favorites;

import android.content.Context;
import android.content.SharedPreferences;

import com.demo.ingredisearch.models.Recipe;
import com.demo.ingredisearch.repository.FavoritesSource;
import com.demo.ingredisearch.repository.util.JsonConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SharedPreferencesFavoritesSource implements FavoritesSource {

    public static final String FAVORITES_KEY = "FAVORITE_KEY";

    private SharedPreferences mPreferences;

    public SharedPreferencesFavoritesSource(Context context) {
        mPreferences = context.getSharedPreferences(FAVORITES_KEY, Context.MODE_PRIVATE);
    }

    @Override
    public void addFavorite(Recipe recipe) {
        List<Recipe> favorites = new ArrayList<>(getFavorites());
        if (isDuplicated(favorites, recipe)) return;

        Recipe newFavorite = new Recipe(recipe);
        newFavorite.setFavorite(true);
        favorites.add(newFavorite);
        saveFavorites(favorites);
    }

    private boolean isDuplicated(List<Recipe> recipes, Recipe recipe) {
        return recipes.stream().anyMatch(r -> r.isSameAs(recipe));
    }

    private void saveFavorites(List<Recipe> favorites) {
        mPreferences.edit().putString(FAVORITES_KEY, JsonConverter.toJson(favorites)).apply();
    }

    @Override
    public void removeFavorite(Recipe recipe) {
        List<Recipe> favorites = new ArrayList<>(getFavorites());
        favorites.removeIf(r -> r.isSameAs(recipe));
        saveFavorites(favorites);
    }

    @Override
    public void clearFavorites() {
        mPreferences.edit().clear().apply();
    }

    @Override
    public List<Recipe> getFavorites() {
        String stringifiedFavorites = mPreferences.getString(FAVORITES_KEY, null);
        return stringifiedFavorites != null ? JsonConverter.toRecipes(stringifiedFavorites) : Collections.emptyList();
    }
}
