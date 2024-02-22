package com.example.foobarapplication;

import static androidx.core.content.res.TypedArrayUtils.getResourceId;

import android.content.Context;

import com.example.foobarapplication.entities.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class readingPosts {

    public static void readingPosts(Context context) {
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.posts);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray jsonArray = new JSONArray("posts");

            List<Post> posts = GlobalPostList.postList;

            for(int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonPost = jsonArray.getJSONObject(i);
                int id = jsonPost.getInt("id");
                String author = jsonPost.getString("author");
                String content = jsonPost.getString("content");
                String date = jsonPost.getString("date");
                int likes = jsonPost.getInt("likes");
                int picResourceId = getResourceId(context, jsonPost.getString("img"));
                int profilePicResourceId = getResourceId(context, jsonPost.getString("img"));
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
