package gongren.com.dlg.fragment.odddetail;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import gongren.com.dlg.R;
import gongren.com.dlg.utils.ToastUtils;

/**
 * Created by lenovo on 2017/4/13.
 * 雇员的订单详情 地图显示及弹框内容逻辑
 */

public class EmployFragment extends Fragment {
    public final static int WAIT_AGREE= 1;//等待同意 弹框
    public final static int WAITING_START = 2;//等待开工 弹框
    public final static int WAIT_PAY = 3;//代收款 弹框
    public final static int IN_WORK = 4;//已完成 弹框
    public final static int EMPLOYER_INVITATION = 5;//雇主邀请 弹框
    public final static int HAS_START = 6;//已开工 弹框
    public final static int WORKING = 7;//正在干活中 弹框

    @Bind(R.id.workerorder_tv_count)
    TextView mWorkerorderTvCount;
    @Bind(R.id.workerorder_tv_name)
    TextView mWorkerorderTvName;
    @Bind(R.id.workerorder_tv_chengdu)
    TextView mWorkerorderTvChengdu;
    @Bind(R.id.workerorder_tv_position)
    TextView mWorkerorderTvPosition;
    @Bind(R.id.workerorder_layout_item)
    LinearLayout mWorkerorderLayoutItem;
    @Bind(R.id.workerorder_iv_head)
    CircleImageView mWorkerorderIvHead;
    @Bind(R.id.workerorder_tvdetail)
    TextView mWorkerorderTvdetail;
    @Bind(R.id.workerorder_layout_detail)
    LinearLayout mWorkerorderLayoutDetail;
    @Bind(R.id.workerorder_layout_cancle)
    LinearLayout mWorkerorderLayoutCancle;
    @Bind(R.id.workerorder_layout_share)
    LinearLayout mWorkerorderLayoutShare;
    @Bind(R.id.workerorder_layout_help)
    LinearLayout mWorkerorderLayoutHelp;
    @Bind(R.id.workerorder_layout_active)
    LinearLayout mWorkerorderLayoutActive;
    @Bind(R.id.tv_active)
    TextView tv_active;
    @Bind(R.id.tv_cancle)
    TextView tv_cancle;
    @Bind(R.id.tv_share)
    TextView tv_share;
    @Bind(R.id.tv_description)
    TextView tv_description;
    private FragmentActivity mFragmentActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mFragmentActivity = (FragmentActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_employ, null);
        ButterKnife.bind(this, inflate);
        mWorkerorderLayoutActive.setClickable(false);//不能点击抢单
        return inflate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.workerorder_layout_cancle, R.id.workerorder_layout_share, R.id.workerorder_layout_help, R.id.workerorder_layout_active})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.workerorder_layout_cancle:
                ToastUtils.showToastShort(mFragmentActivity,"取消");
                break;
            case R.id.workerorder_layout_share:
                ToastUtils.showToastShort(mFragmentActivity,"分享");
                break;
            case R.id.workerorder_layout_help:
                ToastUtils.showToastShort(mFragmentActivity,"帮助");
                break;
            case R.id.workerorder_layout_active:
                ToastUtils.showToastShort(mFragmentActivity,"抢单");


                break;
        }
    }
    /**
     * 雇员的弹框情况
     */
    public void checkDialogType(int dialogType, TextView tv_title) {
        switch (dialogType){
            case WAIT_AGREE://等待同意 雇员抢单
                tv_title.setText("等待雇主同意");
                break;
            case WAITING_START://等待开工
                tv_title.setText("等待开工");
                tv_active.setText("开工打卡");
                mWorkerorderTvCount.setVisibility(View.VISIBLE);
                break;
            case WAIT_PAY://代付款
                tv_title.setText("代收款");
                tv_active.setText("再来一单");
                mWorkerorderLayoutActive.setClickable(true);
                mWorkerorderLayoutActive.setBackgroundColor(Color.parseColor("#ff9752"));
                mWorkerorderLayoutCancle.setVisibility(View.GONE);
                mWorkerorderLayoutDetail.setVisibility(View.VISIBLE);
                tv_description.setText("报酬：200/天");
                mWorkerorderTvdetail.setText("等待雇主支付");
                break;
            case IN_WORK://已完工
                tv_title.setText("已完工");
                tv_cancle.setText("评价");
                mWorkerorderLayoutActive.setClickable(true);
                tv_active.setText("再来一单");
                mWorkerorderLayoutActive.setBackgroundColor(Color.parseColor("#ff9752"));
                mWorkerorderLayoutDetail.setVisibility(View.VISIBLE);
                tv_description.setText("报酬：200/天");
                mWorkerorderTvdetail.setText("雇主已支付");
                mWorkerorderTvdetail.setTextColor(Color.parseColor("#ff9752"));
                tv_cancle.setTextColor(Color.parseColor("#ff9752"));
                break;
            case EMPLOYER_INVITATION://雇主邀请
                tv_title.setText("雇主邀请");
                mWorkerorderTvCount.setVisibility(View.VISIBLE);
                mWorkerorderLayoutActive.setVisibility(View.GONE);
                mWorkerorderLayoutHelp.setVisibility(View.GONE);
                mWorkerorderLayoutShare.setBackgroundColor(Color.parseColor("#ff9752"));
                tv_share.setText("接受邀请");
                tv_share.setTextColor(Color.WHITE);
                tv_cancle.setText("拒绝");
                break;
            case HAS_START://已开工，里面要判断是否到了开工打卡的范围之内
                tv_title.setText("已开工");
                tv_active.setText("开工打卡");
                mWorkerorderTvCount.setVisibility(View.VISIBLE);
                break;
            case WORKING://正在干活中 里面要判断是否到了收工打卡的范围之内
                tv_title.setText("正在干活中");
                tv_active.setText("收工打卡");
                mWorkerorderTvCount.setVisibility(View.VISIBLE);
                break;
        }
    }
}
