package com.example.itemdetailsscreen;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.itemdetailsscreen.databinding.ActivitySignupBinding;
import com.example.itemdetailsscreen.utilities.Constants;
import com.example.itemdetailsscreen.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private String encodedImage;
    private PreferenceManager preferenceManager;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        mAuth = FirebaseAuth.getInstance();
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        encodedImage = encodeImage(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.default_user_image));
        setListeners();

    }

    private void setListeners() {
        binding.loginBtn.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SigninActivity.class)));
        binding.signupBtn.setOnClickListener(v -> {
            if (isValidSignupDetails()) {
                signup();
            }
        });
        binding.layoutImage.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickImage.launch(intent);
        });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void signup() {
        loading(true);
        String email = binding.email.getText().toString();
        String password = binding.password.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("AUTH", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            addUserToDatabase(user);
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            loading(false);
                            Log.w("AUTH", "createUserWithEmail:failure", task.getException());
                            showToast("Authentication failed");
                        }
                    }
                });
    }

    private void addUserToDatabase(FirebaseUser authUser) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME, binding.fullName.getText().toString());
        user.put(Constants.KEY_EMAIL, binding.email.getText().toString());
        user.put(Constants.KEY_PHONE, binding.phone.getText().toString());
        user.put(Constants.KEY_PASSWORD, binding.password.getText().toString());
        user.put(Constants.KEY_IMAGE, encodedImage);
        user.put(Constants.KEY_CITY, binding.city.getText().toString());
        user.put(Constants.KEY_USER_ID, authUser.getUid());
        // TODO: Send data to mySql database
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    loading(false);
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_FIREBASE_ID, documentReference.getId());
                    preferenceManager.putString(Constants.KEY_USER_ID, authUser.getUid());
                    preferenceManager.putString(Constants.KEY_NAME, binding.fullName.getText().toString());
                    preferenceManager.putString(Constants.KEY_IMAGE, encodedImage);
                    preferenceManager.putString(Constants.KEY_CITY, binding.city.getText().toString());
                    preferenceManager.putString(Constants.KEY_EMAIL, binding.email.getText().toString());
                    preferenceManager.putString(Constants.KEY_PHONE, binding.phone.getText().toString());
                    Log.d("FIRESTORE", "Data was stored Successfully");
                })
                .addOnFailureListener(exception -> {
                    loading(false);
                    Log.d("FIRESTORE", "Data was wasn't stored to firebase");
                    showToast(exception.getMessage());
                });
    }

    private void updateUI() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageProfile.setImageBitmap(bitmap);
                            binding.textAddImage.setVisibility(View.GONE);
                            encodedImage = encodeImage(bitmap);
                        } catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private Boolean isValidSignupDetails() {
        Pattern egPhonePattern = Pattern.compile("^01\\d{9}$");
        Matcher phoneMatcher = egPhonePattern.matcher(binding.phone.getText().toString());
        if (binding.fullName.getText().toString().trim().isEmpty()) {
            showToast("Enter Name");
            return false;
        } else if (binding.email.getText().toString().trim().isEmpty()) {
            showToast("Enter Email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getText().toString()).matches()) {
            showToast("Enter a valid Email");
            return false;
        } else if (!phoneMatcher.matches()) { // this assumes a phone is required
            showToast("Enter a valid Phone Number");
            return false;
        } else if (binding.password.getText().toString().trim().isEmpty()) {
            showToast("Enter Password");
            return false;
        } else if (binding.city.getText().toString().trim().isEmpty()) {
            showToast("Enter City");
            return false;
        } else {
            return true;
        }
    }

    private void loading (Boolean isLoading) {
        if (isLoading) {
            binding.signupBtn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.signupBtn.setVisibility(View.VISIBLE);

        }
    }
}