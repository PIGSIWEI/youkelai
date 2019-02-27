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
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.ClassRecordAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.PigListview.PullToRefreshLayout;
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
 * Created by PIGROAD on 2018/3/16.
 */

public class ClassRecordActivity extends AppCompatActivity {
    private LinearLayout back_ll;
    private List<Map<String, Object>> dataList=null;
    private ListView recycler_view;
    private String login_token;
    private ClassRecordAdapter adapter;
    private static Runnable add;
    int alltotal,itemcount,pagacount=1;
    private PullToRefreshLayout ptrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_record_layout);
        ptrl = ((PullToRefreshLayout) findViewById(R.id.refresh_view));
        back_ll = findViewById(R.id.back_ll);
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //加载历史记录
                        itemcount=recycler_view.getCount();
                        if (itemcount < alltotal){
                            pagacount=pagacount+1;
                            postClassRecordMore();
                            ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        }else{
                            Toast.makeText(ClassRecordActivity.this,"已经最低了，不要再拉了",Toast.LENGTH_SHORT).show();
                            ptrl.loadmoreFinish(PullToRefreshLayout.FAIL);
                        }
                    }
                },2000); //1秒后
            }
        });
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //初始化控件
        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
        postClassRecord();
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
//        runThander();
    }
    protected boolean enableSliding() {
        return true;
    }

    /**
     * 初始化控件
     */

    private void init() {
        recycler_view = findViewById(R.id.recycler_view);
        adapter = new ClassRecordAdapter(ClassRecordActivity.this, dataList, R.layout.class_record_listview,login_token);
        recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

//    /**
//     * 初始化
//     */
//    public void runThander(){
//        new Handler().postDelayed(new Runnable() {
//            final TDialog tDialog=new TDialog.Builder(getSupportFragmentManager())
//                    .setCancelOutside(false)
//                    .setLayoutRes(R.layout.login_dialog)                        //弹窗布局
//                    .setScreenWidthAspect(ClassRecordActivity.this, 0.9f)               //屏幕宽度比
//                    .setDimAmount(0f)                                                    //设置焦点
//                    .create()
//                    .show();
//            @Override
//            public void run() {
//                tDialog.dismiss();
//            }
//        },1200);
//    }

    /**
     * 第一次加载listview数据
     */
    public void postClassRecord() {
        OkGo.<String>post(RUOYU_URL + "?request=private.order.history.jiaoban.list.get&token=" + login_token + "&platform=android&jiaoban_no=1514822379&page_size=24&curpage=1")
                .tag(this)
                .headers("header1", "headerValue1")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String responseStr = response.body();
                        Log.i("pppppppppppppppp", responseStr);
                        try {
                            JSONObject jsonObject = new JSONObject(responseStr);
                            int code = (int) jsonObject.get("code");
                            if (code == 0) {
                                JSONObject jsonData = jsonObject.getJSONObject("data");
                                alltotal=jsonData.getInt("total");
                                JSONArray jsonArray = jsonData.getJSONArray("data");
                                Log.i("ppppppppppjsonArray", String.valueOf(jsonArray));
                                dataList = new ArrayList<Map<String, Object>>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    JSONObject usedtemp = new JSONObject(jsonArray.getString((i)));
                                    map.put("addtime", usedtemp.getString("addtime"));
                                    map.put("jiaoban_no", usedtemp.getString("id"));
                                    map.put("jiaoban_name",usedtemp.getString("jiaoban_name"));
                                    map.put("over_time",usedtemp.getString("over_time"));
                                    map.put("start_time",usedtemp.getString("start_time"));
                                    map.put("order_other_money",usedtemp.getString("order_other_money"));
                                    //map.put("order_oil_money",usedtemp.getString("order_oil_money"));
                                    map.put("order_oil_lit",usedtemp.getString("order_oil_lit"));
                                    map.put("order_coupon_total",usedtemp.getString("order_coupon_total"));
                                    map.put("store_name",usedtemp.getString("store_name"));
                                    map.put("order_money",usedtemp.getString("order_money"));
                                    map.put("order_pay_money",usedtemp.getString("order_pay_money"));
                                    map.put("order_count",usedtemp.getString("order_count"));
                                    map.put("order_return_pay_money",usedtemp.getString("refund_pay_money"));
                                    map.put("order_return_count",usedtemp.getString("refund_count"));
                                    map.put("order_discount_money",usedtemp.getString("refund_discount_money"));
                                    dataList.add(map);
                                }
                                init();

                            } else if (code == 1) {
                                Toast.makeText(ClassRecordActivity.this, "服务器错误，请联系管理员", Toast.LENGTH_SHORT).show();
                            } else if (code == 999) {
                                Toast.makeText(ClassRecordActivity.this, "你的身份已过期，请重新登录", Toast.LENGTH_SHORT).show();
                                SharedPreferences sp = ClassRecordActivity.this.getSharedPreferences("LoginUser", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.clear();
                                editor.commit();
                                Intent intent = new Intent(ClassRecordActivity.this, MainActivity.class);
                                startActivity(intent);
                                ClassRecordActivity.this.finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.i("ppppppppppppppppp", "连接失败!!!!!!!!!!!!!!!");
                        Toast.makeText(ClassRecordActivity.this, "请检查你的网络设置", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 下拉加载数据
     */
    public void postClassRecordMore(){
        OkGo.<String>post(RUOYU_URL + "?request=private.order.history.jiaoban.list.get&token=" + login_token + "&platform=android&jiaoban_no=1514822379&page_size=24&curpage="+pagacount)
                .tag(this)
                .headers("header1", "headerValue1")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String responseStr = response.body();
                        Log.i("pppppppppppppppp", responseStr);
                        try {
                            JSONObject jsonObject = new JSONObject(responseStr);
                            int code = (int) jsonObject.get("code");
                            if (code == 0) {
                                JSONObject jsonData = jsonObject.getJSONObject("data");
                                alltotal=jsonData.getInt("total");
                                JSONArray jsonArray = jsonData.getJSONArray("data");
                                Log.i("ppppppppppjsonArray", String.valueOf(jsonArray));
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    JSONObject usedtemp = new JSONObject(jsonArray.getString((i)));
                                    map.put("addtime", usedtemp.getString("addtime"));
                                    map.put("jiaoban_no", usedtemp.getString("jiaoban_no"));
                                    //
                                    map.put("jiaoban_name",usedtemp.getString("jiaoban_name"));
                                    map.put("over_time",usedtemp.getString("over_time"));
                                    map.put("start_time",usedtemp.getString("start_time"));
                                    map.put("order_other_money",usedtemp.getString("order_other_money"));
                                    map.put("order_oil_money",usedtemp.getString("order_oil_money"));
                                    map.put("order_oil_lit",usedtemp.getString("order_oil_lit"));
                                    map.put("order_coupon_total",usedtemp.getString("order_coupon_total"));
                                    map.put("store_name",usedtemp.getString("store_name"));
                                    map.put("order_money",usedtemp.getString("order_money"));
                                    map.put("order_pay_money",usedtemp.getString("order_pay_money"));
                                    map.put("order_count",usedtemp.getString("order_count"));
                                    map.put("order_return_pay_money",usedtemp.getString("order_return_pay_money"));
                                    map.put("order_return_count",usedtemp.getString("order_return_count"));
                                    map.put("order_discount_money",usedtemp.getString("order_discount_money"));
                                    dataList.add(map);
                                }
                                adapter.notifyDataSetChanged();
                            } else if (code == 1) {
                                Toast.makeText(ClassRecordActivity.this, "服务器错误，请联系管理员", Toast.LENGTH_SHORT).show();
                            } else if (code == 999) {
                                Toast.makeText(ClassRecordActivity.this, "你的身份已过期，请重新登录", Toast.LENGTH_SHORT).show();
                                SharedPreferences sp = ClassRecordActivity.this.getSharedPreferences("LoginUser", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.clear();
                                editor.commit();
                                Intent intent = new Intent(ClassRecordActivity.this, MainActivity.class);
                                startActivity(intent);
                                ClassRecordActivity.this.finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.i("ppppppppppppppppp", "连接失败!!!!!!!!!!!!!!!");
                        Toast.makeText(ClassRecordActivity.this, "请检查你的网络设置", Toast.LENGTH_SHORT).show();
                    }
                });
    }




}

