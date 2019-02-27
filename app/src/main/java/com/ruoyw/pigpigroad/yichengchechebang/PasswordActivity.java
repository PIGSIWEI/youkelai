package com.ruoyw.pigpigroad.yichengchechebang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.Password.BackOffPasswordChange;
import com.ruoyw.pigpigroad.yichengchechebang.Password.CarPasswordChange;
import com.ruoyw.pigpigroad.yichengchechebang.Password.OilPasswordChange;
import com.ruoyw.pigpigroad.yichengchechebang.Password.PasswordChange;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

/**
 * Created by XYSM on 2018/3/17.
 */

public class PasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout back_ll;

    private LinearLayout ll_1,ll_2,ll_3,ll_4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_password_layout);
        init();

        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
    }

    //初始化
    private void init() {
        back_ll = findViewById(R.id.back_ll);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ll_1 = findViewById(R.id.ll_1);
        ll_2 = findViewById(R.id.ll_2);
        ll_3 = findViewById(R.id.ll_3);
        ll_4 = findViewById(R.id.ll_4);

        ll_1.setOnClickListener(this);
        ll_2.setOnClickListener(this);
        ll_3.setOnClickListener(this);
        ll_4.setOnClickListener(this);

    }

    protected boolean enableSliding() {
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_1:
                startActivity(new Intent(PasswordActivity.this,PasswordChange.class));
                break;
            case R.id.ll_2:
                startActivity(new Intent(PasswordActivity.this,BackOffPasswordChange.class));
                break;
            case R.id.ll_3:
                startActivity(new Intent(PasswordActivity.this,OilPasswordChange.class));
                break;
            case R.id.ll_4:
                startActivity(new Intent(PasswordActivity.this,CarPasswordChange.class));
                break;
        }
    }
}

