package com.dset.expensetracker.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dset.expensetracker.dao.ExpenseDAO;
import com.dset.expensetracker.dao.LoginDAO;
import com.dset.expensetracker.entities.Expense;
import com.dset.expensetracker.entities.Login;

@Database(entities = {Expense.class, Login.class}, version = 3, exportSchema = false)
public abstract class ExpenseDatabase extends RoomDatabase{

    private static volatile ExpenseDatabase INSTANCE;

    public abstract ExpenseDAO expenseDao();
    public abstract LoginDAO loginDAO();

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

    public static void setTestInstance(ExpenseDatabase testInstance){
        INSTANCE = testInstance;
    }
}
