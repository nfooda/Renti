package com.example.itemdetailsscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class AddItem extends AppCompatActivity implements View.OnClickListener {
    private static final int RESULT_LOAD_IMAGE = 1;
    ImageButton AddPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        EditText itemTitle = (EditText) findViewById(R.id.item_title);
        itemTitle.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent e) {
                if ((e.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String title = itemTitle.getText().toString();
                          //  Context ctx = AddItem.this;
                           // Toast.makeText(ctx, "Yes, Menna!", Toast.LENGTH_LONG).show();
                           // Log.v("test","title is: "+title);
                    return true;
                }
                return false;
            }

        });

        EditText description = (EditText) findViewById(R.id.descriptionID);
        description.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent e) {
                if ((e.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String des = description.getText().toString();
                    return true;
                }
                return false;
            }

        });

        EditText price = (EditText) findViewById(R.id.priceID);
        EditText insuranceID = (EditText) findViewById(R.id.insuranceID);
        Button confirm = (Button) findViewById(R.id.confirmBtn);

        Spinner spinnerPeriods = findViewById(R.id.periodSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.periods, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerPeriods.setAdapter(adapter);

        Spinner citySpinner = findViewById(R.id.citiesSpinner);
        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(this, R.array.cities, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        citySpinner.setAdapter(cityAdapter);

        Spinner CatSpinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> CatAdapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        CatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        CatSpinner.setAdapter(CatAdapter);

        AddPhoto = (ImageButton) findViewById(R.id.AddPhoto);
        AddPhoto.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.AddPhoto:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);

                break;
            /*case R.id.confirmBtn:

                break;*/
    }
}

@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent Data){
        super.onActivityResult(requestCode, resultCode,Data);
        if(requestCode == RESULT_LOAD_IMAGE  && resultCode == RESULT_OK && Data!=null){
            Uri selctedImage = Data.getData();
            AddPhoto.setImageURI(selctedImage);
            Context ctx = AddItem.this;
            Toast.makeText(ctx, "A photo is selected", Toast.LENGTH_LONG).show();

        }
    }
}

/*    public boolean onKey(View v, int keyCode, KeyEvent e) {
        if ((e.getAction() == KeyEvent.ACTION_DOWN) &&
                (keyCode == KeyEvent.KEYCODE_ENTER)) {
                   Context ctx = AddItem.this;
                    Toast.makeText(ctx, "Yes, Menna!", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }*/

/* APIs needed
* uploadImage()
* downloadImage()
*
* */