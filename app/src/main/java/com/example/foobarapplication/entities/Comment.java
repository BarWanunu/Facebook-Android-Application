package com.example.foobarapplication.entities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foobarapplication.R;

public class Comment extends AppCompatActivity {

    private int commentId;
    private String commentAuthor;
    private String commentContent;

    public Comment(String commentAuthor, String commentContent) {
        this.commentAuthor = commentAuthor;
        this.commentContent = commentContent;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getCommentAuthor() {
        return commentAuthor;
    }

    public void setCommentAuthor(String commentAuthor) {
        this.commentAuthor = commentAuthor;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_layout);
    }
}