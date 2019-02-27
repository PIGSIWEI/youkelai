package com.ruoyw.pigpigroad.yichengchechebang.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.R;

import java.util.List;

/**
 * Created by PIGROAD on 2018/5/24.
 * Email:920015363@qq.com
 */

public class OilpageAdapter extends RecyclerView.Adapter<OilpageAdapter.MyViewHolder>{
    private List<OilpageBean> data;
    private Context context;

    public OilpageAdapter(Context context,List<OilpageBean> data){
        this.data=data;
        this.context=context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.oilpage_listitem, parent, false);
        return new OilpageAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.oil_name.setText(data.get(position).getOil_name());
        holder.oil_count.setText(data.get(position).getCount());
        holder.oil_lit.setText(data.get(position).getOil_lit());
        holder.order_money.setText(data.get(position).getOrder_money());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView oil_name,oil_lit,oil_count,order_money;
        public MyViewHolder(View itemView) {
            super(itemView);
            oil_name=itemView.findViewById(R.id.oil_name);
            oil_lit=itemView.findViewById(R.id.oil_lit);
            oil_count=itemView.findViewById(R.id.oil_count);
            order_money=itemView.findViewById(R.id.order_money);
        }
    }
}
