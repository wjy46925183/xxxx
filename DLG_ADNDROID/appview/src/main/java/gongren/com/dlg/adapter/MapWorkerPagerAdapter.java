package gongren.com.dlg.adapter;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import gongren.com.dlg.fragment.MapWorkerFragment;
import gongren.com.dlg.javabean.master.BossMapMsgBean;

/**
 * Created by liukui .
 * on 2017/4/12
 * 文件描述：雇员选项卡适配器
 */

public class MapWorkerPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private BossMapMsgBean mBossMapMsgBean;

    public MapWorkerPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    public MapWorkerPagerAdapter(FragmentManager fm, BossMapMsgBean bossMapMsgBean, Handler handler) {
        this(fm);
        this.mBossMapMsgBean = bossMapMsgBean;
        if (mBossMapMsgBean == null)
        {
            return;
        }
        for (int i = 0; i < mBossMapMsgBean.data.size(); i++) {

            MapWorkerFragment mapWorkerFragment = new MapWorkerFragment();
            mapWorkerFragment.setBossId(mBossMapMsgBean.data.get(i).userId);
            mapWorkerFragment.setDataBean(mBossMapMsgBean.data.get(i));
            mFragments.add(mapWorkerFragment);
        }
    }

    @Override
    public int getCount()
    {
        return mBossMapMsgBean.data.size();
    }
}
