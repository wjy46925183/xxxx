package com.dlg.personal.home.menu;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.utils.StringUtils;
import com.dlg.personal.R;
import com.dlg.personal.base.BaseFragment;
import com.dlg.personal.home.activity.HomeActivity;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * name:wangjinya 2017.6.19
 */
public class UserMenuFragment extends BaseFragment {
    private RelativeLayout mLeft_draw;
    private RelativeLayout mSlide_layout_head;
    private RadioGroup mMap_rg;
    private RadioButton mRb_01;
    private RadioButton mRb_02;
    private CircleImageView mIv_head;
    private TextView mSlid_name;
    private TextView mSlid_status;
    private LinearLayout mLayout_odd;
    private TextView mSlide_linggong;
    private TextView mSlide_money;
    private TextView mSlide_setting;
    private CircleImageView mCiv_drawlay_bottom;
    private TextView mTv_drawlay_zhenwo;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_menu, container, false);
        bindViews(view);
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundColor(Color.WHITE);

        TextView textView = new TextView(mContext);
        textView.setHeight(StringUtils.getStatusHeight(mActivity));
        linearLayout.addView(textView);
        linearLayout.addView(view);
        return linearLayout;
    }

    private void bindViews(View view) {
        mLeft_draw = (RelativeLayout) view.findViewById(R.id.left_draw);
        mSlide_layout_head = (RelativeLayout) view.findViewById(R.id.slide_layout_head);
        mMap_rg = (RadioGroup) view.findViewById(R.id.map_rg);
        mRb_01 = (RadioButton) view.findViewById(R.id.rb_01);
        mRb_02 = (RadioButton) view.findViewById(R.id.rb_02);
        mIv_head = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.iv_head);
        mSlid_name = (TextView) view.findViewById(R.id.slid_name);
        mSlid_status = (TextView) view.findViewById(R.id.slid_status);
        mLayout_odd = (LinearLayout) view.findViewById(R.id.layout_odd);
        mSlide_linggong = (TextView) view.findViewById(R.id.slide_linggong);
        mSlide_money = (TextView) view.findViewById(R.id.slide_money);
        mSlide_setting = (TextView) view.findViewById(R.id.slide_setting);
        mCiv_drawlay_bottom = (CircleImageView) view.findViewById(R.id.civ_drawlay_bottom);
        mTv_drawlay_zhenwo = (TextView) view.findViewById(R.id.tv_drawlay_zhenwo);

        mMap_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(mActivity instanceof HomeActivity){
                    ((HomeActivity) mActivity).checkRole(mRb_01.isChecked());//将状态传递给activity
                }
            }
        });

        mIv_head.setOnClickListener(new View.OnClickListener() {//头像
            @Override
            public void onClick(View v) {

            }
        });
        mLayout_odd.setOnClickListener(new View.OnClickListener() {//零工
            @Override
            public void onClick(View v) {

            }
        });
        mSlide_money.setOnClickListener(new View.OnClickListener() {//钱包
            @Override
            public void onClick(View v) {

            }
        });
        mSlide_setting.setOnClickListener(new View.OnClickListener() {//设置
            @Override
            public void onClick(View v) {

            }
        });
        mTv_drawlay_zhenwo.setOnClickListener(new View.OnClickListener() {//真我
            @Override
            public void onClick(View v) {

            }
        });
    }
}
