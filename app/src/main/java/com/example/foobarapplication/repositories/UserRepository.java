package com.example.foobarapplication.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.foobarapplication.entities.User;
import com.example.foobarapplication.viewModels.UserViewModel;
import com.example.foobarapplication.webServices.UserAPI;

import java.util.LinkedList;
import java.util.List;

public class UserRepository {
    private UserAPI api;

    public UserRepository() {
        api = new UserAPI();
    }


    class UserListData extends MutableLiveData<List<User>> {

        public UserListData() {
            super();
            setValue(new LinkedList<>());
        }
    }

    public void add(final User user, final MutableLiveData<Boolean> isUserAdded) {
        api.add(user, isUserAdded);
    }

    public void check(final User user, final MutableLiveData<Boolean> isUserChecked, UserViewModel userViewModel) {
        api.check(user, isUserChecked, userViewModel);
    }

    public void delete(User user, MutableLiveData<Boolean> isUserDeleted) {
        api.delete(user, isUserDeleted);
    }
}
