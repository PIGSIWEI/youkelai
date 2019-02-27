package com.ruoyw.pigpigroad.yichengchechebang.SlideBack;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.lzy.okgo.OkGo;
import com.ruoyw.pigpigroad.yichengchechebang.MainActivity;
import com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.AppConfig;
import com.ruoyw.pigpigroad.yichengchechebang.SUNMI.util.AidlUtil;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;


/**
 * Created by PIGROAD on 2018/3/16.
 */
public class MyApplication extends Application {
    private static final String TAG = "Init";
    private CloudPushService pushService;
    private List<Activity> oList;//用于存放所有启动的Activity的集合
    private static Context context;
    private static MyApplication sMyApplication;
    private boolean isAidl;
    private static Activity activity;
    private static final int CACHE_TIME = 60 * 60000;// 缓存失效时间
    private Hashtable<String, Object> memCacheRegion = new Hashtable<String, Object>();

    @Override
    public void onCreate() {
        super.onCreate();
        sMyApplication = this;
        isAidl = true;
        AidlUtil.getInstance().connectPrinterService(this);
        context = getApplicationContext();
        oList = new ArrayList<Activity>();
        initCloudChannel(this);
        init();
        hotfitinit();
    }

    private void hotfitinit() {

    }

    /**
     * 初始化云推送通道
     * @param applicationContext
     */
    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(getApplicationContext());
        pushService = PushServiceFactory.getCloudPushService();
        pushService.register(getApplicationContext(), new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "init cloudchannel success");
            }
            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.d(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
    }

    private void init() {
    }

    /**
     * 添加Activity
     */
    public void addActivity_(Activity activity) {
    // 判断当前集合中不存在该Activity
        if (!oList.contains(activity)) {
            oList.add(activity);//把当前Activity添加到集合中
        }
    }
    /**
     * 销毁所有的Activity
     */
    public void removeALLActivity_() {
        //通过循环，把集合中的所有Activity销毁
        for (Activity activity : oList) {
            activity.finish();
        }
    }

    public static void ExitClear(Activity activity) {
        SharedPreferences sp = activity.getSharedPreferences("LoginUser", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public boolean isAidl() {
        return isAidl;
    }

    public void setAidl(boolean aidl) {
        isAidl = aidl;
    }

    public static Context getContext() {
        return context;
    }

    public static Activity getActivity() {
        return activity;
    }

    /**
     * 判断缓存数据是否可读
     *
     * @param cachefile
     * @return
     */
    private boolean isReadDataCache(String cachefile) {
        return readObject(cachefile) != null;
    }

    /**
     * 读取对象
     *
     * @param file
     * @return
     */
    public Serializable readObject(String file) {
        if (!isExistDataCache(file))
            return null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
            // 反序列化失败 - 删除缓存文件
            if (e instanceof InvalidClassException) {
                File data = getFileStreamPath(file);
                data.delete();
            }
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 判断缓存是否存在
     *
     * @param cachefile
     * @return
     */
    private boolean isExistDataCache(String cachefile) {
        boolean exist = false;
        File data = getFileStreamPath(cachefile);
        if (data.exists())
            exist = true;
        return exist;
    }
    /**
     * 保存对象
     *
     * @param ser
     * @param file
     */
    public boolean saveObject(Serializable ser, String file) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = openFileOutput(file, MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }
    public Properties getProperties() {
        return AppConfig.getAppConfig(this).get();
    }

}


