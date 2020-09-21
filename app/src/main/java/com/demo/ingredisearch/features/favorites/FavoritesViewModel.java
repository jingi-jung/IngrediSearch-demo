package com.demo.ingredisearch.features.favorites;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.demo.ingredisearch.models.Recipe;
import com.demo.ingredisearch.repository.RecipeRepository;

import java.util.List;

public class FavoritesViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;

    public FavoritesViewModel(RecipeRepository recipeRepository) {
        this.mRecipeRepository = recipeRepository;
    }

    private MutableLiveData<List<Recipe>> mFavorites = new MutableLiveData<>();

    public LiveData<List<Recipe>> getFavorites() {
        reload();
        return mFavorites;
    }

    public void removeFavorite(Recipe recipe) {
        mRecipeRepository.removeFavorite(recipe);
        reload();
    }

    private void reload() {
        mFavorites.setValue(mRecipeRepository.getFavorites());
    }

    public void clearFavorites() {
        mRecipeRepository.clearFavorites();
        reload();
    }

    public void requestRecipeDetails(String recipeId) {
        mRecipeId.setValue(recipeId);
    }

    private MutableLiveData<String> mRecipeId = new MutableLiveData<>();

    public LiveData<String> navToRecipeDetails() {
        return mRecipeId;
    }

    public void doneNavToRecipeDetails() {
        mRecipeId.setValue(null);
    }
}
