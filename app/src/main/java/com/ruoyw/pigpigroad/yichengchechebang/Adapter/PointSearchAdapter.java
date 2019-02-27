package com.ruoyw.pigpigroad.yichengchechebang.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.Bean.PointSearchBean;
import com.ruoyw.pigpigroad.yichengchechebang.R;

import java.util.List;

public class PointSearchAdapter extends RecyclerView.Adapter<PointSearchAdapter.ViewHolder> {

    private Context context;
    private List<PointSearchBean> datas;

    public PointSearchAdapter(Context context, List<PointSearchBean> datas){
        this.context=context;
        this.datas=datas;
    }

    @Override
    public PointSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.point_search_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PointSearchAdapter.ViewHolder holder, int position) {
        holder.tv_number.setText("手机号："+datas.get(position).getPhone());
        holder.tv_time.setText("时间："+datas.get(position).getAddtime());
        holder.tv_type.setText("积分类型："+datas.get(position).getType_name());
        if (datas.get(position).getPoint()>0){
            holder.tv_point.setTextColor(Color.parseColor("#c61a04"));
        }else {
            holder.tv_point.setTextColor(Color.parseColor("#0fb75f"));
        }
        holder.tv_point.setText(datas.get(position).getPoint()+"");
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_time,tv_number,tv_type,tv_point;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_point=itemView.findViewById(R.id.tv_point);
            tv_time=itemView.findViewById(R.id.tv_time);
            tv_number=itemView.findViewById(R.id.tv_number);
            tv_type=itemView.findViewById(R.id.tv_type);
        }
    }

}
