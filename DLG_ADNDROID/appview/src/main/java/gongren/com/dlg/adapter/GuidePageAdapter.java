package gongren.com.dlg.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * 启动页viewpager的Adapter
 */
public class GuidePageAdapter extends PagerAdapter {

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }

    @Override
    public int getItemPosition(Object object) {

        return super.getItemPosition(object);
    }

    // 获取当前窗体界面数
    @Override
    public int getCount() {
        return views.size();
    }

    // 判断是否由对象生成界面
    @Override
    public boolean isViewFromObject(View v, Object arg1) {
        return v == arg1;
    }

    private ArrayList<ImageView> views;

    public GuidePageAdapter(ArrayList<ImageView> views) {
        this.views = views;
    }

}
