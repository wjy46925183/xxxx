package gongren.com.dlg.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.fragment.WorkerFragment;

/**
 * Created by Administrator on 2017/4/20 0020.
 */

public class WorkOrderDetailActivity extends BaseActivity {

    private String businessNumber;//订单业务编号

    @Bind(R.id.fragment_content)
    FrameLayout fragmentContentView;
    @Bind(R.id.workerorder_title)
    TextView tv_tital;
    @Bind(R.id.worker_detail_loading)
    ProgressBar pro_loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_task_detail_layout);
        ButterKnife.bind(this);
        businessNumber = getIntent().getStringExtra("businessNumber");
        if (businessNumber == null || businessNumber.length() == 0) {
            finish();
            return;
        }
        showWorkTaskFragment();
    }

    private void showWorkTaskFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        WorkerFragment workerFragment = new WorkerFragment();
        workerFragment.setTital(tv_tital);
        workerFragment.setBusinessNumber(businessNumber);
        fragmentTransaction.add(R.id.fragment_content, workerFragment);
        fragmentTransaction.commit();
    }

    public static void open(Context context, String businessNumber) {
        Intent intent = new Intent();
        intent.setClass(context, WorkOrderDetailActivity.class);
        intent.putExtra("businessNumber", businessNumber);
        context.startActivity(intent);
    }

    @OnClick(R.id.workerorder_back)
    public void onClick() {
        finish();
    }

    /**
     * 开启雇员订单加载
     */
    public void startLoadingWorker(){
        pro_loading.setVisibility(View.VISIBLE);
    }
    /**
     * 关闭雇员订单加载
     */
    public void stopLoadingWorker(){
        pro_loading.setVisibility(View.GONE);
    }
}
