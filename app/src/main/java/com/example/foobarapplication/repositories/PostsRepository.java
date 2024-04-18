package com.example.foobarapplication.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foobarapplication.DB.dao.PostsDao;
import com.example.foobarapplication.entities.Post;
import com.example.foobarapplication.webServices.PostAPI;

import java.util.LinkedList;
import java.util.List;
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
        Semaphore semaphore = new Semaphore(0);

        api.getAllPosts(isGetPosts, postListData, token,context,semaphore);
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("getAllFromDb", "isGetPosts value: " + isGetPosts.getValue());

        // Log the value of postListData
        List<Post> posts = postListData.getValue();
        if (posts != null) {
            Log.d("getAllFromDb", "postListData size: " + posts.size());
        } else {
            Log.d("getAllFromDb", "postListData is null");
        }

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