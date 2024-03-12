package com.example.foobarapplication.DB.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import com.example.foobarapplication.Converters;
import com.example.foobarapplication.entities.User;

@TypeConverters(Converters.class)
@Dao
public interface UserDao {
    @Query("SELECT * FROM User WHERE username = username")
    User user(String userName);
    @Insert
    void add(User... users);
}
