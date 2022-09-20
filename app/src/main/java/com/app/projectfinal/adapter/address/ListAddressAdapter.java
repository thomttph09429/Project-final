package com.app.projectfinal.adapter.address;


import static com.app.projectfinal.utils.Constant.ADDRESS;
import static com.app.projectfinal.utils.Constant.PRODUCTS;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.activity.address.WardFragment;
import com.app.projectfinal.model.address.AddressUser;
import com.app.projectfinal.model.address.Districts;
import com.app.projectfinal.utils.ConstantData;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListAddressAdapter extends RecyclerView.Adapter<ListAddressAdapter.MyViewHolder> {
    private Context context;
    private List<AddressUser> addressUserList;

    public ListAddressAdapter(Context context, List<AddressUser> addressUserList) {
        this.context = context;
        this.addressUserList = addressUserList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.tvPhoneNumber.setText(addressUserList.get(position).getPhone());
        holder.tvAddress.setText(addressUserList.get(position).getLocation());
        holder.tvUserName.setText(addressUserList.get(position).getCustomerName());
        holder.itemView.setOnClickListener(v -> {


        });
        holder.ivDelete.setOnClickListener(v -> {




            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage("Bạn chắc chắn muốn xóa địa chỉ này?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Xóa",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            deleteAddress(addressUserList.get(position).getId(), position);
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
private  void  deleteAddress(String id, int position){
    JSONObject user = new JSONObject();
    String url = ADDRESS + "/" + id;
    ProgressBarDialog.getInstance(context).showDialog("Đang xóa địa chỉ", context);
    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, user, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            ProgressBarDialog.getInstance(context).closeDialog();
            addressUserList.remove(position);
            notifyDataSetChanged();

            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();



        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
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
    @Override
    public int getItemCount() {
        return addressUserList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUserName, tvPhoneNumber, tvAddress;
        private ImageView ivDelete;

        public MyViewHolder(@NonNull View v) {
            super(v);
            tvUserName = v.findViewById(R.id.tvUserName);
            tvPhoneNumber = v.findViewById(R.id.tvPhoneNumber);
            tvAddress = v.findViewById(R.id.tvAddress);
            ivDelete = v.findViewById(R.id.ivDelete);
        }
    }
}
