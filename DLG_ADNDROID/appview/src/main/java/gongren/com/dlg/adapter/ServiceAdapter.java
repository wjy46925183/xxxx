package gongren.com.dlg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gongren.com.dlg.R;
import gongren.com.dlg.javabean.worker.MySericeBean;

/**
 * 作者：王进亚
 * 主要功能：
 * 创建时间：2017/6/22 14:51
 */

public class ServiceAdapter extends BaseAdapter {
    private Context mContext;
    private List<MySericeBean.DataBean> mDataBeen;
    private List<Boolean> bools = new ArrayList<>();
    private int selectPosition = -1;

    public ServiceAdapter(Context context) {
        this.mContext = context;

    }

    public void setDataBeen(List<MySericeBean.DataBean> dataBeen) {
        this.mDataBeen = dataBeen;
        if (mDataBeen == null) return;
        for (int i = 0; i < mDataBeen.size(); i++) {
            bools.add(false);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataBeen == null ? 0 : mDataBeen.size();
    }

    @Override
    public MySericeBean.DataBean getItem(int position) {
        return mDataBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_service, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MySericeBean.DataBean item = getItem(position);
        final int serviceMeterUnit = item.getServiceMeterUnit();
        String unitName = null;
        if (serviceMeterUnit == 1) {
            unitName = "/天";
        } else if (serviceMeterUnit == 2) {
            unitName = "/时";
        } else if (serviceMeterUnit == 3) {
            unitName = "/单";
        }
        viewHolder.mServicePrice.setText(item.getPrice() + "元" + unitName);
        viewHolder.mServiceTypeName.setText(item.getServiceTypeName());
        viewHolder.mServiceName.setText(item.getServiceName());
        viewHolder.mCheckService.setChecked(bools.get(position));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bools.clear();
                if (position == selectPosition) {
                    for (int i = 0; i < mDataBeen.size(); i++) {
                        bools.add(false);
                        selectPosition = -1;
                    }
                } else {
                    for (int i = 0; i < mDataBeen.size(); i++) {
                        if (i == position) {
                            bools.add(true);
                            selectPosition = position;
                        } else {
                            bools.add(false);
                        }
                    }
                }

                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    /**
     * 得到被选中的位置
     *
     * @return
     */
    public int getSelectPosition() {
        return selectPosition;
    }

    class ViewHolder {
        RadioButton mCheckService;
        TextView mServiceName;
        TextView mServiceTypeName;
        TextView mServicePrice;

        public ViewHolder(View view) {
            mCheckService = (RadioButton) view.findViewById(R.id.check_service);
            mServiceName = (TextView) view.findViewById(R.id.service_name);
            mServiceTypeName = (TextView) view.findViewById(R.id.service_type_name);
            mServicePrice = (TextView) view.findViewById(R.id.service_price);
        }
    }
}
