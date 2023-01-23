package com.example.itemdetailsscreen;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.itemdetailsscreen.ui.main.SectionsPagerAdapter;
import com.example.itemdetailsscreen.databinding.ActivityTrackingBinding;

public class TrackingActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityTrackingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTrackingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        setNavBarListeners();



    }

    private void setNavBarListeners() {
        ImageButton profile = (ImageButton) findViewById(R.id.navProfile);
        profile.setOnClickListener(this);
        ImageButton addItem = (ImageButton) findViewById(R.id.plusItem);
        addItem.setOnClickListener(this);
        ImageButton chats = (ImageButton) findViewById(R.id.navChat);
        chats.setOnClickListener(this);
        ImageButton home = (ImageButton) findViewById(R.id.navHome);
        home.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){

            case R.id.navHome:
                intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                break;

            case R.id.navProfile:
                intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.plusItem:
                intent = new Intent(getApplicationContext(), AddItemActivity.class);
                startActivity(intent);
                break;

            case R.id.navChat:
                intent = new Intent(getApplicationContext(), RecentChatsActivity.class);
                startActivity(intent);
                break;
        }
    }
}