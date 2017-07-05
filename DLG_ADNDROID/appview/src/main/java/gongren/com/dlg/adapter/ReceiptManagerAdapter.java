package gongren.com.dlg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import aym.util.json.JsonMap;
import butterknife.Bind;
import butterknife.ButterKnife;
import gongren.com.dlg.R;

import com.common.utils.DateUtils;

/**
 * 适配系统消息ListView的Adapter
 */
public class ReceiptManagerAdapter extends SimpleBaseAdapter<JsonMap<String, Object>> {
    private boolean isAllSelect = false;

    public ReceiptManagerAdapter(Context context, List<JsonMap<String, Object>> list) {
        super(context, list);
    }

    public void setAllSelect(boolean isAllSelect) {
        this.isAllSelect = isAllSelect;
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_receipt_bill, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        JsonMap<String, Object> jsonMap = list.get(position);

        if(position==0){
            viewHolder.rl_day_bg.setVisibility(View.VISIBLE);
        }else {
            viewHolder.rl_day_bg.setVisibility(View.GONE);
        }

        viewHolder.tv_bill_price.setText(Math.abs(jsonMap.getDouble("amount", 0)) + "元");
        viewHolder.tv_bill_day.setText(DateUtils.getDatedian_yyyy_MM_dd(jsonMap.getString("createTime")));
        viewHolder.tv_bill_time.setText(DateUtils.getDate_Hm(jsonMap.getString("createTime")));
        viewHolder.tv_bill_month.setText(DateUtils.getDate_yyyyAndMM(jsonMap.getString("createTime")));
        viewHolder.cb_bill_check.setChecked(isAllSelect);
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.rl_day_bg)
        RelativeLayout rl_day_bg;
        @Bind(R.id.tv_bill_month)
        TextView tv_bill_month;
        @Bind(R.id.cb_bill_check)
        CheckBox cb_bill_check;
        @Bind(R.id.tv_bill_day)
        TextView tv_bill_day;
        @Bind(R.id.tv_bill_time)
        TextView tv_bill_time;
        @Bind(R.id.tv_bill_type)
        TextView tv_bill_type;
        @Bind(R.id.tv_bill_order)
        TextView tv_bill_order;
        @Bind(R.id.tv_bill_price)
        TextView tv_bill_price;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
