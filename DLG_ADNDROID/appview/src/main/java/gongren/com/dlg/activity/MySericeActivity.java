package gongren.com.dlg.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.bumptech.glide.Glide;
import com.canyinghao.canrefresh.CanRefreshLayout;
import com.common.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.javabean.worker.MySericeBean;
import gongren.com.dlg.javabean.worker.ServiceShareBean;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.DialogUtils;
import gongren.com.dlg.utils.ShareUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;

/**
 * Created by wangjinya on 2017/6/21.
 */

public class MySericeActivity extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener {

    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.iv_right)
    ImageView mIvRight;
    @Bind(R.id.can_content_view)
    SwipeMenuListView mCanContentView;
    @Bind(R.id.refresh)
    CanRefreshLayout mRefresh;
    @Bind(R.id.tv_add_serice)
    TextView mTvAddSerice;
    @Bind(R.id.fl_empty)
    FrameLayout mFrameLayout;
    private List<MySericeBean.DataBean> data = new ArrayList<>();
    private Dialog mLoadingDialog;

    private ShareAction mShareAction = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myserice);
        ButterKnife.bind(this);
        mTvTitle.setText("我的服务");
        mCanContentView.setAdapter(mBaseAdapter);
        mRefresh.setLoadMoreEnabled(true);
        mRefresh.setRefreshEnabled(true);
        mCanContentView.smoothCloseMenu();
        mCanContentView.setMenuCreator(new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                if (menu.getViewType() == 0) {
                    SwipeMenuItem shareItem = new SwipeMenuItem(getApplicationContext());
                    shareItem.setIcon(R.mipmap.share);
                    shareItem.setWidth(getResources().getDimensionPixelSize(R.dimen.delete_width));
                    // 将创建的菜单项添加进菜单中
                    menu.addMenuItem(shareItem);

                    SwipeMenuItem item = new SwipeMenuItem(getApplicationContext());
                    item.setIcon(R.mipmap.edit);
                    item.setWidth(getResources().getDimensionPixelSize(R.dimen.delete_width));
                    menu.addMenuItem(item);

                    SwipeMenuItem item2 = new SwipeMenuItem(getApplicationContext());
                    item2.setWidth(getResources().getDimensionPixelSize(R.dimen.delete_width));
                    item2.setIcon(R.mipmap.delete);
                    menu.addMenuItem(item2);

                }
            }
        });

        mCanContentView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0://分享
                        initShare(data.get(position).getId());
                        break;
                    case 1://删除
                        Intent intent = new Intent(MySericeActivity.this, ReleaseServiceActivity.class);
                        intent.putExtra("edit", data.get(position));
                        startActivity(intent);
                        break;
                    case 2://删除
                        deleteData(data.get(position).getId());
                        break;
                }
                return false;
            }
        });
        mCanContentView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        mRefresh.setOnRefreshListener(this);
        mRefresh.setOnLoadMoreListener(this);
    }

    private void initShare(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("serviceId", id);
        map.put("format", "json");
        DataUtils.loadData(this, GetDataConfing.SERVICE_SHARE, map, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                try {
                    JSONObject jsonObject = new JSONObject(dataRequest.getResponseData());
                    if (jsonObject.getInt("code") == 0) {
                        ServiceShareBean serviceShareBean = new Gson().fromJson(dataRequest.getResponseData(), ServiceShareBean.class);
                        List<ServiceShareBean.DataBean> data = serviceShareBean.getData();

                        if (data != null && data.size() > 0) {
                            ServiceShareBean.DataBean dataBean = data.get(0);
                            mShareAction = ShareUtils.setUMShareAction(MySericeActivity.this, dataBean.getServiceTitle(),
                                    dataBean.getServiceDescription(),
                                    dataBean.getDetailsUrl(), dataBean.getUserLogo());
                            mShareAction.open();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private int page = 0;

    private void initData() {
        if (mLoadingDialog == null) {
            mLoadingDialog = DialogUtils.showLoadingDialog(this);
        } else if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", SharedPreferencesUtils.getString(this, SharedPreferencesUtils.USERID));
        map.put("pageIndex", page + "");
        map.put("pageSize", "20");
        map.put("format", "json");
        Log.i("====s====", map.toString());
        DataUtils.loadData(this, GetDataConfing.SELECT_SERVICELIST, map, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                mLoadingDialog.dismiss();
                Log.i("=====s====", dataRequest.getResponseData());
                mRefresh.refreshComplete();
                mRefresh.loadMoreComplete();
                try {
                    JSONObject jsonObject = new JSONObject(dataRequest.getResponseData());
                    if (jsonObject.optInt("code") == 0) {
                        MySericeBean mySericeBean = new Gson().fromJson(dataRequest.getResponseData(), MySericeBean.class);
                        if (page == 0) {
                            data.clear();
                        }
                        data.addAll(mySericeBean.getData());
                        if (data.size() == 0) {
                            mFrameLayout.setVisibility(View.VISIBLE);
                            mRefresh.setVisibility(View.GONE);
                            mRefresh.setLoadMoreEnabled(false);
                            mRefresh.setRefreshEnabled(false);
                        } else {
                            mFrameLayout.setVisibility(View.GONE);
                            mRefresh.setVisibility(View.VISIBLE);
                            mRefresh.setLoadMoreEnabled(true);
                            mRefresh.setRefreshEnabled(true);
                        }
                        mBaseAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_add_serice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_add_serice:
                startActivity(new Intent(this, ReleaseServiceActivity.class));
                break;
        }
    }

    BaseAdapter mBaseAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(MySericeActivity.this).inflate(R.layout.item_myserice, null);
                holder = new MyViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (MyViewHolder) convertView.getTag();
            }
            int serviceMeterUnit = data.get(position).getServiceMeterUnit();
            String unitName = null;
            if (serviceMeterUnit == 1) {
                unitName = "/天";
            } else if (serviceMeterUnit == 2) {
                unitName = "/时";
            } else if (serviceMeterUnit == 3) {
                unitName = "/单";
            }
            holder.tv_price.setText(data.get(position).getPrice() + "元" + unitName);
            holder.tv_position.setText(data.get(position).getServiceName());
            if (data.get(position).getImagesUrlList() != null && data.get(position).getImagesUrlList().size() > 0) {
                String s = data.get(position).getImagesUrlList().get(0);
                Log.i("====s===", s);
                Glide.with(context).load(s).error(R.mipmap.morentouxiang)
                        .placeholder(R.mipmap.morentouxiang).into(holder.iv_head);
            }
            return convertView;
        }

        class MyViewHolder {
            TextView tv_position, tv_price;
            ImageView iv_head;

            public MyViewHolder(View itemView) {
                tv_position = (TextView) itemView.findViewById(R.id.tv_position);
                tv_price = (TextView) itemView.findViewById(R.id.tv_price);
                iv_head = (ImageView) itemView.findViewById(R.id.iv_item_myserice);
            }
        }
    };

    @Override
    public void onRefresh() {
        page = 0;
        initData();
    }

    @Override
    public void onLoadMore() {
        page++;
        initData();
    }

    /**
     * 删除订单
     *
     * @param id
     */
    private void deleteData(String id) {
        if (mLoadingDialog == null) {
            mLoadingDialog = DialogUtils.showLoadingDialog(this);
        } else if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
        Map<String, Object> map = new HashMap<>();
        /**
         * 当前登录用户
         id://服务id
         */
        map.put("format", "json");
        map.put("id", id);
        DataUtils.loadData(this, GetDataConfing.DELETE_SERVICE, map, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {

                String responseData = dataRequest.getResponseData();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);

                    if (jsonObject.optInt("code") == 0) {
                        page = 0;
                        initData();//重新刷新数据
                    } else {
                        Toast.makeText(context, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                        mLoadingDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
