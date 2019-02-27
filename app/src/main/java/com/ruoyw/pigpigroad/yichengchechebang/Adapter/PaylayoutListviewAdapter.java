package com.ruoyw.pigpigroad.yichengchechebang.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.R;
import com.ruoyw.pigpigroad.yichengchechebang.SUNMI.util.AidlUtil;
import com.ruoyw.pigpigroad.yichengchechebang.ZxingUtils.OrderBackActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

/**
 * Created by PIGROAD on 2018/3/23.
 */

public class PaylayoutListviewAdapter extends BaseAdapter{
    private List<Map<String, Object>> dataList;
    private Context context;
    private int resource;
    private String login_token;
    /**
     *  有参构造
     * @param context   界面
     * @param dataList     数据
     * @param resoure       列表资源文件
     */
    public PaylayoutListviewAdapter(Context context,List<Map<String, Object>> dataList, int resoure){
        this.context=context;
        this.dataList=dataList;
        this.resource=resoure;
    }

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
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        SharedPreferences sp = context.getSharedPreferences("LoginUser", context.MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
        // 声明内部类
        Util util = null;
        // 中间变量
        final int flag = i;
        /**
         * 根据listView工作原理，列表项的个数只创建屏幕第一次显示的个数。
         * 之后就不会再创建列表项xml文件的对象，以及xml内部的组件，优化内存，性能效率
         */
        if (view == null) {
            util = new Util();
            // 给xml布局文件创建java对象
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, null);
            util.gun_number=view.findViewById(R.id.gun_number);
            util.pay_money=view.findViewById(R.id.pay_money);
            util.gun_kind=view.findViewById(R.id.gun_kind);
        //    util.pay_time=view.findViewById(R.id.pay_time);
            util.pay_state=view.findViewById(R.id.pay_state);
            // 增加额外变量
            view.setTag(util);
        }else {
            util = (Util) view.getTag();
        }
        // 获取数据显示在各组件
        final Map<String, Object> map = dataList.get(i);
        util.gun_number.setText((String) map.get("gun_number"));
        util.pay_money.setText((String) map.get("pay_money")+"元");
        util.gun_kind.setText((String) map.get("gun_kind"));
       // util.pay_time.setText((String) map.get("pay_time"));
       // util.pay_state.setImageResource((Integer) map.get("pay_state"));
        //final int paystate =(Integer) map.get("pay_state");
        final String order_id= (String) map.get("orderid");
        final String pay_time= (String) map.get("pay_time");
        final String order_time= (String) map.get("order_time");
        final String name_= (String) map.get("name");
        final String merchant_oil_price= (String) map.get("merchant_oil_price");
        final String use_oil_price= (String) map.get("use_oil_price");
        final String oil_money= (String) map.get("oil_money");
        final String coupon_money= (String) map.get("coupon_money");
        final String oil_lit= (String) map.get("oil_lit");
        final String oil_name= (String) map.get("oil_name");
        final int pay_type= (int) map.get("pay_type");
        final String gun_id= (String) map.get("gun_id");
        final String pay_money= (String) map.get("pay_money");
        final String transaction_id= (String) map.get("transaction_id");
        final int order_status= (int) map.get("order_status");
        view.setOnClickListener(new View.OnClickListener() {
            private TextView orderid,paytime,ordertime,name,gun,price,oillit,oilmoney,realpay;
            private Button btn_ticketprint,btn_orderback;
            @Override

            public void onClick(View view) {
              if (order_status == 3){
                  final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                  final LayoutInflater inflater = LayoutInflater.from(view.getContext());
                  view = inflater.inflate(R.layout.backorder2, null);
                  final Dialog dialog = builder.create();
                  dialog.show();
                  dialog.setContentView(view);
                  dialog.getWindow().setGravity(Gravity.BOTTOM);//可以设置显示的位置setContentView(view);//自定义布局应该在这里添加，要在dialog.show()的后面
                  Window window = dialog.getWindow();
                  window.setGravity(Gravity.BOTTOM);
                  window.setWindowAnimations(R.style.dialog_animation);
//                  window.getDecorView().setPadding(0, 0, 0, 0);
                  WindowManager.LayoutParams lp = window.getAttributes();
                  lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                  lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                  window.setAttributes(lp);
                  orderid=view.findViewById(R.id.orderid);
                  paytime=view.findViewById(R.id.pay_time);
                  ordertime=view.findViewById(R.id.order_time);
                  btn_orderback=view.findViewById(R.id.btn_orderback);
                  name=view.findViewById(R.id.name);
                  gun= view.findViewById(R.id.gun);
                  price=view.findViewById(R.id.price);
                  oillit=view.findViewById(R.id.oillit);
                  oilmoney=view.findViewById(R.id.oilmoney);
                  realpay=view.findViewById(R.id.realpay);
                  realpay.setText(String.valueOf(pay_money)+"元(共优惠"+String.valueOf(coupon_money)+"元)");
                  oilmoney.setText(String.valueOf(oil_money)+"元");
                  oillit.setText(String.valueOf(oil_lit)+"升");
                  price.setText("挂牌："+String.valueOf(use_oil_price)+"元/升(实际"+String.valueOf(merchant_oil_price)+"元/升)");
                  gun.setText(String.valueOf(gun_id)+"号油枪 "+String.valueOf(oil_name));
                  paytime.setText(String.valueOf(pay_time));
                  ordertime.setText(String.valueOf(order_time));
                  name.setText(String.valueOf(name_));
                  orderid.setText(String.valueOf(order_id));
                  btn_ticketprint=view.findViewById(R.id.btn_ticketprint);
                  //小票补打
                  btn_ticketprint.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                            AlertDialog.Builder builder1=new AlertDialog.Builder(view.getContext());
                            builder1.setMessage("确定打印小票吗？");
                            builder1.setTitle("提示");
                            builder1.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                        //printTicket(String.valueOf(order_id),String.valueOf(name_),String.valueOf(oil_name),price.getText().toString(),String.valueOf(oil_lit),String.valueOf(gun_id),String.valueOf(oil_money),String.valueOf(pay_money));
                                        getPrint(String.valueOf(order_id));
                                }
                            });
                            builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            builder1.create().show();
                      }
                  });
                  //退款操作
                  btn_orderback.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          dialog.dismiss();
                          Intent intent =new Intent(view.getContext(), OrderBackActivity.class);
                          intent.putExtra("order_id",order_id);
                          intent.putExtra("transaction_id",transaction_id);
                          intent.putExtra("fee_type",pay_type);
                          view.getContext().startActivity(intent);
                      }
                  });
              }else {

                  AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                  LayoutInflater inflater = LayoutInflater.from(view.getContext());
                  view = inflater.inflate(R.layout.backorder, null);
                  final Dialog dialog = builder.create();
                  dialog.show();
                  dialog.setContentView(view);
                  dialog.getWindow().setGravity(Gravity.BOTTOM);//可以设置显示的位置setContentView(view);//自定义布局应该在这里添加，要在dialog.show()的后面
                  Window window = dialog.getWindow();
                  window.setGravity(Gravity.BOTTOM);
                  window.setWindowAnimations(R.style.dialog_animation);
//                  window.getDecorView().setPadding(0, 0, 0, 0);
                  WindowManager.LayoutParams lp = window.getAttributes();
                  lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                  lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                  window.setAttributes(lp);
                  orderid=view.findViewById(R.id.orderid);
                  paytime=view.findViewById(R.id.paytime);
                  ordertime=view.findViewById(R.id.ordertime);
                  name=view.findViewById(R.id.name);
                  gun= view.findViewById(R.id.gun);
                  btn_ticketprint=view.findViewById(R.id.btn_ticketprint);
                  price=view.findViewById(R.id.price);
                  oillit=view.findViewById(R.id.oillit);
                  oilmoney=view.findViewById(R.id.oilmoney);
                  realpay=view.findViewById(R.id.realpay);
                  realpay.setText(String.valueOf(pay_money)+"元(共优惠"+String.valueOf(coupon_money)+"元)");
                  oilmoney.setText(String.valueOf(oil_money)+"元");
                  oillit.setText(String.valueOf(oil_lit)+"升");
                  price.setText("挂牌"+String.valueOf(use_oil_price)+"元/升 (实际"+String.valueOf(merchant_oil_price)+"元/升)");
                  gun.setText(String.valueOf(gun_id)+"号汽油 "+String.valueOf(oil_name));
                  paytime.setText(String.valueOf(pay_time));
                  ordertime.setText(String.valueOf(order_time));
                  name.setText(String.valueOf(name_));
                  orderid.setText(String.valueOf(order_id));
                  //小票补打
                  btn_ticketprint.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          AlertDialog.Builder builder1=new AlertDialog.Builder(view.getContext());
                          builder1.setMessage("确定打印小票吗？");
                          builder1.setTitle("提示");
                          builder1.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialogInterface, int i) {
                                  //printTicket(String.valueOf(order_id),String.valueOf(name_),String.valueOf(oil_name),price.getText().toString(),String.valueOf(oil_lit),String.valueOf(gun_id),String.valueOf(oil_money),String.valueOf(pay_money));
                                  TKticket(String.valueOf(order_id));
                              }
                          });
                          builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialogInterface, int i) {
                                  dialogInterface.dismiss();
                              }
                          });
                          builder1.create().show();
                      }
                  });
              }
            }
        });
        return view;
    }
    /**
     * 内部类，用于辅助适配
     *
     * @author qiangzi
     *
     */
    class Util {
        ImageView pay_state;
        TextView gun_number, pay_money, gun_kind,pay_time;

    }

    //联网 补打小票
    private void getPrint(String orderid){
        Log.i(PP_TIP,RUOYU_URL+"?request=private.ticket.jiaoban.total.order.ticket.action&token="+login_token+"&platform=android&orderid="+orderid);
        OkGo.<String>post(RUOYU_URL+"?request=private.ticket.jiaoban.total.order.ticket.action&token="+login_token+"&platform=android&orderid="+orderid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            int code=jsonObject.getInt("code");
                            if (code == 0){
                                JSONArray ticket = jsonObject.getJSONArray("ticket");
                                for (int i = 0; i < ticket.length(); i++) {
                                    JSONObject temp = ticket.getJSONObject(i);
                                    JSONObject style = temp.getJSONObject("style");
                                    printServerText(temp.getString("value"), style.getInt("font_size"), style.getInt("is_bold"), style.getInt("is_underline"));
                                }
                                AidlUtil.getInstance().printText("----------------", 48, true, false);
                                AidlUtil.getInstance().print3Line();
                            }else {
                                Toast.makeText(context,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
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

    /**
     * 退款小票
     */
    private void TKticket(final String orderid){
        OkGo.<String>post(RUOYU_URL+"?request=private.refund.order.refund.ticket&token="+login_token+"&platform=android&id="+orderid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            Log.i(PP_TIP,response.body());
                            Log.i(PP_TIP,RUOYU_URL+"?request=private.refund.order.refund.ticket&token="+login_token+"&platform=android&id="+orderid);
                            JSONObject jsonObject=new JSONObject(response.body());
                            int code=jsonObject.getInt("code");
                            if (code == 0){
                                //语音播报操作
                                //打印小票操作
                                JSONArray ticket = jsonObject.getJSONArray("ticket");
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

}
