package com.example.itemdetailsscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.itemdetailsscreen.utilities.Constants;
import com.example.itemdetailsscreen.utilities.PreferenceManager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;


public class AddItemActivity extends AppCompatActivity implements View.OnClickListener{

    private PreferenceManager preferenceManager;
    private static final int PICK_IMAGE_MULTIPLE = 1;
    StorageReference riversRef;
    FirebaseStorage storage;
    ArrayList<Uri> mArrayUri, fireBaseURI;
    ArrayList<String> mArrayNames;
    ImageButton AddPhoto;
    String title,location,des,itemPrice,unit,category,insurance;
    EditText itemTitle,description,price, insuranceID;
    Spinner spinnerPeriods,citySpinner,CatSpinner;
    UploadTask uploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        setContentView(R.layout.activity_add_item);

        storage= FirebaseStorage.getInstance();

        mArrayUri =new ArrayList<Uri>();
        fireBaseURI=new ArrayList<Uri>();
        mArrayNames=new ArrayList<String>();

        itemTitle = (EditText) findViewById(R.id.item_title);
        description = (EditText) findViewById(R.id.descriptionID);
        price = (EditText) findViewById(R.id.priceID);
        insuranceID = (EditText) findViewById(R.id.insuranceID);

        Button confirm = (Button) findViewById(R.id.confirmBtn);
        confirm.setOnClickListener(this);

        spinnerPeriods = findViewById(R.id.periodSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.periods, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerPeriods.setAdapter(adapter);

        citySpinner = findViewById(R.id.citiesSpinner);
        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(this, R.array.cities, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        citySpinner.setAdapter(cityAdapter);

        CatSpinner = findViewById(R.id.categorySpinner);
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
                /*Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);*/
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);

                break;
            case R.id.confirmBtn:
                title = itemTitle.getText().toString();
                des = description.getText().toString();
                itemPrice=price.getText().toString();
                unit=spinnerPeriods.getSelectedItem().toString();
                location = citySpinner.getSelectedItem().toString();
                category=CatSpinner.getSelectedItem().toString();
                insurance=insuranceID.getText().toString();
                new insertDetails().execute(title,preferenceManager.getString(Constants.KEY_EMAIL),location,des,itemPrice, unit, category,insurance);
                break;
        }
    }
    //itemName, ownerEmail, locationCity, description, rateNum, rateUnit, category, deposit
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_MULTIPLE && data != null) {

            // AddPhoto.setImageURI(selctedImage);
            //Get the selected images
            if (data.getClipData() != null) {
                ClipData ClipData = data.getClipData();
                int x = ClipData.getItemCount();
                for (int i = 0; i < x; i++) {
                    System.out.println("I value isss:" + Integer.toString(i));
                    // adding imageuri in array
                    Uri imageUri = ClipData.getItemAt(i).getUri();
                    String fileName= imageUri.getLastPathSegment();
                    mArrayNames.add(fileName);
                    System.out.println("IMAGE NAME IS: "+fileName);
                    uploadLocalFiles(imageUri);
                }
                String numOfImages=Integer.toString(x);
                Toast.makeText(this, numOfImages + " images uploaded.", Toast.LENGTH_LONG).show();
            }else{
                Uri imageUrl = data.getData();
                String fileName= imageUrl.getLastPathSegment();
                mArrayNames.add(fileName);
                Toast.makeText(this, "1 image uploaded.", Toast.LENGTH_LONG).show();
                uploadLocalFiles(imageUrl);
            }
        } else {
            // show this if no image is selected
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    public void uploadLocalFiles(Uri x) {
        riversRef = storage.getReference().child("images/" + x.getLastPathSegment());
        uploadTask = riversRef.putFile(x);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(AddItemActivity.this, "Failed to upload image!", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddItemActivity.this, "Photo Uploaded to Firebase!", Toast.LENGTH_LONG).show();
            }
        });
    }
    //http://10.0.2.2:3000/addItem?name=Car&ownerEmail=user@gmail.com&location=Alex&description=Very_Good_Car&price=100&unit=day&category=Cars&insurance=1000
    private class insertDetails extends AsyncTask<String, Void, String> {
        String itemID;
        JSONArray jsonArray = null;
        JSONObject imageID;

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
                URL url1 = new URL("http://10.0.2.2:3000/addItem?name=" + params[0] + "&ownerEmail=" + params[1] + "&location=" + params[2] + "&description=" + params[3] + "&price=" + params[4] + "&unit=" + params[5] + "&category=" + params[6] + "&insurance=" + params[7]);
                for (int i=0;i<8;i++) {
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

                    jsonArray = new JSONArray(response);
                    for(int i=0; i<jsonArray.length();i++) {
                        imageID = jsonArray.getJSONObject(i);
                        itemID =  imageID.get("imageID").toString();
                    }
                    Log.v("test0","imageID is: " +  itemID);


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
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for(int i=0;i<mArrayNames.size();i++){
                Log.v("Item", "itemID is: " + itemID);
                System.out.println("Image name NOWW is: "+mArrayNames.get(i));

                API_uploadImage(itemID, mArrayNames.get(i));
            }

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
            Context ctx = AddItemActivity.this;
            Toast.makeText(ctx, "Item posted!", Toast.LENGTH_LONG).show();
            Intent i = new Intent(AddItemActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
        }
    }

    public String API_uploadImage(String id, String url){
        String response2= "";
        try {
            URL url2 = new URL("http://10.0.2.2:3000/uploadImage?itemID="+id + "&photoURL=" + url);
            System.out.println("Image_param is: " +  url);
            System.out.println("ItemID is: " +  id);
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
        return response2;
    }
}
