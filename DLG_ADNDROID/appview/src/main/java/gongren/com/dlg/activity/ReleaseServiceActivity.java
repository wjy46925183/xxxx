package gongren.com.dlg.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gongren.com.dlg.R;
import gongren.com.dlg.adapter.ReleaseServiceAdapter;
import gongren.com.dlg.javabean.SelectData;
import gongren.com.dlg.javabean.worker.MySericeBean;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.DialogUtils;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by Wangjinya on 2017/6/21.
 */

public class ReleaseServiceActivity extends BaseActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, RadioGroup.OnCheckedChangeListener, TabLayout.OnTabSelectedListener {
    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.iv_right)
    ImageView mIvRight;
    @Bind(R.id.tv_odd_serice)
    EditText mTvOddSerice;
    @Bind(R.id.type_text)
    TextView mTypeText;
    @Bind(R.id.type_select_linear)
    LinearLayout mTypeSelectLinear;
    @Bind(R.id.pay_text)
    EditText mPayText;
    @Bind(R.id.jine_linear)
    LinearLayout mJineLinear;
    @Bind(R.id.day_rbtn)
    RadioButton mDayRbtn;
    @Bind(R.id.hour_rbtn)
    RadioButton mHourRbtn;
    @Bind(R.id.rg)
    RadioGroup mRg;
    @Bind(R.id.unit_linear)
    RelativeLayout mUnitLinear;
    @Bind(R.id.desc_tab)
    TextView mDescTab;
    @Bind(R.id.desc_text)
    EditText mDescText;
    @Bind(R.id.gridview)
    GridView mGridView;
    @Bind(R.id.scroll)
    ScrollView mScrollView;
    @Bind(R.id.hour_dan)
    RadioButton mHourDan;
    @Bind(R.id.tv_relese)
    TextView mTvRelese;
    @Bind(R.id.tablayout)
    TabLayout mTabLayout;
    private List<String> paths = new ArrayList<>();
    private ReleaseServiceAdapter mReleaseServiceAdapter;
    private String mServiceMeterUnit = "1";
    private SelectData mSelect_data;
    private String demandType = "1";
    private String id = "";
    private MySericeBean.DataBean mDataBean;
    private Dialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_release_service);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        mTvTitle.setText("发布服务");
        mReleaseServiceAdapter = new ReleaseServiceAdapter(this, paths, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                paths.remove(position);
                mReleaseServiceAdapter.notifyDataSetChanged();
            }
        });
        mGridView.setAdapter(mReleaseServiceAdapter);
        mGridView.setOnItemClickListener(this);
        mGridView.setOnItemLongClickListener(this);
        mRg.setOnCheckedChangeListener(this);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.addTab(mTabLayout.newTab().setText("工作日").setTag(1));
        mTabLayout.addTab(mTabLayout.newTab().setText("双休日").setTag(2));
        mTabLayout.addTab(mTabLayout.newTab().setText("计件").setTag(3));
        mTabLayout.setOnTabSelectedListener(this);

        if (getIntent() != null && getIntent().getSerializableExtra("edit") != null) {
            mDataBean = (MySericeBean.DataBean) getIntent().getSerializableExtra("edit");
            id = mDataBean.getId();
            mTvOddSerice.setText(mDataBean.getServiceName());
            mTypeText.setText(mDataBean.getServiceTypeName());
            mPayText.setText(mDataBean.getPrice() + "");
            int demandType = mDataBean.getDemandType();
            mTabLayout.getTabAt(demandType - 1).select();
            mServiceMeterUnit = mDataBean.getServiceMeterUnit() + "";
            switch (mServiceMeterUnit) {
                case 1 + "":
                    mDayRbtn.setChecked(true);
                    break;
                case 2 + "":
                    mHourRbtn.setChecked(true);
                    break;
                case 3 + "":
                    mHourDan.setChecked(true);
                    break;
            }
            if (mDataBean.getImagesUrlList() != null && mDataBean.getImagesUrlList().size() > 0)
                paths.addAll(mDataBean.getImagesUrlList());
            mReleaseServiceAdapter.notifyDataSetChanged();
            mDescText.setText(mDataBean.getServiceDescription());
        }
    }

    @OnClick({R.id.iv_back, R.id.type_text, R.id.tv_relese})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.type_text:
                Intent intent = new Intent(context, LGTypeActivity.class);
                startActivityForResult(intent, 2);
                break;
            case R.id.tv_relese:
                getBitmapList(paths);
                break;
        }
    }

    /**
     * 发布零工技能
     */
    private void relese(String imgs) {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("demandType", demandType);
        hashMap.put("serviceName", mTvOddSerice.getText().toString());
        if (mSelect_data != null) {
            hashMap.put("serviceTypeName", mSelect_data.getDataName());
            hashMap.put("serviceType", mSelect_data.getDataCode());
        } else if (mDataBean != null) {
            hashMap.put("serviceTypeName", mDataBean.getServiceTypeName());
            hashMap.put("serviceType", mDataBean.getServiceType());
        }

        hashMap.put("serviceMeterUnit", mServiceMeterUnit);
        hashMap.put("price", mPayText.getText().toString());

        hashMap.put("format", "json");
        hashMap.put("serviceDescription", mDescText.getText().toString());
        hashMap.put("imagesUrl", getImgUrls(paths));
        hashMap.put("images", imgs == null?"":imgs);
        DataUtils.loadData(this, GetDataConfing.ADDSERVICE, hashMap, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                if(mLoadingDialog.isShowing()){
                    mLoadingDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(dataRequest.getResponseData());

                    if (jsonObject.optInt("code") == 0) {
                        Toast.makeText(context, "发布服务成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(context, "发布服务失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 检查各个字段是否合格
     * @return
     */
    private boolean checkAdd() {
        if (TextUtils.isEmpty(mTvOddSerice.getText())) {
            Toast.makeText(context, "零工名称不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(mPayText.getText())) {
            Toast.makeText(context, "报酬不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(mTypeText.getText())) {
            Toast.makeText(context, "类型不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(mDescText.getText())) {
            Toast.makeText(context, "描述不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if(mDescText.getText().toString().length() <5){
            Toast.makeText(context, "描述内容不能小于5个字", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {//选择照片回调
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if (paths.size() + path.size() <= 3) {
                    paths.addAll(path);
                    mReleaseServiceAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "最多只能添加3张图片", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == 2) {//类型回调
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    mSelect_data = (SelectData) data.getSerializableExtra("select_type");
                    mTypeText.setText(mSelect_data.getDataName() + "");
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mReleaseServiceAdapter.setIsShowDelete(false);
        if (position == paths.size()) {
            MultiImageSelector.create()
                    .count(3)
                    .start(this, 1);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//        Vibrator vib = (Vibrator)this.getSystemService(Service.VIBRATOR_SERVICE);
//        vib.vibrate(200);//只震动一秒，一次
////        long[] pattern = {1000,2000};
//        //两个参数，一个是自定义震动模式，
//        //数组中数字的含义依次是静止的时长，震动时长，静止时长，震动时长。。。时长的单位是毫秒
//        //第二个是“是否反复震动”,-1 不重复震动
//        //第二个参数必须小于pattern的长度，不然会抛ArrayIndexOutOfBoundsException
////        vib.vibrate(pattern, 1);
//        mReleaseServiceAdapter.setIsShowDelete(true);
        return true;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        mServiceMeterUnit = (String) findViewById(checkedId).getTag();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        demandType = (int) tab.getTag() + "";
        switch ((int) tab.getTag()) {
            case 1:
            case 2:
                mHourDan.setVisibility(View.GONE);
                mHourRbtn.setVisibility(View.VISIBLE);
                mDayRbtn.setVisibility(View.VISIBLE);
                mDayRbtn.setChecked(true);
                break;
            case 3:
                mHourDan.setVisibility(View.VISIBLE);
                mHourRbtn.setVisibility(View.GONE);
                mDayRbtn.setVisibility(View.GONE);
                mHourDan.setChecked(true);
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @OnClick(R.id.hour_dan)
    public void onViewClicked() {
    }

    /**
     * 压缩图片 并base64加密 加密成功进行网络请求
     * @param paths
     */
    private void getBitmapList(List<String> paths) {
        if(!checkAdd()){
            return;
        }
        if(mLoadingDialog == null){
            mLoadingDialog = DialogUtils.showLoadingDialog(this);
        }else if(!mLoadingDialog.isShowing()){
            mLoadingDialog.dismiss();
        }
        final List<String> list = new ArrayList<>();
        if (paths.size() > 0) {
            for (int i = 0; i < paths.size(); i++) {
                if (!paths.get(i).startsWith("http")) {
                    list.add(paths.get(i));
                }
            }
            final StringBuffer imgBase64 = new StringBuffer();
            final List<Integer> integers = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Luban.with(this).load(new File(list.get(i))).setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,60,outputStream);
                        String img  =
                                Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
                        try {
                            imgBase64.append(URLEncoder.encode(img,"utf-8")+";");
                            integers.add(0);
                            if(integers.size() == list.size()){
                                relese(imgBase64.toString());
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }finally {
                            try {
                                outputStream.close();
                                bitmap.recycle();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }).launch();
            }
        }else{
            relese(null);
        }
    }

    /**
     * 得到网址
     *
     * @param paths
     * @return
     */
    private String getImgUrls(List<String> paths) {
        StringBuffer sb = new StringBuffer();
        if (paths.size() > 0) {
            for (int i = 0; i < paths.size(); i++) {
                if (paths.get(i).startsWith("http")) {
                    if (sb.length() == 0) {
                        sb.append(paths.get(i));
                    } else {
                        sb.append(";" + paths.get(i));
                    }
                }
            }
        }
        return sb.toString();
    }
}
