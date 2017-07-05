package gongren.com.dlg.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.common.utils.SharedPreferencesUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import gongren.com.dlg.R;
import gongren.com.dlg.activity.BossOrderActivity;
import gongren.com.dlg.activity.EmployeeDetailsActivity;
import gongren.com.dlg.activity.LoginDialogActivity;
import gongren.com.dlg.javabean.master.BossMapMsgBean;
import gongren.com.dlg.javabean.master.MasterCard;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;

/**
 * Created by liukui .
 * on 2017/4/12
 * 文件描述：
 */

public class MapWorkerFragment extends BaseFragment {
    private final String TAG = "MapWorkerFragment";

    @Bind(R.id.bossmap_iv_head)
    CircleImageView ivHead;

    @Bind(R.id.bossmap_layout_operator)
    TextView liOperator;

    @Bind(R.id.bossmap_layout_call)
    View layCall;

    @Bind(R.id.bossmap_tv_demand)
    TextView tvDemand;

    @Bind(R.id.bossmap_tv_starts)
    RatingBar tvStarts;

    @Bind(R.id.bossmap_tv_distance)
    TextView tvDistance;

    @Bind(R.id.bossmap_tv_time)
    TextView tvTime;

    @Bind(R.id.bossmap_tv_name)
    TextView tvName;

    @Bind(R.id.bossmap_tv_company)
    TextView tvCompany;

    @Bind(R.id.bossmap_iv_worker)
    View ivWorker;
    @Bind(R.id.tv_phone)
    TextView tv_phone;
    @Bind(R.id.bossmap_tv_price)
    TextView bossmapTvPrice;
    @Bind(R.id.bossmap_layout_active)
    LinearLayout bossmapLayoutActive;
    @Bind(R.id.bossmap_layout_order)
    LinearLayout bossmapLayoutOrder;
    @Bind(R.id.tv_service)
    TextView tv_service;

    private String phone, employeeParamId, taskId;
    private String bossId, jobId;
    private MasterCard masterCard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_viewpager_map_order_list, container, false);
        view.setVisibility(View.VISIBLE);
        ButterKnife.bind(this, view);
        loadDatas();
        return view;
    }

    /**
     * 设置当前雇主的用户id
     *
     * @param bossId
     */
    public void setBossId(String bossId) {
        this.bossId = bossId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    private BossMapMsgBean.DataBean mDataBean;

    /**
     * 设置当前雇主的用户id
     *
     * @param dataBean
     */
    public void setDataBean(BossMapMsgBean.DataBean dataBean) {
        this.mDataBean = dataBean;
    }

    /*****
     * 加载该雇主的个人信息
     */
    private void loadDatas() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", bossId);
        map.put("format", "json");
        DataUtils.loadData(activity, GetDataConfing.BOSS_MAP_DETAIL, map, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                String responseData = dataRequest.getResponseData();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    if ("0".equals(jsonObject.optString("code"))) {
                        masterCard = new Gson().fromJson(responseData, MasterCard.class);
                        if (masterCard.data != null && masterCard.data.size() > 0) {
                            MasterCard.DataBean dataBean = masterCard.data.get(0);
                            Glide.with(activity).load(dataBean.userAttributeRestVo.logo)
                                    .error(R.mipmap.morentouxiang)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.mipmap.morentouxiang)
                                    .into(ivHead);
                            bossmapTvPrice.setText(TextUtils.isEmpty(dataBean.userAttributeRestVo.personalizedSignature) ? "自由、灵活、赚钱" : dataBean.userAttributeRestVo.personalizedSignature);
                            tvCompany.setText(dataBean.userAttributeRestVo.creditCount == null ? "36.5" : dataBean.userAttributeRestVo.creditCount);

                            if(dataBean.userAttributeRestVo.serviceCount != 0){
                                tv_service.setVisibility(View.VISIBLE);
                                tv_service.setText("服务"+dataBean.userAttributeRestVo.serviceCount+"项");
                            }else{
                                tv_service.setVisibility(View.GONE);
                            }


                            String name = "";
                            if (!TextUtils.isEmpty(dataBean.userAttributeRestVo.name)) {
                                name = dataBean.userAttributeRestVo.name;
                            } else {
                                if (!TextUtils.isEmpty(dataBean.userRestVo.username)) {
                                    name = dataBean.userRestVo.username;
                                }
                            }
                            tvName.setText(name);
                            Log.e(TAG, "tvName  identity=" + dataBean.userAttributeRestVo.name);
                            String nameType = "自由工作者";
                            if (!TextUtils.isEmpty(dataBean.userAttributeRestVo.identity)) {
                                int identity = Integer.parseInt(dataBean.userAttributeRestVo.identity);
                                switch (identity) {
                                    case 0:
                                        nameType = "在校学生";
                                        break;
                                    case 1:
                                        nameType = "自由工作者";
                                        break;
                                    case 2:
                                        nameType = "兼职人员";
                                        break;
                                }
                            }
                            tvDemand.setText(nameType);//人员类型
                            tvDistance.setText("距离" + mDataBean.distance + "km");
                            tvStarts.setRating(dataBean.userAttributeRestVo.scoreCount == null ? 5 : Float.parseFloat(dataBean.userAttributeRestVo.scoreCount));
                            tvTime.setText(dataBean.userAttributeRestVo.joinCount == null ? "" : "已接" + dataBean.userAttributeRestVo.joinCount+ "单");
                            liOperator.setText("雇他");
                            tv_phone.setText("分享");
                            layCall.setVisibility(View.GONE);
                            phone = dataBean.userRestVo.phone;
                            employeeParamId = dataBean.userAttributeRestVo.userId;

                            taskId = dataBean.userAttributeRestVo.id;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @OnClick({R.id.bossmap_iv_worker, R.id.bossmap_layout_operator, R.id.tv_phone, R.id.bossmap_layout_active})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bossmap_iv_worker:
                Intent intent1 = new Intent(activity, EmployeeDetailsActivity.class);
                intent1.putExtra("masterCard", masterCard);
                intent1.putExtra("employeeParamId", employeeParamId);
                //雇员详情
                startActivity(intent1);
                break;
            case R.id.bossmap_layout_active:
            case R.id.bossmap_layout_operator:
                String userId = SharedPreferencesUtils.getString(activity, SharedPreferencesUtils.USERID);
                if (!TextUtils.isEmpty(employeeParamId) && !TextUtils.isEmpty(userId)) {
                    Intent intent = new Intent(activity, BossOrderActivity.class);
                    intent.putExtra("employeeParamId", employeeParamId);
                    intent.putExtra("bossId",bossId);
                    startActivity(intent);
                } else {
                    startActivityForResult(new Intent(activity, LoginDialogActivity.class), 0);
                }
                break;
            case R.id.tv_phone:
                initShareData();
                break;
        }
    }

    private void initShareData() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
