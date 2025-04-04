package com.dset.expensetracker.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.dset.expensetracker.dao.ExpenseDAO;
import com.dset.expensetracker.entities.Expense;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private ExpenseDAO mExpenseDAO;

    private LiveData<List<Expense>> mAllExpenses;

    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application){
        ExpenseDatabase db = ExpenseDatabase.getInstance(application);
        mExpenseDAO = db.expenseDao();
        mAllExpenses = mExpenseDAO.getAllExpenses();

    }

    public LiveData<List<Expense>> getAllExpenses(){
        databaseExecutor.execute(() ->{
            mAllExpenses=mExpenseDAO.getAllExpenses();
        });
        return mAllExpenses;
    }

    public void insertExpense(Expense expense){
        databaseExecutor.execute(() -> mExpenseDAO.insert(expense));
    }

    public void updateExpense(Expense expense){
        databaseExecutor.execute(() -> mExpenseDAO.update(expense));
    }

    public void deleteExpense(Expense expense){
        databaseExecutor.execute(() -> mExpenseDAO.delete(expense));
    }

}
