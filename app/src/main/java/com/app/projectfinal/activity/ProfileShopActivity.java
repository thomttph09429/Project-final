package com.app.projectfinal.activity;

import static com.app.projectfinal.activity.MainActivity.status;
import static com.app.projectfinal.activity.MainActivity.storeId;
import static com.app.projectfinal.utils.Constant.ADD_STORES;
import static com.app.projectfinal.utils.Constant.DESCRIPTION_STORE;
import static com.app.projectfinal.utils.Constant.LINK_SUPPORT_STORE;
import static com.app.projectfinal.utils.Constant.MY_CAMERA_UPDATE_COVERPHOTO;
import static com.app.projectfinal.utils.Constant.NAME_STORE;
import static com.app.projectfinal.utils.Constant.PICK_IMAGE_REQUEST;
import static com.app.projectfinal.utils.Constant.USER_ID;

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
import com.app.projectfinal.utils.SharedPrefsSingleton;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileShopActivity extends AppCompatActivity {
    private TextView tvCountNameStore, tvEditAvatar;
    private EditText edtEnterStoreName, edtLinkFace, edtDesStore;
    private Button btnSignUpShop;
    private ImageView ivCover, ivBack;
    private CircleImageView ivAvatar;
    private Uri uriAvatar, uriCover;
    private StorageReference storageRef;
    private StorageTask uploadTask;
    private String image1 = "", image2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_shop);
        initView();
        initAction();
        checkWordCount();
        tvEditAvatar.setOnClickListener(v -> {
            openFileChoseAvatar();

        });
        ivCover.setOnClickListener(v -> {
            openFileChoseCover();

        });

        ivBack.setOnClickListener(v -> {
            finish();
        });
        getInforShop();
        updateShop();
    }

    private void getInforShop() {
        ProgressBarDialog.getInstance(this).showDialog("Đang tải", this);
        String url = ADD_STORES + "/" + storeId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonObject = response.getJSONObject("data");
                    JSONObject data = jsonObject.getJSONObject("store");
                    String image1 = data.getString("image1");
                    String image2 = data.getString("image2");
                    String storeName = data.getString("storeName");
                    String description = data.getString("description");
                    String linkSupport = data.getString("linkSupport");
                    Glide.with(ProfileShopActivity.this).load(image1).centerCrop().error(R.drawable.avatar_empty).into(ivAvatar);
                    Glide.with(ProfileShopActivity.this).load(image2).centerCrop().error(R.drawable.avatar_empty).into(ivCover);
                    edtDesStore.setText(description);
                    edtEnterStoreName.setText(storeName);
                    edtLinkFace.setText(linkSupport);
                    ProgressBarDialog.getInstance(ProfileShopActivity.this).closeDialog();


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileShopActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    ProgressBarDialog.getInstance(ProfileShopActivity.this).closeDialog();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressBarDialog.getInstance(ProfileShopActivity.this
                ).closeDialog();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", ConstantData.getToken(ProfileShopActivity.this.getApplicationContext()));
                return headers;
            }
        };
        VolleySingleton.getInstance(this).getRequestQueue().add(jsonObjectRequest);
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
                    uploadImageCover();

                } else {
                    Toast.makeText(ProfileShopActivity.this, "đã xảy ra lỗi" + task.getResult(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }else {
            Toast.makeText(ProfileShopActivity.this, "chưa chọn ảnh đại diện" , Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(ProfileShopActivity.this, "đã xảy ra lỗi" + task.getResult(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }else {
            Toast.makeText(ProfileShopActivity.this, "chưa chọn ảnh bìa" , Toast.LENGTH_SHORT).show();

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
            user.put("name", edtEnterStoreName.getText().toString().trim());
            user.put("linkSupport", edtLinkFace.getText().toString().trim());
            user.put("description", edtDesStore.getText().toString().trim());
            user.put("image1", image1);
            user.put("image2", image2);

            JSONObject datas = new JSONObject();
            datas.put("data", user);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ProgressBarDialog.getInstance(ProfileShopActivity.this).closeDialog();
                ConstantData.showToast("Cập nhật thành công", R.drawable.ic_mark, ProfileShopActivity.this, getWindow().getDecorView().findViewById(android.R.id.content));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressBarDialog.getInstance(ProfileShopActivity.this).closeDialog();

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
    private void updateShop() {
        btnSignUpShop.setOnClickListener(v -> {
            String edtName = edtEnterStoreName.getText().toString().trim();
            if (!ValidateForm.isName(edtName)) {
                Toast.makeText(ProfileShopActivity.this, "" + "Tên cửa hàng từ 8 kí tự", Toast.LENGTH_LONG).show();

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
        ivBack = findViewById(R.id.ivBack);

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