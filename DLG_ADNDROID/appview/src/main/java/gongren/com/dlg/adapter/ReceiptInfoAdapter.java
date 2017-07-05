package gongren.com.dlg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.utils.DateUtils;

import java.util.List;

import aym.util.json.JsonMap;
import butterknife.Bind;
import butterknife.ButterKnife;
import gongren.com.dlg.R;

/**
 * 发票详情-账单适配
 */
public class ReceiptInfoAdapter extends SimpleBaseAdapter<JsonMap<String, Object>> {

    public ReceiptInfoAdapter(Context context, List<JsonMap<String, Object>> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_receipt_info, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        JsonMap<String, Object> jsonMap = list.get(position);

        viewHolder.tvReceiptBillTime.setText(DateUtils.dateTimeFormat("yyyy-MM-dd HH:mm:ss", jsonMap.getString("createTime")));
        viewHolder.tvReceiptBillUs.setText("付给" + jsonMap.getString("name"));
        viewHolder.tvReceiptBillPrice.setText(jsonMap.getString("amount") + "元");
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_receipt_bill_time)
        TextView tvReceiptBillTime;
        @Bind(R.id.tv_receipt_bill_type)
        TextView tvReceiptBillType;
        @Bind(R.id.tv_receipt_bill_us)
        TextView tvReceiptBillUs;
        @Bind(R.id.tv_receipt_bill_price)
        TextView tvReceiptBillPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
