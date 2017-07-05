package gongren.com.dlg.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.umeng.socialize.UMShareAPI;

import butterknife.Bind;
import butterknife.ButterKnife;
import gongren.com.dlg.R;
import gongren.com.dlg.fragment.WorkerFragment;

/**
 * Created by Administrator on 2017/4/20 0020.
 */

public class WorkTaskActivity extends BaseActivity{

    private String businessNumber;//订单业务编号

    @Bind(R.id.fragment_content)
    FrameLayout fragmentContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_task_detail_layout);
        ButterKnife.bind(this);
        businessNumber=getIntent().getStringExtra("businessNumber");
        if(businessNumber==null||businessNumber.length()==0){
            finish();
            return;
        }
        showWorkTaskFragment();
    }

    private void showWorkTaskFragment(){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

        WorkerFragment workerFragment=new WorkerFragment();
        workerFragment.setBusinessNumber(businessNumber);
        fragmentTransaction.add(R.id.fragment_content,workerFragment);
    }

    public static void openWorkTaskDetailView(Context context,String businessNumber){
        Intent intent=new Intent();
        intent.setClass(context,WorkTaskActivity.class);
        intent.putExtra("businessNumber",businessNumber);
        context.startActivity(intent);
    }



    /**
     * 友盟分享，若果在fragment中实现分享，需要在activity中进行调用这个
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }
}
