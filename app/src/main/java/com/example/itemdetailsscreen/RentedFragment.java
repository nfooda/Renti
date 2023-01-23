package com.example.itemdetailsscreen;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itemdetailsscreen.databinding.FragmentRentedBinding;
import com.example.itemdetailsscreen.models.RentalItem;
import com.example.itemdetailsscreen.utilities.Constants;
import com.example.itemdetailsscreen.utilities.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RentedFragment extends Fragment {

    RecyclerView recyclerview;
    ListView listview;
    @NonNull FragmentRentedBinding binding;
    ArrayList<RentalItem> items = new ArrayList<>();
    FragmentListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
//        binding = FragmentRentedBinding.inflate(getLayoutInflater());
        View contentView = inflater.inflate(R.layout.fragment_rented, container, false);
        listview = contentView.findViewById(R.id.listview_rented);
        PreferenceManager preferenceManager = new PreferenceManager(getActivity());
        new FetchRentalItems().execute(preferenceManager.getString(Constants.KEY_EMAIL));
        return contentView;
    }
public void Rank(){

}
    public class FetchRentalItems extends AsyncTask<String, Void, ArrayList<RentalItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<RentalItem> itemsList) {
            super.onPostExecute(itemsList);
            adapter = new FragmentListAdapter(getActivity(),R.layout.fragment_list_item_done,itemsList);
            listview.setAdapter(adapter);
        }

        @Override
        protected ArrayList<RentalItem> doInBackground(String... strings) {
            String searchURL = "http://10.0.2.2:3000/getRentedItems?userEmail=" + strings[0];

            Log.d("FetchItemsList", "FetchItemsList entered");
            String bufferdata = "";
            String imagesURL = "http://10.0.2.2:3000/getItemImages?itemId=";

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
                    RentalItem item = new RentalItem();
                    JSONObject itemData = jsonArray.getJSONObject(i);
                    item.name = (String) itemData.get("itemName");
                    item.itemID = itemData.getInt("itemID");
                    item.rentalID = itemData.getInt("rentalID");
                    item.time = (String) itemData.get("toTime");
                    item.rented = 0;
                    Date firstDate=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(item.time);
                    Date secondDate = new Date();
                    long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
                    int diff = (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                    item.remainingTime=diff;
                    items.add(item);
                }
                for (int i = 0; i < items.size(); i++) {
                    bufferdata = "";
                    url = new URL(imagesURL + String.valueOf(items.get(i).itemID));
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
                    for (int j = 0; j < jsonArray.length()&& j<1; j++) {
                        items.get(i).imageURL=(String) jsonArray.getJSONObject(j).get("imageURL");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return items;
        }
    }

}