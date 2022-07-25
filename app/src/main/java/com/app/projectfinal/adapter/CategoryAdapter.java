package com.app.projectfinal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.app.projectfinal.model.Category;
import com.app.projectfinal.model.Product;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private List<Category> categories;
    private Category category;

    public CategoryAdapter(List<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;

    }

    private Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,  int position) {
        category = categories.get(position);
        holder.name.setText(category.getNameCategory());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.iv_mark.getVisibility() == View.VISIBLE) {
                    holder.iv_mark.setVisibility(View.GONE);
                } else  {
                    holder.iv_mark.setVisibility(View.VISIBLE);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView iv_mark;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_category_name);
            iv_mark = itemView.findViewById(R.id.iv_mark);

        }
    }
}
