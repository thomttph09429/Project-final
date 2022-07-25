package com.app.projectfinal.activity;

import static com.app.projectfinal.utils.Constant.CATEGORY;
import static com.app.projectfinal.utils.Constant.PICK_IMAGE_REQUEST;
import static com.app.projectfinal.utils.Constant.PRODUCTS;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.adapter.CategoryAdapter;
import com.app.projectfinal.adapter.ProductAdapter;
import com.app.projectfinal.model.Category;
import com.app.projectfinal.model.Product;
import com.app.projectfinal.utils.Constant;
import com.app.projectfinal.utils.VolleySingleton;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {
    private ImageView img_add_product;
    private Uri uriImage;
    private TextView tv_add_product;
    private LinearLayout ln_category, ln_price, ln_enter_price;
    private RecyclerView rv_category;
    private List<Category> categories;
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        initView();
        categories = new ArrayList<>();
        getCategory();
        img_add_product.setOnClickListener(v -> {
            openFileChose();
        });
        ln_category.setOnClickListener(v -> {
            if (rv_category.getVisibility()== View.GONE){
                rv_category.setVisibility(View.VISIBLE);

            }else {
                rv_category.setVisibility(View.GONE);

            }

        });
        ln_price.setOnClickListener(v->{
            if (ln_enter_price.getVisibility()== View.GONE){
                ln_enter_price.setVisibility(View.VISIBLE);

            }else {
                ln_enter_price.setVisibility(View.GONE);

            }
        });


    }

    private void initView() {
        img_add_product = findViewById(R.id.img_add_product);
        ln_category = findViewById(R.id.ln_category);
        rv_category = findViewById(R.id.rvCategory);
        ln_price=findViewById(R.id.ln_price);
        ln_enter_price=findViewById(R.id.ln_enter_price);
        tv_add_product=findViewById(R.id.tv_add_product);
    }

    private void openFileChose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uriImage = data.getData();
            Glide.with(this).load(uriImage).centerCrop().into(img_add_product);

        }
    }

    private void getCategory() {
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rv_category.setLayoutManager(layoutManager);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, CATEGORY, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonObject = response.getJSONObject("data");
                    JSONArray jsonArray = jsonObject.getJSONArray("categories");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String categoryName = object.getString("name");
                        categories.add(new Category(categoryName));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AddProductActivity.this, e.toString(), Toast.LENGTH_LONG).show();

                }
                categoryAdapter = new CategoryAdapter(categories, AddProductActivity.this);
                rv_category.setAdapter(categoryAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddProductActivity.this, error.toString(), Toast.LENGTH_LONG).show();

            }
        });
        VolleySingleton.getInstance(this).getRequestQueue().add(jsonObjectRequest);

    }
}