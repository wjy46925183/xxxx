package gongren.com.dlg.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import gongren.com.dlg.R;


/**
 * 点击末尾删除EditText中的内容.
 */
public class VisibleEditText extends EditText {
    private Paint mPaint;

    /**
     * @param context
     * @param attrs
     */
    public VisibleEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mPaint = new Paint();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//      画底线
        canvas.drawLine(0, this.getHeight() - 1, this.getWidth() - 1, this.getHeight() - 1, mPaint);
    }


    /**
     * 左右两侧图片资源
     */
    private Drawable left, top, right, bottom;

    /**
     * 手指抬起时的X坐标
     */
    private int xUp = 0;

    public VisibleEditText(Context context) {
        this(context, null);
    }


    public VisibleEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWedgits();
    }

    /**
     * 初始化各组件
     *
     * @param
     */
    private void initWedgits() {
        try {
            // 获取drawableLeft图片，如果在布局文件中没有定义drawableLeft属性，则此值为空
            left = getCompoundDrawables()[0];
            top = getCompoundDrawables()[1];
            bottom = getCompoundDrawables()[3];

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
//                    // 获取点击时手指抬起的X坐标
//                    xUp = (int) event.getX();
//                    // 当点击的坐标到当前输入框右侧的距离小于等于getCompoundPaddingRight()的距离时，则认为是点击了显示/隐藏密码图标
//                    // getCompoundPaddingRight()的说明：Returns the right padding of the view, plus space for the right Drawable if any.
//                    if ((getWidth() - xUp) <= getCompoundPaddingRight() && !TextUtils.isEmpty(getText().toString())) {
//                        //如果密码处于显示状态
//                        if (getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
//                            //隐藏密码
//                            setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
//                            //设置隐藏密码时的图标
//                            right = getResources().getDrawable(R.mipmap.ic_launcher);
//                        } else {
//                            //显示密码
//                            setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//                            //设置显示密码时的图标
//                            right = getResources().getDrawable(R.mipmap.ic_launcher);
//                        }
//                        setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
//                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onTouchEvent(event);
    }

}
