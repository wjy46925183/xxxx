package gongren.com.dlg.javabean.master;

import java.util.List;

/**
 * Created by lenovo on 2017/5/16.
 */

public class BossMapMsgBean {

    /**
     * code : 0
     * total : 0
     * totalpage : 0
     * data : [{"xCoordinate":121.383887,"yCoordinate":31.232342,"userLogo":"http://s.gongren.com/dds/img/oddjob/logo/731910/s27576315923037596851427339356.jpg","userId":"27576167168493386078838751183"},{"xCoordinate":121.383914,"yCoordinate":31.23232,"userLogo":"http://s.chengxinhutong.com/dds/img/gongren/dlg/logo/559561/s839450436430860288.png","userId":"27576098952433801449595634279"},{"xCoordinate":121.383886,"yCoordinate":31.232306,"userLogo":"http://s.gongren.com/dds/img/oddjob/logo/845690/s27576112068068838287172430934.jpg","userId":"27576090079549901995301306952"}]
     */

    public int code;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * xCoordinate : 121.383887
         * yCoordinate : 31.232342
         * userLogo : http://s.gongren.com/dds/img/oddjob/logo/731910/s27576315923037596851427339356.jpg
         * userId : 27576167168493386078838751183
         */

        public double xCoordinate;
        public double yCoordinate;
        public String userLogo;
        public String userId;
        public String distance; //距离
    }
}
