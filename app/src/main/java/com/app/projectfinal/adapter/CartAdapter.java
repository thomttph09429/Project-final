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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.projectfinal.R;
import com.app.projectfinal.activity.DetailProductActivity;
import com.app.projectfinal.db.Cart;
import com.app.projectfinal.db.CartDatabase;
import com.app.projectfinal.utils.ValidateForm;
import com.bumptech.glide.Glide;

import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    private Context context;
    private List<Cart> cartList;
    private OnItemCheckListener onItemClick;
    int selectedPosition = -1;

    public CartAdapter(Context context, List<Cart> cartList, OnItemCheckListener onItemClick) {
        this.context = context;
        this.cartList = cartList;
        this.onItemClick = onItemClick;
    }


    @NonNull
    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.MyViewHolder holder, int position) {
        Cart cart = cartList.get(position);
        holder.tvNameProduct.setText(cart.getNameProduct());
        holder.tvNameShop.setText(cart.getNameShop());
        holder.tvAmount.setText(cart.getAmount());
        holder.tvPrice.setText(cart.getPrice());
        holder.tvUnit.setText("Phân loại theo: " + cart.getUnit());
        Glide.with(context).load(cart.getImageOfProduct()).centerCrop().into(holder.ivImage);
        holder.lnCart.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailProductActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(NAME_PRODUCT, cartList.get(position).getNameProduct());
            int price = ValidateForm.getPriceToInt(cartList.get(position).getPrice());
            bundle.putString(PRICE_PRODUCT, String.valueOf(price));
            bundle.putString(IMAGE1_PRODUCT, cartList.get(position).getImageOfProduct());
            bundle.putString(STORE_NAME_PRODUCT, cartList.get(position).getNameShop());
            bundle.putString(DESCRIPTION_PRODUCT, cartList.get(position).getDescription());
            bundle.putString(CATEGORY_NAME, cartList.get(position).getCategoryName());
            bundle.putString(STORE_ID_PRODUCT, cartList.get(position).getIdShop());
            bundle.putString(QUANTITY_PRODUCT, cartList.get(position).getQuantity());
            bundle.putString(ID_PRODUCT, cartList.get(position).getIdProduct());
            bundle.putString(UNIT_NAME, cartList.get(position).getUnit());

            intent.putExtras(bundle);
            context.startActivity(intent);


        });
        //delete item from list cart
        holder.ivDelete.setOnClickListener(v -> {
            showDialogConfirm("Bạn có muốn xóa khỏi giỏ hàng?", cart);


        });

        if (holder instanceof MyViewHolder) {
            final Cart currentItem = cartList.get(position);

            ((MyViewHolder) holder).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    ((MyViewHolder) holder).cbSelect.setChecked(
                            !((MyViewHolder) holder).cbSelect.isChecked());
                    if (((MyViewHolder) holder).cbSelect.isChecked()) {
                        onItemClick.onItemCheck(currentItem, currentItem.getIdShop());

                    } else {
                        onItemClick.onItemUncheck(currentItem, currentItem.getIdShop());

                    }
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNameShop, tvAmount, tvPrice, tvNameProduct, tvUnit;
        private ImageView ivImage, ivDelete;
        private CheckBox cbSelect;
        private LinearLayout lnCart;
        View itemView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvNameShop = itemView.findViewById(R.id.tvNameShop);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvUnit = itemView.findViewById(R.id.tvUnit);
            lnCart = itemView.findViewById(R.id.lnCart);
            cbSelect = itemView.findViewById(R.id.cbSelect);
            ivDelete = itemView.findViewById(R.id.ivDelete);

            cbSelect.setClickable(false);


        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            itemView.setOnClickListener(onClickListener);
        }
    }

    public interface OnItemCheckListener {
        void onItemCheck(Cart cart, String storeId);

        void onItemUncheck(Cart cart, String storeId);
    }
    public void showDialogConfirm(String message, Cart cart) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Đồng ý",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // delete item from DB
                        CartDatabase.getInstance(context).cartDAO().delete(cart);
                        cartList.remove(cart);
                        notifyDataSetChanged();
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
