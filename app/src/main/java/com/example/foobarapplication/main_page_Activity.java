package com.example.foobarapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.net.DatagramPacket;

public class main_page_Activity extends AppCompatActivity {
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        // Retrieve the UserCred object from the intent
        Intent intent = getIntent();
        UserCred user = (UserCred) intent.getSerializableExtra("user");

        // Set the profile image using the image path
        if (user != null) {
            String imagePath = user.getImagePath();
            if (imagePath != null && !imagePath.isEmpty()) {
                ImageView profileImageView = findViewById(R.id.profileImageView);
                profileImageView.setImageURI(Uri.parse(imagePath));
            }
        }

        // ... (rest of your code)
    }
}