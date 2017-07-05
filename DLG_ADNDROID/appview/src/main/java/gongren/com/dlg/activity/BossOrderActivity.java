package gongren.com.dlg.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.canyinghao.canrefresh.classic.FooterRefreshView;
import com.common.utils.SharedPreferencesUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import gongren.com.dlg.R;
import gongren.com.dlg.adapter.BossOrderAdapter;
import gongren.com.dlg.adapter.ServiceAdapter;
import gongren.com.dlg.javabean.JobOrdersInfo;
import gongren.com.dlg.javabean.JobOrdersListJson;
import gongren.com.dlg.javabean.ReceiveReturnIdJson;
import gongren.com.dlg.javabean.worker.MySericeBean;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.DialogUtils;
import gongren.com.dlg.utils.GsonUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 选择需求
 */
public class BossOrderActivity extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener {
    @Bind(R.id.rb_01)
    RadioButton mRb01;
    @Bind(R.id.rb_02)
    RadioButton mRb02;
    @Bind(R.id.map_rg)
    RadioGroup mRg;
    @Bind(R.id.iv_empty)
    ImageView mIvEmpty;
    private BossOrderActivity instance;
    @Bind(R.id.iv_back)
    ImageView tvCancel;
    @Bind(R.id.can_content_view)
    SwipeMenuListView canContentView;
    @Bind(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @Bind(R.id.can_refresh_footer)
    FooterRefreshView canRefreshFooter;
    @Bind(R.id.refresh)
    CanRefreshLayout refresh;
    @Bind(R.id.common_nothing)
    ImageView tvNothing;
    @Bind(R.id.boss_order_checked)
    Button confirm;

    private BossOrderAdapter bossOrderAdapter;
    private ArrayList<JobOrdersInfo> mList = new ArrayList<>();
    private static final int TAG_REQUEST = 1;
    private static final int TAG_REQUEST_INVITE = 2;
    private String employeeParamId;
    private String minId = "";
    private ServiceAdapter mServiceAdapter;
    private List<MySericeBean.DataBean> mDataBeen = new ArrayList<>();
    private boolean isService = false;
    private Dialog mLoadingDialog;
    private String bossId;
    private boolean isHasXq = true,isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_order);
        ButterKnife.bind(this);
        initDatas();
        initListener();
    }

    private void initDatas() {
        employeeParamId = getIntent().getStringExtra("employeeParamId");
        bossId = getIntent().getStringExtra("bossId");
        bossOrderAdapter = new BossOrderAdapter(context, mList);
        mServiceAdapter = new ServiceAdapter(this);//服务

        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                minId = "";
                page = 0;
                if (mRb01.isChecked()) {
                    isRefreshing = false;
                    isService = false;
                    upLoadDatas();
                } else {
                    isRefreshing = false;
                    isService = true;
                    service(page);
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(mRb01.isChecked()){
            isHasXq = true;
            upLoadDatas();
        }else{
            service(page);
        }
    }

    private int page;

    private void service(final int page) {
        if (mLoadingDialog == null && !isRefreshing) {
            mLoadingDialog = DialogUtils.showLoadingDialog(this);
        } else if (mLoadingDialog != null && !isRefreshing && !mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", employeeParamId);
        map.put("pageIndex", page + "");
        map.put("pageSize", "20");
        map.put("format", "json");
        DataUtils.loadData(context, GetDataConfing.SELECT_SERVICELIST, map, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                mLoadingDialog.dismiss();
                refresh.loadMoreComplete();
                refresh.refreshComplete();
                try {
                    JSONObject jsonObject = new JSONObject(dataRequest.getResponseData());
                    if (jsonObject.optInt("code") == 0) {
                        MySericeBean mySericeBean = new Gson().fromJson(dataRequest.getResponseData(), MySericeBean.class);
                        if (page == 0) {
                            mDataBeen.clear();
                        }
                        mDataBeen.addAll(mySericeBean.getData());
                        if(mDataBeen.size()==0){
                            canContentView.setVisibility(View.GONE);
                            mIvEmpty.setVisibility(View.VISIBLE);
                            refresh.setRefreshEnabled(false);
                            refresh.setLoadMoreEnabled(false);
                            if(!isHasXq){
                                goToRelese();//去发布
                                return;
                            }
                        }else {
                            canContentView.setVisibility(View.VISIBLE);
                            mIvEmpty.setVisibility(View.GONE);
                            refresh.setRefreshEnabled(true);
                            refresh.setLoadMoreEnabled(true);
                        }

                        canContentView.setAdapter(mServiceAdapter);
                        mServiceAdapter.setDataBeen(mDataBeen);
                        if(isFirst){
                            isFirst = false;
                        }
                    } else {
                        Toast.makeText(context, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void upLoadDatas() {
        if (mLoadingDialog == null && !isRefreshing) {
            mLoadingDialog = DialogUtils.showLoadingDialog(this);
        } else if (mLoadingDialog != null && !isRefreshing && !mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));
        map.put("status", "0");
        map.put("pageSize", "1000");
        map.put("minId", minId);
        map.put("employeeId", employeeParamId);
        DataUtils.loadData(context, GetDataConfing.getJobTaskOfNotUsedPage, map, TAG_REQUEST, responseDataCallback);
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        refresh.setOnRefreshListener(this);
        refresh.setOnLoadMoreListener(this);
    }

    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            Log.i("====s====", dataRequest.getResponseData());
            if (dataRequest.isNetError()) {
                ShowGetDataError.showNetError(context);
            } else {
                String json = dataRequest.getResponseData();
                if (!TextUtils.isEmpty(json)) {
                    if (dataRequest.getWhat() == TAG_REQUEST) {
                        isHasXq = true;
                        JobOrdersListJson jobOrdersListJson = GsonUtils.jsonToBean(json, JobOrdersListJson.class);
                        if (jobOrdersListJson.data != null && jobOrdersListJson.data.size() > 0) {
                            if (minId.equals("")) {
                                mList.clear();
                                isRefreshing = false;
                            }
                            refresh.loadMoreComplete();
                            refresh.refreshComplete();
                            canContentView.setAdapter(bossOrderAdapter);
                            minId = jobOrdersListJson.data.get(jobOrdersListJson.data.size() - 1).id;

                            mList.addAll(jobOrdersListJson.data);
                            if(mList.size()==0){
                                canContentView.setVisibility(View.GONE);
                                mIvEmpty.setVisibility(View.VISIBLE);
                                refresh.setRefreshEnabled(false);
                                refresh.setLoadMoreEnabled(false);
                            }else {
                                canContentView.setVisibility(View.VISIBLE);
                                mIvEmpty.setVisibility(View.GONE);
                                refresh.setRefreshEnabled(true);
                                refresh.setLoadMoreEnabled(true);
                            }
                            bossOrderAdapter.notifyDataSetChanged();//刷新
                            mLoadingDialog.dismiss();
                        } else {

                            isHasXq = false;
                            if(isFirst){
                                mRb02.setChecked(true);
                            }else {
                                if(mList != null){
                                    mList.clear();
                                }
                                canContentView.setAdapter(bossOrderAdapter);
                                canContentView.setVisibility(View.GONE);
                                mIvEmpty.setVisibility(View.VISIBLE);
                                refresh.setRefreshEnabled(false);
                                refresh.setLoadMoreEnabled(false);
                                mLoadingDialog.dismiss();
                            }

                        }
                    } else if (dataRequest.getWhat() == TAG_REQUEST_INVITE) {
                        //邀请
                        ReceiveReturnIdJson returnIdJson = GsonUtils.jsonToBean(json, ReceiveReturnIdJson.class);
                        if (returnIdJson.code.equals("0")) {
                            finish();//邀请成功后 关闭页面
                            ToastUtils.showToastShort(context, "发起邀请成功！");
                        } else {
                            ToastUtils.showToastShort(context, "邀请失败");
                        }
                        mLoadingDialog.dismiss();
                    }
                }
            }
        }
    };

    @OnClick({R.id.iv_back, R.id.boss_order_checked})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.boss_order_checked:
                if (isService) {
                    if (mDataBeen == null || mDataBeen.size() == 0) {
                        Toast.makeText(context, "没有可以选择的服务", Toast.LENGTH_SHORT).show();
                        return;
                    }else if(mServiceAdapter.getSelectPosition()==-1){
                        Toast.makeText(context, "请选择服务", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(this, WillActivity.class);
                    intent.putExtra("dataBean", mDataBeen.get(mServiceAdapter.getSelectPosition()));
                    startActivity(intent);
                } else {
                    if (bossOrderAdapter != null) {
                        int selectPosition = bossOrderAdapter.getSelectPosition();
                        if (selectPosition != -1) {

                            Map<String, Object> map = BaseMapUtils.getMap(context);
                            map.put("userId", SharedPreferencesUtils.getString(BossOrderActivity.this, SharedPreferencesUtils.USERID));
                            map.put("employeeId", employeeParamId);
                            map.put("taskId", mList.get(selectPosition).id);
                            DataUtils.loadData(context, GetDataConfing.getJobTaskAskByEmployee, map, TAG_REQUEST_INVITE, responseDataCallback);

                        } else {
                            if(isHasXq){
                                ToastUtils.showToastShort(context, "请选择需求");
                            }else{
                                goToRelese();
                            }

                        }
                    }
                }
                break;
        }
    }

    /**
     * 去发布任务
     */
    private void goToRelese(){
        SweetAlertDialog dialog = new SweetAlertDialog(this,SweetAlertDialog.NORMAL_TYPE)
                .setContentText("您还没有发布零工，是否去发布")
                .setCancelText("取消")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        finish();
                    }
                }).setConfirmText("确认")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        startActivity(new Intent(context,ReleaseXQActivity.class));
                        sweetAlertDialog.dismiss();
                    }
                }).setTitleText("提示:");
        dialog.show();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onLoadMore() {
        isRefreshing = true;
        if (!isService) {
            minId = "";
            upLoadDatas();
        } else {
            page++;
            service(page);
        }
    }

    private boolean isRefreshing;

    @Override
    public void onRefresh() {
        isRefreshing = true;
        if (!isService) {
            minId = "";
            bossOrderAdapter.isChecked = false;
            upLoadDatas();
        } else {
            page = 0;
            service(page);
        }

    }
}
