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
    private String selectedImagePath;

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
                String email = editTextEmailAddress.getText().toString().trim();
                String password = editTextPasswordSign.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();
                String username = usernameText.getText().toString().trim();

                if (!isValidEmail(email)) {
                    Toast.makeText(signup_Activity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 8 || !containsLetterAndNumber(password)) {
                    Toast.makeText(signup_Activity.this, "Password must be at least 8 characters long and contain both letters and numbers", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!password.equals(confirmPassword)) {
                    Toast.makeText(signup_Activity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (username.length() < 1) {
                    Toast.makeText(signup_Activity.this, "please enter a username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(selectedImagePath)) {
                    Toast.makeText(signup_Activity.this, "Please add a photo", Toast.LENGTH_SHORT).show();
                    return;
                }

                User newUser = new User(email, username, password,confirmPassword, selectedImagePath);
                UserViewModel userViewModel = new UserViewModel();
                userViewModel.add(newUser);
                Intent signInIntent = new Intent(signup_Activity.this, MainActivity.class);
                signInIntent.putExtra("user", newUser);
                startActivity(signInIntent);
                Toast.makeText(signup_Activity.this, "User created successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQ_CODE);
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQ_CODE && data != null) {
                handleGalleryResult(data.getData());
            } else if (requestCode == CAMERA_REQ_CODE && data != null) {
                try {
                    handleCameraResult(data);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void handleGalleryResult(Uri selectedImageUri) {
        selectedImagePath = selectedImageUri.toString();
        profileView.setImageURI(selectedImageUri);
    }

    private void handleCameraResult(Intent data) throws IOException {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            selectedImagePath = saveCameraImage(imageBitmap);// You may want to save this image to a file.
            profileView.setImageBitmap(imageBitmap);
        }
    }

    private String saveCameraImage(Bitmap imageBitmap) throws IOException {
        File imageFile = createImageFile();
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Uri.fromFile(imageFile).toString();
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        selectedImagePath = imageFile.getAbsolutePath();
        return imageFile;
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean containsLetterAndNumber(String password) {
        boolean containsLetter = false;
        boolean containsNumber = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                containsLetter = true;
            } else if (Character.isDigit(c)) {
                containsNumber = true;
            }

            if (containsLetter && containsNumber) {
                return true;  // Password contains both letters and numbers
            }
        }

        return false;  // Password does not contain both letters and numbers
    }

}
