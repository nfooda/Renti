package com.example.itemdetailsscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.itemdetailsscreen.databinding.ActivityRentRequestBinding;
import com.example.itemdetailsscreen.models.Item;
import com.example.itemdetailsscreen.utilities.Constants;
import com.example.itemdetailsscreen.utilities.PreferenceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RentRequestActivity extends AppCompatActivity implements View.OnClickListener {

    public ActivityRentRequestBinding binding;
    public Calendar fromCalendar, toCalendar;
    public String date_time;
    public Item item;
    public int itemID;
    public String ownerEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRentRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fromCalendar = Calendar.getInstance();
        toCalendar = Calendar.getInstance();
        setListeners();
        Button sendRequest = (Button) findViewById(R.id.send_request);
        sendRequest.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        PreferenceManager preferenceManager = new PreferenceManager(this);
        String renterEmail = preferenceManager.getString(Constants.KEY_EMAIL);

        item = (Item) getIntent().getSerializableExtra("item");
        if (item != null) {
            ownerEmail = item.ownerEmail;
            itemID=item.itemID;
        }

        Date date = fromCalendar.getTime();
        String from = new SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date).toString();
        Date date2 = toCalendar.getTime();
        String to = new SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date2).toString();

        new sendRequestTask().execute(Integer.toString(itemID),ownerEmail,renterEmail,from,to);

    }
    private class sendRequestTask extends AsyncTask<String, Void, String> {
        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // Runs in a background thread
        @Override
        protected String doInBackground(String... params) {
            String response= "";
            String response2= "";
            try {
                URL url1 = new URL("http://10.0.22.2:3000/rentRequest?itemID=" + params[0]
                        + "&ownerEmail=" + params[1]
                        + "&renterEmail=" + params[2]
                        + "&from=" + params[3]
                        + "&to=" + params[4]);
                for (int i=0;i<5;i++) {
                    System.out.println("param is: " +  params[i]);
                }
                HttpURLConnection con = (HttpURLConnection) url1.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    // StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        // response.append(inputLine);
                        response = response + inputLine;
                    }
                    in.close();
                    // print result
                    System.out.println("response is: " + response);
                    System.out.println("GET request WORKED.");
                } else {
                    System.out.println("GET request did not work.");
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //http://10.0.2.2:3000/updateItemStatus?itemID=51
            updateItemStatus(Integer.toString(itemID));

            return response2;

        }
        // This is called from background thread but runs in UI
        @Override
        protected void onProgressUpdate(Void... voids) {
            super.onProgressUpdate();
        }
        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(), "Rent Request Done!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(RentRequestActivity.this, TrackingActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setListeners() {
        binding.fromDatePicker.setOnClickListener(v -> {
            datePicker(fromCalendar);
            Date date = fromCalendar.getTime();
            binding.fromDatePicker.setText(
                    new SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date)
            );
        });
        binding.toDatePicker.setOnClickListener(v -> {
            datePicker(toCalendar);
            Date date = toCalendar.getTime();
            binding.toDatePicker.setText(
                    new SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date)
            );
        });
    }

    private void datePicker(Calendar calendarToSet) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendarToSet.set(Calendar.YEAR, year);
                        calendarToSet.set(Calendar.MONTH, month+1);
                        calendarToSet.set(Calendar.DAY_OF_MONTH, day);
                        date_time = day + "-" + (month + 1) + "-" + year;
                        timePicker(calendarToSet);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void timePicker(Calendar calendarToSet) {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                calendarToSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendarToSet.set(Calendar.MINUTE, minute);
            }
        }, mHour, mMinute, false);
        timePickerDialog.show();
    }
    public Void updateItemStatus(String id){
        String response2= "";
        try {
            URL url2 = new URL("http://10.0.2.2:3000/updateItemStatus?itemID="+id);
            HttpURLConnection con2 = (HttpURLConnection) url2.openConnection();
            con2.setRequestMethod("GET");
            int responseCode = con2.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(con2.getInputStream()));
                String inputLine;
                // StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    // response.append(inputLine);
                    response2 = response2 + inputLine;
                }
                in.close();
                // print result
                System.out.println("GET request2 WORKED.");
            } else {
                System.out.println("GET request2 did not work.");
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
