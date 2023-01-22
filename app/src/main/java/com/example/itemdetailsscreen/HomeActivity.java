package com.example.itemdetailsscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.itemdetailsscreen.utilities.Constants;
import com.example.itemdetailsscreen.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.prefs.PreferenceChangeEvent;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    PreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        setContentView(R.layout.activity_home);

        // getToken();

        ImageButton cars = (ImageButton) findViewById(R.id.carButton);
        cars.setOnClickListener(this);
        ImageButton profile = (ImageButton) findViewById(R.id.navProfile);
        profile.setOnClickListener(this);
        ImageButton addItem = (ImageButton) findViewById(R.id.plusItem);
        addItem.setOnClickListener(this);

        ImageButton chats = (ImageButton) findViewById(R.id.nav_chat);
        chats.setOnClickListener(view -> {
            Intent i = new Intent(HomeActivity.this, RecentChatsActivity.class);
            startActivity(i);
        });

    }




    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_FIREBASE_ID)
                );
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnSuccessListener(unused -> showToast("Token updated Successfully"))
                .addOnFailureListener(e -> showToast("Unable to update token"));
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.carButton:
                i = new Intent(HomeActivity.this, ItemsListActivity.class);
                startActivity(i);
                break;
            case R.id.navProfile:
                i = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(i);
                break;
            case R.id.plusItem:
                i = new Intent(HomeActivity.this, AddItemActivity.class);
                startActivity(i);
                break;
        }
    }

}
