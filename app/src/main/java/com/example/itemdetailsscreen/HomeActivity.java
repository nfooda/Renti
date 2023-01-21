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
        ImageButton profile = (ImageButton) findViewById(R.id.navProfile);
        ImageButton AddItemBtn = (ImageButton) findViewById(R.id.plusItem);
    }

    public void BtnClickHandler(View v) {
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
                i = new Intent(HomeActivity.this, AddItem.class);
                startActivity(i);
                break;
        }
    }
}