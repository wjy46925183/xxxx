package gongren.com.dlg.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import gongren.com.dlg.R;
import gongren.com.dlg.javabean.EmployerEmployeeListJson;

/**
 * Created by Administrator on 2017/3/29.
 */
public class BossListAdapter extends BaseAdapter {
    private List<EmployerEmployeeListJson.DataBean> mDataBeanList;
    private Context mContext;

    public BossListAdapter(Context context, List<EmployerEmployeeListJson.DataBean> list) {
        this.mDataBeanList = list;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mDataBeanList == null ? 0 : mDataBeanList.size();
    }

    @Override
    public EmployerEmployeeListJson.DataBean getItem(int position) {
        return mDataBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_bosslist, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        EmployerEmployeeListJson.DataBean employeeData = mDataBeanList.get(position);

        Glide.with(mContext).load(employeeData.userLogo)
                .priority(Priority.HIGH).error(R.mipmap.icon_default_user)
                .into(holder.bossmapIvHead);

        holder.tvEmployeeName.setText(employeeData.name);
        holder.tvOrdersAmount.setText("已接" + employeeData.joinCount + "单");
        if (!TextUtils.isEmpty(employeeData.scoreCount) && employeeData.scoreCount != null) {
            //  holder.rbScoreGrade.setNumStars(Integer.parseInt(employeeData.scoreCount));
            holder.rbScoreGrade.setRating(Float.parseFloat(employeeData.scoreCount));
        }
        if (employeeData.identity == null) {
            holder.tvWorkSkill.setText("自由工作者");
        } else {
            if ("0".equals(employeeData.identity)) {
                holder.tvWorkSkill.setText("在校学生");
            } else if ("1".equals(employeeData.identity)) {
                holder.tvWorkSkill.setText("自由工作者");
            } else {
                holder.tvWorkSkill.setText("兼职人员");
            }
        }
        holder.tvScore.setText(employeeData.creditCount == null ? "36.5" : employeeData.creditCount);
        holder.tvDistance.setText("距离" + employeeData.distance + "km");
        holder.tvRemarksExplain.setText("自由、灵活、赚钱");
        if(employeeData.jobServiceRpcVo!=null){

            holder.tv_skill.setVisibility(View.VISIBLE);

            int serviceMeterUnit = employeeData.jobServiceRpcVo.serviceMeterUnit;
            String unitName = null;
            if (serviceMeterUnit == 1) {
                unitName = "/天";
            } else if (serviceMeterUnit == 2) {
                unitName = "/时";
            } else if (serviceMeterUnit == 3) {
                unitName = "/单";
            }
            holder.tv_skill.setText("提供的服务:" + employeeData.jobServiceRpcVo.serviceName + " "
                    + employeeData.jobServiceRpcVo.price + "元" + unitName
                    + "...等" + employeeData.serviceNum + "项");
        }else{
            holder.tv_skill.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.bossmap_iv_head)
        CircleImageView bossmapIvHead;
        @Bind(R.id.tv_employee_name)
        TextView tvEmployeeName;
        @Bind(R.id.tv_orders_amount)
        TextView tvOrdersAmount;
        @Bind(R.id.rb_score_grade)
        RatingBar rbScoreGrade;
        @Bind(R.id.tv_score)
        TextView tvScore;
        @Bind(R.id.tv_distance)
        TextView tvDistance;
        @Bind(R.id.tv_work_skill)
        TextView tvWorkSkill;
        @Bind(R.id.tv_remarks_explain)
        TextView tvRemarksExplain;
        @Bind(R.id.tv_skill)
        TextView tv_skill;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
