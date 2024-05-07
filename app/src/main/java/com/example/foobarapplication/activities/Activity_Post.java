package com.example.foobarapplication.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.foobarapplication.DB.LocalDB;
import com.example.foobarapplication.DB.dao.PostsDao;
import com.example.foobarapplication.R;
import com.example.foobarapplication.adapters.PostsListAdapter;
import com.example.foobarapplication.entities.ApprovalCallback;
import com.example.foobarapplication.entities.Post;
import com.example.foobarapplication.entities.User;
import com.example.foobarapplication.viewModels.PostsViewModel;
import com.example.foobarapplication.viewModels.UserViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        userIntent = (User) intentUser.getSerializableExtra("user");

        postsViewModel = new PostsViewModel(this);
        userViewModel = new UserViewModel(this);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        // initializing friend request list and friends list from the server
        friendsList = userViewModel.getAllFriends(userIntent);
        friendsRequest = userViewModel.getAllFriendsRequest(userIntent.getUserName());

        // friends button listener
        ImageButton friends = findViewById(R.id.friends);
        friends.setOnClickListener(this::onFriendsClick);

        // menu button listener
        ImageButton menu = findViewById(R.id.menu);
        menu.setOnClickListener(this::onMenuClick);

        // button gallery listener
        ImageButton btnGallery = findViewById(R.id.btnGallery);
        btnGallery.setOnClickListener(v -> openGallery());

        lstPosts = findViewById(R.id.lstPosts);
        adapter = new PostsListAdapter(this, isDarkMode);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnItemClickListener(this);

        // getting all the posts from the server
        postsViewModel.deleteAll();
        postsViewModel.getAllPosts(Activity_Post.this).observe(Activity_Post.this, posts -> {
            adapter.setPosts(posts);
        });

        // adding post button
        Button btnAddPost = findViewById(R.id.btnAddPost);
        btnAddPost.setOnClickListener(v -> addPost());
    }

    private void addPost() {
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

            // adding a post
            MutableLiveData<Post> success = new MutableLiveData<>();
            postsViewModel.add(newPost, Activity_Post.this, success, new ApprovalCallback() {
                @Override
                public void onResponse(String message) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Activity_Post.this, message, Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            });

            // refreshing the posts list
            success.observe(Activity_Post.this, new Observer<Post>() {
                @Override
                public void onChanged(Post post) {
                    postsViewModel.deleteAll();
                    postsViewModel.getAllPosts(Activity_Post.this).observe(Activity_Post.this, posts -> {
                        adapter.setPosts(posts);
                    });
                }
            });

            // Clear the EditText
            whatsOnYourMindEditText.setText("");
        } else {
            // Show a toast message indicating that the text is empty
            Toast.makeText(this, "Please enter text before adding a post", Toast.LENGTH_SHORT).show();
        }
    }

    // Opening the menu options
    private void onMenuClick(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
        List<Post> myPosts = new LinkedList<>();
        List<Post> posts = postsViewModel.get().getValue();
        if (posts != null) {
            for (Post post : posts) {
                if (post.getAuthor().equals(post.getAuthor())) {
                    myPosts.add(post);
                }
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
                userViewModel.deleteAll();
                postsViewModel.deleteAll();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                // Close current activity if necessary
                finish();
                return true;
            } else if (id == R.id.action_user_delete) {
                MutableLiveData<Boolean> success = new MutableLiveData<>();
                userViewModel.delete(finalMyuser, success);
                success.observe(Activity_Post.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        users.remove(finalMyuser);
                    }
                });
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
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
    }

    // The menu opens up when we press the friends button
    private void onFriendsClick(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        friendsList = userViewModel.getAllFriends(userIntent);
        MutableLiveData<Boolean> success = new MutableLiveData<>();
        friendsRequest = userViewModel.getAllFriendsRequest(userIntent.getUserName());
        popupMenu.getMenuInflater().inflate(R.menu.friends_icon_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.my_friends) {
                Intent intent = new Intent(this, FriendsActivity.class);
                intent.putExtra("username", userIntent.getUserName());
                intent.putExtra("friendsList", new ArrayList<>(friendsList));
                startActivity(intent);
                return true;
            } else if (id == R.id.friends_request) {
                Intent intent = new Intent(this, FriendsRequestsActivity.class);
                intent.putExtra("username", userIntent.getUserName());
                intent.putExtra("friendsRequest", new ArrayList<>(friendsRequest));
                intent.putExtra("user", userIntent);
                startActivity(intent);
                List<User> friendsRequestNew = userViewModel.getAllFriendsRequest(userIntent.getUserName());
                // checking if the friend request changed if so update the posts list
                success.observe(Activity_Post.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (friendsRequestNew.size() != friendsRequest.size()) {
                            postsViewModel.deleteAll();
                            postsViewModel.getAllPosts(Activity_Post.this);
                        }
                    }
                });
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
        MutableLiveData<Post> success = new MutableLiveData<>();
        postsViewModel.likePost(post, success);
        success.observe(Activity_Post.this, new Observer<Post>() {
            @Override
            public void onChanged(Post post) {
                postsViewModel.deleteAll();
                postsViewModel.getAllPosts(Activity_Post.this);
            }
        });
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
                MutableLiveData<Boolean> success = new MutableLiveData<>();
                postsViewModel.delete(finalMypost, success, new ApprovalCallback() {
                    @Override
                    public void onResponse(String message) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Activity_Post.this, message, Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
                success.observe(Activity_Post.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        postsViewModel.deleteAll();
                        postsViewModel.getAllPosts(Activity_Post.this).observe(Activity_Post.this, posts2 -> {
                            adapter.setPosts(posts2);
                        });
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

    // The method checks if the picture that was clicked is the user, friend of user or not a friend
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

    // In case the picture was the user the menu will display only profile option
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

    // In case the picture was the user friend the menu will display only profile and remove friend options
    @SuppressLint("NotifyDataSetChanged")
    private void friendsMenu(PopupMenu popup, String userId, String profileImg) {
        popup.getMenuInflater().inflate(R.menu.friends_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.RemoveFriend) {
                MutableLiveData<Boolean> successFriend = new MutableLiveData<>();
                // removing the friend and updating the posts after the removal
                userViewModel.removeFriend(userIntent.getUserName(), userId, successFriend, new ApprovalCallback() {
                    @Override
                    public void onResponse(String message) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Activity_Post.this, message, Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
                successFriend.observe(Activity_Post.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        friendsList = userViewModel.getAllFriends(userIntent);
                        postsViewModel.deleteAll();
                        postsViewModel.getAllPosts(Activity_Post.this);
                    }
                });
                return true;
            } else if (id == R.id.action_profile) {
                goToProfile(userId, profileImg);
                return true;
            }
            return false;
        });
        popup.show();
    }

    //In case the picture wasn't a user friend the menu will display only add friend options
    private void notFriendsMenu(PopupMenu popup, String userId) {
        popup.getMenuInflater().inflate(R.menu.not_friends_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.AddFriend) {
                // adding the friend request and update the list
                userViewModel.addFriendRequest(userId, new ApprovalCallback() {
                    @Override
                    public void onResponse(String message) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Activity_Post.this, message, Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
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
        finish();
        Toast.makeText(Activity_Post.this, "Welcome to " + userId + "'s profile", Toast.LENGTH_SHORT).show();
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
        input2.setText("choose an image from the camera");
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
            if (posts != null) {
                for (Post post : myposts) {
                    posts.remove(post);
                    post.setProfileImg(selectedImageBase64);
                    posts.add(post);
                    dao.update(post);
                }
            }
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
            MutableLiveData<Boolean> success = new MutableLiveData<>();
            postsViewModel.edit(post, success, new ApprovalCallback() {
                @Override
                public void onResponse(String message) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Activity_Post.this, message, Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            });
            success.observe(Activity_Post.this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    postsViewModel.deleteAll();
                    postsViewModel.getAllPosts(Activity_Post.this);
                }
            });
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
}




