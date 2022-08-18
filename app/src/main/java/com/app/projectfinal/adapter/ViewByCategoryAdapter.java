package com.app.projectfinal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.projectfinal.R;
import com.app.projectfinal.listener.ListenerCategoryName;
import com.app.projectfinal.listener.ListenerViewProductByCategory;
import com.app.projectfinal.model.Category;
import com.app.projectfinal.utils.ValidateForm;
import com.bumptech.glide.Glide;

import java.util.List;

public class ViewByCategoryAdapter extends RecyclerView.Adapter<ViewByCategoryAdapter.MyViewHolder> {
    private List<Category> categories;
    private Context context;
    private ListenerViewProductByCategory listener;
    int selectedPosition = -1;

    public ViewByCategoryAdapter(List<Category> categories, Context context, ListenerViewProductByCategory listener) {
        this.categories = categories;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view_by_category, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvCategoryName.setText(ValidateForm.capitalizeFirst(categories.get(position).getNameCategory()));
        Glide.with(context).load(categories.get(position).getImageCategory()).centerCrop().error(R.drawable.avatar_empty).into(holder.ivImage);


        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();

            listener.clickTypeCategory(categories.get(position).getIdCategory());
            selectedPosition = pos;
            notifyDataSetChanged();

        });
        if (selectedPosition == position) {
            holder.lnItemView.setBackgroundColor(Color.parseColor("#E5EADC"));
        } else {

            holder.lnItemView.setBackgroundColor(Color.parseColor("#FFFFFF"));

        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCategoryName;
        private ImageView ivImage;
        private LinearLayout lnItemView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            ivImage = itemView.findViewById(R.id.ivImage);
            lnItemView = itemView.findViewById(R.id.lnItemView);


        }
    }
}
