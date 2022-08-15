package com.app.projectfinal.fragment;

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

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.activity.CartActivity;
import com.app.projectfinal.activity.ListChatActivity;
import com.app.projectfinal.adapter.ProductAdapter;
import com.app.projectfinal.adapter.SliderAddsAdapter;
import com.app.projectfinal.model.Product;
import com.app.projectfinal.model.SliderItem;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private View view;
    private RecyclerView rcvProduct;
    private ProductAdapter productAdapter;
    private List<Product> products;
    private TextView tvLoading;
    private ImageView ivMessage, ivCart;
    private NestedScrollView nestedScrollView;
    private int page = 1;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_carts:
                break;
        }

        return true;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.dashboard_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null)
            view = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        initAction();
        initAds();
        scrollPage();
        clickMessage();
        clickCart();


        return view;
    }

    private void initAds() {
        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.ic_cover));
        sliderItems.add(new SliderItem(R.drawable.ic_cover));
        sliderItems.add(new SliderItem(R.drawable.ic_cover));


    }


    private void initView() {
        rcvProduct = view.findViewById(R.id.rcv_products);
        tvLoading = view.findViewById(R.id.tvLoading);
        ivMessage = view.findViewById(R.id.ivMessage);
        ivCart = view.findViewById(R.id.ivCart);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);


    }

    private void initAction() {
        products = new ArrayList<>();
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        rcvProduct.setHasFixedSize(true);
        rcvProduct.setLayoutManager(layoutManager);
        productAdapter = new ProductAdapter(products, getContext());
        rcvProduct.setAdapter(productAdapter);
        getProducts(page);
    }

    /**
     * load more when scroll page
     * <pre>
     *     author:ThomTT
     *     date:13/08/2022
     * </pre>
     */
    private void scrollPage() {
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    page++;
                    getProducts(page);
                }
            }
        });

    }

    /**
     * get product by page
     * <pre>
     *     author:ThomTT
     *     date:13/08/2022
     * </pre>
     * @param page
     */
    private void getProducts(int page) {

        if (products.size() == 0) {
            ProgressBarDialog.getInstance(getContext()).showDialog("Đang tải", getContext());

        }
        String urlProducts = PRODUCTS + "?" + "page=" + page + "&size=" + 12;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlProducts, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("data");
                        JSONArray jsonArray = jsonObject.getJSONArray("products");
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

                            products.add(new Product(price, productName, image1, description, storeName, categoryName, storeId, quantity, id));
                            ProgressBarDialog.getInstance(getContext()).closeDialog();

                        }
                        productAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();

                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                ProgressBarDialog.getInstance(getContext()).closeDialog();

            }
        });
        VolleySingleton.getInstance(getContext()).getRequestQueue().add(jsonObjectRequest);

    }


    /**
     * open list chat screen when click image chat
     * <pre>
     *     author:ThomTT
     *     date:03/08/2022
     *     TODO
     * </pre>
     */

    private void clickMessage() {
        ivMessage.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ListChatActivity.class);
            startActivity(intent);
        });


    }

    private void clickCart() {
        ivCart.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CartActivity.class);
            startActivity(intent);
        });
    }


}