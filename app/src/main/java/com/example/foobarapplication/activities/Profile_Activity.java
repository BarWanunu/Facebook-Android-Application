package com.example.foobarapplication.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.foobarapplication.R;
import com.example.foobarapplication.adapters.PostsListAdapter;
import com.example.foobarapplication.entities.Post;
import com.example.foobarapplication.entities.User;
import com.example.foobarapplication.viewModels.PostsViewModel;
import com.example.foobarapplication.viewModels.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class Profile_Activity extends AppCompatActivity implements PostsListAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener{
    RecyclerView lstPosts;
    private PostsListAdapter adapter;

    UserViewModel userViewModel;

    PostsViewModel postsViewModel;
    String userId;

    User userIntent;

    String profileImg;

    private List<User> friendsList;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intentUser = getIntent();
        userId = (String) intentUser.getSerializableExtra("userId");
        profileImg = (String) intentUser.getSerializableExtra("profileImg");
        userIntent = (com.example.foobarapplication.entities.User) intentUser.getSerializableExtra("user");

        userViewModel = new UserViewModel(this);
        postsViewModel= new PostsViewModel(this);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        userViewModel.createToken(userIntent);
        LiveData<String> tokenList = userViewModel.getToken();
        tokenList.observe(Profile_Activity.this, new Observer<String>() {
            @Override
            public void onChanged(String token) {
                friendsList = userViewModel.getAllFriends(userIntent);
                tokenList.removeObserver(this);
                userViewModel.createToken(userViewModel.get().get(0));
                tokenList.observe(Profile_Activity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String string) {
                        lstPosts = findViewById(R.id.lstPosts2);
                        adapter = new PostsListAdapter(Profile_Activity.this);
                        adapter.setOnItemClickListener(Profile_Activity.this);
                        lstPosts.setAdapter(adapter);
                        lstPosts.setLayoutManager(new LinearLayoutManager(Profile_Activity.this));
                        postsViewModel.deleteAll();
                        postsViewModel.getPostsByUser(userId, Profile_Activity.this).observe(Profile_Activity.this, posts -> {
                            adapter.setPosts(posts);
                        });
                        tokenList.removeObserver(this);
                    }
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

    public void onFriendsClick(View v, List<User> friendsList) {
        PopupMenu popupMenu = new PopupMenu(this, v);

        popupMenu.getMenuInflater().inflate(R.menu.friends_profile_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.my_friends) {
                Intent intent = new Intent(this, FriendsActivity.class);
                intent.putExtra("friendsList", new ArrayList<>(friendsList));
                startActivity(intent);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    @Override
    public void onShareClick(View v) {

        PopupMenu popup = new PopupMenu(this, v);

        popup.getMenuInflater().inflate(R.menu.menu_share, popup.getMenu());

        popup.show();

    }

    @Override
    public void onLikeClick(Post post, TextView likesTextView) {

        MutableLiveData<Post> success = new MutableLiveData<>();
        postsViewModel.likePost(post, success);
        success.observe(Profile_Activity.this, new Observer<Post>() {
            @Override
            public void onChanged(Post post) {
                postsViewModel.deleteAll();
                postsViewModel.getPostsByUser(userId, Profile_Activity.this);
            }
        });
        // Get the current number of likes as a string
        String currentLikesString = likesTextView.getText().toString();

        // Extract the number of likes from the string
        int currentLikes = Integer.parseInt(currentLikesString.split(" ")[0]);

        likesTextView.setText(currentLikes + " likes");
    }

    @Override
    public void onCommentClick(int postId) {
        Intent intent = new Intent(this, Comment_Activity.class);
        intent.putExtra("POST_ID", postId);
        intent.putExtra("userDetails", userViewModel.get().get(0));
        startActivity(intent);
    }

    public void onOptionClick(View v, int postID) {
        //ImageButton post_option = findViewById(R.id.post_options);
        // Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(this, v);

        // Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.menu_post_option, popup.getMenu());
        Post mypost = null;
        List<Post> posts = postsViewModel.get().getValue();
        for (Post post : posts) {
            if (post.getId() == postID) {
                mypost = post;
                break;
            }
        }
        // Set the item click listener
        Post finalMypost = mypost;
        if (!finalMypost.getAuthor().equals(userViewModel.get().get(0).getUserName())) {
            new AlertDialog.Builder(this).setMessage("Can't edit/delete posts of other users").show();
            return;
        }
        popup.setOnMenuItemClickListener(item -> {
            // Handle item clicks here
            int id = item.getItemId();
            if (id == R.id.action_post_delete) {
                MutableLiveData<Boolean> success = new MutableLiveData<>();
                postsViewModel.delete(finalMypost, success);
                success.observe(Profile_Activity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        postsViewModel.deleteAll();
                        postsViewModel.getPostsByUser(userId, Profile_Activity.this);
                    }
                });
            } else if (id == R.id.action_post_edit) {
                assert finalMypost != null;
                showEditPostDialog(finalMypost);
            }
            return false;
        });

        // Showing the popup menu
        popup.show();
    }

    @Override
    public void onPictureClick(View v, String userId, String profileImg) {}


    private void showEditPostDialog(Post post) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Post");

        // Set up the input
        final EditText input = new EditText(this);
        input.setText(post.getContent());
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Submit", (dialog, which) -> {
            String newContent = input.getText().toString();
            // Update the post content
            post.setContent(newContent);
            // Update post on server
            MutableLiveData<Boolean> success = new MutableLiveData<>();
            postsViewModel.edit(post, success);
            success.observe(Profile_Activity.this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    postsViewModel.deleteAll();
                    postsViewModel.getPostsByUser(userId, Profile_Activity.this);
                }
            });
            dialog.dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    public void onRefresh() {
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, Activity_Post.class);
        intent.putExtra("user", userViewModel.get().get(0));
        finish();
        startActivity(intent);
    }
}
