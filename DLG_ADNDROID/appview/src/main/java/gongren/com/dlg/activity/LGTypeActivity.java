package gongren.com.dlg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aym.util.json.JsonMap;
import aym.util.json.JsonParseHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.adapter.SelectLGTypeAdapter;
import gongren.com.dlg.javabean.SelectData;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.LogUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 零工类型选择页
 * Created by Administrator on 2017/3/30.
 */
public class LGTypeActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    @Bind(R.id.listview)
    ListView listview;

    private SelectLGTypeAdapter selectLGTypeAdapter;
    private List<SelectData> lgtypeList = new ArrayList<>();
    private List<JsonMap<String, Object>> dataList = new ArrayList<>();   //数据源
    private Handler handler = null;
    private static final int TAG_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_lg_type);
        ButterKnife.bind(this);
        handler = new Handler();
        initDatas();
        initViews();

    }

    public void getParams() {
        Intent intent = getIntent();
        String current_select = null;
        if (intent != null) {
            current_select = intent.getStringExtra("current_select");
        }

        if (!DataUtils.isNullStr(current_select)) {
            for (int i = 0; i < lgtypeList.size(); i++) {
                SelectData data = lgtypeList.get(i);
                if (current_select.equals(data.getName())) {
                    data.setCheched(true);
                    lgtypeList.set(i, data);
                    break;
                }
            }
        }
    }

    private void initDatas() {
        tvTitle.setText("选择零工类型");
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("groupCode", "job.type");
        DataUtils.loadData(context, GetDataConfing.dictionaryRest, map, TAG_REQUEST, responseDataCallback);
    }

    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            //            if (canContentView != null) {
            if (dataRequest.isNetError()) {
                ShowGetDataError.showNetError(context);
            } else {
                String json = dataRequest.getResponseData();
                if (!TextUtils.isEmpty(json)) {
                    if (dataRequest.getWhat() == TAG_REQUEST) {
                        LogUtils.logD("zq", json);

                        dataList = JsonParseHelper.getJsonMap_List_JsonMap(json, "data");
                        for (int i = 0; i < dataList.size(); i++) {
                            JsonMap<String, Object> jsonMap = dataList.get(i);
                            SelectData selectData = new SelectData();
                            selectData.setId(jsonMap.getString("id"));
                            selectData.setGroupCode(jsonMap.getString("groupCode"));
                            selectData.setDataCode(jsonMap.getString("dataCode"));
                            selectData.setDataName(jsonMap.getString("dataName"));
                            selectData.setDataValue(jsonMap.getString("dataValue"));
                            selectData.setDataWeight(jsonMap.getString("dataWeight"));
                            selectData.setSpecialIdentification(jsonMap.getString("specialIdentification"));
                            selectData.setCode(jsonMap.getString("code"));
                            lgtypeList.add(selectData);
                        }
//                        selectLGTypeAdapter.refreshDatas(lgtypeList, 1);
                        selectLGTypeAdapter.setDataBean(lgtypeList);
                        getParams();
                    }
                }
            }
        }
        //        }
    };

    private void initViews() {

        selectLGTypeAdapter = new SelectLGTypeAdapter(context, lgtypeList, adpterListener);
        listview.setAdapter(selectLGTypeAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lgtypeSelect(position);
            }
        });
    }

    private View.OnClickListener adpterListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.checkbox://选中按钮
                    int pos = (int) v.getTag();
                    lgtypeSelect(pos);
                    break;
                default:
                    break;
            }
        }
    };

    private Intent intent;

    public void lgtypeSelect(final int pos) {
        for (int i = 0; i < lgtypeList.size(); i++) {
            SelectData data = lgtypeList.get(i);
            data.setCheched(false);
            lgtypeList.set(i, data);
        }

        final SelectData data = lgtypeList.get(pos);
        data.setCheched(true);
        lgtypeList.set(pos, data);
        selectLGTypeAdapter.notifyDataSetChanged();
        Intent intent = new Intent();
        intent.putExtra("select_type", data);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.iv_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
