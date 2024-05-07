package com.example.foobarapplication.repositories;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.foobarapplication.DB.LocalDB;
import com.example.foobarapplication.DB.dao.UserDao;
import com.example.foobarapplication.entities.ApprovalCallback;
import com.example.foobarapplication.entities.User;
import com.example.foobarapplication.viewModels.UserViewModel;
import com.example.foobarapplication.webServices.UserAPI;

import java.util.List;

public class UserRepository {
    private UserAPI api;
    private UserDao dao;

    public UserRepository(Context context) {
        api = new UserAPI();
        dao = LocalDB.getInstance(context).userDao();
    }

    // Retrieves all friends of a user from the server
    public List<User> getAllFriends(User user, String token) {
        return api.getAllFriends(user.getUserName(), token);
    }

    // Removing a friend from the user's friends list
    public void removeFriend(String userId, String friendId, String token, MutableLiveData<Boolean> success, ApprovalCallback callback) {
        api.removeFriend(userId, friendId, token, success, callback);
    }

    // Getting a list of all the user's friends requests
    public List<User> getAllFriendsRequest(String username, String token) {
        return api.getAllFriendsRequest(username, token);
    }

    // Adding a friend request for the specific user we want
    public void addFriendRequest(String userName, String token, ApprovalCallback callback) {
        api.addFriendRequest(userName, token, callback);
    }

    // Approving a friend request and making the two users friends
    public void approveFriendsRequest(String userId, String friendId, String token, ApprovalCallback callback) {
        api.approveFriendsRequest(userId, friendId, token, callback);
    }

    // Rejecting a friend request
    public void rejectFriendsRequest(String userId, String friendId, String token, ApprovalCallback callback) {
        api.rejectFriendsRequest(userId, friendId, token, callback);
    }

    // Adds a new user and observes the success LiveData for database insertion
    public void add(final User user, final MutableLiveData<Boolean> isUserAdded) {
        api.add(user, isUserAdded);
        dao.add(user);
    }

    // Checking if the user's fields exists in the database in order to let him inside the app
    public void check(final User user, final MutableLiveData<Boolean> isUserChecked, UserViewModel userViewModel) {
        api.check(user, isUserChecked, userViewModel);
    }

    // Deleting the user from the database
    public void delete(User user, MutableLiveData<Boolean> isUserDeleted, String token) {
        api.delete(user, isUserDeleted, token);
        dao.delete(user);
    }

    // Creating the token for the user
    public void createToken(User user, UserViewModel userViewModel) {
        api.createToken(user.getUserName(), userViewModel);

    }

    // Getting all users from the local database
    public List<User> getAll() {
        return dao.getAll();
    }

    // Editing the user's details
    public void edit(User user, String oldUserName, String token, UserViewModel model) {
        api.edit(user, token, model);
        dao.delete(oldUserName);
        dao.add(user);
    }

    // Getting the user from the server
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

    // Deletes all users from the local database
    public void deleteAll() {
        dao.deleteAll();
    }
}
