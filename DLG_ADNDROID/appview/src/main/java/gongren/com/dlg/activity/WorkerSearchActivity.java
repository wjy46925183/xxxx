package gongren.com.dlg.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.adapter.WorkerSearcherAdapter;
import gongren.com.dlg.javabean.DictionaryJson;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.GsonUtils;
import gongren.com.dlg.utils.IntegerUtils;
import gongren.com.dlg.utils.LogUtils;
import gongren.com.dlg.utils.MySqliteOpenHelper;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

public class WorkerSearchActivity extends BaseActivity {

    private WorkerSearchActivity instance;

    @Bind(R.id.searcher_common_cancel)
    TextView tvCancel;
    @Bind(R.id.searcher_common_search)
    EditText etSearcher;
    @Bind(R.id.worker_searcher_gridview)
    GridView searcherContent;
    @Bind(R.id.work_search_title)
    TextView titleText;
    @Bind(R.id.work_search_clear)
    TextView clearText;
    @Bind(R.id.search_title)
    LinearLayout searchTitle;
    private WorkerSearcherAdapter workerSearcherAdapter;
    private List<DoingOrderData> historyList = new ArrayList<>();
    private List<DoingOrderData> hotList = new ArrayList<>();
    private final int TAG_REQUEST = 1;
    private DictionaryJson mDictionaryJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_search);
        initDatas();
        setDatas();

    }
    private void initDatas() {
        ButterKnife.bind(this);
        instance = this;
        historyList.clear();
        hotList.clear();
    }

    private void setDatas() {
        searchTitle.setVisibility(View.GONE);
        titleText.setText("历史搜索");
        clearText.setVisibility(View.VISIBLE);
        MySqliteOpenHelper mySqliteOpenHelper = new MySqliteOpenHelper(this);
        SQLiteDatabase readableDatabase = mySqliteOpenHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select * from search", null);
        while (cursor.moveToNext()){
            String searchName = cursor.getString(cursor.getColumnIndex("searchName"));
            String searchDate = cursor.getString(cursor.getColumnIndex("searchDate"));
            String postType = cursor.getString(cursor.getColumnIndex("postType"));
            DoingOrderData history = new DoingOrderData();
            history.setPostName(searchName);
            history.setPostType(postType);
            historyList.add(history);
        }
        readableDatabase.close();
        cursor.close();
        //补全一行
        if(historyList.size()>0){
            int num = historyList.size();
            for(int i=0;i<4 - num%4;i++){

                historyList.add(new DoingOrderData());
            }
        }
//
        //补全一行
        for(int i=0;i<3;i++){

            hotList.add(new DoingOrderData());
        }
        if(historyList.size()>0)
        {
            searchTitle.setVisibility(View.VISIBLE);
        }else {
            searchTitle.setVisibility(View.GONE);
        }
        workerSearcherAdapter = new WorkerSearcherAdapter(instance, R.layout.worker_search_title, R.layout.worker_search_item,historyList,hotList,handler);
        searcherContent.setAdapter(workerSearcherAdapter);

        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("groupCode", "job.type");
        map.put("sign", "1");
        DataUtils.loadData(context, GetDataConfing.COMMON_HOT_CRAFT, map, TAG_REQUEST, responseDataCallback);

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
                        mDictionaryJson = GsonUtils.jsonToBean(json, DictionaryJson.class);
                        if(mDictionaryJson.data!=null && mDictionaryJson.data.size()>0){
                            for (int i = 0; i< mDictionaryJson.data.size(); i++){
                                DoingOrderData hot = new DoingOrderData();
                                hot.setId(mDictionaryJson.data.get(i).id);
                                hot.setPostName(mDictionaryJson.data.get(i).dataName);
                                hot.setPostType(mDictionaryJson.data.get(i).dataCode);//"job.type_0"
                                hotList.add(hot);
                            }
                        }

                        workerSearcherAdapter.setData(historyList,hotList);
                        //刷新数据
//                        workerSearcherAdapter.registerDataSetObserver(dataList, currentPage);
                    }
                }
            }
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case IntegerUtils.SELECT_WORKER:
                    Intent intent= new Intent(context, SearchCommonActivity.class);
                    DoingOrderData doingOrderData = (DoingOrderData) msg.obj;
                    intent.putExtra("postType",doingOrderData.getPostType());
                    intent.putExtra("postName",doingOrderData.getPostName());
                    intent.putExtra("search",doingOrderData.getPostName());
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    private boolean flag = true;
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(flag&&event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
            flag = false;
            Intent intent= new Intent(context, SearchCommonActivity.class);
            intent.putExtra("search",etSearcher.getText().toString().trim());
            startActivity(intent);
            finish();
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

    @OnClick({R.id.searcher_common_cancel, R.id.work_search_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searcher_common_cancel:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                finish();
                break;

            case R.id.work_search_clear:
                MySqliteOpenHelper helper = new MySqliteOpenHelper(this);
                SQLiteDatabase db = helper.getReadableDatabase();
                db.delete("search",null,null);
                db.close();
                historyList.clear();
                searchTitle.setVisibility(View.GONE);
                workerSearcherAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
