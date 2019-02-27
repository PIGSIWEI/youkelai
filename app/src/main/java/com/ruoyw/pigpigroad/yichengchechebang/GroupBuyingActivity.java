package com.ruoyw.pigpigroad.yichengchechebang;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;
import com.ruoyw.pigpigroad.yichengchechebang.ZxingUtils.view.Constant;
import com.ruoyw.pigpigroad.yichengchechebang.fragment.GroupBuyingFragment1;
import com.ruoyw.pigpigroad.yichengchechebang.fragment.GroupBuyingFragment2;
import com.ruoyw.pigpigroad.yichengchechebang.fragment.GroupBuyingFragment3;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

/**
 * Created by PIGROAD on 2018/3/16.
 */

public class    GroupBuyingActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout back_ll, scan_ll;
    private Button btn_yanzhen;
    private String login_token;
    private final int RESULT_REQUEST_CODE = 1;
    private LinearLayout ll_wx_scan,ll_ali_scan,ll_hx_scan,ll_sf_scan,ll_car_scan,ll_xj_scan;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_buying_layout);
        init();

        back_ll = findViewById(R.id.back_ll);
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
        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
    }

    /**
     * 数据初始化
     */
    private void init() {

        ll_wx_scan=findViewById(R.id.ll_wx_scan);
        ll_ali_scan=findViewById(R.id.ll_ali_scan);
        ll_hx_scan=findViewById(R.id.ll_hx_scan);
        ll_sf_scan=findViewById(R.id.ll_sf_scan);
        ll_car_scan=findViewById(R.id.ll_car_scan);
        ll_xj_scan=findViewById(R.id.ll_xj_scan);

        ll_wx_scan.setOnClickListener(this);
        ll_ali_scan.setOnClickListener(this);
        ll_hx_scan.setOnClickListener(this);
        ll_sf_scan.setOnClickListener(this);
        ll_car_scan.setOnClickListener(this);
        ll_xj_scan.setOnClickListener(this);

    }

    protected boolean enableSliding() {
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_wx_scan:
                Intent intent=new Intent(GroupBuyingActivity.this,ScanActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_ali_scan:
                Intent intent1=new Intent(GroupBuyingActivity.this,AliScanActivity.class);
                startActivity(intent1);
                break;
            case R.id.ll_car_scan:
                showDialog();
                break;
            case R.id.ll_xj_scan:
                Intent intent3 = new Intent(GroupBuyingActivity.this, ZBarView.class);
                startActivityForResult(intent3, RESULT_REQUEST_CODE);
                break;
        }
    }

    /**
     * 专车认证弹窗
     */
    private void showDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(GroupBuyingActivity.this);
        View view = inflater.inflate(R.layout.password_dialog, null);
        builder.setView(view);
        final Dialog dialog=builder.create();
        final EditText et_password=view.findViewById(R.id.et_password);
        Button btn_cancel,btn_confirm;
        btn_cancel=view.findViewById(R.id.btn_cancel);
        btn_confirm=view.findViewById(R.id.btn_confirm);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_password.getText().toString().equals("")){
                    Toast.makeText(GroupBuyingActivity.this,"请输入专车认证密码！",Toast.LENGTH_SHORT).show();
                }else {
                    CarPassword(et_password.getText().toString(),dialog);
                }
            }
        });
        dialog.show();
    }

    /**
     * 专车认证密码
     */
    private void CarPassword(String password, final Dialog dialog){
        OkGo.<String>post(RUOYU_URL+"?request=private.admin.pos.quick.car.auth.password&token="+login_token+"&platform=android&password="+password)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            int code =jsonObject.getInt("code");
                            if (code == 0){
                                Intent intent2=new Intent(GroupBuyingActivity.this,CarScanActivity.class);
                                startActivity(intent2);
                                dialog.dismiss();
                            }else {

                            }
                            Toast.makeText(GroupBuyingActivity.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_REQUEST_CODE:
                    if (data == null) return;
                    String type = data.getStringExtra(Constant.EXTRA_RESULT_CODE_TYPE);
                    String content = data.getStringExtra(Constant.EXTRA_RESULT_CONTENT);
                    checkQrcode(content);
                    break;
                default:
                    break;

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 扫一扫查询
     */
    private void checkQrcode(final String content) {
        OkGo.<String>post(RUOYU_URL + "?request=private.order.check_pay_cash_order_qrcode&token=" + login_token + "&platform=android&id=" + content)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            int code = jsonObject.getInt("code");
                            if (code == 0) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                showOrderDialog(
                                        content,
                                        data.getString("orderid"),
                                        data.getString("order_time"),
                                        data.getString("country_oil_price"),
                                        data.getString("oil_lit"),
                                        data.getString("money"),
                                        data.getString("name")
                                );
                            } else {
                                String msg = jsonObject.getString("msg");
                                Toast.makeText(GroupBuyingActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 扫出订单弹窗
     */
    private void showOrderDialog(final String content, final String tv_order_id, final String tv_order_time, final String tv_order_price, final String tv_oil_lit, final String tv_money, final String tv_oil_name) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(GroupBuyingActivity.this);
        LayoutInflater inflater = LayoutInflater.from(GroupBuyingActivity.this);
        View view = inflater.inflate(R.layout.cash_order_dialog, null);
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.setContentView(view);
        dialog.getWindow().setGravity(Gravity.BOTTOM);//可以设置显示的位置setContentView(view);//自定义布局应该在这里添加，要在dialog.show()的后面
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dialog_animation);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        TextView order_id, order_time, order_price, oil_lit, money, oil_name;
        order_id = view.findViewById(R.id.order_id);
        order_time = view.findViewById(R.id.order_time);
        order_price = view.findViewById(R.id.order_price);
        oil_lit = view.findViewById(R.id.oil_lit);
        money = view.findViewById(R.id.money);
        oil_name = view.findViewById(R.id.oil_name);
        order_id.setText("订单：" + tv_order_id);
        order_time.setText("订单时间：" + tv_order_time);
        order_price.setText("油价：" + tv_order_price + "元/升");
        oil_lit.setText("油量：" + tv_oil_lit + "升");
        money.setText("金额：" + tv_money);
        oil_name.setText("油站：" + tv_oil_name);
        Button btn_confirm;
        btn_confirm=view.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                builder1.setMessage("确定现金收款嘛？");
                builder1.setTitle("提示");
                builder1.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                        dialogInterface.dismiss();
                        confirmCash(content);
                    }
                });
                builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder1.create().show();
            }
        });
    }

    /**
     * 确定现金收款
     */
    private void confirmCash(String id) {
        OkGo.<String>post(RUOYU_URL + "?request=private.order.confirm_pay_cash_order&token=" + login_token + "&platform=android&id=" + id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            String msg = jsonObject.getString("msg");
                            Toast.makeText(GroupBuyingActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


}

