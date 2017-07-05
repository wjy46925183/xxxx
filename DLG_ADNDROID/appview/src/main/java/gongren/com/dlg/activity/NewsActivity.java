package gongren.com.dlg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import aym.util.json.JsonParseHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.javabean.UserMessageModel;
import gongren.com.dlg.utils.IntegerUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 消息
 */
public class NewsActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;

    @Bind(R.id.tv_save)
    TextView tvSave;
    @Bind(R.id.ll_system_notice)
    LinearLayout llSystemNotice;//系统通知
    @Bind(R.id.img_system_notice_show)
    ImageView imgSystemNoticeShow;
    @Bind(R.id.ll_activity_extension)
    LinearLayout llActivityExtension;//活动推广
    @Bind(R.id.img_activity_extension_show)
    ImageView imgActivityExtensionShow;
    @Bind(R.id.tv_activity_extension_content)
    TextView tv_activity_extension_content;
    @Bind(R.id.ll_job_recom)
    LinearLayout llJobRecom;//零工推荐
    @Bind(R.id.img_job_recom_show)
    ImageView img_job_recom_show;
    @Bind(R.id.tv_job_recom_content)
    TextView tv_job_recom_content;

    private UserMessageModel userMessageModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initModel();
    }

    private void initView() {
        tvTitle.setText("消息");
        tvSave.setVisibility(View.GONE);
        if (getIntent().getBooleanExtra("userRole",true)) {
            //雇员
//            llJobRecom.setVisibility(View.VISIBLE);
        } else {
            //雇主
            llJobRecom.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.iv_back, R.id.ll_system_notice, R.id.ll_activity_extension, R.id.ll_job_recom})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.ll_system_notice:
                //系统通知
                startActivity(new Intent(context, SystemNewsActivity.class));
                break;

            case R.id.ll_activity_extension:
                //活动推广
                startActivity(new Intent(context, ActivityExtensionActivity.class));
                break;

            case R.id.ll_job_recom:
                //  零工推荐
//                startActivity(new Intent(context, JobRecomActivity.class));
                break;
        }
    }

    private void initModel() {
        if(userMessageModel == null) {
            userMessageModel = new UserMessageModel();
        }
        userMessageModel.getMessageListByType(this, IntegerUtils.API_SYSTEM_MESSAGE,"0",0,1,responseDataCallback);
        userMessageModel.getMessageListByType(this, IntegerUtils.API_ACTIVITY_MESSAGE,"0",0,1,responseDataCallback);
        if (getIntent().getBooleanExtra("userRole",true)) {
            userMessageModel.getMessageListByType(this, IntegerUtils.API_RECOMMEND_MESSAGE,"0",0,1,responseDataCallback);
        }
    }

    /**
     * 系统消息返回，判断是否有未读数据，有展示小红点
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            if (dataRequest.isNetError()) {
                ShowGetDataError.showNetError(context);
            } else {
                String json = dataRequest.getResponseData();
                if (!TextUtils.isEmpty(json)) {
                    switch (dataRequest.getWhat()){
                        case IntegerUtils.API_SYSTEM_MESSAGE:
                            if (JsonParseHelper.getJsonMap(json).getInt("total")>0)
                                imgSystemNoticeShow.setVisibility(View.VISIBLE);
                             else
                                imgSystemNoticeShow.setVisibility(View.GONE);

                            break;

                        case IntegerUtils.API_ACTIVITY_MESSAGE:
                            if (JsonParseHelper.getJsonMap(json).getInt("total")>0)
                                imgActivityExtensionShow.setVisibility(View.VISIBLE);
                             else
                                imgActivityExtensionShow.setVisibility(View.GONE);

                            break;

                        case IntegerUtils.API_RECOMMEND_MESSAGE:
                            if (JsonParseHelper.getJsonMap(json).getInt("total")>0)
                                img_job_recom_show.setVisibility(View.VISIBLE);
                             else
                                img_job_recom_show.setVisibility(View.GONE);

                            break;
                    }

                }
            }
        }
    };
}
