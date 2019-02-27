package com.ruoyw.pigpigroad.yichengchechebang.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.Bean.HomeBean;
import com.ruoyw.pigpigroad.yichengchechebang.R;

import java.util.List;

/**
 * Created by PIGROAD on 2018/10/9
 * Email:920015363@qq.com
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>{

    private List<HomeBean> datas;
    private Context context;

    public HomeAdapter(Context context,List<HomeBean> datas){
        this.datas=datas;
        this.context=context;
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HomeAdapter.ViewHolder holder, int position) {

        if(holder instanceof ViewHolder) {
            final ViewHolder viewHolder = holder;
            holder.order_id.setText("流水号："+datas.get(position).getTransaction_id());
            holder.order_time.setText(datas.get(position).getOrder_time());
            holder.tv_order_money.setText("￥"+datas.get(position).getPay_money());
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
            if (datas.get(position).getIs_confirm() == 1){
                holder.iv.setBackgroundResource(R.drawable.icon_order_finish);
            }else {
                holder.iv.setBackgroundResource(R.drawable.icon_un_order);
            }
        }

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView order_id,order_time,tv_order_money;
        LinearLayout ll_1;
        ImageView iv;

        public ViewHolder(View itemView) {
            super(itemView);
            iv=itemView.findViewById(R.id.iv);
            order_time=itemView.findViewById(R.id.order_time);
            order_id=itemView.findViewById(R.id.order_id);
            tv_order_money=itemView.findViewById(R.id.tv_order_money);
            ll_1=itemView.findViewById(R.id.ll_1);
        }
    }

}
