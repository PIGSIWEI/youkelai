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
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.CardRechargeAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.OnItemClickLitener;
import com.ruoyw.pigpigroad.yichengchechebang.Bean.CardRechargeBean;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

public class CardReChargeActivity extends AppCompatActivity {

    private LinearLayout ll_back;
    private List<CardRechargeBean> datas=new ArrayList<>();
    private CardRechargeAdapter adapter;
    private RecyclerView recyclerView;
    private String login_token;
    private TextView tv_total_money,tv_total_count,tv_null;
    private ImageView iv_search;
    private int Year,month,day;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_recharge_layout);
        this.init();
    }

    private void init() {
        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
        SlidingLayout rootView = new SlidingLayout(this);
        rootView.bindActivity(this);
        ll_back=findViewById(R.id.back_ll);
        recyclerView=findViewById(R.id.recycler_view);
        tv_total_count=findViewById(R.id.tv_total_count);
        tv_total_money=findViewById(R.id.tv_total_money);
        tv_null=findViewById(R.id.tv_null);
        iv_search=findViewById(R.id.iv_search);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        adapter=new CardRechargeAdapter(this,datas);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                OrderDetailDialog(datas.get(position).getOrderid(),datas.get(position).getAddtime(),datas.get(position).getUserid()
                ,datas.get(position).getMoney(),datas.get(position).getTransaction_id(),datas.get(position).getCard_no(),
                        datas.get(position).getErr_status(),datas.get(position).getGift_money(),datas.get(position).getName());
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

    private void getData() {
        datas.clear();
        OkGo.<String>post(RUOYU_URL+"?request=private.card.charge_list_get&token="+login_token+"&platform=android")
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
                                    CardRechargeBean bean=new CardRechargeBean();
                                    bean.setAddtime(temp.getString("addtime"));
                                    bean.setCard_no(temp.getString("card_no"));
                                    bean.setErr_status(temp.getString("err_status"));
                                    bean.setGift_money(temp.getString("gift_money"));
                                    bean.setId(temp.getString("id"));
                                    bean.setMoney(temp.getString("money"));
                                    bean.setName(temp.getString("name"));
                                    bean.setOrderid(temp.getString("orderid"));
                                    bean.setPay_money(temp.getString("pay_money"));
                                    bean.setStatus(temp.getString("status"));
                                    bean.setStore_id(temp.getString("store_id"));
                                    bean.setTime_end(temp.getString("time_end"));
                                    bean.setTransaction_id(temp.getString("transaction_id"));
                                    bean.setUserid(temp.getString("userid"));
                                    datas.add(bean);
                                }
                                JSONObject total=data.getJSONObject("total");
                                tv_total_count.setText("当前充值卡总条数:"+total.getString("count"));
                                tv_total_money.setText("当前充值卡总金额:"+total.getString("total_money"));
                                adapter.notifyDataSetChanged();
                            }else {
                                tv_null.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                tv_total_money.setText("");
                                tv_total_count.setText("");
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
    private void OrderDetailDialog(String orderid,String addtime,String userid,String money,String transaction_id,String card_no,String err_status,String gift_money,String name){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view =LayoutInflater.from(this).inflate(R.layout.card_detail_item,null,false);
        builder.setView(view);
        Dialog dialog=builder.create();
        TextView tv_orderid,tv_addtime,tv_userid,tv_money,tv_transaction_id,tv_card_no,tv_err_status,tv_gift_money,tv_name;
        tv_orderid=view.findViewById(R.id.tv_orderid);
        tv_addtime=view.findViewById(R.id.tv_addtime);
        tv_userid=view.findViewById(R.id.tv_userid);
        tv_money=view.findViewById(R.id.tv_money);
        tv_transaction_id=view.findViewById(R.id.tv_transaction_id);
        tv_card_no=view.findViewById(R.id.tv_card_no);
        tv_err_status=view.findViewById(R.id.tv_err_status);
        tv_gift_money=view.findViewById(R.id.tv_gift_money);
        tv_name=view.findViewById(R.id.tv_name);
        tv_orderid.setText(orderid);
        tv_addtime.setText(addtime);
        tv_userid.setText(userid);
        tv_money.setText(money);
        tv_transaction_id.setText(transaction_id);
        tv_card_no.setText(card_no);
        if (err_status.equals("1")){
            tv_err_status.setText("已到账");
        }else {
            tv_err_status.setText("订单异常");
        }
        tv_gift_money.setText(gift_money);
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

    private void searchData(String order_number,String start_time,String over_time) {
        datas.clear();
        Log.i(PP_TIP,RUOYU_URL+"?request=private.card.charge_list_get&token="+login_token+"&platform=android&card_no="+order_number+"&start_time="+start_time+"&over_time="+over_time);
        OkGo.<String>post(RUOYU_URL+"?request=private.card.charge_list_get&token="+login_token+"&platform=android&card_no="+order_number+"&start_time="+start_time+"&over_time="+over_time)
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
                                    CardRechargeBean bean=new CardRechargeBean();
                                    bean.setAddtime(temp.getString("addtime"));
                                    bean.setCard_no(temp.getString("card_no"));
                                    bean.setErr_status(temp.getString("err_status"));
                                    bean.setGift_money(temp.getString("gift_money"));
                                    bean.setId(temp.getString("id"));
                                    bean.setMoney(temp.getString("money"));
                                    bean.setName(temp.getString("name"));
                                    bean.setOrderid(temp.getString("orderid"));
                                    bean.setPay_money(temp.getString("pay_money"));
                                    bean.setStatus(temp.getString("status"));
                                    bean.setStore_id(temp.getString("store_id"));
                                    bean.setTime_end(temp.getString("time_end"));
                                    bean.setTransaction_id(temp.getString("transaction_id"));
                                    bean.setUserid(temp.getString("userid"));
                                    datas.add(bean);
                                }
                                JSONObject total=data.getJSONObject("total");
                                tv_total_count.setText("当前充值卡总条数:"+total.getString("count"));
                                tv_total_money.setText("当前充值卡总金额:"+total.getString("total_money"));
                                adapter.notifyDataSetChanged();
                            }else {
                                tv_null.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                tv_total_money.setText("");
                                tv_total_count.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
