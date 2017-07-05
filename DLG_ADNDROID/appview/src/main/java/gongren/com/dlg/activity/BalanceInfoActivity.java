package gongren.com.dlg.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.common.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aym.util.json.JsonMap;
import aym.util.json.JsonParseHelper;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.LogUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 收支详情界面
 */
public class BalanceInfoActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_money)
    TextView tvMoney;           //消费金额

    @Bind(R.id.img_head)
    ImageView imgHead;
    @Bind(R.id.tv_username)
    TextView tvUserName;
    @Bind(R.id.tv_type)
    TextView tvType;
    @Bind(R.id.tv_transaction_type)
    TextView tv_transaction_type;
    @Bind(R.id.tv_payment_mode)
    TextView tvPaymentMode;
    @Bind(R.id.tv_emarks_explain)
    TextView tvRmarksExplain;
    @Bind(R.id.tv_create_time)
    TextView tvCreateTime;

    @Bind(R.id.image_type)
    ImageView imageType;
    @Bind(R.id.line_normal)
    View lineNormal;
    @Bind(R.id.tv_pro_type)
    TextView tv_pro_type;
    @Bind(R.id.tv_pro_time)
    TextView tvProTime;

    @Bind(R.id.image_type1)
    ImageView imageType1;
    @Bind(R.id.line_normal1)
    View lineNormal1;
    @Bind(R.id.tv_pro_type1)
    TextView tv_pro_type1;
    @Bind(R.id.tv_pro_time1)
    TextView tv_pro_time1;

    @Bind(R.id.image_type2)
    ImageView image_type2;
    @Bind(R.id.tv_pro_type2)
    TextView tv_pro_type2;
    @Bind(R.id.tv_pro_time2)
    TextView tv_pro_time2;
    @Bind(R.id.lin_status)
    LinearLayout lin_status ;

//    @Bind(R.id.lv_pro_type)
//    ListView lvProType;

    private List<JsonMap<String, Object>> dataList = new ArrayList<>();   //数据源
    private static final int TAG_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_info);
        ButterKnife.bind(this);

        tvTitle.setText("账单详情");
        String billId = getIntent().getStringExtra("billId");


        addData(billId);

    }

    /**
     * 网络请求数据
     */
    private void addData(String billId) {
        Map<String, Object> map = BaseMapUtils.getMap(context);
        map.put("subBillBusinessNumber", billId);
//        map.put("subBillBusinessNumber", "");
        DataUtils.loadData(context, GetDataConfing.findBillDetailInfo, map, TAG_REQUEST, responseDataCallback);
    }

    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            //            if (canContentView != null) {
            if (dataRequest.isNetError()) {
                ShowGetDataError.showNetError(context);
            } else {
                String json = dataRequest.getResponseData();
                if (!TextUtils.isEmpty(json)) {
                    if (dataRequest.getWhat() == TAG_REQUEST) {
                        LogUtils.logD("zq", json);
                        dataList = JsonParseHelper.getJsonMap_List_JsonMap(json, "data");
                        fillData();
                    }
                }
            }
        }
    };

    private void fillData(){
        JsonMap<String, Object> jsonMap = dataList.get(0);
        tvMoney.setText(jsonMap.getDouble("amount")+"元");

        Glide.with(context).load(jsonMap.getString("logo")).error(R.mipmap.icon_default_user)
                .into(imgHead);
        if(TextUtils.isEmpty(jsonMap.getString("name"))){
            tvUserName.setText("打零工平台");
        }else if("7777777777777".equals(jsonMap.getString("name"))){
            tvUserName.setText("打零工平台");
            Glide.with(context).load(R.mipmap.ic_launcher).into(imgHead);
        }else{
            tvUserName.setText(jsonMap.getStringNoNull("name",""));
        }
        String tradeType = jsonMap.getString("tradeType");
        String tradeTypeStr = "";
//        if(tradeType.equals("1")){
//            tradeTypeStr ="充值";
//        }else if(tradeType.equals("2")){
//            tradeTypeStr ="提现";
//            lin_status.setVisibility(View.VISIBLE);
//        } else if(tradeType.equals("3")){
//            tradeTypeStr ="支付";
//        }
        switch (tradeType) {
            case "1":
                tradeTypeStr ="充值";
                break;
            case "2":
                tradeTypeStr = "提现";
                lin_status.setVisibility(View.VISIBLE);
                break;
            case "3":
                tradeTypeStr = "报酬";
                break;
            case "4":
                tradeTypeStr = "小费";
                break;
            case "5":
                tradeTypeStr = "服务费";
                break;
            case "6":
                tradeTypeStr = "税费";
                break;
            case "7":
                tradeTypeStr = "违约金";
                break;
            case "8":
                tradeTypeStr = "补偿";
                break;
            case "9":
                tradeTypeStr = "活动赠送";
                break;
            case "10":
                tradeTypeStr = "提现";
                break;
            case "11":
                tradeTypeStr = "盈利收益";
                break;
            case "12":
                tradeTypeStr = "服务费";
                break;
            case "13":
                tradeTypeStr = "活动赠送";
                break;
            case "14":
                tradeTypeStr = "活动赠送";
                break;
            case "15":
                tradeTypeStr = "保险";
                break;
            default:
                tradeTypeStr = "支付";
                break;
        }
        tv_transaction_type.setText(tradeTypeStr);
        String status = jsonMap.getString("status");
        String tradeStatus = "";
        if(status.equals("0")){
            tradeStatus ="待支付";
        }else if(status.equals("1")){
            tradeStatus ="进行中";
            tv_pro_type.setText("已提交");
            tv_pro_type1.setText("正在处理中");
            tv_pro_type2.setText("");
            image_type2.setImageBitmap(BitmapFactory.decodeResource(this.getResources(),R.mipmap.duihaoblack));
            tv_pro_time2.setText("");
            lineNormal1.setBackgroundResource(R.color.gray_text);
        }else if(status.equals("2")){
            tradeStatus ="成功";
            tv_pro_type.setText("已提交");
            tv_pro_type1.setText("正在处理中");
            tv_pro_type2.setText(tradeTypeStr+tradeStatus);
        } else if(status.equals("3")){
            tradeStatus ="失败";
            tv_pro_type.setText("已提交");
            tv_pro_type1.setText("正在处理中");
            tv_pro_type2.setText(tradeTypeStr+tradeStatus);
        }
        tvType.setText("交易"+tradeStatus);
        String paymentType = jsonMap.getString("paymentType");
        String paymentTypeStr = "";
        if(paymentType.equals("0")){
            paymentTypeStr ="橙子";
        }else if(paymentType.equals("1")){
            paymentTypeStr ="支付宝";
        }else if(paymentType.equals("2")){
            paymentTypeStr ="微信";
        } else if(paymentType.equals("3")){
            paymentTypeStr ="银行卡";
        }
        tvPaymentMode.setText(paymentTypeStr);

        tvRmarksExplain.setText("打零工平台-"+paymentTypeStr+tradeTypeStr);
        String createTimeData = jsonMap.getString("createTime");
        if(!TextUtils.isEmpty(createTimeData)){
            tvProTime.setText(DateUtils.dateTimeFormat("yyyy-MM-dd HH:mm:ss", createTimeData));
            tv_pro_time1.setText(DateUtils.dateTimeFormat("yyyy-MM-dd HH:mm:ss", createTimeData));
            tvCreateTime.setText(DateUtils.dateTimeFormat("yyyy-MM-dd HH:mm:ss", createTimeData));
        }
        String mtime = jsonMap.getString("modifyTime");
        if(!TextUtils.isEmpty(mtime)){
            tv_pro_time2.setText(DateUtils.dateTimeFormat("yyyy-MM-dd HH:mm:ss", mtime));
        }
        if(status.equals("1")){
            tv_pro_time2.setVisibility(View.INVISIBLE);
        }else {
            tv_pro_time2.setVisibility(View.VISIBLE);
        }

    }
    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
