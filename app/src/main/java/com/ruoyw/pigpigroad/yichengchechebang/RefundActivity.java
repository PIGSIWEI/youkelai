package com.ruoyw.pigpigroad.yichengchechebang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhouwei.library.CustomPopWindow;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.RefundRecordAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.DateWidget.CustomDatePicker;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.MyApplication;
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
 * Created by PIGROAD on 2018/3/15.
 */
public class RefundActivity extends AppCompatActivity {
    private CustomPopWindow PayPopWindow;
    private TextView start_time, end_time, start_time_hint, end_time_hint, daytime_tv;
    private CustomDatePicker customDatePicker, customDatePicker2;
    private Date dt1, dt2;
    private SimpleDateFormat df;
    private LinearLayout back_ll;
    private LinearLayout refund_record_btn, time_picket_ll, refund_view;
    private String login_token;
    private List<Map<String, Object>> dataList;
    private ListView refun_listview;
    private RefundRecordAdapter adapter;
    private TextView refund_money, refund_count, order_refund_money, order_refund_count;
    private RelativeLayout empty_ll, check_today_detail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.refund_layout);
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
        init();
    }

    private void init() {
        check_today_detail = findViewById(R.id.check_today_detail);
        empty_ll = findViewById(R.id.empty_ll);
        refund_view = findViewById(R.id.refund_view);
        back_ll = findViewById(R.id.back_ll);
        daytime_tv = findViewById(R.id.daytime_tv);
        refun_listview = findViewById(R.id.refund_listview);
        refund_record_btn = findViewById(R.id.refund_record_btn);
        refund_record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RefundActivity.this, RefundRecordActivity.class);
                startActivity(intent);
            }
        });
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        time_picket_ll = findViewById(R.id.time_picket_ll);
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String gettime = df.format(new Date());
        daytime_tv.setText(gettime);
        time_picket_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View contentView = LayoutInflater.from(RefundActivity.this).inflate(R.layout.time_picket_layout2, null);
                //给时间选择页面绑定窗口
                timeLayout(contentView);
                //弹出时间选择页面
                PayPopWindow = new CustomPopWindow.PopupWindowBuilder(RefundActivity.this)
                        .setView(contentView)
                        .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                        //支付页面消失时候回调
                        .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {

                            }
                        })
                        .create()
                        .showAsDropDown(time_picket_ll);
            }
        });
        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
        refund_count = findViewById(R.id.refund_count);
        refund_money = findViewById(R.id.refund_money);
        order_refund_count = findViewById(R.id.order_refund_count);
        order_refund_money = findViewById(R.id.order_refund_money);
//        new Handler().postDelayed(new Runnable() {
//            final TDialog tDialog = new TDialog.Builder(getSupportFragmentManager())
//                    .setLayoutRes(R.layout.login_dialog)                        //弹窗布局
//                    .setScreenWidthAspect(RefundActivity.this, 0.9f)               //屏幕宽度比
//                    .setDimAmount(0f)                                                    //设置焦点
//                    .create()
//                    .show();
//
//            @Override
//            public void run() {
//                tDialog.dismiss();
//                postTodayRecord();
//            }
//        }, 2000);
        check_today_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RefundActivity.this,CheckTodayActivity.class);
                startActivity(intent);
            }
        });
    }

    private void timeLayout(View contentView) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_cancel:
                        PayPopWindow.dissmiss();
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
                        try {
                            dt1 = df.parse(starttime);
                            dt2 = df.parse(endtime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (starttime.equals("") || endtime.equals("")) {
                            Toast.makeText(view.getContext(), "请正确选择时间！", Toast.LENGTH_SHORT).show();
                        } else {
                            if (dt1.getTime() > dt2.getTime()) {
                                Toast.makeText(view.getContext(), "结束时间不能大于开始时间！请正确选择时间！", Toast.LENGTH_SHORT).show();
                            } else {
                                //确定时间后开始允许
                                Toast.makeText(view.getContext(), "succes！", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                }
            }
        };
        contentView.findViewById(R.id.btn_cancel).setOnClickListener(listener);
        contentView.findViewById(R.id.select_end_time).setOnClickListener(listener);
        contentView.findViewById(R.id.select_start_time).setOnClickListener(listener);
        contentView.findViewById(R.id.btn_time_select).setOnClickListener(listener);
        start_time = contentView.findViewById(R.id.start_time);
        end_time = contentView.findViewById(R.id.end_time);
        start_time_hint = contentView.findViewById(R.id.start_time_hint);
        end_time_hint = contentView.findViewById(R.id.end_time_hint);
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

    /**
     * 加载当天数据
     */
    public void postTodayRecord() {
        OkGo.<String>post(RUOYU_URL + "?request=private.refund.order.refund.tj.get&token=" + login_token + "&platform=android&day=" + (daytime_tv.getText()))
                .tag(this)
                .headers("header1", "headerValue1")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //注意这里已经是在主线程了
                        String responseStr = response.body();//这个就是返回来的结果
                        try {
                            JSONObject jsonObject = new JSONObject(responseStr);
                            int code = (int) jsonObject.get("code");
                            if (code == 0) {
                                JSONObject jsonList = jsonObject.getJSONObject("data");
                                JSONObject refund = jsonList.getJSONObject("refund");
                                refund_count.setText(refund.getString("count"));
                                refund_money.setText(refund.getString("refund_money"));
                                JSONObject other_refund = jsonList.getJSONObject("other_refund");
                                order_refund_count.setText(other_refund.getString("count"));
                                order_refund_money.setText(other_refund.getString("refund_money"));
                                JSONArray jsonArray = jsonList.getJSONArray("refund_total");
                                dataList = new ArrayList<Map<String, Object>>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    final Map<String, Object> map = new HashMap<String, Object>();
                                    JSONObject temp = new JSONObject(jsonArray.getString((i)));
                                    map.put("count", temp.getString("count"));
                                    map.put("oil_name", temp.getString("oil_name"));
                                    map.put("order_money", temp.getString("order_money"));
                                    map.put("oil_lit", temp.getString("oil_lit"));
                                    dataList.add(map);
                                }
                                initAdapter();
                            } else if (code == 1) {
                                Toast.makeText(RefundActivity.this, "服务器错误，请联系管理员", Toast.LENGTH_SHORT).show();
                            } else if (code == 999) {
                                Toast.makeText(RefundActivity.this, "你的身份已过期，请重新登录", Toast.LENGTH_SHORT).show();
                                MyApplication.ExitClear(RefundActivity.this);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RefundActivity.this, "当天没有数据", Toast.LENGTH_SHORT).show();
                            empty_ll.setVisibility(View.VISIBLE);
                            refund_view.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.i("ppppppppppppppppp", "连接失败!!!!!!!!!!!!!!!");
                        Toast.makeText(RefundActivity.this, "请检查你的网络设置", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 适配器初始化
     */
    public void initAdapter() {
        adapter = new RefundRecordAdapter(RefundActivity.this, dataList, R.layout.refund_record_listview);
        adapter.notifyDataSetChanged();
        refun_listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}

