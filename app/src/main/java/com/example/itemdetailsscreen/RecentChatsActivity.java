package com.example.itemdetailsscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.itemdetailsscreen.databinding.ActivityRecentChatsBinding;
import com.example.itemdetailsscreen.utilities.Constants;
import com.example.itemdetailsscreen.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class RecentChatsActivity extends AppCompatActivity {

    PreferenceManager preferenceManager;
    ActivityRecentChatsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecentChatsBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setContentView(binding.getRoot());
    }


}