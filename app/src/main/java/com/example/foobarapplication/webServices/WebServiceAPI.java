package com.example.foobarapplication.webServices;

import com.example.foobarapplication.entities.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface WebServiceAPI {

    @POST("/")
    Call<Void> createUser(@Body User user);

    @POST("/signin")
    Call<Void> checkUser(@Body User user);
}