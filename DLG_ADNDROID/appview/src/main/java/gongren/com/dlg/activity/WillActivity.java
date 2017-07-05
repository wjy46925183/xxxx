package gongren.com.dlg.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.utils.DateUtils;
import com.common.utils.SharedPreferencesUtils;
import com.common.view.calendar.DatePickerController;
import com.common.view.calendar.DayPickerView;
import com.common.view.calendar.SimpleMonthAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.javabean.worker.MySericeBean;
import gongren.com.dlg.pickerview.DatePackerUtil;
import gongren.com.dlg.pickerview.LoopListener;
import gongren.com.dlg.pickerview.LoopView;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.DialogUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;

/**
 * Created by Wangjinya on 2017/6/22.
 */

public class WillActivity extends BaseActivity implements DatePickerController {

    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.iv_right)
    ImageView mIvRight;
    @Bind(R.id.tv_date)
    TextView mTvDate;
    @Bind(R.id.tv_time)
    TextView mTvTime;
    @Bind(R.id.tv_address)
    TextView mTvAddress;
    @Bind(R.id.tv_commit_service)//提交服务
            TextView mTvCommitService;
    private AlertDialog alertDialog;
    private MySericeBean.DataBean mDataBean;
    private int mDemandType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_will);
        ButterKnife.bind(this);
        mTvTitle.setText("预约");
        mDataBean = (MySericeBean.DataBean) getIntent().getSerializableExtra("dataBean");
        mDemandType = mDataBean.getDemandType();
    }

    @OnClick({R.id.iv_back, R.id.tv_date, R.id.tv_time, R.id.tv_address, R.id.tv_commit_service})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_date:
                sbDate.setLength(0);
                showDateDialog();
                break;
            case R.id.tv_time:
                showTime();
                break;
            case R.id.tv_address:
                Intent intent = new Intent(this, AddressSearcherActivity.class);
                intent.putExtra("address", SharedPreferencesUtils.getString(this, "adr"));
                startActivity(intent);
                break;
            case R.id.tv_commit_service:
                commitService();
                break;
        }
    }

    /**
     * 提交服务
     */
    private void commitService() {
        if (TextUtils.isEmpty(mTvDate.getText())) {
            Toast.makeText(context, "日期不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(mTvTime.getText())) {
            Toast.makeText(context, "时间不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(mTvAddress.getText())) {
            Toast.makeText(context, "地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String[] dates = sbDate.toString().split("--");
        String[] times = mTvTime.getText().toString().split("--");

        String startDate = dates[0].replaceAll("[年月]", "-").replaceAll("日", "") + " " + times[0] + ":01";
        String endDate;
        if (dates.length == 1) {
            if (times.length == 1) {
                endDate = dates[0].replaceAll("[年月]", "-").replaceAll("日", "") + " " + times[0] + ":01";
            } else {
                endDate = dates[0].replaceAll("[年月]", "-").replaceAll("日", "") + " " + times[1] + ":01";
            }
        } else {
            if (times.length == 1) {
                endDate = dates[1].replaceAll("[年月]", "-").replaceAll("日", "") + " " + times[0] + ":01";
            } else {
                endDate = dates[1].replaceAll("[年月]", "-").replaceAll("日", "") + " " + times[1] + ":01";
            }
        }
        final Dialog loadingDialog = DialogUtils.showLoadingDialog(this);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("jobServiceId", mDataBean.getId());
        hashMap.put("provinceId", "");
        hashMap.put("provinceName", "");
        hashMap.put("cityId", "");
        hashMap.put("cityName", "");
        hashMap.put("areaId", SharedPreferencesUtils.getString(this, "areaId"));
        hashMap.put("areaName", "");
        hashMap.put("villageId", "");
        hashMap.put("villageName", "");
        hashMap.put("workAddress", SharedPreferencesUtils.getString(this, "adr"));
        hashMap.put("startTime", startDate);
        hashMap.put("endTime", endDate);
        hashMap.put("client", "ANDROID");
        hashMap.put("format", "json");
        DataUtils.loadData(this, GetDataConfing.WILL_SERVICE, hashMap, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                loadingDialog.dismiss();
                Log.i("====s===", dataRequest.getResponseData());
                try {
                    JSONObject jsonObject = new JSONObject(dataRequest.getResponseData());
                    Toast.makeText(context, jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
                    if (jsonObject.optInt("code") == 0) {
                        finish();
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
        mTvAddress.setText(SharedPreferencesUtils.getString(this, "adr"));
    }

    /**
     * 日期
     */
    private void showDateDialog() {
        View dateView = LayoutInflater.from(this).inflate(R.layout.item_pickerview_date, null);
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
                String s = sbDate.toString();
                String start = "";
                String end = "";
                if (mDemandType == 3) {
                    if(s.contains("--")){
                        Toast.makeText(context, "计件为单个时间，不能跨天", Toast.LENGTH_SHORT).show();
                    }else{
                        if (s.length() == 9) {
                            start = s.substring(5, 9);//06月23日--2017年06月30日
                        } else if (s.length() == 10) {
                            start = s.substring(5, 10);
                        }
                        mTvDate.setText(start);
                        alertDialog.dismiss();
                    }
                } else {
                    if (s.contains("--")) {
                        start = s.substring(5, 11);//06月23日--2017年06月30日
                        end = s.substring(s.length() - 6, s.length());
                    } else {
                        if (s.length() == 9) {
                            start = s.substring(5, 9);//06月23日--2017年06月30日
                        } else if (s.length() == 10) {
                            start = s.substring(5, 10);
                        }

                    }
                    if (TextUtils.isEmpty(end)) {
                        mTvDate.setText(start);
                    } else if (start.equals(end)) {
                        mTvDate.setText(start);
                    } else {
                        mTvDate.setText(start + "--" + end);
                    }
                    alertDialog.dismiss();
                }

            }
        });
        dayPickerView.setController(this);
        alertDialog = DialogUtils.showViewDialog(context, dateView);
    }

    private StringBuffer sbDate = new StringBuffer();

    @Override
    public int getMaxYear() {
        return Integer.parseInt(DateUtils.dateFormat("yyyy", System.currentTimeMillis() + "")) + 1;
    }

    @Override
    public void onDayOfMonthSelected(int year, int month, int day) {
        Log.i("====s====", month + "--" + day);
        if (sbDate.length() == 0 || sbDate.toString().contains("--")) {
            sbDate.setLength(0);
            sbDate.append(year + "年" + (month + 1) + "月" + day + "日");
        }
    }

    @Override
    public void onDateRangeSelected(SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays) {
        sbDate.setLength(0);
        long first = selectedDays.getFirst().getDate().getTime();
        long last = selectedDays.getLast().getDate().getTime();

        sbDate.append(DateUtils.dateFormat("yyyy年MM月dd日", Math.min(first, last) + ""))
                .append("--" + DateUtils.dateFormat("yyyy年MM月dd日", Math.max(first, last) + ""));
    }

    private String startTime, endTime;

    /**
     * 时间
     */
    private void showTime() {
        final List<String> listItem = DatePackerUtil.getTimeAllList();
        View timeView = LayoutInflater.from(this).inflate(R.layout.item_pickerview_time, null);
        LoopView loopView1 = (LoopView) timeView.findViewById(R.id.loopView1);
        LoopView loopView2 = (LoopView) timeView.findViewById(R.id.loopView2);
        LinearLayout ll_end = (LinearLayout) timeView.findViewById(R.id.ll_end);
        Button btnCancle = (Button) timeView.findViewById(R.id.btn_cancle);//关闭按钮
        Button btnOk = (Button) timeView.findViewById(R.id.btn_ok);    //确定按钮
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (mDemandType == 3) {
                    mTvTime.setText(startTime);
                } else {
                    mTvTime.setText(startTime + "--" + endTime);
                }

            }
        });
        loopView1.setList(listItem);
        defaultTime(loopView1);
        startTime = listItem.get(loopView1.getCurrentItem());
        loopView1.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                startTime = listItem.get(item);
            }
        });

        if (mDemandType == 3) {//计件
            ll_end.setVisibility(View.GONE);
        } else {
            ll_end.setVisibility(View.VISIBLE);
            loopView2.setList(listItem);
            endTime = listItem.get(loopView2.getCurrentItem());
            loopView2.setListener(new LoopListener() {
                @Override
                public void onItemSelect(int item) {
                    endTime = listItem.get(item);
                }
            });
        }
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
}
