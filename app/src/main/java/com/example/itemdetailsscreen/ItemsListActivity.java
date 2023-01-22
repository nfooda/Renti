package com.example.itemdetailsscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ItemsListActivity extends AppCompatActivity {
    ListView listview;
    ArrayList<Item> itemsList = new ArrayList<Item>();
    SearchView search;
    CustomAdapter customAdapter;
    String [] asyncInput = new String[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);
        listview = (ListView) findViewById(R.id.listview);
        Bundle bundle = getIntent().getExtras();
        String category;
        if (bundle != null) {
            category = bundle.getString("categoryName");
        }
        search = (SearchView) findViewById(R.id.searchView1);
        if(bundle.getInt("listChoice")==1){
            asyncInput[0]=String.valueOf(1);
            asyncInput[1]=bundle.getString("categoryName");
        }
        else if (bundle.getInt("listChoice")==2){
            asyncInput[0]=String.valueOf(2);
            asyncInput[1]=bundle.getString("searchWord");
        }
            new FetchItemsList().execute(asyncInput);

        SetupSearchView();

    }


    public class FetchItemsList extends AsyncTask<String, Void, ArrayList<Item>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Item> itemsList) {
            super.onPostExecute(itemsList);

            customAdapter = new CustomAdapter(getApplicationContext(),R.layout.listview_item,itemsList);
            listview.setAdapter(customAdapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(ItemsListActivity.this, ItemDetailsActivity.class);
                    intent.putExtra("item", itemsList.get(i));
                    startActivity(intent);
                }

            });
        }

        @Override
        protected ArrayList<Item> doInBackground(String... strings) {
            String searchURL;
            if(Integer.valueOf(strings[0])==1)
                searchURL = "http://10.0.2.2:3000/getItemsByCategory?category=" + strings[1];
            else
                searchURL = "http://10.0.2.2:3000/getItemsByWord?word=" + strings[1];

            Log.d("FetchItemsList","FetchItemsList entered");
            String bufferdata = "";
            String imagesURL = "http://10.0.2.2:3000/getItemImages?itemId=";
            String ownerURL = "http://10.0.2.2:3000/getItemOwner?ownerEmail=";

            try {
                URL url = new URL(searchURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while (line != null) {
                    line = reader.readLine();
                    bufferdata = bufferdata + line;
                }
                JSONArray jsonArray = new JSONArray(bufferdata);

                for (int i = 0; i < jsonArray.length(); i++) {
                    Item item = new Item();
                    JSONObject itemData = jsonArray.getJSONObject(i);
                    item.itemID =  itemData.getInt("itemID");
                    item.itemName = (String) itemData.get("itemName");
                    item.ownerEmail = (String) itemData.get("ownerEmail");
                    item.locationCity = (String) itemData.get("locationCity");
                    item.description = (String) itemData.get("description");
                    item.rateNum = (float) itemData.getDouble("rateNum");
                    item.rateUnit = (String) itemData.get("rateUnit");
                    if(itemData.get("rating").equals(null))
                        item.rating = 0;
                    else
                        item.rating = (float) itemData.getDouble("rating");

                    if(itemData.get("deposit").equals(null))
                        item.deposit = 0;
                    else
                        item.deposit = (float) itemData.getDouble("deposit");

                    item.category = (String) itemData.get("category");
                    item.numOfRents = (int) itemData.get("numOfRents");
                    item.status = (int) itemData.get("status");
                    itemsList.add(item);
                }
                for (int i = 0; i < itemsList.size(); i++) {
                    bufferdata = "";
                    url = new URL(imagesURL + String.valueOf(itemsList.get(i).itemID));
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    InputStream inputStream2 = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream2));
                    line = "";
                    while (line != null) {
                        line = reader.readLine();
                        bufferdata = bufferdata + line;
                    }
                    jsonArray = new JSONArray(bufferdata);
                    for (int j = 0; j < jsonArray.length(); j++) {
                        itemsList.get(i).imagesURLs.add((String) jsonArray.getJSONObject(j).get("imageURL"));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return itemsList;
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
                customAdapter = new CustomAdapter(getApplicationContext(),R.layout.listview_item,itemsList);
                listview.setAdapter(customAdapter);
                customAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }
}



