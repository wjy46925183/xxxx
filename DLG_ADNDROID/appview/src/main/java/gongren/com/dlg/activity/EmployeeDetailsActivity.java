package gongren.com.dlg.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.common.utils.SharedPreferencesUtils;
import com.common.view.iamge.TouchImageView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aym.util.json.JsonMap;
import aym.util.json.JsonParseHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import gongren.com.dlg.R;
import gongren.com.dlg.adapter.EmployeeDatailsRecordAdapter;
import gongren.com.dlg.adapter.EmployeeDetailsListAdapter;
import gongren.com.dlg.adapter.ServiceListAdapter;
import gongren.com.dlg.application.MyApplication;
import gongren.com.dlg.javabean.master.MasterCard;
import gongren.com.dlg.javabean.worker.MySericeBean;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.DialogUtils;
import gongren.com.dlg.utils.DimensUtils;
import gongren.com.dlg.view.MyListView;
import gongren.com.dlg.view.ObservableScrollView;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;

/**
 * 雇主个人详情
 */
public class EmployeeDetailsActivity extends BaseActivity {

    @Bind(R.id.employeedetails_title)
    RelativeLayout employeedetails_title;

    @Bind(R.id.iv_back)
    ImageView iv_back;

    @Bind(R.id.employeedetails_head_layout)
    LinearLayout employeedetails_head_layout;

    @Bind(R.id.ll_bill_record)
    LinearLayout llBillRecord;
    @Bind(R.id.employeedetails_lv_bill_record)
    MyListView employeedetailsLvBillRecord;
    @Bind(R.id.ll_order_record)
    LinearLayout llOrderRecord;
    @Bind(R.id.employeedetails_lv_order_record)
    MyListView employeedetailsLvOrderRecord;
    @Bind(R.id.ll_cancel_record)
    LinearLayout llCancelRecord;
    @Bind(R.id.employeedetails_lv_cancel_record)
    MyListView employeedetailsLvCancelRecord;
    @Bind(R.id.ll_come_late_record)
    LinearLayout llComeLateRecord;
    @Bind(R.id.employeedetails_lv_come_late_record)
    MyListView employeedetailsLvComeLateRecord;

    @Bind(R.id.scroll)
    ObservableScrollView scroll;

    @Bind(R.id.employeedetails_head)
    CircleImageView employeedetailsHead;
    @Bind(R.id.employeedetails_name)
    TextView employeedetailsName;
    @Bind(R.id.employeedetails_faithvalue)
    TextView employeedetailsFaithvalue;
    @Bind(R.id.tv_employeedetails_describe)
    TextView tvEmployeedetailsDescribe;
    @Bind(R.id.tv_billing_count)
    TextView tvBillingCount;
    @Bind(R.id.tv_orders_count)
    TextView tvOrdersCount;
    @Bind(R.id.tv_cancel_count)
    TextView tvCancelCount;
    @Bind(R.id.tv_late_count)
    TextView tvLateCount;
    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.tv_real_name)
    TextView tvRealName;
    @Bind(R.id.tv_identity)
    TextView tvIdentity;
    @Bind(R.id.tv_height)
    TextView tvHeight;
    @Bind(R.id.tv_weight)
    TextView tvWeight;
    @Bind(R.id.tv_live_address)
    TextView tvLiveAddress;
    @Bind(R.id.ll)
    LinearLayout ll;
    @Bind(R.id.ll_myservice)
    LinearLayout llMyservice;
    @Bind(R.id.tv_email)
    TextView tvEmail;

    @Bind(R.id.employeedetails_title_head)
    CircleImageView employeedetailsTitleHead;
    @Bind(R.id.employeedetails_title_name)
    TextView employeedetailsTitleName;
    @Bind(R.id.tv_btn_telephone_contact)
    TextView tvBtnTelephoneContact;
    @Bind(R.id.tv_btn_hire_ta)
    TextView tvBtnHireTa;
    @Bind(R.id.bottom_layout)
    LinearLayout bottomLayout;
    private final int CALL_PHONE_TAG = 1;
    @Bind(R.id.service_listview)
    MyListView mServiceListview;
    @Bind(R.id.rl_trust)
    RelativeLayout rlTrust;

    private List<JsonMap<String, Object>> dataList = new ArrayList<>();   //数据源

    private EmployeeDetailsListAdapter detailsListBillAdapter;
    private EmployeeDetailsListAdapter detailsListOrderAdapter;
    private EmployeeDatailsRecordAdapter datailsRecordCancelAdapter;
    private EmployeeDatailsRecordAdapter datailsRecordComeLateAdapter;
    private MasterCard mMasterCard;
    private String phone, userId;

    private List<JsonMap<String, Object>> billRecordDataList = new ArrayList<>();   //发单记录数据源
    private List<JsonMap<String, Object>> orderRecordDataList = new ArrayList<>();   //接单记录数据源
    private List<JsonMap<String, Object>> cancelRecordDataList = new ArrayList<>();   //取消记录数据源
    private List<JsonMap<String, Object>> comeLateRecordDataList = new ArrayList<>();   //迟到记录数据源
    private String iamgeUrl; //头像路径
    private Dialog mLoadingDialog;
    private MySericeBean mMySericeBean;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employeedetails);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    //初始化View
    private void initView() {
        tvBtnTelephoneContact.setVisibility(View.GONE);
        if (!MyApplication.isBoss)//是雇员身份或则游客身份
        {
            bottomLayout.setVisibility(View.GONE);

        } else {
            bottomLayout.setVisibility(View.VISIBLE);
        }

        detailsListBillAdapter = new EmployeeDetailsListAdapter(context, billRecordDataList);
        detailsListOrderAdapter = new EmployeeDetailsListAdapter(context, orderRecordDataList);
        datailsRecordCancelAdapter = new EmployeeDatailsRecordAdapter(context, cancelRecordDataList, 0);
        datailsRecordComeLateAdapter = new EmployeeDatailsRecordAdapter(context, comeLateRecordDataList, 1);

        employeedetailsLvBillRecord.setAdapter(detailsListBillAdapter);//发单记录
        employeedetailsLvOrderRecord.setAdapter(detailsListOrderAdapter);//接单记录
        employeedetailsLvCancelRecord.setAdapter(datailsRecordCancelAdapter);//取消记录
        employeedetailsLvComeLateRecord.setAdapter(datailsRecordComeLateAdapter);//迟到记录

        initListener();
        //处理初始化不置顶的问题
        employeedetails_title.setFocusable(true);
        employeedetails_title.setFocusableInTouchMode(true);
        employeedetails_title.requestFocus();
        loadServiceList();//加载服务列表
        mServiceListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, ServiceDetailActivity.class);
                intent.putExtra("dataBean", mMySericeBean.getData().get(position));
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (getIntent() != null) {
            mMasterCard = (MasterCard) getIntent().getSerializableExtra("masterCard");//雇主地图列表过来
            if (null != getIntent().getStringExtra("userId")) {//雇主列表过来)
                userId = getIntent().getStringExtra("userId");
            } else {
                userId = mMasterCard.getData().get(0).getUserAttributeRestVo().getUserId();
            }
        }
        //从地图列表过来 免加载个人部分，免去加载一次个人信息接口
        if (mMasterCard != null) {
            fillData();
            if (mMasterCard.data.size() > 0) {
                loadList(mMasterCard.data.get(0).id);
            }
        }
        //从雇主列表过来
        if (!TextUtils.isEmpty(userId)) {
            loadDatas();//加载个人信息部分 得到mMasterCard对象
            loadList(userId);
        }
    }

    /**
     * 填充雇员信息
     */
    private void fillData() {
        if (mMasterCard != null && mMasterCard.data != null && mMasterCard.data.size() > 0) {
            List<MasterCard.DataBean> data = mMasterCard.data;
            MasterCard.DataBean.UserAttributeRestVoBean restVo = data.get(0).userAttributeRestVo;
            MasterCard.DataBean.UserRestVoBean userRestVo = data.get(0).userRestVo;
            if(null != mMasterCard && "0".equals(data.get(0).isShowCreditArchive)){
                rlTrust.setVisibility(View.VISIBLE);
            }else {
                rlTrust.setVisibility(View.GONE);
            }
            iamgeUrl = restVo.logo;
            Glide.with(context).load(iamgeUrl).error(R.mipmap.icon_default_user).into(employeedetailsHead);
            Glide.with(context).load(iamgeUrl).error(R.mipmap.icon_default_user).into(employeedetailsTitleHead);

            employeedetailsName.setText(restVo.name);
            employeedetailsTitleName.setText(restVo.name);
            tvEmployeedetailsDescribe.setText(restVo.personalizedSignature);

            employeedetailsFaithvalue.setText(restVo.creditCount == null ? "36.5" : restVo.creditCount);
            tvBillingCount.setText(restVo.releaseCount != null ? restVo.releaseCount : "0");
            tvOrdersCount.setText(restVo.joinCount != null ? restVo.joinCount : "0");
            tvCancelCount.setText(restVo.cancelCount != null ? restVo.cancelCount : "0");
            tvLateCount.setText(restVo.lateCount != null ? restVo.lateCount : "0");
            tvEmail.setText(userRestVo.email == null ? "" : userRestVo.email);

            tvNickname.setText(userRestVo.username);
            tvRealName.setText(restVo.name);
            String nameType = "自由工作者";
            if (!TextUtils.isEmpty(restVo.identity)) {
                int identity = Integer.parseInt(restVo.identity);
                switch (identity) {
                    case 0:
                        nameType = "在校学生";
                        break;
                    case 1:
                        nameType = "自由工作者";
                        break;
                    case 2:
                        nameType = "兼职人员";
                        break;
                }
            }
            tvIdentity.setText(nameType);//1

            tvHeight.setText(restVo.height);
            tvWeight.setText(restVo.weight);
            tvLiveAddress.setText(restVo.location);

            phone = userRestVo.phone;
        }
    }

    private void initListener() {
        scroll.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (DimensUtils.px2dip(context, scrollY) > 200) {
                    employeedetails_title.setBackgroundColor(getResources().getColor(R.color.white));
                    employeedetails_head_layout.setVisibility(View.VISIBLE);
                    iv_back.setImageResource(R.mipmap.back_black);
                } else {
                    employeedetails_title.setBackgroundColor(getResources().getColor(R.color.cmbkb_transparent));
                    employeedetails_head_layout.setVisibility(View.GONE);
                    iv_back.setImageResource(R.mipmap.back);
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.iv_back, R.id.tv_btn_telephone_contact, R.id.tv_btn_hire_ta,
            R.id.employeedetails_head, R.id.rl_trust})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_trust://诚信档案
                Intent intent = new Intent(this, WebViewPageActivity.class);
                intent.putExtra("functionTitle", "诚信档案");
                intent.putExtra("contentUrl", GetDataConfing.BASEURL_H5);
                intent.putExtra("userId", userId);
                intent.putExtra("isShow", false);
                intent.putExtra("type", "9");//
                startActivity(intent);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.employeedetails_head:
                mPopupWindow = new PopupWindow(this);
                mPopupWindow.setWidth(getResources().getDisplayMetrics().widthPixels);
                mPopupWindow.setHeight(getResources().getDisplayMetrics().heightPixels);
                View inflate = LayoutInflater.from(this).inflate(R.layout.page_iamge, null);
                TouchImageView imageView = (TouchImageView) inflate.findViewById(R.id.iamge_view);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPopupWindow.dismiss();
                    }
                });
                Glide.with(this).load(iamgeUrl).placeholder(R.mipmap.morentouxiang).error(R.mipmap.morentouxiang).into(imageView);
                mPopupWindow.setContentView(inflate);
                mPopupWindow.showAtLocation(inflate, Gravity.CENTER, 0, 0);
                break;
            case R.id.tv_btn_telephone_contact:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_TAG);
                    } else {
                        callPhone();
                    }
                } else {
                    callPhone();
                }
                break;
            case R.id.tv_btn_hire_ta:
                String userId = SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID);
                if (!TextUtils.isEmpty(userId)) {
                    Intent bossOrderActivityIntent = new Intent(context, BossOrderActivity.class);
                    if (mMasterCard != null && mMasterCard.data != null && mMasterCard.data.size() > 0) {
                        bossOrderActivityIntent.putExtra("employeeParamId", mMasterCard.data.get(0).userAttributeRestVo.userId);
                    }
                    startActivity(bossOrderActivityIntent);
                } else {
                    startActivityForResult(new Intent(context, LoginDialogActivity.class), 0);
                    //ToastUtils.showToastLong(context, "请先登录");
                }

                break;
        }

    }

    /**
     * 打电话权限
     */
    private void callPhone() {
        if (!TextUtils.isEmpty(phone)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phone));
            startActivity(intent);
        } else {
            Toast.makeText(context, "没有获取该雇员的电话信息！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 权限申请回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CALL_PHONE_TAG) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone();
            } else {
                Toast.makeText(context, "未获取电话权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*****
     * 加载该雇主的个人信息
     */
    private void loadDatas() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("format", "json");
        DataUtils.loadData(this, GetDataConfing.BOSS_MAP_DETAIL, map, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                String responseData = dataRequest.getResponseData();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    if ("0".equals(jsonObject.optString("code"))) {
                        mMasterCard = new Gson().fromJson(responseData, MasterCard.class);
                        fillData();//加载列表
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 服务列表
     */
    public void loadServiceList() {
        if (mLoadingDialog == null) {
            mLoadingDialog = DialogUtils.showLoadingDialog(this);
        } else if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
        final HashMap<String, Object> map = new HashMap<>();
        map.put("userId", getIntent().getStringExtra("employeeParamId"));
        map.put("pageIndex", 0 + "");
        map.put("pageSize", "200");
        map.put("format", "json");
        Log.i("====s====", map.toString());
        DataUtils.loadData(this, GetDataConfing.SELECT_SERVICELIST, map, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                mLoadingDialog.dismiss();
                Log.i("=====s====", dataRequest.getResponseData());
                try {
                    JSONObject jsonObject = new JSONObject(dataRequest.getResponseData());
                    if (jsonObject.optInt("code") == 0) {
                        mMySericeBean = new Gson().fromJson(dataRequest.getResponseData(), MySericeBean.class);
                        mServiceListview.setAdapter(new ServiceListAdapter(context, mMySericeBean.getData()));
                        llMyservice.setVisibility(View.VISIBLE);
                        if(null == mMySericeBean.getData() || mMySericeBean.getData().size() == 0){
                            llMyservice.setVisibility(View.GONE);
                        }
                    } else {
                        llMyservice.setVisibility(View.GONE);
                        //Toast.makeText(context, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 加载各个接单情况
     *
     * @param userId
     */
    private void loadList(String userId) {
        if (mLoadingDialog == null) {
            mLoadingDialog = DialogUtils.showLoadingDialog(this);
        } else if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("format", "json");
        DataUtils.loadData(context, GetDataConfing.queryReleaseJoinLateCancel, map, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                mLoadingDialog.dismiss();
                Log.i("====s==", dataRequest.getResponseData());
                dataList = JsonParseHelper.getJsonMap_List_JsonMap(dataRequest.getResponseData(), "data");
                if (dataList != null && dataList.size() > 0) {
                    billRecordDataList = dataList.get(0).getList_JsonMap("orderDispatchRestVos");
                    orderRecordDataList = dataList.get(0).getList_JsonMap("orderTakingRestVos");
                    cancelRecordDataList = dataList.get(0).getList_JsonMap("orderCancelRestVos");
                    comeLateRecordDataList = dataList.get(0).getList_JsonMap("orderOperateRestVos");
                }
                //刷新数据
                if (billRecordDataList != null && billRecordDataList.size() > 0) {
                    detailsListBillAdapter.refreshDatas(billRecordDataList, 1);
                    llBillRecord.setVisibility(View.VISIBLE);
                } else {
                    llBillRecord.setVisibility(View.GONE);
                }
                if (orderRecordDataList != null && orderRecordDataList.size() > 0) {
                    detailsListOrderAdapter.refreshDatas(orderRecordDataList, 1);
                    llOrderRecord.setVisibility(View.VISIBLE);
                } else {
                    llOrderRecord.setVisibility(View.GONE);
                }
                if (cancelRecordDataList != null && cancelRecordDataList.size() > 0) {
                    datailsRecordCancelAdapter.refreshDatas(cancelRecordDataList, 1);
                    llCancelRecord.setVisibility(View.VISIBLE);
                } else {
                    llCancelRecord.setVisibility(View.GONE);
                }
                if (comeLateRecordDataList != null && comeLateRecordDataList.size() > 0) {
                    datailsRecordComeLateAdapter.refreshDatas(comeLateRecordDataList, 1);
                    llComeLateRecord.setVisibility(View.VISIBLE);
                } else {
                    llComeLateRecord.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        } else {
            super.onBackPressed();
        }
    }
}
