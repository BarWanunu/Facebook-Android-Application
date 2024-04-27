package com.example.foobarapplication.activities;

import android.os.Bundle;
import android.widget.Toast;

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

        List<User> friendRequests = (List<User>) getIntent().getSerializableExtra("friendsRequest");

        RecyclerView recyclerView = findViewById(R.id.friendsRequestRecyclerView);

        if (friendRequests != null && friendRequests.size() == 0) {
            List<User> friends = new ArrayList<>();
            friends.add(new User("You don't have any friends requests"));
            friendRequests = friends;
        }

        FriendsRequestsAdapter adapter = new FriendsRequestsAdapter(this, this);
        adapter.setFriendRequests(friendRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onApproveClick(User user) {
//        userViewModel.approveFriendsRequest(getIntent().getStringExtra("username") ,user.getUserName());
    }

    @Override
    public void onDeclineClick(User user) {
        // Handle decline action
        Toast.makeText(this, "Declined: " + user.getUserName(), Toast.LENGTH_SHORT).show();
    }
}

