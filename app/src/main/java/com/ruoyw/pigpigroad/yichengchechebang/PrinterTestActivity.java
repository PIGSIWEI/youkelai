package com.ruoyw.pigpigroad.yichengchechebang;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruoyw.pigpigroad.yichengchechebang.SUNMI.BaseActivity;
import com.ruoyw.pigpigroad.yichengchechebang.SUNMI.util.AidlUtil;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by PIGROAD on 2018/4/10.
 * Email:920015363@qq.com
 */

public class PrinterTestActivity extends BaseActivity {
    private LinearLayout back_ll;
    private TextView texttip;
    private Button test_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.printer_test_layout);

        back_ll = findViewById(R.id.back_ll);
        texttip = findViewById(R.id.texttip);
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

        test_btn = findViewById(R.id.test_btn);
        test_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
//                    final TDialog tDialog = new TDialog.Builder(getSupportFragmentManager())
//                            .setOnBindViewListener(new OnBindViewListener() {
//                                TextView textView;
//                                @Override
//                                public void bindView(BindViewHolder viewHolder) {
//                                    textView= viewHolder.bindView.findViewById(R.id.text_tip);
//                                    textView.setText("正在检查，请稍候···");
//                                }
//                            })
//                            .setLayoutRes(R.layout.login_dialog)                        //弹窗布局
//                            .setScreenWidthAspect(PrinterTestActivity.this, 0.9f)               //屏幕宽度比
//                            .setDimAmount(0f)                                                    //设置焦点
//                            .create()
//                            .show();
                    @Override
                    public void run() {
                        Toast.makeText(PrinterTestActivity.this,"正在检查，请稍候···",Toast.LENGTH_SHORT).show();
//                        tDialog.dismiss();
                        if (AidlUtil.getInstance().isConnect()) {
                            final SimpleDateFormat simpledf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                            final String time=simpledf.format(new Date());
                            texttip.setText("连接打印机成功！");
                            String content = "*******油客来小票测试*******\n" +
                                    "      "+time+"  \n"+
                                    "******************************";

                            AidlUtil.getInstance().printText(content, 24, false, false);
                            AidlUtil.getInstance().print3Line();

                        } else {
                            texttip.setText("连接打印机失败！");
                        }
                    }
                }, 1000);
            }
        });
    }

    protected boolean enableSliding() {
        return true;
    }

}
