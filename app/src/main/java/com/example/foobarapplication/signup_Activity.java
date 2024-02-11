package com.example.foobarapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class signup_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        EditText editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        EditText editTextPasswordSign = findViewById(R.id.editTextPasswordSign);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editTextEmailAddress.getText().toString();
                String password = editTextPasswordSign.getText().toString();

                if (!username.isEmpty() && !password.isEmpty()) {
                    UserCred newUser = new UserCred(username, password);

                    // Create an intent and put the UserCred object as an extra
                    Intent signInIntent = new Intent(signup_Activity.this, MainActivity.class);
                    signInIntent.putExtra("user", newUser);
                    startActivity(signInIntent);


                    Toast.makeText(signup_Activity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(signup_Activity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
