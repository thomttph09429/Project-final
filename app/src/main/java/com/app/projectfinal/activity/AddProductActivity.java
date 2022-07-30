package com.app.projectfinal.activity;

import static com.app.projectfinal.utils.Constant.ADD_PRODUCTS;
import static com.app.projectfinal.utils.Constant.CATEGORY;
import static com.app.projectfinal.utils.Constant.CATEGORY_NAME;
import static com.app.projectfinal.utils.Constant.DESCRIPTION_PRODUCT;
import static com.app.projectfinal.utils.Constant.IMAGE1_PRODUCT;
import static com.app.projectfinal.utils.Constant.NAME_PRODUCT;
import static com.app.projectfinal.utils.Constant.PICK_IMAGE_REQUEST;
import static com.app.projectfinal.utils.Constant.PRICE_PRODUCT;
import static com.app.projectfinal.utils.Constant.PRODUCTS;
import static com.app.projectfinal.utils.Constant.STORE_NAME_PRODUCT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
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
import com.app.projectfinal.listener.ListenerCategoryName;
import com.app.projectfinal.model.Category;
import com.app.projectfinal.model.Product;
import com.app.projectfinal.utils.Constant;
import com.app.projectfinal.utils.VolleySingleton;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {
    private ImageView img_add_product;
    private Uri uriImage;
    private TextView tv_add_product, tvCountNameProduct, tvCountDescription;
    private LinearLayout ln_category, ln_price, ln_enter_price;
    private RecyclerView rv_category;
    private List<Category> categories;
    private CategoryAdapter categoryAdapter;
    private StorageReference storageRef;
    private StorageTask uploadTask;
    private Button btn_add_product;
    private String linkImageUrlFirebase;
    private EditText edt_enter_name_product, edt_enter_description, edt_enter_price;
    private ListenerCategoryName mListenerCategoryName;
    private String typeOfCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        initView();
        checkWordCount();
        categories = new ArrayList<>();
        getCategory();
        img_add_product.setOnClickListener(v -> {
            openFileChose();
        });
        ln_category.setOnClickListener(v -> {
            if (rv_category.getVisibility() == View.GONE) {
                rv_category.setVisibility(View.VISIBLE);

            } else {
                rv_category.setVisibility(View.GONE);

            }

        });
        ln_price.setOnClickListener(v -> {
            if (ln_enter_price.getVisibility() == View.GONE) {
                ln_enter_price.setVisibility(View.VISIBLE);

            } else {
                ln_enter_price.setVisibility(View.GONE);

            }
        });
        btn_add_product.setOnClickListener(v -> {
            uploadImage();

        });
        mListenerCategoryName = new ListenerCategoryName() {
            @Override
            public void onItemClick(int position, String categoryName) {
                typeOfCategory = categoryName.toString();

            }
        };

    }

    private void checkWordCount() {
        edt_enter_name_product.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int character = charSequence.length();
                tvCountNameProduct.setText(character+"/120");

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edt_enter_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int character = charSequence.length();
                tvCountDescription.setText(character+"/3000");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initView() {
        img_add_product = findViewById(R.id.img_add_product);
        ln_category = findViewById(R.id.ln_category);
        rv_category = findViewById(R.id.rvCategory);
        ln_price = findViewById(R.id.ln_price);
        ln_enter_price = findViewById(R.id.ln_enter_price);
        tv_add_product = findViewById(R.id.tv_add_product);
        btn_add_product = findViewById(R.id.btn_add_product);
        edt_enter_description = findViewById(R.id.edt_enter_description);
        edt_enter_name_product = findViewById(R.id.edt_enter_name_product);
        edt_enter_price = findViewById(R.id.edt_enter_price);
        tvCountNameProduct=findViewById(R.id.tvCountNameProduct);
        tvCountDescription=findViewById(R.id.tvCountDescription);

    }

    private void postProducts() {
        JSONObject user = new JSONObject();
        try {
            user.put(NAME_PRODUCT, edt_enter_name_product.getText().toString().trim());
            user.put(DESCRIPTION_PRODUCT, edt_enter_description.getText().toString().trim());
            user.put(PRICE_PRODUCT, edt_enter_price.getText().toString().trim());
            user.put(IMAGE1_PRODUCT, linkImageUrlFirebase);
            user.put(CATEGORY_NAME, typeOfCategory);

            JSONObject data = new JSONObject();
            data.put("user", user);
            JSONObject product = new JSONObject();
            product.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ADD_PRODUCTS, user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("addProduct", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddProductActivity.this, "Thêm thất bại " + error.toString(), Toast.LENGTH_LONG).show();

            }
        });
        VolleySingleton.getInstance(AddProductActivity.this).getRequestQueue().add(jsonObjectRequest);

    }

    private void uploadImage() {
        storageRef = FirebaseStorage.getInstance().getReference("Posts");
        if (uriImage != null) {

            final StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(uriImage));

            uploadTask = fileReference.putFile(uriImage);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        linkImageUrlFirebase = downloadUri.toString();
                        Log.e("miannn", linkImageUrlFirebase.toString());

                        postProducts();

                    } else {
                        Toast.makeText(AddProductActivity.this, "đã xảy ra lỗi" + task.getResult(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });

        } else {
            Toast.makeText(AddProductActivity.this, "Không có ảnh nào được chọn", Toast.LENGTH_SHORT).show();

        }
    }

    private void openFileChose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
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
                categoryAdapter = new CategoryAdapter(categories, AddProductActivity.this, mListenerCategoryName);
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