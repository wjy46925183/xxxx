package gongren.com.dlg.javabean;

/**
 * 刷新
 */
public class RefreshEvent<T> {

    public final static int USER_INFO_PAGE = 100; //用户信息刷新
    public final static int ORDER_LIST_DATA = 200; //零工列表数据刷新
    public final static int MINE_PAGE_DATA = 300; //首页数据刷新
    public final static int MINE_PAGE_MAP_ORDER_INFO = 400; //地图订单详情
    public final static int EDIT_RELEASE_ADESS = 500; //发布零工地址编辑


    private String msg;
    private T t;
    private int tag;

    public RefreshEvent(String msg, int tag) {
        this.msg = msg;
        this.tag = tag;
    }

    public RefreshEvent(T msg, int tag) {
        this.t = msg;
        this.tag = tag;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
