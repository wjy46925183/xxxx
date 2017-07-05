package gongren.com.dlg.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.javabean.NewsInfoBean;
import gongren.com.dlg.utils.ActivityManager;
import com.common.utils.DateUtils;
import gongren.com.dlg.utils.GsonUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * Created by Administrator on 2017/5/19.
 */
public class NewsInfoActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_news_info_title)
    TextView tv_news_info_title;   //  标题
    @Bind(R.id.tv_news_info_time)
    TextView tv_news_info_time;    //时间
    @Bind(R.id.tv_news_info_content)
    TextView tv_news_info_content; //内容
    private static int tag = 1;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newinfo);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("系统消息");
        if (getIntent() != null) {
            String title = getIntent().getStringExtra("title");
            tvTitle.setText("系统消息");
            tv_news_info_title.setText(title);
            String content = getIntent().getStringExtra("content");
            tv_news_info_content.setText(content);
            double createTime = getIntent().getDoubleExtra("createTime", 0);
            tv_news_info_time.setText(DateUtils.getCurrentTime((long) createTime));

        }

//        dialog = DialogUtils.showLoadingDialog(context);
//        Map<String, Object> map = new HashMap<>();
//        map.put("id",messageId);
//        map.put("userId", SharedPreferencesUtils.getString(context,SharedPreferencesUtils.USERID));
//        map.put("format", "json");
//        DataUtils.loadData(context , GetDataConfing.messageListDetail,map,tag,responseDataCallback);
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        ActivityManager.getActivityManager().finishActivity(this);
    }

    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            if (tvTitle != null) {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (dataRequest.isNetError()) {
                    ShowGetDataError.showNetError(context);
                } else {
                    NewsInfoBean bean = GsonUtils.jsonToBean(dataRequest.getResponseData(), NewsInfoBean.class);
                    tv_news_info_title.setText(bean.getData().get(0).getTitle() + "");
                    tv_news_info_time.setText(DateUtils.getDatedian_yyyy_MM_dd(bean.getData().get(0).getCreateTime() + ""));
                    tv_news_info_content.setText(bean.getData().get(0).getContent() + "");
                }
            }
        }
    };

}
