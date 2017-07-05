package com.dlg.data.model;

import java.io.Serializable;

/**
 * 作者：wangdakuan
 * 主要功能：当前定位的数据
 * 创建时间：2017/6/22 15:10
 * aMapLocation==latitude=31.232434#longitude=121.383917#
 * province=上海市#city=上海市#district=普陀区#cityCode=021#
 * adCode=310107#address=上海市普陀区真江路1500号靠近天地科技广场#
 * country=中国#road=真江路#poiName=天地科技广场#street=真江路#
 * streetNum=1500号#aoiName=天地科技广场#poiid=#floor=#
 * errorCode=0#errorInfo=success#locationDetail=
 * #csid:201fbbcbdad74a8ab30806dc9293e5b1#description=在天地科技广场附近#locationType=5
 */
public class MyMapLocation implements Serializable {

    private double latitude;//纬度
    private double Longitude;//经度
    private String province; //省名
    private String city;  //城市名称
    private String district; //区名
    private String cityCode; //城市ID
    private String adCode; //区ID
    private String address; //详细地址
    private String country; //国家
    private String road; //路名
    private String poiName; //详细名称
    private String street;
    private String streetNum; //牌号
    // TODO: 2017/6/22 暂时用不到
    private String aoiName;
    private String poiid;
    private String floor;
    private String errorCode;
    private String errorInfo;
    private String locationDetail;
    private String description;
    private String locationType;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getPoiName() {
        return poiName;
    }

    public void setPoiName(String poiName) {
        this.poiName = poiName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNum() {
        return streetNum;
    }

    public void setStreetNum(String streetNum) {
        this.streetNum = streetNum;
    }

    public String getAoiName() {
        return aoiName;
    }

    public void setAoiName(String aoiName) {
        this.aoiName = aoiName;
    }

    public String getPoiid() {
        return poiid;
    }

    public void setPoiid(String poiid) {
        this.poiid = poiid;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getLocationDetail() {
        return locationDetail;
    }

    public void setLocationDetail(String locationDetail) {
        this.locationDetail = locationDetail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }
}
