package com.example.foobarapplication.entities;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class Post2  {
    private int id;
    private String text;
    private String profile;
    private Date date;
    private String img;
    private String profileImg;
    private int likes;

    // Constructor
    public Post2(int id, String text, String profile, Date date, String img, String profileImg, int likes) {
        this.id = id;
        this.text = text;
        this.profile = profile;
        this.date = date;
        this.img = img;
        this.profileImg = profileImg;
        this.likes = likes;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
