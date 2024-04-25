package com.example.foobarapplication.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foobarapplication.R;
import com.example.foobarapplication.adapters.PostsListAdapter;
import com.example.foobarapplication.entities.User;
import com.example.foobarapplication.viewModels.PostsViewModel;
import com.example.foobarapplication.viewModels.UserViewModel;

public class Profile_Activity extends AppCompatActivity {
    RecyclerView lstPosts;
    private PostsListAdapter adapter;

    UserViewModel userViewModel;

    PostsViewModel postsViewModel;
    String userId;

    String profileImg;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intentUser = getIntent();
        userId = (String) intentUser.getSerializableExtra("userId");
        profileImg = (String) intentUser.getSerializableExtra("profileImg");

        // Update TextView to show userId
        TextView userIdTextView = findViewById(R.id.textView);
        userIdTextView.setText(userId);

        // Update ImageView to show profile image
        ImageView profileImageView = findViewById(R.id.imageView3);
        if (profileImg != null && !profileImg.isEmpty()) {
            Glide.with(this).load(profileImg).into(profileImageView);
        }

        lstPosts = findViewById(R.id.lstPosts);
        adapter = new PostsListAdapter(this);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
        //adapter.setOnItemClickListener(this);

        postsViewModel= new PostsViewModel(this);
        userViewModel = new UserViewModel(this);
        postsViewModel.deleteAll();
        postsViewModel.getPostsByUser(userId, this).observe(this, posts -> {
            adapter.setPosts(posts);
        });

    }
}
