package gongren.com.dlg.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import gongren.com.dlg.R;
import gongren.com.dlg.javabean.TaskOfOrderInfo;

/**
 * Created by Administrator on 2017/3/31.
 */
public class OrderPopListAdapterV extends SimpleBaseAdapter<TaskOfOrderInfo.DataBean> {
    PopupWindow OrderPop;
    String jobId;

    public OrderPopListAdapterV(Context context, List<TaskOfOrderInfo.DataBean> list, PopupWindow OrderPop, String jobId) {
        super(context, list);
        this.OrderPop = OrderPop;
        this.jobId = jobId;
    }

    public void setDatas(List<TaskOfOrderInfo.DataBean> list) {
        super.list = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_orderpoplist, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TaskOfOrderInfo.DataBean dataBean = list.get(position);
       /* if(jaonMap.getBoolean("flag")){
            holder.cb.setChecked(true);
        }else{
            holder.cb.setChecked(false);
            case 10://有人接单
                            cls = PreOrderActivity.class;
                            break;
                        case 21://进行中
                            cls = DoingOrderActivity.class;
                            break;
                        case 30://待付款
                            cls = PayOrderActivity.class;
                            break;
                        case 40://已完成
                            cls = FinishOrderActivity.class;
        }*/
        if (dataBean != null) {
            if (dataBean.getList() != null) {
                holder.cb.setChecked(true);
                if (position == 0) {
                    holder.cb.setChecked(true);
                } else {
                    holder.cb.setChecked(false);
                }
                if ("0".equals(dataBean.getPrice())) {
                    holder.unit.setText(dataBean.getPostName());
                } else {
                    holder.unit.setText(dataBean.getPostName() + " " + dataBean.getPrice() + "元/" + dataBean.getJobMeterUnitName());
                }
                Log.e("===", "jobId----" + jobId);
                if (!TextUtils.isEmpty(jobId)) {
                    if (jobId.equals(dataBean.getId())) {
                        holder.cb.setChecked(true);
                    } else {
                        holder.cb.setChecked(false);
                    }

                } else {
                    if (position == 0) {
                        holder.cb.setChecked(true);
                    } else {
                        holder.cb.setChecked(false);
                    }
                }
                holder.num.setText("(" + dataBean.getUsedNum() + ")");
//                if(dataBean.getStatus()>0){
//                    if(10==dataBean.getStatus()){
//                        holder.text.setText("有人接单");
//                    }else {
//                        holder.text.setText("等待验收");
//                    }
                switch (dataBean.getStatus()) {
                    case 10://有人接单
                        holder.text.setText("有人接单");
                        break;
                    case 21://进行中
                        holder.text.setText("进行中");
                        break;
                    case 30://待付款
                        holder.text.setText("待付款");
                        break;
                    case 40://已完成
                        holder.text.setText("已完成");
                        break;
                    default:
                        holder.text.setText("等待验收");
                        break;
                }
//                }

            }

        }
       /* holder.layoutClickCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cb.setChecked(true);
                if(OrderPop!=null){
                    OrderPop.dismiss();
                }

            }
        });*/

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.cb)
        CheckBox cb;
        @Bind(R.id.unit)
        TextView unit;
        @Bind(R.id.text)
        TextView text;
        @Bind(R.id.num)
        TextView num;
        @Bind(R.id.layout_click_cb)
        LinearLayout layoutClickCb;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
