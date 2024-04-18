package com.example.foobarapplication.entities;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.foobarapplication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Entity
public class Post  {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private ArrayList<Object> comments;
    private String profile;
    private String text;
    private String sdate;
    private Date date;

//    private Date dDate;
    private int likes;
    private int picture=-1;
    private int profilePicture=-1;

    private Uri uProfilePicture;

    private Uri uPic;

    private String img;
    private String profileImg;
    private boolean isLiked = false;
    private Bitmap imgBitmap; // Bitmap for the post image
    private Bitmap profileImgBitmap;

    public Post() {
    }

    // Constructor with essential fields
    public Post(int id, String author, String content, String date, int likes) {
        this.id = id;
        this.profile = author;
        this.text = content;
        this.sdate = date;
        this.likes = likes;
        this.picture = -1;
        this.profilePicture = -1;
        this.comments = new ArrayList<>();
    }

    // Constructor with additional bitmap images
    public Post(int id, String author, String content, String date, int likes, Bitmap imgBitmap, Bitmap profileImgBitmap) {
        this(id, author, content, date, likes);
        this.imgBitmap = imgBitmap;
        this.profileImgBitmap = profileImgBitmap;
    }

    // Constructor with profile image bitmap
    public Post(int id, String author, String content, String date, int likes, Bitmap profileImgBitmap) {
        this(id, author, content, date, likes);
        this.profileImgBitmap = profileImgBitmap;
    }

    // Constructor with image URLs
    public Post(int id, String author, String content, String date, int likes, String sPic, String sProfilePicture) {
        this(id, author, content, date, likes);
        this.img = sPic;
        this.profileImg = sProfilePicture;
    }
    // Constructor with all fields except image and profile image
    public Post(int id, String author, String content, String date, int likes, int sPic, int sProfilePicture) {
        this(id, author, content, date, likes);
        this.picture = sPic;
        this.profilePicture = sProfilePicture;
    }


    // Constructor with profile image URL
    public Post(int id, String author, String content, String date, int likes, String sProfilePicture) {
        this(id, author, content, date, likes);
        this.profileImg = sProfilePicture;
    }

    // Constructor with image URIs
    public Post(int id, String author, String content, String date, int likes, Uri uPic, Uri uProfilePicture) {
        this(id, author, content, date, likes);
        this.uPic = uPic;
        this.uProfilePicture = uProfilePicture;
    }

    // Constructor with profile image URI
    public Post(int id, String author, String content, String date, int likes, int picture, Uri uProfilePicture) {
        this(id, author, content, date, likes);
        this.picture = picture;
        this.uProfilePicture = uProfilePicture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return profile;
    }

    public void setAuthor(String author) {
        this.profile = author;
    }

    public String getContent() {
        return text;
    }

    public void setContent(String content) {
        this.text = content;
    }

    public int getLikes() {
        return likes;
    }

    public void incrementLikes() {
        likes++;
    }
    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void addLike() {
        this.likes += 1;
    }

    public void unLike() {
        this.likes -= 1;
    }

    public int getPic() {
        return picture;
    }
    public int getProfilePicture() {
        return profilePicture;
    }

    public String getDate() {
        if(sdate==null){
            sdate= new SimpleDateFormat("dd.MM.yy", Locale.getDefault()).format(new Date());
        }
        return sdate;
    }

    public void setPic(int pic) {
        this.picture = pic;
    }
    public Uri getuPic(){
        return uPic;
    }
    public Uri getuProfilePicture(){
        return uProfilePicture;
    }
    public String getsPic(){
        return img;
    }
    public String getsProfilePicture(){
        return profileImg;
    }

    public boolean getIsLiked() {
        return isLiked;
    }
    public void setLiked(boolean liked) {
        this.isLiked = liked;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.post_layout);
//        ImageButton likeBtn = findViewById(R.id.likeButton);
//        likeBtn.setOnClickListener(v -> {
//            if (isLiked == false) {
//                isLiked = true;
//                addLike();
//            } else {
//                isLiked = false;
//                unLike();
//            }
//        });
//    }

    public ArrayList<Object> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Object> comments) {
        this.comments = comments;
    }

    public void setDate(String date) {
        this.sdate = date;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public void setProfilePicture(int profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setuProfilePicture(Uri uProfilePicture) {
        this.uProfilePicture = uProfilePicture;
    }

    public void setuPic(Uri uPic) {
        this.uPic = uPic;
    }

    public boolean isLiked() {
        return isLiked;
    }
    public static List<Post> fromServerModel(List<Post2> serverPosts) {
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        List<Post> posts = new ArrayList<>();
        for (Post2 serverPost : serverPosts) {
            Post post = new Post();
            post.setId(serverPost.getId());
            post.setAuthor(serverPost.getProfile());
            post.setContent(serverPost.getText());
            outputDateFormat.format(serverPost.getDate());
            post.setLikes(serverPost.getLikes());
            // Set other fields accordingly based on the server model
            posts.add(post);
        }
        return posts;
    }
}

