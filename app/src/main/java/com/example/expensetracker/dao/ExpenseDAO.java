package com.example.expensetracker.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.expensetracker.entities.Expense;

import java.util.List;

@Dao
public interface ExpenseDAO {

    @Insert
    void insert(Expense expense);

    @Update
    void update(Expense expense);

    @Delete
    void delete(Expense expense);

    @Query("SELECT * FROM expenses ORDER BY expenseID DESC")
    LiveData<List<Expense>> getAllExpenses();

    @Query("SELECT * FROM expenses WHERE userID = :uID ORDER BY expenseID ASC")
    LiveData<List<Expense>> getAssociatedExpenses(int uID);

    @Query("SELECT * FROM expenses WHERE userID = :uID AND (name LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%') ORDER BY expenseID ASC")
    LiveData<List<Expense>> getSearchExpenses(int uID, String searchQuery);

    @Query("SELECT * FROM expenses WHERE expenseID = :eID LIMIT 1")
    Expense getExpense(int eID);

}
