package gongren.com.dlg.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.utils.DateUtils;
import com.common.utils.SharedPreferencesUtils;
import com.common.utils.StringUtils;
import com.umeng.socialize.ShareAction;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import gongren.com.dlg.R;
import gongren.com.dlg.activity.CancleOrderActivity;
import gongren.com.dlg.activity.EmployeeDetailsActivity;
import gongren.com.dlg.activity.LoginDialogActivity;
import gongren.com.dlg.activity.MainActivity;
import gongren.com.dlg.activity.PingFenActivity;
import gongren.com.dlg.activity.RealNameAuthenticationActivity;
import gongren.com.dlg.activity.WebUtilsActivity;
import gongren.com.dlg.javabean.DoingTaskOrderDetailModel;
import gongren.com.dlg.javabean.TaskStartWorkTimeModel;
import gongren.com.dlg.javabean.base.AssistButtonListBean;
import gongren.com.dlg.javabean.base.ButtonListBean;
import gongren.com.dlg.javabean.worker.WorkerStatusBean;
import gongren.com.dlg.utils.DialogUtils;
import gongren.com.dlg.utils.DlgImageLoader;
import gongren.com.dlg.utils.TimeUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.utils.WorkbenchManager;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.RequestCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

import static gongren.com.dlg.R.id.workerorder_iv_head;
import static java.lang.Integer.parseInt;

/**
 * Created by Administrator on 2017/4/13 0013.
 */
public class DoingTaskDetailCardView extends LinearLayout {


    @Bind(R.id.baoxian)
    TextView baoxian;
    private boolean isInRange = false;
    public TaskStartWorkTimeModel mTaskStartWorkTimeModel;
    private final String HELP = "5";
    private final String HELP_TITAL = "帮助";

    @Bind(workerorder_iv_head)
    CircleImageView workerorderIvHead;
    @Bind(R.id.workerorder_tv_name)
    TextView workerorderTvName;
    @Bind(R.id.workerorder_tv_chengdu)
    TextView workerorderTvChengdu;
    @Bind(R.id.workerorder_tv_time)
    TextView workerorderTvTime;
    @Bind(R.id.workerorder_tv_address)
    TextView workerorderTvAddress;
    @Bind(R.id.workerorder_tv_position)
    TextView workerorderTvPosition;
    @Bind(R.id.workerorder_layout_item)
    View layoutItem;
    @Bind(R.id.workerorder_layout_detail)
    View layoutDetail;
    @Bind(R.id.workerorder_tvdetail)
    TextView tvDetail;

    //--------报酬------------//
    @Bind(R.id.rela_reward)
    RelativeLayout rela_reward;
    @Bind(R.id.tv_reward)
    TextView tv_reward;                   //支付金额
    @Bind(R.id.tv_payStatus)
    TextView tv_payStatus;               //支付状态
    @Bind(R.id.workerorder_iv_creditcount)
    TextView creditCount;
    @Bind(R.id.workerorder_refuseInvite)    //拒绝
            View workerorder_refuseInvite;
    @Bind(R.id.workerorder_evaluation)    //评价
            View workerorder_evaluation;
    @Bind(R.id.workerorder_layout_cancle)    //取消
            View tvCancle;
    @Bind(R.id.workerorder_layout_share)    //分享
            View tvShare;
    @Bind(R.id.workerorder_layout_help)     //帮助
            View tvHelp;
    @Bind(R.id.workerorder_layout_active)
    View lineartvActive;                     //配置按钮
    @Bind(R.id.workerorder_share)
    LinearLayout workerorderShare;  //扩散
    @Bind(R.id.tv_active)
    TextView tvActive;
    @Bind(R.id.tv_countDown)
    TextView tvCountDown;
    @Bind(R.id.tv_refuse)
    TextView tv_refuse;
    //这个view比较特殊，需要外部出进来
    TextView workerorder_title;
    private WorkerStatusBean.DataBean dataBean;
    private DoingTaskOrderDetailModel doingTaskOrderDetailModel;
    private boolean isWithIn;//是否在工作范围内
    private CountDownTimer countDownTimer;

    public DoingTaskDetailCardView(Context context) {
        super(context);
    }

    public DoingTaskDetailCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public DoingTaskDetailCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void init() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.doing_task_detail_card_layout, null);
        addView(contentView);
        ButterKnife.bind(this, contentView);
        initLister();
        goneAllButton();
    }

    public void setWorkerorder_title(TextView workerorder_title) {
        this.workerorder_title = workerorder_title;
    }

    private ShareAction mshareAction;


    public void initViewData(DoingTaskOrderDetailModel doingTaskOrderDetailModel) {
        this.doingTaskOrderDetailModel = doingTaskOrderDetailModel;
        tvCountDown.setVisibility(View.INVISIBLE);
        if (null != countDownTimer) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        bindViewData();
        //请求按钮数据
//        getTaskButtons();
    }

    private void bindViewData() {
        int isFarmersInsurance = doingTaskOrderDetailModel.isFarmersInsurance;
        if (isFarmersInsurance == 1) {
            baoxian.setVisibility(VISIBLE);
        } else {
            baoxian.setVisibility(INVISIBLE);
        }
        workerorderTvName.setText(doingTaskOrderDetailModel.postName);//任务名
        if ("志愿义工".equals(doingTaskOrderDetailModel.postTypeName)) {
            workerorderTvPosition.setText(doingTaskOrderDetailModel.postTypeName);
        } else
            workerorderTvPosition.setText(doingTaskOrderDetailModel.price + "元/" + doingTaskOrderDetailModel.meterUnitName);  //任务价格描述

        creditCount.setText(doingTaskOrderDetailModel.creditCount == null ? "36.5" : doingTaskOrderDetailModel.creditCount);
        String startDate = doingTaskOrderDetailModel.startDate;
        String endDate = doingTaskOrderDetailModel.endDate;

        int startYear = DateUtils.getDate_yyyy(startDate);
        int endYear = DateUtils.getDate_yyyy(endDate);
        int startMonth = DateUtils.getDate_MM(startDate);
        int endMonth = DateUtils.getDate_MM(endDate);
        int startDay = DateUtils.getDate_dd(startDate);
        int endDay = DateUtils.getDate_dd(endDate);
        int startHour = DateUtils.getDate_HH(startDate);
        int endHour = DateUtils.getDate_HH(endDate);
        int startMinute = DateUtils.getDate_mm(startDate);
        int endMinute = DateUtils.getDate_mm(endDate);


        String time = DateUtils.getTimeShow(doingTaskOrderDetailModel.demandType, startYear, startMonth,
                startDay, startHour, startMinute, endYear, endMonth, endDay, endHour, endMinute);
        workerorderTvTime.setText(time);


        //发任务人的诚信分数
        workerorderTvAddress.setText(doingTaskOrderDetailModel.workAddress == null ? "" :
                doingTaskOrderDetailModel.provinceName + doingTaskOrderDetailModel.areaName +
                        doingTaskOrderDetailModel.workAddress);       //任务的地址
        DlgImageLoader.loadImage(getContext(), doingTaskOrderDetailModel.logo, workerorderIvHead);                                 //发任务人的头像
        String type = null;
        switch (doingTaskOrderDetailModel.demandType) {
            case 1:
                type = "工作日";
                break;
            case 2:
                type = "双休日";
                break;
            case 3:
                type = "计件";
                break;
        }
        if (type != null)
            workerorderTvChengdu.setText(type);
        if (doingTaskOrderDetailModel.status.equals("30")) {
            rela_reward.setVisibility(View.VISIBLE);
            float totalPrice = TextUtils.isEmpty(doingTaskOrderDetailModel.totalPrice) ? 0 : parseInt(doingTaskOrderDetailModel.totalPrice);
            float tipAmount = TextUtils.isEmpty(doingTaskOrderDetailModel.tipAmount) ? 0 : parseInt(doingTaskOrderDetailModel.tipAmount);
            float price = totalPrice + tipAmount;
            if (tipAmount > 0) {
                tv_reward.setText("报酬：" + price + "元 (含小费" + tipAmount + "元)");
            } else {
                tv_reward.setText("报酬：" + doingTaskOrderDetailModel.totalPrice + "元");
            }
            tv_payStatus.setText("待雇主支付");
        } else if (doingTaskOrderDetailModel.status.equals("40")) {
            rela_reward.setVisibility(View.VISIBLE);
            float totalPrice = TextUtils.isEmpty(doingTaskOrderDetailModel.totalPrice) ? 0 : Float.parseFloat(doingTaskOrderDetailModel.totalPrice);
            float tipAmount = TextUtils.isEmpty(doingTaskOrderDetailModel.tipAmount) ? 0 : Float.parseFloat(doingTaskOrderDetailModel.tipAmount);
            float price = totalPrice + tipAmount;
            if (tipAmount > 0) {
                tv_reward.setText("报酬：" + price + "元 (含小费" + tipAmount + "元)");
            } else {
                tv_reward.setText("报酬：" + doingTaskOrderDetailModel.totalPrice + "元");
            }
            tv_reward.setText(tv_reward.getText());
            tv_payStatus.setText("雇主已支付");
        } else {
            rela_reward.setVisibility(View.GONE);
        }
    }

    private void goneAllButton() {
        workerorder_refuseInvite.setVisibility(View.GONE);
        workerorder_evaluation.setVisibility(View.GONE);
        tvCancle.setVisibility(View.GONE);
        tvShare.setVisibility(View.GONE);
        tvHelp.setVisibility(View.GONE);
        workerorderShare.setVisibility(View.GONE);
        tvCountDown.setVisibility(View.INVISIBLE);
        if (null != countDownTimer) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    /**
     * 初始化底部操作按钮 倒计时
     *
     * @param workerStatusBean
     */
    public void initButton(WorkerStatusBean workerStatusBean) {
        goneAllButton();
        if (workerStatusBean != null && workerStatusBean.data.size() > 0) {
            dataBean = workerStatusBean.data.get(0);
        } else {
            return;
        }
        if (getContext() instanceof MainActivity)
            workerorder_title.setText(dataBean.statusText);//WorkFragment标题
        if (dataBean.buttonList.size() == 1) {
            ButtonListBean buttonListBean = dataBean.buttonList.get(0);
            tvActive.setText(buttonListBean.operateStatusText);//操作按钮

            List<AssistButtonListBean> assistButtonList = dataBean.assistButtonList;
            if (null != assistButtonList && assistButtonList.size() > 0) {
                for (int i = 0; i < assistButtonList.size(); i++) {
                    AssistButtonListBean assistButtonListBean = assistButtonList.get(i);
                    switch (assistButtonListBean.buttonStatus) {
                        case "101"://取消码
                            tvCancle.setVisibility(VISIBLE);
                            break;
                        case "102"://分享码
                            tvShare.setVisibility(VISIBLE);
                            break;
                        case "103"://帮助码
                            tvHelp.setVisibility(VISIBLE);
                            break;
                        case "105":
                            workerorder_evaluation.setVisibility(VISIBLE);
                            break;
                        case "104":
                            workerorderShare.setVisibility(VISIBLE);
                            break;
                        default:
                            tvShare.setVisibility(VISIBLE);
                            break;
                    }
                }
            }
        } else if (dataBean.buttonList.size() >= 2) {
            ButtonListBean buttonListBean = dataBean.buttonList.get(0);//拒绝邀请
            ButtonListBean buttonListBean1 = dataBean.buttonList.get(1);//同意邀请
            tvActive.setText(buttonListBean1.operateStatusText);
            workerorder_refuseInvite.setVisibility(VISIBLE);
            tv_refuse.setText(buttonListBean.operateStatusText);
            tvShare.setVisibility(GONE);
            tvCancle.setVisibility(GONE);
            tvHelp.setVisibility(GONE);
        }


        if (operateRequestCallBack != null)
            operateRequestCallBack.OnStateUpdateCallBack(0, dataBean);
        updateActivityButton(!dataBean.buttonList.get(0).isGray);

        if (isWithIn && dataBean.countdownVo != null && dataBean.countdownVo.remainingTime != 0
                && !TextUtils.isEmpty(dataBean.countdownVo.mapTipsText)) {
            tvCountDown.setVisibility(VISIBLE);
            tvCountDown.setText(TimeUtils.getRemainTimeByWM((int)dataBean.countdownVo.remainingTime)
                    + dataBean.countdownVo.mapTipsText);
            countDownTimer = new CountDownTimer(dataBean.countdownVo.remainingTime, 1000) {//倒计时
                @Override
                public void onTick(long millisUntilFinished) {
                    if (null != tvCountDown && null != dataBean && null != dataBean.countdownVo) {
                        tvCountDown.setText(TimeUtils.getRemainTimeByWM(millisUntilFinished)
                                + dataBean.countdownVo.mapTipsText);
                    }
                }

                @Override
                public void onFinish() {
                    cancel();//倒计时结束 取消计时
                    if (null != tvCountDown) {
                        tvCountDown.setVisibility(INVISIBLE);//隐藏倒计时控件
                    }
                    if (operateRequestCallBack != null) {
                        operateRequestCallBack.OnRefresh(null);//刷新地图页面数据
                    }
                    updateActivityButton(true);
                }
            }.start();
        }


    }

    /**
     * 初始化监听事件
     */
    private void initLister() {
        workerorderShare.setOnClickListener(onClickListener);
        tvCancle.setOnClickListener(onClickListener);
        tvShare.setOnClickListener(onClickListener);
        tvHelp.setOnClickListener(onClickListener);
        workerorder_evaluation.setOnClickListener(onClickListener);
        workerorder_refuseInvite.setOnClickListener(onClickListener);
        workerorderIvHead.setOnClickListener(onClickListener);
    }

    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.workerorder_iv_head:                                //头像
                    Intent employeeDetailsActivityIntent = new Intent(getContext(), EmployeeDetailsActivity.class);
                    String userRole = SharedPreferencesUtils.getString(getContext(), SharedPreferencesUtils.UserRole);//进入主页，验证登录身份
                    if ("1".equals(userRole) || TextUtils.isEmpty(userRole))//是雇员身份或则游客身份
                    {
                        employeeDetailsActivityIntent.putExtra("userId", doingTaskOrderDetailModel.employerId + "");

                    } else {
                        employeeDetailsActivityIntent.putExtra("userId", doingTaskOrderDetailModel.employeeId + "");
                    }
                    getContext().startActivity(employeeDetailsActivityIntent);
                    break;
                case R.id.workerorder_share:                                //扩散
                case R.id.workerorder_layout_share:                       //分享
                    //启动分享
                    if (mshareAction != null)
                        mshareAction.open();
                    break;
                case R.id.workerorder_layout_cancle:                      //取消
                    if (StringUtils.isLogin(getContext())) {
                        cancleOrder();
                    } else {
                        getContext().startActivity(new Intent(getContext(), LoginDialogActivity.class));
                    }
                    break;

                case R.id.workerorder_layout_help:                        //帮助
                    Intent webIntent = new Intent(getContext(), WebUtilsActivity.class);
                    webIntent.putExtra("type", HELP);
                    webIntent.putExtra("contentUrl", GetDataConfing.BASEURL_H5);
                    webIntent.putExtra("functionTitle", HELP_TITAL);
                    getContext().startActivity(webIntent);

                    break;
                case R.id.workerorder_layout_active:                      //订单操作按钮
                    if (StringUtils.isLogin(getContext())) {
                        if (SharedPreferencesUtils.getString(getContext(), SharedPreferencesUtils.RENZHENG_STATUS).equals("2")) {
                            int operateStatusCode = -1;
                            if (null != dataBean && null != dataBean.buttonList) {
                                operateStatusCode = dataBean.buttonList.get(0).operateStatusCode;
                            }
                            if (operateStatusCode == 0 ||
                                    "知道了".equals(tvActive.getText().toString())
                                    || "再来一单".equals(tvActive.getText().toString())) {
                                Activity activity = (Activity) getContext();
                                if (activity instanceof MainActivity) {
                                    operateRequestCallBack.OnRefresh(dataBean.buttonList.get(0));
//                                EventBus.getDefault().post(new MainToWorkerFragmentEvent("", 13));
                                } else {
                                    Intent intent = new Intent(activity, MainActivity.class);
                                    intent.putExtra("againD", "againD");
                                    intent.putExtra("tag", 0);
                                    activity.startActivity(intent);
                                    activity.finish();
                                }
                            } else {//
                                if (null != dataBean.buttonList && dataBean.buttonList.size() > 1) {
                                    postBtnEvent(dataBean.businessNumber, dataBean.buttonList.get(1));
                                } else {
                                    postBtnEvent(dataBean.businessNumber, dataBean.buttonList.get(0));
                                }

                            }
                        } else {
                            DialogUtils.showSimpleDialog(getContext(), "提示", "未认证身份证信息,去认证", new DialogUtils.ConfirmCallback() {
                                @Override
                                public void confirm(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    getContext().startActivity(new Intent(getContext(), RealNameAuthenticationActivity.class));
                                }
                            });
                        }
                    } else {
                        getContext().startActivity(new Intent(getContext(), LoginDialogActivity.class));
                    }

                    break;
                case R.id.workerorder_evaluation:                         //评价
                    Intent intent = new Intent(getContext(), PingFenActivity.class);
                    Bundle bundle = new Bundle();
                    //评价需要的参数
                    bundle.putSerializable("orderInfo_card", doingTaskOrderDetailModel);
                    //判断是雇员的评价，还是雇主的评价
                    bundle.putBoolean("isGuYuan", true);
                    intent.putExtras(bundle);
                    getContext().startActivity(intent);
                    break;
                case R.id.workerorder_refuseInvite:                       //拒绝
                    postBtnEvent(dataBean.businessNumber, dataBean.buttonList.get(0));
                    break;
            }
        }
    };

    //取消订单
    private void cancleOrder() {
        Intent intent = new Intent(getContext(), CancleOrderActivity.class);
        intent.putExtra("compensatoryTrusty", 1);//诚信值
        if (dataBean.statusCode == 21) {
            intent.putExtra("isfrom", 2);//未开工前取消 扣除信用
            intent.putExtra("compensatoryTrusty", "1");//诚信值
        } else {
            intent.putExtra("isfrom", 1);//未开工前取消 扣除信用
        }
        intent.putExtra("businessNumber", dataBean.businessNumber);//订单编号
        intent.putExtra("isGuYuan", true);
        getContext().startActivity(intent);
    }


    public void setRefreshRequest(OperateRequestCallBack operateRequestCallBack) {
        this.operateRequestCallBack = operateRequestCallBack;
    }

    OperateRequestCallBack operateRequestCallBack;

    public interface OperateRequestCallBack {
        //刷新整个页面
        void OnRefresh(ButtonListBean dataBean);

        //根据状态做操作回调   state :
        void OnStateUpdateCallBack(int state, WorkerStatusBean.DataBean dataBean);
    }

    //----------------------------------------------------------------------------------刷新View代码块----------------------------------------------------------------------------------//


    /**
     * 操作按钮 是否可点击
     */
    private void updateActivityButton(boolean enable) {
        if (doingTaskOrderDetailModel.demandType == 3) {
            isWithIn = true;
        }
        if (enable && isWithIn || "知道了".equals(tvActive.getText().toString())
                || "再来一单".equals(tvActive.getText().toString())) {//后台返回的是不置灰，我们只需要判断是否在工作范围内即可 在工作范围内的时候 置为蓝色
            lineartvActive.setBackgroundColor(getResources().getColor(R.color.bottom_btn_bg));
        } else {
            lineartvActive.setBackgroundColor(getResources().getColor(R.color.bg_gray));
        }
        lineartvActive.setOnClickListener(onClickListener);
        lineartvActive.setClickable(enable);
        int operateStatusCode = -1;
        if (null != dataBean && null != dataBean.buttonList && dataBean.buttonList.size() > 0) {
            operateStatusCode = dataBean.buttonList.get(0).operateStatusCode;
        }
        if (!isWithIn) {
//            if (enable) {
//                lineartvActive.setBackgroundColor(getResources().getColor(R.color.bg_gray));
//                lineartvActive.setClickable(false);
//                if(null != dataBean.countdownVo){
//                    tvCountDown.setText(dataBean.countdownVo.otherMapTipsText);
//                    tvCountDown.setVisibility(VISIBLE);
//                }
//            } else {
            if (operateStatusCode == 21 || operateStatusCode == 22 || operateStatusCode == 23) {
                lineartvActive.setBackgroundColor(getResources().getColor(R.color.bg_gray));
                lineartvActive.setClickable(false);
                if (null != dataBean && null != dataBean.countdownVo) {
                    tvCountDown.setText(dataBean.countdownVo.otherMapTipsText);
                } else {
                    tvCountDown.setText("请到地图上蓝色范围内进行打卡");
                }
                tvCountDown.setVisibility(VISIBLE);
            }
//            }
        }
//        if (doingTaskOrderDetailModel.demandType == 3 && operateStatusCode == 21) { //如果是计价单开工打开的范围限制去掉
//            isWithIn = true;
//            lineartvActive.setClickable(isWithIn);
//            tvCountDown.setVisibility(INVISIBLE);
//            lineartvActive.setBackgroundColor(getResources().getColor(R.color.bottom_btn_bg));
//        }

    }


    //---------------------------------------------------------任务的三个接口请求(按钮，倒计时信息，按钮提交发送)-------------------------------------------------------------//

    /**
     * 请求任务的操作按钮数据
     */
    public void getTaskButtons() {
        WorkbenchManager.getTaskButtons(doingTaskOrderDetailModel.businessNumber, new RequestCallback<WorkerStatusBean>() {
            @Override
            public void onSuccess(WorkerStatusBean workerStatusBean) {
                initButton(workerStatusBean);
            }

            @Override
            public void onError(String mag) {
                ShowGetDataError.showNetError(getContext());
            }
        });
    }

    /**
     * 发送操作按钮点击请求
     */
    private void postBtnEvent(String businessNumber, final ButtonListBean dataBean) {
        final Dialog loadingDialog = DialogUtils.loadingProgressDialog(getContext());
        WorkbenchManager.postBtnEvent(businessNumber, dataBean, new RequestCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                loadingDialog.dismiss();
                if (aBoolean && operateRequestCallBack != null) {
                    postEventSuccessCallBack(dataBean);
                    ToastUtils.showToastShort(getContext(), "操作成功!");
                }
            }


            @Override
            public void onError(String mag) {
                loadingDialog.dismiss();
                ToastUtils.showToastShort(getContext(), mag);
            }
        });
    }

    /**
     * 操作按钮点击后
     *
     * @param dataBean
     */
    private void postEventSuccessCallBack(ButtonListBean dataBean) {
        operateRequestCallBack.OnRefresh(dataBean);
    }

    /**
     * 是否在工作范围内
     *
     * @param isWithIn
     */
    public void setIsWithIn(boolean isWithIn) {
        this.isWithIn = isWithIn;
    }

    public void setShareAction(ShareAction action) {
        this.mshareAction = action;
    }
}
