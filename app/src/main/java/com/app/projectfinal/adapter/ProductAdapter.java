package com.app.projectfinal.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.app.projectfinal.R;
import com.app.projectfinal.activity.ProductDetailActivity;
import com.app.projectfinal.model.Product;
import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    private List<Product> products;
    private Product product;

    public ProductAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    private Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        product = products.get(position);
        holder.name.setText(product.getName());
        holder.price.setText(product.getPrice());
        Glide.with(context).load(product.getImage()).error(R.drawable.ic_image_error).into(holder.image);
        holder.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context, ProductDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("product", position);
            intent.putExtras(bundle);
           context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, price;
        private ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.tvNameProduct);
            price=itemView.findViewById(R.id.tvPriceProduct);
            image = itemView.findViewById(R.id.imageProduct);

        }
    }
}
