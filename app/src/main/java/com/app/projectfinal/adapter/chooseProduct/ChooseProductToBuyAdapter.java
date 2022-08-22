package com.app.projectfinal.adapter.chooseProduct;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.projectfinal.R;
import com.app.projectfinal.db.Cart;
import com.app.projectfinal.model.ChooseProduct;
import com.app.projectfinal.utils.ValidateForm;
import com.bumptech.glide.Glide;

import java.util.List;

public class ChooseProductToBuyAdapter extends RecyclerView.Adapter<ChooseProductToBuyAdapter.MyViewHolder> {
    private List<Cart> chooseProducts;
    private Context context;


    public ChooseProductToBuyAdapter(List<Cart> chooseProducts, Context context) {
        this.chooseProducts = chooseProducts;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pay, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Cart cart = chooseProducts.get(position);
        tvNameProduct.setText(cart.getNameProduct());
        tvPrice.setText(cart.getPrice());
        tvAmount.setText("x"+cart.getAmount());
        Glide.with(context).load(cart.getImageOfProduct()).centerCrop().into(ivProduct);


    }

    @Override
    public int getItemCount() {
        return chooseProducts.size();
    }

    private TextView  tvNameProduct, tvPrice, tvAmount;
    private ImageView ivProduct;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            ivProduct = itemView.findViewById(R.id.ivProduct);
        }


    }
}
