package com.ruoyw.pigpigroad.yichengchechebang.ZxingUtils;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.R;
import com.ruoyw.pigpigroad.yichengchechebang.SUNMI.BaseActivity;
import com.ruoyw.pigpigroad.yichengchechebang.SUNMI.util.AidlUtil;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

/**
 * Created by PIGROAD on 2018/6/5.
 * Email:920015363@qq.com
 */

public class OrderBackActivity extends BaseActivity {
    private LinearLayout ll_back, ll_orderback;
    private TextView tv_order_id;
    private Spinner spinner;
    private String login_token, reback_reason;
    private EditText et_password, et_order_reason;
    private Button btn_orderback;
    private int fee_type = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.order_back);
        init();
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
    }

    private void init() {
        ll_back = findViewById(R.id.back_ll);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_order_id = findViewById(R.id.tv_orderid);
        fee_type = getIntent().getIntExtra("fee_type", 1);
        tv_order_id.setText(getIntent().getStringExtra("order_id"));
        spinner = findViewById(R.id.spinner);
        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
        ll_orderback = findViewById(R.id.ll_orderback);
        et_password = findViewById(R.id.et_password);
        et_order_reason = findViewById(R.id.et_order_reason);
        btn_orderback = findViewById(R.id.btn_orderback);

        //退款按钮
        btn_orderback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断密码是否为空
                String password = et_password.getText().toString().trim();
                if (password.isEmpty() || password.equals("")) {
                    Toast.makeText(OrderBackActivity.this, "请输入退款密码", Toast.LENGTH_SHORT).show();
                } else {
                    ReBackOrder();
                }
            }
        });

        //下拉选择框
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] data = getResources().getStringArray(R.array.reson);
                reback_reason = data[i];
                if (i == 4) {
                    ll_orderback.setVisibility(View.VISIBLE);
                } else {
                    ll_orderback.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    protected boolean enableSliding() {
        return true;
    }

    /**
     * 退款操作
     */
    private void ReBackOrder() {
        final String id = tv_order_id.getText().toString().trim();
        final String password = et_password.getText().toString().trim();
        final String other = et_order_reason.getText().toString().trim();
        Log.i(PP_TIP,RUOYU_URL + "?request=private.refund.order.refund.action&token=" + login_token + "&platform=android&transaction_id=" + getIntent().getStringExtra("transaction_id") + "&fee_type=" + fee_type + "&refund_pwd=" + password + "&reason=" + reback_reason + other);
        OkGo.<String>post(RUOYU_URL + "?request=private.refund.order.refund.action&token=" + login_token + "&platform=android&transaction_id=" + getIntent().getStringExtra("transaction_id") + "&fee_type=" + fee_type + "&refund_pwd=" + password + "&reason=" + reback_reason + other)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String resonseStr = response.body();
                        Log.i(PP_TIP, "res:" + resonseStr);
                        try {
                            JSONObject jsonObject1 = new JSONObject(resonseStr);
                            int code = jsonObject1.getInt("code");
                            String msg = jsonObject1.getString("msg");
                            if (code == 0) {
                                Toast.makeText(OrderBackActivity.this, msg, Toast.LENGTH_SHORT).show();
                                getPrint(id);
                                finish();
                            } else {
                                Toast.makeText(OrderBackActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    //联网 补打小票
    private void getPrint(String orderid) {
        Log.i(PP_TIP, RUOYU_URL + "?request=private.refund.order.refund.ticket&token=" + login_token + "&platform=android&id=" + orderid);
        OkGo.<String>post(RUOYU_URL + "?request=private.refund.order.refund.ticket&token=" + login_token + "&platform=android&id=" + orderid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            int code = jsonObject.getInt("code");
                            if (code == 0) {
                                //语音播报操作
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
