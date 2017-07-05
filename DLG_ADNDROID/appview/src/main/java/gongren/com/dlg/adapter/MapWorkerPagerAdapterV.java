package gongren.com.dlg.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import gongren.com.dlg.activity.BaseActivity;
import gongren.com.dlg.fragment.MapBossOrderFragment;
import gongren.com.dlg.javabean.MainToBossFragmentEvent;
import gongren.com.dlg.javabean.OrderCreateInfo;
import gongren.com.dlg.javabean.TaskOfOrderInfo;
import gongren.com.dlg.utils.OrderToast;
import gongren.com.dlg.view.MyViewPager;

/**
 * Created by liukui .
 * on 2017/4/12
 * 文件描述：雇员选项卡适配器
 */

public class MapWorkerPagerAdapterV extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<Boolean> fragmentsUpdateFlag = new ArrayList<>();
    private List<OrderCreateInfo> datas = new ArrayList<>();
    MyViewPager viewPager;
    BaseActivity activity;
    FragmentManager fm;

    public MapWorkerPagerAdapterV(FragmentManager fm) {
        super(fm);
    }

    List<TaskOfOrderInfo.DataBean> mList = null;

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    public void setList(List<TaskOfOrderInfo.DataBean> list, MyViewPager viewPager) {
        this.mList = list;
        this.viewPager = viewPager;
        if (mList == null || mList.size() == 0) {
            return;
        }
        datas.clear();
        fragmentsUpdateFlag.clear();
        mFragments.clear();
        for (int i = 0; i < mList.size(); i++) {
            List<OrderCreateInfo> list1 = mList.get(i).getList();
            if (list1 != null && list1.size() > 0) {
                for (int j = 0; j < list1.size(); j++) {
                    if (list1 != null) {
                        datas.add(list1.get(j));
                    }
                }
            }
        }
        if (null != datas && datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                MapBossOrderFragment m = new MapBossOrderFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("orderCreateInfo", datas.get(i));
                bundle.putInt("index", i);
                m.setArguments(bundle);
                m.setBusinessNumber(datas.get(i).getBusinessNumber(), this);
                mFragments.add(m);
                fragmentsUpdateFlag.add(false);

            }
        }

    }

    public MapWorkerPagerAdapterV(FragmentManager fm, BaseActivity activity) {
        super(fm);
        this.fm = fm;
        this.activity = activity;
        notifyDataSetChanged();
    }

    public void removeData(int index) {
        if (datas.size() == 1) {
            datas.remove(index);
        }
        if (datas.size() > 0) {
            if (index == datas.size() - 1) {
                if (!datas.get(index).getJobId().equals(datas.get(0).getJobId())) {
                    OrderToast.OrderToastShow(activity, true);
                }
            } else {
                if (!datas.get(index).getJobId().equals(datas.get(index + 1).getJobId())) {
                    OrderToast.OrderToastShow(activity, true);
                }
            }
            datas.remove(index);
            mFragments.clear();
            for (int is = 0; is < datas.size(); is++) {
                MapBossOrderFragment m = new MapBossOrderFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("orderCreateInfo", datas.get(is));
                bundle.putInt("index", is);
                m.setArguments(bundle);
                m.setBusinessNumber(datas.get(is).getBusinessNumber(), this);
                mFragments.add(m);
                fragmentsUpdateFlag.add(false);

            }
            viewPager.setAdapter(this);
            if (index == datas.size()) {
                viewPager.setCurrentItem(0);
            } else {
                viewPager.setCurrentItem(index);
            }
            notifyDataSetChanged();
        } else {
            EventBus.getDefault().post(new MainToBossFragmentEvent("", 10));
        }

    }

    public int getDataSize() {
        return datas.size();
    }

    @Override
    public int getCount() {
        return datas.size();
    }


}
