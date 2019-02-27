package com.ruoyw.pigpigroad.yichengchechebang.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.Bean.PayingBean;
import com.ruoyw.pigpigroad.yichengchechebang.R;

import java.util.List;

public class PayingAdapter extends RecyclerView.Adapter<PayingAdapter.ViewHolder> {

    private Context context;
    private List<PayingBean> datas;

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public PayingAdapter(Context context,List<PayingBean> datas){
        this.context=context;
        this.datas=datas;
    }

    @Override
    public PayingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.paying_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PayingAdapter.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder) {
            final ViewHolder viewHolder = holder;
            holder.tv_money.setText(datas.get(position).getOilmoney()+"元");
            holder.tv_oil_gun.setText(datas.get(position).getOilgun()+"号油枪");
            holder.tv_oil_time.setText(datas.get(position).getOiltime());
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

        TextView tv_oil_gun,tv_oil_time,tv_money;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_oil_gun=itemView.findViewById(R.id.tv_oil_gun);
            tv_oil_time=itemView.findViewById(R.id.tv_oil_time);
            tv_money=itemView.findViewById(R.id.tv_money);
        }
    }
}
