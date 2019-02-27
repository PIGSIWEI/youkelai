package com.ruoyw.pigpigroad.yichengchechebang.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.Bean.CenterSettingBean;
import com.ruoyw.pigpigroad.yichengchechebang.CenterParamActivity;
import com.ruoyw.pigpigroad.yichengchechebang.R;

import java.util.List;

public class CenterSettingAdapter extends RecyclerView.Adapter<CenterSettingAdapter.ViewHolder> {

    private Context context;
    private List<CenterSettingBean> datas;

    public CenterSettingAdapter(Context context,List<CenterSettingBean> datas){
        this.context=context;
        this.datas=datas;
    }

    @Override
    public CenterSettingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.center_setting_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CenterSettingAdapter.ViewHolder holder, final int position) {
            holder.tv_param_name.setText(datas.get(position).getKey_title());
            holder.tv_param_tip.setText(datas.get(position).getKey_tip());
            holder.tv_param_value.setText(datas.get(position).getKey_value());

            holder.change_value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,CenterParamActivity.class);
                    intent.putExtra("id",datas.get(position).getId()+"");
                    intent.putExtra("func_name",datas.get(position).getFunc_name());
                    intent.putExtra("key_title",datas.get(position).getKey_title());
                    intent.putExtra("key_tip",datas.get(position).getKey_tip());
                    intent.putExtra("key_value",datas.get(position).getKey_value()+"");
                    context.startActivity(intent);
                }
            });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_param_name,tv_param,tv_param_value,tv_param_tip;
        LinearLayout change_value;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_param=itemView.findViewById(R.id.tv_param);
            tv_param_name=itemView.findViewById(R.id.tv_param_name);
            tv_param_value=itemView.findViewById(R.id.tv_param_value);
            tv_param_tip=itemView.findViewById(R.id.tv_param_tip);
            change_value=itemView.findViewById(R.id.change_value);
        }
    }
}
