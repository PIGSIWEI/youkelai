package com.ruoyw.pigpigroad.yichengchechebang.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.IndexDataAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.MenuEntity;
import com.ruoyw.pigpigroad.yichengchechebang.AllOrderActivity;
import com.ruoyw.pigpigroad.yichengchechebang.BuleTooth.ClientActivity;
import com.ruoyw.pigpigroad.yichengchechebang.CarPayActivity;
import com.ruoyw.pigpigroad.yichengchechebang.CashActivity;
import com.ruoyw.pigpigroad.yichengchechebang.CashOrderActivity;
import com.ruoyw.pigpigroad.yichengchechebang.ClassActivity;
import com.ruoyw.pigpigroad.yichengchechebang.FlashOrderActivity;
import com.ruoyw.pigpigroad.yichengchechebang.GroupBuyingActivity;
import com.ruoyw.pigpigroad.yichengchechebang.HotActivity;
import com.ruoyw.pigpigroad.yichengchechebang.HotfixActivity;
import com.ruoyw.pigpigroad.yichengchechebang.MenuManageActivity;
import com.ruoyw.pigpigroad.yichengchechebang.OilPriceActivity;
import com.ruoyw.pigpigroad.yichengchechebang.PasswordActivity;
import com.ruoyw.pigpigroad.yichengchechebang.PayingActivity;
import com.ruoyw.pigpigroad.yichengchechebang.PointActivity;
import com.ruoyw.pigpigroad.yichengchechebang.PrinterTestActivity;
import com.ruoyw.pigpigroad.yichengchechebang.R;
import com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.AppConfig;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.MyApplication;
import com.ruoyw.pigpigroad.yichengchechebang.StatisticsActivity;
import com.ruoyw.pigpigroad.yichengchechebang.VipSearchActivity;
import com.ruoyw.pigpigroad.yichengchechebang.Widget.LineGridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

/**
 * Created by PIGROAD on 2018/3/6.
 */

public class Homepage extends Fragment implements View.OnClickListener {
    private ImageView menu_iv;
    private DrawerLayout drawerLayout;
    private TextView hometitle;
    private String login_token;
    private String oil_name="";
    private static MyApplication appContext;
    private LineGridView gridView;
    private List<MenuEntity> indexDataAll = new ArrayList<MenuEntity>();
    private List<MenuEntity> indexDataList = new ArrayList<MenuEntity>();
    private IndexDataAdapter adapter;
    private final static String fileName = "menulist";
    private static final int REQUEST_CODE_SCAN = 0x0000;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View homepageLayout = inflater.inflate(R.layout.homepage_fragment,
                container, false);
        return homepageLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initDate();
        super.onActivityCreated(savedInstanceState);
    }

    private void initDate() {
        SharedPreferences sp = getActivity().getSharedPreferences("LoginUser", getActivity().MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
        hometitle = getActivity().findViewById(R.id.hometitle);
        menu_iv=getActivity().findViewById(R.id.menu_iv);
        menu_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        drawerLayout=getActivity().findViewById(R.id.drawer_layout);
        GetUser();
        appContext = (MyApplication) getActivity().getApplication();
        gridView = (LineGridView) getActivity().findViewById(R.id.gv_lanuch_start);
        gridView.setFocusable(false);
        String strByJson=getJson(getActivity(),fileName);
        //Json的解析类对象
        JsonParser parser = new JsonParser();
        //将JSON的String 转成一个JsonArray对象
        JsonArray jsonArray = parser.parse(strByJson).getAsJsonArray();
        Gson gson = new Gson();
        //加强for循环遍历JsonArray
        for (JsonElement indexArr : jsonArray) {
            //使用GSON，直接转成Bean对象
            MenuEntity menuEntity = gson.fromJson(indexArr, MenuEntity.class);
            indexDataAll.add(menuEntity);
        }
        String key = AppConfig.KEY_All;
        String keyUser = AppConfig.KEY_USER;
        appContext.saveObject((Serializable) indexDataAll, AppConfig.KEY_All);

        List<MenuEntity> indexDataUser = (List<MenuEntity>) appContext.readObject(AppConfig.KEY_USER);
        if(indexDataUser==null||indexDataUser.size()==0) {
            appContext.saveObject((Serializable) indexDataAll, AppConfig.KEY_USER);
        }
        indexDataList = (List<MenuEntity>) appContext.readObject(AppConfig.KEY_USER);
        MenuEntity allMenuEntity=new MenuEntity();
        allMenuEntity.setIco("");
        allMenuEntity.setId("all");
        allMenuEntity.setTitle("全部");
        indexDataList.add(allMenuEntity);
        adapter = new IndexDataAdapter(getActivity(), indexDataList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                String title = indexDataList.get(position).getTitle();
                String strId = indexDataList.get(position).getId();
                switch (strId){
                    case "all":
                        //界面管理
                        intent.setClass(getActivity(), MenuManageActivity.class);
                        intent.putExtra("oil_name",oil_name);
                        startActivity(intent);
                        break;
                    case "id_banjie":
                        //班结
                        if (oil_name.equals("")) {

                        } else {
                            Intent intent2 = new Intent(getActivity(), ClassActivity.class);
                            intent2.putExtra("oil_name", oil_name);
                            startActivity(intent2);
                        }
                        break;
                    case "id_dingdan":
                        Intent intent2 = new Intent(getActivity(), AllOrderActivity.class);
                        startActivity(intent2);
                        break;
                    case "id_youpin":
                        Intent intent3 = new Intent(getActivity(), OilPriceActivity.class);
                        startActivity(intent3);
                        break;
                    case "id_anquan":
                        Intent intent4 = new Intent(getActivity(), PasswordActivity.class);
                        startActivity(intent4);
                        break;
                    case "id_saoyisao":
                        Intent intent5 = new Intent(getActivity(), GroupBuyingActivity.class);
                        startActivity(intent5);
                        break;
                    case "id_wx":
                        Intent intent6 = new Intent(getActivity(), StatisticsActivity.class);
                        intent6.putExtra("pay_type","wxgzh");
                        startActivity(intent6);
                        break;
                    case "id_order_money":
                        Intent intent7=new Intent(getActivity(),CashActivity.class);
                        startActivity(intent7);
                        break;
                    case "id_cash_order":
                        Intent intent8=new Intent(getActivity(),CashOrderActivity.class);
                        startActivity(intent8);
                        break;
                    case "id_xcx":
                        Intent intent9 = new Intent(getActivity(), StatisticsActivity.class);
                        intent9.putExtra("pay_type","wxxcx");
                        startActivity(intent9);
                        break;
                    case "id_ali":
                        Intent intent10 = new Intent(getActivity(), StatisticsActivity.class);
                        intent10.putExtra("pay_type","alipay");
                        startActivity(intent10);
                        break;
                    case "id_dayin":
                        Intent intent11 = new Intent(getActivity(), PrinterTestActivity.class);
                        startActivity(intent11);
                        break;
                    case "id_xiugai":
                        Intent intent12 = new Intent(getActivity(), PasswordActivity.class);
                        startActivity(intent12);
                        break;
                    case "id_paying":
                        Intent intent13 = new Intent(getActivity(), PayingActivity.class);
                        startActivity(intent13);
                        break;
                    case "id_vip":
                        Intent intent14 = new Intent(getActivity(), VipSearchActivity.class);
                        startActivity(intent14);
                        break;
                    case "id_point":
                        Intent intent15 = new Intent(getActivity(), PointActivity.class);
                        startActivity(intent15);
                        break;
                    case "id_face":
                        Intent intent16 =new Intent(getActivity(),ClientActivity.class);
                        startActivity(intent16);
                        break;
                    case "id_gift":

                        break;
                    case "id_flash_pay":
                        Intent intent18=new Intent(getActivity(),FlashOrderActivity.class);
                        startActivity(intent18);
                        break;
                    case "id_car_pay":
                        Intent intent19=new Intent(getActivity(),CarPayActivity.class);
                        startActivity(intent19);
                        break;
                    case "id_hotfix":
                        Intent intent20=new Intent(getActivity(),HotfixActivity.class);
                        startActivity(intent20);
                        break;
                    case "id_hot":
                        Intent intent21=new Intent(getActivity(),HotActivity.class);
                        startActivity(intent21);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    /**
     * 获取门店管理员信息
     */
    private void GetUser() {
        OkGo.<String>post(RUOYU_URL + "?request=private.admin.my.admin.info.get&token=" + login_token + "&platform=android")
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String responseStr = response.body();
                        Log.i(PP_TIP, responseStr);
                        try {
                            JSONObject jsonObject = new JSONObject(responseStr);
                            int code = jsonObject.getInt("code");
                            if (code == 0) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                oil_name=data.getString("admin_name");
                                hometitle.setText(data.getString("admin_name"));
                            } else if (code == 999) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    @Override
    public void onResume() {
        super.onResume();
        indexDataList.clear();
        indexDataList = (List<MenuEntity>) appContext.readObject(AppConfig. KEY_USER);
        MenuEntity allMenuEntity=new MenuEntity();
        allMenuEntity.setIco("icon_setting");
        allMenuEntity.setId("all");
        allMenuEntity.setTitle("界面设置");
        indexDataList.add(allMenuEntity);
        adapter = new IndexDataAdapter(getActivity(), indexDataList);
        gridView.setAdapter(adapter);
    }
}
