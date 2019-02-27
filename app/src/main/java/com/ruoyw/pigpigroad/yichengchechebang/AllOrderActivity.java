package com.ruoyw.pigpigroad.yichengchechebang;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;
import com.ruoyw.pigpigroad.yichengchechebang.fragment.Orderpage;

/**
 * Created by PIGROAD on 2018/10/10
 * Email:920015363@qq.com
 */
public class AllOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ll_search;

    private LinearLayout ll_wx_order, ll_ali_order, ll_xcx_order, ll_app_order,ll_app_money,ll_cash_order,ll_flash_order;

    private LinearLayout back_ll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_choose_layout);
        this.init();
    }

    /**
     * 初始化
     */
    private void init() {

        ll_search = findViewById(R.id.ll_search);
        ll_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllOrderActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }

        ll_wx_order = findViewById(R.id.ll_wx_order);
        ll_ali_order = findViewById(R.id.ll_ali_order);
        ll_xcx_order = findViewById(R.id.ll_xcx_order);
        ll_app_order = findViewById(R.id.ll_app_order);
        ll_app_money = findViewById(R.id.ll_app_money);
        ll_cash_order = findViewById(R.id.ll_cash_order);
        ll_flash_order = findViewById(R.id.ll_flash_order);

        ll_xcx_order.setOnClickListener(this);
        ll_wx_order.setOnClickListener(this);
        ll_ali_order.setOnClickListener(this);
        ll_app_order.setOnClickListener(this);
        ll_app_money.setOnClickListener(this);
        ll_cash_order.setOnClickListener(this);
        ll_flash_order.setOnClickListener(this);


        back_ll=findViewById(R.id.back_ll);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    protected boolean enableSliding() {
        return true;
    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(AllOrderActivity.this, StatisticsActivity.class);
        switch (view.getId()) {
            case R.id.ll_wx_order:
                intent.putExtra("pay_type","wxgzh");
                startActivity(intent);
                break;
            case R.id.ll_ali_order:
                intent.putExtra("pay_type","alipay");
                startActivity(intent);
                break;
            case R.id.ll_xcx_order:
                intent.putExtra("pay_type","wxxcx");
                startActivity(intent);
                break;
            case R.id.ll_app_order:
                intent.putExtra("pay_type","app");
                startActivity(intent);
                break;
            case R.id.ll_app_money:
                Intent intent1=new Intent(AllOrderActivity.this,CashActivity.class);
                startActivity(intent1);
                break;
            case R.id.ll_cash_order:
                Intent intent2=new Intent(AllOrderActivity.this,CashOrderActivity.class);
                startActivity(intent2);
                break;
            case R.id.ll_flash_order:
                Intent intent18=new Intent(AllOrderActivity.this,FlashOrderActivity.class);
                startActivity(intent18);
                break;
        }
    }
}
