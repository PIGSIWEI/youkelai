package com.ruoyw.pigpigroad.yichengchechebang;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.Adapter.MenuChildAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.MenuEntity;
import com.ruoyw.pigpigroad.yichengchechebang.Adapter.MenuParentAdapter;
import com.ruoyw.pigpigroad.yichengchechebang.Widget.MyGridView;

import java.util.List;

public class MenuParentAdapter2  extends BaseExpandableListAdapter
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private MenuManageActivity2 mContext;
    private List<MenuEntity> addressProvincesList;
    private LayoutInflater inflater;
    private MyGridView toolbarGrid;
    private boolean IsEdit;
    private MenuChildAdapter adapter;

    public MenuParentAdapter2(MenuManageActivity2 mContext, List<MenuEntity> addressProvincesList2) {
        this.mContext = mContext;
        this.addressProvincesList = addressProvincesList2;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return addressProvincesList.get(groupPosition).getChilds();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        // if (convertView == null) {

        convertView = inflater.inflate(R.layout.items_cate_grid_child, null);
        toolbarGrid = (MyGridView) convertView.findViewById(R.id.gv_toolbar);
        toolbarGrid.setNumColumns(4);// 设置每行列数
        toolbarGrid.setGravity(Gravity.CENTER);// 位置居中
        // toolbarGrid.setHorizontalSpacing(10);// 水平间隔
        adapter = new MenuChildAdapter(mContext, addressProvincesList.get(groupPosition).getChilds(),
                IsEdit);
        toolbarGrid.setAdapter(adapter);// 设置菜单Adapter
        toolbarGrid.setOnItemClickListener(this);
        toolbarGrid.setOnItemLongClickListener(this);
        // }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return addressProvincesList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return addressProvincesList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        MenuParentAdapter2.GroupViewHolde groupViewHolde = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.items_cate_parent, null);
            groupViewHolde = new MenuParentAdapter2.GroupViewHolde();
            groupViewHolde.tv_item_cate_name = (TextView) convertView.findViewById(R.id.tv_item_cate_name);
            convertView.setTag(groupViewHolde);
        } else {
            groupViewHolde = (MenuParentAdapter2.GroupViewHolde) convertView.getTag();
        }
        groupViewHolde.tv_item_cate_name.setText(addressProvincesList.get(groupPosition).getTitle());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return false;
    }

    class GroupViewHolde {
        TextView tv_item_cate_name;
        ImageView iv_items_cate_pic;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        MenuEntity indexData = (MenuEntity) adapterView.getItemAtPosition(position);
        if (indexData != null) {
            mContext.initUrl(indexData);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        return false;
    }

    public void setEdit() {
        IsEdit = true;
        notifyDataSetChanged();
    }

    public void endEdit() {
        IsEdit = false;
        notifyDataSetChanged();
    }


}
