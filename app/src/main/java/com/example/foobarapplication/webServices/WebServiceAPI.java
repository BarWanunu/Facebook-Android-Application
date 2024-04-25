package com.example.foobarapplication.webServices;

import com.example.foobarapplication.entities.Post;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
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

    @POST("posts/create")
    Call<Post> createPost(@Header("Authorization") String token, @Body JsonObject postBody);

    @DELETE("users/{userId}/posts/{postId}")
    Call<Post> deletePost(@Path("userId") String userId, @Path("postId") int postId, @Header("Authorization") String token);

    @PATCH("users/{id}/posts/{pid}")
    Call<Post> editPost(@Path("id") String userId, @Path("pid") int postId, @Header("Authorization") String token, @Body JsonObject jsonBody);

    @PATCH("User/{id}")
    Call<JsonObject> editUser(@Path("id") String userId, @Header("Authorization") String token, @Body JsonObject jsonObject);


    @GET("User/{id}")
    Call<JsonObject> getUser(@Path("id") String userId, @Header("Authorization") String token);

    @PATCH("users/{userId}/posts/{postId}/like")
    Call<JsonObject> likePost(@Path("userId") String userId, @Path("postId") int postId, @Header("Authorization") String token, @Body JsonObject likeStatus);

    @GET("id/friends")
    Call<JsonObject> getAllFriends(@Path("userId")String userId, @Header("AAuthorization") String token);
}