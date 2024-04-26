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

import java.util.List;

public class FriendsRequestsActivity extends AppCompatActivity implements FriendsRequestsAdapter.OnRequestActionClickListener {

    private RecyclerView recyclerView;
    private FriendsRequestsAdapter adapter;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_requests);

        userViewModel = new UserViewModel(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FriendsRequestsAdapter(this, this);
        recyclerView.setAdapter(adapter);

        // Get data from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String username = extras.getString("username");
            List<User> friendRequests = (List<User>) getIntent().getSerializableExtra("friendRequests");
            if (friendRequests != null) {
                adapter.setFriendRequests(friendRequests);
            }
        }
    }

    @Override
    public void onApproveClick(User user) {
        userViewModel.approveFriendsRequest(getIntent().getStringExtra("username") ,user.getUserName());
    }

    @Override
    public void onDeclineClick(User user) {
        // Handle decline action
        Toast.makeText(this, "Declined: " + user.getUserName(), Toast.LENGTH_SHORT).show();
    }
}

