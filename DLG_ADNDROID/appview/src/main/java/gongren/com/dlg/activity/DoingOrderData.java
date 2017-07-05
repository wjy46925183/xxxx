package gongren.com.dlg.activity;

/**
 * Created by Administrator on 2017/3/28.
 */
public class DoingOrderData {
    private String id;
    private String demandType;
    private String jobType;
    private String jobTypeName;
    private String jobMeterUnit;
    private String price;
    private String postTypeName;

    private String code;
    private String postName;
    private String postType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDemandType() {
        return demandType;
    }

    public String getPostTypeName() {
        return postTypeName;
    }

    public void setPostTypeName(String postTypeName) {
        this.postTypeName = postTypeName;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public void setDemandType(String demandType) {
        this.demandType = demandType;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobTypeName() {
        return jobTypeName;
    }

    public void setJobTypeName(String jobTypeName) {
        this.jobTypeName = jobTypeName;
    }

    public String getJobMeterUnit() {
        return jobMeterUnit;
    }

    public void setJobMeterUnit(String jobMeterUnit) {
        this.jobMeterUnit = jobMeterUnit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int status;//状态,1-,2-,3-,4-,5-


    public String getPostName() {
        return postName;
    }

    public void setPostName(String name) {
        this.postName = name;
    }
}
