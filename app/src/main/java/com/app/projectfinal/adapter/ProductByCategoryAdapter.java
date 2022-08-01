package com.app.projectfinal.adapter;

import static com.app.projectfinal.utils.Constant.CATEGORY_NAME;
import static com.app.projectfinal.utils.Constant.DESCRIPTION_PRODUCT;
import static com.app.projectfinal.utils.Constant.IMAGE1_PRODUCT;
import static com.app.projectfinal.utils.Constant.NAME_PRODUCT;
import static com.app.projectfinal.utils.Constant.PRICE_PRODUCT;
import static com.app.projectfinal.utils.Constant.STORE_ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.STORE_NAME_PRODUCT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.projectfinal.R;
import com.app.projectfinal.activity.DetailProductActivity;
import com.app.projectfinal.model.Product;
import com.bumptech.glide.Glide;

import java.util.List;

public class ProductByCategoryAdapter extends RecyclerView.Adapter<ProductByCategoryAdapter.MyViewHolder> {
    private List<Product> products;
    private Product product;
    private Context context;

    public ProductByCategoryAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        product = products.get(position);
        holder.name.setText(product.getProductName());
        holder.price.setText(product.getPrice());
        Glide.with(context).load(product.getImage1()).error(R.drawable.ic_image_error).into(holder.image);



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
            name = itemView.findViewById(R.id.tvNameProduct);
            price = itemView.findViewById(R.id.tvPriceProduct);
            image = itemView.findViewById(R.id.imageProduct);

        }


    }
}
