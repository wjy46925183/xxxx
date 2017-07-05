package gongren.com.dlg.javabean.master;

import java.util.List;

/**
 * Created by lenovo on 2017/4/20.
 */

public class MasterRemainTimeBean {

    /**
     * code : 0
     * msg : 成功
     * total : 0
     * totalpage : 0
     * data : [{"thisDate":1492674859111,"startDate":1492747200000,"remainingTime":72340889,"remainingTimeToLate":74140889,"remainingTimeToEnd":null,"businessNumber":"27531441038697651487587170788-R-d710a1d0750f4b31ae473eac65f1eae3-1492482409469"}]
     */

    public int code;
    public String msg;
    public int total;
    public int totalpage;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * thisDate : 1492674859111
         * startDate : 1492747200000
         * remainingTime : 72340889
         * remainingTimeToLate : 74140889
         * remainingTimeToEnd : null
         * businessNumber : 27531441038697651487587170788-R-d710a1d0750f4b31ae473eac65f1eae3-1492482409469
         */

        public long thisDate;
        public long startDate;
        public int remainingTime;
        public int remainingTimeToLate;
        public String remainingTimeToEnd;
        public String businessNumber;
    }
}
