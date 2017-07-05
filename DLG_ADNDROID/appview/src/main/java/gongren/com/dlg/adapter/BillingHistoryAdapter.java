package gongren.com.dlg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import aym.util.json.JsonMap;
import butterknife.Bind;
import butterknife.ButterKnife;
import gongren.com.dlg.R;

/**
 * 开票历史适配器
 */
public class BillingHistoryAdapter extends SimpleBaseAdapter<JsonMap<String, Object>> {

    public BillingHistoryAdapter(Context context, List<JsonMap<String, Object>> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_billing_history, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        if(position%2==0){
            viewHolder.tvBillingTypeAdvance.setVisibility(View.GONE);
            viewHolder.tvBillingTypeSendoff.setVisibility(View.VISIBLE);
        }else {
            viewHolder.tvBillingTypeAdvance.setVisibility(View.VISIBLE);
            viewHolder.tvBillingTypeSendoff.setVisibility(View.GONE);
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_billing_history_time)
        TextView tvBillingHistoryTime;
        @Bind(R.id.tv_billing_history_title)
        TextView tvBillingHistoryTitle;
        @Bind(R.id.tv_billing_history_price)
        TextView tvBillingHistoryPrice;
        @Bind(R.id.tv_billing_type_advance)
        TextView tvBillingTypeAdvance;
        @Bind(R.id.tv_billing_type_sendoff)
        TextView tvBillingTypeSendoff;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
