package com.dset.expensetracker.ui.home;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class HomeViewModel extends AndroidViewModel {

    private final MutableLiveData<String> welcome = new MutableLiveData<>();

    public HomeViewModel(Application application) {
        super(application);
        loadWelcome(application);
    }

    private void loadWelcome(Application application) {
        SharedPreferences preferences = application.getSharedPreferences("user_prefs", application.MODE_PRIVATE);
        String firstName = preferences.getString("firstName", "User");
        String lastName = preferences.getString("lastName", "");

        welcome.setValue("Welcome, " + firstName + " " + lastName + "!");
    }

    public LiveData<String> getWelcome() {
        return welcome;
    }
}