package com.ruoyw.pigpigroad.yichengchechebang.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.Bean.CarBean;
import com.ruoyw.pigpigroad.yichengchechebang.R;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {

    private Context context;
    private List<CarBean> datas;
    private OnItemClickLitener mOnItemClickLitener;

    public CarAdapter(Context context,List<CarBean> datas){
        this.context=context;
        this.datas=datas;
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public CarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.car_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CarAdapter.ViewHolder holder, int position) {

        if(holder instanceof ViewHolder) {
            final ViewHolder viewHolder = holder;
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
        holder.tv_car_no.setText(datas.get(position).getCar_no());
        holder.tv_date.setText(datas.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_car_no,tv_date;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_car_no=itemView.findViewById(R.id.tv_car_no);
            tv_date=itemView.findViewById(R.id.tv_date);
        }
    }
}
