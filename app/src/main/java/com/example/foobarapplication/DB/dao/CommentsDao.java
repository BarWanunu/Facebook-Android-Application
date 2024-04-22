package com.example.foobarapplication.DB.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foobarapplication.entities.Comment;

import java.util.List;

@Dao
public interface CommentsDao {
    @Insert
    void insert(Comment comment);
    @Delete
    void delete(Comment comment);
    @Query("select * from comment")
    List<Comment> getAllComments();

    @Update
    void update(Comment comment);

    @Query("select * from comment where postId=:pid")
    List<Comment> getCommentsOfPost(int pid);
}
