package com.ruoyw.pigpigroad.yichengchechebang;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.facebook.stetho.Stetho;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.Suggest.DrawableTextView;
import com.ruoyw.pigpigroad.yichengchechebang.Suggest.KeyboardWatcher;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.Speek;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.control.InitConfig;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.control.MySyntherizer;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.control.NonBlockSyntherizer;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.listener.UiMessageListener;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.util.AutoCheck;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.util.OfflineResource;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;
import static com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.MainHandlerConstant.PRINT;
import static com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.MainHandlerConstant.UI_CHANGE_INPUT_TEXT_SELECTION;
import static com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.MainHandlerConstant.UI_CHANGE_SYNTHES_TEXT_SELECTION;

public class MainActivity extends FragmentActivity implements View.OnClickListener, KeyboardWatcher.SoftKeyboardStateListener {
    private DrawableTextView logo;
    private EditText et_mobile;
    private EditText et_password;
    private ImageView iv_clean_phone;
    private ImageView clean_password;
    private ImageView iv_show_pwd;
    private Button btn_login;
    private int screenHeight = 0;//屏幕高度
    private float scale = 0.8f; //logo缩放比例
    private View body;
    private KeyboardWatcher keyboardWatcher;
    @Bind(R.id.get_msg_btn)
    LinearLayout get_msg_btn;
    @Bind(R.id.get_msg_btn_tv)
    TextView get_msg_btn_tv;
    @Bind(R.id.et_user)
    EditText et_user;
    @Bind(R.id.result)
    TextView result;
    @Bind(R.id.ic_clear_name)
    ImageView ic_clear_name;
    private static final int REQUEST_SIGNUP = 0;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        initView();
        initListener();
        keyboardWatcher = new KeyboardWatcher(findViewById(Window.ID_ANDROID_CONTENT));
        keyboardWatcher.addSoftKeyboardStateListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            initPermission();
            return;
        }
        Grant();
    }

    protected void handle(Message msg) {
        int what = msg.what;
        switch (what) {
            case PRINT:

                break;
            case UI_CHANGE_INPUT_TEXT_SELECTION:

                break;
            case UI_CHANGE_SYNTHES_TEXT_SELECTION:

                break;
            default:
                break;
        }
    }

    /**
     * 权限申请
     */
    private void Grant() {

        int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        int permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        }
    }

    private void initListener() {
        iv_clean_phone.setOnClickListener(this);
        clean_password.setOnClickListener(this);
        iv_show_pwd.setOnClickListener(this);
        et_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && ic_clear_name.getVisibility() == View.GONE) {
                    ic_clear_name.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    ic_clear_name.setVisibility(View.GONE);
                }
            }
        });
        et_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && iv_clean_phone.getVisibility() == View.GONE) {
                    iv_clean_phone.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    iv_clean_phone.setVisibility(View.GONE);
                }
            }
        });
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && clean_password.getVisibility() == View.GONE) {
                    clean_password.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    clean_password.setVisibility(View.GONE);
                }
                if (s.toString().isEmpty())
                    return;
                if (!s.toString().matches("[A-Za-z0-9]+")) {
                    String temp = s.toString();
                    Toast.makeText(MainActivity.this, "请输入数字或字母", Toast.LENGTH_SHORT).show();
                    s.delete(temp.length() - 1, temp.length());
                    et_password.setSelection(s.length());
                }
            }
        });
    }

    /**
     * 缩小
     *
     * @param view
     */
    public void zoomIn(final View view, float dist) {
        view.setPivotY(view.getHeight());
        view.setPivotX(view.getWidth() / 2);
        AnimatorSet mAnimatorSet = new AnimatorSet();
        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, scale);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, scale);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", 0.0f, -dist);
        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX).with(mAnimatorScaleY);
        mAnimatorSet.setDuration(300);
        mAnimatorSet.start();

    }

    /**
     * f放大
     *
     * @param view
     */
    public void zoomOut(final View view) {
        if (view.getTranslationY() == 0) {
            return;
        }
        view.setPivotY(view.getHeight());
        view.setPivotX(view.getWidth() / 2);
        AnimatorSet mAnimatorSet = new AnimatorSet();

        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", scale, 1.0f);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", scale, 1.0f);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), 0);

        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX).with(mAnimatorScaleY);
        mAnimatorSet.setDuration(300);
        mAnimatorSet.start();

    }

    private boolean flag = false;

    private void initView() {
        Stetho.initializeWithDefaults(this);
        ButterKnife.bind(this);
        logo = findViewById(R.id.logo);
        et_mobile = findViewById(R.id.et_mobile);
        et_password = findViewById(R.id.et_password);
        iv_clean_phone = findViewById(R.id.iv_clean_phone);
        clean_password = findViewById(R.id.clean_password);
        iv_show_pwd = findViewById(R.id.iv_show_pwd);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        ic_clear_name.setOnClickListener(this);
        body = findViewById(R.id.body);
        screenHeight = this.getResources().getDisplayMetrics().heightPixels; //获取屏幕高度
        get_msg_btn.setOnClickListener(this);
        et_mobile.setInputType( InputType.TYPE_CLASS_NUMBER);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.ic_clear_name:
                et_user.setText("");
                break;
            case R.id.iv_clean_phone:
                et_mobile.setText("");
                break;
            case R.id.clean_password:
                et_password.setText("");
                break;
            case R.id.iv_show_pwd:
                if (flag == true) {
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    iv_show_pwd.setImageResource(R.drawable.pass_gone);
                    flag = false;
                } else {
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    iv_show_pwd.setImageResource(R.drawable.pass_visuable);
                    flag = true;
                }
                String pwd = et_password.getText().toString();
                if (!TextUtils.isEmpty(pwd))
                    et_password.setSelection(pwd.length());
                break;
            case R.id.get_msg_btn:
                String account = et_mobile.getText().toString().trim();
                if (isMobileNO(account) == true) {
                    if (!TextUtils.isEmpty(account)) {
                        postMSG(view);
                    }
                } else {
                    Toast.makeText(getApplication(), "请输入正确的手机格式", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_login:
                String username = et_user.getText().toString().trim();
                if (!username.equals("")) {
                   posLogin();
                } else {
                    Toast.makeText(getApplication(), "用户名不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        keyboardWatcher.removeSoftKeyboardStateListener(this);
    }

    @Override
    public void onSoftKeyboardOpened(int keyboardSize) {
        Log.e("wenzhihao", "----->show" + keyboardSize);
        int[] location = new int[2];
        body.getLocationOnScreen(location); //获取body在屏幕中的坐标,控件左上角
        int x = location[0];
        int y = location[1];
        Log.e("wenzhihao", "y = " + y + ",x=" + x);
        int bottom = screenHeight - (y + body.getHeight());
        Log.e("wenzhihao", "bottom = " + bottom);
        Log.e("wenzhihao", "con-h = " + body.getHeight());
        if (keyboardSize > bottom) {
            ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(body, "translationY", 0.0f, -(keyboardSize - bottom));
            mAnimatorTranslateY.setDuration(300);
            mAnimatorTranslateY.setInterpolator(new AccelerateDecelerateInterpolator());
            mAnimatorTranslateY.start();
            zoomIn(logo, keyboardSize - bottom);

        }

    }

    @Override
    public void onSoftKeyboardClosed() {
        Log.e("Pigpigroad", "----->hide");
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(body, "translationY", body.getTranslationY(), 0);
        mAnimatorTranslateY.setDuration(300);
        mAnimatorTranslateY.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorTranslateY.start();
        zoomOut(logo);
    }

    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            get_msg_btn.setClickable(false);
            get_msg_btn_tv.setText(l / 1000 + "s");
        }

        @Override
        public void onFinish() {
            //重新给Button设置文字
            get_msg_btn_tv.setText("重新获取");
            //设置可点击
            get_msg_btn.setClickable(true);
        }
    }

    /**
     * 判断输入的是否为手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        String telRegex = "[1]\\d{10}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else return mobiles.matches(telRegex);
    }

    public void postMSG(View view) {
        String account = et_mobile.getText().toString().trim();
        //构建FormBody，传入要提交的参数
        OkGo.<String>post(RUOYU_URL+"?phone="+account+"&request=public.auth.login.code.get")
                .tag(this)
                .headers("header1", "headerValue1")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        Log.i(PP_TIP,response.body());
                        final String responseStr = response.body();
                        try {
                            JSONObject jsonObject=new JSONObject(responseStr);
                            int code= (int) jsonObject.get("code");
                            if (code ==0){
                                final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000,1000);
                                myCountDownTimer.start();
                                Toast.makeText(getApplicationContext(),"验证码发送成功",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"验证码发送失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"服务器错误，请联系管理员",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.i(PP_TIP, String.valueOf(response));
                        Log.i("ppppppppppppppppp", "连接失败!!!!!!!!!!!!!!!");
                        Toast.makeText(MainActivity.this, "请检查你的网络设置", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    
    public void posLogin() {
        final String username=et_user.getText().toString().trim();
        String smsnumber=et_password.getText().toString().trim();
        final String account = et_mobile.getText().toString().trim();
        long time = System.currentTimeMillis()/1000;
        String logintime=String.valueOf(time);
        Log.i(PP_TIP,RUOYU_URL+"?request=public.auth.login_form_password&platform=android&admin="+username+"&password="+smsnumber);
        OkGo.<String>post(RUOYU_URL+"?request=public.auth.login_form_password&platform=android&admin="+username+"&password="+smsnumber)
                .tag(this)
                .headers("header1", "headerValue1")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        final String responseStr = response.body();
                        try {
                            Log.i(PP_TIP,responseStr);
                            JSONObject jsonObject=new JSONObject(responseStr);
                            int code= (int) jsonObject.get("code");
                            if (code ==0){
                                Toast.makeText(getApplicationContext(),"登录成功!",Toast.LENGTH_SHORT).show();
                                String token = (String) jsonObject.get("token");
                                SharedPreferences mSharedPreferences =getSharedPreferences("LoginUser",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = mSharedPreferences.edit();
                                editor.putString("user_token",token);
                                editor.putString("user_number",account);
                                Log.i(PP_TIP,"token:"+token);
                                editor.commit();
                                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                                startActivityForResult(intent, REQUEST_SIGNUP);
                                finish();
                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            }else if(code ==1){
                                String msg = jsonObject.getString("msg");
                                Toast.makeText(getApplicationContext(),"登录失败,"+msg,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.i("ppppppppppppppppp", "连接失败!!!!!!!!!!!!!!!");
                        Toast.makeText(MainActivity.this, "请检查你的网络设置", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.
            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }

}