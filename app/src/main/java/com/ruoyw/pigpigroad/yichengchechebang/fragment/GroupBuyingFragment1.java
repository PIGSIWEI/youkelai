package com.ruoyw.pigpigroad.yichengchechebang.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.R;
import com.ruoyw.pigpigroad.yichengchechebang.SUNMI.util.AidlUtil;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.Speek;
import com.ruoyw.pigpigroad.yichengchechebang.ZxingUtils.view.Constant;
import com.ruoyw.pigpigroad.yichengchechebang.ZxingUtils.view.ScannerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

/**
 * Created by XYSM on 2018/4/4.
 */

public class GroupBuyingFragment1 extends Fragment {
    private EditText et_money, et_oilgun;
    private Button btn_scan;
    private String money, oilgun;
    private String login_token, store_id;
    private final int REQUEST_PERMISION_CODE_CAMARE = 0;
    private final int RESULT_REQUEST_CODE = 1;
    private Speek speek;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.groupbuying_fragment2_layout, container, false);
        return view;
    }

    /**
     * 初始化控件
     */
    private void init() {
        SharedPreferences sp = getActivity().getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
        speek = new Speek(getActivity());
        et_money = getActivity().findViewById(R.id.et_money);
        et_oilgun = getActivity().findViewById(R.id.et_oilgun);
        et_money.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_oilgun.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        btn_scan = getActivity().findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                money = et_money.getText().toString().trim();
                oilgun = et_oilgun.getText().toString().trim();
                if (money.isEmpty() && oilgun.isEmpty()) {
                    Toast.makeText(getActivity(), "请正确输入油枪和金额", Toast.LENGTH_SHORT).show();
                } else {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        goScanner();
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISION_CODE_CAMARE);
                    }
                }
            }
        });
        //GetUser();
    }

    private void goScanner() {
        Intent intent = new Intent(getActivity(), ScannerActivity.class);
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
    private void PayDialog() {
//        new TDialog.Builder(getFragmentManager())
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
//                .setScreenWidthAspect(getActivity(), 0.9f)               //屏幕宽度比
//                .setDimAmount(0f)                                                    //设置焦点
//                .create()
//                .show();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.pay_dialog, null);
        builder.setCancelable(false);
        builder.setView(view);

    }

    /**
     * 等待用户支付弹窗
     */
    private void WaitPayDialog(final String auth_code, final String gun_id, final String oil_name, final String country_oil_money, final String discount_money, final String need_payment, final String who_discount, final String coupon_money) {
//        new TDialog.Builder(getFragmentManager())
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
//                                wxpayinfo(auth_code,money=et_money.getText().toString().trim(),
//                                oilgun=et_oilgun.getText().toString().trim(),discount_money,oil_name,country_oil_money,who_discount);
//                                break;
//                        }
//                    }
//                })
//                .setScreenWidthAspect(getActivity(), 0.9f)               //屏幕宽度比
//                .setDimAmount(0f)                                                    //设置焦点
//                .create()
//                .show();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.paywait_dialog, null);
        builder.setCancelable(false);
        builder.setView(view);
        final Dialog dialog = builder.create();
        TextView tv_oilgun, tv_oil, tv_country_oil_money, tv_discount_money, tv_need_payment, tv_coupon_money;
        tv_oilgun = view.findViewById(R.id.tv_oilgun);
        tv_oil = view.findViewById(R.id.tv_oilgun);
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
     * 微信扫码支付
     */
    public void wxpay(final String auth_code) {
        OkGo.<String>post(RUOYU_URL + "?request=public.pay.order.wx.qrcode.pay.info&token=" + login_token + "&platform=android&auth_code=" + auth_code + "&order_money=" + money + "&gun_id=" + oilgun + "&store_id=" + store_id)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String resStr = response.body();
                        Log.i(PP_TIP, "Url:" + RUOYU_URL + "?request=public.pay.order.wx.qrcode.pay.info&token=" + login_token + "&platform=android&auth_code=" + auth_code + "&order_money=" + money + "&gun_id=" + oilgun + "&store_id=" + store_id + "\nres:" + resStr);
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
                            } else if (code == 1) {
                                String msg = jsonObject.getString("msg");
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                            } else if (code == 999) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    /**
     * 微信支付操作
     */
    private void wxpayinfo(final String auth_code, String order_money, String gun_id, final String discount_money, final String oil_name, final String country_oil_money, final String who_discount) {
        OkGo.<String>post(RUOYU_URL + "?request=public.pay.order.wx.qrcode.wallet.action&token=" + login_token + "&platform=android&auth_code=" + auth_code + "&order_money=" + order_money + "&gun_id=" + gun_id + "&store_id=" + store_id)
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
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
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
