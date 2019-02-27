package com.ruoyw.pigpigroad.yichengchechebang.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.MainActivity;
import com.ruoyw.pigpigroad.yichengchechebang.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

/**
 * Created by PIGROAD on 2018/3/14.
 */

public class Oilpage extends Fragment {
    private TextView
            order_oil_lit,order_count,pay_money,
            pay_order_count,pay_order_money,pay_oil_money,pay_coupon_money,pay_pay_money,pay_other_money,pay_discount_money,pay_order_oil_lit,
            back_count,back_refund_money;

    private JSONObject jsonList,totalObj,refundObj;
    private JSONArray group_totalArray;
    private String login_token;
    private RecyclerView recyclerView;
    private OilpageAdapter adapter;
    private List<OilpageBean> data=new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.oils_fragment_layout,container,false);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init(); //初始化控件
        //拿token
        SharedPreferences sp =getActivity().getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token=sp.getString("user_token",null);
         GetClassData();
    }
    /**
     * 初始化控件
     */
    private void init() {
        order_oil_lit=getActivity().findViewById(R.id.order_oil_lit);
        order_count=getActivity().findViewById(R.id.order_count);
        pay_money=getActivity().findViewById(R.id.pay_money);

        pay_order_count=getActivity().findViewById(R.id.pay_order_count);
        pay_order_money=getActivity().findViewById(R.id.pay_order_money);
        pay_coupon_money=getActivity().findViewById(R.id.pay_coupon_money);
        pay_oil_money=getActivity().findViewById(R.id.pay_oil_money);
        pay_pay_money=getActivity().findViewById(R.id.pay_pay_money);
        pay_other_money=getActivity().findViewById(R.id.pay_other_money);
        pay_discount_money=getActivity().findViewById(R.id.pay_discount_money);
        pay_order_oil_lit=getActivity().findViewById(R.id.pay_order_oil_lit);

        back_count=getActivity().findViewById(R.id.back_count);
        back_refund_money=getActivity().findViewById(R.id.back_refund_money);

        recyclerView=getActivity().findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter=new OilpageAdapter(getContext(),data);
        recyclerView.setAdapter(adapter);
    }

    public void GetClassData(){
        long time = System.currentTimeMillis() / 1000;
        String paylisttime = String.valueOf(time);
        OkGo.<String>post(RUOYU_URL + "?request=private.order.ready.jiaoban.get&token=" + login_token + "&platform=android")
                .tag(this)
                .headers("header1", "headerValue1")
                .execute(new StringCallback(){
                    @Override
                    public void onSuccess(Response<String> response) {
                        String responseStr = response.body();//这个就是返回来的结果
                        Log.i("pppppppppppGetClassData", responseStr);
                        //进行json数据解析
                        try {
                            JSONObject jsonObject = new JSONObject(responseStr);
                            //判断code是否成功
                            int code = (int) jsonObject.get("code");
                            //code 0表示成功，999表示身份过期,1表示服务器错误
                            if (code ==0){
                                jsonList = jsonObject.getJSONObject("data");
                                totalObj = jsonList.getJSONObject("total");
                                refundObj = jsonList.getJSONObject("refund");
                                group_totalArray = jsonList.getJSONArray("group_total");
                                //开始填充数据
                                Log.i("pppppppppppppp","开始填充oil数据~~(☄⊙ω⊙)☄");
                                //填充当前订单数量
                                for (int i=0;i< group_totalArray.length();i++){
                                    JSONObject temp=group_totalArray.getJSONObject(i);
                                    OilpageBean bean=new OilpageBean();
                                    bean.setCount(temp.getString("count"));
                                    bean.setOil_lit(temp.getString("oil_lit"));
                                    bean.setOil_name(temp.getString("oil_name"));
                                    bean.setOrder_money(temp.getString("order_money"));
                                    data.add(bean);
                                }
                                adapter.notifyDataSetChanged();

                                order_oil_lit.setText(totalObj.getString("order_oil_lit"));
                                order_count.setText(totalObj.getString("order_count"));
                                pay_money.setText(totalObj.getString("pay_money"));

                                pay_order_count.setText(totalObj.getString("order_count"));
                                pay_order_money.setText(totalObj.getString("order_money"));
                                pay_oil_money.setText(totalObj.getString("oil_money"));
                                pay_coupon_money.setText(totalObj.getString("coupon_money"));
                                pay_pay_money.setText(totalObj.getString("pay_money"));
                                pay_other_money.setText(totalObj.getString("other_money"));
                                pay_discount_money.setText(totalObj.getString("discount_money"));
                                pay_order_oil_lit.setText(totalObj.getString("order_oil_lit"));

                                back_count.setText(refundObj.getString("count"));
                                back_refund_money.setText(refundObj.getString("refund_money"));

                            }else if(code ==1){
                                Toast.makeText(getActivity(), "服务器错误，请联系管理员", Toast.LENGTH_SHORT).show();
                            }else if(code == 999){
                                Toast.makeText(getActivity(), "你的身份已过期，请重新登录", Toast.LENGTH_SHORT).show();
                                SharedPreferences sp = getActivity().getSharedPreferences("LoginUser", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.clear();
                                editor.commit();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.i("ppppppppppppppppp", "连接失败!!!!!!!!!!!!!!!");
                        Toast.makeText(getActivity(), "请检查你的网络设置", Toast.LENGTH_SHORT).show();
                    }
                } );

    }
}