package com.dlg.personal.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.common.view.loadmore.BaseLoadMoreHeaderAdapter;
import com.common.view.loadmore.BaseViewHolder;
import com.dlg.personal.R;

import java.util.List;

/**
 * 作者：王进亚
 * 主要功能：
 * 创建时间：2017/7/4 10:13
 */

public class HirerDeskAdapter extends BaseLoadMoreHeaderAdapter<String> {
    public HirerDeskAdapter(Context mContext, RecyclerView recyclerView, List<String> mDatas, int mLayoutId) {
        super(mContext, recyclerView, mDatas, mLayoutId);
    }

    @Override
    public void convert(Context mContext, RecyclerView.ViewHolder holder, int position, String s) {
        if(holder instanceof BaseViewHolder){
            ((BaseViewHolder) holder).setText(R.id.unit,s);
        }
    }
}
