package com.example.foobarapplication.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.foobarapplication.entities.User;
import com.example.foobarapplication.repositories.UserRepository;

public class UserViewModel extends ViewModel {

    private UserRepository userRepository;
    private MutableLiveData<Boolean> isUserAdded = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUserChecked = new MutableLiveData<>();
    private MutableLiveData<String> AuthToken = new MutableLiveData<>();

    private MutableLiveData<Boolean> isUserDeleted = new MutableLiveData<>();


    public UserViewModel() {
        userRepository = new UserRepository();
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

    public void delete(User user, String token) {
        userRepository.delete(user, isUserDeleted, token);
    }
}
