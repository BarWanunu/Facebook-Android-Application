package com.example.foobarapplication.webServices;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.example.foobarapplication.activities.PostConverter.convertPost2ListToPostList;
import static com.example.foobarapplication.entities.Post.fromServerModel;

import android.content.Context;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.foobarapplication.activities.ApiResponseCallback;
import com.example.foobarapplication.activities.ReadingPostDb;
import com.example.foobarapplication.entities.Post;
import com.example.foobarapplication.entities.Post2;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

    public void add(Post post) {
        JsonObject postCreate = new JsonObject();
        postCreate.addProperty("text", post.getContent());
        postCreate.addProperty("date", post.getDate());
        postCreate.addProperty("image", "");

        String token = "aa";

        Call<Post> call = webServiceAPI.createPost(token, postCreate);
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

    public void delete(Post post, MutableLiveData<Boolean> isPostDeleted, String token) {
        Call<JsonObject> call = webServiceAPI.deletePost(post.getId(), post.getAuthor(), "Bearer " + token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        boolean success = jsonObject.getBoolean("success");
                        isPostDeleted.postValue(success);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    isPostDeleted.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void getAllPosts(String token, MutableLiveData<List<Post>> posts ) {
//        ApiResponseCallback<List<Post>> callback
        webServiceAPI.getAllPosts("Bearer " + token).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                   posts.postValue(response.body());
//                    List<Post2> post2List = response.body();
//                    List<Post> posts1 = fromServerModel(post2List);


                } else {
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
