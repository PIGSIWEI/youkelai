package com.ruoyw.pigpigroad.yichengchechebang;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.SUNMI.BaseActivity;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

/**
 * Created by PIGROAD on 2018/6/11.
 * Email:920015363@qq.com
 */

public class FeedbackActivity extends BaseActivity{
    private LinearLayout ll_back;
    private EditText et_text,et_title;
    private Button btn_confirm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_layout);
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
        init();
    }

    private void init() {
        ll_back=findViewById(R.id.back_ll);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        et_text=findViewById(R.id.et_text);
        et_title=findViewById(R.id.et_title);
        btn_confirm=findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=et_title.getText().toString().trim();
                String text=et_text.getText().toString().trim();
                if (title.equals("")||text.equals("")){
                    Toast.makeText(FeedbackActivity.this,"请认真填写反馈信息！",Toast.LENGTH_SHORT).show();
                }else {
                    postFeedback(text,title);
                }
            }
        });
    }

    protected boolean enableSliding() {
        return true;
    }

    /**
     * 反馈意见操作
     */
    private  void  postFeedback(String text,String title){
        OkGo.<String>post(RUOYU_URL+"?request=public.feedback.submit.action&platform=android&content="+text+"&title="+title)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String Str=response.body();
                        try {
                            JSONObject jsonObject=new JSONObject(Str);
                            int code =jsonObject.getInt("code");
                            String msg=jsonObject.getString("msg");
                            if ( code == 0){
                                Toast.makeText(FeedbackActivity.this,msg,Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(FeedbackActivity.this,msg,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    /**
     * 程序员犯困
     */
    //zzzzzzzzz
}
