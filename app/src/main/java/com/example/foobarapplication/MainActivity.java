package com.example.foobarapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private String BASE_URL = "http://10.0.2.2:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Button btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(v -> {
            Intent i = new Intent(this, signup_Activity.class);
            startActivity(i);
        });


        Button btnSignin = findViewById(R.id.btnSignin);
        btnSignin.setOnClickListener(v -> {
            // Retrieve the UserCred object from the intent
            Intent intent = getIntent();
            UserCred newUser = (UserCred) intent.getSerializableExtra("user");


            // Check if the entered username and password match the user created during signup
            EditText emailEditText = findViewById(R.id.emailEditText);
            EditText passwordEditText = findViewById(R.id.passwordEditText);
            String enteredUsername = emailEditText.getText().toString();
            String enteredPassword = passwordEditText.getText().toString();

            if (newUser != null && newUser.getUsername().equals(enteredUsername) && newUser.getPassword().equals(enteredPassword)) {
                // Successful login, navigate to the next activity
                Intent signInIntent = new Intent(this, Activity_Post.class);
                signInIntent.putExtra("user", newUser);
                startActivity(signInIntent);
            } else if (enteredUsername.equals("guest") && enteredPassword.equals("Aa12345678")) {
                // Assuming R.drawable.my_drawable is your drawable resource
                int drawableResourceId = R.drawable.guest_profile;
                Uri drawableUri = Uri.parse("android.resource://" + getPackageName() + "/" + drawableResourceId);
                String imageUriString = drawableUri.toString();
                UserCred guestUser=new UserCred("guest@gmail.com","Aa12345678",imageUriString,"guest");
                Intent signInIntent = new Intent(this, Activity_Post.class);
                signInIntent.putExtra("user", guestUser);
                startActivity(signInIntent);
            }

            else {
                // Invalid credentials, show an error message or handle accordingly
                // For simplicity, a toast message is shown here
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
