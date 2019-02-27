package com.ruoyw.pigpigroad.yichengchechebang.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhouwei.library.CustomPopWindow;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.PaylayoutListviewAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.AllOrderActivity;
import com.ruoyw.pigpigroad.yichengchechebang.CashActivity;
import com.ruoyw.pigpigroad.yichengchechebang.CashOrderActivity;
import com.ruoyw.pigpigroad.yichengchechebang.PigListview.PullToRefreshLayout;
import com.ruoyw.pigpigroad.yichengchechebang.R;
import com.ruoyw.pigpigroad.yichengchechebang.SearchActivity;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.MyApplication;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;
import com.ruoyw.pigpigroad.yichengchechebang.StatisticsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

/**
 * Created by PIGROAD on 2018/3/6.
 */
public class Orderpage extends Fragment implements View.OnClickListener {

    private String login_token;

    private LinearLayout ll_search;

    private LinearLayout ll_wx_order, ll_ali_order, ll_xcx_order, ll_app_order,ll_app_money,ll_cash_order;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View orderpageLayout = inflater.inflate(R.layout.orderpage_fragment,
                container, false);
        return orderpageLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InitAll();
        SharedPreferences sp = getActivity().getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
        //postPayLayout();
    }

    /**
     * 初始化所有控件
     */
    private void InitAll() {
        ll_search = getActivity().findViewById(R.id.ll_search);
        ll_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        ll_wx_order = getActivity().findViewById(R.id.ll_wx_order);
        ll_ali_order = getActivity().findViewById(R.id.ll_ali_order);
        ll_xcx_order = getActivity().findViewById(R.id.ll_xcx_order);
        ll_app_order = getActivity().findViewById(R.id.ll_app_order);
        ll_app_money = getActivity().findViewById(R.id.ll_app_money);
        ll_cash_order = getActivity().findViewById(R.id.ll_cash_order);

        ll_xcx_order.setOnClickListener(this);
        ll_wx_order.setOnClickListener(this);
        ll_ali_order.setOnClickListener(this);
        ll_app_order.setOnClickListener(this);
        ll_app_money.setOnClickListener(this);
        ll_cash_order.setOnClickListener(this);

    }


    /**
     *  清理fragment
     */
    private void clearSelection() {

    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), StatisticsActivity.class);
        switch (v.getId()) {
            case R.id.ll_wx_order:
                intent.putExtra("pay_type", "wxgzh");
                startActivity(intent);
                break;
            case R.id.ll_ali_order:
                intent.putExtra("pay_type", "alipay");
                startActivity(intent);
                break;
            case R.id.ll_xcx_order:
                intent.putExtra("pay_type", "wxxcx");
                startActivity(intent);
                break;
            case R.id.ll_app_order:
                intent.putExtra("pay_type", "app");
                startActivity(intent);
                break;
            case R.id.ll_app_money:
                Intent intent1=new Intent(getActivity(),CashActivity.class);
                startActivity(intent1);
                break;
            case R.id.ll_cash_order:
                Intent intent2=new Intent(getActivity(),CashOrderActivity.class);
                startActivity(intent2);
                break;
        }


    }
}


