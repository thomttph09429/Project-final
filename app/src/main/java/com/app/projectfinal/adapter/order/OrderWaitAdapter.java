package com.app.projectfinal.adapter.order;

import android.content.ClipData;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.projectfinal.R;
import com.app.projectfinal.model.order.ItemOrder;
import com.app.projectfinal.model.order.Order;
import com.app.projectfinal.utils.ValidateForm;
import com.bumptech.glide.Glide;

import java.util.List;

public class OrderWaitAdapter extends RecyclerView.Adapter<OrderWaitAdapter.MyViewHolder> {
    private List<Order> orders;
    private Context context;
    private OrderItemWaitAdapter orderItemWaitAdapter;
    private List<ItemOrder> itemOrders;

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
        holder.tvViewDetail.setOnClickListener(v -> {
            Log.e("size", orders.get(position).getItemOrders().size() + "");
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAmount, tvTotalPrice, tvNameShop, tvPrice, tvAmountProduct, tvNameProduct, tvViewDetail;
        private ImageView ivProduct;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvNameShop = itemView.findViewById(R.id.tvNameShop);

            tvViewDetail = itemView.findViewById(R.id.tvViewDetail);


        }
    }
}
