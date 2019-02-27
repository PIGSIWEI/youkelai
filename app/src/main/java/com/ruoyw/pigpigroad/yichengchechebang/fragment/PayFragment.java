package com.ruoyw.pigpigroad.yichengchechebang.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.CashAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.OnItemClickLitener;
import com.ruoyw.pigpigroad.yichengchechebang.Bean.PayBean;
import com.ruoyw.pigpigroad.yichengchechebang.R;
import com.ruoyw.pigpigroad.yichengchechebang.Util.CustomLinearLayoutManager;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

/**
 * Created by PIGROAD on 2018/11/1
 * Email:920015363@qq.com
 */
public class PayFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<PayBean> datas = new ArrayList<>();
    private CashAdapter adapter;
    private String login_token;
    private String getJson;
    private Handler handler = new Handler();
    CustomLinearLayoutManager  linearLayout = new CustomLinearLayoutManager(getActivity());

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            checkData();
            handler.postDelayed(runnable, 5000);
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View homepageLayout = inflater.inflate(R.layout.pay_fragment,
                container, false);
        return homepageLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.init();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 初始化
     */
    private void init() {
        recyclerView = getActivity().findViewById(R.id.recycler_view);
        adapter = new CashAdapter(datas, getActivity());
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setAdapter(adapter);

        SharedPreferences sp = getActivity().getSharedPreferences("LoginUser", getActivity().MODE_PRIVATE);
        login_token = sp.getString("user_token", null);

        adapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                showDetail(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        getData();
        handler.postDelayed(runnable, 5000);

    }

    /**
     * 获取数据
     */

    private void getData() {
        datas.clear();
        adapter.notifyDataSetChanged();
        OkGo.<String>post(RUOYU_URL + "?request=private.order.check_pay_cash_order&token=" + login_token + "&platform=android")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //注意这里已经是在主线程了
                        String responseStr = response.body();//这个就是返回来的结果
                        try {
                            JSONObject jsonObject = new JSONObject(responseStr);
                            Log.i(PP_TIP,response.body());
                            int code = (int) jsonObject.get("code");
                            if (code == 0) {
                                final JSONArray jsonArray = jsonObject.getJSONArray("data");
                                //判断code的状态，如果code为0表示成功，1参数错误，999身份失效
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    PayBean bean = new PayBean();
                                    JSONObject temp = new JSONObject(jsonArray.getString((i)));
                                    bean.setId(temp.getString("id"));
                                    bean.setGun_number(temp.getString("gun_id") + "号油枪");
                                    bean.setPay_money(temp.getString("oil_money"));
                                    bean.setGun_kind(temp.getString("oil_name"));
                                    bean.setPay_time(temp.getString("pay_time"));
                                    bean.setOrderid(temp.getString("orderid"));
                                    bean.setOrder_time(temp.getString("order_time"));
                                    bean.setName(temp.getString("name"));
                                    bean.setMerchant_oil_price(temp.getString("merchant_oil_price"));
                                    bean.setUse_oil_price(temp.getString("use_oil_price"));
                                    bean.setOil_money(temp.getString("oil_money"));
                                    bean.setCoupon_money(temp.getString("coupon_money"));
                                    bean.setGun_id(temp.getString("gun_id"));
                                    bean.setOil_name(temp.getString("oil_name"));
                                    bean.setOil_lit(temp.getString("oil_lit"));
                                    datas.add(bean);
                                }
                                adapter.notifyDataSetChanged();
                                getJson = response.body();
                                linearLayout.setScrollEnabled(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    /**
     * 点击弹出事件
     */
    private void showDetail(final int position) {
        final String order_id = datas.get(position).getOrderid();
        final String pay_time = datas.get(position).getPay_time();
        final String order_time = datas.get(position).getOrder_time();
        final String name_ = datas.get(position).getName();
        final String merchant_oil_price = datas.get(position).getMerchant_oil_price();
        final String use_oil_price = datas.get(position).getUse_oil_price();
        final String oil_money = datas.get(position).getOil_money();
        final String coupon_money = datas.get(position).getCoupon_money();
        final String oil_lit = datas.get(position).getOil_lit();
        final String oil_name = datas.get(position).getOil_name();
        final String gun_id = datas.get(position).getGun_id();
        final String pay_money = datas.get(position).getPay_money();

        TextView orderid, paytime, ordertime, name, gun, price, oillit, oilmoney, realpay;
        Button btn_ticketprint,btn_confirm;

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.cash_unpay_dialog, null);
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
        orderid = view.findViewById(R.id.orderid);
        ordertime = view.findViewById(R.id.order_time);
        btn_confirm = view.findViewById(R.id.btn_confirm);
        name = view.findViewById(R.id.name);
        gun = view.findViewById(R.id.gun);
        realpay = view.findViewById(R.id.realpay);
        price = view.findViewById(R.id.price);
        oillit = view.findViewById(R.id.oillit);
        oilmoney = view.findViewById(R.id.oilmoney);
        oilmoney.setText(String.valueOf(oil_money) + "元");
        oillit.setText(String.valueOf(oil_lit) + "升");
        realpay.setText(pay_money+"元");
        price.setText("挂牌：" + String.valueOf(use_oil_price) + "元/升(实际" + String.valueOf(merchant_oil_price) + "元/升)");
        gun.setText(String.valueOf(gun_id) + "号油枪 " + String.valueOf(oil_name));
        ordertime.setText(String.valueOf(order_time));
        name.setText(String.valueOf(name_));
        orderid.setText(String.valueOf(order_id));
        btn_ticketprint = view.findViewById(R.id.btn_ticketprint);
        //撤销现金
        btn_ticketprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                builder1.setMessage("确定撤销现金收款吗？");
                builder1.setTitle("提示");
                builder1.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                        cancelCash(datas.get(position).getId());
                        showDialog();
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
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
                builder1.setMessage("确定现金收款嘛？");
                builder1.setTitle("提示");
                builder1.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                        confirmCash(datas.get(position).getId());
                        showDialog();
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
     * 确定 现金 收款
     */
    private void confirmCash(String id) {
        OkGo.<String>post(RUOYU_URL + "?request=private.order.confirm_pay_cash_order&token=" + login_token + "&platform=android&id=" + id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            String msg = jsonObject.getString("msg");
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 确定 撤销 现金订单
     */
    private void cancelCash(String id) {
        OkGo.<String>post(RUOYU_URL + "?request=private.order.delete.cash.order&token=" + login_token + "&platform=android&id=" + id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            String msg = jsonObject.getString("msg");
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 检测数据是否更新
     */
    private void checkData() {
        OkGo.<String>post(RUOYU_URL + "?request=private.order.check_pay_cash_order&token=" + login_token + "&platform=android")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //注意这里已经是在主线程了
                        String responseStr = response.body();//这个就是返回来的结果
                        try {
                            JSONObject jsonObject = new JSONObject(responseStr);
                            int code = (int) jsonObject.get("code");
                            if (code == 0) {
                                if (!getJson.equals(response.body())) {
                                    linearLayout.setScrollEnabled(false);
                                    showDialog();
                                } else {

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }

    /**
     * 延迟 操作
     */
    private void showDialog() {

        Toast.makeText(getActivity(),"加载中···",Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                //execute the task
                getData();
                Log.i(PP_TIP,"1");
            }
        }, 2000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
