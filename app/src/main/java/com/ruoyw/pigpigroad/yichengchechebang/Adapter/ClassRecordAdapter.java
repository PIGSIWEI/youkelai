package com.ruoyw.pigpigroad.yichengchechebang.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.R;
import com.ruoyw.pigpigroad.yichengchechebang.SUNMI.util.AidlUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

/**
 * Created by PIGROAD on 2018/3/27.
 */

public class ClassRecordAdapter extends BaseAdapter {
    private List<Map<String, Object>> dataList;
    private Context context;
    private int resource;
    private String login_token;

    public ClassRecordAdapter(Context context, List<Map<String, Object>> dataList, int resoure,String login_token) {
        this.dataList = dataList;
        this.context = context;
        this.login_token=login_token;
        this.resource = resoure;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Util util = null;
        // 中间变量
        final int flag = i;
        /**
         * 根据listView工作原理，列表项的个数只创建屏幕第一次显示的个数。
         * 之后就不会再创建列表项xml文件的对象，以及xml内部的组件，优化内存，性能效率
         */
        if (view == null) {
            util = new Util();
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, null);
            util.addtime = view.findViewById(R.id.addtime);
            util.jiaoban_no = view.findViewById(R.id.jiaoban_no);
            view.setTag(util);
        } else {
            util = (Util) view.getTag();
        }
        final Map<String, Object> map = dataList.get(i);
        util.addtime.setText((String) map.get("addtime"));
        util.jiaoban_no.setText((String) map.get("jiaoban_no"));
        final String add_time = ((String) map.get("addtime"));
        final String jiaoban__no = ((String) map.get("jiaoban_no"));
        final String jiaoban__name = ((String) map.get("jiaoban_name"));
        final String over_time = ((String) map.get("over_time"));
        final String start_time = ((String) map.get("start_time"));
        final String order_other_money = ((String) map.get("order_other_money"));
        //final String order_oil_money = ((String) map.get("order_oil_money"));
        final String order_oil_lit = ((String) map.get("order_oil_lit"));
        final String order_coupon_total = ((String) map.get("order_coupon_total"));
        final String store_name = ((String) map.get("store_name"));
        final String order_money = ((String) map.get("order_money"));
        final String order_pay_money = ((String) map.get("order_pay_money"));
        final String order_count = ((String) map.get("order_count"));
        final String order_return_count = ((String) map.get("order_return_count"));
        final String order_return_pay_money = ((String) map.get("order_return_pay_money"));
        final String order_discount_money = ((String) map.get("order_discount_money"));


        view.setOnClickListener(new View.OnClickListener() {
            private TextView jiaobanno, jiaobanname, starttime, overtime, ordermoney, orderpaymoney, ordercount, orderreturnpaymoney,
                    orderreturncount, orderdiscountmoney, addtime, orderoilmoney, orderothermoney, orderoillit, ordercoupontotal,
                    storename;
            Button btn_ticketprint;

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                LayoutInflater inflater = LayoutInflater.from(view.getContext());
                view = inflater.inflate(R.layout.class_record_details, null);
                final Dialog dialog = builder.create();
                dialog.show();
                dialog.setContentView(view);
                dialog.getWindow().setGravity(Gravity.BOTTOM);//可以设置显示的位置setContentView(view);//自定义布局应该在这里添加，要在dialog.show()的后面
                Window window = dialog.getWindow();
                //    window.setGravity(Gravity.BOTTOM);
                window.setWindowAnimations(R.style.dialog_animation);
                //     window.getDecorView().setPadding(0, 0, 0, 0);
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
                jiaobanno = view.findViewById(R.id.jiaobanno);
                btn_ticketprint = view.findViewById(R.id.btn_ticketprint);
                jiaobanname = view.findViewById(R.id.jiaobanname);
                starttime = view.findViewById(R.id.starttime);
                overtime = view.findViewById(R.id.overtime);
                ordermoney = view.findViewById(R.id.ordermoney);
                orderpaymoney = view.findViewById(R.id.orderpaymoney);
                ordercount = view.findViewById(R.id.ordercount);
                orderreturnpaymoney = view.findViewById(R.id.orderreturnpaymoney);
                orderreturncount = view.findViewById(R.id.orderreturncount);
                orderdiscountmoney = view.findViewById(R.id.orderdiscountmoney);
                addtime = view.findViewById(R.id.addtime);
//                orderoilmoney = view.findViewById(R.id.orderoilmoney);
                orderothermoney = view.findViewById(R.id.orderothermoney);
                orderoillit = view.findViewById(R.id.orderoillit);
                ordercoupontotal = view.findViewById(R.id.ordercoupontotal);
                storename = view.findViewById(R.id.storename);
                jiaobanno.setText(jiaoban__no);
                jiaobanname.setText(jiaoban__name);
                overtime.setText(over_time);
                ordermoney.setText(order_money + "元");
                orderpaymoney.setText(order_pay_money + "元");
                ordercount.setText(order_count);
                orderreturnpaymoney.setText(order_return_pay_money + "元");
                orderreturncount.setText(order_return_count);
                orderdiscountmoney.setText(order_discount_money);
                starttime.setText(start_time);
                addtime.setText(add_time);
                //orderoilmoney.setText(order_oil_money);
                orderothermoney.setText(order_count);
                ordercount.setText(order_other_money);
                orderoillit.setText(order_oil_lit + "升");
                ordercoupontotal.setText(order_coupon_total + "元");
                storename.setText(store_name);
                btn_ticketprint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BJprint(jiaoban__no);
                        dialog.dismiss();
                    }
                });

            }
        });
        return view;
    }

    /**
     * 内部类，用于辅助适配
     *
     * @author qiangzi
     */
    class Util {
        TextView addtime, jiaoban_no;
    }

    /**
     * 班结打印
     */
    private void BJprint(String jiaoban_id){
        OkGo.<String>post(RUOYU_URL+"?request=private.order.jiaoban_ticket_action&token="+login_token+"&platform=android&jiaoban_id="+jiaoban_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            int code=jsonObject.getInt("code");
                            if (code == 0){
                                //语音播报操作
                                //打印小票操作
                                JSONArray ticket = jsonObject.getJSONArray("ticket");
//                                String title = ticket.getString("title");
//                                String who_discount = ticket.getString("who_discount");
//                                String pay_time = ticket.getString("pay_time");
//                                String orderid = ticket.getString("orderid");
//                                String store_name = ticket.getString("store_name");
//                                String oil_name = ticket.getString("oil_name");
//                                String price = ticket.getString("price");
//                                String lit = ticket.getString("lit");
//                                String gun_id = ticket.getString("gun_id");
//                                String total = ticket.getString("total");
//                                String pay_money = ticket.getString("pay_money");
//                                String pay_type=ticket.getString("pay_type");
//                                printTicket(title, who_discount, pay_time, store_name, orderid, oil_name, price, lit, gun_id, total, pay_money, id,pay_type);
                                for (int i = 0; i < ticket.length(); i++) {
                                    JSONObject temp = ticket.getJSONObject(i);
                                    JSONObject style = temp.getJSONObject("style");
                                    printServerText(temp.getString("value"), style.getInt("font_size"), style.getInt("is_bold"), style.getInt("is_underline"));
                                }
                                AidlUtil.getInstance().printText("----------------", 48, true, false);
                                AidlUtil.getInstance().print3Line();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    /**
     * 从服务器获取打印信息
     */
    private void printServerText(String value, int font_size, int isBold, int is_underline) {
        Boolean b_isBolde;
        Boolean b_is_underline;
        if (isBold == 0) {
            b_isBolde = false;
        } else {
            b_isBolde = true;
        }
        if (is_underline == 0) {
            b_is_underline = false;
        } else {
            b_is_underline = true;
        }
        AidlUtil.getInstance().printText(value, font_size, b_isBolde, b_is_underline);
    }
}
