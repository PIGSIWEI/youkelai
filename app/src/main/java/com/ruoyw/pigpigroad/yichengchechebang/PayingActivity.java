package com.ruoyw.pigpigroad.yichengchechebang;

import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.OnItemClickLitener;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.PayingAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.Bean.PayingBean;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

public class PayingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayout ll_back;
    private List<PayingBean> datas=new ArrayList<>();
    private PayingAdapter adapter;
    private String store_id;
    private LinearLayout ll_null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paying_layout);
        this.init();
    }

    /**
     * 初始化
     */
    private void init() {
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
        ll_back=findViewById(R.id.back_ll);
        ll_null=findViewById(R.id.ll_null);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView=findViewById(R.id.recycler_view);
        adapter=new PayingAdapter(this,datas);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        SharedPreferences sp2 = getSharedPreferences("LoginInfo", MODE_PRIVATE);
        store_id = sp2.getString("store_id", null);
        getData();

        adapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                checkState(datas.get(position).getId());
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }

    /**
     * 获取数据
     */
    private void getData(){
        datas.clear();
        Log.i(PP_TIP,RUOYU_URL+"?request=public.pay.order_wx_paying_list&platform=android&store_id="+store_id);
        OkGo.<String>post(RUOYU_URL+"?request=public.pay.order_wx_paying_list&platform=android&store_id="+store_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i(PP_TIP,response.body());
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            int code =jsonObject.getInt("code");
                            if (code == 0){
                                JSONArray data=jsonObject.getJSONArray("data");
                                if (data.length() <= 0){
                                    recyclerView.setVisibility(View.GONE);
                                    ll_null.setVisibility(View.VISIBLE);
                                }else {
                                    for (int i=0;i<data.length();i++){
                                        JSONObject temp=data.getJSONObject(i);
                                        PayingBean bean=new PayingBean();
                                        bean.setOilgun(temp.getString("gun_id"));
                                        bean.setOilmoney(temp.getString("money"));
                                        bean.setOiltime(temp.getString("order_time"));
                                        bean.setId(temp.getString("id"));
                                        datas.add(bean);
                                    }
                                    adapter.notifyDataSetChanged();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            recyclerView.setVisibility(View.GONE);
                            ll_null.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    protected boolean enableSliding() {
        return true;
    }

    /**
     * 查询 状态
     */
    private void checkState(final String id){
        OkGo.<String>post(RUOYU_URL+"?request=public.pay.pos_check.wx.paying.order&platform=android&id="+id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            int code =jsonObject.getInt("code");
                            if (code == 2){
                                JSONObject data=jsonObject.getJSONObject("data");
                                showInfo(data.getString("orderid"),data.getString("order_time")
                                ,data.getString("money"),data.getString("oil_lit"),id
                                );
                            }else {
                                Toast.makeText(PayingActivity.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void showInfo(String order_id, String order_time, String order_money, String order_lit, final String id){
        TextView orderid,paytime,oillit,oilmoney;
        Button btn_ticketprint;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.paying_dialog, null);
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
        orderid=view.findViewById(R.id.orderid);
        paytime=view.findViewById(R.id.paytime);
        btn_ticketprint=view.findViewById(R.id.btn_ticketprint);
        oillit=view.findViewById(R.id.oillit);
        oilmoney=view.findViewById(R.id.oilmoney);
        orderid.setText(order_id);
        paytime.setText(order_time);
        oillit.setText(order_lit+"升");
        oilmoney.setText(order_money+"元");
        //查询订单状态
        btn_ticketprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSearch(id,dialog);
            }
        });
    }

    /**
     * 点击 查询
     */
    private void clickSearch(String id, final Dialog dialog){
        OkGo.<String>post(RUOYU_URL+"?request=public.pay.pos_check.wx.paying.order&platform=android&id="+id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            int code =jsonObject.getInt("code");
                            if (code == 2){
                                Toast.makeText(PayingActivity.this,"该订单尚未支付",Toast.LENGTH_SHORT).show();
                            }else {
                                dialog.dismiss();
                                getData();
                                Toast.makeText(PayingActivity.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
