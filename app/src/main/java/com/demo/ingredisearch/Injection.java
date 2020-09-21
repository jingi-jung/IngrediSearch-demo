package com.demo.ingredisearch;

import android.content.Context;

import com.demo.ingredisearch.repository.RecipeRepository;

public class Injection {

    private Context mContext;
    private RecipeRepository mRecipeRepository;

    public Injection(Context context) {
        this.mContext = context;
    }

    public RecipeRepository getRecipeRepository() {
        // TODO
        return mRecipeRepository;
    }
}
