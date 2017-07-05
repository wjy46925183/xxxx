package gongren.com.dlg.javabean.master;

import java.util.List;

/**
 * Created by Wangjinya on 2017/4/22.
 */

public class AddressBean {
    public int code;
    public String msg;
    public List<DataBean> data;

    public static class DataBean {
        public String areaCode;
        public int areaLevel;
        public String areaName;
        public int areaWeight;
        public String isCity;
        public int isLeaf;
        public String isHot;
        public String parentId;
    }
}
