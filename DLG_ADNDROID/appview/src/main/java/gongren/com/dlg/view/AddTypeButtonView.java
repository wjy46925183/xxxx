package gongren.com.dlg.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import gongren.com.dlg.R;
import gongren.com.dlg.javabean.base.ButtonListBean;

/**
 * 作者：wangdakuan
 * 主要功能：动态添加按钮控件
 * 创建时间：2017/5/25 12:45
 */
public class AddTypeButtonView extends LinearLayout{
    protected LayoutInflater inflater;
    protected Context mContext;
    private OnClickButListener clickButListener;


    public AddTypeButtonView(Context context) {
        super(context);
        mContext = context;
        inflater = LayoutInflater.from(context);
        setVisibility(View.VISIBLE);
        setOrientation(HORIZONTAL);
    }

    public AddTypeButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        inflater = LayoutInflater.from(context);
        setVisibility(View.VISIBLE);
        setOrientation(HORIZONTAL);
    }

    public AddTypeButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        inflater = LayoutInflater.from(context);
        setVisibility(View.VISIBLE);
        setOrientation(HORIZONTAL);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AddTypeButtonView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        inflater = LayoutInflater.from(context);
        setVisibility(View.VISIBLE);
        setOrientation(HORIZONTAL);

    }

    /**
     * 添加按钮
     * @param buttonData
     */
    public void setButtonData(List<ButtonListBean> buttonData){
        if(null != buttonData && buttonData.size()>0){
            removeAllViews();
            setVisibility(View.VISIBLE);
            for (int i = 0; i < buttonData.size(); i++) {
                final ButtonListBean bean = buttonData.get(i);
                View view = inflater.inflate(R.layout.item_button_type,this,false);
                TextView textView = (TextView) view.findViewById(R.id.tv_name);
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(null != clickButListener){
                            clickButListener.OnClickBut(bean);
                        }
                    }
                });
                textView.setText(bean.operateStatusText);
                switch (bean.operateStatusCode) {
                    case 3:  //取消
                    case 11:  //拒绝
                    case 1:  //知道了
                    case 26:  //验收不通过
                        textView.setTextColor(mContext.getResources().getColor(R.color.gray_text));
                        textView.setBackgroundResource(R.drawable.pressed_layout);
                        break;
                    case 8:  //发出邀请
                    case 20:  //同意
                    case 30:  //确认完成
                    case 25:  //验收通过
                    case 40:  //确认支付
                        textView.setTextColor(mContext.getResources().getColor(R.color.white));
                        textView.setBackgroundColor(mContext.getResources().getColor(R.color.yellow_btncolor));
                        break;

                }
                addView(view);
            }
        }
    }

    public interface OnClickButListener{
        void OnClickBut(ButtonListBean bean);
    }

    public OnClickButListener getClickButListener() {
        return clickButListener;
    }

    public void setClickButListener(OnClickButListener clickButListener) {
        this.clickButListener = clickButListener;
    }
}
