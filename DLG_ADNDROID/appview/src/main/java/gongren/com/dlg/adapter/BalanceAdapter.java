package gongren.com.dlg.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.utils.DateUtils;
import com.common.utils.SharedPreferencesUtils;

import java.util.Calendar;
import java.util.List;

import aym.util.json.JsonMap;
import butterknife.Bind;
import butterknife.ButterKnife;
import gongren.com.dlg.R;
import gongren.com.dlg.activity.BalanceInfoActivity;

import static com.common.utils.SharedPreferencesUtils.USERID;

/**
 * 余额明细Adapter
 */
public class BalanceAdapter extends SimpleBaseAdapter<JsonMap<String, Object>> {
    private Context mContext ;

    public BalanceAdapter(Context context, List<JsonMap<String, Object>> list) {
        super(context, list);
        mContext = context ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_balance, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        final JsonMap<String, Object> jsonMap = list.get(position);
        Calendar cal = Calendar.getInstance();

        int year = Integer.parseInt(DateUtils.dateTimeFormat("yyyy", jsonMap.getString("createTime")));//创建年
        int month = Integer.parseInt(DateUtils.dateTimeFormat("MM", jsonMap.getString("createTime")));//创建月份
        int day = Integer.parseInt(DateUtils.dateTimeFormat("dd", jsonMap.getString("createTime")));//创建天
        int currentYear = cal.get(Calendar.YEAR);
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        int currentDay = cal.get(Calendar.DATE);
        String unit = "";

        if (position > 0) {
            int lastMonth = Integer.parseInt(DateUtils.dateTimeFormat("MM", list.get(position - 1).getString("createTime")));
            if (lastMonth == month) {
                viewHolder.rlMonthBg.setVisibility(View.GONE);
            } else {
                viewHolder.rlMonthBg.setVisibility(View.VISIBLE);
            }
        } else {
            viewHolder.rlMonthBg.setVisibility(View.VISIBLE);
        }
        if (month == currentMonth && year == currentYear) {
            //            viewHolder.rlMonthBg.setVisibility(View.GONE);
            viewHolder.tvCreateYuerMonth.setText("本月");
        } else {
            //            viewHolder.rlMonthBg.setVisibility(View.GONE);
            viewHolder.tvCreateYuerMonth.setText(DateUtils.dateTimeFormat("yyyy年MM月", jsonMap.getString("createTime")));
        }

        if (day == currentDay && year == currentYear && month == currentMonth) {
            viewHolder.tvType.setText("今天");
        } else if (day == (currentDay - 1) && year == currentYear && month == currentMonth) {
            viewHolder.tvType.setText("昨天");
        } else {
            viewHolder.tvType.setText(DateUtils.dateTimeFormat("dd日", jsonMap.getString("createTime")));
        }
        viewHolder.tvDate.setText(DateUtils.dateTimeFormat("HH:mm", jsonMap.getString("createTime")));

        String payeeId = jsonMap.getStringNoNull("payeeId");
        String userId = SharedPreferencesUtils.getString(mContext,USERID);
        if(TextUtils.equals(userId,payeeId)){
            unit = "+" ;
        }else {
            unit = "-" ;
        }

        StringBuilder stringBuilder = new StringBuilder();
        int tradeType = jsonMap.getInt("tradeType");
        switch (tradeType) {
            case 1:
                stringBuilder.append("充值\n\n\n-\n\n\n");
                break;
            case 2:
                stringBuilder.append("提现\n\n\n-\n\n\n");
                break;
            case 3:
                stringBuilder.append("报酬\n" +
                        "\n" +
                        "\n" +
                        "-\n" +
                        "\n" +
                        "\n");
                break;
            case 4:
                stringBuilder.append("小费\n" +
                        "\n" +
                        "\n" +
                        "-\n" +
                        "\n" +
                        "\n");
                break;
            case 5:
                stringBuilder.append("服务费\n" +
                        "\n" +
                        "\n" +
                        "-\n" +
                        "\n" +
                        "\n");
                break;
            case 6:
                stringBuilder.append("税费\n" +
                        "\n" +
                        "\n" +
                        "-\n" +
                        "\n" +
                        "\n");
                break;
            case 7:
                stringBuilder.append("违约金\n" +
                        "\n" +
                        "\n" +
                        "-\n" +
                        "\n" +
                        "\n");
                break;
            case 8:
                stringBuilder.append("补偿\n" +
                        "\n" +
                        "\n" +
                        "-\n" +
                        "\n" +
                        "\n");
                break;
            case 9:
                stringBuilder.append("活动赠送\n" +
                        "\n" +
                        "\n" +
                        "-\n" +
                        "\n" +
                        "\n");
            break;
            case 10:
                stringBuilder.append("取现手续费\n" +
                        "\n" +
                        "\n" +
                        "-\n" +
                        "\n" +
                        "\n");
                break;
            case 11:
                stringBuilder.append("盈利收益\n" +
                        "\n" +
                        "\n" +
                        "-\n" +
                        "\n" +
                        "\n");
                break;
            case 12:
                stringBuilder.append("服务费\n" +
                        "\n" +
                        "\n" +
                        "-\n" +
                        "\n" +
                        "\n");
                break;
            case 13:
                stringBuilder.append("活动赠送\n" +
                        "\n" +
                        "\n" +
                        "-\n" +
                        "\n" +
                        "\n");
            case 14:
                stringBuilder.append("活动赠送\n" +
                        "\n" +
                        "\n" +
                        "-\n" +
                        "\n" +
                        "\n");
                break;
            case 15:
                stringBuilder.append("保险\n" +
                        "\n" +
                        "\n" +
                        "-\n" +
                        "\n" +
                        "\n");
                break;
            default:
                stringBuilder.append("支付\n" +
                        "\n" +
                        "\n" +
                        "-\n" +
                        "\n" +
                        "\n");
                break;
        }
        int paymentType = jsonMap.getInt("paymentType");
        if(paymentType==0){
            stringBuilder.append("橙子");
        }else if(paymentType==1){
            stringBuilder.append("支付宝");
        }else if(paymentType==2){
            stringBuilder.append("微信");
        }else if(paymentType==3){
            stringBuilder.append("银行卡");
        }
        viewHolder.tvBalance.setText(stringBuilder.toString());
        String status = jsonMap.getString("status");
        String tradeStatus = "";
        if(status.equals("0")){
            tradeStatus ="待支付";
            viewHolder.tv_status.setTextColor(context.getResources().getColor(R.color.right_change_textcolor));
        }else if(status.equals("1")){
            tradeStatus ="进行中";
            viewHolder.tv_status.setTextColor(context.getResources().getColor(R.color.right_change_textcolor));
        }else if(status.equals("2")){
            tradeStatus ="成功";
            viewHolder.tv_status.setTextColor(context.getResources().getColor(R.color.green_textcolor));
        } else if(status.equals("3")){
            tradeStatus ="失败";
            viewHolder.tv_status.setTextColor(context.getResources().getColor(R.color.textColor_alert_button_destructive));
        }
        viewHolder.tv_status.setText("交易"+tradeStatus);
        //        //设置交易金额
        viewHolder.tvMoney.setText(unit + jsonMap.getDouble("amount", 0) + "元");
        //

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                BalanceInfo balanceInfo = new BalanceInfo(cashAva, afterCashAva, subject, memo);
                Bundle bundle = new Bundle();
                //                bundle.putSerializable("balanceInfo", balanceInfo);
                bundle.putString("billId", jsonMap.getString("subBillBusinessNumber"));
//                bundle.putString("billId", "850642196267929600/I/850642196267929600/1491643878415");
                context.startActivity(new Intent(context, BalanceInfoActivity.class).putExtras(bundle));
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.rl_month_bg)
        RelativeLayout rlMonthBg;
        @Bind(R.id.tv_create_yuer_month)
        TextView tvCreateYuerMonth;
        @Bind(R.id.tv_type)
        TextView tvType;
        @Bind(R.id.tv_date)
        TextView tvDate;
        @Bind(R.id.tv_balance)
        TextView tvBalance;
        @Bind(R.id.tv_money)
        TextView tvMoney;
        @Bind(R.id.tv_status)
        TextView tv_status ;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
