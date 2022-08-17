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
import com.app.projectfinal.utils.ValidateForm;
import com.bumptech.glide.Glide;

import java.util.List;

public class MyProductAdapter  extends RecyclerView.Adapter<MyProductAdapter.MyViewHolder> {
    private List<Product> products;
    private Context context;
    private Product product;
    public MyProductAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context=context;
    }

        @NonNull
    @Override
    public MyProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_my_product, parent, false);
            return new MyViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull MyProductAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        product = products.get(position);
        holder.nameProduct.setText(ValidateForm.capitalizeFirst(product.getProductName()));
        holder.price.setText(ValidateForm.getDecimalFormattedString(product.getPrice()));
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

                intent.putExtras(bundle);
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
    public  class  MyViewHolder extends RecyclerView.ViewHolder{

        private TextView nameProduct, price, quantity;
        private ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameProduct = itemView.findViewById(R.id.tvNameProduct);
            price = itemView.findViewById(R.id.tvPrice);
            image = itemView.findViewById(R.id.ivImage);
            quantity= itemView.findViewById(R.id.tvQuantity);

        }
    }
}
