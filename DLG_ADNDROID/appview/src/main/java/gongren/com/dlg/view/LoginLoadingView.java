package gongren.com.dlg.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import gongren.com.dlg.R;

/**
 * Created by Wangjinya on 2017/6/7.
 * 自定义加载控件
 */

public class LoginLoadingView extends View {
    private Paint mPaint;
    private int width;
    private int height;
    private final int DEFAULT_SIZE = 450;
    private final int maxRadius = 14;
    private final int minRadius = 10;
    private int one = 20, two = 10, three = 10;
    private int whichBig = 1;//默认第一个在变大
    private Handler mHandler;


    public LoginLoadingView(Context context) {
        super(context);
        init();
    }

    public LoginLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoginLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHandler = new Handler();
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#fec106"));
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setStyle(Paint.Style.FILL);
        setBackgroundResource(R.color.bg_gray);
        start();
    }

    //开始翻滚
    private void start() {
        mHandler.post(mRunnable);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(width / 2 - 70, height / 2, one, mPaint);
        canvas.drawCircle(width / 2, height / 2, two, mPaint);
        canvas.drawCircle(width / 2 + 70, height / 2, three, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = getMySize(widthMeasureSpec);
        height = getMySize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    /**
     * 获取测量大小
     */
    private int getMySize(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;//确切大小,所以将得到的尺寸给view
        } else if (specMode == MeasureSpec.AT_MOST) {
            //默认值为450px,此处要结合父控件给子控件的最多大小(要不然会填充父控件),所以采用最小值
            result = Math.min(DEFAULT_SIZE, specSize);
        } else {
            result = DEFAULT_SIZE;
        }
        return result;
    }

    /**
     * 三个圆半径变化图
     */
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            switch (whichBig) {
                case 1:
                    three = minRadius;
                    one = maxRadius;
                    if (one == maxRadius) {
                        whichBig = 2;
                    }
                    break;
                case 2:
                    one = minRadius;
                    two = maxRadius;
                    if (two == maxRadius) {
                        whichBig = 3;
                    }
                    break;
                case 3:
                    two = minRadius;
                    three = maxRadius;
                    if (three == maxRadius) {
                        whichBig = 1;
                    }
                    break;
            }
            invalidate();
            mHandler.postDelayed(mRunnable, 400);
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacks(mRunnable);
    }
}
