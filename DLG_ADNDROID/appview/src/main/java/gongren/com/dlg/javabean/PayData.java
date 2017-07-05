package gongren.com.dlg.javabean;

/**
 * Created by Administrator on 2017/3/28.
 */
public class PayData {

    private String name;
    private boolean isCheched = false;
    private int income;//工资
    private int xiaofei;//小费


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

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public int getXiaofei() {
        return xiaofei;
    }

    public void setXiaofei(int xiaofei) {
        this.xiaofei = xiaofei;
    }


}
