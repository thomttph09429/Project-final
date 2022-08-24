package com.app.projectfinal.adapter.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.projectfinal.R;
import com.app.projectfinal.model.order.ItemOrder;
import com.app.projectfinal.model.order.Order;
import com.bumptech.glide.Glide;

import java.util.List;

public class OrderItemWaitAdapter extends RecyclerView.Adapter<OrderItemWaitAdapter.MyViewHolder> {
    private Context context;
    private List<ItemOrder> itemOrders;

    public OrderItemWaitAdapter(Context context, List<ItemOrder> itemOrders) {
        this.context = context;
        this.itemOrders = itemOrders;
    }

    @NonNull
    @Override
    public OrderItemWaitAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pay, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemOrder itemOrder= itemOrders.get(position);
        holder.tvAmount.setText(itemOrder.getQuantity()+"");
        holder.tvNameProduct.setText(itemOrder.getName());
        holder.tvPrice.setText(itemOrder.getPrice()+"");
        Glide.with(context).load(itemOrder.getQuantity()).error(R.drawable.avatar_empty).into(holder.ivProduct);

    }

    @Override
    public int getItemCount() {
        return itemOrders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNameProduct, tvPrice, tvAmount;
        private ImageView ivProduct;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            ivProduct = itemView.findViewById(R.id.ivProduct);


        }
    }
}
