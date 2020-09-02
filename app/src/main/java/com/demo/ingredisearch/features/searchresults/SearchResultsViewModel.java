package com.demo.ingredisearch.features.searchresults;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.demo.ingredisearch.models.Recipe;
import com.demo.ingredisearch.repository.RecipeRepository;
import com.demo.ingredisearch.util.Status;

import java.util.List;
import java.util.stream.Collectors;

public class SearchResultsViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;

    public SearchResultsViewModel(RecipeRepository recipeRepository) {
        this.mRecipeRepository = recipeRepository;
    }

    private String mQuery;

    public LiveData<List<Recipe>> getRecipes() {
        return Transformations.map(mRecipeRepository.getRecipes(), resource -> {
            if (resource.status == Status.SUCCESS) {
                return markFavorites(resource.data);
            } else if (resource.status == Status.ERROR) {
                mDoRetry.setValue(true);
                return null;
            } else {
                // TODO
                return null;
            }
        });

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
        mQuery = query;
        mRecipeRepository.searchRecipes(query);
    }

    private MutableLiveData<Boolean> mDoRetry = new MutableLiveData<>();

    public LiveData<Boolean> doRetry() {
        return mDoRetry;
    }

    public void doneDoRetry() {
        mDoRetry.setValue(false);
    }

    public void markFavorite(Recipe recipe) {
        mRecipeRepository.addFavorite(recipe);
        reload();
    }

    // TODO: inefficient
    private void reload() {
        mRecipeRepository.searchRecipes(mQuery);
    }

    public void unmarkFavorite(Recipe recipe) {
        mRecipeRepository.removeFavorite(recipe);
        reload();
    }

    public void requestRecipeDetails(Recipe recipe) {
        mNavToRecipeDetails.setValue(recipe.getRecipeId());
    }

    private MutableLiveData<String> mNavToRecipeDetails = new MutableLiveData<>();

    public LiveData<String> navToRecipeDetails() {
        return mNavToRecipeDetails;
    }

    public void doneNavToRecipeDetails() {
        mNavToRecipeDetails.setValue(null);
    }

    public void requestFavorites() {
        mNavToFavorites.setValue(true);
    }

    private MutableLiveData<Boolean> mNavToFavorites = new MutableLiveData<>();

    public LiveData<Boolean> navToFavorites() {
        return mNavToFavorites;
    }

    public void doneNavToFavorites() {
        mNavToFavorites.setValue(false);
    }
}
