package gongren.com.dlg.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.cityUtils.ArrayWheelAdapter;
import gongren.com.dlg.cityUtils.OnWheelScrollListener;
import gongren.com.dlg.cityUtils.WheelView;
import gongren.com.dlg.javabean.master.AddressBean;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import com.common.utils.SharedPreferencesUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;


/**
 * 零工地址选择页
 * Created by Administrator on 2017/3/30.
 */
public class SelectAddressActivity extends Activity implements View.OnClickListener, OnWheelScrollListener {

    private final String PROVINCE_PARENTID = "0";
    private final String PROVINCE_AREALEVEL = "1";//区域级别 省
    private final String CITY_AREALEVEL = "2";//区域级别 市
    private final String AREA_AREALEVEL = "3";//区域级别 区
    private final String STREETE_AREALEVEL = "4";//区域级别 乡镇 街道

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_save)
    TextView tvSave;
    @Bind(R.id.pca_text)
    TextView pcaText;
    @Bind(R.id.pca_linear)
    LinearLayout pcaLinear;
    @Bind(R.id.area_text)
    TextView areaText;
    @Bind(R.id.area_linear)
    LinearLayout areaLinear;
    @Bind(R.id.address_tab)
    TextView addressTab;
    @Bind(R.id.address_text)
    EditText addressText;
    @Bind(R.id.ll_bottom)
    LinearLayout ll_bottom;

    //城市选择器---代码
    @Bind(R.id.id_province)
    WheelView mViewProvince;
    @Bind(R.id.id_city)
    WheelView mViewCity;
    @Bind(R.id.id_area)
    WheelView mViewDistrict;

    //街道选择器--代码
    private String[] mJDDatas;
    @Bind(R.id.id_street)
    WheelView mViewStreet;
    @Bind(R.id.ll_wheel)
    LinearLayout ll_wheel;
    private String currentJD;//当前街道
    private List<AddressBean.DataBean> provinceDatas;
    private List<AddressBean.DataBean> cityDatas;
    private List<AddressBean.DataBean> areaDatas;
    private List<AddressBean.DataBean> streetDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_address_activity);
        ButterKnife.bind(this);
        provinceDatas = new ArrayList<>();
        cityDatas = new ArrayList<>();
        areaDatas = new ArrayList<>();
        streetDatas = new ArrayList<>();
        initViews();
    }

    private void initViews() {
        tvTitle.setText("地址");

        Button mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        Button cancle = (Button) findViewById(R.id.btn_cancle);
        mBtnConfirm.setOnClickListener(cityListener);
        cancle.setOnClickListener(cityListener);
        mViewProvince.addScrollingListener(this);
        mViewCity.addScrollingListener(this);
        mViewDistrict.addScrollingListener(this);
        mViewStreet.addScrollingListener(this);
    }


    //----------------------
    //街道滚轮选择
    //街道选择
    public void selectJD() {
        if(areaDatas.size()>0)
            getHttpAddressData(areaDatas.get(currentPostionArea).areaCode, STREETE_AREALEVEL);
    }

    //------------------------

    //---------------------------
    //省市县3d滚轮
    //省市县选择
    public void selectPCA() {
        initProvinceDatas();
    }


    /**
     * 初始化省数据
     */
    private void initProvinceDatas() {
        mViewStreet.setVisibility(View.GONE);
        ll_wheel.setVisibility(View.VISIBLE);
        getHttpAddressData(PROVINCE_PARENTID, PROVINCE_AREALEVEL);
    }

    private void showPop() {
        if(null != ll_bottom){
            ll_bottom.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 更新地区数据
     */
    private void updateAreas() {
    }

    //    确定和取消按钮的监听
    private View.OnClickListener cityListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_confirm:
                    ll_bottom.setVisibility(View.GONE);
                    if (p_type == 1) {
                        pcaText.setText(provinceDatas.get(currentPostionPro).areaName + cityDatas.get(currentPostionCity).areaName + areaDatas.get(currentPostionArea).areaName);
                        areaText.setText("");//置空
                    } else {
                        areaText.setText(streetDatas.get(currentPostionStreet).areaName);
                    }
                    break;
                case R.id.btn_cancle:
                    ll_bottom.setVisibility(View.GONE);
                    break;
            }
        }
    };

    //---------------------------
    int p_type = 1;

    @OnClick({R.id.iv_back, R.id.pca_linear, R.id.area_linear, R.id.tv_save})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.pca_linear://省市县选择
                p_type = 1;
                selectPCA();
                break;
            case R.id.area_linear://街道
                if (TextUtils.isEmpty(pcaText.getText())) {
                    ToastUtils.showToastShort(this, "省市县地址不能为空");
                } else {
                    p_type = 2;
                    selectJD();
                }
                break;
            case R.id.tv_save://保存
                save();
                break;
        }
    }

    public void save() {
        String pca = pcaText.getText().toString();
        String street = areaText.getText().toString();
        String address = addressText.getText().toString();

        if (DataUtils.isNullStr(pca)) {
            ToastUtils.showToastShort(this, "请选择省市县");
            return;
        }
        if (provinceDatas.size() > 0) {
            SharedPreferencesUtils.saveString(this, "pro", provinceDatas.get(currentPostionPro).areaName);
            SharedPreferencesUtils.saveString(this, "proId", provinceDatas.get(currentPostionPro).areaCode);
        }
        if (cityDatas.size() > 0) {
            SharedPreferencesUtils.saveString(this, "cityId", cityDatas.get(currentPostionCity).areaCode);
            SharedPreferencesUtils.saveString(this, "city", cityDatas.get(currentPostionCity).areaName);
        }
        if (areaDatas.size() > 0) {
            SharedPreferencesUtils.saveString(this, "areaId", provinceDatas.get(currentPostionArea).areaCode);
            SharedPreferencesUtils.saveString(this, "area", areaDatas.get(currentPostionArea).areaName);
        }
        if (streetDatas.size() > 0) {
            SharedPreferencesUtils.saveString(this, "street", streetDatas.get(currentPostionStreet).areaName);
            SharedPreferencesUtils.saveString(this, "streetId", streetDatas.get(currentPostionStreet).areaCode);
        }
        SharedPreferencesUtils.saveString(this, "fullAddress", pca + street + address);
        SharedPreferencesUtils.saveString(this, "adr", address);


        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void getHttpAddressData(String areaCode, final String areaType) {
        Map<String, Object> map = BaseMapUtils.getMap(this);
        map.put("parentId", areaCode);
        map.put("areaLevel", areaType);//区域级别1.省 2.市3.区县4.乡镇
        map.put("format", "json");
        DataUtils.loadData(this, GetDataConfing.ADDRESS, map, 1, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                if (dataRequest.isNetError()) {
                    ShowGetDataError.showNetError(SelectAddressActivity.this);
                } else {
                    String responseData = dataRequest.getResponseData();
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        if ("0".equals(jsonObject.optString("code"))) {
                            AddressBean addressBean = new Gson().fromJson(responseData, AddressBean.class);
                            if (addressBean != null && addressBean.data != null && addressBean.data.size() > 0)
                                initAddress(areaType, addressBean);
                        } else {
                            ToastUtils.showToastShort(SelectAddressActivity.this, "获取地址失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private int currentPostionPro = 0;
    private int currentPostionCity = 0;
    private int currentPostionArea = 0;
    private int currentPostionStreet = 0;

    //初始化数据
    private void initAddress(String areaType, AddressBean addressBean) {

        Message message = new Message();
        switch (areaType) {
            case PROVINCE_AREALEVEL://省
                provinceDatas.clear();
                provinceDatas.addAll(addressBean.data);
                message.what = 1;
                mHandler.sendMessage(message);
                break;
            case CITY_AREALEVEL://市
                cityDatas.clear();
                cityDatas.addAll(addressBean.data);
                message.what = 2;
                mHandler.sendMessage(message);
                break;
            case AREA_AREALEVEL://县区
                areaDatas.clear();
                areaDatas.addAll(addressBean.data);
                message.what = 3;
                mHandler.sendMessage(message);
                break;
            case STREETE_AREALEVEL://乡镇街道
                streetDatas.clear();
                streetDatas.addAll(addressBean.data);
                message.what = 4;
                mHandler.sendMessage(message);
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    getHttpAddressData(provinceDatas.get(currentPostionPro).areaCode, CITY_AREALEVEL);
                    break;
                case 2:
                    getHttpAddressData(cityDatas.get(currentPostionCity).areaCode, AREA_AREALEVEL);
                    break;
                case 3:
                    showPop();
                    initUi();
                    break;
                case 4:
                    ll_bottom.setVisibility(View.VISIBLE);
                    ll_wheel.setVisibility(View.GONE);
                    mViewStreet.setVisibility(View.VISIBLE);
                    initUi();
                    mViewStreet.setViewAdapter(new ArrayWheelAdapter<>(SelectAddressActivity.this, streetDatas));
                    mViewStreet.setWheelBackground(R.color.white_color);
                    break;
            }
        }
    };

    //加载ui
    private void initUi() {
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<>(SelectAddressActivity.this, provinceDatas));
        mViewProvince.setCurrentItem(currentPostionPro);
        mViewProvince.setWheelBackground(R.color.white_color);
        mViewCity.setViewAdapter(new ArrayWheelAdapter<>(SelectAddressActivity.this, cityDatas));
        mViewCity.setWheelBackground(R.color.white_color);
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<>(SelectAddressActivity.this, areaDatas));
        mViewDistrict.setWheelBackground(R.color.white_color);
    }

    @Override
    public void onScrollingStarted(WheelView wheel) {
    }

    @Override
    public void onScrollingFinished(WheelView wheel) {
        switch (wheel.getId()) {
            case R.id.id_province:
                currentPostionPro = wheel.getCurrentItem();
                getHttpAddressData(provinceDatas.get(wheel.getCurrentItem()).areaCode, CITY_AREALEVEL);
                break;
            case R.id.id_city:
                currentPostionCity = wheel.getCurrentItem();
                getHttpAddressData(cityDatas.get(wheel.getCurrentItem()).areaCode, AREA_AREALEVEL);
                break;
            case R.id.id_area:
                currentPostionArea = wheel.getCurrentItem();
                break;
            case R.id.id_street:
                currentPostionStreet = wheel.getCurrentItem();
                break;
        }
    }
}
