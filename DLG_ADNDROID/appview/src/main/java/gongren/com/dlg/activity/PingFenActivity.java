package gongren.com.dlg.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aym.util.json.JsonMap;
import aym.util.json.JsonParseHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import gongren.com.dlg.R;
import gongren.com.dlg.adapter.PJWordsAdapter;
import gongren.com.dlg.javabean.DoingTaskOrderDetailModel;
import gongren.com.dlg.javabean.GuYuanOrderItem;
import gongren.com.dlg.javabean.RefreshEvent;
import gongren.com.dlg.javabean.SelectData;
import gongren.com.dlg.javabean.master.masterlist.ListBean;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import com.common.utils.DateUtils;
import gongren.com.dlg.utils.DialogUtils;
import gongren.com.dlg.utils.LogUtils;
import com.common.utils.SharedPreferencesUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 雇主，我的零工列表，已完成->评分
 */
public class PingFenActivity extends BaseEditActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    @Bind(R.id.checkbox)
    CheckBox checkbox;
    @Bind(R.id.iv_head)
    CircleImageView ivHead;
    @Bind(R.id.name_text)
    TextView nameText;
    @Bind(R.id.fen_text)
    TextView fenText;
    @Bind(R.id.starbar_01)
    RatingBar starbar01;
    @Bind(R.id.call_btn)
    ImageView callBtn;
    @Bind(R.id.people_line)
    View peopleLine;
    @Bind(R.id.ordername_text)
    TextView ordernameText;
    @Bind(R.id.type_text)
    TextView typeText;
    @Bind(R.id.time_text)
    TextView timeText;
    @Bind(R.id.income_text)
    TextView incomeText;
    @Bind(R.id.order_head)
    LinearLayout orderHead;
    @Bind(R.id.starbar)
    RatingBar starbar;
    @Bind(R.id.pingfen_detail_text)
    TextView pingfenDetailText;
    @Bind(R.id.pf_words_gridview)
    GridView pfWordsGridview;
    @Bind(R.id.desc_edit)
    EditText descEdit;
    @Bind(R.id.commit_btn)
    Button commitBtn;
    private PJWordsAdapter pjWordsAdapter;
    private List<SelectData> wordsList = new ArrayList<>();

    //判断是雇员，还是雇主的评价页
    private boolean isGuYuan = false;
    private BaseEditActivity activity = null;

    private String businessNumber = "";//订单标识
    private String accepterUserId = "";//被评价人id
    private String appraiserUserId = "";//评价人id

    private final int TAG_tabs = 10;//获取标签
    private final int TAG_fen_str = 11;//获取选分显示的字
    private final int TAG_userinfo = 12;//获取用户信息
    private final int TAG_commit = 13;//提交评价
    private List<JsonMap<String, Object>> goodList = new ArrayList<>();//好评的标签
    private List<JsonMap<String, Object>> badList = new ArrayList<>();//差评的标签
    private List<JsonMap<String, Object>> showList = new ArrayList<>();//显示的标签

    private List<JsonMap<String, Object>> fenList = new ArrayList<>();//选不同的分，显示不同的文字。
    private ListBean mListBean;
    private int demandType;
    private GuYuanOrderItem orderItem; //雇员订单信息
    private DoingTaskOrderDetailModel infoCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pingfen);
        ButterKnife.bind(this);
        activity = this;

        getParams();
        initView();

        //获取标签
        getTabs();
        //获取选分显示的文字。
        getSelectFenWords();
    }

    public void getParams() {
        Intent intent = getIntent();
        if (intent != null) {
            boolean isGuYuan = intent.getBooleanExtra("isGuYuan", false);
            if (isGuYuan) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    this.isGuYuan = bundle.getBoolean("isGuYuan");
                    if (this.isGuYuan) {
                        orderItem = (GuYuanOrderItem) bundle.getSerializable("orderInfo");
                        if(null == orderItem){
                            orderItem = new GuYuanOrderItem();
                            infoCard = (DoingTaskOrderDetailModel) bundle.getSerializable("orderInfo_card");
                            orderItem.setBusinessNumber(infoCard.businessNumber);
                            orderItem.setEmployeeId(infoCard.employeeId);
                            orderItem.setEmployerId(infoCard.employerId);
                            orderItem.setPostName(infoCard.postName);
                            orderItem.setDemandType(infoCard.demandType+"");
                            orderItem.setPrice(infoCard.price+"");
                            orderItem.setMeterUnitName(infoCard.meterUnitName+"");
                            orderItem.setOrderTimeResultRpcVo(null);
                        }
                        businessNumber = orderItem.getBusinessNumber();
//                        bundle.getString("businessNumber");
                        accepterUserId = orderItem.getEmployerId();
//                                bundle.getString("accepterUserId");
                        appraiserUserId = orderItem.getEmployeeId();
//                                bundle.getString("appraiserUserId");

//                        orderInfo = new JsonMap<String, Object>();
//                        orderInfo.put("postName", bundle.getString("postName"));
//                        orderInfo.put("demandType", bundle.getInt("demandType"));
//                        orderInfo.put("price", bundle.getInt("price"));
//                        orderInfo.put("meterUnitName", bundle.getString("meterUnitName"));
//
//                        JsonMap<String, Object> timeMap = new JsonMap<String, Object>();
//                        timeMap.put("startYear", bundle.getInt("startYear"));
//                        timeMap.put("startMonth", bundle.getInt("startMonth"));
//                        timeMap.put("startDay", bundle.getInt("startDay"));
//                        timeMap.put("startHour", bundle.getInt("startHour"));
//                        timeMap.put("startMinute", bundle.getInt("startMinute"));
//                        timeMap.put("startSecond", bundle.getInt("startSecond"));
//
//                        timeMap.put("endYear", bundle.getInt("endYear"));
//                        timeMap.put("endMonth", bundle.getInt("endMonth"));
//                        timeMap.put("endDay", bundle.getInt("endDay"));
//                        timeMap.put("endHour", bundle.getInt("endHour"));
//                        timeMap.put("endMinute", bundle.getInt("endMinute"));
//                        timeMap.put("endSecond", bundle.getInt("endSecond"));
//
//                        orderInfo.put("orderTimeResultRpcVo", timeMap);
                    }
                }
            } else {//不是雇员的话
                mListBean = (ListBean) intent.getSerializableExtra("item");
                businessNumber = mListBean.businessNumber;
            }
        }
    }

    private void initView() {
        tvTitle.setText("评价");
        ivRight.setVisibility(View.INVISIBLE);
        checkbox.setVisibility(View.GONE);
        //根据是否是雇员，雇主，分别显示2个头部
        if (isGuYuan) {
            findViewById(R.id.guyuan_head).setVisibility(View.GONE);
            findViewById(R.id.order_head).setVisibility(View.VISIBLE);
            //设置订单信息。
            setOrderViewDatas();
        } else {
            findViewById(R.id.order_head).setVisibility(View.GONE);
            findViewById(R.id.guyuan_head).setVisibility(View.VISIBLE);
            //获取用户信息
            setGuZhuView();
        }

        starbar.setOnRatingBarChangeListener(new RatingBarListener());
        pjWordsAdapter = new PJWordsAdapter(context, showList);
        pfWordsGridview.setAdapter(pjWordsAdapter);

        starbar.setRating(1f);//星级默认是0分
        pfWordsGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JsonMap<String, Object> data = showList.get(position);
                if (data.getBoolean("isChecked", false)) {
                    data.put("isChecked", false);
                } else {
                    data.put("isChecked", true);
                }
                showList.set(position, data);
                pjWordsAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 雇员获取数据
     */
    private void setOrderViewDatas() {
        if (orderItem != null) {
            //任务名称
            ordernameText.setText(orderItem.getPostName());
            //需求类型(1.工作日,2.双休日,3.计件)
            demandType = TextUtils.isEmpty(orderItem.getDemandType()) ? -1 : Integer.parseInt(orderItem.getDemandType());
            switch (demandType) {
                case 1:
                    typeText.setText("工作日");
                    break;
                case 2:
                    typeText.setText("双休日");
                    break;
                case 3:
                    typeText.setText("计件");
                    break;
            }
            //单价
            incomeText.setText(orderItem.getPrice() + "元/" + orderItem.getMeterUnitName());
            //日期时间
            GuYuanOrderItem.OrderTimeResultRpcVoBean dateData = orderItem.getOrderTimeResultRpcVo();
            if (dateData != null) {
                //设置订单时间
                setOrderDate(dateData);
            }else {
                if(null != infoCard){
                    String startDate = infoCard.startDate;
                    String endDate = infoCard.endDate;

                    int startYear = DateUtils.getDate_yyyy(startDate);
                    int endYear = DateUtils.getDate_yyyy(endDate);
                    int startMonth = DateUtils.getDate_MM(startDate);
                    int endMonth = DateUtils.getDate_MM(endDate);
                    int startDay = DateUtils.getDate_dd(startDate);
                    int endDay = DateUtils.getDate_dd(endDate);
                    int startHour = DateUtils.getDate_HH(startDate);
                    int endHour = DateUtils.getDate_HH(endDate);
                    int startMinute = DateUtils.getDate_mm(startDate);
                    int endMinute = DateUtils.getDate_mm(endDate);
                    String time = DateUtils.getTimeShow(infoCard.demandType, startYear, startMonth,
                            startDay, startHour, startMinute, endYear, endMonth, endDay, endHour, endMinute);
                    timeText.setText(time);
                }
            }
        }
    }

    public void setOrderDate(GuYuanOrderItem.OrderTimeResultRpcVoBean dateData) {
        int startYear = TextUtils.isEmpty(dateData.getStartYear()) ? 0 : Integer.parseInt(dateData.getStartYear());
        int startMonth = TextUtils.isEmpty(dateData.getStartMonth()) ? 0 : Integer.parseInt(dateData.getStartMonth());
        int startDay = TextUtils.isEmpty(dateData.getStartDay()) ? 0 : Integer.parseInt(dateData.getStartDay());
        int startHour = TextUtils.isEmpty(dateData.getStartHour()) ? 0 : Integer.parseInt(dateData.getStartHour());
        int startMinute = TextUtils.isEmpty(dateData.getStartMinute()) ? 0 : Integer.parseInt(dateData.getStartMinute());
        int startSecond = TextUtils.isEmpty(dateData.getStartSecond()) ? 0 : Integer.parseInt(dateData.getStartSecond());

        int endYear = TextUtils.isEmpty(dateData.getEndYear()) ? 0 : Integer.parseInt(dateData.getEndYear());
        int endMonth =TextUtils.isEmpty(dateData.getEndMonth()) ? 0 : Integer.parseInt(dateData.getEndMonth());
        int endDay = TextUtils.isEmpty(dateData.getEndDay()) ? 0 : Integer.parseInt(dateData.getEndDay());
        int endHour = TextUtils.isEmpty(dateData.getEndHour()) ? 0 : Integer.parseInt(dateData.getEndHour());
        int endMinute = TextUtils.isEmpty(dateData.getEndMinute()) ? 0 : Integer.parseInt(dateData.getEndMinute());
        int endSecond = TextUtils.isEmpty(dateData.getEndSecond()) ? 0 : Integer.parseInt(dateData.getEndSecond());
        String dateStr = DateUtils.getTimeShow(demandType, startYear, startMonth, startDay, startHour, startMinute
                , endYear, endMonth, endDay, endHour, endMinute);

        timeText.setText(dateStr);
    }

    private class RatingBarListener implements RatingBar.OnRatingBarChangeListener {
        public void onRatingChanged(RatingBar ratingBar, float rating,
                                    boolean fromUser) {
            Log.w("等级：", rating + "");//评分
            Log.w("星星：", ratingBar.getNumStars() + "");//总分数

            pingfenDetailText.setText(getFenWords((int) rating));
            //切换下面的标签
            showList.clear();
            if (rating >= 5) {
                showList.addAll(goodList);
            } else {
                showList.addAll(badList);
            }
            pjWordsAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取用户可选的标签
     */
    private void getTabs() {
        Map<String, Object> map = BaseMapUtils.getMap(activity);
        if (isGuYuan)
            map.put("groupCode", "employee.evaluation");//雇员评价雇主
        else
            map.put("groupCode", "employer.evaluation");//雇主评价标签
        map.put("format", "json");
        DataUtils.loadData(activity, GetDataConfing.OrderPingJiaTabs, map, TAG_tabs, responseDataCallback);
    }

    /**
     * 获取用户选分显示的文字
     */
    private void getSelectFenWords() {
        Map<String, Object> map = BaseMapUtils.getMap(activity);
        if (isGuYuan)
            map.put("groupCode", "employee.evaluation.describe");//雇员评价星级内容
        else
            map.put("groupCode", "employer.evaluation.describe");
        map.put("format", "json");
        DataUtils.loadData(activity, GetDataConfing.OrderPingJiaTabs, map, TAG_fen_str, responseDataCallback);
    }
     Dialog dialog = null;
    /**
     * 提交评价
     */
    private void commitPJ() {
        //根据用户选中的标签，获取要传的参数。
        String tabs = getTabsFromView();
        String desc = descEdit.getText().toString();
        float fen = starbar.getRating();
        dialog = DialogUtils.loadingProgressDialog(this);
        Map<String, Object> map = new HashMap<>();
        map.put("userId", SharedPreferencesUtils.getString(context, SharedPreferencesUtils.USERID));
        map.put("orderBusinessNumber", businessNumber);//订单编号
        if (isGuYuan) {
            map.put("accepterUserId", accepterUserId);//被评价人id
            map.put("appraiserUserId", appraiserUserId);//评价人id
        } else {
            map.put("accepterUserId", mListBean.employeeId);//被评价人id
            map.put("appraiserUserId", mListBean.employerId);//评价人id
        }

        map.put("evaluateContent", desc);//其他说明
        map.put("evaluateTag", tabs);//所选评价标签
        map.put("evaluateLevel", String.valueOf((int) fen));//评价等级
        map.put("format", "json");
        DataUtils.loadData(activity, GetDataConfing.CommitPJ, map, TAG_commit, responseDataCallback);
    }

    public String getTabsFromView() {
        String result = "";

        for (int i = 0; i < showList.size(); i++) {
            JsonMap<String, Object> jsonmap = showList.get(i);
            if (jsonmap != null && jsonmap.getBoolean("isChecked")) {
                if (DataUtils.isNullStr(result)) {
                    result += jsonmap.getStringNoNull("dataCode");
                } else {
                    result += "&" + jsonmap.getStringNoNull("dataCode");
                }
            }
        }
        return result;
    }

    /**
     * 数据请求回调接口（在回调接口中进行操作）
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            if(null != dialog){
                dialog.dismiss();
            }
            if (dataRequest.isNetError()) {
                ShowGetDataError.showNetError(activity);
            } else {
                String json = dataRequest.getResponseData();
                if (!TextUtils.isEmpty(json)) {
                    JsonMap<String, Object> jsonMap = null;
                    List<JsonMap<String, Object>> data = null;
                    switch (dataRequest.getWhat()) {
                        case TAG_commit://提交评价
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                ToastUtils.showToastShort(PingFenActivity.this, jsonObject.optString("msg"));
                                if ("0".equals(jsonObject.optString("code"))) {
                                    EventBus.getDefault().post(new RefreshEvent("",RefreshEvent.MINE_PAGE_MAP_ORDER_INFO));
                                    //成功逻辑
                                    setResult(200);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case TAG_tabs://获取标签
//                            json = getNetData();
                            LogUtils.logE("测试加载", "获取标签 json=" + json);
                            jsonMap = JsonParseHelper.getJsonMap(json);
                            data = jsonMap.getList_JsonMap("data");
                            LogUtils.logE("测试列表", "获取标签列表 size=" + data.size());
                            if (data == null || data.size() < 1) {
                                goodList.clear();
                                badList.clear();
                            } else {
                                goodList.clear();
                                badList.clear();

                                //根据状态，把值分别放到2个数组中。
                                slipArray(data);
                            }
                            //默认显示差评的标签，因为默认是0分。
                            showList.addAll(badList);
                            //刷新数据
                            pjWordsAdapter.notifyDataSetChanged();
                            break;
                        case TAG_fen_str://获取雇员评价星级内容
                            jsonMap = JsonParseHelper.getJsonMap(json);
                            data = jsonMap.getList_JsonMap("data");
                            String str = "";
                            LogUtils.logE("测试列表", "获取标签列表 size=" + data.size());
                            if (data == null || data.size() < 1) {
                                fenList.clear();
                            } else {
                                fenList.clear();
                                fenList.addAll(data);
                                str = getFenWords(1);
                            }
                            //刷新数据
                            pingfenDetailText.setText(str);
                            break;
                    }
                }
            }
        }
    };

    private void setGuZhuView() {
        Glide.with(context).
                load(mListBean.logo).
                error(R.mipmap.morentouxiang)
                .placeholder(R.mipmap.morentouxiang)
                .into(ivHead);
        nameText.setText(mListBean.name);
        if(null != mListBean){
            if (mListBean.creditCount==null||mListBean.scoreCount==null){
                starbar01.setClickable(false);
                fenText.setText("");
                return;
            }
            fenText.setText(mListBean.creditCount);
            starbar01.setRating(TextUtils.isEmpty(mListBean.scoreCount) ? 5.0f : Float.parseFloat(mListBean.scoreCount));
        }
    }

    private String getFenWords(int fen) {
        for (int i = 0; i < fenList.size(); i++) {
            int specialIdentification = fenList.get(i).getInt("specialIdentification");
            if (fen == specialIdentification) {
                return fenList.get(i).getStringNoNull("dataValue");
            }
        }
        return "";
    }

    private void slipArray(List<JsonMap<String, Object>> data) {
        for (int i = 0; i < data.size(); i++) {
            JsonMap<String, Object> itemdata = data.get(i);
            int specialIdentification = itemdata.getInt("specialIdentification");
            if (specialIdentification == 5) {//好评标签
                goodList.add(itemdata);
            } else {//差评标签
                badList.add(itemdata);
            }
        }
    }

    private String phone;

    @OnClick({R.id.iv_back, R.id.commit_btn, R.id.call_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.commit_btn://提交
                commitPJ();
                break;
            case R.id.call_btn://拨电电话
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(PingFenActivity.this, Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(PingFenActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    } else {
                        callPhone(phone);
                    }
                } else {
                    callPhone(phone);
                }
                break;
        }
    }

    /**
     * 拨打电话
     *
     * @param phone
     */
    private void callPhone(String phone) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPhone(phone);
                } else {
                    ToastUtils.showToastShort(this, "未获取权限");
                }
                break;
        }
    }
}
