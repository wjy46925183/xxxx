package gongren.com.dlg.javabean;

import java.io.Serializable;
import java.util.Comparator;

/**
 * 雇员零工
 */
public class EmployeeJob implements Serializable,Comparator<EmployeeJob>{
    public String areaName;
    public String cityName;
    public int demandType;
    public String distance;
    public int endDay;
    public int endHour;
    public int endMinute;
    public int endMonth;
    public String endSecond;
    public int endYear;
    public String id;
    public String jobMeterUnit;
    public String jobMeterUnitName;
    public String postName;
    public String postTypeName;
    public String price;
    public String provinceName;
    public int startDay;
    public int startHour;
    public int startMinute;
    public int startMonth;
    public String startSecond;
    public int startYear;
    public String status;
    public String userCreditCount;
    public String userId;
    public String userLogo;
    public String villageName;
    public String workAddress;
    public double xCoordinate;
    public double yCoordinate;
    public int isFarmersInsurance;

    @Override
    public int compare(EmployeeJob lhs, EmployeeJob rhs) {
        return lhs.id.compareTo(rhs.id);
    }
}
