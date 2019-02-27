package com.ruoyw.pigpigroad.yichengchechebang.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.Bean.OilSettingBean;
import com.ruoyw.pigpigroad.yichengchechebang.R;

import java.util.List;

/**
 * Created by PIGROAD on 2018/12/4
 * Email:920015363@qq.com
 */
public class OilSetttingAdapter extends RecyclerView.Adapter<OilSetttingAdapter.ViewHolder> {

    private Context context;
    private List<OilSettingBean> datas;

    public OilSetttingAdapter(Context context,List<OilSettingBean> datas){
        this.context=context;
        this.datas=datas;
    }

    @Override
    public OilSetttingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.oilgun_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OilSetttingAdapter.ViewHolder holder, int position) {
        holder.tv_oil_gun.setText(datas.get(position).getOil_gun()+"号油枪");
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_oil_gun;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_oil_gun=itemView.findViewById(R.id.tv_oil_gun);
        }
    }
}
