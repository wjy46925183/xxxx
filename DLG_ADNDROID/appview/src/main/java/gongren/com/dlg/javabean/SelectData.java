package gongren.com.dlg.javabean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/28.
 */
public class SelectData implements Serializable {

    private String name;
    private String id;
    private String groupCode;
    private String dataCode;
    private String dataName;
    private String dataValue;
    private String dataWeight;
    private String specialIdentification;
    private String code;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    public String getDataWeight() {
        return dataWeight;
    }

    public void setDataWeight(String dataWeight) {
        this.dataWeight = dataWeight;
    }

    public String getSpecialIdentification() {
        return specialIdentification;
    }

    public void setSpecialIdentification(String specialIdentification) {
        this.specialIdentification = specialIdentification;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheched() {
        return isCheched;
    }

    public void setCheched(boolean cheched) {
        isCheched = cheched;
    }

    private boolean isCheched = false;

    @Override
    public String toString() {
        return "SelectData{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", groupCode='" + groupCode + '\'' +
                ", dataCode='" + dataCode + '\'' +
                ", dataName='" + dataName + '\'' +
                ", dataValue='" + dataValue + '\'' +
                ", dataWeight='" + dataWeight + '\'' +
                ", specialIdentification='" + specialIdentification + '\'' +
                ", code='" + code + '\'' +
                ", isCheched=" + isCheched +
                '}';
    }
}
