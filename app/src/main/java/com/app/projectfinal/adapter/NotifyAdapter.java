package com.app.projectfinal.adapter;

import static com.app.projectfinal.utils.Constant.CATEGORY_NAME;
import static com.app.projectfinal.utils.Constant.DESCRIPTION_PRODUCT;
import static com.app.projectfinal.utils.Constant.ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.IMAGE1_PRODUCT;
import static com.app.projectfinal.utils.Constant.NAME_PRODUCT;
import static com.app.projectfinal.utils.Constant.PRICE_PRODUCT;
import static com.app.projectfinal.utils.Constant.QUANTITY_PRODUCT;
import static com.app.projectfinal.utils.Constant.STORE_ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.STORE_NAME_PRODUCT;
import static com.app.projectfinal.utils.Constant.UNIT_NAME;

import android.annotation.SuppressLint;
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
import com.app.projectfinal.activity.DetailProductActivity;
import com.app.projectfinal.model.Product;
import com.app.projectfinal.model.order.DetailOrder;
import com.app.projectfinal.order.myOrder.OrderInformationActivity;
import com.app.projectfinal.utils.ValidateForm;
import com.bumptech.glide.Glide;

import java.util.List;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.MyViewHolder> {
    private List<DetailOrder> orders;
    private Context context;
    private DetailOrder detailOrder;

    public NotifyAdapter(List<DetailOrder> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notify, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        detailOrder = orders.get(position);
        holder.tvIdOrder.setText(detailOrder.getId());
        String id = orders.get(position).getId();

        int status = detailOrder.getStatus();
        if (status == 0) {
            holder.tvContent.setText("Đã bị hủy bởi cửa hàng");
        } else if (status == 1) {
            holder.tvContent.setText("Đã đặt thành công");

        } else if (status == 2) {
            holder.tvContent.setText("Đã được giao cho đơn vị vận chuyển");

        } else if (status == 3) {
            holder.tvContent.setText("Đã giao thành công đến bạn");

        }
        holder.tvTime.setText(detailOrder.getUpdatedAt());

        for (int i = 0; i < orders.get(position).getProducts().size(); i++) {
            Glide.with(context).load(orders.get(position).getProducts().get(0).getImage1()).centerCrop().error(R.drawable.avatar_empty).into(holder.ivProduct);

        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderInformationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", id);

            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvIdOrder, tvContent, tvTime;
        private ImageView ivProduct;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIdOrder = itemView.findViewById(R.id.tvIdOrder);
            tvContent = itemView.findViewById(R.id.tvContent);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvTime = itemView.findViewById(R.id.tvTime);

        }


    }
}
