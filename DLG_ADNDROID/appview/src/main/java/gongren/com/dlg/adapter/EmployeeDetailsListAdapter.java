package gongren.com.dlg.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import aym.util.json.JsonMap;
import butterknife.Bind;
import butterknife.ButterKnife;
import gongren.com.dlg.R;
import com.common.utils.DateUtils;

/**
 * 雇主个人详情---发单记录、接单记录  Adapter
 */
public class EmployeeDetailsListAdapter extends SimpleBaseAdapter<JsonMap<String, Object>> {

    public EmployeeDetailsListAdapter(Context context, List<JsonMap<String, Object>> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_employeedetailslist, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        JsonMap<String, Object> jsonMap = list.get(position);
        if("志愿义工".equals(jsonMap.getInt("postTypeName"))){
            holder.tvOrderPostname.setText(jsonMap.getString("postName")+" "+jsonMap.getString("price")+"元");
        }else{
            if(!TextUtils.isEmpty(jsonMap.getStringNoNull("meterUnitName"))){
                holder.tvOrderPostname.setText(jsonMap.getString("postName")+
                        " "+jsonMap.getString("price")+"元/"+jsonMap.getString("meterUnitName"));
            }else {
                holder.tvOrderPostname.setText(jsonMap.getString("postName")+
                        " "+jsonMap.getString("price")+"元");
            }

        }

        StringBuilder startTime = new StringBuilder();
        if(!TextUtils.isEmpty(jsonMap.getString("startDate"))&& !jsonMap.getString("startDate").equals("null")){
            startTime.append(DateUtils.dateTimeFormat("yyyy.MM.dd", jsonMap.getString("startDate"))).append("-");
        }
        if(!TextUtils.isEmpty(jsonMap.getString("endDate"))&& !jsonMap.getString("endDate").equals("null")){
            startTime.append(DateUtils.dateTimeFormat("yyyy.MM.dd", jsonMap.getString("endDate")));
        }
        holder.tvOrderTime.setText(startTime.toString());
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_order_postname)
        TextView tvOrderPostname;
        @Bind(R.id.tv_order_time)
        TextView tvOrderTime;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
