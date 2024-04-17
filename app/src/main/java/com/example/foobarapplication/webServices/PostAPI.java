package com.example.foobarapplication.webServices;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.foobarapplication.activities.ReadingPostDb;
import com.example.foobarapplication.entities.Post;
import com.example.foobarapplication.entities.Post2;
import com.example.foobarapplication.entities.User;
import com.example.foobarapplication.viewModels.PostsViewModel;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;

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

        String token="aa";

        Call<Post> call = webServiceAPI.createPost(token, postCreate);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(@NonNull Call<Post> call, @NonNull Response<Post> response) {
                if (response.isSuccessful()){
                    Log.d("AddPost", "Post added");
                }
                else {
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

        public void getAllPosts(MutableLiveData<Boolean> isGetPosts, MutableLiveData<List<Post>> postListData, String token, Context context) {
            JsonObject userCheck = new JsonObject();
            Call<JsonObject> call = webServiceAPI.getAllPosts("Bearer " + token);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            boolean success = jsonObject.getBoolean("success");
                            isGetPosts.postValue(success);

                            if (success) {
                                JSONArray postsArray = jsonObject.getJSONArray("posts");
                                List<Post> posts = new ArrayList<>();
                                ReadingPostDb.readingPosts(postsArray, posts, context); // Pass the Context object
                                Log.i(TAG, posts.toString());
                                postListData.postValue(posts);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            isGetPosts.postValue(false);
                        }
                    } else {
                        isGetPosts.postValue(false);
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    isGetPosts.postValue(false);
                }
            });
        }



    private void parsePostFromJson(JSONObject postJson) throws JSONException {
        Log.d("TAG", postJson.toString());
        int id = postJson.getInt("id");
        String text = postJson.getString("text");
        String profile = postJson.getString("profile");
        String dateString = postJson.getString("date");
        int likes = postJson.getInt("likes");
        Post2 post = new Post2(id, profile, text, dateString, likes);


        // Create a new Handler associated with the main UI thread


    }
}
