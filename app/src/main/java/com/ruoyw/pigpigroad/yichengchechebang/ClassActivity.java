package com.ruoyw.pigpigroad.yichengchechebang;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.SUNMI.util.AidlUtil;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;
import com.ruoyw.pigpigroad.yichengchechebang.TimeServer.LocationService;
import com.ruoyw.pigpigroad.yichengchechebang.fragment.OilpageAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.fragment.OilpageBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

/**
 * Created by PIGROAD on 2018/3/14.
 */

public class ClassActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout back_ll;
    private TextView nowTime;
    //    private ViewPager viewpager;
//    private FragAdapter adapter;
//    private List<Fragment> list;
    private TextView vip_tv, oil_tv, tv_starttime;
    private ImageView line_iv;
    private int currentIndex;
    private int screenWidth;
    private ImageView classrecord_btn;
    private Button class_dialog;
    private LinearLayout oil_ll, vip_ll;
    private String user_number;
    private TextView
            order_oil_lit, order_count, pay_money,
            pay_order_count, pay_order_money, pay_oil_money, pay_coupon_money, pay_pay_money, pay_other_money, pay_discount_money, pay_order_oil_lit,
            back_count, back_refund_money;
    private RecyclerView recyclerView;
    private OilpageAdapter adapter;
    private List<OilpageBean> data = new ArrayList<>();
    private JSONObject jsonList, totalObj, refundObj;
    private JSONArray group_totalArray;
    private String login_token;
    private LinearLayout ll_info;
    private ImageView iv_record;
    private TextView tv_record;
    //private LinearLayout ll_wx,ll_ali,ll_xcx,ll_app;

    private TextView wx_order_count, wx_order_money, wx_order_oil_lit, wx_refund_count;
    private TextView ali_order_count, ali_order_money, ali_order_oil_lit, ali_refund_count;
    private TextView xcx_order_count, xcx_order_money, xcx_order_oil_lit, xcx_refund_count;
    private TextView app_order_count, app_order_money, app_order_oil_lit, app_refund_count;
    private TextView wx_xcx_order_count, wx_xcx_order_money, wx_xcx_order_oil_lit, wx_xcx_refund_count;
    private TextView wx_app_order_count, wx_app_order_money, wx_app_order_oil_lit, wx_app_refund_count;
    private TextView ali_app_order_count, ali_app_order_money, ali_app_order_oil_lit, ali_app_refund_count;
    private TextView ali_xcx_order_count, ali_xcx_order_money, ali_xcx_order_oil_lit, ali_xcx_refund_count;

    private String order_pay_money,order_discount_money,t_order_oil_lit,order_coupon_count="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_layout);
        initData();
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        runThander();
//        list = new ArrayList<Fragment>();
//        list.add(new Oilpage());
//        //list.add(new Vippage());
//        DisplayMetrics dpMetrics = new DisplayMetrics();
//        getWindow().getWindowManager().getDefaultDisplay()
//                .getMetrics(dpMetrics);
//        screenWidth = dpMetrics.widthPixels;
//        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) line_iv
//                .getLayoutParams();
//        lp.width = screenWidth / 2;
//        adapter = new FragAdapter(getSupportFragmentManager(), list);
//        viewpager.setAdapter(adapter);
//        viewpager.setCurrentItem(0);
//        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float offset, int offsetPixels) {
//                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) line_iv
//                        .getLayoutParams();
//                if (currentIndex == 0 && position == 0)// 0->1
//                {
//                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 2) + currentIndex
//                            * (screenWidth / 2));
//
//                } else if (currentIndex == 1 && position == 0) // 1->0
//                {
//                    lp.leftMargin = (int) (-(1 - offset)
//                            * (screenWidth * 1.0 / 2) + currentIndex
//                            * (screenWidth / 2));
//
//                }
//                line_iv.setLayoutParams(lp);
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                currentIndex = position;
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
        classrecord_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassActivity.this, ClassRecordActivity.class);
                startActivity(intent);
            }
        });
        class_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new TDialog.Builder(getSupportFragmentManager())
//                        .setOnBindViewListener(new OnBindViewListener() {
//                            TextView tv_usernumber;
//
//                            @Override
//                            public void bindView(BindViewHolder viewHolder) {
//                                tv_usernumber = viewHolder.getView(R.id.tv_usernumber);
//                                tv_usernumber.setText("当前帐号:" + user_number);
//                            }
//                        })
//                        .addOnClickListener(R.id.btn_cancel, R.id.btn_sure)
//                        .setOnViewClickListener(new OnViewClickListener() {
//                            @Override
//                            public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
//                                switch (view.getId()) {
//                                    case R.id.btn_cancel:
//                                        tDialog.dismiss();
//                                        break;
//                                    case R.id.btn_sure:
//                                        ClassOver();
//                                        data.clear();
//                                        adapter.notifyDataSetChanged();
//                                        refust();
//                                        tDialog.dismiss();
//                                        break;
//                                }
//                            }
//                        })
//                        .setLayoutRes(R.layout.class_dialog)                        //弹窗布局
//                        .setScreenWidthAspect(ClassActivity.this, 0.9f)               //屏幕宽度比
//                        .setDimAmount(0f)                                                    //设置焦点
//                        .create()
//                        .show();
                AlertDialog.Builder builder=new AlertDialog.Builder(ClassActivity.this);
                LayoutInflater inflater = LayoutInflater.from(ClassActivity.this);
                View view1=inflater.inflate(R.layout.class_dialog,null);
                builder.setCancelable(false);
                builder.setView(view1);
                final Dialog dialog=builder.create();
                Button btn_cancel,btn_sure;
                btn_cancel=view1.findViewById(R.id.btn_cancel);
                btn_sure=view1.findViewById(R.id.btn_sure);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                btn_sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClassOver();
                        ll_info.setVisibility(View.GONE);
                        iv_record.setVisibility(View.VISIBLE);
                        tv_record.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }

    }

    protected boolean enableSliding() {
        return true;
    }

//    /**
//     * 初始化
//     */
//    public void runThander() {
//        new Handler().postDelayed(new Runnable() {
////            final TDialog tDialog = new TDialog.Builder(getSupportFragmentManager())
////                    .setCancelOutside(false)
////                    .setLayoutRes(R.layout.login_dialog)                        //弹窗布局
////                    .setScreenWidthAspect(ClassActivity.this, 0.9f)               //屏幕宽度比
////                    .setDimAmount(0f)                                                    //设置焦点
////                    .create()
////                    .show();
//
//
//            public void run() {
//
//            }
//        }, 1500);
//    }

    private void initData() {
        ll_info = findViewById(R.id.ll_info);
        iv_record = findViewById(R.id.iv_record);
        tv_record = findViewById(R.id.tv_record);


        wx_order_count = findViewById(R.id.wx_order_count);
        wx_order_money = findViewById(R.id.wx_order_money);
        wx_order_oil_lit = findViewById(R.id.wx_order_oil_lit);
        wx_refund_count = findViewById(R.id.wx_refund_count);

        ali_order_count = findViewById(R.id.ali_order_count);
        ali_order_money = findViewById(R.id.ali_order_money);
        ali_order_oil_lit = findViewById(R.id.ali_order_oil_lit);
        ali_refund_count = findViewById(R.id.ali_refund_count);

        xcx_order_count = findViewById(R.id.xcx_order_count);
        xcx_order_money = findViewById(R.id.xcx_order_money);
        xcx_order_oil_lit = findViewById(R.id.xcx_order_oil_lit);
        xcx_refund_count = findViewById(R.id.xcx_refund_count);

        app_order_count = findViewById(R.id.app_order_count);
        app_order_money = findViewById(R.id.app_order_money);
        app_order_oil_lit = findViewById(R.id.app_order_oil_lit);
        app_refund_count = findViewById(R.id.app_refund_count);

        wx_xcx_order_count = findViewById(R.id.wx_xcx_order_count);
        wx_xcx_order_money = findViewById(R.id.wx_xcx_order_money);
        wx_xcx_order_oil_lit = findViewById(R.id.wx_xcx_order_oil_lit);
        wx_xcx_refund_count = findViewById(R.id.wx_xcx_refund_count);

        wx_app_order_count = findViewById(R.id.wx_app_order_count);
        wx_app_order_money = findViewById(R.id.wx_app_order_money);
        wx_app_order_oil_lit = findViewById(R.id.wx_app_order_oil_lit);
        wx_app_refund_count = findViewById(R.id.wx_app_refund_count);

        ali_app_order_count = findViewById(R.id.ali_app_order_count);
        ali_app_order_money = findViewById(R.id.ali_app_order_money);
        ali_app_order_oil_lit = findViewById(R.id.ali_app_order_oil_lit);
        ali_app_refund_count = findViewById(R.id.ali_app_refund_count);

        ali_xcx_order_count = findViewById(R.id.ali_xcx_order_count);
        ali_xcx_order_money = findViewById(R.id.ali_xcx_order_money);
        ali_xcx_order_oil_lit = findViewById(R.id.ali_xcx_order_oil_lit);
        ali_xcx_refund_count = findViewById(R.id.ali_xcx_refund_count);

        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        user_number = sp.getString("user_number", null);
        login_token = sp.getString("user_token", null);
        back_ll = findViewById(R.id.back_ll);
        nowTime = findViewById(R.id.nowtime_tv);
        tv_starttime = findViewById(R.id.tv_starttime);
//        viewpager = (ViewPager) findViewById(R.id.viewpager);
        vip_tv = findViewById(R.id.vip_tv);
        oil_tv = findViewById(R.id.oil_tv);
        line_iv = findViewById(R.id.line_iv);
        classrecord_btn = findViewById(R.id.classrecord_btn);
//        oilgun_btn = findViewById(R.id.oilgun_btn);
        class_dialog = findViewById(R.id.class_dialog);
        vip_ll = findViewById(R.id.vip_ll);
        oil_ll = findViewById(R.id.oil_ll);
        order_oil_lit = findViewById(R.id.order_oil_lit);
        order_count = findViewById(R.id.order_count);
        pay_money = findViewById(R.id.pay_money);

        pay_order_count = findViewById(R.id.pay_order_count);
        pay_order_money = findViewById(R.id.pay_order_money);
        pay_coupon_money = findViewById(R.id.pay_coupon_money);
        pay_oil_money = findViewById(R.id.pay_oil_money);
        pay_pay_money = findViewById(R.id.pay_pay_money);
        pay_other_money = findViewById(R.id.pay_other_money);
        pay_discount_money = findViewById(R.id.pay_discount_money);
        pay_order_oil_lit = findViewById(R.id.pay_order_oil_lit);

        back_count = findViewById(R.id.back_count);
        back_refund_money = findViewById(R.id.back_refund_money);

        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new OilpageAdapter(this, data);
        recyclerView.setAdapter(adapter);
        GetClassData();

        //获取当前时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        nowTime.setText("现在时间："+formatter.format(curDate));


    }


    //油品信息获取
    private void GetClassData() {
        long time = System.currentTimeMillis() / 1000;
        String paylisttime = String.valueOf(time);
        OkGo.<String>post(RUOYU_URL + "?request=private.order.ready.jiaoban.get&token=" + login_token + "&platform=android")
                .tag(this)
                .headers("header1", "headerValue1")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String responseStr = response.body();//这个就是返回来的结果
                        Log.i("pppppppppppGetClassData", responseStr);
                        //进行json数据解析
                        try {
                            JSONObject jsonObject = new JSONObject(responseStr);
                            //判断code是否成功
                            int code = (int) jsonObject.get("code");
                            //code 0表示成功，999表示身份过期,1表示服务器错误
                            if (code == 0) {
                                jsonList = jsonObject.getJSONObject("data");
                                totalObj = jsonList.getJSONObject("total");
                                //group_totalArray = jsonList.getJSONArray("group_total");
                                //油品类别
                                JSONObject oil_type = jsonList.getJSONObject("oil_type");
                                //判断油品是否有下一个
                                Iterator iterator = oil_type.keys();
                                //开始填充数据
                                //Log.i("pppppppppppppp", "开始填充oil数据~~(☄⊙ω⊙)☄");

                                order_pay_money=totalObj.getString("order_pay_money");
                                order_discount_money=totalObj.getString("order_discount_money");
                                t_order_oil_lit=totalObj.getString("order_oil_lit");
                                order_coupon_count=totalObj.getString("order_coupon_count");

                                while (iterator.hasNext()) {
                                    String key = (String) iterator.next();
                                    JSONObject temp = oil_type.getJSONObject(key);
                                    OilpageBean bean = new OilpageBean();
                                    bean.setCount(temp.getString("order_count"));
                                    bean.setOil_lit(temp.getString("order_oil_lit"));
                                    bean.setOil_name(temp.getString("oil_type_name"));
                                    bean.setOrder_money(temp.getString("order_money"));
                                    data.add(bean);
                                }
                                //填充当前订单数量
//                                for (int i = 0; i < group_totalArray.length(); i++) {
//                                    JSONObject temp = group_totalArray.getJSONObject(i);
//                                    OilpageBean bean = new OilpageBean();
//                                    bean.setCount(temp.getString("count"));
//                                    bean.setOil_lit(temp.getString("oil_lit"));
//                                    bean.setOil_name(temp.getString("oil_name"));
//                                    bean.setOrder_money(temp.getString("order_money"));
//                                    data.add(bean);
//                                }
                                adapter.notifyDataSetChanged();
                                tv_starttime.setText("上班时间：" + jsonList.getString("start_time"));

                                order_oil_lit.setText(totalObj.getString("order_oil_lit"));
                                order_count.setText(totalObj.getString("order_count"));
                                pay_money.setText(totalObj.getString("order_money"));
                                //
//                                pay_order_count.setText(totalObj.getString("order_count"));
////                                pay_order_money.setText(totalObj.getString("order_money"));
////                                pay_oil_money.setText(totalObj.getString("oil_money"));
////                                pay_coupon_money.setText(totalObj.getString("coupon_money"));
////                                pay_pay_money.setText(totalObj.getString("pay_money"));
////                                pay_other_money.setText(totalObj.getString("other_money"));
////                                pay_discount_money.setText(totalObj.getString("discount_money"));
////                                pay_order_oil_lit.setText(totalObj.getString("order_oil_lit"));

                                back_count.setText(totalObj.getString("refund_count"));
                                back_refund_money.setText(totalObj.getString("refund_money"));


                                JSONObject pay_method = jsonList.getJSONObject("pay_method");
                                Iterator pay_method_iterator = pay_method.keys();
                                while (pay_method_iterator.hasNext()) {
                                    String key = (String) pay_method_iterator.next();
                                    //判断不同的支付方式
                                    if (key.equals("1")) {
                                        //微信公众号
                                        wx_order_count.setText("笔数：" + pay_method.getJSONObject("1").getString("order_count"));
                                        wx_order_money.setText("金额：" + pay_method.getJSONObject("1").getString("order_money"));
                                        wx_order_oil_lit.setText("升数：" + pay_method.getJSONObject("1").getString("order_oil_lit"));
                                        wx_refund_count.setText("退款：" + pay_method.getJSONObject("1").getString("refund_count"));
                                    } else if (key.equals("2")) {
                                        //微信小程序
                                        wx_xcx_order_count.setText("笔数：" + pay_method.getJSONObject("2").getString("order_count"));
                                        wx_xcx_order_money.setText("金额：" + pay_method.getJSONObject("2").getString("order_money"));
                                        wx_xcx_order_oil_lit.setText("升数：" + pay_method.getJSONObject("2").getString("order_oil_lit"));
                                        wx_xcx_refund_count.setText("退款：" + pay_method.getJSONObject("2").getString("refund_count"));
                                    } else if (key.equals("3")) {
                                        //APP微信
                                        wx_app_order_count.setText("笔数：" + pay_method.getJSONObject("3").getString("order_count"));
                                        wx_app_order_money.setText("金额：" + pay_method.getJSONObject("3").getString("order_money"));
                                        wx_app_order_oil_lit.setText("升数：" + pay_method.getJSONObject("3").getString("order_oil_lit"));
                                        wx_app_refund_count.setText("退款：" + pay_method.getJSONObject("3").getString("refund_count"));
                                    } else if (key.equals("4")) {
                                        //APP支付宝
                                        ali_app_order_count.setText("笔数：" + pay_method.getJSONObject("4").getString("order_count"));
                                        ali_app_order_money.setText("金额：" + pay_method.getJSONObject("4").getString("order_money"));
                                        ali_app_order_oil_lit.setText("升数：" + pay_method.getJSONObject("4").getString("order_oil_lit"));
                                        ali_app_refund_count.setText("退款：" + pay_method.getJSONObject("4").getString("refund_count"));
                                    } else if (key.equals("5")) {
                                        //支付宝生活号
                                        ali_order_count.setText("笔数：" + pay_method.getJSONObject("5").getString("order_count"));
                                        ali_order_money.setText("金额：" + pay_method.getJSONObject("5").getString("order_money"));
                                        ali_order_oil_lit.setText("升数：" + pay_method.getJSONObject("5").getString("order_oil_lit"));
                                        ali_refund_count.setText("退款：" + pay_method.getJSONObject("5").getString("refund_count"));
                                    } else if (key.equals("6")) {
                                        //支付宝小程序
                                        ali_xcx_order_count.setText("笔数：" + pay_method.getJSONObject("6").getString("order_count"));
                                        ali_xcx_order_money.setText("金额：" + pay_method.getJSONObject("6").getString("order_money"));
                                        ali_xcx_order_oil_lit.setText("升数：" + pay_method.getJSONObject("6").getString("order_oil_lit"));
                                        ali_xcx_refund_count.setText("退款：" + pay_method.getJSONObject("6").getString("refund_count"));
                                    }
                                }


                            } else if (code == 1) {
                                Toast.makeText(ClassActivity.this, "服务器错误，请联系管理员", Toast.LENGTH_SHORT).show();
                            } else if (code == 999) {
                                Toast.makeText(ClassActivity.this, "你的身份已过期，请重新登录", Toast.LENGTH_SHORT).show();
                                SharedPreferences sp = ClassActivity.this.getSharedPreferences("LoginUser", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.clear();
                                editor.commit();
                                Intent intent = new Intent(ClassActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            ll_info.setVisibility(View.GONE);
                            iv_record.setVisibility(View.VISIBLE);
                            tv_record.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.i("ppppppppppppppppp", "连接失败!!!!!!!!!!!!!!!");
                        Toast.makeText(ClassActivity.this, "请检查你的网络设置", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    //班结操作
    private void ClassOver() {
        OkGo.<String>post(RUOYU_URL + "?request=private.order.store.jiaoban.action&token=" + login_token + "&platform=android")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String responseStr = response.body();
                        Log.i(PP_TIP, responseStr);
                        try {
                            JSONObject jsonObject1 = new JSONObject(responseStr);
                            String msg = jsonObject1.getString("msg");
                            int code =jsonObject1.getInt("code");
                            if (code == 0){
                                BJprint(jsonObject1.getString("jiaoban_id"));
                            }else {

                            }
                            Toast.makeText(ClassActivity.this, msg, Toast.LENGTH_SHORT).show();
                            //printTicket();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //初始化刷新
    private void refust() {
        order_oil_lit.setText("0");
        order_count.setText("0");
        pay_money.setText("0");

//        pay_order_count.setText("0");
//        pay_order_money.setText("0");
//        pay_oil_money.setText("0");
//        pay_coupon_money.setText("0");
//        pay_pay_money.setText("0");
//        pay_other_money.setText("0");
//        pay_discount_money.setText("0");
//        pay_order_oil_lit.setText("0");

        wx_order_count.setText("笔数：0");
        wx_order_money.setText("金额：0");
        wx_order_oil_lit.setText("升数：0");
        wx_refund_count.setText("退款：0");

        wx_xcx_order_count.setText("笔数：0");
        wx_xcx_order_money.setText("金额：0");
        wx_xcx_order_oil_lit.setText("升数：0");
        wx_xcx_refund_count.setText("退款：0");

        wx_app_order_count.setText("笔数：0");
        wx_app_order_money.setText("金额：0");
        wx_app_order_oil_lit.setText("升数：0");
        wx_app_refund_count.setText("退款：0");

        ali_order_count.setText("笔数：0");
        ali_order_money.setText("金额：0");
        ali_order_oil_lit.setText("升数：0");
        ali_refund_count.setText("退款：0");

        ali_xcx_order_count.setText("笔数：0");
        ali_xcx_order_money.setText("金额：0");
        ali_xcx_order_oil_lit.setText("升数：0");
        ali_xcx_refund_count.setText("退款：0");

        ali_app_order_count.setText("笔数：0");
        ali_app_order_money.setText("金额：0");
        ali_app_order_oil_lit.setText("升数：0");
        ali_app_refund_count.setText("退款：0");

        back_count.setText("0");
        back_refund_money.setText("0");


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_wx:
                //checkOrderDetail("wxgzh",view);
                break;
            case R.id.ll_ali:
                //checkOrderDetail("alipay",view);
                break;
            case R.id.ll_xcx:
                //checkOrderDetail("wxxcx",view);
                break;
            case R.id.ll_app:
                //checkOrderDetail("app",view);
                break;

        }
    }

    private void checkOrderDetail(String type, final View view) {
        OkGo.<String>post(RUOYU_URL + "?request=private.order.ready.jiaoban.get.for.port&token=" + login_token + "&platform=android&pay_type=" + type)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i(PP_TIP, response.body());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            int code = jsonObject.getInt("code");
                            if (code == 0) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                JSONObject total = data.getJSONObject("total");
                                String order_count = total.getString("order_count");
                                String order_money = total.getString("order_money");
                                String oil_money = total.getString("oil_money");
                                String coupon_money = total.getString("coupon_money");
                                String pay_money = total.getString("pay_money");
                                String other_money = total.getString("other_money");
                                String discount_money = total.getString("discount_money");
                                String order_oil_lit = total.getString("order_oil_lit");
                                showDialog(view, order_count, order_money, oil_money, coupon_money, pay_money, discount_money, order_oil_lit);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void showDialog(View view, String order_count, String order_money, String oil_money, String coupon_money, String pay_money, String discount_money, String order_oil_lit) {

        TextView pay_order_count, pay_order_money, pay_oil_money, pay_coupon_money, pay_pay_money, pay_other_money, pay_discount_money, pay_order_oil_lit;

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.class_detail_layout, null);
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.setContentView(view);
        dialog.getWindow().setGravity(Gravity.BOTTOM);//可以设置显示的位置setContentView(view);//自定义布局应该在这里添加，要在dialog.show()的后面
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dialog_animation);
//                  window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        pay_order_count = view.findViewById(R.id.pay_order_count);
        pay_order_money = view.findViewById(R.id.pay_order_money);
        pay_oil_money = view.findViewById(R.id.pay_oil_money);
        pay_coupon_money = view.findViewById(R.id.pay_coupon_money);
        pay_pay_money = view.findViewById(R.id.pay_pay_money);
        pay_other_money = view.findViewById(R.id.pay_other_money);
        pay_discount_money = view.findViewById(R.id.pay_discount_money);
        pay_order_oil_lit = view.findViewById(R.id.pay_order_oil_lit);

        pay_order_count.setText(order_count);
        pay_order_money.setText(order_money);
        pay_oil_money.setText(oil_money);
        pay_coupon_money.setText(coupon_money);
        pay_pay_money.setText(pay_money);
        pay_other_money.setText(pay_money);
        pay_discount_money.setText(discount_money);
        pay_order_oil_lit.setText(order_oil_lit);

    }

    /**
     * 打印支付信息
     */
    public void printTicket(){
        //获取当前时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());

        String title="         "+getString(R.string.app_name)+"班结小票"+"\n";
        AidlUtil.getInstance().printServerText(title, 24, true, false);
        AidlUtil.getInstance().printLine(1);
        AidlUtil.getInstance().printServerText("--------------------------------", 24, false, false);
        AidlUtil.getInstance().printLine(2);
        String vip="  油站："+getIntent().getStringExtra("oil_name")+"\n"
                +"  "+tv_starttime.getText().toString().trim()+"\n"
                +"  交班时间："+formatter.format(curDate)+"\n";
        AidlUtil.getInstance().printServerText(vip, 24, false, false);
        AidlUtil.getInstance().print3Line();
        String info="  订单笔数："+order_count.getText().toString().trim()+"笔\n"
                    +"  订单金额："+pay_money.getText().toString().trim()+"元\n"
                    +"  实收金额："+order_pay_money+"元\n"
                    +"  订单优惠金额："+order_discount_money+"元\n"
                    +"  订单油品升数："+t_order_oil_lit+"升\n"
                    +"  订单优惠券总数："+order_coupon_count+"升\n"
                    +"  油品升数："+order_oil_lit.getText().toString().trim()+"升\n";
        AidlUtil.getInstance().printServerText(info, 24, false, false);
        AidlUtil.getInstance().printLine(2);
        AidlUtil.getInstance().printServerText("--------------------------------", 24, false, false);
        AidlUtil.getInstance().printLine(2);
        String oilinfo="  油品信息："+"\n";
        AidlUtil.getInstance().printServerText(oilinfo, 24, false, false);
        for (int i=0;i<data.size();i++){
            String info2="  "+data.get(i).getOil_name()+"：\n"
                    +"  订单笔数："+data.get(i).getCount()+"笔\n"
                    +"  订单金额："+data.get(i).getOrder_money()+"元\n"
                    +"  订单升数："+data.get(i).getOil_lit()+"升\n";
            AidlUtil.getInstance().printText(info2, 24, false, false);
        }
        AidlUtil.getInstance().printServerText("--------------------------------", 24, false, false);
        AidlUtil.getInstance().printLine(2);
        String info3="  退款信息："+"\n"
                +"  退款总数："+back_count.getText().toString().trim()+"笔\n"
                +"  退款金额："+back_refund_money.getText().toString().trim()+"元\n";
        AidlUtil.getInstance().printText(info3, 24, false, false);
        AidlUtil.getInstance().printLine(5);
    }


    /**
     * 班结打印
     */
    private void BJprint(String jiaoban_id){
        OkGo.<String>post(RUOYU_URL+"?request=private.order.jiaoban_ticket_action&token="+login_token+"&platform=android&jiaoban_id="+jiaoban_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            int code=jsonObject.getInt("code");
                            if (code == 0){
                                //语音播报操作
                                //打印小票操作
                                JSONArray ticket = jsonObject.getJSONArray("ticket");
//                                String title = ticket.getString("title");
//                                String who_discount = ticket.getString("who_discount");
//                                String pay_time = ticket.getString("pay_time");
//                                String orderid = ticket.getString("orderid");
//                                String store_name = ticket.getString("store_name");
//                                String oil_name = ticket.getString("oil_name");
//                                String price = ticket.getString("price");
//                                String lit = ticket.getString("lit");
//                                String gun_id = ticket.getString("gun_id");
//                                String total = ticket.getString("total");
//                                String pay_money = ticket.getString("pay_money");
//                                String pay_type=ticket.getString("pay_type");
//                                printTicket(title, who_discount, pay_time, store_name, orderid, oil_name, price, lit, gun_id, total, pay_money, id,pay_type);
                                for (int i = 0; i < ticket.length(); i++) {
                                    JSONObject temp = ticket.getJSONObject(i);
                                    JSONObject style = temp.getJSONObject("style");
                                    printServerText(temp.getString("value"), style.getInt("font_size"), style.getInt("is_bold"), style.getInt("is_underline"));
                                }
                                AidlUtil.getInstance().printText("----------------", 48, true, false);
                                AidlUtil.getInstance().print3Line();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    /**
     * 从服务器获取打印信息
     */
    private void printServerText(String value, int font_size, int isBold, int is_underline) {
        Boolean b_isBolde;
        Boolean b_is_underline;
        if (isBold == 0) {
            b_isBolde = false;
        } else {
            b_isBolde = true;
        }
        if (is_underline == 0) {
            b_is_underline = false;
        } else {
            b_is_underline = true;
        }
        AidlUtil.getInstance().printText(value, font_size, b_isBolde, b_is_underline);
    }
}
