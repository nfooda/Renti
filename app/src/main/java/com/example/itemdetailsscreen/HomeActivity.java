package com.example.itemdetailsscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import com.example.itemdetailsscreen.utilities.Constants;
import com.example.itemdetailsscreen.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import java.util.prefs.PreferenceChangeEvent;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    PreferenceManager preferenceManager;
    private LinearLayout categories;
    ArrayList<ImageButton> categoriesButtons= new ArrayList<ImageButton>();

    ListView listview;
    ArrayList<Item> itemsList = new ArrayList<Item>();
    SearchView search;
    CustomAdapter customAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        setContentView(R.layout.activity_home);
        categories=findViewById(R.id.categories);
        int count=0;
        for(int i=0;i<categories.getChildCount();i++){
            LinearLayout categories_row = (LinearLayout) categories.getChildAt(i);
            for(int j=0;j<categories_row.getChildCount();j++){
                ImageButton b  = (ImageButton)categories_row.getChildAt(j);
                categoriesButtons.add(b);
                count++;
            }
        }

        search = (SearchView) findViewById(R.id.searchView2);
        SetupSearchView();
    }
    
    
    @Override
    public void onClick(View view) {
        ImageButton button = (ImageButton) view;

        Intent intent;
        switch (view.getId()){
            case R.id.cars:
            case R.id.clothing:
            case R.id.appliances:
            case R.id.books:
            case R.id.realEstate:
            case R.id.bikes:
                String categoryName = button.getTag().toString();
                intent=new Intent(HomeActivity.this,ItemsListActivity.class);
                intent.putExtra("listChoice",1);
                intent.putExtra("categoryName", categoryName);
                intent.putExtra("searchWord","");
                startActivity(intent);
                break;

            case R.id.navTrack:
                break;

            case R.id.navProfile:
                i = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(i);
                break;
                
            case R.id.plusItem:
                i = new Intent(HomeActivity.this, AddItemActivity.class);
                startActivity(i);
                break;
                
            case R.id.nav_chat:
                Intent i = new Intent(HomeActivity.this, RecentChatsActivity.class);
                startActivity(i);
                break;

        }
    }
    
    public void BtnClickHandler(View view) {
        ImageButton button = (ImageButton) view;

        Intent intent;
        switch (view.getId()){
            case R.id.cars:
            case R.id.clothing:
            case R.id.appliances:
            case R.id.books:
            case R.id.realEstate:
            case R.id.bikes:
                String categoryName = button.getTag().toString();
                intent=new Intent(HomeActivity.this,ItemsListActivity.class);
                intent.putExtra("listChoice",1);
                intent.putExtra("categoryName", categoryName);
                intent.putExtra("searchWord","");
                startActivity(intent);
                break;

            case R.id.navTrack:
                break;

            case R.id.navProfile:
                i = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(i);
                break;
                
            case R.id.plusItem:
                i = new Intent(HomeActivity.this, AddItemActivity.class);
                startActivity(i);
                break;
                
            case R.id.nav_chat:
                Intent i = new Intent(HomeActivity.this, RecentChatsActivity.class);
                startActivity(i);
                break;

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
            
        // getToken();

        ImageButton profile = (ImageButton) findViewById(R.id.navProfile);
        profile.setOnClickListener(this);
        ImageButton addItem = (ImageButton) findViewById(R.id.plusItem);
        addItem.setOnClickListener(this);
        ImageButton chats = (ImageButton) findViewById(R.id.nav_chat);
        chats.setOnClickListener(this);

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

}
