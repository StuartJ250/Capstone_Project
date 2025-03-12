package com.example.expensetracker.ui.expense;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.expensetracker.database.ExpenseDatabase;
import com.example.expensetracker.database.Repository;
import com.example.expensetracker.entities.Expense;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class ExpenseViewModel extends AndroidViewModel {

    private final Repository repository;
    private final LiveData<List<Expense>> allExpenses;

    public ExpenseViewModel(Application application) {
        super(application);
        repository = new Repository(application);
        allExpenses = repository.getAllExpenses();

    }

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public void insertExpense(Expense expense){
        repository.insertExpense(expense);
    }

    public void deleteExpense(Expense expense){
        repository.deleteExpense(expense);
    }

    public void updateExpense(Expense expense){
        repository.updateExpense(expense);
    }

}