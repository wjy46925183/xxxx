package com.dlg.personal.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.common.sys.RuleUtils;
import com.dlg.personal.R;


/**
 * 作者：wangdakuan
 * 主要功能: 标题ToolBar 封装
 * 创建时间：2017/6/1 12:44
 */
public class HomeToolBarHelper extends BaseBarHelper {

    private ImageView toolbarLeftIamge; //左边图片控件
    private ImageView toolbarRightIamg; //右边图片控件
    private ImageView iamgeShow; //右边图片控件 上添加的提示红点
    private TextView editSearch; //首页搜索框
    private ImageView mIv_search;
    private ProgressBar mPb;

    public HomeToolBarHelper(Context context) {
        super(context);
    }

    public HomeToolBarHelper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeToolBarHelper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HomeToolBarHelper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void initToolBar() {
//        super.initToolBar();
        mToolBarView = mInflater.inflate(R.layout.toolbar_title_home_view, this);
        mToolBar = (LinearLayout) mToolBarView.findViewById(R.id.toolbar);
        toolbarLayout = (LinearLayout) mToolBarView.findViewById(R.id.toolbar_layout);
        toolbarTextRight = (TextView) mToolBarView.findViewById(R.id.toolbar_text_right);
        dividerLine = (TextView) mToolBarView.findViewById(R.id.divider_line);
        toolbarLeftIamge = (ImageView) mToolBarView.findViewById(R.id.toolbar_left_iamge);
        toolbarRightIamg = (ImageView) mToolBarView.findViewById(R.id.toolbar_iamg_right);
        iamgeShow = (ImageView) mToolBarView.findViewById(R.id.iamge_show);
        editSearch = (TextView) mToolBarView.findViewById(R.id.tv_search);
        mIv_search = (ImageView) mToolBarView.findViewById(R.id.iv_search);
        mPb = (ProgressBar) mToolBarView.findViewById(R.id.pb_loading);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //设置状态栏背景状态
            //true：表明当前Android系统版本>=4.4
            mToolBar.setPadding(0, RuleUtils.getStatusBarHeight(mContext), 0, 0);

        } else {
            LayoutParams linearParams = (LayoutParams) mToolBar.getLayoutParams(); //取控件textView当前的布局参数
            linearParams.height = mContext.getResources().getDimensionPixelOffset(R.dimen.height_5_5_80);// 控件的高强制设成20
            mToolBar.setLayoutParams(linearParams); //使设置好的布局参数应用到控件</pre>
            mToolBar.setPadding(0, 0, 0, 0);
        }
    }

    /**
     * 是否显示隐藏左边按钮图片
     *
     * @param visibility
     */
    public void setToolbarLeftIamgeVisibility(int visibility) {
        if (null != toolbarLeftIamge) {
            toolbarLeftIamge.setVisibility(visibility);
        }
    }

    /**
     * 是否显示隐藏右边按钮图片
     *
     * @param visibility
     */
    public void setToolbarRightIamgVisibility(int visibility) {
        if (null != toolbarRightIamg) {
            toolbarRightIamg.setVisibility(visibility);
        }
        if (null != iamgeShow && visibility == View.GONE) {
            iamgeShow.setVisibility(visibility);
        }
    }

    /**
     * 是否显示隐藏提示信息红点图片
     *
     * @param visibility
     */
    public void setIamgeShowVisibility(int visibility) {
        if (null != iamgeShow) {
            iamgeShow.setVisibility(visibility);
        }
    }

    public void setEditSearchText(String text){
        if(null != editSearch){
            editSearch.setText(text);
        }
    }

    /**
     * 左边控件的点击事件
     * @param listener
     */
    public void setToolbarRightIamgOnClickListener(OnClickListener listener) {
        if(null != toolbarRightIamg){
            this.toolbarRightIamg.setOnClickListener(listener);
        }
    }

    /**
     * 右边控件的点击事件
     * @param listener
     */
    public void setToolbarLeftIamgeOnClickListener(OnClickListener listener) {
        if(null != toolbarLeftIamge){
            this.toolbarLeftIamge.setOnClickListener(listener);
        }
    }

    public ImageView getToolbarLeftIamge() {
        return toolbarLeftIamge;
    }

    public ImageView getToolbarRightIamg() {
        return toolbarRightIamg;
    }

    public ImageView getIamgeShow() {
        return iamgeShow;
    }

    public TextView getEditSearch() {
        return editSearch;
    }
    public ImageView getIv_search(){return mIv_search;}
    public ProgressBar getPb(){
        return mPb;
    }
}
