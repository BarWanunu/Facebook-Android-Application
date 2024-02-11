package com.example.foobarapplication;

import java.io.Serializable;

public class UserCred implements Serializable {
    private String email;
    private String password;

    public UserCred(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public String getEmail(){
        return this.email;
    }
    public String getPassword()
    {
        return this.password;
    }
}
