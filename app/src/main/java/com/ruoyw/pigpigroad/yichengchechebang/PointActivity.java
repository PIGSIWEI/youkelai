package com.ruoyw.pigpigroad.yichengchechebang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;
import com.ruoyw.pigpigroad.yichengchechebang.ZxingUtils.view.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

public class PointActivity extends AppCompatActivity {

    private LinearLayout back_ll;
    private EditText et_point;
    private CheckBox cb_add,cb_minus;
    private LinearLayout ll_point;
    private Spinner spinner;
    private Button btn_scan;
    private final int REQUEST_PERMISION_CODE_CAMARE = 0;
    private final int RESULT_REQUEST_CODE = 1;
    private String login_token;
    private ImageView iv_search;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.point_layout);
        this.init();
    }

    private void init() {
        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
        back_ll=findViewById(R.id.back_ll);
        et_point=findViewById(R.id.et_point);
        cb_add=findViewById(R.id.cb_add);
        cb_minus=findViewById(R.id.cb_minus);
        ll_point=findViewById(R.id.ll_point);
        spinner=findViewById(R.id.spinner);
        btn_scan=findViewById(R.id.btn_scan);
        iv_search=findViewById(R.id.iv_search);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }

        cb_add.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    cb_add.setChecked(true);
                    cb_minus.setChecked(false);
                }else {
                    cb_add.setChecked(false);
                    cb_minus.setChecked(true);
                }
            }
        });

        cb_minus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    cb_add.setChecked(false);
                    cb_minus.setChecked(true);
                }else {
                    cb_add.setChecked(true);
                    cb_minus.setChecked(false);
                }
            }
        });

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String point=et_point.getText().toString();
                if (point.equals("")){
                    Toast.makeText(PointActivity.this,"请输入积分！",Toast.LENGTH_SHORT).show();
                }else {
                    if (!cb_add.isChecked()&&!cb_minus.isChecked()){
                        Toast.makeText(PointActivity.this,"请选择增加积分还是减少积分操作！",Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent = new Intent(PointActivity.this, ZBarView.class);
                        startActivityForResult(intent, RESULT_REQUEST_CODE);
                    }
                }
            }
        });

        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PointActivity.this,PointSearchActivity.class));
            }
        });

    }

    protected boolean enableSliding() {
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_REQUEST_CODE:
                    if (data == null) return;
                    String type = data.getStringExtra(Constant.EXTRA_RESULT_CODE_TYPE);
                    String content = data.getStringExtra(Constant.EXTRA_RESULT_CONTENT);
                    String point = et_point.getText().toString();
                    String reason = spinner.getSelectedItem().toString();
                    int reason_id = 0;
                    switch (reason){
                        case "卡积分归集":
                            reason_id=101;
                            break;
                        case "购物":
                            reason_id=102;
                            break;
                        case "赠送":
                            reason_id=103;
                            break;
                    }
                    if (cb_add.isChecked()) {
                        checkData(point,content,1,reason_id);
                    } else {
                        checkData(point,content,0,reason_id);
                    }
                    break;
                default:
                    break;

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 查询接口
     */
    private void checkData(String point, String userid, int contro, int reason){
        OkGo.<String>post(RUOYU_URL+"?request=private.point.update.user.point&token="+login_token+"&platform=android&point="+point+"&userid="+userid+"&contro="+contro+"&reason="+reason)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            Log.i(PP_TIP,response.body());
                            JSONObject jsonObject=new JSONObject(response.body());
                            int code =jsonObject.getInt("code");
                            if (code == 0){
                                et_point.setText("");
                                cb_add.setChecked(false);
                                cb_minus.setChecked(false);
                            }else {

                            }
                            Toast.makeText(PointActivity.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
