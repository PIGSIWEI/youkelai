package com.ruoyw.pigpigroad.yichengchechebang.Password;

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
import com.ruoyw.pigpigroad.yichengchechebang.R;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

/**
 * Created by PIGROAD on 2018/12/3
 * Email:920015363@qq.com
 */
public class BackOffPasswordChange extends AppCompatActivity {
    private LinearLayout back_ll;
    private EditText et_oldpwd, et_newpwd;
    private Button btn_confirm;
    private String login_token;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backoff_password_layout);
        init();

        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
    }
    //初始化
    private void init() {
        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
        back_ll = findViewById(R.id.back_ll);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        et_newpwd = findViewById(R.id.et_newpwd);
        et_oldpwd = findViewById(R.id.et_oldpwd);
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newpwd = et_newpwd.getText().toString().trim();
                String oldpwd = et_oldpwd.getText().toString().trim();
                if (newpwd.equals("")||oldpwd.equals("")){
                    Toast.makeText(BackOffPasswordChange.this, "请正确输入密码！", Toast.LENGTH_SHORT).show();
                }else {
                    ChangePwd(oldpwd,newpwd);
                }
            }
        });
    }

    protected boolean enableSliding() {
        return true;
    }

    //修改密码操作
    private void ChangePwd(String oldpwd, String newpwd) {
        OkGo.<String>post(RUOYU_URL + "?request=private.admin.update.refund.pwd.action&token=" + login_token + "&platform=android&old_password=" + oldpwd + "&password=" + newpwd)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String Str = response.body();
                        try {
                            JSONObject jsonObject = new JSONObject(Str);
                            int code = jsonObject.getInt("code");
                            String msg = jsonObject.getString("msg");
                            if (code == 0) {
                                Toast.makeText(BackOffPasswordChange.this, msg, Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(BackOffPasswordChange.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
