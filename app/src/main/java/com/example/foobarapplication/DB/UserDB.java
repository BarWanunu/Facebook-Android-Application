package com.example.foobarapplication.DB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.foobarapplication.DB.dao.UsersDao;
import com.example.foobarapplication.entities.User;

@Database(entities = {User.class}, version = 1)
public abstract class UserDB extends RoomDatabase {
    public abstract UsersDao usersDao();

    public static UserDB getInstance() {
        return null;
    }


}