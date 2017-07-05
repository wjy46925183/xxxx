package gongren.com.dlg.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import gongren.com.dlg.R;
import gongren.com.dlg.cityUtils.JsonFileReader;
import gongren.com.dlg.javabean.JsonBean;
import gongren.com.dlg.utils.BitmapHelper;
import gongren.com.dlg.utils.CacheUtils;
import gongren.com.dlg.utils.CropImage;
import gongren.com.dlg.utils.DataUtils;
import gongren.com.dlg.utils.DimensUtils;
import gongren.com.dlg.utils.PicHelper;
import gongren.com.dlg.utils.ToastUtils;
import gongren.com.dlg.view.AlertView;
import gongren.com.dlg.view.ObservableScrollView;
import gongren.com.dlg.view.OnItemClickListener;

/**
 * Created by Administrator on 2017/6/19 0019.
 */
public class CompanyInfoActivity extends BaseActivity {
    @Bind(R.id.iv_head)
    CircleImageView head;
    @Bind(R.id.tv_certification)
    TextView tvCertification;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.iv_logo)
    ImageView logo;
    @Bind(R.id.ight_img)
    ImageView ightImg;
    @Bind(R.id.layout_logo)
    RelativeLayout layoutLogo;
    @Bind(R.id.et_company_name)
    EditText etCompanyName;
    @Bind(R.id.layout_name)
    LinearLayout layoutName;
    @Bind(R.id.tv_nature)
    TextView nature;
    @Bind(R.id.layout_nature)
    LinearLayout layoutNature;
    @Bind(R.id.tv_industry)
    TextView industry;
    @Bind(R.id.layout_industry)
    LinearLayout layoutIndustry;
    @Bind(R.id.tv_address)
    TextView address;
    @Bind(R.id.layout_address)
    LinearLayout layoutAddress;
    @Bind(R.id.et_introduction)
    EditText etIntroduction;
    @Bind(R.id.et_contact)
    EditText contact;
    @Bind(R.id.et_email)
    EditText email;
    @Bind(R.id.et_phone)
    EditText phone;
    @Bind(R.id.nsv)
    ObservableScrollView nsv;
    @Bind(R.id.root_layout)
    LinearLayout title;

    int code;//区分头像还是logo状态
    private final int SDK_PERMISSION_REQUEST = 127;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_save)
    TextView tvSave;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.actionbar_root)
    RelativeLayout actionbarRoot;
    private String permissionInfo;

    int changeHeight;

    private static final int TAG_CAMERA = 0;     //拍照
    private static final int TAG_LOCAL = 1;      //相册选取
    private static final int TAG_NATURE = 2;//性质
    private static final int TAG_INDUSTRY = 3;//行业

    private File img;     //身份证手持
    private AlertView alertView = null;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_userinfo);
        ButterKnife.bind(this);
        initJsonData();
        initView();

    }

    private void initView() {
        titleChangeByScrow();
        tvTitle.setBackgroundColor(getResources().getColor(R.color.app_color_transparent));
        tvSave.setBackgroundColor(getResources().getColor(R.color.app_color_transparent));
        ivBack.setBackgroundColor(getResources().getColor(R.color.app_color_transparent));
        title.setBackgroundColor(getResources().getColor(R.color.app_color_transparent));
        tvTitle.setTextColor(getResources().getColor(R.color.white));
        tvSave.setTextColor(getResources().getColor(R.color.white));
        titleChangeByScrow();        //scrow和title的随动改变

    }

    private void titleChangeByScrow() {
        changeHeight = DimensUtils.dip2px(context, 44.0f);
        nsv.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(View view, int x, int y, int oldx, int oldy) {
                if (y > changeHeight) {//超出了渐变的最高高度，直接修改布局。
                    title.getBackground().mutate().setAlpha(255);
                    ivBack.setImageResource(R.mipmap.back_black);
                    tvTitle.setTextColor(getResources().getColor(R.color.black_textcolor));
                    tvSave.setTextColor(getResources().getColor(R.color.black_textcolor));
                } else {//背景渐变
                    float bili = y / (changeHeight * 1.0f);
                    title.getBackground().mutate().setAlpha(DataUtils.getIntFromDouble(255 * bili));
                    ivBack.setImageResource(R.mipmap.back);
                    tvTitle.setTextColor(getResources().getColor(R.color.white_color));
                    tvSave.setTextColor(getResources().getColor(R.color.white_color));
                }
                title.invalidate();
            }
        });
    }


    @OnClick({R.id.iv_head, R.id.layout_logo, R.id.layout_nature, R.id.layout_industry, R.id.layout_address, R.id.iv_back, R.id.tv_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_head:
                code = 0;
                checkCameraPermision();
                break;
            case R.id.layout_logo:
                code = 1;
                checkCameraPermision();
                break;
            case R.id.layout_nature://性质
                go2SelectNature();
                break;
            case R.id.layout_industry://产业
                gotoSelectIndustry();
                break;
            case R.id.layout_address:
                selectCity();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save://保存
                break;
        }
    }

    private void gotoSelectIndustry() {
        /*Intent intent = new Intent(this, SelectIndustryActivity.class);
        startActivityForResult(intent, TAG_INDUSTRY);*/
    }

    private void go2SelectNature() {
        Intent intent = new Intent(this, SelectNatureActivity.class);
        startActivityForResult(intent, TAG_NATURE);

    }

    private void selectCity() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String text = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);
                address.setText(text);
            }
        }).setTitleText("")
                .setDividerColor(Color.GRAY)
                .setTextColorCenter(Color.GRAY)
                .setContentTextSize(13)
                .setOutSideCancelable(false)
                .build();
          /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void initJsonData() {   //解析数据
        String JsonData = JsonFileReader.getJson(this, "province_data.json");
        ArrayList<JsonBean> jsonBean = parseData(JsonData);

        /**
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
    }

    public ArrayList<JsonBean> parseData(String result) {
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    @TargetApi(23)
    public void checkCameraPermision() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
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
                            File file = new File(CacheUtils.productPath + "/Photo");
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
                ToastUtils.showToastLong(context, "如果你想使用与摄像头有关的功能，请到系统设置，手动开启权限。");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAG_CAMERA:      //拍的照片
                    String path = img.getAbsolutePath();
                    if (!TextUtils.isEmpty(path)) {
                        img = BitmapHelper.scal(path);
                        Glide.with(context)
                                .load(img)
                                .placeholder(R.mipmap.icon_default_user)
                                .error(R.mipmap.icon_default_user)
                                .dontAnimate()
                                .override(600, 200)
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(code == 0 ? head : logo);
                    }
                    break;
                case TAG_LOCAL:       //本地选取
                    Uri selectedImage = data.getData();
                    String imgPath = PicHelper.getPath(context, selectedImage);
                    if (!TextUtils.isEmpty(imgPath)) {
                        img = BitmapHelper.scal(imgPath);
                        Glide.with(context)
                                .load(Uri.fromFile(img))
                                .placeholder(R.mipmap.icon_default_user)
                                .error(R.mipmap.icon_default_user)
                                .dontAnimate()
                                .override(600, 200)
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(code == 0 ? head : logo);
                    }
                    break;
                case TAG_NATURE:    //企业性质
                    String type = data.getStringExtra("nature_type");
                    nature.setText(type == null ? "自由企业" : type);
                    break;
                case TAG_INDUSTRY:

                    break;

            }
        }
    }

}
