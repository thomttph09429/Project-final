package com.app.projectfinal.fragment;

import static com.app.projectfinal.utils.Constant.CATEGORY;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.activity.AddProductActivity;
import com.app.projectfinal.adapter.CategoryAdapter;
import com.app.projectfinal.listener.ListenerCategoryName;
import com.app.projectfinal.listener.ListenerSendCategory;
import com.app.projectfinal.model.Category;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ListCategoryDialogFragment extends DialogFragment {
    private View view;
    private Button btnChooseCategory;
    private RecyclerView rvListCategory;
    private CategoryAdapter categoryAdapter;
    private List<Category> categories;
    private ListenerSendCategory mListenerSendCategory;
    private ListenerCategoryName mListenerCategory;
    private String id, name;

    public ListCategoryDialogFragment(ListenerSendCategory mListenerCategoryName) {
        this.mListenerSendCategory = mListenerCategoryName;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_list_category_dialog, container);
            initView();
            getCategory();
            initAction();
            clickChooseCategory();
        }
        return view;
    }

    private void initAction() {
        categories = new ArrayList<>();
        mListenerCategory = new ListenerCategoryName() {
            @Override
            public void onItemClick(String categoryId, String categoryName) {
                btnChooseCategory.setEnabled(true);
                id=categoryId;
                name=categoryName;

            }
        };

    }
    private void clickChooseCategory() {
        btnChooseCategory.setOnClickListener(v -> {
            mListenerSendCategory.onClickSaveCategory(name,id );

            dismiss();

        });

    }

    private void initView() {
        rvListCategory = view.findViewById(R.id.rvCategories);
        btnChooseCategory=view.findViewById(R.id.btnChooseCategory);

    }

    private void getCategory() {
        ProgressBarDialog.getInstance(getContext()).showDialog("Đang tải", getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvListCategory.setLayoutManager(layoutManager);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, CATEGORY, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonObject = response.getJSONObject("data");
                    JSONArray jsonArray = jsonObject.getJSONArray("categories");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String categoryName = object.getString("name");
                        String categoryId = object.getString("id");
                        categories.add(new Category(categoryName, categoryId));
                        ProgressBarDialog.getInstance(getContext()).closeDialog();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();

                }
                categoryAdapter = new CategoryAdapter(categories, getContext(), mListenerCategory);
                rvListCategory.setAdapter(categoryAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();

            }
        });
        VolleySingleton.getInstance(getContext()).getRequestQueue().add(jsonObjectRequest);

    }
}