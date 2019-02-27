package com.ruoyw.pigpigroad.yichengchechebang;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.PaylayoutListviewAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

/**
 * Created by PIGROAD on 2018/10/16
 * Email:920015363@qq.com
 */
public class SearchListActivity extends AppCompatActivity {

    private LinearLayout back_ll;
    private ListView listView;
    private PaylayoutListviewAdapter adapter;
    private List<Map<String, Object>> dataList = new ArrayList<>();
    private LinearLayout ll_null;
    private String login_token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_list_layout);
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
        this.init();
    }

    protected boolean enableSliding() {
        return true;
    }


    /**
     * 初始化
     */
    private void init() {
        listView = findViewById(R.id.list_view);
        ll_null = findViewById(R.id.ll_null);
        //listview 初始化
        adapter = new PaylayoutListviewAdapter(SearchListActivity.this, dataList, R.layout.paylayout_listview);
        listView.setAdapter(adapter);
        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
        back_ll = findViewById(R.id.back_ll);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        String orderid=getIntent().getStringExtra("order_id");
        String transaction_id=getIntent().getStringExtra("transaction_id");
        String pay_time_s=getIntent().getStringExtra("pay_time_s");
        String pay_time_e=getIntent().getStringExtra("pay_time_e");
        String pay_method=getIntent().getStringExtra("pay_method");
        Check(orderid,transaction_id,pay_time_s,pay_time_e,pay_method);
    }

    /**
     * 查询接口
     */
    private void Check(final String orderid, final String transaction_id, final String pay_time_s, final String pay_time_e, final String pay_method) {
        dataList.clear();
        OkGo.<String>post(RUOYU_URL + "?request=private.order.search.store.list.get&token=" + login_token + "&platform=android&orderid=" + orderid + "&transaction_id=" + transaction_id + "&pay_time_s=" + pay_time_s + "&pay_time_e=" + pay_time_e +"&pay_method="+pay_method)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String Str = response.body();
                        try {
                            Log.i(PP_TIP,RUOYU_URL + "?request=private.order.search.store.list.get&token=" + login_token + "&platform=android&orderid=" + orderid + "&transaction_id=" + transaction_id + "&pay_time_s=" + pay_time_s + "&pay_time_e=" + pay_time_e +"&pay_method="+pay_method);
                            Log.i(PP_TIP,response.body());
                            JSONObject jsonObject = new JSONObject(Str);
                            int code = (int) jsonObject.get("code");
                            if (code == 0) {
                                JSONObject jsonList = jsonObject.getJSONObject("data");
                                int total=jsonList.getInt("total");
                                if (total == 0){
                                    listView.setVisibility(View.GONE);
                                    ll_null.setVisibility(View.VISIBLE);
                                }else {
                                    final JSONArray jsonArray = jsonList.getJSONArray("data");
                                    Log.i("pppppppppp", "RES！！"+RUOYU_URL + "?request=private.order.search.store.list.get&token=" + login_token + "&platform=android&orderid=" + orderid + "&transaction_id=" + transaction_id + "&pay_time_s=" + pay_time_s + "&pay_time_e=" + pay_time_e +"&pay_method="+pay_method);
                                    //判断code的状态，如果code为0表示成功，1参数错误，999身份失效
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        final Map<String, Object> map = new HashMap<String, Object>();
                                        JSONObject temp = new JSONObject(jsonArray.getString((i)));
                                        //给Listview添加数据
                                        int img[] = {R.drawable.order_paylogo, R.drawable.order_paylogo2};
                                        map.put("gun_number", temp.getString("gun_id") + "号油枪");
                                        map.put("pay_money", temp.getString("oil_money") + "元");
                                        map.put("gun_kind", temp.getString("oil_name"));
                                        map.put("pay_time", temp.getString("pay_time"));
                                        map.put("orderid",temp.getString("orderid"));
                                        //订单详情

                                        map.put("pay_type",temp.getInt("pay_type"));
                                        map.put("orderid",temp.getString("orderid"));
                                        map.put("pay_time",temp.getString("pay_time"));
                                        map.put("order_time",temp.getString("order_time"));
                                        map.put("name",temp.getString("name"));
                                        map.put("merchant_oil_price",temp.getString("merchant_oil_price"));
                                        map.put("use_oil_price",temp.getString("use_oil_price"));
                                        map.put("oil_money",temp.getString("oil_money"));
                                        map.put("coupon_money",temp.getString("coupon_money"));
                                        map.put("gun_id",temp.getString("gun_id"));
                                        map.put("oil_name",temp.getString("oil_name"));
                                        map.put("oil_lit",temp.getString("oil_lit"));
                                        map.put("pay_money",temp.getString("pay_money"));
                                        map.put("transaction_id",temp.getString("transaction_id"));
                                        int order_state = temp.getInt("order_status");
                                        map.put("order_status",order_state);
                                        // 3表示成功,-1表示退款
                                        if (order_state == 3) {
                                            map.put("pay_state", img[0]);
                                        } else if (order_state == -1) {
                                            map.put("pay_state", img[1]);
                                        }
                                        dataList.add(map);
                                    }
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
