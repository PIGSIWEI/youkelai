package com.ruoyw.pigpigroad.yichengchechebang;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.FlashOrderAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.OnItemClickLitener;
import com.ruoyw.pigpigroad.yichengchechebang.Bean.FlashOrderBean;
import com.ruoyw.pigpigroad.yichengchechebang.SUNMI.util.AidlUtil;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.MyApplication;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

public class FlashOrderActivity extends AppCompatActivity {

    private LinearLayout back_ll;
    private RecyclerView recyclerView;
    private List<FlashOrderBean> datas = new ArrayList<>();
    private FlashOrderAdapter adapter;
    private String login_token;
    private LinearLayout ll_null;
    private String imei;
    private Button btn_search;
    private ProgressDialog waitingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flash_order_layout);
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
        SharedPreferences sp = this.getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
        back_ll = findViewById(R.id.back_ll);
        ll_null = findViewById(R.id.ll_null);
        btn_search = findViewById(R.id.btn_search);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new FlashOrderAdapter(this, datas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        getData();

        adapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                showTicket(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        imei = ((TelephonyManager) MyApplication.getContext().getSystemService(TELEPHONY_SERVICE)).getDeviceId();

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查询未打印轮训订单
                LoginDialog();
            }
        });

    }

    protected boolean enableSliding() {
        return true;
    }

    /**
     * 获取数据
     */
    private void getData() {
        Log.i(PP_TIP, RUOYU_URL + "?request=private.quick_order.get.quick.order&token=" + login_token + "&platform=android");
        OkGo.<String>post(RUOYU_URL + "?request=private.quick_order.get.quick.order&token=" + login_token + "&platform=android")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            int code = jsonObject.getInt("code");
                            if (code == 0) {
                                JSONArray data = jsonObject.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    if (data.length() <= 0) {
                                        recyclerView.setVisibility(View.GONE);
                                        ll_null.setVisibility(View.VISIBLE);
                                    } else {
                                        JSONObject temp = data.getJSONObject(i);
                                        FlashOrderBean bean = new FlashOrderBean();
                                        bean.setOrderid(temp.getString("orderid"));
                                        bean.setPay_money(temp.getString("pay_money"));
                                        bean.setPay_time(temp.getString("pay_time"));
                                        bean.setStore_name(temp.getString("store_name"));
                                        bean.setId(temp.getString("id"));
                                        datas.add(bean);
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

    /**
     * 弹窗 小票补打
     */
    private void showTicket(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("要补打订单小票嘛？");
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                printTicket(datas.get(position).getId());
            }
        });
        builder.create().show();
    }

    private void printTicket(String id) {
        Log.i(PP_TIP,RUOYU_URL + "?request=private.quick_order.re.print.quick.order&token=" + login_token + "&platform=android&id=" + id+"&imei="+imei);
        OkGo.<String>post(RUOYU_URL + "?request=private.quick_order.re.print.quick.order&token=" + login_token + "&platform=android&id=" + id+"&imei="+imei)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            int code = jsonObject.getInt("code");
                            if (code == 0) {
                                JSONArray ticket = jsonObject.getJSONArray("ticket");
                                for (int i = 0; i < ticket.length(); i++) {
                                    JSONObject temp = ticket.getJSONObject(i);
                                    JSONObject style = temp.getJSONObject("style");
                                    printServerText(temp.getString("value"), style.getInt("font_size"), style.getInt("is_bold"), style.getInt("is_underline"));
                                }
                                AidlUtil.getInstance().printText("----------------", 48, true, false);
                                AidlUtil.getInstance().print3Line();
                            } else {

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

    /**
     * 弹窗循环
     */
    private void LoginDialog(){
        waitingDialog= new ProgressDialog(FlashOrderActivity.this);
        waitingDialog.setTitle("正在查询未支付闪付订单");
        waitingDialog.setMessage("查询中···");
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(false);
        waitingDialog.show();
        checkOrder();
    }


    /**
     * 查询未支付的闪付订单
     */
    private void checkOrder(){
        Log.i(PP_TIP,RUOYU_URL+"?request=private.quick_order.get_no_print_quick_order&platform=android&imei="+imei+"&token="+login_token);
        OkGo.<String>post(RUOYU_URL+"?request=private.quick_order.get_no_print_quick_order&platform=android&imei="+imei+"&token="+login_token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            int code=jsonObject.getInt("code");
                            if (code == 0){
                                JSONArray data=jsonObject.getJSONArray("data");
                                for (int i=0;i<data.length();i++){
                                    JSONObject temp=data.getJSONObject(i);
                                    String id=temp.getString("id");
                                    PrintTicket(id);
                                }
                            }else {
                                Toast.makeText(FlashOrderActivity.this,jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                            }
                            waitingDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 打印接口
     */
    private void PrintTicket(String id){
        Log.i(PP_TIP,RUOYU_URL+"?request=private.quick_order.ams.print.quick.order&platform=android&id="+id+"&imei="+imei+"&token="+login_token);
        OkGo.<String>post(RUOYU_URL+"?request=private.quick_order.ams.print.quick.order&platform=android&id="+id+"&imei="+imei+"&token="+login_token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            int code=jsonObject.getInt("code");
                            if (code == 0){
                                //打印小票操作
                                JSONArray ticket = jsonObject.getJSONArray("ticket");
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


}
