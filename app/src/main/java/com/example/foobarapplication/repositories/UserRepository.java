package com.example.foobarapplication.repositories;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.foobarapplication.DB.LocalDB;
import com.example.foobarapplication.DB.dao.UserDao;
import com.example.foobarapplication.entities.User;
import com.example.foobarapplication.viewModels.UserViewModel;
import com.example.foobarapplication.webServices.UserAPI;

import java.util.LinkedList;
import java.util.List;

public class UserRepository {
    private UserAPI api;
    private UserDao dao;

    public UserRepository(Context context) {
        api = new UserAPI();
        dao = LocalDB.getInstance(context).userDao();
    }

    public List<User> getAllFriends(User user, String token) {
        return api.getAllFriends(user.getUserName(), token);
    }


    class UserListData extends MutableLiveData<List<User>> {

        public UserListData() {
            super();
            setValue(new LinkedList<>());
        }
    }

    public void add(final User user, final MutableLiveData<Boolean> isUserAdded) {
        api.add(user, isUserAdded);
        dao.add(user);
    }

    public void check(final User user, final MutableLiveData<Boolean> isUserChecked, UserViewModel userViewModel) {
        api.check(user, isUserChecked, userViewModel);
    }

    public void delete(User user, MutableLiveData<Boolean> isUserDeleted, String token) {
        api.delete(user, isUserDeleted, token);
        dao.delete(user);
    }
    public void createToken(User user, UserViewModel userViewModel) {
        api.createToken(user.getUserName(), userViewModel);

    }

    public List<User> getAll() {
        return dao.getAll();
    }

    public void edit(User user, String oldUserName, String token) {
        api.edit(user, token);
        dao.delete(oldUserName);
        dao.add(user);
    }

    public void getUser(String username, String token, MutableLiveData<User> user, LifecycleOwner owner) {
        api.getUser(username, token, user);
        user.observe(owner, new Observer<User>() {
            @Override
            public void onChanged(User user2) {
                dao.delete(user2);
                dao.add(user2);
                user.removeObserver(this);
            }
        });
    }
}
