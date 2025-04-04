package com.dset.expensetracker.ui.report;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dset.expensetracker.database.ExpenseDatabase;
import com.dset.expensetracker.entities.Expense;

import java.util.List;

public class ReportViewModel extends AndroidViewModel {

    private final ExpenseDatabase expenseDatabase;
    private final MutableLiveData<List<Expense>> searchResults;

    public ReportViewModel(Application application) {
        super(application);
        expenseDatabase = ExpenseDatabase.getInstance(application);
        searchResults = new MutableLiveData<>();
    }


    public LiveData<List<Expense>> getSearchResults() {
        return searchResults;
    }

    public void searchExpenses(int userID, String searchQuery){
        expenseDatabase.expenseDao().getSearchExpenses(userID, searchQuery).observeForever(expenses -> {
            searchResults.postValue(expenses);

        });
    }

    public LiveData<List<Expense>> getExpenseUser(int userID) {
        return expenseDatabase.expenseDao().getAssociatedExpenses(userID);
    }


}