package com.ruoyw.pigpigroad.yichengchechebang;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.CarCouponAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.OnItemClickLitener;
import com.ruoyw.pigpigroad.yichengchechebang.Bean.CarCouponBean;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;
import com.ruoyw.pigpigroad.yichengchechebang.ZxingUtils.view.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

public class CarGunMoneyActivity extends AppCompatActivity {

    private LinearLayout ll_back;
    private String login_token;
    private EditText et_oilgun, et_money;
    private Button btn_cash_pay, btn_wx_pay;
    private String car_no;
    private final int RESULT_REQUEST_CODE = 1;
    private Dialog dialog;
    private Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            checkPay();
            handler.postDelayed(runnable, 2500);
        }
    };
    private String id;

    private String order_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_gun_money_layout);
        this.init();
    }

    /**
     * 初始化
     */
    private void init() {
        car_no = getIntent().getStringExtra("car_no");
        SlidingLayout rootView = new SlidingLayout(this);
        rootView.bindActivity(this);
        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
        ll_back = findViewById(R.id.back_ll);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        et_money = findViewById(R.id.et_money);
        et_oilgun = findViewById(R.id.et_oilgun);
        btn_cash_pay = findViewById(R.id.btn_cash_pay);
        btn_wx_pay = findViewById(R.id.btn_wx_pay);

        //现金支付操作
        btn_cash_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInput()) {
                    creatOrder("cash", et_oilgun.getText().toString().trim(), et_money.getText().toString().trim());
                } else {

                }
            }
        });

        //微信支付操作
        btn_wx_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInput()) {
                    creatOrder("weixin", et_oilgun.getText().toString().trim(), et_money.getText().toString().trim());
                } else {

                }
            }
        });

    }

    /**
     * 判断是否输入油枪金额
     */
    private boolean checkInput() {
        String money = et_money.getText().toString().trim();
        String oilgun = et_oilgun.getText().toString().trim();
        if (money.equals("") || oilgun.equals("")) {
            Toast.makeText(CarGunMoneyActivity.this, "请输入油枪和金额！", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    /**
     * 访问接口
     */
    private void creatOrder(final String pay_type, String oil_gun, String money) {
        Log.i(PP_TIP, RUOYU_URL + "?request=private.car_no.create_order_temp&token=" + login_token + "&platform=android&car_no=" + car_no + "&pay_type=" + pay_type + "&gun_id=" + oil_gun + "&mount=" + money);
        OkGo.<String>post(RUOYU_URL + "?request=private.car_no.create_order_temp&token=" + login_token + "&platform=android&car_no=" + car_no + "&pay_type=" + pay_type + "&gun_id=" + oil_gun + "&mount=" + money)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            int code = jsonObject.getInt("code");
                            if (code == 0) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                String user_id = data.getString("userid");
                                String oil_name = data.getString("oil_name");
                                String litre = data.getString("litre");
                                String gun_id = data.getString("gun_id");
                                String oil_money = data.getString("oil_money");
                                id = data.getString("id");
                                JSONArray coupon_list = data.getJSONArray("coupon_list");
                                showDiscountDialog(user_id, oil_name, litre, gun_id, oil_money, coupon_list, pay_type);
                            } else {
                                Toast.makeText(CarGunMoneyActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 弹出优惠窗口
     */
    private void showDiscountDialog(String user_id, String oil_name, String litre, String gun_id, String oil_money, JSONArray coupon_list, final String pay_type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.car_gun_money_dialog, null);
        builder.setView(view);
        final Dialog dialog = builder.create();
        TextView tv_car_no, tv_user_id, tv_oil, tv_oil_money, tv_oil_gun;
        RecyclerView recyclerView;
        LinearLayout ll_coupon_list;
        TextView tv_null;
        Button btn_cancel, btn_confirm;
        final CarCouponAdapter adapter;
        final List<CarCouponBean> datas = new ArrayList<>();
        tv_car_no = view.findViewById(R.id.tv_car_no);
        tv_user_id = view.findViewById(R.id.tv_user_id);
        tv_oil = view.findViewById(R.id.tv_oil);
        tv_oil_money = view.findViewById(R.id.tv_oil_money);
        ll_coupon_list = view.findViewById(R.id.ll_coupon_list);
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new CarCouponAdapter(view.getContext(), datas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        tv_oil_gun = view.findViewById(R.id.tv_oil_gun);
        tv_null = view.findViewById(R.id.tv_null);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_confirm = view.findViewById(R.id.btn_confirm);
        if (coupon_list.length() <= 0) {
            tv_null.setVisibility(View.VISIBLE);
            ll_coupon_list.setVisibility(View.GONE);
        } else {
            tv_null.setVisibility(View.GONE);
            ll_coupon_list.setVisibility(View.VISIBLE);
            for (int i = 0; i < coupon_list.length(); i++) {
                try {
                    JSONObject temp = coupon_list.getJSONObject(i);
                    CarCouponBean bean = new CarCouponBean();
                    bean.setCoupon_money(temp.getString("coupon_money"));
                    bean.setCoupon_name(temp.getString("coupon_name"));
                    bean.setId(temp.getString("id"));
                    datas.add(bean);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            CarCouponBean bean = new CarCouponBean();
            bean.setCoupon_money("");
            bean.setCoupon_name("不使用优惠券");
            bean.setId("0");
            datas.add(bean);
            adapter.notifyDataSetChanged();
        }
        tv_car_no.setText(car_no);
        tv_user_id.setText(user_id);
        tv_oil_money.setText("￥" + oil_money);
        tv_oil.setText(oil_name + "/" + litre + "L");
        tv_oil_gun.setText(gun_id + "号油枪");

        adapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                adapter.setSelection(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断是现金还是微信
                if (pay_type.equals("cash")) {
                    if (adapter.getSelection() < 0) {
                        //没有选择优惠券
                        confirmOrder("0");
                    } else {
                        confirmOrder(datas.get(adapter.getSelection()).getId());
                    }
                } else {
                    Intent intent = new Intent(CarGunMoneyActivity.this, ZBarView.class);
                    startActivityForResult(intent, RESULT_REQUEST_CODE);
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * 确认订单接口
     */
    private void confirmOrder(String coupon_id) {
        Log.i(PP_TIP, RUOYU_URL + "?request=private.car_no.submit_order_cash&token=" + login_token + "&platform=android&id=" + id + "&coupon_id=" + coupon_id);
        OkGo.<String>post(RUOYU_URL + "?request=private.car_no.submit_order_cash&token=" + login_token + "&platform=android&id=" + id + "&coupon_id=" + coupon_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            Toast.makeText(CarGunMoneyActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_REQUEST_CODE:
                    if (data == null) return;
                    String type = data.getStringExtra(Constant.EXTRA_RESULT_CODE_TYPE);
                    String content = data.getStringExtra(Constant.EXTRA_RESULT_CONTENT);
                    scanPost(content);
                    break;
                default:
                    break;

            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 扫描操作
     */
    private void scanPost(String auth_code) {
        Log.i(PP_TIP, RUOYU_URL + "?request=private.car_no.submit_order_wx&token=" + login_token + "&platform=android&id=" + id + "&auth_code=" + auth_code);
        OkGo.<String>post(RUOYU_URL + "?request=private.car_no.submit_order_wx&token=" + login_token + "&platform=android&id=" + id + "&auth_code=" + auth_code)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            Log.i(PP_TIP, response.body());
                            JSONObject jsonObject = new JSONObject(response.body());
                            int code = jsonObject.getInt("code");
                            if (code == 0) {
                                showDialog(1, "付款成功！");
                            } else if (code == 1) {
                                String msg = jsonObject.getString("msg");
                                if (msg.equals("需要用户输入支付密码")) {
                                    order_id = jsonObject.getString("order_id");
                                    showDialog2(2, "正在等待用户支付,请稍后");
                                } else {
                                    showDialog(3, "付款失败！");
                                }
                            } else {
                                showDialog(3, "付款失败！");
                            }
                            Toast.makeText(CarGunMoneyActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 扫码弹出
     */
    private void showDialog(int station, String txt) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.wx_pay_dialog, null);
        builder.setView(view);
        final Dialog dialog = builder.create();
        ImageView iv = view.findViewById(R.id.iv);
        TextView tv = view.findViewById(R.id.tv);
        Button btn = view.findViewById(R.id.btn);
        switch (station) {
            case 1:
                iv.setBackgroundResource(R.drawable.icon_succes);
                break;
            case 2:
                iv.setBackgroundResource(R.drawable.icon_wating);
                btn.setText("用户已支付");
                break;
            case 3:
                iv.setBackgroundResource(R.drawable.icon_errow);
                break;
        }
        tv.setText(txt);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 扫码弹出
     */
    private void showDialog2(int station, String txt) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.wx_pay_dialog, null);
        builder.setView(view);
        builder.setCancelable(false);
        dialog = builder.create();
        ImageView iv = view.findViewById(R.id.iv);
        TextView tv = view.findViewById(R.id.tv);
        Button btn = view.findViewById(R.id.btn);
        ProgressBar progress_circular = view.findViewById(R.id.progress_circular);
        switch (station) {
            case 2:
                iv.setVisibility(View.GONE);
                progress_circular.setVisibility(View.VISIBLE);
                handler.postDelayed(runnable, 2500);
                btn.setText("用户已付款");
                break;
        }
        tv.setText(txt);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                handler.removeCallbacks(runnable);
            }
        });
        dialog.show();
    }

    /**
     * 查询 用户是否支付
     */
    private void checkPay() {
        Log.i(PP_TIP, RUOYU_URL + "?request=private.car_no.check_order_wx_paying&token=" + login_token + "&platform=android&order_id=" + order_id);
        OkGo.<String>post(RUOYU_URL + "?request=private.car_no.check_order_wx_paying&token=" + login_token + "&platform=android&order_id=" + order_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            int code = jsonObject.getInt("code");
                            if (code == 0) {
                                String msg = jsonObject.getString("msg");
                                if (msg.equals("扣款成功")) {
                                    dialog.dismiss();
                                    handler.removeCallbacks(runnable);
                                    Toast.makeText(CarGunMoneyActivity.this, "扣款成功!!", Toast.LENGTH_SHORT).show();
                                }else {
                                    JSONObject data=jsonObject.getJSONObject("data");
                                    dialog.dismiss();
                                    handler.removeCallbacks(runnable);
                                    showDialog(3,"订单未支付！");
                                    Toast.makeText(CarGunMoneyActivity.this,data.getString("trade_state_desc"),Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(CarGunMoneyActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
}
