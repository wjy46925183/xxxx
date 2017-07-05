package com.dlg.personal.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.common.view.loadmore.BaseLoadMoreHeaderAdapter;
import com.dlg.data.home.model.WorkTypeBean;
import com.dlg.personal.R;
import com.dlg.personal.base.BaseActivity;
import com.dlg.personal.home.adapter.WorkTypeAdapter;
import com.dlg.viewmodel.home.WorkTypeViewModel;

import java.util.List;


/**
 * 作者：小明
 * 主要功能：
 * 创建时间：2017/6/29 0029 18:18
 */
public class WorkTypeActivity extends BaseActivity {
    private RecyclerView recycler;
    private WorkTypeViewModel workTypeViewModel;
    private List<WorkTypeBean>workbean;
    WorkTypeAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_worktype_select);
        mToolBarHelper.setToolbarTitle("选择零工类型");
        initData();
        initView();
    }

    private void initData() {
        workTypeViewModel.getWorType( "job.type");
    }

    private void initView() {
        recycler = (RecyclerView) findViewById(R.id.recycler);
        adapter=new WorkTypeAdapter(context,recycler,workbean,R.layout.item_worktype);
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseLoadMoreHeaderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent();
                String name=workbean.get(position).getDataName();
                intent.putExtra("worktype",name);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }

}
