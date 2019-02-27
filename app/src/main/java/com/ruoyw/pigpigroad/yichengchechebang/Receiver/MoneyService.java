package com.ruoyw.pigpigroad.yichengchechebang.Receiver;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

/**
 * Created by PIGROAD on 2018/10/30
 * Email:920015363@qq.com
 */
public class MoneyService extends Service {

    private String login_token;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //拿token
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
    }

    private void CheckMoney(){
        OkGo.<String>post(RUOYU_URL+"?request=private.order.check_pay_cash_order&token="+login_token+"&platform=android")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            int code =jsonObject.getInt("code");
                            if (code == 0){

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


}
