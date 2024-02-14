package com.example.foobarapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class signup_Activity extends AppCompatActivity {
    private final int GALLARY_REQ_CODE=1000;
    private ImageView profileView;
    private String selectedImagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        EditText editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        EditText editTextPasswordSign = findViewById(R.id.editTextPasswordSign);
        EditText editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        Button btnSubmit = findViewById(R.id.btnSubmit);
        profileView= findViewById(R.id.profileView);
        Button btnAddPhoto= findViewById(R.id.btnAddPhoto);
        btnAddPhoto.setOnClickListener(v -> {

                Intent iphoto= new Intent(Intent.ACTION_PICK);
                iphoto.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iphoto,GALLARY_REQ_CODE);


        });



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editTextEmailAddress.getText().toString().trim();
                String password = editTextPasswordSign.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();

                // Validate email format
                if (!isValidEmail(username)) {
                    Toast.makeText(signup_Activity.this, "Invalid email address", Toast.LENGTH_SHORT).show();

                    return;
                }

                // Validate password length
                if (password.length() < 6) {
                    Toast.makeText(signup_Activity.this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate matching passwords
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(signup_Activity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                UserCred newUser = new UserCred(username, password, selectedImagePath);

                // Create an intent and put the UserCred object as an extra
                Intent signInIntent = new Intent(signup_Activity.this, MainActivity.class);
                signInIntent.putExtra("user", newUser);
                startActivity(signInIntent);

                Toast.makeText(signup_Activity.this, "User created successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Function to validate email format
    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLARY_REQ_CODE && data != null) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = selectedImageUri.toString();

                // Display the selected image
                profileView.setImageURI(selectedImageUri);
            }
        }
    }

}
