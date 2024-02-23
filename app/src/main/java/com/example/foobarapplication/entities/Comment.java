package com.example.foobarapplication.entities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foobarapplication.R;

public class Comment extends AppCompatActivity {

    private int commentId;
    private int postId;
    private String commentAuthor;

    private String profilePicPath;
    private String commentContent;

    public Comment(int commentId, int postId, String commentAuthor, String commentContent, String profilePicPath) {
        this.commentId = commentId;
        this.postId = postId;
        this.commentAuthor = commentAuthor;
        this.commentContent = commentContent;
        this.profilePicPath=profilePicPath;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
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

    public String getProfilePicPath() {
        return profilePicPath;
    }
    public void setProfilePicPath(String profilePicPath) {
        this.profilePicPath = profilePicPath;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_layout);
    }
}