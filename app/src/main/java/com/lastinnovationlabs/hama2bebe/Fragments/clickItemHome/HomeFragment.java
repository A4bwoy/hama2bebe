package com.lastinnovationlabs.hama2bebe.Fragments.clickItemHome;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.lastinnovationlabs.hama2bebe.MainOptions.HouseActivity;
import com.lastinnovationlabs.hama2bebe.MainOptions.MapActivity;
import com.lastinnovationlabs.hama2bebe.MapsActivity;
import com.lastinnovationlabs.hama2bebe.MapsAndImplementation.LocationActivity;
import com.lastinnovationlabs.hama2bebe.R;

public class HomeFragment extends Fragment {

    CardView car, house;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        car = view.findViewById(R.id.car);
        house = view.findViewById(R.id.house);

        car.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), MapActivity.class);
            startActivity(i);
        });

        house.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), HouseActivity.class);
            startActivity(intent);
        });

        return view;
    }
}