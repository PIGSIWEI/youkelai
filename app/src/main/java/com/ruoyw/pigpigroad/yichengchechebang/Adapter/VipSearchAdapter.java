package com.ruoyw.pigpigroad.yichengchechebang.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.Bean.VipSearchBean;
import com.ruoyw.pigpigroad.yichengchechebang.R;

import java.util.List;

public class VipSearchAdapter extends RecyclerView.Adapter<VipSearchAdapter.ViewHolder> {

    private Context context;
    private List<VipSearchBean> datas;

    public  VipSearchAdapter(Context context,List<VipSearchBean> datas){
        this.context=context;
        this.datas=datas;
    }

    @Override
    public VipSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.vip_search_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VipSearchAdapter.ViewHolder holder, int position) {
        holder.tv_car_number.setText("车牌号："+datas.get(position).getPlate_number());
        holder.tv_level.setText(datas.get(position).getVip_level());
        holder.tv_phone.setText("手机号："+datas.get(position).getPhone());
        holder.tv_point.setText("积分："+datas.get(position).getPoint());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_level,tv_phone,tv_point,tv_car_number;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_car_number=itemView.findViewById(R.id.tv_car_number);
            tv_level=itemView.findViewById(R.id.tv_level);
            tv_phone=itemView.findViewById(R.id.tv_phone);
            tv_point=itemView.findViewById(R.id.tv_point);
        }
    }
}
