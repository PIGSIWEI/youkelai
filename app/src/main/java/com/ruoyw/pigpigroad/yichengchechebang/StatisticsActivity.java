package com.ruoyw.pigpigroad.yichengchechebang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.PaylayoutListviewAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.PigListview.PullToRefreshLayout;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.MyApplication;
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
 * Created by PIGROAD on 2018/3/15.
 */

public class StatisticsActivity extends AppCompatActivity {
    private LinearLayout back_ll;
    private LinearLayout ll_null;
    private String login_token;
    private ListView order_lv;
    private List<Map<String, Object>> dataList=new ArrayList<>();
    private PaylayoutListviewAdapter adapter;
    private PullToRefreshLayout ptrl;
    int totalcount,pagacount=1,itemcount;
    private TextView et_search;
    private String pay_type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_layout);
        pay_type=getIntent().getStringExtra("pay_type");
        init();
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
       //runThander();
    }

    @Override
    protected void onResume() {
        super.onResume();

           dataList.clear();
           postPayLayout();
    }

    /**
     * 初始化控件
     */
    private void init() {
        et_search=findViewById(R.id.et_search);
        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StatisticsActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        back_ll=findViewById(R.id.back_ll);
        ll_null=findViewById(R.id.ll_null);
        SharedPreferences sp = this.getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
        ptrl = ((PullToRefreshLayout) findViewById(R.id.refresh_view));
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        itemcount=adapter.getCount();
                        if (itemcount<totalcount){
                            Log.d(PP_TIP,"itemcount="+itemcount+"totalcount="+totalcount);
                            pagacount=pagacount+1;
                            postPayLayoutMore();
                            ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        }else {
                            Toast.makeText(StatisticsActivity.this,"不要再拉啦，已经到底了",Toast.LENGTH_SHORT).show();
                            Log.d(PP_TIP,"itemcount="+itemcount);
                            ptrl.loadmoreFinish(PullToRefreshLayout.FAIL);
                        }
                    }
                },2000);
            }
        });

    }
    public void initAdapter(){
        order_lv=findViewById(R.id.order_lv);
        adapter = new PaylayoutListviewAdapter(this, dataList, R.layout.paylayout_listview);
        adapter.notifyDataSetChanged();
        order_lv.setAdapter(adapter);
    }

    protected boolean enableSliding() {
        return true;
    }

    //支付订单的post请求
    public void postPayLayout() {
        Log.i("pppppppppppppp", RUOYU_URL + "?request=private.order.order.list.get&token=" + login_token + "&platform=android&pay_type="+pay_type);
        OkGo.<String>post(RUOYU_URL + "?request=private.order.order.list.get&token=" + login_token + "&platform=android&pay_type="+pay_type)
                .tag(this)
                .headers("header1", "headerValue1")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //注意这里已经是在主线程了
                        String responseStr = response.body();//这个就是返回来的结果
                        Log.i("ppppppppppppppppp", responseStr);
                        try {
                            JSONObject jsonObject = new JSONObject(responseStr);
                            int code = (int) jsonObject.get("code");
                            if (code == 0) {
                                JSONObject jsonList = jsonObject.getJSONObject("data");
                                final JSONArray jsonArray = jsonList.getJSONArray("data");
                                Log.i("pppppppppp", "开始写数据了！！");
                                //判断code的状态，如果code为0表示成功，1参数错误，999身份失效
                                totalcount=jsonList.getInt("total");
                                if (totalcount == 0){
                                    ll_null.setVisibility(View.VISIBLE);
                                    ptrl.setVisibility(View.GONE);
                                }else {
                                    dataList = new ArrayList<Map<String, Object>>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        final Map<String, Object> map = new HashMap<String, Object>();
                                        JSONObject temp = new JSONObject(jsonArray.getString((i)));
                                        //给Listview添加数据
                                        int img[] = {R.drawable.order_paylogo,R.drawable.order_paylogo2};
                                        map.put("gun_number", temp.getString("gun_id") + "号油枪");
                                        map.put("pay_money", temp.getString("oil_money") + "元");
                                        map.put("gun_kind", temp.getString("oil_name"));
                                        map.put("pay_time", temp.getString("pay_time"));
                                        map.put("orderid",temp.getString("orderid"));
                                        //订单详情
                                        map.put("orderid",temp.getString("orderid"));
                                        map.put("pay_type",temp.getInt("pay_type"));
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
                                    initAdapter();
                                }
                            } else if (code == 1) {
                                Toast.makeText(StatisticsActivity.this, "服务器错误，请联系管理员", Toast.LENGTH_SHORT).show();
                            } else if (code == 999) {
                                Toast.makeText(StatisticsActivity.this, "你的身份已过期，请重新登录", Toast.LENGTH_SHORT).show();
                                MyApplication.ExitClear(StatisticsActivity.this);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.i("ppppppppppppppppp", "连接失败!!!!!!!!!!!!!!!");
                        Toast.makeText(StatisticsActivity.this, "请检查你的网络设置", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    /**
     * 下拉获取更多
     */
    public void postPayLayoutMore() {
        OkGo.<String>post(RUOYU_URL + "?request=private.order.working.items.list.get&token=" + login_token + "&platform=android&page_size=24&curpage="+pagacount)
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
                                final JSONArray jsonArray = jsonList.getJSONArray("data");
                                Log.i("pppppppppp", "开始写数据了！！");
                                //判断code的状态，如果code为0表示成功，1参数错误，999身份失效
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    final Map<String, Object> map = new HashMap<String, Object>();
                                    JSONObject temp = new JSONObject(jsonArray.getString((i)));
                                    //给Listview添加数据
                                    int img[] = {R.drawable.order_paylogo,R.drawable.order_paylogo2};
                                    map.put("gun_number", temp.getString("gun_id") + "号油枪");
                                    map.put("pay_money", temp.getString("oil_money") + "元");
                                    map.put("gun_kind", temp.getString("oil_name"));
                                    map.put("pay_time", temp.getString("pay_time"));
                                    map.put("orderid",temp.getString("orderid"));
                                    //订单详情
                                    map.put("orderid",temp.getString("orderid"));
                                    map.put("pay_type",temp.getInt("pay_type"));
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
                                adapter.notifyDataSetChanged();
                            } else if (code == 1) {
                                Toast.makeText(StatisticsActivity.this, "服务器错误，请联系管理员", Toast.LENGTH_SHORT).show();
                            } else if (code == 999) {
                                Toast.makeText(StatisticsActivity.this, "你的身份已过期，请重新登录", Toast.LENGTH_SHORT).show();
                                MyApplication.ExitClear(StatisticsActivity.this);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.i("ppppppppppppppppp", "连接失败!!!!!!!!!!!!!!!");
                        Toast.makeText(StatisticsActivity.this, "请检查你的网络设置", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

