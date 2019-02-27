package com.ruoyw.pigpigroad.yichengchechebang.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.Bean.CardConsumeBean;
import com.ruoyw.pigpigroad.yichengchechebang.R;

import java.util.ArrayList;
import java.util.List;

public class CardConsumeAdapter extends RecyclerView.Adapter<CardConsumeAdapter.ViewHolder> {

    private Context context;
    private List<CardConsumeBean> datas=new ArrayList<>();
    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public CardConsumeAdapter(Context context,List<CardConsumeBean> datas){
        this.context=context;
        this.datas=datas;
    }

    @Override
    public CardConsumeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.card_consume_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardConsumeAdapter.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder) {
            final ViewHolder viewHolder = holder;
            holder.gun_kind.setText(datas.get(position).getOil_name());
            holder.gun_number.setText(datas.get(position).getGun_id()+"号油枪");
            holder.pay_money.setText(datas.get(position).getPay_money()+"元");
            holder.tv_oil_money.setText("订单金额："+datas.get(position).getOrder_money());
            holder.tv_order_time.setText(datas.get(position).getOrder_time());
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

        TextView gun_number,gun_kind,tv_order_time,pay_money,tv_oil_money;

        public ViewHolder(View itemView) {
            super(itemView);
            gun_kind=itemView.findViewById(R.id.gun_kind);
            gun_number=itemView.findViewById(R.id.gun_number);
            tv_order_time=itemView.findViewById(R.id.tv_order_time);
            pay_money=itemView.findViewById(R.id.pay_money);
            tv_oil_money=itemView.findViewById(R.id.tv_oil_money);
        }
    }
}
