package gongren.com.dlg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import gongren.com.dlg.R;
import gongren.com.dlg.javabean.worker.MySericeBean;

/**
 * 作者：王进亚
 * 主要功能：
 * 创建时间：2017/6/23 14:47
 */

public class ServiceListAdapter extends BaseAdapter {
    private Context mContext;
    private List<MySericeBean.DataBean> mStringList;
    public ServiceListAdapter(Context context,List<MySericeBean.DataBean> list){
        this.mContext = context;
        this.mStringList = list;
    }
    @Override
    public int getCount() {
        return mStringList == null?0:mStringList.size();
    }

    @Override
    public MySericeBean.DataBean getItem(int position) {
        return mStringList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_servicelist,null);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_des);
        int serviceMeterUnit = getItem(position).getServiceMeterUnit();
        String unitName = null;
        if (serviceMeterUnit == 1) {
            unitName = "/天";
        } else if (serviceMeterUnit == 2) {
            unitName = "/时";
        } else if (serviceMeterUnit == 3) {
            unitName = "/单";
        }
        textView.setText(getItem(position).getServiceName()+" "+getItem(position).getPrice()+"元"+unitName);
        return convertView;
    }
}
