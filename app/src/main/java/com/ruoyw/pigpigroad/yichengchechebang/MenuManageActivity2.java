package com.ruoyw.pigpigroad.yichengchechebang;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.adapter.Call;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.MenuEntity;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.MenuParentAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.MyAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.MyAdapter2;
import com.ruoyw.pigpigroad.yichengchechebang.Bean.MenuBean;
import com.ruoyw.pigpigroad.yichengchechebang.BuleTooth.ClientActivity;
import com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.AppConfig;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.MyApplication;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;
import com.ruoyw.pigpigroad.yichengchechebang.Widget.DragCallback;
import com.ruoyw.pigpigroad.yichengchechebang.Widget.DragForScrollView;
import com.ruoyw.pigpigroad.yichengchechebang.Widget.DragGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

public class MenuManageActivity2 extends AppCompatActivity {

    private static DragGridView dragGridView;
    private static MyAdapter2 adapterSelect; //
    private TextView tv_set;
    private static ArrayList<MenuEntity> menuList = new ArrayList<MenuEntity>();
    ;
    private ExpandableListView expandableListView;
    private static MenuParentAdapter2 menuParentAdapter;
    private LinearLayout ll_top_back;
    private LinearLayout ll_top_sure;
    private TextView tv_top_title;
    private TextView tv_top_sure;
    private static MyApplication appContext;
    private TextView tv_drag_tip;
    private DragForScrollView sv_index;
    private static List<MenuEntity> indexSelect = new ArrayList<MenuEntity>();
    private LinearLayout back_ll;
    private List<MenuEntity> datas = new ArrayList<>();
    private String login_token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_manage);
        SlidingLayout rootView = new SlidingLayout(this);
        rootView.bindActivity(this);
        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
        appContext = (MyApplication) getApplication();
        dragGridView = (DragGridView) findViewById(R.id.gridview);
        sv_index = (DragForScrollView) findViewById(R.id.sv_index);
        initView();
        initData();
        ll_top_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_top_sure.getText().toString().equals("管理")) {
                    tv_top_sure.setText("完成");
                    adapterSelect.setEdit();
                    if (menuParentAdapter != null) {
                        menuParentAdapter.setEdit();
                    }
                    tv_drag_tip.setVisibility(View.VISIBLE);
                } else {
                    tv_top_sure.setText("管理");
                    tv_drag_tip.setVisibility(View.GONE);
                    adapterSelect.endEdit();
                    if (menuParentAdapter != null) {
                        menuParentAdapter.endEdit();
                    }
                    postMenu();
                }
            }
        });

        getUserMenu();

    }

    protected boolean enableSliding() {
        return true;
    }

    protected void postMenu() {
//        List<MenuEntity> indexDataList = (List<MenuEntity>) appContext.readObject(AppConfig.KEY_USER_TEMP);
//        String key = AppConfig.KEY_USER;
//        appContext.saveObject((Serializable) indexDataList, key);
        //TODO 用户保存操作
        JSONArray jsonArray =new JSONArray();
        JSONObject jsonObject =new JSONObject();
        for (int i = 0; i < indexSelect.size(); i++) {
           JSONObject object=new JSONObject();
            try {
                object.put("id",indexSelect.get(i).getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(object);
        }
        try {
            jsonObject.put("data",jsonArray);
            Log.i(PP_TIP,jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        upMenu(jsonArray.toString());

    }

    public void DelMeun(MenuEntity indexData, int position) {
        // TODO Auto-generated method stub
        for (int i = 0; i < menuList.size(); i++) {
            for (int k = 0; k < menuList.get(i).getChilds().size(); k++) {
                if (menuList.get(i).getChilds().get(k).getTitle().equals(indexData.getTitle())) {
                    menuList.get(i).getChilds().get(k).setSelect(false);
                }
            }
        }
        if (menuParentAdapter != null) {
            menuParentAdapter.notifyDataSetChanged();
        }
        adapterSelect.notifyDataSetChanged();
    }

    public static void AddMenu(MenuEntity menuEntity) {
        // TODO Auto-generated method stub
        indexSelect.add(menuEntity);

        for (int i = 0; i < menuList.size(); i++) {
            for (int k = 0; k < menuList.get(i).getChilds().size(); k++) {
                if (menuList.get(i).getChilds().get(k).getTitle().equals(menuEntity.getTitle())) {
                    menuList.get(i).getChilds().get(k).setSelect(true);
                }
            }
        }
        menuParentAdapter.notifyDataSetChanged();
        adapterSelect.notifyDataSetChanged();
    }

    public void initUrl(MenuEntity cateModel) {
        // TODO Auto-generated method stub
        if (tv_top_sure.getText().toString().equals("管理")) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            String title = cateModel.getTitle();
            String strId = cateModel.getId();
            String url=cateModel.getSrc();
            String oil_name = getIntent().getStringExtra("oil_name");
            //TODO 点击事件
            switch (strId) {
                case "id_banjie":
                    //班结
                    if (oil_name.equals("")) {

                    } else {
                        Intent intent2 = new Intent(MenuManageActivity2.this, ClassActivity.class);
                        intent.putExtra("oil_name", oil_name);
                        startActivity(intent2);
                    }
                    break;
                case "id_dingdan":
                    Intent intent2 = new Intent(MenuManageActivity2.this, AllOrderActivity.class);
                    startActivity(intent2);
                    break;
                case "id_youpin":
                    Intent intent3 = new Intent(MenuManageActivity2.this, OilPriceActivity.class);
                    startActivity(intent3);
                    break;
                case "id_anquan":
                    Intent intent4 = new Intent(MenuManageActivity2.this, PasswordActivity.class);
                    startActivity(intent4);
                    break;
                case "id_saoyisao":
                    Intent intent5 = new Intent(MenuManageActivity2.this, GroupBuyingActivity.class);
                    startActivity(intent5);
                    break;
                case "id_wx":
                    Intent intent6 = new Intent(MenuManageActivity2.this, StatisticsActivity.class);
                    intent6.putExtra("pay_type", "wxgzh");
                    startActivity(intent6);
                    break;
                case "id_order_money":
                    Intent intent7 = new Intent(MenuManageActivity2.this, CashActivity.class);
                    startActivity(intent7);
                    break;
                case "id_cash_order":
                    Intent intent8 = new Intent(MenuManageActivity2.this, CashOrderActivity.class);
                    startActivity(intent8);
                    break;
                case "id_xcx":
                    Intent intent9 = new Intent(MenuManageActivity2.this, StatisticsActivity.class);
                    intent9.putExtra("pay_type", "wxxcx");
                    startActivity(intent9);
                    break;
                case "id_ali":
                    Intent intent10 = new Intent(MenuManageActivity2.this, StatisticsActivity.class);
                    intent10.putExtra("pay_type", "alipay");
                    startActivity(intent10);
                    break;
                case "id_dayin":
                    Intent intent11 = new Intent(MenuManageActivity2.this, PrinterTestActivity.class);
                    startActivity(intent11);
                    break;
                case "id_xiugai":
                    Intent intent12 = new Intent(MenuManageActivity2.this, PasswordActivity.class);
                    startActivity(intent12);
                    break;
                case "id_paying":
                    Intent intent13 = new Intent(MenuManageActivity2.this, PayingActivity.class);
                    startActivity(intent13);
                    break;
                case "id_vip":
                    Intent intent14 = new Intent(MenuManageActivity2.this, VipSearchActivity.class);
                    startActivity(intent14);
                    break;
                case "id_point":
                    Intent intent15 = new Intent(MenuManageActivity2.this, PointActivity.class);
                    startActivity(intent15);
                    break;
                case "id_face":
                    Intent intent16 = new Intent(MenuManageActivity2.this, ClientActivity.class);
                    startActivity(intent16);
                    break;
                case "id_flash_pay":
                    Intent intent17 = new Intent(MenuManageActivity2.this, FlashOrderActivity.class);
                    startActivity(intent17);
                    break;
                case "id_car_pay":
                    Intent intent19 = new Intent(MenuManageActivity2.this, CarPayActivity.class);
                    startActivity(intent19);
                    break;
                case "id_hotfix":
                    Intent intent20 = new Intent(MenuManageActivity2.this, HotfixActivity.class);
                    startActivity(intent20);
                    break;
                case "id_hot":
                    Intent intent21 = new Intent(MenuManageActivity2.this, HotActivity.class);
                    startActivity(intent21);
                    break;
                case "id_card_ic":
                    Intent intent22=new Intent(MenuManageActivity2.this,CardIcActivity.class);
                    startActivity(intent22);
                    break;
                default:
                    //未定义ID事件 统一是webview
                    //统一处理第三方webview
                    Intent webviewIntent =new Intent(MenuManageActivity2.this,WebViewActivity.class);
                    webviewIntent.putExtra("url",url);
                    webviewIntent.putExtra("title",title);
                    startActivity(webviewIntent);
                    break;
            }
        }
    }

    private void initView() {
        // TODO Auto-generated method stub
        ll_top_sure = (LinearLayout) findViewById(R.id.ll_top_sure);
        back_ll = (LinearLayout) findViewById(R.id.back_ll);
        tv_top_sure = (TextView) findViewById(R.id.tv_top_sure);
        tv_top_sure.setText("管理");
        tv_top_sure.setVisibility(View.VISIBLE);

        tv_drag_tip = (TextView) findViewById(R.id.tv_drag_tip);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //获取设置保存到本地的菜单
//        List<MenuEntity> indexDataList = (List<MenuEntity>) appContext.readObject(AppConfig.KEY_USER);
//        if (indexDataList != null) {
//            indexSelect.clear();
//            indexSelect.addAll(indexDataList);
//        }

        adapterSelect = new MyAdapter2(this, appContext, indexSelect);
        dragGridView.setAdapter(adapterSelect);

        dragGridView.setDragCallback(new DragCallback() {
            @Override
            public void startDrag(int position) {
                Log.i("start drag at ", "" + position);
                sv_index.startDrag(position);
            }

            @Override
            public void endDrag(int position) {
                Log.i("end drag at ", "" + position);
                sv_index.endDrag(position);
            }
        });
        dragGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("setOnItemClickListener", adapterSelect.getEditStatue() + "");
                if (!adapterSelect.getEditStatue()) {
                    //dragGridView.clicked(position);
                    MenuEntity cateModel = indexSelect.get(position);
                    initUrl(cateModel);
                }
            }
        });
        dragGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (tv_top_sure.getText().toString().equals("管理")) {
                    tv_top_sure.setText("完成");
                    adapterSelect.setEdit();
                    if (menuParentAdapter != null) {
                        menuParentAdapter.setEdit();
                    }
                    tv_drag_tip.setVisibility(View.VISIBLE);
                }
                dragGridView.startDrag(position);
                return false;
            }
        });
    }

    private void initData() {
        // TODO Auto-generated method stub
//        List<MenuEntity> indexDataList = (List<MenuEntity>) appContext.readObject(AppConfig.KEY_All);
//        init(indexDataList);
        getData();
    }

    private void init(List<MenuEntity> indexAll) {
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListView.setGroupIndicator(null);
            menuList.clear();
        try {
            Log.i(PP_TIP,"set所有应用");
            MenuEntity index = new MenuEntity();
            index.setTitle("所有应用");
            index.setId("1");
            List<MenuEntity> indexLC = new ArrayList<MenuEntity>();
            for (int i = 0; i < indexAll.size(); i++) {
                indexLC.add(indexAll.get(i));
            }
            for (int i = 0; i < indexLC.size(); i++) {
                for (int j = 0; j < indexSelect.size(); j++) {
                    if (indexLC.get(i).getTitle().equals(indexSelect.get(j).getTitle())) {
                        indexLC.get(i).setSelect(true);
                    }
                }
            }
            index.setChilds(indexLC);
            menuList.add(index);

            menuParentAdapter = new MenuParentAdapter2(MenuManageActivity2.this, menuList);
            expandableListView.setAdapter(menuParentAdapter);
            menuParentAdapter.notifyDataSetChanged();

            // expandableListView.expandGroup(6); // 在分组列表视图中 展开一组
            // expandableListView.isGroupExpanded(0); //判断此组是否展开
            for (int i = 0; i < menuParentAdapter.getGroupCount(); i++) {
                expandableListView.expandGroup(i);
            }
            expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                    MenuEntity cateModel = menuList.get(groupPosition);
                    return true;
                }
            });
            expandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if (tv_top_sure.getText().toString().equals("管理")) {
                        MenuEntity cateModel = menuList.get(arg2);
                        initUrl(cateModel);
                    }
                }
            });

            expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if (tv_top_sure.getText().toString().equals("管理")) {
                        tv_top_sure.setText("完成");
                        adapterSelect.setEdit();
                        menuParentAdapter.setEdit();
                    }
                    return false;
                }
            });

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 获取数据
     */
    private void getData() {
        datas.clear();
        Log.i(PP_TIP, RUOYU_URL + "?request=private.menu.get_pos_menu_user_no_select&platform=android&token=" + login_token);
        OkGo.<String>post(RUOYU_URL + "?request=private.menu.get_pos_menu_user_no_select&platform=android&token=" + login_token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i(PP_TIP, response.body());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            int code = jsonObject.getInt("code");
                            if (code == 0) {
                                JSONArray data = jsonObject.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject temp = data.getJSONObject(i);
                                    MenuEntity bean = new MenuEntity();
                                    bean.setSelect(temp.getBoolean("is_select"));
                                    bean.setId(temp.getString("id"));
                                    bean.setIco(temp.getString("img"));
                                    bean.setSort(temp.getString("sort"));
                                    bean.setSrc(temp.getString("url"));
                                    bean.setTitle(temp.getString("title"));
                                    datas.add(bean);
                                }
                                Log.i(PP_TIP, "初始化菜单");
                                init(datas);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 上传用户菜单
     */
    private void upMenu(String data){
        Log.i(PP_TIP,RUOYU_URL+"?request=private.menu.save_pos_menu_user&platform=android&token="+login_token+"&data="+data);
        OkGo.<String>post(RUOYU_URL+"?request=private.menu.save_pos_menu_user&platform=android&token="+login_token+"&data="+data)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.i(PP_TIP,response.body());
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            Toast.makeText(MenuManageActivity2.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    /**
     * 获取用户 个人保存的菜单
     */
    private void getUserMenu(){
        indexSelect.clear();
        Log.i(PP_TIP,RUOYU_URL+"?request=private.menu.get_pos_menu_user&platform=android&token="+login_token);
        OkGo.<String>post(RUOYU_URL+"?request=private.menu.get_pos_menu_user&platform=android&token="+login_token)
                .execute(new StringCallback() {
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
                                    MenuEntity bean=new MenuEntity();
                                    bean.setId(temp.getString("id"));
                                    bean.setIco(temp.getString("img"));
                                    bean.setSort(temp.getString("sort"));
                                    bean.setSrc(temp.getString("url"));
                                    bean.setTitle(temp.getString("title"));
                                    indexSelect.add(bean);
                                }
                                adapterSelect.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        indexSelect.clear();
        datas.clear();
    }
}
