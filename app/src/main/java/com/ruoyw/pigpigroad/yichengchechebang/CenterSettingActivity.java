package com.ruoyw.pigpigroad.yichengchechebang;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.CenterSettingAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.Bean.CenterSettingBean;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

public class CenterSettingActivity extends AppCompatActivity {

    private LinearLayout back_ll;
    private CenterSettingAdapter adapter;
    private List<CenterSettingBean> datas=new ArrayList<>();
    private RecyclerView recyclerView;
    private String login_token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.center_setting_layout);
        this.init();
    }

    /**
     * init
     */
    private void init() {
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
        back_ll=findViewById(R.id.back_ll);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView=findViewById(R.id.recycler_view);
        adapter=new CenterSettingAdapter(this,datas);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);


    }

    protected boolean enableSliding() {
        return true;
    }

    private void getData(){
        datas.clear();
        OkGo.<String>post(RUOYU_URL+"?request=private.setting_config.setting.config.list&token="+login_token+"&platform=android")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            int code =jsonObject.getInt("code");
                            if (code == 0){
                                JSONArray data=jsonObject.getJSONArray("data");
                                for (int i=0;i<data.length();i++){
                                    JSONObject temp=data.getJSONObject(i);
                                    CenterSettingBean bean=new CenterSettingBean();
                                    bean.setFunc_name(temp.getString("func_name"));
                                    bean.setId(temp.getString("id"));
                                    bean.setIs_show(temp.getString("is_show"));
                                    bean.setKey_tip(temp.getString("key_tip"));
                                    bean.setKey_title(temp.getString("key_title"));
                                    bean.setKey_value(temp.getString("key_value"));
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

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getData();
    }
}
