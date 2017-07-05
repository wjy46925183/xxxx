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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.adapter.FinishOrderAdapter;
import gongren.com.dlg.javabean.master.masterlist.OrderStatusListBean;
import gongren.com.dlg.utils.ToastUtils;

/**
 * 雇主，我的需求，已完成的订单详情页
 */
public class FinishOrderActivity extends BaseActivity implements CanRefreshLayout.OnRefreshListener,
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

    private FinishOrderAdapter doingOrderAdapter;
    private OrderStatusListBean mStatusListBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doing_order_detail);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            mStatusListBean = (OrderStatusListBean) getIntent().getSerializableExtra("bean");
            if (mStatusListBean == null || mStatusListBean.list == null || mStatusListBean.list.size() == 0) {
                tvBg.setVisibility(View.VISIBLE);
            }
            tvTitle.setText("已完成");
            tvZhiwei.setText(getIntent().getStringExtra("postName") + " " + getIntent().getStringExtra("salary") + "");
        }
        initView();
    }

    private void initView() {
        ivRight.setVisibility(View.INVISIBLE);
        canContentView.setEmptyView(tvBg);
        if (mStatusListBean.status == 50) {
            tvTitle.setText("已取消");
            doingOrderAdapter = new FinishOrderAdapter(this, mStatusListBean.list, mStatusListBean.status, mListener);
        } else {
            doingOrderAdapter = new FinishOrderAdapter(this, mStatusListBean.list, mListener);
        }

        canContentView.setAdapter(doingOrderAdapter);
        initListener();

        canContentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (mStatusListBean.status == 50) {
                    Intent intent = new Intent(FinishOrderActivity.this, MasterDetailsActivity.class);
                    intent.putExtra("businessNumber", mStatusListBean.list.get(position).businessNumber);//订单业务编号
                    startActivityForResult(intent, 200);
//                }
            }
        });
    }

    String phone;
    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pingfen_btn://评分
                    break;
                case R.id.call_btn://拨号
                    phone = (String) v.getTag();
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(FinishOrderActivity.this, Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(FinishOrderActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
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

    private void callPhone(String phone) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }


    /**
     * 初始化监听
     */
    private void initListener() {
        refresh.setOnRefreshListener(this);
        refresh.setOnLoadMoreListener(this);
    }

    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200 && resultCode == 200){
            finish();
        }
    }

    @Override
    public void onRefresh() {
        refresh.refreshComplete();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
