package com.lastinnovationlabs.hama2bebe.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lastinnovationlabs.hama2bebe.Model.House;
import com.lastinnovationlabs.hama2bebe.R;
import com.lastinnovationlabs.hama2bebe.ViewHouseActivity;

import java.util.List;

public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.ImageViewHolder> {

    private Context mContext;
    private List<House> mHouse;

    public HouseAdapter(Context context, List<House> houses) {
        mContext = context;
        mHouse = houses;
    }

    @NonNull
    @Override
    public HouseAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.house_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HouseAdapter.ImageViewHolder holder, int position) {
        final House house = mHouse.get(position);


        Glide
                .with(mContext)
                .load(house.getHousePictureOne())
                .placeholder(R.drawable.logo)
                .into(holder.post_image);

        holder.house_item.setOnClickListener(view -> {
            Intent i = new Intent(mContext, ViewHouseActivity.class);
            i.putExtra("id", house.getHouseId());
            i.putExtra("housePrice", house.getHousePrice());
            i.putExtra("houseLocation", house.getHouseLocation());
            i.putExtra("houseServiceFee", house.getHouseServiceFee());
            i.putExtra("houseHouseDetails", house.getHouseHouseDetails());
            i.putExtra("houseAvailability", house.getHouseAvailability());
            i.putExtra("housePictureOne", house.getHousePictureOne());
            i.putExtra("housePictureTwo", house.getHousePictureTwo());
            i.putExtra("houseCover", house.getHouseCover());
            mContext.startActivity(i);
        });

        holder.price.setText(house.getHousePrice());

        holder.location_text.setText(house.getHouseLocation());

    }

    @Override
    public int getItemCount() {
        return mHouse.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView post_image;
        public CardView house_item;
        public TextView price, location_text;

        public ImageViewHolder(View itemView) {
            super(itemView);

            post_image = itemView.findViewById(R.id.post_image);
            house_item = itemView.findViewById(R.id.house_item);
            price = itemView.findViewById(R.id.price);
            location_text = itemView.findViewById(R.id.location_text);

        }
    }
}
