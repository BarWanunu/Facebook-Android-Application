package com.example.foobarapplication.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foobarapplication.DB.UserDB;
import com.example.foobarapplication.DB.dao.UsersDao;
import com.example.foobarapplication.entities.User;
import com.example.foobarapplication.webServices.UsersAPI;

import java.util.LinkedList;
import java.util.List;

public class UserRepository {
    private UsersDao dao;
    private UserListData userListData;
    private UsersAPI api;

    public UserRepository() {
        UserDB db = UserDB.getInstance();
        dao = db.usersDao();
        userListData = new UserListData();
        api = new UsersAPI(userListData, dao);
    }

    class UserListData extends MutableLiveData<List<User>> {

        public UserListData() {
            super();
            setValue(new LinkedList<>());
        }
    }
    public LiveData<List<User>> getAll() {
        return userListData;
    }

    public void add(final User user) {
        api.add(user);
    }

    public void delete(final User user) {
        api.delete(user);
    }
    public void reload(final User user) {
        api.get();
    }
}
