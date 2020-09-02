package com.demo.ingredisearch.features.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {

    public void search(String query) {
        if (query == null || query.isEmpty()) {
            mIsEmptyQuery.setValue(true);
        } else {
            mNavToSearchResults.setValue(query);
        }
    }

    private MutableLiveData<String> mNavToSearchResults = new MutableLiveData<>();

    public LiveData<String> navToSearchResults() {
        return mNavToSearchResults;
    }

    public void doneNavToSearchResults() {
        mNavToSearchResults.setValue(null);
    }

    private MutableLiveData<Boolean> mIsEmptyQuery = new MutableLiveData<>();

    public LiveData<Boolean> isEmptyQuery() {
        return mIsEmptyQuery;
    }

    public void doneEmptyQuery() {
        mIsEmptyQuery.setValue(false);
    }
}
