package com.app.projectfinal.adapter.order.myOrder;

import static com.app.projectfinal.utils.Constant.ORDER;
import static com.app.projectfinal.utils.Constant.STATUS;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.model.order.Order;
import com.app.projectfinal.order.myOrder.OrderInformationActivity;
import com.app.projectfinal.order.shopOrder.OrderShopInformationActivity;
import com.app.projectfinal.utils.ConstantData;
import com.app.projectfinal.utils.ValidateForm;
import com.app.projectfinal.utils.VolleySingleton;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    private List<Order> orders;
    private Context context;
    private View view;

    public OrderAdapter(List<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }


    @NonNull
    @Override
    public OrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.MyViewHolder holder, int position) {
        holder.tvAmount.setText(orders.get(position).getTotal() + "");
        holder.tvTotalPrice.setText(ValidateForm.getDecimalFormattedString(orders.get(position).getTotalPrice() + ""));
        holder.tvNameShop.setText(orders.get(position).getName_store());
        String id = orders.get(position).getId();
        int status = orders.get(position).getStatus();
        Log.e("lojjjg", status + "");
        if (status == 1) {
            holder.btnProcess.setVisibility(View.VISIBLE);
        } else if (status == 2 || status == 3) {
            holder.btnConfirm.setVisibility(View.VISIBLE);

        } else {
            holder.btnCancel.setVisibility(View.VISIBLE);

        }
        for (int i = 0; i < orders.get(position).getItemOrders().size(); i++) {
            Glide.with(context).load(orders.get(position).getItemOrders().get(0).getImage1()).centerCrop().error(R.drawable.avatar_empty).into(holder.ivProduct);
            holder.tvNameProduct.setText(orders.get(position).getItemOrders().get(0).getName());
            holder.tvPrice.setText(ValidateForm.getDecimalFormattedString(String.valueOf(orders.get(position).getItemOrders().get(0).getPrice())));
            holder.tvAmountProduct.setText("x" + orders.get(position).getItemOrders().get(0).getQuantity() + "");

        }
        //send orderId to OrderInformationActivity
        holder.rlItem.setOnClickListener(v -> {
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
        private TextView tvAmount, tvTotalPrice, tvNameShop, tvPrice, tvAmountProduct, tvNameProduct;
        private ImageView ivProduct;
        private RelativeLayout rlItem;
        private AppCompatButton btnProcess, btnConfirm, btnCancel;

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
            btnProcess = itemView.findViewById(R.id.btnProcess);
            btnConfirm = itemView.findViewById(R.id.btnConfirm);
            btnCancel = itemView.findViewById(R.id.btnCancel);


        }
    }



}
