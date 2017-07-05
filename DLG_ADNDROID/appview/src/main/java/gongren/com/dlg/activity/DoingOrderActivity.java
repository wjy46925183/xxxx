package gongren.com.dlg.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.canyinghao.canrefresh.classic.FooterRefreshView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.adapter.DoingOrderAdapter;
import gongren.com.dlg.javabean.DoingOrderData;
import gongren.com.dlg.javabean.RefreshEvent;
import gongren.com.dlg.javabean.master.masterlist.OrderStatusListBean;
import gongren.com.dlg.utils.ToastUtils;

/**
 * 雇主，我的需求，正在进行的订单详情页
 */
public class DoingOrderActivity extends BaseActivity implements CanRefreshLayout.OnRefreshListener,
        CanRefreshLayout.OnLoadMoreListener {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_zhiwei)
    TextView tvZhiwei;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    @Bind(R.id.can_content_view)
    SwipeMenuListView canContentView;
    @Bind(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @Bind(R.id.can_refresh_footer)
    FooterRefreshView canRefreshFooter;
    @Bind(R.id.refresh)
    CanRefreshLayout refresh;
    @Bind(R.id.tv_bg)
    ImageView tvBg;
    private DoingOrderAdapter doingOrderAdapter;
    private List<DoingOrderData> mList = new ArrayList<>();
    private OrderStatusListBean mStatusListBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doing_order_detail);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            mStatusListBean = (OrderStatusListBean) getIntent().getSerializableExtra("bean");
            if (mStatusListBean.list.size() == 0) {
                tvBg.setVisibility(View.VISIBLE);
            }
            tvTitle.setText("正在进行中");
            tvZhiwei.setText(getIntent().getStringExtra("postName") + " " + getIntent().getStringExtra("salary") + "");
            initView();
        }
    }

    private void initView() {
        ivRight.setVisibility(View.INVISIBLE);

        doingOrderAdapter = new DoingOrderAdapter(this, mStatusListBean,mListener);
        canContentView.setAdapter(doingOrderAdapter);
        initListener();

        canContentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DoingOrderActivity.this, MasterDetailsActivity.class);
                intent.putExtra("businessNumber", mStatusListBean.list.get(position).businessNumber);//订单业务编号
                startActivityForResult(intent,200);
            }
        });
    }

    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.call_btn://拨号
                    phone = (String) v.getTag();
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(DoingOrderActivity.this, Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(DoingOrderActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
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

    private void go2CancleOrder(int flag) {
        Intent intent = new Intent(context, CancleOrderActivity.class);
        intent.putExtra("isfrom", flag);//1-不扣钱的取消，2-扣钱的取消。
        startActivity(intent);
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        refresh.setOnRefreshListener(this);
        refresh.setOnLoadMoreListener(this);
    }

    private void initDatas() {

        List<DoingOrderData> tempList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            DoingOrderData data = new DoingOrderData();
            data.setStatus(i + 1);
            tempList.add(data);
        }
        mList.addAll(tempList);
    }

    private String phone;

    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onLoadMore() {
        refresh.loadMoreComplete();
        //刷新完自动收起
        refresh.refreshComplete();
    }

    @Override
    public void onRefresh() {
        refresh.setLoadMoreEnabled(true);
        refresh.refreshComplete();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200 && resultCode == 200){
            EventBus.getDefault().post(new RefreshEvent("",RefreshEvent.MINE_PAGE_DATA));
            finish();
        }
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
}
