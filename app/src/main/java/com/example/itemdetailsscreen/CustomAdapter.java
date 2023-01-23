package com.example.itemdetailsscreen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.itemdetailsscreen.models.Item;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Item> implements Filterable {
    private Context context;
    private int resourceLayout;
    private Context mContext;
    ArrayList<Item> items;
    ValueFilter valueFilter;
    ArrayList<Item> itemsList = new ArrayList<Item>();

    private LayoutInflater inflater;

    public CustomAdapter(Context context, int resource, ArrayList<Item> items) {
        super(context, resource);
        this.context = context;
        this.items=items;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_item, viewGroup, false);
        }
        viewHolder.name = (TextView) view.findViewById(R.id.textview_name);
        viewHolder.photo = (ImageView) view.findViewById(R.id.imageview_photo);
        viewHolder.city = (TextView) view.findViewById(R.id.textview_city);
        viewHolder.price = (TextView) view.findViewById(R.id.textview_price);
        viewHolder.rate = (TextView) view.findViewById(R.id.textview_rate);
        viewHolder.rateNo = (TextView) view.findViewById(R.id.textview_rateno);

        viewHolder.name.setText(items.get(i).itemName);
        viewHolder.price.setText(String.valueOf(items.get(i).rateNum));
        viewHolder.city.setText(items.get(i).locationCity);
        viewHolder.rate.setText(String.valueOf(items.get(i).rating));
        viewHolder.rateNo.setText("("+String.valueOf(4)+")");

        if(items.get(i).imagesURLs.size()>0) {
            String imagePath = items.get(i).imagesURLs.get(0);
            setImage(viewHolder.photo, imagePath);

        // items.get(i).bitmap = ((BitmapDrawable)viewHolder.photo.getDrawable()).getBitmap();

//            Picasso.Builder builder = new Picasso.Builder(context.getApplicationContext());
//            builder.listener(new Picasso.Listener() {
//                @Override
//                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
//                    exception.printStackTrace();
//                }
//
//            });
//            builder.build().load(imageURL)   // should put image picture
//                    .into(viewHolder.photo);
        }


        return view;
    }

    private void setImage(ImageView imageView, String imageName) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference dateRef = storageRef.child("images/" + imageName);
        dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                Picasso.Builder builder = new Picasso.Builder(context.getApplicationContext());
                builder.listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        exception.printStackTrace();
                    }

                });
                builder.build().load(downloadUrl)   // should put image picture
                        .into(imageView);
            }
        });
    }

    @Override
    public Filter getFilter() {
        Log.d("getFilter","getFilter entered");
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
//                new SearchItemsList().execute(constraint.toString());
                Log.d("SearchItemsList","SearchItemsList entered");
                String bufferdata = "";
                String searchURL = "http://10.0.2.2:3000/getItemsByWord?word=" + constraint.toString();
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

//                List<String> filterList = new ArrayList<>();
//                for (int i = 0; i < mStringFilterList.size(); i++) {
//                    if ((mStringFilterList.get(i).toUpperCase()).contains(constraint.toString().toUpperCase())) {
//                        filterList.add(mStringFilterList.get(i));
//                    }
//                }
                results.count = itemsList.size();
                results.values = itemsList;
            } else {
                results.count = items.size();
                results.values = items;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            items = (ArrayList<Item>) results.values;
            notifyDataSetChanged();
        }

    }

    public class SearchItemsList extends AsyncTask<String, Void, ArrayList<Item>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Item> itemsList) {
            super.onPostExecute(itemsList);

        }

        @Override
        protected ArrayList<Item> doInBackground(String... strings) {
            Log.d("SearchItemsList","SearchItemsList entered");
            String bufferdata = "";
            String searchURL = "http://10.0.2.2:3000/getItemsByWord?word=" + strings[0];
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


    static class ViewHolder {
        ImageView photo;
        TextView name;
        TextView city;
        TextView price;
        TextView rate;
        TextView rateNo;
    }


}

