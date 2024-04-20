package com.example.foobarapplication.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foobarapplication.DB.LocalDB;
import com.example.foobarapplication.DB.dao.CommentsDao;
import com.example.foobarapplication.entities.Comment;

import java.util.LinkedList;
import java.util.List;

public class CommentsRepository {
    private CommentsDao dao;
    private CommentListData commentListData;

    public CommentsRepository(Context context) {
        LocalDB db = LocalDB.getInstance(context);
        dao = db.commentsDao();
        commentListData = new CommentListData();
    }

    class CommentListData extends MutableLiveData<List<Comment>> {
        public CommentListData() {
            super();
            setValue(new LinkedList<Comment>());
        }
    }

    public List<Comment> getAllFromDb() {
        return dao.getAllComments();
    }





    public LiveData<List<Comment>> getAll() {
        return commentListData;
    }

    public List<Comment> getCommentsOfPost(int pid) {
        return dao.getCommentsOfPost(pid);
    }


    public void add(final Comment comment) {
        dao.insert(comment);
    }

    public void delete(Comment comment) {
        dao.delete(comment);
    }

    public void update(Comment comment) {
        dao.update(comment);
    }

}