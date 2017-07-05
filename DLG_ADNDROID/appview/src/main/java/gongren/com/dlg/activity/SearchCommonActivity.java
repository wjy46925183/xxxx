package gongren.com.dlg.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.canyinghao.canrefresh.classic.FooterRefreshView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aym.util.json.JsonMap;
import aym.util.json.JsonParseHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.adapter.SearcherCommonAdapter;
import gongren.com.dlg.utils.DataCacheUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.MySqliteOpenHelper;
import com.common.utils.SharedPreferencesUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/******
 * 公共搜索界面
 *
 */
public class SearchCommonActivity extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener {

    @Bind(R.id.searcher_common_cancel)
    TextView tvCancel;
    @Bind(R.id.searcher_common_search)
    EditText etSearcher;
    @Bind(R.id.can_content_view)
    SwipeMenuListView canContentView;
    @Bind(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @Bind(R.id.can_refresh_footer)
    FooterRefreshView canRefreshFooter;
    @Bind(R.id.refresh)
    CanRefreshLayout refresh;
    @Bind(R.id.searcher_common_nothing)
    ImageView tvBothing;

    private SearcherCommonAdapter searcherCommonAdapter;
    private List<DoingOrderData> mList = new ArrayList<>();
    private int currentPage = 0;
    private String conditionId;
    private Double x_longitude;
    private Double y_latitude;
    private static final int TAG_REQUEST = 1;
    private static final int TAG_REQUEST_SEARCH = 2;
    private List<JsonMap<String, Object>> dataList = new ArrayList<>();   //数据源
    private String search, postType, postName,data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_common);
        ButterKnife.bind(this);
        data=getIntent().getStringExtra("data");
        initView();
        try {
            search = getIntent().getStringExtra("search");
            postName = getIntent().getStringExtra("postName");
            postType = getIntent().getStringExtra("postType");
            etSearcher.setText(search);
            conditionId = getIntent().getStringExtra("conditionId").toLowerCase().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            x_longitude = getIntent().getDoubleExtra("x_longitude", 0.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            y_latitude = getIntent().getDoubleExtra("y_latitude", 0.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        refresh.autoRefresh();
    }

    private void initView() {
        tvBothing.setVisibility(View.GONE);
        if(!"data".equals(data)){
            tvBothing.setVisibility(View.VISIBLE);
            canContentView.setEmptyView(tvBothing);
        }
        searcherCommonAdapter = new SearcherCommonAdapter(SearchCommonActivity.this, dataList);
        canContentView.setAdapter(searcherCommonAdapter);

        initListener();
        canContentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchCommonActivity.this, WorkerFindOrderActivity.class);
                JsonMap<String,Object> jsonMap = dataList.get(position);

                intent.putExtra("id",jsonMap.getStringNoNull("id"));
                intent.putExtra("yCoordinate",jsonMap.getDouble("yCoordinate"));
                intent.putExtra("xCoordinate",jsonMap.getDouble("xCoordinate"));
                intent.putExtra("postTypeName",jsonMap.getStringNoNull("postTypeName"));
                intent.putExtra("postName",jsonMap.getStringNoNull("postName"));
                intent.putExtra("price",jsonMap.getStringNoNull("price"));
                intent.putExtra("jobMeterUnitName",jsonMap.getStringNoNull("jobMeterUnitName"));
                SearchCommonActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh.autoRefresh();
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        refresh.setOnRefreshListener(this);
        refresh.setOnLoadMoreListener(this);
    }

    private void initDatas() {
        Map<String, Object> map = new HashMap<>();
        map.put("xCoordinate", DataCacheUtils.getIstance().getCurrentLongitudeX());
        map.put("yCoordinate", DataCacheUtils.getIstance().getCurrentLatitudeY() + "");
        map.put("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));
        map.put("pageSize", "20");
        map.put("pageIndex", currentPage + "");
        if (TextUtils.isEmpty(conditionId)) {
            String searcherText = etSearcher.getText().toString().trim();
            boolean isRepeat = false;

            MySqliteOpenHelper mySqliteOpenHelper = new MySqliteOpenHelper(this);
            SQLiteDatabase readableDatabase = mySqliteOpenHelper.getReadableDatabase();
            Cursor cursor = readableDatabase.rawQuery("select * from search", null);
            while (cursor.moveToNext()) {
                String searchName = cursor.getString(cursor.getColumnIndex("searchName"));
                String searchDate = cursor.getString(cursor.getColumnIndex("searchDate"));
                if (cursor.isFirst() && cursor.getCount() > 12) {
                    readableDatabase.delete("search", "searchName = ?", new String[]{searchName});
                }
                if (searchName.equals(searcherText)) {
                    isRepeat = true;
                }
            }

            if (!TextUtils.isEmpty(searcherText) && !isRepeat) {
                MySqliteOpenHelper sqliteOpenHelper = new MySqliteOpenHelper(this);
                SQLiteDatabase writableDatabase = sqliteOpenHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("searchName", searcherText);
                values.put("searchDate", System.currentTimeMillis() + "");
                values.put("postType", postType);
                writableDatabase.insert("search", null, values);
            }
            map.put("postType", postType);

            map.put("demandType", "");
            if (TextUtils.isEmpty(postType)) {//为空的时候 证明是搜索的关键字 不是热门
                map.put("postType", "job.type_0");
                map.put("postName", etSearcher.getText().toString());
            } else {
                map.put("postName", "");//搜索的是热门 该名字为空
            }
            DataUtils.loadData(context, GetDataConfing.jobTaskRestJobTask, map, TAG_REQUEST_SEARCH, responseDataCallback);
        } else {
            map.put("conditionId", conditionId);
            map.put("demandType", "1");
            DataUtils.loadData(context, GetDataConfing.getJobTaskOfEmployeePage, map, TAG_REQUEST, responseDataCallback);
        }
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
                        List<JsonMap<String, Object>> datas = JsonParseHelper.getJsonMap_List_JsonMap(json, "data");
                        if (isRefreshing) {
                            dataList.clear();
                            isRefreshing = false;
                            if(null == datas || datas.size() == 0){
                                tvBothing.setVisibility(View.GONE);
                                ToastUtils.showToastShort(SearchCommonActivity.this,"暂无数据");
                            }
                        }
                        dataList.addAll(datas);
                        //刷新数据
                        searcherCommonAdapter.notifyDataSetChanged();
                    } else if (dataRequest.getWhat() == TAG_REQUEST_SEARCH) {

                        List<JsonMap<String, Object>> datas = JsonParseHelper.getJsonMap_List_JsonMap(json, "data");
                        if (isRefreshing) {
                            dataList.clear();
                            isRefreshing = false;
                            if(null == datas || datas.size() == 0){
                                tvBothing.setVisibility(View.GONE);
                                ToastUtils.showToastShort(SearchCommonActivity.this,"暂无数据");
                            }
                        }
                        dataList.addAll(datas);
                        //刷新数据
                        searcherCommonAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
        //        }
    };

    @OnClick({R.id.searcher_common_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searcher_common_cancel:
                finish();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);
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
        currentPage++;
//        loadData();
        initDatas();
        refresh.loadMoreComplete();
        //刷新完自动收起
        refresh.refreshComplete();
    }

    private boolean isRefreshing;

    @Override
    public void onRefresh() {
        isRefreshing = true;
        currentPage = 0;
        refresh.setLoadMoreEnabled(true);
        initDatas();
        refresh.refreshComplete();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
            mList.clear();
            isRefreshing = true;
            currentPage = 0;
            initDatas();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
