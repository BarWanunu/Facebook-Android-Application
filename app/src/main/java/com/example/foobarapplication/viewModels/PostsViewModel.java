package com.example.foobarapplication.viewModels;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foobarapplication.Globals.GlobalToken;
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

    public LiveData<List<Post>> getPostsByUser(String userID, LifecycleOwner context) {
        posts = postsRepository.getPostsByUser(userID, context);
        return posts;
    }


    public LiveData<List<Post>> getFromCloud(LifecycleOwner context) {
        posts = postsRepository.getAllPosts(GlobalToken.token, context);
        return posts;
    }

    public MutableLiveData<List<Post>> get() {
        posts = postsRepository.get();
        return posts;
    }

//    public LiveData<List<Post>> getAllFromDb(String token) {
//        posts = postsRepository.getAllFromDb(isGetPosts, token);
//        return posts;
//    }
    public static Post create(String profile, String text, String dateString, int likes){
        return new Post(profile, text, dateString, likes);
    }

    public MutableLiveData<Boolean> getIsPostDeleted(){
        return isPostDeleted;
    }

    public MutableLiveData<Boolean> getIsGetPosts(){
        return isGetPosts;
    }

    public void add(Post post, LifecycleOwner owner) {
        postsRepository.add(post, GlobalToken.token, owner);
    }

    public void delete(Post post) {
        postsRepository.delete(post, GlobalToken.token);
    }

    public void edit(Post post) {
        postsRepository.edit(post, GlobalToken.token);
    }

    public void likePost(Post post, LifecycleOwner owner) {
        postsRepository.likePost(post, GlobalToken.token, owner);
    }

    public void deleteAll() {
        postsRepository.deleteAll();
    }
}
