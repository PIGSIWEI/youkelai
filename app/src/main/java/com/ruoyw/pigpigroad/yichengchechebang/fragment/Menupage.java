package com.ruoyw.pigpigroad.yichengchechebang.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.MenuAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.AllOrderActivity;
import com.ruoyw.pigpigroad.yichengchechebang.Bean.MenuBean;
import com.ruoyw.pigpigroad.yichengchechebang.BuleTooth.ClientActivity;
import com.ruoyw.pigpigroad.yichengchechebang.CarPayActivity;
import com.ruoyw.pigpigroad.yichengchechebang.CardIcActivity;
import com.ruoyw.pigpigroad.yichengchechebang.CashActivity;
import com.ruoyw.pigpigroad.yichengchechebang.CashOrderActivity;
import com.ruoyw.pigpigroad.yichengchechebang.ClassActivity;
import com.ruoyw.pigpigroad.yichengchechebang.FlashOrderActivity;
import com.ruoyw.pigpigroad.yichengchechebang.GroupBuyingActivity;
import com.ruoyw.pigpigroad.yichengchechebang.HotActivity;
import com.ruoyw.pigpigroad.yichengchechebang.HotfixActivity;
import com.ruoyw.pigpigroad.yichengchechebang.MenuManageActivity;
import com.ruoyw.pigpigroad.yichengchechebang.MenuManageActivity2;
import com.ruoyw.pigpigroad.yichengchechebang.OilPriceActivity;
import com.ruoyw.pigpigroad.yichengchechebang.PasswordActivity;
import com.ruoyw.pigpigroad.yichengchechebang.PayingActivity;
import com.ruoyw.pigpigroad.yichengchechebang.PointActivity;
import com.ruoyw.pigpigroad.yichengchechebang.PrinterTestActivity;
import com.ruoyw.pigpigroad.yichengchechebang.R;
import com.ruoyw.pigpigroad.yichengchechebang.StatisticsActivity;
import com.ruoyw.pigpigroad.yichengchechebang.VipSearchActivity;
import com.ruoyw.pigpigroad.yichengchechebang.WebViewActivity;
import com.ruoyw.pigpigroad.yichengchechebang.Widget.LineGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

public class Menupage extends Fragment {

    private DrawerLayout drawerLayout;

    private LineGridView gridView;
    private List<MenuBean> datas=new ArrayList<>();
    private MenuAdapter adapter;
    private String oil_name="";
    private String login_token;
    private TextView hometitle;
    private ImageView menu_iv;

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
        GetUser();
        drawerLayout=getActivity().findViewById(R.id.drawer_layout);
        menu_iv=getActivity().findViewById(R.id.menu_iv);
        menu_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        gridView = (LineGridView) getActivity().findViewById(R.id.gv_lanuch_start);
        gridView.setFocusable(false);
        adapter=new MenuAdapter(datas,getActivity());
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                String title = datas.get(position).getTitle();
                String strId = datas.get(position).getId();
                String url=datas.get(position).getSrc();
                switch (strId){
                    case "all":
                        //界面管理
                        intent.setClass(getActivity(), MenuManageActivity2.class);
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
                    case "id_card_ic":
                        Intent intent22=new Intent(getActivity(),CardIcActivity.class);
                        startActivity(intent22);
                        break;
                    default:
                        //未定义ID事件 统一是webview
                        Intent webviewIntent =new Intent(getActivity(),WebViewActivity.class);
                        webviewIntent.putExtra("url",url);
                        webviewIntent.putExtra("title",title);
                        startActivity(webviewIntent);
                        break;
                }
            }
        });

    }

    /**
     * 获取数据
     */
    private void getData(){
        datas.clear();
            Log.i(PP_TIP,RUOYU_URL+"?request=private.menu.get_pos_menu_user&platform=android&token="+login_token);
        OkGo.<String>post(RUOYU_URL+"?request=private.menu.get_pos_menu_user&platform=android&token="+login_token)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.i(PP_TIP,"没有网络");
                        checkConnect();
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i(PP_TIP,response.body());
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            int code =jsonObject.getInt("code");
                            if (code == 0){
                                JSONArray data=jsonObject.getJSONArray("data");
                                for (int i=0;i<data.length();i++){
                                    JSONObject temp=data.getJSONObject(i);
                                    MenuBean bean=new MenuBean();
                                    bean.setId(temp.getString("id"));
                                    bean.setImg(temp.getString("img"));
                                    bean.setSort(temp.getInt("sort"));
                                    bean.setSrc(temp.getString("url"));
                                    bean.setTitle(temp.getString("title"));
                                    datas.add(bean);
                                }
                                MenuBean menuBean=new MenuBean();
                                menuBean.setSort(datas.size()+1);
                                menuBean.setImg("http://www.ykelai.com/source/app/app_icon/icon_setting.png");
                                menuBean.setId("all");
                                menuBean.setTitle("界面设置");
                                datas.add(menuBean);
                                relist();
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    /**
     * 排序
     */
    private List<MenuBean> relist(){
        Collections.sort(datas, new Comparator<MenuBean>() {
            @Override
            public int compare(MenuBean o1, MenuBean o2) {
                int hits0 = o1.getSort();
                int hits1 = o2.getSort();
                if (hits1 < hits0) {
                    return 1;
                } else if (hits1 == hits0) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        return  datas;
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

    /**
     * 监测网络 弹窗口
     */
    private void checkConnect(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("油客来发现你当前没有网络！请检查当前网络连接后再尝试");
        builder.setPositiveButton("重新连接网络", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().finish();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

}
