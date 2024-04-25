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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        // Get the list of friends passed from the previous activity
        List<User> friendsList = (List<User>) getIntent().getSerializableExtra("friendsList");

        // Display the list of friends in a ListView or RecyclerView
        // For example:
        RecyclerView recyclerView = findViewById(R.id.friendsRecyclerView);
        if (friendsList.size() == 0) {
            List<User> friends = new ArrayList<>();
            friends.add(new User("No friends yet :("));
            friendsList = friends;
        }
        FriendsAdapter adapter = new FriendsAdapter(this, friendsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}

