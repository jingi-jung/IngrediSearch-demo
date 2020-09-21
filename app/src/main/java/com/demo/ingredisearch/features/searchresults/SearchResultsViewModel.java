package com.demo.ingredisearch.features.searchresults;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.demo.ingredisearch.models.Recipe;
import com.demo.ingredisearch.repository.RecipeRepository;

import java.util.List;
import java.util.stream.Collectors;

public class SearchResultsViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;

    public SearchResultsViewModel(RecipeRepository recipeRepository) {
        this.mRecipeRepository = recipeRepository;
    }

    public LiveData<List<Recipe>> getRecipes() {
        // TODO
        return null;
    }

    private List<Recipe> markFavorites(List<Recipe> recipes) {
        List<Recipe> favorites = mRecipeRepository.getFavorites();

        return recipes.stream().map(recipe -> {
            if (favorites.stream().anyMatch(f -> f.isSameAs(recipe))) {
                Recipe temp = new Recipe(recipe);
                temp.setFavorite(true);
                return temp;
            } else {
                return recipe;
            }
        }).collect(Collectors.toList());
    }

    public void searchRecipes(String query) {
        mRecipeRepository.searchRecipes(query);
    }

    public void markFavorite(Recipe recipe) {
        mRecipeRepository.addFavorite(recipe);
    }

    public void unmarkFavorite(Recipe recipe) {
        mRecipeRepository.removeFavorite(recipe);
    }

}
