package com.ruoyw.pigpigroad.yichengchechebang.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.Bean.FlashOrderBean;
import com.ruoyw.pigpigroad.yichengchechebang.R;

import java.util.List;

public class FlashOrderAdapter extends RecyclerView.Adapter<FlashOrderAdapter.ViewHolder> {

    private Context context;
    private List<FlashOrderBean> datas;
    private OnItemClickLitener mOnItemClickLitener;

    public FlashOrderAdapter(Context context, List<FlashOrderBean> datas){
        this.context=context;
        this.datas=datas;
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public FlashOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.flash_order_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FlashOrderAdapter.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder) {
            final ViewHolder viewHolder = holder;
            holder.tv_store_name.setText(datas.get(position).getStore_name());
            holder.tv_orderid.setText("订单编号："+datas.get(position).getOrderid());
            holder.tv_pay_time.setText("订单时间："+datas.get(position).getPay_time());
            holder.tv_pay_money.setText("￥"+datas.get(position).getPay_money());
            if (mOnItemClickLitener != null)
            {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        mOnItemClickLitener.onItemClick(viewHolder.itemView, viewHolder.getAdapterPosition());
                    }
                });
            }

        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_store_name,tv_orderid,tv_pay_time,tv_pay_money;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_store_name=itemView.findViewById(R.id.tv_store_name);
            tv_orderid=itemView.findViewById(R.id.tv_orderid);
            tv_pay_time=itemView.findViewById(R.id.tv_pay_time);
            tv_pay_money=itemView.findViewById(R.id.tv_pay_money);
        }
    }
}
