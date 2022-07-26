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
import com.app.projectfinal.listener.ListenerCategoryName;
import com.app.projectfinal.model.Category;
import com.app.projectfinal.model.Product;
import com.app.projectfinal.utils.ValidateForm;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private List<Category> categories;
    private Category category;
    private Context context;
    private ListenerCategoryName listenerCategoryName;
    int selectedPosition=-1;

    public CategoryAdapter(List<Category> categories, Context context, ListenerCategoryName listenerCategoryName) {
        this.categories = categories;
        this.context = context;
        this.listenerCategoryName = listenerCategoryName;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,  int position) {
        category = categories.get(position);
        holder.name.setText(ValidateForm.capitalizeFirst(category.getNameCategory()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get adapter position
                int position=holder.getAdapterPosition();
                // call listener
                listenerCategoryName.onItemClick(categories.get(position).getIdCategory(), categories.get(position).getNameCategory());
                // update position
                selectedPosition=position;
                // notify
                notifyDataSetChanged();





            }
        });
        if(selectedPosition==position)
        {
            holder.ivMark.setVisibility(View.VISIBLE);
        }
        else
        {

            holder.ivMark.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView ivMark;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_category_name);
            ivMark = itemView.findViewById(R.id.ivMark);

        }
    }
}
