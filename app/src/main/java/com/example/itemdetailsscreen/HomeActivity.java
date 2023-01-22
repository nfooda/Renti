package com.example.itemdetailsscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private LinearLayout categories;
    ArrayList<ImageButton> categoriesButtons= new ArrayList<ImageButton>();

    ListView listview;
    ArrayList<Item> itemsList = new ArrayList<Item>();
    SearchView search;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

//        listview = (ListView) findViewById(R.id.listview);

        search = (SearchView) findViewById(R.id.searchView2);
        SetupSearchView();
    }

    public void BtnClickHandler(View view) {
        ImageButton button = (ImageButton) view;

        Intent intent;
        switch (view.getId()){
            case R.id.cars:
//                Log.d("here2","cars clicked");
//                String categoryName = button.getTag().toString();
//                intent=new Intent(HomeActivity.this,ItemsListActivity.class);
//                intent.putExtra("categoryName", categoryName);
//                startActivity(intent);
//                break;
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

            case R.id.navChat:
                break;

            case R.id.navTrack:
                break;

            case R.id.navProfile:
                intent=new Intent(HomeActivity.this,ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.addItem:
                break;

        }
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
//                customAdapter = new CustomAdapter(getApplicationContext(),R.layout.listview_item,itemsList);
//                listview.setAdapter(customAdapter);
//                customAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }
}
