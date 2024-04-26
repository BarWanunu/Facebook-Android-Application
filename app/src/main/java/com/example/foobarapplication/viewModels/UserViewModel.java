package com.example.foobarapplication.viewModels;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foobarapplication.Globals.GlobalToken;
import com.example.foobarapplication.entities.User;
import com.example.foobarapplication.repositories.UserRepository;

import java.util.List;

public class UserViewModel extends ViewModel {

    private UserRepository userRepository;
    private MutableLiveData<Boolean> isUserAdded = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUserChecked = new MutableLiveData<>();
    private MutableLiveData<String> AuthToken = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUserDeleted = new MutableLiveData<>();


    public UserViewModel(Context context) {
        userRepository = new UserRepository(context);
    }

    public LiveData<Boolean> getIsUserDeleted() {
        return isUserDeleted;
    }

    public LiveData<Boolean> getIsUserAdded() {
        return isUserAdded;
    }

    public LiveData<Boolean> getIsUserChecked() {
        return isUserChecked;
    }

    public LiveData<String> getToken() {
        return AuthToken;
    }

    public void setToken(String token) {
        AuthToken.postValue(token);
    }

    public void add(User user) {
        userRepository.add(user, isUserAdded);
    }

    public LiveData<Boolean> check(User user) {
        userRepository.check(user, isUserChecked, this);
        return isUserChecked;
    }

    public void  createToken(User user) {
        userRepository.createToken(user, this);
    }

    public void delete(User user) {
        userRepository.delete(user, isUserDeleted, GlobalToken.token);
    }

    public List<User> get() {
        return userRepository.getAll();
    }

    public void edit(User user, String oldUserName) {
        userRepository.edit(user, oldUserName, GlobalToken.token, this);
    }

    public void getUser(String username, MutableLiveData<User> user, LifecycleOwner owner) {
        userRepository.getUser(username, GlobalToken.token, user, owner);
    }

    public List<User> getAllFriends(User user){
        return userRepository.getAllFriends(user, GlobalToken.token);
    }
}
