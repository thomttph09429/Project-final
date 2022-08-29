package com.app.projectfinal.adapter.order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.projectfinal.R;
import com.app.projectfinal.db.Cart;
import com.app.projectfinal.model.order.ItemOrder;
import com.app.projectfinal.utils.ValidateForm;
import com.bumptech.glide.Glide;

import java.util.List;

public class ItemOrderAdapter extends RecyclerView.Adapter<ItemOrderAdapter.MyViewHolder> {
    private List<ItemOrder> itemOrders;
    private Context context;


    public ItemOrderAdapter(List<ItemOrder> itemOrders, Context context) {
        this.itemOrders = itemOrders;
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
        ItemOrder itemOrder  = itemOrders.get(position);
        tvNameProduct.setText(itemOrder.getName());
        tvPrice.setText(ValidateForm.getDecimalFormattedString(itemOrder.getPrice()+""));
        tvAmount.setText("x"+itemOrder.getQuantity());
        Glide.with(context).load(itemOrder.getImage1()).centerCrop().into(ivProduct);


    }

    @Override
    public int getItemCount() {
        return itemOrders.size();
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
