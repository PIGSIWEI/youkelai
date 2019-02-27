package com.ruoyw.pigpigroad.yichengchechebang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruoyw.pigpigroad.yichengchechebang.R;

/**
 * Created by PIGROAD on 2018/3/14.
 */

public class Vippage extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.vip_fragment_layout,container,false);
        return view;
    }
}
