package gongren.com.dlg.javabean.master;

import java.util.List;

import gongren.com.dlg.javabean.base.ButtonListBean;
import gongren.com.dlg.javabean.base.CountdownVoBean;

/**
 * Created by Wangjinya on 2017/4/20.
 * 操作按钮实体类
 */

public class MasterStatusBean {
    /**
     * code : 0
     * msg : 成功
     * total : 0
     * totalpage : 0
     * data : [{"businessNumber":"27590422590952619990912588645-R-c8d9497f80614b27837e466dffa62e26-1495679805991","roleType":2,"statusCode":20,"statusText":"等待开工","buttonList":[{"operateStatusCode":3,"operateStatusText":"取消","nextStatusCode":3,"isGray":false}],"assistButtonList":null,"countdownVo":{"thisDate":1495680435292,"remainingTime":464708,"mapTipsText":"后开工","otherMapTipsText":null}}]
     */

    public int code;
    public String msg;
    public int total;
    public int totalpage;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * businessNumber : 27590422590952619990912588645-R-c8d9497f80614b27837e466dffa62e26-1495679805991
         * roleType : 2
         * statusCode : 20
         * statusText : 等待开工
         * buttonList : [{"operateStatusCode":3,"operateStatusText":"取消","nextStatusCode":3,"isGray":false}]
         * assistButtonList : null
         * countdownVo : {"thisDate":1495680435292,"remainingTime":464708,"mapTipsText":"后开工","otherMapTipsText":null}
         */

        public String businessNumber;
        public int roleType;
        public int statusCode;
        public String statusText;
        public Object assistButtonList;
        public CountdownVoBean countdownVo;
        public List<ButtonListBean> buttonList;

    }
//    /**
//     * code : 0
//     * msg : 成功
//     * total : 0
//     * totalpage : 0
//     * data : [{"roleType":2,"statusCode":20,"statusText":"等待开工","operateStatusCode":3,"operateStatusText":"取消","nextStatusCode":3,"messageTemplate":null,"mapPromptText":null,"businessNumber":"27531441038697651487587170788-R-d710a1d0750f4b31ae473eac65f1eae3-1492482409469"}]
//     */
//
//    public int code;
//    public String msg;
//    public int total;
//    public int totalpage;
//    public List<DataBean> data;
//    public static class DataBean {
//        /**
//         * roleType : 2
//         * statusCode : 20
//         * statusText : 等待开工
//         * operateStatusCode : 3
//         * operateStatusText : 取消
//         * nextStatusCode : 3
//         * messageTemplate : null
//         * mapPromptText : null
//         * businessNumber : 27531441038697651487587170788-R-d710a1d0750f4b31ae473eac65f1eae3-1492482409469
//         */
//
//        public int roleType;
//        public int statusCode;
//        public String statusText;
//        public int operateStatusCode;
//        public String operateStatusText;
//        public int nextStatusCode;
//        public String messageTemplate;
//        public String mapPromptText;
//        public String businessNumber;
//    }


}
