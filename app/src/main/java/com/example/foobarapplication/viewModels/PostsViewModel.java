package com.example.foobarapplication.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foobarapplication.entities.Post;
import com.example.foobarapplication.entities.User;
import com.example.foobarapplication.repositories.PostsRepository;
import com.example.foobarapplication.repositories.UserRepository;

import java.util.List;

public class PostsViewModel extends ViewModel {

    private PostsRepository postsRepository;

    private LiveData<List<Post>> posts;


    private MutableLiveData<Boolean> isGetPosts = new MutableLiveData<>();
    private MutableLiveData<Boolean> isPostAdded = new MutableLiveData<>();
    private MutableLiveData<Boolean> isPostEdited = new MutableLiveData<>();
    private MutableLiveData<Boolean> isPostDeleted = new MutableLiveData<>();
    private MutableLiveData<List<Post>> postList = new MutableLiveData<>();

    public PostsViewModel() {
        postsRepository = new PostsRepository();
        posts = postsRepository.getAll();

    }

    public LiveData<List<Post>> get(String token) {
        posts = postsRepository.getAllFromDb(isGetPosts, token);
        return posts;
    }

    public LiveData<List<Post>> getAllFromDb(String token) {
        posts = postsRepository.getAllFromDb(isGetPosts, token);
        return posts;
    }

    public MutableLiveData<Boolean> getIsPostDeleted(){
        return isPostDeleted;
    }

    public MutableLiveData<Boolean> getIsGetPosts(){
        return isGetPosts;
    }

    public void delete(Post post, String token) {
        postsRepository.delete(post, isPostDeleted, token);
    }
}
