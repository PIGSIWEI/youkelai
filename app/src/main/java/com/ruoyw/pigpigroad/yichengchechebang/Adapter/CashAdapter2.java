package com.ruoyw.pigpigroad.yichengchechebang.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.Bean.PayBean;
import com.ruoyw.pigpigroad.yichengchechebang.Bean.UnpayBean;
import com.ruoyw.pigpigroad.yichengchechebang.R;

import java.util.List;

/**
 * Created by PIGROAD on 2018/11/1
 * Email:920015363@qq.com
 */
public class CashAdapter2 extends RecyclerView.Adapter<CashAdapter2.ViewHolder> {

    private List<UnpayBean> datas;
    private Context context;
    private OnItemClickLitener mOnItemClickLitener;


    public CashAdapter2(List<UnpayBean> datas, Context context){
        this.datas=datas;
        this.context=context;
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pay_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(holder instanceof ViewHolder) {
            final ViewHolder viewHolder = holder;
            holder.gun_kind.setText(datas.get(position).getGun_kind());
            holder.gun_number.setText(datas.get(position).getGun_number()+"元");
            holder.pay_money.setText(datas.get(position).getPay_money());
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

        TextView gun_number,gun_kind,pay_money;

        public ViewHolder(View itemView) {
            super(itemView);
            gun_number=itemView.findViewById(R.id.gun_number);
            gun_kind=itemView.findViewById(R.id.gun_kind);
            pay_money=itemView.findViewById(R.id.pay_money);
        }
    }
}
