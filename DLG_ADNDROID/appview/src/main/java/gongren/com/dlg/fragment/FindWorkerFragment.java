package gongren.com.dlg.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import aym.util.json.JsonMap;
import aym.util.json.JsonParseHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import gongren.com.dlg.R;
import gongren.com.dlg.activity.EmployeeDetailsActivity;
import gongren.com.dlg.activity.LoginDialogActivity;
import gongren.com.dlg.activity.RealNameAuthenticationActivity;
import gongren.com.dlg.activity.WebUtilsActivity;
import gongren.com.dlg.javabean.DoingTaskOrderButtonModel;
import gongren.com.dlg.javabean.MainToWorkerFragmentEvent;
import gongren.com.dlg.javabean.ShareBean;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import com.common.utils.DateUtils;
import gongren.com.dlg.utils.DialogUtils;
import gongren.com.dlg.utils.IntegerUtils;
import gongren.com.dlg.utils.ShareUtils;
import com.common.utils.SharedPreferencesUtils;
import com.common.utils.StringUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.utils.WorkbenchManager;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.RequestCallback;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * Created by luoxiaohui . 零工详情
 * on 2017/4/12
 * 文件描述：
 */

public class FindWorkerFragment extends BaseFragment {
    private static final String HELP = "5";
    private static final String HELP_TITAL = "帮助";
    private static final String TAG = "FindWorkerFragment";

    @Bind(R.id.workerorder_iv_head)
    CircleImageView workerorderIvHead;

    @Bind(R.id.workerorder_tv_name)
    TextView workerorderTvName;

    @Bind(R.id.workerorder_tv_chengdu)
    TextView workerorderTvChengdu;

    @Bind(R.id.workerorder_tv_time)
    TextView workerorderTvTime;

    @Bind(R.id.workerorder_tv_date)
    TextView workerorderTvDate;


    @Bind(R.id.workerorder_tv_position)
    TextView workerorderTvPosition;

    @Bind(R.id.workerorder_layout_item)
    View layoutItem;

    @Bind(R.id.workerorder_layout_detail)
    View layoutDetail;

    @Bind(R.id.workerorder_tvdetail)
    TextView tvDetail;

    @Bind(R.id.workerorder_iv_creditcount)
    TextView creditCount;

    @Bind(R.id.workerorder_layout_cancle)
    View tvCancle;

    @Bind(R.id.workerorder_layout_share)
    View layoutshare;

    @Bind(R.id.tv_share)
    TextView tv_share;

    @Bind(R.id.workerorder_layout_help)
    View tvHelp;

    @Bind(R.id.workerorder_layout_active)
    LinearLayout ll_active;

    @Bind(R.id.workerorder_tv_num)
    TextView workerorder_tv_num;

    @Bind(R.id.workerorder_tv_type)
    TextView workerorder_tv_type;
    @Bind(R.id.baoxian)
    TextView baoxian;

    private String taskId;

    private JsonMap<String, Object> resultDatas;
    private static Handler mHandler;
    private DoingTaskOrderButtonModel doingTaskOrderButtonModel;
    private static final int TAG_REQUEST = 1;
    private ShareAction mShareAction;

    public static MapOrderFragment newInstance(Handler handler) {
        MapOrderFragment fragment = new MapOrderFragment();
        mHandler = handler;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_worker, container, false);
        ButterKnife.bind(this, view);
        initDatas();
        if (tag == 1)
            layoutDetail.setVisibility(View.VISIBLE);
        return view;
    }

    /*****
     * 初始化数据
     */
    private void initDatas() {
        tvCancle.setVisibility(View.GONE);
        tvHelp.setVisibility(View.GONE);
        tv_share.setText("扩散");
        ll_active.setBackgroundColor(Color.parseColor("#ffb552"));
        loadDatas();
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }


    /*****
     * 加载数据
     */
    private void loadDatas() {
        Map<String, Object> map = BaseMapUtils.getMap(activity);
        map.put("taskId", taskId);
        DataUtils.loadData(activity, GetDataConfing.WORK_MAP_DETAIL, map, IntegerUtils.API_WORK_MAP_DETAIL, responseDataCallback);
    }

    @OnClick({R.id.workerorder_iv_head, R.id.workerorder_layout_item, R.id.workerorder_layout_cancle, R.id.workerorder_layout_share, R.id.workerorder_layout_help, R.id.workerorder_layout_active})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.workerorder_iv_head:
                Intent employeeDetailsActivityIntent = new Intent(activity, EmployeeDetailsActivity.class);
                employeeDetailsActivityIntent.putExtra("userId", resultDatas.getStringNoNull("userId"));
                startActivity(employeeDetailsActivityIntent);
                break;
            case R.id.workerorder_layout_item:
                //零工详情展示
                layoutDetail.setVisibility(View.VISIBLE);
                if (null != mHandler) {
                    Message message = new Message();
                    message.what = IntegerUtils.WORKORDER_SHOW;
                    message.obj = layoutDetail;
                    mHandler.sendMessage(message);
                }
                break;

            case R.id.workerorder_layout_cancle:

                //取消
                break;

            case R.id.workerorder_layout_share:
                if (null != mShareAction) {
                    mShareAction.open();
                }
                //分享
                break;

            case R.id.workerorder_layout_help:
                Intent webIntent = new Intent(getContext(), WebUtilsActivity.class);
                webIntent.putExtra("type", HELP);
                webIntent.putExtra("contentUrl", GetDataConfing.BASEURL_H5);
                webIntent.putExtra("functionTitle", HELP_TITAL);
                getContext().startActivity(webIntent);
                //帮助
                break;

            case R.id.workerorder_layout_active:
                //验证用户是否登录
                if (StringUtils.isLogin(activity)) {//登录了 就去抢单
                    if ("2".equals(SharedPreferencesUtils.getString(activity, SharedPreferencesUtils.RENZHENG_STATUS))) {
                        //抢单
                        postActiveButtonEvent();
                    } else {
                        DialogUtils.showSimpleDialog(activity, "提示", "未认证身份证信息,去认证", new DialogUtils.ConfirmCallback() {
                            @Override
                            public void confirm(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                activity.startActivity(new Intent(activity, RealNameAuthenticationActivity.class));
                            }
                        });
                    }
                } else {
                    activity.startActivity(new Intent(activity, LoginDialogActivity.class));
                }
                break;

        }
    }


    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            if (dataRequest.isNetError()) {
                ShowGetDataError.showNetError(activity);
            } else {
                String json = dataRequest.getResponseData();
                if (!TextUtils.isEmpty(json)) {
                    switch (dataRequest.getWhat()) {
                        case IntegerUtils.API_WORK_MAP_DETAIL:
                            bindViewData(json);
                            //getButtonModel();
                            break;
                        case TAG_REQUEST:
                            break;
                    }
                }
            }
        }
    };
    private int tag = 0;

    public void setWorkerFindTag(int tag) {
        this.tag = tag;
    }

    private void postActiveButtonEvent() {
        WorkbenchManager.striveForOrder(taskId, new RequestCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                //成功发送消息，工作台重新加载页面
                if (aBoolean && tag == 0) {
                    MainToWorkerFragmentEvent event = new MainToWorkerFragmentEvent("", 3);
                    EventBus.getDefault().post(event);
                } else if (aBoolean && tag == 1) {
                    SharedPreferencesUtils.saveBoolean(activity, "isFindWorker", true);
                    activity.finish();
                }
            }

            @Override
            public void onError(String mag) {
                ToastUtils.showToastShort(getContext(), mag);
            }
        });
    }

    private void bindViewData(String json) {
        List<JsonMap<String, Object>> jsonList = JsonParseHelper.getJsonMap_List_JsonMap(json, "data");
        if (jsonList.isEmpty()) {
            return;
        }
        resultDatas = jsonList.get(0);
        //岗位名称
        workerorderTvName.setText(resultDatas.getStringNoNull("postName"));
        Log.e(TAG, resultDatas.getStringNoNull("postName"));

        workerorder_tv_type.setText(resultDatas.getStringNoNull("postTypeName"));
        if (!"志愿义工".equals(resultDatas.getStringNoNull("postTypeName"))) {
            //单价
            workerorderTvPosition.setText(resultDatas.getStringNoNull("price") + "元/" + resultDatas.getStringNoNull("jobMeterUnitName"));
            workerorder_tv_num.setText(resultDatas.getStringNoNull("recruitNumber"));
        } else {
            workerorderTvPosition.setText("");
        }
        int demandType = resultDatas.getInt("demandType");
        int startYear = resultDatas.getInt("startYear");
        int startMonth = resultDatas.getInt("startMonth");
        int startDay = resultDatas.getInt("startDay");
        int startHour = resultDatas.getInt("startHour");
        int startMinute = resultDatas.getInt("startMinute");
        int endYear = resultDatas.getInt("endYear");
        int endMonth = resultDatas.getInt("endMonth");
        int endDay = resultDatas.getInt("endDay");
        int endHour = resultDatas.getInt("endHour");
        int endMinute = resultDatas.getInt("endMinute");
        int isFarmersInsurance=resultDatas.getInt("isFarmersInsurance");
        if (isFarmersInsurance==1){
            baoxian.setVisibility(View.VISIBLE);
        }else {
            baoxian.setVisibility(View.INVISIBLE);
        }
        String date = DateUtils.getDateShow(demandType, startYear, startMonth, startDay,
                endYear, endMonth, endDay);
        String time = DateUtils.getTimeShow(demandType, startHour, startMinute,
                endHour, endMinute);
        workerorderTvDate.setText(date);
        workerorderTvTime.setText(time);
        workerorderTvChengdu.setText(demandType == 3 ? "计件" : demandType == 1 ? "工作日" : "双休日");

        /*workerorderTvAddress.setText("地址: "+resultDatas.getStringNoNull("provinceName") +
                resultDatas.getStringNoNull("cityName") + resultDatas.getStringNoNull("areaName")
                + resultDatas.getStringNoNull("villageName") + resultDatas.getStringNoNull("workAddress"));*/

        String taskDescription = resultDatas.getStringNoNull("taskDescription");
        tvDetail.setText(taskDescription);

        Glide.with(activity)
                .load(resultDatas.getStringNoNull("userLogo"))
                .error(R.mipmap.morentouxiang)
                .into(workerorderIvHead);

        creditCount.setText(resultDatas.getStringNoNull("userCreditCount"));
        tvDetail.setMovementMethod(new ScrollingMovementMethod());

        //获取分享的内容
        WorkbenchManager.getShareData(getContext(), GetDataConfing.SHARE_DATA,
                resultDatas.getStringNoNull("id"), new WorkbenchManager.StringCallBack() {
                    @Override
                    public void onFinish(String json) {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if ("0".equals(jsonObject.optString("code"))) {
                                ShareBean shareBean = new Gson().fromJson(json, ShareBean.class);
                                if (shareBean != null && shareBean.getData() != null && shareBean.getData().size() > 0) {
                                    ShareBean.DataBean dataBean = shareBean.getData().get(0);
                                    String tital = dataBean.getTaskTitle();
                                    String url = dataBean.getDetailsUrl();
                                    String taskDescription = dataBean.getTaskDescription();
                                    double xCoordinate = dataBean.getXCoordinate();
                                    double yCoordinate = dataBean.getYCoordinate();
                                    if (null != mLocationCallBack) {
                                        mLocationCallBack.location(yCoordinate, xCoordinate);
                                    }
                                    String userLogo = dataBean.getUserLogo();
                                    mShareAction = ShareUtils.setUMShareAction(activity,
                                            tital, taskDescription, url, userLogo);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private LocationCallBack mLocationCallBack;

    public void setLocationCallBack(LocationCallBack locationCallBack) {
        this.mLocationCallBack = locationCallBack;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public interface LocationCallBack {
        void location(double yCoordinate, double xCoordinate);
    }

}
