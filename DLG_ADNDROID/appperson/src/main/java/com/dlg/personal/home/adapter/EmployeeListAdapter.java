package com.dlg.personal.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.common.view.loadmore.BaseLoadMoreHeaderAdapter;
import com.common.view.loadmore.BaseViewHolder;
import com.dlg.data.home.model.EmployeeListBean;
import com.dlg.personal.R;

import java.util.List;

/**
 * Created by wangjinya on 2017/6/20.
 */

public class EmployeeListAdapter extends BaseLoadMoreHeaderAdapter<EmployeeListBean> {
    public EmployeeListAdapter(Context mContext, RecyclerView recyclerView, List<EmployeeListBean> mDatas, int mLayoutId) {
        super(mContext, recyclerView, mDatas, mLayoutId);
    }
    @Override
    public void convert(Context mContext, RecyclerView.ViewHolder holder, int position, EmployeeListBean employeeListBean) {
        if(holder instanceof BaseViewHolder){
            ((BaseViewHolder) holder).setText(R.id.workerorder_tv_name,employeeListBean.getPostName());
//            ((BaseViewHolder) holder).setText(R.id.workerorder_tv_chengdu,);
            Glide.with(mContext)
                    .load(employeeListBean.getUserLogo())
                    .placeholder(R.mipmap.icon_default_user)
                    .into((ImageView) ((BaseViewHolder) holder).getView(R.id.workerorder_iv_head));
            ((BaseViewHolder) holder).setText(R.id.tv_grade,employeeListBean.getUserCreditCount()+"");
            if ("志愿义工".equals(employeeListBean.getPostTypeName())) {
                ((BaseViewHolder) holder).setText(R.id.workerorder_tv_position,employeeListBean.getPostTypeName());
            } else {
                ((BaseViewHolder) holder).setText(R.id.workerorder_tv_position,employeeListBean.getPrice()+
                        "元/"+employeeListBean.getJobMeterUnitName());
            }

            ((BaseViewHolder) holder).setText(R.id.tv_grade,TextUtils.isEmpty(employeeListBean.getUserCreditCount()+"") ? "36.5" : employeeListBean.getUserCreditCount()+"'");
            ((BaseViewHolder) holder).setText(R.id.tv_distance,TextUtils.isEmpty(employeeListBean.getDistance()+"")?"未知":employeeListBean.getDistance()+"km");
            ((BaseViewHolder) holder).setText(R.id.workerorder_tv_chengdu,employeeListBean.getDemandTypeStr());
            ((BaseViewHolder) holder).setText(R.id.tv_job_time,employeeListBean.getDataTime());
            ((BaseViewHolder) holder).setText(R.id.tv_job_address,employeeListBean.getAddressInfo());
            int issafe = employeeListBean.getIsFarmersInsurance();
            //是否买保险
            if (issafe==1){
                ((BaseViewHolder) holder).setVisible(R.id.insurance,true);
            }else {
                ((BaseViewHolder) holder).setVisible(R.id.insurance,false);
            }
            if(position < getItemCount()-1){
                ((BaseViewHolder) holder).setVisible(R.id.tv_line,true);
            }else {
                ((BaseViewHolder) holder).setVisible(R.id.tv_line,false);
            }
        }
    }
}
