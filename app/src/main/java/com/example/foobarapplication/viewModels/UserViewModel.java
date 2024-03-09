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


    public UserViewModel() {
        userRepository = new UserRepository();
    }

    public LiveData<Boolean> getIsUserAdded() {
        return isUserAdded;
    }

    public LiveData<Boolean> getIsUserChecked() {
        return isUserChecked;
    }
    public void add(User user) {userRepository.add(user, isUserAdded);}

    public void check(User user) {userRepository.check(user, isUserChecked);}
}
