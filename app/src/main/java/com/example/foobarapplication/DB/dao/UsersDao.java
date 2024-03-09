package com.example.foobarapplication.DB.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foobarapplication.entities.User;

import java.util.List;

@Dao
public interface UsersDao {
    @Query("SELECT * FROM User")
    List<User> list();

    @Query("SELECT * FROM User WHERE username = username")
    User user(String userName);

    @Insert
    void insert(User... users);

    @Update
    void update(User... users);

    @Delete
    void delete(User... users);
}
