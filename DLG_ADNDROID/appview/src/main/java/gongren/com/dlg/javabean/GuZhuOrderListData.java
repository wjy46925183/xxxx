package gongren.com.dlg.javabean;

/**
 * 雇员，我的零工列表的数据结构
 * Created by Administrator on 2017/3/31.
 */
public class GuZhuOrderListData {

    private int status;//当前状态.
    private String startTime;//开工时间。
    private String remainTime;//剩余的时间。
    private int fen;//评分的分数

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(String remainTime) {
        this.remainTime = remainTime;
    }

    public int getFen() {
        return fen;
    }

    public void setFen(int fen) {
        this.fen = fen;
    }
}
