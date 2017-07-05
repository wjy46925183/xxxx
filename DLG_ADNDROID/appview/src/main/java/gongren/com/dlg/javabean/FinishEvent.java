package gongren.com.dlg.javabean;

/**
 * Created by Administrator on 2017/3/3.
 */
public class FinishEvent {
    private String msg;
    private int tag;

    public FinishEvent() {
        super();
    }

    public FinishEvent(String msg, int tag) {
        this.msg = msg;
        this.tag = tag;
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
