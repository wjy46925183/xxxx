package gongren.com.dlg.javabean;

/**
 * Created by Administrator on 2017/3/3.
 */
public class MainToWorkerFragmentEvent {
    private String msg;
    private int tag;

    public MainToWorkerFragmentEvent(String msg, int tag) {
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
