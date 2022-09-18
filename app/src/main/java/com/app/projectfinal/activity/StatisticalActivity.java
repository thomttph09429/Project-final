package com.app.projectfinal.activity;

import static com.app.projectfinal.activity.MyShopActivity.storeId;
import static com.app.projectfinal.utils.Constant.PHONE;
import static com.app.projectfinal.utils.Constant.PRODUCTS;
import static com.app.projectfinal.utils.Constant.STATISTICAL;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.utils.ConstantData;
import com.app.projectfinal.utils.ValidateForm;
import com.app.projectfinal.utils.VolleySingleton;
import com.ghanshyam.graphlibs.Graph;
import com.ghanshyam.graphlibs.GraphData;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class StatisticalActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvStart, tvEnd, tvGetResutl;
    final Calendar myCalendarStart = Calendar.getInstance();
    private String startSate = "", endDate = "";
    final Calendar myCalendarEnd = Calendar.getInstance();
    private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tvTotalMoney;

    private Graph graph;
    Collection<GraphData> data;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical);
        initView();
        initAction();
        graph.setMinValue(0f);
        graph.setMaxValue(100f);
        graph.setDevideSize(0.5f);
        graph.setBackgroundShapeWidthInDp(10);
        graph.setShapeForegroundColor(getResources().getColor(R.color.red));
        graph.setShapeBackgroundColor(getResources().getColor(R.color.black));

        data = new ArrayList<>();

        tvGetResutl.setOnClickListener(v -> {
            if (!startSate.equals("") && !endDate.equals("")) {
                getStatistical();
            } else {
                Toast.makeText(this, "Hãy chọn thời gian", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initAction() {
        tvStart.setOnClickListener(this);
        tvEnd.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    private void initView() {
        tvStart = findViewById(R.id.tvStart);
        tvEnd = findViewById(R.id.tvEnd);
        graph = findViewById(R.id.graph);
        tvGetResutl = findViewById(R.id.tvGetResutl);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tv6 = findViewById(R.id.tv6);
        tv7 = findViewById(R.id.tv7);
        tv8 = findViewById(R.id.tv8);
        tv9 = findViewById(R.id.tv9);
        tvTotalMoney = findViewById(R.id.tvTotalMoney);
        ivBack = findViewById(R.id.ivBack);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvStart:
                openDateStart();
                break;
            case R.id.tvEnd:
                openDateEnd();
                break;
            case R.id.ivBack:
                finish();
                break;
        }
    }

    private void openDateStart() {


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, month);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, day);
                updateLabelStart();

            }
        };
        new DatePickerDialog(StatisticalActivity.this, date, myCalendarStart.get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH), myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();


    }

    private void updateLabelStart() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        startSate = dateFormat.format(myCalendarStart.getTime());
        tvStart.setText(startSate);
    }

    private void updateLabelEnd() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        endDate = dateFormat.format(myCalendarEnd.getTime());
        tvEnd.setText(endDate);
    }

    private void openDateEnd() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendarEnd.set(Calendar.YEAR, year);
                myCalendarEnd.set(Calendar.MONTH, month);
                myCalendarEnd.set(Calendar.DAY_OF_MONTH, day);
                updateLabelEnd();

            }
        };
        new DatePickerDialog(StatisticalActivity.this, date, myCalendarEnd.get(Calendar.YEAR), myCalendarEnd.get(Calendar.MONTH), myCalendarEnd.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void getStatistical() {
        String url = STATISTICAL + "?startDate=" + startSate + "&endDate=" + endDate + "&storeId=" + storeId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        Log.e("hhffffffffffffff", response + "");
                        JSONObject jsonObject = response.getJSONObject("data");
                        float totalMoney = jsonObject.getInt("totalMoney");
                        float totalMoneySaleFlowers = jsonObject.getInt("totalMoneySaleFlowers");
                        float totalMoneySaleCoffee = jsonObject.getInt("totalMoneySaleCoffee");
                        float totalMoneySaleRice = jsonObject.getInt("totalMoneySaleRice");
                        float totalMoneySalePepper = jsonObject.getInt("totalMoneySalePepper");
                        float totalMoneySaleTea = jsonObject.getInt("totalMoneySaleTea");
                        float totalMoneySaleCashew = jsonObject.getInt("totalMoneySaleCashew");
                        float totalMoneySaleFruit = jsonObject.getInt("totalMoneySaleFruit");
                        float totalMoneySaleVegetables = jsonObject.getInt("totalMoneySaleVegetables");
                        int totalMoneySaleTree = jsonObject.getInt("totalMoneySaleTree");
                        tv1.setText("Hoa: " + ValidateForm.getDecimalFormattedString(totalMoneySaleFlowers + ""));
                        tv2.setText("Cà phê: " + ValidateForm.getDecimalFormattedString(totalMoneySaleCoffee + ""));
                        tv3.setText("Lúa gạo: " + ValidateForm.getDecimalFormattedString(totalMoneySaleRice + ""));
                        tv4.setText("Hồ tiêu: " + ValidateForm.getDecimalFormattedString(totalMoneySalePepper + ""));
                        tv5.setText("Chè: " + ValidateForm.getDecimalFormattedString(totalMoneySaleTea + ""));
                        tv6.setText("Hạt điều: " + ValidateForm.getDecimalFormattedString(totalMoneySaleCashew + ""));
                        tv7.setText("Trái cây: " + ValidateForm.getDecimalFormattedString(totalMoneySaleFruit + ""));
                        tv8.setText("Rau củ: " + ValidateForm.getDecimalFormattedString(totalMoneySaleVegetables + ""));
                        tv9.setText("Cây: " + ValidateForm.getDecimalFormattedString(totalMoneySaleTree + ""));
                        tvTotalMoney.setText(ValidateForm.getDecimalFormattedString(totalMoney + "") + "");
                        if (totalMoney != 0) {
                            float percent1 = totalMoneySaleFlowers / totalMoney * 100;
                            float percent2 = totalMoneySaleCoffee / totalMoney * 100;
                            float percent3 = totalMoneySaleRice / totalMoney * 100;
                            float percent4 = totalMoneySalePepper / totalMoney * 100;
                            float percent5 = totalMoneySaleTea / totalMoney * 100;
                            float percent6 = totalMoneySaleCashew / totalMoney * 100;
                            float percent7 = totalMoneySaleFruit / totalMoney * 100;
                            float percent8 = totalMoneySaleVegetables / totalMoney * 100;
                            float percent9 = totalMoneySaleTree / totalMoney * 100;
                            Log.e("hffhfhfff", percent1 + "" + percent7 + "" + percent6 + "" + percent8 + "");
                            Resources resources = getResources();
                            data.add(new GraphData(percent1, resources.getColor(R.color.color_1)));
                            data.add(new GraphData(percent2, resources.getColor(R.color.color_2)));
                            data.add(new GraphData(percent3, resources.getColor(R.color.color_3)));
                            data.add(new GraphData(percent4, resources.getColor(R.color.color_4)));
                            data.add(new GraphData(percent5, resources.getColor(R.color.color_5)));
                            data.add(new GraphData(percent6, resources.getColor(R.color.color_6)));
                            data.add(new GraphData(percent7, resources.getColor(R.color.color_7)));
                            data.add(new GraphData(percent8, resources.getColor(R.color.color_8)));
                            data.add(new GraphData(percent9, resources.getColor(R.color.color_9)));
                            graph.setData(data);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(StatisticalActivity.this, e.toString(), Toast.LENGTH_LONG).show();

                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StatisticalActivity.this, error.toString(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", ConstantData.getToken(getApplicationContext().getApplicationContext()));
                return headers;
            }
        };
        VolleySingleton.getInstance(StatisticalActivity.this).getRequestQueue().add(jsonObjectRequest);

    }
}