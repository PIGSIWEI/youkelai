package com.ruoyw.pigpigroad.yichengchechebang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;
import com.ruoyw.pigpigroad.yichengchechebang.Util.GridPasswordView;
import com.ruoyw.pigpigroad.yichengchechebang.Util.XKeyboardView;
import com.ruoyw.pigpigroad.yichengchechebang.ZxingUtils.view.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

/**
 * Created by PIGROAD on 2018/11/27
 * Email:920015363@qq.com
 */
public class CarScanActivity extends AppCompatActivity {
    private final int REQUEST_PERMISION_CODE_CAMARE = 0;
    private final int RESULT_REQUEST_CODE = 1;
    private GridPasswordView gpvPlateNumber;
    private XKeyboardView viewKeyboard;
    private String login_token,store_id;
    private Spinner spinner;
    private LinearLayout back_ll;
    private Button btn_scan;

    private List<String> list = new ArrayList<String>();
    private List<String> id_list = new ArrayList<String>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_car_layout);
        this.init();
    }
    protected boolean enableSliding() {
        return true;
    }
    private void init() {
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
        back_ll=findViewById(R.id.back_ll);
        btn_scan=findViewById(R.id.btn_scan);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences sp = this.getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
        SharedPreferences sp2 = getSharedPreferences("LoginInfo", MODE_PRIVATE);
        store_id = sp2.getString("store_id", null);
        gpvPlateNumber=findViewById(R.id.gpvPlateNumber);
        spinner=findViewById(R.id.spinner);
        //简单的string数组适配器：样式res，数组
        getData();
        testPlateNumberInput();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int x = 416;
                int y = 187;
                //利用ProcessBuilder执行shell命令
                String[] order = {
                        "input",
                        "tap",
                        "" + x,
                        "" + y
                };
                try {
                    new ProcessBuilder(order).start();
                    Log.i("ppppp","已经点击！");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (gpvPlateNumber.getPassWord().length()<7){
                   Toast.makeText(CarScanActivity.this,"请输入车牌号！",Toast.LENGTH_SHORT).show();
               }else {
                   Intent intent = new Intent(CarScanActivity.this, ZBarView.class);
                   startActivityForResult(intent, RESULT_REQUEST_CODE);
               }
            }
        });

    }

    private void testPlateNumberInput() {
        viewKeyboard = (XKeyboardView) findViewById(R.id.view_keyboard);
        viewKeyboard.setIOnKeyboardListener(new XKeyboardView.IOnKeyboardListener() {
            @Override
            public void onInsertKeyEvent(String text) {
                gpvPlateNumber.appendPassword(text);
            }

            @Override
            public void onDeleteKeyEvent() {
                gpvPlateNumber.deletePassword();
            }
        });
            gpvPlateNumber.togglePasswordVisibility();
        gpvPlateNumber.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public boolean beforeInput(int position) {
                if (position == 0) {
                    viewKeyboard.setKeyboard(new Keyboard(CarScanActivity.this, R.xml.provice));
                    viewKeyboard.setVisibility(View.VISIBLE);
                    return true;
                } else if (position >= 1 && position < 2) {
                    viewKeyboard.setKeyboard(new Keyboard(CarScanActivity.this, R.xml.english));
                    viewKeyboard.setVisibility(View.VISIBLE);
                    return true;
                } else if (position >= 2 && position < 6) {
                    viewKeyboard.setKeyboard(new Keyboard(CarScanActivity.this, R.xml.qwerty_without_chinese));
                    viewKeyboard.setVisibility(View.VISIBLE);
                    return true;
                } else if (position >= 6 && position < 7) {
                    if (gpvPlateNumber.getPassWord().startsWith("粤Z")) {
                        viewKeyboard.setKeyboard(new Keyboard(CarScanActivity.this, R.xml.qwerty));
                    } else {
                        viewKeyboard.setKeyboard(new Keyboard(CarScanActivity.this, R.xml.qwerty_without_chinese));
                    }
                    viewKeyboard.setVisibility(View.VISIBLE);
                    return true;
                }
                viewKeyboard.setVisibility(View.GONE);
                return false;
            }

            @Override
            public void onTextChanged(String psw) {
                Log.e("PlateNumberInputActivity", "onTextChanged：" + psw);
            }

            @Override
            public void onInputFinish(String psw) {
                Log.e("PlateNumberInputActivity", "onInputFinish：" + psw);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (viewKeyboard.isShown()) {
                viewKeyboard.setVisibility(View.GONE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 获取专车类型
     */
    private void getData(){
        OkGo.<String>post(RUOYU_URL+"?request=public.auth.get.car.type&token="+login_token+"&platform=android&store_id="+store_id)
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
                                    list.add(temp.getString("car_name"));
                                    id_list.add(temp.getString("type_id"));
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(CarScanActivity.this,
                                        android.R.layout.simple_spinner_item, list);
                                spinner.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_REQUEST_CODE:
                    if (data == null) return;
                    String type = data.getStringExtra(Constant.EXTRA_RESULT_CODE_TYPE);
                    String content = data.getStringExtra(Constant.EXTRA_RESULT_CONTENT);
                    Certification(content);
                    break;
                default:
                    break;

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     *  认证接口
     */
    private void Certification(final String content){
        int position=spinner.getSelectedItemPosition();
        OkGo.<String>post(RUOYU_URL+"?request=public.auth.pos.quick.car.auth&token="+login_token+"&platform=android&type_id="+id_list.get(spinner.getSelectedItemPosition())+"&car_no="+
                gpvPlateNumber.getPassWord()+"&userid="+content)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i(PP_TIP,RUOYU_URL+"?request=public.auth.pos.quick.car.auth&token="+login_token+"&platform=android&type_id="+id_list.get(spinner.getSelectedItemPosition())+"&car_no="+
                                gpvPlateNumber.getPassWord()+"&userid="+content);
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            Toast.makeText(CarScanActivity.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }



}
