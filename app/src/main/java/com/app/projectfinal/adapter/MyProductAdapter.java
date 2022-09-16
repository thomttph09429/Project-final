package com.app.projectfinal.adapter;

import static com.app.projectfinal.activity.MainActivity.storeId;
import static com.app.projectfinal.utils.Constant.ADD_STORES;
import static com.app.projectfinal.utils.Constant.CATEGORY_NAME;
import static com.app.projectfinal.utils.Constant.DESCRIPTION_PRODUCT;
import static com.app.projectfinal.utils.Constant.ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.IMAGE1_PRODUCT;
import static com.app.projectfinal.utils.Constant.NAME_PRODUCT;
import static com.app.projectfinal.utils.Constant.PRICE_PRODUCT;
import static com.app.projectfinal.utils.Constant.PRODUCTS;
import static com.app.projectfinal.utils.Constant.QUANTITY_PRODUCT;
import static com.app.projectfinal.utils.Constant.STORE_ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.STORE_NAME_PRODUCT;
import static com.app.projectfinal.utils.Constant.UNIT_ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.UNIT_NAME;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.activity.DetailProductActivity;
import com.app.projectfinal.activity.LoginActivity;
import com.app.projectfinal.activity.ProfileSettingActivity;
import com.app.projectfinal.activity.SignUpShopActivity;
import com.app.projectfinal.model.Product;
import com.app.projectfinal.utils.ConstantData;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.SharedPrefsSingleton;
import com.app.projectfinal.utils.ValidateForm;
import com.app.projectfinal.utils.VolleySingleton;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyProductAdapter extends RecyclerView.Adapter<MyProductAdapter.MyViewHolder> {
    private List<Product> products;
    private Context context;
    private Product product;

    public MyProductAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public MyProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_product, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        product = products.get(position);
        holder.nameProduct.setText(ValidateForm.capitalizeFirst(product.getProductName()));
        holder.price.setText(ValidateForm.getDecimalFormattedString(product.getPrice()));
        String productId = product.getId();
        holder.quantity.setText(product.getQuantity());
        Glide.with(context).load(product.getImage1()).error(R.drawable.ic_image_error).into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailProductActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(NAME_PRODUCT, products.get(position).getProductName());
                bundle.putString(PRICE_PRODUCT, products.get(position).getPrice());
                bundle.putString(IMAGE1_PRODUCT, products.get(position).getImage1());
                bundle.putString(STORE_NAME_PRODUCT, products.get(position).getStoreName());
                bundle.putString(DESCRIPTION_PRODUCT, products.get(position).getDescription());
                bundle.putString(CATEGORY_NAME, products.get(position).getCategoryName());
                bundle.putString(STORE_ID_PRODUCT, products.get(position).getStoreId());
                bundle.putString(QUANTITY_PRODUCT, products.get(position).getQuantity());
                bundle.putString(ID_PRODUCT, products.get(position).getId());
                bundle.putString(UNIT_NAME, products.get(position).getUnitName());

                intent.putExtras(bundle);
                context.startActivity(intent);


            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage("Bạn chắc chắn muốn xóa sản phẩm này? sau khi xóa, bạn không thể hoàn tác hay khôi phục sản phẩm");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Xóa",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            deleteProduct(productId, position);
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
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView nameProduct, price, quantity;
        private ImageView image;
        private Button btnDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameProduct = itemView.findViewById(R.id.tvNameProduct);
            price = itemView.findViewById(R.id.tvPrice);
            image = itemView.findViewById(R.id.ivImage);
            quantity = itemView.findViewById(R.id.tvQuantity);
            btnDelete = itemView.findViewById(R.id.btnDelete);


        }
    }


    private void deleteProduct(String productId, int position) {
        JSONObject user = new JSONObject();
        String url = PRODUCTS + "/" + productId;
        ProgressBarDialog.getInstance(context).showDialog("Đang xóa sản phẩm", context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ProgressBarDialog.getInstance(context).closeDialog();
                products.remove(position);
                notifyDataSetChanged();

                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "" + error.toString(), Toast.LENGTH_LONG).show();
                ProgressBarDialog.getInstance(context).closeDialog();

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


}
