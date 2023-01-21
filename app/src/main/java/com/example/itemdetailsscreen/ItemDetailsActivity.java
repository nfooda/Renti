package com.example.itemdetailsscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.itemdetailsscreen.databinding.ActivityItemDetailsBinding;
import com.example.itemdetailsscreen.models.User;
import com.example.itemdetailsscreen.utilities.Constants;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class ItemDetailsActivity extends AppCompatActivity {
    // Urls of our images.
    int honda = R.drawable.honda_1;
    int honda_2 = R.drawable.honda_2;
    ActivityItemDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
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

    private void setListeners() {
        binding.contactOwnerBtn.setOnClickListener(v -> {
            User dummyUser = new User();
            dummyUser.email = "rentiapp@gmail.com";
            dummyUser.name = "Abdallah Taha";
            dummyUser.image = "/9j/4AAQSkZJRgABAQAAAQABAAD/4gIoSUNDX1BST0ZJTEUAAQEAAAIYAAAAAAQwAABtbnRyUkdCIFhZWiAAAAAAAAAAAAAAAABhY3NwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAA9tYAAQAAAADTLQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAlkZXNjAAAA8AAAAHRyWFlaAAABZAAAABRnWFlaAAABeAAAABRiWFlaAAABjAAAABRyVFJDAAABoAAAAChnVFJDAAABoAAAAChiVFJDAAABoAAAACh3dHB0AAAByAAAABRjcHJ0AAAB3AAAADxtbHVjAAAAAAAAAAEAAAAMZW5VUwAAAFgAAAAcAHMAUgBHAEIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAFhZWiAAAAAAAABvogAAOPUAAAOQWFlaIAAAAAAAAGKZAAC3hQAAGNpYWVogAAAAAAAAJKAAAA+EAAC2z3BhcmEAAAAAAAQAAAACZmYAAPKnAAANWQAAE9AAAApbAAAAAAAAAABYWVogAAAAAAAA9tYAAQAAAADTLW1sdWMAAAAAAAAAAQAAAAxlblVTAAAAIAAAABwARwBvAG8AZwBsAGUAIABJAG4AYwAuACAAMgAwADEANv/bAEMAEAsMDgwKEA4NDhIREBMYKBoYFhYYMSMlHSg6Mz08OTM4N0BIXE5ARFdFNzhQbVFXX2JnaGc+TXF5cGR4XGVnY//bAEMBERISGBUYLxoaL2NCOEJjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY2NjY//AABEIAMgAlgMBIgACEQEDEQH/xAAVAAEBAAAAAAAAAAAAAAAAAAAABf/EABQQAQAAAAAAAAAAAAAAAAAAAAD/xAAVAQEBAAAAAAAAAAAAAAAAAAAABf/EABQRAQAAAAAAAAAAAAAAAAAAAAD/2gAMAwEAAhEDEQA/AIwCUiAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP//Z";
            dummyUser.token = "eGaj_-BjQ_yWF27saBccDD:APA91bG4g9agyrAV5DNsP8nbnbVk9vkVkhl_vBJ9067J0lUo_h0FiyogAZRT5bQv9d0V2CdrcLLDYZrBwU-sDB7zIT7b3jI0YbLenpcI1_nhkhNmzrHpOtm6lmFoEQInCcOCM-vRw0XP";
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            intent.putExtra(Constants.KEY_USER, dummyUser);
            startActivity(intent);
        });
    }
}