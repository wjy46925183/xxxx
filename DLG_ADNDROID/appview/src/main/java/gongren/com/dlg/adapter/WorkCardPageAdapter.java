package gongren.com.dlg.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.route.RouteSearch;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import gongren.com.dlg.fragment.WorkerCardFragment;
import gongren.com.dlg.javabean.DoingTaskOrderDetailModel;
import gongren.com.dlg.javabean.MainToWorkerFragmentEvent;
import gongren.com.dlg.utils.WorkMapManager;

/**
 * 作者：wangdakuan
 * 主要功能：
 * 创建时间：2017/6/12 09:39
 */
public class WorkCardPageAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private List<DoingTaskOrderDetailModel> detailModels;

    public WorkCardPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    public WorkCardPageAdapter(FragmentManager fm, List<DoingTaskOrderDetailModel> doingTaskOrderDetailModel,
                               LatLng myLatlng, TextView titleView, WorkMapManager workMapManager,
                               AMap map, RouteSearch mRouteSearch) {
        this(fm);
        this.detailModels = doingTaskOrderDetailModel;
        if (detailModels == null) {
            return;
        }
        mFragments.clear();
        for (int i = 0; i < detailModels.size(); i++) {

            WorkerCardFragment cardFragment = new WorkerCardFragment() {
                @Override
                public void deleteItem(int position) {
                    if (detailModels.size() == 1) {
                        MainToWorkerFragmentEvent event = new MainToWorkerFragmentEvent("", 13);
                        EventBus.getDefault().post(event);
                    } else {
                        detailModels.remove(position);
                        mFragments.remove(position);
                        notifyDataSetChanged();
                    }
                }
            };
            Bundle bundle = new Bundle();
            cardFragment.setOrderDetailModel(detailModels.get(i));
            cardFragment.setPosition(i);
//            bundle.putSerializable("orderDetailModel", detailModels.get(i));
//            bundle.putInt("position", i);
            cardFragment.setArguments(bundle);
            cardFragment.setLatlng(myLatlng, titleView);
            cardFragment.setWorkMapManager(workMapManager, map, mRouteSearch);
//            mapWorkerFragment.setBossId(mBossMapMsgBean.data.get(i).userId);
//            mapWorkerFragment.setDataBean(mBossMapMsgBean.data.get(i));
            mFragments.add(cardFragment);
        }
    }

    @Override
    public int getCount() {
        return detailModels.size();
    }
}

