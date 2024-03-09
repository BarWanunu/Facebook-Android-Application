package com.example.foobarapplication.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.foobarapplication.entities.User;
import com.example.foobarapplication.repositories.UserRepository;

import java.util.List;

public class UserViewModel extends ViewModel {

    private UserRepository userRepository;
    private LiveData<List<User>> users;

    public UserViewModel() {
        userRepository = new UserRepository();
        users = userRepository.getAll();
    }

    public LiveData<List<User>> get() {return  users;}

    public void add(User user) {userRepository.add(user);}

    public void delete(User user) {userRepository.delete(user);}
    public void reload(User user) {userRepository.reload(user);}
}
