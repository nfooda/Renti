package com.example.itemdetailsscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        ImageButton cars = (ImageButton) findViewById(R.id.carButton);
        cars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity1.this, MainActivity2.class);
                startActivity (i);

            }
        });
        ImageButton profile = (ImageButton) findViewById(R.id.navProfile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity1.this, MainActivity4.class);
                startActivity (i);

            }
        });
    }
}
