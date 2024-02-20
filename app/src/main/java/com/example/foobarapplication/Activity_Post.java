package com.example.foobarapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobarapplication.adapters.PostsListAdapter;
import com.example.foobarapplication.entities.Post;

import java.util.ArrayList;
import java.util.List;

public class Activity_Post extends AppCompatActivity implements PostsListAdapter.OnItemClickListener {
    boolean isDarkMode = false;
    List<Post> posts;
    @Override
    protected void onCreate(Bundle savedInstanceState){
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
                    } else {
                        // Set light mode background, text color, etc.
                        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
                        // Adjust other UI elements as needed
                    }
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
        final PostsListAdapter adapter = new PostsListAdapter(this);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnItemClickListener(this);


        posts = new ArrayList<>();
        posts.add(new Post(1, "Itay", "Hello", R.drawable.pingpong));
        posts.add(new Post(2, "Itay2", "Hello2", R.drawable.pingpong));
        posts.add(new Post(3, "Itay3", "Hello3", R.drawable.pingpong));
        posts.add(new Post(4, "Itay4", "Hello4", R.drawable.pingpong));
        adapter.setPosts(posts);



        ImageButton photo_button = findViewById(R.id.photo_button);
        photo_button.setOnClickListener(v -> {
            EditText whatsOnYourMindEditText = findViewById(R.id.whats_on_your_mind);
            String enteredText = whatsOnYourMindEditText.getText().toString();

            if (!enteredText.isEmpty()) {
                int id1 = posts.toArray().length;
                Post newPost = new Post(id1 + 1, User.getUsername(), enteredText, R.drawable.pingpong);
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

    @Override
    public void onShareClick(){
        ImageButton shareButton = findViewById(R.id.shareButton);

        // Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(this, shareButton);

        // Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.menu_share, popup.getMenu());
        //Log.i("Activity_post", "Information message");

        // Showing the popup menu
        popup.show();

    }
    public void onLikeClick(){
        // Find the TextView for likes
        TextView likesTextView = findViewById(R.id.likes);

        // Get the current number of likes as a string
        String currentLikesString = likesTextView.getText().toString();

        // Extract the number of likes from the string
        int currentLikes = Integer.parseInt(currentLikesString.split(" ")[0]);

        // Check if the like button is already liked
        boolean isLiked = currentLikesString.contains("1");

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

    //the user pressed the CommentButton
    public void onCommentClick(int postId) {
        Intent intent = new Intent(this, Comment_Activity.class);
        intent.putExtra("POST_ID", postId);
        startActivity(intent);
    }



}
