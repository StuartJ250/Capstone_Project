package com.example.expensetracker.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.expensetracker.dao.ExpenseDAO;
import com.example.expensetracker.entities.Expense;

@Database(entities = {Expense.class}, version = 1, exportSchema = false)
public abstract class ExpenseDatabase extends RoomDatabase{

    private static volatile ExpenseDatabase INSTANCE;

    public abstract ExpenseDAO expenseDao();

    public static ExpenseDatabase getInstance(Context context) {
        if (INSTANCE == null){
            synchronized (ExpenseDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ExpenseDatabase.class, "expense_database.db")
                            .fallbackToDestructiveMigration() //prevents crashing
                            .build(); //builds database
                }
            }
        }

        return INSTANCE;
    }
}
