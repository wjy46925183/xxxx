package gongren.com.dlg.javabean.master;

import java.util.List;

/**
 * Created by lenovo on 2017/4/28.
 */

public class MapBean {

    /**
     * code : 0
     * total : 0
     * totalpage : 0
     * data : [{"xCoordinate":121.383944,"yCoordinate":31.232312,"userLogo":"http://s.gongren.com/dds/img/oddjob/logo/356106/s27547812733517919327273965020.jpg","userId":"27544528069374666408514337052"},{"xCoordinate":121.383941,"yCoordinate":31.232312,"userLogo":"http://s.chengxinhutong.com/dds/img/gongren/dlg/logo/559561/s839450436430860288.png","userId":"27545881857475491966967547677"},{"xCoordinate":121.383941,"yCoordinate":31.232312,"userLogo":"http://s.chengxinhutong.com/dds/img/gongren/dlg/logo/559561/s839450436430860288.png","userId":"27545881857475491966967547677"},{"xCoordinate":121.383935,"yCoordinate":31.232323,"userLogo":"http://s.chengxinhutong.com/dds/img/gongren/dlg/logo/559561/s839450436430860288.png","userId":"27545775198401257778340103862"},{"xCoordinate":121.383935,"yCoordinate":31.232323,"userLogo":"http://s.chengxinhutong.com/dds/img/gongren/dlg/logo/559561/s839450436430860288.png","userId":"27545775198401257778340103862"},{"xCoordinate":121.383935,"yCoordinate":31.232323,"userLogo":"http://s.chengxinhutong.com/dds/img/gongren/dlg/logo/559561/s839450436430860288.png","userId":"27545775198401257778340103862"},{"xCoordinate":121.38391,"yCoordinate":31.232351,"userLogo":"http://s.chengxinhutong.com/dds/img/gongren/dlg/logo/559561/s839450436430860288.png","userId":"27544513219745687072325286169"}]
     */

    public int code;
    public int total;
    public int totalpage;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * xCoordinate : 121.383944
         * yCoordinate : 31.232312
         * userLogo : http://s.gongren.com/dds/img/oddjob/logo/356106/s27547812733517919327273965020.jpg
         * userId : 27544528069374666408514337052
         */

        public double xCoordinate;
        public double yCoordinate;
        public String userLogo;
        public String userId;
    }
}
