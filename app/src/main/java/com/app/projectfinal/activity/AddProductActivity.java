package com.app.projectfinal.activity;

import static com.app.projectfinal.utils.Constant.ADD_PRODUCTS;
import static com.app.projectfinal.utils.Constant.CATEGORY;
import static com.app.projectfinal.utils.Constant.CATEGORY_ID;
import static com.app.projectfinal.utils.Constant.CATEGORY_NAME;
import static com.app.projectfinal.utils.Constant.DESCRIPTION_PRODUCT;
import static com.app.projectfinal.utils.Constant.IMAGE1_PRODUCT;
import static com.app.projectfinal.utils.Constant.NAME;
import static com.app.projectfinal.utils.Constant.PICK_IMAGE_REQUEST;
import static com.app.projectfinal.utils.Constant.PRICE_PRODUCT;
import static com.app.projectfinal.utils.Constant.QUANTITY_PRODUCT;
import static com.app.projectfinal.utils.Constant.STORE_ID;
import static com.app.projectfinal.utils.Constant.STORE_ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.UNIT_ID_PRODUCT;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.adapter.CategoryAdapter;
import com.app.projectfinal.data.SharedPrefsSingleton;
import com.app.projectfinal.fragment.ListCategoryDialogFragment;
import com.app.projectfinal.fragment.UnitDialogFragment;
import com.app.projectfinal.listener.ListenerCategoryName;
import com.app.projectfinal.listener.ListenerSendCategory;
import com.app.projectfinal.listener.ListenerSendUnit;
import com.app.projectfinal.model.Category;
import com.app.projectfinal.utils.ConstantData;
import com.app.projectfinal.utils.NDigitCardFormatWatcher;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.ValidateForm;
import com.app.projectfinal.utils.VolleySingleton;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {
    private ImageView img_add_product;
    private Uri uriImage;
    private TextView tvCountNameProduct, tvCountDescription, tvShowUnit, tvShowCategory;
    private LinearLayout ln_category, lnUnit;
    private StorageReference storageRef;
    private StorageTask uploadTask;
    private Button btn_add_product;
    private String linkImageUrlFirebase;
    private EditText edt_enter_name_product, edt_enter_description, edt_enter_price, edtEnterQuantity;
    private String typeOfCategory="", typeOfUnit="", unitId, idCategory;
    private ListenerSendUnit mListenerSendUnit;
    private ListenerSendCategory mListenerSendCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        initView();
        checkWordCount();
        openUnitList();
        openCategoryList();
        img_add_product.setOnClickListener(v -> {
            openFileChose();
        });


        btn_add_product.setOnClickListener(v -> {

            if (uriImage == null) {
                Toast.makeText(AddProductActivity.this, "Hãy thêm ảnh", Toast.LENGTH_SHORT).show();

            } else if (edt_enter_name_product.getText().toString().equals("")) {
                Toast.makeText(AddProductActivity.this, "Thêm tên sản phẩm", Toast.LENGTH_SHORT).show();

            } else if (typeOfCategory.toString().equals("")) {
                Toast.makeText(AddProductActivity.this, "Hãy chọn danh mục", Toast.LENGTH_SHORT).show();

            } else if (edt_enter_price.getText().toString().equals("")) {
                Toast.makeText(AddProductActivity.this, "Hãy nhập giá", Toast.LENGTH_SHORT).show();

            }else if (edtEnterQuantity.getText().toString().equals("")) {
                Toast.makeText(AddProductActivity.this, "Hãy chọn số lượng hàng", Toast.LENGTH_SHORT).show();

            }else if (typeOfUnit.toString().equals("")) {
                Toast.makeText(AddProductActivity.this, "Hãy chọn phân loại hàng", Toast.LENGTH_SHORT).show();

            }else {
                uploadImage();

            }










        });

        mListenerSendUnit = new ListenerSendUnit() {
            @Override
            public void onClickSave(String unitName, String id) {
                typeOfUnit = unitName;
                unitId = id;
                tvShowUnit.setText(typeOfUnit);
            }
        };
        mListenerSendCategory = new ListenerSendCategory() {
            @Override
            public void onClickSaveCategory(String categoryName, String id) {
                typeOfCategory = categoryName;
                idCategory = id;
                tvShowCategory.setText(typeOfCategory);
                Log.e("onClickSaveCategory", "" + typeOfCategory + id);
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
                tvCountNameProduct.setText(character + "/120");

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
                tvCountDescription.setText(character + "/3000");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edt_enter_price.addTextChangedListener(new NDigitCardFormatWatcher(edt_enter_price));

    }

    private void initView() {
        img_add_product = findViewById(R.id.img_add_product);
        ln_category = findViewById(R.id.lnCategory);
        lnUnit = findViewById(R.id.lnUnit);
        btn_add_product = findViewById(R.id.btn_add_product);
        edt_enter_description = findViewById(R.id.edt_enter_description);
        edt_enter_name_product = findViewById(R.id.edt_enter_name_product);
        edt_enter_price = findViewById(R.id.edt_enter_price);
        tvCountNameProduct = findViewById(R.id.tvCountNameProduct);
        tvCountDescription = findViewById(R.id.tvCountDescription);
        edtEnterQuantity = findViewById(R.id.edtEnterQuantity);
        tvShowUnit = findViewById(R.id.tvShowUnit);
        tvShowCategory = findViewById(R.id.tvShowCategory);

    }

    /**
     * open units list screen
     * <pre>
     *     author:ThomTT1
     *     date:31/07/2022
     * </pre>
     */
    private void openUnitList() {
        lnUnit.setOnClickListener(v -> {
            UnitDialogFragment post = new UnitDialogFragment(mListenerSendUnit);
            post.show(getSupportFragmentManager(), "UnitDialogFragment");

        });

    }

    private void openCategoryList() {
        ln_category.setOnClickListener(v -> {
            ListCategoryDialogFragment post = new ListCategoryDialogFragment(mListenerSendCategory);
            post.show(getSupportFragmentManager(), "ListCategoryDialogFragment");

        });

    }

    private void postProducts() {
        JSONObject user = new JSONObject();
        Bundle bundle = getIntent().getExtras();
        String storeId = bundle.getString(STORE_ID_PRODUCT);

        try {
            String price = edt_enter_price.getText().toString();
            String newPrice = price.replace(",", "");

            user.put(NAME, ValidateForm.capitalizeFirst(edt_enter_name_product.getText().toString().trim()));
            user.put(DESCRIPTION_PRODUCT, ValidateForm.capitalizeFirst(edt_enter_description.getText().toString().trim()));
            user.put(PRICE_PRODUCT, newPrice);
            user.put(IMAGE1_PRODUCT, linkImageUrlFirebase);
            user.put(CATEGORY_NAME, ValidateForm.capitalizeFirst(typeOfCategory));
            user.put(QUANTITY_PRODUCT, edtEnterQuantity.getText().toString().trim());
            user.put(STORE_ID_PRODUCT, storeId);
            user.put(UNIT_ID_PRODUCT, unitId);
            user.put(CATEGORY_ID, idCategory);

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
                if (response != null) {
                    Log.e("addProduct", response.toString());
                    ProgressBarDialog.getInstance(AddProductActivity.this).closeDialog();
                    Toast.makeText(AddProductActivity.this, "Thêm thành công ", Toast.LENGTH_LONG).show();
                    finish();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddProductActivity.this, "Thêm thất bại " + error.toString(), Toast.LENGTH_LONG).show();
               Log.e("thatbai",user+"");
                ProgressBarDialog.getInstance(AddProductActivity.this).closeDialog();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", ConstantData.getToken(getApplicationContext()));
                return headers;
            }
        };
        VolleySingleton.getInstance(AddProductActivity.this).getRequestQueue().add(jsonObjectRequest);

    }

    private void uploadImage() {
        storageRef = FirebaseStorage.getInstance().getReference("Posts");
        if (uriImage != null) {
            ProgressBarDialog.getInstance(this).showDialog("Vui lòng đợi", this);
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
            Glide.with(getApplicationContext()).load(uriImage).centerCrop().into(img_add_product);

        }
    }


}