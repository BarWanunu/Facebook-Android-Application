package com.example.foobarapplication.DB.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.foobarapplication.entities.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM User WHERE username = username")
    User user(String userName);
    @Insert
    void add(User... users);
}
