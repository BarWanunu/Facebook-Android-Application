package com.example.foobarapplication.entities;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class User implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private  String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String imagePath;

    public User(String email, String password, String imagePath, String username) {
        this.email = email;
        this.password = password;
        this.imagePath = imagePath;
        this.username=username;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String email ,String username, String password, String confirmPassword, String imagePath) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.imagePath = imagePath;
    }

    // Getters and setters for the new field
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getEmail(){
        return this.email;
    }
    public String getPassword()
    {
        return this.password;
    }
    public String getConfirmPasswordPassword() {return this.confirmPassword;}
    public String getUsername(){return this.username;}
}
