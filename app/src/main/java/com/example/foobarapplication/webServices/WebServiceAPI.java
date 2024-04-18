package com.example.foobarapplication.webServices;

import com.example.foobarapplication.entities.Post;
import com.example.foobarapplication.entities.Post2;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceAPI {

    @POST("users/")
    Call<JsonObject> createUser(@Body JsonObject user);

    @POST("users/signin")
    Call<JsonObject> checkUser(@Body JsonObject user);

    @DELETE("users/{id}")
    Call<JsonObject> deleteUser(@Path("id") String userId, @Header("Authorization") String token);

    @POST("token/")
    Call<JsonObject> createToken(@Body JsonObject user);

    @GET("posts/")
    Call<List<Post>> getAllPosts(@Header("Authorization") String token);

    @POST("posts")
    Call<Post> createPost(@Header("token") String token, @Body JsonObject postBody);

    @POST("posts")
    Call<Post> editPost(@Header("token") String token, @Body JsonObject postBody);

    @DELETE("posts/{id}")
    Call<JsonObject> deletePost(@Path("id") int postId, @Path("id") String userId, @Header("Authorization") String token);

}