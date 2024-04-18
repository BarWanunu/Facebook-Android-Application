package com.example.foobarapplication.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.foobarapplication.DB.dao.PostsDao;
import com.example.foobarapplication.activities.ApiResponseCallback;
import com.example.foobarapplication.entities.Post;
import com.example.foobarapplication.webServices.PostAPI;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class PostsRepository {
    private PostsDao dao;
    private PostListData postListData;
    private PostAPI api;

    public PostsRepository() {
        //LocalDatabase db = LocalDatabase.getInstance();
        //dao = db.postDao();
        postListData = new PostListData();
        api = new PostAPI();
    }

    class PostListData extends MutableLiveData<List<Post>> {
        public PostListData() {
            super();
            setValue(new LinkedList<Post>());
        }
    }

    public LiveData<List<Post>> getAllFromDb(MutableLiveData<Boolean> isGetPosts, String token, Context context) {
        MutableLiveData<List<Post>> postData = new MutableLiveData<>();

        api.getAllPosts( token,postListData);

        return postListData;
    }





    public LiveData<List<Post>> getAll() {
        return postListData;
    }




    public void add(final Post post) {
        api.add(post);
    }

    public void delete(Post post, MutableLiveData<Boolean> isPostDeleted, String token) {
        api.delete(post, isPostDeleted, token);
    }

}