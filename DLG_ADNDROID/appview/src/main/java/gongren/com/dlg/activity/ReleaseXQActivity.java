package gongren.com.dlg.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.help.Tip;
import com.common.string.LogUtils;
import com.common.sys.EmojiFilter;
import com.common.utils.DateUtils;
import com.common.utils.SharedPreferencesUtils;
import com.common.utils.StringUtils;
import com.common.view.calendar.DatePickerController;
import com.common.view.calendar.DayPickerView;
import com.common.view.calendar.SimpleMonthAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.adapter.DateAdapter;
import gongren.com.dlg.javabean.ReceiveReturnIdJson;
import gongren.com.dlg.javabean.RefreshEvent;
import gongren.com.dlg.javabean.SelectData;
import gongren.com.dlg.javabean.master.masterlist.DataBean;
import gongren.com.dlg.pickerview.DatePackerUtil;
import gongren.com.dlg.pickerview.LoopListener;
import gongren.com.dlg.pickerview.LoopView;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.DialogUtils;
import gongren.com.dlg.utils.GsonUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 雇主发布需求页
 */
public class ReleaseXQActivity extends BaseEditActivity implements DatePickerController {

    private final int SELECT_LG_TYPE = 10;
    private final int SELECT_ADDRESS = 11;
    public static final String EDIT_DATA = "EDIT_DATA"; //编辑零工数据

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.linggong_text)
    EditText linggongText;
    @Bind(R.id.type_text)
    TextView typeText;
    @Bind(R.id.type_select_linear)
    LinearLayout typeSelectLinear;
    @Bind(R.id.desc_tab)
    TextView descTab;
    @Bind(R.id.desc_text)
    EditText descText;
    @Bind(R.id.people_num_text)
    EditText peopleNumText;
    @Bind(R.id.jine_line1)
    View jineLine1;
    @Bind(R.id.xxx_text)
    EditText payText;
    @Bind(R.id.jine_linear)
    LinearLayout jineLinear;
    @Bind(R.id.jine_line2)
    View jineLine2;
    @Bind(R.id.day_rbtn)
    RadioButton dayRbtn;
    @Bind(R.id.hour_rbtn)
    RadioButton hourRbtn;
    @Bind(R.id.unit_linear)
    RelativeLayout unitLinear;
    @Bind(R.id.date_text)
    TextView dateText;
    @Bind(R.id.date_select_linear)
    LinearLayout dateSelectLinear;
    @Bind(R.id.time_text)
    TextView timeText;
    @Bind(R.id.time_select_linear)
    LinearLayout timeSelectLinear;
    @Bind(R.id.address_tab)
    TextView addressTab;
    @Bind(R.id.address_text)
    TextView addressText;
    @Bind(R.id.add_right_img)
    ImageView addRightImg;
    @Bind(R.id.address_select_re)
    RelativeLayout addressSelectRe;
    @Bind(R.id.commit_btn)
    TextView commitBtn;
    @Bind(R.id.checkbox)
    CheckBox checkbox;
    @Bind(R.id.baoxian_linear)
    LinearLayout baoxianLinear;

    //加载独立布局
    private LayoutInflater inflater;

    //选择日期
    private String selectedDate; //选中的日期是一天
    private long startDate;     //开始日期
    private long endDate;       //结束日期
    private DateAdapter dateAdapter;
    private AlertDialog alertDialog;      //时间选择的Dialog
    private String dateString = null;//存储用工日期

    //选择时间
    private List<String> listItem = new ArrayList<>();      //时间选择控件里的时间值
    private String startTime;    //用工开始时间
    private String endTime;     //用工结束时间
    private String startOneTime;    //计件时的用工开始时间
    private boolean isToday = false;//计件时，判断选择的日期，是否是当天。

    //1-工作日，2-双休日，3-计件
    private int release_type = 1;
    //是否选择了义工
    private boolean isYG = false;

    private final String[] tabArray = {"工作日", "双休日", "计件"};
    private String mCityId;
    private String mProId;
    private String mAreaId;
    private String mCity;
    private String mPro;
    private String mArea;
    private String mAdr;
    private SelectData mSelect_data;
    private boolean isOneDay = false;
    private String mStreet;
    private String mStreetId;

    private String employeeParamId;
    private static final int TAG_REQUEST_INVITE = 2;

    private DataBean mDataBean; //编辑数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_xq);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        employeeParamId = getIntent().getStringExtra("employeeId");
        if (null != getIntent().getExtras()) {
            mDataBean = (DataBean) getIntent().getSerializableExtra(EDIT_DATA);
        }
        LogUtils.e("employeeParamId==" + employeeParamId);
        initView();
        if (null != mDataBean) {
            initEidtView();
        }
    }

    //上级Activity发来的消息
    @Subscribe
    public void onMessageEvent(RefreshEvent event) {
        switch (event.getTag()) {
            case RefreshEvent.EDIT_RELEASE_ADESS:
                Tip tip = (Tip) event.getT();
                String district = tip.getDistrict();
                mAreaId = tip.getAdcode();
                mCity = tip.getDistrict();
                mPro = "";
                String districtCity = "";
                if (!TextUtils.isEmpty(district)) {
                    if (district.contains("省")) {
                        mPro = district.split("省")[0] + "省";
                        if (district.split("省").length > 0) {
                            districtCity = district.split("省")[1];
                        }
                    }
                    if (TextUtils.isEmpty(mPro) && district.contains("市")) {
                        mPro = district.split("市")[0] + "市";
                        mCity = district.split("市")[0] + "市";
                        if (district.split("市").length > 0) {
                            districtCity = district.split("市")[1];
                        }
                        mArea = districtCity;
                    }
                    if (TextUtils.isEmpty(mPro) && district.contains("区")) {
                        mPro = district.split("区")[0] + "区";
                        if (district.split("区").length > 0) {
                            districtCity = district.split("区")[1];
                        }
                        mArea = districtCity;
                    }
                }
                if (!TextUtils.isEmpty(districtCity)) {
                    if (districtCity.contains("市")) {
                        mCity = district.split("市")[0];
                        if (district.split("市").length > 0) {
                            mArea = district.split("省")[1];
                        }
                    }
                }
                mAdr = tip.getAddress() + tip.getName().replaceAll(tip.getDistrict(), "");
                addressText.setText(mPro + mCity + mArea + mAdr);
                break;
        }
    }

    /**
     * 编辑页面数据初始化
     */
    private void initEidtView() {
        mAreaId = "";
        tvTitle.setText("编辑零工");
        linggongText.setText(mDataBean.postName); //名称
        typeText.setText(mDataBean.postTypeName); //类型
        descText.setText(mDataBean.taskDescription); //描述
        peopleNumText.setText(mDataBean.recruitNumber + ""); //人数
        String price = String.valueOf(mDataBean.price);
        payText.setText(price); //报酬
        release_type = mDataBean.demandType;//零工计量单位(1.天,2.时,3.单)
        tablayout.getTabAt(release_type - 1).select();
        setReleaseTypeView(release_type);
        //判断是否选择了义工，用来显示，隐藏金额的部分.
        if (mDataBean.postTypeName.contains("志愿义工")) {
            isYG = true;
        } else {
            isYG = false;
        }
        if (mDataBean.isFarmersInsurance == 1) {
            checkbox.setChecked(true);
        } else {
            checkbox.setChecked(false);
        }
        setYGView(isYG);

        checkbox.setChecked(mDataBean.isFarmersInsurance == 1);

        if (release_type == 3) {
            dateText.setText(getData(mDataBean.startMonth, mDataBean.startDay, mDataBean.startYear));
            String time = StringUtils.get2Str(mDataBean.startHour) + ":" + StringUtils.get2Str(mDataBean.startMinute);
            isOneDay = true;
            isToday = true;
            timeText.setText(time);
        } else {
            String startData = getData(mDataBean.startMonth, mDataBean.startDay, mDataBean.startYear);
            String endData = getData(mDataBean.endMonth, mDataBean.endDay, mDataBean.endYear);
            String startTime = StringUtils.get2Str(mDataBean.startHour) + ":" + StringUtils.get2Str(mDataBean.startMinute);
            String endTime = StringUtils.get2Str(mDataBean.endHour) + ":" + StringUtils.get2Str(mDataBean.endMinute);
            if (startData.equals(endData)) {//如果是单个一天的话，我们只需要显示出来其中的
                isOneDay = true;
                isToday = true;
                dateText.setText(startData);
            } else {
                isOneDay = false;
                isToday = false;
                dateText.setText(startData + "-" + endData);
            }
            timeText.setText(startTime + "-" + endTime);
        }
        mCity = mDataBean.cityName;
        mPro = mDataBean.provinceName;
        mArea = mDataBean.areaName;
        mAdr = mDataBean.workAddress;
        addressText.setText(mPro + mCity + mArea + mAdr);
    }

    public String getData(int month, int day, int year) {
        String selectedDate;
        if (month < 9 && day < 10)
            selectedDate = year + ".0" + (month) + ".0" + day;
        else if (month >= 9 && day < 10)
            selectedDate = year + "." + (month) + ".0" + day;
        else if (month < 9 && day >= 10)
            selectedDate = year + ".0" + (month) + "." + day;
        else
            selectedDate = year + "." + (month) + "." + day;
        return selectedDate;
    }

    private void initView() {
        EmojiFilter filter = new EmojiFilter();
        descText.setFilters(new InputFilter[]{filter});
        linggongText.setFilters(new InputFilter[]{filter});
        tvTitle.setText("发布零工");

        mCity = SharedPreferencesUtils.getString(this, "city");
        mAreaId = SharedPreferencesUtils.getString(this, "areaId");
        mPro = SharedPreferencesUtils.getString(this, "pro");
        mProId = SharedPreferencesUtils.getString(this, "proId");
        mCityId = SharedPreferencesUtils.getString(this, "cityId");
        mArea = SharedPreferencesUtils.getString(this, "area");
        mAdr = SharedPreferencesUtils.getString(this, "adr");
        mStreet = SharedPreferencesUtils.getString(this, "street");
        mStreetId = SharedPreferencesUtils.getString(this, "streetId");

        addressText.setText(mPro + mCity + mArea + mAdr);

        for (int i = 0; i < tabArray.length; i++) {
            TabLayout.Tab tab = tablayout.newTab().setText(tabArray[i]);
            tab.setTag(i);
            tablayout.addTab(tab);
        }
        ivRight.setVisibility(View.INVISIBLE);
        inflater = LayoutInflater.from(context);
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch ((int) (tab.getTag())) {
                    case 0:
                        release_type = 1;
                        setReleaseTypeView(release_type);
                        baoxianLinear.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        release_type = 2;
                        setReleaseTypeView(release_type);
                        baoxianLinear.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        release_type = 3;
                        setReleaseTypeView(release_type);
                        baoxianLinear.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }


    @OnClick({R.id.iv_back, R.id.type_select_linear,
            R.id.address_select_re, R.id.date_select_linear, R.id.time_select_linear, R.id.commit_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.type_select_linear://零工类型选择
                go2SelectLGType();
                break;
            case R.id.address_select_re://地址选择
                Intent intent2 = new Intent(context, AddressSearcherActivity.class);
                intent2.putExtra("address", addressText.getText().toString());
                intent2.putExtra("city", mCity);
                intent2.putExtra("isEditRelease", true);
                startActivity(intent2);
                break;
            case R.id.date_select_linear://日期选择
                if (release_type == 3) {//计件，是选择一个日期
                    addDate(true);
                } else {
                    addDate(false);
                }
                break;
            case R.id.time_select_linear://时间选择
                if (release_type == 3) {//计件，是选择一个时间
                    addOneTime();
                } else {
                    addTime();
                }
                break;
            case R.id.commit_btn://发布零工
                checkOutReleaseCondition();

                releaseJob();
                break;
        }
    }

    /**
     * 发布零工
     */
    private void releaseJob() {
        if (!checkOutReleaseCondition())
            return;
        final Dialog dialog = DialogUtils.loadingProgressDialog(this);
        Map<String, Object> map = BaseMapUtils.getMap(this);
        if (null != mDataBean) {
            map.put("id", mDataBean.id);//任务标题
        }
        map.put("type", "1"); //发布零工  2 发布服务
        map.put("format", "json");
        map.put("userId", SharedPreferencesUtils.getString(this, SharedPreferencesUtils.USERID));
        if (null != mSelect_data) {
            map.put("postType", mSelect_data.getDataCode());//岗位类型
        }
        map.put("postTypeName", typeText.getText().toString());//工作类型
        map.put("postName", linggongText.getText().toString());//工作名称
        map.put("taskTitle", linggongText.getText().toString());//任务标题
        map.put("taskDescription", descText.getText().toString());//工作描述
        map.put("price", isYG ? "" : payText.getText().toString());//单价
        map.put("totalPrice", "");//总价
        map.put("recruitNumber", peopleNumText.getText().toString());//招聘人数
        map.put("surplusRecruitNumber", peopleNumText.getText().toString());//剩余招聘人数
        if (checkbox.isChecked()) {
            map.put("isFarmersInsurance", 1);
        } else {
            map.put("isFarmersInsurance", 0);
        }
        if (release_type == 1 || release_type == 2) {
            map.put("jobMeterUnit", isYG ? "0" : (dayRbtn.isChecked() ? "1" : "2"));//零工计量单位(1.天,2.时,3.单)
            map.put("jobMeterUnitName", isYG ? "" : (dayRbtn.isChecked() ? "天" : "时"));//计量单位
        } else if (!typeText.getText().toString().equals("志愿义工")) {
            map.put("jobMeterUnit", isYG ? "" : "3");//零工计量单位(1.天,2.时,3.单)
            map.put("jobMeterUnitName", isYG ? "" : "单");//计量单位
        }
        map.put("demandType", release_type + "");//发布类型 1.工作日 2.双休日 3.计件
        map.put("sex", "3");//性别(1.男,2女,3男女不限)
        //这不需要传
//            map.put("provinceId", mProId);//市区id
//            map.put("cityId", mCityId);//市id

//            map.put("villageId", SharedPreferencesUtils.getString(this, "streetId"));//镇id
        map.put("areaId", mAreaId);//地区id
        map.put("provinceName", mPro);//省市
        map.put("cityName", mCity);//市区
        map.put("areaName", mArea);//区
        map.put("villageName", mStreet);//镇名称
        map.put("workAddress", mAdr);//工作地址
        map.put("client", "ANDROID");//发布任务的客服端
        String date = dateText.getText().toString();
        String time = timeText.getText().toString();
        if (release_type == 3) {
            map.put("startTime", date.replaceAll("\\.", "-") + " " + time + ":01");
            map.put("endTime", "");//结束日期
        } else {
            if (isOneDay) {
                map.put("startTime", date.replaceAll("\\.", "-") + " " + time.toString().split("-")[0].replaceAll("当日", "") + ":01");//开始时间
                map.put("endTime", date.replaceAll("\\.", "-") + " " + time.toString().split("-")[1].replaceAll("次日", "") + ":01");//结束时间
            } else {
                map.put("startTime", date.split("-")[0].replaceAll("\\.", "-") + " " + time.toString().split("-")[0].replaceAll("当日", "") + ":01");//开始时间
                map.put("endTime", date.split("-")[1].replaceAll("\\.", "-") + " " + time.toString().split("-")[1].replaceAll("次日", "") + ":01");//结束时间
            }
        }
        String url = GetDataConfing.RELEASE_JOB;
        if (null != mDataBean) {
            url = GetDataConfing.EDIT_RELEASE_JOB;
        }

        //将release_type作为tag值，来区分发布类型
        DataUtils.loadData(this, url, map, release_type, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    if (dataRequest.isNetError()) {
                        ShowGetDataError.showNetError(context);
                    } else {
                        String json = dataRequest.getResponseData();
                        try {
                            JSONObject object = new JSONObject(json);
                            ToastUtils.showToastShort(ReleaseXQActivity.this, object.optString("msg"));
                            if ("0".equals(object.optString("code"))) {//成功逻辑
                                JSONArray jsonArray = object.getJSONArray("data");
                                if (null != jsonArray && !TextUtils.isEmpty(employeeParamId)) {
                                    invitationEmployee(jsonArray.get(0).toString());
                                } else {
                                    finish();
                                }
                            } else {//失败逻辑
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void invitationEmployee(String taskId) {
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("userId", SharedPreferencesUtils.getString(this, SharedPreferencesUtils.USERID));
        map.put("employeeId", employeeParamId);
        map.put("taskId", taskId);
        DataUtils.loadData(context, GetDataConfing.getJobTaskAskByEmployee, map, TAG_REQUEST_INVITE, responseDataCallback);
    }


    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            if (dataRequest.isNetError()) {
                ShowGetDataError.showNetError(context);
            } else {
                String json = dataRequest.getResponseData();
                if (!TextUtils.isEmpty(json)) {
                    if (dataRequest.getWhat() == TAG_REQUEST_INVITE) {
                        //邀请
                        ReceiveReturnIdJson returnIdJson = GsonUtils.jsonToBean(json, ReceiveReturnIdJson.class);
                        if (returnIdJson.code.equals("0")) {
                            finish();//邀请成功后 关闭页面
                            ToastUtils.showToastShort(context, "发起邀请成功！");
                        } else {
                            ToastUtils.showToastShort(context, "邀请失败");
                        }
                    }
                }
            }
        }
    };

    /**
     * 检查是否符合发布条件
     */
    private boolean checkOutReleaseCondition() {
        String desc = descText.getText().toString();

        if (TextUtils.isEmpty(linggongText.getText())) {
            ToastUtils.showToastShort(this, "请输入零工名称");
            return false;
        } else if (TextUtils.isEmpty(typeText.getText())) {
            ToastUtils.showToastShort(this, "请输入零工类型");
            return false;
        } else if (desc.length() < 5) {
            ToastUtils.showToastLong(this, "零工描述不能少于5个字");
            return false;
        } else if (TextUtils.isEmpty(descText.getText())) {
            ToastUtils.showToastShort(this, "请输入零工描述内容");
            return false;
        } else if (TextUtils.isEmpty(peopleNumText.getText())) {
            ToastUtils.showToastShort(this, "请输入零工需求数量");
            return false;
        } else if (TextUtils.isEmpty(payText.getText()) && !isYG) {
            ToastUtils.showToastShort(this, "请输入支付报酬");
            return false;
        } else if (TextUtils.isEmpty(dateText.getText())) {
            ToastUtils.showToastShort(this, "请输入零工日期");
            return false;
        } else if (TextUtils.isEmpty(timeText.getText())) {
            ToastUtils.showToastShort(this, "请输入零工时间");
            return false;
        } else if (TextUtils.isEmpty(addressText.getText())) {
            ToastUtils.showToastShort(this, "请输入零工地址");
            return false;
        } else if (!"2".equals(SharedPreferencesUtils.getString(this, SharedPreferencesUtils.RENZHENG_STATUS))) {
            ToastUtils.showToastShort(this, "请进行实名认证");
            return false;
        }
        return true;
    }

    //根据tab选择的，去控制控件
    public void setReleaseTypeView(int release_type) {
        if (release_type == 1 || release_type == 2) {//工作日，双休
            dayRbtn.setVisibility(View.VISIBLE);
            dayRbtn.setChecked(true);
            hourRbtn.setText("小时");
        } else {//计件
            dayRbtn.setVisibility(View.GONE);
            hourRbtn.setText("单");
            hourRbtn.setChecked(true);
        }

        //清空已选的日期，时间.
        dateText.setText("");
        timeText.setText("");
    }

    public void go2SelectLGType() {
        String current_select = typeText.getText().toString();

        Intent intent = new Intent(context, LGTypeActivity.class);
        intent.putExtra("current_select", current_select);
        startActivityForResult(intent, SELECT_LG_TYPE);
    }

    //---------------------
    //选择单个的用工时间

    /**
     * 添加用工时间
     */
    private void addOneTime() {
        listItem = DatePackerUtil.getTimeAllList();
        if (listItem == null)
            listItem = new ArrayList<>();
        final View timeView = inflater.inflate(R.layout.item_pickerview_onetime, null);
        final LoopView loopView1 = (LoopView) timeView.findViewById(R.id.loopView);
        Button btnCancle = (Button) timeView.findViewById(R.id.btn_cancle);//关闭按钮
        Button btnOk = (Button) timeView.findViewById(R.id.btn_ok);    //确定按钮
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog != null && alertDialog.isShowing())
                    alertDialog.cancel();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentTime = DateUtils.dateTimeFormat("yyyy.MM.dd HH:mm", System.currentTimeMillis() + "");
                String select = selectedDate + " " + startOneTime;
                if (select.compareTo(currentTime) <= 0) {//小于等于当前时间
                    ToastUtils.showToastShort(context, "开始时间是在当前时间之后");
                    return;
                }
                timeText.setText(startOneTime);
                if (alertDialog != null && alertDialog.isShowing())
                    alertDialog.cancel();
            }
        });
        loopView1.setList(listItem);
        loopView1.setNotLoop();
        defaultTime(loopView1);
        loopView1.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                startOneTime = listItem.get(item);
            }
        });

        alertDialog = DialogUtils.showViewDialog(context, timeView);
    }

    //---------------------默认时间
    private void defaultTime(LoopView loopView) {
        int hour = DateUtils.getHour();
        int minute = DateUtils.getMinute();
        if (minute < 30) {
            loopView.setCurrentItem(hour * 2 + 1);
        } else {
            loopView.setCurrentItem((hour + 1) * 2);
        }
    }
    //---------------------
    //选择时间

    /**
     * 添加用工时间
     */
    private void addTime() {
        listItem = DatePackerUtil.getTimeAllList();
        if (listItem == null)
            listItem = new ArrayList<>();
        final View timeView = inflater.inflate(R.layout.item_pickerview_time, null);
        final LoopView loopView1 = (LoopView) timeView.findViewById(R.id.loopView1);
        final LoopView loopView2 = (LoopView) timeView.findViewById(R.id.loopView2);
        Button btnCancle = (Button) timeView.findViewById(R.id.btn_cancle);//关闭按钮
        Button btnOk = (Button) timeView.findViewById(R.id.btn_ok);    //确定按钮
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog != null && alertDialog.isShowing())
                    alertDialog.cancel();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int startTimeInt = StringUtils.replaceTime(startTime);    //开始时间格式化成int值
                int endTimeInt = StringUtils.replaceTime(endTime);        //结束时间格式化成int值
                if (startTimeInt == endTimeInt) {
                    ToastUtils.showToastShort(context, "开始时间不能等于结束时间");
                    return;
                }

                if (isToday) {//如果是从当天开始的，那就要判断，起点时间是否在当前时间之前。
                    String currentTime = DateUtils.dateTimeFormat("yyyy.MM.dd HH:mm", System.currentTimeMillis() + "");
                    String selectdate = DateUtils.dateFormat("yyyy.MM.dd", startDate + "");
                    String select = selectdate + " " + startTime;
                    if (select.compareTo(currentTime) <= 0) {//小于等于当前时间
                        ToastUtils.showToastShort(context, "开始时间不能早于现在时间");
                        return;
                    }
                }

                //判断是否是跨天的时间段
                if (startTimeInt < endTimeInt) {
                    //如果是当天的时间段
                    timeText.setText(startTime + "-" + endTime);
                } else {
                    //如果是跨天的时间段
                    timeText.setText("当日" + startTime + "-次日" + endTime);
                    ToastUtils.showToastShort(context, "您选择的是跨天任务");
                }

                if (alertDialog != null && alertDialog.isShowing())
                    alertDialog.cancel();
            }
        });
        loopView1.setList(listItem);
        loopView1.setNotLoop();
        defaultTime(loopView1);
        loopView1.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                startTime = listItem.get(item);
            }
        });
        loopView2.setList(listItem);
        loopView2.setNotLoop();
        loopView2.setCurrentItem(0);
        loopView2.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                endTime = listItem.get(item);
            }
        });
        alertDialog = DialogUtils.showViewDialog(context, timeView);
    }
    //---------------------


    //---------------------

    /**
     * 添加用工日期
     *
     * @param isJJ 判断是否是计件
     */
    private void addDate(final boolean isJJ) {
        selectedDate = null;
        startDate = 0;
        endDate = 0;
        View dateView = inflater.inflate(R.layout.item_pickerview_date, null);
        DayPickerView dayPickerView = (DayPickerView) dateView.findViewById(R.id.pickerView);
        Button btnCancle = (Button) dateView.findViewById(R.id.btn_cancle);
        Button btnOK = (Button) dateView.findViewById(R.id.btn_ok);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alertDialog != null && alertDialog.isShowing())
                    alertDialog.dismiss();
            }
        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isJJ) {//是计件，只选择一个日期 或者是单日工作量的工作
                    selectOneDate();
                } else {//不是计件，选择2个日期
                    select2Date();
                }
                //切换日期，清空时间
                timeText.setText("");
            }
        });
//        SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> daySelectedDays = new SimpleMonthAdapter.SelectedDays<>();
//        daySelectedDays.setFirst(new SimpleMonthAdapter.CalendarDay());
//        daySelectedDays.setLast(new SimpleMonthAdapter.CalendarDay());
        dayPickerView.setController(this);
        alertDialog = DialogUtils.showViewDialog(context, dateView);
    }

    public void selectOneDate() {
        if (selectedDate == null) {
            ToastUtils.showToastShort(context, "请选择一个用工日期");
            return;
        }
        //判断选中的日期是否在当日以及当日之前
        int currentDate = Integer.parseInt(DateUtils.dateFormat("yyyyMMdd", System.currentTimeMillis() + ""));

        if (StringUtils.replaceDate(selectedDate) < currentDate) {
            ToastUtils.showToastShort(context, "用工日期不得早于当前日期");
            return;
        } else if (StringUtils.replaceDate(selectedDate) == currentDate) {//是当天
            isToday = true;
        } else {//不是当天
            isToday = false;
        }
        dateString = selectedDate;
        dateText.setText(dateString);

        if (alertDialog != null && alertDialog.isShowing())
            alertDialog.dismiss();
    }

    public void select2Date() {

        if (selectedDate != null && startDate == 0 && endDate == 0) {
            ToastUtils.showToastShort(context, "请选择用工起点日期，终点日期");
            return;
        }
        //判断选中的日期是否在当日以及当日之前
        int currentDate = Integer.parseInt(DateUtils.dateFormat("yyyyMMdd", System.currentTimeMillis() + ""));
        if ((selectedDate == null && Integer.parseInt(DateUtils.dateFormat("yyyyMMdd", startDate + "")) < currentDate)
                || (selectedDate != null && StringUtils.replaceDate(selectedDate) < currentDate)) {
            ToastUtils.showToastShort(context, "如选择当天日期，再次点击该日期");
            return;
        }

        //判断是否是当天
        if (selectedDate == null && Integer.parseInt(DateUtils.dateFormat("yyyyMMdd", startDate + "")) == currentDate) {
            isToday = true;
        } else {
            isToday = false;
        }
        String start = DateUtils.dateFormat("yyyy.MM.dd", startDate + "");
        String end = DateUtils.dateFormat("yyyy.MM.dd", endDate + "");
        if (start.equals(end)) {//如果是单个一天的话，我们只需要显示出来其中的
            dateString = start;
            isOneDay = true;
            isToday = true;
        } else {
            dateString = start + "-" + end;
            isOneDay = false;
            isToday = false;
        }
        dateText.setText(dateString);

        if (alertDialog != null && alertDialog.isShowing())
            alertDialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //---------------------
    private Intent mIntent;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_LG_TYPE://零工类型选择
                    if (data != null) {
                        mSelect_data = (SelectData) data.getSerializableExtra("select_type");
                        typeText.setText(mSelect_data.getDataName() + "");

                        //判断是否选择了义工，用来显示，隐藏金额的部分.
                        if (mSelect_data.getDataName().contains("志愿义工")) {
                            isYG = true;
                        } else {
                            isYG = false;
                        }
                        setYGView(isYG);
                    }
                    break;
            }
        }
    }

    //判断是否选择了义工，显示隐藏金额部分。
    public void setYGView(boolean isyg) {
        if (isyg) {//是义工
            jineLine1.setVisibility(View.GONE);
            jineLine2.setVisibility(View.GONE);
            jineLinear.setVisibility(View.GONE);
            unitLinear.setVisibility(View.GONE);
        } else {//不是义工
            jineLine1.setVisibility(View.VISIBLE);
            jineLine2.setVisibility(View.VISIBLE);
            jineLinear.setVisibility(View.VISIBLE);
            unitLinear.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public int getMaxYear() {
        return Integer.parseInt(DateUtils.dateFormat("yyyy", System.currentTimeMillis() + "")) + 1;
    }

    int number = 0;//防止二次以上选择 导致判断逻辑的错误

    @Override
    public void onDayOfMonthSelected(int year, int month, int day) {
        //两次调用这个方法，onDateRangeSelected方法被调用一次，单次不能点击确认
        number++;
        if (number % 2 == 1 && release_type != 3) {
            endDate = 0;
            startDate = 0;
            selectedDate = null;
            return;
        }
        if (month < 9 && day < 10)
            selectedDate = year + ".0" + (month + 1) + ".0" + day;
        else if (month >= 9 && day < 10)
            selectedDate = year + "." + (month + 1) + ".0" + day;
        else if (month < 9 && day >= 10)
            selectedDate = year + ".0" + (month + 1) + "." + day;
        else
            selectedDate = year + "." + (month + 1) + "." + day;
    }

    @Override
    public void onDateRangeSelected(SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays) {
        long firstTime = selectedDays.getFirst().getDate().getTime();
        long lastTime = selectedDays.getLast().getDate().getTime();
        number = 0;//重置为 0
        if (Math.abs(lastTime - firstTime) < 24 * 60 * 60 * 1000) {//时间小于一天就说明是一天
            selectedDate = DateUtils.dateFormat("yyyy.MM.dd", firstTime + "");
            startDate = endDate = firstTime;
        } else {
            startDate = lastTime > firstTime ? firstTime : lastTime;
            endDate = lastTime > firstTime ? lastTime : firstTime;
            selectedDate = null;
        }

    }
}
