package com.example.foobarapplication.entities;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.foobarapplication.R;

import java.util.ArrayList;
import java.util.Date;

@Entity
public class Post extends AppCompatActivity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private ArrayList<Object> comments;
    private String author;
    private String content;
    private String date;

//    private Date dDate;
    private int likes;
    private int picture;
    private int profilePicture;

    private Uri uProfilePicture;

    private Uri uPic;

    private String sProfilePicture;
    private String sPic;
    private boolean isLiked = false;
    private Bitmap imgBitmap; // Bitmap for the post image
    private Bitmap profileImgBitmap;

    public Post(int id, String author, String content, String date, int likes, Bitmap imgBitmap, Bitmap profileImgBitmap) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.date = date;
        this.likes = likes;
        this.picture = -1;
        this.profilePicture = -1;
        this.imgBitmap = imgBitmap;
        this.profileImgBitmap = profileImgBitmap;
        this.comments = new ArrayList<>();
    }

    public Post(int id, String author, String content, String date, int likes, Bitmap profileImgBitmap) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.date = date;
        this.likes = likes;
//        this.picture = picture;
        this.profilePicture = -1;
        this.imgBitmap = null;
        this.profileImgBitmap = profileImgBitmap;
        this.comments = new ArrayList<>();
    }
    public Post(int id, String author, String content,String date, int likes, String sPic, String sProfilePicture) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.date = date;
        this.likes = likes;
        this.picture = -1;
        this.profilePicture = -1;
        this.sPic = sPic;
        this.sProfilePicture = sProfilePicture;
        this.comments = new ArrayList<>();
    }
    public Post(int id, String author, String content,String date, int likes, String sProfilePicture) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.date = date;
        this.likes = likes;
        this.picture = -1;
        this.profilePicture = -1;
        this.sPic = null;
        this.sProfilePicture = sProfilePicture;
        this.comments = new ArrayList<>();
    }
    public Post(int id, String author, String content,String date, int likes) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.date = date;
        this.likes = likes;
        this.picture = -1;
        this.profilePicture = -1;
        this.sPic = null;
        this.sProfilePicture = null;
        this.uPic = null;
        this.uProfilePicture = null;
        this.comments = new ArrayList<>();
    }


    public Post(int id, String author, String content,String date, int likes, Uri uPic, Uri uProfilePicture) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.date = date;
        this.likes = likes;
        this.picture = -1;
        this.profilePicture = -1;
        this.uPic = uPic;
        this.uProfilePicture = uProfilePicture;
        this.comments = new ArrayList<>();
    }

    public Post(int id, String author, String content, String date, int likes, int picture, int profilePicture){
        this.id = id;
        this.author = author;
        this.content = content;
        this.date = date;
        this.likes = likes;
        this.picture = picture;
        this.profilePicture = profilePicture;
        this.uPic = null;
        this.uProfilePicture = null;
        this.comments = new ArrayList<>();
    }

    public Post(int id, String author, String content, String date, int likes, int picture, Uri uProfilePicture){
        this.id = id;
        this.author = author;
        this.content = content;
        this.date = date;
        this.likes = likes;
        this.picture = picture;
        this.profilePicture = -1;
        this.uPic = null;
        this.uProfilePicture = uProfilePicture;
        this.comments = new ArrayList<>();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        return date;
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

    public boolean getIsLiked() {
        return isLiked;
    }
    public void setLiked(boolean liked) {
        this.isLiked = liked;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_layout);
        ImageButton likeBtn = findViewById(R.id.likeButton);
        likeBtn.setOnClickListener(v -> {
            if (isLiked == false) {
                isLiked = true;
                addLike();
            } else {
                isLiked = false;
                unLike();
            }
        });
    }

    public ArrayList<Object> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Object> comments) {
        this.comments = comments;
    }

    public void setDate(String date) {
        this.date = date;
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
}

