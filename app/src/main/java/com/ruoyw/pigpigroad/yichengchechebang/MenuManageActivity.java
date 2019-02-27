package com.ruoyw.pigpigroad.yichengchechebang;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.Adapter.MenuEntity;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.MenuParentAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.MyAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.BuleTooth.ClientActivity;
import com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.AppConfig;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.MyApplication;
import com.ruoyw.pigpigroad.yichengchechebang.Widget.DragCallback;
import com.ruoyw.pigpigroad.yichengchechebang.Widget.DragForScrollView;
import com.ruoyw.pigpigroad.yichengchechebang.Widget.DragGridView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PIGROAD on 2018/12/11
 * Email:920015363@qq.com
 */
public class MenuManageActivity extends AppCompatActivity {

    private static DragGridView dragGridView;
    private static MyAdapter adapterSelect; //
    private TextView tv_set;
    private static ArrayList<MenuEntity> menuList= new ArrayList<MenuEntity>();;
    private ExpandableListView expandableListView;
    private static MenuParentAdapter menuParentAdapter;
    private LinearLayout ll_top_back;
    private LinearLayout ll_top_sure;
    private TextView tv_top_title;
    private TextView tv_top_sure;
    private static MyApplication appContext;
    private TextView tv_drag_tip;
    private DragForScrollView sv_index;
    private static List<MenuEntity> indexSelect = new ArrayList<MenuEntity>();
    private LinearLayout back_ll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_manage);
        appContext = (MyApplication) getApplication();
        dragGridView = (DragGridView) findViewById(R.id.gridview);
        sv_index= (DragForScrollView) findViewById(R.id.sv_index);
        initView();
        initData();
        ll_top_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_top_sure.getText().toString().equals("管理")) {
                    tv_top_sure.setText("完成");
                    adapterSelect.setEdit();
                    if(menuParentAdapter!=null){
                        menuParentAdapter.setEdit();
                    }
                    tv_drag_tip.setVisibility(View.VISIBLE);
                } else {
                    tv_top_sure.setText("管理");
                    tv_drag_tip.setVisibility(View.GONE);
                    adapterSelect.endEdit();
                    if(menuParentAdapter!=null){
                        menuParentAdapter.endEdit();
                    }
                    postMenu();
                }
            }
        });

    }

    protected boolean enableSliding() {
        return true;
    }

    protected void postMenu() {
        // TODO Auto-generated method stub
        List<MenuEntity> indexDataList = (List<MenuEntity>) appContext.readObject(AppConfig.KEY_USER_TEMP);
        String key = AppConfig.KEY_USER;
        appContext.saveObject((Serializable) indexDataList, key);
    }

    public  void DelMeun(MenuEntity indexData, int position) {
        // TODO Auto-generated method stub
        for (int i = 0; i < menuList.size(); i++) {
            for (int k = 0; k < menuList.get(i).getChilds().size(); k++) {
                if (menuList.get(i).getChilds().get(k).getTitle().equals(indexData.getTitle())) {
                    menuList.get(i).getChilds().get(k).setSelect(false);
                }
            }
        }
        if(menuParentAdapter!=null){
            menuParentAdapter.notifyDataSetChanged();
        }
        adapterSelect.notifyDataSetChanged();
    }

    public static void AddMenu(MenuEntity menuEntity) {
        // TODO Auto-generated method stub
        indexSelect.add(menuEntity);
        String key = AppConfig.KEY_USER_TEMP;
        appContext.saveObject((Serializable) indexSelect, key);

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
            String oil_name=getIntent().getStringExtra("oil_name");
            //TODO 点击事件
            switch (strId){
                case "id_banjie":
                    //班结
                    if (oil_name.equals("")) {

                    } else {
                        Intent intent2 = new Intent(MenuManageActivity.this, ClassActivity.class);
                        intent.putExtra("oil_name", oil_name);
                        startActivity(intent2);
                    }
                    break;
                case "id_dingdan":
                    Intent intent2 = new Intent(MenuManageActivity.this, AllOrderActivity.class);
                    startActivity(intent2);
                    break;
                case "id_youpin":
                    Intent intent3 = new Intent(MenuManageActivity.this, OilPriceActivity.class);
                    startActivity(intent3);
                    break;
                case "id_anquan":
                    Intent intent4 = new Intent(MenuManageActivity.this, PasswordActivity.class);
                    startActivity(intent4);
                    break;
                case "id_saoyisao":
                    Intent intent5 = new Intent(MenuManageActivity.this, GroupBuyingActivity.class);
                    startActivity(intent5);
                    break;
                case "id_wx":
                    Intent intent6 = new Intent(MenuManageActivity.this, StatisticsActivity.class);
                    intent6.putExtra("pay_type","wxgzh");
                    startActivity(intent6);
                    break;
                case "id_order_money":
                    Intent intent7=new Intent(MenuManageActivity.this,CashActivity.class);
                    startActivity(intent7);
                    break;
                case "id_cash_order":
                    Intent intent8=new Intent(MenuManageActivity.this,CashOrderActivity.class);
                    startActivity(intent8);
                    break;
                case "id_xcx":
                    Intent intent9 = new Intent(MenuManageActivity.this, StatisticsActivity.class);
                    intent9.putExtra("pay_type","wxxcx");
                    startActivity(intent9);
                    break;
                case "id_ali":
                    Intent intent10 = new Intent(MenuManageActivity.this, StatisticsActivity.class);
                    intent10.putExtra("pay_type","alipay");
                    startActivity(intent10);
                    break;
                case "id_dayin":
                    Intent intent11 = new Intent(MenuManageActivity.this, PrinterTestActivity.class);
                    startActivity(intent11);
                    break;
                case "id_xiugai":
                    Intent intent12 = new Intent(MenuManageActivity.this, PasswordActivity.class);
                    startActivity(intent12);
                    break;
                case "id_paying":
                    Intent intent13 = new Intent(MenuManageActivity.this, PayingActivity.class);
                    startActivity(intent13);
                    break;
                case "id_vip":
                    Intent intent14 = new Intent(MenuManageActivity.this, VipSearchActivity.class);
                    startActivity(intent14);
                    break;
                case "id_point":
                    Intent intent15 = new Intent(MenuManageActivity.this, PointActivity.class);
                    startActivity(intent15);
                    break;
                case "id_face":
                    Intent intent16 =new Intent(MenuManageActivity.this,ClientActivity.class);
                    startActivity(intent16);
                    break;
                case "id_flash_pay":
                    Intent intent17=new Intent(MenuManageActivity.this,FlashOrderActivity.class);
                    startActivity(intent17);
                    break;
                case "id_car_pay":
                    Intent intent19=new Intent(MenuManageActivity.this,CarPayActivity.class);
                    startActivity(intent19);
                    break;
                case "id_hotfix":
                    Intent intent20=new Intent(MenuManageActivity.this,HotfixActivity.class);
                    startActivity(intent20);
                    break;
                case "id_hot":
                    Intent intent21=new Intent(MenuManageActivity.this,HotActivity.class);
                    startActivity(intent21);
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

        tv_drag_tip= (TextView) findViewById(R.id.tv_drag_tip);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //获取设置保存到本地的菜单
        List<MenuEntity> indexDataList = (List<MenuEntity>) appContext.readObject(AppConfig.KEY_USER);
        if (indexDataList != null) {
            indexSelect.clear();
            indexSelect.addAll(indexDataList);
        }

        adapterSelect = new MyAdapter(this, appContext, indexSelect);
        dragGridView.setAdapter(adapterSelect);

        dragGridView.setDragCallback(new DragCallback() {
            @Override
            public void startDrag(int position) {
                Log.i("start drag at ", ""+ position);
                sv_index.startDrag(position);
            }
            @Override
            public void endDrag(int position) {
                Log.i("end drag at " ,""+ position);
                sv_index.endDrag(position);
            }
        });
        dragGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("setOnItemClickListener",adapterSelect.getEditStatue()+"");
                if(!adapterSelect.getEditStatue()){
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
                    if(menuParentAdapter!=null){
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
        List<MenuEntity> indexDataList = (List<MenuEntity>) appContext.readObject(AppConfig.KEY_All);
        init(indexDataList);
    }

    private void init(List<MenuEntity> indexAll) {
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListView.setGroupIndicator(null);
        menuList.clear();
        try {
            MenuEntity index = new MenuEntity();
            index.setTitle("所有应用");
            index.setId("1");
            List<MenuEntity> indexLC=new ArrayList<MenuEntity>();
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

//            menuParentAdapter = new MenuParentAdapter(MenuManageActivity.this, menuList);
            expandableListView.setAdapter(menuParentAdapter);

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

}
