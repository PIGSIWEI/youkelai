package com.ruoyw.pigpigroad.yichengchechebang;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.SUNMI.util.AidlUtil;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.Speek;
import com.ruoyw.pigpigroad.yichengchechebang.ZxingUtils.view.Constant;
import com.ruoyw.pigpigroad.yichengchechebang.ZxingUtils.view.ScannerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

/**
 * Created by PIGROAD on 2018/10/15
 * Email:920015363@qq.com
 */
public class ScanActivity extends AppCompatActivity{
    private EditText et_money,et_oilgun;
    private Button btn_scan;
    private String money,oilgun;
    private String login_token,store_id;
    private final int REQUEST_PERMISION_CODE_CAMARE = 0;
    private final int RESULT_REQUEST_CODE = 1;
    private Speek speek;
    private LinearLayout back_ll;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_layout);
        this.init();
    }

    /**
     * 初始化
     */
    private void init() {


        back_ll=findViewById(R.id.back_ll);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
        GetUser();

        et_money=findViewById(R.id.et_money);

        et_oilgun=findViewById(R.id.et_oilgun);
        et_oilgun.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_money.setInputType( InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        et_oilgun.setFocusable(true);
        et_oilgun.setFocusableInTouchMode(true);
        et_oilgun.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


        btn_scan=findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                money=et_money.getText().toString().trim();
                oilgun=et_oilgun.getText().toString().trim();
                if (money.isEmpty()&&oilgun.isEmpty()){
                    Toast.makeText(ScanActivity.this,"请正确输入油枪和金额",Toast.LENGTH_SHORT).show();
                }else {
                    if (ContextCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        goScanner();
                    } else {
                        ActivityCompat.requestPermissions(ScanActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISION_CODE_CAMARE);
                    }
                }
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

    private void goScanner() {
        Intent intent = new Intent(ScanActivity.this, ZBarView.class);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISION_CODE_CAMARE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goScanner();
                }
                return;
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_REQUEST_CODE:
                    if (data == null) return;
                    String type = data.getStringExtra(Constant.EXTRA_RESULT_CODE_TYPE);
                    String content = data.getStringExtra(Constant.EXTRA_RESULT_CONTENT);
                    wxpay(content);
                    break;
                default:
                    break;

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 支付成功弹窗信息
     */
    private void PayDialog(){
    }

    /**
     * 等待用户支付弹窗
     */
    private void WaitPayDialog(final String auth_code, final String gun_id, final String oil_name, final String country_oil_money, final String discount_money, final String need_payment,final String who_discount,final String coupon_money,String merchant_oil_price){
        AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
        LayoutInflater inflater = LayoutInflater.from(ScanActivity.this);
        View view = inflater.inflate(R.layout.paywait_dialog, null);
        builder.setCancelable(false);
        builder.setView(view);
        final Dialog dialog = builder.create();
        TextView tv_oilgun, tv_oil, tv_country_oil_money, tv_discount_money, tv_need_payment, tv_coupon_money,tv_merchant_oil_price;
        tv_oilgun = view.findViewById(R.id.tv_oilgun);
        tv_oil = view.findViewById(R.id.tv_oil);
        tv_country_oil_money = view.findViewById(R.id.tv_country_oil_money);
        tv_discount_money = view.findViewById(R.id.tv_discount_money);
        tv_need_payment = view.findViewById(R.id.tv_need_payment);
        tv_merchant_oil_price = view.findViewById(R.id.tv_merchant_oil_price);
        tv_coupon_money = view.findViewById(R.id.tv_coupon_money);
        tv_oilgun.setText(gun_id + "号");
        tv_oil.setText(oil_name);
        tv_country_oil_money.setText(country_oil_money + "元");
        tv_discount_money.setText(discount_money + "元");
        tv_need_payment.setText(need_payment + "元");
        tv_coupon_money.setText(coupon_money + "元");
        tv_merchant_oil_price.setText(merchant_oil_price + "元");
        Button btn_cancel, btn_confirm;
        btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_confirm = view.findViewById(R.id.btn_confirm);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                wxpayinfo(auth_code, money = et_money.getText().toString().trim(),
                        oilgun = et_oilgun.getText().toString().trim(), discount_money, oil_name, country_oil_money, who_discount);
            }
        });
        dialog.show();
    }

    /**
     * 微信扫码支付
     */
    public void wxpay(final String auth_code){
        OkGo.<String>post(RUOYU_URL+"?request=public.pay.order.wx.qrcode.pay.info&token="+login_token+"&platform=android&auth_code="+auth_code+"&order_money="+money+"&gun_id="+oilgun+"&store_id="+store_id)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String resStr=response.body();
                        Log.i(PP_TIP,"Url:"+RUOYU_URL+"?request=public.pay.order.wx.qrcode.pay.info&token="+login_token+"&platform=android&auth_code="+auth_code+"&order_money="+money+"&gun_id="+oilgun+"&store_id="+store_id+"\nres:"+resStr);
                        try {
                            JSONObject jsonObject=new JSONObject(resStr);
                            final int code =jsonObject.getInt("code");
                            if (code == 0){
                                WaitPayDialog(auth_code,jsonObject.getString("gun_id"),jsonObject.getString("oil_name"),jsonObject.getString("country_oil_price"),jsonObject.getString("discountMoney"),jsonObject.getString("need_payment"),jsonObject.getString("discountName"),jsonObject.getString("coupon_money"),jsonObject.getString("merchant_oil_price"));
                            }else if (code == 1){
                                String msg=jsonObject.getString("msg");
                                Toast.makeText(ScanActivity.this,msg,Toast.LENGTH_SHORT).show();
                            }else if (code == 999){

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    /**
     *  微信支付操作
     */
    private void wxpayinfo(final String auth_code, String order_money, String gun_id, final String discount_money,final String oil_name,final String country_oil_money,final String who_discount){
        Log.i(PP_TIP,RUOYU_URL+"?request=public.pay.order.wx.qrcode.wallet.action&token="+login_token+"&platform=android&auth_code="+auth_code+"&order_money="+order_money+"&gun_id="+gun_id+"&store_id="+store_id);
        OkGo.<String>post(RUOYU_URL+"?request=public.pay.order.wx.qrcode.wallet.action&token="+login_token+"&platform=android&auth_code="+auth_code+"&order_money="+order_money+"&gun_id="+gun_id+"&store_id="+store_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String Str=response.body();
                        Log.i(PP_TIP,response.body());
                        try {
                            JSONObject jsonObject1=new JSONObject(Str);
                            int code =jsonObject1.getInt("code");
                            if (code == 0 ){
//                                PayDialog();
//                                //语音播报
//                                speek.Speeking("温馨提示，"+oilgun+"号油枪加油"+money+"元，实付"+money+"元。");
//                                //调用打印机
//                                printTicket(auth_code,who_discount,oil_name,country_oil_money);
                                Toast.makeText(ScanActivity.this,"付款成功！",Toast.LENGTH_SHORT).show();
                                et_money.setText("");
                                et_oilgun.setText("");
                                et_oilgun.setFocusable(true);
                                et_oilgun.setFocusableInTouchMode(true);
                                et_oilgun.requestFocus();
                            }else if (code == 1){
                                String msg=jsonObject1.getString("msg");
                                Toast.makeText(ScanActivity.this,msg,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //获取门店管理员信息
    private void GetUser(){
        OkGo.<String>post(RUOYU_URL+"?request=private.admin.my.admin.info.get&token="+login_token+"&platform=android")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String Str=response.body();
                        try {
                            JSONObject jsonObject1=new JSONObject(Str);
                            JSONObject data=jsonObject1.getJSONObject("data");
                            store_id=data.getString("store_id");
                            Log.i(PP_TIP,"store_id:"+store_id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 循环 查询 扫描结果
     */
    private void checkPay(){
        OkGo.<String>post(RUOYU_URL+"")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            int code=jsonObject.getInt("code");
                            if (code == 0){

                            }
                        } catch (JSONException e) {
                                e.printStackTrace();
                        }
                    }
                });
    }
}
