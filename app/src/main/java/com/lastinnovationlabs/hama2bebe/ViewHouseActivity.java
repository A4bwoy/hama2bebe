package com.lastinnovationlabs.hama2bebe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.lastinnovationlabs.hama2bebe.MainOptions.HouseActivity;

import java.util.Objects;

public class ViewHouseActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView price, location, availability, house_details;
    RelativeLayout text_back;
    ImageView pic_one, pic_two, pic_three, picture_selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_house);

        price = findViewById(R.id.price);
        location = findViewById(R.id.location);
        availability = findViewById(R.id.availability);
        house_details = findViewById(R.id.house_details);

        text_back = findViewById(R.id.text_back);

        pic_one = findViewById(R.id.pic_one);
        pic_two = findViewById(R.id.pic_two);
        pic_three = findViewById(R.id.pic_three);

        picture_selected = findViewById(R.id.picture_selected);

        toolbar = findViewById(R.id.toolbar_id);
        toolbar.setTitle("House details");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> {
            Intent i = new Intent(ViewHouseActivity.this, HouseActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });


        Intent intent = getIntent();
        String coverHouse = intent.getStringExtra("houseCover");
        String priceHouse = intent.getStringExtra("housePrice");
        String locationHouse = intent.getStringExtra("houseLocation");
        String availabilityHouse = intent.getStringExtra("houseAvailability");
        String serviceHouse = intent.getStringExtra("houseServiceFee");
        String detailsHouse = intent.getStringExtra("houseHouseDetails");
        String pic1 = intent.getStringExtra("housePictureOne");
        String pic2 = intent.getStringExtra("housePictureTwo");

        Glide
                .with(this)
                .load(coverHouse)
                .placeholder(R.drawable.logo)
                .into(picture_selected);

        Glide
                .with(this)
                .load(coverHouse)
                .placeholder(R.drawable.logo)
                .into(pic_one);

        Glide
                .with(this)
                .load(pic1)
                .placeholder(R.drawable.logo)
                .into(pic_two);

        Glide
                .with(this)
                .load(pic2)
                .placeholder(R.drawable.logo)
                .into(pic_three);

        pic_one.setOnClickListener(v ->
                Glide
                .with(ViewHouseActivity.this)
                .load(coverHouse)
                .placeholder(R.drawable.logo)
                .into(picture_selected));

        pic_two.setOnClickListener(v ->
                Glide
                        .with(ViewHouseActivity.this)
                        .load(pic1)
                        .placeholder(R.drawable.logo)
                        .into(picture_selected));

        pic_three.setOnClickListener(v ->
                Glide
                        .with(ViewHouseActivity.this)
                        .load(pic2)
                        .placeholder(R.drawable.logo)
                        .into(picture_selected));


        price.setText(priceHouse);
        location.setText(locationHouse);
        house_details.setText(detailsHouse);

        String available = "Available";
        String not_available = "Not Available";

        if (availabilityHouse.equals("Available")) {
            availability.setText(available);

            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                text_back.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.color.colorGreen));
            }

        }
        if (availabilityHouse.equals("Not Available")) {
            availability.setText(not_available);

            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                availability.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.color.primaryColorRed));
            }
        }

    }



}