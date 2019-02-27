package com.ruoyw.pigpigroad.yichengchechebang;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.CheckTodayAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.MyApplication;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

/**
 * Created by XYSM on 2018/4/7.
 */

public class CheckTodayActivity extends AppCompatActivity {
    private String login_token;
    private ListView ctd_lv;
    private CheckTodayAdapter adapter;
    private List<Map<String, Object>> dataList;
    private LinearLayout ll_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.check_today_detail_layout);
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
        ctd_lv=findViewById(R.id.ctd_lv);
        postCheckTodayDetail();
        ll_back=findViewById(R.id.back_ll);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    protected boolean enableSliding() {
        return true;
    }

    public void postCheckTodayDetail() {
        OkGo.<String>post(RUOYU_URL + "?request=private.refund.order.refund.list.get&token=" + login_token + "&platform=android&jiaoban_no=0")
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
                                JSONArray jsonArray = jsonList.getJSONArray("data");
                                dataList = new ArrayList<Map<String, Object>>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    final Map<String, Object> map = new HashMap<String, Object>();
                                    JSONObject temp = new JSONObject(jsonArray.getString((i)));
                                    map.put("orderid", temp.getString("orderid"));
                                    map.put("order_time", temp.getString("order_time"));
                                    map.put("pay_time", temp.getString("pay_time"));
                                    map.put("pay_money", temp.getString("pay_money"));
                                    map.put("who_discount", temp.getString("who_discount"));
                                    dataList.add(map);
                                }
                                initAdapter();
                            }else if (code == 1) {
                                Toast.makeText(CheckTodayActivity.this, "服务器错误，请联系管理员", Toast.LENGTH_SHORT).show();
                            } else if (code == 999) {
                                Toast.makeText(CheckTodayActivity.this, "你的身份已过期，请重新登录", Toast.LENGTH_SHORT).show();
                                MyApplication.ExitClear(CheckTodayActivity.this);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.i("ppppppppppppppppp", "连接失败!!!!!!!!!!!!!!!");
                        Toast.makeText(CheckTodayActivity.this, "请检查你的网络设置", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    /**
     * 填充适配器
     */
    private void initAdapter() {
        adapter = new CheckTodayAdapter(CheckTodayActivity.this, dataList, R.layout.check_today_detail_listview);
        adapter.notifyDataSetChanged();
        ctd_lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
