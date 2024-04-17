package com.example.foobarapplication.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foobarapplication.Globals.GlobalPostList;
import com.example.foobarapplication.R;
import com.example.foobarapplication.adapters.PostsListAdapter;
import com.example.foobarapplication.entities.Post;
import com.example.foobarapplication.entities.User;
import com.example.foobarapplication.viewModels.PostsViewModel;
import com.example.foobarapplication.viewModels.UserViewModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Activity_Post extends AppCompatActivity implements PostsListAdapter.OnItemClickListener {
    boolean isDarkMode = false;
    public static List<Post> posts = GlobalPostList.postList;
    RecyclerView lstPosts;
    private PostsListAdapter adapter;
    int counterId;
    private static final int PICK_IMAGE_REQUEST = 1;
    Uri selectedImageUri;
    UserViewModel userViewModel;

    PostsViewModel postsViewModel;


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent intentUser = getIntent();
        User User = (com.example.foobarapplication.entities.User) intentUser.getSerializableExtra("user");
        String token = intentUser.getStringExtra("token");

        //userViewModel = new UserViewModel();
        //userViewModel.delete(User, token);
        /*
        userViewModel.getIsUserDeleted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                System.out.println("bye");
            }
        });

         */

        ImageButton menu = findViewById(R.id.menu);
        menu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, v);
            popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.action_dark_mode) {
                    // Toggle the mode
                    isDarkMode = !isDarkMode;

                    if (isDarkMode) {
                        // Set dark mode background, text color, etc.
                        getWindow().getDecorView().setBackgroundColor(Color.BLACK);

                    } else {
                        // Set light mode background, text color, etc.
                        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
                    }
                    adapter.setDarkMode(isDarkMode);

                    // Notify the adapter that the data has changed
                    adapter.notifyDataSetChanged();
                    return true;
                } else if (id == R.id.action_logout) {
                    // Start MainActivity - the login page
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
        lstPosts = findViewById(R.id.lstPosts);
        adapter = new PostsListAdapter(this, isDarkMode);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnItemClickListener(this);

        ReadingPosts.readingPosts(this, posts);
        adapter.setPosts(posts);
        ImageButton btnGallery = findViewById(R.id.btnGallery);
        btnGallery.setOnClickListener(v -> openGallery());

        Button btnAddPhoto = findViewById(R.id.btnAddPost);
        counterId = posts.toArray().length + 10;


        postsViewModel= new PostsViewModel();
//        postsViewModel.getAllFromDb(token);
        postsViewModel.get(token,this);

        postsViewModel.get(token,this).observe(this, posts -> {
            adapter.setPosts(posts);
        });
//
        postsViewModel.get(token,this);

        btnAddPhoto.setOnClickListener(v -> {
            EditText whatsOnYourMindEditText = findViewById(R.id.whats_on_your_mind);
            String enteredText = whatsOnYourMindEditText.getText().toString();

            if (!enteredText.isEmpty()) {
                Post newPost;
                String currentDate = new SimpleDateFormat("dd.MM.yy", Locale.getDefault()).format(new Date());
                if (selectedImageUri != null) {
                    String imageUriString = selectedImageUri.toString();

                    newPost = new Post(counterId++, User.getUsername(), enteredText, currentDate, 0, selectedImageUri, Uri.parse(User.getImagePath()));
                } else {
                    newPost = new Post(counterId++, User.getUsername(), enteredText, currentDate, 0, R.drawable.pingpong, Uri.parse(User.getImagePath()));
                }
                posts.add(newPost);
                adapter.setPosts(posts);

                // Show a toast message
                Toast.makeText(Activity_Post.this, "Post added successfully", Toast.LENGTH_SHORT).show();

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
    public void onLikeClick(Post post, TextView likesTextView) {
        // Get the current number of likes as a string
        String currentLikesString = likesTextView.getText().toString();

        // Extract the number of likes from the string
        int currentLikes = Integer.parseInt(currentLikesString.split(" ")[0]);

        // Check if the like button is already liked
        boolean isLiked = post.getIsLiked();

        // Update the number of likes based on the current state
        int newLikes;
        if (isLiked) {
            // If already liked, decrease the likes by 1
            newLikes = currentLikes - 1;
            post.unLike();
        } else {
            // If not liked, increase the likes by 1
            newLikes = currentLikes + 1;
            post.addLike();
        }

        // Update the TextView with the new number of likes
        likesTextView.setText(newLikes + " likes");
    }

    //comment button was pressed
    public void onCommentClick(int postId) {
        Intent intentUser = getIntent();
        User newUser = (User) intentUser.getSerializableExtra("user");
        Intent intent = new Intent(this, Comment_Activity.class);
        intent.putExtra("POST_ID", postId);
        intent.putExtra("userDetails", newUser);
        startActivity(intent);
    }

    //option button (delete or edit) was pressed
    public void onOptionClick(int postID) {
        ImageButton post_option = findViewById(R.id.post_options);

        // Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(this, post_option);

        // Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.menu_post_option, popup.getMenu());
        Post mypost = null;
        for (Post post : posts) {
            if (post.getId() == postID)
                mypost = post;

        }

        // Set the item click listener
        Post finalMypost = mypost;
        //Intent intentUser = getIntent();
        //String token = intentUser.getStringExtra("token");
        popup.setOnMenuItemClickListener(item -> {
            // Handle item clicks here
            int id = item.getItemId();
            if (id == R.id.action_post_delete) {
                //postsViewModel= new PostsViewModel();
                //postsViewModel.delete(finalMypost, token);
                posts.remove(finalMypost);
                adapter.setPosts(posts);
            } else if (id == R.id.action_post_edit) {
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

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);

                // Display the image in a dialog


                Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    // Helper method to encode Bitmap to base64
    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}



