package com.example.foobarapplication;

import android.content.Context;

import com.example.foobarapplication.entities.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ReadingPosts {

    public static void readingPosts(Context context, List<Post> posts) {
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.posts);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            // Read the content of the JSON file into a StringBuilder
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            // Parse the JSON content into a JSONObject
            JSONObject object = new JSONObject(stringBuilder.toString());
            JSONArray array = object.getJSONArray("posts");

            // Iterate through the JSON array and create Post objects
            for(int i=0; i<array.length(); i++) {
                JSONObject jsonPost = array.getJSONObject(i);
                int id = jsonPost.getInt("id");
                String author = jsonPost.getString("author");
                String content = jsonPost.getString("content");
                String date = jsonPost.getString("date");
                int likes = jsonPost.getInt("likes");
                int picResourceId = getResourceId(context, jsonPost.getString("picture"));
                int profilePicResourceId = getResourceId(context, jsonPost.getString("profilePicture"));
                Post post = new Post(id, author, content, date, likes, picResourceId, profilePicResourceId);
                posts.add(post);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
    public static int getResourceId(Context context, String resourceName) {
        return context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
    }
}
