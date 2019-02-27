package com.ruoyw.pigpigroad.yichengchechebang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.R;

import java.util.List;
import java.util.Map;

/**
 * Created by XYSM on 2018/4/3.
 */

public class RefundRecordAdapter extends BaseAdapter {
    private List<Map<String, Object>> dataList;
    private Context context;
    private int resource;
    public RefundRecordAdapter(Context context,List<Map<String, Object>> dataList, int resoure){
        this.dataList=dataList;
        this.context=context;
        this.resource=resoure;
    }
    @Override
    public int getCount() {
        return dataList.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
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
            util.oil_count=view.findViewById(R.id.oil_count);
            util.oil_price=view.findViewById(R.id.oil_price);
            util.oil_lit=view.findViewById(R.id.oil_lit);
            util.oil_money=view.findViewById(R.id.oil_money);
            view.setTag(util);
        }else {
            util = (Util) view.getTag();
        }
        final Map<String, Object> map = dataList.get(i);
        util.oil_price.setText((String) map.get("oil_name"));
        util.oil_count.setText((String) map.get("count"));
        util.oil_lit.setText((String) map.get("oil_lit"));
        util.oil_money.setText((String) map.get("order_money"));
        return view;
    }
    class Util {
        TextView oil_price, oil_count,oil_lit,oil_money;
    }
}
