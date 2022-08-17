package com.app.projectfinal.adapter.address;

import static com.app.projectfinal.utils.Constant.DISTRICT_NAME;
import static com.app.projectfinal.utils.Constant.PROVINCE_NAME;
import static com.app.projectfinal.utils.Constant.WARD_NAME;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.projectfinal.R;
import com.app.projectfinal.activity.address.AddAddressActivity;
import com.app.projectfinal.model.address.Wards;

import java.util.List;

public class WardAdapter extends RecyclerView.Adapter<WardAdapter.MyViewHolder> {
    private Context context;
    private List<Wards> wardsList;
    private String provinceName, districtName;

    public WardAdapter(Context context, List<Wards> wardsList, String provinceName, String districtName) {
        this.context = context;
        this.wardsList = wardsList;
        this.provinceName = provinceName;
        this.districtName = districtName;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_province, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvName.setText(wardsList.get(position).getName());

        holder.itemView.setOnClickListener(v -> {

            Bundle bundle = new Bundle();
            bundle.putString(DISTRICT_NAME, districtName);
            bundle.putString(PROVINCE_NAME, provinceName);
            bundle.putString(WARD_NAME, wardsList.get(position).getName());
            Log.e("jh", provinceName+districtName+wardsList.get(position).getName());

            Intent intent=new Intent(context, AddAddressActivity.class);
            intent.putExtras(bundle);
            context.startActivity(intent);
            ((Activity)context).finish();

        });


    }

    @Override
    public int getItemCount() {
        return wardsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;

        public MyViewHolder(@NonNull View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
        }
    }
}
