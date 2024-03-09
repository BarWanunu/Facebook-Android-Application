package com.example.foobarapplication.webServices;

import com.example.foobarapplication.activities.MainActivity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface WebServiceAPI {

    @POST("/login")
    Call<MainActivity> onCreate(@Body String name, String password);

    @POST("/signup")
    Call<MainActivity> executeSignup(@Body String name, String email, String password);
}
