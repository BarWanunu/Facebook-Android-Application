package com.example.foobarapplication.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foobarapplication.repositories.PostsRepository;
import com.example.foobarapplication.repositories.UserRepository;

public class PostsViewModel extends ViewModel {

    private PostsRepository postsRepository;

    private MutableLiveData<Boolean> isPostAdded = new MutableLiveData<>();
    private MutableLiveData<Boolean> isPostEdited = new MutableLiveData<>();
    private MutableLiveData<Boolean> isPostDeleted = new MutableLiveData<>();

    public PostsViewModel() {
        postsRepository = new PostsRepository();
    }
}
