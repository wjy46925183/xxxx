package gongren.com.dlg.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.utils.DateUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import gongren.com.dlg.R;
import gongren.com.dlg.javabean.GuYuanOrderItem;
import gongren.com.dlg.javabean.base.CountdownVoBean;
import gongren.com.dlg.javabean.worker.WorkerStatusBean;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.TimeUtils;

import static java.lang.Integer.parseInt;

/**
 * 雇员列表页adapter
 */
public class GuYuanOrderListAdapter extends SimpleBaseAdapter<GuYuanOrderItem> {
    private String status;
    private boolean mIsShowDeleteCheckBox;
    private View.OnClickListener mListener;

    public GuYuanOrderListAdapter(Context context, List<GuYuanOrderItem> list,
                                  String status, View.OnClickListener mListener) {
        super(context, list);
        this.status = status;
        this.mListener = mListener;
    }

    public void showDeleteCheckBox() {
        mIsShowDeleteCheckBox = true;
        notifyDataSetChanged();
    }

    public void hideDeleteCheckBox() {
        mIsShowDeleteCheckBox = false;
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        // menu type count
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // current menu type
        GuYuanOrderItem data = list.get(position);
        int isCancel = TextUtils.isEmpty(data.getIsCancel()) ? 1 : parseInt(data.getIsCancel());
        if (isCancel == 1) {//订单已取消，可以侧滑删除
            return 0;
        } else {//订单没有取消，不可以侧滑删除
            return 1;
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.guyuan_order_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GuYuanOrderItem itemData = list.get(position);
        if (itemData != null) {
            //根据不同的状态，显示不同的布局。
            setPerStatusView(itemData, holder);
            holder.pingfenBtn.setTag(position);


            //设置订单的信息
            //订单名称
            holder.ordernameText.setText(itemData.getPostName());
            //需求类型(1.工作日,2.双休日,3.计件)
            int typeNum = TextUtils.isEmpty(itemData.getDemandType()) ? -1 : parseInt(itemData.getDemandType());
            switch (typeNum) {
                case 1:
                    holder.typeText.setText("工作日");
                    break;
                case 2:
                    holder.typeText.setText("双休日");
                    break;
                case 3:
                    holder.typeText.setText("计件");
                    break;
            }
            if ("志愿义工".equals(itemData.getPostTypeName())) {
                //单价
                holder.incomeText.setText(itemData.getPostTypeName());
            } else {
                //单价
                holder.incomeText.setText(itemData.getPrice() + "元/" + itemData.getMeterUnitName());
            }

            //日期时间
            GuYuanOrderItem.OrderTimeResultRpcVoBean dateData = itemData.getOrderTimeResultRpcVo();
            if (dateData != null) {
                //设置订单时间
                setOrderDate(dateData, TextUtils.isEmpty(itemData.getDemandType()) ? 0 : parseInt(itemData.getDemandType()), holder);
            }

            //判断订单是否已取消。
            String isCancle = itemData.getIsCancel();
            if (!DataUtils.isNullStr(isCancle) && isCancle.equals("1")) {//已取消
                String modifyid = itemData.getModifyUserId();
                String employeeId = itemData.getEmployeeId();
                if (!DataUtils.isNullStr(modifyid) && modifyid.equals(employeeId)) {//雇员取消的
                    holder.leftText.setText("已取消对方");
                } else {//雇主取消的。
                    holder.leftText.setText("对方已取消");
                }
                holder.rightText.setVisibility(View.VISIBLE);
                holder.rightText.setText(itemData.getCancelCause());
            }
            List<WorkerStatusBean.DataBean> buttonData = itemData.getButtonData();
            if (null != buttonData && buttonData.size() > 0) {
                CountdownVoBean bean = buttonData.get(0).countdownVo;
                if (null != bean && bean.remainingTime != 0) {
                    countTime(bean.remainingTime + "", bean.mapTipsText, holder);
                }
            }
        }
        return convertView;
    }

    public void setOrderDate(GuYuanOrderItem.OrderTimeResultRpcVoBean dateData, int demadType, ViewHolder holder) {
        int startYear = TextUtils.isEmpty(dateData.getStartYear()) ? 0 : parseInt(dateData.getStartYear());
        int startMonth = TextUtils.isEmpty(dateData.getStartMonth()) ? 0 : parseInt(dateData.getStartMonth());
        int startDay = TextUtils.isEmpty(dateData.getStartDay()) ? 0 : parseInt(dateData.getStartDay());
        int startHour = TextUtils.isEmpty(dateData.getStartHour()) ? 0 : parseInt(dateData.getStartHour());
        int startMinute = TextUtils.isEmpty(dateData.getStartMinute()) ? 0 : parseInt(dateData.getStartMinute());
        int startSecond = TextUtils.isEmpty(dateData.getStartSecond()) ? 0 : parseInt(dateData.getStartSecond());

        int endYear = TextUtils.isEmpty(dateData.getEndYear()) ? 0 : parseInt(dateData.getEndYear());
        int endMonth = TextUtils.isEmpty(dateData.getEndMonth()) ? 0 : parseInt(dateData.getEndMonth());
        int endDay = TextUtils.isEmpty(dateData.getEndDay()) ? 0 : parseInt(dateData.getEndDay());
        int endHour = TextUtils.isEmpty(dateData.getEndHour()) ? 0 : parseInt(dateData.getEndHour());
        int endMinute = TextUtils.isEmpty(dateData.getEndMinute()) ? 0 : parseInt(dateData.getEndMinute());
        int endSecond = TextUtils.isEmpty(dateData.getEndSecond()) ? 0 : parseInt(dateData.getEndSecond());

        String dateStr = DateUtils.getTimeShow(demadType, startYear, startMonth, startDay, startHour
                , startMinute, endYear, endMonth, endDay, endHour, endMinute);

        holder.timeText.setText(dateStr.toString());
        holder.timeText.invalidate();
    }


    /**
     * 根据不同的状态，显示不同的布局。
     */
    public void setPerStatusView(GuYuanOrderItem orderItem,
                                 ViewHolder holder) {
        int status = TextUtils.isEmpty(orderItem.getStatus()) ? 0 : parseInt(orderItem.getStatus());
        switch (status) {
            case 0://知道了
                holder.pingjiaRe.setVisibility(View.GONE);
                holder.rightText.setVisibility(View.VISIBLE);
                break;
            case 6:
                holder.line2.setVisibility(View.GONE);
                holder.pingjiaRe.setVisibility(View.GONE);//评分行
                holder.rightText.setVisibility(View.GONE);//右边的字
                holder.leftText.setText("有人雇佣");
                break;
            case 7:
                holder.line2.setVisibility(View.GONE);
                holder.pingjiaRe.setVisibility(View.GONE);//评分行
                holder.rightText.setVisibility(View.GONE);//右边的字
                holder.leftText.setText("拒绝雇佣");
                break;
            case 8://等待邀请
                holder.line2.setVisibility(View.GONE);
                holder.pingjiaRe.setVisibility(View.GONE);//评分行
                holder.rightText.setVisibility(View.VISIBLE);//右边的字
                holder.leftText.setText("等待雇员同意");
                break;
            case 9://拒绝邀请
                holder.line2.setVisibility(View.GONE);
                holder.pingjiaRe.setVisibility(View.GONE);//评分行
                holder.rightText.setVisibility(View.GONE);//右边的字

                holder.leftText.setText("已拒绝");
                break;
            case 10://等待雇主同意
                holder.line2.setVisibility(View.GONE);
                holder.pingjiaRe.setVisibility(View.GONE);//评分行
                holder.rightText.setVisibility(View.GONE);//右边的字
                holder.leftText.setText("等待雇主同意");
                break;
            case 11://我拒绝接单
                holder.line2.setVisibility(View.GONE);
                holder.pingjiaRe.setVisibility(View.GONE);//评分行
                holder.rightText.setVisibility(View.GONE);//右边的字

                holder.leftText.setText("拒绝接单");
                break;
            case 20://等待开工，离开工时间还有超过半个小时
                holder.line2.setVisibility(View.GONE);
                holder.pingjiaRe.setVisibility(View.GONE);//评分行
                holder.rightText.setVisibility(View.VISIBLE);//右边的字
                holder.leftText.setText("等待开工");
                break;
            case 21://正在干活中
                holder.line2.setVisibility(View.GONE);
                holder.pingjiaRe.setVisibility(View.GONE);//评分行
                holder.rightText.setVisibility(View.GONE);//右边的字

                holder.leftText.setText("正在干活中");
                break;
            case 22://等待验收
                holder.line2.setVisibility(View.GONE);
                holder.pingjiaRe.setVisibility(View.GONE);//评分行
                holder.rightText.setVisibility(View.GONE);//右边的字
                holder.leftText.setText("等待验收");

                break;
            case 30://待收款
                holder.line2.setVisibility(View.GONE);
                holder.pingjiaRe.setVisibility(View.GONE);//评分行
                holder.rightText.setVisibility(View.GONE);//右边的字
                holder.leftText.setText("待收款");
                break;
            case 40://已完成，未评价
                holder.line2.setVisibility(View.VISIBLE);
                holder.pingjiaRe.setVisibility(View.VISIBLE);//评分行
                holder.pingfenBtn.setVisibility(View.VISIBLE);//评分按钮
                holder.starbar.setVisibility(View.GONE);//评分五星
                holder.rightText.setVisibility(View.GONE);//右边的字

                holder.leftText.setText("已完成");

                //判断订单是否已评价
                String isPingJia = orderItem.getIsEvaluate();
                if (!DataUtils.isNullStr(isPingJia) && isPingJia.equals("1")) {//已评价
                    holder.pingjiaTab.setText("已评价");
                    holder.pingfenBtn.setVisibility(View.GONE);//评分五星
                    holder.starbar.setVisibility(View.VISIBLE);//评分按钮
                    //已评价的，要显示，评价的分数
                    int evaluateLevel = TextUtils.isEmpty(orderItem.getEvaluateLevel()) ? 5 : parseInt(orderItem.getEvaluateLevel());
                    holder.starbar.setRating(evaluateLevel);
                    holder.starbar.setEnabled(false);
                } else {//未评价
                    holder.pingjiaTab.setText("未评价");
                    holder.starbar.setVisibility(View.GONE);//评分五星
                    holder.pingfenBtn.setVisibility(View.VISIBLE);//评分按钮
                }

                break;
            case 50://取消
                holder.pingjiaRe.setVisibility(View.GONE);
                holder.rightText.setVisibility(View.VISIBLE);
                break;
        }
    }

    class ViewHolder {
        @Bind(R.id.ordername_text)
        TextView ordernameText;
        @Bind(R.id.type_text)
        TextView typeText;
        @Bind(R.id.time_text)
        TextView timeText;
        @Bind(R.id.income_text)
        TextView incomeText;
        @Bind(R.id.line1)
        View line1;
        @Bind(R.id.left_text)
        TextView leftText;
        @Bind(R.id.right_text)
        TextView rightText;
        @Bind(R.id.line2)
        View line2;
        @Bind(R.id.pingjia_tab)
        TextView pingjiaTab;
        @Bind(R.id.pingfen_btn)
        TextView pingfenBtn;
        @Bind(R.id.starbar)
        RatingBar starbar;
        @Bind(R.id.pingjia_re)
        RelativeLayout pingjiaRe;

        CountDownTimer countDownTimer;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);

        }
    }

    private void countTime(String remainingTime, final String statusText, final ViewHolder viewHolder) {
        if (null == remainingTime) return;
        long time = Long.parseLong(remainingTime);
        if (null != viewHolder.countDownTimer) {
            viewHolder.countDownTimer.cancel();
            viewHolder.countDownTimer = null;
        }
        CountDownTimer countDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (null != viewHolder && null != viewHolder.rightText) {
                    viewHolder.rightText.setText("");
                    viewHolder.rightText.setText(
                            TimeUtils.getRemainTimeByWM((int) millisUntilFinished) + statusText);
                }
            }

            @Override
            public void onFinish() {
                cancel();
                if (null != viewHolder && null != viewHolder.rightText) {
                    viewHolder.rightText.setText("");
                }
            }
        }.start();
        viewHolder.countDownTimer = countDownTimer;
    }
}
