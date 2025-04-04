package com.dset.expensetracker.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dset.expensetracker.entities.Login;

@Dao
public interface LoginDAO {

    @Insert
    void insert(Login login);

    @Update
    void update(Login login);

    @Delete
    void delete(Login login);

    @Query("SELECT * FROM Accounts WHERE userName = :username AND password = :password LIMIT 1")
    Login loginUser(String username, String password);

    @Query("SELECT userID FROM Accounts WHERE userName = :username LIMIT 1")
    int getUserID(String username);

    @Query("SELECT userName FROM Accounts WHERE userID = :userID LIMIT 1")
        String getUserName(int userID);

}
