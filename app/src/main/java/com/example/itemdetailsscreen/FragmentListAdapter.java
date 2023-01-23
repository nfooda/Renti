package com.example.itemdetailsscreen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.itemdetailsscreen.models.Item;
import com.example.itemdetailsscreen.models.RentalItem;
import com.squareup.picasso.Picasso;

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

public class FragmentListAdapter extends ArrayAdapter<Item> implements Filterable {
    private Context context;
    ArrayList<RentalItem> items;


    public FragmentListAdapter(Context context, int resource, ArrayList<RentalItem> items) {
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
            view = inflater.inflate(R.layout.fragment_list_item_done, viewGroup, false);
        }
        ((LinearLayout) view.findViewById(R.id.fragmentListItem))
                .setTag(items.get(i).rentalID);

        viewHolder.itemName = (TextView) view.findViewById(R.id.itemName);
        viewHolder.itemPhoto = (ImageView) view.findViewById(R.id.itemPhoto);
        viewHolder.remainingTime = (TextView) view.findViewById(R.id.remainingTime);
        viewHolder.time = (TextView) view.findViewById(R.id.time);
        viewHolder.rate = (Button) view.findViewById(R.id.rateBtn);

        viewHolder.itemName.setText(items.get(i).name);
        viewHolder.remainingTime.setText("Remaining time: ");
        viewHolder.time.setText(String.valueOf(items.get(i).remainingTime)+" days");

        viewHolder.rate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String result = new String();
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.rate_prompt, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                TextView textview=(TextView) promptsView.findViewById(R.id.alertTextView);
                EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
//                                        result = String.valueOf(userInput.getText());

                                        new ChangeRentalStatus().execute(String.valueOf(items.get(i).rentalID), String.valueOf(items.get(i).rented),userInput.getText().toString()  );
                                        ///////////////////
                                        // cal async function
                                        // start the same activity again at the end of the async function
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();


            }
        });

        // button there or not
        if(items.get(i).rented==0){
            viewHolder.rate.setText("Rate Item");
        } else {
            viewHolder.rate.setText("Rate User");
        }

        if(items.get(i).remainingTime>0){
            viewHolder.rate.setVisibility(View.GONE);
        }

        if(items.get(i).imageURL != "") {
            String imageURL = items.get(i).imageURL;

            Picasso.Builder builder = new Picasso.Builder(context.getApplicationContext());
            builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    exception.printStackTrace();
                }

            });
            builder.build().load("https://docs.google.com/uc?id=1uIzwQir4XKkki_siC_1-9YXgaEtr2OG0")
                    .into(viewHolder.itemPhoto);
        }

        return view;
    }

    public void remove(int position){
        items.remove(items.get(position));
    }

    static class ViewHolder {
        ImageView itemPhoto;
        TextView itemName;
        TextView remainingTime;
        TextView time;
        Button rate;
    }

    //String.valueOf(items.get(i).rentalID), String.valueOf(items.get(i).rented) ,userInput.getText().toString()
    public class ChangeRentalStatus extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Intent intent = new Intent(context, TrackingActivity.class);
            ((Activity)context).finish();
            context.startActivity(intent);
        }

        @Override
        protected Void doInBackground(String... strings) {
            String searchURL = "http://10.0.2.2:3000/changeRentalStatus?rentalId=" + strings[0] +"&rented="+strings[1]+"&rate="+strings[2];
            String bufferdata = "";
            try {
                URL url = new URL(searchURL);
                HttpURLConnection connection2 = (HttpURLConnection) url.openConnection();
                connection2.setRequestMethod("GET");
                connection2.connect();
                connection2.setRequestMethod("GET");
                connection2.connect();
                InputStream inputStream2 = connection2.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream2));
                String line = "";
                while (line != null) {
                    line = reader.readLine();
                    bufferdata = bufferdata + line;
                }
                items.remove(Integer.parseInt(strings[0]));
//                remove(Integer.parseInt(strings[3]));
                notifyDataSetChanged();
//                ((LinearLayout) view.findViewWithTag(Integer.parseInt(strings[0])))
//                        .setVisibility(View.GONE);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }
    }


}


