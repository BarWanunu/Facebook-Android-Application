package com.example.foobarapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.ImageButton;
import android.widget.PopupMenu;

import java.net.DatagramPacket;

    boolean isDarkMode = false;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_page);

            ImageButton menu = findViewById(R.id.menu);
            menu.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(main_page_Activity.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(item -> {
                    int id = item.getItemId();
                    if (id == R.id.action_dark_mode) {
                        // Toggle the mode
                        isDarkMode = !isDarkMode;

                        // Update UI based on the current mode
                        if (isDarkMode) {
                            // Set dark mode background, text color, etc.
                            getWindow().getDecorView().setBackgroundColor(Color.BLACK);
                            // Adjust other UI elements as needed
                        } else {
                            // Set light mode background, text color, etc.
                            getWindow().getDecorView().setBackgroundColor(Color.WHITE);
                            // Adjust other UI elements as needed
                        }
                        return true;
                    } else if (id == R.id.action_logout) {
                        // Implement logout functionality here
                        // Start MainActivity
                        Intent intent = new Intent(main_page_Activity.this, MainActivity.class);
                        startActivity(intent);
                        // Close current activity if necessary
                        finish();
                        return true;
                    }
                    return false;
                });
                popupMenu.show();
            });
        }
    }
