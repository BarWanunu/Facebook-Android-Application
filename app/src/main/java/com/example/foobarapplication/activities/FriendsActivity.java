package com.example.foobarapplication.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobarapplication.R;
import com.example.foobarapplication.adapters.FriendsAdapter;
import com.example.foobarapplication.entities.User;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FriendsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        // Get the list of friends passed from the previous activity
        List<User> friendsList = (List<User>) getIntent().getSerializableExtra("friendsList");

        // Checks if there aren't any friends in the user's friends list
        if (friendsList != null && friendsList.size() == 0) {
            List<User> friends = new ArrayList<>();
            friends.add(new User("No friends yet :("));
            friendsList = friends;
        }

        // Initialize RecyclerView and adapter
        recyclerView = findViewById(R.id.friendsRecyclerView);
        adapter = new FriendsAdapter(this, friendsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}


