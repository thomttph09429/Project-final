package com.app.projectfinal.adapter.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.projectfinal.R;
import com.app.projectfinal.model.order.ItemOrder;
import com.app.projectfinal.model.order.Order;
import com.app.projectfinal.order.myOrder.OrderInformationActivity;
import com.app.projectfinal.utils.ValidateForm;
import com.bumptech.glide.Glide;

import java.util.List;

public class OrderWaitAdapter extends RecyclerView.Adapter<OrderWaitAdapter.MyViewHolder> {
    private List<Order> orders;
    private Context context;
    private OrderItemWaitAdapter orderItemWaitAdapter;
    private List<ItemOrder> itemOrders;
    private String id;

    public OrderWaitAdapter(List<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }


    @NonNull
    @Override
    public OrderWaitAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_wait, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderWaitAdapter.MyViewHolder holder, int position) {
        holder.tvAmount.setText(orders.get(position).getTotal() + " sản phẩm ");
        holder.tvTotalPrice.setText("Tổng thanh toán: " + ValidateForm.getDecimalFormattedString(orders.get(position).getTotalPrice() + ""));
        holder.tvNameShop.setText(orders.get(position).getName_store());
        for (int i = 0; i < orders.get(position).getItemOrders().size(); i++) {
            Glide.with(context).load(orders.get(position).getItemOrders().get(0).getImage1()).centerCrop().error(R.drawable.avatar_empty).into(holder.ivProduct);
            holder.tvNameProduct.setText(orders.get(position).getItemOrders().get(0).getName());
            holder.tvPrice.setText(ValidateForm.getDecimalFormattedString(String.valueOf(orders.get(position).getItemOrders().get(0).getPrice())));
            holder.tvAmountProduct.setText("x" + orders.get(position).getItemOrders().get(0).getQuantity() + "");
            id = orders.get(position).getId();
        }
        holder.rlItem.setOnClickListener(v -> {
//            Log.e("size", orders.get(position).getItemOrders().size() + "");
            Intent intent = new Intent(context, OrderInformationActivity.class);
            Bundle bundle= new Bundle();
            bundle.putString("id", id);
            Log.e("orderId", id+"");

            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAmount, tvTotalPrice, tvNameShop, tvPrice, tvAmountProduct, tvNameProduct;
        private ImageView ivProduct;
        private RelativeLayout rlItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvNameShop = itemView.findViewById(R.id.tvNameShop);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvAmountProduct = itemView.findViewById(R.id.tvAmountProduct);
            tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
            rlItem = itemView.findViewById(R.id.rlItem);


        }
    }

    public interface SendOrderId {
        void sendOrderId(String id);
    }
}
