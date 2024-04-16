package com.example.foobarapplication.webServices;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.foobarapplication.entities.Post;
import com.example.foobarapplication.entities.User;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
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
    public void getAllPosts(MutableLiveData<Boolean> isGetPosts, MutableLiveData<List<Post>> liveDataPosts, String token) {
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
                            Log.d("TAG", "hello");
                            JSONArray postsArray = jsonObject.getJSONArray("posts");
                            List<Post> posts = new ArrayList<>();
                            String jsonString = jsonObject.toString();
                            // Log the JSON string
                            Log.d("TAG", jsonString);
                            for (int i = 0; i < postsArray.length(); i++) {
                                JSONObject postJson = postsArray.getJSONObject(i);
                                posts.add(parsePostFromJson(postJson));
                            }
                            liveDataPosts.postValue(posts);
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

    private Post parsePostFromJson(JSONObject postJson) throws JSONException {
        int id = postJson.getInt("id");
        String text = postJson.getString("text");
        String profile = postJson.getString("profile");
        String dateString = postJson.getString("date");
        //Date date = new Date(postJson.getLong("date"));  // assuming the date comes as a timestamp
        String img = postJson.optString("img", "");  // optString allows for missing 'img' field
        String profileImg = postJson.getString("profileImg");
        int likes = postJson.getInt("likes");

        //Post(int id, String author, String content, String date, int likes, int picture, int profilePicture)

        return new Post(id, profile, text, dateString, likes, img, profileImg);

    }
}
