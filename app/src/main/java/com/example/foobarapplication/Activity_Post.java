package com.example.foobarapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobarapplication.adapters.PostsListAdapter;
import com.example.foobarapplication.entities.Post;

import java.util.ArrayList;
import java.util.List;

public class Activity_Post extends AppCompatActivity implements PostsListAdapter.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        RecyclerView lstPosts = findViewById(R.id.lstPosts);
        final PostsListAdapter adapter = new PostsListAdapter(this);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnItemClickListener(this);

        List<Post> posts = new ArrayList<>();
        posts.add(new Post(1, "Itay", "Hello", R.drawable.pingpong));
        posts.add(new Post(2, "Itay2", "Hello2", R.drawable.pingpong));
        posts.add(new Post(3, "Itay3", "Hello3", R.drawable.pingpong));
        posts.add(new Post(4, "Itay4", "Hello4", R.drawable.pingpong));
        adapter.setPosts(posts);


    }

    @Override
    public void onShareClick() {
        ImageButton shareButton = findViewById(R.id.shareButton);

        // Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(this, shareButton);

        // Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.menu_share, popup.getMenu());
        //Log.i("Activity_post", "Information message");

        // Showing the popup menu
        popup.show();

    }
    public void onLikeClick() {
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
