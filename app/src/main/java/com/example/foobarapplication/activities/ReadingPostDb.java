package com.example.foobarapplication.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;

import com.example.foobarapplication.R;
import com.example.foobarapplication.entities.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Handler;


import android.content.Context;

import android.os.Looper;
import android.util.Log;

import com.example.foobarapplication.entities.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
public class ReadingPostDb {

    public static void readingPosts(JSONArray array, List<Post> posts, Context context) {
        try {
            // Iterate through the JSON array and create Post objects
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonPost = array.getJSONObject(i);
                int id = jsonPost.getInt("id");
                String author = jsonPost.getString("profile");
                String content = jsonPost.getString("text");
                String date = jsonPost.getString("date");
                int likes = jsonPost.getInt("likes");
                String picResourceName = jsonPost.getString("img");
                String profilePicResourceName = jsonPost.getString("profileImg");

                // Get resource IDs on the main thread
                int picResourceId = 1;
                int profilePicResourceId = 1;

                // Check if the resource IDs are valid
                if (picResourceId != 0 && profilePicResourceId != 0) {
                    // Post the creation of Post object back to the main thread
                    final int finalPicResourceId = 1;
                    final int finalProfilePicResourceId = 1;
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Post post = new Post(id, author, content, date, likes, finalPicResourceId, finalProfilePicResourceId);
                            posts.add(post);
                        }
                    });
                } else {
                    // Handle invalid resource IDs (e.g., log an error, skip the post, etc.)
                    Log.e("ReadingPostDb", "Invalid resource ID for post with ID: " + id);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private static int getResourceId(String resourceName, Context context) {
        return context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
    }

}
