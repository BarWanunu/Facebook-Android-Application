package com.example.foobarapplication.DB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.foobarapplication.DB.dao.PostsDao;
import com.example.foobarapplication.entities.Post;

@Database(entities = {Post.class}, version = 1)
 public abstract class LocalDB extends RoomDatabase {
 public abstract PostsDao postDao();
 }