package com.example.foobarapplication.repositories;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.foobarapplication.DB.LocalDB;
import com.example.foobarapplication.DB.dao.PostsDao;
import com.example.foobarapplication.entities.Post;
import com.example.foobarapplication.webServices.PostAPI;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PostsRepository {
    private PostsDao dao;
    private PostListData postListData;
    private PostAPI api;

    public PostsRepository(Context context) {
        LocalDB db = LocalDB.getInstance(context);
        dao = db.postDao();
        postListData = new PostListData();
        api = new PostAPI();
    }

    class PostListData extends MutableLiveData<List<Post>> {
        public PostListData() {
            super();
            setValue(new LinkedList<Post>());
        }
    }

    public MutableLiveData<List<Post>> getAllPosts(String token, LifecycleOwner context) {
        api.getAllPosts(token, postListData);
        Observer<List<Post>> observer = new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                if (posts != null && !posts.isEmpty()) {
                    Post[] posts2 = postListData.getValue().toArray(new Post[0]);
                    dao.insert(posts2);
                    postListData.removeObserver(this);
                }
            }
        };
        postListData.observe(context, observer);
        return postListData;
    }

    public MutableLiveData<List<Post>> getPostsByUser(String userID, LifecycleOwner context) {
        MutableLiveData<List<Post>> userPosts = new MutableLiveData<>();
        api.getPostsByUser(userID, userPosts);
        userPosts.observe(context, new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                postListData.setValue(posts);
                userPosts.removeObserver(this);
            }
        });
        return postListData;
    }


    public MutableLiveData<List<Post>> get() {
        return postListData;
    }


    public void add(final Post post, String token, LifecycleOwner owner) {
        MutableLiveData<Post> success = new MutableLiveData<>();
        api.add(post, token, success);
        success.observe(owner, new Observer<Post>() {
            @Override
            public void onChanged(Post post) {
                dao.insert(post);
                List<Post> posts = get().getValue();
                posts.add(post);
                //Collections.sort(posts);
                postListData.setValue(posts);
            }
        });

    }

    public void delete(Post post, String token, MutableLiveData<Boolean> success) {
        api.delete(post, token, success);
        dao.delete(post);
    }

    public void edit(Post post, String token, MutableLiveData<Boolean> success) {
        api.edit(post, token, success);
    }

    public void likePost(Post post, String token, MutableLiveData<Post> success) {
        api.likePost(post, token, success);
    }

    public void deleteAll() {
        dao.deleteAll();
    }

}