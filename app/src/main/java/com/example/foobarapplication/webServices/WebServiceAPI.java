package com.example.foobarapplication.webServices;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceAPI {

    @POST("users/")
    Call<JsonObject> createUser(@Body JsonObject user);

    @POST("users/signin")
    Call<JsonObject> checkUser(@Body JsonObject user);

    @DELETE("users/{id}")
    Call<JsonObject> deleteUser(@Path("id") String username);
}