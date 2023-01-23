package com.example.itemdetailsscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.itemdetailsscreen.databinding.ActivityItemDetailsBinding;
import com.example.itemdetailsscreen.models.Item;
import com.example.itemdetailsscreen.models.User;
import com.example.itemdetailsscreen.utilities.Constants;
import com.example.itemdetailsscreen.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ItemDetailsActivity extends AppCompatActivity {
    // Urls of our images.
    private PreferenceManager preferenceManager;
    int honda = R.drawable.honda_1;
    int honda_2 = R.drawable.honda_2;
    Item item;
    TextView itemTitle;
    TextView location;
    TextView price;
    TextView priceRate;
    TextView deposit;
    TextView description;
    TextView ownerName;
    TextView ownerPhone;
    LinearLayout ownerCard;
    ImageView ownerImage;

    User owner=new User();
    ActivityItemDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Bundle bundle = getIntent().getExtras();
        preferenceManager = new PreferenceManager(getApplicationContext());

        item = (Item) bundle.getSerializable("item");
        itemTitle = (TextView) findViewById(R.id.itemTitle);
        location = (TextView) findViewById(R.id.location);
        price = (TextView) findViewById(R.id.price);
        priceRate = (TextView) findViewById(R.id.priceRate);
        deposit = (TextView) findViewById(R.id.deposit);
        description = (TextView) findViewById(R.id.description);

        //owner
        ownerName = (TextView) findViewById(R.id.ownerName);
        ownerPhone = (TextView) findViewById(R.id.ownerPhone);
        ownerCard=(LinearLayout) findViewById(R.id.owner);
        ownerImage=(ImageView) findViewById(R.id.userImage);



        itemTitle.setText(item.itemName);
        location.setText(item.locationCity+" , Egypt");
        price.setText(String.valueOf(item.rateNum));
        priceRate.setText(" / "+item.rateUnit);
        deposit.setText(String.valueOf(item.deposit));
        description.setText(item.description);

        new FetchOwnerData().execute(item.ownerEmail);


        setListeners();

//        // we are creating array list for storing our image urls.
//        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();
//
//        // initializing the slider view.
//        SliderView sliderView = findViewById(R.id.slider);
//
//        // adding the urls inside array list
//        sliderDataArrayList.add(new SliderData(honda));
//        sliderDataArrayList.add(new SliderData(honda_2));
//        sliderDataArrayList.add(new SliderData(honda));
//
//        // passing this array list inside our adapter class.
//        SliderAdapter adapter = new SliderAdapter(this, sliderDataArrayList);
//
//        // below method is used to setadapter to sliderview.
//        sliderView.setSliderAdapter(adapter);

        LinearLayout reviewsLayout = (LinearLayout) findViewById(R.id.reviews);
        for (int i = 0; i < 3; i++) {
            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout review = (LinearLayout) inflater.inflate(R.layout.review_card, null, false);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp2px(150), dp2px(110));
            int left = 10;
            if (i == 0)
                left = 0;
            params.setMargins(left, 10, 10, 10);
            review.setLayoutParams(params);
            TextView user = (TextView) review.getChildAt(0);
            user.setText("User " + i);
            TextView reviewBody = (TextView) review.getChildAt(1);
            reviewBody.setText("Review " + i);
            reviewsLayout.addView(review);
        }
    }

    int dp2px(float dp) {
        Resources r = getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
        return (int) px;
    }

    private void setListeners() {
        binding.contactOwnerBtn.setOnClickListener(v -> {
            // dummy user for testing
            // should send user data and item data as well (or their ids)
            User dummyUser = new User();
            dummyUser.email = "rentiapp@gmail.com";
            dummyUser.name = "Abdallah Taha";
            dummyUser.image = "/9j/4AAQSkZJRgABAQAAAQABAAD/4gIoSUNDX1BST0ZJTEUAAQEAAAIYAAAAAAQwAABtbnRyUkdCIFhZWiAAAAAAAAAAAAAAAABhY3NwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAA9tYAAQAAAADTLQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAlkZXNjAAAA8AAAAHRyWFlaAAABZAAAABRnWFlaAAABeAAAABRiWFlaAAABjAAAABRyVFJDAAABoAAAAChnVFJDAAABoAAAAChiVFJDAAABoAAAACh3dHB0AAAByAAAABRjcHJ0AAAB3AAAADxtbHVjAAAAAAAAAAEAAAAMZW5VUwAAAFgAAAAcAHMAUgBHAEIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAFhZWiAAAAAAAABvogAAOPUAAAOQWFlaIAAAAAAAAGKZAAC3hQAAGNpYWVogAAAAAAAAJKAAAA+EAAC2z3BhcmEAAAAAAAQAAAACZmYAAPKnAAANWQAAE9AAAApbAAAAAAAAAABYWVogAAAAAAAA9tYAAQAAAADTLW1sdWMAAAAAAAAAAQAAAAxlblVTAAAAIAAAABwARwBvAG8AZwBsAGUAIABJAG4AYwAuACAAMgAwADEANv/bAEMAEAsMDgwKEA4NDhIREBMYKBoYFhYYMSMlHSg6Mz08OTM4N0BIXE5ARFdFNzhQbVFXX2JnaGc+TXF5cGR4XGVnY//bAEMBERISGBUYLxoaL2NCOEJjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY//AABEIAMgAlgMBIgACEQEDEQH/xAAVAAEBAAAAAAAAAAAAAAAAAAAABf/EABQQAQAAAAAAAAAAAAAAAAAAAAD/xAAVAQEBAAAAAAAAAAAAAAAAAAAABf/EABQRAQAAAAAAAAAAAAAAAAAAAAD/2gAMAwEAAhEDEQA/AIwCUiAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP//Z";
            dummyUser.token = "eGaj_-BjQ_yWF27saBccDD:APA91bG4g9agyrAV5DNsP8nbnbVk9vkVkhl_vBJ9067J0lUo_h0FiyogAZRT5bQv9d0V2CdrcLLDYZrBwU-sDB7zIT7b3jI0YbLenpcI1_nhkhNmzrHpOtm6lmFoEQInCcOCM-vRw0XP";
            dummyUser.id = "FYhxxACCh7cF60ZgsH11Y9CIwqD3";
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            intent.putExtra(Constants.KEY_USER, dummyUser);
            intent.putExtra("item", item);
            startActivity(intent);
        });

        binding.rentRequestBtn.setOnClickListener(
                v -> {
                    Intent intent = new Intent(getApplicationContext(), RentRequestActivity.class);
                    intent.putExtra("item", item);
                    startActivity(intent);
                }
        );
    }

    public class FetchOwnerData extends AsyncTask<String, Void, User> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(User owner) {
            super.onPostExecute(owner);

            ownerName.setText(owner.name);
            ownerPhone.setText(String.valueOf(owner.phone));

            // set owner image
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.collection(Constants.KEY_COLLECTION_USERS)
                    .whereEqualTo(Constants.KEY_EMAIL, owner.email)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null
                                && task.getResult().getDocuments().size() > 0) {
                            byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            ownerImage.setImageBitmap(bitmap);
                        }
                    });
        }

        @Override
        protected User doInBackground(String... strings) {

            String bufferdata = "";
            String ownerURL = "http://10.0.2.2:3000/getItemOwner?ownerEmail="+strings[0];

            try {
                URL url = new URL(ownerURL);
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
                JSONObject ownerData = jsonArray.getJSONObject(0);
                owner.email =  (String) ownerData.get("email");
                owner.phone = ownerData.getInt("phone");
                owner.name = (String) ownerData.get("name");
                if(ownerData.get("rating").equals(null))
                    owner.rating = 0;
                else
                    owner.rating = (float) ownerData.getDouble("rating");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return owner;
        }
    }

}