package com.ruoyw.pigpigroad.yichengchechebang;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.SUNMI.util.AidlUtil;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;
import com.ruoyw.pigpigroad.yichengchechebang.ZxingUtils.QrCreat;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

/**
 * Created by PIGROAD on 2018/11/27
 * Email:920015363@qq.com
 */
public class CashOrderActivity extends AppCompatActivity{

    private LinearLayout back_ll;
    private EditText et_oilgun,et_money;
    private Button btn;
    private String login_token,store_id;

    private CheckBox cb_1,cb_2;

    private int pt=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cash_order_layout);
        this.init();
    }

    private void init() {
        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        SharedPreferences sp2 = getSharedPreferences("LoginInfo", MODE_PRIVATE);
        store_id = sp2.getString("store_id", null);

        login_token = sp.getString("user_token", null);
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }

        back_ll=findViewById(R.id.back_ll);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_money=findViewById(R.id.et_money);
        et_oilgun=findViewById(R.id.et_oilgun);
        btn=findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money=et_money.getText().toString().trim();
                String oilgun=et_oilgun.getText().toString().trim();
                if (money.equals("")||oilgun.equals("")){
                    Toast.makeText(CashOrderActivity.this,"请输入油枪和金额！",Toast.LENGTH_SHORT).show();
                }else {
                    creatCoupon(oilgun,money);
                }
            }
        });

        cb_1=findViewById(R.id.cb_1);
        cb_2=findViewById(R.id.cb_2);
        cb_1.setChecked(true);

        cb_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    cb_2.setChecked(false);
                }else {
                    cb_2.setChecked(true);
                }
            }
        });

        cb_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    cb_1.setChecked(false);
                }else {
                    cb_1.setChecked(true);
                }
            }
        });
    }

    /**
     * 接口生成卷
     */
    private void creatCoupon(final String gun_id, final String money){
        if (cb_1.isChecked()){
            pt=1;
        }else {
            pt=2;
        }
        OkGo.<String>post(RUOYU_URL + "?request=private.order.cash_order_create_qrcode&token="+login_token+"&platform=android&store_id="+store_id+"&gun_id="+gun_id+"&money="+money+"&pt="+pt)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String responseStr = response.body();
                        Log.i(PP_TIP, responseStr);
                        try {
                            JSONObject jsonObject = new JSONObject(responseStr);
                            int code = jsonObject.getInt("code");
                            if (code == 0) {
                                String url=jsonObject.getString("url");
                                showQrDialog(QrCreat.createQRImage(url),jsonObject.getString("str"),jsonObject.getString("id"));
                            } else{
                                Toast.makeText(CashOrderActivity.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 弹出二维码窗口
     */
    private void showQrDialog(final Bitmap bitmap, String str, final String id){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        View view = LayoutInflater.from(this).inflate(R.layout.qrcode_dialog, null);
        ImageView imageView=view.findViewById(R.id.iv_qrcode);
        Button btn_confirm=view.findViewById(R.id.btn_confirm);
        Button btn_xiadan=view.findViewById(R.id.btn_xiadan);
        TextView tv_str=view.findViewById(R.id.tv_str);
        tv_str.setText(str);
        builder.setView(view);
        final Dialog dialog=builder.create();
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_xiadan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmCash(id,dialog);
            }
        });
        imageView.setImageBitmap(bitmap);
        dialog.show();
    }

    protected boolean enableSliding() {
        return true;
    }

    /**
     * 确定现金收款
     */
    private void confirmCash(String id, final Dialog dialog) {
        Log.i(PP_TIP,RUOYU_URL + "?request=private.order.confirm_pay_cash_order&token=" + login_token + "&platform=android&id=" + id);
        OkGo.<String>post(RUOYU_URL + "?request=private.order.confirm_pay_cash_order&token=" + login_token + "&platform=android&id=" + id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            Log.i(PP_TIP,response.body());
                            JSONObject jsonObject = new JSONObject(response.body());
                            String msg = jsonObject.getString("msg");
                            int code =jsonObject.getInt("code");
                            if (code == 0){
                                dialog.dismiss();
                            }
                            Toast.makeText(CashOrderActivity.this, msg, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
