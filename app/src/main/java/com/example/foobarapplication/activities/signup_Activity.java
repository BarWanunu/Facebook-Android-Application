package com.example.foobarapplication.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foobarapplication.R;
import com.example.foobarapplication.entities.User;
import com.example.foobarapplication.viewModels.UserViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class signup_Activity extends AppCompatActivity {
    private static final int GALLERY_REQ_CODE = 1000;
    private static final int CAMERA_REQ_CODE = 1001;

    private ImageView profileView;
    private String selectedImageBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        EditText editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        EditText editTextPasswordSign = findViewById(R.id.editTextPasswordSign);
        EditText editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        EditText usernameText = findViewById(R.id.usernametext);
        Button btnSubmit = findViewById(R.id.btnSubmit);
        profileView = findViewById(R.id.profileView);
        Button btnAddPhoto = findViewById(R.id.btnAddPhoto);
        Button btnOpenCamera = findViewById(R.id.btnOpenCamera);

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve user input from EditText fields
                String email = editTextEmailAddress.getText().toString().trim();
                String password = editTextPasswordSign.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();
                String username = usernameText.getText().toString().trim();

                // Validate email format
                if (!isValidEmail(email)) {
                    Toast.makeText(signup_Activity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate password requirements
                if (password.length() < 8 || !containsLetterAndNumber(password)) {
                    Toast.makeText(signup_Activity.this, "Password must be at least 8 characters long and contain both letters and numbers", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Confirm password match
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(signup_Activity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if username is provided
                if (username.length() < 1) {
                    Toast.makeText(signup_Activity.this, "please enter a username", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if a photo is selected
                if (TextUtils.isEmpty(selectedImageBase64)) {
                    Toast.makeText(signup_Activity.this, "Please add a photo", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a new User object with provided details
                User newUser = new User(email, username, password, confirmPassword, selectedImageBase64);

                // Add the new user to the database using ViewModel
                UserViewModel userViewModel = new UserViewModel(signup_Activity.this);
                userViewModel.add(newUser);

                // Proceed to MainActivity and pass the new user
                Intent signInIntent = new Intent(signup_Activity.this, MainActivity.class);
                signInIntent.putExtra("user", newUser);
                startActivity(signInIntent);
                Toast.makeText(signup_Activity.this, "User created successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Open the gallery to select an image
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQ_CODE);
    }

    // Open the camera to take an image
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the result of gallery or camera activity
        if (resultCode == RESULT_OK) {
            // Process the selected image from the gallery
            if (requestCode == GALLERY_REQ_CODE && data != null) {
                handleGalleryResult(data.getData());
            } else if (requestCode == CAMERA_REQ_CODE && data != null) {
                try {
                    // Process the captured image from the camera
                    handleCameraResult(data);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    // Convert the selected image to Base64 and display it
    private void handleGalleryResult(Uri selectedImageUri) {
        selectedImageBase64 = ImageUtils.base64FromUri(selectedImageUri, this);
        profileView.setImageURI(selectedImageUri);
    }

    // Retrieve the captured image bitmap and display it
    private void handleCameraResult(Intent data) throws IOException {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            selectedImageBase64 = ImageUtils.base64FromUri(saveCameraImage(imageBitmap), this);// You may want to save this image to a file.
            profileView.setImageBitmap(imageBitmap);
        }
    }

    // Save the captured image to a file and return its URI
    private Uri saveCameraImage(Bitmap imageBitmap) throws IOException {
        File imageFile = createImageFile();
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Uri.fromFile(imageFile);
    }

    // Save the captured image to a file and return its URI
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        return imageFile;
    }

    // Checking if the email is valid
    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean containsLetterAndNumber(String password) {
        boolean containsLetter = false;
        boolean containsNumber = false;

        // Goes over the password to check letter and a number existence
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                containsLetter = true;
            } else if (Character.isDigit(c)) {
                containsNumber = true;
            }

            // Password contains both letters and numbers
            if (containsLetter && containsNumber) {
                return true;
            }
        }
    // Password does not contain both letters and numbers
        return false;
    }

}
