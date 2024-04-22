package com.example.foobarapplication.webServices;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.foobarapplication.entities.Post;
import com.example.foobarapplication.entities.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PostAPI {
    // private MutableLiveData<List<Post>> postListData;
    // private PostDao dao;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public PostAPI() {
        retrofit = RetrofitBuilder.getInstance();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void add(Post post, String token) {
        JsonObject postCreate = new JsonObject();
        postCreate.addProperty("text", post.getContent());
        postCreate.addProperty("date", post.getDate());
        postCreate.addProperty("image", "");
        Call<Post> call = webServiceAPI.createPost("Bearer " + token, postCreate);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(@NonNull Call<Post> call, @NonNull Response<Post> response) {
                if (response.isSuccessful()) {
                    Log.d("AddPost", "Post added");
                } else {
                    Log.d("AddPost", "Failed to add post");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Post> call, @NonNull Throwable t) {
                Log.d("AddPost", "Failed to add post: " + t.getMessage());
            }
        });
    }

    public void delete(Post post, String token) {
        Call<Post> call = webServiceAPI.deletePost(post.getAuthor(), post.getId(), "Bearer " + token);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(@NonNull Call<Post> call, @NonNull Response<Post> response) {
                if (response.isSuccessful()) {
                    Log.d("AddPost", "Post added");
                } else {
                    Log.d("AddPost", "Failed to add post");
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }

    public void edit(Post post, String token) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("editedText", post.getContent());
        Call<Post> call = webServiceAPI.editPost(post.getAuthor(), post.getId(), "Bearer " + token, jsonObject);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(@NonNull Call<Post> call, @NonNull Response<Post> response) {
                if (response.isSuccessful()) {
                    Log.d("EditPost", "Post edited successfully");
                } else {
                    Log.d("EditPost", "Failed to edit post");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Post> call, @NonNull Throwable t) {
                Log.e("EditPost", "Network error: " + t.getMessage());
            }
        });
    }

    public void likePost(Post post, String token) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("isLiked", post.getIsLiked());
        Call<JsonObject> call = webServiceAPI.likePost(post.getAuthor(), post.getId(), "Bearer " + token, jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    User user2 = gson.fromJson(response.body().get("likes"), User.class);
                    Log.d("LikePost", "Post liked successfully");
                } else {
                    Log.d("LikePost", "Failed to like post");
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.e("LikePost", "Network error: " + t.getMessage());
            }
        });
    }



    public void getAllPosts(String token, MutableLiveData<List<Post>> posts) {
//        ApiResponseCallback<List<Post>> callback
        webServiceAPI.getAllPosts("Bearer " + token).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    List<Post> list = response.body();
                    Collections.sort(list);
                    posts.postValue(response.body());
                    Log.d("getAllPosts", "succeeded to fetch posts: ");
                } else {
                    Log.e("API Error", "Failed to fetch posts");
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e("API Error", "Failed to fetch posts: " + t.getMessage());
            }


        });
    }
}


//            @Override//            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
//                // Handle response
//                if (response.isSuccessful()) {
//                    // Process successful response
//                    try {
//                        // Parse JSON response
//                        JSONObject jsonObject = new JSONObject(response.body().toString());
//                        boolean success = jsonObject.getBoolean("success");
//                        isGetPosts.postValue(success);
//
//                        if (success) {
//                            // Parse posts array and update LiveData
//                            JSONArray postsArray = jsonObject.getJSONArray("posts");
//                            List<Post> posts = new ArrayList<>();
//                            posts = ReadingPostDb.readingPosts(postsArray, posts, context,callback); // Pass the latch to readingPosts
//                            postListData.postValue(posts);
//
//                            Log.i(TAG, "hello"+posts.size());
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        isGetPosts.postValue(false);
//                    }
//                } else {
//                    // Handle unsuccessful response
//                    isGetPosts.postValue(false);
//                }
//
//                // Release the latch after handling the response
////                latch.countDown();
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                isGetPosts.postValue(false);
////                latch.countDown(); // Release the latch in case of failure
//            }


//    private void parsePostFromJson(JSONObject postJson) throws JSONException {
//        Log.d("TAG", postJson.toString());
//        int id = postJson.getInt("id");
//        String text = postJson.getString("text");
//        String profile = postJson.getString("profile");
//        String dateString = postJson.getString("date");
//        int likes = postJson.getInt("likes");
////        Post2 post = new Post2(id, profile, text, dateString, likes);
//
//
//        // Create a new Handler associated with the main UI thread
//
//
//    }
//}
