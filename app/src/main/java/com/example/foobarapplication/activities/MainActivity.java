package com.example.foobarapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.foobarapplication.R;
import com.example.foobarapplication.entities.User;
import com.example.foobarapplication.viewModels.UserViewModel;

public class MainActivity extends AppCompatActivity {
    UserViewModel userViewModel = new UserViewModel();

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
            User newUser = (User) intent.getSerializableExtra("user");

//            // Check if the entered username and password match the user created during signup
            EditText emailEditText = findViewById(R.id.emailEditText);
            EditText passwordEditText = findViewById(R.id.passwordEditText);
            String enteredUsername = emailEditText.getText().toString();
            String enteredPassword = passwordEditText.getText().toString();
            User user = new User(enteredUsername, enteredPassword);
            if (newUser == null) {
                newUser = user;
            }
            userViewModel.check(newUser);


            userViewModel.getIsUserChecked().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean isUserChecked) {
                    if (isUserChecked != null && isUserChecked) {
                        // Successful login, navigate to the next activity
                        userViewModel.createToken(user);
                        userViewModel.getToken().observe(MainActivity.this, new Observer<String>() {
                            @Override
                            public void onChanged(String token) {
                                Intent signInIntent = new Intent(MainActivity.this, Activity_Post.class);
                                signInIntent.putExtra("user", user);
                                signInIntent.putExtra("token", token);
                                Toast.makeText(MainActivity.this, "Login Success, welcome to Facebook!", Toast.LENGTH_SHORT).show();
                                startActivity(signInIntent);
                            }
                        });
                    } else {
                        // Invalid credentials, show an error message or handle accordingly
                        // For simplicity, a toast message is shown here
                        Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }
}
