package com.example.itemdetailsscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ImageButton cars = (ImageButton) findViewById(R.id.carButton);
        cars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ItemsListActivity.class);
                startActivity (i);

            }
        });
        ImageButton profile = (ImageButton) findViewById(R.id.navProfile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity (i);

            }
        });
    }
}
