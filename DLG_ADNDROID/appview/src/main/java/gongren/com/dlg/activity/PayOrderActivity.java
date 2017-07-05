package gongren.com.dlg.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.canyinghao.canrefresh.classic.FooterRefreshView;
import com.common.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.adapter.PayOrderAdapter;
import gongren.com.dlg.javabean.FinishEvent;
import gongren.com.dlg.javabean.PayData;
import gongren.com.dlg.javabean.master.masterlist.OrderStatusListBean;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.ToastUtils;

/**
 * 雇主，我的需求，待付款的订单详情页
 */
public class PayOrderActivity extends BaseEditActivity implements CanRefreshLayout.OnRefreshListener,
        CanRefreshLayout.OnLoadMoreListener, PayOrderAdapter.PayMoneyCallBack {


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
    @Bind(R.id.top_panel)
    ImageView topPanel;
    @Bind(R.id.income_text)
    TextView incomeText;

    @Bind(R.id.total_text)
    TextView totalText;
    @Bind(R.id.slid_linear)
    LinearLayout slidLinear;
    @Bind(R.id.checkbox)
    CheckBox checkbox;
    @Bind(R.id.totals_text)
    TextView totalsText;
    @Bind(R.id.bottom_re)
    RelativeLayout bottomRe;
    @Bind(R.id.pay_btn)
    Button payBtn;

    @Bind(R.id.tv_bg)
    ImageView tvBg;
    @Bind(R.id.bottom_panel)
    ImageView bottomPanel;
    @Bind(R.id.xiaofei_text)
    TextView xiaofeiText;
    @Bind(R.id.baoxian_text)
    TextView baoxianText;
    @Bind(R.id.detail_layout)
    LinearLayout detailLayout;
    @Bind(R.id.bao_linear)
    RelativeLayout baoLinear;
    private int lastX, lastY;
    private int chuY;//初始的底部Y轴
    private int chuTY;//初始的底部Y轴
    private int viewHeight;//控件的高度
    private DisplayMetrics dm;
    private float xiaofei_bili = 0.06f;
    private PayOrderAdapter doingOrderAdapter;
    private int mPay_everyday;
    private int mbaofei;
    private OrderStatusListBean mStatusListBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_order_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        dm = getResources().getDisplayMetrics();
        if (getIntent() != null) {
            mStatusListBean = (OrderStatusListBean) getIntent().getSerializableExtra("bean");
            if (mStatusListBean == null || mStatusListBean.list == null || mStatusListBean.list.size() == 0) {
                tvBg.setVisibility(View.VISIBLE);
            }


            /*String logo = getIntent().getStringExtra("logo");
            if(!StringUtils.isEmpty(logo)){
                Glide.with(this).load(logo).fitCenter()
                        .override(150, 150)
                        .error(R.mipmap.ic_launcher).into(iv_head);
            }*/
        }
        initView();
        initDatas();
    }

    //下级Fragment发来的消息
    @Subscribe
    public void onMessageEvent(FinishEvent event) {
        finish();
    }

    private void initView() {
        mPay_everyday = getIntent().getIntExtra("pay_everyday", 0);
        mbaofei = getIntent().getIntExtra("baoxian_money", 0);
        tvZhiwei.setText(getIntent().getStringExtra("postName") + " " + getIntent().getStringExtra("salary") + "");
        if (mbaofei == 2){
            baoLinear.setVisibility(View.VISIBLE);
        } else {
            baoLinear.setVisibility(View.GONE);
        }

        tvTitle.setText("待付款");
        ivRight.setVisibility(View.INVISIBLE);

        canContentView.setEmptyView(tvBg);

        doingOrderAdapter = new PayOrderAdapter(this, mStatusListBean, checkbox, this, mListener);
        canContentView.setAdapter(doingOrderAdapter);
        canContentView.setClickable(true);

        initListener();

        initChouTi();

    }

    public void initChouTi() {
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) slid_linear.getLayoutParams();
        viewHeight = getResources().getDimensionPixelSize(R.dimen.px_375);
        final int f154 = getResources().getDimensionPixelSize(R.dimen.px_f154);
        //最大的bottom值
        chuY = dm.heightPixels - f154;
        //最小的top值
        chuTY = (dm.heightPixels - viewHeight - getResources().getDimensionPixelSize(R.dimen.px_88));

        slidLinear.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int ea = event.getAction();
                final int screenWidth = dm.widthPixels;
                final int screenHeight = dm.heightPixels;
                switch (v.getId()) {
                    case R.id.slid_linear:
                        switch (ea) {
                            case MotionEvent.ACTION_DOWN:
                                lastX = (int) event.getRawX();// 获取触摸事件触摸位置的原始X坐标
                                lastY = (int) event.getRawY();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                int dx = (int) event.getRawX() - lastX;
                                int dy = (int) event.getRawY() - lastY;

                                int l = v.getLeft() + dx;
                                int b = v.getBottom() + dy;
                                int r = v.getRight() + dx;
                                int t = v.getTop() + dy;
                                // 下面判断移动是否超出屏幕
                                if (l < 0) {
                                    l = 0;
                                    r = l + v.getWidth();
                                }
                                if (t < chuTY) {
                                    t = chuTY;
                                    b = t + viewHeight;
                                }
                                if (r > screenWidth) {
                                    r = screenWidth;
                                    l = r - v.getWidth();
                                }
                                if (b > chuY) {
                                    b = chuY;
                                    t = b - viewHeight;
                                }
                                Log.w("测试向上滑动", "t=" + t + ",b=" + b + ",chuY=" + chuY + ",chuTY=" + chuTY);
                                Log.w("测试向上滑动", "viewHeight=" + viewHeight);
                                v.layout(l, t, r, b);
                                lastX = (int) event.getRawX();
                                lastY = (int) event.getRawY();
                                v.postInvalidate();

                                //切换箭头
                                int scrollY = dm.heightPixels - f154;
                                if (b < scrollY) {
                                    topPanel.setImageResource(R.mipmap.xiajiantou);
                                } else {
                                    topPanel.setImageResource(R.mipmap.shangjiantou);
                                }
                                break;
                            case MotionEvent.ACTION_UP:
                                break;
                            default:
                                break;
                        }
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private String phone;
    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.call_btn://拨号
                    phone = (String) v.getTag();
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(PayOrderActivity.this, Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(PayOrderActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
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

    private int current_index = 0;
    private double tip;
    private double pay;
    private String orders;
    private double bao;

    //报酬
    @Override
    public void pay(double pay) {
        this.pay = pay;
    }

    //保险费
    @Override
    public void bao(double bao) {
        this.bao = bao;
    }

    //小费
    @Override
    public void tip(double tip, String orders) {
        this.tip = tip;
        this.orders = orders;
    }
    //一共多少钱 加上服务费

    @Override
    public void total(double totalMoney) {
        incomeText.setText(pay + "元");
        totalsText.setText(String.format("%.2f", totalMoney) + "元");
        totalText.setText(String.format("%.2f", totalMoney) + "元");
        xiaofeiText.setText(String.format("%.2f", tip) + "元");
        baoxianText.setText(String.format("%.2f", bao) + "元");
    }


    public class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int xiaofei = DataUtils.string2Int(String.valueOf(s));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private View.OnFocusChangeListener mOnfocuschange = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                current_index = (int) v.getTag();
            }
        }
    };


    public void jisuan(List<PayData> list) {
        if (list == null || list.size() <= 0) {
            return;
        }

        int total_income = 0;//报酬
        int totalxiaofei = 0;
        double total_money = 0;//合计=报酬+小费
        double pay_total = 0;//总支付金额

        for (int i = 0; i < list.size(); i++) {
            PayData payData = list.get(i);
            if (payData.isCheched()) {
                total_income += payData.getIncome();
                totalxiaofei += payData.getXiaofei();
            }
        }

        total_money = total_income + totalxiaofei;
        pay_total = total_money;

        //给界面设置值
        totalsText.setText(DataUtils.formatDouble(pay_total) + "元");//支付总金额
        incomeText.setText(DataUtils.formatDouble(total_income) + "元");//报酬
        totalText.setText(DataUtils.formatDouble(total_money) + "元");//支付总金额
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        refresh.setOnRefreshListener(this);
        refresh.setOnLoadMoreListener(this);
    }

    private void initDatas() {

        List<PayData> tempList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            PayData data = new PayData();
            data.setName(i + "");
            data.setIncome(100);
            tempList.add(data);
        }
//        mList.addAll(tempList);

        //刷新数据
//        doingOrderAdapter.refreshDatas(tempList, currentpage);
    }

    @OnClick({R.id.iv_back, R.id.bottom_re, R.id.checkbox, R.id.top_panel, R.id.pay_btn, R.id.bottom_panel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.top_panel://点击箭头
                //showBottomDialog();
                topPanel.setVisibility(View.GONE);
                bottomPanel.setVisibility(View.VISIBLE);
                detailLayout.setVisibility(View.GONE);
                break;
            case R.id.checkbox:
                if (checkbox.isChecked()) {//全选
                    doingOrderAdapter.setAllCheck(1, checkbox);
                } else {//全取消
                    doingOrderAdapter.setAllCheck(2, checkbox);
                }
                break;
           /* case R.id.checkbox:
                break;*/
            case R.id.bottom_re://全选
                if (checkbox.isChecked()) {
                    checkbox.setChecked(false);
                } else {
                    checkbox.setChecked(true);
                }
                doingOrderAdapter.setAllCheck(checkbox.isChecked() ? 1 : 2, checkbox);
                break;
            case R.id.pay_btn://支付
                if (!checkbox.isChecked()) {
                    ToastUtils.showToastShort(getApplicationContext(), "请选择支付订单");
                    return;
                }
                go2Pay();
                break;
            case R.id.bottom_panel:
                bottomPanel.setVisibility(View.GONE);
                topPanel.setVisibility(View.VISIBLE);
                detailLayout.setVisibility(View.VISIBLE);
        }
    }

    private boolean isShow = false;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showBottomDialog() {
        detailLayout.setVisibility(View.GONE);


        /*int f88 = getResources().getDimensionPixelSize(R.dimen.px_88);
        int startY = dm.heightPixels - viewHeight - f88;
        int stopY = dm.heightPixels - viewHeight - getResources().getDimensionPixelSize(R.dimen.px_88);

        Log.w("测试点击", "startY=" + startY + ",stopY=" + stopY + ",dm.heightPixels=" + dm.heightPixels);
        Log.w("测试点击", "chuTY=" + chuTY);
        if (!isShow) {//显示
            isShow = true;
            topPanel.setImageResource(R.mipmap.xiajiantou);
            slidLinear.layout(0, stopY, dm.widthPixels, stopY + viewHeight);
        } else {//隐藏
            isShow = false;
            topPanel.setImageResource(R.mipmap.shangjiantou);
            slidLinear.layout(0, startY, dm.widthPixels, startY + viewHeight);
        }

//        slidLinear.postInvalidate();
        slidLinear.postInvalidateOnAnimation();*/
    }

    public void go2Pay() {
        if (SharedPreferencesUtils.getBoolean(this, SharedPreferencesUtils.havePayPwd)) {
            Log.e("paypwd", SharedPreferencesUtils.getBoolean(this, SharedPreferencesUtils.havePayPwd) + "");
            if (!totalsText.getText().toString().equals("0元")) {
                Intent intent = new Intent(context, PayordersActivity.class);
                intent.putExtra("isfrom", 1);
                intent.putExtra("pay", pay);
                intent.putExtra("tip", tip);
                intent.putExtra("orders", orders);
                intent.putExtra("bao", bao);
                startActivity(intent);
                finish();//去支付的时候 关闭页面
            } else {
                if (!checkbox.isChecked()) {
                    ToastUtils.showToastShort(this, "请选择要支付的雇员");
                }
            }
        } else {
            startActivity(new Intent(context, SetPayPasswordActivity.class).putExtra("type", 1));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
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
