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
 * Created by XYSM on 2018/4/7.
 */

public class CheckTodayAdapter  extends BaseAdapter {
    private List<Map<String, Object>> dataList;
    private Context context;
    private int resource;
    public CheckTodayAdapter(Context context,List<Map<String, Object>> dataList, int resoure){
        this.dataList=dataList;
        this.context=context;
        this.resource=resoure;
    }
    public int getCount() {
        return  dataList.size();
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
            util.whodiscount=view.findViewById(R.id.whodiscount);
            util.paymoney=view.findViewById(R.id.paymoney);
            util.paytime=view.findViewById(R.id.paytime);
            util.ordertime=view.findViewById(R.id.ordertime);
            util.order_id=view.findViewById(R.id.order_id);
            view.setTag(util);
        }else {
            util = (Util) view.getTag();
        }
        final Map<String, Object> map = dataList.get(i);
        util.whodiscount.setText("会员类型："+(String) map.get("who_discount"));
        util.paymoney.setText("付款金额："+(String) map.get("pay_money"));
        util.paytime.setText("付款时间："+(String) map.get("pay_time"));
        util.ordertime.setText("订单时间："+(String) map.get("order_time"));
        util.order_id.setText("订单编号："+(String) map.get("orderid"));
        return view;
    }
    class Util {
        TextView whodiscount, paymoney,paytime,ordertime,order_id;
    }
}
