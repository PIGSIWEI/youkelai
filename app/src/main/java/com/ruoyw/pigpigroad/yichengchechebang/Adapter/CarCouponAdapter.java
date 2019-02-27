package com.ruoyw.pigpigroad.yichengchechebang.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.Bean.CarCouponBean;
import com.ruoyw.pigpigroad.yichengchechebang.R;

import java.util.List;

public class CarCouponAdapter extends RecyclerView.Adapter<CarCouponAdapter.ViewHolder> {

    private Context context;
    private List<CarCouponBean> datas;
    private OnItemClickLitener mOnItemClickLitener;
    private int selected = -1;

    public CarCouponAdapter(Context context,List<CarCouponBean> datas){
        this.context=context;
        this.datas=datas;
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public void setSelection(int position){
        this.selected = position;
        notifyDataSetChanged();
    }

    public boolean isSelection(){
        if (selected > 0){
            return  true;
        }else {
            return false;
        }
    }

    public int getSelection(){
        return selected;
    }

    @Override
    public CarCouponAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.car_gun_money_coupon_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CarCouponAdapter.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder) {
            final ViewHolder viewHolder = holder;

            if (selected == position) {
                viewHolder.radioButton.setChecked(true);
                viewHolder.itemView.setSelected(true);
            } else {
                viewHolder.radioButton.setChecked(false);
                viewHolder.itemView.setSelected(false);
            }

            if (mOnItemClickLitener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemClick(viewHolder.itemView, viewHolder.getAdapterPosition());
                    }
                });
            }

            holder.tv_coupon_money.setText(datas.get(position).getCoupon_money());
            holder.tv_coupon_name.setText(datas.get(position).getCoupon_name());

        }

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        RadioButton radioButton;
        TextView tv_coupon_name,tv_coupon_money;

        public ViewHolder(View itemView) {
            super(itemView);
            radioButton=itemView.findViewById(R.id.radio_button);
            tv_coupon_name=itemView.findViewById(R.id.tv_coupon_name);
            tv_coupon_money=itemView.findViewById(R.id.tv_coupon_money);
        }
    }
}
