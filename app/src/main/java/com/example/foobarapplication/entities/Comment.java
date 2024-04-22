package com.example.foobarapplication.entities;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.foobarapplication.R;

import java.io.Serializable;

@Entity
public class Comment {
    @PrimaryKey(autoGenerate = true)
    private int commentId;
    private int postId;
    private String commentAuthor;
    private String sProfilePicture;
    private String commentContent;

    public Comment(int commentId, int postId, String commentAuthor, String commentContent, String sProfilePicture) {
        this.commentId = commentId;
        this.postId = postId;
        this.commentAuthor = commentAuthor;
        this.commentContent = commentContent;
        this.sProfilePicture = sProfilePicture;
    }

    public Comment(int commentId, int postId, String commentAuthor, String commentContent, int profilePicture) {
        this.commentId = commentId;
        this.postId = postId;
        this.commentAuthor = commentAuthor;
        this.commentContent = commentContent;
        this.sProfilePicture = "";
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


    public String getSProfilePicture() {
        return sProfilePicture;
    }

    public void setSProfilePicture(String uProfilePicture) {
        this.sProfilePicture = uProfilePicture;
    }


}