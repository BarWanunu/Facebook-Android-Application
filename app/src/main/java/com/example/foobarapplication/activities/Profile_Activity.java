package com.example.foobarapplication.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foobarapplication.R;
import com.example.foobarapplication.adapters.PostsListAdapter;
import com.example.foobarapplication.entities.User;
import com.example.foobarapplication.viewModels.PostsViewModel;
import com.example.foobarapplication.viewModels.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class Profile_Activity extends AppCompatActivity {
    RecyclerView lstPosts;
    private PostsListAdapter adapter;

    UserViewModel userViewModel;

    PostsViewModel postsViewModel;
    String userId;

    User user;

    String profileImg;

    private LiveData<List<User>> friendsList = new MutableLiveData<>();

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intentUser = getIntent();
        userId = (String) intentUser.getSerializableExtra("userId");
        profileImg = (String) intentUser.getSerializableExtra("profileImg");
        user = (com.example.foobarapplication.entities.User) intentUser.getSerializableExtra("user");
        userViewModel = new UserViewModel(this);
        postsViewModel= new PostsViewModel(this);

        userViewModel.createToken(user);
        userViewModel.getToken().observe(Profile_Activity.this, new Observer<String>() {
            @Override
            public void onChanged(String token) {
                friendsList =userViewModel.getAllFriendsLiveData(user);
                lstPosts = findViewById(R.id.lstPosts);
                adapter = new PostsListAdapter(Profile_Activity.this);
                lstPosts.setAdapter(adapter);
                lstPosts.setLayoutManager(new LinearLayoutManager(Profile_Activity.this));
                postsViewModel.deleteAll();
                postsViewModel.getPostsByUser(userId, Profile_Activity.this).observe(Profile_Activity.this, posts -> {
                    adapter.setPosts(posts);
                });
            }
        });

        ImageButton friends = findViewById(R.id.friends);
        friends.setOnClickListener(v -> {
            onFriendsClick(v, friendsList);
        });

        // Update TextView to show userId
        TextView userIdTextView = findViewById(R.id.textView);
        userIdTextView.setText(userId);

        // Update ImageView to show profile image
        ImageView profileImageView = findViewById(R.id.imageView3);
        if (profileImg != null && !profileImg.isEmpty()) {
            Glide.with(this).load(profileImg).into(profileImageView);
        }
    }

    public void onFriendsClick(View v, LiveData<List<User>> friendsList) {
        List<User> friends = friendsList.getValue();
        PopupMenu popupMenu = new PopupMenu(this, v);

        popupMenu.getMenuInflater().inflate(R.menu.friends_profile_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.my_friends) {
                Intent intent = new Intent(this, FriendsActivity.class);
                intent.putExtra("friendsList", new ArrayList<>(friends));
                startActivity(intent);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }
}
