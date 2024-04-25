package com.example.foobarapplication.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class User implements Serializable {
    @PrimaryKey
    @NonNull
    private String userName;
    private String email;
    private String password;
    private String confirmPassword;
    private String photo;

    @Ignore
    public User(String email, String password, String photo, String userName) {
        this.email = email;
        this.password = password;
        this.photo = photo;
        this.userName = userName;
    }

    @Ignore
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @Ignore
    public User(String userName) {
        this.userName = userName;
    }

    public User(String email, String userName, String password, String confirmPassword, String photo) {
        this(email, password, photo, userName);
        this.confirmPassword = confirmPassword;
    }

    // Getters and setters for the new field
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getConfirmPasswordPassword() {
        return this.confirmPassword;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
