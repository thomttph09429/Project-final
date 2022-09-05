package com.app.projectfinal.adapter.order.shopOrder;

import static com.app.projectfinal.activity.MyShopActivity.storeId;
import static com.app.projectfinal.utils.Constant.DATE_OF_BIRTH;
import static com.app.projectfinal.utils.Constant.EMAIL;
import static com.app.projectfinal.utils.Constant.ORDER;
import static com.app.projectfinal.utils.Constant.STATUS;
import static com.app.projectfinal.utils.Constant.TOTAL;
import static com.app.projectfinal.utils.Constant.TOTAL_PRICE;
import static com.app.projectfinal.utils.Constant.UPDATE_USER;

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
import com.app.projectfinal.activity.ProfileSettingActivity;
import com.app.projectfinal.model.order.ItemOrder;
import com.app.projectfinal.model.order.Order;
import com.app.projectfinal.order.myOrder.OrderInformationActivity;
import com.app.projectfinal.order.shopOrder.OrderShopInformationActivity;
import com.app.projectfinal.utils.ConstantData;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.SingletonDialogConfirm;
import com.app.projectfinal.utils.ValidateForm;
import com.app.projectfinal.utils.VolleySingleton;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderConfirmAdapter extends RecyclerView.Adapter<OrderConfirmAdapter.MyViewHolder> {
    private List<Order> orders;
    private Context context;
    View view;


    public OrderConfirmAdapter(List<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }


    @NonNull
    @Override
    public OrderConfirmAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_shop_order, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderConfirmAdapter.MyViewHolder holder, int position) {
        holder.tvAmount.setText(orders.get(position).getTotal() + "");
        holder.tvTotalPrice.setText(ValidateForm.getDecimalFormattedString(orders.get(position).getTotalPrice() + ""));
        holder.tvUserName.setText(orders.get(position).getCustomerName());
        String id = orders.get(position).getId();
        int status = orders.get(position).getStatus();
        // update order to delivery
        holder.btnProcess.setOnClickListener(v -> {
            showDialogConfirm("Đồng ý xác nhận giao hàng cho người mua?",2,id);

        });
        //update order to finish
        holder.btnConfirm.setOnClickListener(v -> {
            showDialogConfirm("Đã hoàn thành đơn giao cho người mua?" +
                    "",3,id);
        });
        if (status == 1) {
            holder.btnProcess.setVisibility(View.VISIBLE);
        } else if (status == 2) {
            holder.btnConfirm.setVisibility(View.VISIBLE);

        } else {

        }
        for (int i = 0; i < orders.get(position).getItemOrders().size(); i++) {
            Glide.with(context).load(orders.get(position).getItemOrders().get(0).getImage1()).centerCrop().error(R.drawable.avatar_empty).into(holder.ivProduct);
            holder.tvNameProduct.setText(orders.get(position).getItemOrders().get(0).getName());
            holder.tvPrice.setText(ValidateForm.getDecimalFormattedString(String.valueOf(orders.get(position).getItemOrders().get(0).getPrice())));
            holder.tvAmountProduct.setText("x" + orders.get(position).getItemOrders().get(0).getQuantity() + "");

        }
        holder.rlItem.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderShopInformationActivity.class);
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
        private TextView tvAmount, tvTotalPrice, tvUserName, tvPrice, tvAmountProduct, tvNameProduct;
        private ImageView ivProduct;
        private RelativeLayout rlItem;
        private AppCompatButton btnProcess, btnConfirm;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvAmountProduct = itemView.findViewById(R.id.tvAmountProduct);
            tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
            rlItem = itemView.findViewById(R.id.rlItem);
            btnProcess = itemView.findViewById(R.id.btnProcess);
            btnConfirm = itemView.findViewById(R.id.btnConfirm);


        }
    }

    private void updateOrder(String id, int status) {
        String urlOrder = ORDER + "/" + id;
        JSONObject user = new JSONObject();
        try {
            user.put(STATUS, status);
            JSONObject data = new JSONObject();
            data.put("data", user);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, urlOrder, user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ConstantData.showToast("Cập nhật thành công", R.drawable.ic_mark, context, view);
                Log.e("update", response+"");
                Log.e("update", ConstantData.getToken(context)+"");


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "" + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", ConstantData.getToken(context.getApplicationContext()));
                return headers;
            }
        };
        VolleySingleton.getInstance(context.getApplicationContext()).getRequestQueue().add(jsonObjectRequest);
    }

    public void showDialogConfirm(String message, int status, String idOrder) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Đồng ý",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        updateOrder( idOrder, status);
                    }
                });

        builder1.setNegativeButton(
                "Hủy",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }
}
