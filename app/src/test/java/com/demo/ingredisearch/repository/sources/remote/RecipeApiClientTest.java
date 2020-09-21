package com.demo.ingredisearch.repository.sources.remote;

import com.demo.ingredisearch.TestData;
import com.demo.ingredisearch.models.Recipe;
import com.demo.ingredisearch.repository.sources.ResponseCallback;
import com.demo.ingredisearch.util.Resource;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecipeApiClientTest {

    // SUT
    RecipeApiClient mClient;

    @Test
    public void searchRecipes_whenFailedByNetworkError_returnsErrorResponse() {
        // Arrange (Given)

        // Act (When)

        // Assert (Then)

    }

    @Test
    public void searchRecipes_whenFailedWithHTTPError_returnsErrorResponse() {
// Arrange (Given)

        // Act (When)

        // Assert (Then)
    }

    @Test
    public void searchRecipes_whenFailedWithAuthError_returnsErrorResponse() {
        // Arrange (Given)

        // Act (When)

        // Assert (Then)

    }

    @Test
    public void searchRecipes_whenSucceedWithNullResult_returnsEmptyList() {
        // Arrange (Given)

        // Act (When)

        // Assert (Then)
    }

    @Test
    public void searchRecipes_whenSucceed_returnsRecipesList() {
        // Arrange (Given)

        // Act (When)

        // Assert (Then)

    }

    @Mock
    Call<RecipeResponse> recipeResponseCall;

    @Mock
    Response<RecipeResponse> recipeResponseResponse;

    @Captor
    ArgumentCaptor<Callback<RecipeResponse>> mCaptor;

    @Test
    public void searchRecipe_whenFailedByNetworkError_returnsErrorResponse() {
        // Arrange (Given)

        // Act (When)
        mClient.searchRecipe(TestData.recipe1.getRecipeId(), new ResponseCallback<Recipe>() {
            @Override
            public void onDataAvailable(Resource<Recipe> response) {
                fail("should not be called");
            }

            @Override
            public void onError(Resource<Recipe> response) {
                // Assert (Then)
                assertThat(response, is(Resource.error("Network Error", null)));
            }
        });

        verify(recipeResponseCall).enqueue(mCaptor.capture());
        mCaptor.getValue().onFailure(recipeResponseCall, new IOException("Network Error"));
    }

    @Test
    public void searchRecipe_whenFailedWithHTTPError_returnsErrorResponse() {
        // Arrange (Given)
//        when(recipeSearchResponseResponse.isSuccessful()).thenReturn(false);
        when(recipeResponseResponse.message()).thenReturn("HTTP Error");

        // Act (When)
        mClient.searchRecipe(TestData.recipe1.getRecipeId(), new ResponseCallback<Recipe>() {
            @Override
            public void onDataAvailable(Resource<Recipe> response) {
                fail("should not be called");
            }

            @Override
            public void onError(Resource<Recipe> response) {
                // Assert (Then)
                assertThat(response, is(Resource.error("HTTP Error", null)));
            }
        });

        verify(recipeResponseCall).enqueue(mCaptor.capture());
        mCaptor.getValue().onResponse(recipeResponseCall, recipeResponseResponse);
    }

    @Test
    public void searchRecipe_whenFailedWithAuthError_returnsErrorResponse() {
        // Arrange (Given)
        when(recipeResponseResponse.isSuccessful()).thenReturn(true);
        when(recipeResponseResponse.code()).thenReturn(401);
        when(recipeResponseResponse.message()).thenReturn("401 Unauthorized. Token may be invalid");

        // Act (When)
        mClient.searchRecipe(TestData.recipe1.getRecipeId(), new ResponseCallback<Recipe>() {
            @Override
            public void onDataAvailable(Resource<Recipe> response) {
                fail("should not be called");
            }

            @Override
            public void onError(Resource<Recipe> response) {
                // Assert (Then)
                assertThat(response, is(Resource.error("401 Unauthorized. Token may be invalid", null)));
            }
        });

        verify(recipeResponseCall).enqueue(mCaptor.capture());
        mCaptor.getValue().onResponse(recipeResponseCall, recipeResponseResponse);
    }

    @Test
    public void searchRecipe_whenSucceedWithNullResult_returnsNull() {
        // Arrange (Given)
        when(recipeResponseResponse.isSuccessful()).thenReturn(true);
        when(recipeResponseResponse.code()).thenReturn(200);
        when(recipeResponseResponse.body()).thenReturn(null);

        // Act (When)
        mClient.searchRecipe(TestData.recipe1.getRecipeId(), new ResponseCallback<Recipe>() {
            @Override
            public void onDataAvailable(Resource<Recipe> response) {
                // Assert (Then)
                assertThat(response, is(Resource.success(null)));
            }

            @Override
            public void onError(Resource<Recipe> response) {
                fail("should not be called");
            }
        });

        verify(recipeResponseCall).enqueue(mCaptor.capture());
        mCaptor.getValue().onResponse(recipeResponseCall, recipeResponseResponse);
    }

    @Test
    public void searchRecipe_whenSucceed_returnsRecipe() {
        // Arrange (Given)
        when(recipeResponseResponse.isSuccessful()).thenReturn(true);
        when(recipeResponseResponse.code()).thenReturn(200);
        when(recipeResponseResponse.body()).thenReturn(new RecipeResponse(TestData.recipe1));

        // Act (When)
        mClient.searchRecipe(TestData.recipe1.getRecipeId(), new ResponseCallback<Recipe>() {
            @Override
            public void onDataAvailable(Resource<Recipe> response) {
                // Assert (Then)
                assertThat(response, is(Resource.success(TestData.recipe1)));
            }

            @Override
            public void onError(Resource<Recipe> response) {
                fail("should not be called");
            }
        });

        verify(recipeResponseCall).enqueue(mCaptor.capture());
        mCaptor.getValue().onResponse(recipeResponseCall, recipeResponseResponse);
    }

    /**/

    @Mock
    ResponseCallback<Recipe> recipeResponseCallback;

    @Test
    public void searchRecipe_whenSucceed_returnsRecipe2() {
        // Arrange (Given)
        when(recipeResponseResponse.isSuccessful()).thenReturn(true);
        when(recipeResponseResponse.code()).thenReturn(200);
        when(recipeResponseResponse.body()).thenReturn(new RecipeResponse(TestData.recipe1));

        // Act (When)
        mClient.searchRecipe(TestData.recipe1.getRecipeId(), recipeResponseCallback);

        // Assert (Then)
        verify(recipeResponseCall).enqueue(mCaptor.capture());
        mCaptor.getValue().onResponse(recipeResponseCall, recipeResponseResponse);
        verify(recipeResponseCallback).onDataAvailable(Resource.success(TestData.recipe1));
    }

}
