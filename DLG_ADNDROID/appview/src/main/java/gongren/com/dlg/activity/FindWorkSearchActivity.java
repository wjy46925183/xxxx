package gongren.com.dlg.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.common.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aym.util.json.JsonMap;
import aym.util.json.JsonParseHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.adapter.WorkerSearcherHistoryAdapter;
import gongren.com.dlg.javabean.ReceiveReturnIdJson;
import gongren.com.dlg.javabean.SelectData;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.DialogUtils;
import gongren.com.dlg.utils.GsonUtils;
import gongren.com.dlg.utils.LogUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.view.ListViewDeleteItem;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 你想找什么样的零工
 */
public class FindWorkSearchActivity extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener {
    private final int SELECT_LG_TYPE = 10;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.work_searcher_list)
    ListViewDeleteItem searcherList;

    @Nullable
    @Bind(R.id.work_name)
    EditText tvName;

    @Nullable
    @Bind(R.id.et_type)
    TextView etType;

    @Nullable
    @Bind(R.id.layout_worker_type)
    View workerType;

    @Nullable
    @Bind(R.id.low_pay)
    EditText lowPay;

    @Nullable
    @Bind(R.id.unit_day)
    TextView tvDay;

    @Nullable
    @Bind(R.id.unit_time)
    TextView tvTime;
    @Nullable
    @Bind(R.id.unit_num)
    TextView tvNum;
    @Nullable
    @Bind(R.id.btn_comit)
    Button btnComit;
    LinearLayout llDownAcceptPay;
    LinearLayout layoutZhengshu;

    private View view;
    private SelectData selectData;
    private int demandType = 0;
    private int payPattern = 1;
    private static final int TAG_REQUEST = 1;
    private static final int QUERY_REQUEST = 2;
    private static final int DELETE_QUERY_REQUEST = 3;

    private String[] tabArray = {"工作日", "双休日", "计件"};

    private WorkerSearcherHistoryAdapter adapter;
    private List<DoingOrderData> list = new ArrayList<>();   //数据源
    private List<JsonMap<String, Object>> dataList = new ArrayList<>();   //数据源
    private DoingOrderData deleteDoingOrderData;
    private Double x_longitude;
    private Double y_latitude;
    private TextView tvSearchHostory;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fadan_guyuan);
        view = getLayoutInflater().inflate(R.layout.fadan_guyuan_child, null);
        tvSearchHostory = (TextView) findViewById(R.id.textView);
        llDownAcceptPay = (LinearLayout) findViewById(R.id.ll_down_accept_pay);
        layoutZhengshu = (LinearLayout) findViewById(R.id.layout_zhengshu);
        tvSearchHostory.setVisibility(View.GONE);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        x_longitude = getIntent().getDoubleExtra("x_longitude", 0.0);
        y_latitude = getIntent().getDoubleExtra("y_latitude", 0.0);
        dialog = DialogUtils.showLoadingDialog(context);
        tvTitle.setText("您想找什么样的零工");
        for (int i = 0; i < tabArray.length; i++) {
            TabLayout.Tab tab = tablayout.newTab().setText(tabArray[i]);
            tab.setTag(i);
            tablayout.addTab(tab);
        }
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if ((int) (tab.getTag()) == 2) {
                    tvDay.setVisibility(View.GONE);
                    tvTime.setVisibility(View.GONE);
                    tvNum.setVisibility(View.VISIBLE);
                } else {
                    tvDay.setVisibility(View.VISIBLE);
                    tvTime.setVisibility(View.VISIBLE);
                    tvNum.setVisibility(View.GONE);
                }
                demandType = (int) (tab.getTag());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //        List<DoingOrderData> tempList = new ArrayList<>();
        //        for (int i = 0; i < 10; i++) {
        //            DoingOrderData data = new DoingOrderData();
        //            data.setPostName("服务员"+i);
        //            list.add(data);
        //        }
//        searcherList.addHeaderView(view);
        adapter = new WorkerSearcherHistoryAdapter(FindWorkSearchActivity.this, list);
        searcherList.setAdapter(adapter);

        searcherList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                deleteDoingOrderData = (DoingOrderData) adapter.getItem(position);
                Intent intent = new Intent(context, SearchCommonActivity.class);
                intent.putExtra("search", tvName.getText().toString().trim().equals("") ? "" : tvName.getText().toString().trim());
                intent.putExtra("x_longitude", x_longitude);
                intent.putExtra("y_latitude", y_latitude);
                intent.putExtra("conditionId", deleteDoingOrderData.getId());
                intent.putExtra("data", "");
                startActivity(intent);
            }
        });
        searcherList.setBtnDeleteClickListener(new ListViewDeleteItem.BtnDeleteClickListener() {
            @Override
            public void btnClick(int position) {
                deleteDoingOrderData = (DoingOrderData) adapter.getItem(position);
                dialog.show();

                Map<String, Object> map = BaseMapUtils.getMap(context);
                map.put("id", deleteDoingOrderData.getId());
                //                        map.put("id", "27523697867260031270365041338");
                map.put("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));

                //                        adapter.notifyDataSetChanged();
                DataUtils.loadData(context, GetDataConfing.deleteJobConditionRest, map, DELETE_QUERY_REQUEST, responseDataCallback);

            }
        });

        ButterKnife.bind(this);

        initData();
    }

    private void initData() {

        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("id", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));
        DataUtils.loadData(context, GetDataConfing.jobConditionRestList, map, QUERY_REQUEST, responseDataCallback);
    }

    @Nullable
    @OnClick({R.id.iv_back, R.id.btn_comit, R.id.unit_day, R.id.unit_time, R.id.layout_worker_type})
    public void onClick(View view) {
        DialogUtils.closeInputMethod(context);
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.unit_day:
                //天计算
                tvDay.setTextColor(getResources().getColor(R.color.tab_selected_textcolor));
                tvDay.setBackgroundResource(R.drawable.type_style_yellow);
                tvTime.setTextColor(getResources().getColor(R.color.black_text));
                tvTime.setBackgroundColor(getResources().getColor(R.color.white));
                payPattern = 1;
                break;

            case R.id.unit_time:
                //小时
                tvTime.setTextColor(getResources().getColor(R.color.tab_selected_textcolor));
                tvTime.setBackgroundResource(R.drawable.type_style_yellow);
                tvDay.setTextColor(getResources().getColor(R.color.black_text));
                tvDay.setBackgroundColor(getResources().getColor(R.color.white));
                payPattern = 2;
                break;

            case R.id.layout_worker_type:
                //零工类型选择
                go2SelectLGType();
                break;

            case R.id.btn_comit:
                if (dialog != null) {
                    dialog.show();
                }

                //职工提交搜索
                submitDatas();

                //提交
                //                Intent intent= new Intent(context, SearchCommonActivity.class);
                //                intent.putExtra("search",tvName.getText().toString().trim().equals("")?"":tvName.getText().toString().trim());
                //                startActivity(intent);
                //                finish();
                break;
        }
    }

    private void submitDatas() {
        String workName = tvName.getText().toString().trim();
        String minilowPay = lowPay.getText().toString().trim();

        if (TextUtils.isEmpty(workName) && TextUtils.isEmpty(minilowPay) && null == selectData) {
            dialog.dismiss();
            if (TextUtils.isEmpty(workName)) {
                ToastUtils.showToastShort(context, "工种名称不能为空");
                return;
            }
            if (TextUtils.isEmpty(minilowPay)) {
                ToastUtils.showToastShort(context, "报酬不能为空");
                return;
            }
            if (selectData == null) {
                ToastUtils.showToastShort(context, "类型不能为空");
                dialog.dismiss();
                return;
            }
        }

        if (demandType == 2) {
            payPattern = 3;
        }
        Map<String, Object> map = BaseMapUtils.getMap(context);
        if (null != selectData) {
            map.put("jobType", selectData.getDataCode());
            map.put("jobTypeName", selectData.getDataName());
        }
        map.put("postName", workName);
        map.put("demandType", (demandType + 1) + "");
        map.put("price", minilowPay);
        map.put("jobMeterUnit", payPattern + "");
        map.put("id", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));

        DataUtils.loadData(context, GetDataConfing.jobConditionRest, map, TAG_REQUEST, responseDataCallback);
    }

    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            //            if (canContentView != null) {
            if (dialog != null) {
                dialog.dismiss();
            }
            if (dataRequest.isNetError()) {
                ShowGetDataError.showNetError(context);
            } else {
                String json = dataRequest.getResponseData();
                if (!TextUtils.isEmpty(json)) {
                    if (dataRequest.getWhat() == TAG_REQUEST) {
                        LogUtils.logD("zq", json);
                        final ReceiveReturnIdJson idJson = GsonUtils.jsonToBean(json, ReceiveReturnIdJson.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                if (dialog != null) {
//                                    dialog.dismiss();
//                                    startActivity(new Intent(FindWorkSearchActivity.this,SearchCommonActivity.class));
//                                }
                                if (idJson.data != null && idJson.data.size() > 0) {
                                    Intent intent = new Intent(context, SearchCommonActivity.class);
                                    intent.putExtra("search", tvName.getText().toString().trim().equals("") ? "" : tvName.getText().toString().trim());
                                    intent.putExtra("x_longitude", x_longitude);
                                    intent.putExtra("y_latitude", y_latitude);
                                    intent.putExtra("conditionId", idJson.data.get(0));
                                    intent.putExtra("data", "");
                                    startActivity(intent);
                                }

                            }
                        });
                    } else if (dataRequest.getWhat() == QUERY_REQUEST) {
                        dataList = JsonParseHelper.getJsonMap_List_JsonMap(json, "data");
                        if (dataList == null || dataList.size() == 0) {
                            tvSearchHostory.setVisibility(View.GONE);
                        } else {
                            tvSearchHostory.setVisibility(View.VISIBLE);
                        }
                        for (int i = 0; i < dataList.size(); i++) {
                            JsonMap<String, Object> jsonMap = dataList.get(i);
                            DoingOrderData doingOrderData = new DoingOrderData();
                            doingOrderData.setId(jsonMap.getString("id"));
                            doingOrderData.setDemandType(jsonMap.getString("demandType"));
                            doingOrderData.setPostName(jsonMap.getString("postName"));
                            doingOrderData.setJobType(jsonMap.getString("jobType"));
                            doingOrderData.setJobTypeName(jsonMap.getString("jobTypeName"));
                            doingOrderData.setJobMeterUnit(jsonMap.getString("jobMeterUnit"));
                            doingOrderData.setPrice(jsonMap.getString("price"));
                            doingOrderData.setCode(jsonMap.getString("code"));
                            list.add(doingOrderData);
                        }
                        //刷新数据
                        adapter.setDataBean(list);
                    } else if (dataRequest.getWhat() == DELETE_QUERY_REQUEST) {

                        list.remove(deleteDoingOrderData);
                        //刷新数据
                        adapter.setDataBean(list);
                        if (list == null || list.size() == 0) {
                            tvSearchHostory.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
    };

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {

    }


    public void go2SelectLGType() {
        String current_select = etType.getText().toString();

        Intent intent = new Intent(context, LGTypeActivity.class);
        intent.putExtra("current_select", current_select);
        startActivityForResult(intent, SELECT_LG_TYPE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_LG_TYPE://零工类型选择
                    selectData = (SelectData) data.getSerializableExtra("select_type");
                    etType.setText(selectData.getDataName());
                    if (selectData.getDataName().equals("志愿义工")) {
                        llDownAcceptPay.setVisibility(View.INVISIBLE);
                        layoutZhengshu.setVisibility(View.INVISIBLE);

                    } else {
                        llDownAcceptPay.setVisibility(View.VISIBLE);
                        layoutZhengshu.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }

}
