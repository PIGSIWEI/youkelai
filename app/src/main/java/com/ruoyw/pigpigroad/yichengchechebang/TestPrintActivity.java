package com.ruoyw.pigpigroad.yichengchechebang;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by PIGROAD on 2018/12/3
 * Email:920015363@qq.com
 */
public class TestPrintActivity extends AppCompatActivity {

    private LinearLayout back_ll;
    private TextView tv_log;
    private ScrollView scroll_view;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_print_layout);

        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
        try {
            this.init();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void init() throws IOException {
        back_ll = findViewById(R.id.back_ll);
        tv_log = findViewById(R.id.tv_log);
        scroll_view = findViewById(R.id.scroll_view);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_log.setMovementMethod(new ScrollingMovementMethod());
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder log = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line+"\n");
            }
            scroll_view.post(new Runnable() {
                @Override
                public void run() {
                    scroll_view.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
            scrollLog(log.toString());
        }catch (IOException e){
        }
    }

    protected boolean enableSliding() {
        return true;
    }

    private void scrollLog(String message) {
        Spannable colorMessage = new SpannableString(message + "\n");
        colorMessage.setSpan(new ForegroundColorSpan(0xff0000ff), 0, message.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_log.append(colorMessage);
        Layout layout = tv_log.getLayout();
        if (layout != null) {
            int scrollAmount = layout.getLineTop(tv_log.getLineCount()) - tv_log.getHeight();
            if (scrollAmount > 0) {
                tv_log.scrollTo(0, scrollAmount + tv_log.getCompoundPaddingBottom());
            } else {
                tv_log.scrollTo(0, 0);
            }
        }
    }

}
