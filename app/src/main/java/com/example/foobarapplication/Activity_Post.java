package com.example.foobarapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobarapplication.adapters.PostsListAdapter;
import com.example.foobarapplication.entities.Post;

import java.util.List;

public class Activity_Post extends AppCompatActivity implements PostsListAdapter.OnItemClickListener {
    boolean isDarkMode = false;
    public static List<Post> posts = GlobalPostList.postList ;
    RecyclerView lstPosts;
    private PostsListAdapter adapter;
    int counterId;



    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent intentUser = getIntent();
        UserCred User = (UserCred) intentUser.getSerializableExtra("user");

// Get the text from the EditText


        ImageButton menu = findViewById(R.id.menu);
        menu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, v);
            popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.action_dark_mode) {
                    // Toggle the mode
                    isDarkMode = !isDarkMode;

                    // Update UI based on the current mode
                    if (isDarkMode) {
                        // Set dark mode background, text color, etc.
                        getWindow().getDecorView().setBackgroundColor(Color.BLACK);

                        // Adjust other UI elements as needed
                        // Update the adapter with the new dark mode state

                    }
                    else {
                        // Set light mode background, text color, etc.
                        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
                        // Adjust other UI elements as needed
                    }
                    adapter.setDarkMode(isDarkMode);

                    // Notify the adapter that the data has changed
                    adapter.notifyDataSetChanged();
                    return true;
                } else if (id == R.id.action_logout) {
                    // Implement logout functionality here
                    // Start MainActivity
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    // Close current activity if necessary
                    finish();
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
        RecyclerView lstPosts = findViewById(R.id.lstPosts);
         adapter = new PostsListAdapter(this, isDarkMode);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnItemClickListener(this);

        readingPosts.readingPosts(this, posts);
        adapter.setPosts(posts);

        Button btnAddPhoto = findViewById(R.id.btnAddPhoto);
        counterId= posts.toArray().length+10;
        btnAddPhoto.setOnClickListener(v -> {
            EditText whatsOnYourMindEditText = findViewById(R.id.whats_on_your_mind);
            String enteredText = whatsOnYourMindEditText.getText().toString();

            if (!enteredText.isEmpty()) {
                Post newPost = new Post(counterId++, User.getUsername(), enteredText,"22.02.24",0, R.drawable.pingpong, R.drawable.pingpong);
                posts.add(newPost);
                adapter.setPosts(posts);

                // Show a toast message
                Toast.makeText(this, "Post added successfully", Toast.LENGTH_SHORT).show();

                // Clear the EditText
                whatsOnYourMindEditText.setText("");
            } else {
                // Show a toast message indicating that the text is empty
                Toast.makeText(this, "Please enter text before adding a post", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //share button was pressed
    @Override
    public void onShareClick() {
        ImageButton shareButton = findViewById(R.id.shareButton);

        PopupMenu popup = new PopupMenu(this, shareButton);

        popup.getMenuInflater().inflate(R.menu.menu_share, popup.getMenu());

        popup.show();

    }

    //like button was pressed
    public void onLikeClick(int postId) {
        // Find the TextView for likes
        TextView likesTextView = findViewById(R.id.likes);

        // Get the current number of likes as a string
        String currentLikesString = likesTextView.getText().toString();

        // Extract the number of likes from the string
        int currentLikes = Integer.parseInt(currentLikesString.split(" ")[0]);

        // Check if the like button is already liked
        boolean isLiked = currentLikes == (posts.get(postId).getLikes() + 1);

        // Update the number of likes based on the current state
        int newLikes;
        if (isLiked) {
            // If already liked, decrease the likes by 1
            newLikes = currentLikes - 1;
        } else {
            // If not liked, increase the likes by 1
            newLikes = currentLikes + 1;
        }

        // Update the TextView with the new number of likes
        likesTextView.setText(newLikes + " likes");

    }

    //comment button was pressed
    public void onCommentClick(int postId) {
        Intent intentUser = getIntent();
        UserCred newUser = (UserCred) intentUser.getSerializableExtra("user");
        Intent intent = new Intent(this, Comment_Activity.class);
        intent.putExtra("POST_ID", postId);
        intent.putExtra("userDetails", newUser );
        startActivity(intent);
    }

    //option button (delete or edit) was pressed
    public void onOptionClick(int postID){
        ImageButton post_option = findViewById(R.id.post_options);

        // Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(this, post_option);

        // Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.menu_post_option, popup.getMenu());
        Post mypost=null;
        for(Post post:posts) {
            if (post.getId() == postID)
                mypost = post;

        }

        // Set the item click listener
        Post finalMypost = mypost;
        popup.setOnMenuItemClickListener(item -> {
            // Handle item clicks here
            int id = item.getItemId();
            if (id == R.id.action_post_delete) {
                posts.remove(finalMypost);
                adapter.setPosts(posts);
            }
            else if(id==R.id.action_post_edit){
                assert finalMypost != null;
                showEditDialog(finalMypost);
            }


            return false;
        });

        // Showing the popup menu
        popup.show();
    }
    private void showEditDialog(Post post) {
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
            // Update the adapter
            adapter.setPosts(posts);
            dialog.dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

}

