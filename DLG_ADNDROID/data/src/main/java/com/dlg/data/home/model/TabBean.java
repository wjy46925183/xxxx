package com.dlg.data.home.model;

/**
 * Created by wangjinya on 2017/6/19.
 */

public class TabBean {

    /**
     * dataWeight : 0
     * dataValue : 0
     * dataName : 全部
     * id : 180
     * specialIdentification : 0
     * dataCode : job.type_0
     * groupCode : job.type
     */

    private int dataWeight;
    private String dataValue;
    private String dataName;
    private String id;
    private int specialIdentification;
    private String dataCode;
    private String groupCode;

    public int getDataWeight() {
        return dataWeight;
    }

    public void setDataWeight(int dataWeight) {
        this.dataWeight = dataWeight;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSpecialIdentification() {
        return specialIdentification;
    }

    public void setSpecialIdentification(int specialIdentification) {
        this.specialIdentification = specialIdentification;
    }

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    @Override
    public String toString() {
        return "TabBean{" +
                "dataWeight=" + dataWeight +
                ", dataValue='" + dataValue + '\'' +
                ", dataName='" + dataName + '\'' +
                ", id='" + id + '\'' +
                ", specialIdentification=" + specialIdentification +
                ", dataCode='" + dataCode + '\'' +
                ", groupCode='" + groupCode + '\'' +
                '}';
    }
}
