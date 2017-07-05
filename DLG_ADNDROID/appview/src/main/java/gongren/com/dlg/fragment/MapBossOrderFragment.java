package gongren.com.dlg.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.bumptech.glide.Glide;
import com.common.utils.SharedPreferencesUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import gongren.com.dlg.R;
import gongren.com.dlg.activity.PayordersActivity;
import gongren.com.dlg.activity.SetPayPasswordActivity;
import gongren.com.dlg.adapter.MapWorkerPagerAdapterV;
import gongren.com.dlg.javabean.FinishEvent;
import gongren.com.dlg.javabean.MainEvent;
import gongren.com.dlg.javabean.MainToBossFragmentEvent;
import gongren.com.dlg.javabean.OrderButtonInfo;
import gongren.com.dlg.javabean.OrderCreateInfo;
import gongren.com.dlg.javabean.base.ButtonListBean;
import gongren.com.dlg.javabean.master.MasterCard;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.DialogUtils;
import gongren.com.dlg.utils.DlgUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

import static gongren.com.dlg.utils.IntegerUtils.API_TAG_BOSS_TASKING_LIST;

/**
 * Created by liukui .
 * on 2017/4/12
 * 文件描述：
 */

public class MapBossOrderFragment extends BaseFragment {
    private final String TAG = "MapBossOrderFragment";
    @Bind(R.id.bossmap_iv_head)
    CircleImageView ivHead;

    @Bind(R.id.bossmap_layout_operator)
    TextView liOperator;

    @Bind(R.id.bossmap_layout_call)
    View layCall;

    @Bind(R.id.bossmap_tv_demand)
    TextView tvDemand;

    @Bind(R.id.bossmap_tv_starts)
    RatingBar tvStarts;

    @Bind(R.id.bossmap_tv_distance)
    TextView tvDistance;

    @Bind(R.id.bossmap_tv_time)
    TextView tvTime;

    @Bind(R.id.bossmap_tv_name)
    TextView tvName;

    @Bind(R.id.bossmap_tv_company)
    TextView tvCompany;

    @Bind(R.id.bossmap_iv_worker)
    View ivWorker;
    @Bind(R.id.tv_phone)
    TextView tv_phone;
    @Bind(R.id.bossmap_tv_price)
    TextView bossmapTvPrice;


    @Bind(R.id.layout_price)
    LinearLayout layoutPrice; //价格总布局
    @Bind(R.id.tv_total_price)
    TextView tvTotalPrice;  //合计
    @Bind(R.id.tv_remuneration)
    TextView tvRemuneration; //报酬
    @Bind(R.id.tv_service)
    TextView tv_service;

    private String phone, employeeParamId, taskId;
    private String businessNumber;
    private MasterCard masterCard;
    private OrderCreateInfo orderCreateInfo;
    private String bossId;
    private MapWorkerPagerAdapterV mada;
    private OrderButtonInfo orderButtonInfo;
    private int index;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_viewpager_map_order_list, container, false);
        view.setVisibility(View.VISIBLE);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        Bundle arguments = getArguments();
        if (arguments != null) {
            index = arguments.getInt("index", 0);
            // Log.e("===","index-=--"+index);
            orderCreateInfo = (OrderCreateInfo) arguments.getSerializable("orderCreateInfo");
        }
        if (orderCreateInfo != null) {
            setBossDatas(orderCreateInfo);
        }
        tvTotalPrice.setText("");
        tvRemuneration.setText("");
        load();
        return view;
    }

    @Subscribe
    public void onMessageEvent(FinishEvent event) { //支付成功后销毁支付页面进入走这个方法
//        mada.removeData(index);
        EventBus.getDefault().post(new MainToBossFragmentEvent(index + "", 200));
//        EventBus.getDefault().post(new MainToBossFragmentEvent(index + "", 21));
//        if (mada.getDataSize() > 0) {
//            EventBus.getDefault().post(new MainToBossFragmentEvent(index + "", 200));
//            getJobTaskOfOrderList();
//        }
    }

    /**
     * 更新订单操作状态(同意)
     */
    private void updateOrderOperaStatus() {
        final Dialog dialog = DialogUtils.loadingProgressDialog(activity);
        String userId = SharedPreferencesUtils.getString(getContext(), SharedPreferencesUtils.USERID);
        Map<String, Object> map = new HashMap<>();
        map.put("businessNumber", orderCreateInfo.getBusinessNumber());
        map.put("nextStatusCode", orderButtonInfo.getData().get(0).getButtonList().get(1).nextStatusCode);
        map.put("operaStatus", orderButtonInfo.getData().get(0).getButtonList().get(1).operateStatusCode);
//        map.put("userId", userId);
        map.put("format", "json");
        DataUtils.loadData(activity, GetDataConfing.BOSS_UPDATA_ORDER_OPERASTATUS, map, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                String json = dataRequest.getResponseData();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if ("0".equals(jsonObject.optString("code"))) {
//                        mada.removeData(index);
                        EventBus.getDefault().post(new MainToBossFragmentEvent(index + "", 200));
//                        EventBus.getDefault().post(new MainToBossFragmentEvent(index + "", 21));
//                        dialog.dismiss();
//                        if (mada.getDataSize() > 0) {
//                            EventBus.getDefault().post(new MainToBossFragmentEvent(index + "", 200));
//                            getJobTaskOfOrderList();
//                        }

                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }
        });
    }

    /**
     * 更新订单操作状态(拒绝)
     */
    private void updateOrderOperaStatusJ() {
        final Dialog dialog = DialogUtils.loadingProgressDialog(activity);
        String userId = SharedPreferencesUtils.getString(getContext(), SharedPreferencesUtils.USERID);
        Map<String, Object> map = new HashMap<>();
        map.put("businessNumber", orderCreateInfo.getBusinessNumber());
        map.put("nextStatusCode", orderButtonInfo.getData().get(0).getButtonList().get(0).nextStatusCode);
        map.put("operaStatus", orderButtonInfo.getData().get(0).getButtonList().get(0).operateStatusCode);
//        map.put("userId", userId);
        map.put("format", "json");
        DataUtils.loadData(activity, GetDataConfing.BOSS_UPDATA_ORDER_OPERASTATUS, map, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                String json = dataRequest.getResponseData();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if ("0".equals(jsonObject.optString("code"))) {
//                        mada.removeData(index);
//                        EventBus.getDefault().post(new MainToBossFragmentEvent(index + "", 21));
//                        dialog.dismiss();
                        EventBus.getDefault().post(new MainToBossFragmentEvent(index + "", 200));
//                        if (mada.getDataSize() > 0) {
//                            EventBus.getDefault().post(new MainToBossFragmentEvent(index + "", 200));
//                            getJobTaskOfOrderList();
//                        }

                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }
        });
    }

    private void load() {
        String userId = SharedPreferencesUtils.getString(getContext(), SharedPreferencesUtils.USERID);
        Map<String, Object> map = new HashMap<>();
        map.put("businessNumber", orderCreateInfo.getBusinessNumber());
        map.put("userId", userId);
        map.put("format", "json");
        DataUtils.loadData(activity, GetDataConfing.BOSS_ORDERFLOW_CONFIG, map, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                String json = dataRequest.getResponseData();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if ("0".equals(jsonObject.optString("code"))) {
                        orderButtonInfo = new Gson().fromJson(json, OrderButtonInfo.class);
                        if (orderButtonInfo != null) {
                            OrderButtonInfo.DataBean dataBean = orderButtonInfo.getData().get(0);
                            if (null != dataBean) {
                                List<ButtonListBean> buttonList = dataBean.getButtonList();
                                if (buttonList.size() > 1) {
                                    tv_phone.setText(buttonList.get(0).operateStatusText);
                                    liOperator.setText(buttonList.get(1).operateStatusText);
                                } else {
                                    liOperator.setText(buttonList.get(0).operateStatusText);
                                }
                                if (dataBean.getStatusCode() == 30) {
                                    if (null != orderCreateInfo) {
                                        if (!layoutPrice.isShown()) {
                                            layoutPrice.setVisibility(View.VISIBLE);
                                            tv_phone.setVisibility(View.GONE);
                                            tvTotalPrice.setText("合计: " + orderCreateInfo.getTotalPrice() + "元");
                                            tvRemuneration.setText("报酬: " + orderCreateInfo.getTotalPrice() + "元");
                                        }
                                    }
                                } else {
                                    tv_phone.setVisibility(View.VISIBLE);
                                    layoutPrice.setVisibility(View.GONE);
                                }
                                mada.notifyDataSetChanged();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void setOrderCreateInfo(OrderCreateInfo orderCreateInfo, int index) {
        this.orderCreateInfo = orderCreateInfo;
        this.index = index;
    }

    /**
     */
    public void setBusinessNumber(String businessNumber, MapWorkerPagerAdapterV mada) {
        this.businessNumber = businessNumber;
        this.mada = mada;
    }

    public void setBossDatas(OrderCreateInfo orderCreateInfo) {

        if (orderCreateInfo != null) {
            Glide.with(activity).load(orderCreateInfo.getLogo())
                    .error(R.mipmap.morentouxiang)
                    .placeholder(R.mipmap.morentouxiang)
                    .into(ivHead);
            if (orderCreateInfo.getIdentity() == null) {
                tvDemand.setText("自由工作者");
            } else {
                if ("0".equals(orderCreateInfo.getIdentity())) {
                    tvDemand.setText("在校学生");
                } else if ("1".equals(orderCreateInfo.getIdentity())) {
                    tvDemand.setText("自由工作者");
                } else {
                    tvDemand.setText("兼职人员");
                }
            }
            bossmapTvPrice.setText(orderCreateInfo.getDescription() == null ? "自由、灵活、赚钱" : orderCreateInfo.getDescription());
            tvCompany.setText(orderCreateInfo.getCreditCount() == null ? "36.5" : orderCreateInfo.getCreditCount());
            tvName.setText(orderCreateInfo.getName() == null ? DlgUtils.showMidHintPhone(orderCreateInfo.getPhone()) : orderCreateInfo.getName());

            tvDemand.setText(orderCreateInfo.getDescription() == null ? "兼职人员" : orderCreateInfo.getDescription());



            OrderCreateInfo.XyCoordinateVoBean xyCoordinateVo = orderCreateInfo.getXyCoordinateVo();
            if(null != xyCoordinateVo){
                double jobYCoordinate = xyCoordinateVo.getJobYCoordinate();
                double jobXCoordinate = xyCoordinateVo.getJobXCoordinate();
                double userXCoordinate = xyCoordinateVo.getUserXCoordinate();
                double userYCoordinate = xyCoordinateVo.getUserYCoordinate();
                float distance = AMapUtils.calculateLineDistance(new LatLng(jobYCoordinate, jobXCoordinate)
                        , new LatLng(userYCoordinate, userXCoordinate));
                if (xyCoordinateVo != null && xyCoordinateVo.getDistanceToJob() != null) {
                    tvDistance.setText("距离" + xyCoordinateVo.getDistanceToJob() + "km");
                } else {
                    tvDistance.setText("距离" + String.format("%.2f", distance / 1000) + "km");
                }
            }else {
                tvDistance.setText("距离未知");
            }


            tvStarts.setRating(orderCreateInfo.getScoreCount() == null ? 5 : Float.parseFloat(orderCreateInfo.getScoreCount()));
            if (orderCreateInfo.getScoreCount() != null) {
                float i = Float.parseFloat(orderCreateInfo.getScoreCount());
                if (i > 0) {
                    tvStarts.setRating(i);//后台数据格式不对
                }
            }
            if (orderCreateInfo.getReleaseCount() != null) {
                tvTime.setText("已接" + orderCreateInfo.getReleaseCount().intValue() + "单");
            }

        }

    }

    /**
     * 1.1.6（REST）首页-雇主-有人接单、等待验收-列表
     */
    private void getJobTaskOfOrderList() {
//        final Dialog dialog = DialogUtils.showLoadingDialog(activity);
        //String userId = SharedPreferencesUtils.getString(getContext(), SharedPreferencesUtils.USERID);
        Map<String, Object> map = new HashMap<>();
       /* map.put("userId", userId);
        map.put("format", "json");*/
        DataUtils.loadData(activity, GetDataConfing.BOSS_IS_TASKING_LIST, map, API_TAG_BOSS_TASKING_LIST, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                if (dataRequest.isNetError()) {
                    ShowGetDataError.showNetError(activity);
                } else {
                    String json = dataRequest.getResponseData();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if ("0".equals(jsonObject.optString("code"))) {
                            SharedPreferencesUtils.saveString(activity, "orderinfo", json);
                            //taskOfOrderInfo = new Gson().fromJson(json, TaskOfOrderInfo.class);
                            EventBus.getDefault().post(new MainEvent(json, 4));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("===", "e--" + e.getMessage().toString());

                    }
                }
//                if (dialog != null) {
//
//                    dialog.dismiss();
//                }
            }

        });

    }

    @OnClick({R.id.bossmap_iv_worker, R.id.bossmap_layout_operator, R.id.tv_phone})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bossmap_iv_worker:
               /* Intent intent1 = new Intent(activity, EmployeeDetailsActivity.class);
                intent1.putExtra("masterCard",masterCard);
                //雇员详情
                startActivity(intent1);*/
                break;

            case R.id.bossmap_layout_operator:
                // Dialog dialog = DialogUtils.showLoadingDialog(activity);
               /* mada.removeData(index);
                if(mada.getDataSize()>0){
                    //getJobTaskOfOrderList(dialog);
                }else {
                 //   dialog.dismiss();
                }*/
                if (orderButtonInfo != null) {
                    OrderButtonInfo.DataBean dataBean = orderButtonInfo.getData().get(0);
                    if (null != dataBean && dataBean.getStatusCode() == 30) {
                        if (SharedPreferencesUtils.getBoolean(activity, SharedPreferencesUtils.havePayPwd)) {
                            Intent intent = new Intent(activity, PayordersActivity.class);//正常的支付页面
                            String priceXiao = "0";
                            intent.putExtra("orders", businessNumber + "#" + priceXiao);//订单业务编号
                            intent.putExtra("isfrom", 1);
                            intent.putExtra("tip", Double.parseDouble(priceXiao));
                            if (!TextUtils.isEmpty(orderCreateInfo.getTotalPrice())) {
                                intent.putExtra("pay", Double.parseDouble(orderCreateInfo.getTotalPrice()));
                                intent.putExtra("bao", Double.parseDouble(orderCreateInfo.getInsuranceAmount()));
                            }
                            startActivity(intent);
                        } else {
                            startActivity(new Intent(activity, SetPayPasswordActivity.class).putExtra("type", 1));
                        }
                    } else {
                        updateOrderOperaStatus();//同意
                    }
                }

                break;
            case R.id.tv_phone:
                updateOrderOperaStatusJ();//拒绝

                break;
        }
    }

    /**
     * 打电话
     */
    private void callPhone() {
        if (phone != null) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phone));
            startActivity(intent);
        } else {
            ToastUtils.showToastShort(activity, "电话获取失败");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }
}
