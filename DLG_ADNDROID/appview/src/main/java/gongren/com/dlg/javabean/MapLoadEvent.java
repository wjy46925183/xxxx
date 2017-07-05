package gongren.com.dlg.javabean;

/**
 * 地图加载进度
 */
public class MapLoadEvent {
    private int loadStatus;
    public static final int START_LOAD = 1;        //开始加载地图
    public static final int LOAD_SUCCESS = 2;      //加载成功
    public MapLoadEvent(int loadStatus) {
        this.loadStatus = loadStatus;
    }

    public int getLoadStatus() {
        return loadStatus;
    }
}
