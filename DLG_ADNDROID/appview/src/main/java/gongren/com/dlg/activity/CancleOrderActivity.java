package gongren.com.dlg.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
import gongren.com.dlg.adapter.CancleOrderAdapter;
import gongren.com.dlg.javabean.FinishEvent;
import gongren.com.dlg.javabean.MainToWorkerFragmentEvent;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.DialogUtils;
import com.common.utils.SharedPreferencesUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.utils.ViewUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 取消订单的页
 * Created by Administrator on 2017/4/5.
 */
public class CancleOrderActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_save)
    TextView tvSave;
    @Bind(R.id.line1)
    View line1;
    @Bind(R.id.iv_head)
    CircleImageView ivHead;
    @Bind(R.id.fen_text)
    TextView fenText;
    @Bind(R.id.guize_text)
    TextView guizeText;
    @Bind(R.id.income_line)
    View incomeLine;
    @Bind(R.id.income_text)
    TextView incomeText;
    @Bind(R.id.income_linear)
    LinearLayout incomeLinear;
    @Bind(R.id.weiyue_line)
    View weiyueLine;
    @Bind(R.id.xm_text)
    TextView xmText;
    @Bind(R.id.weiyue_linear)
    LinearLayout weiyueLinear;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.scrollview)
    ScrollView scrollview;
    @Bind(R.id.submit_btn)
    TextView submitBtn;
    @Bind(R.id.bottom_linear)
    LinearLayout bottomLinear;

    private Context context;
    private int isfrom = 1;//默认1-是不扣钱的取消订单,2-扣钱的取消订单.
    private boolean isPayd = false;//如果需要
    private int current_select = -1;//取消原因当前选中的是哪一项.

    private String businessNumber;//订单id
    private double price;//报酬
    private double compensatoryPayment;//赔偿金
    private double compensatoryTrusty;//赔偿诚信值
    private String payPassword;//支付密码
    private double baonum;//保险金

    private final int TAG_CancleOrderFare = 12;//获取取消原因.
    private final int TAG_CancleReasons = 10;//获取取消原因.
    private final int TAG_CancleOrder = 11;//取消订单.

    private final int PAY_BACK = 10;//支付成功，回调。

    private CancleOrderAdapter cancleOrderAdapter;
    private Dialog mLoadingDialog;
    private boolean isGuYuan;
    private List<JsonMap<String, Object>> reasonList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancle_order_activity);
        ButterKnife.bind(this);
        context = this;
        EventBus.getDefault().register(this);
        getParams();
        initViews();
        //获取取消原因
        getCancleReasons();
        getCancleOrderFare();
    }
    //下级Fragment发来的消息
    @Subscribe
    public void onMessageEvent(FinishEvent event) {
        finish();
    }

    public void getParams() {
        Intent intent = getIntent();
        if (intent != null) {
            isfrom = intent.getIntExtra("isfrom", 1);
            price = intent.getIntExtra("price", 0);
            compensatoryPayment = intent.getIntExtra("compensatoryPayment", 0);
            compensatoryTrusty = intent.getIntExtra("compensatoryTrusty", 0);
            businessNumber = intent.getStringExtra("businessNumber");
            isGuYuan = intent.getBooleanExtra("isGuYuan", false);// 该界面是雇主和雇员共同订单的界面 必须传递此参数
        }
    }

    private void initViews() {
        ivBack.setVisibility(View.INVISIBLE);
        tvTitle.setText("取消订单");
        tvSave.setText("关闭");
        fenText.setText("扣除诚信值-" + compensatoryTrusty + "分");

        if (isfrom == 1) {//不扣钱的取消订单
            weiyueLine.setVisibility(View.GONE);
            weiyueLinear.setVisibility(View.GONE);
            incomeLine.setVisibility(View.GONE);
            incomeLinear.setVisibility(View.GONE);
        } else {//扣钱的取消订单
            if(SharedPreferencesUtils.getString(context, SharedPreferencesUtils.UserRole).equals("0")){
                incomeLine.setVisibility(View.VISIBLE);
            }else{
                incomeLinear.setVisibility(View.GONE);
            }
            weiyueLine.setVisibility(View.VISIBLE);
            weiyueLinear.setVisibility(View.VISIBLE);
            incomeText.setText(price + "元");
            xmText.setText(compensatoryPayment + "元");
        }

        cancleOrderAdapter = new CancleOrderAdapter(context, reasonList, adpterListener);
        listview.setAdapter(cancleOrderAdapter);
        ViewUtils.setListViewHeightBasedOnChildren(listview);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_select = position;
                reflushData(position);
            }
        });
    }

    public void reflushData(int select_index) {
        for (int i = 0; i < reasonList.size(); i++) {
            JsonMap<String, Object> data = reasonList.get(i);
            data.put("isChecked", false);
            reasonList.set(i, data);
        }
        JsonMap<String, Object> data = reasonList.get(select_index);
        data.put("isChecked", true);
        reasonList.set(select_index, data);

        cancleOrderAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener adpterListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = (int) v.getTag();
            current_select = index;
            reflushData(index);
        }
    };

    public void go2Pay() {
        if (isfrom == 1 || isPayd) {//默认1-是不扣钱的取消订单,2-扣钱的取消订单.,或者已经支付成功了。
            cancleOrder();
        } else{
            if(!SharedPreferencesUtils.getBoolean(this,SharedPreferencesUtils.havePayPwd)) {
                ToastUtils.showToastShort(context, "请设置支付密码");
                startActivity(new Intent(context, SetPayPasswordActivity.class).putExtra("type", 1));
                return;
            }
            Intent intent = new Intent(context, PayordersActivity.class);
            intent.putExtra("isfrom", 2);
//            intent.putExtra("weiyue_money", compensatoryPayment);
            intent.putExtra("isfrom", isfrom);
            if (isfrom == 2) {
                intent.putExtra("tip", price);
                intent.putExtra("bao",baonum);
            }
            intent.putExtra("pay", compensatoryPayment);
            startActivityForResult(intent, PAY_BACK);
        }
    }

    //提交取消订单
    private void cancleOrder() {
        if (current_select == -1) {
            ToastUtils.showToastShort(context, "请选择取消原因");
            return;
        }
        mLoadingDialog = DialogUtils.showLoadingDialog(this);
        Map<String, Object> map = new HashMap<>();
        map.put("businessNumber", businessNumber);
        map.put("cancerCause", reasonList.get(current_select).getStringNoNull("dataCode"));
        map.put("format", "json");
        if (isfrom == 2) {//有违约金的
            map.put("reward", String.valueOf(price));
            map.put("cancerMoney", String.valueOf(compensatoryPayment));
            map.put("payPassword", payPassword);
        }

        DataUtils.loadData(context, GetDataConfing.CancleOrder, map, TAG_CancleOrder, responseDataCallback);
    }

    /**
     * 根据订单号，获取订单取消原因
     */
    private void getCancleReasons() {
        Map<String, Object> map = BaseMapUtils.getMap(context);
        if (isGuYuan)
            map.put("groupCode", "employee.cancel.cause");
        else
            map.put("groupCode", "employer.cancel.cause");//雇主
        DataUtils.loadData(context, GetDataConfing.CancleReasons, map, TAG_CancleReasons, responseDataCallback);
    }

    /**
     * 根据订单号，获取订单违约金及扣除的诚信值
     */
    private void getCancleOrderFare() {
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("businessNumber", businessNumber);
        DataUtils.loadData(context, GetDataConfing.cancleOrderDetail, map, TAG_CancleOrderFare, responseDataCallback);
    }

    /**
     * 数据请求回调接口（在回调接口中进行操作）
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            if (mLoadingDialog != null)
                mLoadingDialog.dismiss();
            if (dataRequest.isNetError()) {
                ShowGetDataError.showNetError(context);
            } else {
                if (listview != null) {
                    String json = dataRequest.getResponseData();

                    if (!TextUtils.isEmpty(json)) {
                        JsonMap<String, Object> jsonMap = JsonParseHelper.getJsonMap(json);
                        List<JsonMap<String, Object>> data = null;
                        switch (dataRequest.getWhat()) {
                            case TAG_CancleReasons://获取订单取消原因
                                data = jsonMap.getList_JsonMap("data");
                                if (data != null && data.size() > 0) {
                                    reasonList.clear();//先清空这个集合 防止二次加载
                                    reasonList.addAll(data);
                                    cancleOrderAdapter.notifyDataSetChanged();
                                    ViewUtils.setListViewHeightBasedOnChildren(listview);
                                }
                                break;
                            case TAG_CancleOrder://取消订单
                                int code = jsonMap.getInt("code");
                                String error = "";
                                if (code == 0) {//取消成功
                                    error = "订单取消成功";
                                    EventBus.getDefault().post(new MainToWorkerFragmentEvent("", 3));
                                    EventBus.getDefault().post(new FinishEvent());
                                    finish();//取消成功后关闭当前界面
                                } else {//取消失败

                                    //回调上一个界面，刷新数据。
                                    //把取消原因带过去。
                                    String cancleReason = String.valueOf(dataRequest.getParams().get("cancerCause"));

//                                    finish();
                                    error = jsonMap.getStringNoNull("msg");
                                }
                                ToastUtils.showToastShort(context, error);
                                break;
                            case TAG_CancleOrderFare:
                                int cancleCode = jsonMap.getInt("code");
                                if (cancleCode==1){
                                    mLoadingDialog.dismiss();
                                    ToastUtils.showToastShort(context,jsonMap.getStringNoNull("msg"));
                                }else {
                                    data = jsonMap.getList_JsonMap("data");
                                    price = data.get(0).getDouble("price");//报酬
                                    compensatoryPayment = data.get(0).getDouble("compensatoryPayment");//赔偿金
                                    compensatoryTrusty = data.get(0).getDouble("compensatoryTrusty");//赔偿诚信值
                                    initViews();
                                    break;
                                }


                        }
                    }
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PAY_BACK://支付成功的回调。
//                    isPayd = true;
                    //支付成功之后，拿到支付密码，再调用提交接口。
                    payPassword = data.getStringExtra("payPassword");
                    cancleOrder();
                    break;
            }
        }
    }

    @OnClick({R.id.tv_save, R.id.submit_btn,R.id.guize_text})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save://关闭
                finish();
                break;
            case R.id.submit_btn://提交
                go2Pay();
                break;
            case R.id.guize_text://扣分规则
                go2Rules();
                break;
        }
    }

    private void go2Rules() {
        Intent chengxinIntent = new Intent(context, WebUtilsActivity.class);
        chengxinIntent.putExtra("functionTitle","打零工平台规则");
        chengxinIntent.putExtra("contentUrl",GetDataConfing.BASEURL_H5);
        chengxinIntent.putExtra("type","5");
        startActivity(chengxinIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }
}
