package com.ruoyw.pigpigroad.yichengchechebang.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.R;

import java.util.List;
import java.util.Map;

/**
 * Created by XYSM on 2018/3/30.
 */

public class ExchangeAdapter extends BaseAdapter {
    private List<Map<String, Object>> dataList;
    private Context context;
    private int resource;

    public ExchangeAdapter(Context context,List<Map<String, Object>> dataList, int resoure){
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
            // 给xml布局文件创建java对象
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, null);
            util.exchange_type=view.findViewById(R.id.exchange_type);
            util.exchange_time=view.findViewById(R.id.exchange_time);
            view.setTag(util);
        }else {
            util = (Util) view.getTag();
        }
        final Map<String, Object> map = dataList.get(i);
        util.exchange_type.setText("兑换类别:"+(String) map.get("type_name"));
        util.exchange_time.setText("兑换时间:"+(String) map.get("addtime"));
        view.setOnClickListener(new View.OnClickListener() {
            private TextView ex_time,ex_point,ex_point_banlace,ex_order_id,ex_type;
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                LayoutInflater inflater = LayoutInflater.from(view.getContext());
                view = inflater.inflate(R.layout.exchange_detail, null);
                final Dialog dialog = builder.create();
                dialog.show();
                dialog.setContentView(view);
                dialog.getWindow().setGravity(Gravity.BOTTOM);//可以设置显示的位置setContentView(view);//自定义布局应该在这里添加，要在dialog.show()的后面
                Window window = dialog.getWindow();
                window.setGravity(Gravity.BOTTOM);
                window.setWindowAnimations(R.style.dialog_animation);
//                window.getDecorView().setPadding(0, 0, 0, 0);
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
                ex_time=view.findViewById(R.id.ex_time);
                ex_point=view.findViewById(R.id.ex_point);
                ex_point_banlace=view.findViewById(R.id.ex_point_banlace);
                ex_order_id=view.findViewById(R.id.ex_order_id);
                ex_type=view.findViewById(R.id.ex_type);
                ex_time.setText((String)map.get("addtime"));
                ex_point.setText((String)map.get("point"));
                ex_point_banlace.setText((String)map.get("point_banlace"));
                ex_order_id.setText((String)map.get("orderid"));
                ex_type.setText((String)map.get("type_name"));

            }
        });
        return view;
    }
    class Util {
        TextView exchange_type, exchange_time;
    }
}
