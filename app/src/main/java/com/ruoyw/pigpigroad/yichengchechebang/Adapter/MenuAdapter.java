package com.ruoyw.pigpigroad.yichengchechebang.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide;
import com.ruoyw.pigpigroad.yichengchechebang.Bean.MenuBean;
import com.ruoyw.pigpigroad.yichengchechebang.R;

import java.util.List;

public class MenuAdapter extends BaseAdapter {

    private List<MenuBean> datas;
    private Context context;
    private LayoutInflater layoutInflater;

    public MenuAdapter(List<MenuBean> datas,Context context){
        this.datas=datas;
        this.context=context;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MenuBean bean=null;
        View view = null;
        bean=datas.get(i);
        if (layoutInflater != null) {
            view = layoutInflater.inflate(R.layout.item_gridview, null);

            ImageView iv_gridview = (ImageView) view.findViewById(R.id.iv_gridview);
            TextView tv_gridview = (TextView) view.findViewById(R.id.tv_gridview);

            tv_gridview.setText(bean.getTitle());
            Glide.with(context).load(bean.getImg()).into(iv_gridview);

        }
        return view;
    }
}
