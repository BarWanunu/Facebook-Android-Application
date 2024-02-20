package com.example.foobarapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Button btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(v -> {
            Intent i = new Intent(this, signup_Activity.class);
            startActivity(i);
        });
        Button itay_btn = findViewById(R.id.itay_btn);
        itay_btn.setOnClickListener(v -> {
            Intent i = new Intent(this, Activity_Post.class);
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
                Intent signInIntent = new Intent(this, main_page_Activity.class);
                signInIntent.putExtra("user", newUser);
                startActivity(signInIntent);
            } else {
                // Invalid credentials, show an error message or handle accordingly
                // For simplicity, a toast message is shown here
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
