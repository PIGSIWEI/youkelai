package com.ruoyw.pigpigroad.yichengchechebang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhouwei.library.CustomPopWindow;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.PaylayoutListviewAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.DateWidget.CustomDatePicker;
import com.ruoyw.pigpigroad.yichengchechebang.SUNMI.BaseActivity;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

/**
 * Created by PIGROAD on 2018/6/12.
 * Email:920015363@qq.com
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout back_ll;
    private TextView start_time, end_time, start_time_hint, end_time_hint,et_orderid,wx_orderid;
    private CustomDatePicker customDatePicker, customDatePicker2;
    private Date dt1, dt2;
    private SimpleDateFormat df;
    private PaylayoutListviewAdapter adapter;
    private List<Map<String, Object>> dataList=new ArrayList<>();
    private Button btn_cancel,btn_time_select;
    private RelativeLayout select_start_time,select_end_time;

    private Spinner spinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
        init();
    }

    //初始化
    private void init() {
        btn_cancel=findViewById(R.id.btn_cancel);
        select_end_time=findViewById(R.id.select_end_time);
        select_start_time=findViewById(R.id.select_start_time);
        start_time=findViewById(R.id.start_time);
        end_time=findViewById(R.id.end_time);
        start_time_hint=findViewById(R.id.start_time_hint);
        end_time_hint=findViewById(R.id.end_time_hint);
        et_orderid=findViewById(R.id.et_orderid);
        wx_orderid=findViewById(R.id.wx_orderid);
        spinner=findViewById(R.id.spinner);
        btn_time_select=findViewById(R.id.btn_time_select);

        btn_cancel.setOnClickListener(this);
        select_end_time.setOnClickListener(this);
        select_start_time.setOnClickListener(this);
        btn_time_select.setOnClickListener(this);


        back_ll=findViewById(R.id.back_ll);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
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
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.select_start_time:
                initDatePicker();
                customDatePicker.show(start_time.getText().toString());
                break;
            case R.id.select_end_time:
                initDatePicker2();
                customDatePicker2.show(end_time.getText().toString());
                break;
            case R.id.btn_time_select:
                String starttime = start_time.getText().toString();
                String endtime = end_time.getText().toString();

                String spinner_tag=spinner.getSelectedItem().toString();
                String pay_method="";
                if (spinner_tag.equals("微信公众号")){
                    pay_method="wxgzh";
                }else if (spinner_tag.equals("支付宝")){
                    pay_method="alipay";
                }else if (spinner_tag.equals("微信小程序")){
                    pay_method="wxxcx";
                }else if (spinner_tag.equals("app")){
                    pay_method="app";
                }
                //确定时间后开始允许
                String orderid=et_orderid.getText().toString().trim();
                String wxid=wx_orderid.getText().toString().trim();
                Intent intent=new Intent(SearchActivity.this,SearchListActivity.class);
                intent.putExtra("order_id",orderid);
                intent.putExtra("transaction_id",wxid);
                intent.putExtra("pay_method",pay_method);
                if (start_time.getText().toString().equals("")){
                    intent.putExtra("pay_time_s","0");
                }else {
                    try {
                        dt1 = df.parse(starttime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    intent.putExtra("pay_time_s",starttime);
                }
                if (end_time.getText().toString().equals("")){
                    intent.putExtra("pay_time_e","0");
                }else {
                    try {
                        dt2 = df.parse(endtime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    intent.putExtra("pay_time_s",starttime);
                }
                startActivity(intent);
                break;
        }
    }
}
