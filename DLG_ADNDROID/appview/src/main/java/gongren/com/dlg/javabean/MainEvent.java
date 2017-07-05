package gongren.com.dlg.javabean;

/**
 * Created by Administrator on 2017/3/3.
 */
public class MainEvent {
    private String msg;
    private int position;
    private int tag;

    public MainEvent(String msg, int tag) {
        this.msg = msg;
        this.tag = tag;
    }

    public MainEvent(String msg, int position, int tag) {
        this.msg = msg;
        this.tag = tag;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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
