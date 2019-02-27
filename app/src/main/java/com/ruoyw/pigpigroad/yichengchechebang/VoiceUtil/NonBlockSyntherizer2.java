package com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.control.InitConfig;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.control.MySyntherizer;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.control.NonBlockSyntherizer;

/**
 * Created by PIGROAD on 2018/11/29
 * Email:920015363@qq.com
 */
public class NonBlockSyntherizer2  extends MySyntherizer2 {

    private static final int INIT = 1;

    private static final int RELEASE = 11;
    private HandlerThread hThread;
    private Handler tHandler;


    private static final String TAG = "NonBlockSyntherizer";


    public NonBlockSyntherizer2(Context context, InitConfig initConfig, Handler mainHandler) {
        super(context, mainHandler,initConfig);
        initThread();
        runInHandlerThread(INIT, initConfig);
    }


    protected void initThread() {
        hThread = new HandlerThread("NonBlockSyntherizer-thread");
        hThread.start();
        tHandler = new Handler(hThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case INIT:
                        InitConfig config = (InitConfig) msg.obj;
                        boolean isSuccess = init(config);
                        if (isSuccess) {
                            // speak("初始化成功");
                            sendToUiThread("NonBlockSyntherizer 初始化成功");
                        } else {
                            sendToUiThread("合成引擎初始化失败, 请查看日志");
                        }
                        break;
                    case RELEASE:
                        NonBlockSyntherizer2.super.release();
                        if (Build.VERSION.SDK_INT < 18) {
                            getLooper().quit();
                        }
                        break;
                    default:
                        break;
                }

            }
        };
    }

    @Override
    public void release() {
        runInHandlerThread(RELEASE);
        if (Build.VERSION.SDK_INT >= 18) {
            hThread.quitSafely();
        }
    }


    private void runInHandlerThread(int action) {
        runInHandlerThread(action, null);
    }

    private void runInHandlerThread(int action, Object obj) {
        Message msg = Message.obtain();
        msg.what = action;
        msg.obj = obj;
        tHandler.sendMessage(msg);
    }

}
