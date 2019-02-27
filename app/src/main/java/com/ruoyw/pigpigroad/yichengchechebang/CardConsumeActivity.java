package com.ruoyw.pigpigroad.yichengchechebang;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.CardConsumeAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.OnItemClickLitener;
import com.ruoyw.pigpigroad.yichengchechebang.Bean.CardConsumeBean;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

public class CardConsumeActivity extends AppCompatActivity {

    private LinearLayout ll_back;
    private RecyclerView recyclerView;
    private List<CardConsumeBean> datas=new ArrayList<>();
    private CardConsumeAdapter adapter;
    private String login_token;
    private TextView tv_total_pay_money,tv_total_order_money,tv_total_refund_money,tv_total_discount_money,tv_null;
    private ImageView iv_search;
    private int Year,month,day;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_consume_layout);
        this.init();
    }

    private void init() {

        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
        SlidingLayout rootView = new SlidingLayout(this);
        rootView.bindActivity(this);
        ll_back=findViewById(R.id.back_ll);
        recyclerView=findViewById(R.id.recycler_view);
        tv_total_pay_money=findViewById(R.id.tv_total_pay_money);
        tv_total_order_money=findViewById(R.id.tv_total_order_money);
        tv_total_refund_money=findViewById(R.id.tv_total_refund_money);
        tv_total_discount_money=findViewById(R.id.tv_total_discount_money);
        iv_search=findViewById(R.id.iv_search);
        tv_null=findViewById(R.id.tv_null);
        tv_null=findViewById(R.id.tv_null);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        adapter=new CardConsumeAdapter(this,datas);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                OrderDetailDialog(datas.get(position).getOrderid(),datas.get(position).getPay_time(),datas.get(position).getOrder_time(),datas.get(position).getGun_id()
                        ,datas.get(position).getOil_lit(),datas.get(position).getOil_money(),datas.get(position).getPay_money(),datas.get(position).getName());
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchDialog();
            }
        });

    }

    private void getData(){
        datas.clear();
        OkGo.<String>post(RUOYU_URL+"?request=private.card.order_card_ic&token="+login_token+"&platform=android")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            int code=jsonObject.getInt("code");
                            if (code == 0){
                                tv_null.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                JSONObject data=jsonObject.getJSONObject("data");
                                JSONArray dataArray=data.getJSONArray("data");
                                for (int i=0;i<dataArray.length();i++){
                                    JSONObject temp=dataArray.getJSONObject(i);
                                    CardConsumeBean bean=new CardConsumeBean();
                                    bean.setFluid(temp.getString("fluid"));
                                    bean.setGun_id(temp.getString("gun_id"));
                                    bean.setId(temp.getString("id"));
                                    bean.setName(temp.getString("name"));
                                    bean.setOil_money(temp.getString("oil_money"));
                                    bean.setOrderid(temp.getString("orderid"));
                                    bean.setOrder_status(temp.getString("order_status"));
                                    bean.setOrder_time(temp.getString("order_time"));
                                    bean.setOrder_money(temp.getString("order_money"));
                                    bean.setPay_money(temp.getString("pay_money"));
                                    bean.setPay_time(temp.getString("pay_time"));
                                    bean.setRefund_money(temp.getString("refund_money"));
                                    bean.setOil_lit(temp.getString("oil_lit"));
                                    datas.add(bean);
                                }
                                JSONObject total=data.getJSONObject("total");
                                tv_total_discount_money.setText("订单总折扣金额：   "+ total.getString("total_discount_money"));
                                tv_total_order_money.setText("订单总金额："+total.getString("total_order_money"));
                                tv_total_pay_money.setText("订单总支付金额："+total.getString("total_pay_money"));
                                tv_total_refund_money.setText("订单退款金额："+total.getString("total_refund_money"));
                                adapter.notifyDataSetChanged();
                            }else {
                                tv_null.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                tv_total_discount_money.setText("");
                                tv_total_order_money.setText("");
                                tv_total_pay_money.setText("");
                                tv_total_refund_money.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    /**
     * 点击出现 订单详情
     */
    private void OrderDetailDialog(String orderid,String pay_time,String order_time,String gun,String oillit,String oilmoney,String realpay,String name){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view =LayoutInflater.from(this).inflate(R.layout.card_consume_detail,null,false);
        builder.setView(view);
        Dialog dialog=builder.create();
        TextView tv_orderid,tv_pay_time,tv_order_time,tv_gun,tv_oillit,tv_oilmoney,tv_realpay,tv_name;
        tv_orderid=view.findViewById(R.id.tv_orderid);
        tv_pay_time=view.findViewById(R.id.tv_pay_time);
        tv_order_time=view.findViewById(R.id.tv_order_time);
        tv_gun=view.findViewById(R.id.tv_gun);
        tv_oillit=view.findViewById(R.id.tv_oillit);
        tv_oilmoney=view.findViewById(R.id.tv_oilmoney);
        tv_realpay=view.findViewById(R.id.tv_realpay);
        tv_name=view.findViewById(R.id.tv_name);
        tv_orderid.setText(orderid);
        tv_pay_time.setText(pay_time);
        tv_order_time.setText(order_time);
        tv_gun.setText(gun+"号油枪");
        tv_oillit.setText(oillit+"升");
        tv_oilmoney.setText(oilmoney+"元");
        tv_realpay.setText(realpay+"元");
        tv_name.setText(name);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dialog_animation);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.show();
    }

    /**
     * 搜索弹窗
     */
    private void SearchDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view =LayoutInflater.from(this).inflate(R.layout.search_dialog,null,false);
        final TextView tv_user_id=view.findViewById(R.id.tv_user_id);
        final TextView tv_oil=view.findViewById(R.id.tv_oil);
        final EditText tv_car_no=view.findViewById(R.id.tv_car_no);
        Button btn_cancel=view.findViewById(R.id.btn_cancel);
        Button btn_confirm=view.findViewById(R.id.btn_confirm);
        tv_user_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                Year = calendar.get(Calendar.YEAR);//获取当前年
                month = calendar.get(Calendar.MONTH);//获取月份，加1是因为月份是从0开始计算的
                day = calendar.get(Calendar.DATE);//获取日
                new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    //实现监听方法
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        //设置文本显示内容，i为年，i1为月，i2为日
                        tv_user_id.setText(i+"-"+(i1+1)+"-"+i2);
                        //以下赋值给全局变量，是为了后面的时间选择器，选择时间的时候不会获取不到日期！
                        Year=i;
                        month=i1+1;
                        day=i2;
                    }
                },Year,month,day).show();//记得使用show才能显示悬浮窗
            }
        });
        tv_oil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                Year = calendar.get(Calendar.YEAR);//获取当前年
                month = calendar.get(Calendar.MONTH);//获取月份，加1是因为月份是从0开始计算的
                day = calendar.get(Calendar.DATE);//获取日
                new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    //实现监听方法
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        //设置文本显示内容，i为年，i1为月，i2为日
                        tv_oil.setText(i+"-"+(i1+1)+"-"+i2);
                        //以下赋值给全局变量，是为了后面的时间选择器，选择时间的时候不会获取不到日期！
                        Year=i;
                        month=i1+1;
                        day=i2;
                    }
                },Year,month,day).show();//记得使用show才能显示悬浮窗
            }
        });
        builder.setView(view);
        final Dialog dialog=builder.create();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String order_number=tv_car_no.getText().toString().trim();
                String start_time=tv_user_id.getText().toString();
                String end_time=tv_oil.getText().toString();
                if (start_time.equals("点击选择开始时间")){
                    start_time="";
                }
                if (end_time.equals("点击选择结束时间")){
                    end_time="";
                }
                searchData(order_number,start_time,end_time);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void searchData(String order_number,String start_time,String over_time){
        datas.clear();
        Log.i(PP_TIP,RUOYU_URL+"?request=private.card.order_card_ic&token="+login_token+"&platform=android&card_no="+order_number+"&start_time="+start_time+"&over_time="+over_time);
        OkGo.<String>post(RUOYU_URL+"?request=private.card.order_card_ic&token="+login_token+"&platform=android&card_no="+order_number+"&start_time="+start_time+"&over_time="+over_time)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i(PP_TIP,response.body());
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            int code=jsonObject.getInt("code");
                            if (code == 0){
                                tv_null.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                JSONObject data=jsonObject.getJSONObject("data");
                                JSONArray dataArray=data.getJSONArray("data");
                                for (int i=0;i<dataArray.length();i++){
                                    JSONObject temp=dataArray.getJSONObject(i);
                                    CardConsumeBean bean=new CardConsumeBean();
                                    bean.setFluid(temp.getString("fluid"));
                                    bean.setGun_id(temp.getString("gun_id"));
                                    bean.setId(temp.getString("id"));
                                    bean.setName(temp.getString("name"));
                                    bean.setOil_money(temp.getString("oil_money"));
                                    bean.setOrderid(temp.getString("orderid"));
                                    bean.setOrder_status(temp.getString("order_status"));
                                    bean.setOrder_time(temp.getString("order_time"));
                                    bean.setOrder_money(temp.getString("order_money"));
                                    bean.setPay_money(temp.getString("pay_money"));
                                    bean.setPay_time(temp.getString("pay_time"));
                                    bean.setRefund_money(temp.getString("refund_money"));
                                    bean.setOil_lit(temp.getString("oil_lit"));
                                    datas.add(bean);
                                }
                                JSONObject total=data.getJSONObject("total");
                                tv_total_discount_money.setText("订单总折扣金额：   "+ total.getString("total_discount_money"));
                                tv_total_order_money.setText("订单总金额："+total.getString("total_order_money"));
                                tv_total_pay_money.setText("订单总支付金额："+total.getString("total_pay_money"));
                                tv_total_refund_money.setText("订单退款金额："+total.getString("total_refund_money"));
                                adapter.notifyDataSetChanged();
                            }else {
                                tv_null.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                tv_total_discount_money.setText("");
                                tv_total_order_money.setText("");
                                tv_total_pay_money.setText("");
                                tv_total_refund_money.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }



}
