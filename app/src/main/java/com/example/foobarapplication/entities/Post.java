package com.example.foobarapplication.entities;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.foobarapplication.R;

import java.util.ArrayList;
@Entity
public class Post extends AppCompatActivity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private ArrayList<Object> comments;
    private String author;
    private String content;
    private String date;
    private int likes;
    private int picture;
    private int profilePicture;
    private Uri uProfilePicture;
    private Uri uPic;
    private boolean isLiked = false;



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
}

