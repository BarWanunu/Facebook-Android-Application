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

public class Post2 extends AppCompatActivity {
        @PrimaryKey(autoGenerate = true)
        private int id;
        private ArrayList<Object> comments;
        private String author;
        private String content;
        private String date;

        //    private Date dDate;
        private int likes;

        private boolean isLiked = false;
    public Post2(int id, String author, String content, String date, int likes) {
        this.id = id;
        this.comments = null;
        this.author = author;
        this.content = content;
        this.date = date;
        this.likes = likes;
        this.isLiked = false;
    }

    }
