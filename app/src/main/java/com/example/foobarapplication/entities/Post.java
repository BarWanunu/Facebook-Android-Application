package com.example.foobarapplication.entities;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foobarapplication.R;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Post extends AppCompatActivity {
    private int id;
    private String author;
    private String content;
    private String date;
    private int likes;
    private int pic;
    private int profilePicture;
    private Uri uProfilePicture;
    private Uri uPic;
    private List<String> comments;
    private boolean clicked = false;

    public Post() {
        this.pic = R.drawable.pingpong;
    }

    public Post(int id, String author, String content,String date, int likes, Uri uPic, Uri uProfilePicture) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.date = date;
        this.likes = likes;
        this.pic = -1;
        this.profilePicture = -1;
        this.uPic = uPic;
        this.uProfilePicture = uProfilePicture;
        this.comments = new ArrayList<>();
    }

    public Post(int id, String author, String content, String date, int likes, int pic, int profilePicture){
        this.id = id;
        this.author = author;
        this.content = content;
        this.date = date;
        this.likes = likes;
        this.pic = pic;
        this.profilePicture = profilePicture;
        this.uPic = null;
        this.uProfilePicture = null;
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
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_layout);
        ImageButton likeBtn = findViewById(R.id.likeButton);
        likeBtn.setOnClickListener(v -> {
                    if (clicked == false) {
                        clicked = true;
                        addLike();
                    } else {
                        clicked = false;
                        unLike();
                    }

                }
        );
    }

}

