package com.example.foobarapplication;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/login")
    Call<MainActivity> onCreate(@Body String name, String password);

    @POST("/signup")
    Call<MainActivity> executeSignup(@Body String name, String email, String password);
}
