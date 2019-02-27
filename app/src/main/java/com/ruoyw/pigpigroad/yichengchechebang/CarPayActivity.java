package com.ruoyw.pigpigroad.yichengchechebang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.CarAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.OnItemClickLitener;
import com.ruoyw.pigpigroad.yichengchechebang.Bean.CarBean;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

public class CarPayActivity extends AppCompatActivity {

    private RecyclerView recycler_view;
    private List<CarBean> datas=new ArrayList<>();
    private CarAdapter adapter;
    private LinearLayout ll_back;
    private String login_token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_pay_layout);
        this.init();
    }

    /**
     * 初始化
     */
    private void init() {
        SlidingLayout rootView = new SlidingLayout(this);
        rootView.bindActivity(this);
        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
        recycler_view=findViewById(R.id.recycler_view);
        ll_back=findViewById(R.id.back_ll);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        adapter=new CarAdapter(this,datas);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        recycler_view.setAdapter(adapter);
        recycler_view.setLayoutManager(gridLayoutManager);

        getData();

        adapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                CheckNumber(datas.get(position).getCar_no());
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
        OkGo.<String>post(RUOYU_URL+"?request=private.car_no.get.car.no.list&token="+login_token+"&platform=android")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            int code=jsonObject.getInt("code");
                            if (code == 0){
                                JSONArray data=jsonObject.getJSONArray("data");
                                for (int i=0;i<data.length();i++){
                                    JSONObject temp=data.getJSONObject(i);
                                    CarBean bean=new CarBean();
                                    bean.setCar_no(temp.getString("car_no"));
                                    bean.setDate(temp.getString("date"));
                                    datas.add(bean);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 点击车牌号查询
     */
    private void CheckNumber(final String car_no){
        OkGo.<String>post(RUOYU_URL+"?request=private.car_no.check.car.no.status&token="+login_token+"&platform=android&car_no="+car_no)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            int code =jsonObject.getInt("code");
                            if (code == 0){
                                Intent intent=new Intent(CarPayActivity.this,CarGunMoneyActivity.class);
                                intent.putExtra("car_no",car_no);
                                startActivity(intent);
                            }
                            Toast.makeText(CarPayActivity.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
