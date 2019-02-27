package com.ruoyw.pigpigroad.yichengchechebang.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ruoyw.pigpigroad.yichengchechebang.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by XYSM on 2018/4/4.
 */

public class GroupBuyingFragment2 extends Fragment {
    private Button btn_yanzhen;
    private LinearLayout scan_ll;
    private static final int REQUEST_CODE_SCAN = 0x0000;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.groupbuying_fragment1_layout, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    /**
     * 数据初始化
     */
    private void init() {
        btn_yanzhen = getActivity().findViewById(R.id.btn_yanzhen);
        btn_yanzhen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new TDialog.Builder(getFragmentManager())
//                        .addOnClickListener(R.id.btn_cancel)
//                        .setOnViewClickListener(new OnViewClickListener() {
//                            @Override
//                            public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
//                                switch (view.getId()) {
//                                    case R.id.btn_cancel:
//                                        tDialog.dismiss();
//                                        break;
//                                }
//                            }
//                        })
//                        .setLayoutRes(R.layout.group_buying_dialog)                        //弹窗布局
//                        .setScreenWidthAspect(getActivity(), 0.9f)               //屏幕宽度比
//                        .setDimAmount(0f)                                                    //设置焦点
//                        .create()
//                        .show();
            }
        });
        scan_ll = getActivity().findViewById(R.id.scan_ll);
        scan_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);

                Toast.makeText(getActivity(),content,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
