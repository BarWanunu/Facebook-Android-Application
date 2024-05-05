package com.example.foobarapplication.DB.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foobarapplication.entities.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM User WHERE userName = :userName")
    User user(String userName);
    @Insert
    void add(User... users);

    @Delete
    void delete(User user);

    @Query("delete from user where userName=:userName")
    void delete(String userName);

    @Update
    void update(User user);

    @Query("select * from user")
    List<User> getAll();

    @Query("delete from user")
    void deleteAll();
}
