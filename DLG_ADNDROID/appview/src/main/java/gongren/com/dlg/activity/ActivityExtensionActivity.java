package gongren.com.dlg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import aym.util.json.JsonMap;
import aym.util.json.JsonParseHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.adapter.ActivityExtensionAdapter;
import gongren.com.dlg.javabean.UserMessageModel;
import gongren.com.dlg.utils.IntegerUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 活动推广
 * liukui 2014/01/24
 */
public class ActivityExtensionActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_save)
    TextView tvSave;

    @Bind(R.id.can_content_view)
    ListView canContentView;
    @Bind(R.id.refresh)
    CanRefreshLayout refresh;

    private int currentPage = 0;
    private List<JsonMap<String, Object>> dataList = new ArrayList<>();   //数据源
    private ActivityExtensionAdapter activityExtensionAdapter;

    private UserMessageModel userMessageModel;
    private int minId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_news);

        ButterKnife.bind(this);
        initView();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case IntegerUtils.MESSAGE_LIST_DETAIL:
                    JsonMap<String, Object> jsonMap = msg.obj instanceof JsonMap?(JsonMap<String, Object>)msg.obj:null;
                    userMessageModel.getMessageListDeatil(context ,jsonMap.getString("id"),IntegerUtils.API_MESSAGE_LISTDETAIL,responseDataCallback);
                    break;
            }
        }
    };

    private void initView() {
        tvTitle.setText("活动推广");
        tvSave.setVisibility(View.GONE);
        activityExtensionAdapter = new ActivityExtensionAdapter(context, dataList,handler);
        canContentView.setAdapter(activityExtensionAdapter);
        canContentView.addHeaderView(View.inflate(context,R.layout.list_header_gray,null));

        //下拉刷新监听
        refresh.setOnRefreshListener(new CanRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                minId = 0;
                currentPage = 1;
                refresh.setLoadMoreEnabled(true);
                refresh.refreshComplete();   //刷新完成
                addData();
            }
        });

        //上拉加载监听
        refresh.setOnLoadMoreListener(new CanRefreshLayout.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                currentPage++;
                refresh.loadMoreComplete();   //加载完成
                addData();
            }
        });

        refresh.autoRefresh();

        if(userMessageModel == null) {
            userMessageModel = new UserMessageModel();
        }
    }

    private void addData() {
        userMessageModel.getMessageListByType(this, IntegerUtils.API_ACTIVITY_MESSAGE,null,minId,10,responseDataCallback);
    }

    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            if (canContentView != null) {
                if (dataRequest.isNetError()) {
                    ShowGetDataError.showNetError(context);
                } else {
                    String json = dataRequest.getResponseData();
                    if (!TextUtils.isEmpty(json)) {
                        switch (dataRequest.getWhat()){
                            case IntegerUtils.API_ACTIVITY_MESSAGE:
                                dataList = JsonParseHelper.getJsonMap_List_JsonMap(json, "data");
                                if (dataList == null || dataList.size() < 1) {
                                    dataList = new ArrayList<>();
                                    refresh.setLoadMoreEnabled(false);
                                }
                                if (dataList.isEmpty()) {
                                    minId = dataList.get(dataList.size() - 1).getInt("id");
                                }
                                //刷新数据
                                activityExtensionAdapter.refreshDatas(dataList, currentPage);
                            break;

                            case IntegerUtils.API_MESSAGE_LISTDETAIL:
                                //消息列表详情
                                List<JsonMap<String, Object>> listJson = JsonParseHelper.getJsonMap_List_JsonMap(json, "data");
                                if(!listJson.isEmpty()){
                                    userMessageModel.getMessageDeatil(context ,listJson.get(0).getString("messageId"),IntegerUtils.API_MESSAGE_DETAIL,responseDataCallback);
                                }else
                                    ToastUtils.showToastShort(context,"未查询到数据");
                                break;

                            case IntegerUtils.API_MESSAGE_DETAIL:
                                //消息列表详情
                                List<JsonMap<String, Object>> mesgDetail = JsonParseHelper.getJsonMap_List_JsonMap(json, "data");
                                if(!mesgDetail.isEmpty()){
                                    JsonMap<String, Object> jsonMap = mesgDetail.get(0);
                                    Intent agreementIntent = new Intent(context, WebUtilsActivity.class);
                                    agreementIntent.putExtra("functionTitle",jsonMap.getString("title"));
                                    agreementIntent.putExtra("contentUrl",jsonMap.getString("messageUrl"));
                                    startActivity(agreementIntent);
                                }else
                                    ToastUtils.showToastShort(context,"参数提交异常");
                                break;
                        }
                    }
                }
            }
        }
    };

    @OnClick(R.id.iv_back)
    public void onClick(View v) {
        finish();
    }
}
