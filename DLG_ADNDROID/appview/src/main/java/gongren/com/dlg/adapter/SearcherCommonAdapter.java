package gongren.com.dlg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import aym.util.json.JsonMap;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import gongren.com.dlg.R;
import com.common.utils.StringUtils;

/**
 * 公共查询适配器
 * 2017/04/05
 * 刘奎
 */
public class SearcherCommonAdapter extends SimpleBaseAdapter<JsonMap<String, Object>> {
    ViewHolder holder;
    private Context context;

    public SearcherCommonAdapter(Context context, List<JsonMap<String, Object>> list) {
        super(context, list);
        this.context = context;
    }

    public void setDataBean(List<JsonMap<String, Object>> list) {
        this.list = list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.search_common_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        JsonMap<String, Object> jsonMap = list.get(position);

        int startYear = jsonMap.getInt("startYear");
        int startMonth = jsonMap.getInt("startMonth");
        int startDay = jsonMap.getInt("startDay");
        int startHour = jsonMap.getInt("startHour");
        int startMinute = jsonMap.getInt("startMinute");
        int endYear = jsonMap.getInt("endYear");
        int endMonth = jsonMap.getInt("endMonth");
        int endDay = jsonMap.getInt("endDay");
        int endHour = jsonMap.getInt("endHour");
        int endMinute = jsonMap.getInt("endMinute");
        int demandType = jsonMap.getInt("demandType");
        StringBuffer sb = new StringBuffer();
        sb.append(StringUtils.get2Str(startYear)).append("-" + StringUtils.get2Str(startMonth))
                .append("-" + StringUtils.get2Str(startDay));
        if (demandType == 3) {
            sb.append("  " + StringUtils.get2Str(startHour)).append(":" + StringUtils.get2Str(startMinute));
        } else {
            if (startYear == endYear && endMonth == startMonth && startDay == endDay) {

            } else {
                if (startYear != endYear) {
                    sb.append(StringUtils.get2Str(endYear) + "-");
                }
                sb.append("至").append(StringUtils.get2Str(endMonth)).append("-" + StringUtils.get2Str(endDay));
            }

            sb.append("  " + StringUtils.get2Str(startHour)).append(":" + StringUtils.get2Str(startMinute))
                    .append("-" + StringUtils.get2Str(endHour)).append(":" + StringUtils.get2Str(endMinute));

        }

        holder.nameText.setText(jsonMap.getString("postName"));
        holder.tvTime.setText(sb.toString());
        Glide.with(context).load(jsonMap.getString("userLogo")).into(holder.ivHead);
        holder.fenText.setText(jsonMap.getStringNoNull("demandType").equals("1") ? "工作日" : jsonMap.getStringNoNull("demandType").equals("2") ? "双休日" : "计件");
        holder.tvAddress.setText(jsonMap.getStringNoNull("cityName") + jsonMap.getStringNoNull("areaName")
                + jsonMap.getStringNoNull("villageName") + jsonMap.getStringNoNull("workAddress"));
        holder.ivCost.setText(jsonMap.getStringNoNull("userCreditCount"));
        if("志愿义工".equals(jsonMap.getStringNoNull("postTypeName"))){
            holder.cancleReseasonText.setText(jsonMap.getStringNoNull("price") + "元");
        }else{
            holder.cancleReseasonText.setText(jsonMap.getStringNoNull("price") + "元/" + jsonMap.getStringNoNull("jobMeterUnitName"));
        }

        holder.cancleReseasonTab.setText(jsonMap.getStringNoNull("distance"));
        return convertView;
    }

    static class ViewHolder {

        @Bind(R.id.searcher_iv_head)
        CircleImageView ivHead;
        @Bind(R.id.searcher_tv_name)
        TextView nameText;
        @Bind(R.id.searcher_tv_chengdu)
        TextView fenText;
        @Bind(R.id.searcher_tv_distance)
        TextView cancleReseasonTab;
        @Bind(R.id.searcher_tv_position)
        TextView cancleReseasonText;
        @Bind(R.id.searcher_tv_time)
        TextView tvTime;
        @Bind(R.id.searcher_tv_address)
        TextView tvAddress;
        @Bind(R.id.searcher_iv_cost)
        TextView ivCost;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
