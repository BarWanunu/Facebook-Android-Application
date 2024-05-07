package com.example.foobarapplication.viewModels;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foobarapplication.Globals.GlobalToken;
import com.example.foobarapplication.entities.ApprovalCallback;
import com.example.foobarapplication.entities.Post;
import com.example.foobarapplication.repositories.PostsRepository;

import java.util.List;

public class PostsViewModel extends ViewModel {

    private PostsRepository postsRepository;

    private MutableLiveData<List<Post>> posts;


    private MutableLiveData<Boolean> isGetPosts = new MutableLiveData<>();
    private MutableLiveData<Boolean> isPostAdded = new MutableLiveData<>();
    private MutableLiveData<Boolean> isPostEdited = new MutableLiveData<>();
    private MutableLiveData<Boolean> isPostDeleted = new MutableLiveData<>();
    private MutableLiveData<List<Post>> postList = new MutableLiveData<>();

    public PostsViewModel(Context context) {
        postsRepository = new PostsRepository(context);
        posts = postsRepository.get();

    }

    public MutableLiveData<List<Post>> getPostsByUser(String userID, LifecycleOwner context) {
        posts = postsRepository.getPostsByUser(userID, context);
        return posts;
    }


    public LiveData<List<Post>> getAllPosts(LifecycleOwner context) {
        posts = postsRepository.getAllPosts(GlobalToken.token, context);
        return posts;
    }

    public MutableLiveData<List<Post>> get() {
        posts = postsRepository.get();
        return posts;
    }

    public static Post create(String profile, String text, String dateString, int likes){
        return new Post(profile, text, dateString, likes);
    }

    public MutableLiveData<Boolean> getIsPostDeleted(){
        return isPostDeleted;
    }

    public MutableLiveData<Boolean> getIsGetPosts(){
        return isGetPosts;
    }

    public void add(Post post, LifecycleOwner owner, MutableLiveData<Post> success, ApprovalCallback callback) {
        postsRepository.add(post, GlobalToken.token, owner, success, callback);
    }

    public void delete(Post post, MutableLiveData<Boolean> success, ApprovalCallback callback) {
        postsRepository.delete(post, GlobalToken.token, success, callback);
    }

    public void edit(Post post, MutableLiveData<Boolean> success, ApprovalCallback callback) {
        postsRepository.edit(post, GlobalToken.token, success, callback);
    }

    public void likePost(Post post, MutableLiveData<Post> success) {
        postsRepository.likePost(post, GlobalToken.token, success);
    }

    public void deleteAll() {
        postsRepository.deleteAll();
    }
}
