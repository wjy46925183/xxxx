package gongren.com.dlg.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.string.StringUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import gongren.com.dlg.R;
import gongren.com.dlg.activity.DoingOrderData;

/**
 * Created by liukui .
 * on 2017/4/6
 * 文件描述：
 */

public class WorkerSearcherHistoryAdapter extends SimpleBaseAdapter<DoingOrderData> {

    private boolean mIsShowDeleteCheckBox;

    private Context context;

    public WorkerSearcherHistoryAdapter(Context context, List<DoingOrderData> list) {
        super(context, list);
        this.context = context;
    }

    public void setDataBean(List<DoingOrderData> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.worker_history_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final DoingOrderData data = list.get(position);
        if (data != null) {
            if (TextUtils.isEmpty(data.getPostName())) {
                holder.nameText.setVisibility(View.GONE);
            } else {
                holder.nameText.setVisibility(View.VISIBLE);
            }
            holder.nameText.setText(data.getPostName());
            if (TextUtils.isEmpty(data.getJobType()) || "null".equals(data.getJobType()) || StringUtil.isEmpty(data.getJobTypeName()) || "null".equals(data.getJobTypeName())) {
                holder.typeText.setVisibility(View.GONE);
            } else {
                holder.typeText.setVisibility(View.VISIBLE);
            }
            holder.typeText.setText(data.getJobTypeName());
            if (data.getDemandType().equals("3")) {
                holder.priceText.setText(data.getPrice() + "元/件");
                holder.timeText.setText("计件");

            } else {
                if (data.getDemandType().equals("1")) {
                    holder.timeText.setText("工作日");
                } else if (data.getDemandType().equals("2")) {
                    holder.timeText.setText("双休日");
                }

                if (TextUtils.isEmpty(data.getPrice()) || "null".equals(data.getPrice())) {
                    holder.priceText.setVisibility(View.GONE);
                } else {
                    holder.priceText.setVisibility(View.VISIBLE);
                }
                if (data.getJobMeterUnit().equals("1")) {
                    holder.priceText.setText(data.getPrice() + "元/日");
                } else {
                    holder.priceText.setText(data.getPrice() + "元/时");
                }

            }


        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.work_history_name)
        TextView nameText;
        @Bind(R.id.work_history_type)
        TextView typeText;
        @Bind(R.id.work_history_price)
        TextView priceText;
        @Bind(R.id.work_history_time)
        TextView timeText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

