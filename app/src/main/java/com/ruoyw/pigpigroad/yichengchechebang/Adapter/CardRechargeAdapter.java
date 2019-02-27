package com.ruoyw.pigpigroad.yichengchechebang.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.Bean.CardRechargeBean;
import com.ruoyw.pigpigroad.yichengchechebang.R;

import java.util.List;

public class CardRechargeAdapter extends RecyclerView.Adapter<CardRechargeAdapter.ViewHolder> {

    private Context context;
    private List<CardRechargeBean> datas;
    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public CardRechargeAdapter(Context context,List<CardRechargeBean> datas){
        this.context=context;
        this.datas=datas;
    }

    @Override
    public CardRechargeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.card_recharge_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardRechargeAdapter.ViewHolder holder, int position) {

        if(holder instanceof ViewHolder) {
            final ViewHolder viewHolder = holder;
            //err  1是到账 其他是异常
            if (datas.get(position).getErr_status().equals("1")){
                holder.iv.setBackgroundResource(R.drawable.icon_card_finsih);
                holder.tv_state.setText("已到账");
            }else {
                holder.iv.setBackgroundResource(R.drawable.icon_card_errow);
                holder.tv_state.setText("异常");
            }

            holder.tv_card_no.setText("充值卡号："+datas.get(position).getCard_no());
            holder.tv_addtime.setText("充值时间："+datas.get(position).getAddtime());
            holder.tv_money.setText("充值金额："+datas.get(position).getPay_money()+" 赠送金额："+datas.get(position).getGift_money());
            holder.tv_name.setText("充值油站："+datas.get(position).getName());

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

        ImageView iv;
        TextView tv_card_no,tv_addtime,tv_name,tv_money,tv_state;

        public ViewHolder(View itemView) {
            super(itemView);
            iv=itemView.findViewById(R.id.iv);
            tv_card_no=itemView.findViewById(R.id.tv_card_no);
            tv_addtime=itemView.findViewById(R.id.tv_addtime);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_money=itemView.findViewById(R.id.tv_money);
            tv_state=itemView.findViewById(R.id.tv_state);
        }
    }
}
