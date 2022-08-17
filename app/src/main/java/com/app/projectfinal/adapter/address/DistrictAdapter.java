package com.app.projectfinal.adapter.address;

import static com.app.projectfinal.utils.Constant.CODE_DISTRICT;
import static com.app.projectfinal.utils.Constant.CODE_PROVINCE;
import static com.app.projectfinal.utils.Constant.DISTRICT_NAME;
import static com.app.projectfinal.utils.Constant.PROVINCE_NAME;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.projectfinal.R;
import com.app.projectfinal.activity.address.DistrictFragment;
import com.app.projectfinal.activity.address.WardFragment;
import com.app.projectfinal.model.address.Districts;
import com.app.projectfinal.model.address.Province;

import java.util.List;

public class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.MyViewHolder> {
    private Context context;
    private List<Districts> districts;
    private  String provinceName;

    public DistrictAdapter(Context context, List<Districts> districts, String provinceName) {
        this.context = context;
        this.districts = districts;
        this.provinceName = provinceName;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_province, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvName.setText(districts.get(position).getName());
        int code = districts.get(position).getCode();

        holder.itemView.setOnClickListener(v->{
            Bundle bundle = new Bundle();
            bundle.putInt(CODE_DISTRICT, code);
            bundle.putString(DISTRICT_NAME, districts.get(position).getName());
            bundle.putString(PROVINCE_NAME, provinceName);

            WardFragment wardFragment = new WardFragment();
            wardFragment.setArguments(bundle);
            wardFragment.show(  ((AppCompatActivity) context).getSupportFragmentManager(),"WardFragment");


        });


    }

    @Override
    public int getItemCount() {
        return districts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;

        public MyViewHolder(@NonNull View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
        }
    }
}
