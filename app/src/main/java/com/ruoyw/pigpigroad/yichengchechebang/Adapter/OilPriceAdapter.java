package com.ruoyw.pigpigroad.yichengchechebang.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.OilChangePriceActivity;
import com.ruoyw.pigpigroad.yichengchechebang.R;

import java.util.List;
import java.util.Map;

/**
 * Created by XYSM on 2018/4/4.
 */

public class OilPriceAdapter  extends BaseAdapter {
    private List<Map<String, Object>> dataList;
    private Context context;
    private int resource;

    @Override
    public int getCount() {
        return dataList.size();
    }
    public OilPriceAdapter(Context context,List<Map<String, Object>> dataList, int resoure){
        this.dataList=dataList;
        this.context=context;
        this.resource=resoure;
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        // 声明内部类
        Util util = null;
        // 中间变量
        final int flag = i;
        /**
         * 根据listView工作原理，列表项的个数只创建屏幕第一次显示的个数。
         * 之后就不会再创建列表项xml文件的对象，以及xml内部的组件，优化内存，性能效率
         */
        if (view == null) {
            util = new Util();
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, null);
            util.oil_name=view.findViewById(R.id.oil_name);
            util.oil_country=view.findViewById(R.id.oil_country);
            util.oil_car=view.findViewById(R.id.oil_car);
            util.change_oil_price=view.findViewById(R.id.change_oil_price);
            util.tv_vip_oil_price=view.findViewById(R.id.tv_vip_oil_price);
            view.setTag(util);
        }else {
            util = (Util) view.getTag();
        }
        final Map<String, Object> map = dataList.get(i);
        util.oil_name.setText((String) map.get("oil_name"));
        util.oil_country.setText((String) map.get("country_oil_price"));
        util.oil_car.setText((String) map.get("merchant_oil_price"));
        util.tv_vip_oil_price.setText((String) map.get("vip_oil_price"));
        util.change_oil_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(view.getContext(),OilChangePriceActivity.class);
                intent.putExtra("country_price",(String) map.get("country_oil_price") );
                intent.putExtra("oil_name",(String) map.get("oil_name") );
                intent.putExtra("car_price",(String) map.get("merchant_oil_price") );
                intent.putExtra("oil_id",(String) map.get("oil_id") );
                intent.putExtra("vip_oil_price",(String) map.get("vip_oil_price") );
                view.getContext().startActivity(intent);
            }
        });
        return view;

    }
    class Util {
        TextView oil_name, oil_country,oil_car,tv_vip_oil_price;
        LinearLayout change_oil_price;
    }
}
