package com.ruoyw.pigpigroad.yichengchechebang;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
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
public class AliScanActivity extends AppCompatActivity {
    private EditText et_money, et_oilgun;
    private Button btn_scan;
    private String money, oilgun;
    private String login_token, store_id;
    private final int REQUEST_PERMISION_CODE_CAMARE = 0;
    private final int RESULT_REQUEST_CODE = 1;
    private Speek speek;
    private LinearLayout back_ll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ali_scan_layout);
        this.init();
    }

    /**
     * 初始化
     */
    private void init() {

        back_ll = findViewById(R.id.back_ll);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
        GetUser();

        speek = new Speek(this);
        et_money = findViewById(R.id.et_money);
        et_money.setFocusable(true);
        et_money.setFocusableInTouchMode(true);
        et_money.requestFocus();
        et_oilgun = findViewById(R.id.et_oilgun);
        et_money.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_oilgun.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        et_oilgun.setFocusable(true);
        et_oilgun.setFocusableInTouchMode(true);
        et_oilgun.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                money = et_money.getText().toString().trim();
                oilgun = et_oilgun.getText().toString().trim();
                if (money.isEmpty() && oilgun.isEmpty()) {
                    Toast.makeText(AliScanActivity.this, "请正确输入油枪和金额", Toast.LENGTH_SHORT).show();
                } else {
                    if (ContextCompat.checkSelfPermission(AliScanActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        goScanner();
                    } else {
                        ActivityCompat.requestPermissions(AliScanActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISION_CODE_CAMARE);
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
        Intent intent = new Intent(AliScanActivity.this, ZBarView.class);
        //这里可以用intent传递一些参数，比如扫码聚焦框尺寸大小，支持的扫码类型。
//        //设置扫码框的宽
//        intent.putExtra(Constant.EXTRA_SCANNER_FRAME_WIDTH, 400);
//        //设置扫码框的高
//        intent.putExtra(Constant.EXTRA_SCANNER_FRAME_HEIGHT, 400);
//        //设置扫码框距顶部的位置
//        intent.putExtra(Constant.EXTRA_SCANNER_FRAME_TOP_PADDING, 100);
//        //设置是否启用从相册获取二维码。
//        intent.putExtra(Constant.EXTRA_IS_ENABLE_SCAN_FROM_PIC,true);
//        Bundle bundle = new Bundle();
//        //设置支持的扫码类型
//        bundle.putSerializable(Constant.EXTRA_SCAN_CODE_TYPE, mHashMap);
//        intent.putExtras(bundle);
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
                    alipay(content);
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
    private void PayDialog() {
//        new TDialog.Builder(getSupportFragmentManager())
//                .setLayoutRes(R.layout.pay_dialog)
//                .addOnClickListener(R.id.btn_confirm)
//                .setOnViewClickListener(new OnViewClickListener() {
//                    @Override
//                    public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
//                        switch (view.getId()){
//                            case R.id.btn_confirm:
//
//                                break;
//                        }
//                    }
//                })
//                .setScreenWidthAspect(this, 0.9f)               //屏幕宽度比
//                .setDimAmount(0f)                                                    //设置焦点
//                .create()
//                .show();

    }

    /**
     * 等待用户支付弹窗
     */
    private void WaitPayDialog(final String auth_code, final String gun_id, final String oil_name, final String country_oil_money, final String discount_money, final String need_payment, final String who_discount, final String coupon_money) {
//        new TDialog.Builder(getSupportFragmentManager())
//                .setLayoutRes(R.layout.paywait_dialog)
//                .addOnClickListener(R.id.btn_cancel,R.id.btn_confirm)
//                .setOnBindViewListener(new OnBindViewListener() {
//                    @Override
//                    public void bindView(BindViewHolder viewHolder) {
//                        TextView tv_oilgun,tv_oil,tv_country_oil_money,tv_discount_money,tv_need_payment,tv_coupon_money;
//                        tv_oilgun=viewHolder.getView(R.id.tv_oilgun);
//                        tv_oil=viewHolder.getView(R.id.tv_oilgun);
//                        tv_country_oil_money=viewHolder.getView(R.id.tv_country_oil_money);
//                        tv_discount_money=viewHolder.getView(R.id.tv_discount_money);
//                        tv_need_payment=viewHolder.getView(R.id.tv_need_payment);
//                        tv_coupon_money=viewHolder.getView(R.id.tv_coupon_money);
//                        tv_oilgun.setText(gun_id+"号");
//                        tv_oil.setText(oil_name);
//                        tv_country_oil_money.setText(country_oil_money+"元");
//                        tv_discount_money.setText(discount_money+"元");
//                        tv_need_payment.setText(need_payment+"元");
//                        tv_coupon_money.setText(coupon_money+"元");
//                    }
//                })
//                .setOnViewClickListener(new OnViewClickListener() {
//                    @Override
//                    public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
//                        switch (view.getId()) {
//                            case R.id.btn_cancel:
//                                tDialog.dismiss();
//                                break;
//                            case R.id.btn_confirm:
//                                //WX支付验证
//                                tDialog.dismiss();
//                                alipayinfo(auth_code,money=et_money.getText().toString().trim(),
//                                        oilgun=et_oilgun.getText().toString().trim(),discount_money,oil_name,country_oil_money,who_discount);
//                                break;
//                        }
//                    }
//                })
//                .setScreenWidthAspect(this, 0.9f)               //屏幕宽度比
//                .setDimAmount(0f)                                                    //设置焦点
//                .create()
//                .show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.paywait_dialog, null);
        builder.setView(view);
        final Dialog dialog = builder.create();
        Button btn_cancel, btn_confirm;
        btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_confirm = view.findViewById(R.id.btn_confirm);
        TextView tv_oilgun, tv_oil, tv_country_oil_money, tv_discount_money, tv_need_payment, tv_coupon_money;
        tv_oilgun = view.findViewById(R.id.tv_oilgun);
        tv_oil = view.findViewById(R.id.tv_oil);
        tv_country_oil_money = view.findViewById(R.id.tv_country_oil_money);
        tv_discount_money = view.findViewById(R.id.tv_discount_money);
        tv_need_payment = view.findViewById(R.id.tv_need_payment);
        tv_coupon_money = view.findViewById(R.id.tv_coupon_money);
        tv_oilgun.setText(gun_id + "号");
        tv_oil.setText(oil_name);
        tv_country_oil_money.setText(country_oil_money + "元");
        tv_discount_money.setText(discount_money + "元");
        tv_need_payment.setText(need_payment + "元");
        tv_coupon_money.setText(coupon_money + "元");
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //WX支付验证
                dialog.dismiss();
                alipayinfo(auth_code, money = et_money.getText().toString().trim(),
                oilgun = et_oilgun.getText().toString().trim(), discount_money, oil_name, country_oil_money, who_discount);
            }
        });
        dialog.show();
    }

    /**
     * 打印支付信息
     */
    private void printTicket(String auth_code, String who_discount, String oil, String country_oil_price) {
        final SimpleDateFormat simpledf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        final String time = simpledf.format(new Date());
        String title = "  易成车车帮支付小票--顾客联" + "\n";
        AidlUtil.getInstance().printText(title, 24, false, false);
        String vip = "          " + who_discount + "\n";
        AidlUtil.getInstance().printText(vip, 24, false, false);
        String ticket = "  出租车优惠1.3/升\n"
                + "  时间：" + time + "\n"
                + "  订单：" + auth_code + "\n"
                + "  油站：美联加油站-泗喜能源" + "\n"
                + "  油品： " + oil + "\n"
                + "  单价：油价" + country_oil_price + "\n"
                + "  油量：60.00升" + "\n";
        AidlUtil.getInstance().printText(ticket, 24, false, false);
        String info = " 油枪：" + oilgun + "号" + "\n"
                + " 合计：" + money + "元" + "\n"
                + " 实付：" + money + "元" + "\n\n\n";
        AidlUtil.getInstance().printText(info, 48, true, false);

    }

    /**
     * 支付宝扫码支付
     */
    public void alipay(final String auth_code) {
        OkGo.<String>post(RUOYU_URL + "?request=public.pay_alipay.order.alipay.qrcode.pay.info&token=" + login_token + "&platform=android&auth_code=" + auth_code + "&order_money=" + money + "&gun_id=" + oilgun + "&store_id=" + store_id + "&sence_no=1")
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String resStr = response.body();
                        Log.i(PP_TIP, "Url:" + RUOYU_URL + "?request=public.pay_alipay.order.alipay.qrcode.pay.info&token=" + login_token + "&platform=android&auth_code=" + auth_code + "&order_money=" + money + "&gun_id=" + oilgun + "&store_id=" + store_id + "\nres:" + resStr);
                        try {
                            JSONObject jsonObject = new JSONObject(resStr);
                            final int code = jsonObject.getInt("code");
                            if (code == 0) {
                                WaitPayDialog(auth_code, jsonObject.getString("gun_id"), jsonObject.getString("oil_name"), jsonObject.getString("country_oil_price"), jsonObject.getString("discountMoney"), jsonObject.getString("need_payment"), jsonObject.getString("discountName"), jsonObject.getString("coupon_money"));
//                                //支付成功
//                                PayDialog(auth_code);
//                                //语音播报
//                                speek.Speeking("温馨提示，"+oilgun+"号油枪加油"+money+"元，实付"+money+"元。");
//                                //调用打印机
//                                printTicket(auth_code);
                                et_money.setText("");
                                et_oilgun.setText("");
                                et_oilgun.setFocusable(true);
                                et_oilgun.setFocusableInTouchMode(true);
                                et_oilgun.requestFocus();
                            } else if (code == 1) {
                                String msg = jsonObject.getString("msg");
                                Toast.makeText(AliScanActivity.this, msg, Toast.LENGTH_SHORT).show();
                            } else if (code == 999) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    /**
     * 支付宝支付操作
     */
    private void alipayinfo(final String auth_code, String order_money, String gun_id, final String discount_money, final String oil_name, final String country_oil_money, final String who_discount) {
        OkGo.<String>post(RUOYU_URL + "?request=public.pay_alipay.order.alipay.qrcode.wallet.action&token=" + login_token + "&platform=android&auth_code=" + auth_code + "&order_money=" + order_money + "&gun_id=" + gun_id + "&store_id=" + store_id + "&sence_no=1")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String Str = response.body();
                        try {
                            JSONObject jsonObject1 = new JSONObject(Str);
                            int code = jsonObject1.getInt("code");
                            if (code == 0) {
//                                PayDialog();
//                                //语音播报
//                                speek.Speeking("温馨提示，"+oilgun+"号油枪加油"+money+"元，实付"+money+"元。");
//                                //调用打印机
//                                printTicket(auth_code,who_discount,oil_name,country_oil_money);
                            } else if (code == 1) {
                                String msg = jsonObject1.getString("msg");
                                Toast.makeText(AliScanActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //获取门店管理员信息
    private void GetUser() {
        OkGo.<String>post(RUOYU_URL + "?request=private.admin.my.admin.info.get&token=" + login_token + "&platform=android")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String Str = response.body();
                        try {
                            JSONObject jsonObject1 = new JSONObject(Str);
                            JSONObject data = jsonObject1.getJSONObject("data");
                            store_id = data.getString("store_id");
                            Log.i(PP_TIP, "store_id:" + store_id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
