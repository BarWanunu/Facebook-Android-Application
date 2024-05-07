package com.example.foobarapplication.repositories;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.foobarapplication.DB.LocalDB;
import com.example.foobarapplication.DB.dao.PostsDao;
import com.example.foobarapplication.entities.ApprovalCallback;
import com.example.foobarapplication.entities.Post;
import com.example.foobarapplication.webServices.PostAPI;

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

    // Retrieves all posts from the API and updates the LiveData with the retrieved data
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

    // Retrieves posts by a specific user from the API and updates the LiveData with the retrieved data
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

    // Retrieves the LiveData containing the list of posts
    public MutableLiveData<List<Post>> get() {
        return postListData;
    }

    // Adds a new post and observes the success LiveData for database insertion
    public void add(final Post post, String token, LifecycleOwner owner, MutableLiveData<Post> success, ApprovalCallback callback) {
        api.add(post, token, success, callback);
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

    // Deletes a post and observes the success LiveData for database deletion
    public void delete(Post post, String token, MutableLiveData<Boolean> success, ApprovalCallback callback) {
        api.delete(post, token, success, callback);
        dao.delete(post);
    }

    // Edits a post and observes the success LiveData for update
    public void edit(Post post, String token, MutableLiveData<Boolean> success, ApprovalCallback callback) {
        api.edit(post, token, success, callback);
    }

    // Likes a post and observes the success LiveData for update
    public void likePost(Post post, String token, MutableLiveData<Post> success) {
        api.likePost(post, token, success);
    }

    // Deletes all posts from the local database
    public void deleteAll() {
        dao.deleteAll();
    }
}