package com.ruoyw.pigpigroad.yichengchechebang.Hotfit;

import android.content.Context;
import android.support.annotation.Keep;
import android.util.Log;

import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.MyApplication;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixApplication;
import com.taobao.sophix.SophixEntry;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

/**
 * Sophix入口类，专门用于初始化Sophix，不应包含任何业务逻辑。
 * 此类必须继承自SophixApplication，onCreate方法不需要实现。
 * 此类不应与项目中的其他类有任何互相调用的逻辑，必须完全做到隔离。
 * AndroidManifest中设置application为此类，而SophixEntry中设为原先Application类。
 * 注意原先Application里不需要再重复初始化Sophix，并且需要避免混淆原先Application类。
 * 如有其它自定义改造，请咨询官方后妥善处理。
 */

public class SophixStubApplication extends SophixApplication {
    private final String TAG = "SophixStubApplication";
    // 此处SophixEntry应指定真正的Application，并且保证RealApplicationStub类名不被混淆。
    /**用来判断是否要使用冷启动（重启App）的方式使补丁生效*/
    public static boolean isRelaunch = false;
    @Keep
    @SophixEntry(MyApplication.class)
    static class RealApplicationStub {}
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//         如果需要使用MultiDex，需要在此处调用。
//         MultiDex.install(this);
        initHotFix();
    }
    /**
     code: 0 准备开始
     code: 1 补丁加载成功
     code: 6 服务端没有最新可用的补丁
     code: 9 路径下载成功
     code: 11 RSASECRET错误，官网中的密钥是否正确请检查
     code: 12 当前应用已经存在一个旧补丁, 应用重启尝试加载新补丁
     code: 13 补丁加载失败, 导致的原因很多种, 比如UnsatisfiedLinkError等异常, 此时应该严格检查logcat异常日志
     code: 16 APPSECRET错误，官网中的密钥是否正确请检查
     code: 18 一键清除补丁
     code: 19 连续两次queryAndLoadNewPatch()方法调用不能短于3s
     */

    private void initHotFix() {
        String appVersion;
        try {
            appVersion = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (Exception e) {
            appVersion = "1.0.0";
        }
        Log.i(TAG,"appVersion:"+appVersion);
        // initialize最好放在attachBaseContext最前面
        SophixManager.getInstance().setContext(this)
                .setAppVersion("1.0")
                .setAesKey(null)
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        Log.i(TAG, "mode:" + mode+",code:"+code+",info:"+info+",handlePatchVersion:"+handlePatchVersion);

                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                            Log.i(TAG, "表明补丁加载成功:" );
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后应用自杀
                            Log.i(TAG, "表明新补丁生效需要重启. 开发者可提示用户或者强制重启:" );
                            isRelaunch = true;
                        } else if (code == PatchStatus.CODE_LOAD_FAIL) {
                            // 内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载
                            SophixManager.getInstance().cleanPatches();
                            Log.i(TAG, "内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载:" );
                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                            Log.i(TAG, "其它错误信息, 查看PatchStatus类说明:" );
                        }
                    }
                }).initialize();

    }
}
