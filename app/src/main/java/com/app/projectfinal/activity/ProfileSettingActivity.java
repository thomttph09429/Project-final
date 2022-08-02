package com.app.projectfinal.activity;

import static com.app.projectfinal.utils.Constant.CATEGORY_NAME;
import static com.app.projectfinal.utils.Constant.DESCRIPTION_PRODUCT;
import static com.app.projectfinal.utils.Constant.IMAGE1_PRODUCT;
import static com.app.projectfinal.utils.Constant.NAME_PRODUCT;
import static com.app.projectfinal.utils.Constant.PHONE_NUMBER;
import static com.app.projectfinal.utils.Constant.PICK_IMAGE_REQUEST;
import static com.app.projectfinal.utils.Constant.PRICE_PRODUCT;
import static com.app.projectfinal.utils.Constant.QUANTITY_PRODUCT;
import static com.app.projectfinal.utils.Constant.STORE_ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.STORE_NAME_PRODUCT;
import static com.app.projectfinal.utils.Constant.UPDATE_USER;
import static com.app.projectfinal.utils.Constant.USER_ID_SAVE;
import static com.app.projectfinal.utils.Constant.USER_NAME_SAVE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.adapter.ProductAdapter;
import com.app.projectfinal.data.SharedPrefsSingleton;
import com.app.projectfinal.model.Product;
import com.app.projectfinal.utils.Constant;
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

public class ProfileSettingActivity extends AppCompatActivity {
    private TextView tvUserLogin, tvNumberLogin, tvUpdateAvatar;
    private Uri uriImage;
    private ImageView ivAvtUser;
    private String linkImageUrlFirebase;
    private StorageReference storageRef;
    private StorageTask uploadTask;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);
        initView();
        getInfo();
        updateAvatar();
        clickSave();

    }

    private void updateAvatar() {
        tvUpdateAvatar.setOnClickListener(v -> {
            if (tvUpdateAvatar.getText().equals("Sửa")) {
                openFileChose();

            } else if (tvUpdateAvatar.getText().equals("Cập nhật")) {
                uploadImage();
            }


        });
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
                        Log.e("miannn", linkImageUrlFirebase.toString());

                        ProgressBarDialog.getInstance(ProfileSettingActivity.this).closeDialog();
                        tvUpdateAvatar.setText("Sửa");

                    } else {
                        Toast.makeText(ProfileSettingActivity.this, "đã xảy ra lỗi" + task.getResult(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });

        } else {
            Toast.makeText(ProfileSettingActivity.this, "Không có ảnh nào được chọn", Toast.LENGTH_SHORT).show();

        }
    }

    private void getInfo() {
        tvUserLogin.setText(SharedPrefsSingleton.getInstance(getApplicationContext()).getStringValue(USER_NAME_SAVE));
        tvNumberLogin.setText(SharedPrefsSingleton.getInstance(getApplicationContext()).getStringValue(PHONE_NUMBER));

    }

    /**
     * init view
     * <pre>
     *     author:ThomTT
     *     date:01/08/2022
     * </pre>
     */
    private void initView() {
        tvUserLogin = findViewById(R.id.tvUserLogin);
        tvNumberLogin = findViewById(R.id.tvNumberLogin);
        tvUpdateAvatar = findViewById(R.id.tvUpdateAvatar);
        ivAvtUser = findViewById(R.id.ivAvtUser);
        btnSave=findViewById(R.id.btnSave);
    }
    private void clickSave(){
        btnSave.setOnClickListener(v->{
//            updateInfoUser();
        });
    }

//    private void updateInfoUser() {
//        ProgressBarDialog.getInstance(this).showDialog("Đang tải", this);
//        JSONObject user = new JSONObject();
//        try {
//            user.put("image1", linkImageUrlFirebase);
//            user.put("userName", linkImageUrlFirebase);
//            user.put("phone", linkImageUrlFirebase);
//
//            JSONObject data = new JSONObject();
//            data.put("user", user);
//            JSONObject datas = new JSONObject();
//            datas.put("data", data);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        String url = UPDATE_USER+"?"+"id"+"="+SharedPrefsSingleton.getInstance(getApplicationContext()).getStringValue(USER_ID_SAVE);
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, user, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                if (response!=null){
//                        ProgressBarDialog.getInstance(ProfileSettingActivity.this).closeDialog();
//
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(ProfileSettingActivity.this, "" + error.toString(), Toast.LENGTH_LONG).show();
//            }
//        });
//        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(jsonObjectRequest);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uriImage = data.getData();
            Glide.with(this).load(uriImage).centerCrop().into(ivAvtUser);
            if (uriImage != null) {
                tvUpdateAvatar.setText("Cập nhật");
            }

        }
    }
}