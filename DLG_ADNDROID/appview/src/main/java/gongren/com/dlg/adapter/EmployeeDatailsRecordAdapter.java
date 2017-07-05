package gongren.com.dlg.adapter;

import android.content.Context;
import android.text.TextUtils;
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
 * 雇主个人详情---取消记录、迟到记录  Adapter
 */
public class EmployeeDatailsRecordAdapter extends SimpleBaseAdapter<JsonMap<String, Object>> {
    private int showType = -1;

    public EmployeeDatailsRecordAdapter(Context context, List<JsonMap<String, Object>> list, int showType) {
        super(context, list);
        this.showType = showType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_employeedetailslist_cancel, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        JsonMap<String, Object> jsonMap = list.get(position);
        if (!TextUtils.isEmpty(jsonMap.getStringNoNull("meterUnit")) && !TextUtils.isEmpty(jsonMap.getStringNoNull("meterUnitName"))) {
            holder.tvOrderPostname.setText(jsonMap.getString("postName") + " " + jsonMap.getString("price") + "元/" + jsonMap.getString("meterUnit") + jsonMap.getString("meterUnitName"));
        } else {
            if (!TextUtils.isEmpty(jsonMap.getStringNoNull("postName"))) {
                holder.tvOrderPostname.setText(jsonMap.getString("postName") + " " + jsonMap.getString("totalPrice") + "元");
            }
        }
        StringBuilder startTime = new StringBuilder();
        if (!TextUtils.isEmpty(jsonMap.getString("startDate")) && !jsonMap.getString("startDate").equals("null")) {
            startTime.append(DateUtils.dateTimeFormat("yyyy.MM.dd", jsonMap.getString("startDate"))).append("-");
        }
        if (!TextUtils.isEmpty(jsonMap.getString("endDate")) && !jsonMap.getString("endDate").equals("null")) {
            startTime.append(DateUtils.dateTimeFormat("yyyy.MM.dd", jsonMap.getString("endDate")));
        }
        holder.tvOrderTime.setText(startTime.toString());

        if (showType == 0) {
            //取消
            holder.tvCausePrompt.setText("取消原因：");
            holder.tvCauseDescribe.setText(jsonMap.getString("cancelCause"));
        } else if (showType == 1) {
            //迟到
            holder.tvCausePrompt.setText("迟到原因：");
            holder.tvCauseDescribe.setText(jsonMap.getString("exceptionTime") + "分钟");
        }

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_order_postname)
        TextView tvOrderPostname;
        @Bind(R.id.tv_order_time)
        TextView tvOrderTime;
        @Bind(R.id.tv_cause_prompt)
        TextView tvCausePrompt;
        @Bind(R.id.tv_cause_describe)
        TextView tvCauseDescribe;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
