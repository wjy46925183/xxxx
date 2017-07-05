package com.dlg.personal.home.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.common.view.loadmore.BaseLoadMoreHeaderAdapter;
import com.common.view.loadmore.BaseViewHolder;
import com.dlg.data.home.model.WorkTypeBean;
import com.dlg.personal.R;

import java.util.List;


/**
 * 作者：小明
 * 主要功能：
 * 创建时间：2017/6/29 0029 19:09
 */
public class WorkTypeAdapter extends BaseLoadMoreHeaderAdapter<WorkTypeBean> {

    public WorkTypeAdapter(Context mContext, RecyclerView recyclerView, List<WorkTypeBean> workTypeBean, int mLayoutId) {
        super(mContext, recyclerView, workTypeBean, mLayoutId);
    }

    @Override
    public void convert(Context mContext, RecyclerView.ViewHolder holder, int position, WorkTypeBean workTypeBean) {
        if (holder instanceof BaseViewHolder){
            ((BaseViewHolder) holder).setText(R.id.name_text,workTypeBean.getDataName());

        }
    }

}
