package com.example.foobarapplication.DB;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.foobarapplication.Converters;
import com.example.foobarapplication.DB.dao.PostsDao;
import com.example.foobarapplication.entities.Post;

@TypeConverters(Converters.class)
@Database(entities = {Post.class}, version = 1)
 public abstract class LocalDB extends RoomDatabase {
 public abstract PostsDao postDao();
 }