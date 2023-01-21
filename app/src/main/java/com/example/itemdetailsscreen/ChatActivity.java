package com.example.itemdetailsscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.itemdetailsscreen.databinding.ActivityChatBinding;
import com.example.itemdetailsscreen.databinding.ActivityItemDetailsBinding;
import com.example.itemdetailsscreen.models.User;
import com.example.itemdetailsscreen.utilities.Constants;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private User receiverUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadReceiverDetails();
    }

    private void loadReceiverDetails() {
        receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        binding.chatName.setText(receiverUser.name);
    }

}