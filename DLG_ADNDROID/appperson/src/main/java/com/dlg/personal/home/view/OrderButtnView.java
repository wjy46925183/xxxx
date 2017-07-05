package com.dlg.personal.home.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dlg.data.common.model.ActionButtonsBean;
import com.dlg.data.common.model.ButtonBean;
import com.dlg.personal.R;

import java.util.List;

/**
 * 作者：wangdakuan
 * 主要功能：订单的操作按钮控件
 * 创建时间：2017/7/3 18:03
 */
public class OrderButtnView extends LinearLayout {

    protected LayoutInflater inflater;
    protected Context mContext;

    public OrderButtnView(Context context) {
        super(context);
        mContext = context;
        inflater = LayoutInflater.from(context);
        setOrientation(HORIZONTAL);
    }

    public OrderButtnView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        inflater = LayoutInflater.from(context);
        setOrientation(HORIZONTAL);
    }

    public OrderButtnView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        inflater = LayoutInflater.from(context);
        setOrientation(HORIZONTAL);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OrderButtnView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        inflater = LayoutInflater.from(context);
        setOrientation(HORIZONTAL);
    }

    /**
     * 添加按钮
     * @param buttonData
     */
    public void setButtonData(ActionButtonsBean buttonData) {
        if (null != buttonData) {
            List<ButtonBean> buttonBeen = buttonData.getButtonList();
            if (null != buttonBeen && buttonBeen.size() > 0) {
                for (final ButtonBean buttonBean : buttonBeen) {
                    View view = inflater.inflate(R.layout.item_button_type, this, false);
                    TextView textView = (TextView) view.findViewById(R.id.tv_name);
                    textView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (null != buttonClickListener) {
                                buttonClickListener.buttonOnClick(buttonBean);
                            }
                        }
                    });
                    textView.setTextColor(mContext.getResources().getColor(R.color.gray_text));
                    textView.setBackgroundResource(R.drawable.pressed_layout);
                    textView.setText(buttonBean.getOperateStatusText());
                    addView(view);
                }
            }
        }
    }

    public interface onButtonClickListener {
        void buttonOnClick(ButtonBean buttonBean);
    }

    /**
     * 按钮事件添加
     */
    private onButtonClickListener buttonClickListener;

    public void setButtonClickListener(onButtonClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
    }
}
