package gongren.com.dlg.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.canyinghao.canrefresh.CanRefreshLayout;
import com.common.string.LogUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.adapter.AddressSearcherAdapter;
import gongren.com.dlg.fragment.BossFragment;
import gongren.com.dlg.fragment.WorkerFragment;
import gongren.com.dlg.javabean.MainToBossFragmentEvent;
import gongren.com.dlg.javabean.MainToWorkerFragmentEvent;
import gongren.com.dlg.javabean.RefreshEvent;
import gongren.com.dlg.utils.AMapUtil;
import com.common.utils.SharedPreferencesUtils;
import gongren.com.dlg.utils.ToastUtils;

/******
 * 地址公共搜索界面
 */
public class AddressSearcherActivity extends BaseActivity implements CanRefreshLayout.OnRefreshListener, TextWatcher, CanRefreshLayout.OnLoadMoreListener, PoiSearch.OnPoiSearchListener, Inputtips.InputtipsListener {

    @Bind(R.id.searcher_common_cancel)
    TextView tvCancel;
    @Bind(R.id.searcher_common_search)
    EditText etSearcher;
    @Bind(R.id.can_content_view)
    SwipeMenuListView canContentView;
    @Bind(R.id.refresh)
    CanRefreshLayout refresh;
    @Bind(R.id.searcher_common_nothing)
    ImageView tvBothing;

    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索

    private AddressSearcherAdapter addressSearcherAdapter;
    private List<Tip> mList = new ArrayList<>();
    private int currentpage = 1;
    public static final int ADDRESS_SEARCHER = 3;
    public boolean isEditRelease;

    //接收消息
    @Subscribe
    public void onMessageEvent(MainToWorkerFragmentEvent event) {
        if (event != null && event.getMsg() != null) ;
        //ToastUtils.showToastShort(this, event.getMsg());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_search_common);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        canContentView.setEmptyView(tvBothing);
        addressSearcherAdapter = new AddressSearcherAdapter(AddressSearcherActivity.this, mList);
        canContentView.setAdapter(addressSearcherAdapter);
        initListener();
        canContentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mList.size() > 0 && mList.get(position).getPoint() != null) {
                    Tip tip = mList.get(position);
                    LogUtils.e("1=" + tip.getAdcode() + "  2=" + tip.getAddress() + "  3=" + tip.getDistrict() +
                            "  4=" + tip.getName()
                            + "  5=" + tip.getPoiID() + "  6=" + tip.getTypeCode() + "   7=" + tip.getPoint()
                    );
                    String ll =
                            mList.get(position).getPoint().getLatitude() + "," + mList.get(position).getPoint().getLongitude();
                    if (isEditRelease) {
                        EventBus.getDefault().post(new RefreshEvent<Tip>(tip, RefreshEvent.EDIT_RELEASE_ADESS));
                    } else {
                        EventBus.getDefault().post(new MainToWorkerFragmentEvent(ll, WorkerFragment.FROM_ADDRESS));
                        EventBus.getDefault().post(new MainToBossFragmentEvent(ll, BossFragment.FROM_ADDRESS));
                    }
                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);//关闭软键盘
                    SharedPreferencesUtils.saveString(AddressSearcherActivity.this,"adr",tip.getDistrict()+tip.getAddress()+tip.getName());
                    SharedPreferencesUtils.saveString(AddressSearcherActivity.this,"areaId",tip.getAdcode());
                    finish();
                }
            }
        });
        Intent intent = getIntent();
        String address = intent.getStringExtra("address");
        isEditRelease = intent.getBooleanExtra("isEditRelease", false);
        if (address == null || TextUtils.isEmpty(address.trim()))
            return;
        etSearcher.setText(address);
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        refresh.setOnRefreshListener(this);
        refresh.setOnLoadMoreListener(this);
        //监控输入事件
        etSearcher.addTextChangedListener(this);
    }

    @OnClick({R.id.searcher_common_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searcher_common_cancel:
                finish();
                break;
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
        currentpage++;
        refresh.loadMoreComplete();
        //刷新完自动收起
        refresh.refreshComplete();
    }

    @Override
    public void onRefresh() {
        currentpage = 0;
        refresh.setLoadMoreEnabled(true);
        refresh.refreshComplete();
    }

    /**
     * POI信息查询回调方法
     */
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    if (poiItems != null && poiItems.size() > 0) {
                        ToastUtils.showToastLong(context, "查出：" + poiItems.size() + "个结果");
                    } else if (suggestionCities != null && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
                        ToastUtils.showToastLong(context, "无结果");
                    }
                }
            } else {
                ToastUtils.showToastLong(context, "无结果");
            }
        } else {
            ToastUtils.showToastLong(context, rCode + "");
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
            mList.clear();
            mList.addAll(tipList);
            Log.i("====s===tipList",new Gson().toJson(tipList));
            addressSearcherAdapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToastLong(context, rCode + "");
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String newText = charSequence.toString().trim();
        if (AMapUtil.IsEmptyOrNullString(newText))
            return;
        InputtipsQuery inputquery = new InputtipsQuery(newText, SharedPreferencesUtils.getString(this,"city"));
        Inputtips inputTips = new Inputtips(context, inputquery);
        inputTips.setInputtipsListener(this);
        inputTips.requestInputtipsAsyn();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName();
        }
        ToastUtils.showToastLong(context, "推荐:" + infomation);
    }
}
