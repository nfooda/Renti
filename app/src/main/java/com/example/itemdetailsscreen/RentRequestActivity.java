package com.example.itemdetailsscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.itemdetailsscreen.databinding.ActivityRentRequestBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RentRequestActivity extends AppCompatActivity {

    public ActivityRentRequestBinding binding;
    public Calendar fromCalendar, toCalendar;
    public String date_time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRentRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fromCalendar = Calendar.getInstance();
        toCalendar = Calendar.getInstance();
        setListeners();
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
}