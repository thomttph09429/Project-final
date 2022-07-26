package com.app.projectfinal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.app.projectfinal.R;
import com.app.projectfinal.model.SliderItem;

import java.util.List;

public class SliderAddsAdapter extends RecyclerView.Adapter<SliderAddsAdapter.SliderViewHolder> {
    private List<SliderItem> sliderItems;
    private ViewPager2 viewPager2;

    public SliderAddsAdapter(List<SliderItem> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_adds, parent, false
                ));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(sliderItems.get(position));

    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_ads);
        }

        void setImage(SliderItem sliderItems) {
        //use glide or picasso in case you get image from internet
            imageView.setImageResource(sliderItems.getImage());
        }
    }
}