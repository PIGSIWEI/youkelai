package com.ruoyw.pigpigroad.yichengchechebang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.ExchangeAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.DateWidget.CustomDatePicker;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.MyApplication;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

/**
 * Created by PIGROAD on 2018/3/15.
 */

public class ExchangeActivity extends AppCompatActivity {
    private LinearLayout back_ll, exchang_add_btn, time_picket_ll,exchang_linearlayout;
    private CustomPopWindow PayPopWindow;
    private String login_token;
    private TextView daytime_tv, usepoint_tv, allcount_tv, wx_count, wx_point;
    private ListView exchang_listview, today_listview;
    private ExchangeAdapter adapter, adapter2;
    private List<Map<String, Object>> dataList;
    private CardView cardView;
    private LinearLayout select_start_time, select_end_time;
    private TextView start_time, end_time, start_time_hint, end_time_hint;
    private CustomDatePicker customDatePicker, customDatePicker2;
    private Date dt1, dt2, dt3;
    private DateFormat df;
    private SimpleDateFormat simpledf;
    private RelativeLayout btn_todaycheck,empty_ll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.exchange_layout);
        back_ll = findViewById(R.id.back_ll);
        df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        btn_todaycheck = findViewById(R.id.btn_todaycheck);
        time_picket_ll = findViewById(R.id.time_picket_ll);
        today_listview = findViewById(R.id.today_listview);
        exchang_linearlayout=findViewById(R.id.exchang_linearlayout);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        exchang_add_btn = findViewById(R.id.exchange_add_btn);
        exchang_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ExchangeAddActivity.class);
                startActivity(intent);
            }
        });
        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
        time_picket_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View contentView = LayoutInflater.from(ExchangeActivity.this).inflate(R.layout.time_picket_layout, null);
                //给时间选择页面绑定窗口
                timeLayout(contentView);
                //弹出时间选择页面
                PayPopWindow = new CustomPopWindow.PopupWindowBuilder(ExchangeActivity.this)
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
        init();//初始化控件
        btn_todaycheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
//                final TDialog tDialog2 = new TDialog.Builder(getSupportFragmentManager())
//                        .setLayoutRes(R.layout.login_dialog)                        //弹窗布局
//                        .setScreenWidthAspect(ExchangeActivity.this, 0.9f)               //屏幕宽度比
//                        .setDimAmount(0f)                                                    //设置焦点
//                        .create()
//                        .show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        tDialog2.dismiss();
                        postTodayUsed();

                    }
                }, 2000);
            }
        });
    }

    protected boolean enableSliding() {
        return true;
    }

    /**
     * 数据初始化
     */
    private void init() {
        simpledf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        daytime_tv = findViewById(R.id.daytime_tv);
        usepoint_tv = findViewById(R.id.usepoint_tv);
        allcount_tv = findViewById(R.id.allcount_tv);
        cardView = findViewById(R.id.cardview);
        wx_count = findViewById(R.id.wx_count);
        wx_point = findViewById(R.id.wx_point);
        empty_ll=findViewById(R.id.empty_ll);
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
        String gettime = simpledf.format(new Date());
        daytime_tv.setText(gettime);
        new Handler().postDelayed(new Runnable() {
//            final TDialog tDialog=new TDialog.Builder(getSupportFragmentManager())
//                    .setLayoutRes(R.layout.login_dialog)                        //弹窗布局
//                    .setScreenWidthAspect(ExchangeActivity.this, 0.9f)               //屏幕宽度比
//                    .setDimAmount(0f)                                                    //设置焦点
//                    .create()
//                    .show();
            @Override
            public void run() {
//                tDialog.dismiss();
                postExchang();
            }
        },1000);
    }

    /**
     * 搜索适配器的填充
     */
    public void initAdapter() {
        //适配器的填充
        exchang_listview = findViewById(R.id.exchang_listview);
        adapter = new ExchangeAdapter(ExchangeActivity.this, dataList, R.layout.exchange_listview);
        adapter.notifyDataSetChanged();
        exchang_listview.setAdapter(adapter);
    }

    public void initAdapter2() {
        //适配器的填充
        today_listview = findViewById(R.id.today_listview);
        adapter2 = new ExchangeAdapter(ExchangeActivity.this, dataList, R.layout.exchange_listview);
        adapter2.notifyDataSetChanged();
        today_listview.setAdapter(adapter2);
    }

    /**
     * 获取当前积分数据
     */
    public void postExchang() {
        OkGo.<String>post(RUOYU_URL + "?request=private.point.day.total.point.get&token=" + login_token + "&platform=android")
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
                                JSONArray jsondata = jsonList.getJSONArray("data");
                                Log.i(PP_TIP,"jsondata="+jsondata);
                               if (jsondata.length()==0){
                                   Toast.makeText(ExchangeActivity.this,"当天没有数据！",Toast.LENGTH_SHORT).show();
                                   exchang_linearlayout.setVisibility(View.GONE);
                                   empty_ll.setVisibility(View.VISIBLE);
                               }else {
                                   JSONObject temp = new JSONObject(jsondata.getString((0)));
                                   JSONObject temp1 = new JSONObject(jsondata.getString((1)));
                                   usepoint_tv.setText("今日兑换积分:" + temp.getString("use_point"));
                                   allcount_tv.setText("交易笔数:" + temp.getString("allcount"));
                                   wx_point.setText("今日获得积分:" + temp1.getString("get_point"));
                                   wx_count.setText("交易笔数:" + temp1.getString("allcount"));
                               }
                            } else if (code == 1) {
                                Toast.makeText(ExchangeActivity.this, "服务器错误，请联系管理员", Toast.LENGTH_SHORT).show();
                            } else if (code == 999) {
                                Toast.makeText(ExchangeActivity.this, "你的身份已过期，请重新登录", Toast.LENGTH_SHORT).show();
                                MyApplication.ExitClear(ExchangeActivity.this);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.i("ppppppppppppppppp", "连接失败!!!!!!!!!!!!!!!");
                        Toast.makeText(ExchangeActivity.this, "请检查你的网络设置", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 通过日期查询门店数据
     */
    public void postPhoneExchang() {
        OkGo.<String>post(RUOYU_URL + "?request=private.point.user.point.list.get&token=" + login_token + "&platform=android&phone=18667676767")
                .tag(this)
                .headers("header1", "headerValue1")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String responseStr = response.body();//这个就是返回来的结果
                        try {
                            Log.i("ppppppppppbbbbbbbbbbb", responseStr);
                            JSONObject jsonObject = new JSONObject(responseStr);
                            int code = (int) jsonObject.get("code");
                            if (code == 0) {
                                JSONObject jsonList = jsonObject.getJSONObject("data");
                                JSONArray jsondata = jsonList.getJSONArray("data");
                                dataList = new ArrayList<Map<String, Object>>();
                                int total = (int) jsonList.get("total");
                                for (int i = 0; i < jsondata.length(); i++) {
                                    final Map<String, Object> map = new HashMap<String, Object>();
                                    JSONObject temp = new JSONObject(jsondata.getString((i)));
                                    dt3 = df.parse(temp.getString("addtime"));
                                    if ((dt1.getTime() < dt3.getTime()) && (dt3.getTime() < dt2.getTime())) {
                                        map.put("addtime", temp.getString("addtime"));
                                        map.put("type_name", temp.getString("type_name"));
                                        map.put("orderid", temp.getString("orderid"));
                                        map.put("type_name", temp.getString("type_name"));
                                        map.put("point_banlace", temp.getString("point_banlace"));
                                        map.put("point", temp.getString("point"));
                                        dataList.add(map);
                                    }
                                }
                                if (dataList.isEmpty()) {
                                    Toast.makeText(getApplication(), "当前期间没有任何数据，请确定后查询！", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    initAdapter();
                                    today_listview.setVisibility(View.GONE);
                                    exchang_listview.setVisibility(View.VISIBLE);
                                    cardView.setVisibility(View.GONE);
                                }
                            } else if (code == 1) {
                                Toast.makeText(ExchangeActivity.this, "服务器错误，请联系管理员", Toast.LENGTH_SHORT).show();
                            } else if (code == 999) {
                                Toast.makeText(ExchangeActivity.this, "你的身份已过期，请重新登录", Toast.LENGTH_SHORT).show();
                                MyApplication.ExitClear(ExchangeActivity.this);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 查询当天使用记录
     */
    public void postTodayUsed() {
        OkGo.<String>post(RUOYU_URL + "?request=private.point.user.point.list.get&token=" + login_token + "&platform=android&page_size=24&curpage=1&day=" + (daytime_tv.getText()))
                .tag(this)
                .headers("header1", "headerValue1")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String responseStr = response.body();//这个就是返回来的结果
                        try {
                            JSONObject jsonObject = new JSONObject(responseStr);
                            int code = (int) jsonObject.get("code");
                            if (code == 0) {
                                JSONObject jsonList = jsonObject.getJSONObject("data");
                                JSONArray jsondata = jsonList.getJSONArray("data");
                                dataList = new ArrayList<Map<String, Object>>();
                                    for (int i = 0; i < jsondata.length(); i++) {
                                        final Map<String, Object> map = new HashMap<String, Object>();
                                        JSONObject temp = new JSONObject(jsondata.getString((i)));
                                        map.put("addtime", temp.getString("addtime"));
                                        map.put("type_name", temp.getString("type_name"));
                                        map.put("orderid", temp.getString("orderid"));
                                        map.put("type_name", temp.getString("type_name"));
                                        map.put("point_banlace", temp.getString("point_banlace"));
                                        map.put("point", temp.getString("point"));
                                        dataList.add(map);
                                    }
                                    Log.i("ppppppppp", "111111111");
                                if (dataList.isEmpty()){
                                    Toast.makeText(ExchangeActivity.this,"当天没有数据！",Toast.LENGTH_SHORT).show();
                                    exchang_linearlayout.setVisibility(View.GONE);
                                    empty_ll.setVisibility(View.VISIBLE);
                                }else {
                                    initAdapter2();
                                    today_listview.setVisibility(View.VISIBLE);
                                }
                            } else if (code == 1) {
                                Toast.makeText(ExchangeActivity.this, "服务器错误，请联系管理员", Toast.LENGTH_SHORT).show();
                            } else if (code == 999) {
                                Toast.makeText(ExchangeActivity.this, "你的身份已过期，请重新登录", Toast.LENGTH_SHORT).show();
                                MyApplication.ExitClear(ExchangeActivity.this);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    /**
     * 时间管理器
     *
     * @param contentView
     */
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
//                                final TDialog tDialog = new TDialog.Builder(getSupportFragmentManager())
//                                        .setLayoutRes(R.layout.login_dialog)                        //弹窗布局
//                                        .setScreenWidthAspect(ExchangeActivity.this, 0.9f)               //屏幕宽度比
//                                        .setDimAmount(0f)                                                    //设置焦点
//                                        .create()
//                                        .show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        postPhoneExchang();
//                                        tDialog.dismiss();
                                        PayPopWindow.dissmiss();
                                    }
                                }, 2000);
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

}

