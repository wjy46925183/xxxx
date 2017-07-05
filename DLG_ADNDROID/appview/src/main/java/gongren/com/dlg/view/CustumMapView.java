package gongren.com.dlg.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.TextureMapView;

/**
 * Created by lenovo on 2017/5/27.
 */

public class CustumMapView extends TextureMapView {
    public CustumMapView(Context context) {
        super(context);
    }

    public CustumMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CustumMapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public CustumMapView(Context context, AMapOptions aMapOptions) {
        super(context, aMapOptions);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
