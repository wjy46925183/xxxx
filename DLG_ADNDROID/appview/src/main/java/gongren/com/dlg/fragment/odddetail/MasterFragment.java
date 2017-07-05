package gongren.com.dlg.fragment.odddetail;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.common.utils.SharedPreferencesUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import gongren.com.dlg.R;
import gongren.com.dlg.activity.CancleOrderActivity;
import gongren.com.dlg.activity.EmployeeDetailsActivity;
import gongren.com.dlg.activity.PayordersActivity;
import gongren.com.dlg.activity.SetPayPasswordActivity;
import gongren.com.dlg.javabean.MasterBean;
import gongren.com.dlg.javabean.base.ButtonListBean;
import gongren.com.dlg.javabean.base.CountdownVoBean;
import gongren.com.dlg.javabean.master.MasterStatusBean;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.DialogUtils;
import gongren.com.dlg.utils.LogUtils;
import gongren.com.dlg.utils.TimeUtils;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.view.AddTypeButtonView;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

import static android.view.View.GONE;

/**
 * Created by lenovo on 2017/4/13.
 * 雇主的订单零工详情 弹框
 */

public class MasterFragment extends Fragment {
    public final static int PEOPLE_ORDER = 1;//有人接单 弹框
    public final static int WAITING_START = 2;//等待开工 弹框
    public final static int WAIT_PAY = 3;//代收款 弹框
    public final static int IN_WORK = 4;//正在干活中 弹框
    public final static int WAITING_ACCEPTANCE = 5;//等待验收 弹框

    @Bind(R.id.iv_head)
    CircleImageView mIvHead;
    @Bind(R.id.tv_trust)
    TextView mTvTrust;
    @Bind(R.id.name)
    TextView mName;
    @Bind(R.id.has_form)
    TextView mHasForm;
    @Bind(R.id.tv_distance)
    TextView mTvDistance;
    @Bind(R.id.rb_star)
    RatingBar mRbStar;
    @Bind(R.id.tv_certificate)//证书
            TextView mTvCertificate;
    @Bind(R.id.tv_work)
    TextView mTvWork;
    @Bind(R.id.add_but_view)
    AddTypeButtonView addTypeButtonView; //添加按钮控件

    @Bind(R.id.tv_tishi)
    TextView tv_tishi;

    @Bind(R.id.layout_price)
    LinearLayout layoutPrice; //价格总布局
    @Bind(R.id.tv_price)
    TextView tvPrice;  //报酬
    @Bind(R.id.xiaofei_edit)
    EditText editXiaofei; //小费

    private CountDownTimer timer;
    private TextView tital;
    private FragmentActivity activity;
    private String businessNumber;
    private Double tip; //小费
    private Double pay;    //金额
    private Double bao; //保险
    private String orders; //支付订单
    private MasterBean mMasterBean;
    private Dialog mDialog;
    private int mStatusCode;
    private List<MasterStatusBean.DataBean> mDataBean;

    private int statusCode; //当前订单状态

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (FragmentActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_master, null);
        ButterKnife.bind(this, inflate);
        addTypeButtonView.setClickButListener(new AddTypeButtonView.OnClickButListener() {
            @Override
            public void OnClickBut(ButtonListBean bean) {
                switch (bean.operateStatusCode) {
                    case 8:  //发出邀请
                    case 20:  //同意
                    case 11:  //拒绝
                    case 30:  //确认完成
                    case 25:  //验收通过
                    case 26:  //验收不通过
                        httpSendStatus(bean.operateStatusCode, bean.nextStatusCode + "");
                        break;
                    case 1:  //知道了
                        getActivity().finish();
                    case 3:  //取消
                        if (statusCode == 20) {
                            Intent intent = new Intent(activity, CancleOrderActivity.class);
                            intent.putExtra("compensatoryTrusty", "1");//诚信值
                            intent.putExtra("businessNumber", businessNumber);//诚信值
                            activity.startActivity(intent);
                        }
                        if (statusCode == 21) {
                            Intent intent = new Intent(activity, CancleOrderActivity.class);
                            intent.putExtra("compensatoryTrusty", "2");//诚信值
                            intent.putExtra("businessNumber", businessNumber);//诚信值
                            intent.putExtra("isfrom", 2);
                            activity.startActivity(intent);
                        }
                        break;
                    case 40:  //确认支付
                        if (SharedPreferencesUtils.getBoolean(activity, SharedPreferencesUtils.havePayPwd)) {
                            Intent intent = new Intent(activity, PayordersActivity.class);//正常的支付页面
                            String priceXiao = "0";
                            if (!TextUtils.isEmpty(editXiaofei.getText().toString())) {
                                priceXiao = editXiaofei.getText().toString();
                            }
                            intent.putExtra("orders", businessNumber + "#" + priceXiao);//订单业务编号
                            intent.putExtra("isfrom", 1);
                            intent.putExtra("tip", Double.parseDouble(priceXiao));
                            intent.putExtra("pay", pay);
                            intent.putExtra("bao", bao);
                            startActivity(intent);
                        } else {
                            startActivity(new Intent(activity, SetPayPasswordActivity.class).putExtra("type", 1));
                        }

                        break;
                    case 50:  //确认支付
                        getActivity().finish();
                        break;

                }
            }
        });

        initView();
        return inflate;
    }

    boolean input = false;

    private void initView() {

        editXiaofei.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    if (!input) {
                        input = true;
                        editXiaofei.setText("");
                    }
                    input = false;
                    return;
                }
                double xiaofei = 0;
                if (!TextUtils.isEmpty(s.toString())) {
                    xiaofei = Double.parseDouble(s.toString().trim());
                }
                if (xiaofei > pay) {
                    if (!input) {
                        input = true;
                        editXiaofei.setText(pay + "");
                        editXiaofei.setSelection(editXiaofei.getText().length());
                    }

                } else {
                    editXiaofei.setSelection(editXiaofei.getText().length());
                }
                input = false;
            }
        });
    }

    /**
     * 雇主弹框类型
     */
    public void setId_Tital(Double tip, Double pay, String businessNumber, String orders, TextView tv_title,Double bao) {
        this.tip = tip;
        this.pay = pay;
        this.tital = tv_title;
        this.orders = orders;
        this.bao = bao;
        this.businessNumber = businessNumber;
        httpData();//网络请求
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * 进行网络请求 获取雇员相关资料
     */
    public void httpData() {
//        mDialog = DialogUtils.showLoadingDialog(activity);
        final Map<String, Object> map = BaseMapUtils.getMap(activity);
        map.put("businessNumber", businessNumber);//订单号
        DataUtils.loadData(activity, GetDataConfing.QUERY_ORDER, map, 1, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
//                if (mDialog != null && mDialog.isShowing()) {
                if (dataRequest.isNetError()) {
                    ShowGetDataError.showNetError(activity);
                } else {
                    String json = dataRequest.getResponseData();
                    try {
                        JSONObject object = new JSONObject(json);
                        LogUtils.logE(getTag() + "~~~~", json.toString());
                        if ("0".equals(object.optString("code"))) {//成功
                            mMasterBean = new Gson().fromJson(json, MasterBean.class);
                            if (mMasterBean != null && mMasterBean.data != null & mMasterBean.data.size() > 0)
                                updateUi(mMasterBean);
                            httpFunction();
                        } else {
                            ToastUtils.showToastShort(activity, object.optString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
//            }
        });
    }

    private LoCallBack mLoCallBack;

    /**
     * 回调雇主雇员的经纬度
     *
     * @param loCallBack
     */
    public void setLoCallBack(LoCallBack loCallBack) {
        mLoCallBack = loCallBack;
    }

    /**
     * 加载按钮状态
     */
    private void httpFunction() {
        final Map<String, Object> map = BaseMapUtils.getMap(activity);
        map.put("businessNumber", businessNumber);//订单号
//        map.put("businessNumber", businessNumber);//订单号

        DataUtils.loadData(activity, GetDataConfing.ACTION_BUTTON, map, 2, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                if (dataRequest.isNetError()) {
                    ShowGetDataError.showNetError(activity);
                } else {
                    String json = dataRequest.getResponseData();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if ("0".equals(jsonObject.optString("code"))) {
                            MasterStatusBean masterStatusBean = new Gson().fromJson(json, MasterStatusBean.class);
                            mDataBean = masterStatusBean.data;
                            if (null != mDataBean && mDataBean.size() > 0) {
                                statusCode = mDataBean.get(0).statusCode;
                                if(null != tital){
                                    tital.setText(mDataBean.get(0).statusText);
                                }
                                addTypeButtonView.setButtonData(mDataBean.get(0).buttonList);
                                if (statusCode == 30) {
                                    layoutPrice.setVisibility(View.VISIBLE);
                                }
                                setStatusTypeList(mDataBean.get(0));
                            }
                        } else {
                            ToastUtils.showToastShort(activity, jsonObject.optString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * 进行相关UI操作
     *
     * @param masterBean
     */
    private void updateUi(MasterBean masterBean) {
        MasterBean.DataBean dataBean = masterBean.data.get(0);
        if (TextUtils.isEmpty(dataBean.name)) {
            mName.setText(dataBean.phone);
        } else {
            mName.setText(dataBean.name);
        }
        tvPrice.setText(pay + " 元");
        if (tip > 0) {
            editXiaofei.setText(tip + "");
        }
        if (dataBean.xyCoordinateVo != null)
            mTvDistance.setText("距离" + dataBean.xyCoordinateVo.distanceToJob + "km");
        mTvTrust.setText(dataBean.creditCount == null ? 36.5f + "" : Float.parseFloat(dataBean.creditCount) + "");//诚信统计
        mRbStar.setRating(dataBean.scoreCount == null ? 5 : Float.parseFloat(dataBean.scoreCount));//星数

        mHasForm.setText("已接" + (TextUtils.isEmpty(dataBean.joinCount) ? "0" : dataBean.joinCount) + "单");
        //mTvWork.setText(dataBean.description);//描述
        String name = "自由工作者";
        if (!TextUtils.isEmpty(dataBean.identity)) {
            int identity = Integer.parseInt(dataBean.identity);
            switch (identity) {
                case 0:
                    name = "在校学生";
                    break;
                case 1:
                    name = "自由工作者";
                    break;
                case 2:
                    name = "兼职人员";
                    break;
            }
        }
        mTvWork.setText(name);//工作类型
        mTvCertificate.setText(TextUtils.isEmpty(dataBean.description) ? "自由、灵活、赚钱" : dataBean.description);//证书
        mStatusCode = Integer.parseInt(dataBean.currentOperateStatus);

        Glide.with(this).load(dataBean.logo == null ? R.mipmap.morentouxiang : dataBean.logo).into(mIvHead);
        if (mLoCallBack != null && dataBean.xyCoordinateVo != null) {
            mLoCallBack.getLogo(dataBean.logo);
            mLoCallBack.employeeLoation(dataBean.xyCoordinateVo.userXCoordinate == null ?
                    0.0 : Double.parseDouble(dataBean.xyCoordinateVo.userXCoordinate), dataBean.xyCoordinateVo.userYCoordinate == null ? 0.0 : Double.parseDouble(dataBean.xyCoordinateVo.userYCoordinate));
            mLoCallBack.masterLoation(dataBean.xyCoordinateVo.jobXCoordinate ==
                            null ? 0.0 : Double.parseDouble(dataBean.xyCoordinateVo.jobXCoordinate),
                    dataBean.xyCoordinateVo.jobYCoordinate == null ? 0.0 : Double.parseDouble(dataBean.xyCoordinateVo.jobYCoordinate));
        }
    }


    @OnClick({R.id.iv_head})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_head:
                if (null != mMasterBean && null != mMasterBean.data && mMasterBean.data.size() > 0) {
                    Intent employeeDetailsActivityIntent = new Intent(activity, EmployeeDetailsActivity.class);
                    employeeDetailsActivityIntent.putExtra("userId", mMasterBean.data.get(0).employeeId + "");
                    startActivity(employeeDetailsActivityIntent);
                }
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (null != timer) {
            timer.cancel();
        }
    }

    /**
     * 设置不同类型下的ui
     *
     * @param beans
     * @return
     */
    private void setStatusTypeList(MasterStatusBean.DataBean beans) {
        tv_tishi.setVisibility(View.GONE);
        final CountdownVoBean voBean = beans.countdownVo;
        if (null != voBean) {
            final StringBuffer buffer = new StringBuffer("%s");
            buffer.append(voBean.mapTipsText);
            if (null != voBean&&voBean.remainingTime !=0 && !TextUtils.isEmpty(voBean.remainingTime + "")) {
                tv_tishi.setVisibility(View.VISIBLE);
                timer = new CountDownTimer(beans.countdownVo.remainingTime, 1000) {//倒计时
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (null != tv_tishi && null != buffer && null != voBean && !TextUtils.isEmpty(voBean.remainingTime + "")) {
                            tv_tishi.setText(String.format(buffer.toString(), TimeUtils.getRemainTimeByWM((int) millisUntilFinished)));
                        }
                    }

                    @Override
                    public void onFinish() {
                        cancel();//倒计时结束 取消计时
                        if (null != tv_tishi) {
                            tv_tishi.setVisibility(GONE);//隐藏倒计时控件
                        }
                        httpFunction();
                    }
                }.start();
            }
        }
//        if(null != voBean && null != beans.buttonList && beans.buttonList.size()>0){
//            addTypeButtonView.setButtonData(beans.buttonList);
//            for (int i = 0; i < beans.buttonList.size(); i++) {
//                final ButtonListBean bean = beans.buttonList.get(i);
//                switch (bean.operateStatusCode) {
//                    case 20:  //同意
//                        tv_tishi.setVisibility(View.VISIBLE);
//                        tv_tishi.setBackgroundColor(getResources().getColor(R.color.white_color));
//                        tv_tishi.setTextColor(getResources().getColor(R.color.black_textcolor));
//                        break;
//                    case 40:  //确认支付
//                        tv_tishi.setVisibility(View.VISIBLE);
//                        break;
//                    case 3:
//                        tv_tishi.setVisibility(View.VISIBLE);
//                        break;
//
//                }
//            }
//        }
    }

    /**
     * 订单操作按钮修改
     *
     * @param operaStatus
     * @param nextStatusCode
     */
    private void httpSendStatus(int operaStatus, String nextStatusCode) {
        final Dialog loadingDialog = DialogUtils.loadingProgressDialog(activity);
        Map<String, Object> map = BaseMapUtils.getMap(activity);
        map.put("businessNumber", businessNumber);
        map.put("nextStatusCode", nextStatusCode);
        map.put("operaStatus", operaStatus + "");
        map.put("userid", SharedPreferencesUtils.getString(activity, SharedPreferencesUtils.USERID));
        DataUtils.loadData(activity, GetDataConfing.post_task_button_event, map, 1, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                if (loadingDialog.isShowing()) {
                    if (dataRequest.isNetError()) {
                        ShowGetDataError.showNetError(activity);
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject(dataRequest.getResponseData());
                            if ("0".equals(jsonObject.optString("code"))) {
                                activity.setResult(200);
                                activity.finish();
                            } else {
                                ToastUtils.showToastShort(activity, jsonObject.optString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}
