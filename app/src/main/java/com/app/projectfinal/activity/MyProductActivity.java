package com.app.projectfinal.activity;

import static com.app.projectfinal.activity.MyShopActivity.storeId;
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
import static com.app.projectfinal.utils.Constant.TOTAL;
import static com.app.projectfinal.utils.Constant.UNIT_ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.UNIT_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.adapter.MyProductAdapter;
import com.app.projectfinal.adapter.ProductAdapter;
import com.app.projectfinal.adapter.ProductByShopAdapter;
import com.app.projectfinal.model.Product;
import com.app.projectfinal.utils.ConstantData;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//date 16/08

public class MyProductActivity extends AppCompatActivity {
    private RecyclerView rvMyProduct;
    private String  total;
    private List<Product> products;
    private MyProductAdapter myProductAdapter;
    private TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product);
        initView();
        initAction();
        getProduct();
    }

    private void initAction() {
        products = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvMyProduct.setHasFixedSize(true);
        rvMyProduct.setLayoutManager(layoutManager);

    }

    private void getProduct() {

        String url = PRODUCTS + "?" + "storeId" + "=" + storeId;
        ProgressBarDialog.getInstance(MyProductActivity.this).showDialog("Đang tải", MyProductActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("data");
                        JSONArray jsonArray = jsonObject.getJSONArray("products");
                        total = jsonObject.getString(TOTAL);
                        tvTotal.setText(total);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String productName = object.getString(NAME_PRODUCT);
                            String image1 = object.getString(IMAGE1_PRODUCT);
                            String price = object.getString(PRICE_PRODUCT);
                            String storeName = object.getString(STORE_NAME_PRODUCT);
                            String categoryName = object.getString(CATEGORY_NAME);
                            String description = object.getString(DESCRIPTION_PRODUCT);
                            String storeId = object.getString(STORE_ID_PRODUCT);
                            String quantity = object.getString(QUANTITY_PRODUCT);
                            String id = object.getString(ID_PRODUCT);
                            String unit = object.getString(UNIT_NAME);

                            products.add(new Product(price, productName, image1, description, storeName, categoryName, storeId, quantity, id,unit));

                        }
                        if (products!=null){
                            myProductAdapter = new MyProductAdapter(products, MyProductActivity.this);
                            rvMyProduct.setAdapter(myProductAdapter);
                            myProductAdapter.notifyDataSetChanged();
                        }else {

                        }

                        ProgressBarDialog.getInstance(MyProductActivity.this).closeDialog();

                    } catch (JSONException e) {

                        e.printStackTrace();
                        Toast.makeText(MyProductActivity.this, e.toString(), Toast.LENGTH_LONG).show();

                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyProductActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                ProgressBarDialog.getInstance(MyProductActivity.this).closeDialog();

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", ConstantData.getToken(getApplicationContext()));
                return headers;
            }
        };
        VolleySingleton.getInstance(MyProductActivity.this).getRequestQueue().add(jsonObjectRequest);
    }

    private void initView() {
        tvTotal = findViewById(R.id.tvTotal);
        rvMyProduct = findViewById(R.id.rvMyProduct);
    }


}