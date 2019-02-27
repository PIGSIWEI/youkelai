package com.ruoyw.pigpigroad.yichengchechebang;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

public class CenterParamActivity extends AppCompatActivity {

    private LinearLayout back_ll;
    private String login_token;
    private String func_name,key_title,key_tip,id,key_value;
    private TextView tv_title,tv_country_price;
    private EditText et_value;
    private Button change_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.center_param_layout);
        this.init();
    }

    private void init() {
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
        func_name=getIntent().getStringExtra("func_name");
        key_title=getIntent().getStringExtra("key_title");
        key_tip=getIntent().getStringExtra("key_tip");
        id=getIntent().getStringExtra("id");
        key_value=getIntent().getStringExtra("key_value");

        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
        back_ll=findViewById(R.id.back_ll);
        tv_country_price=findViewById(R.id.tv_country_price);
        tv_title=findViewById(R.id.tv_title);
        et_value=findViewById(R.id.et_value);
        change_btn=findViewById(R.id.change_btn);
        et_value.setHint(key_value);
        tv_title.setText(key_title);
        tv_country_price.setText(key_tip);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_value.getText().toString().equals("")){
                    Toast.makeText(CenterParamActivity.this,"请输入要修改的参数值！",Toast.LENGTH_SHORT).show();
                }else {
                    ChangeValue(et_value.getText().toString());
                }
            }
        });
    }

    protected boolean enableSliding() {
        return true;
    }

    /**
     * 修改参数
     */
    private void ChangeValue(String key_value2){
        Log.i(PP_TIP,RUOYU_URL+"?request=private.setting_config.exec.func&token="+login_token+"&platform=android&id="+id+"&func_name="+func_name+"&key_value="+key_value2);
        OkGo.<String>post(RUOYU_URL+"?request=private.setting_config.exec.func&token="+login_token+"&platform=android&id="+id+"&func_name="+func_name+"&key_value="+key_value2)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            int code=jsonObject.getInt("code");
                            if (code==0){
                                finish();
                            }
                            Toast.makeText(CenterParamActivity.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
