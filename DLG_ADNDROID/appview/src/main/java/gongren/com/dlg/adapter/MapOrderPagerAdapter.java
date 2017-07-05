package gongren.com.dlg.adapter;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import aym.util.json.JsonMap;
import gongren.com.dlg.fragment.MapOrderFragment;

/**
 * Created by liukui .
 * on 2017/4/12
 * 文件描述：任务选项卡适配器
 */

public class MapOrderPagerAdapter extends FragmentPagerAdapter {
    private List<JsonMap<String, Object>> mList;
    private Handler handler;
    private List<Fragment> mFragments;

    public MapOrderPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    public MapOrderPagerAdapter(FragmentManager fm, List<JsonMap<String, Object>> list, Handler handler) {
        this(fm);
        this.mList = list;
        this.handler = handler;
        mFragments = new ArrayList<>();
        if (mList != null) {
            for (int i = 0; i < mList.size(); i++) {
                MapOrderFragment mapOrderFragment = MapOrderFragment.newInstance(handler,mList.get(i).getStringNoNull("id"));
//                mapOrderFragment.setTaskId();
                mFragments.add(mapOrderFragment);
            }
        }
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }
}
