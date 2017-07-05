package gongren.com.dlg.javabean.worker;

import java.io.Serializable;
import java.util.List;

import gongren.com.dlg.javabean.base.AssistButtonListBean;
import gongren.com.dlg.javabean.base.ButtonListBean;
import gongren.com.dlg.javabean.base.CountdownVoBean;

/**
 * Created by Wangjinya on 2017/5/15.
 */

public class WorkerStatusBean {

    public int code;
    public String msg;
    public List<DataBean> data;


    public static class DataBean  implements Serializable {
        /**
         * businessNumber : 27574680914323367476961916450-R-fea2d976895640d79e816b46458f74d7-1494826447303
         * roleType : 1
         * statusCode : 20
         * statusText : 等待开工
         * buttonList : [{"operateStatusCode":21,"operateStatusText":"开工打卡","nextStatusCode":21,"isGray":true}]
         * assistButtonList : [{"buttonText":"取消","buttonStatus":"101","dataWeight":5,"dataCode":"assist_btn1","groupCode":"assist_btn"},{"buttonText":"分享","buttonStatus":"102","dataWeight":4,"dataCode":"assist_btn2","groupCode":"assist_btn"},{"buttonText":"帮助","buttonStatus":"103","dataWeight":3,"dataCode":"assist_btn3","groupCode":"assist_btn"}]
         * countdownVo : {"thisDate":1494827265225,"remainingTime":4334775,"mapTipsText":"后开工","otherMapTipsText":"请到地图上蓝色范围内进行"}
         */

        public String businessNumber;
        public int roleType;
        public int statusCode;
        public String statusText;
        public CountdownVoBean countdownVo;
        public List<ButtonListBean> buttonList;
        public List<AssistButtonListBean> assistButtonList;
    }
}
