package com.demo.ingredisearch.repository;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.demo.ingredisearch.models.Recipe;
import com.demo.ingredisearch.repository.sources.ResponseCallback;
import com.demo.ingredisearch.util.Resource;

import java.util.List;

public class RecipeRepository implements FavoritesSource {

    private RemoteDataSource mRemoteDataSource;
    private FavoritesSource mFavoritesSource;

    private static RecipeRepository INSTANCE = null;

    public static RecipeRepository getInstance(RemoteDataSource remoteDataSource, FavoritesSource favoritesSource) {
        if (INSTANCE == null) {
            synchronized (RecipeRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RecipeRepository(remoteDataSource, favoritesSource);
                }
            }
        }
        return INSTANCE;
    }

    private RecipeRepository(RemoteDataSource remoteDataSource, FavoritesSource favoritesSource) {
        this.mRemoteDataSource = remoteDataSource;
        this.mFavoritesSource = favoritesSource;
    }

    private MutableLiveData<Resource<List<Recipe>>> mRecipes = new MutableLiveData<>();

    public LiveData<Resource<List<Recipe>>> getRecipes() {
        return mRecipes;
    }

    private MutableLiveData<Resource<Recipe>> mRecipe = new MutableLiveData<>();

    public LiveData<Resource<Recipe>> getRecipe() {
        return mRecipe;
    }

    public void searchRecipes(String query) {
        mRemoteDataSource.searchRecipes(query, new ResponseCallback<List<Recipe>>() {
            @Override
            public void onDataAvailable(Resource<List<Recipe>> response) {
                mRecipes.postValue(response);
            }

            @Override
            public void onError(Resource<List<Recipe>> response) {
                mRecipes.postValue(response);
            }
        });
    }

    public void searchRecipe(String recipeId) {
        mRemoteDataSource.searchRecipe(recipeId, new ResponseCallback<Recipe>() {
            @Override
            public void onDataAvailable(Resource<Recipe> response) {
                mRecipe.postValue(response);
            }

            @Override
            public void onError(Resource<Recipe> response) {
                mRecipe.postValue(response);
            }
        });
    }

    @VisibleForTesting
    public void destroy() {
        INSTANCE = null;
    }

    @Override
    public void addFavorite(Recipe recipe) {
        mFavoritesSource.addFavorite(recipe);
    }

    @Override
    public void removeFavorite(Recipe recipe) {
        mFavoritesSource.removeFavorite(recipe);
    }

    @Override
    public void clearFavorites() {
        mFavoritesSource.clearFavorites();
    }

    @Override
    public List<Recipe> getFavorites() {
        return mFavoritesSource.getFavorites();
    }
}
