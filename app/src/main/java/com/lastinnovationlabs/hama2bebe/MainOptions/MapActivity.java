package com.lastinnovationlabs.hama2bebe.MainOptions;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.lastinnovationlabs.hama2bebe.MainActivity;
import com.lastinnovationlabs.hama2bebe.R;

import java.util.Objects;

import static android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

public class MapActivity extends AppCompatActivity {

    Toolbar toolbar;

    private BottomSheetDialog bsDialogLocations;
    private BottomSheetDialog bsDialogTransport;

    Button btnGetDirection;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        btnGetDirection = findViewById(R.id.btnGetDirection);

        toolbar = findViewById(R.id.toolbar_id);
        toolbar.setTitle("Transport Services");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> {
            Intent i = new Intent(MapActivity.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });

        btnGetDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTransport();
            }
        });

    }

    private void showTransport() {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.bottom_sheet_transport, null);

        LinearLayout kirikuu, fuso, canter;

        kirikuu = view.findViewById(R.id.kirikuu);
        fuso = view.findViewById(R.id.fuso);
        canter = view.findViewById(R.id.canter);
        Drawable highlight = getResources().getDrawable(R.drawable.back_selected);
        Button submit_btn = view.findViewById(R.id.submit_btn);


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (canter.getBackground() == null) {
                    Toast.makeText(MapActivity.this, "Chagua usafiri", Toast.LENGTH_SHORT).show();

                }
                if (kirikuu.getBackground() == null) {
                    Toast.makeText(MapActivity.this, "Chagua usafiri", Toast.LENGTH_SHORT).show();

                }
                if (kirikuu.getBackground() == null) {
                    Toast.makeText(MapActivity.this, "Chagua usafiri", Toast.LENGTH_SHORT).show();

                }

                if (canter.getBackground() == highlight) {
                    Toast.makeText(MapActivity.this, "Selected canter", Toast.LENGTH_SHORT).show();

                }
                if (fuso.getBackground() == highlight) {
                    Toast.makeText(MapActivity.this, "Selected Fuso", Toast.LENGTH_SHORT).show();

                }
                if (kirikuu.getBackground() == highlight) {
                    Toast.makeText(MapActivity.this, "Selected KiriKuu", Toast.LENGTH_SHORT).show();

                }

            }
        });


        kirikuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirikuu.setBackground(highlight);

                if (canter.getBackground() == highlight) {
                    canter.setBackground(null);

                }
                if (fuso.getBackground() == highlight) {
                    fuso.setBackground(null);

                }
            }
        });

        fuso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fuso.setBackground(highlight);

                if (kirikuu.getBackground() == highlight) {
                    kirikuu.setBackground(null);

                }
                if (canter.getBackground() == highlight) {
                    canter.setBackground(null);

                }

            }
        });

        canter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canter.setBackground(highlight);

                if (kirikuu.getBackground() == highlight) {
                    kirikuu.setBackground(null);

                }

                if (fuso.getBackground() == highlight) {
                    fuso.setBackground(null);

                }

            }
        });


        bsDialogTransport = new BottomSheetDialog(this);
        bsDialogTransport.setContentView(view);
        bsDialogTransport.setCanceledOnTouchOutside(false);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Objects.requireNonNull(bsDialogTransport.getWindow()).addFlags(FLAG_TRANSLUCENT_STATUS);

        }

        bsDialogTransport.setOnDismissListener(dialog -> bsDialogTransport = null);

        bsDialogTransport.show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        showLocationPicker();
    }

    private void showLocationPicker() {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.bottom_sheet_location, null);

        view.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bsDialogLocations.dismiss();
                MapActivity.this.finish();

            }
        });


        final EditText destination = view.findViewById(R.id.dropoffLocation);
        final EditText myLocation = view.findViewById(R.id.mymLocation);

        view.findViewById(R.id.continue_btn).setOnClickListener(view12 -> {

            String amHere = myLocation.getText().toString();
            String goingTo = destination.getText().toString();

            if (amHere.isEmpty() || goingTo.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
            } else {
                //updateName(edUserName.getText().toString());
                bsDialogLocations.dismiss();
            }
        });

        bsDialogLocations = new BottomSheetDialog(this);
        bsDialogLocations.setContentView(view);
        bsDialogLocations.setCanceledOnTouchOutside(false);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Objects.requireNonNull(bsDialogLocations.getWindow()).addFlags(FLAG_TRANSLUCENT_STATUS);

        }

        bsDialogLocations.setOnDismissListener(dialog -> bsDialogLocations = null);

        bsDialogLocations.show();
    }

}