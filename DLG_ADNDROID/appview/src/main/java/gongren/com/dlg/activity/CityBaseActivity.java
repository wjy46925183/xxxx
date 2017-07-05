package gongren.com.dlg.activity;

import android.content.res.AssetManager;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import gongren.com.dlg.javabean.ProvinceModel;

public class CityBaseActivity extends BaseActivity {

    /**
     * 省的集合
     */
    protected ProvinceModel.DataEntity[] mProvinceDatas;
    /**
     * key - 省 value - 市的集合
     */
    protected Map<String, ProvinceModel.DataEntity.CityRankListEntity[]> mCitisDatasMap = new TreeMap<>();
    /**
     * key - 市 values - 县区
     */
    protected Map<String, ProvinceModel.DataEntity.CityRankListEntity.AreaRankListEntity[]> mDistrictDatasMap = new TreeMap<>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new TreeMap<>();

    /**
     * 当前选择的省
     */
    protected String mCurrentProviceName;
    /**
     * 当前选择的市
     */
    protected String mCurrentCityName;
    /**
     * 当前选择的县区
     */
    protected String mCurrentDistrictName = "";

    /**
     * 县区的编码
     */
    protected String mCurrentZipCode = "";

    /**
     * 读取本地的xml文件
     */

    private String loadTextFile(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[4096];
        int len = 0;
        while ((len = inputStream.read(bytes)) > 0) {
            byteArrayOutputStream.write(bytes, 0, len);
        }
        return new String(byteArrayOutputStream.toByteArray(), "UTF-8");
    }

    protected void initProvinceDatas() {
        List<ProvinceModel.DataEntity> provinceList = new ArrayList<>();
        AssetManager asset = getAssets();
        InputStream inputStream;
        try {
            inputStream = asset.open("address.txt");
            String text = loadTextFile(inputStream);
            Gson gson = new Gson();
            ProvinceModel model = gson.fromJson(text, ProvinceModel.class);

            for (int i = 0; i < model.getData().size(); i++) {
                // 将获取的集合赋值给集合
                provinceList.add(model.getData().get(i));
            }

            // 循环获得的省集合
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();

                List<ProvinceModel.DataEntity.CityRankListEntity> cityList
                        = provinceList.get(0).getRankList();

                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<ProvinceModel.DataEntity.CityRankListEntity.AreaRankListEntity> districtList
                            = cityList.get(0).getRankList();

                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getCode();
                }
            }
            //
            mProvinceDatas = new ProvinceModel.DataEntity[provinceList.size()];

            for (int i = 0; i < provinceList.size(); i++) {
                // 获取当前所有的省名称
                mProvinceDatas[i] = provinceList.get(i);

                List<ProvinceModel.DataEntity.CityRankListEntity> cityLists
                        = provinceList.get(i).getRankList();

                ProvinceModel.DataEntity.CityRankListEntity[] cityNames =
                        new ProvinceModel.DataEntity.CityRankListEntity[cityLists.size()];
                for (int j = 0; j < cityLists.size(); j++) {
                    // 获取省对应当前的所有市
                    cityNames[j] = cityLists.get(j);
                    List<ProvinceModel.DataEntity.CityRankListEntity.AreaRankListEntity> districtList
                            = cityNames[j].getRankList();

                    ProvinceModel.DataEntity.CityRankListEntity.AreaRankListEntity[] distrinctArray
                            = new ProvinceModel.DataEntity.CityRankListEntity.AreaRankListEntity[districtList.size()];
                    // 获取省对应当前的所有市区
                    for (int k = 0; k <districtList.size(); k++) {

                        ProvinceModel.DataEntity.CityRankListEntity.AreaRankListEntity districtModel
                                = new ProvinceModel.DataEntity.CityRankListEntity.AreaRankListEntity(
                                districtList.get(k).getParent(),
                                districtList.get(k).getWeight(),
                                districtList.get(k).getIscity(),
                                districtList.get(k).getId(),
                                districtList.get(k).getCode(),
                                districtList.get(k).getLevel(),
                                districtList.get(k).getIsleaf(),
                                districtList.get(k).getActive(),
                                districtList.get(k).getName());

                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getCode(),
                                mProvinceDatas[i].getCode()+"=="+mProvinceDatas[i].getRankList().get(j).getCode()+"=="+
                                        mProvinceDatas[i].getRankList().get(j).getRankList().get(k).getCode());

                        distrinctArray[k] = districtModel;
                    }
                    mDistrictDatasMap.put(cityLists.get(j).getName(), distrinctArray);
                }
                mCitisDatasMap.put(mProvinceDatas[i].getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
        }
    }
}
