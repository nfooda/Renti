package com.example.itemdetailsscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.itemdetailsscreen.utilities.Constants;
import com.example.itemdetailsscreen.utilities.PreferenceManager;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    PreferenceManager preferenceManager;
    SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        setContentView(R.layout.activity_home);
        setNavBarListeners();
        search = (SearchView) findViewById(R.id.searchView2);
        SetupSearchView();
        updateWelcomMessage();
    }

    private void updateWelcomMessage() {
        TextView tvWelcome = findViewById(R.id.welcome);
        tvWelcome.setText("Home");
    }

    private void setNavBarListeners() {
        ImageButton profile = (ImageButton) findViewById(R.id.navProfile);
        profile.setOnClickListener(this);
        ImageButton addItem = (ImageButton) findViewById(R.id.plusItem);
        addItem.setOnClickListener(this);
        ImageButton chats = (ImageButton) findViewById(R.id.navChat);
        chats.setOnClickListener(this);
        ImageButton track = (ImageButton) findViewById(R.id.navTrack);
        track.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        Intent intent;
        switch (view.getId()){

            case R.id.navTrack:
                intent = new Intent(HomeActivity.this, TrackingActivity.class);
                startActivity(intent);
                break;

            case R.id.navProfile:
                intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.plusItem:
                intent = new Intent(HomeActivity.this, AddItemActivity.class);
                startActivity(intent);
                break;

            case R.id.navChat:
                intent = new Intent(HomeActivity.this, RecentChatsActivity.class);
                startActivity(intent);
                break;
        }
    }

    // to handle category buttons
    public void BtnClickHandler(View view) {
        ImageButton button = (ImageButton) view;
        Intent intent;

        String categoryName = button.getTag().toString();
        intent = new Intent(HomeActivity.this, ItemsListActivity.class);
        intent.putExtra("listChoice", 1);
        intent.putExtra("categoryName", categoryName);
        intent.putExtra("searchWord", "");
        startActivity(intent);
    }

    private void SetupSearchView(){
        Log.d("SetupSearchView","SetupSearchView entered");
        search.setActivated(true);
        search.setQueryHint("Type your keyword here");
        search.onActionViewExpanded();
        search.setIconified(false);
        search.clearFocus();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent=new Intent(HomeActivity.this,ItemsListActivity.class);
                intent.putExtra("listChoice",2);
                intent.putExtra("categoryName", "");
                intent.putExtra("searchWord",query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}