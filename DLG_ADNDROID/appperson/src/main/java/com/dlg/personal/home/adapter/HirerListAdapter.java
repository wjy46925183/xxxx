package com.dlg.personal.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.common.view.loadmore.BaseLoadMoreHeaderAdapter;
import com.common.view.loadmore.BaseViewHolder;
import com.dlg.data.home.model.BossListBean;
import com.dlg.personal.R;

import java.util.List;

/**
 * 作者：wangdakuan
 * 主要功能：雇主列表数据适配器
 * 创建时间：2017/6/27 09:05
 */
public class HirerListAdapter extends BaseLoadMoreHeaderAdapter<BossListBean> {
    public HirerListAdapter(Context mContext, RecyclerView recyclerView, List<BossListBean> mDatas, int mLayoutId) {
        super(mContext, recyclerView, mDatas, mLayoutId);
    }

    @Override
    public void convert(Context mContext, RecyclerView.ViewHolder holder, int position, BossListBean bossListBean) {
        if (holder instanceof BaseViewHolder) {
            Glide.with(mContext).load(bossListBean.getUserLogo())
                    .priority(Priority.HIGH).error(R.mipmap.icon_default_user)
                    .into((ImageView) ((BaseViewHolder) holder).getView(R.id.iv_head));
            ((BaseViewHolder) holder).setText(R.id.tv_score, TextUtils.isEmpty(bossListBean.getCreditCount()) ? "36.5" : bossListBean.getCreditCount());  //诚信值
            ((BaseViewHolder) holder).setText(R.id.tv_name, bossListBean.getName());  //名称
            ((BaseViewHolder) holder).setText(R.id.tv_orders_amount, "已接" + bossListBean.getJoinCount() + "单");  //接单数
            ((BaseViewHolder) holder).setText(R.id.tv_distance, "距离" + bossListBean.getDistance() + "km");  //距离
            if (!TextUtils.isEmpty(bossListBean.getScoreCount())) {//诚信星级
                RatingBar ratingBar = ((BaseViewHolder) holder).getView(R.id.rb_score_grade);
                ratingBar.setRating(Float.parseFloat(bossListBean.getScoreCount()));
            }
            ((BaseViewHolder) holder).setText(R.id.tv_work_skill, bossListBean.getIdentityName());  //职业
            ((BaseViewHolder) holder).setText(R.id.tv_remarks_explain, "自由、灵活、赚钱");  //职业
            ((BaseViewHolder) holder).setText(R.id.tv_skill, bossListBean.getServerName());  //服务
            ((BaseViewHolder) holder).setVisible(R.id.tv_skill,!TextUtils.isEmpty(bossListBean.getServerName()));
            ((BaseViewHolder) holder).setVisible(R.id.tv_line, position < getItemCount() - 1);  //分割线
        }
    }
}
