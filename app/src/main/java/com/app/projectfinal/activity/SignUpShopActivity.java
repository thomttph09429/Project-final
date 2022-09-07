package com.app.projectfinal.activity;

import static com.app.projectfinal.activity.MainActivity.role;
import static com.app.projectfinal.activity.MainActivity.storeId;
import static com.app.projectfinal.utils.Constant.ADD_STORES;
import static com.app.projectfinal.utils.Constant.DESCRIPTION_STORE;
import static com.app.projectfinal.utils.Constant.IS_ACTIVE;
import static com.app.projectfinal.utils.Constant.LINK_SUPPORT_STORE;
import static com.app.projectfinal.utils.Constant.MY_CAMERA_UPDATE_COVERPHOTO;
import static com.app.projectfinal.utils.Constant.NAME_STORE;
import static com.app.projectfinal.utils.Constant.PICK_IMAGE_REQUEST;
import static com.app.projectfinal.utils.Constant.UPDATE_USER;
import static com.app.projectfinal.utils.Constant.USER_ID;
import static com.app.projectfinal.utils.Constant.USER_ID_SAVE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.activity.address.AddressActivity;
import com.app.projectfinal.data.SharedPrefsSingleton;
import com.app.projectfinal.order.myOrder.OrderInformationActivity;
import com.app.projectfinal.utils.Constant;
import com.app.projectfinal.utils.ConstantData;
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

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpShopActivity extends AppCompatActivity {
    private TextView tvCountNameStore, tvEditAvatar;
    private EditText edtEnterStoreName, edtLinkFace, edtDesStore;
    private Button btnSignUpShop;
    private ImageView ivCover;
    private CircleImageView ivAvatar;
    private Uri uriAvatar, uriCover;
    private StorageReference storageRef;
    private StorageTask uploadTask;
    private String image1, image2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_shop);
        initView();
        initAction();
        checkWordCount();
        signUpShop();
        tvEditAvatar.setOnClickListener(v -> {
            openFileChoseAvatar();

        });
        ivCover.setOnClickListener(v -> {
            openFileChoseCover();

        });
    }

    private void initAction() {
        storageRef = FirebaseStorage.getInstance().getReference("Posts");

    }

    private void openFileChoseAvatar() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    private void openFileChoseCover() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, MY_CAMERA_UPDATE_COVERPHOTO);

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImageAvatar() {
        if (uriAvatar != null) {
            ProgressBarDialog.getInstance(this).showDialog("Vui lòng đợi", this);
            final StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(uriAvatar));

            uploadTask = fileReference.putFile(uriAvatar);
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
                        image1 = downloadUri.toString();
                        Log.e("miannn", image1.toString());

                        uploadImageCover();

                    } else {
                        Toast.makeText(SignUpShopActivity.this, "đã xảy ra lỗi" + task.getResult(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });

        } else {
            Toast.makeText(SignUpShopActivity.this, "Không có ảnh nào được chọn", Toast.LENGTH_SHORT).show();

        }
    }

    private void uploadImageCover() {
        if (uriCover != null) {
            final StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(uriCover));

            uploadTask = fileReference.putFile(uriCover);
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
                        image2 = downloadUri.toString();
                        updateInfoStore();

                    } else {
                        Toast.makeText(SignUpShopActivity.this, "đã xảy ra lỗi" + task.getResult(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });

        } else {
            Toast.makeText(SignUpShopActivity.this, "Không có ảnh nào được chọn", Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * update store when was sign up store
     */
    private void updateInfoStore() {
        JSONObject user = new JSONObject();
        String url = ADD_STORES + "/" + storeId;
        try {

            user.put("userId", ConstantData.getUserId(this));
            user.put("name", edtEnterStoreName.getText().toString());
            user.put("linkSupport", edtLinkFace.getText().toString());
            user.put("description", edtDesStore.getText().toString());
            user.put("image1",image1);
            user.put("image2",image2);

            JSONObject datas = new JSONObject();
            datas.put("data", user);
            Log.e("ma", user + "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ProgressBarDialog.getInstance(SignUpShopActivity.this).closeDialog();
                ConstantData.showToast("Cập nhật thành công", R.drawable.ic_mark, SignUpShopActivity.this, getWindow().getDecorView().findViewById(android.R.id.content));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUpShopActivity.this, "" + error.toString(), Toast.LENGTH_LONG).show();
                ProgressBarDialog.getInstance(SignUpShopActivity.this).closeDialog();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", ConstantData.getToken(getApplicationContext()));
                return headers;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjectRequest);
    }

    /**
     * click button sign up
     * <pre>
     *     author:ThomTT1
     *     date:30/07/2022
     * </pre>
     */
    private void signUpShop() {
        btnSignUpShop.setOnClickListener(v -> {
            String storeName = edtEnterStoreName.getText().toString().trim();
            String linkFace = edtLinkFace.getText().toString().trim();
            String description = edtDesStore.getText().toString().trim();
            String userId = ConstantData.getUserId(this);
            if (role != 2) {
                signUpToBecomeSeller(userId, storeName, description, linkFace);

            } else {
                uploadImageAvatar();
            }


        });
    }


    private void checkWordCount() {
        edtEnterStoreName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int character = charSequence.length();
                tvCountNameStore.setText(character + "/130");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    /**
     * init view
     * <pre>
     *     author:ThomTT
     *     date:30/07/2022
     * </pre>
     */
    private void initView() {
        tvCountNameStore = findViewById(R.id.tvCountNameStore);
        edtLinkFace = findViewById(R.id.edtLinkFace);
        edtEnterStoreName = findViewById(R.id.edtEnterStoreName);
        tvCountNameStore = findViewById(R.id.tvCountNameStore);
        btnSignUpShop = findViewById(R.id.btnSignUpShop);
        edtDesStore = findViewById(R.id.edtDesStore);

        tvEditAvatar = findViewById(R.id.tvEditAvatar);
        ivCover = findViewById(R.id.ivCover);
        ivAvatar = findViewById(R.id.ivAvatar);

    }


    /**
     * Call API sign up to become seller
     *
     * @param userId
     * @param storeName
     * @param description
     * @param linkSupport
     */
    private void signUpToBecomeSeller(String userId, String storeName, String description, String linkSupport) {
        JSONObject user = new JSONObject();
        try {
            user.put(NAME_STORE, ValidateForm.capitalizeFirst(storeName));
            user.put(USER_ID, userId);
            user.put(DESCRIPTION_STORE, ValidateForm.capitalizeFirst(description));
            user.put(LINK_SUPPORT_STORE, linkSupport);
            JSONObject data = new JSONObject();
            data.put("user", user);
            JSONObject datas = new JSONObject();
            datas.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constant.ADD_STORES, user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        Log.e("response", response.toString());
                        JSONObject strRes = response.getJSONObject("data");
                        JSONObject str = strRes.getJSONObject("user");
                        String storeId = str.getString("id");
                        String storeName = str.getString("name");
                        String userId = str.getString("userId");
                        updateStore(storeId, userId);

                        SharedPrefsSingleton.getInstance(getApplicationContext()).putStringValue(Constant.STORE_ID, storeId);
                        SharedPrefsSingleton.getInstance(getApplicationContext()).putStringValue(Constant.STORE_NAME, storeName);

                        ProgressBarDialog.getInstance(SignUpShopActivity.this).closeDialog();
                        Toast.makeText(SignUpShopActivity.this, "" + "Đăng ký thành công!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SignUpShopActivity.this, MyShopActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUpShopActivity.this, "" + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", ConstantData.getToken(getApplicationContext()));
                return headers;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjectRequest);
    }

    private void updateStore(String storeId, String userId) {
        JSONObject user = new JSONObject();
        String url = ADD_STORES + "/" + storeId;
        try {
            user.put("userId", userId);
            user.put("isActive", 1);
            JSONObject datas = new JSONObject();
            datas.put("data", user);
            Log.e("ma", user + "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("huuuu", response + "");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUpShopActivity.this, "" + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", ConstantData.getToken(getApplicationContext()));
                return headers;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjectRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uriAvatar = data.getData();
            Glide.with(this).load(uriAvatar).centerCrop().into(ivAvatar);

        }
        if (requestCode == MY_CAMERA_UPDATE_COVERPHOTO && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uriCover = data.getData();
            Glide.with(this).load(uriCover).centerCrop().into(ivCover);

        }
    }
}