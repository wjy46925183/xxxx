/**
 *
 */
package gongren.com.dlg.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.TypedValue;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.services.core.LatLonPoint;

public class AMapUtil {
    public static boolean IsEmptyOrNullString(String s) {
        return (s == null) || (s.trim().length() == 0);
    }

    /**
     * 把LatLonPoint对象转化为LatLon对象
     */
    public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    public static boolean isPkgInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void zoom(AMap map,Context context, LatLng southwest, LatLng northeast) {
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(southwest).include(northeast).build();
        map.moveCamera(CameraUpdateFactory.newLatLngBoundsRect(latLngBounds,dp2px(context,50),dp2px(context,50),dp2px(context,50),dp2px(context,200)));
    }
    /**
     * dp转px
     *
     * @param context
     * @param
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}
