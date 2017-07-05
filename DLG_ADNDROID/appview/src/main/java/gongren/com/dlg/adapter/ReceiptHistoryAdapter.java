package gongren.com.dlg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.utils.DateUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import gongren.com.dlg.R;
import gongren.com.dlg.javabean.ReceiptHistoryDataJson;

/**
 * 开票历史适配器
 */
public class ReceiptHistoryAdapter extends SimpleBaseAdapter<ReceiptHistoryDataJson.DataBean> {

    public ReceiptHistoryAdapter(Context context, List<ReceiptHistoryDataJson.DataBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_receipt_history, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        ReceiptHistoryDataJson.DataBean dataBean = list.get(position);

        viewHolder.tvBillingHistoryTime.setText(DateUtils.dateTimeFormat("yyyy.MM.dd HH:mm", dataBean.getCreateTime()));
        viewHolder.tvBillingHistoryPrice.setText(dataBean.getInvoiceAmount()+"");
           // JSONObject invoiceInfo = new JSONObject(jsonMap.getString("invoiceInformationRestVo"));
            viewHolder.tvBillingHistoryTitle.setText(dataBean.getInvoiceInformationRestVo().getCompanyName());


        int status =dataBean.getStatus();
        if(status==1){
            viewHolder.tvBillingTypeAdvance.setVisibility(View.VISIBLE);
            viewHolder.tvBillingTypeSendoff.setVisibility(View.GONE);
        }else if(status==2){

        }else if(status==3){
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
        TextView tvBillingTypeAdvance;//申请中
        @Bind(R.id.tv_billing_type_sendoff)
        TextView tvBillingTypeSendoff;//已寄出
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
