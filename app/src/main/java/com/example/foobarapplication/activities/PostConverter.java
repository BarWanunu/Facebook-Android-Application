package com.example.foobarapplication.activities;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.foobarapplication.entities.Post;
import com.example.foobarapplication.entities.Post2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostConverter {

    public static List<Post> convertPost2ListToPostList(List<Post2> post2List) {
        List<Post> postList = new ArrayList<>();
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        for (Post2 post2 : post2List) {

                Post post = new Post(
                        post2.getId(),
                        post2.getText(),
                        post2.getProfile(),
                        outputDateFormat.format(post2.getDate()), // Format the date to string
                        post2.getLikes(),
                        decodeImage(post2.getImg()),
                        decodeImage(post2.getProfileImg())
                );
                postList.add(post);
            }


        return postList;
    }

    private static Bitmap decodeImage(String imagePath) {
        // Here you need to implement logic to decode the image from the imagePath (URL, file path, etc.)
        // For example, if imagePath is a URL, you can use Picasso or Glide library to load the image.
        // If imagePath is a file path, you can use BitmapFactory.decodeFile() method.
        // This is just a placeholder method.
        return null;
    }
}
