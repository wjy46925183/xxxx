package gongren.com.dlg.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import gongren.com.dlg.R;
import gongren.com.dlg.activity.CancleOrderActivity;
import gongren.com.dlg.javabean.master.masterlist.ListBean;
import gongren.com.dlg.javabean.master.masterlist.OrderStatusListBean;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.DialogUtils;
import com.common.utils.SharedPreferencesUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 我的需求，需求详情页 正在进行的adapter
 */
public class DoingOrderAdapter extends BaseAdapter {

    ViewHolder holder;
    private Context context;
    private OrderStatusListBean mStatusListBean;
    private View.OnClickListener listener;

    public DoingOrderAdapter(Context context, OrderStatusListBean bean, View.OnClickListener listener) {
        this.context = context;
        this.mStatusListBean = bean;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        if (mStatusListBean == null || mStatusListBean.list == null || mStatusListBean.list.size() < 0) {
            return 0;
        } else {
            return mStatusListBean.list.size();
        }
    }

    @Override
    public ListBean getItem(int position) {
        return mStatusListBean.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.doing_order_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ListBean item = getItem(position);
        holder.checkbox.setVisibility(View.GONE);
        holder.nameText.setText(TextUtils.isEmpty(item.name) ? item.phone : item.name);
        holder.fenText.setText(item.creditCount == null ? 36.5f + "" : Float.parseFloat(item.creditCount) + "");
        holder.starbar01.setRating(item.scoreCount == null ? 5.0f : Float.parseFloat(item.scoreCount));
        Glide.with(context).load(item.logo).into(holder.ivHead);

        holder.callBtn.setOnClickListener(listener);
        holder.cancleBtn.setOnClickListener(listener);
//        getItem(position).setStatus(20);//TODO  调试
        int status = getItem(position).status;
        Log.e("order", "statues=" + status);
        switch (status) {
            case 20://等待开工
                holder.cancleReseasonText.setVisibility(View.GONE);
                holder.line2.setVisibility(View.GONE);
                holder.peiRe.setVisibility(View.GONE);
                holder.cancleBtn.setVisibility(View.GONE);
                holder.agreeBtn.setTextColor(context.getResources().getColor(R.color.chenghong_textcolor));
                holder.agreeBtn.setText("取消");
                holder.agreeBtn.setTag(position);
                holder.agreeBtn.setTag(R.id.onclick_type, 1);
                holder.cancleReseasonTab.setText("等待开工");
                holder.cancleReseasonText.setVisibility(View.GONE);


                holder.agreeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, CancleOrderActivity.class);
                        intent.putExtra("isfrom", 1);//未开工前取消 扣除信用
                        intent.putExtra("businessNumber", item.businessNumber);//订单编号
                        intent.putExtra("compensatoryTrusty", "");//诚信值
                        intent.putExtra("isGuYuan", false);
                        context.startActivity(intent);
                    }
                });
                break;
            case 21://干活中
                holder.cancleReseasonText.setVisibility(View.GONE);
                holder.line2.setVisibility(View.GONE);
                holder.peiRe.setVisibility(View.GONE);
                holder.cancleBtn.setVisibility(View.VISIBLE);
                holder.agreeBtn.setTextColor(context.getResources().getColor(R.color.green_textcolor));
                holder.agreeBtn.setText("确认完成");
                holder.cancleBtn.setTag(position);
                holder.cancleBtn.setText("取消");
                holder.agreeBtn.setTag(position);
                holder.agreeBtn.setTag(R.id.onclick_type, 3);
                holder.agreeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//干活中 确认完成
                        httpSendStatus(30, 30 + "", item.businessNumber);
                    }
                });
                holder.cancleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, CancleOrderActivity.class);
                        intent.putExtra("isfrom", 2);//未开工前取消 扣除信用
                        intent.putExtra("businessNumber", item.businessNumber);//订单编号
                        intent.putExtra("isGuYuan", false);
                        context.startActivity(intent);
                    }
                });

                holder.cancleReseasonTab.setText("正在干活中");
                holder.cancleReseasonText.setVisibility(View.GONE);
                break;
            case 22://等待验收
                holder.cancleReseasonText.setVisibility(View.GONE);
                holder.line2.setVisibility(View.GONE);
                holder.peiRe.setVisibility(View.GONE);

                holder.agreeBtn.setTextColor(context.getResources().getColor(R.color.green_textcolor));
                holder.agreeBtn.setText("通过");
                holder.agreeBtn.setTag(position);
                holder.agreeBtn.setTag(R.id.onclick_type, 4);
                holder.agreeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showToastShort(context, "通过验收");
                        httpSendStatus(25, 30 + "", item.businessNumber);
                    }
                });

                holder.cancleBtn.setTag(position);
                holder.cancleBtn.setText("不通过");
                holder.cancleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showToastShort(context, "验收不通过");
                        httpSendStatus(26, 21 + "", item.businessNumber);
                    }
                });
                holder.cancleReseasonTab.setText("等待验收");
                holder.cancleReseasonText.setVisibility(View.GONE);
                break;
            case 5://对方取消
                holder.actionRe.setVisibility(View.GONE);
                holder.line2.setVisibility(View.GONE);
                holder.peiRe.setVisibility(View.GONE);
                holder.cancleReseasonText.setVisibility(View.VISIBLE);
                holder.cancleReseasonTab.setText("对方取消");
//                    holder.cancleReseasonText.setVisibility(View.GONE);
                convertView.setClickable(false);
                break;
            case 6://我方取消
                holder.actionRe.setVisibility(View.GONE);
                holder.cancleReseasonText.setVisibility(View.VISIBLE);
                holder.line2.setVisibility(View.VISIBLE);
                holder.peiRe.setVisibility(View.VISIBLE);
                holder.cancleReseasonTab.setText("我方取消");
                convertView.setClickable(false);
                break;
            case 8://等待同意
                holder.actionRe.setVisibility(View.GONE);
                holder.line2.setVisibility(View.VISIBLE);
                holder.peiRe1.setVisibility(View.GONE);
                holder.cancleReseasonText.setVisibility(View.GONE);
                holder.peiRe.setVisibility(View.GONE);
                holder.cancleReseasonTab.setText("等待雇员同意");
                convertView.setClickable(false);
                break;
        }

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.checkbox)
        CheckBox checkbox;
        @Bind(R.id.iv_head)
        CircleImageView ivHead;
        @Bind(R.id.name_text)
        TextView nameText;
        @Bind(R.id.fen_text)
        TextView fenText;
        @Bind(R.id.starbar_01)
        RatingBar starbar01;
        @Bind(R.id.call_btn)
        ImageView callBtn;
        @Bind(R.id.people_line)
        View peopleLine;
        @Bind(R.id.cancle_reseason_tab)
        TextView cancleReseasonTab;
        @Bind(R.id.cancle_reseason_text)
        TextView cancleReseasonText;
        @Bind(R.id.cancle_btn)
        Button cancleBtn;
        @Bind(R.id.agree_btn)
        Button agreeBtn;
        @Bind(R.id.action_re)
        RelativeLayout actionRe;
        @Bind(R.id.line2)
        View line2;
        @Bind(R.id.pei_tab)
        TextView peiTab;
        @Bind(R.id.pei_text)
        TextView peiText;
        @Bind(R.id.pei_re1)
        RelativeLayout peiRe1;
        @Bind(R.id.pei_re)
        RelativeLayout peiRe;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 订单操作按钮修改
     *
     * @param operaStatus
     * @param nextStatusCode
     */
    private void httpSendStatus(int operaStatus, String nextStatusCode, String businessNum) {
        final Dialog loadingDialog = DialogUtils.loadingProgressDialog((Activity) context);
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("businessNumber", businessNum);//TODO
        map.put("nextStatusCode", nextStatusCode);
        map.put("operaStatus", operaStatus + "");
        map.put("userid", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));
        DataUtils.loadData(context, GetDataConfing.post_task_button_event, map, 1, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                    if (dataRequest.isNetError()) {
                        ShowGetDataError.showNetError(context);
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject(dataRequest.getResponseData());
                            if ("0".equals(jsonObject.optString("code"))) {
                                Activity activity = (Activity) context;
                                activity.finish();
                            } else {
                                ToastUtils.showToastShort(context, jsonObject.optString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}
