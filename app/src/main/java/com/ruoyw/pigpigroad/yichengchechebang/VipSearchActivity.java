package com.ruoyw.pigpigroad.yichengchechebang;

import android.content.SharedPreferences;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.VipSearchAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.Bean.VipSearchBean;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;
import com.ruoyw.pigpigroad.yichengchechebang.Util.GridPasswordView;
import com.ruoyw.pigpigroad.yichengchechebang.Util.XKeyboardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

public class VipSearchActivity extends AppCompatActivity {

    private XKeyboardView viewKeyboard;
    private GridPasswordView gpvPlateNumber;
    private RecyclerView recyclerView;
    private LinearLayout ll_back,ll_main;
    private Button btn_search;
    private EditText et_number,et_orderid;
    private RelativeLayout rl_number,rl_orderid;
    private String login_token;
    private List<VipSearchBean> datas=new ArrayList<>();
    private VipSearchAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vip_search_layout);
        this.init();
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
    }

    protected boolean enableSliding() {
        return true;
    }

    /**
     * 初始化
     */
    private void init() {
        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
        gpvPlateNumber=findViewById(R.id.gpvPlateNumber);
        ll_main=findViewById(R.id.ll_main);
        ll_back=findViewById(R.id.back_ll);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView=findViewById(R.id.recycler_view);
        btn_search=findViewById(R.id.btn_search);
        et_orderid=findViewById(R.id.et_orderid);
        et_number=findViewById(R.id.et_number);
        rl_number=findViewById(R.id.rl_number);
        rl_orderid=findViewById(R.id.rl_orderid);
        rl_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewKeyboard.setVisibility(View.GONE);
            }
        });
        rl_orderid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewKeyboard.setVisibility(View.GONE);
            }
        });
        //点击搜索
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number=et_number.getText().toString();
                String orderid=et_orderid.getText().toString();
                String carnumber=gpvPlateNumber.getPassWord();
                if (!number.equals("")||!orderid.equals("")||!carnumber.equals("")){
                    checkData(number,orderid,carnumber);
                }else {
                    Toast.makeText(VipSearchActivity.this,"请至少输入一个搜索条件！",Toast.LENGTH_SHORT).show();
                }
            }
        });
        testPlateNumberInput();
        adapter=new VipSearchAdapter(this,datas);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);

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
                    viewKeyboard.setKeyboard(new Keyboard(VipSearchActivity.this, R.xml.provice));
                    viewKeyboard.setVisibility(View.VISIBLE);
                    return true;
                } else if (position >= 1 && position < 2) {
                    viewKeyboard.setKeyboard(new Keyboard(VipSearchActivity.this, R.xml.english));
                    viewKeyboard.setVisibility(View.VISIBLE);
                    return true;
                } else if (position >= 2 && position < 6) {
                    viewKeyboard.setKeyboard(new Keyboard(VipSearchActivity.this, R.xml.qwerty_without_chinese));
                    viewKeyboard.setVisibility(View.VISIBLE);
                    return true;
                } else if (position >= 6 && position < 7) {
                    if (gpvPlateNumber.getPassWord().startsWith("粤Z")) {
                        viewKeyboard.setKeyboard(new Keyboard(VipSearchActivity.this, R.xml.qwerty));
                    } else {
                        viewKeyboard.setKeyboard(new Keyboard(VipSearchActivity.this, R.xml.qwerty_without_chinese));
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


    /**
     * 查询接口
     */
    private void checkData(String phone,String orderid,String plate_number){
        Log.i(PP_TIP,RUOYU_URL+"?request=private.user.search.user&token="+login_token+"&platform=android&phone="+phone+"&orderid="+orderid+"&plate_number="+plate_number);
        OkGo.<String>post(RUOYU_URL+"?request=private.user.search.user&token="+login_token+"&platform=android&phone="+phone+"&orderid="+orderid+"&plate_number="+plate_number)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i(PP_TIP,response.body());
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            int code=jsonObject.getInt("code");
                            if (code == 0){
                                JSONArray data=jsonObject.getJSONArray("data");
                                if (data.length() <= 0){
                                    Toast.makeText(VipSearchActivity.this,"搜索不到任何数据！",Toast.LENGTH_SHORT).show();
                                }else {
                                    ll_main.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    for (int i=0;i<data.length();i++){
                                        JSONObject temp=data.getJSONObject(i);
                                        VipSearchBean bean=new VipSearchBean();
                                        bean.setPhone(temp.getString("phone"));
                                        bean.setPlate_number(temp.getString("plate_number"));
                                        bean.setPoint(temp.getString("point"));
                                        bean.setCar_name(temp.getString("car_name"));
                                        bean.setUserid(temp.getString("userid"));
                                        bean.setVip_level(temp.getString("vip_level"));
                                        datas.add(bean);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }else {
                                Toast.makeText(VipSearchActivity.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
