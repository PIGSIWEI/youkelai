package com.ruoyw.pigpigroad.yichengchechebang;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.PointSearchAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.Bean.PointSearchBean;
import com.ruoyw.pigpigroad.yichengchechebang.DateWidget.CustomDatePicker;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

public class PointSearchActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout back_ll;
    private String login_token;
    private RelativeLayout select_start_time, select_end_time;
    private TextView start_time_hint, end_time_hint, start_time, end_time;
    private CustomDatePicker customDatePicker, customDatePicker2;
    private EditText et_number;
    private Button btn_search;
    private RecyclerView recyclerView;
    private LinearLayout ll_search;
    private Spinner spinner;

    private PointSearchAdapter adapter;
    private List<PointSearchBean> datas=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.point_search_layout);
        this.init();
    }

    /**
     * 初始化
     */
    private void init() {

        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
        back_ll = findViewById(R.id.back_ll);
        spinner = findViewById(R.id.spinner);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);

        select_end_time = findViewById(R.id.select_end_time);
        select_start_time = findViewById(R.id.select_start_time);
        select_start_time.setOnClickListener(this);
        select_end_time.setOnClickListener(this);
        start_time_hint = findViewById(R.id.start_time_hint);
        end_time_hint = findViewById(R.id.end_time_hint);
        start_time = findViewById(R.id.start_time);
        end_time = findViewById(R.id.end_time);
        et_number = findViewById(R.id.et_number);
        btn_search = findViewById(R.id.btn_search);
        ll_search = findViewById(R.id.ll_search);
        recyclerView = findViewById(R.id.recycler_view);
        btn_search.setOnClickListener(this);

        adapter=new PointSearchAdapter(this,datas);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    protected boolean enableSliding() {
        return true;
    }

    /**
     * 时间选择器的初始化
     */
    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        start_time.setText(now);
        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                start_time.setText(time);
                start_time.setVisibility(View.VISIBLE);
                start_time_hint.setVisibility(View.INVISIBLE);
            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 显示时和分
        customDatePicker.setIsLoop(false); // 允许循环滚动
    }

    private void initDatePicker2() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        end_time.setText(now);
        customDatePicker2 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                end_time.setText(time);
                end_time.setVisibility(View.VISIBLE);
                end_time_hint.setVisibility(View.INVISIBLE);
            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker2.showSpecificTime(false); // 显示时和分
        customDatePicker2.setIsLoop(false); // 允许循环滚动
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_start_time:
                initDatePicker();
                customDatePicker.show(start_time.getText().toString());
                break;
            case R.id.select_end_time:
                initDatePicker2();
                customDatePicker2.show(end_time.getText().toString());
                break;
            case R.id.btn_search:
                String strattime = start_time.getText().toString();
                String endtime = end_time.getText().toString();
                String phone = et_number.getText().toString();
                String type = spinner.getSelectedItem().toString();
                int type_id = 101;
                if (strattime.equals("")) {
                    strattime = "";
                }
                if (endtime.equals("")) {
                    endtime = "";
                }
                switch (type) {
                    case "卡积分归集":
                        type_id=101;
                        break;
                    case "购物":
                        type_id=102;
                        break;
                    case "赠送":
                        type_id=103;
                        break;
                }
                checkData(strattime, endtime, phone,type_id);
                break;
        }
    }

    /**
     * 搜索查询
     */
    private void checkData(String start_time, String end_time, String phone, int type_id) {
        Log.i(PP_TIP,RUOYU_URL + "?request=private.point.user.point.list.get&token=" + login_token + "&platform=android&page_size=24&curpage=1&start_time=" + start_time + "&over_time=" + end_time + "&phone=" + phone + "&type_id=" + type_id);
        OkGo.<String>post(RUOYU_URL + "?request=private.point.user.point.list.get&token=" + login_token + "&platform=android&page_size=24&curpage=1&start_time=" + start_time + "&over_time=" + end_time + "&phone=" + phone + "&type_id=" + type_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            int code = jsonObject.getInt("code");
                            JSONArray data=jsonObject.getJSONArray("data");
                            if (data.length()<=0){
                                Toast.makeText(PointSearchActivity.this,"当前搜索不到任何记录",Toast.LENGTH_SHORT).show();
                            }else {
                                ll_search.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                for (int i=0;i<data.length();i++){
                                    JSONObject temp=data.getJSONObject(i);
                                    PointSearchBean bean=new PointSearchBean();
                                    bean.setAddtime(temp.getString("addtime"));
                                    bean.setPhone(temp.getString("phone"));
                                    bean.setPoint(temp.getInt("point"));
                                    bean.setPoint_banlace(temp.getString("point_banlace"));
                                    bean.setType_name(temp.getString("type_name"));
                                    bean.setUserid(temp.getString("userid"));
                                    datas.add(bean);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


}
