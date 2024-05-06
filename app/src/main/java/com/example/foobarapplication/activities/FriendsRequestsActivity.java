package com.example.foobarapplication.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobarapplication.R;
import com.example.foobarapplication.adapters.FriendsRequestsAdapter;
import com.example.foobarapplication.entities.User;
import com.example.foobarapplication.viewModels.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class FriendsRequestsActivity extends AppCompatActivity implements FriendsRequestsAdapter.OnRequestActionClickListener {

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_requests);

        userViewModel = new UserViewModel(this);

        // Get the list of friends requests passed from the previous activity
        List<User> friendRequests = (List<User>) getIntent().getSerializableExtra("friendsRequest");

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.friendsRequestRecyclerView);

        // Checks if there aren't any friends requests
        if (friendRequests != null && friendRequests.size() == 0) {
            List<User> friends = new ArrayList<>();
            friends.add(new User("You don't have any friends requests"));
            friendRequests = friends;
        }

        // Initialize adapter
        FriendsRequestsAdapter adapter = new FriendsRequestsAdapter(this, this);
        adapter.setFriendRequests(friendRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    // Approving the friend request
    public void onApproveClick(User user, FriendsRequestsAdapter adapter) {
        userViewModel.approveFriendsRequest(getIntent().getStringExtra("username"), user.getUserName());
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    // Rejecting the friend request
    public void onDeclineClick(User user, FriendsRequestsAdapter adapter) {
        userViewModel.rejectFriendsRequest(getIntent().getStringExtra("username"), user.getUserName());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        User user = (User) getIntent().getSerializableExtra("user");
        Intent intent = new Intent(this, Activity_Post.class);
        intent.putExtra("user", user);
        finish();
        startActivity(intent);
    }
}

