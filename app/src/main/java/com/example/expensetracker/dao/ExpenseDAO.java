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
}
