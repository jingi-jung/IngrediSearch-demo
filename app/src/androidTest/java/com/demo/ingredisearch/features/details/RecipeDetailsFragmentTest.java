package com.demo.ingredisearch.features.details;

import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ApplicationProvider;

import com.demo.ingredisearch.R;
import com.demo.ingredisearch.RecipeApplication;
import com.demo.ingredisearch.TestData;
import com.demo.ingredisearch.repository.RecipeRepository;
import com.demo.ingredisearch.repository.sources.remote.FakeRemoteDataSource;
import com.demo.ingredisearch.util.SingleExecutors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class RecipeDetailsFragmentTest {

    RecipeRepository mRecipeRepository;
    FakeRemoteDataSource mRemoteDataSource;
    Bundle args;

    @Before
    public void init() {
        mRemoteDataSource = new FakeRemoteDataSource(new SingleExecutors());
        mRecipeRepository = RecipeRepository.getInstance(mRemoteDataSource, null);
        RecipeApplication app = ApplicationProvider.getApplicationContext();
        app.getInjection().setRecipeRepository(mRecipeRepository);

        args = new RecipeDetailsFragmentArgs.Builder(TestData.recipe1.getRecipeId()).build().toBundle();
    }

    @After
    public void wrapUp() {
        mRecipeRepository.destroy();
    }

    @Test
    public void recipeDetailsFragmentInView() {
        mRemoteDataSource.addRecipes(TestData.mRecipes);

        FragmentScenario.launchInContainer(RecipeDetailsFragment.class, args, R.style.AppTheme, null);

        // Assert (Then)
        onView(withId(R.id.recipe_image)).check(matches(isDisplayed()));
        onView(withId(R.id.recipe_title)).check(matches(isDisplayed()));
        onView(withId(R.id.recipe_social_score)).check(matches(isDisplayed()));
        onView(withId(R.id.ingredients_title)).check(matches(isDisplayed()));
        onView(withId(R.id.ingredients_container)).check(matches(isDisplayed()));
    }

}