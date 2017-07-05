package gongren.com.dlg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.common.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import gongren.com.dlg.R;
import gongren.com.dlg.javabean.master.masterlist.DataBean;
import gongren.com.dlg.javabean.master.masterlist.ListBean;

/**
 * 雇主列表页adapter
 */
public final class FindListAdapter extends BaseAdapter {

    private LayoutInflater inflate;
    private List<DataBean> mBossBean = new ArrayList<>();
    private Context mContext;


    public FindListAdapter(Context context) {
        inflate = LayoutInflater.from(context);
        this.mContext = context;
    }

    public void setDataBean(List<DataBean> bossBean,int pageIndex) {
        if(pageIndex == 0){
            this.mBossBean = bossBean;
        }else {
            if(null !=  this.mBossBean){
                this.mBossBean.addAll(bossBean);
            }
        }

        notifyDataSetChanged();
    }

    public List<DataBean> getBossBean() {
        return mBossBean;
    }

    @Override
    public int getCount() {
        if (mBossBean == null || mBossBean == null) {
            return 0;
        }
        return mBossBean.size();
    }

    @Override
    public DataBean getItem(int position) {
        return mBossBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (mBossBean == null && mBossBean.size() == 0
                || mBossBean.get(position).orderStatusList == null ||
                mBossBean.get(position).orderStatusList.size() == 0
                )
            return 0;//可以侧拉删除
        else
            return 1;//不可以侧拉删除
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflate.inflate(R.layout.item_find_lv, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.finishRelative.setVisibility(View.GONE);
        viewHolder.cancelledRelative.setVisibility(View.GONE);
        viewHolder.doingRelative.setVisibility(View.GONE);
        viewHolder.invitationRelative.setVisibility(View.GONE);
        viewHolder.preRelative.setVisibility(View.GONE);
        viewHolder.payRelative.setVisibility(View.GONE);

        if (mBossBean.get(position).isFarmersInsurance==1){
            viewHolder.baoxian.setVisibility(View.VISIBLE);
        }else {
            viewHolder.baoxian.setVisibility(View.INVISIBLE);
        }

        DataBean item = getItem(position);
        if (item != null) {
            //需求类型(1.工作日,2.双休日,3.计件)
            switch (item.demandType) {
                case 1:
                    viewHolder.typeText.setText("工作日");
                    break;
                case 2:
                    viewHolder.typeText.setText("双休日");
                    break;
                case 3:
                    viewHolder.typeText.setText("计件");
                    break;
            }
            initTime(item, viewHolder);//初始化时间格式
            if (!"志愿义工".equals(item.postTypeName)) {
                viewHolder.incomeText.setText(item.price + "元/" + item.jobMeterUnitName);
            } else {
                viewHolder.incomeText.setText(item.postTypeName);
            }
            viewHolder.ordernameText.setText(item.postName);
            viewHolder.ll_head_doing.removeAllViews();
            viewHolder.ll_head_jiedan.removeAllViews();
            viewHolder.ll_head_finish.removeAllViews();
            viewHolder.ll_head_pay.removeAllViews();
            viewHolder.llHeadCancelled.removeAllViews();
            viewHolder.llHeadInvitation.removeAllViews();
            initUI(viewHolder);
            int status01 = 0;
            int status02 = 0;
            int status03 = 0;
            int status04 = 0;
            int status05 = 0;
            int status06 = 0;
            if (null != mBossBean && mBossBean.get(0).orderStatusList != null && mBossBean
                    .get(position).orderStatusList.size() > 0) {
                for (int m = 0; m < mBossBean.get(position).orderStatusList.size(); m++) {
                    List<ListBean> data = mBossBean.get(position).orderStatusList.get(m).list;
                    for (int i = 0; i < data.size(); i++) {
                        ListBean listBean = data.get(i);


                        switch (data.get(i).status) {
                            case 6://
                            case 7://
                            case 10://等待同意
                                if(data.get(i).status == 7){
                                    viewHolder.jiedanHint.setText("雇员已拒绝");
                                }
                                if(data.get(i).status == 6){
                                    viewHolder.jiedanHint.setText("等待雇员同意");
                                }
                                if(data.get(i).status == 10){
                                    viewHolder.jiedanHint.setText("有人接单");
                                }
                                viewHolder.line2.setVisibility(View.VISIBLE);
                                viewHolder.preRelative.setVisibility(View.VISIBLE);//有人接单
                                viewHolder.preRelative.setTag(data.get(i));
                                status01++;
                                if (status01 >= 4)
                                    viewHolder.jiedanNumText.setText("等" + status01 + "人");
                                if (status01 < 4) {
                                    View inflate = LayoutInflater.from(mContext).inflate(R.layout.head_img, null);
                                    ImageView iv_head = (ImageView) inflate.findViewById(R.id.head_iv);
                                    Glide.with(mContext).load(listBean.logo).fitCenter()
                                            .override(150, 150)
                                            .error(R.mipmap.morentouxiang).into(iv_head);
                                    viewHolder.ll_head_jiedan.addView(inflate);
                                }
                                break;
                            case 20:
                            case 22:
                            case 21://正在干活中
                                status02++;
                                viewHolder.line3.setVisibility(View.VISIBLE);
                                viewHolder.doingRelative.setVisibility(View.VISIBLE);//正在干活中
                                viewHolder.doingRelative.setTag(data.get(i));
                                if (status02 < 4) {
                                    View inflate = LayoutInflater.from(mContext).inflate(R.layout.head_img, null);
                                    ImageView iv_head = (ImageView) inflate.findViewById(R.id.head_iv);
                                    Glide.with(mContext).load(listBean.logo).fitCenter()
                                            .override(150, 150)
                                            .error(R.mipmap.morentouxiang).into(iv_head);
                                    viewHolder.ll_head_doing.addView(inflate);
                                }
                                if (i >= 4) {
                                    viewHolder.doingNumText.setText("等" + status02 + "人");
                                }
                                break;
                            case 30://代付款
                                status03++;
                                viewHolder.line1.setVisibility(View.VISIBLE);
                                viewHolder.payRelative.setVisibility(View.VISIBLE);//代付款
                                viewHolder.payRelative.setTag(data.get(i));
                                if (status03 >= 4)
                                    viewHolder.payNumText.setText("等" + status03 + "人");
                                if (i < 4) {
                                    View inflate = LayoutInflater.from(mContext).inflate(R.layout.head_img, null);
                                    ImageView iv_head = (ImageView) inflate.findViewById(R.id.head_iv);
                                    Glide.with(mContext).load(listBean.logo).fitCenter()
                                            .override(150, 150)
                                            .error(R.mipmap.morentouxiang).into(iv_head);
                                    viewHolder.ll_head_pay.addView(inflate);
                                }
                                break;
                            case 40://已完成
                                status04++;
                                viewHolder.line4.setVisibility(View.VISIBLE);
                                viewHolder.finishRelative.setVisibility(View.VISIBLE);//已完工
                                viewHolder.finishRelative.setTag(data.get(i));
                                if (status04 >= 4)
                                    viewHolder.finishNumText.setText("等" + status04+ "人");
                                if (status04 < 4) {
                                    View inflate = LayoutInflater.from(mContext).inflate(R.layout.head_img, null);
                                    ImageView iv_head = (ImageView) inflate.findViewById(R.id.head_iv);
                                    Glide.with(mContext).load(listBean.logo).fitCenter()
                                            .override(150, 150)
                                            .error(R.mipmap.morentouxiang).into(iv_head);
                                    viewHolder.ll_head_finish.addView(inflate);
                                }
                                break;
                            case 50://已完成
                                status05++;
                                viewHolder.line5.setVisibility(View.VISIBLE);
                                viewHolder.cancelledRelative.setVisibility(View.VISIBLE);//已完工
                                viewHolder.cancelledRelative.setTag(data.get(i));
                                if (status05 >= 4)
                                    viewHolder.cancelledNumText.setText("等" + status05 + "人");
                                if (status05 < 4) {
                                    View inflate = LayoutInflater.from(mContext).inflate(R.layout.head_img, null);
                                    ImageView iv_head = (ImageView) inflate.findViewById(R.id.head_iv);
                                    Glide.with(mContext).load(listBean.logo).fitCenter()
                                            .override(150, 150)
                                            .error(R.mipmap.ic_launcher).into(iv_head);
                                    viewHolder.llHeadCancelled.addView(inflate);
                                }
                                break;

                            case 8://等待雇员同意
                                status06++;
                                viewHolder.line6.setVisibility(View.VISIBLE);
                                viewHolder.invitationRelative.setVisibility(View.VISIBLE);//已完工
                                viewHolder.invitationRelative.setTag(data.get(i));
                                if (status06 >= 4)
                                    viewHolder.invitationNumText.setText("等" + status06 + "人");
                                if (status06 < 4) {
                                    View inflate = LayoutInflater.from(mContext).inflate(R.layout.head_img, null);
                                    ImageView iv_head = (ImageView) inflate.findViewById(R.id.head_iv);
                                    Glide.with(mContext).load(listBean.logo).fitCenter()
                                            .override(150, 150)
                                            .error(R.mipmap.ic_launcher).into(iv_head);
                                    viewHolder.llHeadInvitation.addView(inflate);
                                }
                                break;
                        }
                    }
                }

            }
        }
        return convertView;
    }

    /**
     * 初始化时间
     */
    private void initTime(DataBean item, ViewHolder viewHolder) {
        String time = DateUtils.getTimeShowV(item.demandType, item.startYear, item.startMonth, item.startDay, item.startHour, item.startMinute, item.endYear
                , item.endMonth, item.endDay, item.endHour, item.endMinute);
        viewHolder.timeText.setText(time);
    }

    private void hintUi(RelativeLayout layout, View View) {
        layout.setVisibility(View.GONE);
        View.setVisibility(View.GONE);
    }

    /**
     * ui初始化  由于listview特殊性 每次重用的时候 需要初始化ui以免 重复利用之前的ui导致逻辑错误
     */
    private void initUI(ViewHolder viewHolder) {
        hintUi(viewHolder.preRelative, viewHolder.line2);
        hintUi(viewHolder.doingRelative, viewHolder.line3);
        hintUi(viewHolder.payRelative, viewHolder.line1);
        hintUi(viewHolder.finishRelative, viewHolder.line4);
    }

    static class ViewHolder {
        @Bind(R.id.ordername_text)
        TextView ordernameText;
        @Bind(R.id.type_text)
        TextView typeText;
        @Bind(R.id.time_text)
        TextView timeText;
        @Bind(R.id.income_text)
        TextView incomeText;
        @Bind(R.id.jiedan_hint)
        TextView jiedanHint;
        @Bind(R.id.line1)
        View line1;
        @Bind(R.id.pay_linear)
        LinearLayout payLinear;
        @Bind(R.id.pay_num_text)
        TextView payNumText;
        @Bind(R.id.pay_relative)
        RelativeLayout payRelative;
        @Bind(R.id.line2)
        View line2;
        @Bind(R.id.jiedan_linear)
        LinearLayout jiedanLinear;
        @Bind(R.id.jiedan_num_text)
        TextView jiedanNumText;
        @Bind(R.id.pre_relative)
        RelativeLayout preRelative;
        @Bind(R.id.line3)
        View line3;
        @Bind(R.id.doing_linear)
        LinearLayout doingLinear;
        @Bind(R.id.doing_num_text)
        TextView doingNumText;
        @Bind(R.id.doing_relative)
        RelativeLayout doingRelative;
        @Bind(R.id.line4)
        View line4;
        @Bind(R.id.finish_linear)
        LinearLayout finishLinear;
        @Bind(R.id.finish_num_text)
        TextView finishNumText;
        @Bind(R.id.finish_relative)
        RelativeLayout finishRelative;
        @Bind(R.id.ll_head_jiedan)
        LinearLayout ll_head_jiedan;
        @Bind(R.id.ll_head_doing)
        LinearLayout ll_head_doing;
        @Bind(R.id.ll_head_pay)
        LinearLayout ll_head_pay;
        @Bind(R.id.ll_head_finish)
        LinearLayout ll_head_finish;

        @Bind(R.id.cancelled_num_text)
        TextView cancelledNumText;
        @Bind(R.id.line5)
        View line5;
        @Bind(R.id.cancelled_relative)
        RelativeLayout cancelledRelative;
        @Bind(R.id.fcancelled_linear)
        LinearLayout fcancelledLinear;
        @Bind(R.id.ll_head_cancelled)
        LinearLayout llHeadCancelled;

        @Bind(R.id.invitation_num_text)
        TextView invitationNumText;
        @Bind(R.id.line6)
        View line6;
        @Bind(R.id.invitation_relative)
        RelativeLayout invitationRelative;
        @Bind(R.id.ll_head_invitation)
        LinearLayout llHeadInvitation;
        @Bind(R.id.baoxian)
        TextView baoxian;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
