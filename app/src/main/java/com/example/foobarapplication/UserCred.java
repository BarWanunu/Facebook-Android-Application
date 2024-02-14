package com.example.foobarapplication;
import java.io.Serializable;

public class UserCred implements Serializable {
    private String email;
    private String password;
    private String imagePath;  // New field for image path

    public UserCred(String email, String password, String imagePath) {
        this.email = email;
        this.password = password;
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
}
