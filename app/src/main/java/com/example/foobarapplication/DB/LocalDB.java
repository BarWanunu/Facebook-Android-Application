package com.example.foobarapplication.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.foobarapplication.DB.dao.CommentsDao;
import com.example.foobarapplication.DB.dao.PostsDao;
import com.example.foobarapplication.DB.dao.UserDao;
import com.example.foobarapplication.entities.Comment;
import com.example.foobarapplication.entities.Post;
import com.example.foobarapplication.entities.User;

@Database(entities = {Post.class, User.class, Comment.class}, version = 1)
 public abstract class LocalDB extends RoomDatabase {
 private static volatile LocalDB INSTANCE;
 public static LocalDB getInstance(Context context) {
  if (INSTANCE == null) {
   synchronized (LocalDB.class) {
    if (INSTANCE == null) {
     INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
             LocalDB.class, "local-db").allowMainThreadQueries().build();
    }
   }
  }
  return INSTANCE;
 }

 public abstract PostsDao postDao();
 public abstract UserDao userDao();
 public abstract CommentsDao commentsDao();
 }