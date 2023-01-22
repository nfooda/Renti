package com.example.itemdetailsscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ItemsListActivity extends AppCompatActivity {

    ListView listView;
    String [] names = {"Honda Civic", "Item Name", "Item Name","Item Name","Item Name","Item Name","Item Name","Item Name","Item Name","Item Name"};
    String [] city={"Cairo, Egypt", "City, Country","City, Country","City, Country","City, Country","City, Country","City, Country","City, Country","City, Country","City, Country"};
    String[] price={"500 LE", "Price","Price","Price","Price","Price","Price","Price","Price","Price"};
    double [] rate={4.8,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0};
    int [] rate_no={10,4,4,4,4,4,4,4,4,4};
    int[] photo={R.drawable.honda_1,
            R.drawable.image_placeholder,
            R.drawable.image_placeholder,
            R.drawable.image_placeholder,
            R.drawable.image_placeholder,
            R.drawable.image_placeholder,
            R.drawable.image_placeholder,
            R.drawable.image_placeholder,
            R.drawable.image_placeholder,
            R.drawable.image_placeholder
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);

        listView = (ListView) findViewById(R.id.listview);

        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), names, city, price, rate, rate_no, photo);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ItemsListActivity.this, ItemDetailsActivity.class);
                startActivity(intent);
            }

        });
    }
}