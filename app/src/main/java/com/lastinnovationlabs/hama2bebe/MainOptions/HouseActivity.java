package com.lastinnovationlabs.hama2bebe.MainOptions;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lastinnovationlabs.hama2bebe.Adapter.HouseAdapter;
import com.lastinnovationlabs.hama2bebe.AddHouseActivity;
import com.lastinnovationlabs.hama2bebe.MainActivity;
import com.lastinnovationlabs.hama2bebe.Model.House;
import com.lastinnovationlabs.hama2bebe.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HouseActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    HouseAdapter houseAdapter;
    List<House> houseList;
    CardView loadingBar;
    ImageSlider mainslider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);

        //RecyclerView House
        loadingBar = findViewById(R.id.loadingBar);
        mainslider = findViewById(R.id.image_slider);

        recyclerView = findViewById(R.id.recycler_view_house);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        houseList = new ArrayList<>();
        houseAdapter = new HouseAdapter(HouseActivity.this, houseList);
        recyclerView.setAdapter(houseAdapter);

        toolbar = findViewById(R.id.toolbar_id);
        toolbar.setTitle("Choose a house");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> {
            Intent i = new Intent(HouseActivity.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });

        final List<SlideModel> remoteimages = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Houses")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            House house = snapshot.getValue(House.class);

                            assert house != null;
                            remoteimages.add(new SlideModel(house.getHousePictureOne(),
                                    house.getHouseLocation(), ScaleTypes.FIT));

                            mainslider.setImageList(remoteimages, ScaleTypes.FIT);

                            mainslider.setItemClickListener(new ItemClickListener() {
                                @Override
                                public void onItemSelected(int i) {
                                    Toast.makeText(getApplicationContext(), remoteimages.get(i).getTitle(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        loadHouses();
    }

    private void loadHouses() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Houses");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                houseList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    House house = snapshot.getValue(House.class);

                    houseList.add(house);
                    loadingBar.setVisibility(View.GONE);

                }
                Collections.reverse(houseList);
                houseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_house, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search:
                Toast.makeText(this, "Search...", Toast.LENGTH_SHORT).show();
                //  searchUser();
                return true;
            case R.id.add_house:
                startActivity(new Intent(HouseActivity.this, AddHouseActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}