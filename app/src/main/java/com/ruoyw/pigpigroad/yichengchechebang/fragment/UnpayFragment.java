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
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.CashAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.CashAdapter2;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.OnItemClickLitener;
import com.ruoyw.pigpigroad.yichengchechebang.Bean.PayBean;
import com.ruoyw.pigpigroad.yichengchechebang.Bean.UnpayBean;
import com.ruoyw.pigpigroad.yichengchechebang.R;
import com.ruoyw.pigpigroad.yichengchechebang.SUNMI.util.AidlUtil;
import com.ruoyw.pigpigroad.yichengchechebang.ZxingUtils.OrderBackActivity;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

/**
 * Created by PIGROAD on 2018/11/1
 * Email:920015363@qq.com
 */
public class UnpayFragment extends Fragment {

    private RecyclerView recyclerView2;
    private List<UnpayBean> datas2=new ArrayList<>();
    private CashAdapter2 adapter2;
    private String login_token;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View homepageLayout = inflater.inflate(R.layout.unpay_fragment,
                container, false);
        return homepageLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.init();
    }

    /**
     * 初始化
     */
    private void init() {
        recyclerView2=getActivity().findViewById(R.id.recycler_view2);
        adapter2=new CashAdapter2(datas2,getActivity());
        LinearLayoutManager linearLayout=new LinearLayoutManager(getActivity());
        recyclerView2.setLayoutManager(linearLayout);
        recyclerView2.setAdapter(adapter2);

        SharedPreferences sp = getActivity().getSharedPreferences("LoginUser", getActivity().MODE_PRIVATE);
        login_token = sp.getString("user_token", null);

        adapter2.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                showDetail(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }

    /**
     * 获取数据
     */

    private void checkpay() {
        datas2.clear();
        Log.i(PP_TIP,RUOYU_URL + "?request=private.order.check_pay_cash_order_paid&token=" + login_token + "&platform=android");
        OkGo.<String>post(RUOYU_URL + "?request=private.order.check_pay_cash_order_paid&token=" + login_token + "&platform=android")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //注意这里已经是在主线程了
                        Log.i(PP_TIP,response.body());
                        String responseStr = response.body();//这个就是返回来的结果
                        try {
                            JSONObject jsonObject = new JSONObject(responseStr);
                            int code = (int) jsonObject.get("code");
                            if (code == 0) {
                                final JSONArray jsonArray = jsonObject.getJSONArray("data");
                                //判断code的状态，如果code为0表示成功，1参数错误，999身份失效
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    UnpayBean bean=new UnpayBean();
                                    JSONObject temp = new JSONObject(jsonArray.getString((i)));
                                    bean.setId(temp.getString("id"));
                                    bean.setGun_number(temp.getString("gun_id") + "号油枪");
                                    bean.setPay_money(temp.getString("pay_money"));
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
                                    datas2.add(bean);
                                }
                                adapter2.notifyDataSetChanged();
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
    private void showDetail(final int position){
        final String order_id= datas2.get(position).getOrderid();
        final String pay_time= datas2.get(position).getPay_time();
        final String order_time= datas2.get(position).getOrder_time();
        final String name_= datas2.get(position).getName();
        final String merchant_oil_price= datas2.get(position).getMerchant_oil_price();
        final String use_oil_price= datas2.get(position).getUse_oil_price();
        final String oil_money= datas2.get(position).getOil_money();
        final String coupon_money= datas2.get(position).getCoupon_money();
        final String oil_lit= datas2.get(position).getOil_lit();
        final String oil_name= datas2.get(position).getOil_name();
        final String gun_id= datas2.get(position).getGun_id();
        final String pay_money= datas2.get(position).getPay_money();

        final TextView orderid,ordertime,name,gun,price,oillit,oilmoney,realpay,tv_pay_time;
        Button btn_ticketprint,btn_orderback;

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.backorder2, null);
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

        orderid=view.findViewById(R.id.orderid);
        ordertime=view.findViewById(R.id.order_time);
        name=view.findViewById(R.id.name);
        gun= view.findViewById(R.id.gun);
        price=view.findViewById(R.id.price);
        oillit=view.findViewById(R.id.oillit);
        oilmoney=view.findViewById(R.id.oilmoney);
        tv_pay_time=view.findViewById(R.id.pay_time);
        realpay=view.findViewById(R.id.realpay);
        realpay.setText(String.valueOf(pay_money)+"元(共优惠"+String.valueOf(coupon_money)+"元)");
        oilmoney.setText(String.valueOf(oil_money)+"元");
        oillit.setText(String.valueOf(oil_lit)+"升");
        price.setText("挂牌："+String.valueOf(use_oil_price)+"元/升(实际"+String.valueOf(merchant_oil_price)+"元/升)");
        gun.setText(String.valueOf(gun_id)+"号油枪 "+String.valueOf(oil_name));
        ordertime.setText(String.valueOf(order_time));
        name.setText(String.valueOf(name_));
        orderid.setText(String.valueOf(order_id));
        tv_pay_time.setText(pay_time);
        btn_ticketprint=view.findViewById(R.id.btn_ticketprint);
        btn_orderback=view.findViewById(R.id.btn_orderback);
        //小票补打
        btn_ticketprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1=new AlertDialog.Builder(view.getContext());
                builder1.setMessage("确定补打小票吗？");
                builder1.setTitle("提示");
                builder1.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        getPrint(order_id);
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
//        btn_orderback.setVisibility(View.GONE);
        //退款按钮
        btn_orderback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(v.getContext(), OrderBackActivity.class);
                intent.putExtra("order_id",order_id);
                intent.putExtra("fee_type",7);
                intent.putExtra("transaction_id",order_id);
                v.getContext().startActivity(intent);
            }
        });
    }

    /**
     * 打印支付信息
     */
    private void printTicket(String auth_code,String oilname,String oil,String price,String oillit,String oilgun,String money,String pay){
        final SimpleDateFormat simpledf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        final String time=simpledf.format(new Date());
        String title="     "+getString(R.string.app_name)+"支付小票--顾客联"+"\n";
        AidlUtil.getInstance().printText(title, 24, false, false);
        String vip="          高级会员          "+"\n";
        AidlUtil.getInstance().printText(vip, 24, false, false);
        String ticket="  出租车优惠1.3/升\n"
                +"  时间："+time+"\n"
                +"  订单："+auth_code+"\n"
                +"  油站："+oilname+"\n"
                +"  油品："+oil+"\n"
                +"  "+price+"\n"
                +"  油量："+oillit+"升"+"\n";
        AidlUtil.getInstance().printText(ticket, 24, false, false);
        String info=" 油枪："+oilgun+"号"+"\n"
                +" 合计："+money+"元"+"\n"
                +" 实付："+pay+"元"+"\n\n\n";
        AidlUtil.getInstance().printText(info, 48, true, false);

    }


    /**
     * 刷新 recycleview
     */
    private void refresh(){
        datas2.clear();
        checkpay();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){

        }else {
            showDialog();
            checkpay();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkpay();
    }

    /**
     * 延迟 操作
     */
    private void showDialog() {
        final ZLoadingDialog dialog = new ZLoadingDialog(getActivity());
        dialog.setLoadingBuilder(Z_TYPE.ROTATE_CIRCLE)//设置类型
                .setLoadingColor(Color.parseColor("#32cd32"))//颜色
                .setHintText("加载中")
                .setCanceledOnTouchOutside(false)
                .setHintTextSize(16f)
                .setCancelable(false)
                .show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //execute the task
                dialog.dismiss();
            }
        }, 2000);
    }
    //联网 补打小票
    private void getPrint(String orderid){
        OkGo.<String>post(RUOYU_URL+"?request=private.ticket.jiaoban.total.order.ticket.action&token="+login_token+"&platform=android&orderid="+orderid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            int code=jsonObject.getInt("code");
                            if (code == 0){
                                //语音播报操作
                                //打印小票操作
                                JSONArray ticket = jsonObject.getJSONArray("ticket");
//                                String title = ticket.getString("title");
//                                String who_discount = ticket.getString("who_discount");
//                                String pay_time = ticket.getString("pay_time");
//                                String orderid = ticket.getString("orderid");
//                                String store_name = ticket.getString("store_name");
//                                String oil_name = ticket.getString("oil_name");
//                                String price = ticket.getString("price");
//                                String lit = ticket.getString("lit");
//                                String gun_id = ticket.getString("gun_id");
//                                String total = ticket.getString("total");
//                                String pay_money = ticket.getString("pay_money");
//                                String pay_type=ticket.getString("pay_type");
//                                printTicket(title, who_discount, pay_time, store_name, orderid, oil_name, price, lit, gun_id, total, pay_money, id,pay_type);
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
