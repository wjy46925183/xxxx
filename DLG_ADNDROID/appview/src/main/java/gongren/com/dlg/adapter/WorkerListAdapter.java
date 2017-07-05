package gongren.com.dlg.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.common.utils.DateUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import gongren.com.dlg.R;
import gongren.com.dlg.javabean.EmployeeJob;

/**
 * Created by Administrator on 2017/3/29.
 */
public class WorkerListAdapter extends BaseAdapter {
    private List<EmployeeJob> mList;
    private Context mContext;

    public WorkerListAdapter(Context context, List<EmployeeJob> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public EmployeeJob getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_workerlist, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        EmployeeJob employeeJob = getItem(position);

        holder.workerorder_tv_name.setText(employeeJob.postName);
        if ("志愿义工".equals(employeeJob.postTypeName)) {
            holder.workerorder_tv_position.setText(employeeJob.postTypeName);
        } else {
            holder.workerorder_tv_position.setText(employeeJob.price + "元/" + employeeJob.jobMeterUnitName);
        }
        Glide.with(mContext).load(employeeJob.userLogo).error(R.mipmap.morentouxiang).into(holder.workerorder_iv_head);
        holder.tv_grade.setText(TextUtils.isEmpty(employeeJob.userCreditCount) ? "36.5" : employeeJob.userCreditCount);
        holder.tv_distance.setText(TextUtils.isEmpty(employeeJob.distance)?"未知":employeeJob.distance+"km");
        //需求类型(1.工作日,2.双休日,3.计件)
        switch (employeeJob.demandType) {
            case 1:
                holder.workerorder_tv_chengdu.setText("工作日");
                break;
            case 2:
                holder.workerorder_tv_chengdu.setText("双休日");
                break;
            case 3:
                holder.workerorder_tv_chengdu.setText("计件");
                break;
        }

        int startYear = employeeJob.startYear;
        int startMonth = employeeJob.startMonth;
        int startDay = employeeJob.startDay;
        int startHour = employeeJob.startHour;
        int startMin = employeeJob.startMinute;
        int endYear = employeeJob.endYear;
        int endMonth = employeeJob.endMonth;
        int endDay = employeeJob.endDay;
        int endHour = employeeJob.endHour;
        int endMin = employeeJob.endMinute;
        int issafe = employeeJob.isFarmersInsurance;
        //是否买保险
        if (issafe==1){
            holder.baoxian.setVisibility(View.VISIBLE);
        }else {
            holder.baoxian.setVisibility(View.INVISIBLE);
        }

        String dateStr = DateUtils.getTimeShow(employeeJob.demandType, startYear, startMonth
                , startDay, startHour, startMin, endYear, endMonth, endDay, endHour, endMin);
        holder.tv_job_time.setText(dateStr);
        StringBuilder address = new StringBuilder(employeeJob.cityName);
        address.append(employeeJob.areaName).append(employeeJob.villageName);
        address.append(employeeJob.workAddress);
        holder.tv_job_address.setText(address.toString());
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.workerorder_tv_name)
        TextView workerorder_tv_name;
        @Bind(R.id.workerorder_tv_position)
        TextView workerorder_tv_position;
        @Bind(R.id.workerorder_tv_chengdu)
        TextView workerorder_tv_chengdu;
        @Bind(R.id.workerorder_iv_head)
        CircleImageView workerorder_iv_head;
        @Bind(R.id.tv_job_time)
        TextView tv_job_time;
        @Bind(R.id.tv_grade)
        TextView tv_grade;
        @Bind(R.id.tv_job_address)
        TextView tv_job_address;
        @Bind(R.id.tv_distance)
        TextView tv_distance;
        @Bind(R.id.tv_baoxian)
        TextView baoxian;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
