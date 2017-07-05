package gongren.com.dlg.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.common.utils.SharedPreferencesUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import de.hdodenhof.circleimageview.CircleImageView;
import gongren.com.dlg.R;
import gongren.com.dlg.cityUtils.ArrayWheelAdapter;
import gongren.com.dlg.cityUtils.OnWheelScrollListener;
import gongren.com.dlg.cityUtils.WheelView;
import gongren.com.dlg.javabean.RefreshEvent;
import gongren.com.dlg.javabean.UserInfomationData;
import gongren.com.dlg.javabean.master.AddressBean;
import gongren.com.dlg.utils.BaseMapUtils;
import gongren.com.dlg.utils.BitmapHelper;
import gongren.com.dlg.utils.CropImage;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.DialogUtils;
import gongren.com.dlg.utils.DimensUtils;
import gongren.com.dlg.utils.DlgImageLoader;
import gongren.com.dlg.utils.DlgUtils;
import gongren.com.dlg.utils.LogUtils;
import gongren.com.dlg.utils.PicHelper;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.utils.WorkbenchManager;
import gongren.com.dlg.view.AlertView;
import gongren.com.dlg.view.ObservableScrollView;
import gongren.com.dlg.view.OnItemClickListener;
import gongren.com.dlg.volleyUtils.DataRequest;
import gongren.com.dlg.volleyUtils.GetDataConfing;
import gongren.com.dlg.volleyUtils.ResponseDataCallback;
import gongren.com.dlg.volleyUtils.ShowGetDataError;

/**
 * 设置界面（雇主、雇员共用一个）
 */
public class UserInfoActivity extends BaseActivity implements OnWheelScrollListener {
    private final int SELECT_LG_TYPE = 10;
    private final int SELECT_ADDRESS = 11;


    private static final int TAG_EXIT = 0;      //退出登录
    private final int TAG_GET_INFORMATION = 0x6001;     //请求用户资料信息
    private final int TAG_UPDATE_INFORMATION = 0x6002;     //更新用户资料信息
    private final int TAG_UPDATE_INFORMATION_MAIN = 0x6003;     //更新用户资料的主表信息
    private final int TAG_UPDATE_INFORMATION_ICON = 0x6004;


    RelativeLayout userHeadRe = null;
    @Bind(R.id.iv_head)
    CircleImageView ivHead;
    @Bind(R.id.iv_phone)
    TextView headName;
    @Bind(R.id.head_linear)
    LinearLayout headLinear;
    @Bind(R.id.xm_text)
    EditText ncText;        //昵称
    @Bind(R.id.name_text)
    TextView realNameText;
    @Bind(R.id.name_right_img)
    ImageView nameRightImg;
    @Bind(R.id.sf_text)
    TextView sfText;       //身份
    @Bind(R.id.height_text)
    EditText heightText;
    @Bind(R.id.weight_text)
    EditText weightText;
    @Bind(R.id.liveaddress_text)
    TextView liveAddress;
    @Bind(R.id.email_text)
    EditText emailText;
    @Bind(R.id.qq_text)
    EditText qqText;
    @Bind(R.id.desc_text)
    EditText descText;
    @Bind(R.id.m_scrollview)
    ObservableScrollView mScrollview;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_save)
    TextView tvSave;
    @Bind(R.id.idCard)
    LinearLayout idCard;
    @Bind(R.id.ft)
    FrameLayout ft;

    private Intent mIntent;
    private Resources res;//取资源的对象
    private Context context;
    private int changeHeight = 0;//渐变的高度
    private boolean isguyuan = true;//判断是否是雇员。

    private final String PROVINCE_PARENTID = "0";
    private final String PROVINCE_AREALEVEL = "1";//区域级别 省
    private final String CITY_AREALEVEL = "2";//区域级别 市
    private final String AREA_AREALEVEL = "3";//区域级别 区
    private String path;//图片全路径

    private Dialog dialog;
    private Gson gson = new Gson();
    private List<AddressBean.DataBean> provinceDatas;
    private List<AddressBean.DataBean> cityDatas;
    private List<AddressBean.DataBean> areaDatas;
    private WheelView mWheelView_pro;
    private WheelView mWheelView_city;
    private WheelView mWheelView_area;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        ButterKnife.bind(this);
        context = this;
        res = getResources();
        getParams();
        initView();
        initEvent();
        getUserInformationData();

    }

    @Subscribe
    public void onMessageEvent(RefreshEvent event) {
        switch (event.getTag()) {
            case RefreshEvent.USER_INFO_PAGE:
                getUserInformationData();
                break;
        }
    }

    public void getParams() {
        Intent intent = getIntent();
        if (intent != null) {
            isguyuan = intent.getBooleanExtra("isguyuan", true);
        }
        provinceDatas = new ArrayList<>();
        cityDatas = new ArrayList<>();
        areaDatas = new ArrayList<>();
    }

    private void initView() {
            String headpath=SharedPreferencesUtils.getString(context,"imagepath");
            img = BitmapHelper.scal(headpath);
            Glide.with(context)
                    .load(Uri.fromFile(img))
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .dontAnimate()
                    .override(600, 200)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivHead);

        //验证成功的不可点击
        if (SharedPreferencesUtils.getString(this, SharedPreferencesUtils.STATUS) == "2") {
            realNameText.setEnabled(false);

        }
        String string = SharedPreferencesUtils.getString(this, SharedPreferencesUtils.REAL_NAME);
        if (SharedPreferencesUtils.getString(this, SharedPreferencesUtils.STATUS).equals("2")) {
            realNameText.setText(TextUtils.isEmpty(string) ? "暂无" : string);
        } else {
            realNameText.setText("未实名认证");
        }

        //根据用户身份，设置不同的头部背景颜色
        if (isguyuan) {//雇员
            headLinear.setBackgroundResource(R.mipmap.ditu);
        } else {//雇主
            headLinear.setBackgroundResource(R.mipmap.ditublack);
        }

        ivBack.setImageResource(R.mipmap.back);
        ivBack.setBackgroundColor(getResources().getColor(R.color.cmbkb_transparent));
        tvTitle.setTextColor(getResources().getColor(R.color.white));
        tvTitle.setText("个人资料");

        tvSave.setTextColor(getResources().getColor(R.color.white));
        tvSave.setBackgroundColor(getResources().getColor(R.color.cmbkb_transparent));

        userHeadRe = (RelativeLayout) findViewById(R.id.userinfo_head);
        userHeadRe.getBackground().mutate().setAlpha(0);
        ncText.setText(TextUtils.isEmpty(SharedPreferencesUtils.getString(this, SharedPreferencesUtils.UserName)) ? SharedPreferencesUtils.getString(this, SharedPreferencesUtils.MOBILE) : SharedPreferencesUtils.getString(this, SharedPreferencesUtils.UserName));
//        userHeadRe.setBackgroundColor(getResources().getColor(R.color.tmwhite_color));

        //获取头部渐变的总高度.
        getChangeHeight();

        //监听scrollview滚动
        setScrollViewScrollListener();
    }

    private void initEvent() {
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInformation();

            }
        });
    }


    private void setScrollViewScrollListener() {
        mScrollview.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > changeHeight) {//超出了渐变的最高高度，直接修改布局。
                    userHeadRe.getBackground().mutate().setAlpha(255);
                    ivBack.setImageResource(R.mipmap.back_black);
                    tvTitle.setTextColor(res.getColor(R.color.black_textcolor));
                    tvSave.setTextColor(getResources().getColor(R.color.black_textcolor));
                } else {//背景渐变
                    float bili = scrollY / (changeHeight * 1.0f);
                    userHeadRe.getBackground().mutate().setAlpha(DataUtils.getIntFromDouble(255 * bili));
                    ivBack.setImageResource(R.mipmap.back);
                    tvTitle.setTextColor(res.getColor(R.color.white_color));
                    tvSave.setTextColor(getResources().getColor(R.color.white_color));
                }
                userHeadRe.invalidate();
            }
        });
    }

    public void getChangeHeight() {
        changeHeight = DimensUtils.dip2px(context, 44.0f);
    }

    @OnClick({R.id.iv_back, R.id.iv_head, R.id.userinfo_content, R.id.liveaddress_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:      //返回
                finish();
                break;
            case R.id.iv_head://更头像
                isClick = true;
                getCameraPersimmions();
                break;
            case R.id.liveaddress_text:
//                Intent intent = new Intent(context, SelectAddressActivity.class);
//                startActivityForResult(intent, SELECT_ADDRESS);
                if(null == mPopupWindow){
                    getHttpAddressData(PROVINCE_PARENTID, PROVINCE_AREALEVEL);
                    View inflate = LayoutInflater.from(this).inflate(R.layout.wheel_picker, null);
                    mPopupWindow = new PopupWindow(inflate,
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

//                mPopupWindow.setWidth(getResources().getDisplayMetrics().widthPixels);

                    mWheelView_pro = (WheelView) inflate.findViewById(R.id.id_province);
                    mWheelView_city = (WheelView) inflate.findViewById(R.id.id_city);
                    mWheelView_area = (WheelView) inflate.findViewById(R.id.id_area);
                    mWheelView_pro.addScrollingListener(this);
                    mWheelView_city.addScrollingListener(this);
                    mWheelView_area.addScrollingListener(this);


                    Button btn_cancle = (Button) inflate.findViewById(R.id.btn_cancle);
                    Button btn_confirm = (Button) inflate.findViewById(R.id.btn_confirm);

                    btn_cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPopupWindow.dismiss();
                        }
                    });

                    btn_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            liveAddress.setText(provinceDatas.get(currentPostionPro).areaName + cityDatas.get(currentPostionCity).areaName + areaDatas.get(currentPostionArea).areaName);
                            mPopupWindow.dismiss();
                        }
                    });
                }else {
                    initUi();
                }

//                mPopupWindow.setContentView(inflate);
                break;
        }
    }

    /**
     * 获取地址
     *
     * @param areaCode
     * @param areaType
     */
    private void getHttpAddressData(final String areaCode, final String areaType) {
        Map<String, Object> map = BaseMapUtils.getMap(this);
        map.put("parentId", areaCode);
        map.put("areaLevel", areaType);//区域级别1.省 2.市3.区县4.乡镇
        map.put("format", "json");
        DataUtils.loadData(this, GetDataConfing.ADDRESS, map, 1, new ResponseDataCallback() {
            @Override
            public void onFinish(DataRequest dataRequest) {
                if (dataRequest.isNetError()) {
                    ShowGetDataError.showNetError(UserInfoActivity.this);
                } else {
                    String responseData = dataRequest.getResponseData();
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        if ("0".equals(jsonObject.optString("code"))) {
                            AddressBean addressBean = new Gson().fromJson(responseData, AddressBean.class);
                            if (addressBean != null && addressBean.data != null && addressBean.data.size() > 0)
                                initAddress(areaType,addressBean);
                        } else {
                            ToastUtils.showToastShort(UserInfoActivity.this, "获取地址失败");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

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
        }
    }

    private int currentPostionPro = 0;
    private int currentPostionCity = 0;
    private int currentPostionArea = 0;
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
                    initUi();
                    break;
            }
        }
    };
    //加载ui
    private void initUi() {
        mWheelView_pro.setViewAdapter(new ArrayWheelAdapter<>(UserInfoActivity.this, provinceDatas));
        mWheelView_pro.setCurrentItem(currentPostionPro);
        mWheelView_pro.setWheelBackground(R.color.white_color);
        mWheelView_city.setViewAdapter(new ArrayWheelAdapter<>(UserInfoActivity.this, cityDatas));
        mWheelView_city.setWheelBackground(R.color.white_color);
        mWheelView_area.setViewAdapter(new ArrayWheelAdapter<>(UserInfoActivity.this, areaDatas));
        mWheelView_area.setWheelBackground(R.color.white_color);
        //显示PopupWindow
        View rootview = LayoutInflater.from(this).inflate(R.layout.activity_userinfo, null);
        mPopupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
//        mPopupWindow.showAtLocation(ft, Gravity.BOTTOM,0,0);
    }

    //跳转到实名认证。如果没有实名认证， 就跳转到实名认证。
    public void go2ShiMing(View view) {
        if (SharedPreferencesUtils.getString(context,SharedPreferencesUtils.STATUS).equals("2")){
            startActivity(new Intent(context, RealNameAuthenticationActivity.class));//实名认证
        }else {
            startActivity(new Intent(context, RealNameAuthenticTypeActivity.class));
        }

    }

    //选择身份
    public void selectSF(View view) {
        chooseSF();
    }

    /**
     * 数据请求回调接口
     */
    private ResponseDataCallback responseDataCallback = new ResponseDataCallback() {
        @Override
        public void onFinish(DataRequest dataRequest) {
            int tag = dataRequest.getWhat();
            if (dialog != null) {
                dialog.dismiss();
            }
            if (tag == TAG_GET_INFORMATION) {
                getInformationCallBack(dataRequest);
            } else {
                updateInformationCallBack(dataRequest);
            }
        }
    };


    /**
     * 请求用户信息回调处理
     *
     * @param dataRequest
     */
    private void getInformationCallBack(DataRequest dataRequest) {
        if (tvTitle != null) {
            if (dataRequest.isNetError()) {
                if (dataRequest.getWhat() != TAG_EXIT)
                    ShowGetDataError.showNetError(context);
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(dataRequest.getResponseData());
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    UserInfomationData userInfomationData = gson.fromJson(jsonArray.getString(0), UserInfomationData.class);
                    bindViewData(userInfomationData);

                    SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.REAL_NAME,
                            userInfomationData.userAttributeRestVo.name);
                    SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.CERTIFICATENUMBER,
                            userInfomationData.userAttributeRestVo.certificateNumber);

                    WorkbenchManager.getInstance(context).addUserLogin();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String json = dataRequest.getResponseData();
                if (!TextUtils.isEmpty(json)) {
                    if (dataRequest.getWhat() == TAG_EXIT) {                //退出登录

                    }
                }
            }
        }
    }

    /**
     * 更新用户信息回调处理
     *
     * @param dataRequest
     */
    private void updateInformationCallBack(DataRequest dataRequest) {
        if (!dataRequest.isNetError()) {
            try {
                JSONObject jsonObject = new JSONObject(dataRequest.getResponseData());
                int code = jsonObject.getInt("code");
                if (code == 0) {
                    ToastUtils.showToastLong(context, "保存成功");
                    //TODO
                    if (null != ncText) {
                        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.UserName, ncText.getText().toString().trim());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (alertView != null && alertView.isShowing()) {
                alertView.dismiss();
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    /**
     * 选择照片
     */
    private static final int TAG_CAMERA = 0;     //拍照
    private static final int TAG_LOCAL = 1;      //相册选取
    private static final int TAG_COMPLETE=2;        //拍照选取完成
    private File img;     //身份证手持
    private AlertView alertView = null;
    private boolean isClick = false;

    private AlertDialog alertDialog = null;

    private void choosePhoto() {

        alertView = new AlertView(null, null, "取消", null, new String[]{"拍照", "相册"}, context, AlertView.Style.ActionSheet,
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (position == 0) {      //拍照上传
                            Intent intent = new Intent();
                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (!Environment.getExternalStorageState().equals(
                                    Environment.MEDIA_MOUNTED)) {
                                return;
                            }

                            File file = getCacheDir(context,"photo");
                            if (!file.exists())
                                file.mkdirs();

                            img = new File(file, "IMG" + System.currentTimeMillis() + ".jpg");
                            if (!img.exists()) {
                                try {
                                    img.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(img));
                            startActivityForResult(intent, TAG_CAMERA);
                        } else if (position == 1) {     //从相册选取
                            Intent intent = CropImage.getImageIntent();
                            startActivityForResult(intent, TAG_LOCAL);
                        }
                    }
                });

        alertView.setView(true, true, null).setCancelable(true);
        alertView.show();
    }


    private File getCacheDir(Context context, String cacheName) {
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null) {
            cacheDir = context.getCacheDir();
        }
        cacheDir = new File(cacheDir, cacheName);
        return cacheDir;
    }

    //选择身份
    String[] sfArray = new String[]{"兼职人员", "自由工作者", "在校学生"};

    private void chooseSF() {
        alertView = new AlertView(null, null, "取消", null, sfArray, context, AlertView.Style.ActionSheet,

                new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {

                        if (position != -1) {
                            sfText.setText(sfArray[position]);
                        }
                    }
                });

        String sf = sfText.getText().toString();
        alertView.setView(true, false, sf).setCancelable(true);

        alertView.show();
    }

    /**
     * 图片选择及拍照结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAG_CAMERA:      //拍照的照片
                    /*String path = img.getAbsolutePath();
                    if (!TextUtils.isEmpty(path)) {
//                        img = new File(path);
                        img = BitmapHelper.scal(path);
                        Glide.with(context)
                                .load(img)
                                .placeholder(R.mipmap.icon_default_user)
                                .error(R.mipmap.icon_default_user)
                                .dontAnimate()
                                .override(600, 200)
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(ivHead);

                        uploadImage(img);
                    }*/
                    path = img.getAbsolutePath();
                    Intent intent2=new Intent(UserInfoActivity.this, CropHeadActivity.class);
                    intent2.putExtra("path", path);
                    startActivityForResult(intent2, TAG_COMPLETE);
                    break;
                case TAG_LOCAL:       //本地选取
                    Uri selectedImage = data.getData();
                    path = PicHelper.getPath(context, selectedImage);
                    Intent intent3=new Intent(UserInfoActivity.this, CropHeadActivity.class);
                    intent3.putExtra("path", path);
                    startActivityForResult(intent3, TAG_COMPLETE);
                    break;
                case TAG_COMPLETE:

                    final Uri path = data.getData();
                    img =new File(path.getPath());
                    Glide.with(context)
                            .load(path)
                            .placeholder(R.mipmap.icon_default_user)
                            .error(R.mipmap.icon_default_user)
                            .dontAnimate()
                            .override(600, 200)
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(ivHead);
                    uploadImage(img);
                    SharedPreferencesUtils.saveString(context,"imagepath",path.getPath());

                    break;
                case 10:
//                    DegreeTv.setText(data.getStringExtra("name"));
                    break;
            }
        }
    }
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void uploadImage(File file) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            map.put("base64Str", URLEncoder.encode(getImageStr(file), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        map.put("format", "json");
        DataUtils.loadData(this, GetDataConfing.upload_user_head_icon, map, 0, new ResponseDataCallback() {

            @Override
            public void onFinish(DataRequest dataRequest) {
                String responseData = dataRequest.getResponseData();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    getUserInformationData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String getImageStr(File file) {
        ByteArrayOutputStream stream = null;
        try{
            Bitmap photo = null;
            if (file != null) {
                photo = BitmapFactory.decodeFile(file.getPath());
            }
            return  Bitmap2StrByBase64(photo);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                stream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 通过Base32将Bitmap转换成Base64字符串
     * @param bit
     * @return
     */
    public String Bitmap2StrByBase64(Bitmap bit){
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 100, bos);//参数100表示不压缩
        byte[] bytes=bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }


    private final int SDK_PERMISSION_REQUEST = 127;
    private String permissionInfo;

    @TargetApi(23)
    public void getCameraPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /**
             * 权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 使用摄像头
            if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);
            }

            // 读写权限
            if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            } else {
                choosePhoto();
            }
        } else {
            choosePhoto();
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }

    @TargetApi(23)
    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (SDK_PERMISSION_REQUEST == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 授权
                choosePhoto();
            } else {
                // 未授权,因为小米6.0系统请求一次，拒绝之后，就不再显示，所以提示用户去手动开启权限。
                ToastUtils.showToastLong(context, "如果你想使用与摄像头有关的功能，请到系统设置，手动开启权限。");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        liveAddress.setText(SharedPreferencesUtils.getString(this, "fullAddress"));
    }

    /**
     * 请求用户信息 辛晓龙
     */
    public void getUserInformationData() {
        Map<String, Object> map = new HashMap<>();
        map.put("format", "json");
        DataUtils.loadData(context, GetDataConfing.get_user_information, map, TAG_GET_INFORMATION, responseDataCallback);
        dialog = DialogUtils.showLoadingDialog(this);
        LogUtils.logE("TAG_UPLOAD", 2 + "");
    }

    /**
     * 绑定view数据 统一处理null的值
     *
     * @param editText
     * @param value
     */
    private void setViewText(EditText editText, String value) {
        editText.setText(value == null ? "" : value);
    }

    /**
     * 绑定view数据 统一处理null的值
     *
     * @param textView
     * @param value
     */
    private void setViewText(TextView textView, String value) {
        textView.setText(value == null ? "" : value);
    }


    /**
     * 用户资料数据绑定到视图
     *
     * @param userInfomationData
     */
    //TODO
    public void bindViewData(UserInfomationData userInfomationData) {
        UserInfomationData.UserRestVo userRestVo = userInfomationData.userRestVo;
        UserInfomationData.UserAttributeRestVo userAttributeRestVo = userInfomationData.userAttributeRestVo;
        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.FILELOGO, userAttributeRestVo.logo);
        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.UserName, userRestVo.username);
        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.REAL_NAME, userAttributeRestVo.name);


        //todo ivHead 加载头像
        DlgImageLoader.loadImage(this, userAttributeRestVo.logo, ivHead);
        if (userRestVo.auditStatus.equals("2")) {
            realNameText.setText(TextUtils.isEmpty(userAttributeRestVo.name) ? "暂无" : userAttributeRestVo.name);
            realNameText.setEnabled(false);

        }
        if (!TextUtils.isEmpty(userRestVo.username)) {
            headName.setText(userRestVo.username == null ? "暂无" : userRestVo.username);
            ncText.setText(userRestVo.username);
        } else {
            if (!TextUtils.isEmpty(userAttributeRestVo.name) && TextUtils.isEmpty(userRestVo.username)) {
                headName.setText(userAttributeRestVo.name);
                ncText.setText(DlgUtils.showMidHintPhone(userRestVo.phone));
            } else {
                headName.setText(DlgUtils.showMidHintPhone(userRestVo.phone));
                ncText.setText(DlgUtils.showMidHintPhone(userRestVo.phone));
            }
        }
        setViewText(sfText, userAttributeRestVo.getIdentityName());
        setViewText(heightText, userAttributeRestVo.height);
        setViewText(weightText, userAttributeRestVo.weight);
        setViewText(liveAddress, userAttributeRestVo.location);
        setViewText(emailText, userRestVo.email);
        setViewText(qqText, userRestVo.oicq);
        setViewText(descText, userAttributeRestVo.personalizedSignature);
        SharedPreferencesUtils.saveString(context, SharedPreferencesUtils.STATUS, userRestVo.auditStatus);
    }


    /**
     * 保存用户信息 辛晓龙
     */
    private void saveInformation() {
        buildInformationMap();
        DataUtils.loadData(context, GetDataConfing.update_user_information, updateInformationMap, TAG_UPDATE_INFORMATION, responseDataCallback);
        DataUtils.loadData(context, GetDataConfing.update_user_information_main, updateInformationMapMain, TAG_UPDATE_INFORMATION_MAIN, responseDataCallback);
        dialog = DialogUtils.showLoadingDialog(this);
    }

    Map<String, Object> updateInformationMap = new HashMap<>();      //更新字表的map参数
    Map<String, Object> updateInformationMapMain = new HashMap<>(); //更新主表的map参数

    /**
     * 视图上的数据放入map中
     *
     * @return
     */
    public void buildInformationMap() {
        String userName = ncText.getText().toString();
        String identityType = UserInfomationData.getIdentityTypeByName(sfText.getText().toString());
        String height = heightText.getText().toString();
        String weight = weightText.getText().toString();
        String address = liveAddress.getText().toString();
        String email = emailText.getText().toString();
        String qq = qqText.getText().toString();
        String desc = descText.getText().toString();

        //字表的数据
        updateInformationMap.put("format", "json");
        updateInformationMap.put("height", height);
        updateInformationMap.put("weight", weight);
        updateInformationMap.put("identity", identityType);
        updateInformationMap.put("location", address);                        //缺少字段
        updateInformationMap.put("personalizedSignature", desc);            //缺少字段
        //主表的数据

        updateInformationMapMain.put("format", "json");
        updateInformationMapMain.put("username", userName);
        updateInformationMapMain.put("email", email);
        updateInformationMapMain.put("oicq", qq);
        updateInformationMapMain.put("id", SharedPreferencesUtils.getString(getBaseContext(), SharedPreferencesUtils.USERID));
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
        }
    }
}
