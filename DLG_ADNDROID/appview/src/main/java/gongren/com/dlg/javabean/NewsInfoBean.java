package gongren.com.dlg.javabean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/5/19.
 */
public class NewsInfoBean implements Serializable {

    /**
     * code : 0
     * msg : 成功
     * total : 1
     * totalpage : 1
     * data : [{"id":"27581288556497314154440858373","messageId":"27581288556497314154440858372","createTime":1495184648000,"userId":"27579322318046497824857254115","isRead":0,"messageType":1,"jobTaskId":null,"title":null,"content":null,"summary":null,"imageUrl":null,"messageUrl":null}]
     */

    private int code;
    private String msg;
    private int total;
    private int totalpage;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(int totalpage) {
        this.totalpage = totalpage;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 27581288556497314154440858373
         * messageId : 27581288556497314154440858372
         * createTime : 1495184648000
         * userId : 27579322318046497824857254115
         * isRead : 0
         * messageType : 1
         * jobTaskId : null
         * title : null
         * content : null
         * summary : null
         * imageUrl : null
         * messageUrl : null
         */

        private String id;
        private String messageId;
        private long createTime;
        private String userId;
        private int isRead;
        private int messageType;
        private Object jobTaskId;
        private Object title;
        private Object content;
        private Object summary;
        private Object imageUrl;
        private Object messageUrl;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getIsRead() {
            return isRead;
        }

        public void setIsRead(int isRead) {
            this.isRead = isRead;
        }

        public int getMessageType() {
            return messageType;
        }

        public void setMessageType(int messageType) {
            this.messageType = messageType;
        }

        public Object getJobTaskId() {
            return jobTaskId;
        }

        public void setJobTaskId(Object jobTaskId) {
            this.jobTaskId = jobTaskId;
        }

        public Object getTitle() {
            return title;
        }

        public void setTitle(Object title) {
            this.title = title;
        }

        public Object getContent() {
            return content;
        }

        public void setContent(Object content) {
            this.content = content;
        }

        public Object getSummary() {
            return summary;
        }

        public void setSummary(Object summary) {
            this.summary = summary;
        }

        public Object getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(Object imageUrl) {
            this.imageUrl = imageUrl;
        }

        public Object getMessageUrl() {
            return messageUrl;
        }

        public void setMessageUrl(Object messageUrl) {
            this.messageUrl = messageUrl;
        }
    }
}
