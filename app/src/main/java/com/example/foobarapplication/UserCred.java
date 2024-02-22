package com.example.foobarapplication;
import java.io.Serializable;

public class UserCred implements Serializable {
    private String email;
    private String password;
    private String imagePath;
    private  String username;// New field for image path

    public UserCred(String email, String password, String imagePath,String username) {
        this.email = email;
        this.password = password;
        this.imagePath = imagePath;
        this.username=username;
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
    public String getUsername(){return this.username;}
}
