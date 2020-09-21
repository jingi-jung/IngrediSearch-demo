package com.demo.ingredisearch;

import android.app.Application;

public class RecipeApplication extends Application {

    private Injection injection;

    @Override
    public void onCreate() {
        super.onCreate();
        this.injection = new Injection(this);
    }

    public Injection getInjection() {
        return injection;
    }
}
