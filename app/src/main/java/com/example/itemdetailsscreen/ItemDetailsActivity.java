package com.example.itemdetailsscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class ItemDetailsActivity extends AppCompatActivity {
    // Urls of our images.
    int honda = R.drawable.honda_1;
    int honda_2 = R.drawable.honda_2;
    Item item;
    TextView itemTitle;
    TextView location;
    TextView price;
    TextView priceRate;
    TextView deposit;
    TextView description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        Bundle bundle = getIntent().getExtras();

        item = (Item) bundle.getSerializable("item");
        itemTitle = (TextView) findViewById(R.id.itemTitle);
        location = (TextView) findViewById(R.id.location);
        price = (TextView) findViewById(R.id.price);
        priceRate = (TextView) findViewById(R.id.priceRate);
        deposit = (TextView) findViewById(R.id.deposit);
        description = (TextView) findViewById(R.id.description);

        itemTitle.setText(item.itemName);
        location.setText(item.locationCity+" , Egypt");
        price.setText(String.valueOf(item.rateNum));
        priceRate.setText(" / "+item.rateUnit);
        deposit.setText(String.valueOf(item.deposit));
        description.setText(item.description);

        // we are creating array list for storing our image urls.
        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();

        // initializing the slider view.
        SliderView sliderView = findViewById(R.id.slider);

        // adding the urls inside array list
        sliderDataArrayList.add(new SliderData(honda));
        sliderDataArrayList.add(new SliderData(honda_2));
        sliderDataArrayList.add(new SliderData(honda));

        // passing this array list inside our adapter class.
        SliderAdapter adapter = new SliderAdapter(this, sliderDataArrayList);

        // below method is used to setadapter to sliderview.
        sliderView.setSliderAdapter(adapter);

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
}