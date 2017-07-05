package gongren.com.dlg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import gongren.com.dlg.R;
import gongren.com.dlg.javabean.JobOrdersInfo;
import com.common.utils.DateUtils;

/**
 * Created by liukui .
 * on 2017/4/6
 * 文件描述： 雇主订单列表
 */

public class BossOrderAdapter extends SimpleBaseAdapter<JobOrdersInfo> {

    private Context context;
    public boolean[] isSelectArr;
    private int selectPosition = -1;
    public int checkedIndex = 0;
    public boolean isChecked = false;

    public BossOrderAdapter(Context context, List<JobOrdersInfo> list) {
        super(context, list);
        this.context = context;
    }

    public void setDataBean(List<JobOrdersInfo> list) {
        this.list = list;
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.boss_order_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        JobOrdersInfo dataBean = list.get(position);
        holder.nameText.setText(dataBean.postName);

        holder.typeText.setText(dataBean.postTypeName);


        String time = DateUtils.getTimeShow(dataBean.demandType, dataBean.startYear, dataBean.startMonth, dataBean.startDay
                , dataBean.startHour, dataBean.startMinute, dataBean.endYear, dataBean.endMonth, dataBean.endDay,
                dataBean.endHour, dataBean.endMinute);

        holder.timeText.setText(time);
//        holder.checkBox.setChecked(isSelectArr[position]);
        holder.checkBox.setChecked(false);
        if (selectPosition == position) {
            holder.checkBox.setChecked(true);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectPosition == position){
                    selectPosition = -1;
                }else{
                    selectPosition = position;
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.boss_order_checkbox)
        RadioButton checkBox;
        @Bind(R.id.boss_order_name)
        TextView nameText;
        @Bind(R.id.boss_order_price)
        TextView priceText;
        @Bind(R.id.boss_order_type)
        TextView typeText;
        @Bind(R.id.boss_order_time)
        TextView timeText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
