package com.ruoyw.pigpigroad.yichengchechebang;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.OilSetttingAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.Bean.OilSettingBean;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.MyApplication;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

/**
 * Created by PIGROAD on 2018/12/4
 * Email:920015363@qq.com
 */
public class OilSettingActivity extends AppCompatActivity {


    private LinearLayout ll_back;
    private String login_token;
    private RecyclerView recycler_view;
    private List<OilSettingBean> datas=new ArrayList<>();
    private OilSetttingAdapter adapter;
    private LinearLayout ll_null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oilgun_layout);
        this.init();
    }

    private void init() {
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
        recycler_view=findViewById(R.id.recycler_view);
        ll_null=findViewById(R.id.ll_null);
        ll_back=findViewById(R.id.back_ll);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter=new OilSetttingAdapter(this,datas);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setAdapter(adapter);

        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);

        getData();
    }

    /**
     * 获取服务器信息
     */
    private void getData(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String imei=((TelephonyManager) MyApplication.getContext().getSystemService(TELEPHONY_SERVICE)).getDeviceId();
        Log.i(PP_TIP,RUOYU_URL+"?request=private.imei.get_bind_gun&token="+login_token+"&platform=android&imei="+imei);
        OkGo.<String>post(RUOYU_URL+"?request=private.imei.get_bind_gun&token="+login_token+"&platform=android&imei="+imei)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            int code =jsonObject.getInt("code");
                            if (code == 0){
                                JSONArray data=jsonObject.getJSONArray("data");
                                if (data.length() <=0){
                                    recycler_view.setVisibility(View.GONE);
                                    ll_null.setVisibility(View.VISIBLE);
                                }else {
                                    for (int i=0;i<data.length();i++){
                                        JSONObject temp=data.getJSONObject(i);
                                        OilSettingBean bean=new OilSettingBean();
                                        bean.setOil_gun(temp.getInt("gun_id"));
                                        datas.add(bean);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    protected boolean enableSliding() {
        return true;
    }

}
