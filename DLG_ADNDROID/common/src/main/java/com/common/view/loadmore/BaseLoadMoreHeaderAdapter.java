package com.common.view.loadmore;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import demo.java.com.common.R;

public abstract class BaseLoadMoreHeaderAdapter<T> extends RecyclerView.Adapter {
    private Context mContext;
    private boolean isLoading = false;
    private OnLoadMoreListener mOnLoadMoreListener;
    private OnItemClickListener mItemClickListener;
    private onLongItemClickListener mLongItemClickListener;
    private List<T> mDatas;
    private int mLayoutId;
    private View mHeadView;
    private final static int TYPE_HEADVIEW = 100;
    private final static int TYPE_ITEM = 101;
    private final static int TYPE_PROGRESS = 102;
    private TextView mTv_end;
    private TextView mTv_loading;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;

    public BaseLoadMoreHeaderAdapter(Context mContext, RecyclerView recyclerView, List<T> mDatas, int mLayoutId) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.mLayoutId = mLayoutId;
        this.mRecyclerView = recyclerView;
        init(recyclerView);
    }

    private void init(RecyclerView recyclerView) {
        //mRecyclerView添加滑动事件监听
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //得到当前显示的最后一个item的view
                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1);
                //得到lastChildView的bottom坐标值
                int lastChildBottom = lastChildView.getBottom();
                //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
                int recyclerBottom = recyclerView.getBottom() - recyclerView.getPaddingBottom();
                //通过这个lastChildView得到这个view当前的position值
                int lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();

                //判断lastChildView的bottom值跟recyclerBottom
                //判断lastPosition是不是最后一个position
                //如果两个条件都满足则说明是真正的滑动到了底部
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&lastChildBottom == recyclerBottom && lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
//                    Toast.makeText(mContext, "滑动到底了", Toast.LENGTH_SHORT).show();
                    //此时是刷新状态
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                        mProgressBar.setVisibility(View.VISIBLE);
                        mTv_loading.setVisibility(View.VISIBLE);
                        manager.scrollToPositionWithOffset(mDatas.size(),0);//确保刷新的时候 能看到刷新控件
                    }
                    isLoading = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }
        });
    }

    public void updateData(List<T> data) {
        mDatas.clear();
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    public void addAll(List<T> data) {
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    public void addHeadView(View headView) {
        mHeadView = headView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
            BaseViewHolder baseViewHolder = new BaseViewHolder(itemView);
            return baseViewHolder;
        } else if (viewType == TYPE_HEADVIEW) {
            HeadViewHolder headViewHolder = new HeadViewHolder(mHeadView);
            return headViewHolder;
        } else {
            View progressView = LayoutInflater.from(mContext).inflate(R.layout.progress_item, parent, false);
            mTv_end = (TextView) progressView.findViewById(R.id.end);
            mTv_loading = (TextView) progressView.findViewById(R.id.tv_loading);
            mProgressBar = (ProgressBar) progressView.findViewById(R.id.pb);

            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            if (linearLayoutManager.getHeight() < mContext.getResources().getDisplayMetrics().heightPixels) {
                mProgressBar.setVisibility(View.GONE);
                mTv_loading.setVisibility(View.GONE);
            }

            ProgressViewHolder progressViewHolder = new ProgressViewHolder(progressView);
            return progressViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof BaseViewHolder) {
            convert(mContext, holder, position, mDatas.get(position));
            ((BaseViewHolder) holder).mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null)
                        mItemClickListener.onItemClick(v, position);
                }
            });
            ((BaseViewHolder) holder).mItemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mLongItemClickListener != null)
                        mLongItemClickListener.onLongItemClick(v, position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeadView != null) {
            if (position == getItemCount() - 1) {
                return TYPE_PROGRESS;
            } else if (position == 0) {
                return TYPE_HEADVIEW;
            } else {
                return TYPE_ITEM;
            }
        } else {
            if (position == getItemCount() - 1) {
                return TYPE_PROGRESS;
            } else {
                return TYPE_ITEM;
            }
        }
    }


    public abstract void convert(Context mContext, RecyclerView.ViewHolder holder,int position, T t);

    @Override
    public int getItemCount() {
        return mDatas.size() + 1;
    }

    public void setLoading(boolean b) {
        isLoading = b;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface onLongItemClickListener {
        void onLongItemClick(View view, int postion);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setonLongItemClickListener(onLongItemClickListener listener) {
        this.mLongItemClickListener = listener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mOnLoadMoreListener = listener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class HeadViewHolder extends RecyclerView.ViewHolder {
        public HeadViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void completeLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.GONE);
                mTv_loading.setVisibility(View.GONE);
            }
        },500);
    }
}