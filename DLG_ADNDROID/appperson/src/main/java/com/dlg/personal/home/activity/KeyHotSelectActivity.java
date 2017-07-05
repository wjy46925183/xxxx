package com.dlg.personal.home.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.view.loadmore.BaseLoadMoreHeaderAdapter;
import com.dlg.data.home.model.EmployeeListBean;
import com.dlg.data.home.model.KeyHotBean;
import com.dlg.personal.R;
import com.dlg.personal.base.BaseActivity;
import com.dlg.personal.base.ToolBarType;
import com.dlg.personal.home.adapter.HotSearchAdapter;
import com.dlg.personal.home.fragment.EmployeeListFragment;
import com.dlg.viewmodel.home.KeyHotSelectionViewModle;
import com.dlg.viewmodel.home.presenter.KeyHotSelectionPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：小明
 * 主要功能：热门和关键字搜索界面
 * 创建时间：2017/6/28 0028 11:06
 */
public class KeyHotSelectActivity extends BaseActivity implements KeyHotSelectionPresenter {


    private HotKeyToWorkFragment hotKeyToWorkFragment;//跳转的热门搜索结果界面
    private KeyHotSelectionViewModle mKeyHotSelectionViewModle; //热门关键字ViewModle
    private EditText searchText;//搜索框输入
    private TextView cancel;//取消
    private FrameLayout flHistory;
    private LinearLayout llayoutHistory;
    private TextView historyClear;//清除按钮
    private RecyclerView recyclerHistory;
    private RecyclerView recyclerHot;//
    private List<KeyHotBean> beans = new ArrayList<>(); //数据集
    private List<String> historylist=new ArrayList<>();//历史记录
    private EmployeeListFragment employeeListFragment;
    private final String EMPLOYEE_TAG="10001";
    private HotSearchAdapter hotSearchAdapter;
    //private HistorySearchAdapter historySearchAdapter;

    List<Map> list = new ArrayList<>();
    Map<Integer, String> map = new HashMap<>();
    int num=1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_keyhot_select, ToolBarType.NO);
        initData();
        initView();
    }

    private void initData() {
        llayoutHistory.setVisibility(View.GONE);
        mKeyHotSelectionViewModle = new KeyHotSelectionViewModle(this, this, this);



        /*historySearchAdapter=new HistorySearchAdapter(this,recyclerHistory,list,R.layout.item_keyhot_history);
        recyclerHistory.setLayoutManager(new GridLayoutManager(this,4));
        recyclerHistory.setAdapter(historySearchAdapter);*/

        hotSearchAdapter = new HotSearchAdapter(context,recyclerHot,beans,R.layout.item_keyhot_history);
        recyclerHot.setLayoutManager(new GridLayoutManager(this,4));
        recyclerHot.setAdapter(hotSearchAdapter);
        hotSearchAdapter.setOnItemClickListener(new BaseLoadMoreHeaderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                addFragment(R.id.fl_history, employeeListFragment,EMPLOYEE_TAG);
            }
        });
    }


    @Override
    public void getKeyHot(List<KeyHotBean> keyHotBean) {
        beans.clear();
        beans.addAll(keyHotBean);
        hotSearchAdapter.notifyDataSetChanged();
        //historySearchAdapter.notifyDataSetChanged();
    }

    private void initView() {
        searchText = (EditText) findViewById(R.id.search_text);
        cancel = (TextView) findViewById(R.id.cancel);
        flHistory = (FrameLayout) findViewById(R.id.fl_history);
        llayoutHistory = (LinearLayout) findViewById(R.id.llayout_history);
        historyClear = (TextView) findViewById(R.id.history_clear);
        recyclerHistory = (RecyclerView) findViewById(R.id.recycler_history);
        recyclerHot = (RecyclerView) findViewById(R.id.recycler_hot);
    }

    private boolean flag = true;
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (flag&&event.getKeyCode() == KeyEvent.KEYCODE_ENTER){
            flag=false;
            map.put(num,searchText.getText().toString().trim());
            num++;
            Bundle bundle=new Bundle();
            bundle.putString("type",searchText.getText().toString().trim());
            employeeListFragment.setArguments(bundle);
            addFragment(R.id.fl_history, employeeListFragment,EMPLOYEE_TAG);
            return false;
        }
        return super.dispatchKeyEvent(event);
    }
}

