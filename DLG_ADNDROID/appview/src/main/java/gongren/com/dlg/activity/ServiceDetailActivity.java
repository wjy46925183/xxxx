package gongren.com.dlg.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.common.utils.StringUtils;
import com.common.view.convenientbanner.ConvenientBanner;
import com.common.view.convenientbanner.holder.CBViewHolderCreator;
import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import gongren.com.dlg.R;
import gongren.com.dlg.javabean.worker.MySericeBean;
import gongren.com.dlg.javabean.worker.ServiceDetailBean;
import gongren.com.dlg.javabean.worker.ServiceShareBean;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.NetworkImageHolderView;
import gongren.com.dlg.utils.ShareUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;

import static gongren.com.dlg.R.id.tv_money;

/**
 * 作者：王进亚
 * 主要功能：
 * 创建时间：2017/6/23 15:15
 */

public class ServiceDetailActivity extends BaseActivity {


    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.iv_right)
    ImageView mIvRight;
    @Bind(R.id.banner)
    ConvenientBanner mBanner;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.tv_demand)
    TextView mTvDemand;
    @Bind(R.id.tv_money)
    TextView mTvMoney;
    @Bind(R.id.tv_description)
    TextView mTvDescription;
    @Bind(R.id.tv_edit)
    TextView mTvEdit;
    @Bind(R.id.tv_share)
    TextView mTvShare;
    @Bind(R.id.tv_hire_ta)
    TextView mTvHireTa;
    @Bind(R.id.iv_logo)
    CircleImageView mIvLogo;
    @Bind(R.id.tv_name_person)
    TextView mTvNamePerson;
    @Bind(R.id.iv_banner)
    ImageView iv_banner;
    private MySericeBean.DataBean mDataBean;
    private ShareAction mShareAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        ButterKnife.bind(this);
        mDataBean = (MySericeBean.DataBean) getIntent().getSerializableExtra("dataBean");
        initDetail();
        initShare();
    }

    private void initView(ServiceDetailBean.DataBean dataBean) {

        int serviceMeterUnit = dataBean.serviceMeterUnit;
        String unitName = null;
        if (serviceMeterUnit == 1) {
            unitName = "/天";
        } else if (serviceMeterUnit == 2) {
            unitName = "/时";
        } else if (serviceMeterUnit == 3) {
            unitName = "/单";
        }
        mTvTitle.setText(dataBean.serviceName+" "+dataBean.price + "元" + unitName);
        mTvName.setText(dataBean.serviceName);
        mTvMoney.setText(dataBean.price + "元" + unitName);
        int demandType = dataBean.demandType;
        String demandTypeName = null;
        switch (demandType) {
            case 1:
                demandTypeName = "工作日";
                break;
            case 2:
                demandTypeName = "双休日";
                break;
            case 3:
                demandTypeName = "计件";
                break;
        }
        mTvDemand.setText(demandTypeName);
        mTvEdit.setText(dataBean.serviceDescription);
        mTvNamePerson.setText(dataBean.name == null ? "" : dataBean.name);
        Glide.with(this).load(dataBean.logo).error(R.mipmap.icon_default_user).placeholder(R.mipmap.icon_default_user)
                .into(mIvLogo);
        if(dataBean.imagesUrlList != null&&dataBean.imagesUrlList.size()>0){
            mBanner.setPages(new CBViewHolderCreator() {
                @Override
                public NetworkImageHolderView createHolder() {
                    return new NetworkImageHolderView();
                }
            }, dataBean.imagesUrlList)//设置指示器是否可见
                    //设置自动切换（同时设置了切换时间间隔）
                    .startTurning(2000)
                    .setPointViewVisible(true)
                    //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                    .setPageIndicator(new int[]{R.mipmap.unfoncus, R.mipmap.focused})
                    //设置指示器的方向（左、中、右）
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
            mBanner.getViewPager().setOffscreenPageLimit(1);
        }else{
            iv_banner.setVisibility(View.VISIBLE);
            mBanner.setVisibility(View.GONE);
        }
    }

    public void initDetail(){
        /**
         * id://服务id
         */
        Map<String,Object> map = new HashMap<>();
        map.put("id",mDataBean.getId());
        map.put("format","json");
        DataUtils.loadData(this, GetDataConfing.SERVICE_DETAIL, map, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                Log.i("====s",dataRequest.getResponseData());
                try {
                    JSONObject jsonObject = new JSONObject(dataRequest.getResponseData());
                    if(jsonObject.getInt("code") == 0){
                        ServiceDetailBean serviceDetailBean =
                                new Gson().fromJson(dataRequest.getResponseData(), ServiceDetailBean.class);
                        ServiceDetailBean.DataBean dataBean = serviceDetailBean.data.get(0);

                        initView(dataBean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * 分享
     */
    private void initShare() {
        Map<String,Object> map = new HashMap<>();
        map.put("serviceId",mDataBean.getId());
        map.put("format","json");
        DataUtils.loadData(this, GetDataConfing.SERVICE_SHARE, map, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                try {
                    JSONObject jsonObject = new JSONObject(dataRequest.getResponseData());
                    if(jsonObject.getInt("code") == 0){
                        ServiceShareBean serviceShareBean = new Gson().fromJson(dataRequest.getResponseData(), ServiceShareBean.class);
                        List<ServiceShareBean.DataBean> data = serviceShareBean.getData();

                        if(data != null&&data.size()>0){
                            ServiceShareBean.DataBean dataBean = data.get(0);
                            mShareAction = ShareUtils.setUMShareAction(context,dataBean.getServiceTitle(),
                                    dataBean.getServiceDescription(),
                                   dataBean.getDetailsUrl(),dataBean.getUserLogo());
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_share, R.id.tv_hire_ta})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_share://分享
                if (null != mShareAction) {
                    mShareAction.open();
                }
                break;
            case R.id.tv_hire_ta:
                if(StringUtils.isLogin(this)){
                    Intent bossOrderActivityIntent = new Intent(context, WillActivity.class);
                    bossOrderActivityIntent.putExtra("dataBean",mDataBean);
                    startActivity(bossOrderActivityIntent);
                }else{
                    startActivity(new Intent(this, LoginDialogActivity.class));
                }
                break;
        }
    }
}
