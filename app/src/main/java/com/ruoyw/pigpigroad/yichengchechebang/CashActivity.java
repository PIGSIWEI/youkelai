package com.ruoyw.pigpigroad.yichengchechebang;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;
import com.ruoyw.pigpigroad.yichengchechebang.ZxingUtils.view.Constant;
import com.ruoyw.pigpigroad.yichengchechebang.ZxingUtils.view.ScannerActivity;
import com.ruoyw.pigpigroad.yichengchechebang.fragment.PayFragment;
import com.ruoyw.pigpigroad.yichengchechebang.fragment.UnpayFragment;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

/**
 * Created by PIGROAD on 2018/10/31
 * Email:920015363@qq.com
 */
public class CashActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_pay, tv_paied;
    private LinearLayout ll_pay, ll_paied;
    private FloatingActionButton btn_scan;
    private final int RESULT_REQUEST_CODE = 1;
    private String login_token;
    private FragmentManager fragmentManager;
    private PayFragment payFragment;
    private LinearLayout back_ll;
    private UnpayFragment unpayFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cash_layout);
        this.init();
    }


    private void init() {
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }

        back_ll=findViewById(R.id.back_ll);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);

        ll_paied = findViewById(R.id.ll_paied);
        ll_pay = findViewById(R.id.ll_pay);
        tv_paied = findViewById(R.id.tv_paied);
        tv_pay = findViewById(R.id.tv_pay);
        btn_scan = findViewById(R.id.btn_scan);

        ll_paied.setOnClickListener(this);
        ll_pay.setOnClickListener(this);
        btn_scan.setOnClickListener(this);

        fragmentManager = getSupportFragmentManager();
        setSelectTab(0);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_pay:
                setSelectTab(0);
                break;
            case R.id.ll_paied:
                setSelectTab(1);
                break;
            case R.id.btn_scan:
                try {
                    Intent intent = new Intent(CashActivity.this, ZBarView.class);
                    startActivityForResult(intent, RESULT_REQUEST_CODE);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }

    }

    protected boolean enableSliding() {
        return true;
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
                                Toast.makeText(CashActivity.this, msg, Toast.LENGTH_SHORT).show();
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(CashActivity.this);
        LayoutInflater inflater = LayoutInflater.from(CashActivity.this);
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
                            Toast.makeText(CashActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * Fragment选择方法
     */
    public void setSelectTab(int index) {
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);

        switch (index) {
            case 0:
                ll_pay.setBackgroundColor(Color.parseColor("#7dbdfc"));
                tv_pay.setTextColor(Color.parseColor("#ffffff"));

                if (payFragment == null) {
                    payFragment = new PayFragment();
                    transaction.add(R.id.content, payFragment);

                } else {
                    transaction.show(payFragment);
                }
                break;
            case 1:
                ll_paied.setBackgroundColor(Color.parseColor("#7dbdfc"));
                tv_paied.setTextColor(Color.parseColor("#ffffff"));
                if (unpayFragment == null) {
                    unpayFragment = new UnpayFragment();
                    transaction.add(R.id.content, unpayFragment);
                } else {
                    transaction.show(unpayFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    /**
     * 清除fragment事物
     */
    private void clearSelection() {
        ll_pay.setBackgroundColor(Color.parseColor("#ffffff"));
        tv_pay.setTextColor(Color.parseColor("#000000"));
        ll_paied.setBackgroundColor(Color.parseColor("#ffffff"));
        tv_paied.setTextColor(Color.parseColor("#000000"));
    }


    private void hideFragments(FragmentTransaction transaction) {
        if (payFragment != null) {
            transaction.hide(payFragment);
        }
        if (unpayFragment != null) {
            transaction.hide(unpayFragment);
        }
    }

}
