package com.example.foobarapplication.entities;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foobarapplication.R;

public class Comment extends AppCompatActivity {

    private int commentId;
    private int postId;
    private String commentAuthor;
    private int profilePicture;
    private Uri uProfilePicture;
    private String commentContent;

    public Comment(int commentId, int postId, String commentAuthor, String commentContent, Uri uProfilePicture) {
        this.commentId = commentId;
        this.postId = postId;
        this.commentAuthor = commentAuthor;
        this.commentContent = commentContent;
        this.profilePicture = -1;
        this.uProfilePicture = uProfilePicture;
    }

    public Comment(int commentId, int postId, String commentAuthor, String commentContent, int profilePicture) {
        this.commentId = commentId;
        this.postId = postId;
        this.commentAuthor = commentAuthor;
        this.commentContent = commentContent;
        this.profilePicture = profilePicture;
        this.uProfilePicture = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_layout);
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

    public int getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(int profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Uri getuProfilePicture() {
        return uProfilePicture;
    }

    public void setuProfilePicture(Uri uProfilePicture) {
        this.uProfilePicture = uProfilePicture;
    }
}