package gongren.com.dlg.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.canyinghao.canrefresh.classic.FooterRefreshView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.adapter.PreOrderAdapter;
import gongren.com.dlg.javabean.RefreshEvent;
import gongren.com.dlg.javabean.master.masterlist.ListBean;
import gongren.com.dlg.javabean.master.masterlist.OrderStatusListBean;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.DialogUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 雇主，我的需求，待接单详情页。
 */
public class PreOrderActivity extends BaseActivity implements CanRefreshLayout.OnRefreshListener,
        CanRefreshLayout.OnLoadMoreListener {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_zhiwei)
    TextView tvZhiwei;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    /*@Bind(R.id.pre_tab)
    TextView preTab;
    @Bind(R.id.pre_num_text)
    TextView preNumText;*/
    @Bind(R.id.per_listview)
    ListView perListview;
    @Bind(R.id.per_linear)
    LinearLayout perLinear;
    @Bind(R.id.ok_tab)
    TextView okTab;
    @Bind(R.id.ok_num_text)
    TextView okNumText;
    @Bind(R.id.ok_listview)
    ListView okListview;
    @Bind(R.id.ok_linear)
    LinearLayout okLinear;
    @Bind(R.id.cancle_listview)
    ListView cancleListview;
    @Bind(R.id.can_content_view)
    NestedScrollView canContentView;
    @Bind(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @Bind(R.id.can_refresh_footer)
    FooterRefreshView canRefreshFooter;
    @Bind(R.id.refresh)
    CanRefreshLayout refresh;
    @Bind(R.id.tv_bg)
    ImageView tvBg;
    @Bind(R.id.all_select_cbox)
    CheckBox allSelectCbox;
    @Bind(R.id.all_select_text)
    TextView allSelectText;
    @Bind(R.id.all_select_re)
    RelativeLayout allSelectRe;
    @Bind(R.id.cancle_btn)
    Button cancleBtn;
    @Bind(R.id.agree_btn)
    Button agreeBtn;
    @Bind(R.id.gbottom_re)
    RelativeLayout gbottomRe;

    private PreOrderAdapter preOrderAdapter;
    //    private MasterDetailBean mMasterDetailBean;
    private OrderStatusListBean mStatusListBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_order_detail);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            mStatusListBean = (OrderStatusListBean) getIntent().getSerializableExtra("bean");
            if (mStatusListBean == null) {
                return;
            }
            tvTitle.setText("有人接单");
            if (mStatusListBean.status == 6 || mStatusListBean.status == 7) {
                gbottomRe.setVisibility(View.GONE);
                if(mStatusListBean.status == 6 ){
                    tvTitle.setText("等待雇员同意");
                }else {
                    tvTitle.setText("拒绝邀请");
                }
            }
            if (mStatusListBean.list.size() == 0) {
                tvBg.setVisibility(View.VISIBLE);
            }
            initView();
            initListener();
            iniListViewOnClick();
            //不允许上拉加载更多
            refresh.setLoadMoreEnabled(false);
        }
    }

    private void iniListViewOnClick() {
        final List<ListBean> list = mStatusListBean.list;
        perListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PreOrderActivity.this, MasterDetailsActivity.class);
                intent.putExtra("businessNumber", list.get(position).businessNumber);//"订单业务编号"
                intent.putExtra("status", mStatusListBean.status);
                startActivityForResult(intent, 200);
            }
        });


    }

    private String phone;
    /**
     * 点击事件
     */
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.call_btn:
                    phone = (String) v.getTag();
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(PreOrderActivity.this, Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(PreOrderActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                        } else {
                            callPhone(phone);
                        }
                    } else {
                        callPhone(phone);
                    }
                    break;
            }
        }
    };

    /**
     * 拨打电话
     *
     * @param phone
     */
    private void callPhone(String phone) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    private void initView() {
        tvZhiwei.setText(getIntent().getStringExtra("postName") + "(" + getIntent().getStringExtra("salary") + ")");
        //preNumText.setText(data.get(0).getList().size() + "");
        ivRight.setVisibility(View.INVISIBLE);

        preOrderAdapter = new PreOrderAdapter(this, mStatusListBean, listener);
        perListview.setAdapter(preOrderAdapter);
        preOrderAdapter.setAllCheck(0, allSelectCbox);
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        refresh.setOnRefreshListener(this);
        refresh.setOnLoadMoreListener(this);
    }

    @OnClick({R.id.iv_back, R.id.cancle_btn, R.id.agree_btn, R.id.all_select_cbox, R.id.all_select_re})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.all_select_cbox:
            case R.id.all_select_re://全选
                if (allSelectCbox.isChecked()) {
                    allSelectCbox.setChecked(false);
                } else {
                    allSelectCbox.setChecked(true);
                }
                preOrderAdapter.setAllCheck(allSelectCbox.isChecked() ? 1 : 2, allSelectCbox);
                break;
            case R.id.cancle_btn://我的接单 拒绝
                if (allSelectCbox.isChecked()) {
                    argree(11 + "", 11 + "");
                } else {
                    List<Boolean> mBooleanList = preOrderAdapter.getCheckedtList();
                    boolean is_all = false;
                    for (int i = 0; i < mBooleanList.size(); i++) {
                        if (mBooleanList.get(i)) {
                            argree(11 + "", 11 + "");
                            is_all = true;
                        }
                    }
                    if (!is_all) {
                        ToastUtils.showToastShort(PreOrderActivity.this, "请选择要拒绝的雇员");
                    }
                }

                break;
            case R.id.agree_btn://同意
                if (allSelectCbox.isChecked()) {
                    argree(20 + "", 20 + "");
                } else {
                    List<Boolean> mBooleanList = preOrderAdapter.getCheckedtList();
                    boolean is_all = false;
                    for (int i = 0; i < mBooleanList.size(); i++) {
                        if (mBooleanList.get(i)) {
                            argree(20 + "", 20 + "");
                            is_all = true;
                        }
                    }
                    if (!is_all) {
                        ToastUtils.showToastShort(PreOrderActivity.this, "请选择要同意的雇员");
                    }

                }


                break;
        }
    }

    /**
     * 同意接单
     */
    private void argree(String nextStatusCode, String operaStatus) {
        final Dialog loadingDialog = DialogUtils.showLoadingDialog(this);
        Map<String, Object> map = BaseMapUtils.getMap(this);
        StringBuffer stringBuffer = new StringBuffer();
        List<Boolean> bool = preOrderAdapter.getBool();
        if (bool.size() == 0) {
            ToastUtils.showToastShort(this, "没有选择订单");
            return;
        }
        for (int i = 0; i < bool.size(); i++) {
            if (bool.get(i)) {
                stringBuffer.append(mStatusListBean.list.get(i).businessNumber + ",");//添加已经选择的businessBumber
            }
        }
        map.put("businessNumber", stringBuffer.toString());
        map.put("nextStatusCode", nextStatusCode);
        map.put("operaStatus", operaStatus);
        map.put("format", "json");
        DataUtils.loadData(this, GetDataConfing.BATCH_MODIFICATION, map, 1, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                loadingDialog.dismiss();
                if (dataRequest.isNetError()) {
                    ShowGetDataError.showNetError(PreOrderActivity.this);
                } else {
                    String json = dataRequest.getResponseData();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if ("0".equals(jsonObject.optString("code"))) {
                            ToastUtils.showToastShort(PreOrderActivity.this, jsonObject.optString("msg"));
                            finish();
                        } else {
                            ToastUtils.showToastShort(PreOrderActivity.this, jsonObject.optString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onLoadMore() {
        refresh.loadMoreComplete();
//        //刷新完自动收起
        refresh.refreshComplete();
    }

    @Override
    public void onRefresh() {
        refresh.setLoadMoreEnabled(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh.refreshComplete();
                refresh.setLoadMoreEnabled(false);
            }
        }, 300);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPhone(phone);
                } else {
                    ToastUtils.showToastShort(this, "未获取权限");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == 200) {
            EventBus.getDefault().post(new RefreshEvent("", RefreshEvent.MINE_PAGE_DATA));
            finish();
        }
    }
}
