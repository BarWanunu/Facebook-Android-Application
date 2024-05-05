package com.example.foobarapplication.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.foobarapplication.DB.LocalDB;
import com.example.foobarapplication.DB.dao.PostsDao;
import com.example.foobarapplication.R;
import com.example.foobarapplication.adapters.FriendsAdapter;
import com.example.foobarapplication.adapters.FriendsRequestsAdapter;
import com.example.foobarapplication.adapters.PostsListAdapter;
import com.example.foobarapplication.entities.Post;
import com.example.foobarapplication.entities.User;
import com.example.foobarapplication.viewModels.PostsViewModel;
import com.example.foobarapplication.viewModels.UserViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Activity_Post extends AppCompatActivity implements PostsListAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    boolean isDarkMode = false;
    private RecyclerView lstPosts;
    private PostsListAdapter adapter;

    private FriendsAdapter friendsAdapter;
    private FriendsRequestsAdapter friendsRequestsAdapter;
    private UserViewModel userViewModel;
    private PostsViewModel postsViewModel;
    private User userIntent;
    private List<User> friendsList = new LinkedList<>();
    private List<User> friendsRequest = new LinkedList<>();

    private static final int GALLERY_REQ_CODE = 1000;
    private static final int CAMERA_REQ_CODE = 1001;

    private String selectedImageBase64;


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent intentUser = getIntent();
        userIntent = (com.example.foobarapplication.entities.User) intentUser.getSerializableExtra("user");

        postsViewModel = new PostsViewModel(this);
        userViewModel = new UserViewModel(this);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        friendsList = userViewModel.getAllFriends(userIntent);
        friendsRequest = userViewModel.getAllFriendsRequest(userIntent.getUserName());
   

        ImageButton friends = findViewById(R.id.friends);
        friends.setOnClickListener(v -> {
            onFriendsClick(v);
        });

        ImageButton menu = findViewById(R.id.menu);
        menu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, v);
            popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
            List<Post> myPosts = new LinkedList<>();
            List<Post> posts = postsViewModel.get().getValue();
            for (Post post : posts) {
                if (post.getAuthor().equals(post.getAuthor())) {
                    myPosts.add(post);
                }
            }

            List<User> users = userViewModel.get();

            User finalMyuser = userIntent;
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
                } else if (id == R.id.action_user_delete) {
                    userViewModel.delete(finalMyuser);
                    users.remove(finalMyuser);
                    finish();
                } else if (id == R.id.action_user_edit_name) {
                    assert finalMyuser != null;
                    showEditUsernameDialog(finalMyuser, myPosts);
                } else if (id == R.id.action_user_edit_image) {
                    assert finalMyuser != null;
                    showEditUserImageDialog(finalMyuser, myPosts);
                } else if (id == R.id.profile) {
                    goToProfile(userIntent.getUserName(), userIntent.getPhoto());
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
        ImageButton btnGallery = findViewById(R.id.btnGallery);
        btnGallery.setOnClickListener(v -> openGallery());

        Button btnAddPost = findViewById(R.id.btnAddPost);

        postsViewModel.deleteAll();
        postsViewModel.getAllPosts(this).observe(this, posts -> {
            adapter.setPosts(posts);
        });
//

        btnAddPost.setOnClickListener(v -> {
            EditText whatsOnYourMindEditText = findViewById(R.id.whats_on_your_mind);
            String enteredText = whatsOnYourMindEditText.getText().toString();

            if (!enteredText.isEmpty()) {
                Post newPost;
                String currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date());
                if (selectedImageBase64 != null) {
                    newPost = new Post(userIntent.getUserName(), enteredText, currentDate, 0, selectedImageBase64, userIntent.getPhoto());
                } else {
                    newPost = new Post(userIntent.getUserName(), enteredText, currentDate, 0, userIntent.getPhoto());
                }

                postsViewModel.add(newPost, Activity_Post.this);

                postsViewModel.deleteAll();
                postsViewModel.getAllPosts(this).observe(this, posts -> {
                    adapter.setPosts(posts);
                });

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

    public void onFriendsClick(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        friendsList = userViewModel.getAllFriends(userIntent);
        friendsRequest = userViewModel.getAllFriendsRequest(userIntent.getUserName());
        popupMenu.getMenuInflater().inflate(R.menu.friends_icon_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.my_friends && friendsList != null) {
                Intent intent = new Intent(this, FriendsActivity.class);
                intent.putExtra("username", userIntent.getUserName());
                intent.putExtra("friendsList", new ArrayList<>(friendsList));
                startActivity(intent);
                return true;
            } else if (id == R.id.friends_request) {
                Intent intent = new Intent(this, FriendsRequestsActivity.class);
                intent.putExtra("username", userIntent.getUserName());
                intent.putExtra("friendsRequest", new ArrayList<>(friendsRequest));
                startActivity(intent);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }


    @Override
    public void onRefresh() {
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setRefreshing(false);
    }

    //share button was pressed
    @Override
    public void onShareClick(View v) {

        PopupMenu popup = new PopupMenu(this, v);

        popup.getMenuInflater().inflate(R.menu.menu_share, popup.getMenu());

        popup.show();

    }

    //like button was pressed
    public void onLikeClick(Post post, TextView likesTextView) {

        postsViewModel.likePost(post, Activity_Post.this);
        // Get the current number of likes as a string
        String currentLikesString = likesTextView.getText().toString();

        // Extract the number of likes from the string
        int currentLikes = Integer.parseInt(currentLikesString.split(" ")[0]);

        likesTextView.setText(currentLikes + " likes");
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
        if (!finalMypost.getAuthor().equals(userIntent.getUserName())) {
            new AlertDialog.Builder(this).setMessage("Can't edit/delete posts of other users").show();
            return;
        }
        popup.setOnMenuItemClickListener(item -> {
            // Handle item clicks here
            int id = item.getItemId();
            if (id == R.id.action_post_delete) {
                postsViewModel.delete(finalMypost);
                posts.remove(finalMypost);
                postsViewModel.deleteAll();
                postsViewModel.getAllPosts(this).observe(this, posts2 -> {
                    adapter.setPosts(posts2);
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
    public void onPictureClick(View v, String userId, String profileImg) {
        boolean isFriend = false;
        PopupMenu popup = new PopupMenu(this, v);

        // menu in case they are friends
        for (User user1 : friendsList) {
            if (user1.getUserName().equals(userId)) {
                isFriend = true;
                friendsMenu(popup, userId, profileImg);
                break;
            }
        }

        if (!isFriend) {
            // menu if you are looking at your own user
            if (userId.equals(userIntent.getUserName())) {
                userMenu(popup, userId, profileImg);
            }
            // menu in case they are not friends
            else {
                notFriendsMenu(popup, userId);
            }
        }

    }

    private void userMenu(PopupMenu popup, String userId, String profileImg) {
        popup.getMenuInflater().inflate(R.menu.menu_user_option, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_profile) {
                goToProfile(userId, profileImg);
                return true;
            }
            return false;
        });
        popup.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void friendsMenu(PopupMenu popup, String userId, String profileImg) {
        popup.getMenuInflater().inflate(R.menu.friends_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.RemoveFriend) {
                userViewModel.removeFriend(userIntent.getUserName(), userId);
                friendsList = userViewModel.getAllFriends(userIntent);
                return true;
            } else if (id == R.id.action_profile) {
                goToProfile(userId, profileImg);
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void notFriendsMenu(PopupMenu popup, String userId) {
        popup.getMenuInflater().inflate(R.menu.not_friends_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.AddFriend) {
                userViewModel.addFriendRequest(userId);
                friendsRequest = userViewModel.getAllFriendsRequest(userId);
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void goToProfile(String userId, String profileImg) {
        User myuser = new User(userId);
        Intent profiltIntent = new Intent(Activity_Post.this, Profile_Activity.class);
        profiltIntent.putExtra("userId", userId);
        profiltIntent.putExtra("profileImg", profileImg);
        profiltIntent.putExtra("user", myuser);
        startActivity(profiltIntent);
        Toast.makeText(Activity_Post.this, "Welcome to " + userId + "'s profile, the posts here are only for display", Toast.LENGTH_SHORT).show();
        userViewModel.createToken(userIntent);
    }

    private void showEditUsernameDialog(User user, List<Post> myposts) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit User");

        // Set up the input
        final EditText input = new EditText(this);
        input.setText(user.getUserName());
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Submit", (dialog, which) -> {
            String newUserName = input.getText().toString();
            String oldUserName = user.getUserName();

            // Update user's name in the local user object and ViewModel
            user.setUserName(newUserName);
            userViewModel.edit(user, oldUserName);

            // Prepare to update posts
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                PostsDao dao = LocalDB.getInstance(this).postDao();
                List<Post> updatedPosts = new ArrayList<>();
                for (Post post : myposts) {
                    if (post.getAuthor().equals(oldUserName)) {
                        Post updatedPost = post; // Create a new object to ensure immutability
                        updatedPost.setAuthor(newUserName);
                        dao.update(updatedPost); // Update the post in the database
                        updatedPosts.add(updatedPost);
                    } else {
                        updatedPosts.add(post);
                    }
                }

                // Run on the UI thread after updates
                runOnUiThread(() -> {
                    postsViewModel.get().setValue(updatedPosts); // This should trigger UI update correctly
                    dialog.dismiss();
                });
            });
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showEditUserImageDialog(User user, List<Post> myposts) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit User");

        // Set up the input
        final LinearLayout layout = new LinearLayout(this);
        final Button input = new Button(this);
        input.setText("choose image from gallery");
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        final Button input2 = new Button(this);
        input2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
        input2.setText("choose image from camera");
        layout.addView(input);
        layout.addView(input2);
        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("Submit", (dialog, which) -> {
            // Update the post content
            user.setPhoto(selectedImageBase64);
            // Update post on server
            userViewModel.edit(user, user.getUserName());
            PostsDao dao = LocalDB.getInstance(this).postDao();
            List<Post> posts = postsViewModel.get().getValue();
            for (Post post : myposts) {
                posts.remove(post);
                post.setProfileImg(selectedImageBase64);
                posts.add(post);
                dao.update(post);
            }
            //Collections.sort(posts);
            postsViewModel.get().setValue(posts);
            dialog.dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

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
            postsViewModel.edit(post);
            dialog.dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQ_CODE);
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQ_CODE && data != null) {
                handleGalleryResult(data.getData());
            } else if (requestCode == CAMERA_REQ_CODE && data != null) {
                try {
                    handleCameraResult(data);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void handleGalleryResult(Uri selectedImageUri) {
        selectedImageBase64 = ImageUtils.base64FromUri(selectedImageUri, this);
    }

    private void handleCameraResult(Intent data) throws IOException {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            selectedImageBase64 = ImageUtils.base64FromUri(saveCameraImage(imageBitmap), this);// You may want to save this image to a file.
        }
    }

    private Uri saveCameraImage(Bitmap imageBitmap) throws IOException {
        File imageFile = createImageFile();
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Uri.fromFile(imageFile);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        return imageFile;
    }


    // Helper method to encode Bitmap to base64
    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}




