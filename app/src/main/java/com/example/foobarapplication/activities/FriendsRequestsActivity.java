package com.example.foobarapplication.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobarapplication.R;
import com.example.foobarapplication.adapters.FriendsRequestsAdapter;
import com.example.foobarapplication.entities.ApprovalCallback;
import com.example.foobarapplication.entities.Post;
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
            friends.add(new User("No friends requests"));
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
        // approving the friend request and presenting the response from the server
        userViewModel.approveFriendsRequest(getIntent().getStringExtra("username"), user.getUserName(), new ApprovalCallback() {
            @Override
            public void onResponse(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FriendsRequestsActivity.this, message, Toast.LENGTH_SHORT).show();
                        refreshFriendRequestsList(adapter);
                    }
                });
            }
        });


    }

    @SuppressLint("NotifyDataSetChanged") @Override
    // Rejecting the friend request
    public void onDeclineClick(User user, FriendsRequestsAdapter adapter) {
        // rejecting the friend request and presenting the response from the server
        userViewModel.rejectFriendsRequest(getIntent().getStringExtra("username"), user.getUserName(), new ApprovalCallback() {
            @Override
            public void onResponse(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FriendsRequestsActivity.this, message, Toast.LENGTH_SHORT).show();
                        refreshFriendRequestsList(adapter);
                    }
                });
            }
        });
    }

    // Refresh the friend requests list by fetching updated data from ViewModel
    private void refreshFriendRequestsList(FriendsRequestsAdapter adapter) {
        String userId = getIntent().getStringExtra("username");
        MutableLiveData<Boolean> success=new MutableLiveData<>();
        final List<User>[] updatedFriendRequests = new List[]{userViewModel.getAllFriendsRequest(userId, success)};

        success.observe(FriendsRequestsActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean ) {
                if (updatedFriendRequests[0] != null && updatedFriendRequests[0].size() == 0) {
                    List<User> friends = new ArrayList<>();
                    friends.add(new User("No friends requests"));
                    updatedFriendRequests[0] = friends;
                }
                // Update the adapter with the new data
                adapter.setFriendRequests(updatedFriendRequests[0]);
            }
        });


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

