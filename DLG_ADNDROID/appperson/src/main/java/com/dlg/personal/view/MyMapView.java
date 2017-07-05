package com.dlg.personal.view;

import android.content.Context;
import android.util.AttributeSet;

import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;

/**
 * 作者：wangdakuan
 * 主要功能：公共地图
 * 创建时间：2017/6/27 09:05
 */
public class MyMapView extends MapView{

    public MyMapView(Context context) {
        super(context);
    }

    public MyMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MyMapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public MyMapView(Context context, AMapOptions aMapOptions) {
        super(context, aMapOptions);
    }

}
